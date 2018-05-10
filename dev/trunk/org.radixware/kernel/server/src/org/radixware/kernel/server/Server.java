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
package org.radixware.kernel.server;

import org.radixware.kernel.server.instance.RadixLoaderActualizer;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.xml.transform.TransformerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.NoOpLog;
import org.apache.xalan.xsltc.trax.TransformerFactoryImpl;
import org.radixware.kernel.server.SrvRunParams.DecryptionException;
import org.radixware.kernel.server.dbq.OraExCodes;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.instance.InstanceListener;
import org.radixware.kernel.server.instance.InstanceState;
import org.radixware.kernel.server.jdbc.RadixDataSource;
import org.radixware.kernel.server.jdbc.RadixDataSourceFactory;
import org.radixware.kernel.server.trace.FileLogOptions;
import org.radixware.kernel.server.utils.RecoveryInstanceFactory;
import org.radixware.kernel.server.utils.RecoveryInstanceFactory.InstanceCreationError;
import org.radixware.kernel.starter.Starter;
import org.radixware.kernel.starter.config.ConfigFileParseException;

public class Server {

    private static final long serialVersionUID = 4521912377175732519L;
    public static final long SLEEP_BETWEEN_START_ATTEMPTS = 5000;
    private static final ServerStartupLog STARTUP_LOG;

    static {
        STARTUP_LOG = new ServerStartupLog("ServerStartupLog");
    }

    /**
     * Use {@code Instance.get()} instead
     *
     * @return
     * @deprecated
     */
    @Deprecated
    public static Instance getInstance() {
        return instance;
    }
    private static volatile Instance instance = null;

    private static boolean startWithGui(final Instance instance) {

        if (GraphicsEnvironment.isHeadless()) {
            STARTUP_LOG.error("GUI initialization failed: GUI is not supported by current environment.");
            STARTUP_LOG.error("You can use \"" + SrvRunParams.SWITCH_GUI_OFF + "\" parameter to disable GUI");
            return false;
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ImageIO.setUseCache(false);
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (Exception ex) {
                    //do nothing
                }
                instance.getView().setVisible(true);
            }
        });
        return true;
    }

    private static void tryToEnableLoggingToFile() {
        try {
            if (SrvRunParams.getInstanceId() != 0) {
                final DbConnectionInfo data = openDbConnection(new NoOpLog());
                try {
                    final FileLogOptions fileLogOpts = Instance.readFileLogOptions(SrvRunParams.getInstanceId(), data.dbConnection);
                    if (fileLogOpts != null) {
                        STARTUP_LOG.enableDuplicationToFile(fileLogOpts);
                    }
                } finally {
                    data.dbConnection.close();
                }
            }
        } catch (Exception ex) {
            //sad, but who cares
        }
    }

    private static boolean startWithoutGui(final Instance instance) {
        boolean isStarted = false;
        tryToEnableLoggingToFile();
        try {
            Exception exceptionImplyingRestart = null;
            do {
                if (exceptionImplyingRestart != null) {
                    try {
                        //already running instance detected, or database is not available
                        //lets sleep some time ant try again
                        final Thread mainThread = Thread.currentThread();
                        final Runnable interruptHook = new Runnable() {
                            @Override
                            public void run() {
                                mainThread.interrupt();
                                try {
                                    mainThread.join(1000);
                                } catch (InterruptedException ex) {
                                    //continue
                                }
                            }
                        };
                        Starter.addAppShutdownHook(interruptHook);
                        Thread.sleep(SLEEP_BETWEEN_START_ATTEMPTS);
                        try {
                            Starter.removeAppShutdownHook(interruptHook);
                        } catch (IllegalStateException ex) {//shutdown is already started
                            STARTUP_LOG.error("Interrupted while sleeping before next attempt to start");
                            return false;
                        }
                    } catch (InterruptedException ex) {
                        STARTUP_LOG.error("Interrupted while sleeping before next attempt to start");
                        return false;
                    }
                    exceptionImplyingRestart = null;
                    STARTUP_LOG.error("Trying to start instance #" + SrvRunParams.getInstanceId());
                }
                try {
                    isStarted = attemptTostartWithoutGui(instance);
                } catch (InstanceIsAlreadyRunningException | DatabaseIsUnavailable ex) {
                    STARTUP_LOG.error("Unable to start server: " + ex.getMessage() + ". Waiting " + (SLEEP_BETWEEN_START_ATTEMPTS / 1000) + " seconds before retry.\n");
                    exceptionImplyingRestart = ex;
                }
            } while (exceptionImplyingRestart != null);
            return isStarted;
        } finally {
            STARTUP_LOG.disableDuplicationToFile();
        }
    }

    private static DbConnectionInfo openDbConnection(final Log log) throws SQLException, DecryptionException {
        if (SrvRunParams.getDbUrl() == null || SrvRunParams.getDbUrl().isEmpty()) {
            log.error(MessageFormat.format("Database URL is not defined.\nUse {0} parameter to define database URL.", SrvRunParams.DB_URL));
            return null;
        }
        final RadixDataSource radixDataSource = RadixDataSourceFactory.getDataSource(URI.create(SrvRunParams.getDbUrl()));
        log.info("DB URL: " + SrvRunParams.getDbUrl());
//        oraDataSource.setURL(SrvRunParams.getDbUrl());
        final String proxyOraUser;
        if (SrvRunParams.getDbSchema() == null || SrvRunParams.getDbSchema().isEmpty()) {
            log.error(MessageFormat.format("DB Schema is not defined.\nUse {0} parameter to define the schema.", SrvRunParams.DB_SCHEMA));
            return null;
        }
        log.info("DB Schema: " + SrvRunParams.getDbSchema());
        if (SrvRunParams.getIsExternalAuthOn()) {
            log.info("Connecting to DB using external authentication");
            radixDataSource.setUser("");
            radixDataSource.setPassword("");
            proxyOraUser = SrvRunParams.getDbSchema();
        } else {
            if (SrvRunParams.getUser() == null || SrvRunParams.getUser().isEmpty()) {
                log.error(MessageFormat.format("DB user is not defined.\nUse {0} parameter to define the user or\nuse using external authentication defining {1} parameter", SrvRunParams.USER, SrvRunParams.EXTERNAL_AUTH));
                return null;
            }
            log.info("Connecting to DB as user " + SrvRunParams.getUser());
            radixDataSource.setUser(SrvRunParams.getUser());
            SrvRunParams.recieveDbPwd(new SrvRunParams.PasswordReciever() {
                @Override
                public void recievePassword(String password) {
                    if (password == null || password.isEmpty()) {
                        log.error(MessageFormat.format("DB password is not defined.\nUse {0} parameter to define the password.", SrvRunParams.DB_PWD));
                    }
                    radixDataSource.setPassword(password);
                }
            });

            proxyOraUser = SrvRunParams.getDbSchema().equals(SrvRunParams.getUser()) ? null : SrvRunParams.getDbSchema();
        }
        final Connection dbConnection = Instance.openNewDbConnection(null, null, null, radixDataSource, proxyOraUser, null);
        try {
            log.info("Connection to DB was established");
            final String[] oraWalletPwd = new String[1];
            SrvRunParams.recieveOraWalletPwd(new SrvRunParams.PasswordReciever() {
                @Override
                public void recievePassword(String password) {
                    oraWalletPwd[0] = password;
                }
            });

            if (oraWalletPwd[0] != null) {
                //at first try predefined password
                try {
                    log.info("Ensuring that Oracle instance wallet is opened");
                    Server.tryOraWalletPwd(dbConnection, oraWalletPwd[0]);
                    oraWalletPwd[0] = null;
                } catch (SQLException ex) {
                    log.error("Error while opening Оracle instance's wallet", ex);
                    throw ex;
                }
            }
            return new DbConnectionInfo(radixDataSource, proxyOraUser, dbConnection);
        } catch (Exception ex) {
            dbConnection.close();
            throw ex;
        }
    }

    private static boolean attemptTostartWithoutGui(final Instance instance) throws InstanceIsAlreadyRunningException, DatabaseIsUnavailable {
        try {
            if (SrvRunParams.getInstanceId() == 0 && SrvRunParams.getNewInstanceSettings() == null) {
                STARTUP_LOG.error(MessageFormat.format("Instancd ID is not defined.\nUse {0} parameter to define instance ID.", SrvRunParams.INSTANCE));
                return false;
            }

            int pid = -1;
            try {
                pid = org.radixware.kernel.starter.utils.SystemTools.getCurrentProcessPid();
            } catch (Exception ex) {
            }
            final String pidPart = pid >= 0 ? " (PID: " + pid + ")" : "";

            STARTUP_LOG.info("Starting RadixWare Server Instance #" + SrvRunParams.getInstanceId() + pidPart);

            final DbConnectionInfo connectionData;
            try {
                connectionData = openDbConnection(STARTUP_LOG);
                if (connectionData == null) {
                    throw new DatabaseIsUnavailable();
                }
            } catch (DecryptionException ex) {
                onDecryptError(ex);
                return false;
            } catch (SQLException ex) {
                throw new DatabaseIsUnavailable(ex);
            }

            boolean closeConnection = true;

            try {

                if (SrvRunParams.getNewInstanceSettings() != null) {
                    STARTUP_LOG.info("Creating a recovery instance...");
                    final SrvRunParams.NewInstanceSettings settings = SrvRunParams.getNewInstanceSettings();
                    try {
                        final int instanceId = RecoveryInstanceFactory.create(connectionData.dbConnection, settings.getInstanceSapAddress(), settings.getEasSapAddress(), settings.getScp());
                        SrvRunParams.setInstanceId(instanceId);
                    } catch (InstanceCreationError ex) {
                        STARTUP_LOG.error("Error while creating the recovery instance", ex);
                        return false;
                    }
                }

                if (!SrvRunParams.getIsDevelopmentMode()) {
                    STARTUP_LOG.info("Checking if instance #" + SrvRunParams.getInstanceId() + " is already running");
                    if (Instance.isRunning(SrvRunParams.getInstanceId(), connectionData.dbConnection)) {
                        throw new InstanceIsAlreadyRunningException();
                    }
                }

                final List<RadixLoaderActualizer.DdsVersionWarning> warnings = RadixLoaderActualizer.checkDbStructCompatibility(connectionData.dbConnection);

                if (warnings != null && !warnings.isEmpty()) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Server is not compatible with database: ");
                    for (RadixLoaderActualizer.DdsVersionWarning warning : warnings) {
                        sb.append(warning.getLayerUri());
                        sb.append(": runtime version - ");
                        sb.append(warning.getServerVer());
                        sb.append(", db compatible versions - ");
                        sb.append(warning.getDbCompatibleVersions());
                        sb.append(";");
                    }
                    STARTUP_LOG.error(sb.toString());
                    if (!SrvRunParams.getIsIgnoreDdsWarnings()) {
                        return false;
                    }
                }

                final AtomicBoolean stopMessageGenerated = new AtomicBoolean(false);

                STARTUP_LOG.info("Starting server instance #" + String.valueOf(SrvRunParams.getInstanceId()));
                final Runnable shutdownHook = new Runnable() {
                    @Override
                    public void run() {
                        STARTUP_LOG.info("Stopping server...");
                        try {
                            try {
                                instance.stop("shutdown signal received");
                            } catch (Exception ex) {
                                STARTUP_LOG.error("Error while stopping server", ex);
                            }
                            try {
                                connectionData.dbConnection.close();
                            } catch (SQLException ex) {
                                //do nothing
                            }
                        } finally {
                            returnFromMain();
                        }
                        if (stopMessageGenerated.compareAndSet(false, true)) {
                            STARTUP_LOG.info("Server stopped");
                        }
                    }
                };
                Starter.addAppShutdownHook(shutdownHook);

                //delete Shutdown hook after stop before restart
                instance.registerListener(new InstanceListener() {
                    @Override
                    public void stateChanged(final Instance inst, final InstanceState oldState, final InstanceState newState) {
                        if (newState == InstanceState.STOPPED) {
                            try {
                                try {
                                    Starter.removeAppShutdownHook(shutdownHook);
                                } catch (IllegalStateException e) {
                                    // shudown is in progress
                                }
                                inst.unregisterListener(this);
                            } finally {
                                Server.returnFromMain();
                            }
                            if (stopMessageGenerated.compareAndSet(false, true)) {
                                STARTUP_LOG.info("Server stopped");
                            }
                        }
                    }
                });
                try {
                    SrvRunParams.reencryptPasswords();
                } catch (ConfigFileParseException | SrvRunParams.DecryptionException ex) {
                    onDecryptError(ex);
                }
                STARTUP_LOG.disableDuplicationToFile();
                instance.start(connectionData.radixDataSource, connectionData.proxyOraUser, connectionData.dbConnection, SrvRunParams.getInstanceId(), null);
                closeConnection = false;
                STARTUP_LOG.info("Startup procedure is successfully initiated, logging is switched to instance log");
            } finally {
                if (closeConnection) {
                    try {
                        connectionData.dbConnection.close();
                    } catch (Exception ex) {
                        //ignore
                    }
                }
            }
        } catch (SQLException ex) {
            STARTUP_LOG.error("Error on the instance start", ex);
            return false;
        }
        return true;
    }

    private static void onDecryptError(final Exception ex) {
        STARTUP_LOG.error("Error while decrypting passwords", ex);
    }

    @SuppressWarnings("SleepWhileInLoop")
    public static void main(final String[] args) {
        if (args.length == 0 && GraphicsEnvironment.isHeadless()) {
            new HelpPrinter(System.out).printUsage();
            return;
        }

        if (args.length == 1 && args[0].toLowerCase().endsWith("help")) {
            new HelpPrinter(System.out).printUsage();
            return;
        }

        if (!SrvRunParams.processArgs(args)) {
            System.out.println("Wrong server arguments");
            new HelpPrinter(System.out).printUsage();
            return;
        }

        //RADIX-4697 FIX
        System.setProperty(TransformerFactory.class.getName(), TransformerFactoryImpl.class.getName());

        instance = new Instance();
        fillGlobalInstancePointer(instance);
        Thread.currentThread().setName("Main server thread");
        boolean isStarted = false;
        if (SrvRunParams.getIsGuiOn()) {
            isStarted = startWithGui(instance);
        } else {
            configureHeadlessEnvironment();
            isStarted = startWithoutGui(instance);
        }
        if (isStarted) {
            try {
                returnFromMainLatch.await();
            } catch (InterruptedException ex) {
                return;
            }
        } else {
            returnFromMainLatch.countDown();
        }
    }

    private static void configureHeadlessEnvironment() {
        final String awtHeadlessPropName = "java.awt.headless";
        if (System.getProperty(awtHeadlessPropName) == null) {//if no explicit value is given, set to true
            System.setProperty(awtHeadlessPropName, "true");
        }
    }

    private static void fillGlobalInstancePointer(Instance instance) {
        //we would like to have public Instance.get() without public Instance.set()
        try {
            final Field f = instance.getClass().getDeclaredField("INSTANCE_GLOBAL_POINTER");
            f.setAccessible(true);
            f.set(null, instance);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to fill global instance pointer", ex);
        }
    }

    public static void awaitStop() throws InterruptedException {
        returnFromMainLatch.await();
    }

    public static void returnFromMain() {
        returnFromMainLatch.countDown();
    }

    public static void tryOraWalletPwd(final Connection db, final String pwd) throws SQLException {
        try (final PreparedStatement altSysSt = db.prepareStatement("ALTER SYSTEM SET WALLET OPEN IDENTIFIED BY \"" + pwd + "\"")) {
            altSysSt.execute();
        } catch (SQLException e) {
            if (e.getErrorCode() != OraExCodes.WALLET_IS_ALREADY_OPEN) {
                throw e;
            }
        }
    }
    private static volatile CountDownLatch returnFromMainLatch = new CountDownLatch(1);

    private static class InstanceIsAlreadyRunningException extends Exception {

        public InstanceIsAlreadyRunningException() {
            super("Instance " + SrvRunParams.getInstanceId() + " is already running");
        }
    }

    private static class DatabaseIsUnavailable extends Exception {

        public DatabaseIsUnavailable() {
            this(null);
        }

        public DatabaseIsUnavailable(Exception reason) {
            super("Couldn't open database connection" + (reason == null ? "" : ": " + reason.getMessage()));
        }
    }

    private static class DbConnectionInfo {

        public final RadixDataSource radixDataSource;
        public final String proxyOraUser;
        public final Connection dbConnection;

        public DbConnectionInfo(final RadixDataSource radixDataSource, final String proxyOraUser, final Connection dbConnection) {
            this.radixDataSource = radixDataSource;
            this.proxyOraUser = proxyOraUser;
            this.dbConnection = dbConnection;
        }
    }
}
