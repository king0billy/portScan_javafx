import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
class EditorWin extends JFrame implements ActionListener {
    private JLabel startIp,endIp,l_startPort,l_endPort,l_portOfThread ,showResult ,empty,type ,status;
    private JTextField f_startIp,f_endIp,f_startPort,f_endPort,f_portOfThread ;
    private JScrollPane result ;
    private JComboBox comboBox ;
    private JButton startScanner,exitScanner ,clear,reset;
    private JPanel top,bottom ;
    private JTextArea message ;
    private String startIpStr ,endIpStr;
    private int startPort,endPort,portOfThread ,threadNum ;
    public EditorWin(){
        this.setTitle("多线程端口扫描器") ;
        startIp = new JLabel("扫描的Ip") ;
        l_startPort = new JLabel("起始端口") ;
        l_endPort = new JLabel("结束端口") ;
        l_portOfThread = new JLabel("每个线程扫描端口数") ;
        status=new JLabel("未开始扫描") ;
        showResult = new JLabel("扫描结果") ;
        endIp = new JLabel("结束Ip");
        empty = new JLabel("                                                            ") ;
        type = new JLabel("选择扫描的类型") ;

        startScanner = new JButton("扫描");
        exitScanner = new  JButton("退出");
        clear = new JButton("清空") ;
        reset = new JButton("重置") ;

        f_endIp = new JTextField(12) ;
        f_startIp = new JTextField(12) ;
        f_startPort = new JTextField(5) ;
        f_endPort = new JTextField(5) ;
        f_portOfThread = new JTextField(5) ;

        message = new JTextArea(20,20) ;
        result = new JScrollPane(message) ;
        result.setColumnHeaderView(showResult) ;

        comboBox = new JComboBox() ;
        comboBox.addItem("地址");
        comboBox.addItem("地址段");

        endIp.setVisible(false) ;
        f_endIp.setVisible(false) ;
        top = new JPanel() ;
        top.add(type);
        top.add(comboBox) ;
        top.add(startIp) ;
        top.add(f_startIp) ;
        top.add(endIp) ;
        top.add(f_endIp) ;
        top.add(l_startPort) ;
        top.add(f_startPort) ;
        top.add(l_endPort) ;
        top.add(f_endPort) ;
        top.add(l_portOfThread) ;
        top.add(f_portOfThread) ;
        bottom = new JPanel() ;
        bottom.add(status) ;
        bottom.add(empty) ;
        bottom.add(empty) ;
        bottom.add(empty) ;
        bottom.add(empty) ;
        bottom.add(empty) ;
        bottom.add(empty) ;
        bottom.add(startScanner) ;
        bottom.add(clear);
        bottom.add(reset);
        bottom.add(exitScanner) ;
        this.add(top, BorderLayout.NORTH);
        this.add(result,BorderLayout.CENTER) ;
        this.add(bottom,BorderLayout.SOUTH) ;
        comboBox.addActionListener(this) ;
        startScanner.addActionListener(this) ;
        exitScanner.addActionListener(this) ;
        clear.addActionListener(this) ;
        reset.addActionListener(this) ;
        setSize(1000, 500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==startScanner){ //点击扫描按钮
            //点击时刻
            startIpStr = f_startIp.getText().trim() ;   //得到输入的Ip
            if(checkIP(startIpStr)){
                //判断是否为数字
                try{
                    startPort = Integer.parseInt(f_startPort.getText().trim()) ;
                    endPort =  Integer.parseInt(f_endPort.getText().trim()) ;
                    portOfThread  =Integer.parseInt(f_portOfThread.getText().trim())  ;
                    threadNum = (endPort-startPort)/portOfThread+1 ;
                    //判断端口号的范围
                    if(startPort<0||endPort>65535||startPort>endPort){
                        JOptionPane.showMessageDialog(this, "端口号范围：0~65535,并且最大端口号应大于最小端口号！") ;
                    }
                    else{
                        if(portOfThread>endPort-startPort||portOfThread<1){
                            JOptionPane.showMessageDialog(this, "每个线程扫描的端口数不能大于所有的端口数且不能小于1") ;
                        }else{
                            if(((String) comboBox.getSelectedItem()).equals("地址")){
                                message.append("************************************************************"+"\n") ;
                                message.append("正在扫描  "+startIpStr+"          每个线程扫描端口个数"+portOfThread+"\n"+"开启的线程数"+threadNum+"\n") ;
                                message.append("开始端口  "+startPort+"         结束端口" +endPort+"\n") ;
                                message.append("主机名:"+getHostname(startIpStr)+"\n");
                                message.append("开放的端口如下："+"\n") ;
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
                                    for(int i = start;i<=end;i++){
                                        ipSet.add(starts+"."+i) ;    //地海段的每个地址存入集合
                                    }
                                    for (Object str : ipSet) {
                                        new ScanIp(str.toString()).start() ;
                                    }
                                }else{
                                    JOptionPane.showMessageDialog(this, "请输入正确的Ip地址") ;
                                }

                            }
                        }
                    }
                }
                catch(NumberFormatException e1){
                    JOptionPane.showMessageDialog(this, "错误的端口号或端口号和线程数必须为整数") ;
                }
            }
            else{
                JOptionPane.showMessageDialog(this, "请输入正确的Ip地址") ;
            }
        }
        else if(e.getSource()==reset){
            f_startIp.setText("") ;
            f_startPort.setText("") ;
            f_endPort.setText("") ;
            f_portOfThread.setText("") ;
        }
        else if(e.getSource()==clear){
            message.setText("") ;
            System.out.println((String) comboBox.getSelectedItem());
        }
        else if(e.getSource()==exitScanner){
            System.exit(1);
        }else if(e.getSource()==comboBox){
            String type=(String) comboBox.getSelectedItem();
            if(type.equals("地址")){
                endIp.setVisible(false) ;
                f_endIp.setVisible(false) ;
                startIp.setText("扫描的Ip") ;
            }else{
                endIp.setVisible(true) ;
                f_endIp.setVisible(true) ;
                startIp.setText("开始Ip") ;
            }
        }
    }
    //扫描端口地址的线程
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
            Socket socket = null ;
            for(int i = minPort;i<maxPort;i++){
                try {
                    socket=new Socket(Ip, i);
                    //todo
                    findInfoByPort(i ,Ip);//通过端口号调用数据库信息
                    message.append("\n");
                    socket.close();
                } catch (Exception e) {
                    System.out.println(i+"端口没开放");
                    message.append("");
                }
                status.setText("正在扫描"+i) ;
            }
            status.setText("扫描结束") ;
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
        message.append("-----------------------"+"Ip"+Ip+"的"+"端口号"+port+"------------------------------------"+"\n");
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
                String serviceName = rs.getString("ServiceName");
                String description = rs.getString("Description") ;
                message.append("端口信息："+serviceName+"\n") ;
                message.append("端口说明："+description+"\n") ;
                totalStr =  totalStr+serviceName ;
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
}


