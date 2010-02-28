<#include "header.htm"/>
<#import "../macros/pagination.ftl" as pagination/>
<#import "../macros/presentation.ftl" as presentation/>

<#assign canEditSomeMessage = false/>

<script type="text/javascript" src="${contextPath}/templates/${templateName}/js/jquery.js?${startupTime}"></script>
<script type="text/javascript" src="${contextPath}/templates/${templateName}/js/jquery.jeditable.pack.js?${startupTime}"></script>
<script type="text/javascript" src="${contextPath}/templates/${templateName}/js/post_show.js?${startupTime}"></script>
<script type="text/javascript" src="${contextPath}/templates/${templateName}/js/post.js?${startupTime}"></script>
<script type="text/javascript" src="${contextPath}/templates/${templateName}/js/pagination.js?${startupTime}"></script>

<#if logged>
	<script type="text/javascript" src="${contextPath}/templates/${templateName}/js/watch.js?${startupTime}"></script>
</#if>

<script type="text/javascript">
<!--
<#include "js/karma.js">
<#include "js/utils.js"/>

<#if canRemove || isModerator || isAdmin>
	function confirmDelete(postId)
	{
		if (confirm("${I18n.getMessage("Moderation.ConfirmPostDelete")}")) {
			var reason = prompt("${I18n.getMessage("ModerationLog.changeReason")}");

			if (reason == null || reason == "") {
				alert("${I18n.getMessage("ModerationLog.reasonIsEmpty")}");
				return false;
			}
			else {
				var link = document.getElementById("delete" + postId);
				link.href += "&log_description=" + encodeURIComponent(reason) + "&log_type=1";
			}

			return true;
		}
		
		return false;
	}
</#if>

-->
</script>

<#if moderator>
	<script type="text/JavaScript" src="${JForumContext.encodeURL("/js/list/moderation")}"></script>
</#if>

<table cellspacing="0" cellpadding="10" width="100%" align="center" border="0">
	<tr>
		<td class="bodyline">
			<table cellspacing="2" cellpadding="2" width="100%" border="0">
				<tr>
					<td valign="middle" align="left" colspan="2">
						<span class="maintitle"><a href="${JForumContext.encodeURL("/posts/list/${topic.id}")}" name="top" class="maintitle" id="top">${topic.title?html}</a></span>
						<#if rssEnabled>
						&nbsp;<a href="${contextPath}/rss/topicPosts/${topic.id}${extension}"><img src="${contextPath}/templates/${templateName}/images/xml_button.gif" border="0" alt="XML" /></a>
						</#if>
					</td>
				</tr>
			</table>
			
			<table cellspacing="2" cellpadding="2" width="100%" border="0">
				<tr>
					<#if !readonly>
					<td width="8%" align="left" valign="bottom" nowrap="nowrap">
					</#if>
						<#if topic.status == STATUS_LOCKED>
							<span class="icon_reply_locked"><img src="${contextPath}/images/transp.gif" alt="" /></span>
						<#else>
							<#if !readonly>
								<a href="${JForumContext.encodeURL("/posts/reply/${start}/${topic.id}")}" rel="nofollow" class="icon_reply nav"><img src="${contextPath}/images/transp.gif" alt="" /></a>
							<#else>
								<#assign colspan = "2"/>
							</#if>
						</#if>
					</td>

					<td valign="middle" align="left" colspan="${colspan?default("0")}">
						<span class="nav">
						<a class="nav" href="${JForumContext.encodeURL("/forums/list")}">${I18n.getMessage("ForumListing.forumIndex")} </a> 
            			&raquo; <a class="nav" href="${JForumContext.encodeURL("/forums/show/${forum.id}")}">${forum.name} </a></span>
					</td>
					<td valign="middle" align="right"><#assign paginationData><@pagination.doPagination "list", topic.id/></#assign>${paginationData}</td>
				</tr>
			</table>

			<table class="forumline" cellspacing="1" cellpadding="3" width="100%" border="0">
				<#if poll?exists>
					<tr>
						<td class="cathead cattitle" align="center" colspan="2" nowrap="nowrap" width="100%">${I18n.getMessage("PostShow.pollTitle")}</td>
					</tr>
					<tr>
						<td class="row1" colspan="2" align="center">
							<#if (poll.open && canVoteOnPoll && !request.getParameter("viewResults")?exists)>
								<form action="${JForumContext.encodeURL("/jforum")}" method="post">
									<input type="hidden" name="action" value="vote" />
									<input type="hidden" name="module" value="${moduleName}" />
									<input type="hidden" name="poll_id" value="${poll.id}" />
									<input type="hidden" name="topic_id" value="${topic.id}" />
									<div class="poll">
										<span class="strong">${poll.label?html}</span>
										<table class="poll">
										<#list poll.options as option>
											<tr>
												<td><input type="radio" name="poll_option" value="${option.id}">${option.text?html}</input></td>
											</tr>
										</#list>
										</table>
										<input type="submit" value="${I18n.getMessage("PostShow.pollVote")}"></input><br />
										<span class="gensmall" align="center"><a href="${JForumContext.encodeURL("/jforum${extension}?module=posts&amp;action=list&amp;topic_id=${topic.id}&amp;viewResults=true", "")}">${I18n.getMessage("PostShow.showPollResults")}</a></span>
									</div>
								</form>
							<#else>
								<@presentation.renderPoll poll/>
							</#if>
						</td>
					</tr>
				</#if>
				
				<tr>
					<th class="thleft" nowrap="nowrap" width="150" height="26">${I18n.getMessage("PostShow.author")}</th>
					<th class="thright" nowrap="nowrap" width="100%">${I18n.getMessage("PostShow.messageTitle")}</th>
				</tr>

				<!-- POST LISTING --> 
				<#assign rowColor = ""/>
				<#list posts as post>
					<#if post_index % 2 == 0>
						<#assign rowColor = "row1">
					<#else>
						<#assign rowColor = "row2">
					</#if>
	
					<#assign user = users.get(post.userId)/>
					<#assign canEditCurrentMessage = (post.canEdit && topic.status != STATUS_LOCKED) || moderatorCanEdit/>
					<tr>
						<td colspan="2">
							<#include "post_show_action_buttons_inc.htm"/>
						</td>
					</tr>

					<tr>
						<!-- Username -->
						<#assign rowspan = "3"/>
						<#assign useSignature = (user.attachSignatureEnabled && user.signature?exists && user.signature?length > 0 && post.isSignatureEnabled())/>

						<#if useSignature>
							<#assign rowspan = "3"/>
						<#else>
							<#assign rowspan = "2"/>
						</#if>

						<td class="${rowColor}" valign="top" align="left" rowspan="${rowspan}">
							<#include "post_show_user_inc.htm"/>
						</td>
		
						<!-- Message -->
						<td class="${rowColor}" valign="top" id="post_text_${post.id}">
							<span class="postbody">
								<#if canEditCurrentMessage>
									<#assign canEditSomeMessage = true/>
									<div class="edit_area" id="${post.id}">${post.text}</div>
								<#else>
									${post.text}
								</#if>
							</span>

							<!-- Attachments -->
							<#if post.hasAttachments() && (canDownloadAttachments || attachmentsEnabled)>
								<#assign attachments = am.getAttachments(post.id, post.forumId)/>

								<#include "post_show_attachments_inc.htm"/>
							</#if>

							<#if (post.editCount > 0) && post.editTime?exists>
								<#if post.editCount == 1>
									<#assign editCountMessage = "PostShow.editCountSingle"/>
								<#else>
									<#assign editCountMessage = "PostShow.editCountMany"/>
								</#if>
								
								<p><i><span class="gensmall">${I18n.getMessage(editCountMessage, [post.editCount, post.editTime?datetime?string])}</span></i></p>
							</#if>
						</td>
					</tr>

					<#if useSignature>
						<tr>
							<td colspan="2" class="${rowColor}" width="100%" height="28"><hr/><span class="gensmall">${user.signature}</span></td>
						</tr>
					</#if>
		
					<tr> 
						<td class="${rowColor}" valign="bottom" nowrap="nowrap" height="28" width="100%">
							<#include "post_show_user_profile_inc.htm"/>					
						</td>
					</tr>
		
					<tr>
						<td class="spacerow" colspan="2" height="1"><img src="${contextPath}/templates/${templateName}/images/spacer.gif" alt="" width="1" height="1" /></td>
					</tr>
				</#list>
				<!-- END OF POST LISTING -->
		
				<tr align="center">
					<td class="catbottom" colspan="2" height="28">
						<table cellspacing="0" cellpadding="0" border="0">
							<tr>
								<td align="center"><span class="gensmall">&nbsp;</span></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		
			<table cellspacing="2" cellpadding="2" width="100%" align="center" border="0">
				<tr>
					<#if !readonly>
					<td width="8%" align="left" valign="bottom" nowrap="nowrap">
					</#if>
					<#if topic.status == STATUS_LOCKED>
						<span class="icon_reply_locked"><img src="${contextPath}/images/transp.gif" alt="" /></span>
					<#else>
						<#if !readonly>
							<a href="${JForumContext.encodeURL("/posts/reply/${start}/${topic.id}")}" rel="nofollow" class="icon_reply nav"><img src="${contextPath}/images/transp.gif" alt="" /></a>
						<#else>
							<#assign colspan = "2"/>
						</#if>
		  			</#if>
					</td>
					
					<td valign="middle" align="left" colspan="${colspan?default("0")}">
						<span class="nav">
						<a class="nav" href="${JForumContext.encodeURL("/forums/list")}">${I18n.getMessage("ForumListing.forumIndex")} </a> 
            			&raquo; <a class="nav" href="${JForumContext.encodeURL("/forums/show/${forum.id}")}">${forum.name} </a>
						</span>
					</td>

					<td valign="middle" align="right">${paginationData}</td>
				</tr>
			</table>
			
			<table width="100%" align="center">
				
				<#if (logged || anonymousPosts) && topic.status != STATUS_LOCKED && !readonly>
					<tr>
						<td colspan="3">
					<script type="text/javascript">
					function newCaptcha()
					{
						document.getElementById("captcha_img").src = "${contextPath}/jforum${extension}?module=captcha&action=generate&timestamp=" + new Date().getTime();
					}
					
					function activateQuickReply()
					{
						$("#captcha_img").attr("src", "${JForumContext.encodeURL("/captcha/generate/${timestamp}")}");
						$("#quickReply").slideToggle('slow', function() {
							window.scrollBy(0, 1000);
						});
					}

					function validatePostForm(f)
					{
						if (f.message.value.replace(/^\s*|\s*$/g, "").length == 0) {
							alert("${I18n.getMessage("PostForm.textEmpty")}");
							f.message.focus();
						
							return false;
						}
					
						$("#icon_saving").css("display", "inline");
						$("#btnSubmit").attr("disabled", "disabled").val("${I18n.getMessage("PostForm.saving")}...");
					
						return true;
					}
					-->
					</script>

					
					<form action="${JForumContext.encodeURL("/jforum")}" method="post" name="post" id="post" onsubmit="return validatePostForm(this);" enctype="multipart/form-data" accept-charset="${encoding}">
						<input type="hidden" name="action" value="insertSave" />
						<input type="hidden" name="module" value="posts" />
						<input type="hidden" name="forum_id" value="${forum.id}" />
						<input type="hidden" name="start" value="${start?default("")}" />
						<input type="hidden" name="topic_id" value="${topic.id}" />
						<input type="hidden" name="disable_html" value="1" />
						<input type="hidden" name="quick" value="1" />
	
						<table width="100%">
							<tr>
								<td align="center">
									<img src="${contextPath}/templates/${templateName}/images/icon_mini_message.gif" align="middle" alt="Message" />
									<span class="nav"><a href="javascript:activateQuickReply()">${I18n.getMessage("PostForm.quickReply")}</a></span>
								</td>
							</tr>
						</table>
						
						<p align="center" style="display: none;" id="quickReply">
							<table>
								<tr>
									<td align="center">
										<textarea class="post" style="width: 350px" name="message" rows="10" cols="35" onkeyup="enterText(this);" onclick="enterText(this);" onselect="enterText(this);" onblur="leaveText();"></textarea>
									</td>
								</tr>
								<#if needCaptcha?default(false)>
									<tr>
										<td>
											<img border="0" align="middle" id="captcha_img"/>
											<br />
											<span class="gensmall">${I18n.getMessage("User.captchaResponse")}</span>
											<input type="text" class="post" style="width: 80px; font-weight: bold;" maxlength="25" name="captcha_anwser" /> 
											<br />
											<span class="gensmall">${I18n.getMessage("User.hardCaptchaPart1")} <a href="#newCaptcha" onClick="newCaptcha()"><b>${I18n.getMessage("User.hardCaptchaPart2")}</b></a></span>
										</td>
									</tr>
								</#if>
								<tr>
									<td align="right" valign="center">
										<input type="submit" id="btnSubmit" value="${I18n.getMessage("PostForm.submit")}" class="mainoption" />
										<img src="${contextPath}/images/transp.gif" id="icon_saving">
									</td>
								</tr>
							</table>
						</p>
					</form>
					</p>

						</td>
					</tr>
				</#if>
				
				<#if isModerator || isAdmin>
					<form action="${JForumContext.encodeURL("/jforum")}" method="post" name="formModeration" id="formModeration">
					<input type="hidden" name="action" value="doModeration" />
					<input type="hidden" name="module" value="moderation" />
					<input type="hidden" name="returnUrl" value="${JForumContext.encodeURL("/${moduleName}/${action}/${start}/${topic.id}")}" />
					<input type="hidden" name="forum_id" value="${topic.forumId}" />
					<input type="hidden" name="topic_id" value="${topic.id}" />
					<input type="hidden" name="log_type" value="0"/>
					<input type="hidden" name="log_description">
					<input type="hidden" id="moderationTodo" />

					<tr>
						<td align="left" colspan="3">
							<@presentation.moderationImages/>
						</td>
					</tr>
					</form>
				</#if>
			</table>

			<table cellspacing="0" cellpadding="0" width="100%" border="0">
				<tr>
					<td align="left" valign="top" class="gensmall">
						<#if logged>
							<#if bookmarksEnabled>
								<a href="javascript:addBookmark(2, ${topic.id});"><img src="${contextPath}/templates/${templateName}/images/icon_bookmark.gif" align="middle"  alt="XML" />&nbsp;${I18n.getMessage("Bookmarks.addTo")}</a>
								<br>
							</#if>
						
							<#if !watching>
								<#assign watchMessage = I18n.getMessage("PostShow.watch")/>
								<a href="#watch" onClick="watchTopic('${JForumContext.encodeURL("/posts/watch/${start}/${topic.id}")}', '${I18n.getMessage("PostShow.confirmWatch")}');">
							<#else>
								<#assign watchMessage = I18n.getMessage("PostShow.unwatch")/>
								<a href="${JForumContext.encodeURL("/posts/unwatch/${start}/${topic.id}")}">
							</#if>
							<img src="${contextPath}/templates/${templateName}/images/watch.gif" align="middle" alt="Watch" />&nbsp;${watchMessage}</a>
						</#if>
					</td>
					<td align="right"><@presentation.forumsComboTable/></td>
				</tr>
			</table>
		</td>
	</tr>
</table>

<a name="quick"></a>

<script type="text/javascript">
$(document).ready(function() {
	limitURLSize();

	<#if moderatorCanEdit || canEditSomeMessage>
		$(".edit_area").editable("${contextPath}/jforum${extension}?module=ajax&action=savePost", {
			submit: '${I18n.getMessage("Update")}',
			cancel: '${I18n.getMessage("cancel")}',
			type: 'textarea',
			tooltip: '${I18n.getMessage("PostShow.doubleClickEdit")}',
			rows: 15,
			width: '100%',
			event: 'dblclick',
			indicator: "<img src='${contextPath}/templates/${templateName}/images/indicator.gif'>",
			postload: '${contextPath}/jforum${extension}?module=ajax&action=loadPostContents',
			cssclass: 'inlineedit',
			loadtext: '${I18n.getMessage("PostShow.loading")}...',
			beforesubmit: function(submitdata) { 
				<#if moderationLoggingEnabled>
					var message = prompt("${I18n.getMessage("ModerationLog.changeReason")}");

					if (message == null || message == "") {
						alert("${I18n.getMessage("ModerationLog.reasonIsEmpty")}");
						return false;
					}
					else {
						submitdata["log_description"] = message;
						submitdata["log_type"] = 2;
					}
				</#if>

				return true;
			}
		}, function(s) {
			<#if hasCodeBlock>
				dp.sh.HighlightAll('code');
			<#else>
				if (s.indexOf("name=\"code\"") > -1) {
					document.location.reload(true);
				}
			</#if>
		});
	</#if>
});
</script>

<#include "bottom.htm"/>