package core;

/**
 * This Class is used to encapsulate all the Fields that are needed by the all program
 * and are all declared Statics.
 * 
 * Nicolò Paternoster - Francesco Tudisco - Giorgio Natili
 * 2007
*/
import java.awt.Font;
import java.io.File;



public class Globals
{
	//	Remote url containing the addresses where to get Rss channel
	public static final String RSS_POSTS_URL = "http://rss.adobe.com/en/flex_cookbook_most_recent.rss";
	public static final String RSS_COMMENTS_URL = "http://rss.adobe.com/en/flex_cookbook_most_recent_comments.rss";
	public static final String RSS_EDITS_URL= "http://rss.adobe.com/en/flex_cookbook_most_recent_edits.rss";
	
	//	Used for identify the sort type in the Table
	public static final int SORT_BY_DATE = 1000;
	public static final int SORT_BY_TITLE = 100;
	public static final int SORT_BY_AUTHOR = 10;
	
	//	Title and some infos about Rss Channels
	public static final String RSS_POSTS = "[Flex Cookbook] most recent posts";
	public static final String RSS_COMMENTS = "[Flex Cookbook] most recent comments";
	public static final String RSS_EDITS = "[Flex Cookbook] most recent edits";
	public static final String RSS_WEBSITE = "http://rss.adobe.com/";
	
	//	Where to write the output Archives Files
	public static final String RSS_COMMENTS_FNAME = "flex_cookbook_most_recent_comments.rss_sbrl.xml";
	public static final String RSS_EDITS_FNAME = "flex_cookbook_most_recent_edits.rss_sbrl.xml";
	public static final String RSS_POSTS_FNAME = "flex_cookbook_most_recent.rss_sbrl.xml";
	
	//Hostname of the server that is used to check the connection ( with ping ) 
	//the M.I.T. encourages the use of this server
	public static final String RSS_IP = "216.104.208.201";
	public static final String ONLINE_CHECKER_HOST ="web.mit.edu";
	
	//	Directory Settings 
	public static String PLUGIN_DIR = getPluginDir();
	public static final String XML_DIR = PLUGIN_DIR+"xml_in";
	public static final String TMP_DIR = PLUGIN_DIR+XML_DIR+"/tmp";
	public static final String HTML_DIR = PLUGIN_DIR+"html_out";
	public static final String ICONS_DIR = PLUGIN_DIR+"icons/";
	
	//The location of the css files used for the rendering inside the
	//eclipse's browser
	public static String rssTHEME = PLUGIN_DIR+"theme/theme1.css";
	public static String rssIcon = "fx.png";
	
	
	// Some infos for the table rendering
	public static final String SEARCH_STR = "       Search       ";
	public static final String SEARCH_TITLE_STR = "Search Results looking trough all the three rss channels";
	public static final String NO_DATE_STR = "**wrong*date*format**";	
	public static  int TABLE_FONT_SIZE = 10;

	//Used for identify which rss are we using
	public static final int POSTS = 0;
	public static final int EDITS = 1;
	public static final int COMMENTS = 2;
	
	//	Used for identify which Table Column are selected
	public static final int AUTHOR_COL = 0;
	public static final int TITLE_COL = 1;
	public static final int DATE_COL = 2;
	
	//	What kind of icon there should be on the Table Title ( depends on the sort type)
	public static final int NO_ARROW = 0;
	public static final int UP_ARROW = 1;
	public static final int DN_ARROW = 2;

	//	Auto-Update Time in ms
	public static int dwn_time = (30)*(60)*(1000);
	
	//How often send a ping request to ONLINE_CHECKER_HOST 
	//to verify the connection
	public static final int PING_TIME = (100)*(1000);
	

	//Boolean fields used by the Threads during the operations
	public static boolean rssTook = false;
	public static boolean connected = false;
	public static boolean connectionDetection = false;
	
	//The default font used in the Table
	public static final Font DEF_FONT = new Font("Verdana",Font.BOLD,13);

	//	Used for the AutoUpdate
	public static RssDownThread currentDownThread;
	
	//	Used for the connection Check
	public static ConnectionThread currentConnectionThread;
	
	//The incons location
	public static final String ICON_RSS_FUXX = ICONS_DIR+"rss_fux.png";	
	public static final String ICON_RSS_BLUE = ICONS_DIR+"rss_blue.png";	
	public static final String ICON_RSS_GREEN = ICONS_DIR+"rss_green.png";
	public static final String ICON_RSS_SEARCH = ICONS_DIR+"zoom.png";	
	public static final String ICON_TABLE_AUTHOR = ICONS_DIR+"status_online.png";
	public static final String ICON_TABLE_TITLE = ICONS_DIR+"text_align_justify.png";
	public static final String ICON_TABLE_DATE = ICONS_DIR+"time.png";
	public static final String ICON_TABLE_AUTHOR_UP = ICONS_DIR+"status_online_up.png";
	public static final String ICON_TABLE_AUTHOR_DOWN = ICONS_DIR+"status_online_dn.png";
	public static final String ICON_TABLE_TITLE_UP = ICONS_DIR+"text_align_justify_up.png";
	public static final String ICON_TABLE_TITLE_DOWN = ICONS_DIR+"text_align_justify_dn.png";	
	public static final String ICON_TABLE_DATE_UP = ICONS_DIR+"time_up.png";
	public static final String ICON_TABLE_DATE_DOWN = ICONS_DIR+"time_dn.png";	
	public static final String ICON_OLD = ICONS_DIR+"bullet_black.png"; 
	public static final String ICON_NEW = ICONS_DIR+"bullet_blue.png";
	public static final String ICON_AVATAR = ICONS_DIR+"user_comment.png";
	public static final String ICON_ONLINE = ICONS_DIR+"world.png";
	public static final String ICON_OFFLINE = ICONS_DIR+"world_delete.png";
	public static final String ICON_REFRESH = ICONS_DIR+"refresh.png";
	
	//This methods return the PATH where the plugins is running!
	public static String getPluginDir(){
		String path = new File("rss").getAbsolutePath();
		int i = path.lastIndexOf("/");
		
		return path.substring(0, i+1);
	}
	
}
