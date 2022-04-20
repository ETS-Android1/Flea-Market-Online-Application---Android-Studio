package com.jiang.geo.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.LayoutInflater;

import com.jiang.geo.R;

/**
 * bitmap处理
 */

public class BitmapUtil {

    /**
     * 获取bitmap同时缩放
     *
     * @param c
     * @param resId
     * @return
     */
    public static Bitmap getBitmapFromRes(Context c, int resId) {
        Resources res = c.getResources();
        // set shrinkage to 2
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        // according id to analyse the detail
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private static int getColor(Context c, int noiseValue) {
        if (noiseValue > 70) {
            return c.getResources().getColor(R.color.color_70);
        }
        if (noiseValue >= 60 && noiseValue <= 70) {
            return c.getResources().getColor(R.color.color_60_70);
        }
        if (noiseValue >= 50 && noiseValue <= 60) {
            return c.getResources().getColor(R.color.color_50_60);
        }
        if (noiseValue >= 40 && noiseValue <= 50) {
            return c.getResources().getColor(R.color.color_40_50);
        }
        if (noiseValue < 40) {
            return c.getResources().getColor(R.color.color_30);
        }
        return c.getResources().getColor(R.color.color_30);
    }

    /**
     * Bitmap对象着色
     *
     * @param inBitmap
     * @param tintColor
     * @return
     */
    public static Bitmap tintBitmap(Bitmap inBitmap, int tintColor) {
        if (inBitmap == null) {
            return null;
        }
        Bitmap outBitmap = Bitmap.createBitmap(inBitmap.getWidth(), inBitmap.getHeight(), inBitmap.getConfig());
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(inBitmap, 0, 0, paint);
        return outBitmap;
    }

}
