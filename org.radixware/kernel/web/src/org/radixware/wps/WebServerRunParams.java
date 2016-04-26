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

package org.radixware.wps;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.auth.AuthUtils;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.enums.EClientAuthentication;
import org.radixware.kernel.common.enums.EKeyStoreType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.kerberos.KrbServiceOptions;
import org.radixware.kernel.common.ssl.KeystoreController;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.trace.TraceProfile;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.starter.config.ConfigEntry;
import org.radixware.kernel.starter.config.ConfigFileAccessor;
import org.radixware.kernel.starter.config.ConfigFileParseException;
import org.radixware.wps.WebServerRunParams.ConfigFileNotSpecifiedException;
import org.radixware.wps.WebServerRunParams.OptionProcessingResult;
import org.radixware.wps.WebServerRunParams.UnableToLoadOptionsFromFile;
import org.radixware.wps.utils.KernelLayers;
import org.radixware.wps.utils.SecurityProvider;

public final class WebServerRunParams {

    private enum EParam {
        /*common client parameters*/

        TRACE_DIR("-traceDir", "trace directory"),
        TRACE_PROFILE("-traceProfile", "trace profile", false, true),
        DEVELOPMENT_MODE("-development", "development mode", true, true),
        RESTORE_TREE_POSITION("-restoreTreePosition", "restore position in explorer tree", true),
        /*web server parameters*/
        CONNECTIONS_FILE("-connectionsFile", "connections file"),
        SESSION_MAX_INACTIVE_INTERVAL("-sessionMaxInactiveInterval", "session maximum inactive interval in sec"),
        SSH_REQUIRED("-sshRequired", "security connection required", true),
        /*kerberos parameters*/
        KRB_AUTH_POLICY("-krbAuthPolicy", null),
        WPS_SPN("-wpsSpn", null),
        KEYTAB_FILE("-keyTabFile", null),
        REMOTE_KRB_AUTH("-remoteKrbAuth", null),
        USE_DELEGATED_CREDENTIALS("-useDelegatedCredentials", null, true),
        FALLBACK_TO_CERTIFICATE_AUTH("-fallbackToCertificateAuth", null, true),
        DOWNGRADE_NTLM("-downgradeNtlm", null, true),
        /*certificate parameters*/
        ACC_NAME_CERT_ATTR("-certAttrForAccName", "certificate attribute that contains user account name", false),
        KEYSTORE_FILE("-keyStoreFile", "key store file"),
        CERTIFICATE_ALIAS("-certificateAlias", "alias for certificate in keystore file", false),
        KEYSTORE_PWD("-keyStorePwd", null),
        ENCRYPTED_KS_PWD("-encryptedKsPwd", null, false),
        SALT("-salt", null),
        /*file uploading parameters*/
        FILE_SIZE_SOFT_LIMIT_MB("-uploadFileSizeSoftLimitMb", "uploading file size soft limit (Mb)", false),
        FILE_SIZE_HARD_LIMIT_MB("-uploadFileSizeHardLimitMb", "uploading file size hard limit (Mb)", false),
        SETTINGS_DATABASE_PATH("-settingsDatabasePath", "custom local database storage dir", false, false);
        private final String argument;
        private final String title;
        private final boolean logical;
        private final boolean common;

        private EParam(final String name, final String title) {
            this(name, title, false, false);
        }

        private EParam(final String name, final String title, final boolean isLogical) {
            this(name, title, isLogical, false);
        }

        private EParam(final String arg, final String title, final boolean isLogical, final boolean isCommon) {
            argument = arg;
            this.title = title;
            logical = isLogical;
            common = isCommon;
        }

        public String getName() {
            return argument;
        }

        public String getTitle() {
            return title;
        }

        public boolean isBoolean() {
            return logical;
        }

        public boolean isCommon() {
            return common;
        }

        public static EParam getForArg(final String arg) {
            for (EParam param : EParam.values()) {
                if (param.getName().equals(arg)) {
                    return param;
                }
            }
            return null;
        }
    }
    private final static Map<EParam, Object> PARAM_VALUES = new EnumMap<>(EParam.class);
    private final static ReentrantReadWriteLock PARAM_RW_LOCK = new ReentrantReadWriteLock();
    private static final String CONFIG_FILE = "-configFile";
    private static volatile String configFile;
    private static final String SERVER_SECTION = "WebPresentationServer";
    //array of zeroes (guaranteed by language specification)
    private static final byte[] STUB_KEY = new byte[16];

    private WebServerRunParams() {
        //singleton
    }

    private static Object getParamValue(final EParam param) {
        PARAM_RW_LOCK.readLock().lock();
        try {
            return PARAM_VALUES.get(param);
        } finally {
            PARAM_RW_LOCK.readLock().unlock();
        }
    }

    private static String getStringParamValue(final EParam param) {
        PARAM_RW_LOCK.readLock().lock();
        try {
            return (String) PARAM_VALUES.get(param);
        } finally {
            PARAM_RW_LOCK.readLock().unlock();
        }
    }

    private static int getIntParamValue(final EParam param) {
        PARAM_RW_LOCK.readLock().lock();
        try {
            return ((Integer) PARAM_VALUES.get(param)).intValue();
        } finally {
            PARAM_RW_LOCK.readLock().unlock();
        }
    }

    private static boolean containsParamValue(final EParam param) {
        PARAM_RW_LOCK.readLock().lock();
        try {
            return PARAM_VALUES.containsKey(param);
        } finally {
            PARAM_RW_LOCK.readLock().unlock();
        }
    }

    public static String getConfigFile() {
        return configFile;
    }

    public static String getConnectionsFile() {
        return getStringParamValue(EParam.CONNECTIONS_FILE);
    }

    public static String getSettingsDatabaseDir() {
        return getStringParamValue(EParam.SETTINGS_DATABASE_PATH);
    }

    public static String getTraceDir() {
        return getStringParamValue(EParam.TRACE_DIR);
    }

    public static String getTraceProfile() {
        return getStringParamValue(EParam.TRACE_PROFILE);
    }

    public static String getCertificateAlias() {
        final String alias = getStringParamValue(EParam.CERTIFICATE_ALIAS);
        return alias != null && alias.isEmpty() ? null : alias;
    }

    public static boolean getIsDevelopmentMode() {
        return containsParamValue(EParam.DEVELOPMENT_MODE);
    }

    public static boolean restoreTreePosition() {
        return containsParamValue(EParam.RESTORE_TREE_POSITION);
    }

    public static boolean isSshRequired() {
        return containsParamValue(EParam.SSH_REQUIRED);
    }

    public static int getUploadSoftLimitMb() {
        return getIntParamValue(EParam.FILE_SIZE_SOFT_LIMIT_MB);
    }

    public static int getUploadHardLimitMb() {
        return getIntParamValue(EParam.FILE_SIZE_HARD_LIMIT_MB);
    }

    public static KrbWpsOptions getKerberosOptions() {
        PARAM_RW_LOCK.readLock().lock();
        try {
            EClientAuthentication krbAuth = (EClientAuthentication) PARAM_VALUES.get(EParam.KRB_AUTH_POLICY);
            if (krbAuth == null || krbAuth == EClientAuthentication.None) {
                return null;
            }
            return new KrbWpsOptions(krbAuth,
                    (String) PARAM_VALUES.get(EParam.WPS_SPN),
                    (String) PARAM_VALUES.get(EParam.KEYTAB_FILE),
                    (ERemoteKerberosAuthScheme) PARAM_VALUES.get(EParam.REMOTE_KRB_AUTH),
                    PARAM_VALUES.containsKey(EParam.USE_DELEGATED_CREDENTIALS),
                    PARAM_VALUES.containsKey(EParam.DOWNGRADE_NTLM),
                    PARAM_VALUES.containsKey(EParam.FALLBACK_TO_CERTIFICATE_AUTH));
        } finally {
            PARAM_RW_LOCK.readLock().unlock();
        }
    }

    public static String getCertAttrForUserName() {
        final String certAttr = getStringParamValue(EParam.ACC_NAME_CERT_ATTR);
        return certAttr == null || certAttr.isEmpty() ? "CN" : certAttr;
    }

    public static int getSessionInactiveInteraval() {
        final Integer value = (Integer) getParamValue(EParam.SESSION_MAX_INACTIVE_INTERVAL);
        return value == null ? 0 : value.intValue();
    }

    public static String print() {
        final StringBuffer strBuffer = new StringBuffer();
        strBuffer.append("{");
        PARAM_RW_LOCK.readLock().lock();
        try {
            for (EParam param : EnumSet.allOf(EParam.class)) {
                if (param.getTitle() != null && !param.getTitle().isEmpty()) {
                    strBuffer.append("\n\t");
                    strBuffer.append(param.getTitle());
                    strBuffer.append(": ");
                    final Object value = PARAM_VALUES.get(param);
                    if (value == null) {
                        strBuffer.append(param.isBoolean() ? "false" : "not defined");
                    } else {
                        strBuffer.append(String.valueOf(value).toLowerCase());
                    }
                }
            }
        } finally {
            PARAM_RW_LOCK.readLock().unlock();
        }
        final KrbWpsOptions krbOptions = getKerberosOptions();
        if (krbOptions == null) {
            strBuffer.append("\n\tkerberos authentication: disabled");
        } else {
            strBuffer.append("\n\tkerberos authentication options: {\n");
            strBuffer.append(krbOptions.toString());
            strBuffer.append("\n\t}");
        }
        strBuffer.append("\n}");
        return strBuffer.toString();
    }

    static boolean processArgs(final String[] args) {
        configFile = findConfigFileParameter(args);
        if (configFile == null) {
            try {
                processArgsImpl(args, null);
            } catch (ParamException | ConfigFileParseException ex) {
                System.err.println(ex.getMessage());
                return false;
            } catch (DecryptionException ex) {
                System.err.println("Error while decrypting passwords: " + ex.getMessage());
            }
            return true;

        } else {
            final List<String> argsForProcessing;
            try {
                argsForProcessing = readArgsFromFile(configFile);
            } catch (UnableToLoadOptionsFromFile ex) {
                System.err.println("Error while reading Server parameters:");
                ex.printStackTrace(System.err);
                return false;
            }
            try {
                processArgsImpl(argsForProcessing.toArray(new String[argsForProcessing.size()]), null);
            } catch (ParamException | ConfigFileParseException ex) {
                System.err.println(ex.getMessage());
                return false;
            } catch (DecryptionException ex) {
                System.err.println("Error while decrypting passwords: " + ex.getMessage());
            }
            return true;
        }
    }

    private static List<String> readArgsFromFile(final String fileName) throws UnableToLoadOptionsFromFile {
        return readArgsFromFileImpl(fileName, null);
    }

    /**
     * Load options from file. The file must be a regular java .properties file
     *
     * @param fileName - properties file
     * @param optionsToRead - keys that should be read. null value permits all keys.
     * @return list of strings that represents arguments array
     */
    private static List<String> readArgsFromFileImpl(final String fileName, final Collection<String> optionsToRead) throws UnableToLoadOptionsFromFile {
        try {
            final ConfigFileAccessor configAccessor = ConfigFileAccessor.get(fileName, SERVER_SECTION);
            if (configAccessor == null) {
                throw new UnableToLoadOptionsFromFile(SERVER_SECTION + " section was not found in configuration file");
            }
            final List<String> args = new ArrayList<>();
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
                results.add(new OptionProcessingResult(option, false, "Configuration file doesn't contain this option"));
                continue;
            }
            try {
                processArgsImpl(args.toArray(new String[args.size()]), tracer);
                results.add(new OptionProcessingResult(option, true, ""));
            } catch (ParamException | ConfigFileParseException ex) {
                results.add(new OptionProcessingResult(option, false, ex.getMessage()));
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
            EParam.KEYSTORE_PWD.getName(),
            EParam.ENCRYPTED_KS_PWD.getName(),
            EParam.SALT.getName()});
        final List<String> toRemove = new ArrayList<>();
        for (final ConfigEntry existingEntry : entries) {
            final String nameWithMinus = existingEntry.getKey().startsWith("-") ? existingEntry.getKey() : "-".concat(existingEntry.getKey());
            if (needToClear.contains(nameWithMinus)) {
                toRemove.add(existingEntry.getKey());
            }
        }
        final List<String> wasInFile = new ArrayList<>();
        for (final String removedProperty : toRemove) {
            wasInFile.add(removedProperty.startsWith("-") ? removedProperty : "-".concat(removedProperty));
        }
        final List<ConfigEntry> toWrite = new ArrayList<>();
        final String ksPwd;

        PARAM_RW_LOCK.readLock().lock();
        try {
            ksPwd = decrypt((String) PARAM_VALUES.get(EParam.ENCRYPTED_KS_PWD), (byte[]) PARAM_VALUES.get(EParam.SALT));
        } finally {
            PARAM_RW_LOCK.readLock().unlock();
        }

        final byte[] newSalt = generateSalt();
        final String newEncryptedKsPwd;
        if (ksPwd != null && (wasInFile.contains(EParam.KEYSTORE_PWD.getName()) || wasInFile.contains(EParam.ENCRYPTED_KS_PWD.getName()))) {
            newEncryptedKsPwd = encrypt(ksPwd, newSalt);
            toWrite.add(new ConfigEntry(EParam.ENCRYPTED_KS_PWD.getName().substring(1), newEncryptedKsPwd));
        } else {
            return;
        }

        toWrite.add(new ConfigEntry(EParam.SALT.getName().substring(1), Hex.encode(newSalt)));
        configAccessor.update(toRemove, toWrite);

        PARAM_RW_LOCK.writeLock().lock();
        try {
            PARAM_VALUES.put(EParam.ENCRYPTED_KS_PWD, newEncryptedKsPwd);
            PARAM_VALUES.put(EParam.SALT, newSalt);
        } finally {
            PARAM_RW_LOCK.writeLock().unlock();
        }
    }

    private static String findConfigFileParameter(final String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(CONFIG_FILE)) {
                return args[++i];
            }
        }
        return null;
    }

    private static void processArgsImpl(final String[] args, final LocalTracer tracer) throws ParamException, DecryptionException, ConfigFileParseException {
        final int len = args.length;

        String ksPwd = null, encKsPwd = null;
        byte[] salt = null;
        Integer fileSizeSoftLimitMb = null;
        Integer fileSizeHardLimitMb = null;
        final List<String> commonParamsList = new LinkedList<>();
        PARAM_RW_LOCK.writeLock().lock();
        try {
            for (int i = 0; i < len; i++) {
                final EParam param = EParam.getForArg(args[i]);
                if (param != null) {
                    final String valueAsStr = param.isBoolean() ? null : readValue(args, ++i, param);
                    final Object value;
                    switch (param) {
                        case SESSION_MAX_INACTIVE_INTERVAL:
                            try {
                                value = Integer.parseInt(valueAsStr);
                            } catch (NumberFormatException e) {
                                throw new ParamValueException(param.getName(), e);
                            }
                            break;
                        case KRB_AUTH_POLICY:
                            value = parseKrbAuthPolicy(valueAsStr);
                            break;
                        case REMOTE_KRB_AUTH:
                            value = parseRemoteKrbAuthScheme(valueAsStr);
                            break;
                        case SALT:
                            try {
                                salt = Hex.decode(valueAsStr);
                                value = salt;
                            } catch (WrongFormatError e) {
                                throw new ParamValueException(param.getName(), e);
                            }
                            break;
                        case KEYSTORE_PWD:
                            ksPwd = valueAsStr;
                            continue;
                        case ENCRYPTED_KS_PWD:
                            encKsPwd = valueAsStr;
                            continue;
                        case FILE_SIZE_SOFT_LIMIT_MB:
                            try {
                                fileSizeSoftLimitMb = Integer.parseInt(valueAsStr);
                            } catch (NumberFormatException e) {
                                throw new ParamValueException(param.getName(), e);
                            }
                            value = fileSizeSoftLimitMb;
                            break;
                        case FILE_SIZE_HARD_LIMIT_MB:
                            try {
                                fileSizeHardLimitMb = Integer.parseInt(valueAsStr);
                            } catch (NumberFormatException e) {
                                throw new ParamValueException(param.getName(), e);
                            }
                            value = fileSizeHardLimitMb;
                            break;
                        case TRACE_PROFILE:
                            try{
                                new TraceProfile(valueAsStr);
                            }catch(NoConstItemWithSuchValueError | WrongFormatError error){
                                final Logger logger = Logger.getLogger(WebServer.class.getName());                                
                                final String reason = error.getMessage();
                                if (reason==null || reason.isEmpty()){
                                    logger.log(Level.SEVERE, "Parameter \'\'{0}\'\' has invalid value \'\'{1}\'\'.Using default value (\'\'None\'\')", new Object[]{param.getName(),valueAsStr});
                                }else{
                                    logger.log(Level.SEVERE, "Parameter \'\'{0}\'\' has invalid value \'\'{1}\'\':\n{2}\nUsing default value (\'\'None\'\')", new Object[]{param.getName(),valueAsStr,reason});
                                }
                                continue;
                            }
                            value = valueAsStr;
                            break;
                        default:
                            value = param.isBoolean() ? Boolean.TRUE : valueAsStr;
                    }
                    PARAM_VALUES.put(param, value);
                    if (param.isCommon()) {
                        commonParamsList.add(param.getName());
                        if (!param.isBoolean()) {
                            commonParamsList.add(valueAsStr);
                        }
                    }
                }
            }

            if (salt == null) {
                if (encKsPwd != null) {
                    throw new ParamException("Keystore password is encrypted, but salt is not defined");
                }
                salt = generateSalt();
                PARAM_VALUES.put(EParam.SALT, salt);
            }
            if (ksPwd != null) {
                KeystoreController.setServerKeystorePassword(ksPwd.toCharArray());
                PARAM_VALUES.put(EParam.ENCRYPTED_KS_PWD, encrypt(ksPwd, salt));
                reencryptPasswords();
            } else if (encKsPwd != null) {
                KeystoreController.setServerKeystorePassword(decrypt(encKsPwd, salt).toCharArray());
                PARAM_VALUES.put(EParam.ENCRYPTED_KS_PWD, encKsPwd);
            }
            if (fileSizeSoftLimitMb == null) {
                PARAM_VALUES.put(EParam.FILE_SIZE_SOFT_LIMIT_MB, 10);
            }
            if (fileSizeHardLimitMb == null) {
                PARAM_VALUES.put(EParam.FILE_SIZE_HARD_LIMIT_MB, 100);
            }
        } finally {
            PARAM_RW_LOCK.writeLock().unlock();
        }
        final String keyStoreFilePath = getStringParamValue(EParam.KEYSTORE_FILE);
        if (keyStoreFilePath != null) {
            KeystoreController.setServerKeystoreType(EKeyStoreType.FILE);
            KeystoreController.setServerKeystorePath(keyStoreFilePath);
        }
        if (!commonParamsList.isEmpty()) {
            try {
                RunParams.initialize(commonParamsList.toArray(new String[commonParamsList.size()]));
            } catch (Exception exception) {
                throw new ParamValueException(exception.getMessage(), exception);
            }
        }
    }

    private static String readValue(final String[] args, final int index, final EParam param) throws EmptyParamValueException {
        if (index >= args.length) {
            throw new EmptyParamValueException(param);
        }
        final String value = args[index];
        if (value == null || value.isEmpty()) {
            throw new EmptyParamValueException(param);
        }
        return value;
    }

    private static EClientAuthentication parseKrbAuthPolicy(final String value) throws WrongParamValueException {
        for (EClientAuthentication t : EClientAuthentication.values()) {
            if (t.name().equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new WrongParamValueException(EParam.KRB_AUTH_POLICY, value);
    }

    private static ERemoteKerberosAuthScheme parseRemoteKrbAuthScheme(final String value) throws WrongParamValueException {
        for (ERemoteKerberosAuthScheme t : ERemoteKerberosAuthScheme.values()) {
            if (t.name().equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new WrongParamValueException(EParam.REMOTE_KRB_AUTH, value);
    }

    private static String decrypt(final String encryptedString, final byte[] salt) throws DecryptionException {
        if (encryptedString == null) {
            return null;
        }
        final byte[] decryptedBytes = AuthUtils.decrypt(Hex.decode(encryptedString), getEncryptionKey(salt));
        if (decryptedBytes[0] > 8 || decryptedBytes.length - decryptedBytes[0] < 0) {
            throw new DecryptionException("Encrypted data is corrupted");
        }
        return new String(decryptedBytes, decryptedBytes[0], decryptedBytes.length - decryptedBytes[0]);
    }

    private static String encrypt(final String clearString, final byte[] salt) {
        if (clearString == null) {
            return null;
        }
        final byte[] clearBytes = clearString.getBytes();
        //pad bytes to multiple of 8
        final int pad = 8 - clearBytes.length % 8;
        final byte[] fixedBytes = new byte[pad + clearBytes.length];
        Arrays.fill(fixedBytes, 0, pad, (byte) pad);
        System.arraycopy(clearBytes, 0, fixedBytes, pad, clearBytes.length);
        return Hex.encode(AuthUtils.encrypt(fixedBytes, getEncryptionKey(salt)));
    }

    private static byte[] getBaseKey() {
        final List<Class<?>> providers = KernelLayers.classListFromTopToBottomLayer("wps.app.SecurityProvider");
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

    private static byte[] getEncryptionKey(final byte[] salt) {
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
        
        private static final long serialVersionUID = 7230958948238457475L;

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
        
        private static final long serialVersionUID = -4043723038345083837L;

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

    private static final class WrongParamValueException extends ParamValueException {

        private static final long serialVersionUID = -8518111493343612371L;

        WrongParamValueException(final EParam param, final String paramValue) {
            super("The parameter \"" + param.getName() + "\" has wrong value: \"" + paramValue + "\"");
        }

        WrongParamValueException(final EParam param, final String paramValue, final Throwable cause) {
            super("The parameter \"" + param.getName() + "\" has wrong value: \"" + paramValue + "\"", cause);
        }
    }

    private static final class EmptyParamValueException extends ParamValueException {

        private static final long serialVersionUID = 1811008521421082469L;

        EmptyParamValueException(final EParam param, final Throwable cause) {
            super("The parameter \"" + param.getName() + "\" does not have a value", cause);
        }

        EmptyParamValueException(final EParam param) {
            super("The parameter \"" + param.getName() + "\" does not have a value");
        }
    }

    public static class ConfigFileNotSpecifiedException extends Exception {
        
        private static final long serialVersionUID = -5648382273132573241L;

        public ConfigFileNotSpecifiedException(final Throwable cause) {
            super(cause);
        }

        public ConfigFileNotSpecifiedException(final String message) {
            super(message);
        }
    }

    public static class UnableToLoadOptionsFromFile extends Exception {
        
        private static final long serialVersionUID = 7688520073547402982L;

        public UnableToLoadOptionsFromFile(final Throwable cause) {
            super(cause);
        }

        public UnableToLoadOptionsFromFile(final String message) {
            super(message);
        }
    }

    public static class OptionProcessingResult {

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
    }

    public static final class KrbWpsOptions extends KrbServiceOptions {

        private final EClientAuthentication authPolicy;
        private final ERemoteKerberosAuthScheme remoteAuthScheme;
        private final boolean allowDelegation;
        private final boolean downgradeNtlm;
        private final boolean fallbackToCertificateAuth;

        public KrbWpsOptions(final EClientAuthentication krbAuthPolicy,
                final String spn,
                final String keyTab,
                final ERemoteKerberosAuthScheme remoteAuthScheme,
                final boolean allowDelegatedCredentials,
                final boolean downgradeNtlm,
                final boolean fallbackToCertAuth) {
            super(keyTab, spn, true);
            authPolicy = krbAuthPolicy;
            this.remoteAuthScheme =
                    remoteAuthScheme == null ? ERemoteKerberosAuthScheme.DISABLED : remoteAuthScheme;
            allowDelegation = allowDelegatedCredentials;
            this.downgradeNtlm = downgradeNtlm;
            fallbackToCertificateAuth = fallbackToCertAuth;
        }

        @Override
        protected String getDefaultPrincipalName() {
            try {
                return "HTTP/" + InetAddress.getLocalHost().getCanonicalHostName();
            } catch (UnknownHostException exception) {
                return "HTTP/localhost";
            }
        }

        public ERemoteKerberosAuthScheme getRemoteAuthScheme() {
            return remoteAuthScheme;
        }

        public boolean downgradeNtlm() {
            return downgradeNtlm;
        }

        public boolean isCredentialsDelegationAllowed() {
            return allowDelegation;
        }

        public boolean canUseCertificate() {
            return fallbackToCertificateAuth;
        }

        public boolean isKerberosAuthRequired() {
            return authPolicy == EClientAuthentication.Required;
        }

        @Override
        public String toString() {
            final StringBuffer strBuffer = new StringBuffer();
            strBuffer.append("\t\tkrbAuthPolicy: ");
            strBuffer.append(authPolicy.getName().toLowerCase());
            strBuffer.append("\n");
            strBuffer.append(super.toString());
            strBuffer.append("\n\t\tremoteKrbAuth: ");
            strBuffer.append(remoteAuthScheme.name().toLowerCase());
            strBuffer.append("\n\t\tcredential delegation allowed: ");
            strBuffer.append(String.valueOf(allowDelegation));
            strBuffer.append("\n\t\tNTLM authentication: ");
            strBuffer.append(downgradeNtlm ? "downgrade" : "disabled");
            strBuffer.append("\n\t\tUse authentication by certificate when kerberos failed: ");
            strBuffer.append(fallbackToCertificateAuth ? "yes" : "no");
            return strBuffer.toString();
        }
    }
}
