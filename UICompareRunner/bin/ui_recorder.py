import sys,traceback
from com.android.monkeyrunner import MonkeyRunner
from com.android.monkeyrunner.recorder import MonkeyRecorderExt
from com.android.monkeyrunner.utils import SystemUtils

# get device name
def getDevice():
    return sys.argv[1]
    
# get action
def getAction():
    return sys.argv[2]
    
# get default export dir
def getDefaultExportDir():
    return sys.argv[3]

SystemUtils.init()
device_name = getDevice()
action = getAction()
defaultExportDir = getDefaultExportDir()

# start device and recorder
print ("waitForConnection...")
device = MonkeyRunner.waitForConnection(5,device_name)

if not device:
	print("device connect...fail")
	sys.exit(1)
elif action == 'record':
	print("start monkey recorder")
	MonkeyRecorderExt.start(device, defaultExportDir)
else:
	print("close monkey recorder")
	device.shell("stop")

sys.exit(0)