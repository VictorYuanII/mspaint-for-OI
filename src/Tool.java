import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Tool  extends Main
{
    public Group group=new Group();//都有

    public static double x=300,y=200;

    public static Line f(Line line)
    {
        line.setStroke(Main.nowColor);
        line.setFill(Main.nowColor);
        line.setStrokeWidth(Main.kuan);
        return line;
    }

    public static Rectangle f(Rectangle rectangle)
    {
        rectangle.setStroke(Main.nowColor);
        rectangle.setFill(null);
        rectangle.setStrokeWidth(Main.kuan);
        return  rectangle;
    }

    public  static Circle f(Circle circle)
    {
        circle.setStroke(Main.nowColor);
        circle.setFill(null);
        circle.setStrokeWidth(Main.kuan);
        return  circle;
    }

    public static Text f(Text text)
    {
        text.setStroke(Main.nowColor);
        text.setFill(Main.nowColor);
        text.setStrokeWidth(Main.kuan);
        return text;
    }


    public static Line link(Circle a, Circle b)
    {
        Line line=new Line(a.getCenterX(),a.getCenterY(),b.getCenterX(),b.getCenterY());//先连起来
        line.setFill(Main.nowColor);//static
        line.setStroke(Main.nowColor);
        line.setStrokeWidth(Main.kuan/2);
        double x=line.getEndX()-line.getStartX(),y=line.getEndY()-line.getStartY(),z=x*x+y*y;
        if(x==0&&y==0) return line;//小心爆掉
        double k1=a.getRadius()/Math.sqrt(z),k2=b.getRadius()/Math.sqrt(z);
        if(k1>=1||k2>=1) return line;
        line    =new Line(line.getStartX()+x*k1,line.getStartY()+y*k1,
                          line.getEndX()   -x*k2,line.getEndY()  -y*k2);//浴火重生
        return f(line);
    }

    public static Line link(Rectangle a,Rectangle b)
    {
        return f( new Line(a.getX()+a.getWidth()/2,a.getY()+a.getHeight()
                            ,b.getX()+b.getWidth()/2,b.getY()) );
    }

    public static Text link(Circle a,int n)
    {
        Text text=f(new Text(""+n));
        text.setX(a.getCenterX()-a.getRadius()*0.3);
        text.setY(a.getCenterY()+a.getRadius()*0.35);
        text.setFont(new Font(a.getRadius()));
        return text;
    }


}
