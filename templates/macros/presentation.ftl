<#-- ********************************************* -->
<#-- Displays the topic folder image by its status -->
<#-- ********************************************* -->
<#macro folderImage topic>
	<#if topic.movedId == 0 || (forum?exists && topic.forumId == forum.id)>
		<#if topic.read>
			<#if topic.status == STATUS_UNLOCKED>
				<#if topic.type == TOPIC_ANNOUNCE>
					<img class="icon_folder_announce" src="${contextPath}/images/transp.gif" alt="" />
				<#elseif topic.type == TOPIC_STICKY>
					<img class="icon_folder_sticky" src="${contextPath}/images/transp.gif" alt="" />
				<#else>
					<#if topic.isHot()>
						<img class="icon_folder_hot" src="${contextPath}/images/transp.gif" alt="" />
					<#else>
						<img class="icon_folder" src="${contextPath}/images/transp.gif" alt="" />
					</#if>
				</#if>
			<#else>
				<img class="icon_folder_lock" src="${contextPath}/images/transp.gif" alt="" />
			</#if>
		<#else>
			<#if topic.status == STATUS_UNLOCKED>
				<#if topic.type == TOPIC_ANNOUNCE>
					<img class="icon_folder_announce_new" src="${contextPath}/images/transp.gif" alt="" />
				<#elseif topic.type == TOPIC_STICKY>
					<img class="icon_folder_sticky_new" src="${contextPath}/images/transp.gif" alt="" />
				<#else>
					<#if topic.isHot()>
						<img class="icon_folder_new_hot" src="${contextPath}/images/transp.gif" alt="" />
					<#else>
						<img class="icon_folder_new" src="${contextPath}/images/transp.gif" alt="" />
					</#if>
				</#if>
			<#else>
				<img class="icon_folder_lock_new" src="${contextPath}/images/transp.gif" alt="" />
			</#if>
		</#if>
	<#else>
		<img class="icon_topic_move" src="${contextPath}/images/transp.gif" alt="" />
	</#if>
</#macro>

<#macro renderPoll poll>
	<table class="poll">
		<tr>
			<td colspan="4" class="strong" align="center">${poll.label}</td>
		</tr>
		<tr>
			<#list poll.options as option>
				<tr>
					<td>${option.text}</td>
					<td nowrap="nowrap" width="210">
						<img class="icon_vote_lcap" src="${contextPath}/images/transp.gif" alt="" /><img 
							src="${contextPath}/templates/${templateName}/images/voting_bar.gif" width="${option.votePercentage * 2}" height="12" alt="" /><img 
							class="icon_vote_rcap" src="${contextPath}/images/transp.gif" alt="" />
					</td>
					<td nowrap="nowrap" class="strong">${option.votePercentage}%</td>
					<td nowrap="nowrap">[ ${option.voteCount} ]</td>
				</tr>
			</#list>
		</tr>
		<tr>
			<td colspan="4" class="strong" align="center">${I18n.getMessage("PostShow.pollTotalVotes")} ${poll.totalVotes}</td>
		</tr>
	</table>
</#macro>

<#macro row1Class topic><#if topic.type == TOPIC_ANNOUNCE>row1Announce<#elseif topic.type == TOPIC_STICKY>row1sticky<#else>row1</#if></#macro>
<#macro row2Class topic><#if topic.type == TOPIC_ANNOUNCE>row2Announce<#elseif topic.type == TOPIC_STICKY>row2sticky<#else>row2</#if></#macro>
<#macro row3Class topic><#if topic.type == TOPIC_ANNOUNCE>row3Announce<#elseif topic.type == TOPIC_STICKY>row3sticky<#else>row3</#if></#macro>

<#-- ****************** -->
<#-- Moderation buttons -->
<#-- ****************** -->
<#macro moderationButtons>
	<#if moderator  && openModeration?default(false)>
		<#if can_remove_posts?default(false)><input type="submit" name="topicRemove" value="&nbsp;&nbsp;${I18n.getMessage("Delete")}&nbsp;&nbsp;" class="liteoption" onclick="return validateModerationDelete();" /></#if>
		<#if can_move_topics?default(false)><input type="submit" name="topicMove" value="&nbsp;&nbsp;${I18n.getMessage("move")}&nbsp;&nbsp;" class="liteoption" onclick="return verifyModerationCheckedTopics();" /></#if>
		<#if can_lockUnlock_topics?default(false)>
			<input type="submit" name="topicLock" value="&nbsp;&nbsp;${I18n.getMessage("Lock")}&nbsp;&nbsp;" class="liteoption" onclick="return lockUnlock();" />
			<input type="submit" name="topicUnlock" value="&nbsp;&nbsp;${I18n.getMessage("Unlock")}&nbsp;&nbsp;" class="liteoption" onclick="return lockUnlock();" />
		</#if>
	</#if>
</#macro>

<#-- ****************** -->
<#-- Moderation images -->
<#-- ****************** -->
<#macro moderationImages>
	<script type="text/javascript">
	function todo(name) { var todo = document.getElementById("moderationTodo"); todo.name = name; todo.value = "1"; }
	
	function deleteTopic() {
		if (confirm("${I18n.getMessage("Moderation.ConfirmDelete")}")
			&& askModerationReason()) {
			todo("topicRemove");
			document.formModeration.returnUrl.value = "${JForumContext.encodeURL("/forums/show/${topic.forumId}")}";
			document.formModeration.log_type.value = "1";
			document.formModeration.submit();
		}
	}

	function moveTopic() {
		todo("topicMove");
		document.formModeration.submit();
	}

	function lockUnlock(lock) {
		if (askModerationReason()) {
			document.formModeration.log_type.value = "3";
			todo(lock ? "topicLock" : "topicUnlock");
			document.formModeration.submit();
		}
	}
	</script>
	<#if isModerator || isAdmin>
		<#if can_remove_posts?default(false)>
			<a href="javascript:deleteTopic();"><img class="icon_topic_delete" src="${contextPath}/images/transp.gif" title="${I18n.getMessage("Delete")}" alt="" /></a>
		</#if>
		
		<#if can_move_topics?default(false)>
			<a href="javascript:moveTopic();"><img class="icon_topic_move" src="${contextPath}/images/transp.gif" title="${I18n.getMessage("move")}" alt="" /></a>
		</#if>

		<#if can_lockUnlock_topics?default(false)>			
			<#if topic.status == STATUS_LOCKED>
				<a href="javascript:lockUnlock(false);"><img class="icon_topic_unlock" src="${contextPath}/images/transp.gif" title="${I18n.getMessage("Unlock")}" alt="" /></a>
			<#else>
				<a href="javascript:lockUnlock(true);"><img class="icon_topic_lock" src="${contextPath}/images/transp.gif" title="${I18n.getMessage("Lock")}" alt="" /></a>
			</#if>
		</#if>
	</#if>
</#macro>

<#-- ********************** -->
<#-- Forum navigation combo -->
<#-- ********************** -->
<#macro forumsComboTable>
	<table cellspacing="0" cellpadding="0" border="0">
		<tr>			  
			<td nowrap="nowrap">
				<form action="" name="f" id="f" accept-charset="${encoding}">
					<span class="gensmall">${I18n.getMessage("ForumIndex.goTo")}:&nbsp;</span>
					<select onchange="if(this.options[this.selectedIndex].value != -1){ document.location = '${contextPath}/forums/show/'+ this.options[this.selectedIndex].value +'${extension}'; }" name="select">
						<option value="-1" selected="selected">${I18n.getMessage("ForumIndex.goToSelectAForum")}</option>				
						
						<#list allCategories as category>
                            <optgroup label="${category.name}">
		
							<#list category.getForums() as forum>
								<option value="${forum.id}">${forum.name}</option>
							</#list>
							
                            </optgroup>
						</#list>
					</select>
					&nbsp;
					<input class="liteoption" type="button" value="${I18n.getMessage("ForumIndex.goToGo")}" onclick="if(document.f.select.options[document.f.select.selectedIndex].value != -1){ document.location = '${contextPath}/forums/show/'+ document.f.select.options[document.f.select.selectedIndex].value +'${extension}'; }" />
				</form>
			</td>
		</tr>
	</table>
</#macro>
