import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PaiLie extends Tool
{

    public void work(double x,double y,double t,int n)
    {
        group=new Group();
        group.getChildren().add(f(new Rectangle(x,y,t*n,t)));
        for(int i=1;i<n;++i) group.getChildren().add( f(new Line(x+i*t,y,x+i*t,y+t)) );
        int a[]=new int[n];
        for(int i=0;i<n;++i) a[i]=i+1;
        for(int i=0;i<n;++i)
            for(int j=0;j<n-1;++j)
                if(rd.nextInt(2)==1)
                {
                    a[j]^=a[j+1];
                    a[j+1]^=a[j];
                    a[j]^=a[j+1];
                }
        for(int i=0;i<n;++i)
        {
            Text text=f(new Text(""+a[i]));
            text.setX(x+(i+0.35)*t);
            text.setY(y+0.65*t);
            text.setFont(new Font(t/2));
            group.getChildren().add( text );
        }
    }


    public PaiLie(int n)
    {
        if(Main.flagClick==2)
        {
            double t=Math.min(Main.rectangle2.getWidth()/n,Main.rectangle2.getHeight());
            work(Main.rectangle2.getX(),Main.rectangle2.getY(),t,n);
        }
        else work(400,400,100,n);//自己胡闹
    }


    public PaiLie()
    {
        this(Main.rd.nextInt(3)+6);
    }//6-8
}
