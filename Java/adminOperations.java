package com.library;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import com.google.gson.*;

public class adminOperations extends HttpServlet {

	private static final String dbDriver = "com.mysql.jdbc.Driver";
        private static final String dbUName = "root";
        private static final String dbPass = "hello";
        private static final String dbUrl = "jdbc:mysql://localhost:3306/libwebapp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Inside adminOperations");
		Connection con = null;
		try
		{
			Hashtable hash = new Hashtable();
                        Class.forName(dbDriver);
                        con = DriverManager.getConnection(dbUrl,dbUName,dbPass);
                        int flag = Integer.parseInt(request.getParameter("flag"));
			if(flag == 1)
			{
				String status = "Failed to add";
				String title = request.getParameter("title");
				String isbn = request.getParameter("isbn");
				String author = request.getParameter("author");
				String edition = request.getParameter("edition");
				String copies = request.getParameter("copies");
				String sqlCheckString = "SELECT * FROM bookTable where isbn='"+isbn+"'";
				Statement stmt = null;
				ResultSet rs = null;
				Statement stmt1 = null;
				try
				{
					stmt = con.createStatement();
					rs = stmt.executeQuery(sqlCheckString);
					if(rs.next())
					{
						status = "The book already exists";
					}
					else
					{
						String sqlUpdateString = "INSERT INTO bookTable values('"+isbn+"','"+title+"','"+author+"','"+edition+"','"+copies+"')";
						stmt1 = con.createStatement();
						stmt1.executeUpdate(sqlUpdateString);
						status = "The new book has been added";		
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
				hash.put("status",status);
			}
			else if(flag == 2)
			{
				ArrayList bookList = new ArrayList();
				String sqlString = "SELECT * FROM bookTable";
				Statement stmt = null;
				ResultSet rs =null;
				try
				{
					stmt = con.createStatement();
					rs = stmt.executeQuery(sqlString);
					bookList = getBookList(rs);
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
				hash.put("books",bookList);
			}
			else if(flag == 3)
			{
				ArrayList bookList = new ArrayList();
				String isbn = request.getParameter("isbn");
				String value = request.getParameter("value");
				String sqlUpdateString = "UPDATE bookTable SET copies="+value+" WHERE isbn='"+isbn+"'";
				Statement stmt1 = null;
				Statement stmt = null;
				ResultSet rs = null;
				try
				{
					stmt1 = con.createStatement();
					stmt1.executeUpdate(sqlUpdateString);
					String sqlGetString = "SELECT * FROM bookTable";
					stmt = con.createStatement();
					rs = stmt.executeQuery(sqlGetString);
					bookList = getBookList(rs);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					try
					{
						if(stmt1!=null)
						{ stmt.close(); }
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
				hash.put("books",bookList);
			}
			else if(flag == 4)
			{
				ArrayList bookList = new ArrayList();
				String isbn = request.getParameter("isbn");
				String status=  "failed";

				Statement stmt1 = null;
				Statement stmt = null;
				ResultSet rs = null;
				Statement stmt2 = null;
				ResultSet rs1 = null;
				Statement stmt3 = null;
				Statement stmt4 = null;
				ResultSet rs2 = null;
				try
				{
					String historyCheck = "SELECT * FROM history where isbn='"+isbn+"' and status='BORROWED'";
					stmt4 = con.createStatement();
					rs2 = stmt4.executeQuery(historyCheck);
					if(rs2.next())
					{
						status = "failed";		
					}
					else
					{
						String sqlRemoveString = "DELETE FROM bookTable WHERE isbn='"+isbn+"'";
						stmt1 = con.createStatement();
						stmt1.executeUpdate(sqlRemoveString);
						String checkQueTable = "SELECT * FROM queTable where isbn='"+isbn+"'";
						stmt2 = con.createStatement();
						rs1 = stmt2.executeQuery(checkQueTable);
						if(rs1.next())
						{	
							String updateQueTable = "DELETE FROM queTable where isbn='"+isbn+"'";
							stmt3 = con.createStatement();
							stmt3.executeUpdate(updateQueTable);
						}
						status = "success";
					}
					String sqlGetString = "Select * FROM bookTable";
                                        stmt = con.createStatement();
                                        rs = stmt.executeQuery(sqlGetString);
                                        bookList = getBookList(rs);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					try
					{
						if(stmt1!=null)
						{ stmt1.close(); }
						if(rs!=null)
						{ rs.close(); }
						if(stmt != null)
						{ stmt.close(); }
						if(rs1!=null)
						{ rs1.close(); }
						if(stmt2!=null)
						{ stmt2.close(); }
						if(stmt3!=null)
						{ stmt3.close(); }	
						if(rs2!=null)
						{ rs2.close(); }
						if(stmt4!=null)
						{ stmt4.close(); }
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}	
				hash.put("status",status);
				hash.put("books",bookList);	
			}
			else if(flag == 5)
			{
				ArrayList userList = new ArrayList();
				String sqlGetString = "SELECT * FROM userTable";
				Statement stmt = null;
				ResultSet rs = null;
				try
				{
					stmt = con.createStatement();
					rs = stmt.executeQuery(sqlGetString);
					userList = getUserList(rs);	
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
				hash.put("users",userList);
			}
			else if(flag == 6)
			{
				ArrayList userList = new ArrayList();
				String uname = request.getParameter("user");
				String status = "failed";

				Statement stmt1 = null;
				Statement stmt = null;
				ResultSet rs = null;
				Statement stmt2 = null;
				ResultSet rs1 = null;
				Statement stmt3 =null;
				Statement stmt4 = null;
				ResultSet rs2 = null;
				try
				{
					String checkHistoryTable = "SELECT * FROM history WHERE user='"+uname+"' and status='BORROWED'";
					stmt4 = con.createStatement();
					rs2 = stmt4.executeQuery(checkHistoryTable);
					if(rs2.next())
					{
						status = "failed";
					}
					else
					{
						status = "success";
						String removeUserString = "DELETE FROM userTable WHERE Username='"+uname+"'";
						stmt1 = con.createStatement();
						stmt1.executeUpdate(removeUserString);
						String checkQueTable = "SELECT * FROM queTable WHERE user='"+uname+"'";
						stmt2 = con.createStatement();
						rs1 = stmt2.executeQuery(checkQueTable);
						if(rs1.next())
						{
							String removeFromQueTable = "DELETE FROM queTable WHERE user='"+uname+"'";
							stmt3 = con.createStatement();
							stmt3.executeUpdate(removeFromQueTable);
						}
					}
					String getUserListString = "SELECT * FROM userTable";
                                        stmt = con.createStatement();
                                        rs = stmt.executeQuery(getUserListString);
                                        userList = getUserList(rs);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					try
					{
						if(stmt1!=null)
						{ stmt1.close(); }
						if(rs!=null)
						{ rs.close(); }
						if(stmt!=null)
						{ stmt.close(); }
						if(stmt3!=null)
						{ stmt3.close(); }
						if(rs1!=null)
						{ rs1.close(); }
						if(stmt2!=null)
						{ stmt2.close(); }
						if(rs2!=null)
						{ rs2.close(); }
						if(stmt4!=null)
						{ stmt4.close(); }
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				hash.put("status",status);
				hash.put("users",userList);
			}
			else if(flag == 7)
			{
				Hashtable queList = new Hashtable();
				String getQueListString = "SELECT * FROM queTable";
				Statement stmt = null;
				ResultSet rs = null;
				try
				{
					stmt = con.createStatement();
					rs = stmt.executeQuery(getQueListString);
					queList = getQueList(rs);
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
				hash.put("queDetails",queList);	
			}
			else if(flag == 8)
			{
				Hashtable queList = new Hashtable();
				String isbn = request.getParameter("isbn");
				String user = request.getParameter("user");
				String removeFromQueString = "DELETE FROM queTable WHERE isbn='"+isbn+"' and user ='"+user+"'";
				Statement stmt1 = null;
				Statement stmt = null;
				ResultSet rs = null;
				try
				{
					stmt1 = con.createStatement();
					stmt1.executeUpdate(removeFromQueString);
					String getQueListString = "SELECT * FROM queTable";
					stmt = con.createStatement();
					rs = stmt.executeQuery(getQueListString);
					queList = getQueList(rs);
				}
				catch(Exception e)
				{		
					e.printStackTrace();
				}	
				finally
				{
					try
					{
						if(stmt1!=null)
						{ stmt1.close(); }
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
				hash.put("queDetails",queList);
			}

			String json = new Gson().toJson(hash);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.write(json);
				
			System.out.println("Done with adminOperations");
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
	
	public static ArrayList getBookList(ResultSet rs)
	{
		ArrayList bookList = new ArrayList();
		try
		{
			while(rs.next())
			{
				ArrayList tempList = new ArrayList();
	                       	tempList.add(rs.getString(1));
        	                tempList.add(rs.getString(2));
                	        tempList.add(rs.getString(3));
                        	tempList.add(rs.getString(4));  
                               	tempList.add(rs.getString(5));
                               	bookList.add(tempList);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return bookList;
	}

	public static ArrayList getUserList(ResultSet rs)
	{
		ArrayList userList = new ArrayList();
		try
		{
			while(rs.next())
			{
				ArrayList tempList = new ArrayList();
                                tempList.add(rs.getString(1));
                                tempList.add(rs.getString(3));
                                tempList.add(rs.getString(4));
				userList.add(tempList);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return userList;
	}

	public static Hashtable getQueList(ResultSet rs)
	{
		Hashtable queList = new Hashtable();
		try	
		{
			while(rs.next())
			{
				String isbn = rs.getString(1);
                                String title = rs.getString(3);
                                String key = isbn+" : "+title;
                                if(queList.containsKey(key))
                                {
                                	ArrayList tempList = (ArrayList)queList.get(key);
                                        tempList.add(rs.getString(2));
                                        queList.put(key,tempList);
                              	}
                                else
                                {
                                      	ArrayList tempList = new ArrayList();
                                        tempList.add(rs.getString(2));
                                        queList.put(key,tempList);
                                }
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return queList;
	}
}
