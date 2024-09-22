# All Together Information System (ATIS)

## Overview

All Together Information System is a Java-based application designed to facilitate community-driven collaboration.
The platform enables members of a community to request assistance with tasks, and allows other members to volunteer to help with them.
Each community is overseen by a single community manager who has access to detailed information on completed tasks, including histograms and reports.

The application was implemented using OCSF, JavaFX, mySQL and more, and was mainly developed using InteliJ.

## How to run

The project uses Maven to run, and utilizes MySQL to manage the server connections and the databases.
As such, if you have not already, install [Java](https://www.oracle.com/il-en/java/technologies/downloads/) (The newer versions should suffice on your machine) and [MySQL workbench](https://dev.mysql.com/downloads/workbench/).

The following instructions will be for using InteliJ. The next few steps do not require InteliJ, however it is recommended or a similar IDE. 
You should be able to run these steps easily using your command prompt with Java.


1. Compiling - 
You should use the following configuration:
![image](https://github.com/user-attachments/assets/23a173b2-442e-4ff0-8c72-990f7f42670c)

The next steps would be to run both the server and the client. Enable simultaneous if you have not already.

2. Server - 
The configuration is as follows:
![image](https://github.com/user-attachments/assets/eb4dcc97-a1da-442b-b0e0-18cd65bb2697)

3. Client -
Same as before,
![image](https://github.com/user-attachments/assets/0d67c4bb-2b53-426d-a746-395a64f4451b)

## Using The Application

If you wish to use the application, feel free to do so. If you do use it, please fork the repository and star if it helped you!

Note that there are two .csv files in the repository, namely tasks and users. 
These are the example databases for the tasks and users which we used to simulate its use, meaning if you wish to test the application, in order to log in you could/should use the log in information in these files.

If you are planning to use an actual database or change its initialization, refer to the createDatabase() method in the SimpleServer.java file at server/main/.. folders. 

## Structure

The server side is mainly implemented at the ```bash server/src/main/java/il/cshaifasweng/OCSFMediatorExample/server/``` directory.
The client side is mainly implemented at the ```bash client/src/main/java/il/cshaifasweng/OCSFMediatorExample/client/``` directory, mainly on the controller files. 

