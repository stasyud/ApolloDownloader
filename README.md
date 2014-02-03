ApolloDownloader
================

Java based application for downloading photographs of Apollo moon expeditions from the site of Lunar and Planetary Institute.

http://www.lpi.usra.edu/resources/apollo/catalog/70mm/

Please be aware: size of downloaded photographs are more than 23GB.


======

## Configuring the application


You have to configure application before first launch. There is configuration file [config.properties](/src/main/resources/config.properties) that contains all application related parameters.

Parameter **output.folder** is responsible for directory where downloaded files will be stored, it should be defined before the first launch.


## Creating executable jar file

1.	Make sure you have Maven 3  installed.
	You can check that with the following command:

		> mvn --version

2.	Then you have to run command line console in your project directory and run following command, it will compile and package application classes and corresponding dependencies into jar files:

		> mvn package

	This may take quite some time, if you are running Maven for the first time,
	as it has to download all the dependencies for the different build steps,
	plus our own dependencies.

If everything went smoothly you will see `[INFO] BUILD SUCCESS` in your command line console.

All the output of the build process is under the `target` sub-dir.
This is also where you find the final jar files:
`target/ApolloDownloader*.jar`

##Running the application

Open directory `${project}/target` and run following command:

		>java -jar ApolloDownloader-jar-with-dependencies.jar


## Author

* Author: Stanislav Yudenkov (<stasyud@gmail.com>)
