package com.cy.recyclerviewadapter.activity.grv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.cy.androidview.BitmapUtils;
import com.cy.androidview.R;
import com.cy.androidview.ScreenUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageViewShadow extends View {
    private Paint paint, paint_filter, paintShadow;
    private volatile Bitmap bitmap, bitmapAlpha;
    private int width, height;
    private int left_pic, top_pic, right_pic, bottom_pic;
    private Rect rect;
    private volatile int drawableSrc;
    private int colorFilter;
    private int color_shadow;
    private int shadow_limit = 0;
    private int mask_type = 1;
    private int blur_radius = 1;
    private ExecutorService executorServiceSingle;

    public ImageViewShadow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_HARDWARE, null);
        executorServiceSingle = Executors.newSingleThreadExecutor();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageViewShadow);
        drawableSrc = typedArray.getResourceId(R.styleable.ImageViewShadow_cy_src, -1);
        colorFilter = typedArray.getColor(R.styleable.ImageViewShadow_cy_color_filter, -1);
        color_shadow = typedArray.getColor(R.styleable.ImageViewShadow_cy_color_shadow, 0xff616161);
        shadow_limit = ScreenUtils.dpAdapt(context, 0.4f);
        shadow_limit = typedArray.getDimensionPixelSize(R.styleable.ImageViewShadow_cy_shadow_limit, shadow_limit);
        //BlurMaskFilter的blur_radius  must >0，否则GG
        blur_radius = Math.max(blur_radius, typedArray.getDimensionPixelSize(R.styleable.ImageViewShadow_cy_blur_radius, blur_radius));
        mask_type = typedArray.getInt(R.styleable.ImageViewShadow_cy_mask_type, mask_type);

        typedArray.recycle();

        bitmap = BitmapUtils.decodeResource(getContext(), drawableSrc, 100 * 100);
        if (bitmap != null)
            bitmapAlpha = bitmap.extractAlpha();

        paint = new Paint();
        paint.setAntiAlias(true);

        paint_filter = new Paint();
        paint_filter.setAntiAlias(true);
        if (colorFilter != -1) paint_filter.setColor(colorFilter);

        paintShadow = new Paint();
        paintShadow.setAntiAlias(true);
        paintShadow.setColor(color_shadow);
        switch (mask_type) {
            case 0:
                paintShadow.setMaskFilter(new BlurMaskFilter(blur_radius, BlurMaskFilter.Blur.NORMAL));
                break;
            case 1:
                paintShadow.setMaskFilter(new BlurMaskFilter(blur_radius, BlurMaskFilter.Blur.SOLID));
                break;
            case 2:
                paintShadow.setMaskFilter(new BlurMaskFilter(blur_radius, BlurMaskFilter.Blur.OUTER));
                break;
            case 3:
                paintShadow.setMaskFilter(new BlurMaskFilter(blur_radius, BlurMaskFilter.Blur.INNER));
                break;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        if (bitmap == null) return;

        int width__ = width - getPaddingLeft() - getPaddingRight();
        int height__ = height - getPaddingTop() - getPaddingBottom();
        float ratio_s = height__ * 1f / width__;
        float ratio_b = bitmap.getHeight() * 1f / bitmap.getWidth();

        int width_b;
        int height_b;
        if (ratio_s < ratio_b) {
            height_b = height__;
            width_b = (int) (height_b * 1f / ratio_b);
        } else {
            width_b = width__;
            height_b = (int) (width_b * 1f * ratio_b);
        }

        left_pic = (int) (getPaddingLeft() + (width__ - width_b) * 0.5f);
        top_pic = (int) (getPaddingTop() + (height__ - height_b) * 0.5f);
        right_pic = left_pic + width_b;
        bottom_pic = top_pic + height_b;
        rect = new Rect(left_pic, top_pic, right_pic, bottom_pic);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap == null || rect == null) return;
        canvas.drawBitmap(bitmapAlpha, null, rect, paintShadow);
        canvas.translate(-shadow_limit, -shadow_limit);
        if (colorFilter != -1) {
            canvas.drawBitmap(bitmapAlpha, null, rect, paint_filter);
        } else {
            canvas.drawBitmap(bitmap, null, rect, paint);
        }
    }

    public void setColorFilter(int colorFilter) {
        this.colorFilter = colorFilter;
        if (colorFilter != -1) paint_filter.setColor(colorFilter);
        invalidate();
    }

    public void clearColorFilter() {
        this.colorFilter = -1;
        invalidate();
    }
}
