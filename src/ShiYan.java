import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.Random;

public class ShiYan extends Application
{
    public TextInputDialog input=new TextInputDialog();
    public static Random rd=new Random();
    public Image pencil=new Image("铅笔.png");
    public ImageView pencilView=new ImageView(pencil);
    public Group root=new Group(new Circle(600,400,200,Color.CORAL));//空白的话就没问题了//根在最下面
    public Line line=new Line(700,400,500,700);
    public Circle circle=new Circle(200,300,200);
    public Rectangle rectangle=new Rectangle(300,400,200,100);
    public Text text=new Text(80,700,"ff0");
    public Button button=new Button("随机"),cut=new Button("cut"),link=new Button("link");
    public Line nowLine;
    public int cnt=0;

    //@Override
    public void start(Stage primaryStage)
    {
        circle.setFill(Color.WHITE);
        line.setStroke(Color.DARKTURQUOISE);
        rectangle.setStroke(Color.FORESTGREEN);
        rectangle.setStrokeWidth(2);
        rectangle.setFill(null);
        rectangle.getStrokeDashArray().addAll(10.0);//虚化图形，不知道原理
        rectangle.setArcHeight(20);
        rectangle.setArcWidth(20);

        text.setFill(Color.AQUA);
        text.setFont(new Font(30));

        button.setOnAction(this::processButtonPress);
        button.setPrefWidth(100);
        button.setPrefHeight(100);
        pencilView.setX(50); pencilView.setY(50);
        pencilView.setScaleX(1.2);
        pencilView.setScaleY(1.2);

        cut.setTranslateX(20);
        cut.setTranslateY(200);
        link.setTranslateX(300);
        link.setTranslateY(150);
        link.setTranslateX(300);
        link.setTranslateY(150);//不可叠加
        cut.setOnAction(this::press);
        link.setOnAction(this::press);

        Group leaf=new Group(line,circle,rectangle);//circle载rectangle下面
        leaf.setTranslateX(200);//这里对下面setScaleX产生了影响
        root.getChildren().add(text);
        root.getChildren().addAll(leaf,button,link,cut);
        root.getChildren().add(pencilView);
        Scene scene=new Scene(root,1200,800,Color.BLACK);
        //scene.setFill(Color.RED);
        scene.setOnMouseClicked(this::moveLine);
        scene.setOnMousePressed(this::ysl);
        scene.setOnMouseDragged(this::YSL);
        scene.setOnKeyPressed(this::delete);

        primaryStage.setTitle("实验");
        primaryStage.setScene(scene);
        primaryStage.show();

        Object[] obj=root.getChildren().toArray();//找到取数组的方法
        for(Object o:obj)
        {
            if(o instanceof Text) System.out.println(1);//事实证明可行
            else System.out.println(0);
        }
    }

    public void delete(KeyEvent event)
    {
        if(event.getCode()== KeyCode.BACK_SPACE) root.getChildren().removeAll(text);
    }

    public void ysl(MouseEvent event)
    {
        nowLine=new Line(event.getX(),event.getY(),event.getX(),event.getY());
        nowLine.setFill(Color.BLACK);//什么都没有发生
        nowLine.setStroke(Color.RED);//变好了
        root.getChildren().add(nowLine);
    }
    public void YSL(MouseEvent event)
    {
        ++cnt;
        nowLine.setEndX(event.getX());
        nowLine.setEndY(event.getY());
        ysl(event);
    }

    public void moveLine(MouseEvent event)
    {
        input.setHeaderText("abc");
        input.setTitle("123");
        input.setContentText("ABC");
       // Optional<String> numString=input.showAndWait();
        Alert answer=new Alert(Alert.AlertType.INFORMATION);
        answer.setHeaderText(null);
        answer.setContentText("12342332532655");
       // answer.setContentText("");
        answer.showAndWait();
        if(event.getClickCount()==2) return;//双击不管直接跑
        text.setText(""+cnt+" "+line.getStartX()+" "+line.getStartY()+"   "+event.getX()+" "+event.getY());
        line.setEndX(event.getX()-200);//????为什么-200
        line.setEndY(event.getY());
    }

    public void processButtonPress(ActionEvent event)
    {
        circle.setScaleX(1.1);
        circle.setScaleY(1.1);//只有第一次会生效
        //root.getChildren().removeAll(pencilView);
        pencilView.setX(rd.nextInt()%600+600);
        pencilView.setY(rd.nextInt(800));
    }

    public void press(ActionEvent event)
    {
        Group copy=new Group(pencilView);//居然可以group？
        if(event.getSource()==cut) root.getChildren().removeAll(copy);//我理解为指针一样所以操作一样
        else { if(!root.getChildren().contains(pencilView)) root.getChildren().add(pencilView); }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
