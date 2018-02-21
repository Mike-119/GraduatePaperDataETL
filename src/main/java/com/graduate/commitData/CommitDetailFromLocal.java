package com.graduate.commitData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.helpers.ISO8601DateFormat;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.graduate.db.ConnectionPool;
import com.graduate.db.DBHelper;

public class CommitDetailFromLocal {
	private static final String jdbcDriver = "com.mysql.jdbc.Driver"; // 数据库驱动
	private static final String dbUrl = "jdbc:mysql://localhost:3306/graduatePaper?useUnicode=true&characterEncoding=utf-8&useSSL=false"; // 数据 URL
	private static final String dbUsername = "root"; // 数据库用户名
	private static final String dbPassword = "050105"; // 数据库用户密码
	
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
		ConnectionPool dbpool = new ConnectionPool(jdbcDriver, dbUrl, dbUsername, dbPassword);
		dbpool.createPool();
		Connection conn = dbpool.getConnection();
		conn.setAutoCommit(false);
		PreparedStatement ppsm = null;
		
		int num = 0;  //数据库插入数据计数
		
//		File directory = new File("/home/mike/graduateDesign/commitDetail/detail1220/");
		File directory = new File("D:\\研究生学习\\毕业论文\\数据获取处理\\test\\");
		File[] files = directory.listFiles();
		for (int f = 0; f < files.length; f++) {
			if (files[f].isFile()) {
				// take file path and name
				File file = new File(files[f].getPath());
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				
				String fileName = files[f].getName();
				project = fileName.substring(0, fileName.lastIndexOf("."));
				
				String line = null;
				StringBuilder build = new StringBuilder();
				JSONParser parser = new JSONParser();
				
				while ((line = br.readLine()) != null) {
						build.append(line);
						build.append("\n");
						while (!(line = br.readLine()).equals("}")) {
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
								ppsm = conn.prepareStatement(sql);
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
								
								setString(ppsm, par1, par2, par3);
								ppsm.addBatch();
								num++;
								//批量插入，每次插入2000行
								if(num==2000){
									ppsm.executeBatch();   //批量将2000条记录插入数据库
									conn.commit();         //插入后commit
									dbpool.returnConnection(conn); //插入完成后释放连接
									ppsm.clearBatch();     //插入完成后情况Batch
									num = 0;
								}	
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
			}
		}
		if(num%2000!=0){     
			ppsm.executeBatch();           //最后的记录不足2000 行时，提交插入
			conn.commit();                 //插入后commit
			dbpool.returnConnection(conn); //插入完成后释放连接
			ppsm.clearBatch();             //插入完成后情况Batch
		}
		dbpool.closeConnectionPool();
	}

	public Timestamp timeTrans(String str) throws Exception {
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		  SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	      Date date = sdf.parse(str);//拿到Date对象
	      String tmp = sdf2.format(date);//输出格式：2017-01-22 09:28:33
	      return Timestamp.valueOf(tmp);
	}
	
    public void setString(PreparedStatement preparedStatement,String[] par1,Timestamp[] par2,long[] par3)throws Exception
    {
    	for(int i=0;i<6;i++)
    	{
    		preparedStatement.setString(i+1,par1[i]);
    	}
    	for(int i=0;i<2;i++)
    	{
    		preparedStatement.setTimestamp(i+7,par2[i]);
    	}
    	for(int i=0;i<3;i++)
    	{
    		preparedStatement.setLong(i+9,par3[i]);
    	}  
    }

}
