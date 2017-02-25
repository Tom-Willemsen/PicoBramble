var api_location = window.location.protocol + '//' + window.location.hostname + ":5802/api/";

var total_job_slots = "total_job_slots"
var available_job_slots = "available_job_slots"
var total_nodes = "total_nodes"
var total_jobs = "total_jobs"
var jobs_in_progress = "jobs_in_progress"
var completed_jobs = "completed_jobs"
var max_temperature = "max_temperature"
var avg_temperature = "avg_temperature"
var min_temperature = "min_temperature"
var max_cpu_speed = "max_cpu_speed"
var avg_cpu_speed = "avg_cpu_speed"
var min_cpu_speed = "min_cpu_speed"
var log_messages = "log_messages"
var name = "name"

function update(field, not_available_text = "N/A"){
	$.ajax({ type: "GET",   
		url: api_location + field,   
		async: true,
		success: function (result) {
			
			if(document.getElementById(field).innerHTML === result){
				return;
			}
			
			document.getElementById(field).innerHTML = result;
			
			if(field === log_messages){
				scrollLogMessagesToBottom();
			}
			
		},
		error: function (data, textStatus, jqXHR){
			document.getElementById(field).innerHTML = not_available_text;
		}
	  }).responseText;
}

function updateAll(){
	update(total_job_slots);
	update(available_job_slots);
	update(total_nodes);
	update(total_jobs);
	update(jobs_in_progress);
	update(completed_jobs);
	update(max_temperature);
	update(avg_temperature);
	update(min_temperature);
	update(log_messages, "Log messages (disconnected)");
	update(max_cpu_speed);
	update(avg_cpu_speed);
	update(min_cpu_speed);
	update(name, "Cluster (disconnected)");
}

function scrollLogMessagesToBottom(){
	var elem = document.getElementById(log_messages);
	elem.scrollTop = elem.scrollHeight;
}

updateAll();
setInterval(function(){updateAll()}, 2000);