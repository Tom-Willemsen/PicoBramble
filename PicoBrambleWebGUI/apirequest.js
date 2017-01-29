var api_location = window.location.protocol + '//' + window.location.hostname + ":5802/api/";
var not_available_text = "Not available"

var total_job_slots = "total_job_slots"
var available_job_slots = "available_job_slots"
var total_nodes = "total_nodes"

function updateTotalJobSlots(){
	$.ajax({ type: "GET",   
		url: api_location + total_job_slots,   
		async: true,
		success: function (result) {
			document.getElementById(total_job_slots).innerHTML = result;
		},
		error: function (data, textStatus, jqXHR){
			document.getElementById(total_job_slots).innerHTML = not_available_text;
		}
	  }).responseText;
}


function updateAvailableJobSlots(){
	$.ajax({ type: "GET",   
		url: api_location + available_job_slots,   
		async: true,
		success: function (result) {
			document.getElementById(available_job_slots).innerHTML = result;
		},
		error: function (data, textStatus, jqXHR){
			document.getElementById(available_job_slots).innerHTML = not_available_text;
		}
	  }).responseText;
}


function updateTotalNodes(){
	$.ajax({ type: "GET",   
		url: api_location + total_nodes,   
		async: true,
		success: function (result) {
			document.getElementById(total_nodes).innerHTML = result;
		},
		error: function (data, textStatus, jqXHR){
			document.getElementById(total_nodes).innerHTML = not_available_text;
		}
	  }).responseText;
}

function updateAll(){
	updateTotalJobSlots();
	updateAvailableJobSlots();
	updateTotalNodes();
}

updateAll();
setInterval(function(){updateAll()}, 2000);