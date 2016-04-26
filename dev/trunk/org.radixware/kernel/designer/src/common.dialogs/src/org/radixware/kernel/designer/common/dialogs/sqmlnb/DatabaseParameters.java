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

package org.radixware.kernel.designer.common.dialogs.sqmlnb;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.util.NbPreferences;
import org.radixware.kernel.common.enums.EOptionMode;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.DbConfiguration;
import org.radixware.kernel.common.repository.DbOptionValue;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


@SuppressWarnings({"unchecked", "rawtypes"})
public class DatabaseParameters implements Serializable {

    public static final String NULL_CONNECTION_SERIALIZATION_NAME = "none";
    private static final String CONNECTION_NAME_PREFIX = "Connection-";
    private static final long serialVersionUID = 4L;
    private final boolean useParamsFromDb;
    private final boolean useExplicitVersion;
    private final String explicitVersion;
    private final boolean useExplicitType;
    private final String explicitType;
    private final boolean useExplicitOptions;
    private final boolean useExplicitVariables;
    private final ArrayList<DbOptionValue> explicitOptions;
    private final TreeMap<String, String> explicitVariables;
    private transient AtomicBoolean readStarted = new AtomicBoolean();
    private volatile transient DbConfiguration dbConfiguration;
    private final transient Connection connection;
    private volatile transient Exception exOnRead;
    public boolean doNotShowAgain;
    private final transient CountDownLatch readLatch = new CountDownLatch(1);

    public DatabaseParameters(final DatabaseParameters src, final Connection connection) {
        this.useExplicitOptions = src == null ? false : src.isUseExplicitOptions();
        this.useExplicitType = src == null ? false : src.isUseExplicitType();
        this.useExplicitVersion = src == null ? false : src.isUseExplicitVersion();
        this.explicitOptions = src == null ? null : (src.getExplicitOptions() == null ? new ArrayList<DbOptionValue>() : new ArrayList<>(src.getExplicitOptions()));
        this.explicitType = src == null ? null : src.getExplicitType();
        this.explicitVersion = src == null ? null : src.getExplicitVersion();
        this.useParamsFromDb = src == null ? true : src.isUseParamsFromDb();
        this.doNotShowAgain = src == null ? false : src.isDoNotShowAgain();
        this.useExplicitVariables = src == null ? false : src.isUseExplicitVariables();
        this.explicitVariables = src == null || src.getExplicitVariables() == null ? new TreeMap<String, String>() : new TreeMap<>(src.getExplicitVariables());
        this.connection = connection;
    }

    public DatabaseParameters(boolean useParamsFromDb, boolean useExplicitVersion, String explicitVersion, boolean useExplicitType, String explicitType, boolean useExplicitOptions, List<DbOptionValue> explicitOptions, final boolean useExplicitVariables, final Map<String, String> explicitVariables, final Connection connection, boolean doNotShowAgain) {
        this.useParamsFromDb = useParamsFromDb;
        this.useExplicitVersion = useExplicitVersion;
        this.explicitVersion = explicitVersion;
        this.useExplicitType = useExplicitType;
        this.explicitType = explicitType;
        this.useExplicitOptions = useExplicitOptions;
        this.explicitOptions = new ArrayList<>();
        if (explicitOptions != null) {
            this.explicitOptions.addAll(explicitOptions);
        }
        this.connection = connection;
        this.doNotShowAgain = doNotShowAgain;
        this.useExplicitVariables = useExplicitVariables;
        this.explicitVariables = new TreeMap<>();
        if (explicitVariables != null) {
            this.explicitVariables.putAll(explicitVariables);
        }
    }

    public boolean isUseExplicitVariables() {
        return useExplicitVariables;
    }

    public TreeMap<String, String> getExplicitVariables() {
        return explicitVariables;
    }

    public boolean isDoNotShowAgain() {
        return doNotShowAgain;
    }

    public boolean isUseParamsFromDb() {
        return useParamsFromDb;
    }

    public boolean isUseExplicitVersion() {
        return useExplicitVersion;
    }

    public String getExplicitVersion() {
        return explicitVersion;
    }

    public boolean isUseExplicitType() {
        return useExplicitType;
    }

    public String getExplicitType() {
        return explicitType;
    }

    public boolean isUseExplicitOptions() {
        return useExplicitOptions;
    }

    public List<DbOptionValue> getExplicitOptions() {
        return explicitOptions == null ? null : Collections.unmodifiableList(explicitOptions);
    }

    public void readDbOptions(final Runnable onFinish) {
        if (connection == null) {
            if (exOnRead == null) {
                exOnRead = new NullPointerException("Connection is null");
            }
            onFinish.run();
            return;
        }
        if (!readStarted.getAndSet(true)) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        if (connection != null) {
                            dbConfiguration = DbConfiguration.read(connection);
                        }
                    } catch (Exception ex) {
                        exOnRead = ex;
                    } finally {
                        readLatch.countDown();
                        onFinish.run();
                    }
                }
            }.start();
        } else {
            new Thread() {
                @Override
                public void run() {
                    try {
                        readLatch.await();
                    } catch (InterruptedException ex) {
                        //ignore
                    }
                    onFinish.run();
                }
            }.start();
        }
    }

    public DbConfiguration getDbConfiguration() throws Exception {
        if (exOnRead != null) {
            throw exOnRead;
        }
        return dbConfiguration;
    }

    public Connection getConnection() {
        return connection;
    }

    public static DatabaseParameters load(final String connectionName) {
        byte[] bytes = NbPreferences.forModule(DatabaseParameters.class).getByteArray(getKey(connectionName), null);
        if (bytes == null) {
            return null;
        }
        try {
            final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            final Object obj = ois.readObject();
            if (obj instanceof DatabaseParameters) {
                return (DatabaseParameters) obj;
            }
        } catch (Exception ex) {
            //ignore
        }
        return null;
    }

    public static void save(final DatabaseParameters parameters, final String connectionName) {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            final ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(parameters);
            NbPreferences.forModule(DatabaseParameters.class).putByteArray(getKey(connectionName), bos.toByteArray());
        } catch (IOException ex) {
            DialogUtils.messageError(ex);
        }
    }

    private static String getKey(final String connectionName) {
        return CONNECTION_NAME_PREFIX + connectionName;
    }

    public static DatabaseParameters edit(final DatabaseParameters params) {
        final JPanel contentPane = new JPanel(new MigLayout("fill, nogrid")) {
        };
        final JCheckBox cbUseDbParams = new JCheckBox("Use parameters from database", params.isUseParamsFromDb());
        if (params.getConnection() != null) {
            contentPane.add(cbUseDbParams, "wrap");
            contentPane.add(new DbParamsPanel(params), "growx, wrap");
        } else {
            cbUseDbParams.setSelected(false);
        }

        final JCheckBox cbUseExplicitType = new JCheckBox("Define type explicitly", params.isUseExplicitType());
        contentPane.add(cbUseExplicitType, "wrap");
        final JLabel typeLabel = new JLabel("Type:");
        contentPane.add(typeLabel);
        final JComboBox cbType = new JComboBox(new DefaultComboBoxModel<>(collectAvailableTargetDatabases().toArray()));
        cbType.setEditable(true);
        cbType.setSelectedItem(params.getExplicitType());
        contentPane.add(cbType, "growx, wrap");
        final JCheckBox cbUseExplicitVersion = new JCheckBox("Define version explicitly", params.isUseExplicitVersion());
        contentPane.add(cbUseExplicitVersion, "wrap");
        final JLabel verLabel = new JLabel("Version:");
        contentPane.add(verLabel);
        typeLabel.setMinimumSize(new Dimension(verLabel.getPreferredSize().width, typeLabel.getPreferredSize().height));
        final JComboBox cbVersion = new JComboBox(new DefaultComboBoxModel<>(collectAvailableTargetVersions().toArray()));
        cbVersion.setEditable(true);
        cbVersion.setSelectedItem(params.getExplicitVersion());
        contentPane.add(cbVersion, "growx, wrap");
        final JCheckBox cbUseExplicitOptions = new JCheckBox("Explicitly define options", params.isUseExplicitOptions());
        contentPane.add(cbUseExplicitOptions, "wrap");
        final JPanel optPanel = new JPanel(new MigLayout());

        final JToolBar optToolbar = new JToolBar();
        contentPane.add(optToolbar, "growx, wrap");
        contentPane.add(optPanel, "wrap");
        optToolbar.setFloatable(false);

        final JButton addOptButton = new JButton(RadixWareDesignerIcon.CREATE.ADD.getIcon());
        addOptButton.setToolTipText("Add option");
        optToolbar.add(addOptButton);

        final JCheckBox cbUseExplicitVars = new JCheckBox("Explicitly define variables", params.isUseExplicitVariables());
        contentPane.add(cbUseExplicitVars, "wrap");
        final JPanel varPanel = new JPanel(new MigLayout());

        final JToolBar varToolbar = new JToolBar();
        contentPane.add(varToolbar, "growx, wrap");
        contentPane.add(varPanel, "wrap push");
        varToolbar.setFloatable(false);

        final JButton addVarButton = new JButton(RadixWareDesignerIcon.CREATE.ADD.getIcon());
        addVarButton.setToolTipText("Add variable");
        varToolbar.add(addVarButton);

        final List<DbOptionValue> optModel = new ArrayList<>();
        final Map<String, String> varModel = new TreeMap<>();


        final Runnable stateUpdater = new Runnable() {
            @Override
            public void run() {
                cbType.setEnabled(cbUseExplicitType.isSelected());
                cbVersion.setEnabled(cbUseExplicitVersion.isSelected());
                addOptButton.setEnabled(cbUseExplicitOptions.isSelected());
                addVarButton.setEnabled(cbUseExplicitVars.isSelected());
                setEnabledRecursive(optPanel, cbUseExplicitOptions.isSelected());
                setEnabledRecursive(varPanel, cbUseExplicitVars.isSelected());
            }

            private void setEnabledRecursive(final Component component, boolean enabled) {
                component.setEnabled(enabled);
                if (component instanceof JComponent) {
                    for (Component child : ((JComponent) component).getComponents()) {
                        setEnabledRecursive(child, enabled);
                    }
                }
            }
        };

        if (params.getExplicitOptions() != null) {
            for (DbOptionValue dep : params.getExplicitOptions()) {
                optModel.add(dep);
                addOptionEditor(optPanel, dep, optModel, stateUpdater);
            }
        }

        addOptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DbOptionValue dep = createNewOption(optModel);
                if (dep != null) {
                    optModel.add(dep);
                    addOptionEditor(optPanel, dep, optModel, stateUpdater);
                }
            }
        });

        if (params.getExplicitVariables() != null) {
            for (Map.Entry<String, String> entry : params.getExplicitVariables().entrySet()) {
                varModel.put(entry.getKey(), entry.getValue());
                addVarEditor(varPanel, entry.getKey(), entry.getValue(), varModel, stateUpdater);
            }
        }

        addVarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map.Entry<String, String> newEntry = createNewVariable(varModel);
                if (newEntry != null) {
                    varModel.put(newEntry.getKey(), newEntry.getValue());
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            varPanel.removeAll();
                            for (Map.Entry<String, String> entry : varModel.entrySet()) {
                                addVarEditor(varPanel, entry.getKey(), entry.getValue(), varModel, stateUpdater);
                            }
                            repaintContainer(varPanel);
                        }
                    });

                }
            }
        });

        final ActionListener updateListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stateUpdater.run();
            }
        };

        cbUseExplicitOptions.addActionListener(updateListener);
        cbUseExplicitType.addActionListener(updateListener);
        cbUseExplicitVersion.addActionListener(updateListener);
        cbUseExplicitVars.addActionListener(updateListener);

        stateUpdater.run();

        final JCheckBox cbDoNotShowAgain = new JCheckBox("Do not show this dialog for this connection again (You can open it in 'Select Connection' dialog)", params.isDoNotShowAgain());
        if (params.getConnection() != null) {
            contentPane.add(cbDoNotShowAgain, "wrap");
        }

        contentPane.setMinimumSize(new Dimension(300, (int) (contentPane.getMinimumSize().getHeight() * 1.1)));

        final ModalDisplayer md = new ModalDisplayer(contentPane, "Preprocessor Parameters");
        if (md.showModal()) {
            return new DatabaseParameters(cbUseDbParams.isSelected(), cbUseExplicitVersion.isSelected(), cbVersion.getSelectedItem() == null ? null : cbVersion.getSelectedItem().toString(), cbUseExplicitType.isSelected(), cbType.getSelectedItem() == null ? null : cbType.getSelectedItem().toString(), cbUseExplicitOptions.isSelected(), optModel, cbUseExplicitVars.isSelected(), varModel, params.getConnection(), cbDoNotShowAgain.isSelected());
        } else {
            return null;
        }
    }

    private static void addVarEditor(final JPanel container, final String key, final String value, final Map<String, String> model, final Runnable onRemove) {
        final JLabel label = new JLabel(key);
        container.add(label);
        final JTextField tf = new JTextField(value);
        tf.setMinimumSize(new Dimension(300, tf.getMinimumSize().height));
        container.add(tf);
        final JButton removeButton = new JButton(RadixWareDesignerIcon.DELETE.DELETE.getIcon());
        removeButton.setToolTipText("Remove Variable");
        container.add(removeButton, "wrap");

        tf.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onRemove.run();
                model.put(key, tf.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                model.put(key, tf.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.remove(key);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        container.remove(tf);
                        container.remove(label);
                        container.remove(removeButton);
                        repaintContainer(container);
                        if (onRemove != null) {
                            onRemove.run();
                        }
                    }
                });
            }
        });
    }

    private static void repaintContainer(final JComponent container) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                container.invalidate();
                container.getParent().validate();
                container.getParent().repaint();
            }
        });

    }

    private static Map.Entry<String, String> createNewVariable(final Map<String, String> existingVars) {
        final JPanel panel = new JPanel(new MigLayout("fill", "[shrink][grow]", "")) {
        };

        panel.add(new JLabel("Name:"));

        final JTextField tfName = new JTextField();
        panel.add(tfName, "growx, wrap");

        panel.add(new JLabel("Value:"));

        final JTextField tfValue = new JTextField();
        panel.add(tfValue, "growx, wrap");

        final ModalDisplayer md = new ModalDisplayer(panel, "New Variable");
        md.getDialogDescriptor().setValid(false);

        tfName.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                check();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                check();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            private void check() {
                md.getDialogDescriptor().setValid(!existingVars.containsKey(tfName.getText()) && tfName.getText().length() > 0);
            }
        });

        if (md.showModal()) {
            return new AbstractMap.SimpleEntry<>(tfName.getText(), tfValue.getText());
        } else {
            return null;
        }
    }

    private static void addOptionEditor(final JPanel container, final DbOptionValue dep, final List<DbOptionValue> model, final Runnable onRemove) {
        final JLabel label = new JLabel(dep.getOptionName());
        container.add(label);
        final JComboBox cb = new JComboBox(EOptionMode.values());
        cb.setSelectedItem(dep.getMode());
        container.add(cb);
        final JButton removeButton = new JButton(RadixWareDesignerIcon.DELETE.DELETE.getIcon());
        removeButton.setToolTipText("Remove Option");

        container.add(removeButton, "wrap");
        final DbOptionValue[] depHolder = new DbOptionValue[1];
        depHolder[0] = dep;
        cb.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int idx = model.indexOf(depHolder[0]);
                model.set(idx, new DbOptionValue(dep.getOptionName(), (EOptionMode) cb.getSelectedItem()));
                depHolder[0] = model.get(idx);
                if (onRemove != null) {
                    onRemove.run();
                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.remove(model.indexOf(depHolder[0]));
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        container.remove(cb);
                        container.remove(label);
                        container.remove(removeButton);
                        container.getLayout().layoutContainer(container);
                        container.repaint();
                        if (onRemove != null) {
                            onRemove.run();
                        }
                    }
                });
            }
        });
    }

    private static DbOptionValue createNewOption(final List<DbOptionValue> existinOpts) {
        final List<String> variants = new ArrayList<>(collectAvailableOptions(existinOpts));


        final JPanel contentPane = new JPanel(new MigLayout("fill", "[shrink][grow]", "")) {
        };
        contentPane.add(new JLabel("Option:"));

        final JComboBox cbOptName = new JComboBox(variants.toArray());
        cbOptName.setEditable(true);
        contentPane.add(cbOptName, "growx, wrap");
        contentPane.add(new JLabel("Value:"));
        final JComboBox cbOptVal = new JComboBox(EOptionMode.values());
        contentPane.add(cbOptVal, "growx, wrap, pushy");
        final ModalDisplayer md = new ModalDisplayer(contentPane);
        final ItemListener itemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                md.getDialogDescriptor().setValid(cbOptName.getSelectedItem() != null && !cbOptName.getSelectedItem().toString().isEmpty());
            }
        };
        cbOptName.addItemListener(itemListener);
        itemListener.itemStateChanged(null);
        if (md.showModal()) {
            return new DbOptionValue(cbOptName.getSelectedItem().toString(), (EOptionMode) cbOptVal.getSelectedItem());
        } else {
            return null;
        }
    }

    private static List<String> collectAvailableOptions(final List<DbOptionValue> existingOpts) {
        final List<String> result = new ArrayList<>();
        final Project[] projects = OpenProjects.getDefault().getOpenProjects();
        if (projects == null) {
            return result;
        }

        for (Project project : projects) {
            Branch branch = project.getLookup().lookup(Branch.class);
            if (branch != null) {
                for (Layer layer : branch.getLayers()) {
                    for (Layer.TargetDatabase targetDb : layer.getTargetDatabases()) {
                        for (Layer.DatabaseOption opt : targetDb.getOptions()) {
                            result.add(opt.getQualifiedName());
                        }
                    }
                }
            }
        }

        if (existingOpts != null) {
            for (DbOptionValue existingOpt : existingOpts) {
                result.remove(existingOpt.getOptionName());
            }
        }

        return result;
    }

    private static List<String> collectAvailableTargetDatabases() {
        final List<String> result = new ArrayList<>();
        final Project[] projects = OpenProjects.getDefault().getOpenProjects();
        if (projects == null) {
            return result;
        }

        for (Project project : projects) {
            Branch branch = project.getLookup().lookup(Branch.class);
            if (branch != null) {
                for (Layer layer : branch.getLayers()) {
                    for (Layer.TargetDatabase targetDb : layer.getTargetDatabases()) {
                        if (!result.contains(targetDb.getType())) {
                            result.add(targetDb.getType());
                        }
                    }
                }
            }
        }

        return result;
    }

    private static List<String> collectAvailableTargetVersions() {
        final List<String> result = new ArrayList<>();
        final Project[] projects = OpenProjects.getDefault().getOpenProjects();
        if (projects == null) {
            return result;
        }

        for (Project project : projects) {
            Branch branch = project.getLookup().lookup(Branch.class);
            if (branch != null) {
                for (Layer layer : branch.getLayers()) {
                    for (Layer.TargetDatabase targetDb : layer.getTargetDatabases()) {
                        for (BigDecimal ver : targetDb.getSupportedVersions()) {
                            if (!result.contains(ver.toString())) {
                                result.add(ver.toString());
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    private static class DbParamsPanel extends JPanel {

        public DbParamsPanel(final DatabaseParameters parameters) {
            setLayout(new BorderLayout());
            final JList paramsList = new JList(new Object[]{"Loading..."});
            add(paramsList, BorderLayout.CENTER);
            parameters.readDbOptions(new Runnable() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            final DefaultListModel<String> model = new DefaultListModel<String>();
                            try {
                                final DbConfiguration dbConfig = parameters.getDbConfiguration();
                                if (dbConfig != null) {
                                    model.addElement(Layer.DatabaseOption.TARGET_DB_TYPE + " = " + dbConfig.getTargetDbType());
                                    model.addElement(Layer.DatabaseOption.TARGET_DB_VERSION + " = " + dbConfig.getTargetDbVersion());
                                    if (dbConfig.getOptions() != null) {
                                        for (DbOptionValue dep : dbConfig.getOptions()) {
                                            model.addElement(dep.getOptionName() + " = " + dep.getMode());
                                        }
                                    }
                                }
                            } catch (Exception ex) {
                                model.addElement("Error: " + ex.getMessage());
                                paramsList.setEnabled(false);
                            }
                            paramsList.setModel(model);
                        }
                    });
                }
            });
        }
    }

    public static DatabaseParameters get(String nameForSerialization, final Connection connection) {
        nameForSerialization = (nameForSerialization == null || connection == null) ? NULL_CONNECTION_SERIALIZATION_NAME : nameForSerialization;
        final DatabaseParameters connectionParams = new DatabaseParameters(DatabaseParameters.load(nameForSerialization), connection == null ? null : connection);

        final DatabaseParameters result = connection != null && connectionParams.isDoNotShowAgain() ? connectionParams : DatabaseParameters.edit(connectionParams);
        if (result != null) {
            DatabaseParameters.save(result, nameForSerialization);
        }
        if (result != null) {
            final CountDownLatch readLatch = new CountDownLatch(1);
            result.readDbOptions(new Runnable() {
                @Override
                public void run() {
                    readLatch.countDown();
                }
            });
            try {
                readLatch.await();
            } catch (InterruptedException ex) {
                //ignore
            }
        }
        return result;
    }
}
