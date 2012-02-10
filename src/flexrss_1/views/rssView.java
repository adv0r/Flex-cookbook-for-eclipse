package flexrss_1.views;
/**
 * This file contains all the GUI elements
 * 
 * Nicolò Paternoster - Francesco Tudisco - Giorgio Natili
*/

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.part.ViewPart;

import core.Channel;
import core.ConnectionThread;
import core.Functions;
import core.Globals;
import core.Item;
import core.ParseWriter;
import core.ParserHtml;
import core.RssDownThread;
import core.Searcher;


public class rssView extends ViewPart{
	private Composite PARENT;
	
	protected int[] colState = new int[3];
	
	protected Channel currentChannel = null, defaultChannel=null;
	protected Channel currentCommentsChannel, currentPostsChannel, currentEditsChannel;
	protected Channel defaultCommentsChannel, defaultPostsChannel, defaultEditsChannel;
	
	protected Label TITLELABEL;
	protected ToolsBar TOOLBAR;
	protected ItemsTable TABLE;
	protected Browser BRW;
	
	protected Action statusIc = null, statusTxt = null;
	
	public rssView(){
		
		currentCommentsChannel = new ParserHtml(Globals.RSS_COMMENTS_FNAME).getChannel();
		currentCommentsChannel.setIconPath(Globals.ICON_RSS_FUXX);
		
		currentPostsChannel = new ParserHtml(Globals.RSS_POSTS_FNAME).getChannel();
		currentPostsChannel.setIconPath(Globals.ICON_RSS_BLUE);
		
		currentEditsChannel= new ParserHtml(Globals.RSS_EDITS_FNAME).getChannel();
		currentEditsChannel.setIconPath(Globals.ICON_RSS_GREEN);

		currentChannel = currentCommentsChannel;
		
		Globals.currentConnectionThread = new ConnectionThread(false,this);
		Globals.currentDownThread = new RssDownThread(false, this);
		
	}
	
	public void createPartControl(Composite parent) {
		PARENT = parent;
		
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		Functions.setFontSize();
		parent.setLayout(gl);
		
		

		
		TOOLBAR = new ToolsBar(parent);
		TITLELABEL = new Label(parent, SWT.HORIZONTAL|SWT.CENTER);
		TOOLBAR.setLayoutData();
		
		setTitleLabel(currentChannel);
		
		TABLE = new ItemsTable(parent);
		
		TABLE.setLayoutData();
		
		BRW = new Browser(parent, SWT.DEFAULT|SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 450;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.TOP;
		
		BRW.setLayoutData(gd);
		
		
		addStatusBar();
		
		getSite().getPage().addPartListener(new IPartListener2() {
			public void partActivated(IWorkbenchPartReference partRef) {}
			public void partBroughtToTop(IWorkbenchPartReference partRef) {}
			public void partDeactivated(IWorkbenchPartReference partRef) {}
			public void partHidden(IWorkbenchPartReference partRef) {}
			public void partInputChanged(IWorkbenchPartReference partRef) {}
			public void partOpened(IWorkbenchPartReference partRef) {}
			public void partVisible(IWorkbenchPartReference partRef) {}
			
			public void partClosed(IWorkbenchPartReference partRef) {
				currentCommentsChannel.sortByIndex();
				currentEditsChannel.sortByIndex();
				currentPostsChannel.sortByIndex();
				new ParseWriter(Globals.RSS_COMMENTS_FNAME).xmlExport(currentCommentsChannel);
				new ParseWriter(Globals.RSS_EDITS_FNAME).xmlExport(currentEditsChannel);
				new ParseWriter(Globals.RSS_POSTS_FNAME).xmlExport(currentPostsChannel);
				
			}
		}); 
	}
	
	public void setFocus() {
		
	}
	
	private void addStatusBar() {
		IActionBars bars = getViewSite().getActionBars();
		
		statusIc = new CheckAction(this);
		statusIc.setToolTipText("Online or offline mode - Click To refresh!");
		statusTxt = new CheckAction(this);
		statusIc.setToolTipText("Online or offline mode - Click To refresh!");
		UpdAction updateIcon = new UpdAction(this);
		UpdAction updateImage = new UpdAction(this);
		updateImage.setImageDescriptor(ImageDescriptor.createFromImage(new Image(PARENT.getDisplay(),Globals.ICON_REFRESH)));
		updateIcon.setText("Refresh Channels");
		updateImage.setToolTipText("Click here if you want to check out the lastest rss");
		updateIcon.setToolTipText("Click here if you want to check out the lastest rss");
		setStatusIcon(Functions.isConnected());
		
		bars.getToolBarManager().add(updateImage);
		bars.getToolBarManager().add(updateIcon);
		bars.getToolBarManager().add(statusIc);
		bars.getToolBarManager().add(statusTxt);
		
	}
	
	class UpdAction extends Action
	{
		private rssView rw;
		public UpdAction(rssView rw){this.rw = rw;}
		public void run(){
			new ConnectionThread(true,rw);
			if(Functions.isConnected()){
				new RssDownThread(true, rw);
			}else{
				showMessage("ALERT: cannot update rss in offLine mode");
			}
		}
	}
	class CheckAction extends Action
	{
		private rssView rw;
		public CheckAction(rssView rw){this.rw = rw;}
		public void run(){
			new ConnectionThread(true,rw);
		}
	}
	
	public void setStatusIcon(boolean online){
		if(statusTxt!=null && statusIc!=null){
			if(online){
				statusTxt.setText("onLine");
				statusIc.setImageDescriptor(ImageDescriptor.createFromImage(new Image(PARENT.getDisplay(),Globals.ICON_ONLINE)));
			}else{
				statusTxt.setText("offLine");
				statusIc.setImageDescriptor(ImageDescriptor.createFromImage(new Image(PARENT.getDisplay(), Globals.ICON_OFFLINE)));
			}
		}
	}
	
	public void showMessage(String message) {
		MessageDialog.openInformation(
			new Shell(),
			"Sample View",
			message);
	}
	
	public void setTitleLabel(Channel ch){
		String str = ch.getTitle();
		str += "  ("+ch.getItemsInfo()[0]+" items, "+ ch.getItemsInfo()[1]+" visti)";
		str += "  -  "+ch.getPubdate();
		TITLELABEL.setText(str);
	}
	
	
	public void channelsUpdate(){
		currentCommentsChannel = new ParserHtml(Globals.RSS_COMMENTS_FNAME).getChannel();
		currentCommentsChannel.setIconPath(Globals.ICON_RSS_FUXX);
		
		currentPostsChannel = new ParserHtml(Globals.RSS_POSTS_FNAME).getChannel();
		currentPostsChannel.setIconPath(Globals.ICON_RSS_BLUE);
		
		currentEditsChannel= new ParserHtml(Globals.RSS_EDITS_FNAME).getChannel();
		currentEditsChannel.setIconPath(Globals.ICON_RSS_GREEN);
	}
	
	
	
	
	
	
	
	//This class contains the elements of the toolBar at the top of the plugin
	class ToolsBar 
	{
		private Composite panel;
		private ToolBar toolBar;
		
		private Text tx;
		private Button all;
		public ToolItem commItem,editsItem, postsItem, searchItem;
		
		public ToolsBar(Composite parent){
			panel = new Composite(parent, SWT.NONE);
			
			Listener switchChannel = new Listener(){
				public void handleEvent(Event e) {
					ToolItem rd = (ToolItem)e.widget;
					
					if(rd.getText().trim().equalsIgnoreCase("Edits")){
						if(currentChannel!=currentEditsChannel){
							if(tx.getText()==""||tx.getText().equals(Globals.SEARCH_STR)){
								currentChannel=currentEditsChannel;
								currentChannel.sortByIndex();
							}else{
		
								currentChannel = new Searcher(currentEditsChannel).search(tx.getText());
							}
							TABLE.fillTable(currentChannel);
							setTitleLabel(currentChannel);
							
						}
					}
					if(rd.getText().trim().equalsIgnoreCase("Comments")){
						if(currentChannel!=currentCommentsChannel){
		
							if(tx.getText()==""||tx.getText().equals(Globals.SEARCH_STR)){
								currentChannel=currentCommentsChannel;
								currentChannel.sortByIndex();
							}else{
						
								currentChannel = new Searcher(currentCommentsChannel).search(tx.getText());
							}
							TABLE.fillTable(currentChannel);
							setTitleLabel(currentChannel);
						}
					}
					if(rd.getText().trim().equalsIgnoreCase("Posts")){
						if(currentChannel!=currentPostsChannel){
							
							if(tx.getText()==""||tx.getText().equals(Globals.SEARCH_STR)){
								currentChannel=currentPostsChannel;
								currentChannel.sortByIndex();
							}else{
							
								currentChannel = new Searcher(currentPostsChannel).search(tx.getText());
							}
							TABLE.fillTable(currentChannel);
							setTitleLabel(currentChannel);
						}
					}
					TABLE.setDefaultIcons();
					all.setSelection(false);
				}	
			};
			
			Listener searchButtonListener = new Listener(){
				public void handleEvent(Event e) {
					
					if(tx.getText().equals(Globals.SEARCH_STR)){
					
						currentChannel = null;
						TABLE.table.removeAll();
					}else{
						
						Channel C = new Searcher(currentCommentsChannel).search(tx.getText());					
						Channel E = new Searcher(currentEditsChannel).search(tx.getText());					
						Channel P = new Searcher(currentPostsChannel).search(tx.getText());
						
						currentChannel = Functions.lotToOne(new Channel[]{C,E,P});
						
						
						TABLE.fillTable(currentChannel);
					}
					all.setSelection(true);
					TABLE.setDefaultIcons();
					TITLELABEL.setText(Globals.SEARCH_TITLE_STR);
				}	
			};
			 
			toolBar = new ToolBar(panel, SWT.HORIZONTAL);
			
			
			commItem = new ToolItem(toolBar, SWT.RADIO);
			commItem.setText("  Comments  ");
			commItem.setImage(new Image(parent.getDisplay(), currentCommentsChannel.getIconPath()));
			commItem.setSelection(true);
			commItem.addListener(SWT.Selection, switchChannel);
			commItem.setToolTipText(Globals.RSS_COMMENTS);
			
			
			editsItem = new ToolItem(toolBar, SWT.RADIO);
			editsItem.setText("    Edits     ");
			editsItem.setImage(new Image(parent.getDisplay(), currentEditsChannel.getIconPath()));
			editsItem.addListener(SWT.Selection, switchChannel);
			editsItem.setToolTipText(Globals.RSS_EDITS);
			
			
			postsItem = new ToolItem(toolBar, SWT.RADIO);
			postsItem.setText("    Posts     ");	
			postsItem.setImage(new Image(parent.getDisplay(), currentPostsChannel.getIconPath()));
			postsItem.addListener(SWT.Selection, switchChannel);
			postsItem.setToolTipText(Globals.RSS_POSTS);
			
			
			searchItem = new ToolItem(toolBar, SWT.RADIO);
			searchItem.setImage(new Image(parent.getDisplay(),Globals.ICON_RSS_SEARCH));
			searchItem.setText("Search Results");
			searchItem.addListener(SWT.Selection, searchButtonListener);
			searchItem.setToolTipText("Click to view the search's results");
			
			
			
			new ToolItem(toolBar, SWT.SEPARATOR);
			
			
			tx = new Text(panel, SWT.LEFT|SWT.SINGLE);
			tx.setText(Globals.SEARCH_STR);
			tx.setToolTipText("Insert the string to search");
	
			tx.addKeyListener(new KeyListener(){

				public void keyPressed(KeyEvent e){
					if(currentChannel==null && !all.getEnabled()){
						showMessage("You have to select where to look for!");
						tx.setText("");
					}
				}

				public void keyReleased(KeyEvent e) {
					if(!all.getSelection()){
						
						if(commItem.getSelection()){
							currentChannel = new Searcher(currentCommentsChannel).search(tx.getText());
						}
						else if(editsItem.getSelection()){
							currentChannel = new Searcher(currentEditsChannel).search(tx.getText());
						}
						else if(postsItem.getSelection()){
							currentChannel = new Searcher(currentPostsChannel).search(tx.getText());
						}
						TABLE.fillTable(currentChannel);
					}
					else{
						searchItem.setSelection(true);
						commItem.setSelection(false);
						editsItem.setSelection(false);
						postsItem.setSelection(false);
						
						Channel C = new Searcher(currentCommentsChannel).search(tx.getText());
						
						Channel E = new Searcher(currentEditsChannel).search(tx.getText());
					
						Channel P = new Searcher(currentPostsChannel).search(tx.getText());
						
						TABLE.setDefaultIcons();
						
						
						currentChannel = Functions.lotToOne(new Channel[]{C,E,P});
						
						TABLE.fillTable(currentChannel);
						TITLELABEL.setText(Globals.SEARCH_TITLE_STR);
					}
					
				}
			});
			tx.addListener(SWT.MouseDown, new Listener(){
				public void handleEvent(Event event) {
					if(tx.getText().equals(Globals.SEARCH_STR))
	                   tx.setText("");
	            }              
	        });
			all=new Button(panel, SWT.CHECK);
			all.setText("all");
			all.setToolTipText("If you want to search in all off rss channels check this box");
			all.addListener(SWT.MouseDown, new Listener(){

				public void handleEvent(Event e) {
				
				if(all.getSelection()){
				if(tx.getText().trim().equalsIgnoreCase(Globals.SEARCH_STR.trim())){
				currentChannel = currentCommentsChannel;
				currentChannel.sortByIndex();

				}else{
				currentChannel = new Searcher(currentCommentsChannel).search(tx.getText());
				}
				setTitleLabel(currentChannel);
				commItem.setSelection(true);
				searchItem.setSelection(false);
				}
				else{
				if(tx.getText().trim().equalsIgnoreCase(Globals.SEARCH_STR.trim())){

				currentChannel = null;

				}else{
				Channel C = new Searcher(currentCommentsChannel).search(tx.getText());

				Channel E = new Searcher(currentEditsChannel).search(tx.getText());

				Channel P = new Searcher(currentPostsChannel).search(tx.getText());

				currentChannel = Functions.lotToOne(new Channel[]{C,E,P});
				}

				TABLE.setDefaultIcons();
				TITLELABEL.setText(Globals.SEARCH_TITLE_STR);
				searchItem.setSelection(true);
				commItem.setSelection(false);
				editsItem.setSelection(false);
				postsItem.setSelection(false);
				}
				if(currentChannel!=null)
				TABLE.fillTable(currentChannel);
				else
				TABLE.table.removeAll();
				}

				});
	       
		}
		
		public void setLayoutData(){
			panel.setLayout(new RowLayout());
		}
	}



	
	
	
	
	//This class contains the Table of items
	class ItemsTable  {

			private Composite parent;
			private Table table;
			private TableColumn tcView, tcAuthor, tcTitle, tcDate;
			
		  
		  public ItemsTable(Composite parent) {
			table = new Table(parent, SWT.FULL_SELECTION|SWT.SINGLE|SWT.V_SCROLL|SWT.VIRTUAL);
			table.setBounds(10,10,100,300);
			this.parent = parent;
		    table.setHeaderVisible(true);
		    
		    tcView = new TableColumn(table, SWT.LEFT);
		    tcView.setText("");
		    tcView.setWidth(20);
		    tcView.setResizable(false);
		    
		    tcAuthor = new TableColumn(table, SWT.LEFT);
		    tcAuthor.setText("Author");	    
		    tcAuthor.setWidth(120);
		    
		    tcTitle = new TableColumn(table, SWT.LEFT);
		    tcTitle.setText("Title");
		    tcTitle.setWidth(370);
		    
		    tcDate = new TableColumn(table, SWT.LEFT);
		    tcDate.setText("Date");
		    tcDate.setWidth(210);
		    
		    new TableColumn(table, SWT.NONE).setWidth(300);
		    
			setDefaultIcons();
			
		    
		    tcView.addListener(SWT.Selection, new Listener(){

				public void handleEvent(Event event) {
					currentChannel.sortByIndex();
					TABLE.fillTable(currentChannel);
					getAuthorCol().setImage(new Image(getDisplay(), Globals.ICON_TABLE_AUTHOR));
					getTitleCol().setImage(new Image(getDisplay(), Globals.ICON_TABLE_TITLE));
		    		getDateCol().setImage(new Image(getDisplay(), Globals.ICON_TABLE_DATE));
		    		colState[Globals.AUTHOR_COL] = Globals.NO_ARROW;
		    		colState[Globals.TITLE_COL] = Globals.NO_ARROW;
		    		colState[Globals.DATE_COL] = Globals.NO_ARROW;
				}
		    });
		    
		    
		    tcAuthor.addListener(SWT.Selection, new SortListener(Globals.SORT_BY_AUTHOR, Globals.AUTHOR_COL){
		    	public String setIconUP(){
		    		return Globals.ICON_TABLE_AUTHOR_UP;
		    	}
		    	public String setIconDWN(){
		    		return Globals.ICON_TABLE_AUTHOR_DOWN;
		    	}
		    	public String setIcon(){
		    		return Globals.ICON_TABLE_AUTHOR;
		    	}
		    	public TableColumn setTbCol(){
		 
		    		return getAuthorCol();
		    	}
		    	public void setDefaultIcons(){
		    		getTitleCol().setImage(new Image(getDisplay(), Globals.ICON_TABLE_TITLE));
		    		getDateCol().setImage(new Image(getDisplay(), Globals.ICON_TABLE_DATE));
		    		colState[Globals.TITLE_COL] = Globals.NO_ARROW;
		    		colState[Globals.DATE_COL] = Globals.NO_ARROW;
		    	}
		    });
		    tcTitle.addListener(SWT.Selection, new SortListener(Globals.SORT_BY_TITLE, Globals.TITLE_COL){
		    	public String setIconUP(){
		    		return Globals.ICON_TABLE_TITLE_UP;
		    	}
		    	public String setIconDWN(){
		    		return Globals.ICON_TABLE_TITLE_DOWN;
		    	}
		    	public String setIcon(){
		    		return Globals.ICON_TABLE_TITLE;
		    	}
		    	public TableColumn setTbCol(){
		    		return getTitleCol();
		    	}
		    	public void setDefaultIcons(){
		    		getAuthorCol().setImage(new Image(getDisplay(), Globals.ICON_TABLE_AUTHOR));
		    		getDateCol().setImage(new Image(getDisplay(),Globals.ICON_TABLE_DATE));
		    		colState[Globals.AUTHOR_COL] = Globals.NO_ARROW;
		    		colState[Globals.DATE_COL] = Globals.NO_ARROW;
		    	}
		    });
		    tcDate.addListener(SWT.Selection, new SortListener(Globals.SORT_BY_DATE, Globals.DATE_COL){
		    	public String setIconUP(){
		    		return Globals.ICON_TABLE_DATE_UP;
		    	}
		    	public String setIconDWN(){
		    		return Globals.ICON_TABLE_DATE_DOWN;
		    	}
		    	public String setIcon(){
		    		return Globals.ICON_TABLE_DATE;
		    	}
		    	public TableColumn setTbCol(){
		    		return getDateCol();
		    	}
		    	public void setDefaultIcons(){
		    		getAuthorCol().setImage(new Image(getDisplay(), Globals.ICON_TABLE_AUTHOR));
		    		getTitleCol().setImage(new Image(getDisplay(), Globals.ICON_TABLE_TITLE));
		    		colState[Globals.AUTHOR_COL] = Globals.NO_ARROW;
		    		colState[Globals.TITLE_COL] = Globals.NO_ARROW;
		    	}
		    });
		    
		    addSelectionListener(new SelectionListener(){
				public void widgetSelected(SelectionEvent e) {
					TableItem it = (TableItem)e.item;
					String aut = it.getText(1);
					String tit = it.getText(2);
					Item i = currentChannel.getItem_fromAuthorTitle(aut, tit);
					i.setVsito(true);
					String htmlItem = currentChannel.getParsedItem_fromAuthorTitle(aut, tit);
					it.setFont(new Font(null, "Verdana",Globals.TABLE_FONT_SIZE,	SWT.NULL));
					it.setImage(0, new Image(getDisplay(), Globals.ICON_OLD));				
					BRW.setText(htmlItem);
					setTitleLabel(currentChannel);
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					
					
				}
			});
		    
			addMouseListener(new MouseListener(){

				public void mouseDoubleClick(MouseEvent e) {
					
					TableItem[] selectedRows = TABLE.getSelection();
					for(int i=0; i<selectedRows.length; i++){
						TableItem it = selectedRows[i];
						String aut = it.getText(1);
						String tit = it.getText(2);
						Item rssItem = currentChannel.getItem_fromAuthorTitle(aut, tit);
						
						Functions.launchBrowser(rssItem.getLink());
					}
					
				}

				public void mouseDown(MouseEvent e) {}
				public void mouseUp(MouseEvent e) {}
				
			});
		    
			if(currentChannel!=null)fillTable(currentChannel);
		    
		    parent.pack();
		    
		   
		  }
		  
		  public Display getDisplay(){
			  return parent.getDisplay();
		  }
		  
		  public TableColumn getAuthorCol(){
			return tcAuthor;
		  }
		  
		  public TableColumn getTitleCol(){
				return tcTitle;
		  }
			  
		  public TableColumn getDateCol(){
				return tcDate;
		  }
			  
		    
		  public void setLayoutData(){
			  GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			  gd.grabExcessHorizontalSpace = true;
			  gd.grabExcessVerticalSpace = true;
			  gd.heightHint = 400;
			  table.setLayoutData(gd);
			  
			 int[] i = table.getColumnOrder();
			 for(int k=0; k<i.length; k++){
			 }
		  }
		  
		  public void fillTable(Channel ch){
			  ArrayList<Item> items = ch.getItems();
			  
			  table.removeAll();
			  table.setToolTipText("Double Click to open it in your browser");
			  for(int i=0; i<items.size(); i++){
				  Item item = items.get(i);
				  TableItem row = new TableItem(table, SWT.LEFT);
				  
				  if(!item.isVisto()){
					  row.setFont(new Font(null, "Verdana",Globals.TABLE_FONT_SIZE,SWT.BOLD));
				  	  row.setImage(0, new Image(parent.getDisplay(), Globals.ICON_NEW));
				  }
				  else{
					  row.setFont(new Font(null, "Verdana",Globals.TABLE_FONT_SIZE,SWT.NULL));
					  row.setImage(0, new Image(parent.getDisplay(),Globals.ICON_OLD));
			  	  }
				  row.setImage(1,new Image(parent.getDisplay(),Globals.ICON_AVATAR));
				  row.setText(1, item.getAuthor());
				  row.setText(2, item.getTitle());
				  row.setImage(2, new Image(parent.getDisplay(), item.getIconPath()));
				  GregorianCalendar gc = item.getDate();
				  
				  row.setText(3, Functions.getFormatDate(gc));
				  
				  if(i%2==0){
					  row.setBackground(parent.getShell().getDisplay().getSystemColor(SWT.COLOR_GRAY));
				  }
			  }  
		  }
		  
		  public void setDefaultIcons(){
			  tcAuthor.setImage(new Image(getDisplay(), Globals.ICON_TABLE_AUTHOR));
			  tcTitle.setImage(new Image(getDisplay(), Globals.ICON_TABLE_TITLE));
			  tcDate.setImage(new Image(getDisplay(), Globals.ICON_TABLE_DATE));
			  colState[Globals.AUTHOR_COL] = Globals.NO_ARROW;
			  colState[Globals.TITLE_COL] = Globals.NO_ARROW;
			  colState[Globals.DATE_COL] = Globals.NO_ARROW;
		  }
		  
		  public void addSelectionListener(SelectionListener sl){
			  table.addSelectionListener(sl);
		  }
		  
		  public void addMouseListener(MouseListener sl){
			  table.addMouseListener(sl);
		  }
		  
		  public TableItem[] getSelection(){
			  return table.getSelection();
		  }
		  
		  
		 
	}
	

	//This class contains methods for the sorting
	
	class SortListener implements Listener
	{
		private int type, index;
		private boolean straight = true;
		
		public SortListener(int type, int colIndex){
			this.type=type;
			this.index = colIndex;
		}
		public void handleEvent(Event e) {
			
			if(colState[index]==Globals.NO_ARROW){
				straight=true;
			}			
			
			if(currentChannel!=null){
				currentChannel = currentChannel.sort(type, straight);
			}
			
			if(straight){
				setDefaultIcons();
	    		setTbCol().setImage(new Image(TABLE.getDisplay(),setIconUP()));
	    		colState[index] = Globals.UP_ARROW;
	    		
	    	}else{
	    		setDefaultIcons();
	    		setTbCol().setImage(new Image(TABLE.getDisplay(),setIconDWN()));
	    		colState[index] = Globals.DN_ARROW;
	    	}
			
			straight = !straight;
			
			if(currentChannel!=null){
				TABLE.fillTable(currentChannel);
			}
	    	
	     }
		
		public String setIconUP(){
			return "";
		}
		public String setIconDWN(){
			return "";
		}
		public String setIcon(){
			return "";
		}
		
		public TableColumn setTbCol(){
			return null;
		}
		
		public void setDefaultIcons(){}
		
	}


}









