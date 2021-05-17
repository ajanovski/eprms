# eprms-net build from source instructions

## Testing Database and Authentication Server

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

### Web Authentication Server

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

The application will be built from source using dotnet. 
You will also need node.js and npm, to get the needed dependencies (Bootstrap, JQuery, ...)

### .Net profile configuration

Some configuration parameters can be confidential or can differ from one envirement to another, so we are not providing them with the source. Instead a maven user profile should be configured outside the project, that will be used during the building of the project. Follow the instructions.

Configure settings for the connection string to the created PostgreSQL database in the container. These settings are used to configure the link to the database needed in the following steps, and also needed when starting the application.

	dotnet user-secrets set ConnectionStrings:EPRMSDB "Host=127.0.0.1;Port=5432;Database=epm;Username=epm_owner;Password=lozinka"

Configure settings for the CAS base url:

	dotnet user-secrets set CAS:CasBaseUrl "http://localhost:8080/cas"

The application optionally uses OAuth2 and is prepared to use GitHub or ORCID as authorization providers. The application needs to be registered at GitHub and/or ORCID to be able to use them as OAuth2 providers and the received registration CliendID and ClientSecret should be configured here, before the build. In addition, the source can be changed to enable other providers.

	dotnet user-secrets set GitHub:ClientId "____"
	dotnet user-secrets set GitHub:ClientSecret "____"
	dotnet user-secrets set ORCID:ClientId "____"
	dotnet user-secrets set ORCID:ClientSecret "____"



### Build the source and prepare the runtime

Run the following commands from the project source folder

To check all css/js dependencies:

	npm install

To check all .Net dependencies:

	dotnet restore

To start the web application:

	dotnet watch run

	The application will be running at: http://localhost:8081

To install the dotnet-ef tool for reverse engineering the database model

	dotnet tool install --global dotnet-ef

The following commands reverse engineer the Database into a dbcontext class and the mapping Model classes

	dotnet-ef dbcontext scaffold Name=ConnectionStrings:EPRMSDB Npgsql.EntityFrameworkCore.PostgreSQL -f -v -c EPRMSNetDbContext --context-dir Data/ -o Models/

In case you need to install some additional libraried, run this:

	dotnet add package library.name

Optionally, you can install the ASP.NET code generator in case you need some ready made source examples

	dotnet tool install --global dotnet-aspnet-codegenerator

Optionally, create EF ready razor pages via the code generator

	dotnet aspnet-codegenerator razorpage -m Repository -dc EPRMSNetDbContext -udl -outDir Pages/RepositoryPage --referenceScriptLibraries

Finished!
