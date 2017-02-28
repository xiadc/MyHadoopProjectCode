package com.xdc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class ZIPTile {
	
	private static final short START=60;//数据头偏移量
	private short TILE_SIZE;
	private  int COL_NUM=0;//数据块的列数
	private  int ROW_NUM=0;//数据块的行数
	
	public static void main(String[] args){
		if(args.length !=1){
			System.out.println("参数个数不匹配，应为：paramsPath");
			return;
		}
		
		Params p = new Params(args[0]);
		String[] params=p.getParams();
		String tile_size = params[0];
		short TILE_SIZE =Short.parseShort(tile_size);
		String GRD_PATH = params[1];
		String DES_PATH = params[2];
	   SplitRasFile splitRasFile = new SplitRasFile();
		File f = new File(DES_PATH);
		if(!f.exists()){
			 f.mkdir();//如果文件不存在，则创建一个文件夹
		}
		if(!f.isDirectory()){//如果文件不是一个文件夹
			 System.out.println("目标路径必须是一个文件夹");
			 return;
		}
		System.out.println(GRD_PATH+"文件开始转换为tile文件");
		splitRasFile.doSplit(START, TILE_SIZE, GRD_PATH, DES_PATH);// 生成tile文件，未压缩		  
           
		ZIPTile zipTile = new ZIPTile();
		zipTile.doZIP(DES_PATH);//压缩tile文件，生成tileZip文件
		  
		File tileFile = new File(DES_PATH+"/tile");
		/*if(tileFile.exists()){//删除未压缩的tile文件
			  tileFile.delete();
		}*/
		System.out.println("生成结果文件目录："+ DES_PATH); 
		  
	}
	
	public void doZIP(String path){
		System.out.println("tile文件开始压缩......"); 
		  this.init(path);
		  this.ZIP(path);
	}
	
	
	/**
	 * 初始化瓦片文件数据，得到头文件相关信息
	 * @param path
	 */
	private void init(String path){
		 TileHeadFile tileHeadFile = new TileHeadFile(path+"/headMsg");
		 this.TILE_SIZE = tileHeadFile.getTILE_SIZE();
		 this.COL_NUM = tileHeadFile.getCOL_NUM();
		 this.ROW_NUM = tileHeadFile.getROW_NUM();
		// System.out.println(COL_NUM+" "+ROW_NUM);
		 
	}
       
	private void ZIP(String path){
		 int size = TILE_SIZE*TILE_SIZE*4;
		 //申明输入输出流
		 FileInputStream fis = null;
		 FileOutputStream fos = null;
		 ByteArrayOutputStream byout = null;
		 ZipOutputStream zipout = null;
		 
	   try {
			  fis = new FileInputStream(path+"/tile");
			  fos = new FileOutputStream(path+"/tileZip");
			  byout=new ByteArrayOutputStream();
			  zipout = new ZipOutputStream(byout);

			// zipout.putNextEntry(new ZipEntry("Zip")); //压缩之前必须有，否则报错
			 
			 byte bytes[] = new byte[size]; //  存储原始tile字节
			 byte zipBytes[] = null; //存储压缩后的字节
			 int count;   //读出的字节数
			 int zip_count;  //压缩后的字节数
			 int current_tile_no=1; //当前瓦片编号
			 int total=0;
			 int useness_bytes_num=0;
			 
			 while((count = fis.read(bytes,0,size))!=-1){  //对当前瓦片字节数组循环，读出字节数组到bytes,每次读256kb					 
				 
				   ZipEntry entry = new ZipEntry("zip"+current_tile_no);
				   entry.setSize(bytes.length);
				   zipout.putNextEntry(entry);     
			      zipout.write(bytes, 0, count);//将字节数组写入压缩流中
			      zipout.closeEntry();// 关闭zipEntry
			      
			      zipBytes = byout.toByteArray(); //得到压缩后的数组
			      zip_count = byout.size();  //得到压缩后的字节数
			      byte mixBytes[] = new byte[zip_count+8]; 
			      for(int i = zip_count-1;i>=0;i--){ //将zipbytes字节后移8位，留出前面8位存储瓦片编号和字节长度信息
			    	   mixBytes[i+8] = zipBytes[i];  
			        }
			      byout.reset();
			      
			     
			        //前四个字节存储瓦片编号int
			      mixBytes[0] = (byte) (current_tile_no & 0xff);
			      mixBytes[1] = (byte) ((current_tile_no & 0xff00) >> 8);
			      mixBytes[2] = (byte) ((current_tile_no & 0xff0000) >> 16);
			      mixBytes[3] = (byte) ((current_tile_no & 0xff000000) >> 24);
			        //存储压缩字节长度
			      mixBytes[4] = (byte) (zip_count & 0xff);
			      mixBytes[5] = (byte) ((zip_count & 0xff00) >> 8);
			      mixBytes[6] = (byte) ((zip_count & 0xff0000) >> 16);
			      mixBytes[7] = (byte) ((zip_count & 0xff000000) >> 24);
			        
			      current_tile_no++;
			   //   System.out.println(current_tile_no+" "+zip_count);
			        
			      total += (zip_count + 8);
			      if(total > 128*1024*1024){//如果大于128MB
			    	   total =  total-zip_count - 8;
			    	   useness_bytes_num = 128*1024*1024 - total;
			    	   byte useness_bytes[] = new byte[useness_bytes_num];
			    	   fos.write(useness_bytes);
			    	   total = zip_count + 8;
			        }
			      
			        //写到磁盘
			      fos.write(mixBytes,0,zip_count+8);
			 }
		}catch (IOException e) {
			e.printStackTrace();
			System.out.println("tile文件压缩失败！"); 
		}finally{
			try {		
				   zipout.close();    				
					byout.close();
					fos.close();
					fis.close();
		   System.out.println("tile文件压缩成功！"); 		
			} catch (IOException e) {			
				e.printStackTrace();
			}
			
		}
		 	 
	}

}
