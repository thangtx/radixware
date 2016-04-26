/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.designer.common.dialogs.components;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 *
 */
public class ComponentTitledBorder implements Border, MouseListener, SwingConstants {

    protected int offset = 5;
    protected  Component comp;
    protected JComponent container;
    protected Rectangle rect;
    protected Border border;
    protected JPanel panel;
    private Dimension defaultSize;

    /**
     * @deprecated ComponentTitledBorder replaced by ExternalPanel
     */
    @Deprecated
    public ComponentTitledBorder(final Component comp, JComponent container, Border border) {

        defaultSize = comp.getPreferredSize();
        this.panel = new JPanel(){

            @Override
            public Dimension getMinimumSize() {
                return defaultSize;//comp.getPreferredSize();
            }

            @Override
            public Dimension getMaximumSize() {
                return defaultSize;//comp.getPreferredSize();
            }

            @Override
            public Dimension getPreferredSize() {
                return defaultSize;//comp.getPreferredSize();
            }

            @Override
            public Rectangle getBounds() {
                return new Rectangle(offset, 0, defaultSize.width, defaultSize.height);
            }

        };
        this.panel.setBackground(container.getBackground());
        this.panel.setLayout(new BorderLayout());
        panel.setAlignmentX(0.0f);
        this.panel.add(comp, BorderLayout.CENTER);

        this.comp = comp;
        this.container = container;
        container.add(panel);
        this.border = border;
        container.addMouseListener(this);
        final Insets borderInsets = border.getBorderInsets(container);
        offset += borderInsets.left;
        rect = new Rectangle(offset, 0, defaultSize.width + borderInsets.left , defaultSize.height);//size.width, size.height);
        //comp.setBounds(rect);
    }

    public int getOffset(){
        return offset;
    }

    public int getComponentWidth(){
        return rect != null ? rect.width : -1;
    }

    @Override
    public boolean isBorderOpaque() {
        return border != null && border.isBorderOpaque();
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        final Insets insets = getBorderInsets(c);
        final int temp = (insets.top ) / 2;//- borderInsets.top
        border.paintBorder(c, g, x, y + temp, width, height - temp);
        if (panel.isVisible()&& comp.isVisible()){
            SwingUtilities.paintComponent(g, panel, container, rect);
            SwingUtilities.paintComponent(g, comp, panel, rect); 
        } else {
          border.paintBorder(c, g, x, y + temp, width, height  - temp);   
        }
    }

    @Override
    public Insets getBorderInsets(Component c) {
        final Dimension size = panel.getPreferredSize();
        final Insets insets = border.getBorderInsets(c);
        insets.top = Math.max(insets.top, size.height);
        insets.bottom += (insets.top - border.getBorderInsets(c).top) / 2;
        return insets;
    }

    private void dispatchEvent(MouseEvent me) {
        if (rect != null && rect.contains(me.getX(), me.getY())) {
            if (comp.isVisible()){
                final Point pt = me.getPoint();
                pt.translate(-offset, 0);
                comp.setBounds(rect);
                comp.dispatchEvent(new MouseEvent(comp, me.getID(), me.getWhen(), me.getModifiers(), pt.x, pt.y, me.getClickCount(), me.isPopupTrigger(), me.getButton()));
                panel.repaint();
                container.repaint();
                container.revalidate();
            }
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {
        dispatchEvent(me);
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        dispatchEvent(me);
    }

    @Override
    public void mouseExited(MouseEvent me) {
        dispatchEvent(me);
    }

    @Override
    public void mousePressed(MouseEvent me) {
        dispatchEvent(me);
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        dispatchEvent(me);
    }

    //example of usage
    /*
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        final JPanel proxyPanel = new JPanel();
        proxyPanel.add(new JLabel("Proxy Host: "));
        proxyPanel.add(new JTextField("proxy.xyz.com"));
        proxyPanel.add(new JLabel("  Proxy Port"));
        proxyPanel.add(new JTextField("8080"));
        final JCheckBox checkBox = new JCheckBox("Use Proxy", true);
        checkBox.setFocusPainted(false);
        ComponentTitledBorder componentBorder =
                new ComponentTitledBorder(checkBox, proxyPanel, BorderFactory.createEtchedBorder());
        checkBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean enable = checkBox.isSelected();
                Component comp[] = proxyPanel.getComponents();
                for (int i = 0; i < comp.length; i++) {
                    comp[i].setEnabled(enable);
                }
            }
        });
        proxyPanel.setBorder(componentBorder);
        JFrame frame = new JFrame("ComponentTitledBorder - santhosh@in.fiorano.com");
        Container contents = frame.getContentPane();
        contents.setLayout(new FlowLayout());
        contents.add(proxyPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }*/
}
