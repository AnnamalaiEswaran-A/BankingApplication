

$(document).ready(function () {
    //	alert("hello");
    debugger;
    $('.login-form').hide();
    $('.otherOperations').hide();
    $('.userProfile').hide();

    if (sessionStorage.getItem("userDetails") != null) {
        debugger;
        $('.otherOperations').show();
        $('.userProfile').show();
        $('.login-form').hide();
        $('.signup-form').hide();
        showProfile();
    }
    $('.notification').hide();

    let allUsers = false;
    $('#viewRecords').click(function () {
        getAllUsers();
    });

    $('#viewHistory').click(function () {
        debugger;
        let currentUser = JSON.parse(sessionStorage.getItem("userDetails"));
        if (currentUser != null) {
            getHistory(currentUser.Id);
        } else {
            alert("Login to your account to see history");
        }

    });
    var users = [];

    function bindData(data) {
        debugger;
        let customerList = '';

        customerList = '<h3>All Users! </h3><hr /> <div class="allUsers">';
        data.forEach(customer => {
            customerList += `
            <div class="userDetails" id="userObject">
                <p id="userid">ID: ${customer.Id}</p>
                <p>Name: ${customer.name}</p>
                <p>Email: ${customer.email}</p>
                <p>Account Number: ${customer.acNo}</p>
                <p>Account Balance: ${customer.balance}</p>
             
               </div>
            `;
            users.push(customer);

        });
        customerList += '</div>'
        $('#userDetails').html(customerList);
    }
    function getAllUsers() {
        $.ajax({
            url: 'http://localhost:8080/NewServlet/ExampleServlet',
            method: 'GET',
            data: {
                action: "getAllUsers"
            },
            dataType: 'json',

            success: function (data) {
                bindData(data);
                if (!allUsers)
                    console.log(users);
                //alert("Users data fetched successfully");
               
				showNotifications("Success", "User data fetched successfully");
                // $('#userDetails').html(customerList);
                allUsers = true;
            },
            error: function (textStatus) {
                $('#userDetails').html(textStatus);
            }
        });
    }

	function showNotifications(header, msg){
		$('#notifyHeader').html(header);
		$('#notifyMsg').html(msg);
		$('.notification').show(100).delay(1200).hide(200);
	}
    $('#userObject').click(function () {
        alert($('#userid').val());
    });

    $('#login').submit(function (event) {
        event.preventDefault();
        login();

    });

    $('#signin').submit(function (event) {
        event.preventDefault();
        userCreation();
    });
    function login() {
        //alert($('#email').val() + " " + $('#password').val());
        $.ajax({
            url: 'http://localhost:8080/NewServlet/ExampleServlet',
            method: 'GET',
            data: {
                email: $('#email').val(),
                password: $('#password').val(),
                action: "login"
            },
            dataType: 'json',
            success: function (data) {
               // alert("success" + " " + JSON.stringify(data));
                debugger;
                if (data.Id != 0) {
                    sessionStorage.setItem("userDetails", JSON.stringify(data));
                    //alert("Welcome" + " " + data.name);
					showNotifications("Login Success", "Welcome " + data.name);
                    $('#email').val("");
                    $('#password').val("");
                    loginSuccess();

                } else {
                    alert("Email and password does not match. please try again");
                }

            },
            error(textStatus) {
                //console.log(textStatus + " " + errorThrown + " " + jqXHR);
                alert("Error thrown" + " " + textStatus);
            },
        });
    }

    function userCreation() {
        //alert($('#userName').val() + " " +  $('#email_id').val() + " " + $('#user_password').val());
        debugger;
        $.ajax({
            url: 'http://localhost:8080/NewServlet/ExampleServlet',
            method: 'POST',
            data: {

                name: $('#userName').val(),
                email: $('#email_id').val(),
                password: $('#user_password').val(),
                action: "UserCreation"
            },
            dataType: 'json',

            success: function (data) {
                debugger;
                alert(JSON.stringify(data));
                if (data.message == "Email already exists") {
                    alert("This email already exists");
                } else if(data.message == "Fill all neccessary details"){
					alert("Fill all neccessary details");
				}
                else if (data.name != null) {
                    //alert("User creation success!");
                    var user = {
                        name: data.name,
                        email: data.email,
                        password: data.password,
                        Id: data.Id,
                        balance: data.balance,
                        acNo: data.acNo
                    }
                    console.log(user);
                    sessionStorage.setItem("userDetails", JSON.stringify(user));
                    $('#userName').val("");
                    $('#email_id').val("");
                    $('#user_password').val("");
					showNotifications("Success", "User created successfully");
                    loginSuccess();

                } else {
                    alert(data.status);
                }
            },
            error(textStatus) {
                alert(textStatus);
            },
        });
    }
    $('#deposit').submit(function (event) {
        event.preventDefault();
        //alert("deposit");
        debugger;
        let currentUser = JSON.parse(sessionStorage.getItem("userDetails"));
        if (currentUser == null) {
            alert("Login or create account to deposit");
            return;
        }
        if ($('#depositAmount').val() <= 0) {
            alert("Deposit amount should be greater than 0");
        } else {
            let n = parseInt(currentUser.balance) + parseInt($('#depositAmount').val());
            let action = "Deposit";
            deposit(n, currentUser, $('#depositAmount').val(), action);

        }
    });

    $('#withdrawal').submit(function (event) {
        event.preventDefault();
        debugger;
        //alert("withdrawal");
        let currentUser = JSON.parse(sessionStorage.getItem("userDetails"));
        if (currentUser == null) {
            alert("Login or create account to withdrawal");
            return;
        }
        if ($('#withdrawalAmount').val() <= 0) {
            alert("withdrawal amount should be greater than 0");
        }
        //	else if(parseInt(currentUser.balance) - $('#withdrawalAmount').val() < 1000){
        //		alert("Insufficient balance. Minimum balance should be maintained ");
        //	}
        else {
            let n = parseInt(currentUser.balance) - parseInt($('#withdrawalAmount').val());
            let action = "withdrawal";
            deposit(n, currentUser, $('#withdrawalAmount').val(), action);

        }
    });
    $('#transfer').submit(function (event) {
        event.preventDefault();
        //alert("deposit");
        debugger;
        let currentUser = JSON.parse(sessionStorage.getItem("userDetails"));
        if (currentUser == null) {
            alert("Login or create account to transfer");
            return;
        }
        let n = parseInt(currentUser.balance) - parseInt($('#transferAmount').val());
        if ($('#accountNumber').val() == currentUser.acNo) {
            alert("Sender and receiver account number can't be same");
        }  else if ($('#transferAmount').val() <= 0) {
            alert("Transfer amount should be greater than 0");
        }
        else {
            amountTransfer(n, $('#accountNumber').val(), currentUser);
        }
    });
    function deposit(n, currentUser, balance, transcationAction) {
        debugger;
        // alert("deposit");
        $.ajax({
            url: 'http://localhost:8080/NewServlet/ExampleServlet',
            method: 'PUT',
            data: {
                id: currentUser.Id,
                amount: balance,
                balance: n,
                action: transcationAction
            },

            success: function (data) {
                debugger;
                // alert(JSON.stringify(data));
                if (data.message == "Transcation successfull") {
                    // alert("Transcation has been successsfull. Total balance = "+ " " +n);
                    currentUser.balance = n;
                    sessionStorage.removeItem("userDetails");
                    sessionStorage.setItem("userDetails", JSON.stringify(currentUser));
                    $('#depositAmount').val("");
                    $('#withdrawalAmount').val("");
					showNotifications("Success", "Amount " + transcationAction + " Successfully.<br> Total balance = " + n + "");
                   // showProfile();
                    $('#profileBalance').text('Balance:' + n);
                } else {
                    alert(data.message);
                }
            },
            error(textStatus) {
                alert(textStatus);
            }
        });
    }

    function amountTransfer(n, acNo, currentUser) {
		//alert("transfer");
        debugger;
        $.ajax({
            url: 'http://localhost:8080/NewServlet/ExampleServlet',
            method: 'POST',
            data: {
                id: currentUser.Id,
                amount: $('#transferAmount').val(),
                accNo: acNo,
                action: "transfer"
            },
            type: 'json',

            success: function (data) {
                debugger;
                // alert(JSON.stringify(data));
                if (data.message == "Success") {
                    // alert("Transcation has been successsfull. Total balance = "+ " " +n);
                    currentUser.balance = n;
                    sessionStorage.removeItem("userDetails");
                    sessionStorage.setItem("userDetails", JSON.stringify(currentUser));
                    $('#transferAmount').val("");
                    $('#accountNumber').val("");
					showNotifications("Success", "Amount Transcation Successfully.<br> Total balance = " + n + "");
                  //  showProfile();
                    $('#profileBalance').text('Balance:' + n);
                }else if(data.message == "insufficient"){
					alert("Insufficient balance. Minimum balance should be maintained");
				} 
					else {
                    alert(data.message);
                }
            },
            error(textStatus, errorThrown) {
                alert(textStatus + " " + errorThrown);
            }
        });
    }
    function getHistory(id) {
        debugger;
        $.ajax({
            url: 'http://localhost:8080/NewServlet/ExampleServlet',
            method: 'GET',
            data: {
                Id: id,
                action: "History"
            },
            type: 'json',

            success: function (data) {
                debugger;
                if (data.length == 0) {
                    alert("No record transcation record");
                    return;
                }
                // alert(JSON.stringify(data));
                let historyList = '';
                historyList = '<h3>Your transaction statement </h3><hr /> <div class="historyTable mr-2"><table> <tr><th class="mr-2">Transaction ID</th> <th class="mr-2">Transaction Type</th> <th class="mr-2">Transaction Amount</th> <th class="mr-2"> Account Balance</th><th class="mr-2">Created At</th></tr>';
                data.forEach(history => {
                    historyList += `	
	                    <tr class="mr-2 tableRow">
	                		<td> ${history.Id}</td>
	                		<td> ${history.type}</td>
	                		<td> ${history.amount}</td>
	                		<td> ${history.total_balance}</td>   
							<td> ${history.CreatedAt}</td>        
	                      </tr>
	                    `;
                });
                historyList += '</div> </table>'
                $('#historyDetails').html(historyList);
                alert("Transaction history fetched successfully");
            },
            error( textStatus, errorThrown) {
                alert(textStatus + " " + errorThrown);
            }
        });
    }

    $('#loginClicked').click(function () {
        //alert("clicked");
        showLogin();
    });
    $('#signinClicked').click(function () {
        showSignin();
    });
    $('#backToMsg').click(function () {
        showLogin();
    });
    $('#logout').click(function () {
        showSignin();
    });
	$('#editProfile').click(function(){
		editProfile();
	});

	function editProfile(){
		let user = JSON.parse(sessionStorage.getItem("userDetails"));
		alert(user.name);
		$('#userName').val(user.name);
		$('#email_id').val(user.email);
		$('#user_password').val(user.password);
		$('#signup-btn').text("Update");
		//alert($('#sign-btn').text());
		$('#profileHeader').text("Update profile information");
		$('#loginClicked').hide();
		showSignin();
	}

    function showLogin() {
        $('.login-form').show(200);
        $('.signup-form').hide();
        $('.otherOperations').hide();
        $('.userProfile').hide();
        $('#historyDetails').empty();
        //$('#historyDetails').hide();
        sessionStorage.removeItem("userDetails");
    }
    function showSignin() {
        $('.hideforms').hide();
        $('.signup-form').show(200);
        $('.otherOperations').hide();
        $('#historyDetails').html('<p></p>');
        //$('#historyDetails').hide();
        $('.userProfile').hide();
        sessionStorage.removeItem("userDetails");
    }


    function loginSuccess() {
        $('.hideforms').hide();
        $('.hideforms').hide();
        $('.otherOperations').show();
        $('.userProfile').show();
        $('#historyDetails').html('<p></p>');
		$('#depositAmount').val("");
		$('#withdrawalAmount').val("");
		$('#accountNumber').val("");
		$('#transferAmount').val("");
        showProfile();
    }

    function showProfile() {
    	//alert("show profile");
        debugger;
        let data = JSON.parse(sessionStorage.getItem("userDetails"));
        $('#profileName').text('Name:' + data.name);
        $('#profileAcno').text('Account No:' + data.acNo);
        $('#profileBalance').text('Balance:' + data.balance);
        $('#profileEmail').text('Email:' + data.email);
        //$('#profileName').html('<span> ' +data.name +' </span>');
    }

});