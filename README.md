# blog-xmlunit
Example java code using XMLUnit.

This is example code used in blog "Compering XML parts containing dynamic values by use of wildcards in XMLUnit" published on
http://osmanmrzljak.blogspot.nl/2016/02/compering-xml-parts-containing-dynamic.html

## Import project in IntelliJ
- Clone this repository
- In Intellij got to Open - ... en Select build.gradle file. Idea does the rest.

## Building and running
- run "gradlew.bat test"

### Dependency can't be resolved
- In case your development machine is behind a Proxy , you have to specify HTTPS proxy in gradle properties. Create gradle.properties file in de root of project and
put following properties into it.
systemProp.https.proxyHost=your.proxyhost
systemProp.https.proxyPort=8080
systemProp.https.nonProxyHosts=localhost

