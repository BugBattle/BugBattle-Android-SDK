package bugbattle.io.bugbattle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.Date;

 class ScreenshotTaker {
    private Activity activity;
    public ScreenshotTaker(Activity currActivity) {
        activity = currActivity;
        service = FeedbackService.getInstance();

    }
    private FeedbackService service;

    public void takeScreenshot() throws Exception {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyyMMddhhmmss", now);
        // image naming and path  to include sd card  appending name you choose for file
        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

        // create bitmap screen capture
        View v1 = activity.getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);
        bitmap = getResizedBitmap(bitmap);
        openScreenshot(bitmap);
    }
    public void openScreenshot(Bitmap imageFile) {
        Intent intent = new Intent(service.getMainActivity(), ImageEditor.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        service.setImage(imageFile);
        service.getMainActivity().startActivity(intent);
    }

    private Bitmap getResizedBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        int orientation = service.getMainActivity().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            matrix.postScale(0.7f, 0.7f);
        } else {
            matrix.postScale(0.5f, 0.5f);
        }


        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }



}
