package ca.uwaterloo.cs349;

import android.graphics.PointF;
import java.util.ArrayList;

public class GestureItem implements Comparable<GestureItem> {
    private String name;
    private String path;
    private String originalPath;
    private Float di;

    public GestureItem(String name, String path, String originalPath, Float di) {
        this.name = name;
        this.path = path;
        this.originalPath = originalPath;
        this.di = di;
    }

    public String getName() {
        return name;
    }

    public Float getDi() {
        return di;
    }

    public void setDi(Float di) {
        this.di = di;
    }

    public static String getPathString(ArrayList<PointF> pointArr) {
        StringBuilder sb = new StringBuilder();
        for (PointF p : pointArr) {
            sb.append("#").append(p.x).append(",").append(p.y);
        }
        return sb.substring(1);
    }

    public ArrayList<PointF> getPathPointArray() {
        String[] xys = path.split("#");
        ArrayList<PointF> tP = new ArrayList<>(128);
        for (int i = 0; i < xys.length; i++) {
            String[] xy = xys[i].split(",");
            tP.add(new PointF(parse(xy[0]), parse(xy[1])));
        }
        return tP;
    }
    public ArrayList<PointF> getOriginalShapePathPointArray() {
        String[] xys = originalPath.split("#");
        ArrayList<PointF> tP = new ArrayList<>(128);
        for (int i = 0; i < xys.length; i++) {
            String[] xy = xys[i].split(",");
            tP.add(new PointF(parse(xy[0]), parse(xy[1])));
        }
        return tP;
    }

    private float parse(String s) {
        return Float.parseFloat(s);
    }

    @Override
    public int compareTo(GestureItem gestureItem) {
        return this.di.compareTo(gestureItem.di);
    }
}
