<#if !doc?exists>
	alert("${I18n.getMessage("No")}");
<#else>
	var message = "Post ID: ${doc.get("post.id")}\n";
	message += "Topic ID: ${doc.get("topic.id")}\n";
	
	var date = "${doc.get("date")}";
	date = date.substring(0, 4) + "/"
		+ date.substring(4, 6) + "/"
		+ date.substring(6, 8) + " "
		+ date.substring(8, 10) + ":"
		+ date.substring(10, 12) + ":"
		+ date.substring(12, 14);
		
	message += "Date (yyyy/MM/dd HH:mm:ss): " + date;
	
	alert(message);
</#if>