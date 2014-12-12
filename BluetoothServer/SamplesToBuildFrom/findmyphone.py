import bluetooth

target_name = "Nexus S"
target_address = None

print "###########################"
print "#   Find nearby devices   #"
print "###########################"
print "Target to find: ",target_name

nearby_devices = []

try:
	# The routine discover_devices() scans for approximately 10 
	# seconds and returns a list of addresses of detected devices
	nearby_devices = bluetooth.discover_devices() 
except:
	print "Failed to access the Bluetooth device, make sure it's correctly connected and well recognized"
	exit()

for baddr in nearby_devices:
	#  the routine lookup_name() to connect to each detected device
	#  requests its user-friendly name, and compares the result to the target name
	if target_name == bluetooth.lookup_name( baddr ):
		target_address = baddr
		break
if target_address is not None:
	print "Found target bluetooth device with address ", target_address
else:
	print "Could not find target bluetooth device nearby"
