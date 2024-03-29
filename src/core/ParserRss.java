package core;
/**
 * This Class is used to parse the files .rss that we receive from Adobe Channels:
 * [ See Globals.RSS_POSTS_URL for example ]
 * 
 * The fields are : Title , Author, Link,Decription,pubDate and lang
 * 
 * Nicol� Paternoster - Francesco Tudisco - Giorgio Natili
 * 2007
*/
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;




public class ParserRss
{
	private File file;

	private XMLReader reader = null;
	
	private Handler ascoltatore = new Handler();
	
	public ParserRss(String fileName){	
		
		file = new File(Globals.XML_DIR +"/"+fileName);
		SAXParserFactory fact;
		SAXParser parser;
		fact = SAXParserFactory.newInstance();
		
		try{
			
			parser = fact.newSAXParser();		
			reader = parser.getXMLReader();		
			reader.setContentHandler(ascoltatore);
			
			InputStream inp = new FileInputStream(file);			
			InputSource inpS = new InputSource(inp);			
			reader.parse(inpS);
			
		}
		catch(SAXException e){
			//javax.swing.JOptionPane.showMessageDialog(null, e.toString());
			e.printStackTrace();
		} 
		catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		
	}
	
	public Handler getHandler(){
		return ascoltatore;
	}
	
	public Channel getChannel(){
		return ascoltatore.getChannel();
	}
	

	public void parseChannel(){
		Channel cont = ascoltatore.getChannel();	
		String contOutput = file.getName()+"__parsedContent.html";
        new ParseWriter(contOutput).htmlExport(cont);
			//Functions.exportParsedChannel_html(cont, contOutput);
	}
	
	public Channel getFoundChannel(String what){
		Channel f = new Searcher(ascoltatore.getChannel()).search(what);
		return f;
	}
	
	public void parseFoundChannel(String what){
		Channel f = new Searcher(ascoltatore.getChannel()).search(what);
        new ParseWriter(file.getName()+"__search.html").htmlExport(f);
	}
	
	public String getParsedItem_fromAuthorTitle(String author, String title){
		ArrayList<Item> itms = getChannel().getItems();
		for(int k=0; k<itms.size(); k++){
			String itmTitle = itms.get(k).getTitle();
			String itmAuth = itms.get(k).getAuthor();
			if(itmTitle.equalsIgnoreCase(title))
				if(itmAuth.equalsIgnoreCase(author))
				return itms.get(k).parse();
		}
		return null;
	}
	
}

class Handler implements ContentHandler
{
	private Channel content = new Channel();
	
	private boolean item = false,
					titolo=false,
					autore=false,
					link = false,
					descr = false,
					pubdate = false,
					lang = false;
	
	private String t = "", a = "", l = "", d = "", p = "", lan = "";
	private int i = 0;
	
	public Handler(){}
	
	public Channel getChannel(){
		return content;
	}
	
	public void endPrefixMapping(String prefix) throws SAXException {}
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}
	public void processingInstruction(String target, String data) throws SAXException {}
	public void setDocumentLocator(Locator locator) {}
	public void skippedEntity(String name) throws SAXException {}
	public void startPrefixMapping(String prefix, String uri) throws SAXException {}
	public void startDocument() throws SAXException {}
	public void endDocument() throws SAXException {}
	
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		
		if(qName.equalsIgnoreCase("item")){
			item = true;
		}
		if(item){
			if(qName.equalsIgnoreCase("title")){
				titolo = true; 
			}
			if(qName.equalsIgnoreCase("author")){
				autore=true;
			}
			if(qName.equalsIgnoreCase("link")){
				link = true; 
			}
			if(qName.equalsIgnoreCase("description")){
				descr=true;
			}
			if(qName.equalsIgnoreCase("pubdate")){
				pubdate = true; 
			}
			
		}
		else{
			if(qName.equalsIgnoreCase("title")){
				titolo = true; 
			}
			if(qName.equalsIgnoreCase("link")){
				link=true;
			}
			if(qName.equalsIgnoreCase("description")){
				descr = true; 
			}
			if(qName.equalsIgnoreCase("language")){
				lang=true;
			}
			if(qName.equalsIgnoreCase("pubdate")){
				pubdate = true; 
			}
		}

	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
		if(item){
			if(qName.equalsIgnoreCase("title")){
				titolo=false;
			}
			if(qName.equalsIgnoreCase("author")){
				autore=false;
			}
			if(qName.equalsIgnoreCase("link")){
				link=false;
			}
			if(qName.equalsIgnoreCase("description")){
				descr=false;
			}
			if(qName.equalsIgnoreCase("pubdate")){
				pubdate=false;
			}
		}else{
			if(qName.equalsIgnoreCase("title")){
				titolo = false; 
			}
			if(qName.equalsIgnoreCase("link")){
				link=false;
			}
			if(qName.equalsIgnoreCase("description")){
				descr = false; 
			}
			if(qName.equalsIgnoreCase("language")){
				lang=false;
			}
			if(qName.equalsIgnoreCase("pubdate")){
				pubdate = false; 
			}
		}
		if(qName.equals("item")){
			item=false;
		}
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		String str = new String();
		
		for(int i=0; i<length; i++){
			str+=ch[start+i];
		}
		if(str!=null && !str.equals("")){
			
		}

		if(item){
			if(titolo)
				t += new String(str);
			if(autore)
				a += new String(str);
			if(link){
				l += new String(str);
			}
			if(descr)
				d += new String(str);
			if(pubdate)
				p += new String(str);
		}
		else{
			if(titolo)
				t += new String(str);
			if(link)
				l += new String(str);
			if(descr)
				d += new String(str);
			if(lang)
				lan += new String(str);
			if(pubdate)
				p += new String(str);
		}	
			
		if(t!="" && d!="" && l!="" && p!="" ){
			if(a!=""){
					content.addItem(new Item(t,a,l,d,p,false, Functions.formatDate(p),i));
					t=""; a=""; l=""; d=""; p="";i++;
			}
			else if(lan!=""){
				content.setTitle(t);
				content.setLink(l);
				content.setDescription(d);
				content.setLanguage(lan);
				content.setPubdate(p);
				t=""; l=""; d=""; lan=""; p="";
			}
		}
			
	}
}
