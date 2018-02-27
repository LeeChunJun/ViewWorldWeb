/**
 * 
 */
function exitAccount() {
	$.get('http://localhost:8080/ViewWorldWeb/LogOutServlet', function() {
		window.location.reload();
	});
	return false;
}

$(function() {
	$("#search_btn").click(function() {
		var query = $("#search_input").val();
		postData = "query=" + query;
		window.location.href = "/ViewWorldWeb/IndexItemListServlet?"+postData;
		return false;
	});
});