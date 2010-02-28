package net.jforum.tools.phpbb2jforum;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.jforum.util.DbUtils;
import net.jforum.util.preferences.SystemGlobals;

import org.apache.commons.lang.StringUtils;

/**
 * @author Rafael Steil
 * @version $Id: Main.java,v 1.12 2007/08/31 01:21:34 rafaelsteil Exp $
 */
public class Main
{
	private Connection conn;
	private Connection conn2;
	private String[][] regexps;

	public Main()
	{
		this.regexps = new String[][] { { ConfigKeys.B_REGEX, ConfigKeys.B_REPLACE },
			{ ConfigKeys.COLOR_REGEX, ConfigKeys.COLOR_REPLACE }, { ConfigKeys.I_REGEX, ConfigKeys.I_REPLACE },
			{ ConfigKeys.LIST_REGEX, ConfigKeys.LIST_REPLACE },
			{ ConfigKeys.QUOTE_REGEX, ConfigKeys.QUOTE_REPLACE },
			{ ConfigKeys.QUOTE_USERNAME_OPEN_REGEX, ConfigKeys.QUOTE_USERNAME_OPEN_REPLACE },
			{ ConfigKeys.QUOTE_USERNAME_CLOSE_REGEX, ConfigKeys.QUOTE_USERNAME_CLOSE_REPLACE },
			{ ConfigKeys.U_REGEX, ConfigKeys.U_REPLACE }, { ConfigKeys.IMG_REGEX, ConfigKeys.IMG_REPLACE },
			{ ConfigKeys.CODE_REGEX, ConfigKeys.CODE_REPLACE }, { ConfigKeys.SIZE_REGEX, ConfigKeys.SIZE_REPLACE } };
	}

	private Connection openConnection() throws ClassNotFoundException, SQLException
	{
		Class.forName(SystemGlobals.getValue(ConfigKeys.DATABASE_DRIVER));
		return DriverManager.getConnection(SystemGlobals.getValue(ConfigKeys.DATABASE_JFORUM_URL));
	}

	private void init(String baseDir) throws IOException
	{
		SystemGlobals.initGlobals(baseDir, baseDir + "/phpbb2jforum/resource/SystemGlobals.properties");
		SystemGlobals.loadQueries(baseDir + "/phpbb2jforum/resource/" + SystemGlobals.getValue(ConfigKeys.DATABASE_QUERIES));
	}

	private void runForrestRun() throws Exception
	{
		this.cleanTables();
		
		this.importUsers();
		this.importTables();
		this.importPrivateMessages();
		this.importPosts();
		
		this.setupPermissions();
	}

	private void importPosts() throws SQLException
	{
		int total = this.getTotalPosts();

		if (total == 0) {
			System.out.println("Seems like there are no posts to import. Skipping...");
			return;
		}
		
		System.out.println("Importing posts. This may take a looooong time...");
		System.out.println("Going to process " + total + " posts...");

		int counter = 0;

		Statement s = null;
		ResultSet rs = null;
		PreparedStatement insert = null;
		
		try {
			s = this.conn2.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			s.setFetchSize(50);
	
			insert = this.conn.prepareStatement(this.getSql(ConfigKeys.QUERY_POSTS_TEXT));
			rs = s.executeQuery(this.getSql(ConfigKeys.QUERY_SELECT_POSTS_TEXT));
			
			System.out.println("Ok, here we go");
			
			while (rs.next()) {
				if ((++counter % 100) == 0) {
					System.out.println("Processed " + counter + " posts so far");
				}
	
				insert.setInt(1, rs.getInt("post_id"));
				insert.setString(2, rs.getString("post_subject"));
				insert.setString(3, this.applyRegexToPostText(rs.getString("post_text")));
	
				insert.executeUpdate();
			}
		}
		finally {
			DbUtils.close(rs, insert);
			DbUtils.close(s);
		}
		
		System.out.println("Post importing done...");
	}

	private void importPrivateMessages() throws SQLException
	{
		System.out.println("Importing private messages...");

		Statement s = null;
		ResultSet rs = null;
		PreparedStatement insert = null; 

		try {
			insert = this.conn.prepareStatement(this.getSql(ConfigKeys.QUERY_PRIVMSGS_TEXT));
			s = this.conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			s.setFetchSize(50);
			
			rs = s.executeQuery(this.getSql(ConfigKeys.QUERY_SELECT_PM));
	
			while (rs.next()) {
				insert.setInt(1, rs.getInt("privmsgs_text_id"));
				insert.setString(2, this.applyRegexToPostText(rs.getString("privmsgs_text")));
	
				insert.executeUpdate();
			}
		}
		finally {
			DbUtils.close(rs, insert);
			DbUtils.close(s);
		}

		System.out.println("Private messages text imported...");
	}
	
	private void importUsers() throws SQLException
	{
		System.out.println("Importing users...");
		
		ResultSet rs = null;
		Statement s = null;
		PreparedStatement insert = null;

		try {
			insert = this.conn.prepareStatement(this.getSql(ConfigKeys.QUERY_USERS));
			s = this.conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			s.setFetchSize(50);
			
			rs = s.executeQuery(this.getSql(ConfigKeys.QUERY_SELECT_USERS));
			
			while (rs.next()) {
				insert.setInt(1, rs.getInt("user_id"));
				insert.setString(2, rs.getString("user_active"));
				insert.setString(3, rs.getString("username"));
				insert.setString(4, rs.getString("user_password"));
				insert.setString(5, rs.getString("user_regdate"));
				insert.setString(6, rs.getString("user_level"));
				insert.setString(7, rs.getString("user_posts"));
				insert.setString(8, rs.getString("user_timezone"));
				insert.setString(9, rs.getString("user_style"));
				insert.setString(10, "");
				insert.setString(11, rs.getString("user_dateformat"));
				insert.setString(12, rs.getString("user_new_privmsg"));
				insert.setString(13, rs.getString("user_unread_privmsg"));
				insert.setString(14, rs.getString("user_last_privmsg"));
				insert.setString(15, rs.getString("user_viewemail"));
				insert.setString(16, rs.getString("user_attachsig"));
				insert.setString(17, rs.getString("user_allowhtml"));
				insert.setString(18, rs.getString("user_allowbbcode"));
				insert.setString(19, rs.getString("user_allowsmile"));
				insert.setString(20, rs.getString("user_allowavatar"));
				insert.setString(21, rs.getString("user_allow_pm"));
				insert.setString(22, rs.getString("user_notify"));
				insert.setString(23, rs.getString("user_notify_pm"));
				insert.setString(24, rs.getString("user_popup_pm"));
				insert.setString(25, rs.getString("user_rank"));
				insert.setString(26, rs.getString("user_avatar"));
				insert.setString(27, rs.getString("user_avatar_type"));
				insert.setString(28, rs.getString("user_email"));
				insert.setString(29, rs.getString("user_icq"));
				insert.setString(30, rs.getString("user_website"));
				insert.setString(31, rs.getString("user_from"));
				insert.setString(32, this.applyRegexToPostText(rs.getString("user_sig")));
				insert.setString(33, rs.getString("user_aim"));
				insert.setString(34, rs.getString("user_yim"));
				insert.setString(35, rs.getString("user_msnm"));
				insert.setString(36, rs.getString("user_occ"));
				insert.setString(37, rs.getString("user_interests"));
				insert.setString(38, rs.getString("user_allow_viewonline"));
	
				insert.executeUpdate();
			}
		}
		finally {
			DbUtils.close(rs, insert);
			DbUtils.close(s);
		}
	}

	private String applyRegexToPostText(String text)
	{
		for (int i = 0; i < this.regexps.length; i++) {
			if (text == null) {
				text = "";
			}
			else {
				text = text.replaceAll(SystemGlobals.getValue(this.regexps[i][0]), 
					SystemGlobals.getValue(this.regexps[i][1]));
			}
		}

		return text;
	}

	private int getTotalPosts() throws SQLException
	{
		int total = 0;
		
		Statement s = null;
		ResultSet rs = null;

		try {
			s = this.conn.createStatement();
			rs = s.executeQuery(this.getSql(ConfigKeys.QUERY_TOTAL_POSTS));
	
			if (rs.next()) {
				total = rs.getInt(1);
			}
		}
		finally {
			DbUtils.close(rs, s);
		}

		return total;
	}

	private void cleanTables() throws SQLException
	{
		System.out.println("Cleaning tables...");

		String[] queries = { ConfigKeys.QUERY_CLEAN_BANLIST, ConfigKeys.QUERY_CLEAN_CATEGORIES, 
			ConfigKeys.QUERY_CLEAN_FORUMS, ConfigKeys.QUERY_CLEAN_POSTS, ConfigKeys.QUERY_CLEAN_POSTS_TEXT, 
			ConfigKeys.QUERY_CLEAN_PRIVMSGS, ConfigKeys.QUERY_CLEAN_PRIVMSGS_TEXT, 
			ConfigKeys.QUERY_CLEAN_RANKS, ConfigKeys.QUERY_CLEAN_SMILIES, ConfigKeys.QUERY_CLEAN_TOPICS, 
			ConfigKeys.QUERY_CLEAN_TOPICS_WATCH, ConfigKeys.QUERY_CLEAN_USERS, 
			ConfigKeys.QUERY_CLEAN_VOTE_DESC, ConfigKeys.QUERY_CLEAN_VOTE_RESULTS, 
			ConfigKeys.QUERY_CLEAN_VOTE_VOTERS, ConfigKeys.QUERY_CLEAN_GROUPS, ConfigKeys.QUERY_CLEAN_USERGROUPS  };

		for (int i = 0; i < queries.length; i++) {
			System.out.println("Cleaning " + queries[i]);

			Statement s = this.conn.createStatement();
			s.executeUpdate(this.getSql(queries[i]));
			s.close();
		}

		System.out.println("Tables cleaned...");
	}

	private void importTables() throws SQLException
	{ 
		String[][] queries = { { "categories", ConfigKeys.QUERY_CATEGORIES }, { "forums", ConfigKeys.QUERY_FORUMS },
			{ "private messages", ConfigKeys.QUERY_PRIVMSGS }, { "rankings", ConfigKeys.QUERY_RANKS },
			{ "topics", ConfigKeys.QUERY_TOPICS }, { "topics watch", ConfigKeys.QUERY_TOPICS_WATCH },
			{ "posts", ConfigKeys.QUERY_POSTS }, { "anonymous update", ConfigKeys.QUERY_UPDATE_ANONYMOUS }, 
			{ "banlist", ConfigKeys.QUERY_BANLIST }, { "Vote Desc", ConfigKeys.QUERY_VOTE_DESC }, 
			{ "Vote voters", ConfigKeys.QUERY_VOTE_VOTERS }, { "Vote results", ConfigKeys.QUERY_VOTE_RESULTS}, 
			{ "Groups", ConfigKeys.QUERY_GROUPS }, { "User groups", ConfigKeys.QUERY_USERGROUPS }, 
			{ "Anonymous user", ConfigKeys.QUERY_ANONYMOUSUSER_GROUP }, 
			{ "Admin group", ConfigKeys.QUERY_ADMINGROUP }, { "Admin role", ConfigKeys.QUERY_ADMINROLE } };

		for (int i = 0; i < queries.length; i++) {
			System.out.println("Importing " + queries[i][0] + "...");

			Statement s = null;
			
			try {
				s = this.conn.createStatement();
				s.executeUpdate(this.getSql(queries[i][1]));
			}
			finally {
				DbUtils.close(s);
			}
		}
	}
	
	private void setupPermissions() throws SQLException
	{
		Statement s = null;
		ResultSet rs = null;
		PreparedStatement p = null;
		
		try {
			s = this.conn.createStatement();
			rs = s.executeQuery(this.getSql(ConfigKeys.QUERY_MAXGROUPID));
			
			if (rs.next()) {
				int groupId = rs.getInt(1);
				
				p = this.conn.prepareStatement(this.getSql(ConfigKeys.QUERY_ADMINUSERGROUPS));
				p.setInt(1, groupId);
				p.executeUpdate();
				
			}
		}
		finally {
			DbUtils.close(rs, p);
			DbUtils.close(s);
		}
	}
	
	private String getSql(String queryName)
	{
		String query = SystemGlobals.getSql(queryName);
		
		query = StringUtils.replace(query, "${phpbb}", SystemGlobals.getValue(ConfigKeys.DATABASE_PHPBB));
		query = StringUtils.replace(query, "${table.prefix}", SystemGlobals.getValue(ConfigKeys.PHPBB_TABLE_PREFIX));
		
		return query;
	}

	public static void main(String[] args)
	{
		Main program = new Main();

		if (args.length != 1) {
			System.out.println("Usage: phpbb2jforum <base_directory>");
			System.out.println("Example: phpbb2jforum c:/jforum/tools \n");
			return;
		}

		try {
			program.init(args[0]);

			// We use autoCommit = true because if something wrong
			// happen, it's easier to just drop the database and create it again

			program.conn = program.openConnection();
			program.conn.setAutoCommit(true);

			// We need a second connection because the forward-only
			// query we use later will block until the query is
			// complete and so we can't write the to new table will
			// reading from the old
			program.conn2 = program.openConnection();
			program.conn2.setAutoCommit(true);

			long start = System.currentTimeMillis();
			program.runForrestRun();
			long end = System.currentTimeMillis() - start;

			System.out.println("\nDone!!!");
			System.out.println("Migration was performed in about " + (end / 1000) + " seconds ");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (program.conn != null) {
				try {
					program.conn.close();
				}
				catch (SQLException e) { }
			}
			if (program.conn2 != null) {
				try {
					program.conn2.close();
				}
				catch (SQLException e) { }
			}
		}
	}
}
