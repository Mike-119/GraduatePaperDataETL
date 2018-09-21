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

public class CommitDetailFromLocalSplit {
	String project;
	String sha;
	String parentSha;
	Timestamp reviseDate;
	Timestamp commitDate;
	String fileSha;
	String fileName;
	String status;
	long additions;
	long deletions;
	long changes;

	public void getCommitDetail() throws Exception {
//		File directory = new File("D:\\研究生学习\\毕业论文\\数据获取处理\\test\\");
		File directory = new File("/home/mike/graduateDesign/commitDetail/detail20180224/");
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
				BufferedReader br = new BufferedReader(new InputStreamReader(fis,"utf-8"),10*1024*1024);// 用5M的缓冲读取文本文件  
				
				DBHelper db = new DBHelper();
				String fileName = files[f].getName();
				project = fileName.substring(0, fileName.lastIndexOf("."));
				
				String line = null;
				StringBuilder build = new StringBuilder();
				JSONParser parser = new JSONParser();
				while ((line = br.readLine()) != null && !line.equals("{"))
				{
					
				}
				if(line.equals("{"))
				{
				build.append(line);
				}
				
				while ((line = br.readLine()) != null) {
						build.append(line);
						build.append("\n");
						while ((line = br.readLine()) != null && !line.equals("}")) {
							build.append(line);
							build.append("\n");
							// System.out.println(line);
						}
						build.append(line);
//						System.out.println(build.toString());

						try{
						JSONObject json = (JSONObject) parser.parse(build.toString());
						sha = (String) json.get("sha");
						JSONArray parents = (JSONArray) json.get("parents");
						JSONObject parent = (JSONObject) parents.get(0);
						parentSha = (String) parent.get("sha");
						JSONObject commit = (JSONObject) json.get("commit");
						JSONObject author = (JSONObject) commit.get("author");
						reviseDate = timeTrans((String) author.get("date"));
						JSONObject committer = (JSONObject) commit.get("committer");
						commitDate = timeTrans((String) committer.get("date"));
						JSONArray array = (JSONArray) json.get("files");
						if (array == null) {
							continue;
						}
						for (int i = 0; i < array.size(); i++) {
							try {
								JSONObject obj = (JSONObject) array.get(i);
								fileSha = (String) obj.get("sha");
								fileName = (String) obj.get("filename");
								status = (String) obj.get("status");
								additions = (long) obj.get("additions");
								deletions = (long) obj.get("deletions");
								changes = (long) obj.get("changes");
								String sql = "insert into singleCommitDetail values(?,?,?,?,?,?,?,?,?,?,?)";
								String[] par1 = new String[6];
								Timestamp[] par2 = new Timestamp[2];
								long[] par3 = new long[3];
								par1[0] = project;
								par1[1] = sha;
								par1[2] = parentSha;
								par1[3] = fileSha;
								par1[4] = fileName;
								par1[5] = status;
								par2[0] = reviseDate;
								par2[1] = commitDate;
								par3[0] = additions;
								par3[1] = deletions;
								par3[2] = changes;
								db.insert(sql, par1, par2, par3);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						} catch (Exception e) {
							e.printStackTrace();
						}
					    build.setLength(0);
				}
				br.close();
				db.close();
			}
		}
	}

	public Timestamp timeTrans(String str) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	      Date date = sdf.parse(str);//拿到Date对象
	      String tmp = sdf2.format(date);//输出格式：2017-01-22 09:28:33
	      return Timestamp.valueOf(tmp);
	}

}
