package com.example.testmei2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class EditImage extends View {
	private static final String TAG = "mei";

	private Point lastPoint;
	private Point leftUpPoint, rightDownPoint;
	private Point offsetLeftUpPoint, offsetRightDownPoint;
	private static final int EVENT_NONE = 0, EVENT_MOVE = 1, EVENT_SCALE_LEFTUP = 2, EVENT_SCALE_LEFTDOWN = 3,
			EVENT_SCALE_RIGHTUP = 4, EVENT_SCALE_RIGHTDOWN = 5, EVENT_EDGE_LEFT = 6, EVENT_EDGE_TOP = 7,
			EVENT_EDGE_RIGHT = 8, EVENT_EDGE_BOTTOM = 9;
	private int eventType;
	private static final int FRAME_MIN_WIDTH = 125;
	private static final int FRAME_MIN_HEIGHT = 125;
	private final int DOT_RADIO = 20;
	private Bitmap dot, dotSelected;
	private final int[] EDGE_MIDDLERECT_WH = { 10, 40 };

	public EditImage(Context context, AttributeSet attrs) {
		super(context, attrs);

		FrameLayout.LayoutParams lytp = new FrameLayout.LayoutParams(context, attrs);
		lytp.gravity = Gravity.CENTER;

		dot = BitmapFactory.decodeResource(this.getResources(), R.drawable.img_cut_dot);
		dotSelected = BitmapFactory.decodeResource(this.getResources(), R.drawable.img_cut_dotselect);

		leftUpPoint = new Point();
		rightDownPoint = new Point();
		offsetLeftUpPoint = new Point();
		offsetRightDownPoint = new Point();
		eventType = EVENT_NONE;
	}

	public Point getLeftUpPoint() {
		return leftUpPoint;
	}

	public Point getRightDownPoint() {
		return rightDownPoint;
	}

	public void changeSize(int left, int top, int right, int bottom) {
		leftUpPoint.x = left;
		leftUpPoint.y = top;
		rightDownPoint.x = right;
		rightDownPoint.y = bottom;
		this.invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);

		// 画中间框的外围
		paint.setARGB(180, 0, 0, 0);
		Rect topRect = new Rect(0, 0, this.getWidth(), leftUpPoint.y + offsetLeftUpPoint.y);
		Rect bottomRect = new Rect(0, rightDownPoint.y + offsetRightDownPoint.y, this.getWidth(), this.getHeight());
		Rect leftRect = new Rect(0, leftUpPoint.y + offsetLeftUpPoint.y, leftUpPoint.x + offsetLeftUpPoint.x,
				rightDownPoint.y + offsetRightDownPoint.y);
		Rect rightRect = new Rect(rightDownPoint.x + offsetRightDownPoint.x, leftUpPoint.y + offsetLeftUpPoint.y,
				this.getWidth(), rightDownPoint.y + offsetRightDownPoint.y);
		canvas.drawRect(topRect, paint);
		canvas.drawRect(bottomRect, paint);
		canvas.drawRect(leftRect, paint);
		canvas.drawRect(rightRect, paint);

		// 画中间框
		paint.setARGB(255, 255, 255, 255);
		paint.setStrokeWidth(3);
		paint.setStyle(Style.STROKE);
		Rect frameRect = new Rect(leftUpPoint.x + offsetLeftUpPoint.x, leftUpPoint.y + offsetLeftUpPoint.y,
				rightDownPoint.x + offsetRightDownPoint.x, rightDownPoint.y + offsetRightDownPoint.y);
		canvas.drawRect(frameRect, paint);

		// 画中间框的线条
		paint.setARGB(90, 255, 255, 255);
		paint.setStrokeWidth(2);
		canvas.drawLine(leftUpPoint.x + offsetLeftUpPoint.x + this.getDynamicFrameWidth() / 3, leftUpPoint.y
				+ offsetLeftUpPoint.y, leftUpPoint.x + offsetLeftUpPoint.x + this.getDynamicFrameWidth() / 3,
				rightDownPoint.y + offsetRightDownPoint.y, paint);
		canvas.drawLine(rightDownPoint.x + offsetRightDownPoint.x - this.getDynamicFrameWidth() / 3, leftUpPoint.y
				+ offsetLeftUpPoint.y, rightDownPoint.x + offsetRightDownPoint.x - this.getDynamicFrameWidth() / 3,
				rightDownPoint.y + offsetRightDownPoint.y, paint);
		canvas.drawLine(leftUpPoint.x + offsetLeftUpPoint.x,
				leftUpPoint.y + offsetLeftUpPoint.y + this.getDynamicFrameHeight() / 3, rightDownPoint.x
						+ offsetRightDownPoint.x, leftUpPoint.y + offsetLeftUpPoint.y + this.getDynamicFrameHeight()
						/ 3, paint);
		canvas.drawLine(leftUpPoint.x + offsetLeftUpPoint.x,
				rightDownPoint.y + offsetRightDownPoint.y - this.getDynamicFrameHeight() / 3, rightDownPoint.x
						+ offsetRightDownPoint.x,
				rightDownPoint.y + offsetRightDownPoint.y - this.getDynamicFrameHeight() / 3, paint);

		// 画中间框的四条边线上的四个中心点为中心的 小矩形
		paint.setARGB(255, 255, 255, 255);
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.FILL);
		canvas.drawRect(leftUpPoint.x + offsetLeftUpPoint.x - EDGE_MIDDLERECT_WH[0], leftUpPoint.y
				+ offsetLeftUpPoint.y + this.getDynamicFrameHeight() / 2 - EDGE_MIDDLERECT_WH[1], leftUpPoint.x
				+ offsetLeftUpPoint.x + EDGE_MIDDLERECT_WH[0],
				leftUpPoint.y + offsetLeftUpPoint.y + this.getDynamicFrameHeight() / 2 + EDGE_MIDDLERECT_WH[1], paint);

		// 画中间框里的字
		paint.setARGB(255, 255, 255, 255);
		paint.setStrokeWidth(0);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(10);
		canvas.drawText(this.getDynamicFrameWidth() + "*" + this.getDynamicFrameHeight(), leftUpPoint.x
				+ offsetLeftUpPoint.x + this.getDynamicFrameWidth() / 2,
				leftUpPoint.y + offsetLeftUpPoint.y + (this.getDynamicFrameHeight() + 10) / 2, paint);

		// 画中间框里的四个点
		Rect leftUpRect = new Rect(leftUpPoint.x + offsetLeftUpPoint.x - DOT_RADIO, leftUpPoint.y + offsetLeftUpPoint.y
				- DOT_RADIO, leftUpPoint.x + offsetLeftUpPoint.x + DOT_RADIO, leftUpPoint.y + offsetLeftUpPoint.y
				+ DOT_RADIO);
		Rect leftDownRect = new Rect(leftUpPoint.x + offsetLeftUpPoint.x - DOT_RADIO, rightDownPoint.y
				+ offsetRightDownPoint.y - DOT_RADIO, leftUpPoint.x + offsetLeftUpPoint.x + DOT_RADIO, rightDownPoint.y
				+ offsetRightDownPoint.y + DOT_RADIO);
		Rect rightUpRect = new Rect(rightDownPoint.x + offsetRightDownPoint.x - DOT_RADIO, leftUpPoint.y
				+ offsetLeftUpPoint.y - DOT_RADIO, rightDownPoint.x + offsetRightDownPoint.x + DOT_RADIO, leftUpPoint.y
				+ offsetLeftUpPoint.y + DOT_RADIO);
		Rect rightDownRect = new Rect(rightDownPoint.x + offsetRightDownPoint.x - DOT_RADIO, rightDownPoint.y
				+ offsetRightDownPoint.y - DOT_RADIO, rightDownPoint.x + offsetRightDownPoint.x + DOT_RADIO,
				rightDownPoint.y + offsetRightDownPoint.y + DOT_RADIO);
		canvas.drawBitmap(dot, null, leftUpRect, null);
		canvas.drawBitmap(dot, null, leftDownRect, null);
		canvas.drawBitmap(dot, null, rightUpRect, null);
		canvas.drawBitmap(dot, null, rightDownRect, null);

		switch (eventType) {
		case EVENT_NONE:
			canvas.drawBitmap(dot, null, leftUpRect, null);
			canvas.drawBitmap(dot, null, leftDownRect, null);
			canvas.drawBitmap(dot, null, rightUpRect, null);
			canvas.drawBitmap(dot, null, rightDownRect, null);
			break;
		case EVENT_SCALE_LEFTUP:
			canvas.drawBitmap(dotSelected, null, leftUpRect, null);
			canvas.drawBitmap(dot, null, leftDownRect, null);
			canvas.drawBitmap(dot, null, rightUpRect, null);
			canvas.drawBitmap(dot, null, rightDownRect, null);
			break;
		case EVENT_SCALE_LEFTDOWN:
			canvas.drawBitmap(dot, null, leftUpRect, null);
			canvas.drawBitmap(dotSelected, null, leftDownRect, null);
			canvas.drawBitmap(dot, null, rightUpRect, null);
			canvas.drawBitmap(dot, null, rightDownRect, null);
			break;
		case EVENT_SCALE_RIGHTUP:
			canvas.drawBitmap(dot, null, leftUpRect, null);
			canvas.drawBitmap(dot, null, leftDownRect, null);
			canvas.drawBitmap(dotSelected, null, rightUpRect, null);
			canvas.drawBitmap(dot, null, rightDownRect, null);
			break;
		case EVENT_SCALE_RIGHTDOWN:
			canvas.drawBitmap(dot, null, leftUpRect, null);
			canvas.drawBitmap(dot, null, leftDownRect, null);
			canvas.drawBitmap(dot, null, rightUpRect, null);
			canvas.drawBitmap(dotSelected, null, rightDownRect, null);
			break;
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		//		Log.e(TAG, "w: " + w);
		//		Log.e(TAG, "h: " + h);
		leftUpPoint.x = w / 4;
		leftUpPoint.y = h / 4;
		rightDownPoint.x = w * 3 / 4;
		rightDownPoint.y = h * 3 / 4;

		offsetLeftUpPoint.x = 0;
		offsetLeftUpPoint.y = 0;
		offsetRightDownPoint.x = 0;
		offsetRightDownPoint.y = 0;
		super.onSizeChanged(w, h, oldw, oldh);
	}

	/*
	 * 接收触屏事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			handleActionDown(x, y);
			break;
		case MotionEvent.ACTION_MOVE:
			handleActionMove(x, y);
			break;
		case MotionEvent.ACTION_UP:
			handleActionUp();
			break;
		}
		this.invalidate();
		return true;
	}

	/**
	 * 得到eventType事件类型的值和lastPoint坐标点
	 * 
	 * @param x
	 *            x坐标
	 * @param y
	 *            y坐标
	 */
	private void handleActionDown(int x, int y) {
		if (Math.abs(x - leftUpPoint.x) < EDGE_MIDDLERECT_WH[0]
				&& Math.abs(y - leftUpPoint.y - this.getDynamicFrameHeight() / 2) < EDGE_MIDDLERECT_WH[1]) {
			lastPoint = new Point(x, y);
			eventType = EVENT_EDGE_LEFT;
			// leftup
		} else if (Math.abs(x - leftUpPoint.x) < DOT_RADIO && Math.abs(y - leftUpPoint.y) < DOT_RADIO) {
			lastPoint = new Point(x, y);
			eventType = EVENT_SCALE_LEFTUP;
			// leftdown
		} else if (Math.abs(x - leftUpPoint.x) < DOT_RADIO && Math.abs(y - rightDownPoint.y) < DOT_RADIO) {
			lastPoint = new Point(x, y);
			eventType = EVENT_SCALE_LEFTDOWN;
			// rightup
		} else if (Math.abs(x - rightDownPoint.x) < DOT_RADIO && Math.abs(y - leftUpPoint.y) < DOT_RADIO) {
			lastPoint = new Point(x, y);
			eventType = EVENT_SCALE_RIGHTUP;
			// rightdown
		} else if (Math.abs(x - rightDownPoint.x) < DOT_RADIO && Math.abs(y - rightDownPoint.y) < DOT_RADIO) {
			lastPoint = new Point(x, y);
			eventType = EVENT_SCALE_RIGHTDOWN;
			// move
		} else if (leftUpPoint.x < x && x < rightDownPoint.x && leftUpPoint.y < y && y < rightDownPoint.y) {
			lastPoint = new Point(x, y);
			eventType = EVENT_MOVE;
			// nothing
		} else {
			eventType = EVENT_NONE;
		}
	}

	/**
	 * 根据eventType进行处理
	 * 
	 * @param x
	 *            x坐标
	 * @param y
	 *            y坐标
	 */
	private void handleActionMove(int x, int y) {
		switch (eventType) {
		case EVENT_MOVE:
			offsetLeftUpPoint.x = x - lastPoint.x;
			offsetLeftUpPoint.y = y - lastPoint.y;
			moveRestriction();
			offsetRightDownPoint.x = offsetLeftUpPoint.x;
			offsetRightDownPoint.y = offsetLeftUpPoint.y;
			break;
		case EVENT_SCALE_LEFTUP:
			offsetLeftUpPoint.x = x - lastPoint.x;
			offsetLeftUpPoint.y = y - lastPoint.y;
			leftUpScaleRestriction();
			offsetRightDownPoint.x = 0;
			offsetRightDownPoint.y = 0;
			break;
		case EVENT_SCALE_LEFTDOWN:
			offsetLeftUpPoint.x = x - lastPoint.x;
			offsetRightDownPoint.y = y - lastPoint.y;
			leftDownScaleRestriction();
			offsetLeftUpPoint.y = 0;
			offsetRightDownPoint.x = 0;
			break;
		case EVENT_SCALE_RIGHTUP:
			offsetRightDownPoint.x = x - lastPoint.x;
			offsetLeftUpPoint.y = y - lastPoint.y;
			rightUpScaleRestriction();
			offsetRightDownPoint.y = 0;
			offsetLeftUpPoint.x = 0;
			break;
		case EVENT_SCALE_RIGHTDOWN:
			offsetRightDownPoint.x = x - lastPoint.x;
			offsetRightDownPoint.y = y - lastPoint.y;
			rightDownScaleRestriction();
			offsetLeftUpPoint.x = 0;
			offsetLeftUpPoint.y = 0;
			break;
		case EVENT_EDGE_LEFT:
			offsetLeftUpPoint.x = x - lastPoint.x;
			leftEdgeScaleRestriction();
			offsetLeftUpPoint.y = 0;
			offsetRightDownPoint.x = 0;
			offsetRightDownPoint.y = 0;
			break;
		case EVENT_NONE:
			break;
		}

	}

	/**
	 * 进行复位
	 */
	private void handleActionUp() {
		leftUpPoint.x += offsetLeftUpPoint.x;
		leftUpPoint.y += offsetLeftUpPoint.y;
		rightDownPoint.x += offsetRightDownPoint.x;
		rightDownPoint.y += offsetRightDownPoint.y;

		offsetLeftUpPoint.x = 0;
		offsetLeftUpPoint.y = 0;
		offsetRightDownPoint.x = 0;
		offsetRightDownPoint.y = 0;
		eventType = EVENT_NONE;

		//		Log.e(TAG, "leftUpPoint x: " + leftUpPoint.x);
		//		Log.e(TAG, "leftUpPoint y: " + leftUpPoint.y);
		//		Log.e(TAG, "rightDownPoint x: " + rightDownPoint.x);
		//		Log.e(TAG, "rightDownPoint y: " + rightDownPoint.y);
	}

	/**
	 * 移动限制
	 */
	private void moveRestriction() {
		if (leftUpPoint.x + offsetLeftUpPoint.x < 0) {
			offsetLeftUpPoint.x = -leftUpPoint.x;
		} else if (leftUpPoint.x + offsetLeftUpPoint.x > this.getWidth() - (rightDownPoint.x - leftUpPoint.x)) {
			offsetLeftUpPoint.x = this.getWidth() - (rightDownPoint.x - leftUpPoint.x) - leftUpPoint.x;
		}
		if (leftUpPoint.y + offsetLeftUpPoint.y < 0) {
			offsetLeftUpPoint.y = -leftUpPoint.y;
		} else if (leftUpPoint.y + offsetLeftUpPoint.y > this.getHeight() - (rightDownPoint.y - leftUpPoint.y)) {
			offsetLeftUpPoint.y = this.getHeight() - (rightDownPoint.y - leftUpPoint.y) - leftUpPoint.y;
		}
	}

	/**
	 * 左上缩放限制
	 */
	private void leftUpScaleRestriction() {
		if (leftUpPoint.x + offsetLeftUpPoint.x < 0) {
			offsetLeftUpPoint.x = -leftUpPoint.x;
		}
		if (rightDownPoint.x - (leftUpPoint.x + offsetLeftUpPoint.x) < FRAME_MIN_WIDTH) {
			offsetLeftUpPoint.x = rightDownPoint.x - leftUpPoint.x - FRAME_MIN_WIDTH;
		}

		if (leftUpPoint.y + offsetLeftUpPoint.y < 0) {
			offsetLeftUpPoint.y = -leftUpPoint.y;
		}
		if (rightDownPoint.y - (leftUpPoint.y + offsetLeftUpPoint.y) < FRAME_MIN_HEIGHT) {
			offsetLeftUpPoint.y = rightDownPoint.y - leftUpPoint.y - FRAME_MIN_HEIGHT;
		}
	}

	/**
	 * 左下缩放限制
	 */
	private void leftDownScaleRestriction() {
		if (leftUpPoint.x + offsetLeftUpPoint.x < 0) {
			offsetLeftUpPoint.x = -leftUpPoint.x;
		}
		if (rightDownPoint.x - (leftUpPoint.x + offsetLeftUpPoint.x) < FRAME_MIN_WIDTH) {
			offsetLeftUpPoint.x = rightDownPoint.x - leftUpPoint.x - FRAME_MIN_WIDTH;
		}
		if (rightDownPoint.y + offsetRightDownPoint.y > this.getHeight()) {
			offsetRightDownPoint.y = this.getHeight() - rightDownPoint.y;
		}
		if ((rightDownPoint.y + offsetRightDownPoint.y) - leftUpPoint.y < FRAME_MIN_HEIGHT) {
			offsetRightDownPoint.y = FRAME_MIN_HEIGHT + leftUpPoint.y - rightDownPoint.y;
		}
	}

	/**
	 * 右上缩放限制
	 */
	private void rightUpScaleRestriction() {
		if (rightDownPoint.x + offsetRightDownPoint.x > this.getWidth()) {
			offsetRightDownPoint.x = this.getWidth() - rightDownPoint.x;
		}
		if ((rightDownPoint.x + offsetRightDownPoint.x) - leftUpPoint.x < FRAME_MIN_WIDTH) {
			offsetRightDownPoint.x = FRAME_MIN_WIDTH + leftUpPoint.x - rightDownPoint.x;
		}
		if (leftUpPoint.y + offsetLeftUpPoint.y < 0) {
			offsetLeftUpPoint.y = -leftUpPoint.y;
		}
		if (rightDownPoint.y - (leftUpPoint.y + offsetLeftUpPoint.y) < FRAME_MIN_HEIGHT) {
			offsetLeftUpPoint.y = rightDownPoint.y - leftUpPoint.y - FRAME_MIN_HEIGHT;
		}
	}

	/**
	 * 右下缩放限制
	 */
	private void rightDownScaleRestriction() {

		if (rightDownPoint.x + offsetRightDownPoint.x > this.getWidth()) {
			offsetRightDownPoint.x = this.getWidth() - rightDownPoint.x;

		}

		if ((rightDownPoint.x + offsetRightDownPoint.x) - leftUpPoint.x < FRAME_MIN_WIDTH) {
			offsetRightDownPoint.x = FRAME_MIN_WIDTH + leftUpPoint.x - rightDownPoint.x;
		}

		if (rightDownPoint.y + offsetRightDownPoint.y > this.getHeight()) {
			offsetRightDownPoint.y = this.getHeight() - rightDownPoint.y;
		}

		if ((rightDownPoint.y + offsetRightDownPoint.y) - leftUpPoint.y < FRAME_MIN_HEIGHT) {
			offsetRightDownPoint.y = FRAME_MIN_HEIGHT + leftUpPoint.y - rightDownPoint.y;
		}
	}

	/****
	 * 左边缘缩放限制
	 */
	private void leftEdgeScaleRestriction() {
		if (leftUpPoint.x + offsetLeftUpPoint.x < 0) {
			offsetLeftUpPoint.x = -leftUpPoint.x;
		}

		if (rightDownPoint.x - (leftUpPoint.x + offsetLeftUpPoint.x) < FRAME_MIN_WIDTH) {
			offsetLeftUpPoint.x = rightDownPoint.x - leftUpPoint.x - FRAME_MIN_WIDTH;
		}
	}

	/**
	 * @return 得到中间的框的宽
	 */
	private int getDynamicFrameWidth() {
		return (rightDownPoint.x + offsetRightDownPoint.x) - (leftUpPoint.x + offsetLeftUpPoint.x);
	}

	/**
	 * @return 得到中间的框的高
	 */
	private int getDynamicFrameHeight() {
		return (rightDownPoint.y + offsetRightDownPoint.y) - (leftUpPoint.y + offsetLeftUpPoint.y);
	}

}
