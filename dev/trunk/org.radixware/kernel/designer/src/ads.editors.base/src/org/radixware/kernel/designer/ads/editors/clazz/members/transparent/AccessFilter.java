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
 * AccessFilterPanel.java
 *
 * Created on May 22, 2012, 11:19:38 AM
 */
package org.radixware.kernel.designer.ads.editors.clazz.members.transparent;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.designer.common.dialogs.components.selector.IItemFilter;


final class AccessFilter extends JPanel implements IItemFilter<ClassMemberItem> {
//    private static final String FINAL = "final";
//    private static final String STATIC = "static";

    public AccessFilter() {
        initComponents();

        init();
    }
    private final Map<Object, JCheckBox> accessMap = new HashMap<>();

    private void init() {
        JCheckBox checkBox;
        for (EClassMemberType type : EClassMemberType.values()) {
            checkBox = new JCheckBox(type.getName() + "s", true);
            installCheckBox(type, checkBox);
        }

        for (final EClassMemberExtraFilterType type : EClassMemberExtraFilterType.values()) {
            checkBox = new JCheckBox(type.getName(), true);
            installCheckBox(type, checkBox);
            
            final JCheckBox mainCheckBox = accessMap.get(type.getMember());
            if (mainCheckBox != null){
                ItemListener listener = new ItemListener() {
                    JCheckBox currentCheckBox = accessMap.get(type);
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (currentCheckBox != null){
                            if (mainCheckBox.isSelected()) {
                                currentCheckBox.setEnabled(true);
                            } else {
                                currentCheckBox.setEnabled(false);
                                currentCheckBox.setSelected(false);
                            }
                        }
                    }
                };
                mainCheckBox.addItemListener(listener);
            }
        }
        add(Box.createHorizontalGlue());
//        for (EAccess access : EAccess.values()) {
//            if (access != EAccess.PRIVATE && access != EAccess.DEFAULT) {
//                checkBox = new JCheckBox(access.getName(), true);
//                installCheckBox(access, checkBox);
//            }
//        }
//
//        checkBox = new JCheckBox(STATIC, true);
//        installCheckBox(STATIC, checkBox);
//
//        checkBox = new JCheckBox(FINAL, true);
//        installCheckBox(FINAL, checkBox);
    }

    private void installCheckBox(Object key, JCheckBox checkBox) {
        add(checkBox);
        accessMap.put(key, checkBox);
        checkBox.addItemListener(itemListener);
        checkBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
    }
    private final ItemListener itemListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            changeSupport.fireChange();
        }
    };
    private final ChangeSupport changeSupport = new ChangeSupport(this);

    @Override
    public boolean accept(final ClassMemberItem value) {
        boolean result = true;
        if (value != null) {
            JCheckBox checkBox = accessMap.get(value.getClassMemberType());
            result = result && checkBox != null && checkBox.isSelected(); 
            if (result && value instanceof ClassMethodItem){
                checkBox = accessMap.get(EClassMemberExtraFilterType.OVERRIDEN_METHODS);
                if (checkBox == null || !checkBox.isSelected()){
                    result = !((ClassMethodItem) value).isOverriden();
                }
                
            }

//            checkBox = accessMap.get(value.getAccess());
//            result = result && checkBox != null && checkBox.isSelected();
//
//            checkBox = accessMap.get(STATIC);
//            result = result && (!value.isStatic() || checkBox.isSelected());
//
//            checkBox = accessMap.get(FINAL);
//            result = result && (!value.isFinal() || checkBox.isSelected());
        }
        return result;
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 0, 4, 0));
        setAlignmentY(0.0F);
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public Component getComponent() {
       return this;
    }

    @Override
    public void reset() {
        for (Object type : accessMap.keySet()) {
            JCheckBox checkBox = accessMap.get(type);
            if (type instanceof EClassMemberType){
                checkBox.setSelected(true);
            } else {
                checkBox.setSelected(false);
            }
        }
        
    }
}
