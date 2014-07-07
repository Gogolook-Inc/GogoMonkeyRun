GogoMonkeyRun
=============
GogoMonkeyRun is an auto-testing tool developed by Po-chi Huang in Gogolook Inc.

As most people know that there are several auto-testing tools for Android.

Namely JUnit, InstrumentationTestCase, uiautomator, Monkey, and MonkeyRunner

JUnit is well-known for testing units, like function or class, but is not suitable for test a complete flow for an app.

InstrumentationTestCase is some kind like JUnit, and more straightly to test UIs. However, it is not easy to test functions over an activity and another activity, and also maintaining an InstrumentationTestCase is such costly.

uiautomator which was revealed in Google I/O at 2013, is a powerful tool to test UIs and functions. It perfectly solves the weakness mentioned above and keeps the advantages. Sad enough, it doesn't support Android SDK Platform, under API 16.

For Monkey and MonkeyRunner, we may simulate user actions and take screenshot with MonkeyRunner in python code. Android even provides a recorder tool called MonkeyRecorder to record actions. Through the recorded actions, we may generate our python script for MonkeyRunner. Does it seem so well? Unfortunately, everything we talked about here was so un-straight...such a mess...

GogoMonkeyRun, the role of this article, is developed based on Monkey and MonkeyRunner. GogoMonkeyRun does not only make everything visualization, but also solves the weakness of them. If auto-testing includes screenshot, it would compare the result each time running the script. It even can report crash!

See more information, please visit wiki https://github.com/Gogolook-Inc/GogoMonkeyRun/wiki