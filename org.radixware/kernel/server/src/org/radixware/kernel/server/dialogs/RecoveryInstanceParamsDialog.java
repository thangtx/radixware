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

package org.radixware.kernel.server.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.radixware.kernel.server.dialogs.AsyncComboBoxModel.Item;
import org.radixware.kernel.server.utils.RecoveryInstanceFactory;
import org.radixware.kernel.server.utils.RecoveryInstanceFactory.InstanceCreationError;


public class RecoveryInstanceParamsDialog extends JPanel {

    private final Login dlg;
    private final Connection connection;

    /** Creates new form RecoveryInstanceParamsDialog */
    public RecoveryInstanceParamsDialog(final Login dlg, final Connection connection) {
        this.dlg = dlg;
        this.connection = connection;
        initComponents();
        ((AsyncComboBoxModel) cbScp.getModel()).load(false);
    }

    /**
     * Return id of newly created instance or -1 if CANCEL was pressed
     * @param dlg - caller dialog
     * @return
     * @throws org.radixware.kernel.server.utils.RecoveryInstanceFactory.InstanceCreationError
     */
    public static int createNewInstance(final Login dlg, final Connection connection) throws InstanceCreationError {
        final RecoveryInstanceParamsDialog paramsPane = new RecoveryInstanceParamsDialog(dlg, connection);
        final JDialog dialog = new JDialog(dlg, Messages.TITLE_CREATE_RECOVERY_INSTANCE);
        dialog.setMinimumSize(new Dimension(400, 180));
        dialog.setModal(true);
        dialog.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
        JOptionPane optionPane = new JOptionPane(paramsPane, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        dialog.setContentPane(optionPane);
        final int[] result = new int[1];
        result[0] = -1;//if recovery instance options dialog will be closed by close button instead of cancel
        final InstanceCreationError[] ice = new InstanceCreationError[1];
        final ActionListener okLisener = new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    final int res = paramsPane.createInstance(dialog);
                    if (res == -1) {//operation cancelled
                        return;
                    }
                    result[0] = res;
                    dialog.dispose();
                } catch (InstanceCreationError ex) {
                    ice[0] = ex;
                }
            }
        };
        final ActionListener cancelListener = new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                result[0] = -1;
                dialog.dispose();
            }
        };
        optionPane.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (!e.getPropertyName().equals(JOptionPane.VALUE_PROPERTY) || e.getNewValue() == null) {
                    return;
                }
                final int t = ((Integer) e.getNewValue()).intValue();
                if (t == JOptionPane.OK_OPTION) {
                    okLisener.actionPerformed(null);
                } else {
                    cancelListener.actionPerformed(null);
                }
            }
        });
        dialog.pack();
        dialog.setLocationRelativeTo(dlg);
        dialog.setVisible(true);
        if (ice[0] != null) {
            throw ice[0];
        }
        return result[0];
    }

    private int createInstance(JDialog owner) throws InstanceCreationError {

        final Callable<Integer> createRunnable = new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                return RecoveryInstanceFactory.create(connection, getInstanceSapAddress(), getEasSapAddress(), getScp());
            }
        };
        try {
            Integer result = WaitDialog.invokeAndShow(createRunnable, 1000, owner);
            if (result == null) {
                return -1;
            }
            return result.intValue();
        } catch (Exception ex) {
            throw new InstanceCreationError(ex);
        }


    }

    private String getInstanceSapAddress() {
        return tfInstanceSap.getText();
    }

    private String getEasSapAddress() {
        return tfEasSap.getText();
    }

    private String getScp() {
        final Integer selectedId = ((AsyncComboBox) cbScp).getSelectedId();
        if (selectedId == null || selectedId == -1) {
            return null;
        } else if (selectedId == 1) {
            return ((Item) cbScp.getSelectedItem()).name;
        }
        throw new IllegalStateException("Unknown item");
    }

    private static final class ScpProvider implements AsyncComboBoxModel.DataProvider {

        private final Connection connection;

        public ScpProvider(final Connection connnection) {
            this.connection = connnection;
        }

        @Override
        public List<Item> getData() throws SQLException {
            final List<AsyncComboBoxModel.Item> data = new ArrayList<Item>();
            final PreparedStatement st = connection.prepareStatement("select name from rdx_scp where systemid = 1 order by name");
            try {
                data.add(new Item(-1, null) {

                    @Override
                    public String toString() {
                        return "<" + Messages.NO_SCP + ">";//TODO: localization
                    }
                });
                final ResultSet rs = st.executeQuery();
                try {
                    while (rs.next()) {
                        data.add(new Item(1, rs.getString(1)) {

                            @Override
                            public String toString() {
                                return name;
                            }
                        });
                    }
                    return data;
                } finally {
                    rs.close();
                }

            } finally {
                st.close();
            }
        }
    }

    static final class Messages {

        static {
            final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.dialogs.mess.messages");

            TITLE_CREATE_RECOVERY_INSTANCE = bundle.getString("TITLE_CREATE_RECOVERY_INSTANCE");
            INSTANCE_SAP_ADDRESS = bundle.getString("INSTANCE_SAP_ADDRESS");
            EAS_SAP_ADDRESS = bundle.getString("EAS_SAP_ADDRESS");
            SCP_NAME = bundle.getString("SCP_NAME");
            NO_SCP = bundle.getString("NO_SCP");

        }
        static final String TITLE_CREATE_RECOVERY_INSTANCE;
        static final String INSTANCE_SAP_ADDRESS;
        static final String EAS_SAP_ADDRESS;
        static final String SCP_NAME;
        static final String NO_SCP;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        tfInstanceSap = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tfEasSap = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cbScp = new AsyncComboBox(dlg, new ScpProvider(connection));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText(Messages.INSTANCE_SAP_ADDRESS);

        tfInstanceSap.setText("localhost:10000");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText(Messages.EAS_SAP_ADDRESS);

        tfEasSap.setText("localhost:10001");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText(Messages.SCP_NAME);

        cbScp.setModel(cbScp.getModel());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfEasSap, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                    .addComponent(tfInstanceSap, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                    .addComponent(cbScp, 0, 424, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfInstanceSap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfEasSap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbScp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cbScp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField tfEasSap;
    private javax.swing.JTextField tfInstanceSap;
    // End of variables declaration//GEN-END:variables
}
