package dk.itu.eyedroid.io.protocols;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import dk.itu.eyedroid.Constants;
import dk.itu.eyedroid.MainActivity;
import dk.itu.eyedroid.MainFragment;
import dk.itu.eyedroid.filters.RGB2GRAYFilter;
import dk.itu.spcl.jlpf.io.IOProtocolWriter;

public class PreviewProtocol implements IOProtocolWriter{
	
	private Context mContext;
	private ImageView mImageView;
	
	public PreviewProtocol(Context context , ImageView imageView) {
		mContext = context;
		mImageView = imageView;
	}

	@Override
	public void cleanup() {
	}

	@Override
	public void init() {
	}

	@Override
	public void write(dk.itu.spcl.jlpf.common.Bundle arg0) throws IOException {
		Log.i("---------------", "Writer is active");

		final Bitmap bitmap = (Bitmap) arg0.get(Constants.SINK_BITMAP);
		Log.i(RGB2GRAYFilter.TAG, "----------------------------------------");
		arg0 = null;
		((MainActivity)mContext).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mImageView.setImageBitmap(bitmap);
				
			}
		});
	}
}
