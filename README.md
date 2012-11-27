javasendmail
============

javasendmail is a simple commandline tool to send emails via smtp servers and test if there any problem.

compiling 
mvn install

example usage
java -jar SendEmail.jar -b test -from who@com.com -smtp localhost -s title -to hello@body.com