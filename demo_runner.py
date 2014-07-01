import sys,traceback
from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice, MonkeyImage

def startSteps():
	print ("start monkey runner.")

	# ui_debug.mr

	# add START_FROM_DESKTOP at the 'first' line of code

	# to start app from desktop.

	# add END_BACK_TO_DESKTOP at the 'last' line of code.

	# to end app back to desktop.
	print("TOUCH|{'x':87,'y':888,'type':'downAndUp',}")
	device.touch(87,888,MonkeyDevice.DOWN_AND_UP)
	MonkeyRunner.sleep(4.0)

	print("TOUCH|{'x':666,'y':88,'type':'downAndUp',}")
	device.touch(666,88,MonkeyDevice.DOWN_AND_UP)
	MonkeyRunner.sleep(4.0)

	print("TOUCH|{'x':445,'y':672,'type':'downAndUp',}")
	device.touch(445,672,MonkeyDevice.DOWN_AND_UP)
	MonkeyRunner.sleep(4.0)

	print("TOUCH|{'x':650,'y':181,'type':'downAndUp',}")
	device.touch(650,181,MonkeyDevice.DOWN_AND_UP)
	MonkeyRunner.sleep(4.0)

	print("TOUCH|{'x':175,'y':874,'type':'downAndUp',}")
	device.touch(175,874,MonkeyDevice.DOWN_AND_UP)
	MonkeyRunner.sleep(4.0)

	print("TOUCH|{'x':63,'y':1146,'type':'downAndUp',}")
	device.touch(63,1146,MonkeyDevice.DOWN_AND_UP)
	MonkeyRunner.sleep(4.0)

	print("TAKE SNAPSHOT")
	result = device.takeSnapshot()
	print("Writes the screenshot to a file")
	result.writeToFile('/Users/spring60569/Documents/screenshot_0.png','png')

	print("PRESS|{'name':'BACK','type':'downAndUp',}")
	device.press('KEYCODE_BACK',MonkeyDevice.DOWN_AND_UP)
	MonkeyRunner.sleep(4.0)

	print("PRESS|{'name':'BACK','type':'downAndUp',}")
	device.press('KEYCODE_BACK',MonkeyDevice.DOWN_AND_UP)
	MonkeyRunner.sleep(4.0)

	print("PRESS|{'name':'BACK','type':'downAndUp',}")
	device.press('KEYCODE_BACK',MonkeyDevice.DOWN_AND_UP)
	MonkeyRunner.sleep(4.0)

	print("end monkey runner.")

print("Connects to the current device, returning a MonkeyDevice object")
device = MonkeyRunner.waitForConnection(5,"CB5A1LY5YK")

if not device:
	print("device connect...fail")
	sys.exit(1)
else:
	print("device connect...success")
	startSteps()
	device.shell("stop")
	sys.exit(0)

