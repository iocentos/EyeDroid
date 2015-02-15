package dk.itu.eyedroid.io.protocols;

import java.io.IOException;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import android.graphics.Bitmap;
import dk.itu.eyedroid.Constants;
import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.io.IOProtocolReader;

public class InputStreamNet implements IOProtocolReader {

	private String mUrl;
	private MjpegInputStream mInputStream;

	public InputStreamNet(String url) {
		mUrl = url;
	}

	@Override
	public void cleanup() {
		try {
			mInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() throws IOException {
		mInputStream = MjpegInputStream.read(mUrl);
		if (mInputStream == null) {
			throw new IOException();
		}
	}

	@Override
	public Bundle read() throws IOException {
		Bundle bundle = new Bundle();
		Bitmap bitmap = mInputStream.readMjpegFrame();

		Mat mat = new Mat();
		Utils.bitmapToMat(bitmap, mat);

		bundle.put(Constants.SOURCE_MAT_RGB, mat);
		bundle.put(Constants.SOURCE_BITMAP, bitmap);

		return bundle;
	}
}