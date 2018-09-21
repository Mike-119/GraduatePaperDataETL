package com.graduate.commitData;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.helpers.ISO8601DateFormat;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.graduate.db.DBHelper;

public class WatchStarFork {
	String project;
	long subscribers;
	long stargazers;
	long forks;
	long size;
	Timestamp date;

	public void getWatchStarFork(String path, String time) throws Exception {
//		File directory = new File("D:\\研究生学习\\毕业论文\\数据获取处理\\test\\");
		File directory = new File(path);
		File[] files = directory.listFiles();
		Arrays.sort(files, (f1, f2) -> {
			return new Long(f1.length()).compareTo(new Long(f2.length()));
		});
		for (int f = 0; f < files.length; f++) {
			if (files[f].isFile()) {
				// take file path and name
				File file = new File(files[f].getPath());
				
				//之前的写法，但是运行过程中内存超了
//				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));  
				
				//新的写法
				BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));    
				BufferedReader br = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);// 用5M的缓冲读取文本文件  				
				DBHelper db = new DBHelper();
				String line = null;
				StringBuilder build = new StringBuilder();
				JSONParser parser = new JSONParser();
				
				while ((line = br.readLine()) != null) 
				{
						build.append(line);
//						build.append("\n");
				}
		
						try{
						JSONObject json = (JSONObject) parser.parse(build.toString());
						project = (String)json.get("name");
						subscribers = (long)json.get("subscribers_count");
						stargazers = (long)json.get("stargazers_count");
						forks = (long)json.get("forks_count");
						size = (long)json.get("size");
						date = Timestamp.valueOf(time);
								
								String sql = "insert into watchStarForkSize values(?,?,?,?,?,?)";
								String[] par1 = new String[1];
								long[] par2 = new long[4];
								Timestamp[] par3 = new Timestamp[1];
								
								par1[0] = project;
								par2[0] = subscribers;
								par2[1] = stargazers;
								par2[2] = forks;
								par2[3] = size;
								par3[0] = date;
								db.insertStarForkWatch(sql, par1, par2, par3);
							
						
						} catch (Exception e) {
							e.printStackTrace();
						}
					    build.setLength(0);
				
				br.close();
				db.close();
			}
		}
	}

}

