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

package org.radixware.kernel.designer.common.dialogs.sqlscript;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.StringEscapeUtils;
import org.netbeans.api.db.explorer.DatabaseConnection;
import org.radixware.kernel.common.enums.EOptionMode;
import org.radixware.kernel.common.repository.DbConfiguration;
import org.radixware.kernel.common.repository.DbOptionValue;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.sqlscript.parser.SQLScriptValue;
import org.radixware.kernel.common.sqlscript.parser.spi.VariablesProvider;
import org.radixware.kernel.designer.common.dialogs.sqmlnb.DatabaseParameters;


public class DesignerVariablesProvider implements VariablesProvider {

    private static final SQLScriptValue NO_VALUE = new SQLScriptValue();
    private final Map<String, SQLScriptValue> variables = new HashMap<String, SQLScriptValue>() {
        @Override
        public SQLScriptValue put(String key, SQLScriptValue value) {
            return super.put(key == null ? null : key.toUpperCase(), value);
        }
    };
    private final DatabaseParameters connectionParams;
    private final DatabaseConnection connection;

    public DesignerVariablesProvider(DatabaseConnection connection, final DatabaseParameters connectionParams) {
        this.connection = connection;
        this.connectionParams = connectionParams;
        if (connectionParams.isUseParamsFromDb()) {
            try {
                final CountDownLatch readLatch = new CountDownLatch(1);
                connectionParams.readDbOptions(new Runnable() {
                    @Override
                    public void run() {
                        readLatch.countDown();
                    }
                });
                readLatch.await();
            } catch (InterruptedException ex) {
                //ignore
            }
            try {
                final DbConfiguration dbConfig = connectionParams.getDbConfiguration();
                variables.put(Layer.DatabaseOption.TARGET_DB_TYPE, new SQLScriptValue(dbConfig.getTargetDbType()));
                variables.put(Layer.DatabaseOption.TARGET_DB_VERSION, new SQLScriptValue(dbConfig.getTargetDbVersion().toString()));
                for (DbOptionValue dep : dbConfig.getOptions()) {
                    variables.put(dep.getOptionName(), new SQLScriptValue(dep.getMode().getValue()));
                }
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
        if (connectionParams.isUseExplicitType()) {
            variables.put(Layer.DatabaseOption.TARGET_DB_TYPE, new SQLScriptValue(connectionParams.getExplicitType()));
        }
        if (connectionParams.isUseExplicitVersion()) {
            variables.put(Layer.DatabaseOption.TARGET_DB_VERSION, new SQLScriptValue(connectionParams.getExplicitVersion()));
        }
        if (connectionParams.isUseExplicitOptions()) {
            if (connectionParams.getExplicitOptions() != null) {
                for (DbOptionValue dep : connectionParams.getExplicitOptions()) {
                    variables.put(dep.getOptionName(), new SQLScriptValue(dep.getMode().getValue()));
                }
            }
        }
        if (connectionParams.isUseExplicitVariables()) {
            if (connectionParams.getExplicitVariables() != null) {
                for (Map.Entry<String, String> entry : connectionParams.getExplicitVariables().entrySet()) {
                    if (!variables.containsKey(entry.getKey())) {
                        variables.put(entry.getKey(), new SQLScriptValue(entry.getValue()));
                    }
                }
            }
        }
        //put username param
        if (connection != null) {
            variables.put("USER", new SQLScriptValue(connection.getUser()));
        }
    }

    @Override
    public SQLScriptValue getVariable(final String name) {

        if (variables.containsKey(name)) {
            final SQLScriptValue value = variables.get(name);
            return value == NO_VALUE ? null : value;
        }
        final SQLScriptValue value = promptForVariable(name);
        if (value == null) {
            variables.put(name, NO_VALUE);
            return null;
        }
        variables.put(name, value);
        return value;
    }

    @Override
    public void putVariable(final String name, final SQLScriptValue value) {
        variables.put(name, value);
    }

    @Override
    public void removeVariable(final String name) {
        variables.remove(name);
    }

    private SQLScriptValue promptForVariable(final String name) {
        String title = "Enter Parameter Value";

        final JTextField tfValue = new JTextField("");
        final JPanel panel = new JPanel(new MigLayout("fill", "[shrink][grow]", ""));
        panel.add(new JLabel("<html><body>Value of <b>\"" + StringEscapeUtils.escapeHtml(name) + "\"</b>:"), "span, wrap");
        final JRadioButton rbDefine = new JRadioButton("Define:");
        panel.add(rbDefine);
        panel.add(tfValue, "w 340!, growx, wrap");
        final JRadioButton rbUndefined = new JRadioButton("Leave undefined");
        panel.add(rbUndefined, "span, wrap");
        panel.add(new JLabel("Hint: use value " + EOptionMode.ENABLED.getValue() + " to make function isEnabled(...) return true"), "gaptop 16, span");
        final JCheckBox cbRemember = new JCheckBox("Remeber for this connection");
        panel.add(cbRemember, "gaptop 16, span");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbDefine);
        bg.add(rbUndefined);
        rbDefine.setSelected(true);
        rbDefine.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                tfValue.setEnabled(rbDefine.isSelected());
                cbRemember.setEnabled(rbDefine.isSelected());
                if (!rbDefine.isSelected()) {
                    cbRemember.setSelected(false);
                }
            }
        });
        JOptionPane.showMessageDialog(null, panel, title, JOptionPane.QUESTION_MESSAGE);
        if (rbUndefined.isSelected()) {
            return null;
        }
        if (cbRemember.isSelected()) {
            connectionParams.getExplicitVariables().put(name, tfValue.getText());
            if (connectionParams.isUseExplicitVariables()) {
                JOptionPane.showConfirmDialog(null, "Value has been saved, but using of saved variables should be enabled in connection parameters");
            }
            DatabaseParameters.save(connectionParams, connection.getName());
        }
        return new SQLScriptValue(tfValue.getText());
    }
}
