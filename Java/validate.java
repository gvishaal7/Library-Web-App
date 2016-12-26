package com.library;

import java.io.*;
import java.sql.*;
import java.text.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import com.google.gson.*;

public class validate extends HttpServlet{

	private static final String dbDriver = "com.mysql.jdbc.Driver";
	private static final String dbUName = "root";
	private static final String dbPass = "hello";
	private static final String dbUrl = "jdbc:mysql://localhost:3306/libwebapp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Inside Validate");
		Connection con = null;
		try
		{
			Hashtable hash = new Hashtable();
			String status = "failed";
			Class.forName(dbDriver);
			con = DriverManager.getConnection(dbUrl,dbUName,dbPass);
			int flag = Integer.parseInt(request.getParameter("flag"));
			if(flag == 1)
			{
				String userName = request.getParameter("userName");
				String password = request.getParameter("password");
				String sqlString = "SELECT * FROM userTable where Username='"+userName+"' and Password='"+password+"'";
//				System.out.println("query :: "+sqlString);
				Statement stmt = null;
				ResultSet rs = null;
				try
				{
					stmt = con.createStatement();
					rs = stmt.executeQuery(sqlString);
					if(rs.next())
					{
						status = userName;	
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					try
					{
						if(rs!=null)
						{ rs.close(); }
						if(stmt!=null)
						{ stmt.close(); }
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			else if(flag == 2)
			{
				String uname = request.getParameter("uname");
				String pass = request.getParameter("pass");
				String email = request.getParameter("email");
				String phoneNo = request.getParameter("phno");
				String sqlString = "SELECT * FROM userTable where Username='"+uname+"'";
		
				Statement stmt = null;
				ResultSet rs = null;
				Statement stmt1 = null;
				try
				{
					stmt = con.createStatement();
					rs = stmt.executeQuery(sqlString);
					if(rs.next())
					{
						status = "failed";
					}	
					else
					{
						String sqlUpdateStatement = "INSERT INTO userTable values('"+uname+"','"+pass+"','"+email+"','"+phoneNo+"')";
						stmt1 = con.createStatement();
						stmt1.executeUpdate(sqlUpdateStatement);
						status ="success";
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					try
					{
						if(rs!=null)
						{ rs.close(); }
						if(stmt!=null)
						{ stmt.close(); }
						if(stmt1!=null)
						{ stmt1.close(); }
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}

			hash.put("status",status);
			String json = new Gson().toJson(hash);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.write(json);

			System.out.println("Done with validate");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally 
               	{
                	try
                       	{
                                if(con!=null)
                               	{ con.close(); }
                   	}       
                        catch(Exception e)
                        {
                             	e.printStackTrace();
                        }
          	}

	}	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

}
