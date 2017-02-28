package com.hbaseClients;

import java.io.IOException;
import java.util.ArrayList;

public class TestClient {
	
	public static void main(String[] args) {
		
		try {
			System.out.println(HBaseClient.selectOneRow("USER_INFO", "admin123456", "user", "username"));
			
			
			String [] qualifiers = {"username", "password"};
			String [] values = {"aaa","123456"};
			ArrayList<StringBuilder[]> ls = new ArrayList<StringBuilder[]>();
			StringBuilder family = new StringBuilder("user");
			for (int i=0; i<qualifiers.length; i++){
				StringBuilder Qualifier = new StringBuilder(qualifiers[i]);
				StringBuilder value = new StringBuilder(values[i]);
				StringBuilder [] sbs = {family,Qualifier,value};
				ls.add(sbs);
			}
						
			HBaseClient.addRecord("USER_INFO", "test", ls);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		
	}

}
