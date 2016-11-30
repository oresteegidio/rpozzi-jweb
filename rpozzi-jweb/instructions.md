Get started with ${app}
-----------------------------------
This is a template for Bluemix Java Web database application development.

The sample is a simple todo list where users can add, modify, and delete individual todo items, while those items persist to the backend database. In the sample is a clear demonstration of how to use JPA or JDBC to access the database service that binds to the application.

1. [Install the cf command-line tool](${doc-url}/#starters/BuildingWeb.html#install_cf).
2. [Download the starter package](${ace-url}/rest/apps/${app-guid}/starter-download).
3. Extract the package and `cd` to it.
4. Connect to Bluemix:

		cf api ${api-url}

5. Log into Bluemix:

		cf login -u ${username}
		cf target -o ${org} -s ${space}
		
6. Compile the Java code and generate the war package using ant.
7. Deploy your app:

		cf push ${app} -p libertyDBApp.war

8. Access your app: [http://${route}](http://${route})
