package com.lvchehui.www.xiangbc.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.lvchehui.www.xiangbc.R;


public class ParallaxScollView extends ScrollView implements OnScrollListener {

	public final static double NO_ZOOM = 1;
	public final static double ZOOM_X2 = 2;

	private ImageView mImageView;
	private int mImageViewHeight = -1;
	private int mDefaultImageViewHeight = 0;
	private int originImageViewHeight;
	private OnMyScrollChangedListener onScrollChangedListener = null;

	private interface OnOverScrollByListener {
		public boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY,
									boolean isTouchEvent);
	}

	public void setOnScrollChangedListener(OnMyScrollChangedListener onScrollChangedListener) {
		this.onScrollChangedListener = onScrollChangedListener;
	}

	private interface OnTouchEventListener {
		public void onTouchEvent(MotionEvent ev);
	}

	public ParallaxScollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ParallaxScollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ParallaxScollView(Context context) {
		super(context);
		init(context);
	}

	public void init(Context context) {
		mDefaultImageViewHeight = context.getResources().getDimensionPixelSize(R.dimen.size_default_height);
		originImageViewHeight = context.getResources().getDimensionPixelSize(R.dimen.size_default_height);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// super.onScrollChanged(x, y, oldx, oldy);
		// if (onScrollChangedListener != null) {
		// onScrollChangedListener.onScrollChanged(this, x, y, oldx, oldy);
		// }
	}

	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY,
			boolean isTouchEvent) {
		boolean isCollapseAnimation = false;

		isCollapseAnimation = scrollByListener.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent)
				|| isCollapseAnimation;

		/*
		 * return isCollapseAnimation ? true : super.overScrollBy(deltaX,
		 * deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX,
		 * 0, isTouchEvent);
		 */
		// return false;
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);

		// super.onScrollChanged(x, y, oldx, oldy);
		if (onScrollChangedListener != null) {
			onScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
		}
		View firstView = (View) mImageView.getParent();
		// firstView.getTop < getPaddingTop means mImageView will be covered by
		// top padding,
		// so we can layout it to make it shorter
		if (firstView.getTop() < getPaddingTop() && mImageView.getHeight() > mImageViewHeight) {
			mImageView.getLayoutParams().height = Math.max(mImageView.getHeight() - (getPaddingTop() - firstView.getTop()), mImageViewHeight);
			// to set the firstView.mTop to 0,
			// maybe use View.setTop() is more easy, but it just support from
			// Android 3.0 (API 11)
			firstView.layout(firstView.getLeft(), 0, firstView.getRight(), firstView.getHeight());
			mImageView.requestLayout();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		touchListener.onTouchEvent(ev);
		return super.onTouchEvent(ev);
	}

	public void setParallaxImageView(ImageView iv) {
		mImageView = iv;
		mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	}

	public void setViewsBounds(double zoomRatio) {
		if (mImageViewHeight == -1) {
			mImageViewHeight = mImageView.getHeight();
			if (mImageViewHeight <= 0) {
				mImageViewHeight = mDefaultImageViewHeight;
			}
		//	double ratio = ((double) mImageView.getDrawable().getIntrinsicWidth()) / ((double) mImageView.getWidth());

		}
	}

	private OnOverScrollByListener scrollByListener = new OnOverScrollByListener() {
		@Override
		public boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY,
				boolean isTouchEvent) {
			if (isTouchEvent) {
				if (scrollY <= 0) {
					mImageView.getLayoutParams().height = mImageView.getHeight() - deltaY / 2;
					mImageView.requestLayout();
				}
			}
			return false;
		}
	};

	private OnTouchEventListener touchListener = new OnTouchEventListener() {
		@Override
		public void onTouchEvent(MotionEvent ev) {
			if (ev.getAction() == MotionEvent.ACTION_UP) {
				if (mImageViewHeight - 1 < mImageView.getHeight()) {
					ResetAnimimation animation = new ResetAnimimation(mImageView, mImageViewHeight);
					animation.setDuration(300);
					mImageView.startAnimation(animation);
				}
			}
		}
	};

	public class ResetAnimimation extends Animation {
		int targetHeight;
		int originalHeight;
		int extraHeight;
		View mView;

		protected ResetAnimimation(View view, int targetHeight) {
			this.mView = view;
			this.targetHeight = targetHeight;
			originalHeight = view.getHeight();
			extraHeight = this.originalHeight - originImageViewHeight;
			Log.i("debug", "target heitht " + targetHeight + " original height " + originalHeight + " extraheight " + extraHeight);
		}

		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {

			int newHeight;
			newHeight = (int) (originImageViewHeight + extraHeight * (1 - interpolatedTime));
			mView.getLayoutParams().height = newHeight;
			mView.requestLayout();
		}
	}

	public interface OnMyScrollChangedListener {
		void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy);
	}
}
