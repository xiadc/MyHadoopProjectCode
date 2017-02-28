package com.xdc;

import java.io.FileInputStream;

public class Params {
	private String paramsPath ;
	public Params(String paramsPath){
		this.paramsPath = paramsPath;
	}
	public String[] getParams(){
		try {
			FileInputStream  fis = new FileInputStream(paramsPath);
			byte[] bytes = new byte[2048];
			fis.read(bytes);
			String s= new  String(bytes, "utf-8");
			//System.out.println(s);
			s=s.replaceAll("( )( )+"," ");// 去掉字符中间多于2个的空格
			s =s .trim();//去掉首尾多余的空格
			//System.out.println(s);
			String[] params =s.split(" ");
			if(params.length!=3){
				System.out.println("参数错误！");
			}
			params[1] = params[1].trim();
			params[2] = params[2].trim();
			//System.out.println(params[0]);
			//System.out.println(params[1]);
			//System.out.println(params[2]);
			fis.close();
			return params;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
}
