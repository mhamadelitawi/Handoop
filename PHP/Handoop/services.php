<?php
include 'Database.php';


function signIn()
{
    echo "signIns";
}

function register()
{
	$name   = urldecode($_POST['name']);
	$lastName   = urldecode($_POST['lastName']);
	$username   = urldecode($_POST['username']);
	$password   = urldecode($_POST['password']);
	$pushId   = urldecode($_POST['pushId']);

$D = new Database();
$D->openConnection();
$sql = "INSERT INTO user VALUES ('".$name."', '".$lastName."', '".$username."', '".$password."', '".$pushId."')";
$result=$D->queryWithNoReturn($sql);
$D->closeConnection();	
echo $result;
}



function getDifferentUser()
{

	shell_exec ('/home/hadoopuser/hadoop/bin/hadoop jar /home/hadoopuser/Desktop/Handoop.jar getDifferentUser > /dev/null 2>/dev/null &');
	echo "itawi" ;
}



function getResult(){
$guid   = urldecode($_POST['guid']);
$D = new Database();
$D->openConnection();
$sql = "SELECT * from history where guid = '".$guid."'";
//$sql = "SELECT * from results ";
//echo $sql;
$result=$D->queryWithReturn($sql);
if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        echo $row["guid"]."&#$#&".$row["task"]."&#$#&".$row["title"]."&#$#&".$row["preview"]."&#$#&".$row["pathresult"]."&#$#&".$row["time"];
    }
} 
$D->closeConnection();
}


function changeFCMID($id)
{
//$id   = urldecode($_POST['id']);
$D = new Database();
$D->openConnection();
$sql = "Delete from fcmid";
$result=$D->queryWithNoReturn($sql);
$sql = "INSERT INTO fcmid VALUES ('".$id."')";
$result=$D->queryWithNoReturn($sql);
$D->closeConnection();  
//echo "done";
}



function getMyFollowing()
{
  $user   = urldecode($_POST['user']);
  shell_exec ('/home/hadoopuser/hadoop/bin/hadoop jar /home/hadoopuser/Desktop/Handoop.jar getMyFollowing '.$user.' > /dev/null 2>/dev/null &');
  echo "itawi" ;
}

function getMyFollower()
{
  $user   = urldecode($_POST['user']);
  shell_exec ('/home/hadoopuser/hadoop/bin/hadoop jar /home/hadoopuser/Desktop/Handoop.jar getMyFollower '.$user.' > /dev/null 2>/dev/null &');
  echo "itawi" ;
}


function getSuggestion()
{
  $user   = urldecode($_POST['user']);
  shell_exec ('/home/hadoopuser/hadoop/bin/hadoop jar /home/hadoopuser/Desktop/Handoop.jar getSuggestion '.$user.' > /dev/null 2>/dev/null &');
  echo "itawi" ;
}


function viewTrendingHashtag()
{
  shell_exec ('/home/hadoopuser/hadoop/bin/hadoop jar /home/hadoopuser/Desktop/Handoop.jar viewTrendingHashtag > /dev/null 2>/dev/null &');
  echo "itawi" ;
}



function relatedTweet()
{
  $keys   = urldecode($_POST['keys']);
  shell_exec ('/home/hadoopuser/hadoop/bin/hadoop jar /home/hadoopuser/Desktop/Handoop.jar relatedTweet '.$keys.' > /dev/null 2>/dev/null &');
  echo "itawi" ;
}



$id   = urldecode($_POST['id']);
changeFCMID($id);


$serviceRequest   = urldecode($_POST['serviceRequest']);


switch ($serviceRequest)
  {
       case "signIn" :  signIn(); break;
       case "register" :  register(); break;
       case "getDifferentUser" :  getDifferentUser(); break; 
       case "getResult" : getResult(); break;
       case "getMyFollowing" :  getMyFollowing(); break;
       case "getMyFollower" :  getMyFollower(); break;
       case "getSuggestion" :  getSuggestion(); break;
       case "relatedTweet" :  relatedTweet(); break;
       case "viewTrendingHashtag" : viewTrendingHashtag(); break;
}
       








?>
