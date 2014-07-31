# this is a test python for old version monkeyrunner
# no need to use this file

import sys,traceback
from com.android.monkeyrunner import MonkeyRunner
from com.android.monkeyrunner.recorder import MonkeyRecorder

# get action
def getAction():
	a='record'
	if len(sys.argv) > 1:
		a=sys.argv[1]
	return a
    
action = getAction()
# start device and recorder
print ("waitForConnection...")
device = MonkeyRunner.waitForConnection(5,"CB5A1N21LE")

if not device:
	print("device connect...fail")
	sys.exit(1)
elif action == 'record':
	print("start monkey recorder")
	MonkeyRecorder.start(device)
else:
	print("close monkey recorder")
	device.shell("stop")

sys.exit(0)