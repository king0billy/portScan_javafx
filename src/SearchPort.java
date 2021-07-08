/**
 * @author 22427(king0liam)
 * @version 1.0
 * @ClassName: Shit
 * @Description
 * @Date: 2021/7/8 16:27
 * @since version-0.0
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
public class SearchPort  extends JFrame implements ActionListener
{
    private static JTextArea jta;
    private JScrollPane js;
    private JTextField jt1,jt2,jt3,jt4;
    private JButton jb1,jb2,jb3,jb4,jb5,jb6,jb7,jb8,jb10;
    String path;
    ImageIcon background;
    static double b,a;;
    private static InetAddress bjip, wip;
    private static InetAddress ip;
    private static String bjname;
    private String name;
    private String commandstr;
    private static int qishi;
    private int zhongzhi;
    Socket socket;
    public SearchPort()
    {
        super("端口扫描器");//名字
        //设置大小
        setSize(800,600);
        //设置位置
        setLocation(0, 0);
        path = "background10.jpg";
        //背景图片
        background = new ImageIcon(path);
        // 把背景图片显示在一个标签里面
        JLabel label = new JLabel(background);
        // 把标签的大小位置设置为图片刚好填充整个面板
        label.setBounds(0, 0, this.getWidth(), this.getHeight());
        // 把内容窗格转化为JPanel，否则不能用方法setOpaque()来使内容窗格透明
        JPanel imagePanel = (JPanel) this.getContentPane();
        imagePanel.setOpaque(false);
        // 把背景图片添加到分层窗格的最底层作为背景
        this.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
        //设置为显示
        setVisible(true);
        //创建jpanel
        JPanel jp=new JPanel();
        JPanel jp1=new JPanel();
        JPanel jp2=new JPanel();
        JPanel jp3=new JPanel();
        JPanel jp4=new JPanel();
        JPanel jp5=new JPanel();
        JPanel jp6=new JPanel();
        JPanel jp7=new JPanel();
        JPanel jp8=new JPanel();
        JPanel jp9=new JPanel();
        JPanel jp10=new JPanel();
        JPanel jp11=new JPanel();
        JPanel jp12=new JPanel();
        JPanel jp13=new JPanel();
        JPanel jp14=new JPanel();
        JPanel jp15=new JPanel();
        JPanel jp16=new JPanel();
        JPanel jp17=new JPanel();
        JPanel jp18=new JPanel();
        //需要透明的组件全设置为透明
        jp.setOpaque(false);
        jp1.setOpaque(false);
        jp2.setOpaque(false);
        jp3.setOpaque(false);
        jp4.setOpaque(false);
        jp5.setOpaque(false);
        jp6.setOpaque(false);
        jp7.setOpaque(false);
        jp8.setOpaque(false);
        jp9.setOpaque(false);
        jp10.setOpaque(false);
        jp11.setOpaque(false);
        jp12.setOpaque(false);
        jp13.setOpaque(false);
        jp14.setOpaque(false);
        jp15.setOpaque(false);
        jp16.setOpaque(false);
        jp17.setOpaque(false);
        jp18.setOpaque(false);
        //按钮的设置
        jb1=new JButton("复制");
        jb2=new JButton("剪切");
        jb3=new JButton("清空");
        jb4=new JButton("本机IP");
        jb5=new JButton("查网址IP");
        jb6=new JButton("扫描");
        jb8=new JButton("换肤");
        jb10=new JButton("本机扫描");
        //按钮添加监听事件
        jb1.setOpaque(false);
        jb1.addActionListener(this);
        jb2.addActionListener(this);
        jb3.addActionListener(this);
        jb4.addActionListener(this);
        jb5.addActionListener(this);
        jb6.addActionListener(this);
        jb8.addActionListener(this);
        jb10.addActionListener(this);
        //建立滚动窗格，把文本域添加进去
        jta=new JTextArea("      结果 ",27,36);
        js=new JScrollPane(jta);
        //滚动窗格及文本域设置透明
        jta.setOpaque(false);
        js.setOpaque(false);
        //文本行的设置
        jt1=new JTextField("192.168.38.12",12);
        jt2=new JTextField("www.baidu.com",12);
        jt3=new JTextField("起始端口",12);
        jt4=new JTextField("终止端口",12);
        //文本行设置透明
        jt1.setOpaque(false);
        jt2.setOpaque(false);
        jt3.setOpaque(false);
        jt4.setOpaque(false);
        //添加JPanel
        this.add(jp);
        //jp设置一行两列
        jp.setLayout(new GridLayout(1,2));
        //jp添加jp1,jp2
        jp.add(jp1);
        jp.add(jp2);
        //jp1设置边界
        jp1.setBorder(BorderFactory.createTitledBorder("扫描结果"));
        //jp1设置边布局
        jp1.setLayout(new BorderLayout());
        //jp1添加Jp3到上方
        jp1.add(jp3,"North");
        //滚动窗格设置透明
        js.setOpaque(false);
        js.getViewport().setOpaque(false);
        //把js添加到jp3
        jp3.add(js);
        //jp4添加到jp1下方
        jp1.add(jp4,"South");
        // jp4添加四个按钮
        jp4.add(jb1);
        jp4.add(jb2);
        jp4.add(jb3);
        jp4.add(jb8);
        //jp3设置大小
        jp3.setPreferredSize(new Dimension(400,600));
        //jp2设置边布局
        jp2.setLayout(new BorderLayout());
        //jp5添加到jp2 上方
        jp2.add(jp5,"North");
        //JP5设置大小
        jp5.setPreferredSize(new Dimension(400,500));
        //jp5设置边界
        jp5.setBorder(BorderFactory.createTitledBorder("设扫描地址"));
        //把jp6添加到jp2的中间
        jp2.add(jp6,"Center");
        //jp6设置边界
        jp6.setBorder(BorderFactory.createTitledBorder("扫描"));
        //jp6设置网格布局4行1列
        jp5.setLayout(new GridLayout(4,1));
        //把Jp7 jp8 jp9 jp10插入到jp5
        jp5.add(jp7);
        jp5.add(jp8);
        jp5.add(jp9);
        jp5.add(jp10);
        //jp7设置1行2列
        jp7.setLayout(new GridLayout(1,2));
        //jp8设置1行2列
        jp8.setLayout(new GridLayout(1,2));
        //jp9设置1行2列
        jp9.setLayout(new GridLayout(1,2));
        //jp10设置1行2列
        jp10.setLayout(new GridLayout(1,2));
        //把jp11jp15添加到Jp7
        jp7.add(jp11);
        jp7.add(jp15);
        //jp15设置边界
        jp15.setBorder(BorderFactory.createTitledBorder("IP地址"));
        //把文本行添加到Jp15
        jp15.add(jt1);
        //把jp12jp16添加到Jp8
        jp8.add(jp12);
        jp8.add(jp16);
        //jp16设置边界
        jp16.setBorder(BorderFactory.createTitledBorder("网址"));
        //把文本行添加到Jp15
        jp16.add(jt2);
        //把jp13jp17添加到Jp9
        jp9.add(jp13);
        jp9.add(jp17);
        //jp17设置边界
        jp17.setBorder(BorderFactory.createTitledBorder("起始端口"));
        //把文本行添加到Jp17
        jp17.add(jt3);
        //把jp14jp18添加到Jp10
        jp10.add(jp14);
        jp10.add(jp18);
        //jp18设置边界
        jp18.setBorder(BorderFactory.createTitledBorder("终止端口"));
        //把文本行添加到Jp18
        jp18.add(jt4);
        //把按钮添加到jp6
        jp6.add(jb4);
        jp6.add(jb10);
        jp6.add(jb5);
        jp6.add(jb6);
    }
    //动作响应事件
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==jb1)//如果jb1被按下
        {
            jta.copy();//复制
        }
        if(e.getSource()==jb2)
        {
            jta.cut();//剪切
        }
        if(e.getSource()==jb3)
        {
            jta.setText("      结果 ");//清空
        }
        if(e.getSource()==jb4)//获取本机ip
        {
            try {
                bjip=InetAddress.getLocalHost();   //获得本机ip
                bjname=InetAddress.getLocalHost().getHostName();//获得本机名
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            }
            jta.setText(jta.getText()+"\n"+"      本机IP："+bjip+"\n"+"      本机名字"+bjname);   //输出
        }
        if(e.getSource()==jb5)   //查本机端口
        {
            search s=new search();
            Thread t1=new Thread(s);
            Thread t2=new Thread(s);
            t1.start();
            t2.start();
        }
        if(e.getSource()==jb6)//扫描端口
        {
            qishi=Integer.parseInt(jt3.getText());//jt3的输入转数字
            zhongzhi =Integer.parseInt(jt4.getText());
            InetAddress ip = null;
            if (qishi!=0&&zhongzhi!=0)    //判断起始终止端口是否为空
            {
                if(qishi>0&&zhongzhi<65535)//判断端口是否正确
                {
                    if(qishi<zhongzhi)               //判断是否起始端口小于终止端口
                    {
                        if ((zhongzhi-qishi)<1000)     //判断要扫描的端口数是否小于1000
                        {
                            try
                            {
                                wip = InetAddress.getByName(jt1.getText());   //获取ip
                            }
                            catch (UnknownHostException e1)
                            {
                                JOptionPane.showMessageDialog(rootPane, "请输入正确的ip地址！");
                            }
                            for (int port = qishi; port <=zhongzhi; port++) //端口循环
                            {
                                searchport s = new searchport(wip,port);
                                Thread t = new Thread(s);
                                t.start();
                                if (port==zhongzhi)
                                {
                                    jta.setText(jta.getText()+"\n"+"          扫描完毕");  //判断是否扫锚完毕
                                }
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(rootPane, "扫描端口数过多！");//扫描太多
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(rootPane, "起始端口必须小于终止端口");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(rootPane, "请输入正确的端口！");
                }
            }
            else {
                JOptionPane.showMessageDialog(rootPane, "请输入端口！");
            }
        }
        if(e.getSource()==jb10)   //获取本机开放端口
        {
            new Thread(new Runnable(){
                public void run()
                {
                    BufferedReader br = null;
                    try {
                        String commands="nslookup";
                        String www=commands+" "+jt2.getText();
                        commandstr=www;                            //命令
                        Process p = Runtime.getRuntime().exec("Netstat -an");     //调用系统控制命令符
                        br = new BufferedReader(new InputStreamReader(p.getInputStream()));  //截取命令符的返回代码
                        String line = null;
                        StringBuilder sb = new StringBuilder();  //字符串变量非线程
                        while ((line = br.readLine()) != null)
                        {
                            sb.append(line + "\n");      //  如果返回值为空，连接一个字符串到末尾
                        }
                        jta.setText(jta.getText()+"\n"+sb.toString());  //把截取的字符串输出到文本域
                    }
                    catch (Exception ev)
                    {
                        ev.printStackTrace();
                    }
                    finally
                    {
                        if (br != null)      //如果返回值不为空则关闭br
                        {
                            try
                            {
                                br.close();
                            } catch (Exception ev)
                            {
                                ev.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
    }
    public static void main(String[] args)
    {
        new SearchPort();
    }
    class search implements Runnable
    {
        public void run()
        {
            BufferedReader br = null;
            try {
                String commands="nslookup -an";
                String www=commands+" "+jt2.getText();
                commandstr=www;                       //需要调用系统命令符的命令
                Process p = Runtime.getRuntime().exec(commandstr);  //调用系统命令符
                br = new BufferedReader(new InputStreamReader(p.getInputStream()));    //截取命令符返回的信息
                String line = null;
                StringBuilder sb = new StringBuilder();  //字符串变量非线程
                while ((line = br.readLine()) != null)
                {
                    sb.append(line + "\n");  //如果返回值为空，连接一个字符串到末尾
                }
                jta.setText(jta.getText()+"\n"+"            "+sb.toString());  //把截取的返回值输出到文本域
            }
            catch (Exception ev)
            {
                ev.printStackTrace();
            }
            finally
            {
                if (br != null)  //如果返回值不为空则关闭br
                {
                    try
                    {
                        br.close();
                    } catch (Exception ev)
                    {
                        ev.printStackTrace();
                    }
                }
            }
        }
    }
    class searchport implements Runnable
    {  private InetAddress wip;
        private int port;
        searchport(InetAddress wip,int port)
        {
            this.wip=wip;
            this.port=port;
        }
        @Override
        public void run()
        {
            try
            {
                Socket socket=new Socket(wip,port);      //与目标ip的端口建立连接
                jta.setText(jta.getText()+"\n"+"                    "+port +"开启"); //成功则输出到文本域
                socket.close(); //连接关闭
            }
            catch (Exception ev)
            {
                jta.setText(jta.getText()+"\n"+"      "+port +"关闭");//连接失败则输出到文本域
            }
        }

    }
}
