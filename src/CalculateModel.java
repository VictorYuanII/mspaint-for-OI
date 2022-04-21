
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;

public class CalculateModel {
    /**
     * 专门用于计算的类
     * 解析输入的表达式，判断其为函数还是仅为一般的方程
     * 选择合适的方法得到所有的散点，将散点传输出去
     */
    static final int BLANK = 0;
    static final double FUNCTION_WALK_LENGTH = 0.01;
    private double THRESHOLD_VAL = 0.5;
    private int WALK_LENGTH = 16;
    private int ALTER_BLANK = 2;

    static final String COS_FORM = "cos(";
    static final String SIN_FORM = "sin(";
    static final String TAN_FORM = "tan(";
    static final String ARC_SIN_FORM = "arcsin(";
    static final String ARC_COS_FORM = "arccos(";
    static final String ARC_TAN_FORM = "arctan(";
    static final String LN_FORM = "ln(";

    String input, ZBL, YBL;
    int[] dorm_P = new int[2];
    int[] range_P = new int[2];
    int color;
    boolean isFunction;
    private TransformModel T_model;
    public PointMap map;
    ArrayList<String> allStr = new ArrayList<>();
    ArrayList<String> store = new ArrayList<>();
    static HashSet<String> set = new HashSet<>();

    public CalculateModel(String input, String ZBL, String YBL, TransformModel T_model, int color) {
        this.ZBL = ZBL;
        this.YBL = YBL;
        this.input = judgeType(input);
        this.T_model = T_model;
        map = new PointMap(T_model);
        this.color = color;
        this.dorm_P = T_model.dorm_P;
        this.range_P = T_model.range_P;
        a = new double[3000][3000];
        initializeSet();
        breakInput();
        processInputs();
        buildAll();
        getAllScatters();
    }

    public CalculateModel(String input, String ZBL, String YBL, TransformModel T_model, int color
            ,int THRESHOLD_VAL,int WALK_LENGTH) {
        this.ZBL = ZBL;
        this.YBL = YBL;
        this.input = judgeType(input);
        this.T_model = T_model;
        map = new PointMap(T_model);
        this.color = color;
        this.dorm_P = T_model.dorm_P;
        this.range_P = T_model.range_P;
        this.THRESHOLD_VAL = THRESHOLD_VAL;
        this.WALK_LENGTH = WALK_LENGTH;
        a = new double[3000][3000];
        initializeSet();
        breakInput();
        processInputs();
        buildAll();
    }

    public void setAll(String input, String ZBL, String YBL, TransformModel T_model,int color
            ,int THRESHOLD_VAL,int WALK_LENGTH) {
        this.ZBL = ZBL;
        this.YBL = YBL;
        this.input = judgeType(input);
        this.T_model = T_model;
        map = new PointMap(T_model);
        this.color = color;
        this.dorm_P = T_model.dorm_P;
        this.range_P = T_model.range_P;
        this.THRESHOLD_VAL = THRESHOLD_VAL;
        this.WALK_LENGTH = WALK_LENGTH;
        a = new double[3000][3000];
        initializeSet();
        breakInput();
        processInputs();
        buildAll();
    }

    private String judgeType(String input) {
        String temp1 = String.valueOf(input.charAt(input.length() - 1));
        String temp2 = String.valueOf(input.charAt(0));
        boolean b1 = temp1.equals(YBL);
        boolean b2 = temp2.equals(YBL);
        isFunction = b1 || b2;
        if (!b1 && b2) {
            return input.substring(2);
        } else return input.substring(0,input.length()-2);
    }

    public double f(double x) {

        Stack<Double> numbers = new Stack<>();
        double X = x/(T_model.stretchRatio);

        for (int i = store.size()-1;i>=0;i--) {
            if (isNumber(store.get(i))) {
                numbers.push(transformIt(store.get(i),X));
            } else {
                String cur = store.get(i);
                if (set.contains(cur)) {
                    double num = numbers.pop();
                    numbers.push(calculateIt(num,cur));
                } else if (cur.equals("*")) {
                    double num1 = numbers.pop();
                    double num2 = numbers.pop();
                    numbers.push(num1*num2);
                } else if (cur.equals("^")) {
                    double num1 = numbers.pop();
                    double num2 = numbers.pop();
                    numbers.push(Math.pow(num1,num2));
                } else if (cur.equals("+")) {
                    double num1 = numbers.pop();
                    double num2 = numbers.pop();
                    numbers.push(num1+num2);
                } else if (cur.equals("-"))  {
                    double num1 = numbers.pop();
                    double num2 = numbers.pop();
                    numbers.push(num1-num2);
                }
            }
        }
        return numbers.pop();
    }

    public double f(double y,double x,boolean use) {

        PointMap.Point point = T_model.transRealPoint(x, y);
        Stack<Double> numbers = new Stack<>();
        x = point.x;
        y = point.y;

        for (int i = store.size()-1;i>=0;i--) {
            if (isNumber(store.get(i))) {
                numbers.push(transformIt(store.get(i),x,y));
            } else {
                String cur = store.get(i);
                if (set.contains(cur)) {
                    double num = numbers.pop();
                    numbers.push(calculateIt(num,cur));
                } else if (cur.equals("*")) {
                    double num1 = numbers.pop();
                    double num2 = numbers.pop();
                    numbers.push(num1*num2);
                } else if (cur.equals("^"))  {
                    double num1 = numbers.pop();
                    double num2 = numbers.pop();
                    numbers.push(Math.pow(num1,num2));
                } else if (cur.equals("+")) {
                    double num1 = numbers.pop();
                    double num2 = numbers.pop();
                    numbers.push(num1+num2);
                } else if (cur.equals("-")) {
                    double num1 = numbers.pop();
                    double num2 = numbers.pop();
                    numbers.push(num1-num2);
                }
            }
        }
        return numbers.pop();
    }

    public double transformIt(String str,double x,double y) {
        if (str.equals("e")) return Math.E;
        if (str.equals(ZBL)) return x;
        if (str.equals(YBL)) return y;
        return Double.parseDouble(str);
    }
    public double transformIt(String str,double x) {
        if (str.equals("e")) return Math.E;
        if (str.equals(ZBL)) return x;
        return Double.parseDouble(str);
    }

    public double calculateIt(double num,String cur) {
        int type = containsForms(cur);
        if (type==1) return Math.sin(num);
        else if (type==2) return Math.cos(num);
        else if (type==3) return Math.tan(num);
        else if (type==4) return Math.asin(num);
        else if (type==5) return Math.acos(num);
        else if (type==6) return Math.atan(num);
        else return Math.log(num);
    }

    private boolean isNumber(String str) {
        boolean b1 = !str.equals("+");
        boolean b2 = !str.equals("-");
        boolean b3 = !str.equals("*");
        boolean b4 = !str.equals("^");
        return !set.contains(str) && b1 && b2 && b3 && b4 ;
    }

    private void buildAll() {
        for (int i = 0;i<allStr.size();i++) {
            if (i!=allStr.size()-1)
                store.add("+");
            build(allStr.get(i));
        }
    }

    private void build(String str) {
        if (isSimplest(str)) {
            if (!str.equals(ZBL) && str.equals(YBL)) {
                store.add(YBL);
            } else if (str.equals(ZBL) && !str.equals(YBL)) {
                store.add(ZBL);
            } else if (str.equals("e")) {
                store.add("e");
            } else if (containsForms(str)!=-1) {
                store.add(String.valueOf(processIt(str,containsForms(str))));
            } else {
                if (str.contains("(")) {
                    store.add(str.substring(1,str.indexOf(")")));
                } else store.add(str);
            }
            return;
        }
        int s1 = containSkipBracket(str,"*");
        int s2 = containSkipBracket(str,"^");
        int s3 = containSkipBracket(str,"+");
        int s4 = containSkipBracket(str,"-");
        if ((s4!=-1&&s4!=0) || s3!=-1) {
            String str1,str2;
            if (s3!=-1) {
                store.add("+");
                str1 = str.substring(0,s3);
                str2 = str.substring(s3+1);
            }
            else {
                store.add("-");
                str1 = str.substring(0,s4);
                str2 = str.substring(s4+1);
            }
            build(str1);
            build(str2);
        } else {
            if (s1!=-1 || s2!=-1) {
                String str1,str2;
                if (s1!=-1) {
                    store.add("*");
                    str1 = str.substring(0,s1);
                    str2 = str.substring(s1+1);
                } else {
                    store.add("^");
                    str1 = str.substring(0,s2);
                    str2 = str.substring(s2+1);
                }
                build(str1);
                build(str2);
            }else {
                String newStr;
                int i = containsForms(str);
                if (i==1 || i==2 || i==3) {
                    if (i==1) store.add("sin(");
                    if (i==2) store.add("cos(");
                    if (i==3) store.add("tan(");
                    newStr = str.substring(4,str.length()-1);
                } else if (i==4 || i==5 || i==6) {
                    if (i==4) store.add("arcsin(");
                    if (i==5) store.add("arccos(");
                    if (i==6) store.add("arctan(");
                    newStr = str.substring(7,str.length()-1);
                } else if (i==7) {
                    store.add("ln(");
                    newStr = str.substring(3,str.length()-1);
                } else {
                    newStr = str.substring(1,str.length()-1);
                }
                build(newStr);
            }
        }
    }

    private int containSkipBracket(String in,String find) {
        for (int i = 0;i<in.length();i++) {
            if (in.charAt(i)=='(') {
                i = findRightBracket(in,i);
            }
            if (find.equals(String.valueOf(in.charAt(i))))
                return i;
        }
        return -1;
    }

    public double processIt(String in,int type) {
        if (type==1 || type==2 || type==3) {
            String pro = in.substring(4,in.length()-1);
            double num;
            num = Double.parseDouble(pro);
            if (type==1) return Math.sin(num);
            if (type==2) return Math.cos(num);
            return Math.tan(num);
        } else if (type==4 || type==5 || type==6) {
            String pro = in.substring(7,in.length()-1);
            double num;
            num = Double.parseDouble(pro);
            if (type==4) return Math.asin(num);
            if (type==5) return Math.acos(num);
            return Math.atan(num);
        } else {
            String pro = in.substring(3,in.length()-1);
            double num;
            if (pro.equals("e")) num = Math.E;
            else num = Double.parseDouble(pro);
            return Math.log(num);
        }
    }

    private boolean isSimplest(String str) {
        if (str.contains("*") || str.contains("^"))
            return false;
        if (str.equals("e")) return true;
        else if ((!str.contains(ZBL)) && (!str.contains(YBL)))
            return true;
        else return str.equals(ZBL) || str.equals(YBL);
    }

    private void breakInput() {
        int last = 0;
        for (int i = 0;i<input.length();i++) {
            boolean b1 = input.charAt(i)=='+';
            boolean b2 = input.charAt(i)=='-';
            boolean b3 = input.charAt(i)=='(';
            if (b3) {
                i = findRightBracket(input,i);
                continue;
            }
            if ((b1&&i!=0) || (b2&&i!=0)) {
                allStr.add(input.substring(last,i));
                last = i;
            }
        }
        allStr.add(input.substring(last));
    }

    private void processInputs() {
        for (int i = 0;i<allStr.size();i++) {
            String str = allStr.get(i);
            if (str.charAt(0)=='+') {
                str = str.substring(1);
            }
            if (str.charAt(0)=='-') {
                str = "(-1)*" + str.substring(1);
            }
            allStr.set(i,str);
        }
    }

    private int findRightBracket(String in,int pos) {
        int left = 1,right = 0;
        while (left!=right && pos<in.length()) {
            pos++;
            if (in.charAt(pos)=='(')
                left++;
            else if (in.charAt(pos)==')')
                right++;
        }
        return pos;
    }

    private boolean containsForm(String in, String FORM) {
        if (!in.contains("(")) return false;
        int pos = in.indexOf("(");
        int begin = Math.max(pos - FORM.length()+1, 0);
        String cut = in.substring(begin, pos+1);
        return cut.equals(FORM);
    }

    public int containsForms(String in) {
        String input = in.substring(0,in.indexOf("(")+1);
        int length = input.length();
        if (length<4) {
            if (containsForm(in,LN_FORM)) return 7;
        } else if (length<5) {
            boolean b1 = containsForm(in,COS_FORM);
            boolean b2 = containsForm(in,SIN_FORM);
            boolean b3 = containsForm(in,TAN_FORM);
            if (b1) return 2;
            if (b2) return 1;
            if (b3) return 3;
        } else if (length<8) {
            boolean b1 = containsForm(in,ARC_TAN_FORM);
            boolean b2 = containsForm(in,ARC_COS_FORM);
            boolean b3 = containsForm(in,ARC_SIN_FORM);
            if (b1) return 6;
            if (b2) return 5;
            if (b3) return 4;
        }
        return -1;
    }
    private void initializeSet() {
        set.add(SIN_FORM);
        set.add(COS_FORM);
        set.add(TAN_FORM);
        set.add(ARC_COS_FORM);
        set.add(ARC_SIN_FORM);
        set.add(ARC_TAN_FORM);
        set.add(LN_FORM);
    }

    /**
     * 整合两个用于以不同的方向扫描所有的点的方法
     */

    public int testMode = 2;

    public PointMap getAllScatters() {
        if (!isFunction) {
             if(testMode==0) {
                 getAllScatters_Vertical();
                 getAllScatters_Horizontal();
             }
             else if (testMode==1)
                 getAllScatters_Combine(map);
             else if (testMode==2)
                 iterationDepthSearch();
             else if (testMode==3)
                 getAllScatters_OriBlocked(2,1000);
        } else {
            getAllScatters_Function(map);
        }
        return map;
    }

    /**
     * 在判断当前的输入是一个函数的时候，采用特殊的方法求得所有的散点
     */
    public void getAllScatters_Function(PointMap map) {
        for (double X = (dorm_P[0] - BLANK); X<dorm_P[1]+BLANK; X+=FUNCTION_WALK_LENGTH) {
            double Y = ((f(X))* T_model.stretchRatio);
            PointMap.Point p = T_model.transPixelPoint(X,Y,0);
            map.add(p);
        }
    }

    /**
     * 用于得到所有点的方法之一，用于水平扫描所有的点
     * 适用于图形的斜率很大的地方
     */
    public void getAllScatters_Horizontal() {
        double cur, L, l;
        for (int Y = range_P[0] - BLANK; Y < range_P[1] + BLANK; Y++) {
            L = f(Y, dorm_P[0] - BLANK);
            l = f(Y, dorm_P[0] - BLANK + 1);
            for (int X = dorm_P[0] - BLANK + 2; X < dorm_P[1] + BLANK; X++) {
                cur = f(Y, X);
                double absL = Math.abs(L);
                double absl = Math.abs(l);
                double abscur = Math.abs(cur);
                if (absl < absL && absl < abscur && L*cur<0) {
                    PointMap.Point p = T_model.transPixelPoint(X-1, Y, 0);
                    map.add(p);
                }
                L = l;
                l = cur;
            }
        }
    }
    /**
     * 用于得到散点的方法之一，垂直方向扫描所有点的方法
     * 适用于图形的斜率很小的地方
     */
    public void getAllScatters_Vertical() {
        double cur, L, l;
        for (int X = dorm_P[0] - BLANK; X < dorm_P[1] + BLANK; X++) {
            L = f(range_P[0] - BLANK, X);
            l = f(range_P[0] - BLANK + 1, X);
            for (int Y = range_P[0] - BLANK + 2; Y < range_P[1] + BLANK; Y++) {
                cur = f(Y, X);
                double absL = Math.abs(L);
                double absl = Math.abs(l);
                double abscur = Math.abs(cur);
                if (absl < absL && absl < abscur && L*cur<0) {
                    PointMap.Point p = T_model.transPixelPoint(X, Y-1, 1);
                    map.add(p);
                }
                L = l;
                l = cur;
            }
        }
    }


    /**
     * 将水平方向和垂直方向最后合并成一个方向，计算当前点上下左右的所有点误差值
     * 没能够达到预期的效果，效率还是非常低
     */
    public void getAllScatters_Combine(PointMap map) {
        double cur, lastInRow, nearInRow, leftInCol, rightInCol;
        for (int X = dorm_P[0] - BLANK; X < dorm_P[1] + BLANK; X++) {
            lastInRow = f(range_P[0] - BLANK, X);
            nearInRow = f(range_P[0] - BLANK + 1, X);
            for (int Y = range_P[0] - BLANK + 2; Y < range_P[1] + BLANK; Y++) {
                cur = f(Y, X);
                leftInCol = f(Y, X-1);
                rightInCol = f(Y, X+1);
                double absLastInRow = Math.abs(lastInRow);
                double absNearInRow = Math.abs(nearInRow);
                double absLeftInCol = Math.abs(leftInCol);
                double absRightInCol = Math.abs(rightInCol);
                double absCur = Math.abs(cur);
                boolean b1 = absCur<=absNearInRow && absCur<=absLastInRow;
                boolean b2 = absCur<=absLeftInCol && absCur<=absRightInCol;
                boolean b3 = lastInRow*cur<=0 && leftInCol*rightInCol<=0;
                if (b1 && b2 && b3) {
                    PointMap.Point p = T_model.transPixelPoint(X, Y, 1);
                    map.add(p);
                }
                lastInRow = nearInRow;
                nearInRow = cur;
            }
        }
    }

    /**
     * 为了提升效率重新把阈值这一概念引入
     * 同时加入了步长
     */

    public void getAllScatters_Blocked(int WALK_LENGTH,int THRESHOLD_VAL) {
        if (WALK_LENGTH<1) return;
        if (WALK_LENGTH==1) {
            ALTER_BLANK = 0;
            getAllScatters_Horizontal();
            getAllScatters_Vertical();
            return;
        }
        for (int X = dorm_P[0] - BLANK; X < dorm_P[1]+BLANK;X+=WALK_LENGTH) {
            for (int Y = range_P[0]-BLANK;Y < range_P[1]+BLANK;Y+=WALK_LENGTH) {
                int rX = (int)(Math.random()*WALK_LENGTH)+X;
                int rY = (int)(Math.random()*WALK_LENGTH)+Y;
                double temp = Math.abs(f(rY,rX));
                if (temp<=THRESHOLD_VAL) {
                    double cur;
                    double farLast1,nearLast1,farLast2,nearLast2;
                    for (int i = X-ALTER_BLANK; i< X +WALK_LENGTH+ALTER_BLANK; i++) {
                        farLast1 = f(Y, i);
                        nearLast1 = f(Y+1, i);
                        for (int j = Y+2-ALTER_BLANK;j<Y+WALK_LENGTH+ALTER_BLANK;j++) {
                            cur = f(j,i);
                            double absFarLast = Math.abs(farLast1);
                            double absNearLast = Math.abs(nearLast1);
                            double absCur = Math.abs(cur);
                            if (absFarLast>absNearLast && absNearLast<absCur && farLast1*cur<0) {
                                PointMap.Point p = T_model.transPixelPoint(i,j-1,0);
                                map.add(p);
                            }
                            farLast1 = nearLast1;
                            nearLast1 = cur;
                        }
                    }
                    for (int i = Y-ALTER_BLANK; i<Y+WALK_LENGTH+ALTER_BLANK;i++) {
                        farLast2 = f(i,X);
                        nearLast2 = f(i,X+1);
                        for (int j = X+2-ALTER_BLANK;j<X+WALK_LENGTH+ALTER_BLANK;j++) {
                            cur = f(i,j);
                            double absFarLast = Math.abs(farLast2);
                            double absNearLast = Math.abs(nearLast2);
                            double absCur = Math.abs(cur);
                            if (absFarLast>absNearLast && absNearLast<absCur && farLast2*cur<0) {
                                PointMap.Point p = T_model.transPixelPoint(j-1,i,0);
                                map.add(p);
                            }
                            farLast2 = nearLast2;
                            nearLast2 = cur;
                        }
                    }
                }
            }
        }
    }

    /**
     * 还原最初的模式
     */
    public void getAllScatters_OriBlocked(int WALK_LENGTH,int THRESHOLD_VAL) {
        for (int X = dorm_P[0] - BLANK; X < dorm_P[1]+BLANK;X+=WALK_LENGTH) {
            for (int Y = range_P[0]-BLANK;Y < range_P[1]+BLANK;Y+=WALK_LENGTH) {
                //int rX = (int)(Math.random()*WALK_LENGTH)+X;
                //int rY = (int)(Math.random()*WALK_LENGTH)+Y;
                //double temp = Math.abs(f(rY,rX));
                double temp = Math.abs(f(Y,X));
                if (temp<=THRESHOLD_VAL) {
                    double cur;
                    double farLast1,nearLast1,farLast2,nearLast2;
                    for (int i = X-ALTER_BLANK; i< X +WALK_LENGTH+ALTER_BLANK; i++) {
                        farLast1 = f(Y, i);
                        nearLast1 = f(Y+1, i);
                        for (int j = Y+2-ALTER_BLANK;j<Y+WALK_LENGTH+ALTER_BLANK;j++) {
                            cur = f(j,i);
                            double absFarLast = Math.abs(farLast1);
                            double absNearLast = Math.abs(nearLast1);
                            double absCur = Math.abs(cur);
                            if (absFarLast>absNearLast && absNearLast<absCur && farLast1*cur<0) {
                                PointMap.Point p = T_model.transPixelPoint(i,j-1,0);
                                map.add(p);
                            }
                            farLast1 = nearLast1;
                            nearLast1 = cur;
                        }
                    }
                    for (int i = Y-ALTER_BLANK; i<Y+WALK_LENGTH+ALTER_BLANK;i++) {
                        farLast2 = f(i,X);
                        nearLast2 = f(i,X+1);
                        for (int j = X+2-ALTER_BLANK;j<X+WALK_LENGTH+ALTER_BLANK;j++) {
                            cur = f(i,j);
                            double absFarLast = Math.abs(farLast2);
                            double absNearLast = Math.abs(nearLast2);
                            double absCur = Math.abs(cur);
                            if (absFarLast>absNearLast && absNearLast<absCur && farLast2*cur<0) {
                                PointMap.Point p = T_model.transPixelPoint(j-1,i,0);
                                map.add(p);
                            }
                            farLast2 = nearLast2;
                            nearLast2 = cur;
                        }
                    }
                }
            }
        }
    }

    /**
     * 记忆化搜索，用于排除重复的点，提高效率
     */
    private double[][] a;

    public double f(int y,int x) {
        if (a[x+1500][y+1500]==0)
            a[x+1500][y+1500]=f(y, x,true);
        return a[x+1500][y+1500];
    }
    /**
     * 迭代加深搜索，用于逐步减小步长，增大阈值
     */

    private int lastSize = -10000;
    private double ji = 0.98;
    private int cha = 10;

    public void iterationDepthSearch() {
        System.out.println(WALK_LENGTH);
        if (WALK_LENGTH==1||(map.getSize()*ji<=lastSize)||(Math.abs(map.getSize()-lastSize)<cha)||map.lianXuGraph()) {
            return;
        }
        //if (WALK_LENGTH!=1) {
            WALK_LENGTH /= 2;
            THRESHOLD_VAL *= 10;
        //}
        if (lastSize!=-1) lastSize = map.getSize();
        getAllScatters_Blocked(WALK_LENGTH,(int)THRESHOLD_VAL);
        iterationDepthSearch();
    }
}
