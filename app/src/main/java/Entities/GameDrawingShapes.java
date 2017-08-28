package Entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by Admin on 31/05/2017.
 */

public class GameDrawingShapes {
    private String Tag = getClass().getSimpleName();
    private int size = 300;
    private int x = size / 2;
    private int y = size / 2;
    private Paint paint;
    private Bitmap bitmap;
    private Canvas canvas;
    private Context mContext;

    public GameDrawingShapes(Context context) {
        this.mContext = context;
        paint = new Paint();
    }

    public Drawable Draw(int colorResID, ShapesQuestion gameShape) {
        bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint.setColor(colorResID);
        paint.setStyle(Paint.Style.FILL);
        Log.d(Tag, "game shape = " + gameShape);
        switch (gameShape) {
            case CIRCLE:
                canvas.drawCircle(x, y, size / 2, paint);
                break;
            case RHOMBUS:
                canvas.drawPath(RhombusPathShape(), paint);
                break;
            case RECTANGLE:
                canvas.drawPath(RectanglePathShape(), paint);
                break;
            case SQUARE:
                canvas.drawPath(SquarePathShape(), paint);
                break;
            case TRIANGLE:
                canvas.drawPath(TrianglesPathShape(), paint);
                break;
            default:
                return null;
        }
        Log.d(Tag, "Canvas");
        return new BitmapDrawable(mContext.getResources(),bitmap);
    }

    private Path RhombusPathShape() {
        int halfWidth = size / 2;
        Path path = new Path();
        path.moveTo(x, y + halfWidth); // Top
        path.lineTo(x - halfWidth, y); // Left
        path.lineTo(x, y - halfWidth); // Bottom
        path.lineTo(x + halfWidth, y); // Right
        path.lineTo(x, y + halfWidth); // Back to Top
        path.close();
        return path;
    }


    private Path TrianglesPathShape() {
        float halfSize = size / 2;
        Path path = new Path();
        path.moveTo(x, y - halfSize); // Top
        path.lineTo(x - halfSize, y + halfSize); // Bottom left
        path.lineTo(x + halfSize, y + halfSize); // Bottom right
        path.lineTo(x, y - halfSize); // Back to Top
        path.close();
        return path;
    }

    private Path SquarePathShape() {
        Path path = new Path();
        path.moveTo(0, 0); // Top
        path.lineTo(size, 0); // Bottom left
        path.lineTo(size, size); // Bottom right
        path.lineTo(0, size); // Back to Top
        path.close();
        return path;
    }

    private Path RectanglePathShape() {
        float halfSize = size / 2;
        Path path = new Path();
        path.moveTo(0, halfSize - x / 2); // Top
        path.lineTo(size, halfSize - x / 2); // Top right
        path.lineTo(size, size - x / 2); // Bottom right
        path.lineTo(0, size - x / 2); // Back to Top
        path.close();
        return path;
    }


}
