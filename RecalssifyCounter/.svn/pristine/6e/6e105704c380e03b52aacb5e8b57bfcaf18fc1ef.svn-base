package com.xdc;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class ReclassifyCounter {

	private static final int RAS_HEAD = 60; 
	private static Map<Float,Long> map = new TreeMap<Float,Long>();
	
	public static void counter(String path){	
			try(MyDataInputStream myDis= new MyDataInputStream(new FileInputStream(path))){
					   myDis.skip(RAS_HEAD);
				     float currentKey;
				     byte[] bufferElevation = new byte[256*256*4];
				   
				     int lengthElevation =0;
		    		 while((lengthElevation =  myDis.read(bufferElevation))!=-1)
		    		 {   
		    			 for (int i = 0; i < lengthElevation; i=i+4) {
		    				 currentKey =  fromBytestoFloat(bufferElevation, i);
		    				 
		    				if( !map.containsKey(currentKey)){
		    					map.put(currentKey, 0L);
		    				}
		                    Long currentValue =  map.get(currentKey);
		                    map.put(currentKey,++currentValue );		    				
		    			 }
		    		 }
    	
        	} catch (IOException e) {
	            e.printStackTrace();	     
             }
	}
	
	private static void getMap(){
		for(Map.Entry<Float, Long> entry:map.entrySet()){
			System.out.println("key:"+entry.getKey()+" value:"+ entry.getValue());
		}
			
	}
	
	/**
	 * 辅助函数，bytes转int
	 * @param b
	 * @param start
	 * @return
	 */
	public static int fromBytestoInt(byte[] b,int start){
		 int temp;
		 if(b.length >= start+4)
		    temp=(0xff & b[start]) | (0xff00 & (b[start+1] << 8)) | (0xff0000 & (b[start+2] << 16)) | (0xff000000 & (b[start+3] << 24));
		 else
			 temp = Integer.MAX_VALUE;
		 return temp;
	}

	/**
	 * 辅助函数，bytes转Float
	 * @param b
	 * @param start
	 * @return
	 */
	public static Float fromBytestoFloat(byte[] b,int start){
		 float temp;
		 int tempi = fromBytestoInt(b,start);
		 if(tempi ==Integer.MAX_VALUE)
			 temp = Float.MAX_VALUE;
		 else
			 temp = Float.intBitsToFloat(tempi);
		 return temp;
	}
	
	public static void main(String[] args) {

	//	conuter("/xdcTileZip/HubeiResult1.Ras");
		if(args.length!=1){
			System.out.println("参数个数不匹配，应为1个参数");
			System.exit(0);
		}
		counter(args[0]);
		getMap();
	}

}
