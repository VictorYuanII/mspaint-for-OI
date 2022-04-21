import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Tu extends Tool
{
    public int n,flag,is[][];
    Circle a[];
    Line b[];


    public void work(double x,double y,double wide,double high,double r)
    {
        a[1]=f(new Circle(rd.nextInt( (int)(wide-r-r) )+x+r,rd.nextInt( (int)(high-r-r) )+y+r,r));
        i:for(int i=2;i<=n;++i)
        {
            flag=0;
            T:for(int T=1;T<=50;++T)//尝试加边
            {
                int lst=rd.nextInt(i-1)+1;//强制连边
                a[i]=f(new Circle(rd.nextInt((int)(wide-r-r))+x+r,rd.nextInt((int)(high-r-r))+y+r,r));
                for(int k=2;k<i;++k)
                    if(XianDuan.dis(new XianDuan(b[k]),new Node(a[i].getCenterX(),a[i].getCenterY()))<1.2*r)
                        continue T;
                Line line=link(a[i],a[lst]);
                k:for(int k=1;k<=i;++k)
                {
                    //System.out.println(k);
                    if(k==i) continue k;
                    Node node=new Node(a[k].getCenterX(),a[k].getCenterY());
                    if( Node.dis(node,new Node(a[i].getCenterX(),a[i].getCenterY()) ) <2.2*r ) continue T;
                    if(k==lst) continue k;
                    if(XianDuan.dis(new XianDuan(line),node)<r*1.2) continue T;//单方面宣布java最好用的功能没有之一
                }
                //System.out.println();
                flag=1;
                is[lst][i]=1;
                b[i]=line;
                break ;
            }
            if(flag==0) { is=new int[n+1][n+1]; work(x,y,wide,high,r); return; }// 重开 remake!
        }
        int mx=rd.nextInt(n)+2,cnt=0;
        T:for(int T=0;T<100&&cnt<=mx;++T)
        {
            int xx= rd.nextInt(n)+1,yy=rd.nextInt(n)+1;
            if(xx==yy) continue;
            Line line=link(a[xx],a[yy]);
            i:for(int i=1;i<=n;++i)
            {
                if(i==xx||i==yy) continue;
                Node node=new Node(a[i].getCenterX(),a[i].getCenterY());
                if(XianDuan.dis(new XianDuan(line),node)<r*1.2) continue T;
            }
            is[xx][yy]=1;
            ++cnt;
        }
    }

    public Tu()
    {
        n= rd.nextInt(4)+5;
        a=new Circle[n+1];
        b=new Line[n+1];
        is=new int[n+1][n+1];
        double t=Math.min( Math.min(rectangle2.getWidth(),rectangle2.getHeight())/10 ,50);
        if(flagClick==2) work(rectangle2.getX(),rectangle2.getY(),rectangle2.getWidth(),rectangle2.getHeight(),t);
        else work(250,50,900,700,50);
        for(int i=1;i<=n;++i) group.getChildren().add(a[i]);
        for(int i=1;i<=n;++i) group.getChildren().add(link(a[i],i));
        for(int i=1;i<=n;++i)
            for(int j=1;j<=n;++j)
                if(is[i][j]==1)
                    group.getChildren().add(link(a[i],a[j]));
    }
}
