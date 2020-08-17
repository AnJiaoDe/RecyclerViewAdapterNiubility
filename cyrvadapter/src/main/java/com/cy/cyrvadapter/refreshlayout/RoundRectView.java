package com.cy.cyrvadapter.refreshlayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class RoundRectView extends View {
    private Paint paint;
    private int width = 4;
    private int height = 20;
    private float radius = 2;
    private RectF rectF;
    public RoundRectView(Context context) {
        this(context, null);
    }

    public RoundRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        width=ScreenUtils.dpAdapt(context,width);
        height=ScreenUtils.dpAdapt(context,height);
        radius=ScreenUtils.dpAdapt(context,radius);

        paint = new Paint();
        //画圆角矩形
        paint.setStyle(Paint.Style.FILL);//充满
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);// 设置画笔的锯齿效果

        rectF=new RectF();
    }

    public RoundRectView setWidth(int width) {
        this.width = width;
        return this;
    }

    public RoundRectView setHeight(int height) {
        this.height = height;
        return this;
    }

    public RoundRectView setColor(int color) {
        paint.setColor(color);
        return this;
    }

    public RoundRectView setRadius(float radius) {
        this.radius = radius;
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rectF.left=0;
        rectF.top=0;
        rectF.right=width;
        rectF.bottom=height;
        canvas.drawRoundRect(rectF, radius, radius, paint);//第二个参数是x半径，第三个参数是y半径
    }

}
