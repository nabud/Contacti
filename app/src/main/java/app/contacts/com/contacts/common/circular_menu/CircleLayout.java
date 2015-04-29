package app.contacts.com.contacts.common.circular_menu;

/*
 * Copyright 2015 Csaba Szugyiczki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;

import app.contacts.com.contacts.R;


/**
 * 
 * @author Szugyi Creates a rotatable circle menu which can be parameterized by
 *         custom attributes. Handles touches and gestures to make the menu
 *         rotatable, and to make the menu items selectable and clickable.
 * 
 */
public class CircleLayout extends ViewGroup {
	// Event listeners
	private OnItemClickListener onItemClickListener = null;
	private OnItemSelectedListener onItemSelectedListener = null;
	private OnCenterClickListener onCenterClickListener = null;
	private OnRotationFinishedListener onRotationFinishedListener = null;

	// Background image
	private Bitmap imageOriginal, imageScaled;
	private Matrix matrix;

	// Sizes of the ViewGroup
	private int circleWidth, circleHeight;
	private int radius = 0;

	// Child sizes
	private int maxChildWidth = 0;
	private int maxChildHeight = 0;
	private int childWidth = 0;
	private int childHeight = 0;

	// Touch detection
	private GestureDetector gestureDetector;
	// Detecting inverse rotations
	private boolean[] quadrantTouched;

	// Settings of the ViewGroup
	private int speed = 25;
	private float angle = 90;
	private float firstChildPos = 90;
	private boolean isRotating = true;

	// Tapped and selected child
	private int tappedViewsPostition = -1;
	private View tappedView = null;
	private int selected = 0;

	// Rotation animator
	private ObjectAnimator animator;

	public CircleLayout(Context context) {
		this(context, null);
	}

	public CircleLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}

	/**
	 * Initializes the ViewGroup and modifies it's default behavior by the
	 * passed attributes
	 * 
	 * @param attrs
	 *            the attributes used to modify default settings
	 */
	protected void init(AttributeSet attrs) {
		gestureDetector = new GestureDetector(getContext(),
				new MyGestureListener());
		quadrantTouched = new boolean[] { false, false, false, false, false };

		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs,
					R.styleable.CircleLayout);

			// The angle where the first menu item will be drawn
			angle = a.getInt(R.styleable.CircleLayout_firstChildPosition,
					(int) angle);
			firstChildPos = angle;

			speed = a.getInt(R.styleable.CircleLayout_speed, speed);
			isRotating = a.getBoolean(R.styleable.CircleLayout_isRotating,
					isRotating);

			if (imageOriginal == null) {
				int picId = a.getResourceId(
						R.styleable.CircleLayout_circleBackground, -1);

				// If a background image was set as an attribute,
				// retrieve the image
				if (picId != -1) {
					imageOriginal = BitmapFactory.decodeResource(
							getResources(), picId);
				}
			}

			a.recycle();

			// Initialize the matrix only once
			if (matrix == null) {
				matrix = new Matrix();
			} else {
				// Not needed, you can also post the matrix immediately to
				// restore the old state
				matrix.reset();
			}

			// Needed for the ViewGroup to be drawn
			setWillNotDraw(false);
		}
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle % 360;
		setChildAngles();
	}

	/**
	 * Returns the currently selected menu
	 * 
	 * @return the view which is currently the closest to the first item
	 *         position
	 */
	public View getSelectedItem() {
		return (selected >= 0) ? getChildAt(selected) : null;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// The sizes of the ViewGroup
		circleHeight = getHeight();
		circleWidth = getWidth();

		if (imageOriginal != null) {
			// Scaling the size of the background image
			if (imageScaled == null) {
				float sx = (((radius + childWidth / 4) * 2) / (float) imageOriginal
						.getWidth());
				float sy = (((radius + childWidth / 4) * 2) / (float) imageOriginal
						.getHeight());

				matrix = new Matrix();
				matrix.postScale(sx, sy);

				imageScaled = Bitmap.createBitmap(imageOriginal, 0, 0,
						imageOriginal.getWidth(), imageOriginal.getHeight(),
						matrix, false);
			}

			if (imageScaled != null) {
				// Move the background to the center
				int cx = (circleWidth - imageScaled.getWidth()) / 2;
				int cy = (circleHeight - imageScaled.getHeight()) / 2;

				Canvas g = canvas;
				canvas.rotate(0, circleWidth / 2, circleHeight / 2);
				g.drawBitmap(imageScaled, cx, cy, null);
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		maxChildWidth = 0;
		maxChildHeight = 0;

		// Measure once to find the maximum child size.
		int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
				MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST);
		int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
				MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST);

		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() == GONE) {
				continue;
			}

			child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

			maxChildWidth = Math.max(maxChildWidth, child.getMeasuredWidth());
			maxChildHeight = Math
					.max(maxChildHeight, child.getMeasuredHeight());
		}

		// Measure again for each child to be exactly the same size.
		childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(maxChildWidth,
				MeasureSpec.EXACTLY);
		childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(maxChildHeight,
				MeasureSpec.EXACTLY);

		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() == GONE) {
				continue;
			}

			child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
		}

		setMeasuredDimension(resolveSize(maxChildWidth, widthMeasureSpec),
				resolveSize(maxChildHeight, heightMeasureSpec));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int layoutWidth = r - l;
		int layoutHeight = b - t;

		// Laying out the child views
		final int childCount = getChildCount();
		int left, top;
		radius = (layoutWidth <= layoutHeight) ? layoutWidth / 3
				: layoutHeight / 3;

		childWidth = (int) (radius / 1.5);
		childHeight = (int) (radius / 1.5);

		float angleDelay = 360.0f / getChildCount();

		for (int i = 0; i < childCount; i++) {
			final CircleImageView child = (CircleImageView) getChildAt(i);
			if (child.getVisibility() == GONE) {
				continue;
			}

			if (angle > 360) {
				angle -= 360;
			} else {
				if (angle < 0) {
					angle += 360;
				}
			}

			child.setAngle(angle);
			child.setPosition(i);

			left = Math
					.round((float) (((layoutWidth / 2) - childWidth / 2) + radius
							* Math.cos(Math.toRadians(angle))));
			top = Math
					.round((float) (((layoutHeight / 2) - childHeight / 2) + radius
							* Math.sin(Math.toRadians(angle))));

			child.layout(left, top, left + childWidth, top + childHeight);
			angle += angleDelay;
		}
	}

	/**
	 * Rotates the given view to the firstChildPosition
	 * 
	 * @param view
	 *            the view to be rotated
	 */
	private void rotateViewToCenter(CircleImageView view) {
		Log.v(VIEW_LOG_TAG, "rotateViewToCenter");
		if (isRotating) {
			float destAngle = (float) (firstChildPos - view.getAngle());

			if (destAngle < 0) {
				destAngle += 360;
			}

			if (destAngle > 180) {
				destAngle = -1 * (360 - destAngle);
			}

			animateTo(angle + destAngle, 7500 / speed);
		}
	}

	private void rotateButtons(float degrees) {
		angle += degrees;
		setChildAngles();
	}

	private void animateTo(float endDegree, long duration) {
		if (animator != null && animator.isRunning()
				|| Math.abs(angle - endDegree) < 1) {
			return;
		}

		animator = ObjectAnimator.ofFloat(CircleLayout.this, "angle", angle,
				endDegree);
		animator.setDuration(duration);
		animator.setInterpolator(new DecelerateInterpolator());
		animator.addListener(new AnimatorListener() {
			private boolean wasCanceled = false;

			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (wasCanceled) {
					return;
				}

				if (onRotationFinishedListener != null) {
					CircleImageView view = (CircleImageView) getSelectedItem();
					onRotationFinishedListener.onRotationFinished(view,
							view.getName());
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				wasCanceled = true;
			}
		});
		animator.start();
	}

	private void stopAnimation() {
		if (animator != null && animator.isRunning()) {
			animator.cancel();
			animator = null;
		}
	}

	private void setChildAngles() {
		int left, top, childCount = getChildCount();
		float angleDelay = 360.0f / childCount;
		float localAngle = angle;

		for (int i = 0; i < childCount; i++) {
			if (localAngle > 360) {
				localAngle -= 360;
			} else {
				if (localAngle < 0) {
					localAngle += 360;
				}
			}

			final CircleImageView child = (CircleImageView) getChildAt(i);
			if (child.getVisibility() == GONE) {
				continue;
			}
			left = Math
					.round((float) (((circleWidth / 2) - childWidth / 2) + radius
							* Math.cos(Math.toRadians(localAngle))));
			top = Math
					.round((float) (((circleHeight / 2) - childHeight / 2) + radius
							* Math.sin(Math.toRadians(localAngle))));

			child.setAngle(localAngle);

			if (Math.abs(localAngle - firstChildPos) < (angleDelay / 2)
					&& selected != child.getPosition()) {
				selected = child.getPosition();

				if (onItemSelectedListener != null && isRotating) {
					onItemSelectedListener.onItemSelected(child,
							child.getName());
				}
			}

			child.layout(left, top, left + childWidth, top + childHeight);
			localAngle += angleDelay;
		}
	}

	/**
	 * @return The angle of the unit circle with the image views center
	 */
	private double getPositionAngle(double xTouch, double yTouch) {
		double x = xTouch - (circleWidth / 2d);
		double y = circleHeight - yTouch - (circleHeight / 2d);

		switch (getPositionQuadrant(x, y)) {
			case 1:
				return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
			case 2:
			case 3:
				return 180 - (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
			case 4:
				return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
			default:
				// ignore, does not happen
				return 0;
		}
	}

	/**
	 * @return The quadrant of the position
	 */
	private static int getPositionQuadrant(double x, double y) {
		if (x >= 0) {
			return y >= 0 ? 1 : 4;
		} else {
			return y >= 0 ? 2 : 3;
		}
	}

	// Touch helpers
	private double touchStartAngle;
	private boolean didMove = false;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isEnabled()) {
			gestureDetector.onTouchEvent(event);
			if (isRotating) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						// reset the touched quadrants
						for (int i = 0; i < quadrantTouched.length; i++) {
							quadrantTouched[i] = false;
						}

						stopAnimation();
						touchStartAngle = getPositionAngle(event.getX(),
								event.getY());
						didMove = false;
						break;
					case MotionEvent.ACTION_MOVE:
						double currentAngle = getPositionAngle(event.getX(),
								event.getY());
						rotateButtons((float) (touchStartAngle - currentAngle));
						touchStartAngle = currentAngle;
						didMove = true;
						break;
					case MotionEvent.ACTION_UP:
						if (didMove) {
							rotateViewToCenter((CircleImageView) getChildAt(selected));
						}
						break;
				}
			}

			// set the touched quadrant to true
			quadrantTouched[getPositionQuadrant(event.getX()
					- (circleWidth / 2), circleHeight - event.getY()
					- (circleHeight / 2))] = true;
			return true;
		}
		return false;
	}

	private class MyGestureListener extends SimpleOnGestureListener {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (!isRotating) {
				return false;
			}
			// get the quadrant of the start and the end of the fling
			int q1 = getPositionQuadrant(e1.getX() - (circleWidth / 2),
					circleHeight - e1.getY() - (circleHeight / 2));
			int q2 = getPositionQuadrant(e2.getX() - (circleWidth / 2),
					circleHeight - e2.getY() - (circleHeight / 2));

			if ((q1 == 2 && q2 == 2 && Math.abs(velocityX) < Math
					.abs(velocityY))
					|| (q1 == 3 && q2 == 3)
					|| (q1 == 1 && q2 == 3)
					|| (q1 == 4 && q2 == 4 && Math.abs(velocityX) > Math
							.abs(velocityY))
					|| ((q1 == 2 && q2 == 3) || (q1 == 3 && q2 == 2))
					|| ((q1 == 3 && q2 == 4) || (q1 == 4 && q2 == 3))
					|| (q1 == 2 && q2 == 4 && quadrantTouched[3])
					|| (q1 == 4 && q2 == 2 && quadrantTouched[3])) {
				// the inverted rotations
				animateTo(
						getCenteredAngle(angle - (velocityX + velocityY) / 25),
						25000 / speed);
			} else {
				// the normal rotation
				animateTo(
						getCenteredAngle(angle + (velocityX + velocityY) / 25),
						25000 / speed);
			}

			return true;
		}

		private float getCenteredAngle(float angle) {
			float angleDelay = 360 / getChildCount();
			float localAngle = angle % 360;

			if (localAngle < 0) {
				localAngle = 360 + localAngle;
			}

			for (float i = firstChildPos; i < firstChildPos + 360; i += angleDelay) {
				float locI = i % 360;
				float diff = localAngle - locI;
				if (Math.abs(diff) < angleDelay / 2) {
					angle -= diff;
					break;
				}
			}

			return angle;
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			tappedViewsPostition = pointToPosition(e.getX(), e.getY());
			if (tappedViewsPostition >= 0) {
				tappedView = getChildAt(tappedViewsPostition);
				tappedView.setPressed(true);
			} else {
				float centerX = circleWidth / 2;
				float centerY = circleHeight / 2;

				if (e.getX() < centerX + (childWidth / 2)
						&& e.getX() > centerX - childWidth / 2
						&& e.getY() < centerY + (childHeight / 2)
						&& e.getY() > centerY - (childHeight / 2)) {
					if (onCenterClickListener != null) {
						onCenterClickListener.onCenterClick();
						return true;
					}
				}
			}

			if (tappedView != null) {
				CircleImageView view = (CircleImageView) (tappedView);
				if (selected == tappedViewsPostition) {
					if (onItemClickListener != null) {
						onItemClickListener.onItemClick(tappedView,
								view.getName());
					}
				} else {
					rotateViewToCenter(view);
					if (!isRotating) {
						if (onItemSelectedListener != null) {
							onItemSelectedListener.onItemSelected(tappedView,
									view.getName());
						}

						if (onItemClickListener != null) {
							onItemClickListener.onItemClick(tappedView,
									view.getName());
						}
					}
				}
				return true;
			}
			return super.onSingleTapUp(e);
		}

		private int pointToPosition(float x, float y) {
			for (int i = 0; i < getChildCount(); i++) {
				View item = (View) getChildAt(i);
				if (item.getLeft() < x && item.getRight() > x
						& item.getTop() < y && item.getBottom() > y) {
					return i;
				}
			}
			return -1;
		}
	}

	public interface OnItemClickListener {
		void onItemClick(View view, String name);
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public interface OnItemSelectedListener {
		void onItemSelected(View view, String name);
	}

	public void setOnItemSelectedListener(
			OnItemSelectedListener onItemSelectedListener) {
		this.onItemSelectedListener = onItemSelectedListener;
	}

	public interface OnCenterClickListener {
		void onCenterClick();
	}

	public void setOnCenterClickListener(
			OnCenterClickListener onCenterClickListener) {
		this.onCenterClickListener = onCenterClickListener;
	}

	public interface OnRotationFinishedListener {
		void onRotationFinished(View view, String name);
	}

	public void setOnRotationFinishedListener(
			OnRotationFinishedListener onRotationFinishedListener) {
		this.onRotationFinishedListener = onRotationFinishedListener;
	}
}