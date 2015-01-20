/*
 * Config.cpp
 */

#include "Config.h"

/*
 * Default EyeDroid image processing methods configuration.
 */
namespace IMGP {
volatile int Config::ErodeDilate::BEFORE_THRESHOLD_ERODE = 3;
volatile int Config::ErodeDilate::BEFORE_THRESHOLD_DILATE = 2;
volatile int Config::ErodeDilate::AFTER_THRESHOLD_ERODE = 3;
volatile int Config::ErodeDilate::AFTER_THRESHOLD_DILATE = 2;

volatile int Config::PupilROI::DIAMETER_FACTOR = 3;
volatile int Config::PupilROI::ROI_CONSTANT_X = 100;
volatile int Config::PupilROI::ROI_CONSTANT_Y = 50;
volatile int Config::PupilROI::ROI_CONSTANT_W = 400;
volatile int Config::PupilROI::ROI_CONSTANT_H = 350;
volatile int Config::PupilROI::ROI_PUPIL_FOUND_W = 80;
volatile int Config::PupilROI::ROI_PUPIL_FOUND_H = 80;

volatile int Config::Thresshold::LOWER_LIMIT = 70;

volatile int Config::BlobDetection::MIN_NEIGHBOR_DISTANCE_FACTOR = 4;
volatile int Config::BlobDetection::MIN_BLOB_SIZE = 20;
volatile int Config::BlobDetection::MAX_BLOB_SIZE = 50;
volatile int Config::BlobDetection::UPPER_THRESHOLD = 250;
volatile int Config::BlobDetection::THRESHOLD_CENTER = 20;
volatile int Config::BlobDetection::SCALE_FACTOR = 2;

}
