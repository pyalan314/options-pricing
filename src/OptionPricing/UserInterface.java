/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package OptionPricing;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author kailun
 */
public class UserInterface extends JFrame  implements WindowListener,ActionListener{
    private Dialog about;
    private JMenuBar jmb;
    private JMenu jms[];
    private JMenuItem jmis[];
    private Button aboutBtn;

    public static void main(String[] args) {
        UserInterface MyUI = new UserInterface();
        MyUI.setSize(600,500);
        MyUI.setVisible(true);
        //MyUI.setVisible(false);
        //System.exit(0);
    }
    public UserInterface() {
        super.setTitle("OptionPricing");
        addWindowListener(this);
        setMenuBar();
        setInputPanel();
        setAboutDialog();
    }
    public void setMenuBar() {
        jmb=new JMenuBar();
        jms=new JMenu[2];
        jms[0]=new JMenu("Option");
        jms[1]=new JMenu("About");
        jmb.add(jms[0]);
        jmb.add(jms[1]);
        jmis= new JMenuItem[5];
        int itemnum = 0;
        jms[0].add(jmis[itemnum++]=new JMenuItem("Setting"));
        jms[0].add(jmis[itemnum++]=new JMenuItem("Option 2"));
        jms[0].add(jmis[itemnum++]=new JMenuItem("Option 3"));
        jms[0].add(jmis[itemnum++]=new JMenuItem("Quit"));
        jms[1].add(jmis[itemnum++]=new JMenuItem("About"));
        for (int i = 0; i < itemnum; i++) jmis[i].addActionListener(this);
        this.setJMenuBar(jmb);
    }
    public void setInputPanel() {
        InputPanel iPanel=new InputPanel();
        this.add(iPanel);
        this.pack();
    }
    public void setAboutDialog() {
        about = new Dialog(this, "About", true);
        about.setSize(300, 200);
        about.add(new JLabel("<html>Developed by:<br>Chan Sau Lung<br>Chong Wing Ling<br>Wong Kai Lun</html>",JLabel.CENTER));
        aboutBtn = new Button("Close");
        aboutBtn.addActionListener(this);
        about.add(aboutBtn,BorderLayout.SOUTH);
    }
    public void showAbout() {about.setVisible(true);}
    public void closeAbout() {about.setVisible(false);}
    public void windowClosing(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {dispose(); System.exit(0);}
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==jmis[4]) {showAbout();}
        else if(e.getSource()==jmis[3]) {System.exit(0);}
        else if(e.getSource()==aboutBtn) {closeAbout();}
    }
}