# Simple-Java-UDP-Example
Introduction:
	This set of classes is designed to show the reliability of UDP in Java.
	The way it does this is by trying to send a specified number of messages from the client
	to the server.  Each message contains the message sequence number and the total number of
	messages to be sent.  The server keeps track of the messages received and when there are 
	no more messages, outputs a summary of the number of messages received and also which
	ones were lost.
Use - General:
	To use the system you must first start the server and then start the client.
	Faliure to do so will mean the client will not be able to find to the server
	and thus throw an error saying that it could not connect to the server.
Disclaimer
	USE THIS SOFTWARE AT YOUR OWN RISK. SOUCE FILES ARE INCLUDED FOR INSPECTION.
