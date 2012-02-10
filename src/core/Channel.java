package core;
/**
 * This Class is used to encapsulate an rss channel and it contains an arrayList 
 * of Items [ An item is the Single notice ]
 * 
 * Nicolò Paternoster - Francesco Tudisco - Giorgio Natili
*/
import java.util.ArrayList;
import java.util.Date;


public class Channel {
	
	protected String link;
	protected String title;
	protected String description;
	protected String language;
	protected String pubdate;
	protected ArrayList<Item> itemsList = new ArrayList<Item>();
	protected String iconPath;
	
	public Channel(){}
	
	public Channel(String title, String link, String description, String language,String pubdate)
	{
		this.link=link;
		this.description=description;
		this.language=language;
		this.pubdate=pubdate;
		this.title=title;
	}
	
	public Channel(String[] sign){
		this.title=sign[0];
		this.link=sign[1];
		this.description=sign[2];
		this.language=sign[3];
		this.pubdate=sign[4];
	}
	
	public String getTitle() { return title; }
	
	public String getLink() { return link; }
	
	public String getDescription() { return description; }
	
	public String getLanguage() { return language; }
	
	public String getPubdate() { return pubdate; }
	
	public String getIconPath() { return iconPath; }
	
	public Item getLastItem(){
		return itemsList.get(0);
	}
	
	public ArrayList<Item> getItems(){
		return itemsList;
	}
	
	public String[] getInfo(){
		return new String[]{title, link, description, language, pubdate};
	}
	
	public int size() { return itemsList.size(); } 
	
	public int indexOf(Item i){
		return itemsList.indexOf(i);
	}
	
	public void setItem(int ind, Item it){
		itemsList.set(ind, it);
	}
	
	public void setTitle(String newstring) { this.title=newstring; }
	
	public void setLink(String newstring) { this.link=newstring; }
	
	public void setDescription(String newstring) { this.description=newstring; }
	
	public void setLanguage(String newstring) { this.language=newstring; }
	
	public void setPubdate(String newstring) { this.pubdate=newstring; }
	
	public void setIconPath(String newstring) { 
		this.iconPath=newstring; 
		for(int i=0; i<getItems().size(); i++){
			getItems().get(i).setIconPath(newstring);
			
		}
	}
	
	public boolean contains(Item it){
		for(int j=0; j<this.size(); j++){
			Item own = itemsList.get(j);
			if(it.equals(own)){
				return true;
			}
		}
		return false;
	}
	
	public void add(ArrayList<Item> arr){
		int numNewItems = arr.size();
		
		int max = getMaxItemsIndex();
		
		for(int i=numNewItems-1; i>=0; i--){
			Item it = arr.get(i);
			int index = Math.abs(i-numNewItems)+max;
			it.setIndice(index);
		}
		itemsList.addAll(arr);
		sortByIndex();
	}
	
	public int getMaxItemsIndex(){
		int max = getItemAt(0).getIndice();
		for(int i=1; i<size(); i++){
			if(max<getItemAt(i).getIndice()){
				max = getItemAt(i).getIndice();
			}
		}
		return max;
	}
	
	
	public void addItem(Item item) {itemsList.add(item);}
	
	public String getParsedItem_fromAuthorTitle(String author, String title){
		ArrayList<Item> itms = getItems();
		for(int k=0; k<itms.size(); k++){
			String itmTitle = itms.get(k).getTitle();
			String itmAuth = itms.get(k).getAuthor();
			if(itmTitle.equalsIgnoreCase(title))
				if(itmAuth.equalsIgnoreCase(author))
				return itms.get(k).parse();
		}
		return null;
	}
	
	public Item getItem_fromAuthorTitle(String author, String title){
		ArrayList<Item> itms = getItems();
		for(int k=0; k<itms.size(); k++){
			String itmTitle = itms.get(k).getTitle();
			String itmAuth = itms.get(k).getAuthor();
			if(itmTitle.equalsIgnoreCase(title))
				if(itmAuth.equalsIgnoreCase(author))
				return itms.get(k);
		}
		return null;
	}
	
	public Item getItemAt(int i){
		return (Item)itemsList.get(i);
	}
	public Channel sort(int sortType, boolean straight)
	{
		switch(sortType)
		{
			case Globals.SORT_BY_AUTHOR:{
				sortByAuthor(straight);		
			    break;
			}

			case Globals.SORT_BY_TITLE:{
				sortByTitle(straight);		
			    break;
			}
			case Globals.SORT_BY_DATE:{
				sortByDate(straight);		
				break;
			}
		}
		return this;
	}
	
	public int indicePrimoCarattereDifferente(String uno, String due){
		int size = uno.length()<due.length() ? uno.length()  : due.length();
		//Functions.msg(size+" "+uno.length()+" "+due.length());
		for(int k=0; k<size; k++){
			char c1 = uno.toLowerCase().charAt(k);
			char c2 = due.toLowerCase().charAt(k);
			if(c1 != c2){return k;}
		}
		return -1;
		
	}
	
	
	public int[] getItemsInfo(){
		int[] info = new int[2];
		info[0] = size();
		info[1] = 0;
		for(int i=0; i<size(); i++){
			if(getItemAt(i).isVisto())info[1]++;
		}
		return info;
	}
	
	public void sortByAuthor(boolean straight){
		boolean ordinato = false;
	     while(!ordinato){	        
	            ordinato = true;
	            for(int i=0; i<size()-1; i++){
	            	String autore1 = itemsList.get(i).getAuthor();
	            	String autore2 = itemsList.get(i+1).getAuthor();
	            	
	            	int comp = autore1.compareToIgnoreCase(autore2);
	            	if(straight){
	            		if(comp>0){
	            			Item temp = itemsList.get(i);
                    		itemsList.set(i, itemsList.get(i+1));
                    		itemsList.set(i+1 , temp);
                    		ordinato = false;
	            		}
	            	}else{
	            		if(comp<0){
	            			Item temp = itemsList.get(i);
                    		itemsList.set(i, itemsList.get(i+1));
                    		itemsList.set(i+1 , temp);
                    		ordinato = false;
	            		}
	            	}
	           }
	     }
	}
	
	public void sortByTitle(boolean straight){
		boolean ordinato = false;
	     while(!ordinato){	        
	            ordinato = true;
	            for(int i=0; i<size()-1; i++){
	            	String title1 = itemsList.get(i).getTitle();
	            	//Functions.msg("autore1: "+autore1);
	            	String title2 = itemsList.get(i+1).getTitle();
	            	
	            	int comp = title1.compareToIgnoreCase(title2);
	            	if(straight){
	            		if(comp>0){
	            			Item temp = itemsList.get(i);
                    		itemsList.set(i, itemsList.get(i+1));
                    		itemsList.set(i+1 , temp);
                    		ordinato = false;
	            		}
	            	}else{
	            		if(comp<0){
	            			Item temp = itemsList.get(i);
                    		itemsList.set(i, itemsList.get(i+1));
                    		itemsList.set(i+1 , temp);
                    		ordinato = false;
	            		}
	            	}
	           }
	     }
	}
	
	public void sortByDate(boolean straight){
		boolean ordinato = false;
	     while(!ordinato){	        
	            ordinato = true;
	            for(int i=0; i<size()-1; i++){
	            	if(itemsList.get(i).getDate()!=null && itemsList.get(i+1).getDate()!=null){
		            	Date date1 = itemsList.get(i).getDate().getTime();
		            	
		            	Date date2 = itemsList.get(i+1).getDate().getTime();
		            	
		            	if(straight){
		            		if(date1.after(date2)){
		            			Item temp = itemsList.get(i);
	                    		itemsList.set(i, itemsList.get(i+1));
	                    		itemsList.set(i+1 , temp);
	                    		ordinato = false;
		            		}
		            	}else{
		            		if(date1.before(date2)){
		            			Item temp = itemsList.get(i);
	                    		itemsList.set(i, itemsList.get(i+1));
	                    		itemsList.set(i+1 , temp);
	                    		ordinato = false;
		            		}
		            	}
	            	}
	           }
	     }
	}
	
	/*public void sortByDate(boolean straight){
		for(int i=0; i<size(); i++){
			//if(itemsList.get(i).getDate()!=null)
				Functions.msg("data alla riga num "+(i+1)+" : "+itemsList.get(i).getDate().getTime().toString());
			//else
			//	Functions.msg("data alla riga num "+(i+1)+" : null");
		}
	}*/
	  
	public void sortByIndex(){
		boolean ordinato = false;
	     while(!ordinato){	        
	            ordinato = true;
	            for(int i=0; i<size()-1; i++){
	            	int ind1 = itemsList.get(i).getIndice();
	            	
	            	int ind2 = itemsList.get(i+1).getIndice();
	            	
	            	if(ind1<ind2){
            			Item temp = itemsList.get(i);
            			itemsList.set(i, itemsList.get(i+1));
            			itemsList.set(i+1 , temp);
            			ordinato = false;
            		}
	           }
	     }
	}
	
	  public ArrayList<Item> flipOrder(ArrayList<Item> toOrder){
		  ArrayList<Item> temp = new ArrayList<Item>();
		  for(int i=toOrder.size()-1; i>=0; i--){
			  temp.add(toOrder.get(i));
		  }
		  return temp;
	  }

}
