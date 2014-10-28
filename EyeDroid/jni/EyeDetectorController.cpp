/*
 * EyeDetectorController.cpp
 *
 *  Created on: Oct 28, 2014
 *      Author: centos
 */

#include "EyeDetectorController.h"
#include "EyeDetector.h"



JNIEXPORT jlong JNICALL Java_dk_itu_eyedroid_filters_EyeDetectionFilter_createNativeObject
  (JNIEnv *, jclass){


	jlong result = (jlong)new IMGP::EyeDetector();

	return result;

}
JNIEXPORT void JNICALL Java_dk_itu_eyedroid_filters_EyeDetectionFilter_destroyNativeObject
  (JNIEnv *, jclass, jlong thiz){

	delete ((IMGP::EyeDetector*)thiz);

}


JNIEXPORT void JNICALL Java_dk_itu_eyedroid_filters_EyeDetectionFilter_processNativeFrame
  (JNIEnv *, jclass, jlong thiz , jlong inputFrame , jlong outputFrame){

	((IMGP::EyeDetector*)thiz)->process( *((Mat*)inputFrame)  , *((Mat*)outputFrame)     );
}

