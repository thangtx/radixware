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

/*
 * BoundsPanel.java
 *
 * Created on Apr 26, 2012, 1:42:07 PM
 */
package org.radixware.kernel.common.design.msdleditor.piece;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import org.radixware.kernel.common.design.msdleditor.AbstractEditItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.design.msdleditor.field.panel.simple.AlignPanel;
import org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtHexPanel;
import org.radixware.kernel.common.design.msdleditor.enums.EAlign;
import org.radixware.kernel.common.msdl.enums.EEncoding;
import org.radixware.schemas.msdl.EmbeddedLenDef;


public class BoundsPanel extends AbstractEditItem {
    
    private JCheckBox boundedCheckBox;
    private JSpinner highBoundSpinner;
    private JLabel jLabelHighBound;
    private JLabel jLabelLowBound;
    private JSpinner lowBoundSpinner;
    
    private JPanel lowPanel;
    private JPanel highPanel;
    private JPanel checkBoxPanel;
    
    //Panel, holding low, high and checkbox panels
    private JPanel paramsPanel;
    
    //Panel, holding align and pad panels
    private JPanel storagePanel;
    
    private AlignPanel alignPane;
    private ExtHexPanel padPanel;
    
    boolean boundsSet;
    
    public final class BoundsPair {
        public int low, high;
        public BoundsPair(int low, int high) {
            this.low = low; this.high = high;
        }
    }
    
    private int getSpinnerValue(JSpinner spin) {
        return ((Integer)spin.getModel().getValue()).intValue();
    }
    
    public BoundsPair getBoundsPair() {
        if(boundsSet && 
                (getSpinnerValue(highBoundSpinner) > getSpinnerValue(lowBoundSpinner)) 
                )
            return new BoundsPair(getSpinnerValue(lowBoundSpinner),
                    getSpinnerValue(highBoundSpinner)
                    );
        return null;
    }
    
    public BoundsPanel() {
        initComponents();
        boundsSet = false;
    }
    
    private void initComponents() {
//        boundedCheckBox = new JCheckBox();
//        boundedCheckBox.setText("Bounded");
//        
//        boundedCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
//        checkBoxPanel = new JPanel();
//        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.X_AXIS));
//        checkBoxPanel.add(boundedCheckBox);
//        checkBoxPanel.add(Box.createGlue());
//        boundedCheckBox.addItemListener(new ItemListener() {
//
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                boolean selected = e.getStateChange() == ItemEvent.SELECTED;
//                setPanelsEnabled(selected);
//            }
//        });
//        
//        
//        jLabelLowBound = new  JLabel();
//        jLabelLowBound.setText("Low bound");
//        lowBoundSpinner = new JSpinner();
//        lowBoundSpinner.setModel(new javax.swing.SpinnerNumberModel());
//                
//        jLabelHighBound = new JLabel();
//        highBoundSpinner = new JSpinner();     
//        jLabelHighBound.setText("High bound");
//        highBoundSpinner.setModel(new javax.swing.SpinnerNumberModel());
//        
//        lowBoundSpinner.setPreferredSize(new Dimension(40, lowBoundSpinner.getPreferredSize().height));
//        highBoundSpinner.setPreferredSize(new Dimension(40, lowBoundSpinner.getPreferredSize().height));
//        
//        lowPanel = new JPanel();
//        lowPanel.setLayout(new BoxLayout(lowPanel, BoxLayout.X_AXIS));
//        lowPanel.add(jLabelLowBound);
//        lowPanel.add(Box.createGlue());
//        lowPanel.add(lowBoundSpinner);
//        
//        highPanel = new JPanel();
//        highPanel.setLayout(new BoxLayout(highPanel, BoxLayout.X_AXIS));
//        highPanel.add(jLabelHighBound);
//        highPanel.add(Box.createGlue());
//        highPanel.add(highBoundSpinner);
//        
//        paramsPanel = new JPanel();
//        paramsPanel.setLayout(new BoxLayout(paramsPanel, BoxLayout.Y_AXIS));
//        paramsPanel.add(checkBoxPanel);
//        paramsPanel.add(lowPanel);
//        paramsPanel.add(highPanel);
//       
//        JPanel alignAndLabel = new JPanel();
//        alignAndLabel.setLayout(new BoxLayout(alignAndLabel, BoxLayout.X_AXIS));
//        alignAndLabel.add(new JLabel("Alignment: "));
//        //alignAndLabel.add(Box.createGlue());
//        alignAndLabel.add(Box.createHorizontalStrut(10));
//        alignPane = new AlignPanel(new EAlign[]{EAlign.LEFT,EAlign.RIGHT});
//        alignAndLabel.add(alignPane);
//        
//        JPanel padAndLabel = new JPanel();
//        padAndLabel.setLayout(new BoxLayout(padAndLabel, BoxLayout.X_AXIS));
//        padAndLabel.add(new JLabel("Padding: "));
////        padAndLabel.add(Box.createGlue());
//        padAndLabel.add(Box.createHorizontalStrut(10));
//        padPanel = new ExtHexPanel();
//        padAndLabel.add(padPanel);
//                
//        storagePanel = new JPanel();
//        storagePanel.setLayout(new BoxLayout(storagePanel, BoxLayout.Y_AXIS));
//        storagePanel.add(alignAndLabel);
//        storagePanel.add(padAndLabel);
//        
//                
//        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
//        add(paramsPanel);
////        add(Box.createHorizontalStrut(10));
//        add(Box.createGlue());
//        add(storagePanel);
//        setPreferredSize(new Dimension(400, 10));
//        setPanelsEnabled(false);
        setLayout(new GridBagLayout());
        
        boundedCheckBox = new JCheckBox();
        boundedCheckBox.setText("Bounded");
        
        boundedCheckBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean selected = e.getStateChange() == ItemEvent.SELECTED;
                setPanelsEnabled(selected);
            }
        });
        
        {
        GridBagConstraints с = new GridBagConstraints();
        с.gridx = 0;
        с.gridy = 0;
        с.gridheight = 1;
        с.gridwidth = 4;
        с.anchor = GridBagConstraints.WEST;
        с.fill = GridBagConstraints.HORIZONTAL;
        add(boundedCheckBox, с);
        }
        
        
        jLabelLowBound = new  JLabel("Low bound");
        add(jLabelLowBound, createConstraint(0, 1, 1, 0));
        
        lowBoundSpinner = new JSpinner(new javax.swing.SpinnerNumberModel());
        lowBoundSpinner.setPreferredSize(new Dimension(40, lowBoundSpinner.getPreferredSize().height));
        add(lowBoundSpinner, createConstraint(1, 1, 1, 0.4));
                
        jLabelHighBound = new JLabel("High bound");
        add(jLabelHighBound, createConstraint(0, 2, 1, 0));
        
        highBoundSpinner = new JSpinner(new javax.swing.SpinnerNumberModel());
        highBoundSpinner.setPreferredSize(new Dimension(40, lowBoundSpinner.getPreferredSize().height));
        add(highBoundSpinner, createConstraint(1, 2, 1, 0.4));
                
        

        JLabel alignLabel = new JLabel("Alignment: ");
        add(alignLabel, createConstraint(2, 1, 1, 0));

        alignPane = new AlignPanel(new EAlign[]{EAlign.LEFT,EAlign.RIGHT});
        add(alignPane, createConstraint(3, 1, 1, 0.4));
        
        JLabel padLabel = new JLabel("Padding: ");
        add(padLabel, createConstraint(2, 2, 1, 0));
        
        padPanel = new ExtHexPanel();
        add(padPanel, createConstraint(3, 2, 1, 0.4));
                
        setPreferredSize(new Dimension(400, 10));
        setPanelsEnabled(false);
    }
    
    private void setPanelsEnabled(boolean status) {
        boundsSet = status;
        highBoundSpinner.setEnabled(status);
        lowBoundSpinner.setEnabled(status);
        alignPane.setEnabled(status);
        padPanel.setEnabled(status);
    }
    
    /**
     * Strangely enough, add change listener to bounds spinners. It's a common
     * pattern around MSDL editor widgets
     * @param l Some class, implementing ActionListener interface
     */
    public void addChangeListener(final ChangeListener l) {
        highBoundSpinner.addChangeListener(l);
        lowBoundSpinner.addChangeListener(l);
        boundedCheckBox.addChangeListener(l);
        
    }
    
    public EAlign getAlignment() {
        return alignPane.getAlign();
    }
    
    public byte[] getPadding() {
        return padPanel.getValue();
    }
    
    public void setBounds(EmbeddedLenDef.Bounds b) {
        if(!boundedCheckBox.isSelected())
            boundedCheckBox.doClick();
        lowBoundSpinner.setValue(b.getLowBound() );
        highBoundSpinner.setValue(b.getHighBound());
    }
    
    public void setAlign(EAlign value, EAlign parentValue) {
        alignPane.setAlign(value, parentValue);
    }
    
    public void setPad(byte [] value, byte[] parentValue, EEncoding viewType, EEncoding parentViewType) {
        padPanel.setValue(value, parentValue, viewType, parentViewType);
    }
    
    public void addActionListener (ActionListener l) {
        alignPane.getSetParentPanel().addActionListener(l);
        alignPane.addActionListener(l);
        
        padPanel.addActionListener(l);
    }
    
    private GridBagConstraints createConstraint(int x, int y, int horSize, double horWeight) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.gridheight = c.gridwidth = horSize;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = horWeight;
        if(horWeight != 0.0)
            c.fill = GridBagConstraints.HORIZONTAL;
        else
            c.fill = GridBagConstraints.NONE;
        return c;
    }
}
