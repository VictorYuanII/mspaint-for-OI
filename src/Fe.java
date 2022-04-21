import java.util.Random;

public class Fe
{
    public static void main(String[] args)
    {
        Random rand=new Random();
        for(int i=0;i<=100;++i) System.out.println(rand.nextInt(2));//0或1
    }
}
