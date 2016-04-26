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

package org.radixware.kernel.designer.common.dialogs.commitpanel;

import java.awt.Dimension;
//import org.netbeans.modules.versioning.util.VerticallyNonResizingPanel;
import static java.awt.Component.LEFT_ALIGNMENT;
import static javax.swing.BoxLayout.X_AXIS;
import javax.swing.*;
//import org.netbeans.modules.subversion.SvnModuleConfig;
//import org.jdesktop.layout.LayoutStyle;

//import static org.jdesktop.layout.LayoutStyle.UNRELATED;

import org.radixware.kernel.common.svn.RadixIssueComitType;
import org.radixware.kernel.common.utils.Utils;


public class MicroCommitPanel extends JPanel {

    private JComboBox typeCommitComboBox = new JComboBox();
    private JComboBox issueCommitComboBox = new JComboBox();
    private JComboBox componentCommitComboBox = new JComboBox();
    private JTextField firstAppearedTextField = new JTextField();
    private static final int MAX_CFG_RECORTS = 30;
    private static final String TYPE_CFG_NAME = "Type_cfg_commit";
    private static final String ISSUE_CFG_NAME = "Issue_cfg_commit";
    private static final String FIRST_APPERED_VERSION = "First_Appeared_Version";
    
    private static final String COMPONENT_CFG_NAME = "Component_cfg_commit";
    private static final String IS_EMPTY = "Is_empty";
    private static final String CFG = "MicroCommitPanel";
    private static final String LAST_COMMOT_MASSEGE = "LastCommitMessage";

    public MicroCommitPanel() {
        initComponents();

    }
    
    @Override
    public Dimension getMaximumSize() { 
        Dimension origPref = super.getPreferredSize();
        Dimension origMax = super.getMaximumSize();
        int height = origPref.height;
        height = Math.min(height, 28);
        return new Dimension(origMax.width, height);
    }
    
    @Override
    public Dimension getMinimumSize() { 
        Dimension origPref = super.getPreferredSize();
        Dimension origMax = super.getMinimumSize();
        int height = origPref.height;
        height = Math.max(height, 28);
        return new Dimension(origMax.width, height);
    }
    
    
//    public void addAndSetMergeMode() { 
//        typeCommitComboBox.addItem(RadixIssueComitType.MERGE);
//        typeCommitComboBox.setSelectedItem(RadixIssueComitType.MERGE);
//    }

    
    static public String getLastCommitMessage(){
         return Utils.findOrCreatePreferences(CFG).get(LAST_COMMOT_MASSEGE, null);                
    }
    
    static public void setLastCommitMessage(String s){
         Utils.findOrCreatePreferences(CFG).put(LAST_COMMOT_MASSEGE, s);
    }
    
    private void initComponents() {


        this.setLayout(new BoxLayout(this, X_AXIS));
        
        JLabel lbl = new JLabel("Type:");
        this.add(lbl);


        typeCommitComboBox.addItem(RadixIssueComitType.MAJOR_IMPROVEMENT);
        typeCommitComboBox.addItem(RadixIssueComitType.MINOR_IMPROVEMENT);
        typeCommitComboBox.addItem(RadixIssueComitType.CHANGE);
        typeCommitComboBox.addItem(RadixIssueComitType.DELETION);
        typeCommitComboBox.addItem(RadixIssueComitType.BUG_FIX);
        typeCommitComboBox.addItem(RadixIssueComitType.INCOMPLETE);
        typeCommitComboBox.addItem(RadixIssueComitType.REFACTORING);
        typeCommitComboBox.addItem(RadixIssueComitType.INTERNAL_IMPROVEMENT);
        typeCommitComboBox.addItem(RadixIssueComitType.INTERNAL_BUG_FIX);


        this.add(lbl);




        typeCommitComboBox.getMaximumSize();
        Dimension size1 = new Dimension(130, Short.MAX_VALUE);
        typeCommitComboBox.setMaximumSize(size1);
        typeCommitComboBox.setMinimumSize(size1);
        this.add(typeCommitComboBox);



        int index = Utils.findOrCreatePreferences(CFG).getInt(TYPE_CFG_NAME, 0);
        if (index >= 0 && index < typeCommitComboBox.getItemCount()) {
            typeCommitComboBox.setSelectedIndex(index);
        }


        JLabel lbl2 = new JLabel(" Issue: ");
        this.add(lbl2);



        issueCommitComboBox.setEditable(true);
        Dimension size2 = new Dimension(70, Short.MAX_VALUE);
        issueCommitComboBox.setMaximumSize(size2);

        for (int i = 0; i < MAX_CFG_RECORTS; i++) {
            String val = Utils.findOrCreatePreferences(CFG).get(ISSUE_CFG_NAME + String.valueOf(i), "");
            if (val == null || val.isEmpty()) {
                break;
            }
            issueCommitComboBox.addItem(val);
        }
        if (Utils.findOrCreatePreferences(CFG).getBoolean(ISSUE_CFG_NAME + IS_EMPTY, false)) {
            issueCommitComboBox.setSelectedIndex(-1);
        }



        for (int i = 0; i < MAX_CFG_RECORTS; i++) {
            String val = Utils.findOrCreatePreferences(CFG).get(COMPONENT_CFG_NAME + String.valueOf(i), "");
            if (val == null || val.isEmpty()) {
                break;
            }
            componentCommitComboBox.addItem(val);
        }
        if (Utils.findOrCreatePreferences(CFG).getBoolean(COMPONENT_CFG_NAME + IS_EMPTY, false)) {
            componentCommitComboBox.setSelectedIndex(-1);
        }
        
        firstAppearedTextField.setText(Utils.findOrCreatePreferences(CFG).get(FIRST_APPERED_VERSION, ""));



        this.add(issueCommitComboBox);


        JLabel lbl3 = new JLabel(" Component: ");
        this.add(lbl3);

        componentCommitComboBox.setEditable(true);


        this.add(componentCommitComboBox);





        componentCommitComboBox.setAlignmentX(LEFT_ALIGNMENT);
        
        JLabel lbl4 = new JLabel(" First appeared in: ");        
        this.add(lbl4);
        
        
        this.add(firstAppearedTextField);
        
    }

    public String getCommitMessageEx(String message) {
        String issue = (String) issueCommitComboBox.getSelectedItem();
        if (issue == null) {
            issue = "";
        }
        String component = (String) componentCommitComboBox.getSelectedItem();
        if (component == null) {
            component = "";
        }
        
        String firstAppearedVersion = firstAppearedTextField.getText();
        if (firstAppearedVersion==null)
            firstAppearedVersion = "";
        else
            firstAppearedVersion = firstAppearedVersion.trim();
        
        if (!firstAppearedVersion.isEmpty())
            firstAppearedVersion = " <" + firstAppearedVersion + ">"; 
        

        return "["
                + ((RadixIssueComitType) typeCommitComboBox.getSelectedItem()).getValue()
                + "] ("
                + issue
                + ") "
                + component
                + ". "
                + message
                + firstAppearedVersion;        
    }

    public void saveConfigurationOptions() {

        Utils.findOrCreatePreferences(CFG).putInt(TYPE_CFG_NAME, typeCommitComboBox.getSelectedIndex());

        String currVal = (String) issueCommitComboBox.getSelectedItem();
        if (currVal != null && !currVal.isEmpty()) {
            for (int i = issueCommitComboBox.getItemCount() - 1; i >= 0; i--) {
                String oldVal = (String) issueCommitComboBox.getItemAt(i);
                if (oldVal.equals(currVal)) {
                    issueCommitComboBox.removeItemAt(i);
                    //break;
                }
            }
            if (issueCommitComboBox.getItemCount() == 0) {
                issueCommitComboBox.addItem(currVal);
            } else {
                issueCommitComboBox.insertItemAt(currVal, 0);
            }

            for (int i = 0, n = Math.min(MAX_CFG_RECORTS, issueCommitComboBox.getItemCount()); i < n; i++) {
                Utils.findOrCreatePreferences(CFG).put(
                        ISSUE_CFG_NAME + String.valueOf(i),
                        (String) issueCommitComboBox.getItemAt(i));
            }
        }

        {
            String S = (String) issueCommitComboBox.getSelectedItem();
            Utils.findOrCreatePreferences(CFG).putBoolean(ISSUE_CFG_NAME + IS_EMPTY, S == null || S.isEmpty());
            //if (issueCommitComboBox.getItemCount()>0)
            //    issueCommitComboBox.setSelectedIndex(0);
            issueCommitComboBox.setSelectedItem(currVal);
        }





        currVal = (String) componentCommitComboBox.getSelectedItem();
        if (currVal != null && !currVal.isEmpty()) {
            for (int i = componentCommitComboBox.getItemCount() - 1; i >= 0; i--) {
                String oldVal = (String) componentCommitComboBox.getItemAt(i);
                if (oldVal.equals(currVal)) {
                    componentCommitComboBox.removeItemAt(i);
                    //break;
                }
            }
            if (componentCommitComboBox.getItemCount() == 0) {
                componentCommitComboBox.addItem(currVal);
            } else {
                componentCommitComboBox.insertItemAt(currVal, 0);
            }

            for (int i = 0, n = Math.min(MAX_CFG_RECORTS, componentCommitComboBox.getItemCount()); i < n; i++) {
                Utils.findOrCreatePreferences(CFG).put(
                        COMPONENT_CFG_NAME + String.valueOf(i),
                        (String) componentCommitComboBox.getItemAt(i));
            }
        }
        {
            String S = (String) componentCommitComboBox.getSelectedItem();
            Utils.findOrCreatePreferences(CFG).putBoolean(COMPONENT_CFG_NAME + IS_EMPTY, S == null || S.isEmpty());
            componentCommitComboBox.setSelectedItem(currVal);
        }

        Utils.findOrCreatePreferences(CFG).put(FIRST_APPERED_VERSION, firstAppearedTextField.getText());
 
    
    }    
}
