package com.willkernel.app.hencoderviewdraworder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

public class MyEditText extends AppCompatEditText {
    public MyEditText(Context context) {
        super(context);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private final Paint mPaint;

    {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.CYAN);
    }

    @Override
    public void draw(Canvas canvas) {
        //在draw之前调用,设置背景,并且不会像设置backgroundColor 那样遮挡EditText 下划线
        //被背景盖住
        canvas.drawColor(Color.GREEN);

        super.draw(canvas);
        //盖住前景
        //canvas.drawColor(Color.GREEN);
    }
}
