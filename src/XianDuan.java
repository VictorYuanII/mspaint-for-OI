import javafx.scene.shape.Line;

public class XianDuan
{
    Node a,b;
    public XianDuan(Node x,Node y) { a=x; b=y; }
    public XianDuan(double x1,double y1,double x2,double y2) { this(new Node(x1,y1),new Node(x2,y2)); }
    public XianDuan(Line x) { this(x.getStartX(),x.getStartY(),x.getEndX(),x.getEndY()); }
    public static boolean at(XianDuan X,XianDuan Y)//线段相交
    {
        double f1= Node.ji( Node.cha(Y.a,X.a) , Node.cha(Y.a,X.b) ) ;
        double f2= Node.ji( Node.cha(Y.b,X.a) , Node.cha(Y.b,X.b) ) ;
        double f3= Node.ji( Node.cha(X.a,Y.a) , Node.cha(X.a,Y.b) ) ;
        double f4= Node.ji( Node.cha(X.b,Y.a) , Node.cha(X.b,Y.b) ) ;
        if(f1*f2<=0&&f3*f4<=0) return true;
        return false;
    }
    public static double dis(XianDuan X,Node Y)// 只有平行线能这么用
    {
        if(X.a.x==X.b.x&&X.a.y==X.b.y) return Node.dis(X.a,Y);//特判
        Node v1=Node.cha(X.b,X.a),v2=Node.cha(Y,X.a),v3=Node.cha(Y,X.b);
        if(Node.dot(v1,v2)<=0) return v2.dis();
        if(Node.dot(v1,v3)>=0) return v3.dis();
        return Math.abs(Node.ji(v1,v2))/v1.dis();
        //return 6662333;//很大的数

        /*double ans=Math.min(Node.dis(X.a,Y),Node.dis(X.b,Y));

        if(X.a.x<=Y.x&&X.b.x>=Y.x) ans=Math.min(ans,Math.abs(X.a.y-Y.y));
        if(X.a.x>=Y.x&&X.b.x<=Y.x) ans=Math.min(ans,Math.abs(X.a.y-Y.y));
        //跨过
        if(X.a.y<=Y.y&&X.b.y>=Y.y) ans=Math.min(ans,Math.abs(X.a.x-Y.x));
        if(X.a.y>=Y.y&&X.b.y<=Y.y) ans=Math.min(ans,Math.abs(X.a.x-Y.x));*/

    }
}
