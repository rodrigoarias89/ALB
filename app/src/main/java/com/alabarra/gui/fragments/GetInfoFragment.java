package com.alabarra.gui.fragments;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.alabarra.R;
import com.alabarra.gui.components.wave.WaveView;
import com.alabarra.gui.listeners.LocationInteractionListener;

/**
 * Created by rodrigoarias on 7/11/17.
 */

public class GetInfoFragment extends Fragment {

    public final static String TAG = "GetInfoFragment";
    public final static String GO_TO_MAP_ARG = "GO_TO_MAP_ARG";

    private LocationInteractionListener mListener;

    private boolean mGoToMap;


    //For API pre 23
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        super.onAttach(activity);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            initFragment(activity);
        }
    }

    //Not called in API pre 23
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initFragment(context);
    }

    private void initFragment(Context context) {
        mListener = (LocationInteractionListener) context;

        if (getArguments() != null) {
            mGoToMap = getArguments().getBoolean(GO_TO_MAP_ARG, false);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        initAnimation(view);
        mListener.getLocation(mGoToMap);
    }

    private void initAnimation(View view) {
        WaveView waveView = (WaveView) view.findViewById(R.id.wave_view);
        if (waveView != null) {
            int accent = ResourcesCompat.getColor(getResources(), R.color.colorAccent, null);
            int accentDark = ResourcesCompat.getColor(getResources(), R.color.colorAccentDark, null);
            waveView.setWaveColor(accentDark, accent);
            waveView.setWaterLevelRatio(0.8f);
            waveView.setShowWave(true);
            waveView.setShapeType(WaveView.ShapeType.SQUARE);

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
