package com.alabarra.gui.components;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.alabarra.R;
import com.alabarra.gui.components.wave.WaveView;

/**
 * Created by rodrigoarias on 8/3/17.
 */

public class CupProgressView extends FrameLayout {

    public CupProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        View view = LayoutInflater.from(context).inflate(R.layout.cup_progress_view, this);

        WaveView waveView = (WaveView) view.findViewById(R.id.wave_view);
        if (waveView != null) {
            initAnimation(waveView);
        }

    }

    private void initAnimation(View view) {
        WaveView waveView = (WaveView) view.findViewById(R.id.wave_view);
        if (waveView != null) {
            int accent = ResourcesCompat.getColor(getResources(), R.color.colorAccent, null);
            int accentDark = ResourcesCompat.getColor(getResources(), R.color.colorAccentDark, null);
            waveView.setWaveColor(accentDark, accent);
            waveView.setWaterLevelRatio(0.8f);
            waveView.setShowWave(true);
            waveView.setShapeType(WaveView.ShapeType.CUSTOM);

            // horizontal animation.
            ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(
                    waveView, "waveShiftRatio", 0f, 1f);
            waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
            waveShiftAnim.setDuration(1000);
            waveShiftAnim.setInterpolator(new LinearInterpolator());
            waveShiftAnim.start();
        }
    }

}
