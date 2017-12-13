package com.willkernel.app.hencodercanvas;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class MyCanvas extends View {
    private Paint mPaint;
    private Bitmap mBitmap;
    private Path mPath;
    private Matrix mMatrix;
    private Camera mCamera;
    private int degree = 120;
    private ObjectAnimator objectAnimator;

    public MyCanvas(Context context) {
        super(context);
        init();
    }

    public MyCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_google_maps);
        mPath = new Path();
        mPath.addCircle(300, 500, 150, Path.Direction.CW);
        mMatrix = new Matrix();
        mCamera = new Camera();
        objectAnimator = ObjectAnimator.ofInt(this, "degree", 0, 180);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.setDuration(2000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        objectAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        objectAnimator.cancel();
    }

    @SuppressWarnings("unused")
    public void setDegree(int degree) {
        this.degree = degree;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.save();
////        裁切
//        canvas.clipRect(50, 50, 500, 300);
////        错切
//        canvas.skew(-0.2f, 0);
//        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
//        canvas.restore();
//
//        canvas.save();
//        canvas.clipPath(mPath);
//        canvas.drawBitmap(mBitmap, 50, 200, mPaint);
//        canvas.restore();
//
//        canvas.save();
//        canvas.translate(200, 600);
////        旋转
//        canvas.rotate(30);
////        放缩
//        canvas.scale(1.1f, 1.1f, 10 + mBitmap.getWidth(), 15 + mBitmap.getHeight());
//        canvas.drawBitmap(mBitmap, 10, 15, mPaint);
//        canvas.restore();

//        mMatrix.reset();
//        mMatrix.postRotate(-20,mBitmap.getWidth()/2,mBitmap.getHeight()/2+50);
//        mMatrix.postTranslate(100, 0);
//
////        不同的系统中  setMatrix(matrix) 的行为可能不一致，所以还是尽量用 concat(matrix) 吧
////        用 Canvas 当前的变换矩阵和 Matrix 相乘，即基于 Canvas 当前的变换，叠加上 Matrix 中的变换
//        canvas.save();
//        canvas.concat(mMatrix);
//        canvas.drawBitmap(mBitmap, 0, 50, mPaint);
//        canvas.restore();
//
////        使用 Matrix 来做自定义变换
////        srcIndex 和 dstIndex 是第一个点的偏移；pointCount 是采集的点的个数（个数不能大于 4，因为大于 4 个点就无法计算变换了）
//        float[] pointSrc = {0, mBitmap.getHeight() / 2, mBitmap.getWidth(), mBitmap.getHeight() / 2,
//                0, mBitmap.getHeight(), mBitmap.getWidth(), mBitmap.getHeight()};
//        float[] pointDst = {30, mBitmap.getHeight() / 2, mBitmap.getWidth() - 30, mBitmap.getHeight() / 2,
//                -30, mBitmap.getHeight(), mBitmap.getWidth() + 30, mBitmap.getHeight()};
//        mMatrix.reset();
//        canvas.save();
//        mMatrix.setPolyToPoly(pointSrc, 0, pointDst, 0, 4);
//        canvas.concat(mMatrix);
//        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
//        canvas.restore();

//        Camera 的三维变换有三类：旋转、平移、移动相机
//        rotateX(deg) rotateY(deg) rotateZ(deg) rotate(x, y, z)
//        canvas.save();


//        mCamera.save();
////      单位inch  Android 底层的图像引擎 Skia 。在 Skia 中，Camera 的位置单位是英寸，
////      英寸和像素的换算单位在 Skia 中被写死为了 72 像素
////      在 Camera 中，相机的默认位置是 (0, 0, -8)（英寸）
////      8 x 72 = 576，所以它的默认位置是 (0, 0, -576)
//        mCamera.setLocation(0, 0, -10);
//        mCamera.rotateX(30);
//        mCamera.translate(10, 10, 0);
//
//        canvas.translate(mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
//        mCamera.applyToCanvas(canvas);
//        canvas.translate(-mBitmap.getWidth() / 2, -mBitmap.getHeight() / 2);
//
//        mCamera.restore();
//        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
//
//        canvas.restore();

        //FlipBoard
        int bitmapX = mBitmap.getWidth();
        int bitmapY = mBitmap.getHeight();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int x = centerX - bitmapX / 2;
        int y = centerY - bitmapY / 2;

        canvas.save();
        canvas.clipRect(x, y, x + bitmapX, y + bitmapY / 2);
        canvas.drawBitmap(mBitmap, x, y, mPaint);
        canvas.restore();

        canvas.save();
        mCamera.save();

        //当旋转大于90°,clipRect 下半部分图片会翻面,会反过来  所以clipRect 变成 0 ,0 ,getWidth,centerY
        if (degree < 90) {
            canvas.clipRect(x, centerY, getWidth(), getHeight());
        } else {
            canvas.clipRect(0, 0, getWidth(), centerY);
        }


        //canvas 绘制顺序是反向的 ,先移回原位置
        canvas.translate(centerX, centerY);
        //再旋转
        mCamera.rotateX(degree);
        //将camera三维运动应用到canvas中
        mCamera.applyToCanvas(canvas);
        //再移动到原点
        canvas.translate(-centerX, -centerY);
        mCamera.restore();


        canvas.drawBitmap(mBitmap, x, y, mPaint);
        canvas.restore();

    }
}