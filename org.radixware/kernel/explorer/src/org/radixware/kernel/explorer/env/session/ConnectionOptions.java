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

package org.radixware.kernel.explorer.env.session;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QLayout.SizeConstraint;
import com.trolltech.qt.gui.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import javax.security.auth.kerberos.KerberosPrincipal;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.LocaleManager;
import org.radixware.kernel.common.client.exceptions.FileException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.types.ExplorerRoot;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.kernel.explorer.dialogs.*;
import org.radixware.kernel.explorer.dialogs.certificates.CertificateManager;
import org.radixware.kernel.explorer.editors.valeditors.ValListEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.widgets.LazyComboBox;
import org.radixware.kernel.explorer.widgets.LazyComboBox.Item;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.schemas.connections.ConnectionsDocument;

/**
 * Параметры необходимые для открытия DAS-сессии, адрес http-сервера дефиниций,
 * а также параметры доступа к ресурсам клиента.
 *
 */
public final class ConnectionOptions extends org.radixware.kernel.common.client.eas.connections.ConnectionOptions {

    public ConnectionOptions(final IClientEnvironment environment, final String connectionName) {
        super(environment, connectionName);
    }
    
    private ConnectionOptions(final IClientEnvironment environment, final org.radixware.kernel.common.client.eas.connections.ConnectionOptions source, final String connectionName, final boolean isReadOnly) {
        super(environment, source, connectionName,isReadOnly);
    }

    public ConnectionOptions(final IClientEnvironment environment, final org.radixware.kernel.common.client.eas.connections.ConnectionOptions source, final String connectionName) {
        super(environment, source, connectionName);
    }

    public ConnectionOptions(final IClientEnvironment environment, final ConnectionsDocument.Connections.Connection connection, final boolean isLocal) {
        super(environment, connection, isLocal);
    }

    @Override
    public boolean edit(final List<String> existingConnections) {
        final ConnectionEditorDialog dialog = new ConnectionEditorDialog(getEnvironment(), this, existingConnections);
        return QDialog.DialogCode.resolve(dialog.exec()) == QDialog.DialogCode.Accepted;
    }

    @Override
    protected int showSelectRootDialog(final IClientEnvironment env, final List<ExplorerRoot> explorerRoots, final int currentSelection) {
        final SelectRootDialog dialog = new SelectRootDialog(getEnvironment(), explorerRoots, 0);
        return dialog.selectedItem();
    }

    @Override
    public ConnectionOptions createUnmodifableCopy() {
        final ConnectionOptions result = new ConnectionOptions(getEnvironment(), this, null, true);
        result.id = id;
        return result;
    }
    
    private ConnectionOptions copy() {
        final ConnectionOptions result = new ConnectionOptions(getEnvironment(), this, null, false);
        result.id = id;
        return result;
    }    

    /**
     * диалог редактирования параметров подключения
     *
     */
    private static class ConnectionEditorDialog extends ExplorerDialog {
        private enum AuthType { 
            PASSWORD(0), CERTIFICATE(1), KERBEROS(2);
            
            public final int index;
            AuthType(int index) {
                this.index = index;
            }
        };
        
        private static final int FlagsRole = Qt.ItemDataRole.UserRole - 1;
        
        private final Ui_ConnectionEditor ui = new Ui_ConnectionEditor();
        private final ValStrEditor commentData;
        private final ValStrEditor addressData;
        private final ValListEditor countryData;
        private final LazyComboBox<Id> explorerRootsData;
        private final QToolButton tbEditAddresses = new QToolButton();
        private final ConnectionOptions options;        
        private final SslOptions savedSslOptions;
        private final List<String> connectionNames;

        public ConnectionEditorDialog(final IClientEnvironment environment, final ConnectionOptions connectionOptions, final List<String> existingConnections) {
            super(environment, Application.getMainWindow(), null);
            
            final EditMaskStr editMask = new EditMaskStr();
            editMask.setNoValueStr("");
            commentData = new ValStrEditor(environment, this, editMask, true, false);
            commentData.addMemo();
            addressData = new ValStrEditor(environment, this, editMask, true, false);
            countryData = new ValListEditor(environment, this, Collections.<EditMaskList.Item>emptyList());
            countryData.setMandatory(false);
            options = connectionOptions;
            connectionNames = existingConnections;            
            setupUi();
            explorerRootsData = initExplorerRootsEditor();
            initTabOrder();
            savedSslOptions = options.getSslOptions()==null ? null : new SslOptions(options.getSslOptions());
            final ConnectionOptions optionsCopy = //To get saved user name instead of connected user name
                new ConnectionOptions(environment, options, options.getName());
            readOptions(optionsCopy);
        }

        private void setupUi() {
            addButton(EDialogButtonType.OK);
            addButton(EDialogButtonType.CANCEL);
            ui.setupUi(this);
            setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Connection.CONNECT));
            ui.optionsLayout.addWidget(addressData, 1, 1, 1, 1);
            ui.initialAddressLabel.setBuddy(addressData);
            tbEditAddresses.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.EDIT));
            tbEditAddresses.clicked.connect(this, "onEditAddresses()");
            addressData.addButton(tbEditAddresses);
                        
            ui.optionsLayout.addWidget(countryData, 6, 1, 1, 1);
            ui.countryLabel.setBuddy(countryData);
            
            ui.optionsLayout.addWidget(commentData, 9, 1, 1, 1);
            ui.commentLabel.setBuddy(commentData);                        
            
            ((QHBoxLayout) ui.manifestWidget.layout()).setSpacing(2);
            ((QVBoxLayout) layout()).insertWidget(0, ui.tabWidget);
            ((QVBoxLayout) layout()).insertWidget(0, ui.connectionNameWidget);
            setCenterButtons(true);
            acceptButtonClick.connect(this, "onAccept()");
            rejectButtonClick.connect(this, "onReject()");

            int currentLng = 0;
            final List<EIsoLanguage> supportedLanguages = getEnvironment().getSupportedLanguages();
            for (EIsoLanguage lng : supportedLanguages) {
                ui.languageData.addItem(lng.getName(), lng);
                if (lng == options.getLanguage()) {
                    ui.languageData.setCurrentIndex(currentLng);
                }
                currentLng++;
            }
            
            ui.languageData.activatedIndex.connect(this, "onLanguageChanged()");
            onLanguageChanged();            
                                    
            ui.pbCertificateManager.setIcon(ExplorerIcon.getQIcon(ClientIcon.Connection.CERTIFICATE_MANAGER));
            onKeyStoreTypeChanged();

            ui.rbFileKeyStore.toggled.connect(this, "onKeyStoreTypeChanged()");
            ui.rbPKCS11KeyStore.toggled.connect(this, "onKeyStoreTypeChanged()");
            ui.pbCertificateManager.clicked.connect(this, "onCertificatesClick()");
            ui.pbPkcs11Config.clicked.connect(this, "onPkcs11ConfigClick()");           
            
            final MessageProvider msgProvider = getEnvironment().getMessageProvider();
            ui.cmbAuthType.addItem(msgProvider.translate("ConnectionEditor", "Password"), AuthType.PASSWORD);
            ui.cmbAuthType.addItem(msgProvider.translate("ConnectionEditor", "Certificate"), AuthType.CERTIFICATE);
            ui.cmbAuthType.addItem(msgProvider.translate("ConnectionEditor", "Kerberos"), AuthType.KERBEROS);
            ui.cmbAuthType.currentIndexChanged.connect(this, "onAuthTypeChanged(Integer)");
            ui.sslGroupBox.toggled.connect(this, "setCertAuthEnabled(boolean)");
            setCertAuthEnabled(ui.sslGroupBox.isChecked());
            
            dialogLayout().setSizeConstraint(SizeConstraint.SetFixedSize);
            ui.connectionNameData.setFocus();
        }
        
        private LazyComboBox<Id> initExplorerRootsEditor(){
            final LazyComboBox<Id> lazyComboBox;
            if (RadixLoader.getInstance()==null){
                final LazyComboBox.ItemsProvider<Id> itemsProvider = new LazyComboBox.ItemsProvider<Id>(){

                    @Override
                    public List<Item<Id>> getItems() {
                        return Collections.<Item<Id>>emptyList();
                    }                    
                };
                lazyComboBox = 
                    new LazyComboBox<>(itemsProvider,null,false,getEnvironment().getDefManager(),this);
                lazyComboBox.setEnabled(false);                
            }else{
                final String noExplorerRootTitle = 
                    getEnvironment().getMessageProvider().translate("ConnectionEditor", "<Select later>");
                final LazyComboBox.ItemsProvider<Id> itemsProvider = new LazyComboBox.ItemsProvider<Id>(){
                    @Override
                    public List<LazyComboBox.Item<Id>> getItems() {
                        final List<LazyComboBox.Item<Id>> items = new LinkedList<>();
                        items.add(new LazyComboBox.Item<Id>(noExplorerRootTitle,(ClientIcon)null,null));
                        final List<ExplorerRoot> explorerRoots = options.updateExplorerRoots();
                        for (ExplorerRoot explorerRoot: explorerRoots){                            
                            if (explorerRoot.getIconId()==null){
                                items.add(new LazyComboBox.Item<>(explorerRoot.getTitle(), ClientIcon.Definitions.TREE, explorerRoot.getId()));
                            }else{
                                items.add(new LazyComboBox.Item<>(explorerRoot.getTitle(), explorerRoot.getIconId(), explorerRoot.getId()));
                            }
                        }
                        return items;
                    }
                };
                final LazyComboBox.Item<Id> initialItem;
                if (options.getExplorerRootId(null)==null){
                    initialItem = new LazyComboBox.Item<>(noExplorerRootTitle,(ClientIcon)null,null);                        
                }else{
                    initialItem = 
                        new LazyComboBox.Item<>(getEnvironment().getMessageProvider().translate("ConnectionEditor", "<Loading...>"),(ClientIcon)null,null);
                }
                lazyComboBox = new LazyComboBox<>(itemsProvider,initialItem,true,getEnvironment().getDefManager(),this);
                lazyComboBox.lineEdit().setStyleSheet("color: "+QColor.gray.name()+";");
                final QFont font = new QFont(lazyComboBox.font());
                font.setItalic(true);
                lazyComboBox.lineEdit().setFont(font);
            }
            
            ui.optionsLayout.addWidget(lazyComboBox, 8, 1, 1, 1);            
            ui.explorerRootLabel.setBuddy(lazyComboBox);
            ui.explorerRootLabel.setEnabled(lazyComboBox.isEnabled());
            final String waitingText = 
                getEnvironment().getMessageProvider().translate("Wait Dialog", "Please Wait...");
            lazyComboBox.setWaitingText(waitingText);
            lazyComboBox.loadingFinished.connect(this,"onExplorerRootsLoaded()");
            lazyComboBox.currentIndexChanged.connect(this,"onExplorerRootChanged()");
            return lazyComboBox;
        }
        
        private void initTabOrder(){
            QWidget.setTabOrder(ui.userNameData, addressData);
            QWidget.setTabOrder(addressData, ui.stationNameData);
            QWidget.setTabOrder(ui.languageData, countryData);
            QWidget.setTabOrder(countryData, ui.eventSeverityData);
            QWidget.setTabOrder(explorerRootsData, commentData);
            QWidget.setTabOrder(commentData, ui.tabWidget);            
        }
        
        private void onLanguageChanged(){
            final int languageIndex = ui.languageData.currentIndex();
            final EditMaskList editMask = new EditMaskList();
            String currentCountry = null;
            editMask.setNoValueStr(getEnvironment().getMessageProvider().translate("ConnectionEditor", "<default>"));
            if (languageIndex>-1){
                final EIsoLanguage language = (EIsoLanguage)ui.languageData.itemData(languageIndex);                
                final EIsoCountry defaultCountry = LocaleManager.getDefaultCountry(language);
                currentCountry = defaultCountry==null ? null : defaultCountry.getValue();
                final EnumSet<EIsoCountry> countries = LocaleManager.getCountriesForLanguage(language);                                
                for (EIsoCountry country: countries){
                     editMask.addItem(country.getName(), country.getValue());                     
                     if (country==options.getCountry()){
                         currentCountry = country.getValue();
                     }
                }
            }
            countryData.setEditMask(editMask);            
            countryData.setValue(currentCountry);
        }
        
        @SuppressWarnings("unused")
        private void onExplorerRootsLoaded(){
            if (!explorerRootsData.isPopupActive()){
                final Id explorerRootId = options.getExplorerRootId(null);
                if (explorerRootId!=null){
                    for (int i=1, count=explorerRootsData.count(); i<count; i++){
                        if (explorerRootId.equals(explorerRootsData.getItemValue(i))){
                            explorerRootsData.setCurrentIndex(i);
                            break;
                        }
                    }
                }
            }
        }
        
        @SuppressWarnings("unused")
        private void onExplorerRootChanged(){
            if (explorerRootsData.getCurrentItemValue()==null){
                explorerRootsData.lineEdit().setStyleSheet("color: "+QColor.gray.name()+";");
                final QFont font = new QFont(explorerRootsData.font());
                font.setItalic(true);
                explorerRootsData.lineEdit().setFont(font);                                
            }else{                
                explorerRootsData.lineEdit().setStyleSheet(null);
                explorerRootsData.lineEdit().setFont(null);
            }
        }

        @SuppressWarnings("unused")
        private void onEditAddresses() {
            final String[] arrAddresses = addressData.getValue() == null ? new String[0] : addressData.getValue().split(";");
            ArrStr array = new ArrStr();
            for (String address : arrAddresses) {
                if (!address.trim().isEmpty()) {
                    array.add(address.trim());
                }
            }
            final ArrayEditorDialog dialog = new ArrayEditorDialog(getEnvironment(), EValType.ARR_STR, ArrStr.class, false, this);
            dialog.setMandatory(true);
            dialog.setItemMandatory(true);
            dialog.setWindowTitle(getEnvironment().getMessageProvider().translate("ConnectionEditor", "Servers List"));
            dialog.setCurrentValue(array);
            if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
                array = (ArrStr) dialog.getCurrentValue();
                final StringBuilder addressDataBuilder = new StringBuilder();
                if (array != null) {
                    for (String address : array) {
                        if (!address.isEmpty()) {
                            if (addressDataBuilder.length() != 0) {
                                addressDataBuilder.append("; ");
                            }
                            addressDataBuilder.append(address);
                        }
                    }
                    addressData.setValue(addressDataBuilder.toString());
                }
            }
        }

        private EKeyStoreType getCurrentKeyStoreType() {
            return  ui.rbFileKeyStore.isChecked() ? EKeyStoreType.FILE : EKeyStoreType.PKCS11;
        }

        @SuppressWarnings("unused")
        private void onKeyStoreTypeChanged() {
            final EKeyStoreType ksType = getCurrentKeyStoreType();
            if(ksType == EKeyStoreType.FILE) {
                ui.pbCertificateManager.setEnabled(true);
                ui.pbPkcs11Config.setEnabled(false);
            } else {
                ui.pbCertificateManager.setEnabled(true);
                try {
                    Class.forName("sun.security.pkcs11.SunPKCS11");
                    ui.pbPkcs11Config.setEnabled(true);
                } catch (ClassNotFoundException ex) {
                    ui.pbPkcs11Config.setEnabled(false);
                } 
            }
        }        

        @SuppressWarnings("unused")
        private void onCertificatesClick() {
            List<InetSocketAddress> addresses = addressData.getValue() == null ? null : parseAddresses(addressData.getValue());         
            ConnectionOptions currentOptions = options.copy();
            writeOptions(addresses, currentOptions);
            final CertificateManager dialog;
            try{
                dialog = CertificateManager.newCertificateManager(getEnvironment(), this, currentOptions);
            }catch(KeystoreControllerException ex){
                getEnvironment().processException(ex);
                return;
            }catch(InterruptedException ex){
                return;
            }
            dialog.exec();
        }
        
        @SuppressWarnings("unused")
        private void onPkcs11ConfigClick(){
            final String configFilePath = options.getPkcs11ConfigFilePath();
            final SslOptions sslOptions;
            if (options.getSslOptions()==null){
                sslOptions = new SslOptions();
            }else{
                sslOptions = new SslOptions(options.getSslOptions());                
            }
            sslOptions.setKeyStoreType(EKeyStoreType.PKCS11);
            final Pkcs11EditorDialog dialog = 
                new Pkcs11EditorDialog(getEnvironment(), this, configFilePath, sslOptions);
            if (dialog.exec()==DialogCode.Accepted.value()){
                options.setSslOptions(sslOptions);
            }
        }
        
        @SuppressWarnings("unused")
        private void onReject() {
            options.setSslOptions(savedSslOptions);
            if (savedSslOptions==null || savedSslOptions.getKeyStoreType()!=EKeyStoreType.PKCS11){
                final Path configFilePath = 
                    Paths.get(getEnvironment().getWorkPath(), options.getId().toString()+".pkcs11");
                try{
                    Files.deleteIfExists(configFilePath);
                }catch(IOException ex){                
                    getEnvironment().getTracer().error(new FileException(getEnvironment(), FileException.EExceptionCode.CANT_DELETE, configFilePath.toString(), ex));
                }
            }
            reject();
        }

        @SuppressWarnings("unused")
        private void onAccept() {
            final MessageProvider msgProvider = getEnvironment().getMessageProvider();
            if (ui.connectionNameData.text().isEmpty()) {
                getEnvironment().messageError(msgProvider.translate("ConnectionEditor", "Connection Name Was Not Specified!"),
                        msgProvider.translate("ConnectionEditor", "Please enter connection name"));
                ui.connectionNameData.setFocus();
                return;
            }

            if (addressData.getValue() == null || addressData.getValue().isEmpty()) {
                getEnvironment().messageError(msgProvider.translate("ConnectionEditor", "Server Was Not Specified!"),
                        msgProvider.translate("ConnectionEditor", "Please enter valid server address"));
                addressData.setFocus();
                return;
            }

            final List<InetSocketAddress> addresses = new ArrayList<>();
            final String[] arrAddresses = addressData.getValue().split(";");
            for (String address : arrAddresses) {
                if (!address.trim().isEmpty()) {
                    try {
                        addresses.add(ValueFormatter.parseInetSocketAddress(address.trim()));
                    } catch (IllegalArgumentException ex) {
                        if (arrAddresses.length == 1) {
                            getEnvironment().messageError(msgProvider.translate("ConnectionEditor", "Server Address Is Invalid!"),
                                    msgProvider.translate("ConnectionEditor", "Please enter valid server address"));
                            addressData.setFocus();
                            return;
                        } else {
                            final String message = msgProvider.translate("ConnectionEditor", "Server Address '%s' Is Invalid!\nDo you want to continue?");
                            final String title = msgProvider.translate("ConnectionEditor", "Confirm to Save Options");
                            if (!getEnvironment().messageConfirmation(title, String.format(message, address))) {
                                addressData.setFocus();
                                return;
                            }
                        }
                    }
                }
            }

            if (addresses.isEmpty()) {
                getEnvironment().messageError(msgProvider.translate("ConnectionEditor", "No Valid Addresses Was Specified"),
                        msgProvider.translate("ConnectionEditor", "Please enter valid server address"));
                addressData.setFocus();
                return;
            }

            if (ui.stationNameData.text().isEmpty()) {
                getEnvironment().messageError(msgProvider.translate("ConnectionEditor", "Station Name Was Not Specified!"),
                        msgProvider.translate("ConnectionEditor", "Please enter station name"));
                ui.stationNameData.setFocus();
                return;
            }

            if (connectionNames != null && connectionNames.contains(ui.connectionNameData.text())) {
                getEnvironment().messageError(msgProvider.translate("ConnectionEditor", "Invalid Connection Name"),
                        String.format(msgProvider.translate("ConnectionEditor", "Connection with name \"%s\" is already exists!"), ui.connectionNameData.text()));
                ui.connectionNameData.setFocus();
                return;
            }
            if (ui.cmbAuthType.currentIndex() == AuthType.KERBEROS.index) {
                final String spn = ui.leEasPrincipalName.text();
                if (spn.isEmpty()){
                    getEnvironment().messageError(msgProvider.translate("ConnectionEditor", "EAS Principal Name Was Not Specified!"),
                            msgProvider.translate("ConnectionEditor", "Please enter EAS principal name"));
                    ui.leEasPrincipalName.setFocus();
                    return;                    
                }
                final KerberosPrincipal krbPrinc;
                try{
                    krbPrinc = new KerberosPrincipal(spn, KerberosPrincipal.KRB_NT_SRV_INST);
                }catch(IllegalArgumentException exception){
                    final String message = msgProvider.translate("ConnectionEditor","Wrong Format of EAS Principal Name");
                    final String title = msgProvider.translate("ConnectionEditor", "Wrong Format of EAS Principal Name");
                    getEnvironment().messageError(title,message);
                    ui.tabWidget.setCurrentIndex(1);
                    ui.leEasPrincipalName.setFocus();
                    return;
                }
                for (char letter: krbPrinc.getRealm().toCharArray()){
                    if (Character.isLowerCase(letter)){
                        final String message = msgProvider.translate("ConnectionEditor","Kerberos realm name is usually in uppercase.\nDo you want to continue?");
                        if (getEnvironment().messageConfirmation(msgProvider.translate("ConnectionEditor", "Confirm to Use this Options"),message)){
                            break;
                        }else{
                            ui.tabWidget.setCurrentIndex(1);
                            ui.leEasPrincipalName.setFocus();
                            return;
                        }
                    }
                }
            }
            writeOptions(addresses, options);
            accept();
        }

        private void readOptions(final ConnectionOptions readFrom) {
            ui.connectionNameData.setText(readFrom.getName());
            ui.stationNameData.setText(readFrom.getStationName());
            addressData.setValue(readFrom.getInitialAddressesAsStr());
            ui.userNameData.setText(readFrom.getUserName());
            ui.stationNameData.setText(readFrom.getStationName());
            ui.eventSeverityData.setCurrentIndex(readFrom.getEventSeverity().getValue().intValue());
            commentData.setValue(readFrom.getComment());
            if (readFrom.getSslOptions() != null) {
                ui.sslGroupBox.setChecked(true);
                if (readFrom.getSslOptions().useSSLAuth()){
                    ui.cmbAuthType.setCurrentIndex(AuthType.CERTIFICATE.index);
                    final EKeyStoreType keyStoreType = readFrom.getSslOptions().getKeyStoreType();
                    ui.rbFileKeyStore.setChecked(keyStoreType==EKeyStoreType.FILE);
                    ui.rbPKCS11KeyStore.setChecked(keyStoreType==EKeyStoreType.PKCS11);
                }else{
                    ui.rbFileKeyStore.setChecked(true);
                    ui.rbPKCS11KeyStore.setEnabled(false);
                }
            }
            if(readFrom.getKerberosOptions() != null) {
                ui.cmbAuthType.setCurrentIndex(AuthType.KERBEROS.index);
                if (readFrom.getKerberosOptions().getServicePrincipalName()==null || 
                    readFrom.getKerberosOptions().getServicePrincipalName().isEmpty()){
                    ui.leEasPrincipalName.setText(KerberosOptions.getDefaultEasPN());
                }else{
                    ui.leEasPrincipalName.setText(readFrom.getKerberosOptions().getServicePrincipalName());
                }
                ui.leEasPrincipalName.home(false);
            }
        }

        private void writeOptions(final List<InetSocketAddress> addresses, final ConnectionOptions writeTo) {
            writeTo.setStationName(ui.stationNameData.text());
            if (addresses != null) {
                writeTo.setInitialAddressesAsStr(addressData.getValue());
                writeTo.setInitialAddresses(addresses);
            }
            writeTo.setUserName(ui.userNameData.text());
            if (!ui.stationNameData.text().isEmpty()) {
                writeTo.setStationName(ui.stationNameData.text());
            }
            writeTo.setName(ui.connectionNameData.text());

            writeTo.setEventSeverity(EEventSeverity.getForValue(Long.valueOf(ui.eventSeverityData.currentIndex())));
            writeTo.setLanguage((EIsoLanguage) ui.languageData.itemData(ui.languageData.currentIndex()));
            final String country = (String) countryData.getValue();
            writeTo.setCountry(country==null ? null : EIsoCountry.getForValue(country));            
            if (explorerRootsData.itemsLoaded()){
                writeTo.setExplorerRootId(explorerRootsData.getCurrentItemValue());
            }
            writeTo.setComment(commentData.getValue());

            if (ui.sslGroupBox.isChecked()) {
                if (writeTo.getSslOptions() == null) {
                    writeTo.setSslOptions(new SslOptions());
                }
                writeTo.getSslOptions().setUseSSLAuth(ui.cmbAuthType.currentIndex() == AuthType.CERTIFICATE.index);
                writeTo.getSslOptions().setKeyStoreType(getCurrentKeyStoreType());
            } else {
                writeTo.setSslOptions(null);
            }
            
            final AuthType selectedType = (AuthType) ui.cmbAuthType.itemData(ui.cmbAuthType.currentIndex(), Qt.ItemDataRole.UserRole);
            switch(selectedType) {
                case PASSWORD:
                    writeTo.setKerberosOptions(null);
                    if (writeTo.getSslOptions() != null){
                        writeTo.getSslOptions().setUseSSLAuth(false);
                    }
                    break;
                case CERTIFICATE:
                    writeTo.setKerberosOptions(null);
                    if (writeTo.getSslOptions() == null){
                        writeTo.setSslOptions(new SslOptions());
                        writeTo.getSslOptions().setKeyStoreType(EKeyStoreType.FILE);
                    }
                    writeTo.getSslOptions().setUseSSLAuth(true);
                    break;
                case KERBEROS:
                    writeTo.setKerberosOptions(new KerberosOptions());
                    writeTo.getKerberosOptions().setServicePrincipalName(ui.leEasPrincipalName.text());
                    if(writeTo.getSslOptions() != null){
                        writeTo.getSslOptions().setUseSSLAuth(false);
                    }        
                    break;
                default:
                    throw new IllegalArgumentException("Unknown authentication type");
            }
        }

        @Override
        public void done(int result) {
            explorerRootsData.close();
            super.done(result);
        }          
        
        @SuppressWarnings("unused")
        private void onAuthTypeChanged(final Integer index) {
            final AuthType selectedType = (AuthType) ui.cmbAuthType.itemData(index, Qt.ItemDataRole.UserRole);
            switch(selectedType) {
                case PASSWORD:
                    ui.krbGroupBox.setEnabled(false);
                    updateUserName(true);
                    ui.rbFileKeyStore.setChecked(true);
                    ui.rbPKCS11KeyStore.setChecked(false);                    
                    if (ui.sslGroupBox.isChecked()){
                        ui.rbPKCS11KeyStore.setEnabled(false);
                    }
                    break;
                case CERTIFICATE:
                    ui.krbGroupBox.setEnabled(false);
                    ui.sslGroupBox.setChecked(true);
                    updateUserName(false);
                    ui.rbPKCS11KeyStore.setEnabled(true);
                    break;
                case KERBEROS:
                    ui.krbGroupBox.setEnabled(true);
                    if (ui.leEasPrincipalName.text().isEmpty()){
                        ui.leEasPrincipalName.setText(KerberosOptions.getDefaultEasPN());
                        ui.leEasPrincipalName.home(false);
                    }
                    updateUserName(!SystemTools.isWindows);
                    ui.rbFileKeyStore.setChecked(true);
                    ui.rbPKCS11KeyStore.setChecked(false);                    
                    if (ui.sslGroupBox.isChecked()){
                        ui.rbPKCS11KeyStore.setEnabled(false);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown authentication type");
            }
        }
        
        private void updateUserName(final boolean isEnabled) {
            if (isEnabled) {
                if (!ui.userNameData.isEnabled()) {
                    ui.userNameData.setEnabled(true);
                    ui.userNameLabel.setEnabled(true);
                    ui.userNameData.setText(options.getUserName());
                }                
            } else {
                ui.userNameData.clear();
                ui.userNameData.setEnabled(false);
                ui.userNameLabel.setEnabled(false);                
            }
        }        
        
        private void setCertAuthEnabled(final boolean isEnabled) {
            if(!isEnabled && ui.cmbAuthType.currentIndex() == AuthType.CERTIFICATE.index) {
                ui.cmbAuthType.setCurrentIndex(AuthType.PASSWORD.index);
            }else if (isEnabled){
                if (ui.cmbAuthType.currentIndex() == AuthType.CERTIFICATE.index){
                    ui.rbPKCS11KeyStore.setEnabled(true);
                }else{
                    ui.rbFileKeyStore.setChecked(true);
                    ui.rbPKCS11KeyStore.setChecked(false);                    
                    ui.rbPKCS11KeyStore.setEnabled(false);
                }
            }
            
        }
    }
}
