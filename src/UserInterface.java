import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
public class UserInterface {
    static final class class4Count{
        long portCount=0;
        long IPCount=1;
    }
    static final class4Count class4Count=new class4Count();
    private int startPort;private int endPort;
    private int portOfPerThread;
    StringBuffer sb=new StringBuffer();//synchronized的
    OneTask oneTask=new OneTask();
    @FXML Button clearArea;
    @FXML Button startScanner;
    @FXML Button export;
    @FXML Button searchIp;
    @FXML TextField IPStartField;
    @FXML TextField IPEndField;
    @FXML TextField PortStartField;
    @FXML TextField PortEndField;
    @FXML TextField portNumberOfPerThreadField;
    @FXML TextField domainName;
    @FXML Label portStatus;
    @FXML Label IPStatus;
    @FXML TextArea output;
    @FXML ProgressBar progressBarOfAll;
    @FXML ProgressBar progressBarOfThread;
    class OneTask extends Task<Number>{
        @Override
        protected void updateProgress(long l, long l1) {
            super.updateProgress(l, l1);
        }

        @Override
        protected void updateProgress(double v, double v1) {
            super.updateProgress(v, v1);
        }

        @Override
        protected void updateMessage(String s) {
            super.updateMessage(s);
        }

        @Override
        protected void updateTitle(String s) {
            super.updateTitle(s);
        }

        @Override
        protected void updateValue(Number number) {
            super.updateValue(number);
        }

        @Override
        protected Number call() throws Exception {
            String IPStartString = IPStartField.getText().trim();   //得到输入的Ip
            int count=0;
            int start =2;int end=3;//int re4return;
            if(checkLegality(IPStartString)){//判断是否为数字
                try{
                    startPort = Integer.parseInt(PortStartField.getText().trim()) ;
                    endPort =  Integer.parseInt(PortEndField.getText().trim()) ;
                    portOfPerThread  =Integer.parseInt(portNumberOfPerThreadField.getText().trim())  ;
                    int threadNumber = (endPort - startPort) / portOfPerThread + 1;
                    //判断端口号的范围
                    if(startPort<0||endPort>65535||startPort>endPort){
                        pop("端口号范围：0~65535,并且最大端口号应大于最小端口号！") ;
                    }
                    else{
                        if(portOfPerThread>endPort-startPort||portOfPerThread<1){
                            pop("每个线程扫描的端口数不能大于所有的端口数且不能小于1") ;
                        }else{
                            if(IPEndField.getText().equals("")){         //if(((String) comboBox.getSelectedItem()).equals("地址")){
                                sb.append("#######################################################################"+"\n");
                                sb.append("正在扫描:"+ IPStartString +"\t\t每个线程扫描端口个数:"+portOfPerThread+"\t"+"开启的线程数:"+ threadNumber +"\n");
                                sb.append("端口起点:"+startPort+"\t端口终点:" +endPort+"\n");
                                sb.append("有下面这些开放的端口："+"\n");
                                oneTask.updateTitle(IPStartField.getText());
                                oneTask.updateMessage(sb.toString());
                                for(int i = startPort;!oneTask.isCancelled()&&i <= endPort;i += portOfPerThread) {
                                    if((i + portOfPerThread) <= endPort) {
                                        count=i;
                                        new ScanIP(i, i + portOfPerThread, IPStartString).start();
                                        System.out.println((count-startPort)*1.0/(endPort-startPort));
                                        updateValue((count-startPort)*1.0/(endPort-startPort));
                                    }
                                    else {
                                        new ScanIP(i, endPort, IPStartString).start();
                                    }
                                }
                            }else{
                                String IPEndString = IPEndField.getText();
                                if(checkLegality(IPEndString)){
                                    //扫描Ip地址段
                                    Set ipSet = new HashSet<Object>() ;
                                    start = Integer.valueOf(IPStartString.split("\\.")[3]);
                                    end = Integer.valueOf(IPEndString.split("\\.")[3]);

                                    String starts = IPStartString.split("\\.")[0]+"."+ IPStartString.split("\\.")[1]+"."+ IPStartString.split("\\.")[2];
                                    //生成IP地址
                                    if(start>end){pop("请输入正确的Ip地址") ;}
                                    else{
                                        for(int i = start;i<=end;i++){
                                            ipSet.add(starts+"."+i) ;    //地海段的每个地址存入集合
                                        }
                                        for (Object str : ipSet) {
                                            new ScanIPBlock(str.toString()).start() ;
                                            System.out.println(count*1.0/(end-start));
                                            updateValue(count*1.0/(end-start));
                                            count++;
                                        }
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
            System.out.println("call结束了");
            if(IPEndField.getText().equals(""))return 1.0*(count-startPort)/(endPort-startPort);
            else return count*1.0/(end-start);
        }
    }
    public  void initialize(){
         oneTask.progressProperty().addListener(new ChangeListener<Number>() {
             @Override
             public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                 progressBarOfAll.setProgress(t1.doubleValue());
                 output.setText(sb.toString());
             }
         });
        oneTask.messageProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                output.setText(sb.toString());
            }
        });
        oneTask.titleProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                IPStatus.setText(t1);
            }
        });
        oneTask.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                progressBarOfThread.setProgress(t1.doubleValue());
            }
        });
    }
    @FXML void EventOnScan (javafx.event.ActionEvent event){
        oneTask.cancel();
        oneTask=new OneTask();
        synchronized (class4Count){
            class4Count.portCount=0;
            class4Count.IPCount=1;
        }
        initialize();
        Thread th = new Thread(oneTask);
        //th.setDaemon(true);
        th.start();
    }
    //todo task 扫描端口地址的线程
    class ScanIP extends Thread{
        int maxPort, minPort;
        String IP;
        ScanIP(int minPort, int maxPort,String IP){
            this.minPort=minPort ;
            this.maxPort=maxPort ;
            this.IP=IP;
        }
        public  void run() {
            Socket socket = null ;
            for(int i = minPort;i<maxPort;i++){
                try {
                    socket=new Socket(IP, i);
                    //todo
                    queryInDataBase(i ,IP);//通过端口号调用数据库信息
                    sb.append("\n");
                    System.out.println(i+"端口开放了");
                    oneTask.updateMessage(sb.toString());
                    socket.close();
                } catch (Exception e) {//包括jdbc,大忌
                }
                    synchronized(class4Count){
                        class4Count.portCount++;
                        //System.out.println(class4Count.portCount+" "+(startPort-endPort));
                        oneTask.updateProgress(class4Count.portCount,class4Count.IPCount*(endPort-startPort));
                    }
                int finalI = i;
                Platform.runLater(() -> portStatus.setText(String.valueOf(finalI)));//"正在扫描"+
            }
            Platform.runLater(() -> portStatus.setText("当前小线程结束"));
        }
    }
    //扫描Ip地址段查看合法Ip的线程
    class ScanIPBlock extends Thread{
        String  IP ;
        int tagOfIPCount=0;
        ScanIPBlock(String  IP ){
            this.IP = IP ;
        }
        public synchronized void run(){
            try {
                for(int i = startPort;i <= endPort; i++) {
                    //扫描开放的Ip
                    if(tagOfIPCount==0){
                        InetAddress.getByName(IP);
                        class4Count.IPCount++;
                    }
                    if((i + portOfPerThread) <= endPort) {
                        new ScanIP(i, i + portOfPerThread,IP).start();
                        i += portOfPerThread;
                    }
                    else {
                        new ScanIP(i, endPort,IP).start();
                        i += portOfPerThread;
                    }
                    oneTask.updateTitle(IP);
                }
            } catch (Exception e) {
                System.out.println(IP+"\n");
            }
        }
    }
    //根据端口号，查询数据库中端口号的相应信息并显示在文本域之中
    synchronized void queryInDataBase(int port,String IP){
        sb.append("#####################"+"IP: "+IP+"的端口号: "+port+"########################"+"\n");
        Connection connection ;
        PreparedStatement pst  ;
        ResultSet rs  ;
        connection = DriverUtils.getConnection() ;//与数据库建立连接，获取Connection对象
        String sql = "Select * from inna where PortNumber ="+port;
        try {
            pst = connection.prepareStatement(sql) ;
            rs = pst.executeQuery() ;
            while(rs.next()){
                String transportProtocol=rs.getString("TransportProtocol");
                String serviceName = rs.getString("ServiceName");
                String description = rs.getString("Description") ;
                if(description.equals("Reserved")||serviceName.equals("Null")){}
                else{
                    if(transportProtocol.equals("tcp"))sb.append("UDP/TCP："+transportProtocol+"\t\t") ;
                    else{
                        sb.append("UDP/TCP："+transportProtocol+"\t") ;
                    }
                    sb.append("端口信息："+serviceName+"\t") ;
                    sb.append("端口说明："+description+"\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 判断输入的IP是否合法
    private boolean checkLegality(String str) {//字符串里面\要变\\,|的优先级最低
//        Pattern pattern = Pattern
//                .compile("^((\\d" +
//                        "|[1-9]\\d" +
//                        "|1\\d\\d" +
//                        "|2[0-4]\\d|" +
//                        "25[0-5]"
//                        + "|[*])" +
//                        "\\.)" +
//                        "{3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
        Pattern pattern = Pattern
                .compile("^((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}$");
        return pattern.matcher(str).matches();
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
        try {
            FileWriter writeOut = new FileWriter(saveFile);
            writeOut.write(output.getText());
            writeOut.close();
            pop("保存成功!");
        }
        catch (Exception ex) {
            pop("保存失败!");
        }
    }
    @FXML void EventOnGetIp (javafx.event.ActionEvent event) {
        BufferedReader br = null;
        try {
            String commands = "nslookup -an";
            String www = commands + " " + domainName.getText();             //需要调用系统命令符的命令
            Process p = Runtime.getRuntime().exec(www);  //调用系统命令符
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));    //截取命令符返回的信息
            String line = null;
            StringBuilder sb = new StringBuilder();  //字符串变量非线程
//                while ((line = new String(br.readLine().getBytes(),"gbk")) .equals("")!=false) {
//                    sb.append(line + "\n");  //如果返回值为空，连接一个字符串到末尾
//                }
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");  //如果返回值为空，连接一个字符串到末尾
            }
            //String ss=new String(br.toString().getBytes(),"GBK"); //转码UTF8
            String ss=new String(sb.toString().getBytes(),"gbk"); //转码gbk
            //String ss=new String(sb.toString().getBytes("ISO-8859-1"),"utf-8"); //转码UTF8
            pop(ss.toString());
            //pop(sb.toString());
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
    @FXML void EventOnClear (javafx.event.ActionEvent event) {
        output.clear();
        sb.delete(0,sb.length()-1);
    }
    @FXML Button cancelScan;
    @FXML void EventOnCancel (javafx.event.ActionEvent event) {
        oneTask.cancel();
        //要终止线程会很麻烦
        if(oneTask.isCancelled()==true){
            pop("成功暂停");
        }
    }
}


