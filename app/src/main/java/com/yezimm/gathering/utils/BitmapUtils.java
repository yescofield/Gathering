package com.yezimm.gathering.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.view.View.MeasureSpec;

public class BitmapUtils {

	public static Bitmap convertViewToBitmap(View view, int bitmapWidth, int bitmapHeight){
		Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
		view.draw(new Canvas(bitmap));
		return bitmap;
	}

	/*
	 * 一般情况下，这个方法能够正常的工作。但有时候，生成Bitmap会出现问题(Bitmap全黑色)。主要原因是drawingCache的值大于系统给定的值。
	 *
	 * @param view
	 * @return
	 */
//	public static Bitmap convertViewToBitmap(View view){
//       view.buildDrawingCache();
//       Bitmap bitmap = view.getDrawingCache();
//       return bitmap;
//	}

	/*
	 * 我们可以看一下buildDrawingCache()方法中的一段代码：
	 */
//	if (width <= 0 || height <= 0 ||
//			(width * height * (opaque && !translucentWindow ? 2 : 4) >
//			ViewConfiguration.get(mContext).getScaledMaximumDrawingCacheSize())) {
//        destroyDrawingCache();
//        return;
//	}
	/*
	 * 上面的代码中，width和height是所要cache的view绘制的宽度和高度，
	 * 所以(width * height * (opaque && !translucentWindow ? 2 : 4)
	 * 计算的是当前所需要的cache大小。
	 * ViewConfiguration.get(mContext).getScaledMaximumDrawingCacheSize()
	 * 得到的是系统所提供的最大的DrawingCache的值。
	 * 当所需要的drawingCache >系统所提供的最大DrawingCache值时，
	 * 生成Bitmap就会出现问题，此时获取的Bitmap就为null。
	 */
//	所以在只需要修改所需的cache值就可以解决问题了。于是我们引入第二种方法：

	public static Bitmap convertViewToBitmap(View view){
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}

}
