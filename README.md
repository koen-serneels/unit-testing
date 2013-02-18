System requirements
===================

Make sure that:

- You have the latest JDK7 on your system (currently 1.7.0u13)
- You are using the latest Maven version (currently 3.0.4)
- Latest tomcat (currently 7.0.35) is somewhere on your system (required for running within eclipse)
- Paths are set correctly and verify that Maven is actually using Java7 

$ mvn -version
...
Java version: 1.7.0_13, vendor: Oracle Corporation
...

- Firefox is installed as this is the default browser for the selenium tests
- "m2e - Maven integration for eclipse" plugin is installed in eclipse
- "TestNG" plugin installed in eclipse

If you are behind a proxy, make sure that eclipse and maven can access the internet.
Or, in the case of maven, can access your local nexus if your project is using this.

Running from CLI
================

* mvn clean install
** will both run the unit (resource) tests (surefire) as the selenium/frontend tests (failsafe-cargo).
* mvn clean install -DheadlessIT
** will do the same as above, but run the browser in a virtual framebuffer this allows you to run the tests without the browser popping up and interfering with your work. Also, it allows the test to run on a CI machine where there might not be a window manager present. Requires xvfb to be installed on ubuntu (sudo apt-get install xvfb)
* mvn clean install -DskipITs
** Skips the frontend tests (failsafe term = integration tests)

Importing in eclipse
====================

Use "import->existing projects->maven->existing maven projects"
Make sure to enter "tomcat" as the default profile (unfold the "advanced" section on the bottom of the dialog after click "next" on "import->existing projects->maven->existing maven projects").
If there is any error reported by the maven plugin, use clean: "right click on project -> maven -> update project..."
If there is a problem with the dependencies you might want to perform "mvn eclipse:eclipse" prior importing the project.

Deploying on tomcat
-------------------

Add tomcat7 as server and deploy the project onto it. No settings are required.
If the project does not deploy, go to the server view, right click on your tomcat server and click "Clean", then restart tomcat.
Point browser to: http://localhost:8080/testing/
username/password: koen/koen

Running frontend test from eclipse
----------------------------------

Change the profile to "tomcat-selenium", by right clicking on the project -> properties -> maven. Let maven update the project. Stop/start tomcat.
You can now run the frontend test (right click -> run as -> TestNG test).

Sometimes the maven plugin and WTP are not so nice. It could be that the profile change was not propagated to the deploy dir:
Go to the server view, right click on your tomcat server and click "Clean".
You can always verify which profiles are active in your deployment dir by opening: %workspace_location%/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/testing/WEB-INF/classes/profiles.properties
