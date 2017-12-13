package com.willkernel.app.hencoderlayout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;


public class AdjustablePanel extends LinearLayout {
    FrameLayout frameLayout;
    AppCompatSeekBar heightBar, widthBar;

    public AdjustablePanel(Context context) {
        super(context);
    }

    public AdjustablePanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AdjustablePanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float minWidth = 150;
    private float minHeight = 200;
    private float bottomMargin = 46;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        frameLayout = findViewById(R.id.frameLayout);
        widthBar = findViewById(R.id.widthBar);
        heightBar = findViewById(R.id.heightBar);
        AppCompatSeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LinearLayout.LayoutParams layoutParam = (LayoutParams) frameLayout.getLayoutParams();
                layoutParam.width = (int) (minWidth + ((getWidth() - minWidth) * widthBar.getProgress()) / 100);
                layoutParam.height = (int) (minHeight + ((getHeight() - minHeight - bottomMargin) * heightBar.getProgress()) / 100);
                frameLayout.setLayoutParams(layoutParam);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        widthBar.setOnSeekBarChangeListener(listener);
        heightBar.setOnSeekBarChangeListener(listener);
    }

}
