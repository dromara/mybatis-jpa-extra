set  MAVEN_HOME=D:\IDE\apache-maven-3.8.8
set  JAVA_HOME=D:\IDE\jdk-17.0.2.8

call %MAVEN_HOME%/bin/mvn versions:set -DnewVersion=3.3.0

call %MAVEN_HOME%/bin/mvn versions:update-child-modules
