package com.example.testmei2;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Utils {
	private static final String TAG = "mei";

	//	public static Bitmap compressWithWidthHeight(Resources resources, int resId, int reqWidth, int reqHeight){
	//		Bitmap bit = compressImage(resources, resId, reqWidth, reqHeight);
	//		if(bit.getWidth() > reqWidth || bit.getHeight() > reqHeight){
	//			
	//		}
	//	}

	/****
	 * 压缩图片
	 * @param bit
	 * @param resources
	 * @param resId
	 * @return
	 */
	public static Bitmap compressImage(Resources resources, int resId, int reqWidth, int reqHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(resources, resId, options);
		Log.e(TAG, "options width: " + options.outWidth + "    options height: " + options.outHeight);
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		Log.e(TAG, "inSampleSize: " + options.inSampleSize);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(resources, resId, options);
	}

	/****
	 * 计算图片的缩放值ֵ
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		int height = options.outHeight;
		int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			int heightRatio = (int) Math.ceil((float) height / (float) reqHeight);
			int widthRatio = (int) Math.ceil((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}
		return calculateNearPow(inSampleSize);
	}

	/****
	 * 计算比num大的最小的2的幂数
	 * @param num
	 * @return
	 */
	public static int calculateNearPow(int num) {
		int k = 0;
		while ((int) Math.pow(2, k) < num) {
			k++;
		}
		return (int) Math.pow(2, k);
	}

	public static void main(String[] args) {
		int d = calculateNearPow(3);
		System.out.println(d);
	}
}
