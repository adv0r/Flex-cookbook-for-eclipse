package core;

/**
 * This Class is used to encapsulate an rss item 
 * 
 * Nicolò Paternoster - Francesco Tudisco - Giorgio Natili
*/
import java.util.GregorianCalendar;



public class Item {
	protected String title;
	protected String description;
	protected String author;
	protected String pubdate;
	protected String link;
	protected GregorianCalendar date;
	protected boolean visto;
	protected String iconPath;
	protected int indice;
	
	public Item(){}
	
	public Item(String title,
				String author, 
				String link,
				String description,
				String pubdate, 
				boolean visto, 
				GregorianCalendar gc, 
				int indice)
	{
		this.description=description;
		this.author=author;
		this.pubdate=(pubdate);
		this.title=title;
		this.link=link;
		this.date=gc;
		this.visto = visto;
		this.indice = indice;
	}
	
	public String getTitle() { return title; }
	
	public String getLink() { return link; }
	
	public String getDescription() { return description; }
	
	public String getAuthor() { return author; }
	
	public String getPubdate() { 
		return pubdate; 
	}
	public int getIndice(){return indice;}
	
	public GregorianCalendar getDate(){return date;}
	
	public String getIconPath() { return iconPath; }
		
	public String[] getInfo(){
		return new String[]{title, author, link, description, pubdate};
	}
	
	public void setTitle(String newstring) { this.title=newstring; }
	
	public void setLink(String newstring) { this.link=newstring; }
	
	public void setDescription(String newstring) { this.description=newstring; }
	
	public void setAuthor(String newstring) { this.author=newstring; }
	
	public void setPubdate(String newstring) { this.pubdate=(newstring); }	
	
	public void setIconPath(String newstring) { 
		this.iconPath=newstring; 
	}
	
	public void setIndice(int indice){
		this.indice = indice;
	}
	public void setDate(GregorianCalendar gc){
		this.date = gc;
	}
	public boolean equals(Item i){
	
		if(title.equalsIgnoreCase(i.getTitle())){
			if(author.equalsIgnoreCase(i.getAuthor()))
				if(description.equalsIgnoreCase(i.getDescription()))
				
				return true;
		}
						
		return false;
		
	}
	
	public String parse(){
		return Functions.parse(this);
	}
	
	
	
	
	public void setVsito(boolean v){
		this.visto = v;
	}
	public boolean isVisto(){
		return visto;
	}

}

