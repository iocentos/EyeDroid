package dk.itu.eyedroid.io.protocols;

import java.io.IOException;

import android.graphics.Bitmap;
import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.io.IOProtocolReader;

public class InputNetStreamingProtocol implements IOProtocolReader{
	
	public static final String INPUT_BITMAP = "initial_bitmap";
	public static final String INPUT_RGBA_MAT = "rgba";
	public static final String INPUT_GRAY_MAT = "gray";
	
	private String mUrl;
	private MjpegInputStream mInputStream;
	
	public InputNetStreamingProtocol(String url) {
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
	public void init() throws IOException{
		mInputStream = MjpegInputStream.read(mUrl);
		if( mInputStream == null ){
			throw new IOException();
		}
	}

	@Override
	public Bundle read() throws IOException{
		Bundle bundle = new Bundle();
		Bitmap bitmap = mInputStream.readMjpegFrame();
		
		bundle.put(INPUT_BITMAP, bitmap);
		
		return bundle;
	}
}