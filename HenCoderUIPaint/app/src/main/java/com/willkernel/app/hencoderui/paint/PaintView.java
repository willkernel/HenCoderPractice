package com.willkernel.app.hencoderui.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.DashPathEffect;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Canvas 绘制的内容，有三层对颜色的处理：
 * 基本颜色: canvas.drawColor/ARGB   canvas.drawBitmap    canvas.drawPath,drawText,  paint.setShader()
 * ColorFilter: Paint.setColorFilter()
 * XferMode: Paint.setXferMode()
 * XferMode 指的是你要绘制的内容和 Canvas 的目标位置的内容应该怎样结合计算出最终的颜色。
 * 但通俗地说，其实就是要你以绘制的内容作为源图像，以 View 中已有的内容作为目标图像，
 * 选取一个  PorterDuff.Mode 作为绘制内容的颜色处理方案
 */
public class PaintView extends View {
    private Paint mPaint;
    private PorterDuffXfermode mXferMode;
    private DashPathEffect pathEffect;

    public PaintView(Context context) {
        super(context);
        init(context);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * CLAMP 会在端点之外延续端点处的颜色；MIRROR 是镜像模式；REPEAT 是重复模式
     * 在设置了 Shader 的情况下， Paint.setColor/ARGB() 所设置的颜色就不再起作用
     * <p>
     * 注意： 这段代码中我使用了两个 BitmapShader 来作为 ComposeShader() 的参数，
     * 而  ComposeShader() 在硬件加速下是不支持两个相同类型的 Shader 的，所以这里也需要关闭硬件加速才能看到效果。
     * <p>
     * PorterDuff.Mode.DST_OUT，就会变成挖空效果
     * PorterDuff.Mode.DST_IN，就会变成蒙版抠图效果
     * <p>
     * PorterDuff.Mode
     * Alpha 合成 (Alpha Compositing)
     * 混合 (Blending)
     * <p>
     * <p>
     * Paint.setColorFilter
     * R' = R * mul.R / 0xff + add.R
     * G' = G * mul.G / 0xff + add.G
     * B' = B * mul.B / 0xff + add.B
     * 一个「保持原样」的「基本 LightingColorFilter 」，mul 为 0xffffff，add 为 0x000000（也就是0），那么对于一个像素，它的计算过程就是：
     * <p>
     * R' = R * 0xff / 0xff + 0x0 = R // R' = R
     * G' = G * 0xff / 0xff + 0x0 = G // G' = G
     * B' = B * 0xff / 0xff + 0x0 = B // B' = B
     * 基于这个「基本 LightingColorFilter 」，你就可以修改一下做出其他的 filter。比如，如果你想去掉原像素中的红色，可以把它的 mul 改为 0x00ffff （红色部分为 0 ） ，那么它的计算过程就是：
     * <p>
     * R' = R * 0x0 / 0xff + 0x0 = 0 // 红色被移除
     * G' = G * 0xff / 0xff + 0x0 = G
     * B' = B * 0xff / 0xff + 0x0 = B
     */
    private void init(Context context) {
        int color1 = Color.parseColor("#E91E63");
        int color2 = Color.parseColor("#2196F3");
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Shader mLinerGradient = new LinearGradient(0, 0, 600, 600,
                color1, color2, Shader.TileMode.CLAMP);
        Shader mRadialGradient = new RadialGradient(300, 300, 100,
                color1, color2, Shader.TileMode.MIRROR);
        Shader mSweepGradient = new SweepGradient(300, 300, color1, color2);

//        Bitmap bitmap = scaleBitmap();
//        Shader mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        Shader mBitmapShader = new BitmapShader(BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_batman), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Shader mLogoShader = new BitmapShader(BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_logo), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//        Shader mComposeShader = new ComposeShader(mBitmapShader, mLogoShader, PorterDuff.Mode.SRC_OVER);
//        Shader mComposeShader = new ComposeShader(mBitmapShader, mLogoShader, PorterDuff.Mode.XOR);
//        Shader mComposeShader = new ComposeShader(mBitmapShader, mLogoShader, PorterDuff.Mode.DARKEN);
        Shader mComposeShader = new ComposeShader(mBitmapShader, mLogoShader, PorterDuff.Mode.OVERLAY);

//        mPaint.setShader(mLinerGradient);
//        mPaint.setShader(mRadialGradient);
//        mPaint.setShader(mSweepGradient);
//        mPaint.setShader(mBitmapShader);
        mPaint.setShader(mComposeShader);

        //去掉红色 mul 0x00ffff , add 0x000000; 增加绿色 add 0x000300
//        LightingColorFilter lightingColorFilter = new LightingColorFilter(0x00ffff, 0x000000);
        LightingColorFilter lightingColorFilter = new LightingColorFilter(0x00ffff, 0x003000);
        mPaint.setColorFilter(lightingColorFilter);

        PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(color1, PorterDuff.Mode.DST_IN);
        mPaint.setColorFilter(porterDuffColorFilter);


//        ColorMatrixColorFilter colorMatrixColorFilter=new ColorMatrixColorFilter();

        mXferMode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

//        使用虚线来绘制线条
//        第一个参数 intervals 是一个数组，它指定了虚线的格式：数组中元素必须为偶数（最少是 2 个），按照「画线长度、
//        空白长度、画线长度、空白长度」……的顺序排列，例如上面代码中的 20, 5, 10, 5
//        就表示虚线是按照「画 20 像素、空 5 像素、画 10 像素、空 5 像素」的模式来绘制；第二个参数 phase 是虚线的偏移量。
        pathEffect = new DashPathEffect(new float[]{10, 5}, 10);

//        把所有拐角变成圆角
//        PathEffect pathEffect = new CornerPathEffect(20);

//        DiscretePathEffect
//        把线条进行随机的偏离，让轮廓变得乱七八糟。乱七八糟的方式和程度由参数决定
//        DiscretePathEffect 具体的做法是，把绘制改为使用定长的线段来拼接，并且在拼接的时候对路径进行随机偏离
//        它的构造方法 DiscretePathEffect(float segmentLength, float deviation) 的两个参数中，
//        segmentLength 是用来拼接的每个线段的长度，deviation 是偏离量
//        这两个值设置得不一样，显示效果也会不一样，具体的你自己多试几次就明白了
//        PathEffect pathEffect = new DiscretePathEffect(20, 5);


//        Path dashPath = ...; // 使用一个三角形来做 dash
//        它的构造方法 PathDashPathEffect(Path shape, float advance, float phase, PathDashPathEffect.Style style) 中， shape 参数是用来绘制的 Path ； advance 是两个相邻的 shape 段之间的间隔，不过注意，这个间隔是两个 shape 段的起点的间隔，而不是前一个的终点和后一个的起点的距离； phase 和 DashPathEffect 中一样，是虚线的偏移；最后一个参数 style，是用来指定拐弯改变的时候 shape 的转换方式。style 的类型为 PathDashPathEffect.Style ，是一个 enum ，具体有三个值：
//        TRANSLATE：位移
//        ROTATE：旋转
//        MORPH：变体
//        PathEffect pathEffect = new PathDashPathEffect(dashPath, 40, 0,
//                PathDashPathEffectStyle.TRANSLATE);

//        SumPathEffect是一个组合效果类的 PathEffect 。它的行为特别简单，就是分别按照两种 PathEffect 分别对目标进行绘制
//         SumPathEffect(PathEffect first, PathEffect second)

//        ComposePathEffect
//        构造方法 ComposePathEffect(PathEffect outerpe, PathEffect innerpe) 中的两个 PathEffect 参数，
//        innerpe 是先应用的， outerpe 是后应用的
//        这也是一个组合效果类的 PathEffect 。不过它是先对目标 Path 使用一个 PathEffect，然后再对这个改变后的 Path 使用另一个 PathEffect。
//        PathEffect dashEffect = new DashPathEffect(new float[]{20, 10}, 0);
//        PathEffect discreteEffect = new DiscretePathEffect(20, 5);
//        pathEffect = new ComposePathEffect(dashEffect, discreteEffect);
//        PathEffect 在有些情况下不支持硬件加速，需要关闭硬件加速才能正常使用:
//        Canvas.drawLine() 和 Canvas.drawLines() 方法画直线时，setPathEffect() 是不支持硬件加速的；
//        PathDashPathEffect 对硬件加速的支持也有问题，所以当使用 PathDashPathEffect 的时候，最好也把硬件加速关了。


//        剩下的两个效果类方法：setShadowLayer() 和 setMaskFilter() ，
//        它们和前面的效果类方法有点不一样：它们设置的是「附加效果」，也就是基于在绘制内容的额外效果

//        setShadowLayer(float radius, float dx, float dy, int shadowColor)
//        在之后的绘制内容下面加一层阴影, 方法的参数里， radius 是阴影的模糊范围；
//        dx dy 是阴影的偏移量； shadowColor 是阴影的颜色。如果要清除阴影层，使用 clearShadowLayer()
//        注意：
//        在硬件加速开启的情况下， setShadowLayer() 只支持文字的绘制，文字之外的绘制必须关闭硬件加速才能正常绘制阴影。
//        如果 shadowColor 是半透明的，阴影的透明度就使用 shadowColor 自己的透明度；而如果 shadowColor 是不透明的，阴影的透明度就使用 paint 的透明度。


//        setMaskFilter(MaskFilter maskfilter)整个画面来进行过滤,
//        模糊效果 BlurMaskFilter 和 浮雕效果 EmbossMaskFilter
//        为之后的绘制设置 MaskFilter。上一个方法 setShadowLayer() 是设置的在绘制层下方的附加效果；而这个  MaskFilter 和它相反，设置的是在绘制层上方的附加效果


//        获取绘制的 Path
//        getFillPath() 和 getTextPath() ，就是获取绘制的 Path 的方法。之所以把它们归类到「效果」类方法，
//        是因为它们主要是用于图形和文字的装饰效果的位置计算
    }

    private Bitmap scaleBitmap() {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
//                R.mipmap.beauty, options);
//        options.inSampleSize = options.outWidth >= options.outHeight ?
//                options.outWidth / 300 : options.outHeight / 300;
//        options.inJustDecodeBounds = false;
//        bitmap=BitmapFactory.decodeResource(getResources(),
//                R.mipmap.beauty, options);

        Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.mipmap.beauty), 300, 300, true);
        return bitmap;
    }

    /**
     * Xfermode 注意事项
     * 1. 使用离屏缓冲（Off-screen Buffer）
     * 通过使用离屏缓冲，把要绘制的内容单独绘制在缓冲层，
     * Xfermode 的使用就不会出现奇怪的结果了。使用离屏缓冲有两种方式
     * Canvas.saveLayer()
     * mPaint.setXfermode(mXferMode);
     * 如果没有特殊需求，可以选用第一种方法 Canvas.saveLayer() 来设置离屏缓冲，
     * 以此来获得更高的性能。
     * <p>
     * 2. 控制好透明区域
     * 控制图片的透明区域,不要太小，要让它足够覆盖到要和它结合绘制的内容，否则得到的结果很可能不是你想要的
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(100, 100, 200, mPaint);

        mPaint.setShader(null);
        int count = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
//        setLayerType(LAYER_TYPE_SOFTWARE, mPaint);
        canvas.drawRect(400, 400, 600, 600, mPaint);

        // 设置 Xfermode
        mPaint.setXfermode(mXferMode);

        canvas.drawCircle(600, 600, 100, mPaint);

        // 用完及时清除 Xfermode
        mPaint.setXfermode(null);
        canvas.restoreToCount(count);


        mPaint.setStrokeWidth(10);
//        有三个值可以选择：MITER 尖角、 BEVEL 平角和 ROUND 圆角。默认为 MITER。
//        setStrokeJoin() 的一个补充，它用于设置 MITER 型拐角的延长线的最大值。所谓「延长线的最大值」，是这么一回事：
//        当线条拐角为 MITER 时，拐角处的外缘需要使用延长线来补偿：
        mPaint.setStrokeJoin(Paint.Join.MITER);
//        setStrokeMiter(miter) 方法中的 miter 参数。miter 参数是对于转角长度的限制，
//        具体来讲，是指尖角的外缘端点和内部拐角的距离与线条宽度的比。miter=a/b
//        这个 miter limit 的默认值是 4，对应的是一个大约 29° 的锐角：小于这个∠,Join 会变成Bevel
        mPaint.setStrokeMiter(3);
        canvas.drawText("7", 100, 800, mPaint);


//        色彩优化
//        有两个方法： setDither(boolean dither) 和 setFilterBitmap(boolean filter
//        因为现在的 Android 版本的绘制，默认的色彩深度已经是 32 位的 ARGB_8888 ，效果已经足够清晰了。
//        只有当你向自建的 Bitmap 中绘制，并且选择 16 位色的 ARGB_4444 或者 RGB_565 的时候，开启它才会有比较明显的效果
        mPaint.setDither(true);

//        setFilterBitmap(boolean filter) 设置是否使用双线性过滤来绘制 Bitmap 。
//        图像在放大绘制的时候，默认使用的是最近邻插值过滤，这种算法简单，但会出现马赛克现象；
//        而如果开启了双线性过滤，就可以让结果图像显得更加平滑。优化bitmap放大绘制
        mPaint.setFilterBitmap(true);


//        setPathEffect(PathEffect effect)
//        使用 PathEffect 来给图形的轮廓设置效果。对 Canvas 所有的图形绘制有效，
//        也就是 drawLine() drawCircle() drawPath() 这些方法。
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setPathEffect(pathEffect);
        canvas.drawCircle(200, 500, 100, mPaint);
    }
}
