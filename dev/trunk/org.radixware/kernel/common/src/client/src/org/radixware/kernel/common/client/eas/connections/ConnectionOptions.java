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

package org.radixware.kernel.common.client.eas.connections;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import javax.security.auth.kerberos.KerberosPrincipal;
import java.security.cert.X509Certificate;
import java.util.EnumSet;
import java.util.Set;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.ITaskWaiter;
import org.radixware.kernel.common.client.env.LocaleManager;
import org.radixware.kernel.common.client.exceptions.FileException;
import org.radixware.kernel.common.client.exceptions.Pkcs11Exception;
import org.radixware.kernel.common.client.types.ExplorerRoot;
import org.radixware.kernel.common.client.utils.Pkcs11Token;
import org.radixware.kernel.common.enums.EAuthType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EIsoCountry;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EKeyStoreType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.ssl.KeystoreController;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.schemas.connections.ConnectionsDocument;

/**
 * Параметры необходимые для открытия DAS-сессии, адрес http-сервера дефиниций,
 * а также параметры доступа к ресурсам клиента.
 *
 */
public abstract class ConnectionOptions {

    public final static String CERTIFICATE_ALIAS = "org.radixware/explorer/key";
    private final static char[] KEYSTORE_EXPLORER_PUBLIC_PASSWORD = "password1234".toCharArray();    
    private KeystoreController keystoreController;
    protected Id id;
    private final boolean isReadOnly;

    public Id getId() {
        return id;
    }
    
    private String name;

    public String getName() {
        return name;
    }
    private String comment;

    public String getComment() {
        return comment;
    }

    /**
     * Адрес сервера, на который будет послан первый запрос
     */
    public List<InetSocketAddress> getInitialServerAddresses() {
        return Collections.unmodifiableList(initialAddresses);
    }

    public String getInitialAddressesAsStr() {
        return initialAddressesAsStr;
    }

    public void setInitialAddressesAsStr(String initialAddressesAsStr) {
        setInitialAddresses(parseAddresses(initialAddressesAsStr));
        this.initialAddressesAsStr = initialAddressesAsStr;
    }

    public boolean hasResolvedAddress() {
        for (InetSocketAddress address : initialAddresses) {
            if (!address.isUnresolved()) {
                return true;
            }
        }
        return false;
    }
    
    List<InetSocketAddress> initialAddresses = new ArrayList<>();
    private String initialAddressesAsStr;//В этом поле хранятся адреса в том виде как их вводил пользователь
    //(InetSocketAddress.toString() форматирует адрес. )

    /**
     * Уровень трассировки во время установки подключения
     */
    public EEventSeverity getEventSeverity() {
        return eventSeverity;
    }

    public void setEventSeverity(EEventSeverity eventSeverity) {
        if (isReadOnly){
            throw new UnsupportedOperationException("Failed to change connection options");
        }        
        this.eventSeverity = eventSeverity;
    }
    
    private EEventSeverity eventSeverity = EEventSeverity.NONE;
    // Параметры запроса CreateSession:

    public String getStationName() {
        return stationName;
    }
    
    private String userName;
    private String stationName;
    private String connectedUser;

    public String getUserName() {
        if (connectedUser == null) {
            return userName;
        } else {
            return connectedUser;
        }
    }

    public void setConnectedUserName(final String userName) {
        connectedUser = userName;
    }
    
    private EIsoLanguage language;

    public EIsoLanguage getLanguage() {
        return language;
    }

    public void setLanguage(final EIsoLanguage language) {
        if (isReadOnly){
            throw new UnsupportedOperationException("Failed to change connection options");
        }        
        this.language = language;
    }
    
    private EIsoCountry country;

    public EIsoCountry getCountry() {
        return country;
    }

    protected void setCountry(final EIsoCountry country) {
        if (isReadOnly){
            throw new UnsupportedOperationException("Failed to change connection options");
        }        
        this.country = country;
    }
    
    private Id explorerRootId;

    protected void setExplorerRootId(final Id explorerRootId) {
        if (isReadOnly){
            throw new UnsupportedOperationException("Failed to change connection options");
        }        
        this.explorerRootId = explorerRootId;
    }

    protected void setComment(String comment) {
        if (isReadOnly){
            throw new UnsupportedOperationException("Failed to change connection options");
        }        
        this.comment = comment;
    }

    public void setName(String name) {
        if (isReadOnly){
            throw new UnsupportedOperationException("Failed to change connection options");
        }        
        this.name = name;
    }

    public void setSslOptions(SslOptions sslOptions) {
        if (isReadOnly){
            throw new UnsupportedOperationException("Failed to change connection options");
        }        
        this.sslOptions = sslOptions;
    }

    public void setStationName(String stationName) {
        if (isReadOnly){
            throw new UnsupportedOperationException("Failed to change connection options");
        }        
        this.stationName = stationName;
    }

    public void setUserName(String userName) {
        if (isReadOnly){
            throw new UnsupportedOperationException("Failed to change connection options");
        }        
        this.userName = userName;
    }

    public void setInitialAddresses(List<InetSocketAddress> initialAddresses) {
        if (isReadOnly){
            throw new UnsupportedOperationException("Failed to change connection options");
        }
        this.initialAddresses.clear();
        this.initialAddresses.addAll(initialAddresses);
    }

    //Параметры ssl
    public final static class SslOptions {

        private boolean useSSLAuth;
        private EKeyStoreType keyStoreType;
        private String certificateAlias;          //only for keyStoreType=EKeyStoreType.PKCS11
        private boolean autoDetectSlotId = true;  //only for keyStoreType=EKeyStoreType.PKCS11
        private int slotId = -1;                  //only for keyStoreType=EKeyStoreType.PKCS11
        private String pkcs11Lib;                 //only for keyStoreType=EKeyStoreType.PKCS11
        private final String trustStoreFilePath;
        private final boolean trustStorePathIsRelative;
        private final char[] trustStorePassword;

        public SslOptions() {
            certificateAlias = CERTIFICATE_ALIAS;
            trustStoreFilePath = null;
            trustStorePathIsRelative = false;
            trustStorePassword = null;
        }

        public SslOptions(SslOptions source) {
            useSSLAuth = source.useSSLAuth;
            keyStoreType = source.keyStoreType;
            certificateAlias = source.certificateAlias;
            pkcs11Lib = source.pkcs11Lib;
            slotId = source.slotId;
            autoDetectSlotId = source.autoDetectSlotId;
            trustStoreFilePath = source.trustStoreFilePath;
            trustStorePathIsRelative = source.trustStorePathIsRelative;
            trustStorePassword = source.trustStorePassword;
        }

        public SslOptions(final ConnectionsDocument.Connections.Connection.SSLOptions options){
            useSSLAuth = options.getUseSslAuth();
            autoDetectSlotId = options.getAutoDetectSlotIndex();
            slotId = options.getSlotIndex();
            pkcs11Lib = options.getPkcs11Lib();
            if (options.getTrustStoreFilePath()==null || options.getTrustStoreFilePath().isEmpty()){
                trustStoreFilePath = null;
                trustStorePathIsRelative = false;
                trustStorePassword = null;
            }else{
                trustStoreFilePath = options.getTrustStoreFilePath();
                trustStorePathIsRelative = options.getTrustStorePathIsRelative();
                if (options.getTrustStorePassword() != null) {
                    trustStorePassword = options.getTrustStorePassword().toCharArray();
                }else{
                    trustStorePassword = null;
                }
            }            
            try {
                keyStoreType = options.getKeyStoreType() == null ? EKeyStoreType.FILE : options.getKeyStoreType();
            } catch (NoConstItemWithSuchValueError err) {
                keyStoreType = EKeyStoreType.FILE;
            }

            if (options.getCertAlias() != null && !options.getCertAlias().isEmpty()) {
                certificateAlias = options.getCertAlias();
            } else {
                certificateAlias = CERTIFICATE_ALIAS;
            }
        }

        public char[] getTrustStorePassword() {
            return trustStorePassword;//NOPMD
        }

        public String getTrustStoreFilePath() {
            return trustStoreFilePath;
        }

        public boolean isTrustStorePathRelative() {
            return trustStorePathIsRelative;
        }

        public boolean useSSLAuth() {
            return useSSLAuth;
        }

        public EKeyStoreType getKeyStoreType() {
            return useSSLAuth ? keyStoreType : EKeyStoreType.FILE;
        }

        public void setKeyStoreType(EKeyStoreType kyeStoreType) {
            this.keyStoreType = kyeStoreType;
        }

        public void setUseSSLAuth(boolean useSSLAuth) {
            this.useSSLAuth = useSSLAuth;
        }

        public void setCertificateAlias(final String certificateAlias) {
            this.certificateAlias = certificateAlias;
        }

        public void setPkcs11Lib(final String libPath) {
            if (libPath == null) {
                throw new IllegalArgumentException("Library path must not be null");
            }
            if (libPath.length() == 0) {
                throw new IllegalArgumentException("Library path must not be empty");
            }
            this.pkcs11Lib = libPath;
        }        

        public void setSlotId(final int slotId) {
            this.slotId = slotId;
        }

        public String getCertificateAlias() {
            return certificateAlias;
        }

        public String getPkcs11Lib() {
            return pkcs11Lib;
        }

        public int getSlotId() {
            return slotId;
        }

        public boolean isAutoDetectSlotId() {
            return autoDetectSlotId;
        }

        public void setAutoDetectSlotId(boolean autoDetectSlotId) {
            this.autoDetectSlotId = autoDetectSlotId;
        }        
    }

    public final static class KerberosOptions {

        public static final String SERVICE_NAME = "HTTP/eas.radixware.org";
        private String servicePrincipalName;

        public KerberosOptions() {
            this.servicePrincipalName = getDefaultEasPN();
        }

        public KerberosOptions(final KerberosOptions copy) {
            this.servicePrincipalName = copy.servicePrincipalName;
        }

        public KerberosOptions(ConnectionsDocument.Connections.Connection.KerberosOptions krbOptions) {
            this.servicePrincipalName = krbOptions.getSpn();
        }

        /**
         * @return the servicePrincipalName
         */
        public String getServicePrincipalName() {
            return servicePrincipalName;
        }

        /**
         * @param servicePrincipalName the servicePrincipalName to set
         */
        public void setServicePrincipalName(String servicePrincipalName) {
            this.servicePrincipalName = servicePrincipalName;
        }

        /**
         * Returns default EAS Principle Name
         *
         * @return
         */
        public static String getDefaultEasPN() {
            final KerberosPrincipal principal;
            try {
                principal = new KerberosPrincipal(SERVICE_NAME, KerberosPrincipal.KRB_NT_SRV_INST);
            } catch (IllegalArgumentException ex) {
                return SERVICE_NAME;
            }
            return principal.getName();
        }
    }
    
    private SslOptions sslOptions;
    private KerberosOptions krbOptions;

    public SslOptions getSslOptions() {
        if (isReadOnly){
            return sslOptions == null ? null : new SslOptions(sslOptions);
        }else{
            return sslOptions;
        }
    }
    
    public final Pkcs11Config getPkcs11Config(){
        if (getSslOptions()!=null && getSslOptions().getKeyStoreType()==EKeyStoreType.PKCS11){
            final Pkcs11Config config = new Pkcs11Config(environment);
            final String configFilePath = getPkcs11ConfigFilePath();
            try{
                config.readFromFile(configFilePath);
            }catch(IOException exception){
                final FileException fileException = 
                    new FileException(getEnvironment(), FileException.EExceptionCode.CANT_READ, configFilePath, exception);
                environment.getTracer().error(fileException);
            }
            config.readFromSslOptions(getSslOptions());
            return config;
        }else{
            return null;
        }
    }
    
    public final void setPkcs11Config(final Pkcs11Config config){
        if (getSslOptions()==null || getSslOptions().getKeyStoreType()!=EKeyStoreType.PKCS11){
            throw new IllegalUsageError("Using of PKCS#11 is not configured for this connection");
        }
        final String configFilePath = getPkcs11ConfigFilePath();
        try{
            config.writeToFile(configFilePath);
        }catch(IOException exception){
            final FileException fileException = 
                new FileException(getEnvironment(), FileException.EExceptionCode.CANT_WRITE, configFilePath, exception);
            environment.getTracer().error(fileException);
        }
        config.writeToSslOptions(getSslOptions());
    }
    
    protected final String getPkcs11ConfigFilePath(){
        final StringBuilder configFilePathBuilder = new StringBuilder(environment.getWorkPath());
        configFilePathBuilder.append('/');
        configFilePathBuilder.append(getId());
        configFilePathBuilder.append(".pkcs11");
        return configFilePathBuilder.toString();
    }

    public KerberosOptions getKerberosOptions() {
        if (isReadOnly){
            return krbOptions == null ? null : new KerberosOptions(krbOptions);
        }else{
            return krbOptions;
        }
    }
    
    public final EAuthType getAuthType(){
        if (getKerberosOptions()!=null){
            return EAuthType.KERBEROS;
        }else if (getSslOptions()!=null && getSslOptions().useSSLAuth()){
            return EAuthType.CERTIFICATE;
        }else{
            return EAuthType.PASSWORD;
        }
    }

    public void setKerberosOptions(final KerberosOptions krbOptions) {
        if (isReadOnly){
            throw new UnsupportedOperationException("Failed to change connection options");
        }                
        this.krbOptions = krbOptions;
    }

    public KeystoreController getKeyStoreController() throws KeystoreControllerException {
        if (sslOptions == null) {
            return null;
        } else if (keystoreController == null) {
            keystoreController = KeystoreController.newClientInstance(
                    getEnvironment().getWorkPath(),
                    getId().toString(),
                    getSslOptions().getKeyStoreType(),
                    ConnectionOptions.KEYSTORE_EXPLORER_PUBLIC_PASSWORD);
        }
        return keystoreController;
    }

    public KeystoreController getKeyStoreController(File keystoreFile, char[] keystorePassword) throws KeystoreControllerException {
        if (sslOptions == null) {
            return null;
        } else if (keystoreController == null) {
            keystoreController = KeystoreController.newClientInstance(keystoreFile,
                    keystorePassword);
        }
        return keystoreController;
    }

    /**
     * Returns keystore controller which wraps PKCS#11 key store. Loading is
     * perfomed in a background thread with 'Please, wait...' dialog on the
     * foreground.
     *
     * @param ksPassword PKCS#11 keystore password
     * @return
     * @throws KeystoreControllerException
     */
    public KeystoreController getKeyStoreController(final char[] ksPassword) throws KeystoreControllerException {
        if (getSslOptions() == null) {
            return null;
        } else if (keystoreController == null) {
            final ITaskWaiter taskWaiter = environment.getApplication().newTaskWaiter();
            try {
                taskWaiter.setMessage(environment.getMessageProvider().translate("PKCS11", "Reading the HSM"));
                keystoreController = taskWaiter.runAndWait(new Callable<KeystoreController>() {
                    @Override
                    public KeystoreController call() throws Exception {
                        return getKeystoreControllerImpl(ksPassword);
                    }
                });
            } catch (ExecutionException | InterruptedException ex) {
                throw new Pkcs11Exception(ex);
            }finally{
                taskWaiter.close();
            }

        }
        return keystoreController;
    }
    
    private KeystoreController getKeystoreControllerImpl(final char[] ksPassword) throws KeystoreControllerException{
        final String workPath = getEnvironment().getWorkPath();
        final String connectionId = getId().toString();
        final SslOptions sslOptions = getSslOptions();        
        final EKeyStoreType keyStoreType = sslOptions.getKeyStoreType();        
        if (keyStoreType==EKeyStoreType.PKCS11 && sslOptions.isAutoDetectSlotId()){
            final Pkcs11Config pkcs11Config = getPkcs11Config();
            final String certAlias = sslOptions.getCertificateAlias();
            Integer slotIndex = Pkcs11SlotDetector.findSlotIndexByCertAlias(getEnvironment(), pkcs11Config, certAlias, ksPassword, false);
            if (slotIndex==null){
                final List<Pkcs11Token> tokens = 
                    Pkcs11Token.enumTokens(getEnvironment(), sslOptions.getPkcs11Lib(), false);
                if (tokens.isEmpty()){
                    return KeystoreController.newClientInstance(workPath,connectionId,keyStoreType,ksPassword);
                }else{
                    slotIndex = Integer.valueOf((int)tokens.get(0).getSlotId());
                }
            }
            if (slotIndex.intValue()!=sslOptions.getSlotId()){
                pkcs11Config.setFieldValue(Pkcs11Config.Field.SLOTLI, slotIndex.toString());
                setPkcs11Config(pkcs11Config);
            }
        }
        return KeystoreController.newClientInstance(workPath,connectionId,keyStoreType,ksPassword);
    }
    
    private final boolean local;

    public boolean isLocal() {
        return local;
    }
    
    public boolean isReadOnly(){
        return isReadOnly;
    }
    
    private final IClientEnvironment environment;

    public ConnectionOptions(final IClientEnvironment environment, final String connectionName) {
        this.environment = environment;
        id = Id.Factory.newInstance(EDefinitionIdPrefix.EXPLORER_CONNECTION_OPTIONS);
        name = connectionName;
        language = environment.getLanguage();
        country = environment.getCountry();
        local = true;
        isReadOnly = false;
    }
    
    protected ConnectionOptions(final IClientEnvironment environment, final ConnectionOptions source, final String connectionName, final boolean isReadOnly) {
        this.environment = environment;
        id = Id.Factory.newInstance(EDefinitionIdPrefix.EXPLORER_CONNECTION_OPTIONS);
        name = connectionName == null ? source.name : connectionName;
        initialAddressesAsStr = source.initialAddressesAsStr;
        initialAddresses.addAll(source.initialAddresses);
        comment = source.comment;
        userName = source.userName;
        stationName = source.stationName;
        language = source.language;
        country = source.country;
        explorerRootId = source.explorerRootId;
        eventSeverity = source.eventSeverity;
        if (source.sslOptions != null) {
            sslOptions = new SslOptions(source.sslOptions);
        }
        if (source.krbOptions != null) {
            krbOptions = new KerberosOptions(source.krbOptions);
        }
        local = true;
        explorerRoots=source.explorerRoots;
        this.isReadOnly = isReadOnly;
    }

    public ConnectionOptions(final IClientEnvironment environment, final ConnectionOptions source, final String connectionName) {
        this(environment,source,connectionName,false);
    }

    public ConnectionOptions(final IClientEnvironment environment, ConnectionsDocument.Connections.Connection connection, boolean isLocal) {
        this.environment = environment;
        id = connection.getId() != null ? connection.getId() : Id.Factory.newInstance(EDefinitionIdPrefix.EXPLORER_CONNECTION_OPTIONS);
        name = connection.getName();
        comment = connection.getComment();
        userName = connection.getUserName();
        stationName = connection.getStationName();
        explorerRootId = Id.Factory.loadFrom(connection.getExplorerRootId());
        initialAddressesAsStr = connection.getInitialAddress();
        initialAddresses = parseAddresses(initialAddressesAsStr);


        try {
            language = connection.getLanguage();
        } catch (NoConstItemWithSuchValueError error) {
            language = EIsoLanguage.ENGLISH;
        }

        if (language == null) {
            language = EIsoLanguage.ENGLISH;
        }

        try {
            country = connection.getCountry();
        } catch (NoConstItemWithSuchValueError error) {
            country = null;
        }

        if (country == null) {
            country = LocaleManager.getDefaultCountry(language);
        }

        if (connection.getTraceLevel() != null) {
            eventSeverity = connection.getTraceLevel();
        }

        if (connection.getSSLOptions() != null) {
            sslOptions = new SslOptions(connection.getSSLOptions());
        }

        if (connection.getKerberosOptions() != null) {
            krbOptions = new KerberosOptions(connection.getKerberosOptions());
        }

        local = isLocal;
        isReadOnly = false;
        restoreSettings();
    }

    protected IClientEnvironment getEnvironment() {
        return environment;
    }

    protected static List<InetSocketAddress> parseAddresses(final String addressesStr) {
        final List<InetSocketAddress> addresses = new ArrayList<>();
        if (addressesStr == null) {
            return addresses;
        }
        final String[] arrInitialAddress = addressesStr.split(";");
        for (String initialAddress : arrInitialAddress) {
            if (!initialAddress.trim().isEmpty()) {
                try {
                    addresses.add(ValueFormatter.parseInetSocketAddress(initialAddress.trim()));
                } catch (IllegalArgumentException e) {
                    continue;
                }
            }
        }
        return addresses;
    }

    public void writeToXml(ConnectionsDocument.Connections.Connection connection) {
        connection.setId(id);
        connection.setName(name);
        connection.setComment(comment);
        connection.setUserName(userName);
        connection.setStationName(stationName);
        connection.setInitialAddress(initialAddressesAsStr);
        connection.setLanguage(language);
        connection.setCountry(country);
        connection.setExplorerRootId(explorerRootId == null ? null : explorerRootId.toString());
        connection.setTraceLevel(eventSeverity);

        if (sslOptions != null) {
            final ConnectionsDocument.Connections.Connection.SSLOptions options = connection.addNewSSLOptions();
            options.setUseSslAuth(sslOptions.useSSLAuth());
            options.setKeyStoreType(sslOptions.getKeyStoreType());
            options.setCertAlias(sslOptions.getCertificateAlias());
            options.setSlotIndex(sslOptions.getSlotId());
            options.setPkcs11Lib(sslOptions.getPkcs11Lib());
            options.setAutoDetectSlotIndex(sslOptions.isAutoDetectSlotId());
        }

        if (krbOptions != null) {
            final ConnectionsDocument.Connections.Connection.KerberosOptions options = connection.addNewKerberosOptions();
            options.setSpn(krbOptions.getServicePrincipalName());
        }
    }

    private void restoreSettings() {
        final ClientSettings settings = environment.getConfigStore();
        final int connectionsCount = settings.beginReadArray(Connections.SETTING_NAME);
        for (int i = 0; i < connectionsCount; i++) {
            settings.setArrayIndex(i);
            if (name.equals(settings.value("name"))) {
                explorerRoots.clear();
                explorerRoots.addAll(ExplorerRoot.loadFromConfig(settings, environment));
                break;
            }
        }
        settings.endArray();
    }
    
    private List<ExplorerRoot> explorerRoots = new LinkedList<>();

    public List<ExplorerRoot> getExplorerRoots() {
        return Collections.unmodifiableList(explorerRoots);
    }

    protected List<ExplorerRoot> updateExplorerRoots() {        
        List<ExplorerRoot> orderedRoots = 
            orderedExplorerRoots(explorerRoots, ExplorerRoot.loadFromDefManager(environment));
        explorerRoots.clear();
        explorerRoots.addAll(orderedRoots);
        return Collections.unmodifiableList(explorerRoots);
    }

    public Id getExplorerRootId(List<ExplorerRoot> serverRoots) {
        if (serverRoots == null) {
            return explorerRootId;
        }
        //Необходимо обновлять в любом случае т.к. могли измениться заголовки
        List<ExplorerRoot> orderedRoots = orderedExplorerRoots(explorerRoots, serverRoots);
        explorerRoots.clear();
        explorerRoots.addAll(orderedRoots);

        //Если список пуст - корней нет
        if (explorerRoots.isEmpty()) {
            return null;
        }

        //если содержит один элемент - его и возвращаем
        if (explorerRoots.size() == 1) {
            return explorerRoots.get(0).getId();
        }

        if (explorerRootId != null) {//если есть в настройках
            for (ExplorerRoot explorerRoot : explorerRoots) {
                if (explorerRootId.equals(explorerRoot.getId())) {//и в списке серверных,
                    return explorerRootId;//то возвращаем его
                }
            }
        }

        //иначе - запускаем диалог выбора
        return selectExplorerRootId();
    }

    private static List<ExplorerRoot> orderedExplorerRoots(final List<ExplorerRoot> current, final List<ExplorerRoot> actual) {
        final List<ExplorerRoot> result = new ArrayList<>();
        //first add roots from current
        int index;
        for (ExplorerRoot item : current) {
            index = actual.indexOf(item);
            if (index > -1) {
                result.add(actual.get(index));
            }
        }
        //then add other roots
        for (ExplorerRoot item : actual) {
            if (!result.contains(item)) {
                result.add(item);
            }
        }
        return result;
    }

    protected abstract int showSelectRootDialog(IClientEnvironment env, List<ExplorerRoot> roots, int currentSelection);

    public Id selectExplorerRootId() {
        final int explorerRootIdx = showSelectRootDialog(environment, Collections.unmodifiableList(explorerRoots), 0);
        if (explorerRootIdx > -1) {
            explorerRoots.add(0, explorerRoots.remove(explorerRootIdx));
            return explorerRoots.get(0).getId();
        } else {
            return null;
        }
    }

    public abstract boolean edit(final List<String> existingConnections);

    /**
     * диалог редактирования параметров подключения
     *
     */
    public void onClose() {
        if (keystoreController != null) {
            try {
                keystoreController.close();
            } catch (KeystoreControllerException ex) {
                getEnvironment().getTracer().error(ex);
            } finally {
                keystoreController = null;
            }
        }
    }

    public X509Certificate getCertificate(char[] keystorePassword) throws KeystoreControllerException, FileException {
        if (getSslOptions().getKeyStoreType() == EKeyStoreType.FILE) {
            final String fileName = getKeyStoreController().getKeyStoreName();
            final File certificateFile = new File(fileName);
            if (!certificateFile.exists() || !certificateFile.isFile()) {
                //Возможно когда для данного подключения ни разу не запускался менеджер сертификатов.
                //Считаем, что сертификата нет
                return null;
            }
            if (!certificateFile.canRead()) {
                throw new FileException(getEnvironment(), FileException.EExceptionCode.CANT_READ, fileName);
            }
            return getKeyStoreController().getCertificate(ConnectionOptions.CERTIFICATE_ALIAS);
        } else if (keystorePassword!=null && keystorePassword.length > 0) {
            final KeystoreController ksController = getKeyStoreController(keystorePassword);
            final String alias = getSslOptions().getCertificateAlias();
            return ksController.getCertificate(alias);
        }
        return null;
    }
    
    boolean deleteConfigFiles(final boolean forced){
        final Path keystoreFilePath = 
            Paths.get(getEnvironment().getWorkPath(), getId().toString()+".jceks");
        if (Files.exists(keystoreFilePath, LinkOption.NOFOLLOW_LINKS)){
            final String title = 
                getEnvironment().getMessageProvider().translate("ConnectionEditor", "Confirm to Delete Certificates");
            final String message = 
                getEnvironment().getMessageProvider().translate("ConnectionEditor", "Do you want to delete certificates for this connection?");
            final Set<EDialogButtonType> buttons;
            if (forced){
                buttons = 
                    EnumSet.of(EDialogButtonType.YES, EDialogButtonType.NO);
            }else{
                buttons = 
                    EnumSet.of(EDialogButtonType.YES, EDialogButtonType.NO, EDialogButtonType.CANCEL);                
            }
            final EDialogButtonType answer = 
                    getEnvironment().messageBox(title, message, EDialogIconType.QUESTION, buttons);
            if (answer==EDialogButtonType.CANCEL){
                return false;
            }else if (answer==EDialogButtonType.YES){
                try{
                    Files.deleteIfExists(keystoreFilePath);
                }catch(IOException exception){
                    getEnvironment().getTracer().error(new FileException(getEnvironment(), FileException.EExceptionCode.CANT_DELETE, keystoreFilePath.toString(), exception));
                }
            }
        }
        final Path pkcs11ConfigFilePath = 
                Paths.get(getEnvironment().getWorkPath(), getId().toString()+".pkcs11");
        try{
            Files.deleteIfExists(pkcs11ConfigFilePath);
        }catch(IOException exception){
                    getEnvironment().getTracer().error(new FileException(getEnvironment(), FileException.EExceptionCode.CANT_DELETE, pkcs11ConfigFilePath.toString(), exception));
        }
        return true;        
    }
    
    public abstract ConnectionOptions createUnmodifableCopy();
}
