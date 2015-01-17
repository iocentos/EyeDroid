package dk.itu.eyedroid.filters;

import java.util.concurrent.atomic.AtomicBoolean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import dk.itu.eyedroid.Constants;
import dk.itu.eyedroid.R;
import dk.itu.eyedroid.demo.MainActivity;
import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.core.Filter;

public class PreviewFilter extends Filter {

	private Context mContext;

	private AtomicBoolean mIsPreviewShown;

	private ImageView mImageView;

	private boolean mLogoFlag = false;

	public PreviewFilter(Context context, ImageView imageView) {
		mContext = context;
		mIsPreviewShown = new AtomicBoolean();
		mImageView = imageView;
	}

	@Override
	protected Bundle execute(Bundle arg0) {

		if (mIsPreviewShown.get()) {
			mLogoFlag = false;
			final Bitmap bitmap = (Bitmap) arg0.get(Constants.SINK_BITMAP);
			((MainActivity) mContext).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mImageView.setVisibility(View.VISIBLE);
					mImageView.setImageBitmap(bitmap);
				}
			});
		} else {
			if (!mLogoFlag) {
				mLogoFlag = true;
				((MainActivity) mContext).runOnUiThread(new Runnable() {
					@SuppressLint("NewApi")
					@Override
					public void run() {
						mImageView.setVisibility(View.GONE);
					}
				});
			}
		}

		return arg0;
	}

	public boolean isEnabled() {
		return mIsPreviewShown.get();
	}

	public void enablePreview() {
		mIsPreviewShown.set(true);
	}

	public void disablePreview() {
		mIsPreviewShown.set(false);
	}

}
