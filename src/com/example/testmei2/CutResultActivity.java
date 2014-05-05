package com.example.testmei2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class CutResultActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cut_result);

		ImageView imageView = (ImageView) findViewById(R.id.new_image);

		Intent mIntent = getIntent();
		byte[] bf = mIntent.getByteArrayExtra("image");
		Bitmap bitmap = BitmapFactory.decodeByteArray(bf, 0, bf.length);

		imageView.setImageBitmap(bitmap);
	}
}
