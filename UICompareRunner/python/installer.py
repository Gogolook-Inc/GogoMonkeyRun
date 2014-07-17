import sys,traceback
# Imports the monkeyrunner modules used by this program
from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice

# get device name
def getDevice():
    return sys.argv[1]
    
# get apk name
def getApk():
    return sys.argv[2]
    
def getCurrentModel():
	rel_path = "current_devices.txt"
	abs_file_path = os.path.join(script_dir, rel_path)
	file = open(abs_file_path, "r")
	deviceModel = file.readline()
	file.close() # you can omit in most cases as the destructor will call if
	return deviceModel

device_name = getDevice()
apk_name = getApk();
script_dir = os.path.dirname(__file__) #<-- absolute dir the script is in

# Connects to the current device, returning a MonkeyDevice object
print ("waitForConnection...")
device = MonkeyRunner.waitForConnection(5,device_name)

# get device model
deviceModel = device.getProperty('build.model')
print(deviceModel)

# Installs the Android package. Notice that this method returns a boolean, so you can test
# to see if the installation worked.
print ("installPackage...")
device.installPackage(apk_name)

print ("installPackage...Complete")

list = getEceptionModels()
if not currentModel in list:
	print("not contain "+currentModel)
	device.shell("stop")
else:
	print("contain "+currentModel)

