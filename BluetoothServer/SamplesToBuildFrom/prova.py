import sys
import random

listMsg = []
for i in range(1,33):
	msgStr = "{ id: " + str(i) + ", temperature: " + str(random.randint(1, 100)) + \
		", clock:" + str(random.randint(1, 100)) + \
		", network: { up: " + str(random.randint(1,100)) + ", down: " + str(random.randint(1, 100)) + " }, " + \
		"ram: { tot: " + str(random.randint(1,100)) + ", used: " + str(random.randint(1,100)) + " }, " + \
		"procload: { cpu1: " + str(random.randint(1,100)) + " }, " + \
		"sd: { tot: " + str(random.randint(1,100)) + ", used: " + str(random.randint(1,100)) + " } }"
	print msgStr
	print ""
