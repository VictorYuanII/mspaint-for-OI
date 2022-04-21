
public class Node//计算几何
{
    double x,y;
    public Node(double a,double b) { x=a; y=b; }
    public  double dis()//自己的长度
    {
        return Math.sqrt(x*x+y*y);
    }


    public static  double dis(Node a,Node b)//距离
    {
        double x=a.x-b.x,y=a.y-b.y;
        return Math.sqrt(x*x+y*y);
    }
    public static Node cha(Node a,Node b)//减法
    {
        return new Node(a.x-b.x,a.y-b.y);
    }
    public static double ji(Node a,Node b)//叉积
    {
        return a.x*b.y-a.y*b.x;
    }
    public static double dot(Node a,Node b)//点积
    {
        return a.x*b.x+a.y*b.y;
    }
}
