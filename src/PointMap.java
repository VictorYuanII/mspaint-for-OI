
import java.util.ArrayList;

/**
 * 一个专门用于存储点的map，目标是提高检索效率，便于存储
 * 同时快速实现对于各个属性的点的访问，可以更随意地访问各个组件
 */
public class PointMap {

    static final int initialSize = TransformModel.MAX_WINDOW_WIDTH+(int)CalculateModel.BLANK*2;
    public Node[] nodes;
    private int size;
    private static final int ALLOW_VAL = 20;
    TransformModel T_model;

    public PointMap() {
        initializeNodes();
    }
    public PointMap(TransformModel T_model) {
        this.T_model = T_model;
        initializeNodes();
    }

    private void initializeNodes() {
        nodes = new Node[initialSize];
        for (int i = 0; i < initialSize; i++) {
            Node node = new Node(0, i, new Point(0, 0));
            node.setLengthAft(0);
            nodes[i] = node;
        }
    }

    public void add(Point point) {
        int hash = point.hashCode();
        if (nodes[hash].next == null) {
            nodes[hash].next = new Node(1, hash, point);
            nodes[hash].setLengthAft(1);
            size++;
            return;
        }
        Node pointer = nodes[hash];
        do {
            pointer = pointer.next;
            if (pointer.point.equals(point))
                return;
        } while (pointer.next != null);
        pointer.next = new Node(pointer.index + 1, hash, point);
        nodes[hash].setLengthAft(nodes[hash].getLengthAft()+1);
        size++;
    }

    public Point[] toPoint() {
        ArrayList<Point> temp = new ArrayList<>();
        for (int i = 0; i < initialSize; i++) {
            Node pointer = nodes[i].next;
            while (pointer != null) {
                temp.add(new Point(pointer.point));
                pointer = pointer.next;
            }
        }
        Point[] out = new Point[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            out[i] = temp.get(i);
        }
        return out;
    }

    public static int[] dx={-1,-1,-1, 0, 0, 1, 1 ,1};
    public static int[] dy={-1, 0, 1,-1, 1,-1, 0, 1};
    public static int[][] flag=new int[2000][2000];

    public int dfs(int x,int y)
    {
        int ans=1;
        flag[x][y]=2;
        if(x==0||y==0) return 1;
        for(int i=0;i<8;++i)
            if(flag[x+dx[i]][y+dy[i]]==1)
                ans+=dfs(x+dx[i],y+dy[i]);
        return ans;
    }

    public boolean lianXuGraph() {
        if (this.getSize()<ALLOW_VAL)
            return false;
        flag=new int[2000][2000];
        for(int i=0;i<TransformModel.MAX_WINDOW_WIDTH+CalculateModel.BLANK*2;++i)
        {
            if (nodes[i].getLengthAft()!=0) {
                Node pointer = nodes[i].next;
                while (pointer!=null) {
                    flag[i][(int)pointer.point.y]=1;
                    pointer = pointer.next;
                }
            }
        }
        for(int i=0;i<TransformModel.MAX_WINDOW_WIDTH+CalculateModel.BLANK*2;++i)
        {
            for(int j=0;j<TransformModel.MAX_WINDOW_HEIGHT+CalculateModel.BLANK*2;++j)
            {
                if(flag[i][j]!=1) continue;
                if(dfs(i,j)<ALLOW_VAL) return false;
            }
        }
        return true;
        /*int minX = 0,maxX = nodes.length-1;
        boolean s1 = true,s2 = true;
        while (s1 || s2) {
            if (nodes[minX].getLengthAft()==0) {
                if (s1) minX++;
            } else s1 = false;
            if (nodes[maxX].getLengthAft()==0) {
                if (s2) maxX--;
            } else s2 = false;
        }
        int find = minX;
        int flag = 0;
        while (find++ < maxX) {
            if (nodes[find].getLengthAft() == 0) {
                if (nodes[find-1].getLengthAft()!=0) {
                    double res = nodes[find-1].next.point.y;
                    if (!(res < CalculateModel.BLANK || res > (CalculateModel.BLANK + TransformModel.MAX_WINDOW_HEIGHT))) {
                        //不是合法的间断点
                        flag++;
                        System.out.print(find+" ");
                    }
                }
                if (nodes[find+1].getLengthAft()!=0) {
                    double res = nodes[find+1].next.point.y;
                    if (!(res < CalculateModel.BLANK || res > (CalculateModel.BLANK + TransformModel.MAX_WINDOW_HEIGHT))) {
                        //不是合法的间断点
                        flag++;
                        System.out.print(find+" ");
                    }
                }
            }
            if (flag==3) return false;
        }
        return true;*/
    }

    public int getSize() {return size;}

    public Point findClosestPointRight(int hash, double y) {
        int cur = hash;
        double ERROR_RANGE = 20;
        Point des = new Point();
        a:
        while (cur++ < nodes.length) {
            Node pointer = nodes[cur-1].next;
            while (pointer != null) {
                if (Math.abs(pointer.point.y - y) < ERROR_RANGE) {
                    des = new Point(pointer.point);
                    break a;
                } else pointer = pointer.next;
            }
        }
        return des;
    }

    /**
     * 一个内部类，用于实现PointMap数据结构
     */
    static class Node implements Comparable<Node> {
        int hash, index, lengthAft;
        Point point;
        Node next;

        public Node() {}

        public Node(int index, int hash, Point point) {
            this.index = index;
            this.hash = hash;
            this.point = point;
        }

        public void setLengthAft(int num) {this.lengthAft = num;}
        public int getLengthAft() {return this.lengthAft;}

        public int compareTo(Node node) {
            return this.point.compareTo(node.point);
        }
    }

    public static class Point implements Comparable<Point> {
        double x, y;
        double wuCha;

        public Point() {
        }

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public Point(double x, double y, double wuCha) {
            this.x = x;
            this.y = y;
            this.wuCha = wuCha;
        }

        public Point(Point point) {
            this.x = point.x;
            this.y = point.y;
            this.wuCha = point.wuCha;
        }

        public double getX() {return x;}
        public double getY() {return y;}
        public double getWuCha() {return wuCha;}

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
        }

        public int hashCode() {
            return (int) x;
        }

        public int compareTo(Point p) {
            if (this.x == p.x)
                return (int) (this.y - p.y);
            else return (int) (this.x - p.x);
        }
    }
}
