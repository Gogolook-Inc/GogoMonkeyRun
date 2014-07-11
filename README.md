GogoMonkeyRun
=============
Welcome to the GogoMonkeyRun wiki!

GogoMonkeyRun is an auto-testing tool developed by Po-chi Huang in Gogolook Inc.


As most people know that there are several auto-testing tools for Android.

Namely JUnit, [InstrumentationTestCase](http://developer.android.com/tools/testing/activity_testing.html), [uiautomator](http://developer.android.com/tools/testing/testing_ui.html), [Monkey](http://developer.android.com/tools/help/monkey.html), and [MonkeyRunner](http://developer.android.com/tools/help/monkeyrunner_concepts.html)

JUnit is well-known for testing units, like function or class, but is not suitable for test a complete flow for an app.

InstrumentationTestCase is some kind like JUnit, and more straightly to test UIs.
However, it is not easy to test functions over an activity and another activity, and also maintaining an InstrumentationTestCase is such costly.

uiautomator which was revealed in Google I/O at 2013, is a powerful tool to test UIs and functions.
It perfectly solves the weakness mentioned above and keeps the advantages.
Sad enough, it doesn't support Android SDK Platform, under API 16.

For Monkey and MonkeyRunner, we may simulate user actions and take screenshot with MonkeyRunner in python code.
Android even provides a recorder tool called MonkeyRecorder to record actions.
Through the recorded actions, we may generate our python script for MonkeyRunner.
Does it seem so well?
Unfortunately, everything we talked about here was so un-straight...such a mess...

GogoMonkeyRun, the role of this article, is developed based on Monkey and MonkeyRunner.
GogoMonkeyRun does not only make everything visualization, but also solves the weakness of them.
If auto-testing includes screenshot, it would compare the result each time running the script.
It even can report crash!


The pictures below are introductions for GogoMonkeyRun.
![Files needed](https://dl.dropboxusercontent.com/u/43934047/GogoMonkeyRun/%E8%9E%A2%E5%B9%95%E5%BF%AB%E7%85%A7%202014-07-07%20%E4%B8%8B%E5%8D%885.27.09.png)
These files are needed in the same directory.

![Set SDK path](https://dl.dropboxusercontent.com/u/43934047/GogoMonkeyRun/%E8%9E%A2%E5%B9%95%E5%BF%AB%E7%85%A7%202014-07-07%205.29.48%20PM.png)
Don't worry if see this alert.
SDK-path must be set at the first time.

![select sdk directory](https://dl.dropboxusercontent.com/u/43934047/GogoMonkeyRun/Screenshot%202014-07-07%2017.40.40.png)
Select the right sdk path.

![Select a device](https://dl.dropboxusercontent.com/u/43934047/GogoMonkeyRun/Screenshot%202014-07-07%2017.png)
Select a device to perform auto-testing.

![Set crash repot](https://dl.dropboxusercontent.com/u/43934047/GogoMonkeyRun/%E8%9E%A2%E5%B9%95%E5%BF%AB%E7%85%A7%202014-07-07%205.53.00%20PM.png)
Setting crash report, just input your email address and password.
If crash report is not set, we still can find the error report in the directory of app.

![Make a record](https://dl.dropboxusercontent.com/u/43934047/GogoMonkeyRun/%E8%9E%A2%E5%B9%95%E5%BF%AB%E7%85%A7%202014-07-07%205.57.53%20PM.png)
If there are no action recorded before, please make a new record here.
After press "yes", we may see a MonkeyRecorder shown.

Notice that this recorder is not like the original one.
I make a new plug-in to enhance flexibility of original one.
The following shows the difference.

![Compare of new MonkeyRecorder and Old one](https://dl.dropboxusercontent.com/u/43934047/GogoMonkeyRun/compare.jpg)
The left one is the new MonkeyRecorder, and the right one is the original.

After exporting actions and exiting MonkeyRecorder, GogoMonkeyRun would generate a python script.
In fact, we may directly run this python script to perform auto-testing.
![Edit Python script](https://dl.dropboxusercontent.com/u/43934047/GogoMonkeyRun/%E8%9E%A2%E5%B9%95%E5%BF%AB%E7%85%A7%202014-07-07%206.38.33%20PM.png)
In this dialog, we may add some action to make script more flexible.

![Add scripts](https://dl.dropboxusercontent.com/u/43934047/GogoMonkeyRun/%E8%9E%A2%E5%B9%95%E5%BF%AB%E7%85%A7%202014-07-07%206.41.04%20PM.png)
In this dialog, we may assign multi-scripts to run in the same cycle for auto-testing.
Press "Run Script" to perform auto-testing!

At the end, GogoMonkeyRun would compare the result for the latest run
![Result](https://dl.dropboxusercontent.com/u/43934047/GogoMonkeyRun/%E8%9E%A2%E5%B9%95%E5%BF%AB%E7%85%A7%202014-07-07%206.44.37%20PM.png)
This result would show comparison if developers are not aware to make a logical mistake while debuging code.


This Project is Completely Open Source.
If you have any idea, modifying the project and making pull request is welcome.

Contact me with my email:
pochihuang@gogolook.com
or
spring60569@gmail.com












See more information, please visit wiki https://github.com/Gogolook-Inc/GogoMonkeyRun/wiki
