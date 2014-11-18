/*
 * Config.cpp
 *
 *  Created on: Nov 18, 2014
 *      Author: centos
 */

#include "Config.h"

namespace IMGP {
volatile int Config::ErodeDilate::BEFORE_THRESHOLD_ERODE = 4;
volatile int Config::ErodeDilate::BEFORE_THRESHOLD_DILATE = 3;
volatile int Config::ErodeDilate::AFTER_THRESHOLD_ERODE = 8;
volatile int Config::ErodeDilate::AFTER_THRESHOLD_DILATE = 6;

volatile int Config::PupilROI::DIAMETER_FACTOR = 3;
volatile int Config::PupilROI::ROI_CONSTANT_X = 150;
volatile int Config::PupilROI::ROI_CONSTANT_Y = 150;
volatile int Config::PupilROI::ROI_CONSTANT_W = 400;
volatile int Config::PupilROI::ROI_CONSTANT_H = 250;
volatile int Config::PupilROI::ROI_PUPIL_FOUND_W = 300;
volatile int Config::PupilROI::ROI_PUPIL_FOUND_H = 200;

volatile int Config::Thresshold::LOWER_LIMIT = 70;

volatile int Config::BlobDetection::MIN_NEIGHBOR_DISTANCE_FACTOR = 4;
volatile int Config::BlobDetection::MIN_BLOB_SIZE = 20;
volatile int Config::BlobDetection::MAX_BLOB_SIZE = 50;
volatile int Config::BlobDetection::UPPER_THRESHOLD = 250;
volatile int Config::BlobDetection::THRESHOLD_CENTER = 20;
volatile int Config::BlobDetection::SCALE_FACTOR = 2;

}
