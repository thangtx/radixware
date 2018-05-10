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
import org.radixware.kernel.common.enums.EEventSeverity;
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
import org.radixware.wps.rwt.Banner;
import org.radixware.wps.utils.KernelLayers;
import org.radixware.wps.utils.SecurityProvider;

public final class WebServerRunParams {

    private enum EParam {
        /*common client parameters*/

        TRACE_DIR("-traceDir", "trace directory"),
        TRACE_PROFILE("-traceProfile", "trace profile", false, true),
        DEVELOPMENT_MODE("-development", "development mode", true, true),
        TRACE_MIN_SEVERITY("-traceMinSeverity", "trace min severity", false, true),
        RESTORE_TREE_POSITION("-restoreTreePosition", "restore position in explorer tree", true),
        /*web server parameters*/
        CONNECTIONS_FILE("-connectionsFile", "connections file"),
        SETTINGS_DATABASE_PATH("-settingsDatabasePath", "custom local database storage dir", false, false),
        SESSION_MAX_INACTIVE_INTERVAL("-sessionMaxInactiveInterval", "session maximum inactive interval in sec"),
        SSH_REQUIRED("-sshRequired", "security connection required", true),
        /*kerberos parameters*/
        KRB_AUTH_POLICY("-krbAuthPolicy", null),
        DISABLE_SPNEGO_AUTH("-disableSPNEGOAuth", "disable SPNEGO mechanism authentication", true),
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
        /*banner parameters*/
        BANNER_DIR("-bannerDir", "banner directory", false),
        BANNER_FILE("-bannerFile", "banner file", false),
        BANNER_FRAME_STYLE("-bannerFrameStyle", "banner frame style", false),
        BANNER_FRAME_HEIGHT("-bannerFrameHeight", "banner frame height", false),
        /*admin panel parameters*/
        ADMIN_URL_PARAM("-adminPanelUrlParam", "Url parameter to open admin panel", false),
        ADMIN_USERS("-adminUsers", "Admin user names", false),
        /*other parameters*/
        WRITE_OBJECT_NAMES_TO_HTML("-writeObjectNamesToHtml","write object names to html",true);
        
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
    
    public static final WebServerRunParams EMPTY = new WebServerRunParams();
    
    private static final String CONFIG_FILE_PARAM_NAME = "-configFile";
    private static volatile String CONFIG_FILE_PATH;
    private static final String SERVER_SECTION = "WebPresentationServer";
    //array of zeroes (guaranteed by language specification)
    private static final byte[] STUB_KEY = new byte[16];    
    
    private static final WebServerRunParams COMMON_INSTANCE = new WebServerRunParams();
    private final static ReentrantReadWriteLock COMMON_INSTANCE_RW_LOCK = new ReentrantReadWriteLock();
    
    private final Map<EParam, Object> paramValues = new EnumMap<>(EParam.class);

    private WebServerRunParams() {
        //singleton
    }
    
    public static WebServerRunParams newInstance(){
        final WebServerRunParams instance = new WebServerRunParams();
        COMMON_INSTANCE_RW_LOCK.readLock().lock();
        try{
            instance.paramValues.putAll(COMMON_INSTANCE.paramValues);
        }finally{
            COMMON_INSTANCE_RW_LOCK.readLock().unlock();
        }
        return instance;
    }
    
    public static WebServerRunParams readFromFile(){
        final WebServerRunParams params = new WebServerRunParams();
        return params.readFromConfigFile(false) ? params : null;
    }

    private Object getParamValue(final EParam param) {
        return paramValues.get(param);
    }

    private String getStringParamValue(final EParam param) {
        return (String) paramValues.get(param);
    }

    private int getIntParamValue(final EParam param) {
        return ((Integer) paramValues.get(param)).intValue();
    }

    private boolean containsParamValue(final EParam param) {
        return paramValues.containsKey(param);
    }
    
    private static void setParamValue(final EParam param, final Object value){
        COMMON_INSTANCE_RW_LOCK.writeLock().lock();
        try{
            COMMON_INSTANCE.paramValues.put(param, value);
        }finally{
            COMMON_INSTANCE_RW_LOCK.writeLock().unlock();
        }
    }

    public static String getConfigFile() {
        return CONFIG_FILE_PATH;
    }
    
    public String getCertificateKeystoreFile() {
        return getStringParamValue(EParam.KEYSTORE_FILE);
    }

    public String getConnectionsFile() {
        return getStringParamValue(EParam.CONNECTIONS_FILE);
    }
    
    public static void setConnectionsFile(final String filePath){
        setParamValue(EParam.CONNECTIONS_FILE, filePath);
    }

    public String getSettingsDatabaseDir() {
        return getStringParamValue(EParam.SETTINGS_DATABASE_PATH);
    }
    
    public static void setSettingsDatabaseDir(final String dirPath){
        setParamValue(EParam.SETTINGS_DATABASE_PATH, dirPath);
    }

    public String getTraceDir() {
        return getStringParamValue(EParam.TRACE_DIR);
    }
    
    public static void setTraceDir(final String dirPath){
        setParamValue(EParam.TRACE_DIR, dirPath);
    }

    public String getTraceProfile() {
        return getStringParamValue(EParam.TRACE_PROFILE);
    }
    
    public static void setTraceProfile(final String profile){
        setParamValue(EParam.TRACE_PROFILE, profile);
    }

    public String getCertificateAlias() {
        final String alias = getStringParamValue(EParam.CERTIFICATE_ALIAS);
        return alias != null && alias.isEmpty() ? null : alias;
    }
    
    public static void setCertificateAlias(final String alias){
        setParamValue(EParam.CERTIFICATE_ALIAS, alias);
    }

    public static boolean getIsDevelopmentMode() {
        COMMON_INSTANCE_RW_LOCK.readLock().lock();
        try{
            return COMMON_INSTANCE.containsParamValue(EParam.DEVELOPMENT_MODE);
        }finally{
            COMMON_INSTANCE_RW_LOCK.readLock().unlock();
        }        
    }

    public static boolean restoreTreePosition() {
        COMMON_INSTANCE_RW_LOCK.readLock().lock();
        try{        
            return COMMON_INSTANCE.containsParamValue(EParam.RESTORE_TREE_POSITION);
        }finally{
            COMMON_INSTANCE_RW_LOCK.readLock().unlock();
        }
    }

    public static boolean isSshRequired() {
        COMMON_INSTANCE_RW_LOCK.readLock().lock();
        try{                
            return COMMON_INSTANCE.containsParamValue(EParam.SSH_REQUIRED);
        }finally{
            COMMON_INSTANCE_RW_LOCK.readLock().unlock();
        }
    }

    public int getUploadSoftLimitMb() {
        return getIntParamValue(EParam.FILE_SIZE_SOFT_LIMIT_MB);
    }
    
    public static void setUploadSoftLimitMb(final int limit){
        setParamValue(EParam.FILE_SIZE_SOFT_LIMIT_MB, limit);
    }

    public static int getUploadHardLimitMb() {
        COMMON_INSTANCE_RW_LOCK.readLock().lock();
        try{                        
            return COMMON_INSTANCE.getIntParamValue(EParam.FILE_SIZE_HARD_LIMIT_MB);
        }finally{
            COMMON_INSTANCE_RW_LOCK.readLock().unlock();
        }
    }
    
    public String getTraceMinSeverity() {
        return getStringParamValue(EParam.TRACE_MIN_SEVERITY);
    }
    
    public static void setTraceMinSeverity(final EEventSeverity severity){
        setParamValue(EParam.TRACE_MIN_SEVERITY, severity == null ? null : severity.name());
    }
    
    public boolean writeObjectNamesToHtml(){
        boolean contains = containsParamValue(EParam.WRITE_OBJECT_NAMES_TO_HTML);
        if (!contains) {
            return false;
        } else {
            return (Boolean)getParamValue(EParam.WRITE_OBJECT_NAMES_TO_HTML) != null;
        }   
    }
    
    public static void setWriteObjectNamesToHtml(final boolean write){        
        setParamValue(EParam.WRITE_OBJECT_NAMES_TO_HTML, write ? Boolean.TRUE : null);
    }
    
    public KrbWpsOptions getKerberosOptions() {
        EClientAuthentication krbAuth = (EClientAuthentication) paramValues.get(EParam.KRB_AUTH_POLICY);
        if (krbAuth == null) { 
            return null;
        }
        return new KrbWpsOptions(krbAuth,
                (String) paramValues.get(EParam.WPS_SPN),
                (String) paramValues.get(EParam.KEYTAB_FILE),
                (ERemoteKerberosAuthScheme) paramValues.get(EParam.REMOTE_KRB_AUTH),
                paramValues.containsKey(EParam.USE_DELEGATED_CREDENTIALS),
                paramValues.containsKey(EParam.DOWNGRADE_NTLM),
                paramValues.containsKey(EParam.FALLBACK_TO_CERTIFICATE_AUTH),
                paramValues.containsKey(EParam.DISABLE_SPNEGO_AUTH));
    }
    
    public static void setKerberosOptions(final KrbWpsOptions options){        
        if (options==null){
            final EnumSet<EParam> krbParams = 
                EnumSet.of(EParam.KRB_AUTH_POLICY, EParam.WPS_SPN, EParam.KEYTAB_FILE, 
                                  EParam.REMOTE_KRB_AUTH, EParam.USE_DELEGATED_CREDENTIALS, 
                                  EParam.DOWNGRADE_NTLM, EParam.FALLBACK_TO_CERTIFICATE_AUTH);
            COMMON_INSTANCE_RW_LOCK.writeLock().lock();
            try{
                for (EParam param: krbParams){
                    COMMON_INSTANCE.paramValues.put(param, null);
                }
            }finally{
                COMMON_INSTANCE_RW_LOCK.writeLock().unlock();
            }
        }else{
            COMMON_INSTANCE_RW_LOCK.writeLock().lock();
            try{
                if (options.isKerberosAuthRequired()){
                    COMMON_INSTANCE.paramValues.put(EParam.KRB_AUTH_POLICY, EClientAuthentication.Required);
                }else{
                    COMMON_INSTANCE.paramValues.put(EParam.KRB_AUTH_POLICY, EClientAuthentication.Enabled);
                }
                COMMON_INSTANCE.paramValues.put(EParam.WPS_SPN, options.getPrincipalName());
                COMMON_INSTANCE.paramValues.put(EParam.KEYTAB_FILE, options.getAbsoluteKeyTabPath());
                
                COMMON_INSTANCE.paramValues.put(EParam.REMOTE_KRB_AUTH, options.getRemoteAuthScheme());
                if (options.isCredentialsDelegationAllowed()){
                    COMMON_INSTANCE.paramValues.put(EParam.USE_DELEGATED_CREDENTIALS, Boolean.TRUE);
                }else{
                    COMMON_INSTANCE.paramValues.put(EParam.USE_DELEGATED_CREDENTIALS, null);
                }
                if (options.downgradeNtlm()){
                    COMMON_INSTANCE.paramValues.put(EParam.DOWNGRADE_NTLM, Boolean.TRUE);
                }else{
                    COMMON_INSTANCE.paramValues.put(EParam.DOWNGRADE_NTLM, null);
                }
                if (options.canUseCertificate()){
                    COMMON_INSTANCE.paramValues.put(EParam.FALLBACK_TO_CERTIFICATE_AUTH, Boolean.TRUE);
                }else{
                    COMMON_INSTANCE.paramValues.put(EParam.FALLBACK_TO_CERTIFICATE_AUTH, null);
                }
                if (options.isSpnego()) {
                    COMMON_INSTANCE.paramValues.put(EParam.DISABLE_SPNEGO_AUTH, null);
                } else {
                    COMMON_INSTANCE.paramValues.put(EParam.DISABLE_SPNEGO_AUTH, Boolean.TRUE);
                }
            }finally{
                COMMON_INSTANCE_RW_LOCK.writeLock().unlock();
            }            
        }
    }

    public String getCertAttrForUserName() {
        final String certAttr = getStringParamValue(EParam.ACC_NAME_CERT_ATTR);
        return certAttr == null || certAttr.isEmpty() ? "CN" : certAttr;
    }
    
    public static void setCertAttrForUserName(final String attr){
        setParamValue(EParam.ACC_NAME_CERT_ATTR, attr);
    }

    public int getSessionInactiveInteraval() {
        final Integer value = (Integer) getParamValue(EParam.SESSION_MAX_INACTIVE_INTERVAL);
        return value == null ? 0 : value.intValue();
    }
    
    public static void setSessionInactiveInterval(final int interval){
        setParamValue(EParam.SESSION_MAX_INACTIVE_INTERVAL, interval);
    }
    
    public Banner.Options getBannerOptions(){
        final String bannerDir = getStringParamValue(EParam.BANNER_DIR);
        if (bannerDir!=null && !bannerDir.isEmpty()){
            final String bannerFile = getStringParamValue(EParam.BANNER_FILE);
            if (bannerFile!=null && !bannerFile.isEmpty()){
                return new Banner.Options(bannerDir, 
                                                          bannerFile, 
                                                          getStringParamValue(EParam.BANNER_FRAME_STYLE), 
                                                          getStringParamValue(EParam.BANNER_FRAME_HEIGHT));
            }
        }
        return null;
    }
    
    public static void setBannerOptions(final Banner.Options options){
        if (options==null){
            final EnumSet<EParam> bannerParams = 
                EnumSet.of(EParam.BANNER_DIR, EParam.BANNER_FILE, EParam.BANNER_FRAME_STYLE, EParam.BANNER_FRAME_HEIGHT); 
            COMMON_INSTANCE_RW_LOCK.writeLock().lock();
            try{
                for (EParam param: bannerParams){
                    COMMON_INSTANCE.paramValues.put(param, null);
                }
            }finally{
                COMMON_INSTANCE_RW_LOCK.writeLock().unlock();
            }            
        }else{
            COMMON_INSTANCE_RW_LOCK.writeLock().lock();
            try{
                COMMON_INSTANCE.paramValues.put(EParam.BANNER_DIR, options.getDirPath());
                COMMON_INSTANCE.paramValues.put(EParam.BANNER_FILE, options.getFileName());
                COMMON_INSTANCE.paramValues.put(EParam.BANNER_FRAME_STYLE, options.getFrameStyle());
                COMMON_INSTANCE.paramValues.put(EParam.BANNER_FRAME_HEIGHT, options.getFrameHeight());
            }finally{
                COMMON_INSTANCE_RW_LOCK.writeLock().unlock();
            }
        }
    }
    
    public static String getAdminPanelUrlParam(){
        COMMON_INSTANCE_RW_LOCK.readLock().lock();
        try{
            return COMMON_INSTANCE.getStringParamValue(EParam.ADMIN_URL_PARAM);
        }finally{
            COMMON_INSTANCE_RW_LOCK.readLock().unlock();
        }
    }
    
    @SuppressWarnings("unchecked")
    public static List<String> getAdminUserNames(){
        COMMON_INSTANCE_RW_LOCK.readLock().lock();
        try{        
            final Object value = COMMON_INSTANCE.getParamValue(EParam.ADMIN_USERS);
            if (value instanceof List){
                return (List<String>)value;
            }else{
                return Collections.emptyList();
            }
        }finally{
            COMMON_INSTANCE_RW_LOCK.readLock().unlock();
        }
    }

    public String print() {
        final StringBuffer strBuffer = new StringBuffer();
        strBuffer.append("{");
        for (EParam param : EnumSet.allOf(EParam.class)) {
            if (param==EParam.ADMIN_URL_PARAM || param==EParam.ADMIN_USERS){
                continue;
            }
            if (param.getTitle() != null && !param.getTitle().isEmpty()) {
                strBuffer.append("\n\t");
                strBuffer.append(param.getTitle());
                strBuffer.append(": ");
                final Object value = paramValues.get(param);
                if (value == null) {
                    strBuffer.append(param.isBoolean() ? "false" : "not defined");
                } else {
                    strBuffer.append(String.valueOf(value).toLowerCase());
                }
            }          
        }
           final KrbWpsOptions krbOptions = getKerberosOptions();
            if (krbOptions == null || !krbOptions.isKerberosOptionsEnabled()) {
                strBuffer.append("\n\tkerberos authentication: disabled");
            } else {
                strBuffer.append("\n\tkerberos authentication options: {\n");
                strBuffer.append(krbOptions.toString());
                strBuffer.append("\n\t}");
            }
            strBuffer.append("\n}");               
        return strBuffer.toString();
    }

    static WebServerRunParams processArgs(final String[] args) {        
        CONFIG_FILE_PATH = findConfigFileParameter(args);        
        if (CONFIG_FILE_PATH == null) {
            COMMON_INSTANCE_RW_LOCK.writeLock().lock();
            try {
                COMMON_INSTANCE.processArgsImpl(args, null, true);
            } catch (ParamException | ConfigFileParseException ex) {
                Logger.getLogger(WebServer.class.getName()).severe(ex.getMessage());                
                return null;
            } catch (DecryptionException ex) {
                Logger.getLogger(WebServer.class.getName()).severe("Error while decrypting passwords: " + ex.getMessage());
            }finally{
                COMMON_INSTANCE_RW_LOCK.writeLock().unlock();
            }
            return WebServerRunParams.newInstance();
        } else {
            COMMON_INSTANCE_RW_LOCK.writeLock().lock();
            try{
                if (!COMMON_INSTANCE.readFromConfigFile(true)){
                    COMMON_INSTANCE.paramValues.clear();
                    return null;
                }
            }finally{
                COMMON_INSTANCE_RW_LOCK.writeLock().unlock();
            }
            return WebServerRunParams.newInstance();
        }
    }
    
    private boolean readFromConfigFile(final boolean apply){
        final List<String> argsForProcessing;
        try {
            argsForProcessing = readArgsFromFile(CONFIG_FILE_PATH);
        } catch (UnableToLoadOptionsFromFile ex) {
            Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, "Error while reading Server parameters", ex);
            return false;
        }
        try {
            processArgsImpl(argsForProcessing.toArray(new String[argsForProcessing.size()]), null, apply);
        } catch (ParamException | ConfigFileParseException ex) {
            Logger.getLogger(WebServer.class.getName()).severe(ex.getMessage());
            return false;
        } catch (DecryptionException ex) {
            Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE,"Error while decrypting passwords", ex);
        }
        return true;
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

    /*
    public static List<OptionProcessingResult> rereadFromFile(final List<String> options, final LocalTracer tracer) throws ConfigFileNotSpecifiedException, UnableToLoadOptionsFromFile, DecryptionException {
        if (CONFIG_FILE_PATH == null) {
            throw new ConfigFileNotSpecifiedException("Configuration file was not specified at startup");
        }
        final List<OptionProcessingResult> results = new ArrayList<>();
        for (String option : options) {//read every option individually. This approach has a greater cost, but simplifies the code.
            final List<String> args = readArgsFromFileImpl(CONFIG_FILE_PATH, Collections.singleton(option));
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
    }*/

    public static void reencryptPasswords() throws ConfigFileParseException, DecryptionException {
        if (CONFIG_FILE_PATH == null || CONFIG_FILE_PATH.isEmpty()) {
            return;
        }
        final ConfigFileAccessor configAccessor = ConfigFileAccessor.get(CONFIG_FILE_PATH, SERVER_SECTION);
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

        COMMON_INSTANCE_RW_LOCK.readLock().lock();
        try {
            ksPwd = decrypt((String) COMMON_INSTANCE.paramValues.get(EParam.ENCRYPTED_KS_PWD), (byte[]) COMMON_INSTANCE.paramValues.get(EParam.SALT));
        } finally {
            COMMON_INSTANCE_RW_LOCK.readLock().unlock();
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

        COMMON_INSTANCE_RW_LOCK.writeLock().lock();
        try {
            COMMON_INSTANCE.paramValues.put(EParam.ENCRYPTED_KS_PWD, newEncryptedKsPwd);
            COMMON_INSTANCE.paramValues.put(EParam.SALT, newSalt);
        } finally {
            COMMON_INSTANCE_RW_LOCK.writeLock().unlock();
        }
    }

    private static String findConfigFileParameter(final String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(CONFIG_FILE_PARAM_NAME)) {
                return args[++i];
            }
        }
        return null;
    }

    private void processArgsImpl(final String[] args, final LocalTracer tracer, final boolean apply) throws ParamException, DecryptionException, ConfigFileParseException {
        final int len = args.length;

        String ksPwd = null, encKsPwd = null;
        byte[] salt = null;
        Integer fileSizeSoftLimitMb = null;
        Integer fileSizeHardLimitMb = null;
        final List<String> commonParamsList = new LinkedList<>();
        String keyStoreFilePath = null;
        EClientAuthentication krbAuthPolicy = null;
        ERemoteKerberosAuthScheme remoteKrbAuth = null;
        Boolean disableSPNEGOAuth = false;
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
                        krbAuthPolicy = parseKrbAuthPolicy(valueAsStr);
                        value = krbAuthPolicy;
                        break;
                    case REMOTE_KRB_AUTH:
                        remoteKrbAuth = parseRemoteKrbAuthScheme(valueAsStr);
                        value = remoteKrbAuth;
                        break;
                    case SALT:
                        try {
                            salt = Hex.decode(valueAsStr);
                            value = salt;
                        } catch (WrongFormatError e) {
                            throw new ParamValueException(param.getName(), e);
                        }
                        break;
                    case KEYSTORE_FILE:
                        keyStoreFilePath = valueAsStr;
                        value = valueAsStr;
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
                    case TRACE_MIN_SEVERITY:
                         try{
                            EEventSeverity.getForName(valueAsStr);
                        }catch(NoConstItemWithSuchValueError error){
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
                    case ADMIN_USERS:
                        value = parseUserNames(valueAsStr);
                        break;
                    case DISABLE_SPNEGO_AUTH:
                        disableSPNEGOAuth = Boolean.TRUE;
                        value = Boolean.TRUE;
                        break;
                    default:
                        value = param.isBoolean() ? Boolean.TRUE : valueAsStr;
                }
                paramValues.put(param, value);
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
            paramValues.put(EParam.SALT, salt);
        }
        if (ksPwd != null) {
            KeystoreController.setServerKeystorePassword(ksPwd.toCharArray());
            paramValues.put(EParam.ENCRYPTED_KS_PWD, encrypt(ksPwd, salt));
            reencryptPasswords();
        } else if (encKsPwd != null) {
            KeystoreController.setServerKeystorePassword(decrypt(encKsPwd, salt).toCharArray());
            paramValues.put(EParam.ENCRYPTED_KS_PWD, encKsPwd);
        }
        if (fileSizeSoftLimitMb == null) {
            paramValues.put(EParam.FILE_SIZE_SOFT_LIMIT_MB, 10);
        }
        if (fileSizeHardLimitMb == null) {
            paramValues.put(EParam.FILE_SIZE_HARD_LIMIT_MB, 100);
        }

        if (EClientAuthentication.Required.equals(krbAuthPolicy)  && disableSPNEGOAuth.equals(Boolean.TRUE) && 
            (remoteKrbAuth == null || ERemoteKerberosAuthScheme.DISABLED.equals(remoteKrbAuth))) {
            paramValues.remove(EParam.DISABLE_SPNEGO_AUTH);
            Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, "Parameter \'\'{0}\'\' conflicts with \'\'{1}\'=\'{2}\'\' and \'\'{3}\'=\'{4}\'\'. It will be ignored.", new Object[]{EParam.DISABLE_SPNEGO_AUTH.getName(), EParam.KRB_AUTH_POLICY.getName(), EClientAuthentication.Required.getName() , EParam.REMOTE_KRB_AUTH.getName(), ERemoteKerberosAuthScheme.DISABLED.name()});
        }
        if (apply && keyStoreFilePath != null) {
            KeystoreController.setServerKeystoreType(EKeyStoreType.FILE);
            KeystoreController.setServerKeystorePath(keyStoreFilePath);
        }
        if (apply && !commonParamsList.isEmpty()) {
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
    
    private static List<String> parseUserNames(final String value) throws WrongParamValueException, EmptyParamValueException{
        if (value==null || value.isEmpty()){
            throw new EmptyParamValueException(EParam.ADMIN_USERS);
        }else{
            final List<String> userNames = new LinkedList<>();
            final StringBuilder userNameBuilder = new StringBuilder();
            boolean inQuotes = false;
            for (int i=0,length=value.length(); i<length; i++){
                final char symbol = value.charAt(i);
                if (symbol=='"'){
                    if (i<length-1 && value.charAt(i+1)=='"'){
                        userNameBuilder.append('"');
                        i++;
                    }else{
                        inQuotes = !inQuotes;
                    }
                }else if (symbol==',' && !inQuotes){
                    userNames.add(userNameBuilder.toString());
                    userNameBuilder.setLength(0);
                }else{
                    userNameBuilder.append(symbol);
                }
            }
            if (inQuotes){
                throw new WrongParamValueException(EParam.ADMIN_USERS, value);
            }else{
                if (userNameBuilder.length()>0){
                    userNames.add(userNameBuilder.toString());
                }
                return userNames;
            }                    
        }
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
            this(krbAuthPolicy, spn, keyTab, remoteAuthScheme, allowDelegatedCredentials, downgradeNtlm, fallbackToCertAuth, false);
        }
        
        public KrbWpsOptions(final EClientAuthentication krbAuthPolicy,
                final String spn,
                final String keyTab,
                final ERemoteKerberosAuthScheme remoteAuthScheme,
                final boolean allowDelegatedCredentials,
                final boolean downgradeNtlm,
                final boolean fallbackToCertAuth, 
                final boolean spnegoAuthDisabled) {
            super(keyTab, spn, !spnegoAuthDisabled);
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
        
        public boolean isKerberosOptionsEnabled() {
            return authPolicy != EClientAuthentication.None;
        }
        
        @Override
        public String toString() {
            final StringBuffer strBuffer = new StringBuffer();
            strBuffer.append("\t\tkrbAuthPolicy: ");
            strBuffer.append(authPolicy.getName().toLowerCase());
            strBuffer.append("\n\t\tSPNEGO mechanism: ");
            strBuffer.append(isSpnego() ?  "enabled" : "disabled");
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
