package com.yezimm.gathering.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewUtils {

	public static int getPixel(Context context, float dip) {
		if(context == null){
			return 0;
		}
		return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
				context.getResources().getDisplayMetrics()));
	}

	public static int getSpPixel(Context context, float sp) {
		if(context == null){
			return 0;
		}
		return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
				context.getResources().getDisplayMetrics()));
	}

	public static void expandListView(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		params.height += ViewUtils.getPixel(listView.getContext(), 15);
		listView.setLayoutParams(params);
	}

	public static void addShadow(TextView view) {
		view.setShadowLayer(1.5f, 0f, 1f, 0xFFFFFFFF);
	}

	/**
	 * 合并颜色和透明度
	 * @param color
	 * @param alpha 0-255
	 * @return
	 */
	public static int mergeColor(int color,int alpha){
		int r = Color.red(color);
		int g = Color.green(color);
		int b = Color.blue(color);
		return Color.argb(alpha, r, g, b);
	}

	public static void highLightText(TextView tv, String str, String... highLightWords){
		if(highLightWords != null && highLightWords.length > 0){
			Spannable spannable = new SpannableString(str);
			int color = Color.parseColor("#ed5026");
			for (int i = 0; i < highLightWords.length; i++) {
				List<Integer> idxs=new ArrayList<Integer>();
				String replacedStr = highLightWords[i];
				String tempstr = str;
				while (tempstr != null && tempstr.lastIndexOf(replacedStr) > 0) {
					int idxTemp = tempstr.lastIndexOf(replacedStr);
					idxs.add(tempstr.lastIndexOf(replacedStr));
					tempstr = tempstr.substring(0, idxTemp);
				}
				for (Integer idx : idxs) {
					int start = idx;
					if(start >= 0){
						int end = start + highLightWords[i].length();
						spannable.setSpan(new ForegroundColorSpan(color), idx, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
			}
			tv.setText(spannable);
		}
	}

	/**
	 * 获得字符串绘制长度
	 * @param len
	 * @return
	 */
	public static int getTextLen(String text, int size) {
		return getTextLen(text, size, null);
	}

	public static int getTextLen(String text, int size, Typeface typeface) {
		Paint paint = new Paint();
		paint.setTextSize(size);
		if (typeface != null) {
			paint.setTypeface(typeface);
		}

		return getTextLen(text, paint);
	}

	public static int getTextLen(TextView view) {
		return getTextLen(view.getText().toString(), view.getPaint());
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 * @author renjie
	 * @date 2013.10.24
	 */
	public static float dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 * @author renjie
	 * @date 2013.10.24
	 */
	public static float px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (pxValue / scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 *
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static float px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 *
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static float sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (spValue * fontScale + 0.5f);
	}

	public static int getTextLen(String text, Paint paint) {
		float len = 0;
		float[] widths = new float[text.length()];
		paint.getTextWidths(text, widths);
		for (int i = 0; i < widths.length; i++) {
			len += widths[i];
		}
		return (int) len;
	}

	/**
	 * 获得字符串绘制高度
	 * @param len
	 * @return
	 */
	public static int getTextHeight(String text, int size, int lineHeight, int width) {
		Paint paint = new Paint();
		paint.setTextSize(size);

		float w = 0;
		float height = lineHeight;
		float[] widths = new float[text.length()];
		paint.getTextWidths(text, widths);
		for (int i = 0; i < widths.length; i++) {
			w += widths[i];
			if (w > width) {
				height += lineHeight;
				w = widths[i];
			}
		}
		return (int)height + 2;
	}

	/**
	 * 按一个长度截断字符串，并在末尾加“...”
	 * @param text
	 * @param size
	 * @param len
	 * @return
	 */
	public static String parseTextByLen(String text, int size, int len) {
		String end = "...";
		boolean cut = false;

		Paint paint = new Paint();
		paint.setTextSize(size);

		float endw = 0;
		float[] widths = new float[end.length()];
		paint.getTextWidths(end, widths);
		for (int i = 0; i < widths.length; i++) {
			endw += widths[i];
		}

		widths = new float[text.length()];
		paint.getTextWidths(text, widths);
		StringBuffer buffer = new StringBuffer();
		float w = 0;
		for (int i = 0; i < widths.length; i++) {
			w += widths[i];
			if (w + endw > len) {
				cut = true;
				break;
			}
			buffer.append(text.charAt(i));
		}
		if (cut)
			return buffer.toString() + end;

		return buffer.toString();
	}

	/**
	 * 按宽度和行数截断字符串，并在末尾加“...”
	 * @param text
	 * @param size
	 * @param width
	 * @param lineNum
	 * @return
	 */
	public static String parseTextByWidthAndLineNum(String text, int size, int width, int lineNum) {
		String end = "...";
		String numChars = "0123456789.";
		String interpunctionChars = "，/";

		Paint paint = new Paint();
		paint.setTextSize(size);

		float endw = 0;
		float[] widths = new float[end.length()];
		paint.getTextWidths(end, widths);
		for (int i = 0; i < widths.length; i++) {
			endw += widths[i];
		}

		widths = new float[text.length()];
		paint.getTextWidths(text, widths);
		StringBuffer buffer = new StringBuffer();
		int line = 1;
		float w = 0;
		for (int i = 0; i < widths.length; i++) {
			if (text.charAt(i) == '\n') {
				buffer.append("\n");
				w = 0;
				line++;
			} else {
				StringBuffer unitBuf = new StringBuffer();
				float unitW = 0;
				if (numChars.contains(text.charAt(i) + "")) {
					int j = i;
					for (j = i; j < widths.length; j++) {
						if (numChars.contains(text.charAt(j) + "")) {
							unitW += widths[j];
							unitBuf.append(text.charAt(j));
						} else {
							break;
						}
					}
					i = j - 1;
				} else {
					unitBuf.append(text.charAt(i));
					unitW = widths[i];
				}

				if (i < text.length() - 1 && interpunctionChars.contains(text.charAt(i + 1) + "")) {
					i++;

					unitW += widths[i];
					unitBuf.append(text.charAt(i));
				}

				if (line == lineNum) {
					if (w + unitW + endw > width) {
						buffer.append(end);
						break;
					} else {
						w += unitW;
					}
				} else {
					if (w + unitW > width) {
						buffer.append("\n");
						w = unitW;
						line++;
					} else {
						w += unitW;
					}
				}
				buffer.append(unitBuf);
			}
		}

		return buffer.toString();
	}
}
