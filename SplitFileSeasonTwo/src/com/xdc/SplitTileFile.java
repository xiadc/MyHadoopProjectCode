package com.xdc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class SplitTileFile {

	private int ROW;// grd数据的像素行
	private int COL;// grd数据的像素列
	private short TILE_SIZE;

	private double x1, x2, y1, y2;
	private int ROW_NUM = 0;// 数据块的行数
	private int COL_NUM = 0;// 数据块的列数
	private boolean stateX;// 数据块是否需要填充X的标志
	private boolean stateY;// 数据头偏移量否需要填充Y的标志

	private short COL_ADD;// 数据需要填充的像素列
	private short ROW_ADD;// 数据需要填充的像素行	

	private RandomAccessFile raf = null;
	private FileOutputStream fos = null;

	public static void main(String[] args){
		if(args.length != 5){
			System.out.println("参数个数不匹配，应为：gzTilePath headMsgPath grdPath,z1l,z2");
			return;
		}
		 String gzTilePath = args[0];
		 String headMsgPath = args[1];
		 String grdPath = args[2];
		 double z1 = Double.parseDouble(args[3]);
		 double z2 = Double.parseDouble(args[4]);
		 SplitTileFile s = new SplitTileFile();
		 try {
			s.jieYa(gzTilePath);
			System.out.println("开始生成ras文件......");
			//s.doSplit(grdPath, gzTilePath, headMsgPath,z1,z2);
			s.doSplit(grdPath, gzTilePath+"/result", headMsgPath,z1,z2);
			System.out.println("ras文件生成成功！ras文件目录："+grdPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("生成ras文件失败");
			e.printStackTrace();
		}
	}
	
	public void doSplit(String grdPath, String tilePath, String headMsgPath,double z1,double z2) {
		try {	
			raf = new RandomAccessFile(tilePath, "r");
			this.init(headMsgPath);
			this.writeGrdHead(grdPath,z1,z2);//写grd文件头
			this.writeGrdFile(grdPath);//写grd文件内容
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				raf.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * 初始化瓦片文件数据，得到头文件相关信息
	 * 
	 * @param path
	 */
	private void init(String headMsgPath) {
		TileHeadFile tileHeadFile = new TileHeadFile(headMsgPath);
		this.TILE_SIZE = tileHeadFile.getTILE_SIZE();
		this.ROW = tileHeadFile.getROW();
		this.COL = tileHeadFile.getCOL();
		this.x1 = tileHeadFile.getX1();
		this.x2 = tileHeadFile.getX2();
		this.y1 = tileHeadFile.getY1();
		this.y2 = tileHeadFile.getY2();
		//this.z1 = tileHeadFile.getZ1();
	//	this.z2 = tileHeadFile.getZ2();
		this.ROW_NUM = tileHeadFile.getROW_NUM();
		this.COL_NUM = tileHeadFile.getCOL_NUM();
		this.ROW_ADD = tileHeadFile.getROW_ADD();
		this.COL_ADD = tileHeadFile.getCOL_ADD();

		this.stateX = ROW_ADD == 0 ? false : true;
		this.stateY = COL_ADD == 0 ? false : true;
	}

	/**
	 * 瓦片文件还原为grd文件
	 * 
	 * @param GRD_PATH
	 * @throws IOException
	 */
	private void writeGrdFile(String grdPath) throws IOException {
		int byteSize = TILE_SIZE * 4;
		byte[] bytes = new byte[byteSize];
		fos = new FileOutputStream(grdPath, true);// 继续往grd里追加内容而不是覆盖
		long startPX = 0;
		int temp = 0;
		int current_row = 0;
		int current_tile_row = 0;
		while (current_row < ROW) {
			current_tile_row = current_row / TILE_SIZE;
			startPX = (TILE_SIZE * 4L * (current_row % TILE_SIZE))
					+ (4L*current_tile_row * COL_NUM * TILE_SIZE * TILE_SIZE);
			raf.seek(startPX);
			temp = COL_NUM;
			while (temp > 1) {
				raf.read(bytes, 0, byteSize);
				fos.write(bytes, 0, byteSize);
				startPX += 4L*TILE_SIZE * TILE_SIZE ;// 当前实际要读取的位置
				raf.seek(startPX);
				temp--;
			}
			raf.read(bytes, 0, (TILE_SIZE - COL_ADD) * 4);
			fos.write(bytes, 0, (TILE_SIZE - COL_ADD) * 4);
			current_row++;
		}
	}

	/**
	 * 写grd头文件
	 * 
	 * @param GRD_PATH
	 */
	private void writeGrdHead(String grdPath,double z1,double z2) {
		RasHead rasHead = new RasHead(ROW, COL,x1, x2, y1, y2,z1,z2);
		rasHead.writeGrdHead(grdPath);

	}

	private void jieYa(String gzTilePath) throws IOException {
		File dir = new File(gzTilePath);
		
		File newDir = new File(gzTilePath+"/result");
		if(newDir.exists()){//如果存在就先删除
			//newDir.delete();
			System.out.println("tile文件解压拼接完成!");
			return;
		}	
	//	File[] contentFile = dir.listFiles();
		List<File> files = Arrays.asList(dir.listFiles());
		//解压文件以文件名排序
		Collections.sort(files, new Comparator<File>() {
			   @Override
			   public int compare(File o1, File o2) {
				if (o1.isDirectory() && o2.isFile())
			          return -1;
				if (o1.isFile() && o2.isDirectory())
			          return 1;
				return o1.getName().compareTo(o2.getName());
			   }
			});		
		
		String fileName = "";
      FileOutputStream fos = new FileOutputStream(newDir,true); 
		for (int i = 0; i < files.size(); i++) {
			fileName = ((File)files.get(i)).getName();
			if (fileName.endsWith(".gz")) {
				// jieya
				GZIPInputStream GZIPin = new GZIPInputStream(new FileInputStream((File)files.get(i)));
				byte[] buf = new byte[1024];
		      int len;
		      while ((len = GZIPin.read(buf)) > 0) {
		          fos.write(buf, 0, len);
		        }
		      System.out.println(fileName+"解压完成");
		      GZIPin.close();
			}
		}
		System.out.println("tile文件解压拼接完成!");
		fos.close();
	}
	
				 
}