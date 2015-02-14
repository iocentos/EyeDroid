package dk.itu.eyedroid.settings;

/*
 *This class is a wrapper around the Config.h file in the c++ code.
 *The getters and setters of the class will modify the configuration
 *of the image processing c++ methods at runtime.
 *
 * The class is used by the settings activity.
 * 
 * WARNING
 * There is no checking for the values before they are set in the
 * image processing methods. The settings activity is using sliders to 
 * set all these values usually ranging from 0 to max.
 * The real ranges of these values is not the same so if not set
 * to correct value it might cause the application to crash.
 * 
 * The default configuration of EyeDroid has been configured with these
 * values based on experimentation. If you wish to experiment to tune 
 * the image processing methods use the settings activity to change the values
 * at runtime and once you find your configuration you can hardcode it both here
 * in the Config file and in the jni/Config.h file.
 */
public class Config {

	// Algorithm configuration parameters

	public static final int BEFORE_THRESHOLD_ERODE = 0;
	public static final int BEFORE_THRESHOLD_DILATE = 1;
	public static final int AFTER_THRESHOLD_ERODE = 2;
	public static final int AFTER_THRESHOLD_DILATE = 3;

	public static final int DIAMETER_FACTOR = 4;
	public static final int ROI_CONSTANT_X = 5;
	public static final int ROI_CONSTANT_Y = 6;
	public static final int ROI_CONSTANT_W = 7;
	public static final int ROI_CONSTANT_H = 8;
	public static final int ROI_PUPIL_FOUND_W = 9;
	public static final int ROI_PUPIL_FOUND_H = 10;

	public static final int THRESHOLD_LOWER_LIMIT = 11;

	public static final int MIN_NEIGHBOR_DISTANCE_FACTOR = 12;
	public static final int MIN_BLOB_SIZE = 13;
	public static final int MAX_BLOB_SIZE = 14;
	public static final int UPPER_THRESHOLD = 15;
	public static final int THRESHOLD_CENTER = 16;
	public static final int SCALE_FACTOR = 17;

	/**
	 * Set configuration value
	 * 
	 * @param key
	 *            Key
	 * @param value
	 *            Value
	 */
	public static void setConfigValue(int key, int value) {

		switch (key) {
		case Config.BEFORE_THRESHOLD_ERODE:
			setBeforeErode(value);
			break;
		case Config.BEFORE_THRESHOLD_DILATE:
			setBeforeDilate(value);
			break;
		case Config.AFTER_THRESHOLD_ERODE:
			setAfterErode(value);
			break;
		case Config.AFTER_THRESHOLD_DILATE:
			setAfterDilate(value);
			break;
		case Config.DIAMETER_FACTOR:
			setDiameterFactor(value);
			break;
		case Config.ROI_CONSTANT_Y:
			setConstantRoi_Y(value);
			break;
		case Config.ROI_CONSTANT_X:
			setConstantRoi_X(value);
			break;
		case Config.ROI_CONSTANT_W:
			setConstantRoi_W(value);
			break;
		case Config.ROI_CONSTANT_H:
			setConstantRoi_H(value);
			break;
		case Config.ROI_PUPIL_FOUND_W:
			setPupilFoundRoi_W(value);
			break;
		case Config.ROI_PUPIL_FOUND_H:
			setPupilFoundRoi_H(value);
			break;
		case Config.THRESHOLD_LOWER_LIMIT:
			setThresholdLimit(value);
			break;
		case Config.MIN_NEIGHBOR_DISTANCE_FACTOR:
			setMinNeighborBlob(value);
			break;
		case Config.MIN_BLOB_SIZE:
			setMinBlobSize(value);
			break;
		case Config.MAX_BLOB_SIZE:
			setMaxBlobSize(value);
			break;
		case Config.UPPER_THRESHOLD:
			setUpperThreshold(value);
			break;
		case Config.THRESHOLD_CENTER:
			setThresholdCenter(value);
			break;
		case Config.SCALE_FACTOR:
			setScaleFactor(value);
			break;
		default:
			break;
		}
	}

	/**
	 * Get value
	 * 
	 * @param key
	 *            Key
	 * @return Value
	 */
	public static int getConfigValue(int key) {

		int value = 0;

		switch (key) {
		case Config.BEFORE_THRESHOLD_ERODE:
			value = getBeforeErode();
			break;
		case Config.BEFORE_THRESHOLD_DILATE:
			value = getBeforeDilate();
			break;
		case Config.AFTER_THRESHOLD_ERODE:
			value = getAfterErode();
			break;
		case Config.AFTER_THRESHOLD_DILATE:
			value = getAfterDilate();
			break;
		case Config.DIAMETER_FACTOR:
			value = getDiameterFactor();
			break;
		case Config.ROI_CONSTANT_Y:
			value = getConstantRoi_Y();

			break;
		case Config.ROI_CONSTANT_X:
			value = getConstantRoi_X();

			break;
		case Config.ROI_CONSTANT_W:
			value = getConstantRoi_W();
			break;

		case Config.ROI_CONSTANT_H:
			value = getConstantRoi_H();
			break;

		case Config.ROI_PUPIL_FOUND_W:
			value = getPupilFoundRoi_W();
			break;
		case Config.ROI_PUPIL_FOUND_H:
			value = getPupilFoundRoi_H();
			break;
		case Config.THRESHOLD_LOWER_LIMIT:
			value = getThresholdLimit();
			break;
		case Config.MIN_NEIGHBOR_DISTANCE_FACTOR:
			value = getMinNeighborBlob();
			break;

		case Config.MIN_BLOB_SIZE:
			value = getMinBlobSize();
			break;
		case Config.MAX_BLOB_SIZE:
			value = getMaxBlobSize();
			break;
		case Config.UPPER_THRESHOLD:
			value = getUpperThreshold();
			break;
		case Config.THRESHOLD_CENTER:
			value = getThresholdCenter();
			break;

		case Config.SCALE_FACTOR:
			value = getScaleFactor();
			break;

		default:
			break;
		}
		return value;
	}

	// getters
	public static native int getBeforeErode();

	public static native int getBeforeDilate();

	public static native int getAfterErode();

	public static native int getAfterDilate();

	public static native int getDiameterFactor();

	public static native int getConstantRoi_X();

	public static native int getConstantRoi_Y();

	public static native int getConstantRoi_W();

	public static native int getConstantRoi_H();

	public static native int getPupilFoundRoi_W();

	public static native int getPupilFoundRoi_H();

	public static native int getThresholdLimit();

	public static native int getMinNeighborBlob();

	public static native int getMinBlobSize();

	public static native int getMaxBlobSize();

	public static native int getUpperThreshold();

	public static native int getThresholdCenter();

	public static native int getScaleFactor();

	// setters
	public static native void setBeforeErode(int value);

	public static native void setBeforeDilate(int value);

	public static native void setAfterErode(int value);

	public static native void setAfterDilate(int value);

	public static native void setDiameterFactor(int value);

	public static native void setConstantRoi_X(int value);

	public static native void setConstantRoi_Y(int value);

	public static native void setConstantRoi_W(int value);

	public static native void setConstantRoi_H(int value);

	public static native void setPupilFoundRoi_W(int value);

	public static native void setPupilFoundRoi_H(int value);

	public static native void setThresholdLimit(int value);

	public static native void setMinNeighborBlob(int value);

	public static native void setMinBlobSize(int value);

	public static native void setMaxBlobSize(int value);

	public static native void setUpperThreshold(int value);

	public static native void setThresholdCenter(int value);

	public static native void setScaleFactor(int value);
}