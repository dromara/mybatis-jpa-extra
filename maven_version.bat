set  MAVEN_HOME=C:\ide\apache-maven-3.8.8
set  JAVA_HOME=C:\ide\jdk-17

call %MAVEN_HOME%/bin/mvn versions:set -DnewVersion=3.4.0

call %MAVEN_HOME%/bin/mvn versions:update-child-modules
