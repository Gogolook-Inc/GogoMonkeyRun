import sys,traceback
from com.android.monkeyrunner import MonkeyRunner
from com.android.monkeyrunner.recorder import MonkeyRecorder

# get device name
def getDevice():
    return sys.argv[1]
    
# get action
def getAction():
    return sys.argv[2]

device_name = getDevice()
action = getAction();

# start device and recorder
print ("waitForConnection...")
device = MonkeyRunner.waitForConnection(5,device_name)

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