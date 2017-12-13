package com.willkernel.app.hencoderviewdraworder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 个完整的绘制过程会依次绘制以下几个内容：
 * <p>
 * 背景 drawBackground()不能重写
 * 主体（onDraw()）
 * 子 View（dispatchDraw()）
 * 滑动边缘渐变和滑动条
 * 前景
 * <p>
 * 需要注意，前景的支持是在 Android 6.0（也就是 API 23）才加入的；之前其实也有，不过只支持  FrameLayout，
 * 而直到 6.0 才把这个支持放进了 View 类里
 */
public class SpotLinearLayout extends LinearLayout {
    private Paint mPaint;

    public SpotLinearLayout(Context context) {
        super(context);
        init();
    }

    public SpotLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpotLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#ff9988"));
    }


    /**
     * 绘制主体
     * Android 的绘制顺序：在绘制过程中，每一个 ViewGroup 会先调用自己的 onDraw() 来绘制完自己的主体之后再去绘
     * 制它的子 View。对于上面这个例子来说，就是你的 LinearLayout 会在绘制完斑点后再去绘制它的子 View
     * 那么在子 View 绘制完成之后，先前绘制的斑点就被子 View 盖住了
     * 这里说的「绘制子 View」是通过另一个绘制方法的调用来发生的，这个绘制方法叫做：dispatchDraw()
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawCircle(50, 50, 20, mPaint);
    }


    /**
     * If this view doesn't do any drawing on its own, set this flag to
     * allow further optimizations. By default, this flag is not set on
     * View, but could be set on some View subclasses such as ViewGroup.
     * <p>
     * Typically, if you override {@link #onDraw(android.graphics.Canvas)}
     * you should clear this flag.
     * <p>
     * willNotDraw whether or not this View draw on its own
     * <p>
     * 关于绘制方法，有两点需要注意一下：
     * 1.出于效率的考虑，ViewGroup 默认会绕过 draw() 方法，换而直接执行 dispatchDraw()，以此来简化绘制流程
     * 所以如果你自定义了某个 ViewGroup 的子类（比如 LinearLayout）并且需要在它的除  dispatchDraw()
     * 以外的任何一个绘制方法内绘制内容，你可能会需要调用 View.setWillNotDraw(false) 这行代码来切换到完整的
     * 绘制流程（是「可能」而不是「必须」的原因是，有些 ViewGroup 是已经调用过 setWillNotDraw(false) 了的，
     * 例如 ScrollView）
     * 2.有的时候，一段绘制代码写在不同的绘制方法中效果是一样的，这时你可以选一个自己喜欢或者习惯的绘制方法来重写
     * 但有一个例外：如果绘制代码既可以写在 onDraw() 里，也可以写在其他绘制方法里，那么优先写在 onDraw() ，
     * 因为 Android 有相关的优化，可以在不需要重绘的时候自动跳过  onDraw() 的重复执行，以提升开发效率
     * 享受这种优化的只有 onDraw() 一个方法。
     */
    @Override
    public void setWillNotDraw(boolean willNotDraw) {
        super.setWillNotDraw(willNotDraw);
    }


    /**
     * dispatchDraw() 方法来绘制子 View
     * 注：虽然 View 和 ViewGroup 都有 dispatchDraw() 方法，不过由于 View 是没有子 View 的，所以一般来说
     * dispatchDraw() 这个方法只对 ViewGroup（以及它的子类）有意义
     * <p>
     * 只要重写 dispatchDraw()，并在 super.dispatchDraw() 的下面写上你的绘制代码，这段绘制代码就会发生
     * 在子 View 的绘制之后，从而让绘制内容盖住子 View 了(写在super.dispatchDraw()之前和在onDraw之后是一样被遮挡)
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawCircle(200, 100, 20, mPaint);
    }
}
