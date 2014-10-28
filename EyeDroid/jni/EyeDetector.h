/*
 * EyeDetector2.h
 *
 *  Created on: Sep 26, 2014
 *      Author: centos
 */

#ifndef EYEDETECTOR2_H_
#define EYEDETECTOR2_H_


#include <string>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/contrib/contrib.hpp>
#include <vector>


namespace IMGP {

using namespace cv;

class EyeDetector{
private:

	cv::Point* avgPoint;
	long totalFrames;
	long totalX;
	long totalY;

protected:

	void detectCircles( cv::Mat& original , cv::Mat& output);

	void detectCorners( cv::Mat& original , cv::Mat& output);

public:

	EyeDetector();
	void process( cv::Mat& input , cv::Mat& output );




}; /*end of class EyeDetector.h*/


} //end of namespace

#endif /* EYEDETECTOR2_H_ */
