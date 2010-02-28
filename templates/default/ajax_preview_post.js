$("#previewSubject").html("${post.subject?html}");
$("#previewMessage").html("${post.text}");
$("#previewTable").show();

var s = document.location.toString();
var index = s.indexOf("#preview");

if (index > -1) {
	s = s.substring(0, index);
}

document.location = s + "#preview";

dp.sh.ClipboardSwf = '${contextPath}/templates/${templateName}/js/clipboard.swf';
dp.sh.HighlightAll('code');
