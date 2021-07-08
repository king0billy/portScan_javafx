import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author 22427(king0liam)
 * @version 1.0
 * @ClassName: actionWin
 * @Description
 * @Date: 2021/7/8 12:53
 * @since version-0.0
 */
public class EditorWin extends JFrame {
    //private JLabel startIp,endIp,l_startPort,l_endPort,l_portOfThread ,showResult ,empty,type ,status;
    //private JTextField f_startIp,f_endIp,f_startPort,f_endPort,f_portOfThread ;
    private JScrollPane result ;
    private JComboBox comboBox ;
   // private JButton startScanner,exitScanner ,clear,reset;
    private JPanel top,bottom ;
    //private JTextArea message ;
    private String startIpStr ,endIpStr;
    private int startPort,endPort,portOfThread ,threadNum ;
    @FXML Button startScanner;
    @FXML Button export;
    @FXML Button searchIp;
    @FXML TextField f_startIp;
    @FXML TextField f_endIp;
    @FXML TextField f_startPort;
    @FXML TextField f_endPort;
    @FXML TextField f_portOfThread;
    @FXML TextField domainName;
    @FXML Label status;
    @FXML TextArea message;
    @FXML void EventOnScan (javafx.event.ActionEvent event){
        startIpStr = f_startIp.getText().trim() ;   //得到输入的Ip
        if(checkIP(startIpStr)){//判断是否为数字
            try{
                startPort = Integer.parseInt(f_startPort.getText().trim()) ;
                endPort =  Integer.parseInt(f_endPort.getText().trim()) ;
                portOfThread  =Integer.parseInt(f_portOfThread.getText().trim())  ;
                threadNum = (endPort-startPort)/portOfThread+1 ;
                //判断端口号的范围
                if(startPort<0||endPort>65535||startPort>endPort){
                    pop("端口号范围：0~65535,并且最大端口号应大于最小端口号！") ;
                }
                else{
                    if(portOfThread>endPort-startPort||portOfThread<1){
                        pop("每个线程扫描的端口数不能大于所有的端口数且不能小于1") ;
                    }else{
                        if(f_endIp.getText().equals("")){         //if(((String) comboBox.getSelectedItem()).equals("地址")){
                            Platform.runLater(() -> message.appendText("************************************************************"+"\n"));
                            Platform.runLater(() -> message.appendText("正在扫描  "+startIpStr+"          每个线程扫描端口个数"+portOfThread+"\n"+"开启的线程数"+threadNum+"\n"));
                            Platform.runLater(() -> message.appendText("开始端口  "+startPort+"         结束端口" +endPort+"\n"));
                            Platform.runLater(() -> message.appendText("主机名:"+getHostname(startIpStr)+"\n"));
                            Platform.runLater(() -> message.appendText("开放的端口如下："+"\n"));
                            for(int i = startPort;i <= endPort; i++) {
                                if((i + portOfThread) <= endPort) {
                                    new Scan(i, i + portOfThread,startIpStr).start();
                                    i += portOfThread;
                                }
                                else {
                                    new Scan(i, endPort,startIpStr).start();
                                    i += portOfThread;
                                }
                            }
                        }else{
                            endIpStr = f_endIp.getText() ;
                            if(checkIP(endIpStr)){
                                //扫描Ip地址段
                                Set ipSet = new HashSet<Object>() ;
                                int start = Integer.valueOf(startIpStr.split("\\.")[3]);
                                int end = Integer.valueOf(endIpStr.split("\\.")[3]);
                                String starts = startIpStr.split("\\.")[0]+"."+startIpStr.split("\\.")[1]+"."+startIpStr.split("\\.")[2];
                                //生成IP地址
                                if(start>end){pop("请输入正确的Ip地址") ;}
                                for(int i = start;i<=end;i++){
                                    ipSet.add(starts+"."+i) ;    //地海段的每个地址存入集合
                                }
                                for (Object str : ipSet) {
                                    new ScanIp(str.toString()).start() ;
                                }
                            }else{
                                pop("请输入正确的Ip地址") ;
                            }
                        }
                    }
                }
            }
            catch(NumberFormatException e1){
                pop("错误的端口号或端口号和线程数必须为整数") ;
            }
        }
        else{
            pop("请输入正确的Ip地址") ;
        }
    }
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        if(e.getSource()==startScanner){ //点击扫描按钮
//            //点击时刻
//            startIpStr = f_startIp.getText().trim() ;   //得到输入的Ip
//            if(checkIP(startIpStr)){
//                //判断是否为数字
//                try{
//                    startPort = Integer.parseInt(f_startPort.getText().trim()) ;
//                    endPort =  Integer.parseInt(f_endPort.getText().trim()) ;
//                    portOfThread  =Integer.parseInt(f_portOfThread.getText().trim())  ;
//                    threadNum = (endPort-startPort)/portOfThread+1 ;
//                    //判断端口号的范围
//                    if(startPort<0||endPort>65535||startPort>endPort){
//                        pop("端口号范围：0~65535,并且最大端口号应大于最小端口号！") ;
//                    }
//                    else{
//                        if(portOfThread>endPort-startPort||portOfThread<1){
//                            pop("每个线程扫描的端口数不能大于所有的端口数且不能小于1") ;
//                        }else{
//                            if(((String) comboBox.getSelectedItem()).equals("地址")){
//                                Platform.runLater(() -> message.appendText("************************************************************"+"\n"));
//                                Platform.runLater(() -> message.appendText("正在扫描  "+startIpStr+"          每个线程扫描端口个数"+portOfThread+"\n"+"开启的线程数"+threadNum+"\n"));
//                                Platform.runLater(() -> message.appendText("开始端口  "+startPort+"         结束端口" +endPort+"\n"));
//                                Platform.runLater(() -> message.appendText("主机名:"+getHostname(startIpStr)+"\n"));
//                                Platform.runLater(() -> message.appendText("开放的端口如下："+"\n"));
//                                for(int i = startPort;i <= endPort; i++) {
//                                    if((i + portOfThread) <= endPort) {
//                                        new Scan(i, i + portOfThread,startIpStr).start();
//                                        i += portOfThread;
//                                    }
//                                    else {
//                                        new Scan(i, endPort,startIpStr).start();
//                                        i += portOfThread;
//                                    }
//                                }
//                            }else{
//                                endIpStr = f_endIp.getText() ;
//                                if(checkIP(endIpStr)){
//                                    //扫描Ip地址段
//                                    Set ipSet = new HashSet<Object>() ;
//                                    int start = Integer.valueOf(startIpStr.split("\\.")[3]);
//                                    int end = Integer.valueOf(endIpStr.split("\\.")[3]);
//                                    String starts = startIpStr.split("\\.")[0]+"."+startIpStr.split("\\.")[1]+"."+startIpStr.split("\\.")[2];
//                                    //生成IP地址
//                                    if(start>end){pop("请输入正确的Ip地址") ;}
//                                    for(int i = start;i<=end;i++){
//                                        ipSet.add(starts+"."+i) ;    //地海段的每个地址存入集合
//                                    }
//                                    for (Object str : ipSet) {
//                                        new ScanIp(str.toString()).start() ;
//                                    }
//                                }else{
//                                    pop("请输入正确的Ip地址") ;
//                                }
//
//                            }
//                        }
//                    }
//                }
//                catch(NumberFormatException e1){
//                    pop("错误的端口号或端口号和线程数必须为整数") ;
//                }
//            }
//            else{
//                pop("请输入正确的Ip地址") ;
//            }
//        }
////        else if(e.getSource()==reset){
////            f_startIp.setText("") ;
////            f_startPort.setText("") ;
////            f_endPort.setText("") ;
////            f_portOfThread.setText("") ;
////        }
////        else if(e.getSource()==clear){
////            message.setText("") ;
////            System.out.println((String) comboBox.getSelectedItem());
////        }
////        else if(e.getSource()==exitScanner){
////            System.exit(1);
////        }else if(e.getSource()==comboBox){
////            String type=(String) comboBox.getSelectedItem();
////            if(type.equals("地址")){
////                endIp.setVisible(false) ;
////                f_endIp.setVisible(false) ;
////                startIp.setText("扫描的Ip") ;
////            }else{
////                endIp.setVisible(true) ;
////                f_endIp.setVisible(true) ;
////                startIp.setText("开始Ip") ;
////            }
////        }
//    }
    //todo task 扫描端口地址的线程
    class Scan extends Thread{
        int maxPort, minPort;
        String Ip;
        Scan(int minPort, int maxPort,String Ip){
            this.minPort=minPort ;
            this.maxPort=maxPort ;
            this.Ip=Ip;
        }
        @SuppressWarnings("unchecked")
        public  void run() {
            //new MyTask();
//            Socket socket = null ;
//            for(int i = minPort;i<maxPort;i++){
//                try {
//                    socket=new Socket(Ip, i);
//                    //todo
//                    findInfoByPort(i ,Ip);//通过端口号调用数据库信息
//                    Platform.runLater(() -> message.appendText("\n"));
//                    socket.close();
//                } catch (Exception e) {
//                    //System.out.println(i+"端口没开放");
//                    Platform.runLater(() -> message.appendText(""));
//                }
//                int finalI = i;
//                Platform.runLater(() -> status.setText("正在扫描"+ finalI));
//            }
//            Platform.runLater(() -> status.setText("扫描结束"));
        }
    }
    //扫描Ip地址段查看合法Ip的线程
    class ScanIp extends Thread{
        String  Ip ;
        ScanIp(String  Ip ){
            this.Ip = Ip ;
        }
        public synchronized void run(){
            try {
                for(int i = startPort;i <= endPort; i++) {
                    //扫描开放的Ip
                    InetAddress.getByName(Ip);
                    if((i + portOfThread) <= endPort) {
                        new Scan(i, i + portOfThread,Ip).start();
                        i += portOfThread;
                    }
                    else {
                        new Scan(i, endPort,Ip).start();
                        i += portOfThread;
                    }
                }
            } catch (Exception e) {
                System.out.println(Ip+"\n");
            }

        }
    }
    //根据端口号，查询数据库中端口号的相应信息并显示在文本域之中
    synchronized void findInfoByPort(int port,String Ip){
        Platform.runLater(() -> message.appendText("-----------------------"+"Ip"+Ip+"的"+"端口号"+port+"------------------------------------"+"\n"));
        Connection conn ;
        PreparedStatement pst  ;
        ResultSet rs  ;
        conn = JdbcUtils.getConnection() ;//与数据库建立连接，获取Connection对象
        String sql = "Select * from inna where PortNumber ="+port;
        try {
            pst = conn.prepareStatement(sql) ;
            rs = pst.executeQuery() ;
            String totalStr = null ;
            while(rs.next()){
                String transportProtocol=rs.getString("TransportProtocol");
                String serviceName = rs.getString("ServiceName");
                String description = rs.getString("Description") ;
                if(description.equals("Reserved")||serviceName.equals("Null")){}
                else{
                    Platform.runLater(() -> message.appendText("UDP/TCP："+transportProtocol+"\t\t")) ;
                    Platform.runLater(() -> message.appendText("端口信息："+serviceName+"\t")) ;
                    Platform.runLater(() -> message.appendText("端口说明："+description+"\n"));
                    totalStr =  totalStr+serviceName ;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 判断输入的IP是否合法
    private boolean checkIP(String str) {
        Pattern pattern = Pattern
                .compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]"
                        + "|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
        return pattern.matcher(str).matches();
    }
    //根据Ip获得主机名、
    public static  synchronized String getHostname(String host){
        InetAddress addr ;
        try {
            addr = InetAddress.getByName(host);
            return addr.getHostName();
        } catch (UnknownHostException e) {
            return "网络不通或您输入的信息无法构造InetAddress对象！";
        }
    }
    public static void  pop(String string) {
        Label lbl = new Label("Hint！");
        TextArea textArea=new TextArea(string);
        lbl.setFont(Font.font("Cambria", 20));
        lbl.setWrapText(true);
        FlowPane pane1 = new FlowPane();
        pane1.setHgap(20);
        pane1.setStyle("-fx-background-color:tan;-fx-padding:10px;");//组件加入面板
        pane1.getChildren().addAll(lbl,textArea);
        Scene creatingScene = new Scene(pane1, 600, 400);
        Stage PopStage = new Stage();
        //设置为弹窗类型
        PopStage.initModality(Modality.APPLICATION_MODAL);
        PopStage.setScene(creatingScene);
        PopStage.show();
    }
    @FXML void EventOnExport (javafx.event.ActionEvent event){
        FileChooser fc=new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fc.getExtensionFilters().add(extFilter);

        Stage reflectedStage = (Stage) ((Node) event.getSource()).getScene().getWindow();//【反射】通过event获得当前node然后一路get window
        //fc.showOpenDialog(reflectedStage);
        File saveFile = fc.showSaveDialog(reflectedStage);
//        String content=message.getText();
//        FileOutputStream  fileOutStream=new FileOutputStream(saveFile);
//        try{
//            fileOutStream.write(content.getBytes());
//
//        }catch(IOException ioe){
//            pop("写入文件错误");
//        }
//        finally{
//    try{
//        fileOutStream.close();
//    }catch(IOException ioe2){
//
//    }
//}
                try {
                    FileWriter writeOut = new FileWriter(saveFile);
                    writeOut.write(message.getText());
                    writeOut.close();
                    pop("保存成功!");
                }
                catch (IOException ex) {
                    pop("保存失败!");
                }
}
@FXML void EventOnGetIp (javafx.event.ActionEvent event) {
            BufferedReader br = null;
            try {
                String commands = "nslookup -an";
                String www = commands + " " + domainName.getText();
                String commandstr = www;                       //需要调用系统命令符的命令
                Process p = Runtime.getRuntime().exec(commandstr);  //调用系统命令符
                br = new BufferedReader(new InputStreamReader(p.getInputStream()));    //截取命令符返回的信息
                String line = null;
                StringBuilder sb = new StringBuilder();  //字符串变量非线程
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");  //如果返回值为空，连接一个字符串到末尾
                }
                pop(sb.toString());
                //jta.setText(jta.getText()+"\n"+"            "+sb.toString());  //把截取的返回值输出到文本域
            } catch (Exception ev) {
                ev.printStackTrace();
            } finally {
                if (br != null)  //如果返回值不为空则关闭br
                {
                    try {
                        br.close();
                    } catch (Exception ev) {
                        ev.printStackTrace();
                    }
                }
            }
}
//    class  MyTask extends Task<String>{
//        @Override
//        protected void updateProgress(long l, long l1) {
//            super.updateProgress(l, l1);
//        }
//
//        @Override
//        protected void updateProgress(double v, double v1) {
//            super.updateProgress(v, v1);
//        }
//
//        @Override
//        protected void updateMessage(String s) {
//            super.updateMessage(s);
//        }
//
//        @Override
//        protected void updateTitle(String s) {
//            super.updateTitle(s);
//        }
//
//        @Override
//        protected void updateValue(String s) {
//            super.updateValue(s);
//        }
//
//        @Override public String call() {
//            Socket socket = null ;
//            for(int i = minPort;i<maxPort;i++){
//                try {
//                    socket=new Socket(Ip, i);
//                    //todo
//                    findInfoByPort(i ,Ip);//通过端口号调用数据库信息
//                    Platform.runLater(() -> message.appendText("\n"));
//                    socket.close();
//                } catch (Exception e) {
//                    //System.out.println(i+"端口没开放");
//                    Platform.runLater(() -> message.appendText(""));
//                }
//                int finalI = i;
//                Platform.runLater(() -> status.setText("正在扫描"+ finalI));
//            }
//            Platform.runLater(() -> status.setText("扫描结束"));
//
//            final int max = 1000000; for (int i=1; i<=max; i++) { updateProgress(i, max); } return null; }
//    };
    //ProgressBar bar = new ProgressBar(); bar.progressProperty().bind(task.progressProperty());
}


