package core;
/**
 * This Class is used to encapsulate methods that are used by the entire program
 * and are all declared Statics
 * 
 * Nicolò Paternoster - Francesco Tudisco - Giorgio Natili
 * 2007
*/


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

public class Functions
{
	 public static File getRemoteFile(String fileName) {
		 	mkDir(Globals.XML_DIR);
		    String FilePos = Globals.XML_DIR +"/"+ getFilenameFromUrl(fileName);		    
	        try {
	            PrintWriter out = new PrintWriter(FilePos);
	            URL url = new URL(fileName);
	            URLConnection ex = url.openConnection();
	            BufferedReader in = new BufferedReader( new InputStreamReader( ex.getInputStream() ) );
	            String inputLine = ""; 
	            while ( ( inputLine = in.readLine() ) != null ) { 
	                out.println( inputLine );	 
	            }
	            out.flush();
	            out.close();
	           
	            in.close(); 
	        } catch( Exception e ) {
	        	e.printStackTrace();
	        	return null;
	        }
	        
	        return new File(FilePos);
	 }
 
    //Returns the filename starting from an url
	 public static String getFilenameFromUrl(String url)
	 {
	 		int ind = 0;
	 		
	 		if(url.contains("/"))
	 			ind = url.lastIndexOf("/");
	 		else if(url.contains("\\"))
	 			ind = url.lastIndexOf("\\");
	 			
	 		String name = url.substring(ind+1, url.length());
	 		
	 		return name;
	 }

	//Returns true if connected to internet
	//tring to ping the m.i.t. website
	public static boolean isConnected()
	{
		 return Globals.connected ;
	}
	
	/*public static void msg(String str)
	{
		System.out.println(">> Debug : " + str);
	}
*/
	public static boolean mkDir(String path)
	{
		File newdir = new File(path);
		
		if(!newdir.exists()){
			if(newdir.mkdir()){
				return true;
			}
		}
		return false;
	}
	
	
	public static void addItemsToRss(Vector items, String fileName){
		try{
			for(int i=0; i<items.size(); i++){
				
			}
			
		}catch(Exception e){}
	}

    public static boolean isWindowsPlatform()
    {
        String os = System.getProperty("os.name");
        if ( os != null && os.startsWith("Windows"))
            return true;
        else
            return false;
    }
    
    public static boolean isMacPlatform()
    {
        String os = System.getProperty("os.name");
        if ( os != null && os.startsWith("Mac"))
            return true;
        else
            return false;
    }
    
	public static void launchBrowser(String url)
	{
		BrowserControl.displayURL(url);
	}
	
	public static String parse(Item it){
		String curPath = new File(Globals.rssTHEME).getAbsolutePath();
		String tp = getFilenameFromUrl(curPath);
		curPath = curPath.substring(0, curPath.indexOf(tp));
		String  str;
		str = "<html> \n <head>";
		str += getCssFromFile(Globals.rssTHEME);
		
		str+="</head>";
		str+="<body class='myBody'>";

			str+="<div class='channel_title'>\n";
			
			str+="<div class='item_logo'><img src='"+Globals.rssIcon+"' width='100px' height='102px'></div>\n";
			
			str+="<div class='item_sopra'>\n";
				str+="<div class='item_title'>"+it.getTitle()+ "</div>\n";
				
				str+="<div class='item_author'>Posted by "+it.getAuthor()+" on "+it.getPubdate()+"</div>\n";
				
				str+="<div class='item_link'><a href='"+it.getLink()+"'> Go to page</a></div>\n";
				str+="</div>\n";
			
			
			
				str+="<div class='item_sotto'>\n";
			
				str+="<div class='item_description'>\n";
				str+=it.getDescription();
				str+="</div>\n";
				
			str+="</div>\n";
			
		str+="</div>\n";
		str+="</div>\n";
		str+="</body>		</html>";
		
		scrivisufile(str);
		return str;
	}
	
	
	public static String getCssFromFile(String url){
		String str = new String("<style type='text/css'>");
		
		try{
			File f = new File(url);
			BufferedReader r = new BufferedReader(new FileReader(f));
			String line = new String();
			while((line = r.readLine()) !=null){
				str += line;
			}
			
			r.close();
		}catch (FileNotFoundException e){}
		 catch (IOException e){}
		 
		str += "</style>";
		//scrivisufile(str);
		return str;
	}
	
	public static void setFontSize()
	{
		if (isMacPlatform()) {  Globals.TABLE_FONT_SIZE = 10; }
		else { Globals.TABLE_FONT_SIZE = 8; ;}
		
	}
	
	public static void scrivisufile(String cosa)
	{
		String nomefile="dati.htm";
		try
		{
			FileWriter out = new FileWriter (nomefile);
			out.write(cosa);
			out.close();
		}
		catch (Exception ex){ex.printStackTrace();}
	}
	
	public static Channel lotToOne(Channel[] chs){
		  Channel list = new Channel();
		  
		  for(int i=0; i<chs.length; i++){
		  for(int j=0; j<chs[i].size(); j++){
		  list.addItem(chs[i].getItemAt(j));
		  	}
		  }
		  
		  return list;
		  
		}
	
	public static Channel lotToOne(Channel uno, Channel due, Channel tre){
		  Channel C = uno;
		  C.add(due.getItems());
		  C.add(tre.getItems());
		  return C;
	}
	
	public static GregorianCalendar formatDate(String pubdate)
	  {
		 try{
				pubdate = pubdate.substring(0, pubdate.lastIndexOf(" "));
				
				String pubdateHours = pubdate.substring(pubdate.lastIndexOf(":") - 5,pubdate.length() );
				
				String pubdateDate = pubdate.substring(0, pubdate.lastIndexOf(":") -5);
				
				pubdate = pubdateHours +" @ "+ pubdateDate;
				
				return new DateWrapper(pubdate); 
			
		}
		catch(Exception e){return new GregorianCalendar(0,0,0);}
	 }
	
	public static String getFormatDate(GregorianCalendar gc){
			  String day = gc.get(Calendar.DAY_OF_MONTH)+"";
			  if(day.length()==1)day = "0"+day;
			  String month = (gc.get(Calendar.MONTH)+1)+"";
			  if(month.length()==1)month = "0"+month;
			  int year = gc.get(Calendar.YEAR);
			  String hour = gc.get(Calendar.HOUR_OF_DAY)+"";
			  
			  if(hour.length()==1)hour = "0"+hour;
			  String min = gc.get(Calendar.MINUTE)+"";
			  if(min.length()==1)min = "0"+min;
			  String sec = gc.get(Calendar.SECOND)+"";
			  if(sec.length()==1)sec = "0"+sec;
			  String time = hour+":"+min+":"+sec;
			  if(hour.equalsIgnoreCase("00") && min.equalsIgnoreCase("00") && sec.equalsIgnoreCase("00")){
				  return Globals.NO_DATE_STR;
			  }
			  return new String(day+"/"+month+"/"+year+" [at] "+time);
	}
	

}