--------------------------------------------------------------
Example Application (Android 6.0+)
--------------------------------------------------------------
IMPORTANT NOTES:
	
	- Testing should be done on an actual Android device and
	not in the Android emulator, as the emulator has been known
	to cause issues with receiving UDP packets.
	
	- This database is just an example and is not required
	to be done with SQLite or contain the fields as done in
	this database.
	
--------------------------------------------------------------

The executable emulates the look and feel of the Example Application shown on the website. The code contains a UDP receiver class for capturing Caller ID packets from the LAN. It includes methods for parsing data from call records, displaying Caller ID on multiple lines in a Window, and storing call data. Two small SQLite databases are created in order to show examples of database commands for customer pop-ups and call logging.