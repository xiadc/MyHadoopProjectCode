package com.xdc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Main {
	
	private static final short START=64;//数据头偏移量
	//private static final short TILE_SIZE = 256;
	//private static final String GRD_PATH = "/result/result.Grd";//grd文件路径
	//private static final String DES_PATH = "/test";//生成新文件路径

	public static void main(String[] args){

		Params p = new Params("/params");
		String[] params=p.getParams();
		String tile_size = params[0];
		short TILE_SIZE =Short.parseShort(tile_size);
		String GRD_PATH = params[1];
		String DES_PATH = params[2];
	   SplitRasFile splitRasFile = new SplitRasFile();
		 // String[] s= GRD_PATH.split("\\.");
		  /* if(s.length != 2 || !("Grd".equals(s[1]))){	   
		    	  System.out.println("输入必须是GRD文件");
				   return;
		    }*/
		  File f = new File(DES_PATH);
		  if(!f.exists()){
			   f.mkdir();//如果文件不存在，则创建一个文件夹
		  }
		  if(!f.isDirectory()){//如果文件不是一个文件夹
			   System.out.println("目标路径必须是一个文件夹");
			   return;
		  }
		  splitRasFile.doSplit(START, TILE_SIZE, GRD_PATH, DES_PATH);
		
	//	SplitTileFile splitTileFile = new SplitTileFile();
	//	splitTileFile.doSplit(GRD_PATH, DES_PATH+"/tile", DES_PATH+"/headMsg");
	//	TileFile tilefile = new TileFile();
	//	tilefile.fileUpdateToHDFS("/Hubei1");	
//		TileFile t = new TileFile();
	//	t.fileUpdateToHDFS("/Hubei3");
		
	//	ZIPTile ziptile = new ZIPTile();
	//	ziptile.doZIP("/tmp/result");
	}

}
