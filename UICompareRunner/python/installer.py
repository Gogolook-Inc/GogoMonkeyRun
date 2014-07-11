import sys,traceback
# Imports the monkeyrunner modules used by this program
from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice

# get device name
def getDevice():
    return sys.argv[1]
    
# get apk name
def getApk():
    return sys.argv[2]

device_name = getDevice()
apk_name = getApk();

# Connects to the current device, returning a MonkeyDevice object
print ("waitForConnection...")
device = MonkeyRunner.waitForConnection(5,device_name)

# Installs the Android package. Notice that this method returns a boolean, so you can test
# to see if the installation worked.
print ("installPackage...")
device.installPackage(apk_name)

print ("installPackage...Complete")

if not device_name == 'HT43VWM04049':
		device.shell("stop")

