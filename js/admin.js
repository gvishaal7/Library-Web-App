$(document).on("change","#adminAddBook",function(event) {
	if(document.getElementById("adminAddBook").checked)
	{
		setAdminDisplay(1);
	}
	event.preventDefault();
});

function setAdminDisplay(choice)
{
	if(choice == 1)
	{
		document.getElementById("addBookDiv").style.display = 'block';
	        document.getElementById("updateBookDiv").style.display = 'none';
        	document.getElementById("removeBookDiv").style.display = 'none';
	        document.getElementById("remUserDiv").style.display = 'none';
        	document.getElementById("queDetDiv").style.display = 'none';
	}
	else if(choice == 2)
	{
		document.getElementById("addBookDiv").style.display = 'none';
        	document.getElementById("updateBookDiv").style.display = 'block';
	        document.getElementById("removeBookDiv").style.display = 'none';
        	document.getElementById("remUserDiv").style.display = 'none';
	        document.getElementById("queDetDiv").style.display = 'none';
	}
	else if(choice == 3)
	{
		document.getElementById("addBookDiv").style.display = 'none';
	        document.getElementById("updateBookDiv").style.display = 'none';
        	document.getElementById("removeBookDiv").style.display = 'block';
	        document.getElementById("remUserDiv").style.display = 'none';
        	document.getElementById("queDetDiv").style.display = 'none';
	}
	else if(choice == 4)
	{
		document.getElementById("addBookDiv").style.display = 'none';
	        document.getElementById("updateBookDiv").style.display = 'none';
        	document.getElementById("removeBookDiv").style.display = 'none';
	        document.getElementById("remUserDiv").style.display = 'block';
        	document.getElementById("queDetDiv").style.display = 'none';
	}
	else if(choice == 5)
	{
		document.getElementById("addBookDiv").style.display = 'none';
	        document.getElementById("updateBookDiv").style.display = 'none';
        	document.getElementById("removeBookDiv").style.display = 'none';
	        document.getElementById("remUserDiv").style.display = 'none';
	        document.getElementById("queDetDiv").style.display = 'block';
	}
	else
	{
		document.getElementById("addBookDiv").style.display = 'none';
                document.getElementById("updateBookDiv").style.display = 'none';
                document.getElementById("removeBookDiv").style.display = 'none';
                document.getElementById("remUserDiv").style.display = 'none';
                document.getElementById("queDetDiv").style.display = 'none';
	}
}

$(document).on("click","#addBookSubmit",function(event) {
	var title = document.getElementById("addBookTitle").value;
	if(title == "" || title == null)
	{
		alert("Enter the book title");
	}
	else
	{
		var isbnNo = document.getElementById("addBookNo").value;
		if(isbnNo == "" || isbnNo == null)
		{
			alert("Enter an ISBN Number");
		}
		else
		{
			var author = document.getElementById("addBookAuthor").value;
			if(author == "" || author == null)
			{
				alert("Enter the name of the author");
			}
			else
			{
				var edition = document.getElementById("addBookEdition").value;
				if(edition == "" || edition == null)
				{
					alert("Enter a valid Edition");
				}
				else
				{
					var noOfCopies = document.getElementById("addBookCopies").value;
					if(noOfCopies == "" || noOfCopies == null || noOfCopies < 0)
					{
						alert("Entera a valid number of copies");
					}
					else
					{
						addBook(title,isbnNo,author,edition,noOfCopies);
					}
				}
			}
		}
	}
	event.preventDefault();
});

function addBook(title,isbnNo,author,edition,noOfCopies)
{
	var flag = 1;
	$.post("../adminOperations",{flag : flag,title : title,isbn : isbnNo, author : author, edition : edition, copies : noOfCopies},function(responseJson) {
		var status = responseJson["status"];
		alert(status);
	});
}

$(document).on("change","#adminUpdateBook", function(event) {
	if(document.getElementById("adminUpdateBook").checked)
	{
		setAdminDisplay(2);
		var flag = 2;
        	$.post("../adminOperations",{flag : flag}, function(responseJson) {
                	var books = responseJson["books"];
			displayBooks(books,"update");
		});
	}
	event.preventDefault();
});

$(document).on("change","#adminRemoveBook", function(event) {
	if(document.getElementById("adminRemoveBook").checked)
	{
		setAdminDisplay(3);
		var flag = 2;
		$.post("../adminOperations",{flag : flag} ,function(responseJson) {
			var books = responseJson["books"];
			displayBooks(books,"remove");
		});
	}
	event.preventDefault();
});

function displayBooks(books,oper)
{
	var table = "<table id='bookList' cellspacing='0' cellpadding='0' class='outputTable' class='outputTable'>";
	var colspan = 7;
	if(oper == "update")
	{
		colspan = 8;	
	}
	table += "<tr class='heading'><td colspan='"+colspan+"'>"+oper.toUpperCase()+" Books</td></tr>";
	table += "<tr class='heading'><td> S.No. </td><td> ISBN No. </td><td> Title </td><td> Author </td><td> Edition </td><td> Existing Copies </td>";
	if(oper == "update")
	{
		table += "<td>New Copies</td>";
	}
	table += "<td>"+oper.toUpperCase()+"</td></tr>";
	for(var i=0;i<books.length;i++)
        {
       		var curBook = books[i];
		table += "<tr><td>"+(i+1)+"</td><td>"+curBook[0]+"</td><td>"+curBook[1]+"</td><td>"+curBook[2]+"</td><td>"+curBook[3]+"</td><td>"+curBook[4]+"</td>";
		if(oper == "update")
		{
			table += "<td><input type='text' id="+curBook[0]+"copies /></td>";
		}
		table += "<td><button class='modifyButton' onclick=modifyCurBook('"+curBook[0]+"','"+oper+"') >"+oper.toUpperCase()+"</button></td></tr>";
	}
        table += "</table>";
	$("#"+oper+"BookDiv").empty();
	$("#"+oper+"BookDiv").append(table);
}

function modifyCurBook(isbn,oper)
{
	if(oper == "update")
	{
		updateBook(isbn);
	}
	else
	{
		removeBook(isbn);		
	}
}

function updateBook(isbn)
{
	var flag = 3;
	var value = document.getElementById(isbn+"copies").value;
	if(value == null || value == "")
	{
		alert("Enter a value");
	}
	else
	{
		$.post("../adminOperations",{flag : flag, isbn : isbn, value : value}, function(responseJson) {
			var books = responseJson["books"];
			displayBooks(books,"update");
		});
	}	
}

function removeBook(isbn)
{
	var flag = 4;
	$.post("../adminOperations",{flag : flag, isbn : isbn}, function(responseJson) {
		var status = responseJson["status"];
		var books = responseJson["books"];
		if(status == "failed")
		{
			alert("Cannot remove the book as it is been borrowed at the moment");
		}
		displayBooks(books,"remove");
	});
}

$(document).on("change","#adminRemUser",function(event) {
	if(document.getElementById("adminRemUser").checked)
	{
		setAdminDisplay(4);
		var flag = 5;
		$.post("../adminOperations",{flag : flag}, function(responseJson) {
			var users = responseJson["users"];
			displayUsers(users);
		});
	}
	event.preventDefault();
});

function displayUsers(users)
{
	var table = "<table id='userDetailsTable' cellpadding='0' cellspacing='0'  class='outputTable' class='outputTable'>";
	table += "<tr class='heading'><td colspan=5>Registered Users</td></tr>";
	table += "<tr class='heading'><td> S.No. </td><td> User Name </td><td> Email </td><td> Phone Number </td><td> Remove </td></tr>";
	for(var i=0;i<users.length;i++)
	{
		var curUser = users[i];
		if(curUser[0] != "admin")
		{
			table += "<tr><td>"+(i+1)+"</td><td>"+curUser[0]+"</td><td>"+curUser[1]+"</td><td>"+curUser[2]+"</td>";
			table += "<td><button class='modifyButton' onclick=removeUser('"+curUser[0]+"')> Remove </button></td></tr>";
		}
	}
	table += "</table>";
	$("#remUserDiv").empty();
	$("#remUserDiv").append(table);
}

function removeUser(user)
{
	var flag = 6;
	$.post("../adminOperations",{flag : flag,user : user}, function(responseJson) {
		var status = responseJson["status"];
		var users = responseJson["users"];
		if(status == "failed")
		{
			alert("Cannot remove the user, as the user is yet to return book(s)");	
		}
		displayUsers(users);
	});
}

$(document).on("change","#adminQueDet",function(event) {
	if(document.getElementById("adminQueDet").checked)
	{
		setAdminDisplay(5);
		var flag = 7;
		$.post("../adminOperations",{flag : flag}, function(responseJson) {
			var queDetails = responseJson["queDetails"];
			displayQueDetails(queDetails);
		});	
	}
	event.preventDefault();
});

function displayQueDetails(queDetails)
{
	var table = "<table id='queDetailsTable' cellpadding='0' cellspacing='0'  class='outputTable'>";
	table += "<tr class='heading'><td colspan='4'> Que Details </td></tr>";
	table += "<tr class='heading'><td> S.No. </td><td> Book (ISBN no. : Title) </td><td> Users </td><td> Remove </td></tr>";
	var books = Object.keys(queDetails);
	for(var i=0;i<books.length;i++)
	{
		var users = queDetails[books[i]];
		var isbn = books[i].substring(0,books[i].indexOf(":")-1);
		table += "<tr><td rowspan="+users.length+">"+(i+1)+"</td><td rowspan="+users.length+">"+books[i]+"</td>";
		for(var j=0;j<users.length;j++)
		{
			table += "<td>"+users[j]+"</td>";
			table += "<td><button class='modifyButton' onclick=removeFromQue('"+isbn+"','"+users[j]+"')> Remove </td></tr>";
		}
	}
	table += "</table>";
	
	$("#queDetDiv").empty();
	$("#queDetDiv").append(table);	
}

function removeFromQue(isbn,user)
{
	var flag = 8;
	$.post("../adminOperations",{flag : flag, isbn:isbn, user:user}, function(responseJson) {
		var queDetails = responseJson["queDetails"];
		displayQueDetails(queDetails);
	});
}
