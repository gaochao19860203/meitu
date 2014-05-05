package com.example.testmei2;

import java.io.ByteArrayOutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private static final String TAG = "mei";
	private Button button;
	private ImageView imageView;
	private EditImage editImage;
	private Point leftUpPoint;
	private Point rightDownPoint;
	private Bitmap bit;
	private Bitmap newBitmap;
	private Intent intent;
	private byte[] bf;
	private Button size1;

	//	private RelativeLayout relative1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		button = (Button) findViewById(R.id.button_img);
		imageView = (ImageView) findViewById(R.id.back_image);
		editImage = (EditImage) findViewById(R.id.edit_image);
		size1 = (Button) findViewById(R.id.size1);
		//		relative1 = (RelativeLayout) findViewById(R.id.relative1);

		//		ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
		//		viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
		//
		//			@Override
		//			public boolean onPreDraw() {
		//				imageView.getViewTreeObserver().removeOnPreDrawListener(this);
		//				Log.e(TAG, "width: " + imageView.getWidth());
		//				Log.e(TAG, "height: " + imageView.getHeight());
		//				Log.e(TAG, "left: " + imageView.getLeft());
		//				Log.e(TAG, "top: " + imageView.getTop());
		//				Log.e(TAG, "right: " + imageView.getRight());
		//				Log.e(TAG, "bottom: " + imageView.getBottom());
		//
		//				Log.e(TAG, "" + editImage.getLeftUpPoint());
		//				Log.e(TAG, "" + editImage.getRightDownPoint());
		//				Log.e(TAG, "new " + editImage.getLeftUpPoint());
		//				Log.e(TAG, "new " + editImage.getRightDownPoint());
		//				return true;
		//			}
		//
		//		});

		button.setOnClickListener(new View.OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {

				leftUpPoint = editImage.getLeftUpPoint();
				rightDownPoint = editImage.getRightDownPoint();

				Log.e(TAG, "leftUpPoint.x: " + leftUpPoint.x + "     leftUpPoint.y:" + leftUpPoint.y);
				Log.e(TAG, "rightDownPoint.x: " + rightDownPoint.x + "     rightDownPoint.y:" + rightDownPoint.y);
				Log.e(TAG, "imageView left: " + imageView.getLeft());
				Log.e(TAG, "imageView top: " + imageView.getTop());

				Drawable drawable = imageView.getDrawable();
				bit = ((BitmapDrawable) drawable).getBitmap();

				float scaleX = imageView.getScaleX();
				float scaleY = imageView.getScaleY();

				Log.e(TAG, "scaleX: " + scaleX + "     scaleY: " + scaleY);

				int realWidth = rightDownPoint.x - leftUpPoint.x;
				int realHeight = rightDownPoint.y - leftUpPoint.y;

				int width = 0, height = 0;
				if (realWidth < bit.getWidth()) {
					width = realWidth;
				} else {
					width = bit.getWidth();
				}

				if (realHeight < bit.getHeight()) {
					height = realHeight;
				} else {
					height = bit.getHeight();
				}
				Log.e(TAG, "width: " + width + "     height: " + height);
				newBitmap = Bitmap.createBitmap(bit, leftUpPoint.x - imageView.getLeft(),
						leftUpPoint.y - imageView.getTop(), width, height);

				bf = bitmap2Bytes(newBitmap);

				intent = new Intent(MainActivity.this, CutResultActivity.class);
				intent.putExtra("image", bf);

				startActivityForResult(intent, 1);

			}

		});

		size1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int width = imageView.getWidth();
				int height = imageView.getHeight();
				int w = 0, h = 0;
				if (width / height > 4.0f / 3.0f) {
					h = height;
					w = height * 4 / 3;
				} else {
					w = width;
					h = width * 3 / 4;
				}
				Log.e(TAG, "width: " + width + "    height:" + height);
				Log.e(TAG, "w: " + w + "    h:" + h);
				int left = width / 2 - w / 2;
				int top = height / 2 - h / 2;
				int right = width / 2 + w / 2;
				int bottom = height / 2 + h / 2;
				Log.e(TAG, "left: " + left + "     top:" + top + "    right:" + right + "     bottom:" + bottom);
				editImage.changeSize(left, top, right, bottom);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			Log.e(TAG, "result finish");
		}
	}

	/***
	 * 图片转化为byte数组
	 * @param bm
	 * @return
	 */
	private byte[] bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}
}
