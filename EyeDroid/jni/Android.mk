LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

OPENCV_CAMERA_MODULES:=on
OPENCV_INSTALL_MODULES:=on

include $(OPENCV_HOME)/sdk/native/jni/OpenCV.mk


LOCAL_MODULE    := EyeDroid
LOCAL_SRC_FILES := ImageProc.c EyeDetector.cpp EyeDetectorController.cpp
LOCAL_LDLIBS	:= -llog -ljnigraphics

include $(BUILD_SHARED_LIBRARY)
