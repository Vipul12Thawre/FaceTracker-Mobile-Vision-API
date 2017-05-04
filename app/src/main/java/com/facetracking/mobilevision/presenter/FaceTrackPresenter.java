package com.facetracking.mobilevision.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.SparseArray;

import com.facetracking.mobilevision.R;
import com.facetracking.mobilevision.contracts.FaceTrackContract;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

/**
 * Created by RAHUL on 04-05-2017.
 */

public class FaceTrackPresenter {
    private final Context context;
    private FaceTrackContract.DetectFaceListener detectFaceListener;
    private FaceTrackContract.ChooseImageListener chooseImageListener;
    private Paint myRectPaint, circleBursh;



    public FaceTrackPresenter(Context context, FaceTrackContract.DetectFaceListener detectFaceListener) {
        this.context = context;
        this.detectFaceListener = detectFaceListener;
    }

    public void detectFaces(Bitmap bitmap) {
        detectFacesInImage(bitmap);
    }

    private void detectFacesInImage(Bitmap bitmap) {

        //Create a Paint object for drawing with
        myRectPaint = new Paint();
        myRectPaint.setStrokeWidth(10);
        myRectPaint.setColor(context.getResources().getColor(R.color.colorAccent));
        myRectPaint.setStyle(Paint.Style.STROKE);

        circleBursh = new Paint();
        circleBursh.setStrokeWidth(3);
        circleBursh.setColor(context.getResources().getColor(R.color.colorAccent));
        circleBursh.setStyle(Paint.Style.STROKE);
        //Create a Canvas object for drawing on
        Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(bitmap, 0, 0, null);

        //Detect the Faces
        FaceDetector faceDetector = new FaceDetector.Builder(context)
                .setTrackingEnabled(true).setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();

        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Face> faces = faceDetector.detect(frame);

        if (faces.size() == 0) {
            detectFaceListener.onFaceDetectError(context.getResources().getString(R.string.no_face_detect));

        } else {

            //Draw facial features on the Faces
            for (int i = 0; i < faces.size(); i++) {
                Face thisFace = faces.valueAt(i);
                for (Landmark landmark : thisFace.getLandmarks()) {
                    int cx = (int) (landmark.getPosition().x);
                    int cy = (int) (landmark.getPosition().y);
                    tempCanvas.drawCircle(cx, cy, 3, circleBursh);
                }

                //Draw Rectangles on the Faces
                float x1 = thisFace.getPosition().x;
                float y1 = thisFace.getPosition().y;
                float x2 = x1 + thisFace.getWidth();
                float y2 = y1 + thisFace.getHeight();
                tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myRectPaint);
            }
            detectFaceListener.onFaceDetectSuccess(new BitmapDrawable(context.getResources(), tempBitmap));

        }
    }
}
