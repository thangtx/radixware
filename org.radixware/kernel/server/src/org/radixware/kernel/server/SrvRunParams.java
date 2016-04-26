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

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.common.auth.AuthUtils;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.ssl.KeystoreController;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.server.utils.KernelLayers;
import org.radixware.kernel.starter.config.ConfigEntry;
import org.radixware.kernel.starter.config.ConfigFileAccessor;
import org.radixware.kernel.starter.config.ConfigFileParseException;

public final class SrvRunParams {

    static final String DEV_MODE = "-development";
    static final String USE_LOCAL_JOB_EXECUTOR = "-useLocalJobExecutor";
    public static final String DETAILED_3RD_PARTY_LOGGING = "-detailed3rdPartyLogging";
    static final String SWITCH_EAS_VER_CHECKS_OFF = "-switchEasVerChecksOff";
    public static final String DB_URL = "-dbUrl";
    public static final String DB_SCHEMA = "-dbSchema";
    public static final String EXTERNAL_AUTH = "-externalAuth";
    public static final String USER = "-user";
    public static final String INSTANCE = "-instance";
    static final String RESTART = "-restart";
    static final String CONFIG_FILE = "-configFile";
    public static final String SENS_TRC_FINISH_TIME_DATE_FORMAT = "yyyy-MM-dd-HH:mm";
    public static final String SENS_TRC_FINISH_TIME = "-sensitiveTraceFinishTime";
    public static final String DB_PWD = "-dbPwd";
    static final String OLD_DB_PWD = "-pwd";
    public static final String AUTOSTART = "-autostart";
    static final String JDWP_ADDRESS = "-jdwpAddress"; // ????? ?? -agentlib:jdwp=transport=dt_socket,server=y,address=localhost:8000,suspend=n
    public static final String KEYSTORE_PWD = "-keyStorePwd";
    static final String OLD_KEYSTORE_PWD = "-kspwd";
    public static final String SWITCH_GUI_OFF = "-switchGuiOff";
    static final String NEW_INSTANCE_MARK = "new";
    static final String INSTANCE_SAP_ADDRESS = "instanceSap";
    static final String EAS_SAP_ADDRESS = "easSap";
    static final String SCP_NAME = "scp";
    public static final String ORA_WALLET_PWD = "-oraWalletPwd";
    public static final String ENCRYPTED_DB_PWD = "-encryptedDbPwd";
    public static final String ENCRYPTED_KS_PWD = "-encryptedKsPwd";
    public static final String ENCRYPTED_ORA_WALLET_PWD = "-encryptedOraWalletPwd";
    public static final String SALT = "-salt";
    public static final String ROTATE_FILE_LOGS_DAILY = "-rotateFileLogsDaily";
    public static final String LICENSE_SERVER_ADDRESS = "-licenseServerAddress";
    public static final String LICENSE_TRIAL_FILE_PATH = "-licenseTrialFilePath";
    public static final String MARK_UNCHECKED_STRINGS = "-markUncheckedStrings";
    public static final String UDS_BUILD_PATH = "-udsBuildPath";
    public static final String IGNORE_DDS_WARNINGS = "-ignoreDdsWarnings";
    public static final String TDUMPS_STORE_DAYS = "-tdumpsStoreDays";
    public static final String DUAL_CONTROL_ROLE_ASSIGNMENT = "-dualControlRoleAssignment";
    public static final String DISABLE_ARTE_DB_CONN_CLOSE_ON_CLIENT_DISCONNECT = "-disableArteDbConnCloseOnClientDisconnect";
    //
    public static final String SERVER_SECTION = "Server";
    public static final int SENS_TRACE_TIME_MAX_MILLIS = 8 * 60 * 60 * 1000;//from PA-DSS standarts
    //array of zeroes (guaranteed by language specification)
    private static final byte[] STUB_KEY = new byte[16];
    private static volatile boolean restart = false;
    private static volatile String configFile = null;
    private static volatile int instanceId = 0;
    private static volatile String repositoryDir = "";
    private static volatile String dbUrl = "";
    private static volatile String dbSchema = "";
    private static volatile String user = "";
    private static volatile byte[] salt = null;
    private static volatile String encryptedDbPwd = null;
    private static volatile String encryptedKsPwd = null;
    private static volatile String encryptedOraWalletPwd = null;
    private static volatile boolean autostart = false;
    private static volatile boolean hasGui = true;
    private static volatile boolean isEasVerChecksOn = true;
    private static volatile boolean externalAuth = false;
    private static volatile String jdwpAddress = null;
    private static volatile boolean isDevMode = false;
    private static volatile boolean isDetailed3rdPartyLogging = false;
    private static volatile long localSensitiveTracingFinishMillis = 0;
    private static volatile NewInstanceSettings newInstanceSettings;
    private static volatile boolean isRotateFileLogsDaily = false;
    private static volatile String licenseServerAddress;
    private static volatile String licenseTrialFilePath;
    private static volatile boolean isUseLocalJobExecutor;
    private static volatile boolean ignoreDdsWarnings = false;
    private static volatile int tdumpsStoreDays = -1;
    private static volatile boolean dualControlRoleAssignment = false;
    private static volatile boolean disableArteDbConnCloseOnClientDisconnect = false;

    private SrvRunParams() {
        //singleton
    }
    
    
    public static final boolean getIsDisableArteDbConnCloseOnClientDisconnect() {
        return disableArteDbConnCloseOnClientDisconnect;
    }
    
    public static final void setIsDisableArteDbConnCloseOnClientDisconnect(final boolean value) {
        disableArteDbConnCloseOnClientDisconnect = value;
    }

    public static final boolean getIsIgnoreDdsWarnings() {
        return ignoreDdsWarnings;
    }

    public static final void setIgnoreDdsWarnings(final boolean val) {
        ignoreDdsWarnings = val;
    }
    
    public static boolean getIsDualControlRoleAssignment() {
        return dualControlRoleAssignment;
    }
    
    public static void setDualControlRoleAssignment(final boolean val) {
        dualControlRoleAssignment = val;
    }

    public static void setExternalAuth(final boolean isOn) {
        SrvRunParams.externalAuth = isOn;
    }

    public static void setDbUrl(final String val) {
        dbUrl = val;
    }

    public static void setRepositoryDir(final String val) {
        repositoryDir = val;
    }

    public static void setJdwpAddress(final String val) {
        jdwpAddress = val;
    }

    public static void setDbSchema(final String val) {
        dbSchema = val;
    }

    public static void setUser(final String val) {
        user = val;
    }

    public static void setDbPwd(final String val) {
        encryptedDbPwd = encrypt(val);
    }

    private static void setOraWalletPwd(final String val) {
        encryptedOraWalletPwd = encrypt(val);
    }

    public static void setKeystorePwd(final String val) {
        encryptedKsPwd = encrypt(val);
        KeystoreController.setServerKeystorePassword(val.toCharArray());
    }

    public static void recieveDbPwd(final PasswordReciever reciever) throws DecryptionException {
        reciever.recievePassword(decrypt(encryptedDbPwd));
    }

    public static void recieveOraWalletPwd(final PasswordReciever reciever) throws DecryptionException {
        reciever.recievePassword(decrypt(encryptedOraWalletPwd));
    }

    public static void recieveKeystorePwd(final PasswordReciever reciever) throws DecryptionException {
        reciever.recievePassword(decrypt(encryptedKsPwd));
    }

    public static void setRotateFileLogsDaily(final boolean val) {
        isRotateFileLogsDaily = val;
    }

    public static void setAutostartOn() {
        autostart = true;
    }

    public static boolean isUseLocalJobExecutor() {
        return isUseLocalJobExecutor;
    }

    public static void swithGuiOff() {
        hasGui = false;
    }

    public static void swithEasVerChecksOff() {
        isEasVerChecksOn = false;
    }

    public static void setConfigFile(final String configFile) {
        SrvRunParams.configFile = configFile;
    }

    public static String getConfigFile() {
        return configFile;
    }

    public static void setDevelopmentMode() {
        isDevMode = true;
    }

    public static void setDetailed3rdPartyLoggingEnabled(boolean enabled) {
        isDetailed3rdPartyLogging = enabled;
    }

    public static void setUseLocalJobExecutor(boolean use) {
        isUseLocalJobExecutor = use;
    }

    public static void setInstanceId(final int id) {
        instanceId = id;
    }

    public static String getDbUrl() {
        return dbUrl;
    }

    public static boolean isRotateFileLogsDaily() {
        return isRotateFileLogsDaily;
    }

    public static String getLicenseServerAddress() {
        return licenseServerAddress;
    }

    public static void setLicenseServerAddress(final String address) {
        licenseServerAddress = address;
    }

    public static String getLicenseTrialFilePath() {
        return licenseTrialFilePath;
    }

    public static void setLicenseTrialFilePath(String licenseTrialFilePath) {
        SrvRunParams.licenseTrialFilePath = licenseTrialFilePath;
    }

    public static String getEncryptedDbPwd() {
        return encryptedDbPwd;
    }

    public static String getEncryptedKsPwd() {
        return encryptedKsPwd;
    }

    public static String getEncryptedOraWalletPwd() {
        return encryptedOraWalletPwd;
    }

    public static String getSaltString() {
        return Hex.encode(salt);
    }

    public static String getRepositoryDir() {
        return repositoryDir;
    }

    public static String getDbSchema() {
        return dbSchema;
    }

    public static String getUser() {
        return user;
    }

    public static boolean getIsExternalAuthOn() {
        return externalAuth;
    }

    public static boolean getIsGuiOn() {
        return hasGui;
    }

    public static boolean getIsAutostartOn() {
        return autostart;
    }

    public static boolean getIsDevelopmentMode() {
        return isDevMode;
    }

    public static boolean isDetailed3rdPartyLoggingEnabled() {
        return isDetailed3rdPartyLogging;
    }

    public static boolean getIsUseLocalJobExecutor() {
        return isUseLocalJobExecutor;
    }

    public static boolean getIsEasVerChecksOn() {
        return isEasVerChecksOn;
    }

    public static int getInstanceId() {
        return instanceId;
    }

    public static long getLocalSensitiveTracingFinishMillis() {
        return localSensitiveTracingFinishMillis;
    }

    public static boolean getIsRestart() {
        return restart;
    }

    private static void setLocalSensitiveTracingFinishMillis(final long localSensitiveTracingFinishMillis) {
        SrvRunParams.localSensitiveTracingFinishMillis = localSensitiveTracingFinishMillis;
    }

    public static void setTdumpsStoreDays(final int days) {
        tdumpsStoreDays = days;
    }

    public static int getTdumpsStoreDays() {
        return tdumpsStoreDays;
    }

    public static String[] getRestartParams() {
        final ArrayList<String> params = new ArrayList<String>(20);
        params.add(AUTOSTART);
        if (!getIsGuiOn()) {
            params.add(SWITCH_GUI_OFF);
        }
        if (!getIsEasVerChecksOn()) {
            params.add(SWITCH_EAS_VER_CHECKS_OFF);
        }
        if (getIsDevelopmentMode()) {
            params.add(DEV_MODE);
        }
        params.add(DB_URL);
        params.add(getDbUrl());
        params.add(DB_SCHEMA);
        params.add(getDbSchema());
        if (getIsExternalAuthOn()) {
            params.add(EXTERNAL_AUTH);
        } else {
            params.add(USER);
            params.add(getUser());
            try {
                final String dbPwd = decrypt(encryptedDbPwd);
                params.add(DB_PWD);
                params.add(dbPwd);
            } catch (DecryptionException ex) {
                LogFactory.getLog(SrvRunParams.class).error("Error while decrypting database password", ex);
            }
        }
        params.add(INSTANCE);
        params.add(String.valueOf(getInstanceId()));
        if (getLocalSensitiveTracingFinishMillis() != 0) {
            params.add(SENS_TRC_FINISH_TIME);
            params.add(new SimpleDateFormat(SENS_TRC_FINISH_TIME_DATE_FORMAT, Locale.US).format(new Date(localSensitiveTracingFinishMillis)));
        }
        if (encryptedKsPwd != null && !encryptedKsPwd.isEmpty()) {
            try {
                final String ksPwd = decrypt(encryptedKsPwd);
                params.add(KEYSTORE_PWD);
                params.add(ksPwd);
            } catch (DecryptionException ex) {
                LogFactory.getLog(SrvRunParams.class).error("Error while decrypting keystore password", ex);
            }
        }
        if (encryptedOraWalletPwd != null && !encryptedOraWalletPwd.isEmpty()) {
            try {
                final String oraWalletPwd = decrypt(encryptedOraWalletPwd);
                params.add(ORA_WALLET_PWD);
                params.add(oraWalletPwd);
            } catch (DecryptionException ex) {
                LogFactory.getLog(SrvRunParams.class).error("Error while decrypting Oracle Wallet password", ex);
            }
        }
        if (isRotateFileLogsDaily()) {
            params.add(ROTATE_FILE_LOGS_DAILY);
        }

        if (licenseServerAddress != null && !licenseServerAddress.isEmpty()) {
            params.add(LICENSE_SERVER_ADDRESS);
            params.add(licenseServerAddress);
        }

        if (licenseTrialFilePath != null && !licenseTrialFilePath.isEmpty()) {
            params.add(LICENSE_TRIAL_FILE_PATH);
            params.add(licenseTrialFilePath);
        }

        if (isUseLocalJobExecutor) {
            params.add(USE_LOCAL_JOB_EXECUTOR);
        }

        if (isDetailed3rdPartyLogging) {
            params.add(DETAILED_3RD_PARTY_LOGGING);
        }

        if (ignoreDdsWarnings) {
            params.add(IGNORE_DDS_WARNINGS);
        }

        if (tdumpsStoreDays != -1) {
            params.add(TDUMPS_STORE_DAYS);
            params.add(String.valueOf(tdumpsStoreDays));
        }
        
        if (dualControlRoleAssignment) {
            params.add(DUAL_CONTROL_ROLE_ASSIGNMENT);
        }

        params.add(RESTART);
        if (configFile != null) {
            params.add(CONFIG_FILE);
            params.add(configFile);
        }
        return params.toArray(new String[]{});
    }

    public static NewInstanceSettings getNewInstanceSettings() {
        return newInstanceSettings;
    }

    public static void setNewInstanceSettings(final NewInstanceSettings newInstanceSettings) {
        SrvRunParams.newInstanceSettings = newInstanceSettings;
    }

    public static boolean processArgs(final String[] args) {
        if (!checkOnlyFileOrRestart(args)) {
            LogFactory.getLog(SrvRunParams.class).error("Config file and other parameters can not be used together");
            return false;
        }
        List<String> argsForProcessing = null;
        if (configFile != null && !restart) {
            try {
                argsForProcessing = readArgsFromFile(configFile);
            } catch (UnableToLoadOptionsFromFile ex) {
                final ByteArrayOutputStream bos = new ByteArrayOutputStream(4000);
                ex.printStackTrace(new PrintWriter(bos));
                LogFactory.getLog(SrvRunParams.class).error("Error while reading Server parameters:\n" + bos.toString());
                return false;
            }
        } else {
            argsForProcessing = new ArrayList<String>();
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals(CONFIG_FILE)) {
                    i++;//skip config file name
                    continue;
                }
                if (args[i].equals(RESTART)) {
                    continue;
                }
                argsForProcessing.add(args[i]);
            }
        }
        try {
            processArgsImpl(argsForProcessing.toArray(new String[argsForProcessing.size()]), null);
        } catch (ParamException ex) {
            LogFactory.getLog(SrvRunParams.class).error(ex.getMessage());
            return false;
        } catch (DecryptionException ex) {
            LogFactory.getLog(SrvRunParams.class).error("Error while decrypting passwords: " + ex.getMessage());
            return getIsGuiOn();//user can enter passwords from gui lately
        }
        return true;
    }

    private static int parseInstanceSettings(final String[] args, int i) throws EmptyParamValueException, WrongParamValueException {
        try {
            SrvRunParams.setInstanceId(Integer.parseInt(args[++i]));
        } catch (NullPointerException e) {
            throw new EmptyParamValueException(SrvRunParams.INSTANCE);
        } catch (NumberFormatException e) {
            //try to parse new recovery instance parameters
            //expected format: new:instanceSap=1.2.3.4:5,easSap=1.2.3.4:6,scp=SCP_NAME
            final String arg = args[i];//cut off whitespaces XXX: whitespaces in SCP name?
            final Exception reason = new Exception("-instance must be an integer or a string with format \"new: instanceSap=1.2.3.4:5, easSap=1.2.3.4:6, scp=SCP_NAME\"", e);
            final String newAndParams[] = arg.split(":", 2);
            if (newAndParams.length != 2) {
                throw new WrongParamValueException(SrvRunParams.INSTANCE, reason);
            }
            if (!"new".equals(newAndParams[0].trim())) {
                throw new WrongParamValueException(SrvRunParams.INSTANCE, reason);
            }
            final String[] namesAndValues = newAndParams[1].split(",");
            final NewInstanceSettingsImpl settings = new NewInstanceSettingsImpl();
            for (int j = 0; j < namesAndValues.length; j++) {
                final String[] nameAndValue = namesAndValues[j].split("=");
                if (nameAndValue.length != 2) {
                    throw new WrongParamValueException(SrvRunParams.INSTANCE, reason);
                }
                if (INSTANCE_SAP_ADDRESS.equals(nameAndValue[0].trim())) {
                    if (settings.getInstanceSapAddress() != null) {
                        throw new WrongParamValueException(SrvRunParams.INSTANCE, reason);
                    } else {
                        settings.setInstanceSapAddress(nameAndValue[1].trim());
                    }
                }
                if (EAS_SAP_ADDRESS.equals(nameAndValue[0].trim())) {
                    if (settings.getEasSapAddress() != null) {
                        throw new WrongParamValueException(SrvRunParams.INSTANCE, reason);
                    } else {
                        settings.setEasSapAddress(nameAndValue[1].trim());
                    }
                }

                if (SCP_NAME.equals(nameAndValue[0].trim())) {
                    if (settings.getScp() != null) {
                        throw new WrongParamValueException(SrvRunParams.INSTANCE, reason);
                    } else {
                        settings.setScp(nameAndValue[1].trim());
                    }
                }
            }
            if (settings.getEasSapAddress() == null || settings.getInstanceSapAddress() == null) {
                throw new WrongParamValueException(SrvRunParams.INSTANCE, reason);
            }
            setNewInstanceSettings(settings);
        }
        return i;
    }

    private static List<String> readArgsFromFile(final String fileName) throws UnableToLoadOptionsFromFile {
        return readArgsFromFileImpl(fileName, null);
    }

    /**
     * Load options from file. The file must be a regular java .properties file
     *
     * @param fileName - properties file
     * @param optionsToRead - keys that should be read. null value permits all
     * keys.
     * @return list of strings that represents arguments array
     */
    private static List<String> readArgsFromFileImpl(final String fileName, final Collection<String> optionsToRead) throws UnableToLoadOptionsFromFile {
        try {
            final ConfigFileAccessor configAccessor = ConfigFileAccessor.get(fileName, SERVER_SECTION);
            if (configAccessor == null) {
                throw new UnableToLoadOptionsFromFile(SERVER_SECTION + " section was not found in configuration file");
            }
            final List<String> args = new ArrayList<String>();
            for (final ConfigEntry entry : configAccessor.getEntries()) {
                final String nameWithMinus = entry.getKey().startsWith("-") ? entry.getKey() : "-".concat(entry.getKey());
                if (optionsToRead != null && !optionsToRead.contains(nameWithMinus)) {
                    continue;
                }
                args.add(nameWithMinus);
                final String propVal = entry.getValue();
                if (propVal != null && !propVal.isEmpty()) {
                    args.add(propVal);
                }
            }
            return args;
        } catch (ConfigFileParseException ex) {
            throw new UnableToLoadOptionsFromFile(ex);
        }
    }

    public static List<OptionProcessingResult> rereadFromFile(final List<String> options, final LocalTracer tracer) throws ConfigFileNotSpecifiedException, UnableToLoadOptionsFromFile, DecryptionException {
        if (configFile == null) {
            throw new ConfigFileNotSpecifiedException("Configuration file was not specified at startup");
        }
        final List<String> loadedOptions = new ArrayList<>();
        final List<OptionProcessingResult> results = new ArrayList<>();
        for (String option : options) {//read every option individually. This approach has a greater cost, but simplifies the code.
            loadedOptions.clear();
            final List<String> args = readArgsFromFileImpl(configFile, Collections.singleton(option));
            if (args.isEmpty()) {
                results.add(new OptionProcessingResult(option, false, OptionProcessingResult.NO_SUCH_OPTION));
                continue;
            }
            try {
                processArgsImpl(args.toArray(new String[args.size()]), tracer);
                results.add(new OptionProcessingResult(option, true, ""));
            } catch (ParamException ex) {
                results.add(new OptionProcessingResult(option, false, ex.getMessage()));
            }
        }
        boolean shouldPerformReencrypt = false;
        for (OptionProcessingResult optionResult : results) {
            if ((DB_PWD.equals(optionResult.getOptionName()) || OLD_DB_PWD.equals(optionResult.getOptionName())) && optionResult.isSucceed()) {
                shouldPerformReencrypt = true;
            }
        }
        if (shouldPerformReencrypt) {
            try {
                reencryptPasswords();
            } catch (ConfigFileParseException ex) {
                throw new UnableToLoadOptionsFromFile(ex);
            }
        }
        return results;
    }

    public static void reencryptPasswords() throws ConfigFileParseException, DecryptionException {
        if (configFile == null || configFile.isEmpty()) {
            return;
        }
        final ConfigFileAccessor configAccessor = ConfigFileAccessor.get(configFile, SERVER_SECTION);
        if (configAccessor == null) {
            throw new ConfigFileParseException(SERVER_SECTION + " section was not found in configuration file");
        }
        final List<ConfigEntry> entries = configAccessor.getEntries();
        final List<String> needToClear = Arrays.asList(new String[]{
            DB_PWD,
            KEYSTORE_PWD,
            ORA_WALLET_PWD,
            OLD_DB_PWD,
            OLD_KEYSTORE_PWD,
            ENCRYPTED_DB_PWD,
            ENCRYPTED_KS_PWD,
            ENCRYPTED_ORA_WALLET_PWD,
            SALT});
        final List<String> toRemove = new ArrayList<String>();
        for (final ConfigEntry existingEntry : entries) {
            final String nameWithMinus = existingEntry.getKey().startsWith("-") ? existingEntry.getKey() : "-".concat(existingEntry.getKey());
            if (needToClear.contains(nameWithMinus)) {
                toRemove.add(existingEntry.getKey());
            }
        }
        final List<String> wasInFile = new ArrayList<String>();
        for (final String removedProperty : toRemove) {
            wasInFile.add(removedProperty.startsWith("-") ? removedProperty : "-".concat(removedProperty));
        }
        final List<ConfigEntry> toWrite = new ArrayList<ConfigEntry>();
        final String dbPwd = decrypt(encryptedDbPwd);
        final String ksPwd = decrypt(encryptedKsPwd);
        final String oraWalletPwd = decrypt(encryptedOraWalletPwd);
        salt = generateSalt();
        encryptedDbPwd = encrypt(dbPwd);
        encryptedKsPwd = encrypt(ksPwd);
        encryptedOraWalletPwd = encrypt(oraWalletPwd);
        if (dbPwd != null && (wasInFile.contains(DB_PWD) || wasInFile.contains(ENCRYPTED_DB_PWD) || wasInFile.contains(OLD_DB_PWD))) {
            toWrite.add(new ConfigEntry(ENCRYPTED_DB_PWD.substring(1), encryptedDbPwd));
        }

        if (ksPwd != null && (wasInFile.contains(KEYSTORE_PWD) || wasInFile.contains(ENCRYPTED_KS_PWD) || wasInFile.contains(OLD_KEYSTORE_PWD))) {
            toWrite.add(new ConfigEntry(ENCRYPTED_KS_PWD.substring(1), encryptedKsPwd));
        }

        if (oraWalletPwd != null && (wasInFile.contains(ORA_WALLET_PWD) || wasInFile.contains(ENCRYPTED_ORA_WALLET_PWD))) {
            toWrite.add(new ConfigEntry(ENCRYPTED_ORA_WALLET_PWD.substring(1), encryptedOraWalletPwd));
        }
        toWrite.add(new ConfigEntry(SALT.substring(1), getSaltString()));
        configAccessor.update(toRemove, toWrite);
    }

    private static boolean checkOnlyFileOrRestart(final String[] args) {
        boolean isConfigFile = false;
        boolean otherParams = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(CONFIG_FILE)) {
                isConfigFile = true;
                configFile = args[++i];
            } else if (args[i].equals(RESTART)) {
                restart = true;
            } else {
                otherParams = true;
            }
        }
        if (isConfigFile && otherParams && (!restart)) {
            return false;
        }
        return true;
    }

    private static void processArgsImpl(final String[] args, final LocalTracer tracer) throws ParamException, DecryptionException {
        final int len = args.length;
        String dbPwd = null, encDbPwd = null;
        String ksPwd = null, encKsPwd = null;
        String oraWalletPwd = null, encOraWalletPwd = null;
        for (int i = 0; i < len; i++) {
            try {
                if (args[i].equals(SrvRunParams.DB_URL)) {
                    SrvRunParams.setDbUrl(args[++i]);
                } else if (args[i].equals(SrvRunParams.DB_SCHEMA)) {
                    SrvRunParams.setDbSchema(args[++i]);
                } else if (args[i].equals(SrvRunParams.USER)) {
                    SrvRunParams.setUser(args[++i]);
                } else if (args[i].equals(SrvRunParams.DB_PWD) || args[i].equals(SrvRunParams.OLD_DB_PWD)) {
                    dbPwd = args[++i];
                } else if (args[i].equals(SrvRunParams.ORA_WALLET_PWD)) {
                    oraWalletPwd = args[++i];
                } else if (args[i].equals(SrvRunParams.INSTANCE)) {
                    i = parseInstanceSettings(args, i);
                } else if (args[i].equals(SrvRunParams.JDWP_ADDRESS)) {
                    SrvRunParams.setJdwpAddress(args[++i]);
                } else if (args[i].equals(SrvRunParams.AUTOSTART)) {
                    SrvRunParams.setAutostartOn();
                } else if (args[i].equals(SrvRunParams.SWITCH_GUI_OFF)) {
                    SrvRunParams.swithGuiOff();
                } else if (args[i].equals(SrvRunParams.SWITCH_EAS_VER_CHECKS_OFF)) {
                    SrvRunParams.swithEasVerChecksOff();
                } else if (args[i].equals(SrvRunParams.EXTERNAL_AUTH)) {
                    SrvRunParams.setExternalAuth(true);
                } else if (args[i].equals(SrvRunParams.DEV_MODE)) {
                    SrvRunParams.setDevelopmentMode();
                } else if (args[i].equals(SrvRunParams.DETAILED_3RD_PARTY_LOGGING)) {
                    SrvRunParams.setDetailed3rdPartyLoggingEnabled(true);
                } else if (args[i].equals(SrvRunParams.DUAL_CONTROL_ROLE_ASSIGNMENT)) {
                    SrvRunParams.setDualControlRoleAssignment(true);
                }  else if (args[i].equals(SrvRunParams.USE_LOCAL_JOB_EXECUTOR)) {
                    SrvRunParams.setUseLocalJobExecutor(true);
                } else if (args[i].equals(SrvRunParams.ROTATE_FILE_LOGS_DAILY)) {
                    SrvRunParams.setRotateFileLogsDaily(true);
                } else if (args[i].equals(SrvRunParams.LICENSE_SERVER_ADDRESS)) {
                    SrvRunParams.setLicenseServerAddress(args[++i]);
                } else if (args[i].equals(SrvRunParams.LICENSE_TRIAL_FILE_PATH)) {
                    SrvRunParams.setLicenseTrialFilePath(args[++i]);
                } else if (args[i].equals(SrvRunParams.KEYSTORE_PWD) || args[i].equals(SrvRunParams.OLD_KEYSTORE_PWD)) {
                    ksPwd = args[++i];
                } else if (args[i].equals(SrvRunParams.ENCRYPTED_DB_PWD)) {
                    encDbPwd = args[++i];
                } else if (args[i].equals(ENCRYPTED_KS_PWD)) {
                    encKsPwd = args[++i];
                } else if (args[i].equals(ENCRYPTED_ORA_WALLET_PWD)) {
                    encOraWalletPwd = args[++i];
                } else if (args[i].equals(SALT)) {
                    salt = Hex.decode(args[++i]);
                } else if (args[i].equals(SrvRunParams.SENS_TRC_FINISH_TIME)) {
                    try {
                        try {
                            final Date finishDate = new SimpleDateFormat(SENS_TRC_FINISH_TIME_DATE_FORMAT, Locale.US).parse(args[++i]);
                            setSensitiveTraceFinishMillis(finishDate.getTime(), tracer);
                        } catch (ParseException ex) {
                            throw new WrongParamValueException(SrvRunParams.SENS_TRC_FINISH_TIME, ex);
                        }
                    } catch (NullPointerException e) {
                        throw new EmptyParamValueException(SrvRunParams.SENS_TRC_FINISH_TIME);
                    }
                } else if (args[i].equals(SrvRunParams.MARK_UNCHECKED_STRINGS)) {
                    System.setProperty("org.radixware.kernel.client.markUncheckedStrings", "true");
                } else if (args[i].equals(SrvRunParams.UDS_BUILD_PATH)) {
                    System.setProperty("org.radixware.kernel.uds.buildPath", args[++i]);
                } else if (args[i].equals(SrvRunParams.IGNORE_DDS_WARNINGS)) {
                    setIgnoreDdsWarnings(true);
                } else if (args[i].equals(SrvRunParams.TDUMPS_STORE_DAYS)) {
                    try {
                    setTdumpsStoreDays(Integer.parseInt(args[++i]));
                    } catch (NumberFormatException ex) {
                        throw new ParamValueException(TDUMPS_STORE_DAYS + " should be an integer");
                    }
                } else if (args[i].equals(SrvRunParams.DISABLE_ARTE_DB_CONN_CLOSE_ON_CLIENT_DISCONNECT)) {
                    setIsDisableArteDbConnCloseOnClientDisconnect(true);
                }
                else {
                    throw new UnsupportedParamException(args[i]);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new EmptyParamValueException(args[i - 1]);
            }
        }

        if (salt == null) {
            if (encKsPwd != null) {
                throw new ParamException("Keystore password is encrypted, but salt is not defined");
            }
            if (encDbPwd != null) {
                throw new ParamException("Database password is encrypted, but salt is not defined");
            }
            if (encOraWalletPwd != null) {
                throw new ParamException("Oracle wallet password is encrypted, but salt is not defined");
            }
            salt = generateSalt();

        }
        if (dbPwd != null) {
            setDbPwd(dbPwd);
            encDbPwd = null;
        }
        if (ksPwd != null) {
            setKeystorePwd(ksPwd);
            encKsPwd = null;
        }
        if (oraWalletPwd != null) {
            setOraWalletPwd(oraWalletPwd);
            encOraWalletPwd = null;
        }

        decryptPasswords(encDbPwd, encKsPwd, encOraWalletPwd);

    }

    private static void decryptPasswords(final String encDbPwd, final String encKsPwd, final String encOraWalletPwd) throws DecryptionException {
        List<String> failed = new ArrayList<String>(3);
        if (encDbPwd != null) {
            try {
                setDbPwd(decrypt(encDbPwd));
            } catch (DecryptionException ex) {
                failed.add("database");
            }
        }
        if (encKsPwd != null) {
            try {
                setKeystorePwd(decrypt(encKsPwd));
            } catch (DecryptionException ex) {
                failed.add("keystore");
            }
        }
        if (encOraWalletPwd != null) {
            try {
                setOraWalletPwd(decrypt(encOraWalletPwd));
            } catch (DecryptionException ex) {
                failed.add("oracle wallet");
            }
        }

        if (failed.isEmpty()) {
            return;
        }

        StringBuilder sb = new StringBuilder("Unable to decrypt passwords for");
        for (int i = 0; i < failed.size(); i++) {
            if (i > 0) {
                if (i == failed.size() - 1) {
                    sb.append(" and ");
                } else {
                    sb.append(", ");
                }
            } else {
                sb.append(" ");
            }

            sb.append(failed.get(i));
        }

        throw new DecryptionException(sb.toString());

    }

    public static void setSensitiveTraceFinishMillis(final long finishMillis, final LocalTracer tracer) {
        final long curTimeMillis = System.currentTimeMillis();
        long totalTraceTime = finishMillis - curTimeMillis;
        if (totalTraceTime > SENS_TRACE_TIME_MAX_MILLIS) {
            if (tracer != null) {
                tracer.put(EEventSeverity.WARNING, "Attempt to enable sensitive data tracing for period longer than 8 hours. Tracing will be enabled only for 8 hours.", null, null, false);
            }
            totalTraceTime = SENS_TRACE_TIME_MAX_MILLIS;
        }
        setLocalSensitiveTracingFinishMillis(curTimeMillis + totalTraceTime);
    }

    private static String decrypt(final String encryptedString) throws DecryptionException {
        if (encryptedString == null) {
            return null;
        }
        if (encryptedString.isEmpty()) {
            return encryptedString;
        }
        final byte[] decryptedBytes = AuthUtils.decrypt(Hex.decode(encryptedString), getEncryptionKey());
        if (decryptedBytes[0] <= 0 || decryptedBytes[0] > 8 || decryptedBytes.length - decryptedBytes[0] < 0) {
            throw new DecryptionException("Encrypted data is corrupted");
        }
        return new String(decryptedBytes, decryptedBytes[0], decryptedBytes.length - decryptedBytes[0]);
    }

    private static String encrypt(final String clearString) {
        if (clearString == null) {
            return null;
        }
        if (clearString.isEmpty()) {
            return "";
        }
        final byte[] clearBytes = clearString.getBytes();
        //pad bytes to multiple of 8
        final int pad = 8 - clearBytes.length % 8;
        final byte[] fixedBytes = new byte[pad + clearBytes.length];
        Arrays.fill(fixedBytes, 0, pad, (byte) pad);
        System.arraycopy(clearBytes, 0, fixedBytes, pad, clearBytes.length);
        return Hex.encode(AuthUtils.encrypt(fixedBytes, getEncryptionKey()));
    }

    private static byte[] getBaseKey() {
        final List<Class<?>> providers = KernelLayers.classListFromTopToBottomLayer("server.app.SecurityProvider");
        if (providers.isEmpty()) {
            return STUB_KEY;
        } else {
            try {
                return ((SecurityProvider) providers.get(0).newInstance()).getBaseKey();
            } catch (Throwable t) {
                throw new RadixError("Unable to get base encryption key", t);
            }
        }
    }

    private static byte[] getEncryptionKey() {
        return AuthUtils.encrypt(getBaseKey(), salt);
    }

    /**
     * generate 16 random bytes
     *
     * @return
     */
    private static byte[] generateSalt() {
        final SecureRandom secureRandom = new SecureRandom();
        final byte[] saltBytes = new byte[16];
        secureRandom.nextBytes(saltBytes);
        return saltBytes;
    }

    public static class DecryptionException extends Exception {

        public DecryptionException(Throwable cause) {
            super(cause);
        }

        public DecryptionException(String message, Throwable cause) {
            super(message, cause);
        }

        public DecryptionException(String message) {
            super(message);
        }

        public DecryptionException() {
        }
    }

    public static class UnableToLoadParamsFromFileException extends RuntimeException {

        public UnableToLoadParamsFromFileException(Throwable cause) {
            super(cause);
        }

        public UnableToLoadParamsFromFileException(String message, Throwable cause) {
            super(message, cause);
        }

        public UnableToLoadParamsFromFileException(String message) {
            super(message);
        }

        public UnableToLoadParamsFromFileException() {
        }
    }

    /**
     * Interface for accessing stored passwords. Using char[] instead of String
     * for better security is useless, because JDBC accepts database password as
     * String anyway.
     */
    public static interface PasswordReciever {

        public void recievePassword(String password);
    }

    public static interface NewInstanceSettings {

        String getInstanceSapAddress();

        String getEasSapAddress();

        String getScp();
    }

    private static class NewInstanceSettingsImpl implements NewInstanceSettings {

        private volatile String instanceSapAddress;
        private volatile String easSapAddress;
        private volatile String scp;

        @Override
        public String getEasSapAddress() {
            return easSapAddress;
        }

        public void setEasSapAddress(final String easSapAddress) {
            this.easSapAddress = easSapAddress;
        }

        @Override
        public String getInstanceSapAddress() {
            return instanceSapAddress;
        }

        public void setInstanceSapAddress(final String instanceSapAddress) {
            this.instanceSapAddress = instanceSapAddress;
        }

        @Override
        public String getScp() {
            return scp;
        }

        public void setScp(final String scp) {
            this.scp = scp;
        }
    }

    public static class ParamException extends Exception {

        private static final long serialVersionUID = 7535453305422186668L;

        protected ParamException(final String mess) {
            super(mess);
        }

        protected ParamException(final String mess, final Throwable cause) {
            super(mess, cause);
        }
    }

    public static class UnsupportedParamException extends ParamException {

        private static final long serialVersionUID = -2302839513811727928L;

        public UnsupportedParamException(final String paramName) {
            super("Unsupported command line parameter: " + paramName);
        }
    }

    public static class ParamValueException extends ParamException {

        private static final long serialVersionUID = -248839982902346062L;

        protected ParamValueException(final String mess) {
            super(mess);
        }

        protected ParamValueException(final String mess, final Throwable cause) {
            super(mess, cause);
        }
    }

    public static final class EmptyParamValueException extends ParamValueException {

        private static final long serialVersionUID = -248839982902346062L;

        public EmptyParamValueException(final String paramName) {
            super("The parameter \"" + paramName + "\" does not have a value");
        }
    }

    public static final class WrongParamValueException extends ParamValueException {

        private static final long serialVersionUID = 1265846871362594666L;

        WrongParamValueException(final String paramName, final Throwable cause) {
            super("The parameter \"" + paramName + "\" has wrong value: " + ExceptionTextFormatter.getExceptionMess(cause), cause);
        }
    }

    public static class ConfigFileNotSpecifiedException extends Exception {

        public ConfigFileNotSpecifiedException(final Throwable cause) {
            super(cause);
        }

        public ConfigFileNotSpecifiedException(final String message) {
            super(message);
        }
    }

    public static class UnableToLoadOptionsFromFile extends Exception {

        public UnableToLoadOptionsFromFile(final Throwable cause) {
            super(cause);
        }

        public UnableToLoadOptionsFromFile(final String message) {
            super(message);
        }
    }

    public static class OptionProcessingResult {

        public static final String NO_SUCH_OPTION = "Configuration file doesn't contain this option";
        private final String optionName;
        private final boolean succeed;
        private final String comment;

        public OptionProcessingResult(final String optionName, final boolean succeed, final String comment) {
            this.optionName = optionName;
            this.succeed = succeed;
            this.comment = comment;
        }

        public String getComment() {
            return comment;
        }

        public String getOptionName() {
            return optionName;
        }

        public boolean isSucceed() {
            return succeed;
        }

        public boolean isNoSuchOption() {
            return NO_SUCH_OPTION.equals(comment);
        }
    }
}
