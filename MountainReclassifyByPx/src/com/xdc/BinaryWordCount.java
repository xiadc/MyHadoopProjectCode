package com.xdc;

import java.io.IOException;
import java.net.URI;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapred.lib.InputSampler;
import org.apache.hadoop.mapred.lib.TotalOrderPartitioner;
import org.apache.hadoop.mapred.lib.InputSampler.RandomSampler;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;


public class BinaryWordCount {
  public static class TokenizerMapper extends Mapper<LongWritable, BytesWritable, LongWritable, BytesWritable>{

    private final static IntWritable one = new IntWritable(1); 
    private Text intNum = new Text();                             

    public void map(LongWritable key, BytesWritable value, Context context
        ) throws IOException, InterruptedException {
      context.write(key, value);                            
    }
  }

  public static class IntSumReducer 
  extends Reducer<LongWritable,BytesWritable,LongWritable,BytesWritable> { 
	   private static final float INVAILDDATA=1.70141E38f;//填充的无用数据，与地图中无用数据一致，为1.70141E38
    public void reduce(LongWritable key, Iterable<BytesWritable> values, 
        Context context 
        ) throws IOException, InterruptedException { 
            	Iterator<BytesWritable> ite = values.iterator();        
            	int temp;    
            	float currentElevation,currentSlope=0.0f,change_tile;
            	BytesWritable bw = ite.next();
    	         byte[] bytes =bw.getBytes();
    	         int length = bw.getLength();
    	         temp=(0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16)) | (0xff000000 & (bytes[3] << 24)); 
    	         currentElevation = Float.intBitsToFloat(temp);
    	     
               while(ite.hasNext()){
                	 bw = ite.next();
                	 bytes = bw.getBytes();
                	 temp=(0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16)) | (0xff000000 & (bytes[3] << 24)); 
        	         currentSlope = Float.intBitsToFloat(temp);           
                 }
               
                          if(currentElevation==INVAILDDATA||currentSlope == INVAILDDATA){ //如果高程或坡度值是无效值，则结果值也为无效值     	    
    	                       change_tile =INVAILDDATA;
                            }
                            else if(currentElevation>=4500.0f){
  	            	            change_tile = 1.0f;  //山地类型1
  	                        }
  	    					else if(currentElevation>=3500.0f && currentElevation < 4500.0f){
  	    						change_tile = 2.0f;   //山地类型2
  	    					}
  	    					else if(currentElevation>=2500.0f && currentElevation < 3500.0f){
  	    						change_tile= 3.0f;   //山地类型3
  	    					}
  	    					else if(currentElevation>=1500.0f && currentElevation <2500.0f && currentSlope >= 2.0f){
  	    						change_tile = 4.0f;   //山地类型4
  	    					}
  	    					else if(currentElevation>=1000.0f && currentElevation <1500.0f && currentSlope >= 5.0f){
  						      change_tile = 5.0f;   //山地类型5
  					        }
  	    					else if(currentElevation >= 300.0f && currentElevation < 1000.0f){
  						      change_tile= 6.0f;   //山地类型6
  					        }
  	    					else{
  	    						change_tile = 0.0f; //非山地
  	    					}  
               
          int data;
          byte[] result_bytes = new byte[4];
    	    	     data = Float.floatToIntBits(change_tile);
    	    	     result_bytes[0] = (byte) (data & 0xff);  
    	    	     result_bytes[1] = (byte) ((data & 0xff00) >> 8);  
    	    	     result_bytes[2] = (byte) ((data & 0xff0000) >> 16);  
    	    	     result_bytes[3] = (byte) ((data & 0xff000000) >> 24);     	    	     
        	context.write(key, new BytesWritable(result_bytes)); 
    	
    }
  }

  public static void main(String[] args) throws Exception {
	
	    Configuration conf = new Configuration();         	   
	    /**  
	    * JobConf：map/reduce的job配置类，向hadoop框架描述map-reduce执行的工作  
	    * 构造方法：JobConf()、JobConf(Class exampleClass)、JobConf(Configuration conf)等  
	    */    
		 if(args.length !=6){
				System.out.println("需要的6个参数不匹配，应为inputPath1,inputPath2,outputPath,reduceTaskNum,mapCpmpress,reduceCompress");
				return;
		 }	  
	  
		 if("y".equals(args[4])){
			conf.set("mapreduce.map.output.compress","true");
		 }
		 if("y".equals(args[5])){
		   conf.setBoolean("mapreduce.output.fileoutputformat.compress",true);
		   conf.setClass("mapreduce.output.fileoutputformat.compress.codec",GzipCodec.class, CompressionCodec.class);	 
		 }
		 conf.setBoolean("dfs.support.append", true);
		 //Job job = new Job(conf, "Reclassify");//Job(Configuration conf, String jobName) 设置job名称
		 Job job =Job.getInstance(conf, "MountainReclassify");
		 job.setJarByClass(BinaryWordCount.class);  	 
		 job.setMapperClass(TokenizerMapper.class); //为job设置Mapper类   
		// job.setCombinerClass(MyReducer.class); //为job设置Combiner类    
		 job.setReducerClass(IntSumReducer.class); //为job设置Reduce类     
		 job.setOutputKeyClass(LongWritable.class);        //设置输出key的类型  
		 job.setOutputValueClass(BytesWritable.class);//  设置输出value的类型 
		 job.setInputFormatClass(BinaryInputFormat.class);
		 job.setOutputFormatClass(BinaryOutputFormat.class);
		 String s1 = args[0];
		 String s2 = args[1];
		 //MyReducer.getClassifyMsgList(args[2]);
		 String s3 = args[2];
		 String inputPath1="hdfs://master:9000"+s1;
		 String inputPath2="hdfs://master:9000"+s2;	 
	    String outputPath="hdfs://master:9000"+s3;
		 FileInputFormat.addInputPath(job, new Path(inputPath1)); 
		 FileInputFormat.addInputPath(job, new Path(inputPath2)); 
		 FileOutputFormat.setOutputPath(job, new Path(outputPath));//为map-reduce任务设置OutputFormat实现类  设置输出路径
		 int tasksNum = Integer.parseInt(args[3]);
		 job.setNumReduceTasks(tasksNum);
		 
		 long startMili=System.currentTimeMillis();// 当前时间对应的毫秒数
		 if(tasksNum>1){
		 job.setPartitionerClass(TotalOrderPartitioner.class);
		 // RandomSampler第一个参数表示key会被选中的概率，第二个参数是一个选取samples数，第三个参数是最大读取input splits数
		 RandomSampler<LongWritable, BytesWritable> sampler = new InputSampler.RandomSampler<LongWritable, BytesWritable>(0.1, 5000);
	    InputSampler.writePartitionFile(job, sampler); 
	    String partitionFile = TotalOrderPartitioner.getPartitionFile(conf);
	    URI partitionUri= new URI(partitionFile);//？？
	    job.addCacheArchive(partitionUri);//添加一个档案进行本地化
		 }
		
		
		 boolean state= job.waitForCompletion(true);
		 long endMili=System.currentTimeMillis();
		 System.out.println("总耗时为："+(endMili-startMili)+"毫秒");
		 
		/* for(int i = 0;i<MyMapper.sumList.length;i++){
			      System.out.println(MyMapper.sumList[i]);
		 }*/
		 
		 System.exit(state? 0 : 1);  
	  }
}
