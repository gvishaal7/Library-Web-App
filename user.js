function setUserDisplay(choice)
{
	if(choice == 1)
	{
		document.getElementById("borrowBookDiv").style.display = 'block';
		document.getElementById("returnBookDiv").style.display = 'none';
		document.getElementById("bookMarkDetDiv").style.display = 'none';
		document.getElementById("history").style.display = 'none';
	}
	else if(choice == 2)
	{
		document.getElementById("borrowBookDiv").style.display = 'none';
                document.getElementById("returnBookDiv").style.display = 'block';
                document.getElementById("bookMarkDetDiv").style.display = 'none';
                document.getElementById("history").style.display = 'none';
	}
	else if(choice == 3)
	{
		document.getElementById("borrowBookDiv").style.display = 'none';
                document.getElementById("returnBookDiv").style.display = 'none';
                document.getElementById("bookMarkDetDiv").style.display = 'block';
                document.getElementById("history").style.display = 'none';
	}
	else if(choice == 4)
	{
		document.getElementById("borrowBookDiv").style.display = 'none';
                document.getElementById("returnBookDiv").style.display = 'none';
                document.getElementById("bookMarkDetDiv").style.display = 'none';
                document.getElementById("history").style.display = 'block';
	}
	else
	{
		document.getElementById("borrowBookDiv").style.display = 'none';
                document.getElementById("returnBookDiv").style.display = 'none';
                document.getElementById("bookMarkDetDiv").style.display = 'none';
                document.getElementById("history").style.display = 'none';
	}
}

function setQueDetails(user)
{
        var flag = 0;
        $.post("../userOperations",{flag : flag, user : user} , function(responseJson) {
                var queDetails = responseJson["queDetails"];
                updateQueDetails(queDetails);
        });
}

function updateQueDetails(queDetails)
{
        var table = "<table cellpadding='0' cellspacing='0' id='userQueDetailsTable' class='outputTable'>";
	table += "<tr class='heading'><td colspan='4'>Books In Que</td></tr>";
        table += "<tr class='heading'><td> S.No. </td><td> ISBN number </td><td> Title </td><td> Edition </td></tr>";
	for(var i=0;i<queDetails.length;i++)
	{
		var book = queDetails[i];
		table += "<tr><td>"+(i+1)+"</td><td>"+book[0]+"</td><td>"+book[1]+"</td><td>"+book[2]+"</td>";
	}
        table += "</table>";
	$("#booksOnQueDiv").empty();
	$("#booksOnQueDiv").append(table);
}

$(document).on("change","#userBorrowBook",function(event) {
	if(document.getElementById("userBorrowBook").checked)
	{
		setUserDisplay(1);
		var flag = 7;
		var table = "<table cellpadding='0' cellspacing='0' id='borrowBookTable' class='outputTable'>";
		table += "<tr><td> ISBN no. : </td><td><input type='text' id='borrowBookID' /></td></tr>";
		table += "<tr><td><button id='borrowBookSubmit' class='submitButton'> Borrow </button></td>";
		table += "<td><button id='borrwoBookClear' class='clearButton'> Clear </button></td></tr>";
		table += "</table>";
		var bookList = [];
		$.post("../userOperations",{flag : flag}, function(responseJson) {
			bookList = responseJson["bookList"];
			table += "<table cellpadding='0' cellspacing='0' id='borrowBookList' class='outputTable'>";
			table += "<tr class='heading'><td colspan='5'> Available Books </td></tr>";
			table += "<tr class='heading'><td> S.No. </td><td> ISBN No. </td><td> Title </td><td> Author </td><td> Edition </td>";
			for(var i=0;i<bookList.length;i++)
			{
				var curBook= bookList[i];
				table += "<tr><td>"+(i+1)+"</td><td>"+curBook[0]+"</td><td>"+curBook[1]+"</td><td>"+curBook[2]+"</td><td>"+curBook[3]+"</td></tr>";
			}
			table += "</table>";
			$("#borrowBookDiv").empty();
			$("#borrowBookDiv").append(table); 
		});
	}
	event.preventDefault();
});

$(document).on("click","#borrowBookSubmit", function(event) {
	var user = document.getElementById("userName").value;
	var isbn = document.getElementById("borrowBookID").value;
	if(isbn == "" || isbn == null)
	{
		alert("Enter an ISBN Number");
	}
	else
	{
		borrowBook(isbn,user);	
	}
	event.preventDefault();
});

$(document).on("change","#userReturnBook",function(event) {
	if(document.getElementById("userReturnBook").checked)
	{
		setUserDisplay(2);
		var user = document.getElementById("userName").value;
		updateReturnBook(user);
		var flag = 8;
		/*
		var table ="<table cellpadding='0' cellspacing='0' id='returnBookTable' class='outputTable'>";
		table += "<tr><td> ISBN No. : </td><td><input type='text' id='returnBookID' /></td></tr>";
		table += "<tr><td><button id='returnBookSubmit' class='submitButton'> Return </button></td>";
		table += "<td><button id='returnBookClear' class='classButton'> Clear </button></td></tr>";
		table += "</table>";
		var bookList = [];
		$.post("../userOperations",{flag : flag, user : user}, function(responseJson) {
			bookList = responseJson["bookList"];
			table += "<table cellpadding='0' cellspacing='0' id='returnBookList' class='outputTable'>";
			table += "<tr class='heading'><td colspan='5'> Borrowed Books </td></tr>";
			table += "<tr class='heading'><td> S.No. </td><td> ISBN No. </td><td> Title </td><td> Edition </td><td> Borrow Date </td></tr>";
			for(var i=0;i<bookList.length;i++)
			{
				var curBook = bookList[i];
				table += "<tr><td>"+(i+1)+"</td><td>"+curBook[0]+"</td><td>"+curBook[1]+"</td><td>"+curBook[2]+"</td><td>"+curBook[3]+"</td></tr>";
			}
			table += "</table>";
			$("#returnBookDiv").empty();
			$("#returnBookDiv").append(table);
		}); */
	}
	event.preventDefault();
});

$(document).on("click","#returnBookSubmit", function(event) {
	var user = document.getElementById("userName").value;
	var isbn = document.getElementById("returnBookID").value;
	if(isbn == null || isbn == "")
	{
		alert("Enter an ISBN Number");
	}
	else
	{
		returnBook(isbn,user);
	}
});


function borrowBook(isbn,user)
{
	var flag = 1;
	$.post("../userOperations",{flag : flag, isbn : isbn, user : user}, function(responseJson) {
		var status = responseJson["status"];
		if(status == "qued")
		{
			alert("The current book is not available, so you are qued");
			var queItems = responseJson["queItems"];
			updateQueDetails(queItems);			
		}	
		else if(status == "success")
		{
			alert("You have been lent the book");
			updateBorrowDisplay(user);
		}
		else if(status == "fail")
		{
			alert("The book does not exist");
		}
		else if(status == "alreadyq")
		{	
			alert("You are already qued for this book. Please wait!");
		}
		else if(status == "alreadyb")
		{
			alert("Return the previous copy of this book, before you can borrow");
		}
	});
}

function returnBook(isbn,user)
{
	var flag = 2;
	$.post("../userOperations",{flag : flag, isbn : isbn, user : user}, function(responseJson) {
		var status = responseJson["status"];
		if(status == "returned")
		{
			alert("Thanks for returning the book");
			updateReturnBook(user);
		}
		else if(status == "fail")
		{
			alert("The book does not exist");
		}
		else if(status == "not")
		{
			alert("You have not borrowed this book");
		}
	});
}

function updateReturnBook(user)
{
	var flag = 8;
        var table ="<table cellpadding='0' cellspacing='0' id='returnBookTable' class='outputTable'>";
        table += "<tr><td> ISBN No. : </td><td><input type='text' id='returnBookID' /></td></tr>";
        table += "<tr><td><button id='returnBookSubmit' class='submitButton'> Return </button></td>";
        table += "<td><button id='returnBookClear' class='classButton'> Clear </button></td></tr>";
        table += "</table>";
        var bookList = [];
        $.post("../userOperations",{flag : flag, user : user}, function(responseJson) {
        	bookList = responseJson["bookList"];
                table += "<table cellpadding='0' cellspacing='0' id='returnBookList' class='outputTable'>";
                table += "<tr class='heading'><td colspan='5'> Borrowed Books </td></tr>";
                table += "<tr class='heading'><td> S.No. </td><td> ISBN No. </td><td> Title </td><td> Edition </td><td> Borrow Date </td></tr>";
                for(var i=0;i<bookList.length;i++)
                {
                	var curBook = bookList[i];
                        table += "<tr><td>"+(i+1)+"</td><td>"+curBook[0]+"</td><td>"+curBook[1]+"</td><td>"+curBook[2]+"</td><td>"+curBook[3]+"</td></tr>";
                }
                table += "</table>";
                $("#returnBookDiv").empty();
                $("#returnBookDiv").append(table);
      	});

}

$(document).on("change","#bookMarkDetails",function(event) {
	if(document.getElementById("bookMarkDetails").checked)
	{
		setUserDisplay(3);
		var table = "<table cellpadding='0' cellspacing='0'  id='bookMarkTable' class='outputTable'>";
		table += "<tr><td> ISBN no. : </td><td> <input type='text' id='bookMarkID' /></td></tr>";
		table += "<tr><td> Page no. : </td><td> <input type='text' id='bookMarkPage' /></td></tr>";
		table += "<tr><td><button id='bookMarkSubmit' class='submitButton'> Bookmark </button></td>";
		table += "<td><button id='bookMarkClear' class='clearButton'> Clear </button></td></tr>";
		table += "</table>";
		var flag = 8;
		var user = document.getElementById("userName").value;
		var bookList = [];
		$.post("../userOperations",{flag : flag, user : user}, function(responseJson) {
                        bookList = responseJson["bookList"];
	                table += "<table cellpadding='0' cellspacing='0' id='returnBookList' class='outputTable'>";
        	        table += "<tr class='heading'><td colspan='5'> Borrowed Books </td></tr>";
                	table += "<tr class='heading'><td> S.No. </td><td> ISBN No. </td><td> Title </td><td> Edition </td></tr>";
	                for(var i=0;i<bookList.length;i++)
        	        {
                		var curBook = bookList[i];
                        	table += "<tr><td>"+(i+1)+"</td><td>"+curBook[0]+"</td><td>"+curBook[1]+"</td><td>"+curBook[2]+"</td></tr>";
	                }
        	        table += "</table>";
			$("#bookMarkInputDiv").empty();
			$("#bookMarkInputDiv").append(table);
		});
		getBookMarkDet();
	}
	event.preventDefault();
});

$(document).on("click","#bookMarkSubmit",function(event) {
	var user = document.getElementById("userName").value;
	var isbn = document.getElementById("bookMarkID").value;
	if(isbn == "" || isbn == null)
	{
		alert("Enter an ISBN number");
	}
	else
	{
		var pageNo = document.getElementById("bookMarkPage").value;
		if(pageNo == "" || pageNo == null)
		{
			alert("Enter a page number");
		}
		else
		{
			bookMarkThePage(user,isbn,pageNo);
		}
	}
	event.preventDefault();
});

function getBookMarkDet()
{
	var user = document.getElementById("userName").value;
	var flag = 3;
	$.post("../userOperations",{flag : flag, user : user}, function(responseJson) {
		var bookMarkDetails = responseJson["bookMarkDet"];
		setBookMarkDetDisplay(user,bookMarkDetails);
	});
}

function setBookMarkDetDisplay(user,bookMarkDetails)
{
	var table = "<table cellspacing='0' cellpadding='0' id='bookMarkDetailsTable' class='outputTable'>";
	table += "<tr class='heading'><td colspan='5'>Bookmark Details</td></tr>";
	table += "<tr class='heading'><td> S.No. </td><td> ISBN no. </td><td> Title </td><td> Page no. </td><td> Remove </td></tr>";
	for(var i=0;i<bookMarkDetails.length;i++)	
	{
		var curBook = bookMarkDetails[i];
		table += "<tr><td>"+(i+1)+"</td><td>"+curBook[0]+"</td><td>"+curBook[1]+"</td><td>"+curBook[2]+"</td>";
		table += "<td><button class='modifyButton' onclick=removeBookMark('"+user+"','"+curBook[0]+"')> Remove </td></tr>";
	}
	table += "</table>";
	$("#bookMarkDetailsDiv").empty();
	$("#bookMarkDetailsDiv").append(table);
}

function bookMarkThePage(user,isbn,page)
{
	var flag =4;
	$.post("../userOperations",{flag : flag, isbn : isbn, user : user, page : page}, function(responseJson) {
		var status = responseJson["status"];
		if(status == "success")
		{
			alert("Bookmarked");
			var bookMarkDetails = responseJson["bookMarkDet"];
			setBookMarkDetDisplay(user,bookMarkDetails);
		}
		else if(status == "failed")
		{
			alert("The Book does not exist");
		}	
		else if(status == "not")
		{
			alert("You cannot bookmark on books, that you have not borrowed");
		}
	});
}

function removeBookMark(user,isbn)
{
	var flag =5;
	$.post("../userOperations",{flag : flag, user : user, isbn : isbn}, function(responseJson) {
		var status = responseJson["status"];
		if(status == "success")
		{
			alert("Bookmark Removed");
			var bookMarkDetails = responseJson["bookMarkDet"];
			setBookMarkDetDisplay(user,bookMarkDetails);
		}
		else 
		{
			alert("Count Remove Book at the moment");
		}
	});
}

$(document).on("change","#userHistory",function(event) {
	if(document.getElementById("userHistory").checked)
	{
		setUserDisplay(4);
		var flag = 6;
		var user = document.getElementById("userName").value;
		$.post("../userOperations",{flag : flag, user : user}, function(responseJson) {
			var userHistory = responseJson["userHistory"];
			var table = "<table cellpadding='0' cellspacing='0' id='userHistoryTable' class='outputTable'>";
			table += "<tr class='heading'><td colspan='6'>User History</td></tr>";
			table += "<tr class='heading'><td> S.No. </td><td> ISBN no. </td><td> Title </td><td> Edition </td><td>Borrow Date</td><td> Status </td></tr>";
			for(var i=0;i<userHistory.length;i++)
			{
				var curBook = userHistory[i];
				table += "<tr><td>"+(i+1)+"</td><td>"+curBook[0]+"</td><td>"+curBook[1]+"</td><td>"+curBook[2]+"</td><td>"+curBook[3]+"</td><td>"+curBook[4]+"</td></tr>";	
			}
			table += "</table>";
			$("#history").empty();
			$("#history").append(table);
		});
	}
	event.preventDefault();
});
