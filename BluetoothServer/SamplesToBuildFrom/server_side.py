import bluetooth
import sys

# create the socket
server_socket = bluetooth.BluetoothSocket( bluetooth.RFCOMM )
#try:
	# check for available ports. In a try/except cause it only checks for open ports
	# but does no system resources reservation so it could become in use during the 
	# transition time from the check to the effective use.
port = 0#bluetooth.get_available_port( bluetooth.RFCOMM )
#except:
#	print "There are no suitable port free, try to re-run the server application or disable some other bluetooth services"
#	print "### exit ###"
#	sys.exit(0)
# start listen on the free port caught using any bt-adapter available
server_socket.bind(("",port))
server_socket.listen(1)
print "Server listening on port %d" % port

uuid = "6209b6a0-e676-11e3-ac10-0800200c9a66" # will contain the uuid for the server part of the application
# the server broadcast the uuid and name of service  with relatively linked port
# to nearby devices, the client find out who's near him and match the uuid to 
# know what software service is goig to use to catch the right one.
bluetooth.advertise_service(server_socket, "Example service", uuid)
client_sock, address = server_socket.accept()
print "Accepted connection from ", address
# receive data from the stream 
data = client_sock.recv(1024)
print "Received [%s]" % data
# close the streams 
client_sock.close()
server_socket.close()
