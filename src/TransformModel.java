
import java.util.ArrayList;

public class TransformModel {
    /**
     * 一个用于坐标转化，储存大量的常量的类
     * 作为计算以及显示的交接
     */
    static final int MAX_WINDOW_WIDTH = 600;
    static final int MAX_WINDOW_HEIGHT = 720;
    static final int ORI_POINT_X = (int) ((MAX_WINDOW_WIDTH / 2)+CalculateModel.BLANK);
    static final int ORI_POINT_Y = (int) ((MAX_WINDOW_HEIGHT / 2)+CalculateModel.BLANK);
    static final int OP_POS_X = 0;
    static final int OP_POS_Y = 0;
    static final int OP_WIDTH = 380;
    static final int OP_HEIGHT = 330;
    static final int SP_WIDTH = 380;
    static final int SP_HEIGHT = 170;
    static final int SP_POS_X = 0;
    static final int SP_POS_Y = 330;
    static final int IP_WIDTH = 380;
    static final int IP_HEIGHT = 220;
    static final int IP_POS_X = 0;
    static final int IP_POS_Y = 500;
    static final int GP_WIDTH = 600;
    static final int GP_HEIGHT = 720;
    static final int GP_POS_X = 380;
    static final int GP_POS_Y = 0;
    static final double ScreenRatio = 0.9;

    double stretchRatio;
    double[] dorm_R, range_R;
    int[] dorm_P, range_P;

    public TransformModel() {}

    public TransformModel(double[] dorm, double[] range) {
        this.dorm_R = dorm;
        this.range_R = range;
        setStretchRatios();
        translateDormAndRangeToPixel();
    }

    public void setDormAndRange(double[] dorm, double[] range) {
        this.dorm_R = dorm;
        this.range_R = range;
        setStretchRatios();
        translateDormAndRangeToPixel();
    }

    private void setStretchRatios() {
        double length = Math.max(Math.abs(dorm_R[1]-dorm_R[0]),Math.abs(range_R[0]-range_R[1]));
        double ratio1 = (MAX_WINDOW_WIDTH * ScreenRatio) / (length);
        double ratio2 = (MAX_WINDOW_HEIGHT * ScreenRatio) / (length);
        this.stretchRatio = Math.min(ratio1, ratio2);
    }

    /**
     * 当前的像素点由相对于原点的绝对坐标转化成相对于画板的原点的相对坐标
     */
    public PointMap.Point transPixelPoint(double inputX, double inputY, double wuCha) {
        PointMap.Point newP = new PointMap.Point();
        newP.x = (int) (inputX + MAX_WINDOW_WIDTH / 2 + CalculateModel.BLANK);
        newP.y = (int) (MAX_WINDOW_HEIGHT / 2 - inputY + CalculateModel.BLANK);
        newP.wuCha = wuCha;
        return newP;
    }

    public PointMap.Point[] transAllPixelPoints(PointMap.Point[] points) {
        ArrayList<PointMap.Point> temp = new ArrayList<>();
        for (PointMap.Point point : points) {
            temp.add(this.transPixelPoint(point.x, point.y, point.wuCha));
        }
        PointMap.Point[] out = new PointMap.Point[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            out[i] = temp.get(i);
        }
        return out;
    }

    public ArrayList<PointMap.Point> transAllPixelPoints(ArrayList<PointMap.Point> points) {
        ArrayList<PointMap.Point> out = new ArrayList<>(points.size());
        for (int i = 0; i < points.size(); i++) {
            out.add(i, this.transPixelPoint(points.get(i).x, points.get(i).y, points.get(i).wuCha));
        }
        return out;
    }

    private void translateDormAndRangeToPixel() {
        dorm_P = new int[]{(int) (dorm_R[0] * stretchRatio), (int) (dorm_R[1] * stretchRatio)};
        range_P = new int[]{(int) (range_R[0] * stretchRatio), (int) (range_R[1] * stretchRatio)};
    }

    /**
     * 把输入的点坐标由单位为像素转化成单位为实际值
     * 从而可能代入到方程当中求解
     * 此时的像素坐标是相对于原点的坐标，只需要经过缩放就可以达到目的
     */
    public PointMap.Point transRealPoint(double x, double y) {
        PointMap.Point p = new PointMap.Point(x, y);
        p.x = x / stretchRatio;
        p.y = y / stretchRatio;
        return p;
    }
}
