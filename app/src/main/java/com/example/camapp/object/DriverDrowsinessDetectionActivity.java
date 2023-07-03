package com.example.camapp.object;

import android.os.Bundle;

import com.example.camapp.helpers.MLVideoHelperActivity;
import com.example.camapp.helpers.vision.drowsiness.FaceDrowsinessDetectorProcessor;
import com.example.camapp.helpers.vision.VisionBaseProcessor;

public class DriverDrowsinessDetectionActivity extends MLVideoHelperActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected VisionBaseProcessor setProcessor() {
        return new FaceDrowsinessDetectorProcessor(graphicOverlay);
    }
}
