package ca.uwaterloo.cs349.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import ca.uwaterloo.cs349.GestureItem;


public class GestureView extends View {
    private final Path path = new Path();
    private final Paint paint = new Paint();

    public GestureView(Context context) {
        this(context, null);
    }

    public GestureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setStrokeWidth(8f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    public void setPath(GestureItem gestureItem) {
        path.reset();
        ArrayList<PointF> originalShapePathPointArray = gestureItem.getOriginalShapePathPointArray();
        PointF firstPt = originalShapePathPointArray.get(0);
        path.moveTo(firstPt.x, firstPt.y);

        for (PointF p : originalShapePathPointArray)
            path.lineTo(p.x, p.y);


        Matrix mMatrix = new Matrix();
        RectF bounds = new RectF();
        path.computeBounds(bounds, true);
        float cx = bounds.centerX();
        float cy = bounds.centerY();
        int w = getLayoutParams().width / 2;
        int h = getLayoutParams().height / 2;
        mMatrix.postScale(w / cx, h / cy);

        path.transform(mMatrix);
        postInvalidate();
    }
}