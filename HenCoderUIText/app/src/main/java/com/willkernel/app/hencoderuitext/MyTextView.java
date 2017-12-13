package com.willkernel.app.hencoderuitext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Arrays;

public class MyTextView extends View {
    private String TAG = "MyTextView";
    private String text = "Hello Lady Gaga!MyTextView";
    private String longText1 = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";
    private String longText2 = "a\nbc\ndefghi\njklm\nnopqrst\nuvwx\nyz";
    private String[] textAry = new String[]{"hello", "mac", "中午"};
    private Paint mPaint;
    private Path mPath;
    private TextPaint mTextPaint;
    private StaticLayout staticLayout1, staticLayout2;
    private Rect mRectBounds;

    public MyTextView(Context context) {
        super(context);
        init();
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#aa3355"));
//        mPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"**.ttf"));
        mTextPaint = new TextPaint(mPaint);
        mPath = new Path();
        mPath.moveTo(200, 200);
        mPath.lineTo(250, 300);
        mPath.lineTo(300, 250);
        mPath.lineTo(400, 300);
        mPath.lineTo(450, 280);
        //拐角变圆角
        PathEffect pathEffect = new CornerPathEffect(10);
        mPaint.setPathEffect(pathEffect);


//        width 是文字区域的宽度，文字到达这个宽度后就会自动换行
//        align 是文字的对齐方向
//        spacingmult 是行间距的倍数，通常情况下填 1 就好
//        spacingadd 是行间距的额外增加值，通常情况下填 0 就好
//        includeadd 是指是否在文字上下添加额外的空间，来避免某些过高的字符的绘制出现越界
        mTextPaint.setTextSize(30);
        mTextPaint.setTypeface(Typeface.SERIF);
//        是否加下划线
        mTextPaint.setUnderlineText(true);
//        设置文字横向放缩
        mTextPaint.setTextScaleX(1.2f);
        staticLayout1 = new StaticLayout(longText1, mTextPaint, 300,
                Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
        staticLayout2 = new StaticLayout(longText2, mTextPaint, 300,
                Layout.Alignment.ALIGN_CENTER, 1, 0, true);


        mRectBounds = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setTextSize(30);
//        设置文字横向错切角度,其实就是文字倾斜度
        mPaint.setTextSkewX(-0.5f);
//        是否使用伪粗体   它并不是通过选用更高 weight 的字体让文字变粗，而是通过程序在运行时把文字给「描粗」了
        mPaint.setFakeBoldText(false);
//        设置字符间距,默认值是 0
        mPaint.setLetterSpacing(0.2f);
//        用 CSS 的 font-feature-settings 的方式来设置文字, 设置 "small caps"

//        是否加删除线
        mPaint.setStrikeThruText(true);
        mPaint.setFontFeatureSettings("smcp");
        canvas.drawText(text, 200, 50, mPaint);
        mPaint.setTextSize(20);
        canvas.drawTextRun("عربى", 1, 3, 0, 3,
                200, 100, false, mPaint);

        canvas.drawPath(mPath, mPaint);
//        hOffset 和 vOffset。它们是文字相对于 Path 的水平偏移量和竖直偏移量
        mPaint.setTextSize(24);
        canvas.drawTextOnPath(text, mPath, 5, 10, mPaint);

        mPaint.setStrikeThruText(false);
        mPaint.setFakeBoldText(true);
//        Canvas.drawText() 只能绘制单行的文字，不能在 View 的边缘自动折行
        canvas.drawText(longText1, 400, 500, mPaint);
        canvas.drawText(longText2, 400, 550, mPaint);


//        如果需要绘制多行的文字，你必须自行把文字切断后分多次使用 drawText() 来绘制，或者——使用  StaticLayout
//        StaticLayout 并不是一个 View 或者 ViewGroup ，而是 android.text.Layout 的子类，它是纯粹用来绘制文字的
//        StaticLayout 支持换行，它既可以为文字设置宽度上限来让文字自动换行，也会在 \n 处主动换行
        canvas.save();
        canvas.translate(50, 400);
        staticLayout1.draw(canvas);
        canvas.translate(0, 200);
        staticLayout2.draw(canvas);
        canvas.restore();


//        Paint 对文字绘制的辅助，有两类方法：设置显示效果的和测量文字尺寸的
//        setTextSize,setTypeface,setFakeBoldText,setStrikeThruText,setUnderlineText,setTextSkewX,
//        setTextScaleX,setLetterSpacing,setFontFeatureSettings,setTextAlign

//        setTextLocale(Locale locale) / setTextLocales(LocaleList locales)
//        由于 Android 7.0 ( API v24) 加入了多语言区域的支持，所以在 API v24 以及更高版本上，
//        还可以使用 setTextLocales(LocaleList locales) 来为绘制设置多个语言区域。

//        setHinting 设置是否启用字体的 hinting 字体微调
//        Android 设备大多数都是是用的矢量字体通过向字体中加入 hinting 信息，
//        让矢量字体在尺寸过小的时候得到针对性的修正，从而提高显示效果  基本上没有必要使用

//        setElegantTextHeight 压缩某些语言的字符高度
//        开发者会需要使用它们的原始（优雅）版本 使用 setElegantTextHeight() 就可以切换到原始版本：
//        paint.setElegantTextHeight(true);

//        setSubpixelText(boolean subpixelText)
//        是否开启次像素级的抗锯齿（ sub-pixel anti-aliasing ）基本上没有必要使用


        /*  测量文字尺寸类*/
//        获取推荐的行距
//        即推荐的两行文字的 baseline 的距离
//        float getFontSpacing()
        canvas.drawText(textAry[0], 300, 600, mPaint);
        canvas.drawText(textAry[1], 300, 600 + mPaint.getFontSpacing(), mPaint);
        canvas.drawText(textAry[2], 300, 600 + mPaint.getFontSpacing() * 2, mPaint);


//        FontMetrics getFontMetrics()
//        FontMetrics 提供的就是 Paint 根据当前字体和字号，得出的这些值的推荐值
//        它把这些值以变量的形式存储，供开发者需要时使用
//        文字排印方面的数值：ascent, descent, top, bottom,  leading
//        ------ top      -25
//        ------ ascent   -20
//        ------ baseline 0
//        ------ descent  10
//        ------ bottom   12

//        ascent / descent: 它们的作用是限制普通字符的顶部和底部范围,普通的字符，上不会高过 ascent ，下不会低过 descent
//        ascent 和 descent 这两个值还可以通过 Paint.ascent() 和 Paint.descent() 来快捷获取

//        top / bottom: 它们的作用是限制所有字形（ glyph ）的顶部和底部范围
//        除了普通字符，有些字形的显示范围是会超过 ascent 和 descent 的
//        而 top 和 bottom 则限制的是所有字形的显示范围，包括这些特殊字形

//        leading 即对于上下相邻的两行，上行的 bottom 线和下行的 top 线的距离
//        这个词的本意其实并不是行的额外间距，而是行距，即两个相邻行的 baseline 之间的距离
//        不过对于很多非专业领域，leading 的意思被改变了，被大家当做行的额外间距来用
//        而 Android 里的  leading ，同样也是行的额外间距的意思

//        两行文字的 font spacing (即相邻两行的 baseline 的距离) 可以通过  bottom - top + leading (top 的值为负)
//        bottom - top + leading 的结果是要大于 getFontSpacing() 的返回值的
//        getFontSpacing() 的结果并不是通过 FontMetrics 的标准值计算出来的，而是另外计算出来的一个值，
//        它能够做到在两行文字不显得拥挤的前提下缩短行距，以此来得到更好的显示效果。所以如果你要对文字手动换行绘制，
//        多数时候应该选取 getFontSpacing() 来得到行距，不但使用更简单，显示效果也会更好
//        getFontMetrics() 的返回值是 FontMetrics 类型。它还有一个重载方法
//        getFontMetrics(FontMetrics fontMetrics) ，计算结果会直接填进传入的 FontMetrics 对象，
//        而不是重新创建一个对象。这种用法在需要频繁获取 FontMetrics 的时候性能会好些

        mPaint.setPathEffect(null);
//        获取文字的显示范围
//        getTextBounds(String text, int start, int end, Rect bounds)
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, 300, 700, mPaint);

        mPaint.getTextBounds(text, 0, text.length(), mRectBounds);
        mRectBounds.top += 700;
        mRectBounds.bottom += 700;
        mRectBounds.left += 300;
        mRectBounds.right += 300;
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(mRectBounds, mPaint);


//        float measureText(String text) 测量文字的宽度并返回
        float mTextWidth = mPaint.measureText(text);
        canvas.drawLine(300, 710, 300 + mTextWidth, 710, mPaint);
//        如果你用代码分别使用 getTextBounds() 和 measureText() 来测量文字的宽度，你会发现 measureText()


//        测出来的宽度总是比 getTextBounds() 大一点点。这是因为这两个方法其实测量的是两个不一样的东西
//        getTextBounds: 它测量的是文字的显示范围（关键词：显示）
//        形象点来说，你这段文字外放置一个可变的矩形，
//        然后把矩形尽可能地缩小，一直小到这个矩形恰好紧紧包裹住文字，那么这个矩形的范围，就是这段文字的 bounds


//        measureText(): 它测量的是文字绘制时所占用的宽度（关键词：占用）
//        前面已经讲过，一个文字在界面中，往往需要占用比他的实际显示宽度更多一点的宽度，
//        以此来让文字和文字之间保留一些间距，不会显得过于拥挤。
//        上面的这幅图，我并没有设置 setLetterSpacing() ，这里的 letter spacing 是默认值 0，但你可以看到，
//        图中每两个字母之间都是有空隙的。另外，下方那条用于表示文字宽度的横线，在左边超出了第一个字母 H 一段距离的，
//        在右边也超出了最后一个字母 r（虽然右边这里用肉眼不太容易分辨），而就是两边的这两个「超出」，
//        导致了 measureText() 比 getTextBounds() 测量出的宽度要大一些


//        getTextWidths(String text, float[] widths)   获取字符串中每个字符的宽度，并把结果填入参数 widths


//        int breakText(String text, boolean measureForwards, float maxWidth, float[] measuredWidth)
//        这个方法也是用来测量文字宽度的。但和 measureText() 的区别是， breakText() 是在给出宽度上限的前提下
//        测量文字的宽度。如果文字的宽度超出了上限，那么在临近超限的位置截断文字


//        breakText() 的返回值是截取的文字个数（如果宽度没有超限，则是文字的总个数）。参数中， text 是要测量的文字
//        measureForwards 表示文字的测量方向，true 表示由左往右测量；maxWidth 是给出的宽度上限；measuredWidth
//        是用于接受数据，而不是用于提供数据的：方法测量完成后会把截取的文字宽度（如果宽度没有超限，则为文字总宽度）
//        赋值给 measuredWidth[0]

        float[] measureWidth = {0};
        int measureCount = mPaint.breakText(text, 0, text.length(), true,
                300, measureWidth);
        canvas.drawText(text, 0, measureCount, 300, 750, mPaint);
        Log.e(TAG, "measureCount 1 " + measureCount + "  measureWidth=" + Arrays.toString(measureWidth));

        measureCount = mPaint.breakText(text, 0, text.length(), true,
                400, measureWidth);
        canvas.drawText(text, 0, measureCount, 300, 750 + mPaint.getFontSpacing(), mPaint);
        Log.e(TAG, "measureCount " + measureCount + "  measureWidth=" + Arrays.toString(measureWidth));

        measureCount = mPaint.breakText(text, 0, text.length(), true,
                500, measureWidth);
        canvas.drawText(text, 0, measureCount, 300, 750 + 2 * mPaint.getFontSpacing(), mPaint);
        Log.e(TAG, "measureCount " + measureCount + "  measureWidth=" + Arrays.toString(measureWidth));

        measureCount = mPaint.breakText(text, 0, text.length(), true,
                600, measureWidth);
        canvas.drawText(text, 0, measureCount, 300, 750 + 3 * mPaint.getFontSpacing(), mPaint);
        Log.e(TAG, "measureCount 4 " + measureCount + "  measureWidth=" + Arrays.toString(measureWidth));


//        光标相关
//        API23: getRunAdvance(CharSequence text, int start, int end, int contextStart, int contextEnd,
//        boolean isRtl, int offset)
//        对于一段文字，计算出某个字符处光标的 x 坐标。 start end 是文字的起始和结束坐标；
//        contextStart contextEnd 是上下文的起始和结束坐标；isRtl 是文字的方向；
//        offset 是字数的偏移，即计算第几个字符处的光标

        // 包含特殊符号的绘制（如 emoji 表情）
        String emojiText = "Hello China \uD83C\uDDE8\uD83C\uDDF3"; // "Hello HenCoder 🇨🇳"
//        🇨🇳 虽然占了 4 个字符（\uD83C\uDDE8\uD83C\uDDF3），但当 offset 是表情中间处时，
//        getRunAdvance() 得出的结果并不会在表情的中间处
//        为什么？因为这是用来计算光标的方法啊，光标当然不能出现在符号中间
        int length = emojiText.length();
        float advance1 = mPaint.getRunAdvance(emojiText, 0, length, 0, length,
                false, length);
        float advance2 = mPaint.getRunAdvance(emojiText, 0, length, 0, length,
                false, length - 1);
        float advance3 = mPaint.getRunAdvance(emojiText, 0, length, 0, length,
                false, length - 2);
        float advance4 = mPaint.getRunAdvance(emojiText, 0, length, 0, length,
                false, length - 3);
        float advance5 = mPaint.getRunAdvance(emojiText, 0, length, 0, length,
                false, length - 4);
        canvas.drawText(emojiText, 100, 900, mPaint);
        Log.e(TAG, "advance1=" + advance1 + " advance2=" + advance2 + " advance3=" + advance3 +
                " advance4=" + advance4 + " advance5=" + advance5);

        int offset = mPaint.getOffsetForAdvance(emojiText, 0, length, 0, length,
                false, 150);
        Log.e(TAG, "offset=" + offset + " text=" + emojiText.charAt(offset));


//        hasGlyph(String string) 检查指定的字符串中是否是一个单独的字形 (glyph）
        Log.e(TAG,"a is glyph "+mPaint.hasGlyph("a"));
        Log.e(TAG,"ab is glyph "+mPaint.hasGlyph("ab"));
        Log.e(TAG,"\uD83C\uDDE8\uD83C\uDDF3 is glyph "+
                mPaint.hasGlyph("\uD83C\uDDE8\uD83C\uDDF3"));

    }
}
