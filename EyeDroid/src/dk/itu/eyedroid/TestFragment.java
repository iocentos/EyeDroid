package dk.itu.eyedroid;

import java.io.IOException;

import org.opencv.android.CameraBridgeViewBase;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import dk.itu.eyedroid.io.IOAndroidController;
import dk.itu.eyedroid.io.protocols.InputNetStreamingProtocol;
import dk.itu.eyedroid.io.protocols.InputStreamCamera;
import dk.itu.eyedroid.io.protocols.InputStreamUSBCamera;
import dk.itu.eyedroid.io.protocols.OutputNetTCPProtocol;
import dk.itu.spcl.jlpf.core.Filter;
import dk.itu.spcl.jlpf.core.ProcessingCore;
import dk.itu.spcl.jlpf.io.IOController;
import dk.itu.spcl.jlpf.io.IOProtocolWriter;
import dk.itu.spcl.jlpf.io.IORWDefaultImpl;

public class TestFragment extends Fragment {
	
	final String URL = "http://217.197.157.7:7070/axis-cgi/mjpg/video.cgi?resolution=320x240";

	private View mRootView;
	private ImageView mImageView;

	private ProcessingCore core;
	private IOController ioController;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.streaming_layout, container,false);
		mImageView = (ImageView) mRootView.findViewById(R.id.mjpeg_view);
		
		CameraBridgeViewBase camera = (CameraBridgeViewBase) mRootView.findViewById(R.id.opencv_camera_view);

//		InputNetStreamingProtocol inProtocol = new InputNetStreamingProtocol(URL);
//		InputStreamCamera inProtocol = new InputStreamCamera(getActivity(), 
//				camera	, CameraInfo.CAMERA_FACING_BACK);
		
		InputStreamUSBCamera inProtocol = new InputStreamUSBCamera(getActivity(), 3);
//		OutputNetTCPProtocol outProtocol = new OutputNetTCPProtocol(6000);
		TestWriter outProtocol = new TestWriter();
		IORWDefaultImpl io_rw = new IORWDefaultImpl(inProtocol, outProtocol);

		core = new ProcessingCore();
		core.addFilter(new TestFilter());
		core.addFilter(new TestFilter());

		ioController = new IOAndroidController(core, io_rw, io_rw);

		return mRootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		core.start(1);
		ioController.start();
	}

	@Override
	public void onPause() {
		super.onPause();
		core.stop();
		ioController.stop();
	}

	public class TestWriter implements IOProtocolWriter {

		@Override
		public void cleanup() {

		}

		@Override
		public void init() {

		}

		@Override
		public void write(dk.itu.spcl.jlpf.common.Bundle arg0) throws IOException {
			Log.i("---------------", "Writer is active");

			final Bitmap bitmap = (Bitmap) arg0.get(InputNetStreamingProtocol.INPUT_BITMAP);
			TestFragment.this.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mImageView.setImageBitmap(bitmap);
				}
			});
			arg0 = null;
		}
	}

	public class TestFilter extends Filter {
		@Override
		protected dk.itu.spcl.jlpf.common.Bundle execute( dk.itu.spcl.jlpf.common.Bundle arg0) {
			Log.i("---------------", "in filter");
			return arg0;
		}
	}
}