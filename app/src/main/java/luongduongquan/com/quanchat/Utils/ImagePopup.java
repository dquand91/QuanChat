package luongduongquan.com.quanchat.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.squareup.picasso.Picasso;

import java.io.File;

import luongduongquan.com.quanchat.R;

public class ImagePopup extends android.support.v7.widget.AppCompatImageView implements View.OnTouchListener {

	private Context context;
	private PopupWindow popupWindow;
	View layout;
	private ImageView imageView;

	private int windowHeight = 0;
	private int windowWidth = 0;
	private boolean imageOnClickClose;
	private boolean hideCloseIcon;
	private boolean fullScreen;

	private int backgroundColor = Color.parseColor("#FFFFFF");

	private static final String TAG = "Touch123";
	@SuppressWarnings("unused")
	private static final float MIN_ZOOM = 1f,MAX_ZOOM = 1f;

	// These matrices will be used to scale points of the image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();

	// The 3 states (events) which the user is trying to perform
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	// these PointF objects are used to record the point(s) the user is touching
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;


	public ImagePopup(Context context) {
		super(context);
		this.context = context;
	}

	public ImagePopup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	public int getWindowHeight() {
		return windowHeight;
	}

	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
	}

	public int getWindowWidth() {
		return windowWidth;
	}

	public void setWindowWidth(int windowWidth) {
		this.windowWidth = windowWidth;
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}

	@Override
	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}


	/**
	 * Close Options
	 **/

	public void setImageOnClickClose(boolean imageOnClickClose) {
		this.imageOnClickClose = imageOnClickClose;
	}


	public boolean isImageOnClickClose() {
		return imageOnClickClose;
	}

	public boolean isHideCloseIcon() {
		return hideCloseIcon;
	}

	public void setHideCloseIcon(boolean hideCloseIcon) {
		this.hideCloseIcon = hideCloseIcon;
	}

	public boolean isFullScreen() {
		return fullScreen;
	}

	public void setFullScreen(boolean fullScreen) {
		this.fullScreen = fullScreen;
	}

	public void initiatePopup(Drawable drawable) {

		try {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

			layout = inflater.inflate(R.layout.image_preview_full, (ViewGroup) findViewById(R.id.popup));

			layout.setBackgroundColor(getBackgroundColor());

			imageView = (ImageView) layout.findViewById(R.id.imageView);
			imageView.setImageDrawable(drawable);

			/** Background dim part **/
//            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) layout.getLayoutParams();
//            layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//            layoutParams.dimAmount = 0.3f;
//            windowManager.updateViewLayout(layout, layoutParams);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initiatePopupWithPicasso(String imageUrl) {

		try {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

			layout = inflater.inflate(R.layout.image_preview_full, (ViewGroup) findViewById(R.id.popup));

			layout.setBackgroundColor(getBackgroundColor());

			imageView = (ImageView) layout.findViewById(R.id.imageView);

			Picasso.with(context).load(imageUrl).into(imageView);

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("ImagePopup ", e.getMessage());

		}
	}

	public void initiatePopupWithPicasso(Uri imageUri) {

		try {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

			layout = inflater.inflate(R.layout.image_preview_full, (ViewGroup) findViewById(R.id.popup));

			layout.setBackgroundColor(getBackgroundColor());

			imageView = (ImageView) layout.findViewById(R.id.imageView);

			Picasso.with(context).load(imageUri).into(imageView);


		} catch (Exception e) {
			e.printStackTrace();
			Log.e("ImagePopup ", e.getMessage());

		}
	}

	public void initiatePopupWithPicasso(File imageFile) {

		try {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

			layout = inflater.inflate(R.layout.image_preview_full, (ViewGroup) findViewById(R.id.popup));

			layout.setBackgroundColor(getBackgroundColor());

			imageView = (ImageView) layout.findViewById(R.id.imageView);

			Picasso.with(context)
					.load(imageFile)
					.into(imageView);

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("ImagePopup ", e.getMessage());
		}
	}

	public void setLayoutOnTouchListener(OnTouchListener onTouchListener) {
		layout.setOnTouchListener(onTouchListener);
	}

	public void viewPopup() {

		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);


		int width = metrics.widthPixels;
		int height = metrics.heightPixels;

		if(isFullScreen()) {
			popupWindow = new PopupWindow(layout, (width), (height), true);
		}else {
			if (windowHeight != 0 || windowWidth != 0) {
				width = windowWidth;
				height = windowHeight;
				popupWindow = new PopupWindow(layout, (width), (height), true);
			} else {
				popupWindow = new PopupWindow(layout, (int) (width * .8), (int) (height * .6), true);
			}
		}


		popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

		ImageView closeIcon = (ImageView) layout.findViewById(R.id.closeBtn);

		if (isHideCloseIcon()) {
			closeIcon.setVisibility(View.GONE);
		}
		closeIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
			}
		});

		if (isImageOnClickClose()) {
			imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
//					popupWindow.dismiss();
				}
			});
		}
		imageView.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView view = (ImageView) v;
		view.setScaleType(ImageView.ScaleType.MATRIX);
		float scale;

		dumpEvent(event);
		// Handle touch events here...

		switch (event.getAction() & MotionEvent.ACTION_MASK)
		{


			case MotionEvent.ACTION_DOWN:   // first finger down only
				matrix.set(view.getImageMatrix());
				savedMatrix.set(matrix);
				start.set(event.getX(), event.getY());
				Log.d(TAG, "mode=DRAG"); // write to LogCat
				mode = DRAG;
				break;

			case MotionEvent.ACTION_UP: // first finger lifted

			case MotionEvent.ACTION_POINTER_UP: // second finger lifted

				mode = NONE;
				Log.d(TAG, "mode=NONE");
				break;

			case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

				oldDist = spacing(event);
				Log.d(TAG, "oldDist=" + oldDist);
				if (oldDist > 5f) {
					savedMatrix.set(matrix);
					midPoint(mid, event);
					mode = ZOOM;
					Log.d(TAG, "mode=ZOOM");
				}
				break;

			case MotionEvent.ACTION_MOVE:

				if (mode == DRAG)
				{
					matrix.set(savedMatrix);
					matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
				}
				else if (mode == ZOOM)
				{
					// pinch zooming
					float newDist = spacing(event);
					Log.d(TAG, "newDist=" + newDist);
					if (newDist > 5f)
					{
						matrix.set(savedMatrix);
						scale = newDist / oldDist; // setting the scaling of the
						// matrix...if scale > 1 means
						// zoom in...if scale < 1 means
						// zoom out
						matrix.postScale(scale, scale, mid.x, mid.y);
					}
				}
				break;
		}

		view.setImageMatrix(matrix); // display the transformation on screen

		return true; // indicate event was handled
	}

	/*
	 * --------------------------------------------------------------------------
	 * Method: spacing Parameters: MotionEvent Returns: float Description:
	 * checks the spacing between the two fingers on touch
	 * ----------------------------------------------------
	 */

	private float spacing(MotionEvent event)
	{
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}

	/*
	 * --------------------------------------------------------------------------
	 * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
	 * Description: calculates the midpoint between the two fingers
	 * ------------------------------------------------------------
	 */

	private void midPoint(PointF point, MotionEvent event)
	{
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	/** Show an event in the LogCat view, for debugging */
	private void dumpEvent(MotionEvent event)
	{
		String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE","POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		sb.append("event ACTION_").append(names[actionCode]);

		if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP)
		{
			sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
			sb.append(")");
		}

		sb.append("[");
		for (int i = 0; i < event.getPointerCount(); i++)
		{
			sb.append("#").append(i);
			sb.append("(pid ").append(event.getPointerId(i));
			sb.append(")=").append((int) event.getX(i));
			sb.append(",").append((int) event.getY(i));
			if (i + 1 < event.getPointerCount())
				sb.append(";");
		}

		sb.append("]");
		Log.d("Touch Events ---------", sb.toString());
	}
}
