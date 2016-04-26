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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.starter.Starter;
import org.radixware.kernel.starter.StarterArguments;


public class CreateConfigFilePanel extends JPanel {

    private static final String OPTION_WITHOUT_VALUE = new String();//NOPMD;
    private static final String BLOCKERS_COUNT = "count-of-blockers";
    private static final String BUNDLE_NAME = "org.radixware.kernel.server.dialogs.mess.messages";
    private final Map<EOptionInfo, JCheckBox> option2checkBox = new EnumMap<EOptionInfo, JCheckBox>(EOptionInfo.class);
    private final ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);

    public CreateConfigFilePanel() {
        setLayout(new GridBagLayout());
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipadx = 4;
        constraints.gridx = 0;
        final JTextArea previewArea = new JTextArea();
        previewArea.setEditable(false);
        final ActionListener listener = new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final JCheckBox cb = (JCheckBox) e.getSource();
                final EOptionInfo info = (EOptionInfo) cb.getClientProperty(EOptionInfo.class);
                for (EOptionInfo blockedOption : info.getBlockedOptions()) {
                    final JCheckBox blockedCb = option2checkBox.get(blockedOption);
                    if (((EOptionInfo) blockedCb.getClientProperty(EOptionInfo.class)).isAvailable()) {
                        final AtomicInteger blockersCount = (AtomicInteger) blockedCb.getClientProperty(BLOCKERS_COUNT);
                        if (blockersCount.addAndGet(cb.isSelected() ? 1 : -1) == 0) {
                            blockedCb.setEnabled(true);
                        } else {
                            blockedCb.setEnabled(false);
                        }
                    }
                }
                bos.reset();
                try {
                    store(bos);
                } catch (IllegalVaLueException ex) {
                    messageError(ex.getMessage(), CreateConfigFilePanel.this);
                }
                previewArea.setText(bos.toString());
            }
        };
        final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME);
        int count = 0;
        for (EOptionInfo optionInfo : EOptionInfo.values()) {
            final JCheckBox cb = new JCheckBox(bundle.getString(optionInfo.getDescriptionKey()));
            cb.putClientProperty(EOptionInfo.class, optionInfo);
            cb.putClientProperty(BLOCKERS_COUNT, new AtomicInteger());
            add(cb, constraints);
            option2checkBox.put(optionInfo, cb);
            if (optionInfo.isAvailable()) {
                cb.addActionListener(listener);
            } else {
                cb.setEnabled(false);
            }
            count++;
        }
        final JPanel previewPanel = new JPanel();
        previewPanel.setBorder(BorderFactory.createTitledBorder("Preview"));
        previewPanel.setLayout(new BorderLayout());
        previewPanel.add(previewArea, BorderLayout.CENTER);
        previewPanel.setPreferredSize(new Dimension(400, 40));
        constraints.gridheight = count + 1;
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1;
        constraints.weightx = 1;
        add(previewPanel, constraints);
    }

    public Map<String, String> getServerParameters() {
        final Map<String, String> paramsMap = new HashMap<String, String>();
        boolean requireSalt = false;
        for (Map.Entry<EOptionInfo, JCheckBox> entry : option2checkBox.entrySet()) {
            if (entry.getValue().isSelected() && entry.getKey() != EOptionInfo.STARTER_PARAMS) {
                paramsMap.put(entry.getKey().getNameToWrite(), emptyIfNull(entry.getKey().getValueToWrite()));
                if (entry.getKey().isRequireSalt()) {
                    requireSalt = true;
                }
            }
        }
        if (requireSalt) {
            paramsMap.put(SrvRunParams.SALT.substring(1), SrvRunParams.getSaltString());
        }
        return paramsMap;
    }

    private String emptyIfNull(final String target) {
        return target == null ? "" : target;
    }

    private void store(final OutputStream output) throws IllegalVaLueException {
        final PrintWriter printWriter = new PrintWriter(output);
        if (option2checkBox.get(EOptionInfo.STARTER_PARAMS).isSelected()) {
            writeSection(StarterArguments.STARTER_SECTION, Starter.getArguments().getStarterParametersForConfig(), printWriter);
            printWriter.println();
        }
        final Map<String, String> serverParameters = getServerParameters();
        if (!serverParameters.isEmpty()) {
            writeSection(SrvRunParams.SERVER_SECTION, getServerParameters(), printWriter);
        }
        printWriter.flush();
    }

    private void writeSection(final String sectionName, final Map<String, String> parameters, final PrintWriter printWriter) throws IllegalVaLueException {
        printWriter.append("[");
        printWriter.append(sectionName);
        printWriter.append("]");
        printWriter.println();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            printWriter.append(entry.getKey().toString());
            if (entry.getValue() != null && !entry.getValue().toString().isEmpty()) {
                final String value;
                value = wrapValue(entry.getValue().toString());
                printWriter.append(" = ");
                printWriter.append(value);
            }
            printWriter.println();
        }
    }

    public static void showDialog(final Window owner) {
        final JButton cancelButton = new JButton(Messages.BTN_CANCEL);
        final JButton saveButton = new JButton(Messages.BTN_SAVE);
        final CreateConfigFilePanel panel = new CreateConfigFilePanel();
        final JOptionPane pane = new JOptionPane(
                panel,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                null,
                new Object[]{saveButton, cancelButton});
        final JDialog dialog = pane.createDialog(owner, Messages.TITLE_CREATE_CONFIG_FILE);
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                dialog.setVisible(false);
            }
        });
        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                if (fc.showSaveDialog(owner) == JFileChooser.APPROVE_OPTION) {
                    OutputStream os = null;
                    try {
                        try {
                            os = new FileOutputStream(fc.getSelectedFile());
                            panel.store(os);
                        } finally {
                            if (os != null) {
                                os.close();
                            }
                        }
                        String useMessage;
                        if (SrvRunParams.getConfigFile() != null) {
                            useMessage = MessageFormat.format(Messages.MSG_USE_FILE_INSTEAD, SrvRunParams.getConfigFile());
                        } else {
                            useMessage = Messages.MSG_USE_FILE;
                        }
                        if (JOptionPane.showConfirmDialog(owner, useMessage, "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            SrvRunParams.setConfigFile(fc.getSelectedFile().getPath());
                        }
                        dialog.setVisible(false);
                    } catch (Throwable t) {
                        messageError(t.getMessage(), owner);
                    }
                }
            }
        });
        dialog.setResizable(true);
        dialog.setMinimumSize(new Dimension(400, 320));
        dialog.setVisible(true);
    }

    private static String wrapValue(final String value) throws IllegalVaLueException {
        if (value.matches(".*\\s.*")) {
            if (!value.contains("'")) {
                return "'" + value + "'";
            } else if (!value.contains("\"")) {
                return "\"" + value + "\"";
            } else {
                throw new IllegalVaLueException("Value can not contain whitespaces, single quotes and double quotes at the same time: " + value);
            }
        } else {
            return value;
        }
    }

    private static class IllegalVaLueException extends Exception {

        public IllegalVaLueException(String message) {
            super(message);
        }
    }

    private static void messageError(final String error, final Component parent) {
        JOptionPane.showMessageDialog(parent, error, "", JOptionPane.ERROR_MESSAGE);
    }

    private static enum EOptionInfo {

        STARTER_PARAMS("OPT_STARTER_PARAMS", false),
        DB_URL("OPT_DB_URL", false),
        DB_SCHEMA("OPT_DB_SCHEMA", false),
        USER("OPT_USER", false),
        DB_PWD("OPT_DB_PWD", true),
        EXTERNAL_AUTH("OPT_EXTERNAL_AUTH", false),
        KEYSTORE_PWD("OPT_KEYSTORE_PWD", true),
        ORA_WALLET_PWD("OPT_ORA_WALLET_PWD", true),
        INSTANCE("OPT_INSTANCE", false),
        AUTOSTART("OPT_AUTOSTART", false),
        SWITCH_GUI_OFF("OPT_SWITCH_GUI_OFF", false);
        private final String descriptionKey;
        private final boolean requireSalt;

        private EOptionInfo(final String descriptionKey, final boolean requireSalt) {
            this.descriptionKey = descriptionKey;
            this.requireSalt = requireSalt;
        }

        public String getDescriptionKey() {
            return descriptionKey;
        }

        public String getNameToWrite() {
            switch (this) {
                case AUTOSTART:
                    return SrvRunParams.AUTOSTART.substring(1);
                case DB_PWD:
                    return SrvRunParams.ENCRYPTED_DB_PWD.substring(1);
                case DB_SCHEMA:
                    return SrvRunParams.DB_SCHEMA.substring(1);
                case DB_URL:
                    return SrvRunParams.DB_URL.substring(1);
                case EXTERNAL_AUTH:
                    return SrvRunParams.EXTERNAL_AUTH.substring(1);
                case INSTANCE:
                    return SrvRunParams.INSTANCE.substring(1);
                case KEYSTORE_PWD:
                    return SrvRunParams.ENCRYPTED_KS_PWD.substring(1);
                case ORA_WALLET_PWD:
                    return SrvRunParams.ENCRYPTED_ORA_WALLET_PWD.substring(1);
                case SWITCH_GUI_OFF:
                    return SrvRunParams.SWITCH_GUI_OFF.substring(1);
                case USER:
                    return SrvRunParams.USER.substring(1);
                default:
                    throw new RadixError("Can not get option name for " + this);
            }
        }

        public String getValueToWrite() {
            switch (this) {
                case AUTOSTART:
                    return OPTION_WITHOUT_VALUE;
                case DB_PWD:
                    return SrvRunParams.getEncryptedDbPwd();
                case DB_SCHEMA:
                    return SrvRunParams.getDbSchema();
                case DB_URL:
                    return SrvRunParams.getDbUrl();
                case EXTERNAL_AUTH:
                    return OPTION_WITHOUT_VALUE;
                case INSTANCE:
                    return String.valueOf(SrvRunParams.getInstanceId());
                case KEYSTORE_PWD:
                    return SrvRunParams.getEncryptedKsPwd();
                case ORA_WALLET_PWD:
                    return SrvRunParams.getEncryptedOraWalletPwd();
                case SWITCH_GUI_OFF:
                    return OPTION_WITHOUT_VALUE;
                case USER:
                    return SrvRunParams.getUser();
                default:
                    throw new RadixError("Can not get option value for " + this);
            }
        }

        public boolean isAvailable() {
            switch (this) {
                case DB_PWD:
                    return SrvRunParams.getEncryptedDbPwd() != null;
                case KEYSTORE_PWD:
                    return SrvRunParams.getEncryptedKsPwd() != null;
                case ORA_WALLET_PWD:
                    return SrvRunParams.getEncryptedOraWalletPwd() != null;
                default:
                    return true;
            }
        }

        public boolean isRequireSalt() {
            return requireSalt;
        }

        public EOptionInfo[] getBlockedOptions() {
            switch (this) {
                case DB_PWD:
                case USER:
                    return new EOptionInfo[]{EXTERNAL_AUTH};
                case EXTERNAL_AUTH:
                    return new EOptionInfo[]{DB_PWD, USER};
                default:
                    return new EOptionInfo[0];
            }
        }
    }

    private static final class Messages {

        static {
            final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME);
            TITLE_CREATE_CONFIG_FILE = bundle.getString("TITLE_CREATE_CONFIG_FILE");
            BTN_CANCEL = bundle.getString("BTN_CANCEL");
            BTN_SAVE = bundle.getString("BTN_SAVE");
            MSG_USE_FILE = bundle.getString("MSG_USE_FILE");
            MSG_USE_FILE_INSTEAD = bundle.getString("MSG_USE_FILE_INSTEAD");
        }
        private static final String TITLE_CREATE_CONFIG_FILE;
        private static final String BTN_CANCEL;
        private static final String BTN_SAVE;
        private static final String MSG_USE_FILE;
        private static final String MSG_USE_FILE_INSTEAD;
    }
}
