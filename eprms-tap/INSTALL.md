# eprms-tap build from source instructions

## Testing Database and Web App and Authentication Server

For testing purposes, you can use Docker/Podman to run all the needed services in containers.

### Database

The system is developed and tested against a PostgreSQL 12 started as a Docker/Podman container, but in general it is database agnostic and uses only Hibernate APIs, so you can switch to another database.

To start a containerized PostgreSQL instance that stores all it's data in a local folder follow the instructions to create the folder and map the container to use that folder:

	$ mkdir DatabaseFolder
	$ podman run \
		--cgroup-manager=cgroupfs  \
		-d \
		-e POSTGRES_USER=postgres \
		-e POSTGRES_PASSWORD=CHANGE_THIS_PASSWORD \
		--network "host" \
		--privileged \
		--mount type=bind,source=./DatabaseFolder,target=/var/lib/postgresql/data  \
		--name postgres_for_eprms \
		postgres:12

This will create a PostgreSQL server instance container, that stores it's data in the DatabaseFolder. The data will survive container restarts or removals. By default, the DB instance listens on port 5432. The DatabaseFolder privileges will be changed so that it functions properly under the container, and will no longer be accessible to you directly.

Check that the container is running.

	podman ps

Then connect to the container console, to create the initial empty database.

	$ podman exec -i -t postgres_for_eprms bash

... type the same password that you typed in the starting script above, you will get a root user prompt, from which you can connect to the postgres instance in order to create the database.

	# psql -h localhost -U postgres -W postgres
	
Create a database user owner for the database, then the database and two needed schemas, owned by the database owner.

	postgres=# 
		create user eprms_owner encrypted password 'DB_PASSWORD_CHANGE_IT';
		create database eprms owner eprms_owner;
		create schema epm_main;
		create schema epm_util;
		alter schema epm_main owner to eprms_owner;
		alter schema epm_util owner to eprms_owner;

### Web Application and Authentication Server

For testing purposes it is recommended to use Apache Tomcat as a Java web application server, and you can also run it in a separate container.

	$ mkdir WebAppsFolder
	$ podman run \
		--name tomcat-cas  \
		--privileged   \
		--mount type=bind,source=./webapps,target=/usr/local/tomcat/webapps   \
		-p 8080:8080 \
		-d \
		tomcat:9.0

The Tomcat container should be running at this moment, check:

	podman ps

You can use the WebAppsFolder to place the packaged web application archives (WAR files). 

The application uses Apereo CAS for authentication. Copy the provided *cas.war* to the *WebAppsFolder* to initiate a demo instance of Apereo CAS, where all usernames are valid and anyone can log in provided the password that is entered is the same as the username.

Check if CAS is working:

	http://localhost:8080/cas

## Build the web application

The application will be built from source using Maven.

### Maven profile configuration

Some configuration parameters can be confidential or can differ from one envirement to another, so we are not providing them with the source. Instead a maven user profile should be configured outside the project, that will be used during the building of the project. Follow the instructions.

For Maven to build the project you need to setup a maven profile in 

	$HOME/.m2/settings.xml

You can use the following example:

	<profile>
		<id>development-eprms</id>
		<activation>
			<property>
				<name>env</name>
				<value>development-eprms</value>
			</property>
		</activation>
		<properties>
			<jdbc.driver>org.postgresql.Driver</jdbc.driver>
			<jdbc.url>jdbc:postgresql://localhost:5432/eprms</jdbc.url>
			<jdbc.test.url>jdbc:postgresql://localhost:5432/eprms</jdbc.test.url>
			<jdbc.username>eprms_owner</jdbc.username>
			<jdbc.password>DB_PASSWORD_CHANGE_IT</jdbc.password>
			<jdbc.default_schema>epm_main</jdbc.default_schema>
			<hib.hbm2ddlauto>update</hib.hbm2ddlauto>
			<hib.dialect>org.hibernate.dialect.PostgreSQL94Dialect</hib.dialect>
			<hib.showsql>false</hib.showsql>
			<cas.server>http://localhost:8080</cas.server>
			<app.server>http://localhost:8080</app.server>
			<logout.redirectToServer>http://localhost:8080</logout.redirectToServer>
			<production.mode>false</production.mode>
			<logging.path></logging.path>
			<logging.rootCategory>INFO,Console</logging.rootCategory>
			<tapestry.hmac-passphrase>CHANGE_THIS_ALSO</tapestry.hmac-passphrase>
		</properties>
	</profile>
	


### Build the source and prepare the runtime into a standard Java WAR package

Run the following commands from the project source folder

	mvn -P development-eprms clean package

You will get an eprms.war package in the *target* folder 

Copy the built *eprms.war* to the *WebAppsFolder* created in the preceeding preparatory steps.

After a short initializaion period you will be able to access the application at:

	http://localhost:8080/epm

Clicking on the login should redirect to the CAS system at:

	http://localhost:8080/cas/...

### Create Users and Roles

On the first start of the application, empty database tables will be created.

So, initially you won't have any access rights, and you need to fill-in the database with:

 * sample users in the table epm_main.person
 * roles - ADMINISTRATOR, INSTRUCTOR, STUDENT in the table epm_util.roles
 * assign user roles for the created users in the table epm_util.person_role

Finished!
