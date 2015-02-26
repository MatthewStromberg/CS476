--Matthew Stromberg (Mstromb1@binghamton.edu)
--Austin Thompson (Athomp12@binghamton.edu)

--The device we used to test the app has Android Version 4.4.4, and Kernel Version 3.4.0
--The device (for both Matt and Austin) were Samsung Galaxy S5
--

--The program is able to compile and execute without warnings or errors

--We used some sample code from resources online, these resources are cited below:
*******
To better understand how to use the device's motion sensors (Accelerometer and gravity sensor),
we researched online to learn more. Descriptions and sample code were utilized from these websites, to provide us with a better understanding of Android's motion sensor utilities.
http://developer.android.com/guide/topics/sensors/sensors_motion.html
http://stackoverflow.com/questions/2317428/android-i-want-to-shake-it

Because our wake lock was unable to function properly, we went with a different method
which involved using a WindowManager instead of a PowerManager. The WindowManager sample
came from a stack overflow sample, and knowledge of how "BRIGHTNESS_OVERRIDE_OFF" came from the android development website.
http://stackoverflow.com/questions/7385583/get-min-display-brightness
http://developer.android.com/reference/android/view/WindowManager.LayoutParams.html
