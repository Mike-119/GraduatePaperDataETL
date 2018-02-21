package com.graduate.commitData;

import com.graduate.db.*;

import java.sql.ResultSet;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.event.TreeWillExpandListener;

import java.io.*;
import java.net.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SingleCommitDetail {
	private final String USER_AGENT = "Mike-119";
	private final String access_token = "";
	String project;
	String sha;
	String fileSha;
	String fileName;
	String status;
	long additions;
	long deletions;
	long changes;

	public SingleCommitDetail() {

	}

	public void getCommitDetail() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("/home/mike/graduateDesign/projectList"));
			String project = null;
			while ((project = br.readLine()) != null) {

				try{
					DBHelper db = new DBHelper();
					ResultSet rs = db.query("select sha from allCommitUnique where project='" + project + "'");
					while (rs.next()) {
						DBHelper db2 = new DBHelper();
						sha = rs.getString(1);
						String response = getRequest(project, sha);
						JSONParser parser = new JSONParser();
						JSONObject object = (JSONObject) parser.parse(response);
						JSONArray files = (JSONArray) object.get("files");
						if (files == null) {
							continue;
						}
						for (int i = 0; i < files.size(); i++) {
							JSONObject file = (JSONObject) files.get(i);
							fileSha = (String) file.get("sha");
							fileName = (String) file.get("filename");
							status = (String) file.get("status");
							additions = (long) file.get("additions");
							deletions = (long) file.get("deletions");
							changes = (long) file.get("changes");
							String sql = "insert into singleCommitDetail values(?,?,?,?,?,?,?,?)";
							String[] par1 = new String[5];
							long[] par2 = new long[3];
							par1[0] = sha;
							par1[1] = project;
							par1[2] = fileSha;
							par1[3] = fileName;
							par1[4] = status;
							par2[0] = additions;
							par2[1] = deletions;
							par2[2] = changes;
							try {
//								db2.insert(sql, par1, par2);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						db2.close();
						Thread.sleep(720);
					}
					System.out.println("finished: "+project);
					rs.close();
					db.close();
			}catch(Exception e)
			{
				System.out.println("error: "+project);
				e.printStackTrace();
			}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getRequest(String project, String sha) throws Exception {
		String url = "https://api.github.com/repos/apache/" + project + "/commits/" + sha
				+ "?access_token=" + access_token;

		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		// 默认值我GET
		con.setRequestMethod("GET");

		// 添加请求头
		con.setRequestProperty("User-Agent", USER_AGENT);
		// con.setRequestProperty("Authorization", "token
		// 24040dfb80ba27f2bc93eef4b0fcd906d92112e3");

		// int responseCode = con.getResponseCode();
		// System.out.println("\nSending 'GET' request to URL : " + url);
		// System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();

	}
}
