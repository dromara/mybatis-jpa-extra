set  MAVEN_HOME=C:\ide\apache-maven-3.8.8
set  JAVA_HOME=C:\IDE\jdk-17.0.2.8

call %MAVEN_HOME%/bin/mvn versions:set -DnewVersion=3.3.5

call %MAVEN_HOME%/bin/mvn versions:update-child-modules
