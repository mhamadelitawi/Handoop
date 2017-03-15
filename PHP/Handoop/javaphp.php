<?php
include 'Database.php';


function getID(){
$D = new Database();
$D->openConnection();
$sql = "SELECT * from fcmid ";
$result=$D->queryWithReturn($sql);
if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        return $row["id"]."";
    }
} 
$D->closeConnection();
return "Error";
}





function sendFCM($id , $title , $guid , $status) {


$json_data = '{ "data": { 
                  
                  "title": "'.$title.'",
                  "guid": "'.$guid.'",
                  "status": "'.$status.'" 
                },
                "notification": {
                  "title": "Handoop",
                  "body": "'.$guid.'",
                  "sound": "default",
                  "click_action": "FCM_PLUGIN_ACTIVITY",
                  "icon": "icon_name"
                },
                "to": "'.$id.'",
                "priority": "high"
              }';


$ch = curl_init();

curl_setopt($ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send');
curl_setopt($ch, CURLOPT_POST, 1);
curl_setopt($ch, CURLOPT_HTTPHEADER, array(                                                                          
                                            'Content-Type: application/json',                                                                                
                                            'Content-Length: '.strlen($json_data),
                                            'Authorization:key=AA....VcNw:APA91.......jJLkVu6' //put here your  Authorization:key
                                          ));           
curl_setopt($ch, CURLOPT_POSTFIELDS, $json_data);
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

$output = curl_exec($ch);
curl_close($ch);

//echo $json_data;
echo "done";


}


//$idDevice = "euannr2OmGw:APA91bHXsJme21XiXu7G6IkB6ezHPGtmK0Er3eAQutI-7QLGXORQBqPUYvcfoLr28SkvXtsGbbkoCPbZXNGuvus1IyEM3R0a-gsYBlKR-VlZJPuSURaE1DpUSL2--jqLlatdGicdFEns";
$idDevice = getID();
$title   = urldecode($_POST['title']);
$guid   = urldecode($_POST['guid']);
$status   = urldecode($_POST['status']);

sendFCM($idDevice , $title , $guid , $status);








?>









