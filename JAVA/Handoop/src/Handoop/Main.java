package Handoop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Date;
import java.sql.PreparedStatement;
import org.apache.hadoop.util.ToolRunner;

import algebra.GroupAndSum;
import algebra.Selection;
import algebra.Union;
import algebra.selectDistinct;
import twitter.GetMyFollowingOnly;
import twitter.GetRelated;
import twitter.Suggestion;
import twitter.TrendsHashtags;
import twitter.Utils;


public class Main {

	//inputJson
	static String inputJson = "json";
	static String nodesPath = "mininodes";
	static String downloadPath = "/var/www/html/Handoop/download/";
	static String pathResult = "download/";
	
	static String idNexus = "fUg9hKwzYbw:APA91bGCCQ_jHLi8htJqe3pvKXF2MpQWXzZ8B1Y4ILwdmkZh52AiIagxdj4bivmnLtDJPo6rTs1YinSBwIXhCIgh6fhj6fLU6iWv3k24VH77G3wHJecc6duC7NqKMogTILxOdMd_4Llt";

	
static void insertIntoResults(String guid , String preview , String results)
{
	Database d = new Database();
	String sql = "Insert into results values ('"+guid+"' , '"+preview+"' , '"+results+"' )";
	d.queryWithNoResult(sql);
	d.terminer();
}	
	

static void insertIntohistorynomal(String guid ,  String task , String title , String preview , String pathResult , long time)
{
Database d = new Database();
String sql = "Insert into history values ('"+guid+"' , '"+task+"'  ,'"+title+"'  , '"+preview+"' , '"+pathResult+"'  , '"+time+"'   )";
d.queryWithNoResult(sql);
d.terminer();
}	



static void insertIntohistory(String guid ,  String task , String title , String preview , String pathResult , long time)
{
	try{
Database d = new Database();
String sql = "Insert into history values (? , ?  ,?  , ? , ?  , ?   )";
d.ps = d.cnx.prepareStatement(sql);
d.ps.setString(1, guid) ;
d.ps.setString(2, task) ;
d.ps.setString(3, title) ;
d.ps.setString(4, preview) ;
d.ps.setString(5, pathResult) ;
d.ps.setString(6, time+"") ;
d.ps.executeUpdate();
d.terminer();
	}
	catch(Exception e){}
}

	

static void jobSelection(String input ,  String selection)
{
	System.out.println("JOB SELECTION FROM " + input);
	String[] var = { input, input+"SelectionOut", selection }; 
	try{
	ToolRunner.run(new Selection() , var );
	}catch(Exception e)
	{
		System.out.println(e.getMessage());
	}
}



static void jobSelectDistinct(String input )
{
	System.out.println("JOB SELECTION Distinct FROM " + input);
	String[] var = { input, input+"selectDistinctOut" }; 
	try{
	ToolRunner.run(new selectDistinct() , var );
	}catch(Exception e)
	{
		System.out.println(e.getMessage());
	}
}




static void jobSelectGroupAndSum(String input )
{
	System.out.println("JOB Word Count FROM " + input);
	String[] var = { input, input+"selectGroupAndSum" }; 
	try{
	ToolRunner.run(new GroupAndSum() , var );
	}catch(Exception e)
	{
		System.out.println(e.getMessage());
	}
}




static void jobUnion(String input )
{
	System.out.println("JOB UNION " + input);
	String[] var = { input, input+"Union" }; 
	try{
	ToolRunner.run(new Union() , var );
	}catch(Exception e)
	{
		System.out.println(e.getMessage());
	}
}



static void testBenchmark()
{
	jobSelection("4.txt" , "b,f");
	jobSelection("121.txt" , "b,f");
	jobSelection("213.txt" , "b,f");
	jobSelection("405.txt" , "b,f");
	jobSelection("843.txt" , "b,f");
	jobSelection("1199.txt" , "b,f");
	jobSelection("1557.txt" , "b,f");
	jobSelection("3144.txt" , "b,f");
	jobSelection("6228.txt" , "b,f");
	jobSelection("12456.txt" , "b,f");
	jobSelection("24912.txt" , "b,f");
	System.out.println("done selection");

	
	jobSelectDistinct("4.txt");
	jobSelectDistinct("121.txt" );
	jobSelectDistinct("213.txt" );
	jobSelectDistinct("405.txt" );
	jobSelectDistinct("843.txt" );
	jobSelectDistinct("1199.txt" );
	jobSelectDistinct("1557.txt" );
	jobSelectDistinct("3144.txt" );
	jobSelectDistinct("6228.txt" );
	jobSelectDistinct("12456.txt" );
	jobSelectDistinct("24912.txt" );
	System.out.println("done jobSelectDistinct");
	

	jobSelectGroupAndSum("4.txt");
	jobSelectGroupAndSum("121.txt" );
	jobSelectGroupAndSum("213.txt" );
	jobSelectGroupAndSum("405.txt" );
	jobSelectGroupAndSum("843.txt" );
	jobSelectGroupAndSum("1199.txt" );
	jobSelectGroupAndSum("1557.txt" );
	jobSelectGroupAndSum("3144.txt" );
	jobSelectGroupAndSum("6228.txt" );
	jobSelectGroupAndSum("12456.txt" );
	jobSelectGroupAndSum("24912.txt" );
	System.out.println("done jobSelectGroupAndSum");
	
	for (int i = 1 ; i <= 10 ; i++)
	{
		jobUnion(""+i);
	}
	System.out.println("done jobUnion");
	
	
}


public static void getDifferentUser( String nodesLocation , long date)
{
	try{
		String guid = "outDU" + date;
		String outFile = downloadPath+guid+".txt";
		Suggestion.runDifferentUser( nodesLocation,  guid);
		Tools.copyFromHDFSToLocal(guid+"/part-r-00000", outFile);
		
		BufferedReader br = new BufferedReader(new FileReader(outFile));
		String preview = "";
		int i = 0 ;
		while (br.ready() && i<40)
		{
			String line = br.readLine();
			preview += line+"$!$!$";
			i++;
		}
		br.close();	
		
		insertIntohistory(guid , "getDifferentUser" ,"Different users" , preview , pathResult+guid , date);
		
		Tools.callThePush("title" ,  guid , "status");
		
		
		
		}catch (Exception e)
		{
			System.out.println(e.getMessage());
			Tools.callThePush("get Different User" , "Error" , "Error in getting all usernames");
			return;
		}
	
	
	
		
}

public static void getMyFollowing( String nodesLocation , long date , String user )
{
	String guid = "outMFO" + date;
	String outFile = downloadPath+guid+".txt";
	try{
	GetMyFollowingOnly.runGetMyFollowingOnly(nodesLocation, guid, user , "yes");
	Tools.copyFromHDFSToLocal(guid+"/part-m-00000", outFile);
	BufferedReader br = new BufferedReader(new FileReader(outFile));
	String preview = "";
	int i = 0 ;
	while (br.ready() && i<40)
	{
		String line = br.readLine();
		preview += line+"$!$!$";
		i++;
	}
	br.close();	
	if(preview.equals("") || preview.equals("$!$!$")) preview = "This user follow anyone";
	System.out.println(preview);
	insertIntohistory(guid , "getMyFollowing" ,"Following of "+user , preview , pathResult+guid , date);
	
	Tools.callThePush("title" ,  guid , "status");
	
	}catch(Exception e){
		System.out.println(e.getMessage());
	Tools.callThePush("Following of "+user , "Error" , "Error in getting the following of "+user);
		return;
	}
}	

public static void getMyFollower( String nodesLocation , long date , String user )
{
	String guid = "outMFR" + date;
	String outFile = downloadPath+guid+".txt";
	try{
	GetMyFollowingOnly.runGetMyFollowingOnly(nodesLocation, guid, user , "no");
	Tools.copyFromHDFSToLocal(guid+"/part-m-00000", outFile);
	BufferedReader br = new BufferedReader(new FileReader(outFile));
	String preview = "";
	int i = 0 ;
	while (br.ready() && i<40)
	{
		String line = br.readLine();
		preview += line+"$!$!$";
		i++;
	}
	br.close();	
	if(preview.equals("") || preview.equals("$!$!$")) preview = "This user has no follower";
	System.out.println(preview);
	insertIntohistory(guid , "getMyFollower" ,"Follower of "+user , preview , pathResult+guid , date);
	
	Tools.callThePush("title" ,  guid , "status");
	
	}catch(Exception e){
		System.out.println(e.getMessage());
	Tools.callThePush("Follower of "+user , "Error" , "Error in getting the follower of "+user);
		return;
	}
}	

public static void getSuggestion( String nodesLocation , long date , String user )
{
	String guid = "outGS" + date;
	String outFile = downloadPath+guid+".txt";
	String DifferentUser = guid+"/DifferentUser";
	String FillFollowers = guid+"/FillFollowers";
	String SameFollowers = guid+"/SameFollowers";
	String SameFollowersSorted = guid+"/SameFollowersSorted";
	
	try{
	Suggestion.runDifferentUser( nodesLocation,  DifferentUser);
	Suggestion.runFillFollowers( nodesLocation,  DifferentUser,  FillFollowers);
	Suggestion.runSameFollowers(user, FillFollowers, SameFollowers);
	Suggestion.runFollowersSorted(user, SameFollowers, SameFollowersSorted);
	
	Tools.copyFromHDFSToLocal(SameFollowersSorted+"/part-r-00000", outFile);
	
	BufferedReader br = new BufferedReader(new FileReader(outFile));
	String preview = "";
	int i = 0 ;
	while (br.ready() && i<40)
	{
		String line = br.readLine();
		preview += line+"$!$!$";
		i++;
	}
	br.close();	
	
	insertIntohistory(guid , "getSuggestion" ,"Suggestion for "+user , preview , pathResult+guid , date);
	
	Tools.callThePush("title" ,  guid , "status");
	
	
	}catch(Exception e){
		System.out.println(e.getMessage());
	Tools.callThePush("Suggestion for "+user , "Error" , "Error in getting "+user);
		return;
		}
		
}
	
public static void viewTrendingHashtag( String inputJson , long date)
{
	
	String guid = "outTH" + date;
	String outFile = downloadPath+guid+".txt";
	String tempPathHashtag = guid+"/tempPathHashtag";
	String outputJson = guid+"/outputJson";
	try{
	TrendsHashtags.runTrendingHashtag(inputJson,  tempPathHashtag,  outputJson) ;
	Tools.copyFromHDFSToLocal(outputJson+"/part-r-00000", outFile);
	BufferedReader br = new BufferedReader(new FileReader(outFile));
	String preview = "";
	int i = 0 ;
	while (br.ready() && i<40)
	{
		String line = br.readLine();
		preview += line+"$!$!$";
		i++;
	}
	br.close();	
	insertIntohistory(guid , "viewTrendingHashtag" ,"Trending Hashtag" , preview , pathResult+guid , date);
	Tools.callThePush("title" ,  guid , "status");
	}catch (Exception e)
	{
		System.out.println(e.getMessage());
		Tools.callThePush("get trending hashtags" , "Error" , "Error in getting all trending hashtags");
		return;
	}
}




public static void relatedTweet( String nodesLocation , long date , String keys )
{
	String guid = "outGRK" + date;
	String outFile = downloadPath+guid+".txt";
	try{
	GetRelated.runGetRelated(nodesLocation, guid, keys);
	Tools.copyFromHDFSToLocal(guid+"/part-m-00000", outFile);
	BufferedReader br = new BufferedReader(new FileReader(outFile));
	String preview = "";
	int i = 0 ;
	while (br.ready() && i<40)
	{
		String line = br.readLine();
		preview += line+"$!$!$";
		i++;
	}
	br.close();	
	if(preview.equals("") || preview.equals("$!$!$")) preview = "No tweets for the entered key";
	System.out.println(preview);
	insertIntohistory(guid , "relatedTweet" ,"Related Tweets for "+keys , preview , pathResult+guid , date);
	
	Tools.callThePush("title" ,  guid , "status");
	
	}catch(Exception e){
		System.out.println(e.getMessage());
	Tools.callThePush("Related Tweets for "+keys , "Error" , "Error in getting the tweets for "+keys);
		return;
	}
}



public static void main (String[] args) throws Exception 
	{
// *-$-%
	
	
	long d = new Date().getTime();
	switch (args[0])
	{
	case "getDifferentUser" :
		getDifferentUser(nodesPath , d ); break;
	case "getMyFollowing" :
		getMyFollowing(nodesPath , d , args[1] ); break;
	case "getMyFollower" :
		getMyFollower(nodesPath , d , args[1] ); break;
	case "getSuggestion" :
		getSuggestion(nodesPath , d , args[1] ); break;
	case "viewTrendingHashtag" :
		viewTrendingHashtag(inputJson , d ); break;
	case "relatedTweet" :
		relatedTweet(inputJson , d ,  args[1]); break;
		
		
	default: 

	
	
	}
	
	
	
	
		
	}
}
