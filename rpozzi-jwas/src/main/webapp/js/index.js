/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
// JQuery Initialization
$(document).on("pagecreate", "#start", function(event) {
	$("#listaSpesaBtn").on("click", function() {
		handle("listaSpesa");
	});
	$("#userMgmtBtn").on("click", function() {
		handle("userMgmt");
	});
	$("#fileMgmtBtn").on("click", function() {
		handle("fileMgmt");
	});
});
$(document).on("pagecreate", "#userMgmt", function(event) {
	getAllUsers();
	/* ############### Binding events on user mgmt page */
	$("#addUser").on("click", function() {
		createUser($("#username").val(), $("#age").val(), $("#location").val());
	});
	/* ############### Binding events on delete user popup page */
	$("#deleteUser").on("click", function() {
		deleteUser(clickedId);
	});
});
$(document).on("pagecreate", "#listaSpesa", function(event) {
	getAllItems();
	$("#addItem").on("click", function() {
		createItem($("#itemInput").val());
	});
	/* ############### Binding events on delete item popup page */
	$("#deleteItem").on("click", function() {
		deleteItem(clickedId);
	});
});
/* ############### Binding events on dynamically generated <a href> elements */
$(document).on("click", "a.removable", function(event) {
	console.log("Binding events on dynamically generated <a href> elements");
	clickedId = $(this).attr("id");
});
$(document).on("pagecreate", "#fileMgmt", function(event) {
	// ################### jQuery Upload File plugin configuration - START
	var progressbar = $("#progressbar"), progressLabel = $(".progress-label");
	/*progressbar.progressbar({
		value : false,
	});*/
	var uploadObj = $("#fileuploader").uploadFile({
		url : "/upload",
		fileName : "file",
		autoSubmit : false,
		onSuccess : function(files, data, xhr) {
			// files: list of files
			// data: response from server
			// xhr : jquery xhr object
			console.log("############## RETURNED FROM UPLOAD = " + JSON.stringify(data));
			progressbar.addClass("displaynone");
		}
	});
	$("#startUpload").click(function() {
		progressLabel.text( "Uploading file, please wait ..." );
		progressbar.removeClass("displaynone");
		uploadObj.startUpload();
	});
	// ################### jQuery Upload File plugin configuration - END
	// =================== Dropzone configuration - START
	/* Configure programmatically */
	/*$("#dropzoneFileUpload").dropzone({
		url : "/files/upload",
		dictDefaultMessage : "Hey, drop it here"
	});*/
	/* Configure HTML element */
	/*Dropzone.options.dropzoneUploadFiles = {
		dictDefaultMessage : "Hey, drop it here"
	};*/
	// =================== Dropzone configuration - END
});
/* ############### Handle page forward logic */
function handle(data) {
	$.mobile.activePage.find('[data-role=panel]').panel('close');
	if (data == "listaSpesa") {
		var listaSpesaTitle = "LISTA DELLA SPESA";
		$("#listaSpesaTitle").html(listaSpesaTitle);
	}
	if (data == "userMgmt") {
		var userMgmtTitle = "GESTIONE UTENTI";
		$("#userMgmtTitle").html(userMgmtTitle);
	}
	if (data == "fileMgmt") {
		var fileMgmtTitle = "GESTIONE FILE";
		$("#fileMgmtTitle").html(fileMgmtTitle);
	}
}

var clickedId;
// ######################## USERS SERVICES - START ######################## 
function createUser(username, age, location){
	if (username=="" || age=="" || location=="") {
		alert("Fill all the fields");
		return;
	}
	var person = {name:username,age:age,location:location,phone:"(555) 234-4423"};
	$.ajax({
		url : "/users",
		type : "POST",
		dataType : "json",
		data : person,
		success : function(data) {
			// Person has been save successfully
		    console.log("########## Save successful, now get all users and refresh ...");
		    //refresh list
		    getAllUsers();
		},
		error : function(data) {
			// Handle Errors
	        console.log("********** ERROR CREATING OBJECT");
		}
	});
	//blank out input texts
    $("#username").val("");
    $("#age").val("");
    $("#location").val("");
};
function getAllUsers(){
	$.ajax({
		url : "/users",
		type : "GET",
		dataType : "json",
		success : function(data) {
			console.log("########## Done finding all");
			var users = "<li data-role=\"list-divider\" role=\"heading\">Lista Utenti</li>";
	        var usercount = 0;
	        var rows = data.rows;
	        for (var int = 0; int < rows.length; int++) {
	        	var currperson = rows[int].doc;
	        	var currid = currperson._id;
				users += "<li>" +
							"<a href=\"\">" + currperson.name + " age: " + currperson.age + " location: " + currperson.location + "</a>" +
							"<a href=\"#deleteUserPopup\" id=\"" + currid + "\" class=\"removable\" data-rel=\"popup\" data-position-to=\"window\" data-transition=\"pop\"></a>" +
						"</li>";
				usercount++;
			}
	        if (usercount > 0){
	        	$("#listOfUsers").html(users);
	        	$("#listOfUsers").listview( "refresh" );
	        } else {
	        	$("#users").html("There are currently no users on the IBM Bluemix cloud");
	        }
		},
		error : function(data) {
			//If an error occurs in any of the above requests.
	        console.error("********** An error occurred finding all users: " + data);
		}
	});
};
function getAllUsersWithName(username){
    /*var query = bluemixdata.Query.ofType("Person");
    //Find objects with the name and delete them.
    query.find({name: username}).done(function(users){
        users.forEach(function(curruser){
            console.log(curruser);
        });
    });*/
};
//get person by name and update them
function updateUser(username, newage){
    //console.log('########## about to update the person with name: ' + username + ' with age: ' + newage);
    /*var query = bluemixdata.Query.ofType("Person");
    var updatedPerson = {age:newage, name:username};
    query.find({name: username}).then(function(people){
        // Update the details.
        people.forEach(function(person){
        	person.set(updatedPerson);
        	person.save().then(function(savedPerson){
        		//console.log("########## successfully saved person: " + JSON.stringify(savedPerson));
        		//refresh list
                getAllUsers();
                return savedPerson.read();
            }).done(function(latest){
            	console.log("########## TESTING READ AFTER UPDATING INFO: " + JSON.stringify(latest));  
            });
        });
    });*/
};
function deleteUser(objId){
	//console.log("########## DELETE USER = " + objId);
	var resturl = "/users/" + objId;
	$.ajax({
		url : resturl,
		type : "DELETE",
		dataType : "json",
		success : function(data) {
			console.log("########## Done DELETE");
			//refresh list
            getAllUsers();
		},
		error : function(data) {
			//If an error occurs in any of the above requests.
	        console.error("********** An error occurred during DELETE: " + JSON.stringify(data));
		}
	});
};
function deleteUserByName(user2del){
    //console.log('########## about to delete user with name: ' + user2del);
    /*var query = bluemixdata.Query.ofType("Person");
    query.find({name: user2del}).then(function(people){
        // Update the details.
        people.forEach(function(person){
            person.del().done(function(deleted) {
                var isDeleted = deleted.isDeleted();
                //console.log("########## Object deleted? %s", isDeleted ? "yes" : "no");
                //refresh list
                getAllUsers();
            });
        });
    }, function(error){
    	//If an error occurs in any of the above requests.
    	console.error("********** An error occurred deleting the user: "+error);
    });*/
};
// ######################## USERS SERVICES - END ########################
// ######################## ITEMS SERVICES - START ########################
function createItem(itemName){
	if (itemName == "" ) {
		alert("Fill in item field");
		return;
	}
	var item = {name:itemName};
	$.ajax({
		url : "/items",
		type : "POST",
		dataType : "json",
		data : item,
		success : function(data) {
			// Item has been save successfully
		    console.log("########## Save successful, now get all items and refresh ...");
		    //refresh list
		    getAllItems();
		},
		error : function(data) {
			// Handle Errors
	        console.log("********** ERROR CREATING OBJECT");
		}
	});
	//blank out input texts
	$("#itemInput").val("");
};
function getAllItems(){
	$.ajax({
		url : "/rest/items",
		type : "GET",
		dataType : "json",
		success : function(data) {
			console.log("########## Done finding all");
			var items = "<li data-role=\"list-divider\" role=\"heading\">Lista Spesa</li>";
	        var itemcount = 0;
	        var rows = data.rows;
	        for (var int = 0; int < rows.length; int++) {
	        	var curritem = rows[int].doc;
				var currid = curritem._id;
				items += "<li>" +
							"<a href=\"\">" + curritem.name + "</a>" +
							"<a href=\"#deleteItemPopup\" id=\"" + currid + "\" class=\"removable\" data-rel=\"popup\" data-position-to=\"window\" data-transition=\"pop\"></a>" +
						"</li>";
				itemcount++;
			}
	        if (itemcount > 0){
	        	$("#listOfItems").html(items);
	        	$("#listOfItems").listview( "refresh" );
	        } else {
	        	$("#listOfItems").html("There are currently no items on the IBM Bluemix cloud");
	        }
		},
		error : function(data) {
			//If an error occurs in any of the above requests.
	        console.error("********** An error occurred finding all items: " + data);
		}
	});
};
function deleteItem(objId){
	//console.log("########## DELETE ITEM = " + objId);
	var resturl = "/items/" + objId;
	$.ajax({
		url : resturl,
		type : "DELETE",
		dataType : "json",
		success : function(data) {
			console.log("########## Done DELETE");
			//refresh list
            getAllItems();
		},
		error : function(data) {
			//If an error occurs in any of the above requests.
	        console.error("********** An error occurred during DELETE: " + JSON.stringify(data));
		}
	});
};
// ######################## ITEMS SERVICES - END ########################