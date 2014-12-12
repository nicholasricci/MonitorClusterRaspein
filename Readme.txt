Project Raspbein.

Monitor HPC Application for Android.

This application allows to set informations for 32 raspberries.
The information must be passed through bluetooth or via web server.
Anyway you must set the information of each raspberry like this.

e.g. json:

{
	id: I,
	temperatura:X,
	clock:Y,
	network:{ up:S, down:G },
	ram: { tot:T, used:U },
	procload:{ 1: P1, 2: P2, ..., n: PN },
	sd:{ tot:T, used:U }
}

Bluetooth Mode:

For Bluetooth you must set a bluetooth client in the server, there is an 
example in BluetoothServer "client_side.py" and a server app MonitorHPC in 
Android device. 
The bluetooth connection is insecure RFCOMM with uuid and doesn't require 
paired at the user but the server side and the client side have the same uuid 
and they can connect.
Keep in mind that you want to use this Mode you must edit the client_side.py
with real value in json to send to Android app.

Web Server Mode:
With web server mode there is needed one page in .php or other server-side 
language that allow you to retrieve the info of each raspberry, 
in that case you must configure in java source code of android application 
the url of your web page.
Keep in mind that the parameter of your page is in range [1..32], 
the same number that is the raspberries. The HTTP method request is GET. 
E.g. 
	web_server/raspberry_info.php?id=1 OR
	web_server/raspberry_info.php?id=2 OR
	web_server/raspberry_info.php?id=32

Application created by Nicholas Ricci and Luca Melandri
