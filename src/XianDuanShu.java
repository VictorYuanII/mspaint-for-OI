import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class XianDuanShu extends Tool
{
    public Rectangle dfs(double x,double y,double t,int n,int m,int l,int r)//横着n，竖着m
    {
        //System.out.println(x+" "+y+" "+t+" "+n+" "+m+" "+l+" "+r);
        Rectangle now=f(new Rectangle(x+t/4,y,(n-0.25)*t,0.8*t));
        group.getChildren().add(now);
        if(l==r)
        {
            Text text=f(new Text(""+l) );
            text.setX(x+0.5*t);
            text.setY(y+0.65*t);
            text.setFont(new Font(t/2));
            group.getChildren().add(text);
            return now;
        }
        int mid=(l+r)/2;
        group.getChildren().add(link(now,dfs(x,y+t*1.2,t,mid-l+1,m-2,l,mid)));
        group.getChildren().add(link(now,dfs(x+(mid-l+1)*t,y+t*1.2,t,r-mid,m-2,mid+1,r)));
        return now;
    }

    public XianDuanShu(int n)
    {
        double t=100;
        if(flagClick==2)
        {
            t=Math.min(rectangle2.getWidth()/n,rectangle2.getHeight()/4.4);
            dfs(rectangle2.getX(),rectangle2.getY(),t,n,8,1,n);
            return;
        }
        dfs(300,200,t,n,8,1,n);

    }
    public XianDuanShu() {this(Main.rd.nextInt(4)+5);}//5-8
}
