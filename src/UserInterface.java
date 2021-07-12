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
    static final class Class4Count{
        long portCount=0;
        long IPCount=1;
    }
    static final Class4Count Class4Count=new Class4Count();
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
        protected Number call()  {
            String IPStartString = IPStartField.getText().trim();   //得到输入的Ip
            int count=0;int start =2;int end=3;//int re4return;
            if(checkLegality(IPStartString)){//判断IP格式是否正确
                try{
                    startPort = Integer.parseInt(PortStartField.getText().trim()) ;
                    endPort =  Integer.parseInt(PortEndField.getText().trim()) ;
                    portOfPerThread  =Integer.parseInt(portNumberOfPerThreadField.getText().trim())  ;
                    int threadNumber = (endPort - startPort) / portOfPerThread + 1;
                    if(startPort<0||endPort>65535||startPort>endPort){//判断端口号的范围
                        updateMessage("端口号范围：0~65535,并且最大端口号应大于最小端口号！");
                    }
                    else{
                        if(portOfPerThread>endPort-startPort||portOfPerThread<1){
                            updateMessage("每个线程扫描的端口数不能大于所有的端口数且不能小于1");
                        }else{
                            if(IPEndField.getText().equals("")){
                                sb.append("#######################################################################"+"\n");
                                sb.append("正在扫描:").append(IPStartString).
                                        append("\t\t每个线程扫描端口个数:").append(portOfPerThread).
                                        append("\t").append("开启的线程数:").append(threadNumber).append("\n");
                                sb.append("端口起点:").append(startPort).append("\t端口终点:").append(endPort).append("\n");
                                sb.append("有下面这些开放的端口："+"\n");
                                oneTask.updateTitle(IPStartField.getText());//oneTask.updateMessage(sb.toString());
                                for(int i = startPort;!oneTask.isCancelled()&&i <= endPort;i += portOfPerThread) {
                                    if((i + portOfPerThread) <= endPort) {
                                        count=i;
                                        new ScanIP(i, i + portOfPerThread, IPStartString).start();//System.out.println((count-startPort)*1.0/(endPort-startPort));
                                        updateValue((count-startPort)*1.0/(endPort-startPort));//更新线程进度条用
                                    }
                                    else { new ScanIP(i, endPort, IPStartString).start(); }
                                }
                            }else{
                                String IPEndString = IPEndField.getText();
                                if(checkLegality(IPEndString)){//扫描Ip地址段
                                    Set<Object> IPSet = new HashSet<>() ;
                                    start = Integer.parseInt(IPStartString.split("\\.")[3]);
                                    end = Integer.parseInt(IPEndString.split("\\.")[3]);
                                    String starts = IPStartString.split("\\.")[0]+"."+
                                            IPStartString.split("\\.")[1]+"."+
                                            IPStartString.split("\\.")[2];//IP地址根据"."划分成了4块
                                    if(start>end){ updateMessage("请输入正确的Ip地址"); }
                                    else{
                                        synchronized (Class4Count){
                                            Class4Count.portCount=0;
                                            Class4Count.IPCount=0;
                                        }
                                        for(int i = start;i<=end;i++){
                                            IPSet.add(starts+"."+i) ;    //地海段的每个地址存入集合
                                        }
                                        for (Object str : IPSet) {
                                            new ScanIPBlock(str.toString()).start() ;//System.out.println(count*1.0/(end-start));
                                            updateValue(count*1.0/(end-start));//更新线程进度条用
                                            count++;//System.out.println("count="+count);
                                        }
                                    }
                                }else{ updateMessage("请输入正确的Ip地址"); }
                            }
                        }
                    }
                }
                catch(NumberFormatException e1){
                    updateMessage("错误的端口号或端口号和线程数必须为整数");
                }
            }
            else{
                updateMessage("请输入正确的Ip地址");
            }
            System.out.println("call结束了");
            if(IPEndField.getText().equals(""))return 1.0*(count-startPort)/(endPort-startPort);
            else return count*1.0/(end-start);
        }
    }
    public  void initialize(){
        oneTask.progressProperty().addListener((observableValue, number, t1) -> {
            progressBarOfAll.setProgress(t1.doubleValue());
            output.setText(sb.toString());
        });
        oneTask.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                progressBarOfThread.setProgress(t1.doubleValue());
            }
        });
        oneTask.messageProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                pop(t1);
            }
        });
        oneTask.titleProperty().addListener((observableValue, s, t1) -> IPStatus.setText(t1));
    }
    @FXML void EventOnScan (javafx.event.ActionEvent event){
        oneTask.cancel();
        oneTask=new OneTask();
        synchronized (Class4Count){
            Class4Count.portCount=0;
            Class4Count.IPCount=1;
        }
        initialize();
        Thread th = new Thread(oneTask);
        th.setDaemon(true);//设置为低优先级的守护线程
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
                    queryInDataBase(i ,IP);//通过端口号调用数据库信息
                    sb.append("\n");//System.out.println(i+"端口开放了");//oneTask.updateMessage(sb.toString());
                    socket.close();
                } catch (Exception e) {  //包括socket和数据库的异常                                                                            //catch Exception 包括jdbc,大忌
                }
                synchronized(Class4Count){//锁住并实时更新总的进度条
                    Class4Count.portCount++;                                                                    //System.out.println(Class4Count.portCount+" "+Class4Count.IPCount*(startPort-endPort));
                    oneTask.updateProgress(Class4Count.portCount,Class4Count.IPCount*(endPort-startPort));
                    oneTask.updateTitle(IP);
                }
                int finalI = i;
                Platform.runLater(() -> portStatus.setText(String.valueOf(finalI)));
            }//当前正则扫描哪个端口不太需要实时,所以runLater
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
        //public  void run(){
            public synchronized void run(){
            try {
                for(int i = startPort;i <= endPort;i += portOfPerThread) {
                    if(tagOfIPCount==0){                    //扫描开放的Ip
                        if(InetAddress.getByName(IP).isReachable(3000)){
                            synchronized (UserInterface.Class4Count){
                                Class4Count.IPCount++;
                            }
                            tagOfIPCount++;
                        }
                        else return;
                    }
                    if((i + portOfPerThread) <= endPort) {
                        new ScanIP(i, i + portOfPerThread,IP).start();
                    }
                    else {
                        new ScanIP(i, endPort,IP).start();
                    }
                    //oneTask.updateTitle(IP);
                }
            } catch (Exception e) {
                System.out.println(IP+"\n");
            }
        }
    }
    //根据端口号，查询数据库中端口号的相应信息并显示在文本域之中
    synchronized void queryInDataBase(int port,String IP){
        sb.append("#####################" + "IP: ").append(IP).append("的端口号: ").
                append(port).append("########################").append("\n");
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
                    if(transportProtocol.equals("tcp")){
                        sb.append("UDP/TCP：").append(transportProtocol).append("\t\t");}
                    else{
                        sb.append("UDP/TCP：").append(transportProtocol).append("\t");
                    }
                    sb.append("端口信息：").append(serviceName).append("\t")
                            .append("端口说明：").append(description).append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 判断输入的IP是否合法
    private boolean checkLegality(String str) {//字符串里面\要变\\,|的优先级最低,中间不能有空格
        Pattern pattern = Pattern
                .compile("^((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}$");
        return pattern.matcher(str).matches();
    }
    //        Pattern pattern = Pattern
//                .compile("^((\\d" +
//                        "|[1-9]\\d" +
//                        "|1\\d\\d" +
//                        "|2[0-4]\\d|" +
//                        "25[0-5]"
//                        + "|[*])"//bug
//                        +"\\.)" +
//                        "{3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
//        Pattern pattern = Pattern
//                .compile("^((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2}) (\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}$");
// 正则表达式中间不能有空格
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
        PopStage.initModality(Modality.APPLICATION_MODAL);        //设置为弹窗类型
        PopStage.setScene(creatingScene);
        PopStage.show();
    }
    @FXML void EventOnExport (javafx.event.ActionEvent event){
        FileChooser fc=new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fc.getExtensionFilters().add(extFilter);
        Stage reflectedStage = (Stage) ((Node) event.getSource()).getScene().getWindow();//【反射】通过event获得当前node然后一路get window
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
            String line ;
            StringBuilder sb = new StringBuilder();  //字符串变量线程安全的
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");  //如果返回值为空，连接一个字符串到末尾
            }
            String ss=new String(sb.toString().getBytes(),"gbk"); //转码gbk
            pop(ss.toString());
        } catch (Exception ev) {
            ev.printStackTrace();
        } finally {
            if (br != null) {//如果返回值不为空则关闭br
                try {
                    br.close();
                } catch (Exception ev) {
                    ev.printStackTrace();
                }
            }
        }
    }
    //                while ((line = new String(br.readLine().getBytes(),"gbk")) .equals("")!=false) {
//                    sb.append(line + "\n");  //如果返回值为空，连接一个字符串到末尾
//                }
    //String ss=new String(br.toString().getBytes(),"GBK"); //转码UTF8

    //String ss=new String(sb.toString().getBytes("ISO-8859-1"),"utf-8"); //转码UTF8
    //pop(sb.toString());
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


