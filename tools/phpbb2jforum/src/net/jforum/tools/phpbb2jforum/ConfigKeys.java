package net.jforum.tools.phpbb2jforum;

/**
* Encapsulate all configuration keys in constants. This is more typesafe and provides
* a nice overview of all configuration keys. Last but not least this lets us autocomplete
* configuration keys under eclipse ;-)
* 
* @author Rafael Steil
* @version $Id: ConfigKeys.java,v 1.8 2007/08/31 01:21:34 rafaelsteil Exp $
*/

public class ConfigKeys {
	private ConfigKeys() { }

	public static final String INSTALLATION = "installation";

	public static final String INSTALLATION_CONFIG = "installation.config";
	public static final String DEFAULT_CONFIG = "default.config";

	public static final String RESOURCE_DIR = "resource.dir";
	public static final String CONFIG_DIR = "config.dir";
	
	public static final String DATABASE_DRIVER = "database.driver";
	public static final String DATABASE_JFORUM_URL = "database.jforum.url";
	public static final String DATABASE_PHPBB = "database.phpbb";
	public static final String DATABASE_QUERIES = "database.queries";
	public static final String PHPBB_TABLE_PREFIX = "phpbb.table.prefix";
	
	public static final String B_REGEX = "b.regex";
	public static final String U_REGEX = "u.regex";
	public static final String I_REGEX = "i.regex";
	public static final String QUOTE_REGEX = "quote.regex";
	public static final String QUOTE_USERNAME_OPEN_REGEX = "quote.username.open.regex";
	public static final String QUOTE_USERNAME_CLOSE_REGEX = "quote.username.close.regex";
	public static final String LIST_REGEX = "list.regex";
	public static final String COLOR_REGEX = "color.regex";
	public static final String SIZE_REGEX = "size.regex";
	public static final String IMG_REGEX = "img.regex";
	public static final String CODE_REGEX = "code.regex";
	
	public static final String B_REPLACE = "b.replace";
	public static final String I_REPLACE = "i.replace";
	public static final String U_REPLACE = "u.replace";
	public static final String QUOTE_REPLACE = "quote.replace";
	public static final String QUOTE_USERNAME_OPEN_REPLACE = "quote.username.open.replace";
	public static final String QUOTE_USERNAME_CLOSE_REPLACE = "quote.username.close.replace";
	public static final String LIST_REPLACE = "list.replace";
	public static final String COLOR_REPLACE = "color.replace";
	public static final String SIZE_REPLACE = "size.replace";
	public static final String IMG_REPLACE = "img.replace";
	public static final String CODE_REPLACE = "code.replace";
	
	public static final String QUERY_PRIVMSGS = "query.privmsgs";
	public static final String QUERY_PRIVMSGS_TEXT = "query.privmsgs.text";
	public static final String QUERY_CATEGORIES = "query.categories";
	public static final String QUERY_FORUMS = "query.forums";
	public static final String QUERY_POSTS = "query.posts";
	public static final String QUERY_POSTS_TEXT = "query.posts.text";
	public static final String QUERY_RANKS = "query.ranks";
	public static final String QUERY_TOPICS = "query.topics";
	public static final String QUERY_TOPICS_WATCH = "query.topics.watch";
	public static final String QUERY_USERS = "query.users";

	public static final String QUERY_TOTAL_POSTS = "query.totalposts";
	public static final String QUERY_SELECT_POSTS_TEXT = "query.select.poststext";
	public static final String QUERY_SELECT_PM = "query.select.pm";
	public static final String QUERY_SELECT_USERS = "query.select.users";
	
	public static final String QUERY_UPDATE_ANONYMOUS = "query.update.anonymous";
	
	public static final String QUERY_CLEAN_BANLIST = "query.clean.banlist";
	public static final String QUERY_CLEAN_CATEGORIES = "query.clean.categories";
	public static final String QUERY_CLEAN_FORUMS = "query.clean.forums";
	public static final String QUERY_CLEAN_GROUPS = "query.clean.groups";
	public static final String QUERY_CLEAN_POSTS = "query.clean.posts";
	public static final String QUERY_CLEAN_POSTS_TEXT = "query.clean.posts.text";
	public static final String QUERY_CLEAN_PRIVMSGS = "query.clean.privmsgs";
	public static final String QUERY_CLEAN_PRIVMSGS_TEXT = "query.clean.privmsgs.text";
	public static final String QUERY_CLEAN_RANKS = "query.clean.ranks";
	public static final String QUERY_CLEAN_SMILIES = "query.clean.smilies";
	public static final String QUERY_CLEAN_TOPICS = "query.clean.topics";
	public static final String QUERY_CLEAN_TOPICS_WATCH = "query.clean.topicswatch";
	public static final String QUERY_CLEAN_USERS = "query.clean.users";
	public static final String QUERY_CLEAN_USERGROUPS = "query.clean.usergroups";
	public static final String QUERY_CLEAN_VOTE_DESC = "query.clean.votedesc";
	public static final String QUERY_CLEAN_VOTE_RESULTS = "query.clean.voteresults";
	public static final String QUERY_CLEAN_VOTE_VOTERS = "query.clean.votevoters";

	public static final String QUERY_BANLIST = "query.banlist";
	public static final String QUERY_VOTE_DESC = "query.votedesc";
	public static final String QUERY_VOTE_RESULTS = "query.voteresults";
	public static final String QUERY_VOTE_VOTERS = "query.votevoters";
	public static final String QUERY_GROUPS = "query.groups";
	public static final String QUERY_USERGROUPS = "query.usergroups";
	public static final String QUERY_ANONYMOUSUSER_GROUP = "quer.anonymoususer.group";
	public static final String QUERY_ADMINGROUP = "query.admingroup";
	public static final String QUERY_ADMINROLE = "query.adminrole";
	public static final String QUERY_MAXGROUPID = "query.maxgroupid";
	public static final String QUERY_ADMINUSERGROUPS = "query.adminusergroups";
}
