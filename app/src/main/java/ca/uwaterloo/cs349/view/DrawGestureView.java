package ca.uwaterloo.cs349.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;

public class DrawGestureView extends View {
    private Path path = new Path();
    private final Paint paint = new Paint();
    private final ArrayList<PointF> pointList = new ArrayList<>();

    public DrawGestureView(Context context) {
        this(context, null);
    }

    public DrawGestureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setStrokeWidth(8f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setAntiAlias(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        PointF point = new PointF(x, y);

        if (action == MotionEvent.ACTION_DOWN) {
            if (touchListener != null) touchListener.start();
            clear();
            path.moveTo(x, y);
            pointList.add(point);
            return true;
        } else if (action == MotionEvent.ACTION_MOVE) {
            path.lineTo(x, y);
            pointList.add(point);
        } else if (action == MotionEvent.ACTION_UP) {
            if (touchListener != null) touchListener.end(pointList);
        }
        postInvalidate();
        return true;
    }


    public void clear() {
        path.reset();
        pointList.clear();
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    public double getAngle(PointF firstPoint, int x, int y) {
        double inRadians = Math.atan2(-(firstPoint.y - y), firstPoint.x - x);
        return Math.toDegrees(inRadians < 0 ? (Math.abs(inRadians)) : (2 * Math.PI - inRadians));
    }

    public ArrayList<PointF> getOriginalShape() {
        Path tempPath = new Path();
        tempPath.addPath(path);

        Matrix m = new Matrix();
        RectF rectf = new RectF();
        path.computeBounds(rectf, true);

        float cx = rectf.centerX();
        float cy = rectf.centerY();

        m.postScale(100 / cx, 100 / cy);

        path.transform(m);
        ArrayList<PointF> points = getPoints();
        path = tempPath;
        postInvalidate();
        return points;
    }

    public ArrayList<PointF> getNPoints() {
        Path tempPath = new Path();
        tempPath.addPath(path);

        Matrix m = new Matrix();
        RectF rectf = new RectF();
        path.computeBounds(rectf, true);

        float cx = rectf.centerX();
        float cy = rectf.centerY();

        double rotateAngle = getAngle(pointList.get(0), (int) cx, (int) cy);
        m.postRotate((float) -rotateAngle, cx, cy);
        m.postTranslate(-cx, -cy);
        m.postScale(200 / cx, 200 / cy);

        path.transform(m);
        ArrayList<PointF> points = getPoints();
        path = tempPath;
        postInvalidate();
        return points;
    }

    int N = 128;
    private ArrayList<PointF> getPoints() {
        PathMeasure pathMeasure = new PathMeasure(path, false);
        float totalDistanceOfAPath = getLength(pathMeasure);
        final float interval = totalDistanceOfAPath / N;

        ArrayList<PointF> linearEquidistantSample = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            PointF pf = new PointF();
            float[] xy = new float[2];
            pathMeasure.getPosTan(i * interval, xy, null);
            pf.x = xy[0];
            pf.y = xy[1];
            linearEquidistantSample.add(pf);
        }

        return linearEquidistantSample;
    }

    private float getLength(PathMeasure pm) {
//        float totalDistanceOfAPath = 0;
//        for (int i = 0; i < pointList.size() - 1; i++) {
//            try {
//                PointF p1 = pointList.get(i);
//                PointF p2 = pointList.get(i + 1);
//                totalDistanceOfAPath += Math.sqrt((Math.pow((p2.x - p1.x), 2) + Math.pow((p2.y - p1.y), 2)));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return totalDistanceOfAPath;
        return pm.getLength();
    }

    public boolean isValidGesture() {
        return pointList.size() >= 10;
    }

    public interface IStrokeListener {
        void start();

        void end(ArrayList<PointF> xyList);
    }

    private IStrokeListener touchListener;

    public void setTouchEventListener(IStrokeListener mTouchListener) {
        this.touchListener = mTouchListener;
    }
}