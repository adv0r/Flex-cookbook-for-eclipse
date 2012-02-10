package core;

/**
 * This Class is used to encapsulate the thread that cheks if you are working in
 * online or in offline mode.
 * The thread trys to ping an host[ONLINE_CHECKER_HOST], and waits for the answer, 
 * each "Globals.PING_TIME"  milliseconds.
 * 
 * Nicolò Paternoster - Francesco Tudisco - Giorgio Natili
 * 2007
*/
import java.net.InetAddress;

import flexrss_1.views.rssView;



public class ConnectionThread implements Runnable
{
	private Thread th = null;
	private rssView plugin_view;
	private boolean once;
	
	public ConnectionThread(boolean once, rssView plugin_view){
		this.plugin_view = plugin_view;this.once = once;
		start();
	}
	
	public void start(){
		if(th==null){
			th = new Thread(this);
			th.start();
		}
	}
	
	public void run()
	{  
		while(th!=null){
			 Globals.connectionDetection = false;
			 InetAddress loc;		
				try {
					loc = InetAddress.getByName(Globals.ONLINE_CHECKER_HOST);
					Globals.connected = loc.isReachable(3000);
					plugin_view.setStatusIcon(Globals.connected);
					
					//Functions.updateDate();
				}
				catch (Exception e)
					{
					Globals.connected = false;
					plugin_view.setStatusIcon(false);
					
					}
			  Globals.connectionDetection = true;
			  try {
				th.sleep(Globals.PING_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(once)th=null;
		}
	 }
	 
}	


