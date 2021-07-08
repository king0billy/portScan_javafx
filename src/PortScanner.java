import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
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
//        new EditorWin();
//    }
//}
public class PortScanner extends Application {
    public static void main(String[] args) throws UnknownHostException {
        System.out.println("本机\"IP\"地址："+ InetAddress.getLocalHost());
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("EditorWin.fxml"));
        Scene scene=new Scene(root);
        primaryStage.setTitle("多线程端口扫描器");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
