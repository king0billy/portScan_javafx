import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * @author 22427(king0liam)
 * @version 1.0
 * @ClassName: portScaner
 * @Description
 * @Date: 2021/7/8 12:50
 * @since version-0.0
 */


//public class PortScanner{
//    public static void main(String[] args){
//        new UserInterface();
//    }
//}
//        System.out.println(InetAddress.getByName("www.baidu.com").getHostName());
//        System.out.println(InetAddress.getByName("www.baidu.com").getHostAddress());
//        System.out.println(InetAddress.getByName("www.baidu.com").getHostAddress());
//System.out.println(InetAddress.getByName("10.30.18.143").getHostName());
public class MainView extends Application {
    public static void main(String[] args) throws UnknownHostException {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("UserInterface.fxml"));
        Scene scene=new Scene(root);
        primaryStage.setTitle("java多线程端口扫描器");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
