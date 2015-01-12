

/* This crawler bot will accept a list of websites with their root URL as an XML/Excel file
 * The crawler bot will then iterate through each website and access every element that is
 * configured to be verified. These elements can include links, images, headers, footers, divs 
 * logos, lists, tables along with a list of configured assertions for each element type. For 
 * links the crawler will go as many nested levels as needed till it encounters circular references
 * or duplicate links. The intent is to verify that every resource is accessible. The crawler bot 
 * will store a configurable list of properties for each element alongwith the expected result. 
 * The output of the scan will be generated as an HTML report. This crawler bot can be configured 
 * to run in any environment
 */

package univision.com.crawler;

import java.awt.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class crawler 
{
	
/*Input Type can be either "csv" or "xml" */
	
	public ArrayList<resultset> finalresultset;
	public static int websitecount=0;
	public static ArrayList<crawlerbotlist> configuredlist;
	public static ArrayList<crawledwebsite> crawledsites=new ArrayList<crawledwebsite>() ;
	public static boolean botisrunning=true;
    public static  String uri;
	public static void main(String[] args)
	{
		
		/*Code to read in list of url's from a csv file or xml. Iterate through this list 
		 * to verify all links work for each website. Provide root url for each website this
		 * crawler needs to run on. For e.g http://www.univision.com.The crawler will iterate 
		 * through every website and create an in memory Data structure for a configurable set
		 * of elements and their corresponding assertions. For the first version we are looking 
		 * at images and links.The only assertions for now would be the return of a sucessfull HTTP 
		 * status code 200. These assertions will be configurable for each element type and can be 
		 * expanded to include other elements and assertions as needed in the future. 
		 */
		
		String inputtype="";
		String inputpath="";
		
	     for(int i = 0; i < args.length; i++) 
	     {
	            inputtype=args[0];
	            inputpath=args[1];
	     } 
	     
	            
	     if (inputtype.toLowerCase()=="csv") 
	     {
	    	 
	    	 /* Call method to parse excel and create a list of root URL's, elements and assertions for each*/
	    	 parsecsvfile(inputpath);
	    	 
	     }
	     
	     else if (inputtype.toLowerCase()=="xml") 
	    	 
	     {
	    	 
	    	 /* Call method to parse XML file and create a list of root URL's, elements and assertions for each*/
	     }
	    	 	    	 
	     configuredlist= parsecsvfile("C:/test.csv");
	     for(int i=0;i<configuredlist.size();i++)
	     {
	    	 uri=configuredlist.get(i).websiteurl;
	    	 String elementlist=configuredlist.get(i).elementype;
	    	 String assertionlist=configuredlist.get(i).assertiontype;
	    	// rootdomreader(uri,elementlist, assertionlist);
	    	 rootdomreader(uri);
	     }
	
	}
	

	
	//public static void rootdomreader(String url, String elementlist, String assertionlist)
	public static boolean isexistchecker(String s)
	{
		boolean value=true;
		if(crawledsites.size()>0)
		{
		for(int i=0; i<crawledsites.size();i++)
		 {
			if(s.toLowerCase()==crawledsites.get(i).site.toLowerCase())
			{
				value=true;

			}
		 }
	    return value;
		}
		else
		{
			return value;
		}
	}
	
	public static String nextsitetocrawl()
	{

		if(crawledsites.size()>0)
		{
		for(int i=0; i<crawledsites.size();i++)
		 {
			if(crawledsites.get(i).iscrawled==false)
			{
				
				return crawledsites.get(i).site;

			}
		 }
		}
		else
		{
		    botisrunning=false;
			return "Error: Size of List Array expected to be greater than zero";
		}
		botisrunning=false;
		return "All sites crawled based on the root URL provided";
	}
	
	
		
	public static void rootdomreader(String url)
	{
     while(botisrunning==true)
     {
       URL testurl;
	   String inputLine="";
       BufferedReader in;
       String xmlstring="";
       crawledwebsite site;
       String next_site;
       boolean contains ;
       boolean shouldbecrawled=true; 
       boolean containsyahoo;
       boolean containsgoogle;
       boolean containsfacebook;
       boolean containstwitter;
       boolean containsgtalk;
       try 
       {

   	if(crawledsites.size()==0)
   	{
	       site=new crawledwebsite();
	       site.site=url;
	       site.iscrawled=true;
	       crawledsites.add(site);
   	}
   	else
		{
	       boolean isexist;
	       isexist=isexistchecker(url);
		for(int i=0; i<crawledsites.size();i++)
		 {
			if(crawledsites.get(i).site.toLowerCase().equals(url.toLowerCase()) && crawledsites.get(i).iscrawled==false)
			{
				
				crawledsites.get(i).iscrawled=true;
                break;
			}
			else if(crawledsites.get(i).site.toLowerCase().equals(url.toLowerCase()) && crawledsites.get(i).iscrawled==true)
			{
                shouldbecrawled=false;
                break;
			}
			
		 }
		}
   	
   	if(shouldbecrawled==true)
   	{
       testurl = new URL(url);
 	   in = new BufferedReader(new InputStreamReader(testurl.openStream()));

 	   while ((inputLine = in.readLine()) != null)
 	   {
 	   xmlstring=xmlstring+inputLine;
 	   }
       in.close();
	   Document document = Jsoup.parse(xmlstring); 
	   System.out.println(document.getElementsByTag("h1"));
	   for (org.jsoup.nodes.Element element : document.getElementsByTag("img")) 
	   {
	     //  System.out.println(element.attr("src"));
	     //  System.out.println(element.attr("href"));
	   }

          

	   for (org.jsoup.nodes.Element element : document.getElementsByTag("a")) 
	   {

	       boolean isexists=false;
	       site=new crawledwebsite();
	       site.site=element.attr("href");
	       contains = site.site.contains("javascript");
	       containsyahoo=site.site.contains("yahoo");
	       containsgoogle = site.site.contains("google");
	       containsfacebook=site.site.contains("facebook");
	       containstwitter = site.site.contains("twitter");
	       containsgtalk=site.site.contains("gtalk");
	       if(!"/".equals(site.site) && !"#".equals(site.site) && !" ".equals(site.site) && !"".equals(site.site) && site.site!=null && contains!=true  && containsyahoo!=true  && containsgoogle!=true  && containsfacebook!=true  && containstwitter!=true  && containsgtalk!=true )
	       {
	    	   if(site.site.substring(0,1).equals("/") ||site.site.substring(0,1).equals("#"))
	    			   {
	    		   if (site.site.substring(1,2).equals("/"))
	    		   {
	    			   site.site="http:"+site.site;
	    			   
	    		   }
	    		   else
	    		   {
	    		        site.site=uri+site.site;
	    		   }
	    			   }
	   		for(int i=0; i<crawledsites.size();i++)
			 {
				if(crawledsites.get(i).site.toLowerCase().equals(site.site.toLowerCase()))
				{
					
					isexists=true;
					break;

				}
			 }
	   		
	   		if(isexists==false)
	   		{
	       crawledsites.add(site);
	   		}
	       }
	     }
   	}
	       System.out.println(String.valueOf(crawledsites.size()));
	       next_site=nextsitetocrawl();
	       if(!"All sites crawled based on the root URL provided".equals(next_site))
	       {
	       System.out.println("NEXT SITE: "+next_site);
	       rootdomreader(next_site);
	       }
	       else
	       {
		       System.out.println("All sites have been crawled!"); 
	       }

	       
	   }
	   

       catch (IOException e1) 
       {
        next_site=nextsitetocrawl();
        rootdomreader(next_site);
	    e1.printStackTrace();
       }
     }
	}
	
	/*
	private static void parseXmlFile(String xmldom )
	
	{
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	   	DocumentBuilder db;
		List listA = new List();
		try 
		{

			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		    InputSource is = new InputSource();
		    is.setCharacterStream(new StringReader(xmldom));
			 try 
			 {
				Document doc = db.parse(is);
				System.out.println(doc.toString());
			 } 
			 catch (SAXException | IOException e) 
			 {
				e.printStackTrace();
			 }
		} 
		catch (ParserConfigurationException e) 
		{

			e.printStackTrace();
		}


	}
	
	*/
	
	private static 	ArrayList<crawlerbotlist> parsecsvfile(String filepath)
	{
		
		/* This subroutine will parse an csv file and return an array that contains 
		 * objects which have website URL's, elements that need to be verified and 
		 * the assertions configured for each */
		
		String csvFile = filepath;
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		ArrayList<crawlerbotlist> websitelist=new ArrayList<crawlerbotlist>();
		
		try 
		{
	 
			br = new BufferedReader(new FileReader(csvFile));
			
			while ((line = br.readLine()) != null) 
			{	 
				// use comma as separator
				String[] lines = line.split(cvsSplitBy);
				crawlerbotlist items=new crawlerbotlist();
				items.identifier=websitecount;
				items.websiteurl=lines[0];
				items.elementype=lines[1];
				websitelist.add(items);	 
				websitecount++;
		     }
	 
		 } 
		
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		finally 
		{
			if (br != null) 
			{
				try 
				{
					br.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	 
		return websitelist;
	}
	
	public static void gethttpresponsse(String testurl)
	{
		URL url;
		HttpURLConnection connection;
		try {
			url = new URL(testurl);
			try 
			    {
				connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("GET");
				connection.connect();
				int code = connection.getResponseCode();
			    } 
			catch (IOException e) 
			    {
				e.printStackTrace();
			    }

		   } 
		  catch (MalformedURLException e) 
		   {
			e.printStackTrace();
		   }

	}

	
	private static String imageverifier(String imageurl) 
	
	{
		/*  Write code that will identify every configured element attributes and assertions for each */
		String imagelink=imageurl;
		
		String result="";
		return result;
        
	}
	
	/* This subroutine does a get on a url and verifies if the correct http status code is returned. Gathers information such as title and passes the url
	    to domreader subroutine to parse the dom and*/ 
	private static void urlverifier(String url) 
	
	{
     /* Write code that will identify every element thats configured in the list 
      * attributes and again c
      */
        
		
	}
	
	private static void reportgenerator(List resultlist)
	{
	     /* Write code that will generate an HTML report that 
	      * will display the list elements that will have information
	      * on each element that was tested for alongwith the title of the 
	      * page that they are present in and the result
	      */
		
	}

}
