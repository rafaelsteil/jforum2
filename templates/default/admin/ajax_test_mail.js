$("#mailButton").val("${I18n.getMessage("Config.Form.SmtpTest")}").removeAttr("disabled");

<#if status != "OK">
	alert("${I18n.getMessage("Config.Form.SmtpTestFail")}: \n ${status}");
<#else>
	alert("${I18n.getMessage("Config.Form.SmtpTestSuccess")}");
</#if>
