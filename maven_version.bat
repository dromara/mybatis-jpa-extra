set  MAVEN_HOME=D:\IDE\apache-maven-3.9.1
set  JAVA_HOME=D:\IDE\jdk-17.0.2.8

call %MAVEN_HOME%/bin/mvn versions:set -DnewVersion=3.2.1

call %MAVEN_HOME%/bin/mvn versions:update-child-modules
