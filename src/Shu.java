import javafx.scene.shape.Circle;

public class Shu extends Tool
{
    public int[] fa,siz,dep;
    public int[][] a;
    public int mx,n;

    public Circle dfs (double x, double y, double t, int now)
    {
        Circle circle=f( new Circle(x+(siz[now]*0.5)*t,y+t*0.5,t*0.4) );
        int nowSize=0;
        for(int i=1;i<=n;++i)
        {
            if(a[now][i]==0) continue;
            Circle nxt=nxt=dfs(x+(nowSize+(dep[i]%2))*t,y+t*1.5,t,i);
            group.getChildren().add(link(circle,nxt));
            nowSize+=siz[i];
        }
        group.getChildren().add(circle);
        group.getChildren().add(link(circle,now));
        return circle;
    }

    public Shu()
    {
        double t=70;
        n=rd.nextInt(4)+6;
        fa=new int[n+1];
        siz=new int[n+1];
        dep=new int[n+1];
        a=new int[n+1][n+1];
        for(int i=1;i<=n;++i) siz[i]=1;
        for(int i=2;i<=n;++i) fa[i]=rd.nextInt(i-1)+1;
        dep[1]=1;
        for(int i=2;i<=n;++i) dep[i]=dep[fa[i]]+1;
        for(int i=n;i>=2;--i) siz[fa[i]]+=siz[i];
        for(int i=1;i<=n;++i) mx=Math.max(dep[i],mx);//最深的
        for(int i=2;i<=n;++i) a[fa[i]][i]=1;//存一下
        if(flagClick==2)
        {
            t=Math.min(rectangle2.getWidth()/n,rectangle2.getHeight()/(mx*1.5));
            dfs(rectangle2.getX(),rectangle2.getY(),t,1);
        }
        else dfs(400,100,t,1);
    }
}
