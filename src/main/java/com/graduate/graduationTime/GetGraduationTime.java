package com.graduate.graduationTime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.graduate.db.DBHelper;

public class GetGraduationTime {
	String project;
	String graduationTime;
	
	public void getTime(String path)throws Exception
	{
		File file = new File(path);
		BufferedReader br = new BufferedReader(new FileReader(file));
		DBHelper db = new DBHelper();
		String line = null;
		while((line = br.readLine())!=null)
		{
			project = line.substring(line.indexOf("\"")+1, line.lastIndexOf("\""));
			System.out.println(project);
			while(!(line = br.readLine()).startsWith("          <td>20"))
			{
//				System.out.println(line);
			}
			graduationTime = br.readLine().substring(14, 24)+" 0:0:0";
			System.out.println(graduationTime);
			
			String sql = "insert into graduateTime values(\"" + project + "\",\"" + graduationTime + "\");";
			System.out.println(sql);
			db.insert(sql);
		}
		
	}

}
