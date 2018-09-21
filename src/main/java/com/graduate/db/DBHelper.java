package com.graduate.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

public class DBHelper {
    private Connection connect=null;
    private PreparedStatement preparedStatement=null;
    private Statement statement=null;
    private ResultSet result=null;
	private String url="jdbc:mysql://localhost:3306/guoshixin?"
            + "user=root&password=050105&useUnicode=true&useSSL=false&characterEncoding=UTF8";
    
    public ResultSet getResult() {
		return result;
	}
    
    
    public DBHelper()throws Exception
    {
    	Class.forName("com.mysql.jdbc.Driver");
    	connect=DriverManager.getConnection(url);
    }
    
    public void createTable(String sql)throws Exception
    {
    	statement=connect.createStatement();
    	statement.execute(sql);
    }
    
    public void insert(String sql)throws Exception
    {
    	preparedStatement=connect.prepareStatement(sql);
    	preparedStatement.execute();
    }
    
    public void insert(String sql,String[] par1,Timestamp[] par2,long[] par3)throws Exception
    {
    	preparedStatement=connect.prepareStatement(sql);
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
//    	System.out.println(preparedStatement);
    	preparedStatement.execute();
    }
    
    public void insertStarForkWatch(String sql,String[] par1,long[] par2,Timestamp[] par3)throws Exception
    {
    	preparedStatement=connect.prepareStatement(sql);
    	for(int i=0;i<1;i++)
    	{
    		preparedStatement.setString(i+1,par1[i]);
    	}
    	for(int i=0;i<4;i++)
    	{
    		preparedStatement.setLong(i+2,par2[i]);
    	}
    	for(int i=0;i<1;i++)
    	{
    		preparedStatement.setTimestamp(i+6,par3[i]);
    	}  
//    	System.out.println(preparedStatement);
    	preparedStatement.execute();
    }
    
    public ResultSet query(String sql)throws Exception
    {
    	preparedStatement=connect.prepareStatement(sql);
    	return preparedStatement.executeQuery();
    }
    
    
    public void close()
    {
    	if(connect!=null)
    	{
    		try{
    		connect.close();
    		}catch(SQLException e)
    		{
    			e.printStackTrace();
    		}
    	}
    	
    	if(statement!=null)
    	{
    		try{
    			statement.close();
    		}catch(SQLException e)
    		{
    			e.printStackTrace();
    		}
    	}
    	
    	if(preparedStatement!=null)
    	{
    		try{
    		preparedStatement.close();
    	    }catch(SQLException e)
    	    {
    		    e.printStackTrace();
    	    }
    	}
    }
    
    
}
