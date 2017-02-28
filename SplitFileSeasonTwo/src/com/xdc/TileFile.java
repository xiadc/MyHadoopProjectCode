package com.xdc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class TileFile {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*GrdSplit g = new GrdSplit();
		int[] pos = new int[]{19};
		ArrayList<byte[]> arr = new ArrayList<byte[]>();
		arr = g.getSplitArr(pos);
		System.out.println("得到arr数组");	*/
		if(args.length != 2){
			System.out.println("参数个数不匹配，应为：localPath hdfsPath");
			return;
		}
		String localPath = args[0];
		String hdfsPath = args[1];
		TileFile tilefile = new TileFile();
		tilefile.fileUpdateToHDFS(localPath,hdfsPath);
	}

	/**
	 * 得到多个瓦片数组
	 * @param pos
	 * @return
	 */
	public ArrayList<byte[]> getSplitArr(int[] pos,short TILE_SIZE,String hdfsPath){
		int byteSize = TILE_SIZE*TILE_SIZE*4;
		ArrayList<byte[]> arr = new ArrayList<byte[]>();
		byte[] bytes =new byte[byteSize];
		for(int i=0;i<pos.length;i++){
			try {
				bytes=this.getSplit(pos[i],TILE_SIZE,hdfsPath);
				arr.add(bytes);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("未知错误");
			}
		}
		    return arr;
	}
	
	/**
	 * 得到单个瓦片
	 * @param pos
	 * @return  bytes数组，单个瓦片字节流
	 * @throws IOException
	 */
	public byte[] getSplit(int pos,short TILE_SIZE,String hdfsPath) throws IOException{
		int byteSize = TILE_SIZE*TILE_SIZE*4;
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create("hdfs://singlehost:9000"),conf);//得到hdfs文件系统对象
		FSDataInputStream in = fs.open(new Path("hdfs://singlehost:9000"+hdfsPath));
		int startPX=pos*TILE_SIZE*TILE_SIZE;
		in.seek(startPX*4);
		byte[] bytes =new byte[byteSize];
		in.read(bytes);
		return bytes;
	}
	/**
	 * 将本地文件拷贝到HDFS
	 * @throws IOException
	 */
	public void fileUpdateToHDFS(String localPath,String hdfsPath){
		Configuration conf = new Configuration();
		try {
			FileSystem fs = FileSystem.get(URI.create("hdfs://singlehost:9000"),conf);//得到hdfs文件系统对象
			InputStream in = null;
			OutputStream out = null;
         String fileDir = localPath;
			File f= new File(fileDir);
			if(f.isDirectory()){
				File[] fileArr=f.listFiles();
				for(int i=0;i<fileArr.length;i++){
					 in  = new FileInputStream(fileArr[i]);
					 out = fs.create(new Path("hdfs://singlehost:9000"+hdfsPath+fileArr[i].getName()));
					 IOUtils.copyBytes(in,out,4096,true);//true代表用完之后关闭流
			         }	
   }else{
			    //System.out.println("需输入一个文件夹");
			     in = new FileInputStream(fileDir);
			     out = fs.create(new Path("hdfs://singlehost:9000"+hdfsPath+f.getName()));
			     IOUtils.copyBytes(in, out,4096,true);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	  
	}
	
	public void fileDownloadFromHDFS(String hdfsPath,String localPath){
		  Configuration conf = new Configuration();
		  InputStream in = null;
		  OutputStream out = null;
		  try {
			  FileSystem fs = FileSystem.get(URI.create("hdfs://singlehost:9000"),conf);
			  FileStatus[] fileStatus = fs.listStatus(new Path("hdfs://singlehost:9000"+hdfsPath));
			   for(int i = 0;i<fileStatus.length;i++){
				   // in = fileStatus[i].getPath()
			    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//得到hdfs文件系统对象
	}
	
}
