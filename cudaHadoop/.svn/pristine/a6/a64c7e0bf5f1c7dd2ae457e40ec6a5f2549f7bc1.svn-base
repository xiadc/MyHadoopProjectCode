package com.xdc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.WritableComparable;

//自定义序列化类，用于存储瓦片中的最小值与最大值
public class NumMix  implements WritableComparable<NumMix>{
   
	 private IntWritable min_x;//最小值对应横坐标
	 private IntWritable min_y;//最小值对应纵坐标
	 private FloatWritable min;//最小值
	 
	 private IntWritable max_x;
	 private IntWritable max_y;
	 private FloatWritable max;
	 
	 //需要无参默认构造函数,反射过程中需要调用无参构造,不然运行报错.
	 public NumMix(){
		 min_x = new IntWritable(0);
		 min_y = new IntWritable(0);
		 min = new FloatWritable(0.0f);
		 
		 max_x = new IntWritable(0);
		 max_y = new IntWritable(0);
		 max = new FloatWritable(0.0f);
	 }
	 
	 public NumMix(IntWritable min_x, IntWritable min_y,FloatWritable min , 
			                  IntWritable max_x,IntWritable max_y,FloatWritable max){
		      this.min_x = min_x;
		      this.min_y = min_y;
		      this.min = min;
		      this.max_x = max_x;
		      this.max_y = max_y;
		      this.max = max;
	 }
	 
	public FloatWritable getMin() {
		return min;
	}
	
	public FloatWritable getMax() {
		return max;
	}
	
	public IntWritable getMin_x() {
		return min_x;
	}

	public IntWritable getMin_y() {
		return min_y;
	}

	public IntWritable getMax_x() {
		return max_x;
	}

	public IntWritable getMax_y() {
		return max_y;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		  min_x.readFields(in);
		  min_y.readFields(in);
	      min.readFields(in);
	      max_x.readFields(in);
	      max_y.readFields(in);
	      max.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		 min_x.write(out);
		 min_y.write(out);
		 min.write(out);
		 max_x.write(out);
		 max_y.write(out);
		 max.write(out);
	}

	@Override
	public int compareTo(NumMix arg0) {
		// TODO Auto-generated method stub
		return 0;
	}	 
}
