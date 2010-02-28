var panelOpen = false;
var total = 0;
var ignoreStart = false;
var maxAttachments = ${maxAttachments?default(0)};
var counter = 0;

<#if attachmentsEnabled>
	var template = "<div id='attach_#counter#'><table width='100%' class='gensmall'><tr><td>${I18n.getMessage("Attachments.filename")}</td>";
	template += "<td><input type='file' size='50' name='file_#counter#'></td></tr>";
	template += "<tr><td>${I18n.getMessage("Attachments.description")}</td>";
	template += "<td><input type='text' name='comment_#counter#' size='50'>";
	template += "&nbsp;&nbsp;<a href='javascript:removeAttach(#counter#)' class='gensmall'>[${I18n.getMessage("Attachments.remove")}]</a></td></tr>";
	template += "</table><div style='border-top: 1px dashed #000;'>&nbsp;</div></div>";

	function addAttachmentFields()
	{
		if (counter < maxAttachments) {
			var s = template.replace(/#counter#/g, total);
			$("#attachmentFields").append(s);
			$("#total_files").val(++total);

			counter++;

			defineAttachmentButtonStatus();
		}
	}

	function removeAttach(index)
	{
		$("#attach_" + index).empty();
		counter--;
		defineAttachmentButtonStatus();
	}

	function defineAttachmentButtonStatus()
	{
		var disabled = !(counter < maxAttachments);
		document.post.add_attach.disabled = disabled;
		document.post.add_attach.style.color = disabled ? "#cccccc" : "#000000";
	}
</#if>

<#if attachments?exists>
	var templateEdit = "<table width='100%'><tr><td class='row2 gen'>${I18n.getMessage("Attachments.filename")}</td>";
	templateEdit += "<td class='row2 gen'>#name#</td></tr>";
	templateEdit += "<tr><td class='row2 gen'>${I18n.getMessage("Attachments.description")}</td>";
	templateEdit += "<td class='row2' valign='middle'><input type='text' size='50' name='edit_comment_#id#' value='#value#'>";
	templateEdit += "&nbsp;&nbsp;<span class='gensmall'><input type='checkbox' onclick='configureAttachDeletion(#id#, this);'>${I18n.getMessage("Attachments.remove")}</span></td></tr>";
	templateEdit += "<tr><td colspan='2' width='100%' class='row3'></td></tr></table>";
	
	function editAttachments()
	{
		var data = new Array();
		<#list attachments as a>
			var attach_${a.id} = new Array();

			attach_${a.id}["filename"] = "${a.info.realFilename}";
			attach_${a.id}["description"] = "${a.info.comment}";
			attach_${a.id}["id"] = "${a.id}";

			data.push(attach_${a.id});
		</#list>
		
		counter = data.length;
		<#if attachmentsEnabled>defineAttachmentButtonStatus();</#if>
		
		for (var i = 0; i < data.length; i++) {
			var a = data[i];
			var s = templateEdit.replace(/#value#/, a["description"]);
			s = s.replace(/#name#/, a["filename"]);
			s = s.replace(/#id#/g, a["id"]);

			var v = document.getElementById("edit_attach").innerHTML;
			v += s;
			document.getElementById("edit_attach").innerHTML = v;
			document.post.edit_attach_ids.value += a["id"] + ",";
		}
	}

	function configureAttachDeletion(id, f)
	{
		if (f.checked) {
			document.post.delete_attach.value += id + ",";
		}
		else {
			var p = document.post.delete_attach.value.split(",");
			document.post.delete_attach.value = "";
			for (var i = 0; i < p.length; i++) {
				if (p[i] != id) {
					document.post.delete_attach.value += p[i] + ",";
				}
			}
		}
	}
</#if>