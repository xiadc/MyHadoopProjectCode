package com.xdc;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.FloatWritable;

public class FloatArrayWritable extends ArrayWritable{
	    public FloatArrayWritable() { 
	        super(FloatWritable.class);     
	 }
	    
	    public FloatArrayWritable(Float[] f) {
	    	 super(FloatWritable.class);
	    	 FloatWritable[] fw = new FloatWritable[f.length];
	    	 for (int i = 0; i < f.length; i++) {
	    	     fw[i] = new FloatWritable(f[i]);
	    	 }
	    	 set(fw);
	   }
	    	
}
