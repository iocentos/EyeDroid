/*
 * EyeDetector2.cpp
 *
 *  Created on: Sep 26, 2014
 *      Author: centos
 */


#include "EyeDetector.h"



namespace IMGP {

using namespace cv;


EyeDetector::EyeDetector(): totalFrames(0) , avgPoint(NULL) , totalX(0) , totalY(0){
	totalFrames = 0;
}


void EyeDetector::process(cv::Mat& input, cv::Mat& output) {

	cv::cvtColor(input, output, CV_BGR2GRAY);

	cv::equalizeHist(output, output);

	GaussianBlur(output, output, cv::Size(9, 9), 2, 2);

	cv::threshold(output, output, 10, 255, CV_THRESH_BINARY);

//	if( avgPoint != NULL ){
//		cv::Mat mask = cv::Mat::zeros(output.size() , CV_8UC1);
//
//		mask = 255;
//
//		imshow("Eye" , mask);
//
//	}



	cv::erode(output, output, cv::Mat(), cv::Point(-1, -1), 2, 1, 1);
//	cv::erode(output, output, cv::Mat(), cv::Point(-1, -1), 2, 1, 1);
	cv::dilate(output, output, cv::Mat(), cv::Point(-1, -1), 2, 1, 1);
//	cv::dilate(output, output, cv::Mat(), cv::Point(-1, -1), 2, 1, 1);
	cv::erode(output, output, cv::Mat(), cv::Point(-1, -1), 2, 1, 1);
	cv::dilate(output, output, cv::Mat(), cv::Point(-1, -1), 2, 1, 1);


	detectCircles(input , output);

}

void EyeDetector::detectCorners( cv::Mat& original , cv::Mat& output ){


	  Mat dst, dst_norm, dst_norm_scaled;
	  dst = Mat::zeros( output.size(), CV_32FC1 );

	  /// Detector parameters
	  int blockSize = 2;
	  int apertureSize = 3;
	  double k = 0.04;

	  /// Detecting corners
	  cornerHarris( output, dst, blockSize, apertureSize, k, BORDER_DEFAULT );

	  /// Normalizing
	  normalize( dst, dst_norm, 0, 255, NORM_MINMAX, CV_32FC1, Mat() );
	  convertScaleAbs( dst_norm, dst_norm_scaled );

	  /// Drawing a circle around corners
	  for( int j = 0; j < dst_norm.rows ; j++ )
	     { for( int i = 0; i < dst_norm.cols; i++ )
	          {
	            if( (int) dst_norm.at<float>(j,i) > 200 )
	              {
	               circle( dst_norm_scaled, Point( i, j ), 5,  Scalar(0), 2, 8, 0 );
	              }
	          }
	     }
	  /// Showing the result
	  namedWindow( "Corners", CV_WINDOW_AUTOSIZE );
	  imshow( "Corners", dst_norm_scaled );
}


void EyeDetector::detectCircles(cv::Mat& original, cv::Mat& output) {
	std::vector<cv::Vec3f> circles;

	// Apply the Hough Transform to find the circles
	HoughCircles(output, circles, CV_HOUGH_GRADIENT, 2, output.rows, 250, 20,
			20, 50);


	/// Draw the circles detected
	for (size_t i = 0; i < circles.size() && i < 1; i++) {
		totalFrames++;
		cv::Point center(cvRound(circles[i][0]), cvRound(circles[i][1]));
		int radius = cvRound(circles[i][2]);

		if( avgPoint == NULL ){
			avgPoint = new cv::Point(cvRound(circles[i][0]), cvRound(circles[i][1]));
		}else{

			totalX += center.x;
			totalY += center.y;

			avgPoint->x = totalX / totalFrames;
			avgPoint->y = totalY / totalFrames;
		}


		// circle center
		cv::circle(original, center, 3, cv::Scalar(0, 255, 0), -1, 8, 0);
		// circle outline
		cv::circle(original, center, radius, cv::Scalar(0, 0, 255), 3, 8, 0);
	}

}

}
