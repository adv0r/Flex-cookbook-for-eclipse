package core;
/**
 * In this class we perform the operations of output write 
 * 
 * Nicolò Paternoster - Francesco Tudisco - Giorgio Natili
*/
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;


public class ParseWriter {
	private String outputFileName;
	
	public ParseWriter(String to){
		outputFileName = (to);
	}
	
	
	public boolean htmlExport(Channel ch) {
		try{
			Functions.mkDir(Globals.HTML_DIR);
			File parsedFile = new File(Globals.HTML_DIR+"/"+outputFileName);
			FileOutputStream outStream = new FileOutputStream(parsedFile);
			PrintWriter writer = new PrintWriter(outStream);
			
			if(ch.size()>0){
				writer.println("<html>");
				writer.println("<head>");
				writer.println("<link href=\""+Globals.rssTHEME+"\" type=\"text/css\" rel=\"stylesheet\" media=\"screen\" />");
				writer.println("</head>");
				writer.println("<body class='myBody'>");
				
				writeMenu_html(writer);
				
				writeChannelInfo_html(ch, writer);
				
				writeChannelItems_html(ch, writer);
							
				writer.println("</body>");
				writer.println("</html>");
				
				writer.flush();
				writer.close();
			}
			return true;
		}catch (Exception e){return false;}
	}
	
	
	public boolean htmlExport(ArrayList<Channel> channels) {
		try{
			Functions.mkDir(Globals.HTML_DIR);
			File parsedFile = new File(Globals.HTML_DIR+"/"+outputFileName);
			FileOutputStream outStream = new FileOutputStream(parsedFile);
			PrintWriter writer = new PrintWriter(outStream);
			
			writer.println("<html>");
			writer.println("<head>");
			writer.println("<link href=\""+Globals.rssTHEME+"\" type=\"text/css\" rel=\"stylesheet\" media=\"screen\" />");
			writer.println("</head>");
			writer.println("<body class='myBody'>");
			
			writeMenu_html(writer);
			
			for(int k=0; k<channels.size(); k++){
				Channel ch = channels.get(k);
				writeChannelInfo_html(ch, writer);
				
				writeChannelItems_html(ch, writer);
			}
			
			writer.println("</body>");
			writer.println("</html>");
			
			writer.flush();
			writer.close();
			return true;
		}catch (Exception e){return false;}
	}
	
	
	
	public void writeMenu_html(PrintWriter writer){
		String com = Functions.getFilenameFromUrl(Globals.RSS_COMMENTS_URL)+"__parsedContent.html";
		String edit = Functions.getFilenameFromUrl(Globals.RSS_EDITS_URL)+"__parsedContent.html";
		String post = Functions.getFilenameFromUrl(Globals.RSS_POSTS_URL)+"__parsedContent.html";
		writer.println("<div class='menu'>");
		writer.println("<a class='switch' href='"+com+"'> COMMENTS </a>");
		writer.println("<a class='switch' href='"+edit+"'> EDITS </a>");
		writer.println("<a class='switch' href='"+post+"'> POSTS </a>");
		writer.println("</div>");
	}
	
	public void writeChannelInfo_html(Channel ch, PrintWriter writer){
		writer.println("<div class='channel_title'>"+ch.getTitle()+"</div>");
		writer.println("<a class='channel_author' href='"+ch.getLink()+"'>"+ch.getLink()+"</a>");
		writer.println("<div class='channel_link'>"+ch.getDescription()+"</div>");	
		writer.println("<div class='channel_description'>"+ch.getLanguage()+"</div>");
		writer.println("<div class='channel_pubdate'>"+ch.getPubdate()+"</div><hr><br><br>");	

	}
	
	public void writeChannelItems_html(Channel ch , PrintWriter writer){
		for(int k=0; k<ch.size(); k++){
			Item item = (Item)ch.getItems().get(k);
				writer.println("<div class='item_title'>"+item.getTitle()+"</div>");
				writer.println("<div class='item_author'>"+item.getAuthor()+"</div>");
				writer.println("<a class='item_link' href='"+item.getLink()+"'>"+item.getLink()+"</a>");	
				writer.println("<div class='item_description'>"+item.getDescription()+"</div>");
				writer.println("<div class='item_pubdate'>"+item.getPubdate()+"</div><br><br><hr>");	
		}
	}
	
	public boolean xmlExport(Channel ch){
		try{
			File parsedFile = new File(Globals.XML_DIR+"/"+outputFileName);
			FileOutputStream outStream = new FileOutputStream(parsedFile);
			PrintWriter writer = new PrintWriter(outStream);
			
			
				writer.println("<xml version = \"2.0\">");
				writer.println("<channel>");
				writer.println("\n\n");
				writer.println("<title>"+ch.getTitle()+"</title>");
				writer.println("<link>"+ch.getLink()+"</link>");
				writer.println("<description>"+ch.getDescription()+"</description>");
				writer.println("<language>"+ch.getLanguage()+"</language>");
				writer.println("<pubdate>"+ch.getPubdate()+"</pubdate>");
				writer.println("\n\n\n\n");
				
			if(ch.size()>0){	
				for(int k=0; k<ch.size(); k++){
					Item item = (Item)ch.getItems().get(k);
						writer.println("<item>");
						writer.println("<visto>"+item.isVisto()+"</visto>");
						writer.println("<indice>"+item.getIndice()+"</indice>");
						writer.println("<title><![CDATA["+item.getTitle()+"]]></title>");
						writer.println("<author>"+item.getAuthor()+"</author>");
						writer.println("<link><![CDATA["+item.getLink()+"]]></link>");	
						writer.println("<description><![CDATA["+item.getDescription()+"]]></description>");
						writer.println("<pubdate>"+item.getPubdate()+"</pubdate>");	
						
						writer.println("</item>");
						writer.println("\n\n");
				}
			}
			writer.println("\n\n\n\n");
			writer.println("</channel>");
			writer.println("</xml>");
			
			writer.flush();
			writer.close();
			return true;
		}catch(Exception e){return false;}
		
		
	}
}
