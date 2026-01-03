call jpa_set_env.bat

call %MAVEN_HOME%/bin/mvn versions:set -DnewVersion=3.4.1

call %MAVEN_HOME%/bin/mvn versions:update-child-modules
