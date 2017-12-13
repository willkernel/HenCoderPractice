package com.willkernel.app.hencoderviewdraworder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Draw traversal performs several drawing steps which must be executed
 * in the appropriate order:
 * <p>
 * 1. Draw the background
 * 2. If necessary, save the canvas' layers to prepare for fading
 * 3. Draw view's content
 * 4. Draw children
 * 5. If necessary, draw the fading edges and restore layers
 * 6. Draw decorations (scrollbars for instance)
 * <p>
 * <p>
 * // Step 1, draw the background, if needed
 * int saveCount;
 * <p>
 * if (!dirtyOpaque) {
 * drawBackground(canvas);
 * }
 * <p>
 * // skip step 2 & 5 if possible (common case)
 * final int viewFlags = mViewFlags;
 * boolean horizontalEdges = (viewFlags & FADING_EDGE_HORIZONTAL) != 0;
 * boolean verticalEdges = (viewFlags & FADING_EDGE_VERTICAL) != 0;
 * if (!verticalEdges && !horizontalEdges) {
 * // Step 3, draw the content
 * if (!dirtyOpaque) onDraw(canvas);
 * <p>
 * // Step 4, draw the children
 * dispatchDraw(canvas);
 * <p>
 * drawAutofilledHighlight(canvas);
 * <p>
 * // Overlay is part of the content and draws beneath Foreground
 * if (mOverlay != null && !mOverlay.isEmpty()) {
 * mOverlay.getOverlayView().dispatchDraw(canvas);
 * }
 * <p>
 * // Step 6, draw decorations (foreground, scrollbars)
 * onDrawForeground(canvas);
 * <p>
 * // Step 7, draw the default focus highlight
 * drawDefaultFocusHighlight(canvas);
 * <p>
 * if (debugDraw()) {
 * debugDrawFocus(canvas);
 * }
 * <p>
 * // we're done...
 */
public class MyImageView extends AppCompatImageView {
    private Paint mPaint;

    public MyImageView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(20);
        mPaint.setColor(Color.parseColor("#33ffff"));
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        //super.onDraw之前 被原内容覆盖,一般是作为底色,边框出现
//        canvas.drawColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
        super.onDraw(canvas);
        // 之后 覆盖在原内容上面
        String text = "尺寸" + width + "*" + height;
        float mTextWidth = mPaint.measureText(text);
        canvas.drawText(text, width / 2 - mTextWidth / 2, height / 2, mPaint);
    }

    /**
     * 滑动边缘渐变和滑动条以及前景，这两部分被合在一起放在了 onDrawForeground() 方法里，这个方法是可以重写
     * 滑动边缘渐变和滑动条可以通过 xml 的 android:scrollbarXXX 系列属性或 Java 代码的
     * View.setXXXScrollbarXXX() 系列方法来设置；前景可以通过 xml 的 android:foreground 属性或 Java
     * 代码的  View.setForeground() 方法来设置。而重写 onDrawForeground() 方法，并在它的
     * super.onDrawForeground() 方法的上面或下面插入绘制代码，则可以控制绘制内容和滑动边缘渐变、滑动条以及前景的遮盖关系
     * 如果你把绘制代码写在了 super.onDrawForeground() 的下面，绘制代码会在滑动边缘渐变、滑动条和前景之后被执行，
     * 那么绘制内容将会盖住滑动边缘渐变、滑动条和前景
     */
    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);
        drawNewTab(canvas);
    }

    private void drawNewTab(Canvas canvas) {
        mPaint.setColor(Color.RED);
        canvas.drawRect(0, 20, 60, 50, mPaint);
        mPaint.setColor(Color.WHITE);
        canvas.drawText("New", 10, 40, mPaint);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        // 和重写onDrawForeground 方法一致
    }
}
