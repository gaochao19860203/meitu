package com.example.testmei2;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
	private RelativeLayout relative1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		button = (Button) findViewById(R.id.button_img);
		imageView = (ImageView) findViewById(R.id.back_image);
		editImage = (EditImage) findViewById(R.id.edit_image);
		size1 = (Button) findViewById(R.id.size1);
		relative1 = (RelativeLayout) findViewById(R.id.relative1);

		ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
		viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {
				imageView.getViewTreeObserver().removeOnPreDrawListener(this);

				Log.e(TAG, "relative1 width: " + relative1.getWidth() + "    height: " + relative1.getHeight());

				bit = Utils.compressImage(getResources(), R.drawable.mao, relative1.getWidth(), relative1.getHeight());
				imageView.setImageBitmap(bit);

				Log.e(TAG, "bitmap width: " + bit.getWidth() + "    height: " + bit.getHeight());
				Log.e(TAG, "imageView width: " + imageView.getWidth() + "    height: " + imageView.getHeight());

				leftUpPoint = editImage.getLeftUpPoint();
				rightDownPoint = editImage.getRightDownPoint();
				int startX = imageView.getWidth() / 2 - bit.getWidth() / 2 + imageView.getLeft();
				int startY = imageView.getHeight() / 2 - bit.getHeight() / 2 + imageView.getTop();

				Log.e(TAG, "startX: " + startX + "  startY:" + startY);
				editImage.setMaxSize(startX, startY, startX + bit.getWidth(), startY + bit.getHeight());
				editImage.changeSize(startX, startY, startX + bit.getWidth(), startY + bit.getHeight());

				return true;
			}

		});

		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				leftUpPoint = editImage.getLeftUpPoint();
				rightDownPoint = editImage.getRightDownPoint();

				Drawable drawable = imageView.getDrawable();
				bit = ((BitmapDrawable) drawable).getBitmap();

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
				Log.e(TAG, "width: " + bit.getWidth() + "    height: " + bit.getHeight());
				Log.e(TAG, "leftUpPoint: " + editImage.getLeftUpPoint());
				int startX = leftUpPoint.x - (imageView.getWidth() / 2 - bit.getWidth() / 2) - imageView.getLeft();
				int startY = leftUpPoint.y - (imageView.getHeight() / 2 - bit.getHeight() / 2) - imageView.getTop();
				Log.e(TAG, "startX:" + startX + "   startY:" + startY + "     width: " + width + "     height: "
						+ height);

				newBitmap = Bitmap.createBitmap(bit, startX, startY, width, height);

				bf = bitmap2Bytes(newBitmap);

				intent = new Intent(MainActivity.this, CutResultActivity.class);
				intent.putExtra("image", bf);

				startActivityForResult(intent, 1);

			}

		});

		size1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Drawable drawable = imageView.getDrawable();
				bit = ((BitmapDrawable) drawable).getBitmap();

				int width = bit.getWidth();
				int height = bit.getHeight();
				int w = 0, h = 0;
				if (width / height > 4.0f / 3.0f) {
					h = height;
					w = height * 4 / 3;
				} else {
					w = width;
					h = width * 3 / 4;
				}
				int left = imageView.getWidth() / 2 - w / 2 + imageView.getLeft();
				int top = imageView.getHeight() / 2 - h / 2 + imageView.getTop();
				int right = imageView.getWidth() / 2 + w / 2 + imageView.getLeft();
				int bottom = imageView.getHeight() / 2 + h / 2 + imageView.getTop();

				//				Log.e(TAG, "left: " + left + "     top:" + top + "    right:" + right + "     bottom:" + bottom);
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
