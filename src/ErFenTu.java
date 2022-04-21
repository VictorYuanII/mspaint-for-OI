import javafx.scene.shape.Circle;

public class ErFenTu extends Tool
{
    public void work(double x,double y,double t,int n,int m)
    {
        double yy=y+Math.max(n,m)*t,mx=Math.max(n,m);
        Circle[] l=new Circle[n];
        Circle[] r=new Circle[m];
        for(int i=0;i<n;++i) l[i]=f( new Circle(x+t*0.5,y+mx/n*(i+0.5)*t,t/2.5) );
        for(int i=0;i<m;++i) r[i]=f( new Circle(x+t*3,y+mx/m*(i+0.5)*t,t/2.5) );
        for(int i=0;i<n;++i) group.getChildren().add(link(l[i],r[rd.nextInt(m)]));
        for(int i=0;i<m;++i) group.getChildren().add(link(l[rd.nextInt(n)],r[i]));
        for(int i=0;i<n;++i) group.getChildren().add(l[i]);
        for(int i=0;i<m;++i) group.getChildren().add(r[i]);
        for(int i=0;i<n;++i) group.getChildren().add(link(l[i],i+1));
        for(int i=0;i<m;++i) group.getChildren().add(link(r[i],i+1));
    }

    public ErFenTu(int n,int m)
    {
        double t=90;
        if(flagClick==2)
        {
            t=Math.min(rectangle2.getWidth()/3.5,rectangle2.getHeight()/Math.max(n,m));
            work(rectangle2.getX(),rectangle2.getY(),t,n,m);
            return ;
        }
        work(500,100,t,n,m);
    }
    public ErFenTu() { this(rd.nextInt(5)+2,rd.nextInt(5)+2); }//5-8
}
