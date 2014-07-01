import sys,traceback
from com.android.monkeyrunner import MonkeyRunner
from com.android.monkeyrunner.recorder import MonkeyRecorder

# start device and recorder
print ("waitForConnection...")
device = MonkeyRunner.waitForConnection(5,"CB5A1LY5YK")

if not device:
	print("device connect...fail")
	sys.exit(1)
else:
	print("start monkey recorder")
	MonkeyRecorder.start(device)

sys.exit(0)