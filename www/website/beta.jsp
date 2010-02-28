
<jsp:include page="header.jsp"/>
<!-- InstanceBeginEditable name="MainContent" -->
<div style="padding:10px; height:496px;" >
	<img src="images/bt_beta.gif">
	<div><img src="images/hr.gif" width="100%" height="10"></div>
	
	<p>
		The beta releases of JForum allow you to check out the latest available features and improvements, giving you a good
		idea of what's comming next. As the name implies, a <i>beta relaese</i> is still under development, and it is possible
		that you may find a feature that's not finished yet, for example.
	</p>
	
	<p>
		Also, the beta release <b>does not have the web installer enabled</b>, so you have to <a href="/install_no_wizard.jsp">install it manually</a>. Other
		advices and upgrade instructions are available at <a href="/doc/InstallingTheLatestBetaRelease">Installing the latest beta release</a>.
	</p>
	
	<div class="blue-title">Downloading the beta</div>
	<div><img src="images/hr.gif" width="100%" height="5"></div>
	<p><img src="images/orange_arrow.jpg" width="8" height="6">&nbsp;<a href="/beta/latest_dev.jsp" class="blue">Click here to download the latest beta of JForum</a></p>
	
	<p>
		Please keep in mind that beta releases are not destined to final users, but in fact to those who want to take advantage of a new feature or buf fix.
	</p>
	
	<p>
		<b><img src="images/orange_arrow.jpg" width="8" height="6">&nbsp;If you are a developer wanting to help JForum, please check the <a href="/development.jsp">development</a> section.</b>
	</p>
	
	<div class="blue-title">Running the upgrade scripts / updating the database schema</div>
	<div><img src="images/hr.gif" width="100%" height="5"></div>
	<p>
		Each new release of JForum may require changes to the database schema, due to changes in the object model. The <i>upgrade scripts</i> are usually located
		in the directory <i>upgrade/&lt;version&gt;</i>, and all you have to do is to run it on your database. Don't forget to first make a backup of your current
		database, in order to not lose anything. 
	</p>
	
	<p>
		For more detailed beta installining instructions, check the page <a href="/doc/UpgradingFrom2.1.6To2.1.7">installing the latest beta release</a>.
	</p>
</div>
<!-- InstanceEndEditable -->
<jsp:include page="bottom.jsp"/>