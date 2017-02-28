package com.xdc;

import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.compress.Decompressor;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.RecordReader;

/**
 * Return a single record (filename, "") where the filename is taken from
 * the file split.
 */
public class TileRecordReader extends RecordReader<LongWritable, BytesWritable> {
  private FSDataInputStream inputStream = null;
  private InputStream in = null;
  private long start,end,pos;
  private Configuration conf = null;
  private FileSplit fileSplit = null;
  private LongWritable key = new LongWritable();
  private BytesWritable value = new BytesWritable();
  private boolean processed = false;
  private short TILE_SIZE;
  
  //private boolean isCompressedInput;
  private Decompressor decompressor;
 // private CompressionInputStream cis; 
  
  public TileRecordReader() throws IOException {
  }

  /*关闭文件流
   * */
  public void close() {
    try {
      if(inputStream != null)
        inputStream.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /*
   * 获取处理进度
   **/
  public float getProgress() {
    return ((processed == true)? 1.0f : 0.0f);
  }

  /*
   * 获取当前的Key
   * */
  public LongWritable getCurrentKey() throws IOException,
  InterruptedException {
    // TODO Auto-generated method stub
    return key;
  }

  /* 获取当前的Value
   * */
  public BytesWritable getCurrentValue() throws IOException,InterruptedException {
    // TODO Auto-generated method stub
    return value;
  }

  /*
   * 进行初始化工作，打开文件流，根据分块信息设置起始位置和长度等等
   * */
  public void initialize(InputSplit inputSplit, TaskAttemptContext context)
      throws IOException, InterruptedException {
    // TODO Auto-generated method stub
    fileSplit = (FileSplit)inputSplit;
    conf = context.getConfiguration();
   
    //long tempStart=fileSplit.getStart();
    //this.start =( tempStart==0?tempStart+60:tempStart );    
    this.start = fileSplit.getStart();
    this.end = fileSplit.getStart() + fileSplit.getLength();
    	 
    //Configuration job = context.getConfiguration();
    Path path = fileSplit.getPath();
    FileSystem fs = path.getFileSystem(conf);
	 this.inputStream = fs.open(path);
	 inputStream.seek(start);
	 this.pos = this.start;   
	/* try{  
	      FSDataInputStream fsis = fs.open(new Path("hdfs://master:9000/ShanXiTile/headMsg"));
	      HeadMsg headMsg = new HeadMsg(fsis);
	      TILE_SIZE = headMsg.getTILE_SIZE();
	      fsis.close();    
    }	catch(IOException e)	{
      e.printStackTrace();
    }*/
	 //tile_size为1相当于以单个像素计算
	//TILE_SIZE = 1;
  }

  /*生成下一个键值对
   **/
  public boolean nextKeyValue() throws IOException, InterruptedException {
    // TODO Auto-generated method stub
	 
    if(this.pos < this.end) 
    {
    /*
	      key.set(this.pos/4);
	      byte[] bytes=new byte[4];
	      inputStream.read(bytes);
	      int temp=(0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16)) | (0xff000000 & (bytes[3] << 24));
	      float t=Float.intBitsToFloat(temp);  
	      FloatWritable[] fw =new FloatWritable[1] ;
	      fw[0]= new FloatWritable(t);
	      value.set(fw);
	      this.pos = inputStream.getPos();
	      return true;*/
    	 //  FloatWritable[] fw =new FloatWritable[TILE_SIZE*TILE_SIZE];
	     // FloatWritable f;
    	
    	
   	  /* int size= 4*TILE_SIZE*TILE_SIZE;
	      key.set((this.pos-60)/size);
	      byte[] bytes = new byte[size];
	      byte[] bytes_temp = new byte[1024];
	      int i = 0;
	      if(size<1024){
	    	    inputStream.read(bytes);  
	        }
	        while(i<size/1024){
	        	if(i==0){
	        	   inputStream.read(bytes,0,1024);
	        	}else{//需保证size是1024的整数倍，一般情况下tile_size=128,256,512时满足
	        	   inputStream.read(bytes_temp);
	        	   for(int j =0;j<1024;j++){
	        		   bytes[j+i*1024] = bytes_temp[j];
	        	    }
	        	 }
	         	i++;
	        }
	      BytesWritable bw = new BytesWritable(bytes);
         value.set(bw);
         this.pos = inputStream.getPos();*/
    	//******************************************************************************
    	   int byteLength = 0;
    	   myDataInputStream mydis = new myDataInputStream(inputStream);
    	   
    	   if(end-pos>=4){
    	       key.set(mydis.readInt());
    	       if(key.get()==0){
    	    	   //System.out.println("分片读取结束！！！！！！！！！！！！！");
    	    	   processed = true;    	    	  
       		   return false;
       	         }
    	    }else{// 如果当前位置距离文件末尾不足4个字节，则必定后面为填充无用数据
    	      	processed = true;    	    	  
        		   return false;
    	    }
    	   
    	   byteLength =mydis.readInt();
    	  // System.out.println(key+" "+ byteLength+" "+mydis.getPos());
    	   byte[] bytes = new byte[byteLength];
    	   int len = mydis.read(bytes, 0, byteLength);
    	   int temp;
    	   while(len < byteLength){
    	      temp = mydis.read(bytes, len, byteLength-len);
    	      len += temp;    	      
    	    }
    	    
    	 //  System.out.println(key+" "+ byteLength+" "+mydis.getPos());
    	   BytesWritable bw = new BytesWritable(bytes);
         value.set(bw);
         this.pos = mydis.getPos();  
 	      return true;
    } 
    else
    {
      processed = true;
      return false;
    }
  }
  
}
