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
		const static int BEFORE_THRESHOLD_ERODE = 4;
		const static int BEFORE_THRESHOLD_DILATE = 3;
		const static int AFTER_THRESHOLD_ERODE = 16;
		const static int AFTER_THRESHOLD_DILATE = 8;
	};


	class PupilROI{
	public:
		const static int DIAMETER_FACTOR = 3;
		const static int ROI_CONSTANT_X = 100;
		const static int ROI_CONSTANT_Y = 100;
		const static int ROI_CONSTANT_W = 500;
		const static int ROI_CONSTANT_H = 350;
		const static int ROI_PUPIL_FOUND_W = 300;
		const static int ROI_PUPIL_FOUND_H = 200;
	};


	class Thresshold{
	public:
		const static int LOWER_LIMIT = 90;
	};

	class BlobDetection{
	public:
		const static int MIN_NEIGHBOR_DISTANCE_FACTOR = 4;
		const static int MIN_BLOB_SIZE = 20;
		const static int MAX_BLOB_SIZE = 50;
		const static int UPPER_THRESHOLD = 250;
		const static int THRESHOLD_CENTER = 20;
		const static int SCALE_FACTOR = 2;
	};

};
/*end of class Config.h*/

}

#endif /* CONFIG_H_ */
