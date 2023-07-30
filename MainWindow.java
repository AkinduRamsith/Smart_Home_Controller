import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

interface SmartHome{
    void controller(String text);
    void updateTime(String startHour,String startMinute,String endHour,String endMinute);
}

class SmartHomeController {
    private String startHour;
    private String startMinute;
    private String endHour;
    private String endMinute;
    private SmartHome[] switchArray=new SmartHome[100];
    private int nextIndex;
    private int index;

    public void setIndex(int index){
        this.index=index;

    }
    public void windowByIndex(){
        switchArray[index].updateTime(startHour,startMinute,endHour,endMinute);
    }
    public void addSwitch(SmartHome sh){
        switchArray[nextIndex++]=sh;
    }
    public void windowByName(String text){
        for(SmartHome s : switchArray ){
            if(s!=null){
                s.controller(text);

            }
        }
    }
    public void setTime(String startHour,String startMinute,String endHour,String endMinute){
        if(this.startHour!=startHour || this.startMinute!=startMinute || this.endHour!=endHour || this.endMinute!=endMinute){
            this.startHour=startHour;
            this.startMinute=startMinute;
            this.endHour=endHour;
            this.endMinute=endMinute;
            windowByIndex();
        }
    }

}
class Controller extends JFrame{
    private DefaultListModel<String> l1;
    private JList<String> list;
    SmartHomeController smartHomeController;
    private TimeComponent timeComponent;

    Controller(SmartHomeController smartHomeController){
        this.smartHomeController=smartHomeController;
        setSize(300,200);
        setTitle("Controller");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocation(618,500);

        l1=new DefaultListModel<>();
        l1.addElement("TV Living Room");
        l1.addElement("Speaker Living Room");
        l1.addElement("Window Living Room");
        l1.addElement("TV Dining Room");
        list =new JList<>(l1);
        list.setBounds(100,100, 75,75);
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        list.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !list.isSelectionEmpty()) {
                String value = ""+list.getSelectedValue();
                int index=list.getSelectedIndex();
                int x = 925 ;
                int y=40;
                y = y + index  * 200;

                smartHomeController.setIndex(index);
                new TimeComponent(value,smartHomeController,x,y).setVisible(true);

            }

        });

        add(new JScrollPane(list));

    }
}
class TimeModel {
    private String startHour;
    private String startMinute;
    private String endHour;
    private String endMinute;

    public TimeModel(String startHour,String startMinute,String endHour,String endMinute){
        this.startHour=startHour;
        this.startMinute=startMinute;
        this.endHour=endHour;
        this.endMinute=endMinute;
    }

    public String getStartHour(){
        return startHour;
    }
    public String getStartMinute(){
        return startMinute;
    }
    public String getEndHour(){
        return endHour;
    }
    public String getEndMinute(){
        return endMinute;
    }

    public void setStarthour(String startHour){
        this.startHour=startHour;
    }
    public void setStartMinute(String startMinute){
        this.startMinute=startMinute;
    }
    public void setEndHour(String endHour){
        this.endHour=endHour;
    }
    public void setEndMinute(String endMinute){
        this.endMinute=endMinute;
    }
    public String toString(){
        return "Start at :"+startHour+"."+startMinute+" End at :"+endHour+"."+endMinute;
    }
}
class TimeComponent extends JFrame {
    private JLabel lblStartHour;
    private JLabel lblStartMinute;
    private JLabel lblEndHour;
    private JLabel lblEndMinute;
    private JButton btnSet;
    private JSpinner spinnerStartHour;
    private JSpinner spinnerStartMinute;
    private JSpinner spinnerEndHour;
    private JSpinner spinnerEndMinute;

    private String startHour;
    private String startMinute;
    private String endHour;
    private String endMinute;

    private DefaultListModel<TimeModel> l1;
    private JList<TimeModel> list;
    SmartHomeController smartHomeController;

    private TimeModel timeModel;

    public TimeComponent(String value, SmartHomeController smartHomeController,int x,int y){
        this.smartHomeController=smartHomeController;
        setSize(600, 180);
        setTitle(value);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocation(x,y);

        JPanel bottomPanel = new JPanel();
        lblStartHour = new JLabel();
        lblStartHour.setFont(new Font("", 1, 15));
        lblStartHour.setText("Start : Hour:");
        lblStartMinute = new JLabel();
        lblStartMinute.setFont(new Font("", 1, 15));
        lblStartMinute.setText("Minute :");
        lblEndHour = new JLabel();
        lblEndHour.setFont(new Font("", 1, 15));
        lblEndHour.setText("End : Hour:");
        lblEndMinute = new JLabel();
        lblEndMinute.setFont(new Font("", 1, 15));
        lblEndMinute.setText("Minute :");

        spinnerStartHour = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
        spinnerStartMinute = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        spinnerEndHour = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
        spinnerEndMinute = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));

        l1=new DefaultListModel<>();
        list =new JList<>(l1);
        list.setFont(new Font("",1,15));
        list.getSelectionModel().addListSelectionListener(e->{
            if (!e.getValueIsAdjusting() && !list.isSelectionEmpty()) {
                btnSet.setText("Edit");
                timeModel=list.getSelectedValue();

                spinnerStartHour.setValue(Integer.parseInt(timeModel.getStartHour()));
                spinnerStartMinute.setValue(Integer.parseInt(timeModel.getStartMinute()));
                spinnerEndHour.setValue(Integer.parseInt(timeModel.getEndHour()));
                spinnerEndMinute.setValue(Integer.parseInt(timeModel.getEndMinute()));

                btnSet.addActionListener(f -> {
                    int index = list.getSelectedIndex();
                    if (index >= 0) {
                        l1.remove(l1.getSize() - 1);
                        list.repaint();
                    }
                    btnSet.setText("Set");
                });
            }
        });


        btnSet = new JButton("Set");
        btnSet.setFont(new Font("", 1, 15));
        btnSet.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                startHour= String.valueOf(spinnerStartHour.getValue());
                startMinute= String.valueOf(spinnerStartMinute.getValue());
                endHour= String.valueOf(spinnerEndHour.getValue());
                endMinute= String.valueOf(spinnerEndMinute.getValue());

                l1.addElement(new TimeModel(startHour,startMinute,endHour,endMinute));
                smartHomeController.setTime(startHour,startMinute,endHour,endMinute);

            }


        });
        add(new JScrollPane(list));
        bottomPanel.add(lblStartHour);
        bottomPanel.add(spinnerStartHour);
        bottomPanel.add(lblStartMinute);
        bottomPanel.add(spinnerStartMinute);
        bottomPanel.add(lblEndHour);
        bottomPanel.add(spinnerEndHour);
        bottomPanel.add(lblEndMinute);
        bottomPanel.add(spinnerEndMinute);
        bottomPanel.add(btnSet);

        add("South", bottomPanel);
    }

}
class Time extends JFrame {
    private JLabel timeLabel;
    private DateTimeFormatter timeFormat;

    public Time() {
        setTitle("Local Time");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setResizable(false);
        setLocation(618,50);

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Times New Roman", Font.BOLD + Font.ITALIC, 36));
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setForeground(new Color(0, 100, 0));
        timeLabel.setBackground(Color.YELLOW);
        timeLabel.setOpaque(true);

        timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

        updateClock();

        getContentPane().add(timeLabel);
    }

    private void updateClock() {
        Thread clock = new Thread(() -> {
            while (true) {
                LocalTime currentTime = LocalTime.now();
                String formatTime = currentTime.format(timeFormat);
                timeLabel.setText(formatTime);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
            }
        });
        clock.start();
    }
}
class Switch extends JFrame{

    private JButton btnPower;
    private JButton btnSettings;
    private SmartHomeController smartHomeController;
    private TimeModel timeModel;

    private Controller controller;

    public Switch(SmartHomeController smartHomeController){
        this.smartHomeController=smartHomeController;
        setSize(300,250);
        setTitle("Switch");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocation(618,240);
        setLayout(null);
        btnPower=new JButton();
        btnPower.setText("ON");
        btnPower.setBounds(25,30,240,40);
        btnPower.setBackground(new Color(227, 66, 52));
        btnPower.setForeground(Color.WHITE);
        btnPower.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                if (btnPower.getText().equals("ON")) {

                    btnPower.setText("OFF");

                } else {
                    btnPower.setText("ON");
                }
                smartHomeController.windowByName(btnPower.getText());


            }
        });

        btnSettings=new JButton();
        btnSettings.setText("Settings");
        btnSettings.setBounds(25,80,240,40);
        btnSettings.setBackground(new Color(227, 66, 52));
        btnSettings.setForeground(Color.WHITE);
        btnSettings.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                if(controller==null){
                    controller=new Controller(smartHomeController);
                }
                controller.setVisible(true);

            }

        });




        add(btnPower);
        add(btnSettings);

    }



}


class SwitchBoard extends JFrame implements SmartHome{
    private JLabel lblStart;
    private JLabel lblEnd;
    private JLabel label;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private LocalTime currentTime;
    private javax.swing.Timer timer;
    private String timeStart,timeEnd;

    public SwitchBoard(String text,int x,int y){
        setSize(300,200);
        setTitle(text);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocation(x,y);
        setLayout(null);
        setVisible(true);
        label = new JLabel();
        label.setBounds(125,85,80,60);
        label.setFont(new Font("Georgia",1,23));

        lblStart=new JLabel();
        lblStart.setFont(new Font("Georgia",1,23));
        lblStart.setBounds(05,25,140,50);
        lblStart.setForeground(Color.WHITE);
        lblStart.setBackground(new Color(60,179,113));
        lblStart.setOpaque(true);
        lblStart.setHorizontalAlignment(SwingConstants.CENTER);


        lblEnd=new JLabel();
        lblEnd.setFont(new Font("Georgia",1,23));
        lblEnd.setBounds(140,25,140,50);
        lblEnd.setForeground(Color.WHITE);
        lblEnd.setBackground(Color.RED);
        lblEnd.setOpaque(true);
        lblEnd.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblEnd);
        add(lblStart);
        add(label);

    }
    @Override
    public void controller(String text){
        if(text.equals("ON")){
            label.setText("OFF");
        }else{
            label.setText("ON");
        }
    }
    @Override
    public void updateTime(String startHour,String startMinute,String endHour,String endMinute){
        this.startHour=Integer.parseInt(startHour);
        this.startMinute=Integer.parseInt(startMinute);
        this.endHour=Integer.parseInt(endHour);
        this.endMinute=Integer.parseInt(endMinute);

        lblStart.setText(String.format("%02d:%02d:00",this.startHour,this.startMinute));
        lblEnd.setText(String.format("%02d:%02d:00",this.endHour,this.endMinute));
        timer = new javax.swing.Timer(1000, e -> {
            checkTime();
        });
        timer.start();


    }
    private void checkTime(){
        currentTime=LocalTime.now();
        timeStart=lblStart.getText();
        timeEnd=lblEnd.getText();
        if(currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")).equals(timeStart)){
            label.setText("ON");
        }else if(currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")).equals(timeEnd)){
            label.setText("OFF");
        }

    }

}
class MainWindow{
    public static void main(String args []){
        SmartHomeController smartHomeController=new SmartHomeController();

        smartHomeController.addSwitch(new SwitchBoard("TV Living Room",10,200));
        smartHomeController.addSwitch(new SwitchBoard("Speaker",310,200));
        smartHomeController.addSwitch(new SwitchBoard("Window",10,410));
        smartHomeController.addSwitch(new SwitchBoard("TV Dining Room",310,410));
        new Switch(smartHomeController).setVisible(true);
        new Time().setVisible(true);

    }
}
                                           
