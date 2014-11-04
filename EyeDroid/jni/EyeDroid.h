/*
 * EyeDroid.h
 *
 *  Created on: Nov 2, 2014
 *      Author: centos
 */

#include "Eye.h"
#include "Config.h"


#include <string>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/contrib/contrib.hpp>
#include <vector>


#ifndef EYEDROID_H_
#define EYEDROID_H_

namespace IMGP {

class EyeDroid {
private:

protected:

public:
	static cv::Rect getPupilRoi(cv::Mat&, bool diameter);
	static void convertToGray(cv::Mat&, cv::Mat&);
	static void erodeDilate(cv::Mat&, cv::Mat&, int erodes, int dilations);
	static void thresholdImage(cv::Mat&, cv::Mat&);
	static std::vector<cv::Vec3f> detectBlobs(cv::Mat&);
	static void detectPupil(cv::Mat&, std::vector<cv::Vec3f>);
	static void drawPupil(cv::Mat&, cv::Rect);

};
/*end of class EyeDroid.h*/

}

#endif /* EYEDROID_H_ */

