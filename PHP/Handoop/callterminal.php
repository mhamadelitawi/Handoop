<?php

//echo shell_exec(dirname(__FILE__) . '/test.sh');
#zabart passthru ('/home/hadoopuser/hadoop/bin/hadoop jar /home/hadoopuser/Desktop/Handoop.jar' , $z);
shell_exec ('/home/hadoopuser/hadoop/bin/hadoop jar /home/hadoopuser/Desktop/Handoop.jar');
#echo "z === ".$z;
#echo dirname(__FILE__) . '/test.sh';
//echo dirname(__FILE__);
//$output = exec('/var/www/html/Handoop/test.sh');
//echo "output = " . $output;
//$output = shell_exec('ls');
//echo $output;
//$output = shell_exec('cat hi');
//echo $output;
//chdir($old_path);


//shell_exec( "./test" . "> /dev/null 2>/dev/null &" );

?>
<?//php phpinfo(); ?>
