package com.library;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.google.gson.*;
import java.sql.*;
import java.text.*;

public class userOperations extends HttpServlet {

	private static final String dbDriver = "com.mysql.jdbc.Driver";
        private static final String dbUName = "root";
        private static final String dbPass = "hello";
        private static final String dbUrl = "jdbc:mysql://localhost:3306/libwebapp";
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Inside userOperations");
		Connection con = null;
		try
		{
			Hashtable hash = new Hashtable();
			Class.forName(dbDriver);
			con = DriverManager.getConnection(dbUrl,dbUName,dbPass);
                        int flag = Integer.parseInt(request.getParameter("flag"));
			if(flag == 0)
			{
				ArrayList queList = new ArrayList();
				String user = request.getParameter("user");
				Statement stmt = null;
				ResultSet rs = null;
				try
				{
					String getFromQueTable = "SELECT * FROM queTable where user='"+user+"'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(getFromQueTable);
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
			else if(flag == 1)
			{	
				String status = "fail";
				String isbn = request.getParameter("isbn");
				String user = request.getParameter("user");

				Statement stmt = null;
				ResultSet rs = null;
				Statement stmt1 = null;
				ResultSet rs1 = null;
				Statement stmt2 =null;
				Statement stmt3 =null;
				ResultSet rs2 =null;
				Statement stmt4 = null;
				ResultSet rs3 = null;

				try
				{
					String checkForBook = "SELECT * from bookTable where isbn='"+isbn+"'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(checkForBook);
					if(rs.next())
					{
						String checkQueTable = "SELECT * from queTable where isbn='"+isbn+"' and user='"+user+"'";
						stmt1 = con.createStatement();
						rs1 = stmt1.executeQuery(checkQueTable);
						if(rs1.next())
						{
							status = "alreadyq";
						}		
						else
						{
							String checkHistory = "SELECT * from history where isbn='"+isbn+"' and user ='"+user+"' and status='BORROWED' order by borrowDate desc";
							stmt4 = con.createStatement();
							rs3 = stmt4.executeQuery(checkHistory);
							if(rs3.next())
							{
								status = "alreadyb";
							}
							else
							{
								int copies = Integer.parseInt(rs.getString(5));
								String edition = rs.getString(4);
								String title = rs.getString(2);
								if(copies == 0)
								{
									status = "qued";
									String insertIntoQueTable = "INSERT INTO queTable values ('"+isbn+"','"+user+"','"+title+"','"+edition+"')";
									stmt2 = con.createStatement();
									stmt2.executeUpdate(insertIntoQueTable);	
									String getInfoFromQueTable = "SELECT * from queTable where user='"+user+"'";
									stmt3 = con.createStatement();
									rs2 = stmt3.executeQuery(getInfoFromQueTable);
									hash.put("queItems",getQueList(rs2));	
								}
								else
								{
									status = "success";
									copies--;
									String updateBookTable = "UPDATE bookTable SET copies='"+copies+"' WHERE isbn='"+isbn+"'";
									stmt2 = con.createStatement();
									stmt2.executeUpdate(updateBookTable);
									String curTime = String.valueOf(System.currentTimeMillis());
									String updateHistoryTable = "INSERT INTO history values('"+isbn+"','"+user+"','"+title+"','"+edition+"','"+curTime+"','BORROWED')";
									stmt3 = con.createStatement();
									stmt3.executeUpdate(updateHistoryTable);	
								}
							}
						}
					}
					else
					{
						status = "fail";
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
						if(rs1!=null)
						{ rs1.close(); }
						if(stmt1!=null)
						{ stmt1.close(); }
						if(stmt2!=null)
						{ stmt2.close(); }
						if(rs2!=null)
						{ rs.close(); }
						if(stmt3!=null)
						{ stmt3.close(); }
						if(rs3!=null)
						{ rs3.close(); }
						if(stmt4!=null)
						{ stmt4.close(); }
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
				String status = "fail";
				String isbn = request.getParameter("isbn");
				String user = request.getParameter("user");
				
				Statement stmt = null;
				ResultSet rs = null;
				Statement stmt1 = null;
				ResultSet rs1 =null;
				Statement stmt2 = null;
				Statement stmt3 = null;
				Statement stmt4 = null;
				Statement stmt5 = null;
				Statement stmt6 = null;
				ResultSet rs2 = null;

				try
				{
					String checkBookTable = "SELECT * FROM bookTable where isbn='"+isbn+"'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(checkBookTable);
					if(rs.next())
					{
						String historyTable = "SELECT * FROM history where user='"+user+"' and isbn='"+isbn+"' and status='BORROWED' order by borrowDate desc";
						stmt1 = con.createStatement();
						rs1 = stmt1.executeQuery(historyTable);
						if(rs1.next())
						{
							status = "returned";
							String borrowDate = rs1.getString(5);
							String updateHistoryTable = "UPDATE history SET status='RETURNED' WHERE isbn='"+isbn+"' and user='"+user+"' and borrowDate='"+borrowDate+"'";
							stmt2 = con.createStatement();
							stmt2.executeUpdate(updateHistoryTable);
							int copies = Integer.parseInt(rs.getString(5)) +1;
							if(copies == 1)
							{
								String getFromQueTable = "SELECT * FROM queTable where isbn='"+isbn+"'";
								stmt4 = con.createStatement();
								rs2 = stmt4.executeQuery(getFromQueTable);
								if(rs2.next())
								{
									String userName = rs2.getString(2);
									String title = rs.getString(2);
									String edition = rs.getString(4);
									String time = String.valueOf(System.currentTimeMillis());
									String addToHistory = "INSERT INTO history values('"+isbn+"','"+userName+"','"+title+"','"+edition+"','"+time+"','BORROWED')";
									stmt5 = con.createStatement();
									stmt5.executeUpdate(addToHistory);
									String removeFromQueTable = "DELETE FROM queTable WHERE isbn='"+isbn+"' and user = '"+userName+"'"; 
									stmt6 = con.createStatement();
									stmt6.executeUpdate(removeFromQueTable);
									copies--;
								}	
							}
							String updateBookTable = "UPDATE bookTable SET copies='"+copies+"' WHERE isbn='"+isbn+"'";
                                                        stmt3 = con.createStatement();
                                                        stmt3.executeUpdate(updateBookTable);	
						}
						else
						{
							status = "not";	
						}
					}
					else
					{
						status = "fail";
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
						if(rs1!=null)
						{ rs1.close(); }
						if(stmt1!=null)
						{ stmt1.close(); }
						if(stmt2!=null)
						{ stmt2.close(); }
						if(stmt3!=null)
						{ stmt3.close(); }
						if(rs2!=null)
						{ rs2.close(); }
						if(stmt4!=null)
						{ stmt4.close(); }
						if(stmt5!=null)
						{ stmt5.close(); }
						if(stmt6!=null)
						{ stmt6.close(); }
					}
					catch(Exception e)
					{		
						e.printStackTrace();	
					}
				}

				hash.put("status",status);	
			}
			else if(flag == 3)
			{
				ArrayList bookMarkDet = new ArrayList();
				String user = request.getParameter("user");
				Statement stmt = null;	
				ResultSet rs = null;
				try
				{
					String getFromBookMarkTable = "SELECT * FROM bookMarkTable where user='"+user+"'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(getFromBookMarkTable);
					bookMarkDet = getBookMarkDet(rs);
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
				hash.put("bookMarkDet",bookMarkDet);
			}
			else if(flag == 4)
			{
				String status = "failed";
				String user = request.getParameter("user");
				String isbn = request.getParameter("isbn");
				String page = request.getParameter("page");
				String checkBookTable = "SELECT * FROM bookTable where isbn='"+isbn+"'";

				Statement stmt = null;
				ResultSet rs = null;
				Statement stmt1 = null;
				ResultSet rs1 = null;
				Statement stmt2 = null;
				ResultSet rs2 = null;
				Statement stmt3 = null;
				Statement stmt4 = null;
				ResultSet rs3 = null;

				try
				{
					stmt = con.createStatement();
					rs = stmt.executeQuery(checkBookTable);
					if(rs.next())
					{
						String checkHistory = "SELECT * FROM history WHERE isbn='"+isbn+"' and user='"+user+"' and status='BORROWED' order by borrowDate desc";
						stmt1 = con.createStatement();
						rs1 = stmt1.executeQuery(checkHistory);
						if(rs1.next())
						{
							status = "success";
							String checkBMTable = "SELECT * FROM bookMarkTable WHERE isbn='"+isbn+"' and user ='"+user+"'";
							stmt2 = con.createStatement();
							rs2 = stmt2.executeQuery(checkBMTable);
							if(rs2.next())
							{
								String update = "UPDATE bookMarkTable SET page='"+page+"' WHERE isbn='"+isbn+"' and user='"+user+"'";
								stmt3 = con.createStatement();
								stmt3.executeUpdate(update);
							}
							else
							{
								String title = rs.getString(2);
								String add = "INSERT INTO bookMarkTable VALUES('"+user+"','"+isbn+"','"+title+"','"+page+"')";
								stmt3 = con.createStatement();
								stmt3.executeUpdate(add);
							}
							checkBMTable = "SELECT * FROM bookMarkTable WHERE user='"+user+"'";
							stmt4 = con.createStatement();
							rs3 = stmt4.executeQuery(checkBMTable);
							ArrayList bmList = getBookMarkDet(rs3);
							hash.put("bookMarkDet",bmList);
						}
						else
						{
							status= "not";
						}
					}
					else
					{
						status= "failed";
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
						if(stmt3!=null)
						{ stmt1.close(); }
						if(rs!=null)
						{ rs.close(); }
						if(stmt!=null)
						{ stmt.close(); }
						if(rs1!=null)
						{ rs1.close(); }
						if(stmt1!=null)
						{ stmt1.close(); }
						if(rs2!=null)
						{ rs2.close(); }
						if(stmt2!=null)
						{ stmt2.close(); }
						if(rs3!=null)
						{ rs3.close(); }
						if(stmt4!=null)
						{ stmt4.close(); }
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				hash.put("status",status);
			}
			else if(flag == 5)
			{
				String status = "failed";
				String user = request.getParameter("user");
				String isbn = request.getParameter("isbn");

				Statement stmt = null;
				Statement stmt1 = null;
				ResultSet rs = null;
				
				try
				{
					String removeBookMark = "DELETE FROM bookMarkTable WHERE isbn='"+isbn+"' and user ='"+user+"'";
					stmt = con.createStatement();
					stmt.executeUpdate(removeBookMark);
					status = "success";
					String getBMList = "SELECT * FROM bookMarkTable WHERE user='"+user+"'";
					stmt1 = con.createStatement();
					rs = stmt1.executeQuery(getBMList);
					ArrayList bmList = getBookMarkDet(rs);
					hash.put("bookMarkDet",bmList);
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
						if(stmt1!=null)
						{ stmt1.close(); }
						if(stmt!=null)
						{ stmt.close(); }
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				hash.put("status",status);
			}
			else if(flag == 6)
			{
				ArrayList userHistory = new ArrayList();
				String user = request.getParameter("user");
				Statement stmt = null;
				ResultSet rs = null;
				try
				{
					String getDetFromHistory = "SELECT * FROM history WHERE user='"+user+"'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(getDetFromHistory);
					while(rs.next())
					{
						ArrayList tempList = new ArrayList();
						tempList.add(rs.getString(1));
						tempList.add(rs.getString(3));
						tempList.add(rs.getString(4));
						long time = Long.parseLong(rs.getString(5));
						java.util.Date date = new java.util.Date(time);
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
						String bDate = sdf.format(date);
						tempList.add(bDate);
						tempList.add(rs.getString(6));
						userHistory.add(tempList);	
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
				hash.put("userHistory",userHistory);
			}
			else if(flag == 7)
			{
				ArrayList bookList = new ArrayList();
				Statement stmt = null;
				ResultSet rs = null;
				try
				{
					String getBookListString = "SELECT * FROM bookTable";
					stmt = con.createStatement();
					rs = stmt.executeQuery(getBookListString);
					while(rs.next())
					{
						ArrayList tempList = new ArrayList();
						tempList.add(rs.getString(1));
						tempList.add(rs.getString(2));
						tempList.add(rs.getString(3));
						tempList.add(rs.getString(4));
						bookList.add(tempList);
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
				hash.put("bookList",bookList);
			}
			else if(flag == 8)
			{
				ArrayList bookList = new ArrayList();
				String user = request.getParameter("user");	
				Statement stmt = null;
				ResultSet rs = null;
				try
				{
					String getHistory = "SELECT * FROM history WHERE user='"+user+"' and status = 'BORROWED'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(getHistory);
					while(rs.next())
					{	
						ArrayList tempList = new ArrayList();
						tempList.add(rs.getString(1));
						tempList.add(rs.getString(3));
						tempList.add(rs.getString(4));
						long time = Long.parseLong(rs.getString(5));
                                                java.util.Date date = new java.util.Date(time);
                                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
                                                String bDate = sdf.format(date);
						tempList.add(bDate);
						bookList.add(tempList);
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
				hash.put("bookList",bookList);
			}

			String json = new Gson().toJson(hash);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        PrintWriter out = response.getWriter();
                        out.write(json);
		
			System.out.println("Done with userOperations");
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

	public static ArrayList getBookMarkDet(ResultSet rs)
	{
		ArrayList bookMarkDet = new ArrayList();
		try
		{
			while(rs.next())	
			{	
				ArrayList tempList = new ArrayList();
				tempList.add(rs.getString(2));
				tempList.add(rs.getString(3));
				tempList.add(rs.getString(4));
				bookMarkDet.add(tempList);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return bookMarkDet;
	}

	public static ArrayList getQueList(ResultSet rs)
	{
		ArrayList queList = new ArrayList();
		try
		{
			while(rs.next())
			{
				ArrayList tempList = new ArrayList();
				tempList.add(rs.getString(1));
				tempList.add(rs.getString(3));
				tempList.add(rs.getString(4));
				queList.add(tempList);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return queList;
	}
}
