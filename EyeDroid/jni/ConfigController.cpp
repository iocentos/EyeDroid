/*
 * ConfigController.cpp
 */

#include "ConfigController.h"
#include "Config.h"

/*
 * Class:dk_itu_eyedroid_settings_Config
 * Method:getBeforeErode
 * Signature:()I
 */
JNIEXPORT jint JNICALL Java_dk_itu_eyedroid_settings_Config_getBeforeErode(JNIEnv *, jclass) {
	return IMGP::Config::ErodeDilate::BEFORE_THRESHOLD_ERODE;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: getBeforeDilate
 * Signature:()I
 */
JNIEXPORT jint JNICALL Java_dk_itu_eyedroid_settings_Config_getBeforeDilate(JNIEnv *, jclass) {
	return IMGP::Config::ErodeDilate::BEFORE_THRESHOLD_DILATE;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: getAfterErode
 * Signature:()I
 */
JNIEXPORT jint JNICALL Java_dk_itu_eyedroid_settings_Config_getAfterErode(JNIEnv *, jclass) {
	return IMGP::Config::ErodeDilate::AFTER_THRESHOLD_ERODE;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: getAfterDilate
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_dk_itu_eyedroid_settings_Config_getAfterDilate( JNIEnv *, jclass) {
	return IMGP::Config::ErodeDilate::AFTER_THRESHOLD_DILATE;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: getDiameterFactor
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_dk_itu_eyedroid_settings_Config_getDiameterFactor(JNIEnv *, jclass) {
	return IMGP::Config::PupilROI::DIAMETER_FACTOR;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: getConstantRoi_X
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_dk_itu_eyedroid_settings_Config_getConstantRoi_1X(JNIEnv *, jclass) {
	return IMGP::Config::PupilROI::ROI_CONSTANT_X;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: getConstantRoi_Y
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_dk_itu_eyedroid_settings_Config_getConstantRoi_1Y(JNIEnv *, jclass) {
	return IMGP::Config::PupilROI::ROI_CONSTANT_Y;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: getConstantRoi_W
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_dk_itu_eyedroid_settings_Config_getConstantRoi_1W(JNIEnv *, jclass) {
	return IMGP::Config::PupilROI::ROI_CONSTANT_W;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: getConstantRoi_H
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_dk_itu_eyedroid_settings_Config_getConstantRoi_1H(JNIEnv *, jclass) {
	return IMGP::Config::PupilROI::ROI_CONSTANT_H;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: getPupilFoundRoi_W
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_dk_itu_eyedroid_settings_Config_getPupilFoundRoi_1W(JNIEnv *, jclass) {
	return IMGP::Config::PupilROI::ROI_PUPIL_FOUND_W;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: getPupilFoundRoi_H
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_dk_itu_eyedroid_settings_Config_getPupilFoundRoi_1H(JNIEnv *, jclass) {
	return IMGP::Config::PupilROI::ROI_PUPIL_FOUND_H;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: getThresholdLimit
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_dk_itu_eyedroid_settings_Config_getThresholdLimit(JNIEnv *, jclass) {
	return IMGP::Config::Thresshold::LOWER_LIMIT;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: getMinNeighborBlob
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_dk_itu_eyedroid_settings_Config_getMinNeighborBlob(JNIEnv *, jclass) {
	return IMGP::Config::BlobDetection::MIN_NEIGHBOR_DISTANCE_FACTOR;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: getMinBlobSize
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_dk_itu_eyedroid_settings_Config_getMinBlobSize(JNIEnv *, jclass) {
	return IMGP::Config::BlobDetection::MIN_BLOB_SIZE;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: getMaxBlobSize
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_dk_itu_eyedroid_settings_Config_getMaxBlobSize(JNIEnv *, jclass) {
	return IMGP::Config::BlobDetection::MAX_BLOB_SIZE;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: getUpperThreshold
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_dk_itu_eyedroid_settings_Config_getUpperThreshold(JNIEnv *, jclass) {
	return IMGP::Config::BlobDetection::UPPER_THRESHOLD;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: getThresholdCenter
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_dk_itu_eyedroid_settings_Config_getThresholdCenter(JNIEnv *, jclass) {
	return IMGP::Config::BlobDetection::THRESHOLD_CENTER;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: getScaleFactor
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_dk_itu_eyedroid_settings_Config_getScaleFactor(JNIEnv *, jclass) {
	return IMGP::Config::BlobDetection::SCALE_FACTOR;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: setBeforeErode
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_dk_itu_eyedroid_settings_Config_setBeforeErode
(JNIEnv *, jclass, jint value) {
	IMGP::Config::ErodeDilate::BEFORE_THRESHOLD_ERODE = value;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: setBeforeDilate
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_dk_itu_eyedroid_settings_Config_setBeforeDilate
(JNIEnv *, jclass, jint value) {
	IMGP::Config::ErodeDilate::BEFORE_THRESHOLD_DILATE = value;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: setAfterErode
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_dk_itu_eyedroid_settings_Config_setAfterErode
(JNIEnv *, jclass, jint value) {
	IMGP::Config::ErodeDilate::AFTER_THRESHOLD_ERODE = value;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: setAfterDilate
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_dk_itu_eyedroid_settings_Config_setAfterDilate
(JNIEnv *, jclass, jint value) {
	IMGP::Config::ErodeDilate::AFTER_THRESHOLD_DILATE = value;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: setDiameterFactor
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_dk_itu_eyedroid_settings_Config_setDiameterFactor
(JNIEnv *, jclass, jint value) {
	IMGP::Config::PupilROI::DIAMETER_FACTOR = value;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: setConstantRoi_X
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_dk_itu_eyedroid_settings_Config_setConstantRoi_1X
(JNIEnv *, jclass, jint value) {
	IMGP::Config::PupilROI::ROI_CONSTANT_X = value;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: setConstantRoi_Y
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_dk_itu_eyedroid_settings_Config_setConstantRoi_1Y
(JNIEnv *, jclass, jint value) {
	IMGP::Config::PupilROI::ROI_CONSTANT_Y = value;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: setConstantRoi_W
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_dk_itu_eyedroid_settings_Config_setConstantRoi_1W
(JNIEnv *, jclass, jint value) {
	IMGP::Config::PupilROI::ROI_CONSTANT_W = value;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: setConstantRoi_H
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_dk_itu_eyedroid_settings_Config_setConstantRoi_1H
(JNIEnv *, jclass, jint value) {

	IMGP::Config::PupilROI::ROI_CONSTANT_H = value;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: setPupilFoundRoi_W
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_dk_itu_eyedroid_settings_Config_setPupilFoundRoi_1W
(JNIEnv *, jclass, jint value) {
	IMGP::Config::PupilROI::ROI_PUPIL_FOUND_W = value;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: setPupilFoundRoi_H
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_dk_itu_eyedroid_settings_Config_setPupilFoundRoi_1H
(JNIEnv *, jclass, jint value) {

	IMGP::Config::PupilROI::ROI_PUPIL_FOUND_H = value;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: setThresholdLimit
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_dk_itu_eyedroid_settings_Config_setThresholdLimit
(JNIEnv *, jclass, jint value) {
	IMGP::Config::Thresshold::LOWER_LIMIT = value;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: setMinNeighborBlob
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_dk_itu_eyedroid_settings_Config_setMinNeighborBlob
(JNIEnv *, jclass, jint value) {
	IMGP::Config::BlobDetection::MIN_NEIGHBOR_DISTANCE_FACTOR = value;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: setMinBlobSize
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_dk_itu_eyedroid_settings_Config_setMinBlobSize
(JNIEnv *, jclass, jint value) {
	IMGP::Config::BlobDetection::MIN_BLOB_SIZE = value;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: setMaxBlobSize
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_dk_itu_eyedroid_settings_Config_setMaxBlobSize
(JNIEnv *, jclass, jint value) {

	IMGP::Config::BlobDetection::MAX_BLOB_SIZE = value;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: setUpperThreshold
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_dk_itu_eyedroid_settings_Config_setUpperThreshold
(JNIEnv *, jclass, jint value) {
	IMGP::Config::BlobDetection::UPPER_THRESHOLD = value;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: setThresholdCenter
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_dk_itu_eyedroid_settings_Config_setThresholdCenter
(JNIEnv *, jclass, jint value) {
	IMGP::Config::BlobDetection::THRESHOLD_CENTER = value;
}

/*
 * Class: dk_itu_eyedroid_settings_Config
 * Method: setScaleFactor
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_dk_itu_eyedroid_settings_Config_setScaleFactor
(JNIEnv *, jclass, jint value) {
	IMGP::Config::BlobDetection::SCALE_FACTOR = value;
}

