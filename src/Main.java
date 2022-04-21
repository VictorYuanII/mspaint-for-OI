import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Group;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.StringTokenizer;


public class Main  extends Application implements Serializable
{
    //所有变量全都static方便其他地方访问

    public static final int wide=1200,high=800,leftWide=200,xpWide=60;//宽，高，左边的宽

    public static Random rd=new Random();//好啊
    public static ArrayList<Object>      stack=new ArrayList<>();//事件栈(line和group不相容)
    public static ArrayList<Integer>     flag=new ArrayList<>();//记录栈中的东西是删除还是加入以及一次操作的断点
    //0表示终止 -1表示删除线段 1表示增加线段 -2表示删组件 2表示加组件
    //public static ArrayList<Group>   hasGroup=new ArrayList<>();//目前已有的图形
    public static ArrayList<Line>[][] lineMap=new ArrayList[2000][1500];//存所有的线段

    //下面搞左边颜色和工具按钮
    public static double kuan=2.5;//宽度
    public static Color nowColor=Color.BLACK,black=Color.BROWN;//一个很好的全局变量, 记录画笔颜色
    public static int nowTool=0;//当前选中的组件是谁//后面
    //0铅笔 1橡皮 2选择   3线段 4矩形 5圆形   6撤退 7清空 (21种颜色)
    public static final int toolCnt=9,colorCnt=20,buttonCnt=6;//数量
    public static Image[]toolMan={ new Image("铅笔.png"),new Image("橡皮.png"),new Image("选择.png")
                                  ,new Image("线段.png"),new Image("矩形.png"),new Image("圆.png")
                                  ,new Image("撤退.png"),new Image("清空.png"),new Image("删除.png") };
    public static ImageView[] tool=new ImageView[toolCnt];
    public static Rectangle[] toolBack=new Rectangle[toolCnt];
    public static Color[] colorTool={Color.web("#000000"),Color.web("#ffffff"),Color.web("#848683"),
            Color.web("#b97b56"),Color.web("#89010d"),Color.web("#f01e1f"),
            Color.web("#ff7c26"),Color.web("#ffc80c"),Color.web("#088A08"),
            Color.web("#00dd00"),Color.web("#b6e51d"),Color.web("#fef007"),
            Color.web("#390878"),Color.web("#3b46e0"),
            Color.web("#07a0e6"),Color.web("#1BC9A0"),
            Color.web("#9402FD"),Color.web("#9c4ca1"),Color.web("#FD0183"),
            Color.web("#ffadd6")};
    public static Rectangle[] color=new Rectangle[colorCnt];
    public static Rectangle currentColor;
    public static Button[] button={new Button("排列"),new Button("线段树"),new Button("二分图")
                                  ,new Button("树"),new Button("图"),new Button("有向无环图")};
    public static ImageView yinGu=new ImageView(new Image("YinGu.jpg"));

    public static Rectangle background=new Rectangle(0,0,leftWide,high);//左边大背景板
    //public static Rectangle xiangPi=new Rectangle(0,0,xpWide,xpWide);
    public static Group lineSet=new Group();//线单独开,保证性能
    //根,图层和画图用的临时形状
    public static Group root=new Group(),left=new Group(background),right=new Group();
    public static Scene scene=new Scene(root,wide,high);
    public static Line line;
    public static Rectangle rectangle,rectangle1,rectangle2,rectangleWhite;
    public static Circle circle;
    public static Text text;
    public static int flagClick;//0普通画图 1左边工具 2选择！
    public static ArrayList<Group> nowGroup=new ArrayList<>();//记录选择的工具
    public static ArrayList<Line> nowLine=new ArrayList<>();//现在的line要单独记一个
    public static double lstX=-1,lstY=-1;//如果这里是-1，说明没有选
    //public static double clickX,clickY;//单击的位置

    public static Alert caiDan1=new Alert(Alert.AlertType.INFORMATION);

    public static Line f(Line g)
    {//暴力克隆
        Line f=new Line();
        f.setStartX(g.getStartX());
        f.setStartY(g.getStartY());
        f.setEndX(g.getEndX());
        f.setEndY(g.getEndY());
        f.setFill(g.getFill());
        f.setStroke(g.getStroke());
        f.setStrokeWidth(g.getStrokeWidth());
        f.setTranslateX(g.getTranslateX());
        f.setTranslateY(g.getTranslateY());
        return f;
    }

    public static Rectangle f(Rectangle g)
    {
        Rectangle f=new Rectangle();
        f.setX(g.getX());
        f.setY(g.getY());
        f.setWidth(g.getWidth());
        f.setHeight(g.getHeight());
        f.setFill(g.getFill());
        f.setStroke(g.getStroke());
        f.setStrokeWidth(g.getStrokeWidth());
        f.setTranslateX(g.getTranslateX());
        f.setTranslateY(g.getTranslateY());
        return f;
    }

    public static Circle f(Circle g)
    {
        Circle f=new Circle();
        f.setCenterX(g.getCenterX());
        f.setCenterY(g.getCenterY());
        f.setRadius(g.getRadius());
        f.setFill(g.getFill());
        f.setStroke(g.getStroke());
        f.setStrokeWidth(g.getStrokeWidth());
        f.setTranslateX(g.getTranslateX());
        f.setTranslateY(g.getTranslateY());
        return f;
    }

    public static Text f(Text g)
    {
        Text f=new Text();
        f.setX(g.getX());
        f.setY(g.getY());
        f.setText(g.getText());
        f.setFont(g.getFont());
        f.setFill(g.getFill());
        f.setStroke(g.getStroke());
        f.setStrokeWidth(g.getStrokeWidth());
        f.setTranslateX(g.getTranslateX());
        f.setTranslateY(g.getTranslateY());
        return f;
    }



    public static Group f(Group g)
    {
        Group f=new Group();
        Object[] obj=g.getChildren().toArray();
        for(Object o:obj)
        {
            if(o instanceof Line) f.getChildren().add(f((Line) o));
            if(o instanceof Rectangle) f.getChildren().add(f((Rectangle) o));
            if(o instanceof Circle) f.getChildren().add(f((Circle) o));
            if(o instanceof Text) f.getChildren().add(f((Text) o));
        }
        return f;
    }

    public static Rectangle setRectangle(Rectangle now,double x1,double y1,double x2,double y2)
    {
        now.setX(Math.min(x1,x2));//初始位置
        now.setY(Math.min(y1,y2));
        now.setWidth(Math.abs(x1-x2));//长和宽
        now.setHeight(Math.abs(y1-y2));
        return now;
    }


    public static void pushTool(int startX,int nextX,int startY,int nextY)//放工具
    {//开始的横/竖坐标,
        for(int i=0;i<toolCnt;++i)//工具三分
        {
            tool[i].setX(startX+(i%3)*nextX);
            tool[i].setY(startY+(i/3)*nextY);
            toolBack[i].setX(startX+(i%3)*nextX);
            toolBack[i].setY(startY+(i/3)*nextY);
        }
    }

    public static void pushColor(int startX,int nextX,int startY,int nextY)//放颜色
    {
        currentColor=new Rectangle(color[0].getWidth()+nextX,color[0].getHeight());
        currentColor.setX(startX+nextX);
        currentColor.setY(startY-nextY);
        for(int i=0;i<colorCnt;++i)
        {
            color[i].setX(startX+(i%4)*nextX);
            color[i].setY(startY+(i/4)*nextY);
        }
    }

    public static void pushButton(int startX,int nextX,int startY,int nextY)
    {
        for(int i=0;i<buttonCnt;++i)
        {//按钮必须Translate
            button[i].setTranslateX(startX+(i%2)*nextX);
            button[i].setTranslateY(startY+(i/2)*nextY);
        }
    }

    public static void initLeft()//放左边的东西
    {
        for(int i=0;i<toolCnt;++i)//把图像搞下来
        {
            toolBack[i]=new Rectangle(40,40);//背景
            toolBack[i].setFill(Color.rgb(50,50,50));
            tool[i]=new ImageView(toolMan[i]);//每个图像是工具人
            tool[i].setFitWidth(40);//缩小
            tool[i].setFitHeight(40);
        }
        background.setFill(Color.rgb(50,50,50));//背景偏黑
        pushTool(15,60,10,70);

        for(int i=0;i<colorCnt;++i)        //颜色
        {
            color[i]=new Rectangle(40,40);
            color[i].setFill(colorTool[i]);//涂上
        }
        pushColor(12,45,270,45);

        for(int i=0;i<buttonCnt;++i)//最后放按钮
        {
            button[i].setPrefWidth(80);
            button[i].setPrefHeight(30);
        }
        pushButton(10,100,525,40);

        yinGu.setScaleX(0.35);//夹带私货环节
        yinGu.setScaleY(0.35);
        yinGu.setX(-170);
        yinGu.setY( 540);

        toolBack[0].setFill(black);

        caiDan1.setTitle("恭喜你发现彩蛋！");
        caiDan1.setHeaderText(null);
        caiDan1.setContentText("实用工具《画图(OI教具版)》现已上架Alpha Mall,欢迎大家前来购买我们的产品");
    }

    public static void initRight()//为画图而准备
    {
        rectangle1=new Rectangle(xpWide,xpWide);//橡皮
        rectangle1.setFill(Color.WHITE);
        rectangle1.setTranslateX(xpWide*(-0.5));
        rectangle1.setTranslateY(xpWide*(-0.5));//左上角平移大法

        rectangle2=new Rectangle(0,0);
        rectangle2.getStrokeDashArray().addAll(10.0);//虚化
        rectangle2.setFill(null);
        rectangle2.setStroke(Color.BLACK);
        rectangle2.setStrokeWidth(2);

        rectangleWhite=new Rectangle(0,0,0,0);
        rectangleWhite.setFill(null);//做空
        rectangleWhite.setStrokeWidth(0);

        stack.add(null);//事件栈里先放一个好的
        flag.add(0);

        for(int i=0;i<2000;++i)//新建空的
        {
            for(int j=0;j<1500;++j)
            {
                lineMap[i][j]=new ArrayList<>();
            }
        }
    }

    public  void asd(ActionEvent event)
    {
        Group group=new Group();
        if(event.getSource()==button[0])
        {
            PaiLie a=new PaiLie();
            group=a.group;
        }
        if(event.getSource()==button[1])
        {
            XianDuanShu a=new XianDuanShu();
            group=a.group;
        }
        if(event.getSource()==button[2])
        {
            ErFenTu a=new ErFenTu();
            group=a.group;
        }
        if(event.getSource()==button[3])
        {
            Shu a=new Shu();
            group=a.group;
        }
        if(event.getSource()==button[4])
        {
            Tu a=new Tu();
            group=a.group;
        }
        if(event.getSource()==button[5])
        {
            DAG a=new DAG();
            group=a.group;
        }
        nowGroup.add(group);
        right.getChildren().add(group);
        stack.add(null);
        flag.add(0);
        stack.add(group);
        flag.add(2);

    }

    //public String toLine(Line o) { return o.getStartX()+" "+o.getStartY()+" "+o.getEndX()+" "+o.getEndY();}
    //public String toRectangle(Rectangle o) {return o.getX()+" "+o.getY()+" "+o.getWidth()+" "+o.getHeight(); }
    //public String toColor(Color o) { return o.getRed()+" "+o.getGreen()+" "+o.getBlue(); }
    public String toStr(Line o)
    {
        Line oo=f(o);
        for(int i=0;i<colorCnt;++i)
        {
            oo.setStroke(colorTool[i]);
            Paint s1=oo.getStroke(),s2=o.getStroke();
            if(s1.equals(s2)) return o.getStartX()+" "+o.getStartY()+" "+o.getEndX()+" "+o.getEndY()+" "+i+" "+o.getStrokeWidth();
        }
        return "WA";
    }
    public String toStr(Rectangle o)
    {
        Rectangle oo=f(o);
        for(int i=0;i<colorCnt;++i)
        {
            oo.setStroke(colorTool[i]);
            Paint s1=oo.getStroke(),s2=o.getStroke();
            if(s1.equals(s2)) return o.getX()+" "+o.getY()+" "+o.getWidth()+" "+o.getHeight()+" "+i+" "+o.getStrokeWidth();
        }
        return "WA";
    }
    public String toStr(Circle o)
    {
        Circle oo=f(o);
        for(int i=0;i<colorCnt;++i)
        {
            oo.setStroke(colorTool[i]);
            Paint s1=oo.getStroke(),s2=o.getStroke();
            if(s1.equals(s2)) return o.getCenterX()+" "+o.getCenterY()+" "+o.getRadius()+" "+i+" "+o.getStrokeWidth();
        }
        return "WA";
    }
    public String toStr(Text o)
    {
        Text oo=f(o);
        //oo.getFont().getSize();
        for(int i=0;i<colorCnt;++i)
        {
            oo.setStroke(colorTool[i]);
            Paint s1=oo.getStroke(),s2=o.getStroke();
            if(s1.equals(s2)) return o.getText()+" "+o.getX()+" "+o.getY()+" "+o.getFont().getSize()+" "+i+" "+o.getStrokeWidth();
        }
        return "WA";
    }

    public void saved(KeyEvent event)
    {
        if(event.getCode()!= KeyCode.S) return;
        try
        {
            //FileOutputStream fos=new FileOutputStream("out.txt");
            ///ObjectOutputStream oos=new ObjectOutputStream(fos);
            //oos.writeObject(lineSet);//oos.writeObject(right);
            //oos.writeObject(flag);//oos.writeObject(stack);
            //oos.flush();oos.close();
            FileWriter out=new FileWriter("out.txt");
            Object[] obj1=lineSet.getChildren().toArray();
            Object[] obj2=right.getChildren().toArray();
            out.write(obj1.length+" "+obj2.length+'\n');

            for(Object o:obj1){out.write(toStr((Line)o)+'\n');}
            for(Object oo:obj2)
            {
                if(!(oo instanceof Group)) { out.write(0+'\n'); continue; }//和上面输出的不一致所以可能会出事
                Object[] obj3=((Group)oo).getChildren().toArray();
                String s=obj3.length+"\n";
                out.write(s);
                for(Object o:obj3)
                {
                    if(o instanceof Line) out.write("1 "+toStr((Line)o)+'\n');
                    if(o instanceof Rectangle) out.write("2 "+toStr((Rectangle) o)+'\n');
                    if(o instanceof Circle) out.write("3 "+toStr((Circle) o)+'\n');
                    if(o instanceof Text) out.write("4 "+toStr((Text) o)+'\n');
                }
            }
            out.flush(); out.close();
        }
        catch (Exception e) { System.out.println("RE");}
    }

    public int toInt(String s)
    {
        int ans=0;
        for(int i=0;i<s.length();++i) ans=ans*10+(int)(s.charAt(i)-'0');
        return ans;
    }
    public double toD(String s)//好生难写
    {
        double ans=0;
        double j=1;
        int i=0;
        for(;i<s.length();++i)
        {
            char c=s.charAt(i);
            if(c=='.') break;
            ans=ans*10+(c-'0');
        }
        for(i=i+1,j=10;i<s.length();++i,j=j*10)
            ans+=(s.charAt(i)-'0')/j;
        return ans;
    }

    @Override
    public void start(Stage primaryStage) throws IOException
    {
        try
        {
            BufferedReader in=new BufferedReader(new FileReader("out.txt"));
            String c=in.readLine();
            StringTokenizer s=new StringTokenizer(c," ");
            int n=toInt(s.nextToken()),m=toInt(s.nextToken());
            for(int i=1;i<=n;++i)
            {//o.getStartX()+" "+o.getStartY()+" "+o.getEndX()+" "+o.getEndY()+" "+i+" "+o.getStrokeWidth();

                c=in.readLine();
                s=new StringTokenizer(c," ");
                Line o=new Line();
                o.setStartX(toD(s.nextToken()));
                o.setStartY(toD(s.nextToken()));
                o.setEndX(toD(s.nextToken()));
                o.setEndY(toD(s.nextToken()));
                o.setStroke(colorTool[toInt(s.nextToken())]);
                o.setStrokeWidth(toD(s.nextToken()));
                flag.add(1);
                stack.add(o);
                lineMap[(int)o.getEndX()][(int)o.getEndX()].add(o);
                lineSet.getChildren().add(o);//非常离谱
            }
            for(int i=1;i<=m;++i)
            {
                c=in.readLine();
                s=new StringTokenizer(c," ");
                int T=toInt(s.nextToken());
                Group now=new Group();
                for(int j=1;j<=T;++j)
                {
                    c=in.readLine();
                    s=new StringTokenizer(c," ");
                    int op=toInt(s.nextToken());
                    //o.getStartX()+" "+o.getStartY()+" "+o.getEndX()+" "+o.getEndY()+" "+i+" "+o.getStrokeWidth();)
                    if(op==1)
                    {
                        Line o=new Line();
                        o.setStartX(toD(s.nextToken()));
                        o.setStartY(toD(s.nextToken()));
                        o.setEndX(toD(s.nextToken()));
                        o.setEndY(toD(s.nextToken()));
                        o.setStroke(colorTool[toInt(s.nextToken())]);
                        o.setStrokeWidth(toD(s.nextToken()));
                        now.getChildren().add(o);
                    }
                    //o.getX()+" "+o.getY()+" "+o.getWidth()+" "+o.getHeight()+" "+i+" "+o.getStrokeWidth();
                    if(op==2)
                    {
                        Rectangle o=new Rectangle();
                        o.setX(toD(s.nextToken()));
                        o.setY(toD(s.nextToken()));
                        o.setWidth(toD(s.nextToken()));
                        o.setHeight(toD(s.nextToken()));
                        o.setStroke(colorTool[toInt(s.nextToken())]);
                        o.setStrokeWidth(toD(s.nextToken()));
                        o.setFill(null);
                        now.getChildren().add(o);
                    }
                    //o.getCenterX()+" "+o.getCenterY()+" "+o.getRadius()+" "+i+" "+o.getStrokeWidth();
                    if(op==3)
                    {
                        Circle o=new Circle();
                        o.setCenterX(toD(s.nextToken()));
                        o.setCenterY(toD(s.nextToken()));
                        o.setRadius(toD(s.nextToken()));
                        o.setStroke(colorTool[toInt(s.nextToken())]);
                        o.setStrokeWidth(toD(s.nextToken()));
                        o.setFill(null);
                        now.getChildren().add(o);
                    }
                    //o.getText()+" "+o.getX()+" "+o.getY()+" "+o.getFont().getSize()+" "+i+" "+o.getStrokeWidth();
                    if(op==4)
                    {
                        Text o=new Text();
                        o.setText(s.nextToken());
                        o.setX(toD(s.nextToken()));
                        o.setY(toD(s.nextToken()));
                        o.setFont(new Font(toD(s.nextToken())));
                        int k=toInt(s.nextToken());
                        o.setFill(colorTool[k]);
                        o.setStroke(colorTool[k]);
                        o.setStrokeWidth(toD(s.nextToken()));
                        now.getChildren().add(o);
                    }

                }
                flag.add(2);
                stack.add(now);
                right.getChildren().add(now);
            }
        }
        catch(Exception e) {System.out.println("WA");}

        flag.add(0);
        stack.add(null);//后补一个
        for(int i=0;i<buttonCnt;++i) button[i].setOnAction(this::asd);


        root.getChildren().add(right);
        root.getChildren().addAll(lineSet);//这样会导致所有自己画的线在左边的下面，右边的上面
        root.getChildren().add(left);//左在右之后加入，保证左边的一定在上面

        left.getChildren().addAll(toolBack);
        left.getChildren().addAll(tool);//全进去
        left.getChildren().addAll(color);
        left.getChildren().addAll(currentColor);
        left.getChildren().addAll(button);
        left.getChildren().addAll(yinGu);

        scene.setOnMousePressed(this::press);
        scene.setOnMouseDragged(this::drag);
        scene.setOnMouseReleased(this::release);
        scene.setOnKeyPressed(this::saved);

        primaryStage.setTitle("画图");
        primaryStage.setScene(scene);
        scene.setFill(Color.rgb(216,216,216));
        primaryStage.show();
    }

    public static Line[] trans(Rectangle a)
    {
        double x=a.getX(),w=a.getWidth(),y=a.getY(),h=a.getHeight();
        Line[] l={new Line(x,y,x+w,y),new Line(x+w,y,x+w,y+h)
                 ,new Line(x,y,x,y+h),new Line(x,y+h,x+w,y+h) };
        return l;
    }


    public static boolean at(Rectangle a,double x,double y)//点在矩形里面
    {
        if(x<a.getX()||x>a.getX()+a.getWidth()) return false;
        if(y<a.getY()||y>a.getY()+a.getHeight()) return false;
        return true;
    }

    public static boolean at(Rectangle a,Line b)
    {
        if(at(a,b.getStartX(),b.getStartY())) return true;
        if(b.getEndX()==b.getStartX()&&b.getEndY()==b.getStartY()) return false;
        Line[] l=trans(a);
        for(int i=0;i<4;++i)
            if( XianDuan.at( new XianDuan(l[i]) , new XianDuan(b) ) )
                return true;
        return false;
    }



    public static double checkR(Line l,double x,double y)
    {
        double s1= (new Node(l.getStartX()-x,l.getStartY()-y)).dis();
        double s2= (new Node(l.getEndX()-x,l.getEndY()-y)).dis();
        return Math.max(s1,s2);
    }

    public static boolean at(Rectangle a,Circle b)
    {
        //if(at(a,b.getCenterX(),b.getCenterY())) return true;
        Line[] l=trans(a);
        Node r=new Node(b.getCenterX(),b.getCenterY());
        for(int i=0;i<4;++i)
            if(XianDuan.dis(new XianDuan(l[i]),r)<=b.getRadius()&&checkR(l[i],b.getCenterX(),b.getCenterY())>=b.getRadius())
                return true;
        if(a.getWidth()<=2*b.getRadius()||a.getWidth()<=2*b.getRadius()) return false;
        double fff=0;
        for(int i=0;i<4;++i)
            fff+=XianDuan.dis(new XianDuan(l[i]),r);
        if(Math.abs(fff-a.getWidth()-a.getHeight())<0.01) return true;
        return false;
    }


    public static boolean at(Rectangle a,Rectangle b)//矩形有交
    {
        if(at(a,b.getX(),b.getY())) return true;//包含
        Line[] l1=trans(a),l2=trans(b);
        for(int i=0;i<4;++i)//有一个交上了就是交上了
            for(int j=0;j<4;++j)
                if( XianDuan.at( new XianDuan(l1[i]) , new XianDuan(l2[j]) ) )
                    return true;
        return false;
    }

    public void tuiZhan() { stack.remove(stack.size()-1); flag.remove(flag.size()-1); }

    public void retreat()
    {
        if(stack.size()<=1) return;//不能空掉
        while(flag.get(flag.size()-1)!=0)//最后一步
        {
            int x=flag.get(flag.size()-1);

            if(x==1)//加入线段(所以要删掉)
            {
                Line y=(Line)stack.get(stack.size()-1);
                lineSet.getChildren().removeAll(y);//先删掉
                int i=(int) y.getEndX(),j=(int) y.getEndY();
                for(int k=0;k<lineMap[i][j].size();++k)
                    if(lineMap[i][j].get(k).equals(y))
                        lineMap[i][j].remove(k);//里面也删掉
            }
            if(x==-1)//原本删除，要加上
            {
                Line y=(Line)stack.get(stack.size()-1);
                lineSet.getChildren().add(y);
                //System.out.println(y.getStartX()+" "+y.getStartY()+"  "+y.getEndX()+" "+y.getEndY());
                lineMap[(int) y.getEndX()][(int) y.getEndY()].add(y);
            }

            if(x==2)//原本有的要处理掉
            {
                Group y=(Group) stack.get(stack.size()-1);
                right.getChildren().remove(y);
            }
            if(x==-2)
            {
                Group y=(Group) stack.get(stack.size()-1);
                right.getChildren().add(y);
            }

            tuiZhan();
        }
        if(stack.size()>1) tuiZhan();//不能空掉
    }

    public void clear()
    {
        for(int i=0;i<2000;++i)
            for(int j=0;j<1500;++j)
                lineMap[i][j].clear();
        stack.clear();
        flag.clear();
        nowLine.clear();
        right.getChildren().clear();
        lineSet.getChildren().clear();

    }

    public void press(MouseEvent event)//鼠标按下去
    {
        double x=event.getX(),y=event.getY();
        lstX=x;
        lstY=y;
        if(flagClick==2)//选择要单独处理
        {
            if(at(rectangle2,x,y)||at(toolBack[8],x,y))//选到了或者是删除
            {
                stack.add(null);//新的栈内容
                flag.add(0);
                for(Line o:nowLine)//开始的时候已经需要删了
                {
                    stack.add(o);
                    flag.add(-1);
                    if(at(toolBack[8],x,y))
                    {
                        lineSet.getChildren().remove(o);
                        int i=(int)o.getEndX(),j=(int)o.getEndY();
                        for(int k=0;k<lineMap[i][j].size();++k) //桶里也要删掉
                            if(lineMap[i][j].get(k).equals(o))
                                lineMap[i][j].remove(k);
                    }
                }
                for(Group o:nowGroup)//这里也是同理
                {
                    stack.add(o);
                    flag.add(-2);
                    if(at(toolBack[8],x,y)) right.getChildren().remove(o);
                }
                if(at(toolBack[8],x,y)) //全清掉
                {
                    nowGroup.clear();
                    nowLine.clear();
                    flagClick=1;
                    right.getChildren().remove(rectangle2);
                }
                return;
            } else {
                right.getChildren().removeAll(rectangle2);//移调
                nowGroup.clear();//没选到直接跑路
                nowLine.clear();
            }
        }

        flagClick=1;//跳过drag和release

        for(int i=0;i<6;++i)//换工具
        {
            if(at(toolBack[i],x,y))
            {
                toolBack[nowTool].setFill(Color.rgb(50,50,50));
                toolBack[i].setFill(black);
                nowTool=i;
                return;
            }
        }
        //撤退和清空
        if(at(toolBack[6],x,y)) { retreat(); return; }
        if(at(toolBack[7],x,y)) { clear(); return; }
        if(at(currentColor,x,y)) { caiDan1.showAndWait(); return;}//!

        for(int i=0;i<colorCnt;++i)
        {
            if(at(color[i],x,y))
            {
                nowColor=colorTool[i];
                currentColor.setFill(nowColor);
                return;
            }
        }

        if(x<=200&&y>=660)//神与佛祖的天和之作
        {
            TextInputDialog in1=new TextInputDialog();
            in1.setHeaderText(null);
            in1.setTitle("恭喜你发现彩蛋");
            in1.setContentText("尝试输入一个解析式看看？");
            Optional<String> toolStr= in1.showAndWait();
            if(!toolStr.isPresent()) return;
            String str=toolStr.get();
            TextInputDialog in2=new TextInputDialog();
            in2.setTitle("不，这不是彩蛋，这是神与佛祖的力量！");
            in2.setHeaderText(null);
            in2.setContentText("再设置一个定义域");
            Optional<String> toolString=in2.showAndWait();
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("此乃真·全取向最强大作业！");
            alert.setHeaderText(null);
            alert.setContentText("见证奇迹的时刻到了!");
            alert.showAndWait();
            double dom=toD(toolString.get());
            double[] shuZu={-1*dom,dom};
            TransformModel tran=new TransformModel(shuZu,shuZu);
            CalculateModel cal=new CalculateModel(str,"x","y",tran,1);
            int wide=600,high=720;
            Group g=new Group();
            PointMap mp=cal.map;
            for(int i=0;i<TransformModel.MAX_WINDOW_WIDTH+CalculateModel.BLANK*2;++i)
            {
                if (mp.nodes[i].getLengthAft()!=0) {
                    PointMap.Node pointer = mp.nodes[i].next;
                    while (pointer!=null) {
                        Line o=new Line(i+400,pointer.point.y+50,i+400,pointer.point.y+50);
                        o.setStroke(nowColor);
                        o.setFill(nowColor);
                        o.setStrokeWidth(kuan/1.67);
                        g.getChildren().add(o);
                        pointer=pointer.next;
                    }
                }
            }
            Circle a=new Circle(350,410,0),b=new Circle(1075,410,0);
            Line[] l=DAG.link(a,b,50);
            for(int i=0;i<3;++i)  { l[i].setStroke(Color.BLACK); g.getChildren().add(l[i]); }
            a=new Circle(700,750,0); b=new Circle(700,50,0);
            l=DAG.link(a,b,50);
            for(int i=0;i<3;++i)  { l[i].setStroke(Color.BLACK); g.getChildren().add(l[i]); }
            flag.add(0);
            stack.add(null);
            flag.add(2);
            stack.add(g);

            right.getChildren().add(g);
            //System.out.println(str);
            return;
        }

        if(x<=200) return;//左边的事就不掺和了吧
        flagClick=0;//要开始画了，不能跳过
        if(nowTool!=2) stack.add(null);//选择不需要改
        if(nowTool!=2) flag.add(0);//标记

        //说明在画图  0铅笔 1橡皮 2选择   3线段 4矩形 5圆形
        if(nowTool==0)//铅笔 新线
        {
            line=new Line(x,y,x,y);
            line.setStrokeWidth(kuan/2);
            line.setStroke(nowColor);//换颜色
        }
        if(nowTool==1)//橡皮
        {
            rectangle1.setX(x);
            rectangle1.setY(y);
            right.getChildren().add(rectangle1);// 连上
        }
        if(nowTool==2)//选择
        {//flagClick必须release的时候搞
            rectangle2.setTranslateX(0);//加上这句话就对了？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？
            rectangle2.setTranslateY(0);
            rectangle2.setX(x);
            rectangle2.setY(y);
            rectangle2.setWidth(0);
            rectangle2.setHeight(0);
            right.getChildren().add(rectangle2);//连上
        }
        if(nowTool==3)//画线段
        {
            line=new Line(x,y,x,y);
            line.setStroke(nowColor);
            line.setStrokeWidth(kuan);
            right.getChildren().add(line);
        }
        if(nowTool==4)//矩形
        {
            rectangle=new Rectangle(x,y,0,0);
            rectangle.setFill(null);//必须清掉
            rectangle.setStroke(nowColor);
            rectangle.setStrokeWidth(kuan);
            right.getChildren().add(rectangle);
        }
        if(nowTool==5)//圆
        {
            circle=new Circle(x,y,0);
            circle.setFill(null);
            circle.setStroke(nowColor);
            circle.setStrokeWidth(kuan);
            right.getChildren().add(circle);
        }
    }

    public void drag(MouseEvent event)
    {
        double x=event.getX(),y=event.getY();
        if(flagClick==2)//在选择
        {//全都Translate
            rectangle2.setTranslateX(x-lstX);
            rectangle2.setTranslateY(y-lstY);

            for(Line o:nowLine)//线
            {
                lineSet.getChildren().remove(o);//先清掉
                o.setTranslateX(x-lstX);
                o.setTranslateY(y-lstY);
                lineSet.getChildren().add(o);//换一下加回去
             }
            for(Group o:nowGroup)//组件平移  同理
            {
                right.getChildren().removeAll(o);//直接清除
                o.setTranslateX(x-lstX);
                o.setTranslateY(y-lstY);
                right.getChildren().add(o);//换一下再放进去
            }
            return;//特判直接跑路
        }

        if(flagClick==1) return;//干完事了直接跑


        //说明在画图  0铅笔 1橡皮 2选择   3线段 4矩形 5圆形
        if(nowTool==0)//铅笔
        {
            line.setEndX(x);
            line.setEndY(y);
            if(x<0||x>=2000||y<0||y>=1500)
            {
                line.setStartX(line.getEndX());
                line.setStartY(line.getEndY());
                return;
            }//强制爆掉
            lineMap[(int)x][(int)y].add(line);//按像素存线段
            stack.add(line);//表示事件栈加入一条线段
            flag.add(1);//1表示线段
            lineSet.getChildren().add(line);
            line=new Line(x,y,x,y);//必须新建线段来搞
            line.setStrokeWidth(kuan/2);
            line.setStroke(nowColor);
        }
        if(nowTool==1)//橡皮
        {
            rectangle1.setX(x);
            rectangle1.setY(y);
            for(int i=(int)(x-xpWide*0.5);i<=(int)(x+xpWide*0.5);++i)//橡皮枚举擦掉的点
            {
                for(int j=(int)(y-xpWide*0.5);j<=(int)(y+xpWide*0.5);++j)
                {
                    if(i<0||j<0||i>=2000||j>=1500) continue;//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    for(int k=0;k<lineMap[i][j].size();++k)
                    {
                        lineSet.getChildren().remove(lineMap[i][j].get(k));//消掉
                        stack.add(lineMap[i][j].get(k));//事件栈,这句话会把原本图形上的点删掉！？？？？？？？？？？？？？???????????
                        flag.add(-1);
                    }
                    lineMap[i][j].clear();
                    //lineSet.getChildren().removeAll(line);//枚举完了删掉
                }
            }
        }
        if(nowTool==2)//选择
        {//不在选择就很轻松
            rectangle2=setRectangle(rectangle2,lstX,lstY,x,y);//rectangle2.setWidth(x-lstX)rectangle2.setHeight(y-lstY);
        }
        if(nowTool==3)//线段
        {
            line.setEndX(x);
            line.setEndY(y);
        }
        if(nowTool==4)//矩形
        {
            rectangle=setRectangle(rectangle,lstX,lstY,x,y);//rectangle.setWidth(x-rectangle.getX());
        }
        if(nowTool==5)//半径设小的就好
        {
            double a=x-circle.getCenterX(),b=y-circle.getCenterY();
            circle.setRadius( Math.sqrt(a*a+b*b) );
        }
    }

    public static Rectangle setRectangle(double x1,double y1,double x2,double y2)
    {
        return setRectangle(rectangleWhite,x1,y1,x2,y2);//默认用空的
    }

    public static Line move(Line oo,double x,double y)
    {
        oo.setTranslateX(0);//必须先把它清空
        oo.setTranslateY(0);
        Line o=f(oo);
        o.setStartX(o.getStartX()+x);//平移大法
        o.setEndX(o.getEndX()+x);
        o.setStartY(o.getStartY()+y);
        o.setEndY(o.getEndY()+y);
        return o;
    }

    public static Rectangle move(Rectangle oo,double x,double y)
    {
        oo.setTranslateX(0);//清
        oo.setTranslateY(0);
        Rectangle o=f(oo);
        o.setX(o.getX()+x);//移
        o.setY(o.getY()+y);
        return o;
    }

    public static Circle move(Circle oo,double x,double y)
    {
        oo.setTranslateX(0);//清
        oo.setTranslateY(0);
        Circle o=f(oo);
        o.setCenterX(o.getCenterX()+x);//移
        o.setCenterY(o.getCenterY()+y);
        return o;
    }

    public static Text move(Text oo,double x,double y)
    {
        oo.setTranslateX(0);
        oo.setTranslateY(0);
        Text o=f(oo);
        o.setX(o.getX()+x);
        o.setY(o.getY()+y);
        return o;
    }


    public void release(MouseEvent event)
    {
        double x=event.getX(),y=event.getY();
        if(flagClick==1) return;//直接跑没的说

        if(flagClick==2)//移动特殊处理
        {
            right.getChildren().remove(rectangle2);
            rectangle2.setTranslateX(0);
            rectangle2.setTranslateY(0);
            rectangle2.setX(rectangle2.getX()+x-lstX);
            rectangle2.setY(rectangle2.getY()+y-lstY);
            //rectangle2=move(rectangle2,x-lstX,y-lstY);//不行了
            right.getChildren().add(rectangle2);//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1删掉解决一切bug
            for(int i=0;i<nowLine.size();++i)//线
            {
                Line o=nowLine.get(i);//不能auto，可能会出奇怪的bug
                lineSet.getChildren().remove(o);//先清掉
                lineMap[(int)o.getEndX()][(int)o.getEndY()].remove(0);//每加入一个移出最后一个，保证肯定是对的
                o=move(o,x-lstX,y-lstY);//更新
                if(o.getEndX()<0||o.getEndX()>=2000||o.getEndY()<0||o.getEndY()>=1500)//不行了
                {
                    nowLine.remove(i);//直接删掉，没什么好说的
                    --i;//删掉后指针也删掉
                    continue;
                }
                lineMap[(int)o.getEndX()][(int)o.getEndY()].add(o);//后入新位置
                lineSet.getChildren().add(o);//换一下加回去
                stack.add(o);
                flag.add(1);
                nowLine.set(i,o);//改回来
            }
            for(int i=0;i<nowGroup.size();++i)//组件平移  同理
            {
                Group o=nowGroup.get(i);
                o.setTranslateX(0);
                o.setTranslateY(0);
                right.getChildren().removeAll(o);//直接清除
                Object[] obj=o.getChildren().toArray();//找到组里的所有人
                o=new Group();//新建一个，因为数组没法形成
                //o.setTranslateX(0);//自己先清掉
                //o.setTranslateY(0);
                for(Object oo:obj)//每个组员依次变换
                {
                    if(oo instanceof Line) o.getChildren().add(move((Line)oo,x-lstX,y-lstY));
                    if(oo instanceof Rectangle) o.getChildren().add(move((Rectangle)oo,x-lstX,y-lstY));
                    if(oo instanceof Circle) o.getChildren().add(move((Circle)oo,x-lstX,y-lstY));
                    if(oo instanceof Text) o.getChildren().add(move((Text) oo,x-lstX,y-lstY));
                }
                right.getChildren().add(o);//换一下再放进去
                stack.add(o);
                flag.add(2);
                nowGroup.set(i,o);//及时改

            }
            return;//2很特殊，直接跑
        }

        //说明在画图  0铅笔 1橡皮 2选择   3线段 4矩形 5圆形
        if(nowTool==0)//铅笔 把最后一条加进去
        {
            line.setEndX(x);
            line.setEndY(y);
            if(x<0||x>=2000||y<0||y>=1500) { lineSet.getChildren().remove(line); return; }//强制不选这条
            lineMap[(int)x][(int)y].add(line);//按像素存线段
            stack.add(line);//表示事件栈加入一条线段
            flag.add(1);//1表示线段
            lineSet.getChildren().add(line);
            line=null;//事了拂衣去，深藏功与名
        }
        if(nowTool==1)//橡皮
        {//简单一行
            right.getChildren().remove(rectangle1);
        }
        if(nowTool==2)//选择
        {
            flagClick=2;//只有它最特殊
            nowLine.clear();
            nowGroup.clear();
            for(int i=Math.min((int)lstX,(int)x);i<=Math.max((int)lstX,x);++i)//先加入线段
            {
                for(int j=Math.min((int)lstY,(int)y);j<=Math.max((int)lstY,(int)y);++j)
                {
                    if(i<0||j<0||i>=2000||j>=1500) continue;
                    for(int k=0;k<lineMap[i][j].size();++k)
                    {
                        nowLine.add(lineMap[i][j].get(k));
                    }
                }
            }
            Object[] obj=right.getChildren().toArray();
            for(Object o:obj)//实现方法是每个组件里面放一个无意义长方形搞他
            {
                int flag=0;
                if(!(o instanceof Group)) return;//只有group需要搞
                Object[] nowObj=((Group)o).getChildren().toArray();//枚举每一个组件
                for(Object oo:nowObj)
                {//从虚空组件里找
                    if(oo instanceof Rectangle) if( at(rectangle2,(Rectangle)oo) ) flag=1;
                    if(oo instanceof Line) if(at(rectangle2,(Line)oo)) flag=1;
                    if(oo instanceof Circle) if(at(rectangle2,(Circle)oo)) flag=1;
                    //这里不需要Text
                }
                if(flag==1) nowGroup.add((Group)o);
            }
        }
        if(nowTool==3)//线段
        {
            right.getChildren().remove(line);
            Group o=new Group();
            o.getChildren().addAll(line);
            right.getChildren().add(o);//每个组件都创立一个矩形
            stack.add(o);//事件栈
            flag.add(2);
        }
        if(nowTool==4)//矩形
        {
            right.getChildren().remove(rectangle);
            Group o=new Group();
            o.getChildren().add(rectangle);
            right.getChildren().add(o);
            stack.add(o);
            flag.add(2);
        }
        if(nowTool==5)//圆
        {
            right.getChildren().remove(circle);
            Group o=new Group();
            double x1=circle.getCenterX(),y1=circle.getCenterY(),r=circle.getRadius();
            o.getChildren().addAll(circle);
            right.getChildren().add(o);
            stack.add(o);
            flag.add(2);
        }
    }



    public static void main(String[] args)
    {

        initLeft();
        initRight();
        launch(args);
    }
}
