package com.facetracking.mobilevision.contracts;

import android.graphics.drawable.BitmapDrawable;

/**
 * Created by RAHUL on 04-05-2017.
 */

public class FaceTrackContract {

    public interface ChooseImageListener{

    }
    public interface DetectFaceListener {
        void onFaceDetectError(String error);

        void onFaceDetectSuccess(BitmapDrawable resultBitmap);
    }
}
