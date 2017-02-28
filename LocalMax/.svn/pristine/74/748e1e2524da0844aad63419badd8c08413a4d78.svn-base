package com.xdc;

import java.io.IOException;

import org.apache.hadoop.fs.FSDataInputStream;

public class HeadMsg {
	private  short COL;//grd数据的像素列
	private  short ROW;//grd数据的像素行
	private  short COL_NUM=0;//数据块的列数
	private  short ROW_NUM=0;//数据块的行数
	private  short COL_ADD;//数据需要填充的像素列
	private  short ROW_ADD;//数据需要填充的像素行
   private  short TILE_SIZE;//瓦片尺寸
	private double x1,x2,y1,y2,z1,z2;
	
	public HeadMsg(FSDataInputStream fsis){
		myDataInputStream mydis =null;
		    try {
		    	  mydis = new myDataInputStream(fsis);
				 this.TILE_SIZE = mydis.readShort();
				  this.COL = mydis.readShort();
				  this.ROW = mydis.readShort();
				  this.x1 = mydis.readDouble();
				  this.x2 = mydis.readDouble();
				  this.y1 = mydis.readDouble();
				  this.y2 = mydis.readDouble();
				  this.z1 = mydis.readDouble();
				  this.z2 = mydis.readDouble();
				  this.COL_NUM = mydis.readShort();
				  this.ROW_NUM = mydis.readShort();
				  this.COL_ADD = mydis.readShort();
				  this.ROW_ADD = mydis.readShort();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					mydis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		    
		
	}
	
	
	public short getCOL() {
		return COL;
	}
	public short getROW() {
		return ROW;
	}
	public short getCOL_NUM() {
		return COL_NUM;
	}
	public short getROW_NUM() {
		return ROW_NUM;
	}
	public short getCOL_ADD() {
		return COL_ADD;
	}
	public short getROW_ADD() {
		return ROW_ADD;
	}
	public short getTILE_SIZE() {
		return TILE_SIZE;
	}
	public double getX1() {
		return x1;
	}
	public double getX2() {
		return x2;
	}
	public double getY1() {
		return y1;
	}
	public double getY2() {
		return y2;
	}
	public double getZ1() {
		return z1;
	}
	public double getZ2() {
		return z2;
	}	 
	
	

}
