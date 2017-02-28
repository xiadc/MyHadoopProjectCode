package com.xdc;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;

import com.UtilClass.myDataOutputStream;


public class SplitRasFile {	
	private static final short START=60;//数据头偏移量
	private  int COL;//grd数据的像素列
	private  int ROW;//grd数据的像素行
	private  int COL_NUM=0;//数据块的列数
	private  int ROW_NUM=0;//数据块的行数
	private  boolean stateX;//数据块是否需要填充X的标志
	private  boolean stateY;//数据头偏移量否需要填充Y的标志
	
	private short COL_ADD;//数据需要填充的像素列
	private short ROW_ADD;//数据需要填充的像素行
	
	//private int no_use;
	private double x1,x2,y1,y2,z1,z2;
	
	private RandomAccessFile raf = null;
	
	/*public static void main(String[] args){
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
	   SplitGrdFile splitGrdFile = new SplitGrdFile();
		  String[] s= GRD_PATH.split("\\.");
		   if(s.length != 2 || !("Grd".equals(s[1]))){	   
		    	  System.out.println("输入必须是GRD文件");
				   return;
		    }
		  File f = new File(DES_PATH);
		  if(!f.exists()){
			   f.mkdir();//如果文件不存在，则创建一个文件夹
		  }
		  if(!f.isDirectory()){//如果文件不是一个文件夹
			   System.out.println("目标路径必须是一个文件夹");
			   return;
		  }
		  splitGrdFile.doSplit(START, TILE_SIZE, GRD_PATH, DES_PATH);
	}*/

	public  void doSplit(short START,short TILE_SIZE,String GRD_PATH,String DES_PATH)  {
		// TODO Auto-generated method stub
		
		try {
			 raf = new RandomAccessFile(GRD_PATH,"r");  
			this.init(TILE_SIZE,GRD_PATH);//初始化
         this.split(START,TILE_SIZE,DES_PATH);//分片
         this.writeHeadFile(DES_PATH+"/headMsg");//写新的头文件信息         
		  } catch (Exception e) {
			  System.out.println("分片异常！文件转换失败");
			e.printStackTrace();
		  }finally{
		    try {
			      raf.close();
			      System.out.println("转换为tile文件完成！"); 
		     } catch (IOException e) {
			   e.printStackTrace();
		     }
		}
	}
	
	/**
	 * 初始化，得到文件相关信息
	 * @param raf
	 * @throws IOException
	 */
	private void init(short TILE_SIZE,String GRD_PATH){//读入数据初始化
			RasHead rashead = new RasHead(GRD_PATH);		  
			this.ROW = rashead.getNx();
			this.COL = rashead.getNy();
			//this.no_use = rashead.getNo_use();
			this.x1 = rashead.getX1();
			this.x2 = rashead.getX2();
			this.y1 = rashead.getY1();
			this.y2 = rashead.getY2();
			this.z1 = rashead.getZ1();
			this.z2 = rashead.getZ2();
		   System.out.println("ROW="+ROW);
		   System.out.println("COL="+COL);
		
		if(COL%TILE_SIZE!=0){//数据块列尾需要填充
			COL_NUM=(short) ((COL/TILE_SIZE)+1);
			COL_ADD=(short) (COL_NUM*TILE_SIZE-COL);
			System.out.println("COL_NUM="+COL_NUM);
			System.out.println("COL_ADD="+COL_ADD);
			stateY = true;
		}else{//数据块列尾不需要填充
			COL_NUM=(short) (COL/TILE_SIZE);
			COL_ADD=0;  
			stateY = false;
		}
		if(ROW%TILE_SIZE!=0){//数据块行尾需要填充
			ROW_NUM=(short) ((ROW/TILE_SIZE)+1);
			ROW_ADD=(short) ((ROW_NUM*TILE_SIZE)-ROW);
			System.out.println("ROW_NUM="+ROW_NUM);
			System.out.println("ROW_ADD="+ROW_ADD);
			stateX = true;
		}else{
			ROW_NUM=(short) (ROW/TILE_SIZE);
			ROW_ADD=0;
			stateX = false;
		}
	}
	/**
	 * 处理分块
	 * @param raf
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	private void split(short START,short TILE_SIZE,String DES_PATH) throws IOException{//切分
	   	long startPX = Long.MAX_VALUE;
		   int byteSize = TILE_SIZE*4;
		   byte[] bytes = new byte[byteSize];
		   float NO_USE=1.70141E38f;//填充的无用数据，与地图中无用数据一致，为1.70141E38
		   //int times=1; //当前写入第几个文件
		   myDataOutputStream mydos = null;
		   DataOutputStream dos =null;
			FileOutputStream fos = null;
		   fos = new  FileOutputStream(DES_PATH+"/tile");	
			dos = new DataOutputStream(fos);//输出流
			mydos = new myDataOutputStream(dos);
				for(int x=0;x<ROW_NUM;x++){//x，y代表逻辑上的第x行，第y列的数据块
					for(int y=0;y<COL_NUM;y++){
						/*if((x*COL_NUM+y)%512==0&&(x!=0||y!=0)){//如果数据块达到512，作为一个文件，并新开一个文件
							dos.close();
			            fos.close();
			        	System.out.println("文件"+times+"完成！");
			            times++;TILE_SIZE*
			            fos = new  FileOutputStream("/Hubei/helloworld"+times);	
						   dos = new DataOutputStream(fos);
						}*/
						bytes = null;//byte数组清空
					   bytes = new byte[byteSize];//再new一个新的bytes数组
					   startPX = x*COL*4L*TILE_SIZE+y*TILE_SIZE*4L+START;//当前实际要读取的位置,加L是为了强制转换为long类型					 
						int i = 0;
						int check = 0;		
					 //  byte[] tempBytes =this.floatToByteArr(f);// 将99.99转换成字节，并倒序，与其他有效值一样倒序，C++与java读写文件差异导致
						if((y==COL_NUM-1&&stateY)||(x==ROW_NUM-1&&stateX)){//如果处于边界，且要填充数据
						    if(y==COL_NUM-1&&stateY){//列填充
						    	raf.seek(startPX);
								while((check=raf.read(bytes, 0, (TILE_SIZE-COL_ADD)*4))!=-1&&i<TILE_SIZE){
								//	if(x==36&&y==19)
								//		System.out.println(i+" "+check);
								  dos.write(bytes,0,(TILE_SIZE-COL_ADD)*4);//读入有数据信息内容
								  i++;
								  short temp = COL_ADD;
								  while(temp>0){//填充无用数据
								    mydos.writeFloat(NO_USE);
								    temp--;
							      }
								  startPX+=4L*COL;
								  if(startPX>=4L*COL*ROW+START){//如果当前操作像素节点超过输入流总节点，退出循环
									  break;
								  }
								  raf.seek(startPX);
								}
							}
							if(x==ROW_NUM-1&&stateX){//行填充
								if(!(y==COL_NUM-1&&stateY)){//如果当前不处于所有数据块的最后一块，防止最后一块被写入2次
									raf.seek(startPX);
								   for(int row=0;row<TILE_SIZE-ROW_ADD;row++){
								        raf.read(bytes,0,byteSize); 
								        dos.write(bytes,0,byteSize);
								        startPX+=4L*COL;
								        raf.seek(startPX);
								       }
								}
								 short temp = ROW_ADD;
								 while(temp>0){//填充无用数据
									 for(int t=0;t<TILE_SIZE;t++){
								      mydos.writeFloat(NO_USE);
									 }
								    temp--;
							     }			
							 }
						}else{//正常情况读取一块数据块内容
							  i=0;	
						     raf.seek(startPX);
							while(raf.read(bytes, 0, byteSize)!=-1&&i<TILE_SIZE){
								  dos.write(bytes,0,byteSize);//读入有数据信息内容
								  i++;
								  startPX+=4L*COL;  
								  raf.seek(startPX);
						     }
						}
					}
				}
			  	dos.close();
	         fos.close();
	     //	System.out.println("文件"+times+"完成！");
	}
	
/**
 * 写新的头文件
 * @param headPath
 * @throws IOException
 */
 private void writeHeadFile(String headPath) throws IOException{
	  TileHeadFile sp = new TileHeadFile(ROW, COL, x1,x2,y1,y2,z1,z2,ROW_NUM, COL_NUM, ROW_ADD, COL_ADD);
	  sp.writeHeadFile(headPath);
}
}
