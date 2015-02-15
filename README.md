EyeDroid
========

<img src="Images/EyeDroid_logo.png?raw=true" height="150" width="150"/>

WHAT IS EYEDROID?
--------------

EyeDroid is a mobile Android platform eye tracking system designed to be used with a USB connected head mounted camera. EyeDroid receives video streaming from the user’s eye as input, process it and sends the resulting 2-axis coordinates to networked clients. Unlike other eye tracking systems which use a stationary processing server, EyeDroid performs all its processing workload in a mobile device and sends the resulting coordinates to a network client. For this reason, EyeDroid supports user's mobility when used along wearable and mobile devices.

EyeDroid also provides alternative video streaming inputs, such as built-int smartphone front/back cameras. Because the Android platform does not provide support to connect an external USB camera, the OS needs to own root access to the phone and use customized camera video drivers. On EyeDroid, [open source third party drivers were used.] (http://brain.cc.kogakuin.ac.jp/research/usb-e.html)

SOFTWARE ARCHITECTURE
--------------

The software architecture of the EyeDroid application is designed based on pipes and filters design pattern and implemented on top of the [Java Lightweight Processing Framework] (https://github.com/centosGit/JLPF). The main eye tracking algorithm is decomposed into steps (filters), grouped into three compistes and executed in parallel (a thread per composite).

<img src="Images/EyeDroid_SoftwareArchitecture.PNG?raw=true" height="300"/>

SOFTWARE DEPENDENCIES
---------

Java 1.7 <br/>
Android SDK API 20 (recommended) <br/>
Android NDK r10b <br/>
Android Support Library v4 <br/>
OpenCV Library 2.4.9 <br/>
[JLPF 1.0](https://github.com/centosGit/JLPF)<br/>

HARDWARE
--------------

The hardware requirements in the current implementation of the EyeDroid eye tracker are an Android mobile device (minimum API level 15)and a head mounted USB 2.0 infrared camera connected directly to the phone. The recommended camera resolution.
is 640 × 480 pixels. 

<img src="Images/EyeDroid_Hardware.PNG?raw=true" height="300"/>

HOW TO USE?
---------

- Download, install and configure OpenCV library. Full instruction and more detailes can be found at [Open CV Eclipse tutorial] (http://docs.opencv.org/doc/tutorials/introduction/android_binary_package/O4A_SDK.html).
- Download, and configure Android NDK. Full instruction and more detailes can be found at [Using the NDK plugin](http://tools.android.com/recent/usingthendkplugin).
- Download the source code and open the solution in a compliant [Eclipse](https://eclipse.org/) version. Add a project dependency to OpenCV library.
- Compile and install the app on an Android Device.

HOW TO CONNECT A CLIENT
---------

In order to start consuming coordinates from EyeDroid, any client can connect via TCP protocol to the port 5000. After connecting, the client will automatically start receiving the resulting coordinates.
The result format consists in a byte array of three integers (12 bytes in total), where:
- The first 4 bytes is a testing message (integer).
- The second 4 bytes is the X-axis coordinate (integer).
- The third 4 bytes is the Y-axis coordinate (integer).

GLASS GAZE INTEGRATION
---------

In [glassgaze_demo branch](https://github.com/centosGit/EyeDroid/tree/glassgaze_demo), EyeDroid is integrated to the [GLAZZ GAZE application client] (https://github.com/dmardanbeigi/GlassGaze). The original project was forked and small modifications were done in order to provide extra functionallity.
The changes made are listed bellow:
- Changes in calibration process were made in order to fit EyeDroid.
- An activity was added to conduct an experiment. The experiment consisted in showing a set of random dots on the glass display while coordinates were sampled from EyeDroid in order to evaluate the system accuracy.

<img src="Images/EyeDroid_GlassGaze.PNG?raw=true" height="300"/>

Collaborators:
--------------
Daniel Garcia <dgac@itu.dk>, Ioannis Sintos <isin@itu.dk>, Diako Mardanbegi <dima@itu.dk>, Shahram Jalalinia <jsha@itu.dk>

[IT University of Copenhagen](www.itu.dk/en)
