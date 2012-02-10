package core;
/**
 * This Class is used to encapsulate the dates that the file.rss (2.0) 
 * gives us in the format : Sat, 07 Sep 2002 00:00:01 GMT
 * 
 * More info @ http://asg.web.cmu.edu/rfc/rfc822.html
 * 
 * Nicolò Paternoster - Francesco Tudisco - Giorgio Natili
 * 2007
*/
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


@SuppressWarnings("serial")
public class DateWrapper  extends GregorianCalendar{
	
	public DateWrapper(String pubdate){
		//this.pubdate = pubdate;
		
		String[] date = splitDate(pubdate);
		String[] time = splitTime(date[0]);
		
		int anno = new Integer(date[4]).intValue();
		int mese = getMonthNum(date[3]);
		int giorno = new Integer(date[2]).intValue();
		
		int ore = new Integer(time[0]).intValue();
		int min = new Integer(time[1]).intValue();
		int sec = new Integer(time[2]).intValue();
		
		
		this.set(anno, mese, giorno, ore, min, sec);
	}
	
	public Date getDate(){
		return getTime();
	}
	
	
	public String[] splitDate(String pd){
		String[] str = new String[5];
		
		String[] u = pd.split("@");
		
		str[0] = u[0].trim();
		String[] str2 = u[1].split(" ");
		
		str[1] = str2[1].trim().substring(0, str2[1].length()-1);
		
		str[2] = str2[2].trim(); str[3] = str2[3].trim(); str[4] = str2[4].trim();
		
		return str;
	}
	
	
	public String[] splitTime(String time){
		String[] s = time.split(":");
		//Functions.msg("split1="+s[0]+" spli2 "+s[1]+" split3 "+s[2] );
		return s;
	}
	
	public int getMonthNum(String month){
		if(month.equalsIgnoreCase("Jan")){
			return Calendar.JANUARY;
		}
		else if(month.equalsIgnoreCase("Feb")){
			return Calendar.FEBRUARY;
		}
		else if(month.equalsIgnoreCase("Mar")){
			return Calendar.MARCH;
		}
		else if(month.equalsIgnoreCase("Apr")){
			return Calendar.APRIL;
		}
		else if(month.equalsIgnoreCase("May")){
			return Calendar.MAY;
		}
		else if(month.equalsIgnoreCase("Jun")){
			return Calendar.JUNE;
		}
		else if(month.equalsIgnoreCase("Jul")){
			return Calendar.JULY;
		}
		else if(month.equalsIgnoreCase("Aug")){
			return Calendar.AUGUST;
		}
		else if(month.equalsIgnoreCase("Sep")){
			return Calendar.SEPTEMBER;
		}
		else if(month.equalsIgnoreCase("Oct")){
			return Calendar.OCTOBER;
		}
		else if(month.equalsIgnoreCase("Nov")){
			return Calendar.NOVEMBER;
		}
		else if(month.equalsIgnoreCase("Dec")){
			return Calendar.DECEMBER;
		}
		else return -1;
	}
	
}

