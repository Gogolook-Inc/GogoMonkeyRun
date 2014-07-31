import os,sys,traceback
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
    
def setCurrentModel(deviceModel):
	if deviceModel:
		rel_path = "current_devices.txt"
		abs_file_path = os.path.join(script_dir, rel_path)
		file = open(abs_file_path, "w")
		file.write(deviceModel) # python will convert \n to os.linesep
		file.close() # you can omit in most cases as the destructor will call if
		
def getCurrentModel():
	rel_path = "current_devices.txt"
	abs_file_path = os.path.join(script_dir, rel_path)
	file = open(abs_file_path, "r")
	deviceModel = file.readline()
	file.close() # you can omit in most cases as the destructor will call if
	return deviceModel
	
def getExceptionModels():
	rel_path = "exception_devices.txt"
	abs_file_path = os.path.join(script_dir, rel_path)
	file = open(abs_file_path, "r")
	list = [line.strip() for line in file]
	file.close()
	return list

SystemUtils.init()
device_name = getDevice()
action = getAction()
defaultExportDir = getDefaultExportDir()
script_dir = os.path.dirname(__file__) #<-- absolute dir the script is in

# start device and recorder
print ("waitForConnection...")
device = MonkeyRunner.waitForConnection(5,device_name)

if not device:
	print("device connect...fail")
	sys.exit(1)

if action == 'record':
	print("start monkey recorder")
	deviceModel = device.getProperty('build.model')
	print(deviceModel)
	setCurrentModel(deviceModel)
	MonkeyRecorderExt.start(device, defaultExportDir)
else:
	print("close monkey recorder: ")
	currentModel = getCurrentModel()
	list = getExceptionModels()
	if not currentModel in list:
		print("not contain "+currentModel)
		device.shell("stop")
	else:
		print("contain "+currentModel)
	

sys.exit(0)