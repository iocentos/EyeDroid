/*
 * EyeDroidController.cpp
 *
 *  Created on: Nov 4, 2014
 *      Author: centos
 */


#include "EyeDroidController.h"


/*
 * Class:     dk_itu_eyedroid_filters_RGB2GRAYFilter
 * Method:    rgb2gray
 * Signature: (JJ)J
 */
JNIEXPORT jlong JNICALL Java_dk_itu_eyedroid_filters_RGB2GRAYFilter_rgb2gray
  (JNIEnv *, jclass, jlong inputFrame , jlong outputFrame){

	cv::Mat* inputMat = ((cv::Mat*)inputFrame);
	cv::Mat* outputMat = ((cv::Mat*)outputFrame);


	cv::Rect pupilRoi = IMGP::EyeDroid::getPupilRoi(*inputMat , false);

	cv::Rect* pupilRoiPtr = new cv::Rect(pupilRoi.x , pupilRoi.y , pupilRoi.width , pupilRoi.height);

	cv::Mat temp = (*inputMat)(pupilRoi);

	temp.assignTo(*outputMat);

	IMGP::EyeDroid::convertToGray(*outputMat , *outputMat);

	return (jlong) pupilRoiPtr;
}


/*
 * Class:     dk_itu_eyedroid_filters_BeforeErodeDilateFilter
 * Method:    beforeErodeDilate
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_dk_itu_eyedroid_filters_BeforeErodeDilateFilter_beforeErodeDilate
  (JNIEnv *, jclass, jlong frame){

	cv::Mat* inputMat = (cv::Mat*)frame;
	IMGP::EyeDroid::erodeDilate(*inputMat , *inputMat ,IMGP::Config::ErodeDilate::BEFORE_THRESHOLD_ERODE,
			IMGP::Config::ErodeDilate::BEFORE_THRESHOLD_DILATE);
}


/*
 * Class:     dk_itu_eyedroid_filters_ThresholdFilter
 * Method:    thresholdFrame
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_dk_itu_eyedroid_filters_ThresholdFilter_thresholdFrame
  (JNIEnv *, jclass, jlong inputFrame ){

	cv::Mat* inputMat = (cv::Mat*)inputFrame;
	IMGP::EyeDroid::thresholdImage(*inputMat , *inputMat);
}


/*
 * Class:     dk_itu_eyedroid_filters_AfterErodeDilateFilter
 * Method:    afterErodeDilate
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_dk_itu_eyedroid_filters_AfterErodeDilateFilter_afterErodeDilate
  (JNIEnv *, jclass, jlong frame){

	cv::Mat* inputMat = (cv::Mat*)frame;
	IMGP::EyeDroid::erodeDilate(*inputMat , *inputMat ,IMGP::Config::ErodeDilate::AFTER_THRESHOLD_ERODE,
			IMGP::Config::ErodeDilate::AFTER_THRESHOLD_DILATE);

}


/*
 * Class:     dk_itu_eyedroid_filters_BlobDetectionFilter
 * Method:    blobDetection
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_dk_itu_eyedroid_filters_BlobDetectionFilter_blobDetection
  (JNIEnv *, jclass, jlong frame){

	cv::Mat* inputMat = (cv::Mat*)frame;
	std::vector<cv::Vec3f> circles =  IMGP::EyeDroid::detectBlobs(*inputMat);
	std::vector<cv::Vec3f>* circlesPtr = new std::vector<cv::Vec3f>(circles.size());

	for( int i = 0 ; i < circles.size() ; i++ )
		(*circlesPtr)[i] = circles[i];

	return (jlong) circlesPtr;
}

/*
 * Class:     dk_itu_eyedroid_filters_DetectAndDrawPupilFilter
 * Method:    detectPupilAndDraw
 * Signature: (JJJJ)V
 */
JNIEXPORT void JNICALL Java_dk_itu_eyedroid_filters_DetectAndDrawPupilFilter_detectPupilAndDraw
  (JNIEnv *, jclass, jlong originalFrame , jlong processedFrame , jlong pupilRoi, jlong detectedCircles ){

	cv::Mat* inputMat = (cv::Mat*)originalFrame;
	cv::Mat* outputMat = (cv::Mat*)processedFrame;

	cv::Rect* pupilRoiRect = (cv::Rect*)pupilRoi;
	std::vector<cv::Vec3f>* circles = (std::vector<cv::Vec3f>*)detectedCircles;


	IMGP::EyeDroid::detectPupil(*outputMat , *circles);
	IMGP::EyeDroid::drawPupil(*inputMat ,*pupilRoiRect);

	delete pupilRoiRect;
	delete circles;
  }
