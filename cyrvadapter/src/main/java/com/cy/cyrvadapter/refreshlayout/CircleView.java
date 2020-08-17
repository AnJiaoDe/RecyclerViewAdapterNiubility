package com.cy.cyrvadapter.refreshlayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CircleView extends View {
    private Paint paint;
    private float radius = 2;
    private Context context;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);// 设置画笔的锯齿效果
        paint.setStyle(Paint.Style.FILL);//充满
        setColor(0x44454545);
        radius=ScreenUtils.dpAdapt(context, radius);
    }

    public CircleView setColor(int color) {
        paint.setColor(color);
        return this;
    }

    public CircleView setRadius(float radius) {
        this.radius = radius;
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec((int) (2 * radius), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec((int) (2 * radius), MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(radius, radius, radius, paint);
    }
}
