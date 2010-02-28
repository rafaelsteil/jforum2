<jsp:include page="header.jsp"/>

<!-- InstanceBeginEditable name="MainContent" -->
<div style="padding:0px 10px">
	<img src="images/bt_install_config.gif">
		<div><img src="images/hr.gif" width="100%" height="10"></div>
		<p>In this section is shown how to configure and install JForum using the Web Wizard interface. 
		It is assumed that you have some knowledge on how to install/configure a Java servlet container 
		(or already have one up and running), and the database is properly configured.</p>
		
		<p><i>For manual installation instructions, check the <a class="blue" href="install_no_wizard.jsp">Installation & configuration - Manual Install</a> section.</i></p>
		<p>Note: These instructions are for the installation of JForum, release version 2.1.8. 
				Some of the steps here described may not be valid for older versions, which are no longer supported.
		</p>
		
		<!-- Downloading -->
		<div class="blue-title">Downloading JForum</div>
		<div><img src="images/hr.gif" width="100%" height="5"></div>
		<p>To get JForum, go to the <a class="blue" href="download.jsp">download page</a> and get the latest version.</p>
		
		<!-- Unpacking -->
		<div class="blue-title">Unpacking</div>
		<div><img src="images/hr.gif" width="100%" height="5"></div>
		<p>
			After the download, unpack the .ZIP file into your webapp's directory (or anyplace you want to put it). A directory named 
			<i>JForum-&lt;release&gt;</i> will be created, where &lt;release&gt; is the version, which may be "2.0", "2.1.7" etc... this it just for easy version identification. 
		</p>
		<p>
			You can rename the directory if you want. The next step you should do is register the JForum application within your Servlet Container, 
			like <a href="http://jakarta.apache.org/tomcat" class="blue">Tomcat</a>. This document will use the context name "jforum", but of course you can use any name you want.
		</p>
		
		<!-- Directory permissions -->
		<div class="blue-title">Directory permissions</div>
		<div><img src="images/hr.gif" width="100%" height="5"></div>
		<p>
			JForum expects that some directories are writable by the webserver. Before you start installing, please check if the following directories, 
			and their sub-directories, exist and have full write permissions: 
			
			<ul>
				<li>upload</li>
				<li>tmp</li>
				<li>images</li>
				<li>WEB-INF/config</li>
				<li>WEB-INF/jforumLuceneIndex</li>
			</ul>
		</p>
		
		<!-- Configuring -->
		<!-- Downloading -->
		<div class="blue-title">Configuring</div>
		<div><img src="images/hr.gif" width="100%" height="5"></div>
		<p>Next, point your browser to the following address: </p>
		<p><a href="http://localhost:8080/jforum/" class="blue">http://localhost:8080/<b>jforum</b>/install.jsp</a></p>
		<p>The bold text, "jforum", is the context's name. If you changed the context name you will need to change it here too. 
		After the page loads, you should see the screen shown by <i>Image 1</i>:</p>
		<p align="center">
			<img src="images/install_step_1.jpg"><br>
			<i>Image 1 - Welcome page</i>
		</p>
		<p>Please read carefully the fields' tips, since they contain valuable information. Below is a little explanation of each field:</p>
		
		<table align="center">
		<tr>
			<td>
				<table bgcolor="#3aa315" cellspacing="2" width="100%">
					<tr>
						<th width="30%" class="th-header">Field Name</th>
						<th class="th-header">Required</th>
						<th class="th-header">Description</th>
					</tr>
	
					<tr class="fields">
						<td>Default Board Language</td>
						<td align="center">Yes</td>
						<td>The language to display the messages in the forum. Note that, if the translation for some text is not available,
							an English text will be used</td>
					</tr>
	
					<tr class="fields">
						<td>Database Type</td>
						<td align="center">Yes</td>
						<td>The database server to use. If you don't know which one to choose, select "HSQLDB" from the list</td>
					</tr>
	
					<tr class="fields">
						<td>Installation Type</td>
						<td align="center">Yes</td>
						<td>Installation mode. Currently only "New Installation" is supported</td>
					</tr>
	
					<tr class="fields">
						<td>Connection type</td>
	
						<td align="center">Yes</td>
						<td>Which method to use to connect to database. <i>Native</i> will use regular connections, while <i>DataSource</i> will try
						to connect using the specified datasource name</td>
					</tr>
	
					<tr class="fields">
	
						<td>DataSource name</td>
						<td align="center">no</td>
						<td>If you chosen <i>DataSource</i> as Connection Type, then inform the name of the datasource</td>
					</tr>
	
					<tr class="fields">
	
						<td>Database Server Hostname</td>
						<td align="center">Yes</td>
						<td>The host where the database is located</td>
					
	
					</tr>
					
					<tr class="fields">
						<td>Database Name</td>
						<td align="center">Yes</td>
	
						<td>
							<p>Where the tables will be created. <b>Note that the database should already exist</b>.</p>
							<p>If you're going go use HSQLDB, no extra configuration is needed, since it is created on the fly</p>
							<p>If you are using Oracle, you probably will have to enter the SID here.</p>
						</td>
					</tr>
	
					<tr class="fields">
						<td>Database username</td>
						<td align="center">No*</td>
						<td>The user of your database instance. *This field is required for databases other than HSQLDB</td>
	
					</tr>
	
					<tr class="fields">
						<td>Database Password</td>
						<td align="center">No</td>
						<td>The database password, if any. Please note that errors may occur when not using any password</td>
					</tr>
	
					<tr class="fields">
						<td>Database Encoding</td>
						<td align="center">No</td>
						<td>The text encoding for the database. You can specify it by hand in the "Other" field</td>
					</tr>
	
					<tr class="fields">
						<td>Use Connection Pool</td>
	
						<td align="center">Yes</td>
						<td>In case of doubt, choose <b>No</b></td>
					</tr>
	
					<tr class="fields">
						<td>Forum Link</td>
						<td align="center">Yes</td>
	
						<td>The link to the forum context. If you are hosting it under some "real" domain and the context path is "/", 
							the "Forum Link" will be the address of your site, in most cases. 
						</td>
					</tr>
	
					<tr class="fields">
						<td>Website Link</td>
						<td align="center">Yes</td>
						<td>The link to your website.</td>
	
					</tr>
	
					<tr class="fields">
						<td>Administrator Password</td>
						<td align="center">Yes</td>
						<td>Type the password of the administrator (<i>Admin</i> user). </td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	
	<p>Now you can click the "Next Step "button. You will see the page shown in Image 2, which that contains a summary of your choices.</p>
	
	<p align="center">
		<img src="images/install_step_2.jpg"><br>
		<i>Image 2 - Checking the configuration before installation begins</i>
	</p>
	
	<p>If is everthing looks good, click on the "Begin Install" button. Note that if some warning message is shown, then there are some required steps that should be made before proceding with the installation.</p>
	<p>Wait until the installation ends. If no errors occur, then you will see a page like the one shown in Image 3</p>
	<p align="center">
		<img src="images/install_step_3.jpg"><br>
		<i>Image 3 - Installation Finished</i>
	</p>
	
	<p><b>Congratulations!</b> You have finished your JForum Installation. Before accessing the forum, do the actions pointed by the arrows, click on the check button and click on the button "Click Here to Access the Forum".</p>
	
	<!-- Administering -->
	<div class="blue-title">Administering the Forum</div>
	<div><img src="images/hr.gif" width="100%" height="5"></div>
	<p>Now you can login as <b>Admin</b> / &lt;the_password_you_set&gt; and click in the link "Admin Control Panel", at the end of the page. There you will be able to create Categories, Forums, Groups, and Users.
Don't forget to give write access to the webserver's user to the directories "images" and "tmp" ( as well from its subdiretories, if any ).</p>
</div>
<!-- InstanceEndEditable -->
<jsp:include page="bottom.jsp"/>