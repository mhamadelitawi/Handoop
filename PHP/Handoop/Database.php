<?php
class Database 
{
	
	public $servername ;
	public $username ;
	public $password ;
	public $database ;
	public $conn;
	
function __construct( ) {
  $this->servername = "localhost";
  $this->username = ""; //put here your username
  $this->password = ""; // put here your password
  $this->database = "handoop";
}

function openConnection(){
$this->conn = new mysqli($this->servername, $this->username, $this->password, $this->database);
if ($this->conn->connect_error) {
    die("Connection failed: " . $this->conn->connect_error);
} 
}


function queryWithNoReturn($sql)
{
if ($this->conn->query($sql) === TRUE) {
	echo "";
    //echo "New record created successfully";
} else {
    echo "Error: " . $sql . " " . $this->conn->error;
}
}

function queryWithReturn($sql)
{
   return $this->conn->query($sql);
}

function closeConnection(){
	$this->conn->close();
}

}


?>
