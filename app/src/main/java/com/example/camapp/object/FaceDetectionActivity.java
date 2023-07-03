package com.example.camapp.object;

import android.annotation.SuppressLint;
import  android.graphics.Bitmap;
import android.os.Bundle;

import com.example.camapp.helpers.BoxWithText;
import com.example.camapp.helpers.MLImageHelperActivity;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.ArrayList;
import java.util.List;

public class FaceDetectionActivity extends MLImageHelperActivity {

    private FaceDetector faceDetector;
    private float focalLength;


    private float calculateDistance(Face face, float focalLength) {
        // Calculate the distance of the face from the camera in centimeters
        float distance;
        return distance = (face.getBoundingBox().width() * focalLength) / 2.0f;
    }

    private float getCameraFocalLength() {
        // Get the focal length of the camera in millimeters
        float focalLengthInMM = 100.0f;
        return focalLengthInMM / 1000.0f; // Convert to centimeters
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // High-accuracy landmark detection and face classification
        FaceDetectorOptions highAccuracyOpts =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .enableTracking()
                        .build();

        faceDetector = FaceDetection.getClient(highAccuracyOpts);
        focalLength = getCameraFocalLength();
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    protected void runDetection(Bitmap bitmap) {
        Bitmap finalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        InputImage image = InputImage.fromBitmap(finalBitmap, 0);
        faceDetector.process(image)
                .addOnFailureListener(Throwable::printStackTrace)
                .addOnSuccessListener(faces -> {
                    if (faces.isEmpty()) {
                        getOutputTextView().setText("No faces detected");
                    } else {
                        getOutputTextView().setText(String.format("%d faces detected", faces.size()));
                        List<BoxWithText> boxes = new ArrayList<>();
                        for (Face face : faces) {
//                            boxes.add(new BoxWithText(face.getTrackingId() + "", face.getBoundingBox()));
                            // Calculate the distance of the face from the camera
                            float distance = calculateDistance(face, focalLength);
                            String text = String.format("Dis: %.2f cm", distance);
                            boxes.add(new BoxWithText(text, face.getBoundingBox()));
                        }
                        getInputImageView().setImageBitmap(drawDetectionResult(finalBitmap, boxes));
                    }
                });
    }

}
