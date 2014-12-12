import bluetooth
import sys
import random
import time

def randomValueForRaspberries():
	listMsg = []
	for i in range(1,33):
		msgStr = "{ id: " + str(i) + ", temperature: " + str(random.randint(1, 100)) + \
		", clock:" + str(random.randint(1, 100)) + \
		", network: { up: " + str(random.randint(1,100)) + ", down: " + str(random.randint(1, 100)) + " }, " + \
		"ram: { tot: " + str(random.randint(1,100)) + ", used: " + str(random.randint(1,100)) + " }, " + \
		"procload: { cpu1: " + str(random.randint(1,100)) + " }, " + \
		"sd: { tot: " + str(random.randint(1,100)) + ", used: " + str(random.randint(1,100)) + " } }"
		listMsg.append(msgStr)
	return listMsg

first_match = None
#app uuid used in bluetooth communication to identify the software service
uuid_addr = "6209b6a0-e676-11e3-ac10-0800200c9a66"
# try to match the service uuid with the available broadcasted near me
# if it doesn't find any service available nor the bluetooth device it exits
try:
	services_matches = bluetooth.find_service(uuid = uuid_addr)
except:
	print "The bluetooth device isn't available or the service under lookup is not in near field.\nCheck your adapter correctly connected and recognized by the system or the server side is fully activated and work correctly."
	print "### exit1 ###"
	sys.exit(0)
# caught the service, get port/name/host params
if len(services_matches) == 0:
	print "### exit2 ###"
	sys.exit(0)
first_match = services_matches[0]
port = first_match["port"]
name = first_match["name"]
host = first_match["host"]

# and connect 
print "Connecting to \"%s\" on %s" % (name, host)
sock = bluetooth.BluetoothSocket( bluetooth.RFCOMM )
sock.connect((host,port))
# send a string in the stream
strMsg = "prova"
while strMsg != "001": 
	#TODO
	#change the randomValueForRaspberries() function with real value in json
	listMsg = randomValueForRaspberries()
	for msg in listMsg:
		time.sleep(0.5)
		sock.send(msg)
# and close
sock.close()
