/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.designer.common.editors.documentation;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 *
 * @author dkurlyanov
 */
public class HotKey {
      public static void main(String[] args) {
        JButton button = new JButton();
        JButton button2 = new JButton();
        button.setAction(new AbstractAction("press me") {
            {
                putValue(Action.ACTION_COMMAND_KEY, getValue(Action.NAME));
            }
            public void actionPerformed(ActionEvent e) {
                System.out.println(e.getActionCommand());
            }
        });
        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        JList list = new JList();
        panel.add(list);
        panel.add(panel2);
        panel2.add(button);
        panel.add(button2);
        button2.setText("test");
        panel.setPreferredSize(new Dimension(200,100));
        // Bind a keystroke to the button to act as accelerator.
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_INSERT,
                                    0);//InputEvent.SHIFT_DOWN_MASK
        panel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(ks, "add map");
        panel.getActionMap().put("add map", button.getAction());
        JOptionPane.showMessageDialog(null, panel,"", -1);
    }
    
}
