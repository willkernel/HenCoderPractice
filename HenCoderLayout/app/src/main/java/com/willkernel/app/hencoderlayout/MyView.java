package com.willkernel.app.hencoderlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * View 或 ViewGroup 的布局过程
 * <p>
 * 测量阶段，measure() 方法被父 View 调用，在 measure() 中做一些准备和优化工作后，调用  onMeasure()
 * 来进行实际的自我测量。 onMeasure() 做的事，View 和 ViewGroup 不一样：
 * <p>
 * View：View 在 onMeasure() 中会计算出自己的尺寸然后保存；
 * ViewGroup：ViewGroup 在 onMeasure() 中会调用所有子 View 的 measure() 让它们进行自我测量，并根据子 View
 * 计算出的期望尺寸来计算出它们的实际尺寸和位置（实际上 99.99% 的父 View 都会使用子 View 给出的期望尺寸来作为实际
 * 尺寸，原因在下期或下下期会讲到）然后保存。同时，它也会根据子 View 的尺寸和位置来计算出自己的尺寸然后保存；
 * 布局阶段，layout() 方法被父 View 调用，在 layout() 中它会保存父 View 传进来的自己的位置和尺寸，并且调用
 * onLayout() 来进行实际的内部布局。onLayout() 做的事， View 和 ViewGroup 也不一样：
 * <p>
 * View：由于没有子 View，所以 View 的 onLayout() 什么也不做。
 * ViewGroup：ViewGroup 在 onLayout() 中会调用自己的所有子 View 的 layout() 方法，把它们的尺寸和位置传给它们，
 * 让它们完成自我的内部布局
 */

public class MyView extends ViewGroup {
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 1.重写onMeasure()修改已有view的尺寸
     * 2.重写onMeasure()全新计算自定义view的尺寸
     * 3.重写onMeasure(),onLayout() 全新计算自定义ViewGroup的内部布局
     */
    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //计算原有尺寸信息,通过 setMeasuredDimension()保存测得尺寸,期望尺寸
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //修改已有尺寸信息,通过getMeasuredHeight ,getMeasuredWidth获取测量结果
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        if (measuredWidth > measuredHeight) {
            measuredWidth = measuredHeight;
        } else {
            measuredHeight = measuredWidth;
        }
        //保存修改后的尺寸信息
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
