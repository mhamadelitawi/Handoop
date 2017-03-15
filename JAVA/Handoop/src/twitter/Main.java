package twitter;


import java.io.BufferedReader;
import java.io.InputStreamReader;

//import java.io.BufferedReader;

//import java.io.InputStreamReader;

import org.apache.hadoop.util.ToolRunner;

public class Main {

	public static void menu() {
		System.out.println("");
		System.out.println("---------Twitter Analytics------------");
		System.out.println("Please choose one of these options : ");
		System.out.println("1- View trending hashtags");
		System.out.println("2- Get Tweets Related to a hashtag");
		System.out.println("3- Sorted Words used with a hashtag or word");
		System.out.println("4- View Users");
		System.out.println("5- View User followed by a user");
		System.out.println("6- Get Suggested users");
		System.out.println("7- change parameters");
		System.out.println("8- end");
		System.out.println("");
	}
	
	

	public static String inputJson = "js";
	public static String outputJson = "outputJson";
	public static String tempPathHashtag = "tempPathHashtag";
	public static String outputRelatedWordsHashtags = "outputRelatedHashtags";
	public static String nodesLocation = "mininodes";
	public static String DifferentUser = "DifferentUser";
	public static String FillFollowers = "FillFollowers";
	public static String SameFollowers = "SameFollowers";
	public static String SameFollowersSorted = "SameFollowersSorted";
	public static String user;	
	
	public static boolean FillFollowersDone = false;
	public static boolean DifferentUserDone = false;
	
    public static void DeleteCache()
{
	Utils.deleteDestinationHDFS(outputJson);
	Utils.deleteDestinationHDFS(tempPathHashtag);
	Utils.deleteDestinationHDFS(outputRelatedWordsHashtags);
	Utils.deleteDestinationHDFS(DifferentUser);
	Utils.deleteDestinationHDFS(FillFollowers);
	Utils.deleteDestinationHDFS(SameFollowers);
	Utils.deleteDestinationHDFS(SameFollowersSorted);
	
}
    
    
    
    

	public static int viewTrendingHashtags() {
		String[] var = { inputJson, tempPathHashtag, outputJson };
		
		try {
			ToolRunner.run(new TrendsHashtags(), var);
			
			System.out.println("Hashtags     Number");
			Utils.readFileHDFS(outputJson+"/part-r-00000");
			
			return 0;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return -1;
		}
	}

	public static int getRelatedHashtags( String hash) {
		String[] var = { inputJson, outputRelatedWordsHashtags  , hash};
		
		try {
			ToolRunner.run(new RelatedHashtags(), var);
			Utils.readFileHDFS(outputRelatedWordsHashtags+"/part-r-00000");
		    
			return 0;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return -1;
		}
	}

	public static int getRelatedWords(String key)
	{
		
		String[] var = { inputJson, outputRelatedWordsHashtags  , key};
		
		try {
			ToolRunner.run(new RelatedWords(), var);
			Utils.readFileHDFS(outputRelatedWordsHashtags+"/part-r-00000");
			
			return 0;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return -1;
		}
	}
	
	public static boolean viewUsers()
	{
		
		try{
		Suggestion.runDifferentUser( nodesLocation,  DifferentUser);
		Utils.readFileHDFS(DifferentUser+"/part-r-00000");
		DifferentUserDone = true;
		}catch (Exception e)
		{
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	public static boolean viewFollowing()
	{
		
		try{
			Suggestion.runDifferentUser( nodesLocation,  DifferentUser);
			Suggestion.runFillFollowers( nodesLocation,  DifferentUser,  FillFollowers);
			Utils.readFileHDFS(FillFollowers+"/"+user+"-r-00000");
			FillFollowersDone = true;
			}catch (Exception e)
			{
				System.out.println(e.getMessage());
				return false;
			}
			return true;
	}
	
	public static boolean getSuggestedUSer()
	{
		try{
			Suggestion.runDifferentUser( nodesLocation,  DifferentUser);
			Suggestion.runFillFollowers( nodesLocation,  DifferentUser,  FillFollowers);
			Suggestion.runSameFollowers(user, FillFollowers, SameFollowers);
			Suggestion.runFollowersSorted(user, SameFollowers, SameFollowersSorted);
			Utils.readFileHDFS(SameFollowersSorted+"/part-r-00000");
			}catch (Exception e)
			{
				System.out.println(e.getMessage());
				return false;
			}
			return true;
	}
	
	public static String oneChange(String change , String value)
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("do you want to change "+change +" ?" );
		System.out.println("Current value is : "+value);
		System.out.println("press enter for skip");
		try {
			return br.readLine();
		} catch (Exception e) {
			return "";
		}
	}
	
	public static void changeParameter()
	{
		
		try
		{
			String tmp = oneChange("input Json Directory " , inputJson);
			if (!tmp.equals(""))
				inputJson = tmp;
			
			tmp = oneChange("output Json Directory " , outputJson);
			if (!tmp.equals(""))
				outputJson = tmp;
			
			
			tmp = oneChange("output Related Hashtags or words" , outputRelatedWordsHashtags);
			if (!tmp.equals(""))
				outputRelatedWordsHashtags = tmp;
			
			tmp = oneChange("nodes Directory " , nodesLocation);
			if (!tmp.equals(""))
				nodesLocation = tmp;
			
			tmp = oneChange("Different User Directory " , DifferentUser);
			if (!tmp.equals(""))
				DifferentUser = tmp;
			
			tmp = oneChange("Users Following Directory" , FillFollowers);
			if (!tmp.equals(""))
				FillFollowers = tmp;
			
			
			tmp = oneChange("Suggested followers Directory" , SameFollowersSorted);
			if (!tmp.equals(""))
				SameFollowersSorted = tmp;
			
			
			
		}catch(Exception e){}
		
		
	}
	
	
	
	

	public static void main(String[] args) {
		String hashtags ;
		
		int choice = 0;
		
		
		do {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			menu();
			
			System.out.println("Choose a number from the menu");
		choice = Integer.parseInt(br.readLine());
		
		switch (choice) {
		case 1:
			DeleteCache();
			viewTrendingHashtags( );
			break;
		case 2:
			DeleteCache();
			System.out.println("Enter hashtags");
			try{
			hashtags = br.readLine();
			
			getRelatedHashtags( hashtags);
			}
			catch (Exception e) {}
			break;
		case 3:
			DeleteCache();
			System.out.println("Enter Words");
			try{
			hashtags = br.readLine();
			
			getRelatedWords(  hashtags);
			}
			catch (Exception e) {}
			break;
		case 4: 
			DeleteCache();
			viewUsers(  );
			break;
		case 5:
			DeleteCache();
			System.out.println("Enter User");
			try{
				user = br.readLine();
				
			viewFollowing();
			}
			catch (Exception e) {}
			break;
		case 6:
			DeleteCache();
			try{
				System.out.println("Enter User");
				user = br.readLine();
				
				
				getSuggestedUSer();
				
				
				
				}catch (Exception e) {}
			break;
		case 7:
			DeleteCache();
			changeParameter();
			break;
		case 8:
			break;
		default: 
			System.out.println("choose a number between 1 and 8");
			break;
		}
		}catch(Exception ex){choice = 0;}
		}while(choice != 8);
		
	}

}
