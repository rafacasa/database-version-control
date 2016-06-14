# database-version-control
DVS is a java library to manage the version of the database's schema of your software.  
All you need to do is have a folder to put the files. In this folder put a file named database.info with a single integer number which means the lastest database schema version. Then, in the same folder, put the sql files from each schema's versions(1.sql, 2.sql, ...). In your code you have to make a implementation of DAO to the SGDB you are using(MySql included in the library). Then you need to have a instance of your DAO and create a instance of Dvs with the Path to the folder you are using and the instance of your DAO. Then you just havo to execute the only public method of Dvs: verifyVersion()

