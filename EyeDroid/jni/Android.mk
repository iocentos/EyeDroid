LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := EyeDroid
LOCAL_SRC_FILES := EyeDroid.cpp

include $(BUILD_SHARED_LIBRARY)
