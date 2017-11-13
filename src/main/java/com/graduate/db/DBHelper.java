package com.graduate.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class DBHelper {
    private Connection connect=null;
    private PreparedStatement preparedStatement=null;
    private Statement statement=null;
    private ResultSet result=null;
    private String url="jdbc:mysql://localhost:3306/graduatePaper?"
            + "user=root&password=050105&useUnicode=true&useSSL=false&characterEncoding=UTF8";
    
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
