package dk.itu.eyedroid.filters;

import dk.itu.eyedroid.Constants;
import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.core.Filter;
/**
 * Algorithm step: 7
 * Finally, because the previous step can detect many circles, the one that is 
 * closest to the center of the image is taken as the pupil and the 2-axis coordinates 
 * are computed. The location of the detected pupil is used later as feedback to the 
 * first algorithm step in order to compute the ROI on subsequent frames.
 */
public class CoordinatesFilter extends Filter{

	@Override
	protected Bundle execute(Bundle arg0) {
		
		arg0.put(Constants.PUPIL_COORDINATES_X, getCoordinates_X());
		arg0.put(Constants.PUPIL_COORDINATES_Y, getCoordinates_Y());
		
		return arg0;
	}
	
	public static native int getCoordinates_X();
	public static native int getCoordinates_Y();
}
