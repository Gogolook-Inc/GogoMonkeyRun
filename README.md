GogoMonkeyRun
=============
<br>Welcome to the GogoMonkeyRun!
<br>
<br>GogoMonkeyRun is an auto-testing tool developed by Po-chi Huang in Gogolook Inc.
<br>
<br>
<br>As most people know that there are several auto-testing tools for Android.
<br>
<br>Namely JUnit, [InstrumentationTestCase](http://developer.android.com/tools/testing/activity_testing.html), [uiautomator](http://developer.android.com/tools/testing/testing_ui.html), [Monkey](http://developer.android.com/tools/help/monkey.html), and [MonkeyRunner](http://developer.android.com/tools/help/monkeyrunner_concepts.html)
<br>
<br>JUnit is well-known for testing units, like function or class, but is not suitable for test a complete flow for <br>an app.
<br>
<br>InstrumentationTestCase is some kind like JUnit, and more straightly to test UIs.
<br>However, it is not easy to test functions over an activity and another activity, and also maintaining an <br>InstrumentationTestCase is such costly.
<br>
<br>uiautomator which was revealed in Google I/O at 2013, is a powerful tool to test UIs and functions.
<br>It perfectly solves the weakness mentioned above and keeps the advantages.
<br>Sad enough, it doesn't support Android SDK Platform, under API 16.
<br>
<br>For Monkey and MonkeyRunner, we may simulate user actions and take screenshot with MonkeyRunner in python code.
<br>Android even provides a recorder tool called MonkeyRecorder to record actions.
<br>Through the recorded actions, we may generate our python script for MonkeyRunner.
<br>Does it seem so well?
<br>Unfortunately, everything we talked about here was so un-straight...such a mess...
<br>
<br>GogoMonkeyRun, the role of this article, is developed based on Monkey and MonkeyRunner.
<br>GogoMonkeyRun does not only make everything visualization, but also solves the weakness of them.
<br>If auto-testing includes screenshot, it would compare the result each time running the script.
<br>It even can report crash!
<br>
## Installation
<br>Pull project from git repository: [https://github.com/Gogolook-Inc/GogoMonkeyRun.git](https://github.com/Gogolook-Inc/GogoMonkeyRun.git)
<br>
<br>Open or unzip GogoMonkeyRun1_0_2.zip, and we'll see the following picture.
<br>
![Files needed](https://dl.dropboxusercontent.com/u/43934047/GogoMonkeyRun/github_image/file_needed.png)
<br>These files are needed in the same directory.
<br>Double-click UICompare.jar to open application.
<br>
## First Time to Open Application
![Set SDK path](https://dl.dropboxusercontent.com/u/43934047/GogoMonkeyRun/github_image/set_sdk_path.png)
<br>Don't worry if see this alert.
<br>SDK-path must be set at the first time.

![select sdk directory](https://dl.dropboxusercontent.com/u/43934047/GogoMonkeyRun/github_image/select_sdk_path.png)
<br>Select the right sdk path.

![Select a device](https://dl.dropboxusercontent.com/u/43934047/GogoMonkeyRun/github_image/select_a_device.png)
<br>Select a device to perform auto-testing.

![Set crash repot](https://dl.dropboxusercontent.com/u/43934047/GogoMonkeyRun/github_image/set_crash_report.png)
![Set crash report2](https://dl.dropboxusercontent.com/u/43934047/GogoMonkeyRun/github_image/set_crash_report2.png)
<br>Setting crash report, just input your email address and password.
<br>If crash report is not set, we still can find the error report in the directory of app.

## Start Record a Script
![Make a record](https://dl.dropboxusercontent.com/u/43934047/GogoMonkeyRun/github_image/make_a_record.png)
<br>If there are no action recorded before, please make a new record here.
<br>After press "yes", we may see a MonkeyRecorder shown.
<br>
<br>Notice that this recorder is not like the original one.
<br>I make a new plug-in to enhance flexibility of original one.
<br>The following shows the comparison.
<br>
![Compare of new MonkeyRecorder and Old one](https://dl.dropboxusercontent.com/u/43934047/GogoMonkeyRun/github_image/recorder_comparison.jpg)
<br>The right one is the new MonkeyRecorder, and the left one is the original.
<br>Noticed that, compare to the original one, button of fling is removed.
<br>To perform fling(or drag), just use mouse to perform drag action.
<br>
<br>After exporting actions and exiting MonkeyRecorder, GogoMonkeyRun would generate a python script.
<br>In fact, we may directly run this python script to perform auto-testing.
![Edit Python script](https://dl.dropboxusercontent.com/u/43934047/GogoMonkeyRun/github_image/edit_python_script.png)
<br>In this dialog, we may add some actions to make script more flexible.
<br><br>**Be careful to use "Clear Data", it will clear all data depends on your packagename.**

## Run Scripts
![Add scripts](https://dl.dropboxusercontent.com/u/43934047/GogoMonkeyRun/github_image/add_scripts.png)
<br>In this dialog, we may assign multi-scripts to run in the same cycle for auto-testing.
<br>Press "Run Script" to perform auto-testing!
<br>
<br>At the end, GogoMonkeyRun would compare the result for the latest run
![Result](https://dl.dropboxusercontent.com/u/43934047/GogoMonkeyRun/github_image/result.png)
<br>This result would show comparison if developers are not aware to make a logical mistake while debuging code.
<br>
<br>
<br>This Project is Completely Open Source.
<br>If you have any idea, modifying the project and making pull request is welcome.
<br>
<br>Contact me with my email:
<br>pochihuang@gogolook.com or spring60569@gmail.com
<br>
<br>
<br>See more information, please visit wiki https://github.com/Gogolook-Inc/GogoMonkeyRun/wiki
