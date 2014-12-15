/**
 * 
 */
package cn.salesuite.saf.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import cn.salesuite.saf.app.SAFApp;

/**
 *  使用该控件，app需要添加如下的权限
 * 	&ltuses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" /><br>
 *  &ltuses-permission android:name="android.permission.SYSTEM_OVERLAY_WINDOW" />
 * @author Tony Shen
 * 
 */
public class WindowUI {

	private static WindowManager wm;
	private static FloatView floatView;

	/***
	 * 产生悬浮的ImageView 窗口
	 * 
	 * @param context
	 * @param id resourceid
	 * @param l 单击事件
	 */
	public static void createFloatWindow(Context context, int id,
			OnClickListener l) {
		
		removeFloatWindow();
		
		wm = (WindowManager) context.getSystemService(
				Context.WINDOW_SERVICE);

		WindowManager.LayoutParams wmlp = ((SAFApp) context
				.getApplicationContext()).getLayoutParams();
		floatView = new WindowUI.FloatView(context);
		floatView.setImageResource(id);
		floatView.setOnClickListener(l);
		wmlp.gravity = Gravity.TOP | Gravity.RIGHT;

		wmlp.x = 0;
		wmlp.y = wm.getDefaultDisplay().getHeight() / 2;

		wmlp.width = LayoutParams.WRAP_CONTENT;
		wmlp.height = LayoutParams.WRAP_CONTENT;

		wm.addView(floatView, wmlp);// add the view
	}

	public static void removeFloatWindow() {
		if (floatView != null)
			wm.removeViewImmediate(floatView);
		floatView = null;
	}

	public static void showFloatWindow() {
		if (floatView != null)
			floatView.setVisibility(View.VISIBLE);
	}

	public static void hideFloatWindow() {
		if (floatView != null)
			floatView.setVisibility(View.GONE);
	}

	public static boolean isShowing() {
		if (floatView != null)
			return floatView.isShown();
		return false;
	}

	private static class FloatView extends ImageView {
		private float mTouchX;
		private float mTouchY;
		private float x;
		private float y;
		private float mStartX;
		private float mStartY;
		private OnClickListener mClickListener;

		// 此windowManagerParams变量为获取的全局变量，用以保存悬浮窗口的属性
		private WindowManager.LayoutParams windowManagerParams = ((SAFApp) getContext()
				.getApplicationContext()).getLayoutParams();

		public FloatView(Context context) {
			super(context);
		}

		public FloatView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// 获取到状态栏的高度

			Rect frame = new Rect();
			getWindowVisibleDisplayFrame(frame);
			int statusBarHeight = frame.top;
			// 获取相对屏幕的坐标，即以屏幕左上角为原点

			x = event.getRawX();
			y = event.getRawY() - statusBarHeight; // statusBarHeight是系统状态栏的高度

			// Log.i("tag", "currX" + x + "====currY" + y);
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作

				// 获取相对View的坐标，即以此View左上角为原点
				mTouchX = event.getX();
				mTouchY = event.getY();
				mStartX = x;
				mStartY = y;

				break;

			case MotionEvent.ACTION_MOVE: // 捕获手指触摸移动动作

				updateViewPosition();
				break;

			case MotionEvent.ACTION_UP: // 捕获手指触摸离开动作

				updateViewPosition();
				mTouchX = mTouchY = 0;
				if ((x - mStartX) < 10 && (y - mStartY) < 10) {
					if (mClickListener != null) {
						mClickListener.onClick(this);
					}
				}
				break;
			}
			return true;
		}

		@Override
		public void setOnClickListener(OnClickListener l) {
			this.mClickListener = l;
		}

		private void updateViewPosition() {
			// 更新浮动窗口位置参数
			windowManagerParams.x = (int) -(x - mTouchX);
			windowManagerParams.y = (int) (y - mTouchY);
			wm.updateViewLayout(this, windowManagerParams); // 刷新显示
		}
	}
}
