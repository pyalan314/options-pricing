/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package OptionPricing;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author kailun
 */
class InputPanel extends JPanel implements ActionListener{
    private Simulator mySim;
    private Item arg[], result[];
    private JButton sim;
    public InputPanel() {
        setLayout(new GridLayout(0,1));
        arg = new Item[25];
        result = new Item[2];
        short basic = 1;
        arg[basic++] = new Item("Unit Value", "10000");
        arg[basic++] = new Item("Marked Dates(fixing, settlement, etc)", "_data\\markeddate.txt");
        arg[basic++] = new Item("Stock 1 data", "_data\\368HK.txt");
        arg[basic++] = new Item("Stock 2 data", "_data\\1800HK.txt");
        arg[basic++] = new Item("Stock 3 data", "_data\\3968HK.txt");
        arg[basic++] = new Item("Stock 1 parameter", "_data\\368parameter.txt");
        arg[basic++] = new Item("Stock 2 parameter", "_data\\1800parameter.txt");
        arg[basic++] = new Item("Stock 3 parameter", "_data\\3968parameter.txt");
        arg[basic++] = new Item("Stock Correlation", "_data\\newcorr.txt");
        arg[basic++] = new Item("Fixed Distribution Strike Price (% of ISP)", "0.01");
        arg[basic++] = new Item("Distribution Refrence Price (% of ISP)", "0.84");
        arg[basic++] = new Item("Knock-In Price (% of ISP)", "0.68");
        arg[basic++] = new Item("Settlement Conversion Price (% of ISP)", "0.84");
        arg[basic++] = new Item("Call Strike Price (% of ISP)", "0.95");
        arg[basic++] = new Item("Distribution Period", "_data\\distdate.txt");
        arg[basic++] = new Item("Call Valuation Date", "_data\\calldate.txt");
        arg[basic++] = new Item("Accural Factor", "0.05");
        for(short i=1; i<basic; i++) add(arg[i]);

        sim = new JButton("Simulate");
        sim.addActionListener(this);
        add(sim);

        result[0] = new Item("Derivative Price", "");
        result[1] = new Item("Time Elapsed (ms)", "");
        for(short i=0; i<2; i++) add(result[i]);

        add(new Label("More options", Label.CENTER));
        arg[0] = new Item("Trading Days Database", "_data\\datedb.txt");
        add(arg[0]);
        short extra = basic;
        arg[extra++] = new Item("Forward Curve", "_data\\forward.txt");
        arg[extra++] = new Item("Yield Curve", "_data\\yield.txt");
        arg[extra++] = new Item("Iteration number", "1");
        for(short i=basic; i<extra; i++) add(arg[i]);
        //mySim = new Simulator(arg);
        //DataOutput dop = new DataOutput();
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==sim) {
            //DataOutput dop = new DataOutput();
            mySim = new Simulator();
            mySim.compute(arg);
            //result[0].setValue(myData.getPrice());
            //result[1].setValue(myData.getTime());
        }
    }
}