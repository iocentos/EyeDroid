/*
 * Config.cpp
 *
 *  Created on: Nov 18, 2014
 *      Author: centos
 */

#include "Config.h"

namespace IMGP {
int Config::ErodeDilate::BEFORE_THRESHOLD_ERODE = 4;
int Config::ErodeDilate::BEFORE_THRESHOLD_DILATE = 3;
int Config::ErodeDilate::AFTER_THRESHOLD_ERODE = 16;
int Config::ErodeDilate::AFTER_THRESHOLD_DILATE = 8;

int Config::PupilROI::DIAMETER_FACTOR = 3;
int Config::PupilROI::ROI_CONSTANT_X = 100;
int Config::PupilROI::ROI_CONSTANT_Y = 100;
int Config::PupilROI::ROI_CONSTANT_W = 500;
int Config::PupilROI::ROI_CONSTANT_H = 350;
int Config::PupilROI::ROI_PUPIL_FOUND_W = 300;
int Config::PupilROI::ROI_PUPIL_FOUND_H = 200;

int Config::Thresshold::LOWER_LIMIT = 90;

int Config::BlobDetection::MIN_NEIGHBOR_DISTANCE_FACTOR = 4;
int Config::BlobDetection::MIN_BLOB_SIZE = 20;
int Config::BlobDetection::MAX_BLOB_SIZE = 50;
int Config::BlobDetection::UPPER_THRESHOLD = 250;
int Config::BlobDetection::THRESHOLD_CENTER = 20;
int Config::BlobDetection::SCALE_FACTOR = 2;

}
