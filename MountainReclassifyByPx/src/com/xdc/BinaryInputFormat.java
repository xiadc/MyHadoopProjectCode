package com.xdc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.RecordReader;

class BinaryInputFormat extends FileInputFormat<LongWritable, BytesWritable> {
  
  private static final double SPLIT_SLOP=1.1;
  
  /*
   * 查询判断当前文件是否可以分块？"true"为可以分块，"false"表示不进行分块
   */
  protected boolean isSplitable(Configuration conf, Path path) {
    return true;
  }
  
  /*
   * MapReduce的客户端调用此方法得到所有的分块，然后将分块发送给MapReduce服务端。
   * 注意，分块中不包含实际的信息，而只是对实际信息的分块信息。具体的说，每个分块中
   * 包含当前分块对应的文件路径，当前分块在该文件中起始位置，当前分块的长度以及对应的
   * 实际数据所在的机器列表。在实现这个函数时，将这些信息填上即可。
   * */
  public List<InputSplit> getSplits(Configuration conf) throws IOException {
    List<InputSplit> splits = new ArrayList<InputSplit>();
    long minSplitSize = conf.getLong("mapred.min.split.size",1);
    long maxSplitSize = conf.getLong("mapred.max.split.size", 1);
    long blockSize = conf.getLong("dfs.block.size",1);
    long splitSize = Math.max(minSplitSize, Math.min(maxSplitSize, blockSize));
    FileSystem fs = FileSystem.get(conf);
    String path = conf.get(INPUT_DIR);
    FileStatus[] files = fs.listStatus(new Path(path));

    for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {
      FileStatus file = files[fileIndex];
      System.out.println("input file: " + file.getPath().toString());
      long length = file.getLen();
      FileSystem fsin = file.getPath().getFileSystem(conf);
        BlockLocation[] blkLocations = fsin.getFileBlockLocations(file, 0, length);
        if ((length != 0) && isSplitable(conf, file.getPath())) {
            long bytesRemaining = length;
            while (((double) bytesRemaining)/splitSize > SPLIT_SLOP) {
              int blkIndex = getBlockIndex(blkLocations, length-bytesRemaining);
              splits.add(new FileSplit(file.getPath(), length-bytesRemaining, splitSize,
                                       blkLocations[blkIndex].getHosts()));
              bytesRemaining -= splitSize;
            }
            
            if (bytesRemaining != 0) {
              splits.add(new FileSplit(file.getPath(), length-bytesRemaining, bytesRemaining,
                         blkLocations[blkLocations.length-1].getHosts()));
            }
          } else if (length != 0) {
            splits.add(new FileSplit(file.getPath(), 0, length, blkLocations[0].getHosts()));
          } else {
            //Create empty hosts array for zero length files
            splits.add(new FileSplit(file.getPath(), 0, length, new String[0]));
          }
    }
    return splits;
  }
  
  /*
   * 类RecordReader是用来创建传给map函数的Key-Value序列，传给此类的参数有两个：一个分块(split)和作业的配置信息(context).
   * 在Mapper的run函数中可以看到MapReduce框架执行Map的逻辑：
   * public void run(Context context) throws IOException, InterruptedException {
   * 		setup(context);
   * 		调用RecordReader方法的nextKeyValue，生成新的键值对。如果当前分块(Split)中已经处理完毕了，则nextKeyValue会返回false.退出run函数
   *		while (context.nextKeyValue()) {	
   *			map(context.getCurrentKey(), context.getCurrentValue(), context);
   *		}
   *		cleanup(context);
   * }
   **/
  public RecordReader<LongWritable, BytesWritable> createRecordReader(InputSplit split, TaskAttemptContext context)
      throws IOException, InterruptedException {
    // TODO Auto-generated method stub
    BinaryRecordReader reader = new BinaryRecordReader();
    //reader.initialize(split,context);
    return reader;
  }
}