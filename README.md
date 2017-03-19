Description 
============
This project is a client/server application where the client is an Android application and the server is based on a Hadoop Infrastructure. It provides efficient Hadoop Map-Reduce implementations of many scientific computations.
This is a beta version.

Architecture:
=============
![Handoop Architecture](https://github.com/mhamadelitawi/Handoop/blob/master/architecture.png)



Algorithms and features
======================
* Selection
* Distinct selection
* Grouping and aggregation
* Union
* Intersection
* Difference 
* Sorting 
* Neighbors of Neighbors
* Vector Multiplication
* Matrix Multiplication

Instruction 
============
Before running everything : 
1. put your authorization key in : PHP\Handoop\javaphp.php
2. enter you database username and password : PHP\Handoop\Database.php
3. verify the path of jar folder needed in your java project (all jar exists in JAVA\jars)
4. enter you database username and password : JAVA\Handoop\src\Handoop\Database.java
5. put your google-services.json under Android\Handoop\app


Notes : 
=======

be careful : your appache should have the right to activate shell_exec and  run yarn framework (you can change default user of appache)

For quick test put folders in "HDFS" in your hdfs

Tested on : Hadoop 2.X

To activate your cluster : 
start-dfs.sh
start-yarn.sh


To manage database from terminal : 
mysql -u username -p


Default database : 
USE handoop;
Tables : fcmid - history


manage job history : 
mr-jobhistory-daemon.sh --config /home/hadoopuser/hadoop start historyserver
mr-jobhistory-daemon.sh --config /home/hadoopuser/hadoop stop historyserver


to build the projet in eclipse :
Handoop project >  export >  Java > Runnable jar file > OK  (Extract required libraries)


Appache should run on the master

Hadoop configuration notes can be found at : https://github.com/mhamadelitawi/Configurations

For more information feel free to contact me : mhamadelitawi@hotmail.com 
