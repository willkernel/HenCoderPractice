package com.willkernel.app.hencoderanimator;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.BounceInterpolator;

/**
 * 比如有的时候，你要给一个第三方控件做动画，你需要更新的那个属性没有 setter 方法，只能直接修改，这样的话
 * ObjectAnimator 就不灵了啊。怎么办？这个时候你就可以用 ValueAnimator，在它的 onUpdate() 里面更新这
 * 个属性的值，并且手动调用 invalidate()。
 * 所以你看，ViewPropertyAnimator、ObjectAnimator、ValueAnimator 这三种 Animator，它们其实是一种递进的
 * 关系：从左到右依次变得更加难用，也更加灵活。但我要说明一下，它们的性能是一样的，因为 ViewPropertyAnimator
 * 和 ObjectAnimator 的内部实现其实都是 ValueAnimator，ObjectAnimator 更是本来就是 ValueAnimator 的子类，
 * 它们三个的性能并没有差别。它们的差别只是使用的便捷性以及功能的灵活性。所以在实际使用时候的选择，只要遵循一个原
 * 则就行：尽量用简单的。能用 View.animate() 实现就不用 ObjectAnimator，能用 ObjectAnimator 就不用 ValueAnimator
 * <p>
 * 第二部分，「关于复杂的属性关系来做动画」，就这么三种：
 * <p>
 * 使用 PropertyValuesHolder 来对多个属性同时做动画；
 * 使用 AnimatorSet 来同时管理调配多个动画；
 * PropertyValuesHolder 的进阶使用：使用 PropertyValuesHolder.ofKeyframe() 来把一个属性拆分成多段，执行更加精细的属性动画。
 */
@SuppressWarnings("unused")
public class AnimImageView extends View {
    private Paint mPaint;
    private float progress;
    private float mRadius;
    private Path mPath;
    private int color = 0xffff0000;
    private PointF position;

    public float getMRadius() {
        return mRadius;
    }

    public void setMRadius(float mRadius) {
        this.mRadius = mRadius;
        invalidate();
    }

    public AnimImageView(Context context) {
        super(context);
        init();
    }

    public AnimImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 硬件加速指的是使用 GPU 来完成绘制的计算工作，代替 CPU。它从工作分摊和绘制机制优化这两个角度提升了绘制的速度。
     * 硬件加速可以使用 setLayerType() 来关闭硬件加速，但这个方法其实是用来设置 View Layer 的：
     * 参数为 LAYER_TYPE_SOFTWARE 时，使用软件来绘制 View Layer，绘制到一个 Bitmap，并顺便关闭硬件加速；
     * 参数为 LAYER_TYPE_HARDWARE 时，使用 GPU 来绘制 View Layer，绘制到一个 OpenGL texture（如果硬件加速关闭，那么行为和 VIEW_TYPE_SOFTWARE 一致）；
     * 参数为 LAYER_TYPE_NONE 时，关闭 View Layer
     * View Layer 可以加速无 invalidate() 时的刷新效率，但对于需要调用 invalidate() 的刷新无法加速
     * View Layer 绘制所消耗的实际时间是比不使用 View Layer 时要高的，所以要慎重使用
     */
    public void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPaint.setTextSize(40);
        mPaint.setTextAlign(Paint.Align.CENTER);

        mRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
        mPath = new Path();

//        设置一个 ColorMatrixColorFilter 来让 View 变成黑白的

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        mPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));

//        另外，由于设置了 View Layer 后，View 在初次绘制时以及每次 invalidate() 后重绘时，需要进行两次的绘制
//        工作（一次绘制到 Layer，一次从 Layer 绘制到显示屏），所以其实它的每次绘制的效率是被降低了的。所以一定要
//        慎重使用 View Layer，在需要用到它的时候再去使用
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
//        设置了硬件加速后,不需要调用invalidate() ,绘制效率,重绘机制更高
//        不过一定要注意，只有你在对 translationX translationY rotation alpha 等无需调用 invalidate()
//        的属性做动画的时候，这种方法才适用，因为这种方法本身利用的就是当界面不发生时，缓存未更新所带来的时间的节省

//        所以这种方式不适用于基于自定义属性绘制的动画

//        .withLayer()
//        setLayerType(LAYER_TYPE_SOFTWARE,null);

//        invalidate();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
//        invalidate();
    }

    public PointF getPosition() {
        return position;
    }

    public void setPosition(PointF position) {
        this.position = position;
//        invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //animate() return ViewPropertyAnimator
        //AccelerateDecelerateInterpolator 默认的插值器
//        animate() .scaleX(1)
//                .scaleY(1)
//                .alpha(1)
//                .withLayer(); // withLayer() 开启硬件加速 ,自动完成 setLayerType(LAYER_TYPE_NONE,null);
//                .translationX(200).rotation(20).setDuration(500)
//                .setInterpolator(new AccelerateDecelerateInterpolator())
//                .setListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//
//                    }
//
//                    /**由于 ViewPropertyAnimator 不支持重复，所以这个方法对 ViewPropertyAnimator 相当于无效*/
//                    @Override
//                    public void onAnimationRepeat(Animator animation) {
//                    }
//                })
//                .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//
//                    }
//                })
//
//                .withStartAction(new Runnable() {
//                    /** ViewPropertyAnimator.withStartAction/EndAction()
//                     这两个方法是 ViewPropertyAnimator 的独有方法。它们和 set/addListener() 中回调的
//                     onAnimationStart() /  onAnimationEnd() 相比起来的不同主要有两点：
//                     withStartAction() / withEndAction() 是一次性的，在动画执行结束后就自动弃掉了，就算之后再重用
//                     ViewPropertyAnimator 来做别的动画，用它们设置的回调也不会再被调用。而 set/addListener() 所设置的
//                     AnimatorListener 是持续有效的，当动画重复执行时，回调总会被调用
//
//                     withEndAction() 设置的回调只有在动画正常结束时才会被调用，而在动画被取消时不会被执行
//                     这点和 AnimatorListener.onAnimationEnd() 的行为是不一致的*/
//                    @Override
//                    public void run() {
//
//                    }
//                })
//                .withEndAction(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
//        //1. 自定义view为属性值添加setter方法/ 如果属性值只有一个目标值,也要加getter方法
//        mPath.quadTo(0, 0, 1, 1);
//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this,
//                "translationY", 0, 100);
//        objectAnimator.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                  setLayerType(LAYER_TYPE_NONE,null);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            /**由于 ViewPropertyAnimator 不支持重复，所以这个方法对 ViewPropertyAnimator 相当于无效*/
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//            }
//        });
//        objectAnimator.addPauseListener(new Animator.AnimatorPauseListener() {
//            @Override
//            public void onAnimationPause(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationResume(Animator animation) {
//
//            }
//        });
//        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//
//            }
//        });
//        objectAnimator.removeAllListeners();
//        objectAnimator.setInterpolator(new PathInterpolator(mPath));
//        objectAnimator.start();

//        ObjectAnimator.ofFloat(this, "progress", 0, 50).setDuration(2000).start();
        setLayerType(LAYER_TYPE_HARDWARE, null);
        //在 PropertyValuesHolder 的基础上更进一步，通过设置  Keyframe （关键帧） 把同一个动画属性拆分成多个阶段
        //在0%处开始
        Keyframe keyframe1 = Keyframe.ofFloat(0, 0);
        //经过50%
        Keyframe keyframe2 = Keyframe.ofFloat(0.5f, 100);
        //完成100%时,回到正常位置
        Keyframe keyframe3 = Keyframe.ofFloat(1f, 60);
        PropertyValuesHolder holder = PropertyValuesHolder.ofKeyframe("progress", keyframe1, keyframe2, keyframe3);
        ObjectAnimator.ofPropertyValuesHolder(this, holder).setDuration(2000).start();


        //ObjectAnimator.ofArgb();
        ObjectAnimator colorAnim = ObjectAnimator.ofInt(this, "color", 0xffff0000, 0xff0000ff)
                .setDuration(2000);
        colorAnim.setEvaluator(new HSVEvaluator());
        colorAnim.start();

        ObjectAnimator.ofObject(this, "position", new PonitFEValuator(),
                new PointF(0, 0), new PointF(100, 100)).setDuration(2000).start();

        ObjectAnimator scaleAnim = ObjectAnimator.ofFloat(this, "mRadius", 60, 150).setDuration(2000);
        scaleAnim.setInterpolator(new BounceInterpolator());
        scaleAnim.start();

//        PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("alpha", 0, 1);
//        PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("scaleX", 0, 1);
//        PropertyValuesHolder holder3 = PropertyValuesHolder.ofFloat("scaleY", 0, 1);
//        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(this, holder1, holder2, holder3)
//                .setDuration(2000);
//        animator.start();

//        ObjectAnimator animator1 = ObjectAnimator.ofFloat(...);
//        animator1.setInterpolator(new LinearInterpolator());
//        ObjectAnimator animator2 = ObjectAnimator.ofInt(...);
//        animator2.setInterpolator(new DecelerateInterpolator());
//
//        AnimatorSet animatorSet = new AnimatorSet();
//// 两个动画依次执行
//        animatorSet.playSequentially(animator1, animator2);
//        animatorSet.start();

        // 使用 AnimatorSet.play(animatorA).with/before/after(animatorB)
// 的方式来精确配置各个 Animator 之间的关系
//        animatorSet.play(animator1).with(animator2);
//        animatorSet.play(animator1).before(animator2);
//        animatorSet.play(animator1).after(animator2);
//        animatorSet.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(color);
        mPaint.setStrokeWidth(20);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        canvas.drawArc(getWidth() / 2 - mRadius, getHeight() / 2 - mRadius, getWidth() / 2 + mRadius,
                getHeight() / 2 + mRadius, 90,
                progress * 3.6f, false, mPaint);

        mPaint.setStrokeWidth(1);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawText((int) progress + "%", getWidth() / 2, getHeight() / 2 -
                (mPaint.ascent() + mPaint.descent()) / 2, mPaint);

        canvas.drawCircle(position.x, position.y, 20, mPaint);
    }
}
