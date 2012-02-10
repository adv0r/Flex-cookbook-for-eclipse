package core;

/**
 * This Class is used to encapsulate the Thread that performs the Update operations
 * 
 * Nicolò Paternoster - Francesco Tudisco - Giorgio Natili
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import flexrss_1.views.rssView;


public class RssDownThread implements Runnable {
	private Thread th = null;
	private boolean once;
	private rssView plugin_rss_view;
	
	public RssDownThread(boolean once, rssView plugin_rss_view){
		Globals.rssTook = false;
		this.once = once;
		this.plugin_rss_view = plugin_rss_view;
		start();
	}
	public RssDownThread(int time){
		Globals.dwn_time = time;
		start();
	}
	
	public void start(){
		if(th == null){
			th = new Thread(this);
			th.start();
			
		}
	}
	
	public void run(){
		
		while(th!=null){
			if(Functions.isConnected()){
				try{
					
					getRemoteFile(Globals.RSS_COMMENTS_URL);
					
					getRemoteFile(Globals.RSS_POSTS_URL);
					
					getRemoteFile(Globals.RSS_EDITS_URL);
					th.sleep(1000);
					plugin_rss_view.channelsUpdate();
					th.sleep(Globals.dwn_time);
				}
				catch (Exception e){}
				
			}
			if(once)th = null;
		}
		
	}
	
	public void setDownTime(int time){
		Globals.dwn_time = time;
	}
	
	
	public File getRemoteFile(String fileName) throws Exception{
		Channel v1=null, v2=null;
	 	Functions.mkDir(Globals.XML_DIR);
	    String FilePos = Globals.XML_DIR +"/"+ Functions.getFilenameFromUrl(fileName)+"_sbrl.xml";
	    URL url = new URL(fileName);

	    Functions.mkDir(Globals.TMP_DIR);

	    
	    String tmp_path = new String(Globals.XML_DIR+"/tmp/tmp_"+Functions.getFilenameFromUrl(fileName));
	    boolean xmlExists = new File(FilePos).exists();
  
        if(xmlExists){
        	v1 = new ParserHtml(Functions.getFilenameFromUrl(fileName)+"_sbrl.xml").getHandler().getChannel();
        }
  
        v2 = new ParserRss("tmp/tmp_"+Functions.getFilenameFromUrl(fileName)).getHandler().getChannel();
        if(v1!=null)
        	fondi(v1, v2);
        else v1 = new ParserRss("tmp/tmp_"+Functions.getFilenameFromUrl(fileName)).getHandler().getChannel();
        try{
        	new ParseWriter(Functions.getFilenameFromUrl(fileName)+"_sbrl.xml").xmlExport(v1);
        	
        	
        }catch(Exception e){e.printStackTrace();}
               
        return new File(FilePos);
	}
	
	public boolean snUguali(String[] uno, String[] due){
		for(int k=0; k<uno.length; k++){
			if(uno[k]!=due[k])
				return false;
		}
		return true;
	}
	
	public void fondi(Channel v1, Channel v2){
		int k = 0;
        ArrayList<Item> n = new ArrayList<Item>();
        
        while(v1.getItems().get(0).equals(v2.getItems().get(k))==false){
        	Item daAggiungere = v2.getItems().get(k);
        	daAggiungere.setVsito(false);
        	n.add(daAggiungere);     
        	k++;	
        }
        v1.add(n);
	}
	
	
	
	public boolean downloadRss(String path, URL url){
		 try {
			 PrintWriter out = new PrintWriter(path);
	            //URL url = new URL(fileName);
	            URLConnection ex = url.openConnection();
	            BufferedReader in = new BufferedReader( new InputStreamReader( ex.getInputStream() ) );
	            String inputLine = ""; 
	            while ( ( inputLine = in.readLine() ) != null ) { 
	                out.println( inputLine );	 
	            }
	            out.flush();
	            out.close();
	           
	            in.close(); 
	            return true;
	        } catch( Exception e ) {
	        	e.printStackTrace();
	        	return false;
	        }
	}
	
}




