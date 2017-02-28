package com.xdc;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MountainReclassify {

	private static final int RAS_HEAD = 60; 
	private static final float INVAILDDATA=1.70141E38f;//填充的无用数据，与地图中无用数据一致，为1.70141E38
	
	public static void main(String[] args) {

		if(args.length!=3){
			System.out.println("参数错误！");
		}
		
		String inputElevationPath =args[0];
		String inputSlopePath = args[1];
		String outputPath = args[2];
		long start = System.currentTimeMillis();
		readRasFile(inputElevationPath, inputSlopePath, outputPath);
		long end = System.currentTimeMillis();
		long time = (end-start)/1000;
		System.out.println("用时:"+time+"秒");
	}
	

	/**
	 * 主逻辑，山地分类
	 * @param inputElevationPath
	 * @param inputSlopePath
	 * @param outputPath
	 */
	public static void readRasFile(String inputElevationPath, String inputSlopePath,String outputPath)  {
		    MyDataInputStream myElevationDis = null;
		    MyDataInputStream mySlopeDis = null;
		    MyDataOutputStream myDos = null;
			try{
				myElevationDis = new MyDataInputStream(new FileInputStream(inputElevationPath));
				mySlopeDis = new MyDataInputStream(new FileInputStream(inputSlopePath));
				myDos = new MyDataOutputStream(new DataOutputStream(new FileOutputStream(outputPath)));
				writeHead(myElevationDis,myDos);
				mySlopeDis.skip(RAS_HEAD);
			
		     float currentElevation,currentSlope,result; 
		     byte[] bufferElevation = new byte[256*256*4];
		     byte[] bufferSlope = new byte[256*256*4];
		     int lengthElevation =0;
		     int lengthSlope = 0;
			    	
			    		 while((lengthElevation =  myElevationDis.read(bufferElevation))!=-1& (lengthSlope = mySlopeDis.read(bufferSlope))!=-1)
			    		 {
			    			 if(lengthElevation != lengthSlope) 
			    				 {
			    				 
			    				    System.out.println(lengthElevation +" " + lengthSlope+"失败！");
			    				    break;
			    				 }
			    			 for (int i = 0; i < lengthElevation; i=i+4) {
			    				 currentElevation =  fromBytestoFloat(bufferElevation, i);
			    				 currentSlope = fromBytestoFloat(bufferSlope, i);
			    				 
			    				  if(currentElevation==INVAILDDATA||currentSlope == INVAILDDATA){ //如果高程或坡度值是无效值，则结果值也为无效值     	    
			   	        	            result =INVAILDDATA;
			   	        	            myDos.writeFloat(result);
			   	        	            continue;
			   	                     }
			    					if(currentElevation>=4500.0f){
			            	          result = 1.0f;  //山地类型1
			                        }
			    					else if(currentElevation>=3500.0f && currentElevation < 4500.0f){
			    						result = 2.0f;   //山地类型2
			    					}
			    					else if(currentElevation>=2500.0f && currentElevation < 3500.0f){
			    						result = 3.0f;   //山地类型3
			    					}
			    					else if(currentElevation>=1500.0f && currentElevation <2500.0f && currentSlope >= 2.0f){
			    						result = 4.0f;   //山地类型4
			    					}
			    					else if(currentElevation>=1500.0f && currentElevation <1000.0f && currentSlope >= 5.0f){
								      result = 5.0f;   //山地类型5
							        }
			    					else if(currentElevation >= 300.0f && currentElevation < 1000.0f){
								      result = 6.0f;   //山地类型6
							        }
			    					else{
			    						result = 0.0f; //非山地
			    					}				
			    					myDos.writeFloat(result);		    				 
							    }
			    		   }	
		            	} catch (IOException e) {
				            e.printStackTrace();
				            close(myElevationDis, mySlopeDis, myDos);
			             }
				} 
		
	
	/**
	 * 写头文件
	 * @param in
	 * @param myDos
	 * @throws IOException
	 */
	private static void writeHead(InputStream in, MyDataOutputStream myDos) throws IOException{
		   byte[] headBytes = new byte[RAS_HEAD];
		   in.read(headBytes,0,headBytes.length);
		   myDos.write(headBytes, 0, headBytes.length);
	}
	
	private static void close(MyDataInputStream myElevationDis,MyDataInputStream mySlopeDis,MyDataOutputStream myDos){
		try {
			if (myDos != null)
				myDos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (myElevationDis != null)
					myElevationDis.close();
			} catch (Exception e2) {
				// TODO: handle exception
			} finally {
				try {
					if (mySlopeDis != null)
						mySlopeDis.close();
				} catch (Exception e3) {
					// TODO: handle exception
				}
			}
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

}
