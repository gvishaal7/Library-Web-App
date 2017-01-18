HOME_DIR= #your tomcat/lib directory path

export CLASSPATH=$HOME_DIR/gson-2.6.1.jar:$HOME_DIR/servlet-api.jar #all the jar files you want to include in your project

JDK_HOME=/usr/bin #jdk home page

$JDK_HOME/javac -d jar validate.java
$JDK_HOME/javac -d jar adminOperations.java
$JDK_HOME/javac -d jar userOperations.java 

#create a directory as jar/com/library in the java folder
#this is where you will store your class files

cd jar
$JDK_HOME/jar -cvf com.jar com
cp com.jar ../../WEB-INF/lib
cp -rf com.jar ../../../../lib
cd ..

