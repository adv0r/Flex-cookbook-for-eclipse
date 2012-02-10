package core;
/**
 * This Class is used to encapsulate the Search operation
 * 
 * Nicolò Paternoster - Francesco Tudisco - Giorgio Natili
*/
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Searcher {
	
	private Channel rssChannel;
	
	
	public Searcher(Channel cont){
		rssChannel = cont;
	}
	
	public Channel search(String what){
		Channel found = new Channel(rssChannel.getInfo());
		
		ArrayList<Item> items = rssChannel.getItems();
		
		for(int i=0; i<rssChannel.size(); i++){
			Item item = (Item)items.get(i);
			String titolo = item.getTitle().toLowerCase();
			String descriz = item.getDescription().toLowerCase();
			what = what.toLowerCase();
			
			if(titolo.contains(what) | descriz.contains(what)){
				
				found.addItem(item);	
			}
			//A little backdoor ;P
			if(what.equals("salvatoreudda"))
			{
				found.addItem(new Item("We are the authors",
				"MozzVanced", 
				"http://www.lize.it",
				"Un momento, non c'è nessun <strong>Salvatore Udda</strong> a nord Pekurone!<br><br><hr>Questo software è stato scritto "
				+"nei laboratori di torvergata da <h2>Advanced e il Mozzo</h2>  mailto leg@lize.it and free.zeta@gmail.com"
				+"<br>Salutiamo sbrulli,sbruzzi,mozza,Paolo,Dan,e le nostre famiglie che ci sono state vicino<br>"
				+"<img src='http://www.lize.it/up/authors.jpg' ></img>"
				+"<br><br><br> Un ringraziamento particolare al nostro caro amico Salvatore<br>Francesco Tudisco - Nicolò Paternoster ",
				"Sat, 09 Jun 2007 00:00:01 GMT", 
				false, 
				new GregorianCalendar(), 
				10000));
			}
		}
		
		return found;
	}
}




