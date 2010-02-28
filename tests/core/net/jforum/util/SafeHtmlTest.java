package net.jforum.util;

import junit.framework.TestCase;
import net.jforum.TestCaseUtils;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

/**
 * @author Rafael Steil
 * @version $Id: SafeHtmlTest.java,v 1.12 2007/09/19 14:08:56 rafaelsteil Exp $
 */
public class SafeHtmlTest extends TestCase
{
	private static final String WELCOME_TAGS = "a, b, i, u, img";
	private String input;
	private String expected;
	
	/** 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		TestCaseUtils.loadEnvironment();
		
		StringBuffer sb = new StringBuffer(512);
		sb.append("<a href='http://somelink'>Some Link</a>");
		sb.append("bla <b>bla</b> <pre>code code</pre>");
		sb.append("<script>document.location = 'xxx';</script>");
		sb.append("<img src='http://imgPath' onLoad='window.close();'>");
		sb.append("<a href='javascript:alert(bleh)'>xxxx</a>");
		sb.append("<img src='javascript:alert(bloh)'>");
		sb.append("<img src=\"&#106ava&#115cript&#58aler&#116&#40&#39Oops&#39&#41&#59\">");
		sb.append("\"> TTTTT <");
		sb.append("<img src='http://some.image' onLoad=\"javascript:alert('boo')\">");
		sb.append("<b>heeelooo, nurse</b>");
		sb.append("<b style='some style'>1, 2, 3</b>");
		this.input = sb.toString();
		
		sb = new StringBuffer(512);
		sb.append("<a href='http://somelink'>Some Link</a>");
		sb.append("bla <b>bla</b> &lt;pre&gt;code code&lt;/pre&gt;");
		sb.append("&lt;script&gt;document.location = 'xxx';&lt;/script&gt;");
		sb.append("<img src='http://imgPath' >");
		sb.append("<a >xxxx</a>");
		sb.append("<img >");
		sb.append("<img >");
		sb.append("&quot;&gt; TTTTT &lt;");
		sb.append("<img src='http://some.image' >");
		sb.append("<b>heeelooo, nurse</b>");
		sb.append("<b >1, 2, 3</b>");
		this.expected = sb.toString();
	}
	
	public void testJavascriptInsideURLTagExpectItToBeRemoved()
	{
		String input = "<a class=\"snap_shots\" rel=\"nofollow\" target=\"_new\" onmouseover=\"javascript:alert('test2');\" href=\"before\">test</a>";
		String expected = "<a class=\"snap_shots\" rel=\"nofollow\" target=\"_new\"  >test</a>";
		
		String result = new SafeHtml().ensureAllAttributesAreSafe(input);
		
		assertEquals(expected, result);
	}
	
	public void testJavascriptInsideImageTagExpectItToBeRemoved()
	{
		String input = "<img border=\"0\" onmouseover=\"javascript:alert('buuuh!!!');\"\"\" src=\"javascript:alert('hi from an alert!');\"/>";
		String expected = "<img border=\"0\" \"\" />";
		
		String result = new SafeHtml().ensureAllAttributesAreSafe(input);
		
		assertEquals(expected, result);
	}
	
	public void testIframe() 
	{
		String input = "<iframe src='http://www.google.com' onload='javascript:parent.document.body.style.display=\'none\'; alert(\'where is the forum?\'); ' style='display:none;'></iframe>";
		String output = "&lt;iframe src='http://www.google.com' onload='javascript:parent.document.body.style.display=\'none\'; alert(\'where is the forum?\'); ' style='display:none;'&gt;&lt;/iframe&gt;";
		
		SystemGlobals.setValue(ConfigKeys.HTML_TAGS_WELCOME, WELCOME_TAGS);
		assertEquals(output, new SafeHtml().makeSafe(input));
	}
	
	public void testMakeSafe() throws Exception
	{
		SystemGlobals.setValue(ConfigKeys.HTML_TAGS_WELCOME, WELCOME_TAGS);
		assertEquals(this.expected, new SafeHtml().makeSafe(this.input));
	}
}
