$(document).on("click","#loginSubmit",function(event) {
//	alert("in here");
	var userName = document.getElementById("userName").value;
	if(userName == null || userName == "")
	{
		alert("Enter a User Name");
	}
	else
	{
		var password = document.getElementById("password").value;
		if(password == null || password == "")
		{
			alert("Enter the password");
		}
		else
		{
			validate(userName,password);	
		}
	}
	event.preventDefault();
});

function validate(userName,password)
{
	var flag =1;
	$.post("../validate",{flag : flag,userName : userName, password : password},function(responseJson) {
		var status = responseJson["status"];
		respond(status);
	});
}

function respond(status)
{
	if(status == "failed")
	{
		alert("Invalid Credentials");
	}
	else if(status == "admin")
	{
//		alert("in here too");
		setUpAdminOptions();	
	}
	else
	{
		setUpUserOptions();
	}
}

function setUpAdminOptions()
{
	document.getElementById("inputDiv").style.display = 'none';
	document.getElementById("responseDiv").style.display = 'block';
	document.getElementById("adminDiv").style.display = 'block';
	document.getElementById("userDiv").style.display = 'none';
	$('input[name="adminOptions"]').prop('checked',false);
	setAdminDisplay(0);
}

function setUpUserOptions()
{
	document.getElementById("inputDiv").style.display = 'none';
        document.getElementById("responseDiv").style.display = 'block';
        document.getElementById("adminDiv").style.display = 'none';
        document.getElementById("userDiv").style.display = 'block';
	$('input[name="userOptions"]').prop('checked',false);
	setUserDisplay(0);
	var user = document.getElementById("userName").value;
	setQueDetails(user);
}

function regLink()
{
	document.getElementById("loginDiv").style.display = 'none';
	document.getElementById("registerDiv").style.display = 'block';
}

$(document).on("click",".backButton",function(event) {
	backOperation();
	event.preventDefault();
});

function backOperation()
{
	document.getElementById("inputDiv").style.display = 'block';
	document.getElementById("loginDiv").style.display = 'block';
        document.getElementById("responseDiv").style.display = 'none';
        document.getElementById("registerDiv").style.display = 'none';
}

$(document).on("click","#registerSubmit",function(event) {
	var uname = document.getElementById("regUName").value;
	if(uname == null || uname == "")
	{
		alert("Enter an User Name");
	}
	else
	{
		var pass = document.getElementById("regPass").value;
		if(pass == null || pass == "")
		{
			alert("Enter a password");
		}
		else
		{
			var email = document.getElementById("regEmail").value;
			if(email == null || email == "")
			{
				alert("Enter an Email address");
			}
			else
			{
				var phno = document.getElementById("regPhone").value;
				if(phno == null || phno == "")
				{
					alert("Enter a Phone number");
				}
				else
				{
					register(uname,pass,email,phno);
				}
			}
		}
	}
	event.preventDefault();
});

function register(uname, pass, email, phno)
{
	var flag =2;
	$.post("../validate",{flag : flag, uname : uname, pass : pass, email : email, phno : phno} ,function(responseJson) {
		var status = responseJson["status"];
		if(status == "failed")
		{
			alert("User Name already exists");
		}
		else if(status == "success")
		{
			alert("Registration successfull");
			backOperation();		
		}
	});
}
