package com.willkernel.app.hencoderanimator;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by willkernel on 2017/12/13.
 */

public class PonitFEValuator implements TypeEvaluator<PointF> {
    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        PointF newPointF = new PointF();
        float x = startValue.x + (endValue.x - startValue.x) * fraction;
        float y = startValue.y + (endValue.y - startValue.y) * fraction;
        newPointF.set(x, y);

        return newPointF;
    }
}
