import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class DAG extends Tool
{

    public int n,dep[],fa[],mx=0,tong[],now[],is[][],vis[],flag;

    public Circle[] circle;

    public static Line[] link(Circle a,Circle b,double t)//a到b有向边
    {
        double jiao=30*Math.PI/180,cos=Math.cos(jiao),sin=Math.sin(jiao);
        Line[] now=new Line[3];
        now[0]=link(b,a);
        double x0=now[0].getStartX(),y0=now[0].getStartY();
        //Node f1=new Node(now[0].getEndX()-now[0].getStartX(), now[0].getEndY()-now[0].getStartY());
        double x=now[0].getEndX()-now[0].getStartX();
        double y=now[0].getEndY()-now[0].getStartY();
        double x1=x*cos-y*sin,y1=x*sin+y*cos,t1=t/Math.sqrt(x1*x1+y1*y1);
        now[1]=f(new Line(x0,y0,x0+x1*t1,y0+y1*t1));
        jiao=-30*Math.PI/180; cos=Math.cos(jiao); sin=Math.sin(jiao);
        double x2=x*cos-y*sin,y2=x*sin+y*cos,t2=t/Math.sqrt(x2*x2+y2*y2);
        now[2]=f(new Line(x0,y0,x0+x2*t2,y0+y2*t2));
        return  now;
    }

    public int dfs(int now)
    {
        if(vis[now]==1) return 0;
        int ans=1;
        vis[now]=1;
        for(int i=1;i<=n;++i)
            if(is[now][i]==1)
                ans+=dfs(i);
        return ans;
    }

    public void work(double x,double y,double t,int n,int m)
    {
        circle[0]=new Circle();
        for(int i=1;i<=this.n;++i)
        {
            circle[i]=f(new Circle(x+(dep[i]-0.5)*t*2,y+2*t*m/tong[dep[i]]*(now[dep[i]]+0.5),t*0.5));
            now[dep[i]]++;
            group.getChildren().add(circle[i]);
            group.getChildren().add(link(circle[i],i));
            if(fa[i]==0) continue;
            Line[] abc=link(circle[ fa[i] ],circle[i],t/4);
            for(int j=0;j<3;++j) group.getChildren().add(abc[j]);
        }
        int sup=rd.nextInt(this.n)+2,cnt=0;//this.n缓解一下
        T:for(int T=1;T<=100&&(flag==0||cnt<=mx);++T)
        {// if(flag!=0&&cnt>=mx) return;
            int xx=rd.nextInt(this.n)+1,yy=rd.nextInt(this.n)+1;
            if(dep[xx]==dep[yy]) continue;
            if(dep[xx]>dep[yy]) { xx^=yy; yy^=xx; xx^=yy; }//swap;
            //System.out.println(xx+" "+yy+" "+dep[xx]+" "+dep[yy]);
            Line[] abc=link(circle[xx],circle[yy],t/4);
            i:for(int i=1;i<=this.n;++i)
            {
                if(i==xx||i==yy) continue;
                Node node=new Node(circle[i].getCenterX(),circle[i].getCenterY());
                if( XianDuan.dis(new XianDuan(abc[0]),node) < 0.5*t ) continue T;
            }
            ++cnt;
            for(int i=0;i<3;++i)  group.getChildren().add(abc[i]);
            is[xx][yy]=1; is[yy][xx]=1;
            for(int i=1;i<=this.n;++i) vis[i]=0;
            int abcde=dfs(1);
            if(abcde==this.n) flag=1;
            //System.out.println(abcde+" "+T+" "+cnt+" "+mx+" "+flag);
        }
    }

    public DAG()
    {
        mx=0; flag=0;
        n=rd.nextInt(4)+6;//6-9
        //System.out.println(n);
        fa=new int[n+1];
        now=new int[n+1];
        vis=new int[n+1];
        dep=new int[n+1];
        tong=new int[n+1];
        circle=new Circle[n+1];
        is=new int[n+1][n+1];
        for(int i=1;i<=n;++i) fa[i]=rd.nextInt(i);
        for(int i=1;i<=n;++i) is[ fa[i] ][i]=is[i][ fa[i] ]=1;
        for(int i=1;i<=n;++i) dep[i]=dep[fa[i]]+1;
        for(int i=1;i<=n;++i) mx=Math.max(mx,dep[i]);
        for(int i=1;i<=n;++i) tong[dep[i]]++;
        for(int i=1;i<=n;++i) tong[0]=Math.max(tong[0],tong[i]);
        if(flagClick==2)
        {
            double t=Math.min( rectangle2.getWidth()/(2*mx) , rectangle2.getHeight()/(2*tong[0]) );
            work(rectangle2.getX(),rectangle2.getY(),t,mx,tong[0]);
        }
        else work(300,0,70,mx,tong[0]);
    }


}
