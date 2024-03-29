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

package org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class TimeoutPanel extends JPanel {

    public TimeoutPanel() {
        initComponents();        
    }

    public TimeoutPanel(String title) {
        initComponents();
        chkTimeout.setText(title);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chkTimeout = new javax.swing.JCheckBox();
        spinnerSeconds = new javax.swing.JSpinner();
        spinnerMinutes = new javax.swing.JSpinner();
        spinnerDays = new javax.swing.JSpinner();
        labelHours = new javax.swing.JLabel();
        spinnerHours = new javax.swing.JSpinner();
        labelDays = new javax.swing.JLabel();
        labelSeconds = new javax.swing.JLabel();
        labelMinutes = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TimeoutPanel.class, "TimeoutPanel.border.title"))); // NOI18N
        setMinimumSize(new java.awt.Dimension(250, 70));
        setPreferredSize(new java.awt.Dimension(250, 70));
        setRequestFocusEnabled(false);
        setLayout(null);

        chkTimeout.setText(org.openide.util.NbBundle.getMessage(TimeoutPanel.class, "TimeoutPanel.chkTimeout.text")); // NOI18N
        chkTimeout.setMaximumSize(new java.awt.Dimension(75, 23));
        chkTimeout.setMinimumSize(new java.awt.Dimension(75, 23));
        chkTimeout.setPreferredSize(new java.awt.Dimension(75, 23));
        chkTimeout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkTimeoutActionPerformed(evt);
            }
        });
        add(chkTimeout);
        chkTimeout.setBounds(10, -2, 110, 23);

        spinnerSeconds.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerSecondsStateChanged(evt);
            }
        });
        add(spinnerSeconds);
        spinnerSeconds.setBounds(100, 102, 100, 20);

        spinnerMinutes.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerMinutesStateChanged(evt);
            }
        });
        add(spinnerMinutes);
        spinnerMinutes.setBounds(100, 77, 100, 20);

        spinnerDays.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerDaysStateChanged(evt);
            }
        });
        add(spinnerDays);
        spinnerDays.setBounds(100, 27, 100, 20);

        labelHours.setText(org.openide.util.NbBundle.getMessage(TimeoutPanel.class, "TimeoutPanel.labelHours.text")); // NOI18N
        add(labelHours);
        labelHours.setBounds(20, 55, 70, 14);

        spinnerHours.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerHoursStateChanged(evt);
            }
        });
        add(spinnerHours);
        spinnerHours.setBounds(100, 52, 100, 20);

        labelDays.setText(org.openide.util.NbBundle.getMessage(TimeoutPanel.class, "TimeoutPanel.labelDays.text")); // NOI18N
        add(labelDays);
        labelDays.setBounds(20, 30, 70, 14);

        labelSeconds.setText(org.openide.util.NbBundle.getMessage(TimeoutPanel.class, "TimeoutPanel.labelSeconds.text")); // NOI18N
        add(labelSeconds);
        labelSeconds.setBounds(20, 105, 70, 14);

        labelMinutes.setText(org.openide.util.NbBundle.getMessage(TimeoutPanel.class, "TimeoutPanel.labelMinutes.text")); // NOI18N
        add(labelMinutes);
        labelMinutes.setBounds(20, 80, 70, 14);
    }// </editor-fold>//GEN-END:initComponents

    private void chkTimeoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkTimeoutActionPerformed
        // TODO add your handling code here:
        update(chkTimeout.isSelected());
        if (!locked)
            fireStateChanged();
    }//GEN-LAST:event_chkTimeoutActionPerformed

    private void spinnerDaysStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerDaysStateChanged
        // TODO add your handling code here:
        if (!locked)
            fireStateChanged();
    }//GEN-LAST:event_spinnerDaysStateChanged

    private void spinnerHoursStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerHoursStateChanged
        // TODO add your handling code here:
        if (!locked)
            fireStateChanged();
    }//GEN-LAST:event_spinnerHoursStateChanged

    private void spinnerMinutesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerMinutesStateChanged
        // TODO add your handling code here:
        if (!locked)
            fireStateChanged();
    }//GEN-LAST:event_spinnerMinutesStateChanged

    private void spinnerSecondsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerSecondsStateChanged
        // TODO add your handling code here:
        if (!locked)
            fireStateChanged();
    }//GEN-LAST:event_spinnerSecondsStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkTimeout;
    private javax.swing.JLabel labelDays;
    private javax.swing.JLabel labelHours;
    private javax.swing.JLabel labelMinutes;
    private javax.swing.JLabel labelSeconds;
    private javax.swing.JSpinner spinnerDays;
    private javax.swing.JSpinner spinnerHours;
    private javax.swing.JSpinner spinnerMinutes;
    private javax.swing.JSpinner spinnerSeconds;
    // End of variables declaration//GEN-END:variables

    private boolean locked = false;
    private boolean notNull = false;

    public void setTimeout(BigDecimal timeout) {
        int days = 0, hours = 0, minutes = 0, seconds = 0;
        if (timeout != null) {
            days = (int) (timeout.longValue() / (24 * 60 * 60));
            hours = (int) (timeout.longValue() / (60 * 60) - days * 24);
            minutes = (int) (timeout.longValue() / (60) - hours * 60 - days * 24 * 60);
            seconds = (int) (timeout.longValue() - minutes * 60 - hours * 60 * 60 - days * 24 * 60 * 60);
        }

        locked = true;
        spinnerDays.setValue(new Integer(days));
        spinnerHours.setValue(new Integer(hours));
        spinnerMinutes.setValue(new Integer(minutes));
        spinnerSeconds.setValue(new Integer(seconds));
        locked = false;

        update(timeout != null);
    }

    public void setTimeout(BigDecimal timeout, boolean notNull) {
        this.notNull = notNull;
        if (notNull) {
            if (timeout == null)
                timeout = new BigDecimal(0.0);
        }
        setTimeout(timeout);
    }

    public BigDecimal getTimeout() {
        BigDecimal timeout = null;
        if (chkTimeout.isSelected()) {
            int days = ((Integer) spinnerDays.getValue()).intValue();
            int hours = ((Integer) spinnerHours.getValue()).intValue();
            int minutes = ((Integer) spinnerMinutes.getValue()).intValue();
            int seconds = ((Integer) spinnerSeconds.getValue()).intValue();
            timeout = BigDecimal.valueOf(days * 24 * 60 * 60 + hours * 60 * 60 + minutes * 60 + seconds);
        }
        return timeout;
    }

    private void update(boolean active) {
        chkTimeout.setSelected(active);
        chkTimeout.setEnabled(!notNull && isEnabled());
        spinnerDays.setEnabled(active && isEnabled());
        spinnerHours.setEnabled(active && isEnabled());
        spinnerMinutes.setEnabled(active && isEnabled());
        spinnerSeconds.setEnabled(active && isEnabled());
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        chkTimeout.setEnabled(enabled);
        spinnerDays.setEnabled(enabled);
        spinnerHours.setEnabled(enabled);
        spinnerMinutes.setEnabled(enabled);
        spinnerSeconds.setEnabled(enabled);
    }

    private final Set<ChangeListener> listeners = new HashSet<ChangeListener>();

    public void addChangeListener(ChangeListener listener) {
        synchronized(listeners) {
            listeners.add(listener);
        }
    }

    public void removeChangeListener(ChangeListener listener) {
        synchronized(listeners) {
            listeners.remove(listener);
        }
    }

    private void fireStateChanged() {
        synchronized(listeners) {
            for (ChangeListener listener : listeners)
                listener.stateChanged(new ChangeEvent(this));
        }
    }
}
