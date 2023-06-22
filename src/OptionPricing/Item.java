/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package OptionPricing;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;


/**
 *
 * @author kailun
 */
    public class Item extends Panel {
        TextField text;
        Label label;
        public Item(String name, String initial) {
            setLayout(new GridLayout(0,2));
            text = new TextField(initial,10);
            label = new Label(name, Label.LEFT);
            add(label); add(text);
        }
        public String getText() {return text.getText();}
        public float getValue() {return Float.parseFloat(text.getText());}
        public void setValue(double t) {text.setText(Double.toString(t));}

    }
