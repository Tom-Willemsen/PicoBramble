var api_location = window.location.protocol + '//' + window.location.hostname + ":5802/api/";
var not_available_text = "Not available"

var total_job_slots = "total_job_slots"
var available_job_slots = "available_job_slots"
var total_nodes = "total_nodes"
var total_jobs = "total_jobs"
var jobs_in_progress = "jobs_in_progress"
var completed_jobs = "completed_jobs"
var max_temperature = "max_temperature"
var avg_temperature = "avg_temperature"

function update( field ){
	$.ajax({ type: "GET",   
		url: api_location + field,   
		async: true,
		success: function (result) {
			document.getElementById(field).innerHTML = result;
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
}

updateAll();
setInterval(function(){updateAll()}, 2000);