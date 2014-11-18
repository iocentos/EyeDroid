/*
 * Config.h
 *
 *  Created on: Nov 4, 2014
 *      Author: centos
 */

#ifndef CONFIG_H_
#define CONFIG_H_

namespace IMGP {

class Config {
private:

protected:

public:


	class ErodeDilate{
	public:
		 static int BEFORE_THRESHOLD_ERODE;
		 static int BEFORE_THRESHOLD_DILATE;
		 static int AFTER_THRESHOLD_ERODE;
		 static int AFTER_THRESHOLD_DILATE;
	};


	class PupilROI{
	public:
		 static int DIAMETER_FACTOR;
		 static int ROI_CONSTANT_X;
		 static int ROI_CONSTANT_Y;
		 static int ROI_CONSTANT_W;
		 static int ROI_CONSTANT_H;
		 static int ROI_PUPIL_FOUND_W;
		 static int ROI_PUPIL_FOUND_H;
	};


	class Thresshold{
	public:
		 static int LOWER_LIMIT;
	};

	class BlobDetection{
	public:
		 static int MIN_NEIGHBOR_DISTANCE_FACTOR;
		 static int MIN_BLOB_SIZE;
		 static int MAX_BLOB_SIZE;
		 static int UPPER_THRESHOLD;
		 static int THRESHOLD_CENTER;
		 static int SCALE_FACTOR;
	};

};
/*end of class Config.h*/

}

#endif /* CONFIG_H_ */
