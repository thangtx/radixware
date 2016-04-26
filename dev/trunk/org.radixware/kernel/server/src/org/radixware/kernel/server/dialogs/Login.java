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
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import oracle.jdbc.pool.OracleDataSource;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EKeyStoreType;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.ssl.KeystoreController;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.RadixLoaderActualizer;
import org.radixware.kernel.server.Server;

import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.SrvRunParams.DecryptionException;
import org.radixware.kernel.server.dbq.OraExCodes;
import org.radixware.kernel.server.dialogs.AsyncComboBoxModel.Item;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.utils.RecoveryInstanceFactory;
import org.radixware.kernel.server.utils.RecoveryInstanceFactory.InstanceCreationError;
import org.radixware.kernel.server.widgets.Label;
import org.radixware.kernel.starter.Starter;
import org.radixware.kernel.starter.config.ConfigFileParseException;

public final class Login extends JDialog {

    public interface Listener {

        void actionPerformed(Event e);

        public void restart();
    }

    public static final class Event extends ActionEvent {

        public Event(final Object source, final OracleDataSource oraDataSource, final String proxyOraUser, final Connection dbConnection, final int instanceId, final String instanceName, final boolean isAutoStart) {
            super(source, ActionEvent.ACTION_PERFORMED, "login");
            this.oraDataSource = oraDataSource;
            this.proxyOraUser = proxyOraUser;
            this.instanceName = instanceName;
            this.instanceId = instanceId;
            this.dbConnection = dbConnection;
            this.isAutoStart = isAutoStart;
        }
        public final boolean isAutoStart;
        public final int instanceId;
        public final String instanceName;
        public final Connection dbConnection;
        public final OracleDataSource oraDataSource;
        public final String proxyOraUser;
        private static final long serialVersionUID = 5539890398628603225L;
    }

    public static enum EDialogMode {

        LAUNCH_INSTANCE,
        OPEN_DB_CONNECTION;
    }
    private final Item CREATE_RECOVERY_INSTANCE_ITEM = new Item(-1, Messages.CBX_CREATE_RECOVERY_INSTANCE) {
        @Override
        public String toString() {
            return name;
        }
    };
    private static final long serialVersionUID = -3645800640629520457L;
    private final JTextField edDbUrl;
    private final JTextField edSchema;
    private final JTextField edUser;
    private final JPasswordField edPwd;
    private final JPasswordField edKeystorePwd;
    private final Listener listener;
    private final AsyncComboBox cmbxInst;
    private final JOptionPane pane;
    private final JComboBox cmbxAuthType;
    private final LocalTracer tracer;
    private volatile Connection dbConnection = null;
    private volatile OracleDataSource oraDataSource = null;
    private volatile String proxyOraUser = null;
    private final EDialogMode dialogMode;
    private final Runnable shutdownHook;

    public Login(final Frame owner, final Listener listener, final LocalTracer tracer, final EDialogMode dialogMode) {
        super(owner);
        shutdownHook = createHook();
        try {
            Starter.addAppShutdownHook(shutdownHook);
        } catch (IllegalStateException ex) {
            shutdownHook.run();
            throw new RuntimeException("Shutdown called before start");
        }
        this.tracer = tracer;
        this.dialogMode = dialogMode;
        setTitle(Messages.TITLE_LOGIN_DIALOG);
        this.listener = listener;
        setModal(true);
        setLocationRelativeTo(null);//center window
        //InstanceList cleaner
        final DocumentListener instanceListCleaner = new DocumentListener() {
            @Override
            public void changedUpdate(final DocumentEvent e) {
                if (dialogMode == EDialogMode.LAUNCH_INSTANCE) {
                    cmbxInst.clear();
                }
            }

            @Override
            public void insertUpdate(final DocumentEvent e) {
                if (dialogMode == EDialogMode.LAUNCH_INSTANCE) {
                    cmbxInst.clear();
                }
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                if (dialogMode == EDialogMode.LAUNCH_INSTANCE) {
                    cmbxInst.clear();
                }
            }
        };

        final JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(7, 1, 0, 3));

        final JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(7, 1, 0, 3));

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 0));
        mainPanel.add(labelPanel, BorderLayout.WEST);
        mainPanel.add(textPanel, BorderLayout.CENTER);

        //edDbUrl
        final JLabel lblDbUrl = new Label(Messages.LBL_DB_URL);
        lblDbUrl.setHorizontalAlignment(SwingConstants.RIGHT);
        edDbUrl = new JTextField();
        edDbUrl.setText(SrvRunParams.getDbUrl());
        lblDbUrl.setLabelFor(edDbUrl);
        edDbUrl.getDocument().addDocumentListener(instanceListCleaner);
        labelPanel.add(lblDbUrl);
        textPanel.add(edDbUrl);

        //edDbScheme
        final JLabel lblScheme = new Label(Messages.LBL_DB_SCHEMA);
        lblScheme.setHorizontalAlignment(SwingConstants.RIGHT);
        edSchema = new JTextField();
        edSchema.setText(SrvRunParams.getDbSchema());
        lblScheme.setLabelFor(edSchema);
        edSchema.getDocument().addDocumentListener(instanceListCleaner);
        labelPanel.add(lblScheme);
        textPanel.add(edSchema);

        //cmbxAuthType
        final JLabel lblAuthType = new Label(Messages.LBL_AUTH_TYPE);
        lblAuthType.setHorizontalAlignment(SwingConstants.RIGHT);
        cmbxAuthType = new JComboBox(new String[]{Messages.CBX_BY_PWD, Messages.CBX_EXTERNAL});
        if (SrvRunParams.getIsExternalAuthOn()) {
            cmbxAuthType.setSelectedIndex(1);
        }
        cmbxAuthType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final boolean byPwd = (cmbxAuthType.getSelectedIndex() == 0);
                edUser.setEnabled(byPwd);
                edPwd.setEnabled(byPwd);
                if (dialogMode == EDialogMode.LAUNCH_INSTANCE) {
                    cmbxInst.clear();
                }
            }
        });
        lblAuthType.setLabelFor(cmbxAuthType);
        labelPanel.add(lblAuthType);
        textPanel.add(cmbxAuthType);

        //edUser
        final JLabel lblUser = new Label(Messages.LBL_USER);
        lblUser.setHorizontalAlignment(SwingConstants.RIGHT);
        edUser = new JTextField();
        edUser.setText(SrvRunParams.getUser());
        lblUser.setLabelFor(edUser);
        edUser.getDocument().addDocumentListener(instanceListCleaner);
        edUser.setEnabled(!SrvRunParams.getIsExternalAuthOn());
        labelPanel.add(lblUser);
        textPanel.add(edUser);

        //edPwd
        final JLabel lblPwd = new Label(Messages.LBL_PWD);
        lblPwd.setHorizontalAlignment(SwingConstants.RIGHT);
        edPwd = new JPasswordField();
        try {
            SrvRunParams.recieveDbPwd(new SrvRunParams.PasswordReciever() {
                @Override
                public void recievePassword(final String password) {
                    edPwd.setText(password);
                }
            });
        } catch (DecryptionException ex) {
            //do nothing
        }

        lblPwd.setLabelFor(edPwd);
        edPwd.getDocument().addDocumentListener(instanceListCleaner);
        edPwd.setEnabled(!SrvRunParams.getIsExternalAuthOn());
        labelPanel.add(lblPwd);
        textPanel.add(edPwd);

        //edKeyStorePwd
        final JLabel lblKeystorePwd = new Label(Messages.LBL_KEYSTORE_PWD);
        lblKeystorePwd.setHorizontalAlignment(SwingConstants.RIGHT);
        edKeystorePwd = new JPasswordField();
        try {
            SrvRunParams.recieveKeystorePwd(new SrvRunParams.PasswordReciever() {
                @Override
                public void recievePassword(final String password) {
                    edKeystorePwd.setText(password);
                }
            });
        } catch (DecryptionException ex) {
            //do nothing
        }
        lblKeystorePwd.setLabelFor(edKeystorePwd);
        labelPanel.add(lblKeystorePwd);
        textPanel.add(edKeystorePwd);

        //cmbxInst
        if (dialogMode == EDialogMode.LAUNCH_INSTANCE) {
            final JLabel lblInst = new Label(Messages.LBL_INSTANCE);
            lblInst.setHorizontalAlignment(SwingConstants.RIGHT);
            final JPanel cbPanel = new JPanel();
            cbPanel.setLayout(new BorderLayout(1, 0));
            cmbxInst = new AsyncComboBox(this, new AsyncComboBoxModel.DataProvider() {
                @Override
                public List<Item> getData() throws SQLException {
                    final Connection db;
                    try {
                        db = openDbConnection();
                    } catch (Exception e) { //RADIX-4851
                        if (isAutoStart()) {
                            tracer.put(EEventSeverity.ERROR, "Unable to open database connection: " + ExceptionTextFormatter.throwableToString(e), null, null, false);
                        } else {
                            messageError(Messages.ERR_ON_DB_CONNECTION, e.getMessage());
                        }
                        return Collections.emptyList();
                    }
                    while (true) {
                        final List<AsyncComboBoxModel.Item> data = new ArrayList<>();
                        final PreparedStatement st = db.prepareStatement(
                                "select i.id, i.title from rdx_instance i order by i.id");
                        try {
                            final ResultSet rs = st.executeQuery();
                            try {
                                while (rs.next()) {
                                    data.add(new Item(Integer.valueOf(rs.getInt(1)), rs.getString(2)));
                                }
                                //                        data.add(CREATE_RECOVERY_INSTANCE_ITEM);
                                return data;
                            } finally {
                                rs.close();
                            }
                        } catch (SQLException e) {
                            if (e.getErrorCode() != OraExCodes.WALLET_IS_NOT_OPENED
                                    || !openOraWallet(db, e.getMessage())) {
                                throw e;
                            }
                        } finally {
                            st.close();
                        }
                    }
                }
            });
            cmbxInst.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        if (e.getItem() == CREATE_RECOVERY_INSTANCE_ITEM) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    login(false);
                                }
                            });
                        }
                    }
                }
            });
            lblInst.setLabelFor(cmbxInst);
            cbPanel.add(cmbxInst, BorderLayout.CENTER);
            final JButton cbButton = new JButton(new ImageIcon(Login.class.getResource("img/reread.png")));
            cbButton.setMaximumSize(new Dimension(25, 25));
            cbButton.setMinimumSize(new Dimension(15, 15));
            cbButton.setPreferredSize(new Dimension(23, 19));
            //cbButton.setSize(new Dimension(15, 15));
            cbButton.addActionListener(
                    new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    cmbxInst.clear();
                    cmbxInst.getModel().load(true);
                    btnWasPressed = true;
                }
            });
            cbPanel.add(cbButton, BorderLayout.EAST);
            labelPanel.add(lblInst);
            textPanel.add(cbPanel);
        } else {
            cmbxInst = null;
        }

        pane = new JOptionPane(mainPanel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        pane.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent e) {
                if (!e.getPropertyName().equals(JOptionPane.VALUE_PROPERTY) || e.getNewValue() == null || e.getNewValue() == JOptionPane.UNINITIALIZED_VALUE) {
                    return;
                }
                btnWasPressed = true;
                final int t = ((Integer) e.getNewValue()).intValue();
                if (t == JOptionPane.OK_OPTION) {
                    login(false);
                } else {
                    dispose();
                }
            }
        });

        setContentPane(pane);

        //try to set default instance
        if (SrvRunParams.getDbSchema() != null
                && SrvRunParams.getDbUrl() != null
                && SrvRunParams.getUser() != null) {

            if (SrvRunParams.getNewInstanceSettings() != null) {
                final SrvRunParams.NewInstanceSettings settings = SrvRunParams.getNewInstanceSettings();
                try {
                    final int instanceId = RecoveryInstanceFactory.create(openDbConnection(), settings.getInstanceSapAddress(), settings.getEasSapAddress(), settings.getScp());
                    SrvRunParams.setInstanceId(instanceId);
                } catch (InstanceCreationError | SQLException ex) {
                    messageError(Messages.ERROR_WHILE_CREATING_RECOVERY_INSTANCE, ex.getMessage());
                }
            }
            if (dialogMode == EDialogMode.LAUNCH_INSTANCE) {
                cmbxInst.getModel().load(false);
                if (SrvRunParams.getInstanceId() != 0) {
                    cmbxInst.setSelectedInstance(SrvRunParams.getInstanceId());
                }
            }
        }

        if (isAutoStart()) {
            if (checkDbConnectionParams(false)) {
                if (SrvRunParams.getInstanceId() > 0) {
                    firstDlgStart = false;
                    SwingUtilities.invokeLater(
                            new Runnable() {
                        @Override
                        public void run() {
                            boolean error = false;
                            try {
                                final Connection db = dbConnection; //RADIX-4196: storing and checking mutable field
                                if (db == null) {
                                    error = true;
                                } else {
                                    final List<RadixLoaderActualizer.DdsVersionWarning> warnings = RadixLoaderActualizer.checkDbStructCompatibility(db);
                                    if (!warnings.isEmpty()) {
                                        tracer.put(EEventSeverity.ERROR, MessageFormat.format(Messages.ERR_UNCOMPATIBLE_DB_ON_AUTOSTART, warnings.toString()), null, null, false);
                                        if (!SrvRunParams.getIsIgnoreDdsWarnings()) {
                                            error = true;
                                        }
                                    }
                                    if (Instance.isRunning(SrvRunParams.getInstanceId(), dbConnection)) {
                                        tracer.put(EEventSeverity.ERROR, Messages.INSTANCE_IS_RUNNING, null, null, false);
                                        if (!SrvRunParams.getIsDevelopmentMode()) {
                                            error = true;
                                        }
                                    }
                                }
                            } catch (SQLException ex) {
                                tracer.put(EEventSeverity.ERROR, MessageFormat.format(Messages.ERR_ON_AUTOSTART, ex.getMessage()), null, null, false);
                                error = true;
                            }
                            if (!error) {
                                login(true);
                            } else {
                                final Runnable hook = createHook();
                                Starter.addAppShutdownHook(hook);
                                Login.this.dispose();
                                new Thread() {
                                    {
                                        setDaemon(true);
                                    }

                                    @Override
                                    public void run() {
                                        try {
                                            tracer.put(EEventSeverity.EVENT, "Wait " + (Server.SLEEP_BETWEEN_START_ATTEMPTS / 1000) + " seconds before restart", null, null, false);
                                            try {
                                                Thread.sleep(Server.SLEEP_BETWEEN_START_ATTEMPTS);
                                            } catch (InterruptedException ex) {
                                                return;
                                            }
                                            if (!btnWasPressed) {
                                                firstDlgStart = true;
                                                listener.restart();
                                            }
                                        } finally {
                                            Starter.removeAppShutdownHook(hook);
                                        }
                                    }
                                }.start();
                            }
                        }
                    });
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            messageError(Messages.TITLE_INSUF_DATA_ERR, Messages.ENTER_INSTANCE);
                        }
                    });
                }
            }
        }

        pack();
        validate();
        setMinimumSize(new Dimension(Math.max(getPreferredSize().width, 600), getPreferredSize().height));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));
    }

    private Runnable createHook() {
        return new Runnable() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        System.err.println(new Date() + ": interrupted before start");
                        Login.this.dispose();
                        if (Instance.get() != null && Instance.get().getView() != null) {
                            Instance.get().getView().dispose();
                        }
                    }
                });
            }
        };
    }

    private void ensureHookRemoved() {
        try {
            Starter.removeAppShutdownHook(shutdownHook);
        } catch (Exception ex) {
            //ingore
        }
    }

    private boolean isAutoStart() {
        return !btnWasPressed && firstDlgStart && SrvRunParams.getIsAutostartOn();
    }

    private boolean tryAutoOpenOraWallet(final Connection db) {
        //at first try predefined password
        final String[] oraWalletPwd = new String[1];
        try {
            SrvRunParams.recieveOraWalletPwd(new SrvRunParams.PasswordReciever() {
                @Override
                public void recievePassword(String password) {
                    oraWalletPwd[0] = password;
                }
            });
        } catch (DecryptionException ex) {
            //continue
        }
        if (oraWalletPwd[0] != null) {

            try {
                Server.tryOraWalletPwd(db, oraWalletPwd[0]);
                return true; //predefined password is OK
            } catch (SQLException e) {
                //continue;
            }
        }
        return false;
    }

    private boolean openOraWallet(final Connection db, final String errMess) throws SQLException {
        if (tryAutoOpenOraWallet(db)) {
            return true;
        }
        final JPasswordField edWalletPwd = new JPasswordField();
        final JPanel panel = new JPanel();
        final JLabel lblInfo = new JLabel(errMess);
        final JLabel lblPwd = new JLabel(Messages.ENTER_ORA_WALLET_PWD);
        lblPwd.setLabelFor(edWalletPwd);
        final LayoutManager layout = new GridLayout(3, 1, 0, 3);
        panel.setLayout(layout);
        panel.add(lblInfo);
        panel.add(lblPwd);
        panel.add(edWalletPwd);
        if (JOptionPane.showOptionDialog(
                this, panel, Messages.ERR_ON_DB_CONNECTION, JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.ERROR_MESSAGE, null, null, null) == JOptionPane.OK_OPTION) {
            Server.tryOraWalletPwd(db, new String(edWalletPwd.getPassword()));
            return true;
        }
        return false;
    }
    private static volatile boolean firstDlgStart = true;
    private static volatile boolean btnWasPressed = false;

    protected void login(final boolean isAutoStart) {
        if (dialogMode == EDialogMode.OPEN_DB_CONNECTION) {
            final Callable<Connection> openConnectionCallable = new Callable<Connection>() {
                @Override
                public Connection call() throws Exception {
                    return openDbConnection();
                }
            };
            try {
                final Connection result = WaitDialog.invokeAndShow(openConnectionCallable, 1000, this);
                if (result == null) {//cancel was pressed
                    return;
                }
            } catch (Exception ex) {
                messageError(Messages.ERR_ON_DB_CONNECTION, ExceptionTextFormatter.exceptionStackToString(ex));
                return;
            }

            final Event event = new Event(Login.this, oraDataSource, proxyOraUser, dbConnection, -1, null, isAutoStart);
            Login.this.listener.actionPerformed(event);
            pane.setValue(JOptionPane.UNINITIALIZED_VALUE);
            return;
        }



        if (cmbxInst.getSelectedId() == null) {
            if (isAutoStart && SrvRunParams.getInstanceId() != 0) {
                messageError(Messages.TITLE_INSUF_DATA_ERR, Messages.INSTANCE_DOES_NOT_EXISTS_OR_IS_RUNNING);
            } else {
                messageError(Messages.TITLE_INSUF_DATA_ERR, Messages.ENTER_INSTANCE);
            }
            cmbxInst.grabFocus();
            pane.setValue(null);
            return;
        }

        if (isAutoStart && btnWasPressed) {
            dispose();
        }

        int instId = cmbxInst.getSelectedId().intValue();
        if (instId == -1) {//create new instance
            try {
                instId = RecoveryInstanceParamsDialog.createNewInstance(this, dbConnection);
            } catch (InstanceCreationError ex) {
                messageError(Messages.ERROR_WHILE_CREATING_RECOVERY_INSTANCE, ex.getMessage());//TODO: exception trace
                cmbxInst.grabFocus();
                pane.setValue(null);
                return;
            }
        }

        if (instId == -1) {//cancel in recovery instance creation dialog
            cmbxInst.grabFocus();
            pane.setValue(null);
            return;
        }
        try {
            if (!SrvRunParams.getIsDevelopmentMode() && Instance.isRunning(instId, dbConnection)) {
                JOptionPane.showMessageDialog(rootPane, Messages.INSTANCE_IS_RUNNING);
                pane.setValue(null);
                return;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, ExceptionTextFormatter.throwableToString(ex));
            pane.setValue(null);
            return;
        }

        SrvRunParams.setInstanceId(instId);
        SrvRunParams.setKeystorePwd(new String(edKeystorePwd.getPassword()));

        //[RADIX-4380] check server's keystore availability
        String errorMessage = null, errorTitle = null;
        try {
            //instance is not started yet, so we read it's settings explictly
            for (int i = 0; i < 1; i++) {
                PreparedStatement qry = null;
                try {
                    qry = dbConnection.prepareStatement("select keystoretype, keystorepath from rdx_instance where id = ?");
                    qry.setInt(1, instId);
                    final ResultSet rs = qry.executeQuery();
                    try {
                        if (rs.next()) {
                            final EKeyStoreType keystoreType = EKeyStoreType.getForValue(rs.getLong("keystoretype"));
                            final String keystorePath = rs.getString("keystorepath");
                            KeystoreController.checkServerKeystoreAvailability(keystoreType, keystorePath);
                        } else {
                            errorMessage = Messages.INSTANCE_SETTINGS_NOT_FOUND;
                            errorTitle = Messages.ERROR_WHILE_READING_INSTANCE_SETTINGS_FROM_DB;
                        }
                    } finally {
                        rs.close();
                    }
                } catch (SQLException e) {
                    if (e.getErrorCode() != OraExCodes.WALLET_IS_NOT_OPENED
                            || !openOraWallet(dbConnection, e.getMessage())) {
                        errorMessage = e.getMessage();
                        errorTitle = Messages.ERROR_WHILE_READING_INSTANCE_SETTINGS_FROM_DB;
                    } else {
                        i--;//try again
                    }
                } finally {
                    try {
                        if (qry != null) {
                            qry.close();
                        }
                    } catch (SQLException e) {
                        //do nothing
                    }
                }
            }
        } catch (SQLException e) {
            errorMessage = e.getMessage();
            errorTitle = Messages.ERROR_WHILE_READING_INSTANCE_SETTINGS_FROM_DB;
        } catch (KeystoreControllerException e) {
            errorMessage = (KeystoreController.isIncorrectPasswordException(e) ? Messages.INCORRECT_KEYSTORE_PASSWORD : e.getMessage());
            errorTitle = Messages.ERROR_WHILE_CHECKING_KEYSTORE;
        }
        //allow user to proceed with instance starting despite inaccessible keystore
        if (errorMessage != null && !SrvRunParams.getIsRestart() && !messageConfirmation(errorTitle, errorMessage + "\n" + Messages.CONFIRM_CONTINUE_INSTANCE_STARTING)) {
            pane.setValue(null);
            return;
        }

        try {
            SrvRunParams.reencryptPasswords();
        } catch (ConfigFileParseException | DecryptionException ex) {
            messageError("Error While Encrypting Passwords", ex.getMessage());
        }

        dispose();

        final Event event = new Event(Login.this, oraDataSource, proxyOraUser, dbConnection, instId, cmbxInst.getInstanceName(), isAutoStart);
        Login.this.listener.actionPerformed(event);
    }

    @Override
    public void dispose() {
        ensureHookRemoved();
        super.dispose(); //To change body of generated methods, choose Tools | Templates.
    }

    protected synchronized Connection openDbConnection() throws SQLException {
        if (dbConnection != null) {
            try {
                dbConnection.close();
            } catch (SQLException e2) {
            }
            dbConnection = null;
        }
        if (oraDataSource != null) {
            try {
                oraDataSource.close();
            } catch (SQLException e2) {
            }
            oraDataSource = null;
        }
        try {
            final String url = edDbUrl.getText();
            final String dbSchema = edSchema.getText();
            final boolean authByPwd = cmbxAuthType.getSelectedItem().equals(Messages.CBX_BY_PWD);
            final String login = authByPwd ? edUser.getText() : "";
            final String password = authByPwd ? new String(edPwd.getPassword()) : "";

            SrvRunParams.setDbSchema(dbSchema);
            oraDataSource = new OracleDataSource();
            oraDataSource.setURL(url);
            SrvRunParams.setDbUrl(url);
            oraDataSource.setUser(login);
            oraDataSource.setPassword(password);
            SrvRunParams.setExternalAuth(!authByPwd);
            if (authByPwd) {
                SrvRunParams.setUser(login);
                SrvRunParams.setDbPwd(password);
            }
            proxyOraUser = dbSchema.equals(login) ? null : dbSchema;
            dbConnection = Instance.openNewDbConnection(null, null, null, oraDataSource, proxyOraUser, null);
            tryAutoOpenOraWallet(dbConnection);
            return dbConnection;
        } catch (SQLException e) {
            try {
                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException e2) {
            }
            dbConnection = null;
            throw e;
        }
    }

    protected final boolean checkDbConnectionParams(boolean showDialogs) {
        final String dbUrl = edDbUrl.getText();
        if (dbUrl == null || dbUrl.length() == 0) {
            if (showDialogs) {
                SwingUtilities.invokeLater(
                        new Runnable() {
                    @Override
                    public void run() {
                        messageError(Messages.TITLE_INSUF_DATA_ERR, Messages.ENTER_DB_URL);
                        edDbUrl.grabFocus();
                    }
                });
            }
            return false;
        }
        final String dbSchema = edSchema.getText();
        if (dbSchema == null || dbSchema.length() == 0) {
            if (showDialogs) {
                SwingUtilities.invokeLater(
                        new Runnable() {
                    @Override
                    public void run() {
                        messageError(Messages.TITLE_INSUF_DATA_ERR, Messages.ENTER_DB_SCHEMA);
                        edSchema.grabFocus();
                    }
                });
            }
            return false;
        }
        final boolean authByPwd = cmbxAuthType.getSelectedItem().equals(Messages.CBX_BY_PWD);
        final String user = edUser.getText();
        if (authByPwd && (user == null || user.length() == 0)) {
            if (showDialogs) {
                SwingUtilities.invokeLater(
                        new Runnable() {
                    @Override
                    public void run() {
                        messageError(Messages.TITLE_INSUF_DATA_ERR, Messages.ENTER_USER);
                        edUser.grabFocus();
                    }
                });
            }
            return false;
        }
        final char[] pwd = edPwd.getPassword();
        if (authByPwd && (pwd == null || pwd.length == 0)) {
            if (showDialogs) {
                SwingUtilities.invokeLater(
                        new Runnable() {
                    @Override
                    public void run() {
                        messageError(Messages.TITLE_INSUF_DATA_ERR, Messages.ENTER_PWD);
                        edPwd.grabFocus();
                    }
                });
            }
            return false;
        }
        return true;
    }

    final void messageError(final String title, final String mess) {
        JOptionPane.showMessageDialog(this, mess, title, JOptionPane.ERROR_MESSAGE);
    }

    final boolean messageConfirmation(final String title, final String mess) {
        return JOptionPane.showConfirmDialog(this, mess, title, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
    }

    static final class Messages {

        static {
            final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.dialogs.mess.messages");

            TITLE_LOGIN_DIALOG = bundle.getString("TITLE_LOGIN_DIALOG");

            LBL_DB_URL = bundle.getString("LBL_DB_URL");
            LBL_DB_SCHEMA = bundle.getString("LBL_DB_SCHEMA");
            LBL_USER = bundle.getString("LBL_USER");
            LBL_PWD = bundle.getString("LBL_PWD");
            LBL_KEYSTORE_PWD = bundle.getString("LBL_KEYSTORE_PWD");
            LBL_INSTANCE = bundle.getString("LBL_INSTANCE");
            LBL_AUTH_TYPE = bundle.getString("LBL_AUTH_TYPE");

            CBX_BY_PWD = bundle.getString("CBX_BY_PWD");
            CBX_EXTERNAL = bundle.getString("CBX_EXTERNAL");

            TITLE_INSUF_DATA_ERR = bundle.getString("TITLE_INSUF_DATA_ERR");
            TITLE_CONNECTION_ERR = bundle.getString("TITLE_CONNECTION_ERR");

            ENTER_DB_URL = bundle.getString("ENTER_DB_URL");
            ENTER_DB_SCHEMA = bundle.getString("ENTER_DB_SCHEMA");
            ENTER_USER = bundle.getString("ENTER_USER");
            ENTER_PWD = bundle.getString("ENTER_PWD");
            ENTER_INSTANCE = bundle.getString("ENTER_INSTANCE");
            ERR_ON_AUTOSTART = bundle.getString("ERR_ON_AUTOSTART");
            ERR_ON_DB_CONNECTION = bundle.getString("ERR_ON_DB_CONNECTION");
            ERR_UNCOMPATIBLE_DB_ON_AUTOSTART = bundle.getString("ERR_UNCOMPATIBLE_DB_ON_AUTOSTART");
            PLEASE_WAIT = bundle.getString("PLEASE_WAIT");
            CBX_CREATE_RECOVERY_INSTANCE = bundle.getString("CBX_CREATE_RECOVERY_INSTANCE");
            ERROR_WHILE_CREATING_RECOVERY_INSTANCE = bundle.getString("ERROR_WHILE_CREATING_RECOVERY_INSTANCE");
            ERROR_WHILE_READING_INSTANCE_SETTINGS_FROM_DB = bundle.getString("ERROR_WHILE_READING_INSTANCE_SETTINGS_FROM_DB");
            INSTANCE_SETTINGS_NOT_FOUND = bundle.getString("INSTANCE_SETTINGS_NOT_FOUND");
            ERROR_WHILE_CHECKING_KEYSTORE = bundle.getString("ERROR_WHILE_CHECKING_KEYSTORE");
            INCORRECT_KEYSTORE_PASSWORD = bundle.getString("INCORRECT_KEYSTORE_PASSWORD");
            CONFIRM_CONTINUE_INSTANCE_STARTING = bundle.getString("CONFIRM_CONTINUE_INSTANCE_STARTING");

            ENTER_ORA_WALLET_PWD = bundle.getString("ENTER_ORA_WALLET_PWD");

            INSTANCE_DOES_NOT_EXISTS_OR_IS_RUNNING = bundle.getString("INSTANCE_DOES_NOT_EXISTS_OR_IS_RUNNING");
            INSTANCE_IS_RUNNING = bundle.getString("INSTANCE_IS_RUNNING");
        }
        static final String INSTANCE_IS_RUNNING;
        static final String PLEASE_WAIT;
        static final String TITLE_LOGIN_DIALOG;
        static final String LBL_DB_URL;
        static final String LBL_DB_SCHEMA;
        static final String LBL_USER;
        static final String LBL_PWD;
        static final String LBL_KEYSTORE_PWD;
        static final String LBL_INSTANCE;
        static final String TITLE_INSUF_DATA_ERR;
        static final String TITLE_CONNECTION_ERR;
        static final String ENTER_DB_URL;
        static final String ENTER_DB_SCHEMA;
        static final String ENTER_USER;
        static final String ENTER_PWD;
        static final String ENTER_INSTANCE;
        static final String INSTANCE_DOES_NOT_EXISTS_OR_IS_RUNNING;
        static final String LBL_AUTH_TYPE;
        static final String CBX_BY_PWD;
        static final String CBX_EXTERNAL;
        static final String CBX_CREATE_RECOVERY_INSTANCE;
        static final String ERR_ON_AUTOSTART;
        static final String ERR_ON_DB_CONNECTION;
        static final String ERR_UNCOMPATIBLE_DB_ON_AUTOSTART;
        static final String ERROR_WHILE_CREATING_RECOVERY_INSTANCE;
        static final String ERROR_WHILE_READING_INSTANCE_SETTINGS_FROM_DB;
        static final String INSTANCE_SETTINGS_NOT_FOUND;
        static final String ERROR_WHILE_CHECKING_KEYSTORE;
        static final String INCORRECT_KEYSTORE_PASSWORD;
        static final String CONFIRM_CONTINUE_INSTANCE_STARTING;
        static final String ENTER_ORA_WALLET_PWD;
    }

    public static void onManualStart() {
        btnWasPressed = true;
    }
}
