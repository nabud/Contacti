package app.contacts.com.contacts.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;

import app.contacts.com.contacts.R;
import app.contacts.com.contacts.models.Contact;

public class ImageUtil {

    /**
     * Round out an image bitmap
     * @param bitmap
     * @return
     */
    public static Bitmap GetBitmapClippedCircle(Bitmap bitmap) {

        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        final Path path = new Path();
        path.addCircle(
                (float)(width / 2)
                , (float)(height / 2)
                , (float) Math.min(width, (height / 2))
                , Path.Direction.CCW);

        final Canvas canvas = new Canvas(outputBitmap);
        canvas.clipPath(path);
        canvas.drawBitmap(bitmap, 0, 0, null);
        return outputBitmap;
    }

    public static Bitmap getBitmapFromRes(Context context, int res){
        return BitmapFactory.decodeResource(context.getResources(), res);
    }

    public static Bitmap getCircleBitmapFromRes(Context context, int res){
        return GetBitmapClippedCircle(getBitmapFromRes(context, res));
    }
}
