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
 * OracleDbServerTraceConfigDialog.java
 *
 * Created on 19 June 2008, 14:12
 */
package org.radixware.kernel.server.widgets;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.radixware.kernel.server.utils.OracleDbServerTraceConfig;
import javax.swing.event.ListDataListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class OracleDbServerTraceConfigDialog extends javax.swing.JDialog {

    private final OracleDbServerTraceConfig cfg;

    /**
     * Creates new form OracleDbServerTraceConfigDialog
     */
    public OracleDbServerTraceConfigDialog(final java.awt.Frame parent, final OracleDbServerTraceConfig cfg, final boolean modal) {
        super(parent, modal);
        this.cfg = cfg;
        initComponents();
    }

    /**
     * Creates new form OracleDbServerTraceConfigDialog
     */
    public OracleDbServerTraceConfigDialog(final java.awt.Dialog parent, final OracleDbServerTraceConfig cfg, final boolean modal) {
        super(parent, modal);
        this.cfg = cfg;
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        optionPane = new javax.swing.JOptionPane();
        panel = new javax.swing.JPanel();
        lblOSpid = new javax.swing.JLabel();
        edOspid = new javax.swing.JTextField();
        lblDumpDest = new javax.swing.JLabel();
        edDumpDest = new javax.swing.JTextField();
        lblDumpSize = new javax.swing.JLabel();
        edDumpSize = new javax.swing.JTextField();
        optionPane = new javax.swing.JOptionPane();
        btChangeTraceLevel = new JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        final java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/server/widgets/mess/messages"); // NOI18N
        setTitle(bundle.getString("TITLE_SRV_SQL_TRACE_CFG")); // NOI18N

        btChangeTraceLevel.setText(bundle.getString("CHANGE_TRACE_LEVEL"));

        btChangeTraceLevel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JPanel panel = new JPanel(new BorderLayout());
                panel.add(new JLabel(bundle.getString("NEW_TRACE_LEVEL")), BorderLayout.WEST);
                final JComboBox cbLevel = new JComboBox(new TraceLevelsCbxModel());
                panel.add(cbLevel, BorderLayout.CENTER);
                final int result = JOptionPane.showConfirmDialog(OracleDbServerTraceConfigDialog.this, panel, bundle.getString("TRACE_LEVEL"), JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    final TraceLevelsCbxModel.Item item = (TraceLevelsCbxModel.Item) cbLevel.getModel().getSelectedItem();
                    try {
                        if (item == null || item.level == null) {
                            cfg.setSqlTraceOff();
                        } else {
                            cfg.setSqlTraceOn(item.level);
                        }
                    } catch (Exception ex) {
                        messageError("Exception: " + ex.getMessage());
                    }
                }
            }
        });

        optionPane.setBorder(null);
        optionPane.setMessage(panel);
        optionPane.setMessageType(javax.swing.JOptionPane.PLAIN_MESSAGE);
        optionPane.setOptionType(javax.swing.JOptionPane.CLOSED_OPTION);
        optionPane.setOptions(new Object[]{bundle.getString("BTN_CLOSE")});
        optionPane.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                closeDlg(evt);
            }
        });

        panel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblOSpid.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblOSpid.setLabelFor(edOspid);
        lblOSpid.setText(bundle.getString("LBL_SID")); // NOI18N
        lblOSpid.setName("lblSid"); // NOI18N

        edOspid.setEditable(false);
        try {
            edOspid.setText(cfg.getSessionId());
        } catch (SQLException e) {
            edOspid.setText(e.getLocalizedMessage());
            messageError(e.getLocalizedMessage());
        }
        edOspid.setName("edSid"); // NOI18N
        edOspid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edSidActionPerformed(evt);
            }
        });

        lblDumpDest.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblDumpDest.setLabelFor(edDumpDest);
        lblDumpDest.setText(bundle.getString("LBL_USER_DUMP_DEST")); // NOI18N
        lblDumpDest.setName("lblDumpDest"); // NOI18N

        edDumpDest.setEditable(false);
        try {
            edDumpDest.setText(cfg.getTraceFileName());
        } catch (SQLException e) {
            edDumpDest.setText(e.getLocalizedMessage());
            messageError(e.getLocalizedMessage());
        }
        edDumpDest.setName("edDumpDest"); // NOI18N

        lblDumpSize.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblDumpSize.setLabelFor(edDumpSize);
        lblDumpSize.setText(bundle.getString("LBL_MAX_USER_DUMP_SIZE")); // NOI18N

        edDumpSize.setEditable(false);
        try {
            edDumpSize.setText(cfg.getTraceFileSize());
        } catch (SQLException e) {
            edDumpSize.setText(e.getLocalizedMessage());
            messageError(e.getLocalizedMessage());
        }
        edDumpSize.setName("edDumpSize"); // NOI18N

        optionPane.setBorder(null);
        optionPane.setMessage(panel);
        optionPane.setMessageType(javax.swing.JOptionPane.PLAIN_MESSAGE);
        optionPane.setOptionType(javax.swing.JOptionPane.CLOSED_OPTION);
        optionPane.setOptions(new Object[]{bundle.getString("BTN_CLOSE")});
        optionPane.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                closeDlg(evt);
            }
        });

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
                panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(lblDumpSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblDumpDest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblOSpid, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(edDumpDest, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addComponent(edOspid)
                .addComponent(edDumpSize, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap()));
        panelLayout.setVerticalGroup(
                panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblOSpid, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(edOspid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblDumpDest, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(edDumpDest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(edDumpSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblDumpSize, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(47, Short.MAX_VALUE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btChangeTraceLevel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(optionPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btChangeTraceLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(optionPane, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        pack();
    }

    private void edSidActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void closeDlg(java.beans.PropertyChangeEvent evt) {
        if (!evt.getPropertyName().equals(javax.swing.JOptionPane.VALUE_PROPERTY) || evt.getNewValue() == null) {
            return;
        }
        this.dispose();
    }

    private final void messageError(final String mess) {
        javax.swing.JOptionPane.showMessageDialog(this, mess, "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }

    private static final class TraceLevelsCbxModel implements javax.swing.ComboBoxModel {

        protected static final class Item {

            final OracleDbServerTraceConfig.Level level;
            final String title;

            Item(final OracleDbServerTraceConfig.Level level, final String title) {
                this.level = level;
                this.title = title;
            }

            @Override
            public String toString() {
                if (level == null) {
                    return title;
                }
                return String.valueOf(level.intValue()) + " - " + title;
            }
        }
        private Object selected = null;

        TraceLevelsCbxModel() {
        }

        @Override
        public Object getSelectedItem() {
            return selected;
        }

        @Override
        public void setSelectedItem(Object anItem) {
            selected = anItem;
        }

        @Override
        public Object getElementAt(int index) {
            return data[index];
        }

        @Override
        public int getSize() {
            return data.length;
        }
        private static final Item[] data;

        static {
            final java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/server/widgets/mess/messages"); // NOI18N
            data = new Item[]{
                new Item(null, bundle.getString("CBX_ORA_TRACE_NONE")),
                new Item(OracleDbServerTraceConfig.Level.BASIC, bundle.getString("CBX_ORA_TRACE_BASIC")),
                new Item(OracleDbServerTraceConfig.Level.BASIC_AND_BIND_PARAMS, bundle.getString("CBX_ORA_TRACE_BASIC_PARAMS")),
                new Item(OracleDbServerTraceConfig.Level.BASIC_AND_WAIT_EVENTS, bundle.getString("CBX_ORA_TRACE_BASIC_EVENTS")),
                new Item(OracleDbServerTraceConfig.Level.BASIC_AND_BIND_PARAMS_AND_WAIT_EVENTS, bundle.getString("CBX_ORA_TRACE_BASIC_PARAMS_EVENTS")),};
        }

        static final int indexOf(OracleDbServerTraceConfig.Level lvl) {
            for (int i = 0; i < data.length; i++) {
                if (data[i].level == lvl) {
                    return i;
                }

            }
            return -1;
        }

        @Override
        public void addListDataListener(final ListDataListener l) {
        }

        @Override
        public void removeListDataListener(final ListDataListener l) {
        }
    }
    private javax.swing.JTextField edDumpDest;
    private javax.swing.JTextField edDumpSize;
    private javax.swing.JTextField edOspid;
    private javax.swing.JLabel lblDumpDest;
    private javax.swing.JLabel lblDumpSize;
    private javax.swing.JLabel lblOSpid;
    private javax.swing.JOptionPane optionPane;
    private javax.swing.JPanel panel;
    private JButton btChangeTraceLevel;
}
