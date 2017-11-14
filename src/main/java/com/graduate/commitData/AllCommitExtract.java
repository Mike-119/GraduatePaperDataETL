package com.graduate.commitData;

import com.graduate.db.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import java.io.File;

public class AllCommitExtract {
	String sha;
	String authorName;
	String authorEmail;
	String reviseDate;
	String committerName;
	String committerEmail;
	String commitDate;
	String parentSha;

	public void extractAllCommit() {
		JSONParser parser = new JSONParser();
		try {
			File file=new File("D:\\test");
//			File file = new File("/home/mike/graduateDesign/1114/allCommitPages");
			File[] files = file.listFiles();
			String path;
			String fileName;
			for (File f : files) {
				path = f.getAbsolutePath();
				fileName = f.getName();
				int i = fileName.indexOf(".");
				fileName = fileName.substring(0, i);
				// System.out.println(path+","+fileName);

				DBHelper db = new DBHelper();
				try {
					JSONArray array = (JSONArray) parser.parse(new FileReader(path));
					for (Object o : array) {
						JSONObject obj = (JSONObject) o;
						sha = (String) obj.get("sha");
						JSONObject commit = (JSONObject) obj.get("commit");
						JSONObject author = (JSONObject) commit.get("author");
						authorName = (String) author.get("name");
						authorEmail = (String) author.get("email");
						reviseDate = (String) author.get("date");
						reviseDate = reviseDate.replace("T", " ").replace("Z", "");
						JSONObject committer = (JSONObject) commit.get("committer");
						committerName = (String) committer.get("name");
						committerEmail = (String) committer.get("email");
						commitDate = (String) committer.get("date");
						commitDate = commitDate.replace("T", " ").replace("Z", "");
						JSONArray parents = (JSONArray) obj.get("parents");
						JSONObject parent = (JSONObject) parents.get(0);
						parentSha = (String) parent.get("sha");

						// System.out.println(sha+","+authorName+","+"authorEmail"+","+reviseDate+","+committerName+","+committerEmail+","+commitDate+","+parentSha);
						String sql = "insert into allCommit values(\"" + fileName + "\",\"" + sha + "\",\"" + authorName
								+ "\",\"" + authorEmail + "\",\"" + reviseDate + "\",\"" + committerName + "\",\""
								+ committerEmail + "\",\"" + commitDate + "\",\"" + parentSha + "\")";
						try {
							db.insert(sql);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					db.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
