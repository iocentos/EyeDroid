/*
 * EyeDroid.cpp
 *
 *  Created on: Nov 2, 2014
 *      Author: centos
 */

#include "EyeDroid.h"


namespace IMGP {


cv::Rect EyeDroid::getPupilRoi(cv::Mat& input, bool useDiameter) {
	cv::Rect* pupilROI;

	Eye* tempEye = new Eye();
	Eye::getInstance(tempEye);

	if (tempEye->isPupilFound()) {
//	if( false ){
		int W = 0;
		int H = 0;
		if (useDiameter) {
			W = (tempEye->getPupilDiameter() > 0) ?
					(int) tempEye->getPupilDiameter()
							* Config::PupilROI::DIAMETER_FACTOR :
					Config::PupilROI::ROI_PUPIL_FOUND_W;

			H = Config::PupilROI::ROI_PUPIL_FOUND_H;
		} else {
			W = Config::PupilROI::ROI_PUPIL_FOUND_W;
			H = Config::PupilROI::ROI_PUPIL_FOUND_H;
		}

		if (tempEye->getPupil_X() - (W / 2) > 0
						&& tempEye->getPupil_Y() - (H / 2) > 0
						&& tempEye->getPupil_X() + (W + 200 - (W / 2)) < input.cols
						&& tempEye->getPupil_Y() + (H + 150 - (H / 2)) < input.rows) {
					pupilROI = new cv::Rect(tempEye->getPupil_X() - (W / 2),
							tempEye->getPupil_Y() - (H / 2), W + 200, H + 150);
		} else {
			pupilROI = new cv::Rect(Config::PupilROI::ROI_CONSTANT_X,
					Config::PupilROI::ROI_CONSTANT_Y,
					Config::PupilROI::ROI_CONSTANT_W,
					Config::PupilROI::ROI_CONSTANT_H);
		}
	} else {
		pupilROI = new cv::Rect(Config::PupilROI::ROI_CONSTANT_X,
				Config::PupilROI::ROI_CONSTANT_Y,
				Config::PupilROI::ROI_CONSTANT_W,
				Config::PupilROI::ROI_CONSTANT_H);
	}

	delete tempEye;
	return *pupilROI;
}

void EyeDroid::convertToGray(cv::Mat& input, cv::Mat& output) {
	cv::cvtColor(input, output, CV_RGB2GRAY);
}

void EyeDroid::erodeDilate(cv::Mat& input, cv::Mat& output, int erodes,
		int dilations) {

	for (int i = 0; i < erodes; i++)
		cv::erode(input, output, cv::Mat(), cv::Point(-1, -1), 2, 1, 1);

	for (int i = 0; i < dilations; i++)
		cv::dilate(input, output, cv::Mat(), cv::Point(-1, -1), 2, 1, 1);
}

void EyeDroid::thresholdImage(cv::Mat& input, cv::Mat& output) {
	cv::threshold(input, output, Config::Thresshold::LOWER_LIMIT, 255,
			CV_THRESH_BINARY);

}

/*
std::vector<cv::Vec3f> EyeDroid::detectBlobs(cv::Mat& output) {

	std::vector<cv::Vec3f> circles;

	// Apply the Hough Transform to find the circles
	HoughCircles(output, circles, CV_HOUGH_GRADIENT,
			Config::BlobDetection::SCALE_FACTOR,
			output.rows / Config::BlobDetection::MIN_NEIGHBOR_DISTANCE_FACTOR,
			Config::BlobDetection::UPPER_THRESHOLD,
			Config::BlobDetection::THRESHOLD_CENTER,
			Config::BlobDetection::MIN_BLOB_SIZE,
			Config::BlobDetection::MAX_BLOB_SIZE);
	return circles;
}
*/

std::vector<cv::Vec3f> EyeDroid::detectBlobs(cv::Mat& output) {

	cv::SimpleBlobDetector::Params params;
	params.minDistBetweenBlobs = 40.0f;
	params.filterByInertia = false;
	params.filterByConvexity = false;
	params.minConvexity = 1;
	params.maxConvexity = 10000;
	params.filterByColor = 0;
	params.filterByCircularity = false;
	params.filterByArea = true;
	params.minArea = 2000.0f;
	params.maxArea = 20000.0f;

	std::vector<cv::Vec3f> circles;

	cv::SimpleBlobDetector blob_detector(params);

	// detect!
	std::vector<cv::KeyPoint> keypoints;
	blob_detector.detect(output, keypoints);


	cv::Vec3f* array = new cv::Vec3f[keypoints.size()];

	for (int i=0; i<keypoints.size(); i++){

		cv::Vec3f p;
		p[0] = keypoints[i].pt.x;
		p[1] = keypoints[i].pt.y;
		p[2] = 30;
		array[i] = p;
	}

	circles.insert(circles.begin() , array , array + keypoints.size());
	return circles;
}

void EyeDroid::detectPupil(cv::Mat& input, std::vector<cv::Vec3f> circles) {

	cv::Vec3f selectedBlob;

	if (circles.size() == 0) {
		Eye::nullInstance();
	} else {
		double minDist = 100000;

		cv::Point center;
		int radius;

		for (int i = 0; i < circles.size(); i++) {
			cv::Point centerTemp(cvRound(circles[i][0]),
					cvRound(circles[i][1]));
			center = centerTemp;

			radius = cvRound(circles[i][2]);

			double dist = sqrt(
					pow(center.x - input.cols / 2, 2)
							+ pow(center.y - input.rows / 2, 2));

			if (dist < minDist) {
				minDist = dist;
				selectedBlob = circles[i];
			}
		}

		Eye::newInstance(selectedBlob[0], selectedBlob[1], selectedBlob[2] * 2);
	}
}

void EyeDroid::drawPupil(cv::Mat& input,cv::Rect roi ) {

	Eye* tempEye = new Eye();
	Eye::getInstance(tempEye);

	if (tempEye->isPupilFound()) {
		cv::circle(input,
				cv::Point(tempEye->getPupil_X() + roi.x, tempEye->getPupil_Y() + roi.y),
				tempEye->getPupilDiameter() / 8, cv::Scalar(0, 255, 0), -1, 8,
				0);
	}
	delete tempEye;
}

}

