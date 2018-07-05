package com.comcast.BulkSolve.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Repository;

import com.comcast.BulkSolve.Utils.ApplicationConfiguration;


@Repository
public class DatabaseConnection {

	
	@Autowired
	private ApplicationConfiguration appprop;
	
	private JdbcTemplate jdbctemplate;
	
	Connection conn = null;
	
	public Connection getJdbcConnection(){
		
		if(conn == null){
			appprop.readProperties();
			System.out.println("Db driver is:" +appprop.getDb_driver());
			System.out.println("Db conn is:" + appprop.getDb_connection());
			System.out.println("Db user is:" +appprop.getDb_user());
			
			try{
				Class.forName(appprop.getDb_driver());
			}catch(ClassNotFoundException ex){
				ex.printStackTrace();
			}
			  
			try{
				conn =DriverManager.getConnection(appprop.getDb_connection(), appprop.getDb_user(), appprop.getDb_password());
				return conn;
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		System.out.println("Connection is:" + conn.toString());
		return conn;
	}
	
	public JdbcTemplate getJdbcTemplate(){
		
		if(conn==null){
			getJdbcConnection();
		}
		
		jdbctemplate = new JdbcTemplate(new SingleConnectionDataSource(conn,true));
		return jdbctemplate;
	}
}
