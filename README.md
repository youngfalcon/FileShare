# FileShare
A hackaton project aimed to enable users share files with a server and in result, with each other.

It should be noted that as a result of the structure of the database used, there must exist a folder named Files in the same folder where the server.jar(if it is to be run as a runnable JAR). This file will act as the destination all the required files will be searched in and uploaded to.

The ClientHandler class defines the threads which will be run by the Server class.

The Client class(which can also be found under the server folder) is to be used as a mean to access the server and act as the client-side. It should be noted that it is merely a mean to access the server and therefore is not built to be compatible with operating systems other than Windows. The data system which the client will run in must contain a folder named "Send" and a folder named "Recieve" (the names should be exactly the same). The client will look search for the files to send ind the "Send" folder and will attemp to save received files to the "Recieve" folder.
