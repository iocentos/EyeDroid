package dk.itu.eyedroid.filters;

import dk.itu.eyedroid.Constants;
import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.core.Filter;

public class CoordinatesFilter extends Filter {

	@Override
	protected Bundle execute(Bundle arg0) {

		arg0.put(Constants.PUPIL_COORDINATES_X, getCoordinates_X());
		arg0.put(Constants.PUPIL_COORDINATES_Y, getCoordinates_Y());

		return arg0;
	}

	public static native int getCoordinates_X();

	public static native int getCoordinates_Y();
}
