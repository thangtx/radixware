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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QButtonGroup;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLayout.SizeConstraint;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QRadioButton;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.client.eas.connections.Pkcs11Config;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.exceptions.FileException;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.meta.mask.validators.ValidatorsFactory;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.ssl.KeystoreController;
import org.radixware.kernel.explorer.dialogs.pkcs11.*;//NOPMD
import org.radixware.kernel.explorer.editors.valeditors.ValBoolEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.progress.TaskWaiter;
import org.radixware.kernel.common.client.utils.Pkcs11Token;
import org.radixware.kernel.starter.radixloader.RadixLoader;


public class Pkcs11EditorDialog extends ExplorerDialog {
       
    private class AliasSelectorDialog extends ExplorerDialog {
        private final QButtonGroup buttons;
        private String selectedAlias = "";
        
        @SuppressWarnings("LeakingThisInConstructor")
        public AliasSelectorDialog(final IClientEnvironment env, final QWidget parent, final List<String> aliases) {
           super(env, parent);
                      
           setWindowTitle(env.getMessageProvider().translate("PKCS11", "Select alias"));
           
           buttons = new QButtonGroup();
           buttons.setExclusive(true);
           int i = 0;
           for(String a : aliases) {
               final QRadioButton choice = new QRadioButton(a, (QWidget)this);
               buttons.addButton(choice, i++);
               layout().addWidget(choice);
           }
           buttons.buttonClicked.connect(this,"buttonClicked()");
           addButtons(EnumSet.of(EDialogButtonType.OK,EDialogButtonType.CANCEL),true).get(EDialogButtonType.OK).setEnabled(false);
           layout().setAlignment(new Alignment(AlignmentFlag.AlignTop));
           layout().setSizeConstraint(SizeConstraint.SetFixedSize);
        }
        
        @SuppressWarnings("unused")
        private void buttonClicked(){
            getButton(EDialogButtonType.OK).setEnabled(true);
        }

        @Override
        public void accept() {
            final QAbstractButton checkedButton = buttons.checkedButton();
            selectedAlias = checkedButton == null ? "" : checkedButton.text();
            super.accept();
        }

        public String getSelectedAlias() {
            return selectedAlias;
        }
        
    }
    
    private final static long SLOT_MIN = 0, SLOT_MAX = 99;
    private final static String EXPANDER_STYLE = "QPushButton { background-color:transparent; border:0 ; text-align:left}";
    
    private final Map<Pkcs11Config.Field, IPkcsSetting> cfgOptionsMap = new EnumMap<>(Pkcs11Config.Field.class);
    private final String configFileName;
    private final ConnectionOptions.SslOptions sslOptions;
    private ValBoolEditor autoDetectSlotId;
    private QPushButton detailsButton;
    private QLabel slotIndexLabel;
    private Pkcs11ConfigIntOption slotIndexOption;
    private ValStrEditor certAliasEditor = null;
    private QWidget advancedSection;
    private final List<File> filesToDelete = new LinkedList<>();
    private final Pkcs11Config pkcs11Config;
    
    public Pkcs11EditorDialog(final IClientEnvironment environment, final QWidget parent, final String configFileName, final ConnectionOptions.SslOptions sslOptions) {
        super(environment, parent);
        pkcs11Config = new Pkcs11Config(environment);
        this.configFileName = configFileName;
        this.sslOptions = sslOptions;
        
        setAttribute(WidgetAttribute.WA_DeleteOnClose, true);
        setWindowTitle(environment.getMessageProvider().translate("PKCS11", "PKCS#11 Configuration"));
        setUpUi();
        
        addButtons(EnumSet.of(EDialogButtonType.OK,EDialogButtonType.CANCEL),true);
        //Trying to read configuration
        readConfig(configFileName);
    }
    
    private void setUpUi() {
        final QVBoxLayout mainLayout = dialogLayout();
        mainLayout.setAlignment(new Alignment(AlignmentFlag.AlignTop));
        mainLayout.setSizeConstraint(SizeConstraint.SetFixedSize);        
        final QGridLayout grid = new QGridLayout();
        mainLayout.addLayout(grid);
        
        //Configuration name field
        final EditMaskStr nameEditMask = new EditMaskStr();
        nameEditMask.setValidator(ValidatorsFactory.createRegExpValidator("^[a-zA-Z_]+\\w*", false));
        String fieldName = getEnvironment().getMessageProvider().translate("PKCS11", "Name");
        final QLabel nameLabel = new QLabel(fieldName + ":", this);
        final Pkcs11ConfigStrOption name = 
                new Pkcs11ConfigStrOption(getEnvironment(), this, nameEditMask, Pkcs11Config.Field.NAME);
        name.setName(fieldName);
        cfgOptionsMap.put(Pkcs11Config.Field.NAME, name);
        grid.addWidget(nameLabel, 0, 0);
        grid.addWidget(name, 0, 1);
        
        //Library path field
        fieldName = getEnvironment().getMessageProvider().translate("PKCS11", "Library path");
        final QLabel libPathLabel = new QLabel(fieldName + ":", this);
        final Pkcs11ConfigStrOption libPath = new Pkcs11ConfigStrOption(getEnvironment(), this, new EditMaskStr(), Pkcs11Config.Field.LIBPATH);
        libPath.setName(fieldName);
        libPath.setMinimumWidth(300);
        libPath.addButton(createFileDialogButton());
        cfgOptionsMap.put(Pkcs11Config.Field.LIBPATH, libPath);
        grid.addWidget(libPathLabel, 1, 0);
        grid.addWidget(libPath, 1, 1);
        
        //AutoDetect slot index check box
        fieldName = getEnvironment().getMessageProvider().translate("PKCS11", "Autodetect slot index");
        final QLabel autoDetectSlotIndexLabel = new QLabel(fieldName + ":", this);
        autoDetectSlotId = new ValBoolEditor(getEnvironment(), this);
        autoDetectSlotId.setValue(Boolean.valueOf(sslOptions.isAutoDetectSlotId()));
        autoDetectSlotId.valueChanged.connect(this,"onChangeAutoDetect()");
        grid.addWidget(autoDetectSlotIndexLabel, 2, 0);
        grid.addWidget(autoDetectSlotId, 2, 1);
               
        //Slot index field
        final EditMaskInt slotIndexMask = new EditMaskInt();
        slotIndexMask.setMinValue(SLOT_MIN);
        slotIndexMask.setMaxValue(SLOT_MAX);
        fieldName = getEnvironment().getMessageProvider().translate("PKCS11", "Slot index");
        slotIndexLabel = new QLabel(fieldName + ":", this);
        slotIndexLabel.setEnabled(!sslOptions.isAutoDetectSlotId());
        slotIndexOption =
                new Pkcs11ConfigIntOption(getEnvironment(), this, slotIndexMask, true, Pkcs11Config.Field.SLOTLI);        
        slotIndexOption.setName(fieldName);
        final QToolButton selectTokenBtn = new QToolButton(this);    // "..."-button to invoke a dialog with accessible aliases 
        selectTokenBtn.setToolButtonStyle(Qt.ToolButtonStyle.ToolButtonTextOnly);
        selectTokenBtn.setText("...");
        selectTokenBtn.clicked.connect(this, "chooseToken()");
        selectTokenBtn.setToolTip(getEnvironment().getMessageProvider().translate("PKCS11", "Select device"));
        slotIndexOption.addButton(selectTokenBtn);
        slotIndexOption.setEnabled(!sslOptions.isAutoDetectSlotId());
        cfgOptionsMap.put(Pkcs11Config.Field.SLOTLI, slotIndexOption);
        slotIndexOption.setValue(SLOT_MIN);
        grid.addWidget(slotIndexLabel, 3, 0);
        grid.addWidget(slotIndexOption, 3, 1);
                
        //Certificate alias
        fieldName = getEnvironment().getMessageProvider().translate("PKCS11", "Certificate alias");
        final QLabel certAliasLabel = new QLabel(fieldName + ":", advancedSection);
        certAliasEditor = new ValStrEditor(getEnvironment(), this);
        grid.addWidget(certAliasLabel, 4, 0);
        grid.addWidget(certAliasEditor, 4, 1);
        final String aliasValue = sslOptions.getCertificateAlias();
        certAliasEditor.setValue(aliasValue == null || aliasValue.isEmpty() ? "" : aliasValue);
        final QToolButton selectAliasesBtn = new QToolButton(this);    // "..."-button to invoke a dialog with accessible aliases 
        selectAliasesBtn.setToolButtonStyle(Qt.ToolButtonStyle.ToolButtonTextOnly);
        selectAliasesBtn.setText("...");
        selectAliasesBtn.clicked.connect(this, "chooseCertAlias()");
        selectAliasesBtn.setToolTip(getEnvironment().getMessageProvider().translate("PKCS11", "Login to a token to load aliases"));
        certAliasEditor.addButton(selectAliasesBtn);
        
        //Description field 
        final QLabel descLabel = 
                new QLabel(getEnvironment().getMessageProvider().translate("PKCS11", "Description:"), this);
        final Pkcs11ConfigStrOption description =
                new Pkcs11ConfigStrOption(getEnvironment(), this, new EditMaskStr(), Pkcs11Config.Field.DESCRIPTION);
        cfgOptionsMap.put(Pkcs11Config.Field.DESCRIPTION, description);
        grid.addWidget(descLabel, 5, 0);
        grid.addWidget(description, 5, 1);
        
        //Expand button
        detailsButton = new QPushButton(getEnvironment().getMessageProvider().translate("PKCS11", "Details"), this);
        detailsButton.setStyleSheet(EXPANDER_STYLE);
        detailsButton.setCheckable(true);
        detailsButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.BUTTON_ARRIGHT));
        grid.addWidget(detailsButton, 6, 0);
        
        advancedSection = buildAdvancedSection();
        mainLayout.addWidget(advancedSection);
        advancedSection.hide();
        detailsButton.toggled.connect(this, "onExpand(boolean)");
    }
    
    private QWidget buildAdvancedSection() {
        final QWidget widget = new QWidget(this);
        final QVBoxLayout layout = new QVBoxLayout();
        layout.setMargin(0);
        
        //Mechanisms
        final Pkcs11MechanismsWidget mechanisms = new Pkcs11MechanismsWidget(getEnvironment(), widget);
        cfgOptionsMap.put(Pkcs11Config.Field.DISABLED_MECH, mechanisms);
        layout.addWidget(mechanisms);
        
        //Advanced
        final Pkcs11AdvancedConfig advanced = new Pkcs11AdvancedConfig(getEnvironment(), widget);
        cfgOptionsMap.put(Pkcs11Config.Field.ADVANCED, advanced);
        layout.addWidget(advanced);
        
        widget.setLayout(layout);
        return widget;
    }
    
    @SuppressWarnings("unused")
    private void onExpand(final boolean flag) {
        if(flag) {
            advancedSection.setVisible(flag);
            detailsButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.BUTTON_ARDOWN));
        } else {
            advancedSection.setVisible(flag);
            detailsButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.BUTTON_ARRIGHT));
        }
    }
    
    @SuppressWarnings("unused")
    private void onChangeAutoDetect(){
        final boolean isAutoDetect = autoDetectSlotId.getValue()==Boolean.TRUE;
        slotIndexLabel.setEnabled(!isAutoDetect);
        slotIndexOption.setEnabled(!isAutoDetect);
    }
    
    private void readConfig(final String configFilePath){
        try{
            pkcs11Config.readFromFile(configFileName);
        }catch(IOException exception){
            final FileException fileException = 
                new FileException(getEnvironment(), FileException.EExceptionCode.CANT_READ, configFilePath, exception);
            getEnvironment().getTracer().error(fileException);
        }
        pkcs11Config.readFromSslOptions(sslOptions);
        for(IPkcsSetting s : cfgOptionsMap.values()) {
            s.readConfiguration(pkcs11Config);
        }        
    }
    
    private void writeConfig(final String configFilePath) throws IOException {
        for(IPkcsSetting s : cfgOptionsMap.values()) {
            s.writeConfiguration(pkcs11Config);
        }
        try{
            pkcs11Config.writeToFile(configFilePath);
        }catch(IOException exception){
            final FileException fileException = 
                new FileException(getEnvironment(), FileException.EExceptionCode.CANT_WRITE, configFilePath, exception);
            getEnvironment().getTracer().error(fileException);
        }
        pkcs11Config.writeToSslOptions(sslOptions);
        sslOptions.setCertificateAlias(certAliasEditor.getValue());
        sslOptions.setAutoDetectSlotId(autoDetectSlotId.getValue()==Boolean.TRUE);
    }
    
    private void testPKCS11() throws Pkcs11DeviceTester.Pkcs11DeviceTestException, org.radixware.kernel.common.client.exceptions.Pkcs11Exception, InterruptedException {
        final EnterPasswordDialog pwdDialog = new EnterPasswordDialog(getEnvironment(), null);
        pwdDialog.setMessage(getEnvironment().getMessageProvider().translate("PKCS11", "Enter password for HSM access:"));
        final char[] password;
        if(pwdDialog.execDialog() == DialogResult.ACCEPTED) {
            password = pwdDialog.getPassword().toCharArray();
        } else {
            throw new InterruptedException();
        }        
        final boolean autoDetectSlot = autoDetectSlotId.getValue()==Boolean.TRUE;
        final Pkcs11DeviceTester test = 
            new Pkcs11DeviceTester(getEnvironment(),
                                   pkcs11Config,
                                   configFileName, 
                                   certAliasEditor.getValue(), 
                                   password,
                                   autoDetectSlot
                                   );
        final TaskWaiter waiter = new TaskWaiter(getEnvironment(),this);
        try {
            waiter.setMessage(getEnvironment().getMessageProvider().translate("PKCS11", "PKCS#11 device test"));
            waiter.setCanBeCanceled(false);        
            waiter.runAndWait(test);
        } catch (ExecutionException ex) {
            final Throwable causeException = ex.getCause();
            if (causeException.getCause()==null){
                throw new org.radixware.kernel.common.client.exceptions.Pkcs11Exception(causeException.getMessage(),null);//NOPMD
            }else{
                throw new org.radixware.kernel.common.client.exceptions.Pkcs11Exception(causeException.getCause());//NOPMD
            }
        }finally{
            Arrays.fill(password, ' ');
            waiter.close();
        }
    }

    @Override
    public void accept() {        
        //Check required fields for emptyness
        for(Map.Entry<Pkcs11Config.Field,IPkcsSetting> entry : cfgOptionsMap.entrySet()) {
            if (entry.getKey().isRequired && entry.getValue().isEmpty()) {
                final String message = getEnvironment().getMessageProvider().translate("PKCS11","%s field is empty.");
                getEnvironment().messageError(String.format(message, entry.getValue().getName()));
                ((QWidget)entry.getValue()).setFocus();
                return;
            }
        }
        // Check if selected library file exists
        if(!libraryExists()) {
            return;
        }
        //Write configuration
        try {
            writeConfig(configFileName);
        } catch (FileNotFoundException ex) {
            getEnvironment().messageError(ex.getLocalizedMessage());
            return;
        } catch (IOException ex) {
            getEnvironment().messageError(ex.getLocalizedMessage());
            return;
        }
        
        final String testTitle = getEnvironment().getMessageProvider().translate("PKCS11", "PKCS#11 device test");
        final String message = getEnvironment().getMessageProvider().translate("PKCS11","Do you want to test configuration?");
        if (getEnvironment().messageConfirmation(testTitle, message)){
            try{
                testPKCS11();
            }catch(Pkcs11DeviceTester.Pkcs11DeviceTestException exception){
                final String title =getEnvironment().getMessageProvider().translate("PKCS11", "Errors in the configuration");
                final String proceedMessage = getEnvironment().getMessageProvider().translate("PKCS11", "Proceed?");
                String mainMessage = exception.getLocalizedMessage();
                mainMessage = mainMessage.substring(mainMessage.indexOf(": ") + 1);            
                if (!getEnvironment().messageConfirmation(title, mainMessage + ". " + proceedMessage)) {
                    return;
                }            
            }catch(org.radixware.kernel.common.client.exceptions.Pkcs11Exception exception){
                getEnvironment().processException(exception);
                return;
            }catch(InterruptedException exception){
                return;
            }            
        }
        super.accept();
    }
    
    private QToolButton createFileDialogButton() {
        final QToolButton button = new QToolButton(this);
        button.setIcon(ExplorerIcon.getQIcon(ClientIcon.Editor.OPEN));
        final QFileDialog fileDialog = new QFileDialog(this);
        fileDialog.setFileMode(QFileDialog.FileMode.ExistingFile);
        fileDialog.setModal(true);
        
        button.clicked.connect(fileDialog, "show()");
        fileDialog.fileSelected.connect(this, "onFileSelect(String)");
                
        return button;
    }
    
    @SuppressWarnings("unused")
    private void onFileSelect(final String fileName) {
        ((ValStrEditor)cfgOptionsMap.get(Pkcs11Config.Field.LIBPATH)).setValue(fileName);
    }
    
    private boolean libraryExists() {
        final ValStrEditor libPathEditor = (ValStrEditor) cfgOptionsMap.get(Pkcs11Config.Field.LIBPATH);
        final String fileName = libPathEditor.getValue();
        final File existenceChecker = new File(fileName);
        if(!existenceChecker.exists()) {
            final String message = getEnvironment().getMessageProvider().translate("PKCS11", "Selected file does not exist. Proceed?");
            final String title = getEnvironment().getMessageProvider().translate("PKCS11", "Confirm selection");
            final boolean choice = getEnvironment().messageConfirmation(title, message);
            if(!choice) {
                libPathEditor.setFocus();
                return false;
            }
        } else if(existenceChecker.isDirectory()) {
            final String message = getEnvironment().getMessageProvider().translate("PKCS11", "You have selected a directory, not a file. Proceed?");
            final String title = getEnvironment().getMessageProvider().translate("PKCS11", "Confirm selection");
            final boolean choice = getEnvironment().messageConfirmation(title, message);
            if(!choice) {
                libPathEditor.setFocus();
                return false;
            }
        }
        return true;
    }
    
    @SuppressWarnings("unused")
    private void chooseToken(){
        final IClientEnvironment env = getEnvironment();
        final Pkcs11Token token = 
            SelectPkcs11TokenDialog.selectToken(env, (String)cfgOptionsMap.get(Pkcs11Config.Field.LIBPATH).getValue(), this, true);
        if (token!=null){
            slotIndexOption.setValue(token.getSlotId());
        }
    }
    
    @SuppressWarnings("unused")
    private void chooseCertAlias() {
        final IClientEnvironment env = getEnvironment();
        if (autoDetectSlotId.getValue()==Boolean.TRUE){
            final Pkcs11Token token = 
                SelectPkcs11TokenDialog.selectToken(env, (String)cfgOptionsMap.get(Pkcs11Config.Field.LIBPATH).getValue(), this);
            if (token==null){
                return;
            }else{
                slotIndexOption.setValue(token.getSlotId());
            }
        }
        char[] password;
        final EnterPasswordDialog pwdDlg = new EnterPasswordDialog(env, this);
        pwdDlg.setMessage(env.getMessageProvider().translate("PKCS11", "Enter password for HSM access:"));
        if(pwdDlg.execDialog() == DialogResult.ACCEPTED) {
            password = pwdDlg.getPassword().toCharArray();
        } else {
            return;
        }        
        if (password == null || password.length == 0) {
            env.messageError(env.getMessageProvider().translate("PKCS11", "Empty password."));
            return;
        }

        final TaskWaiter taskWaiter = new TaskWaiter(getEnvironment(),this);
        try {            
            final File tmpConfigFile;
            if (RadixLoader.getInstance()==null){
                final File tmpDir = new File(getEnvironment().getWorkPath());
                tmpConfigFile = File.createTempFile("pkcs11", ".cfg", tmpDir);                
            }else{
                tmpConfigFile = RadixLoader.getInstance().createTempFile("pkcs11_cfg_");
            }
            final String tmpConfigFilePath = tmpConfigFile.getAbsolutePath();
            try{
                writeConfig(tmpConfigFilePath);
                final String[] aliases = taskWaiter.runAndWait(getAliasesCaller(password, tmpConfigFilePath));
                if (aliases == null) {
                    getEnvironment().messageWarning(env.getMessageProvider().translate("PKCS11", "No certificates with RSA key were found."));
                } else {                        
                    final AliasSelectorDialog dlg = new AliasSelectorDialog(env, this, Arrays.asList(aliases));
                    if(dlg.execDialog() == DialogResult.ACCEPTED) {
                        certAliasEditor.setValue(dlg.getSelectedAlias());
                    }
                }
            }finally{
                Arrays.fill(password, ' ');
                if (!tmpConfigFile.delete() || tmpConfigFile.exists()){
                    filesToDelete.add(tmpConfigFile);
                }
            }
        } catch (IOException ex) { 
            env.messageError(env.getMessageProvider().translate("PKCS11", "Configuration file could not be read."));
        } catch (ExecutionException ex) {
            getEnvironment().processException(new org.radixware.kernel.common.client.exceptions.Pkcs11Exception(ex));
        }catch(InterruptedException ex){
            //do nothing
        }finally{
            taskWaiter.close();
        }            
    }
    
    private String[] getSelectedAlias(final char[] password, final String configPath) throws KeystoreControllerException {
        KeystoreController kc = null;
        String[] aliases = null;
        
        try {
            kc = KeystoreController.newTempInstance(configPath, password);
            aliases = kc.getRsaKeyAliases();
        } finally {
            try {
                if(kc != null) {
                    kc.close();
                }                
            } catch (KeystoreControllerException ex) {
                getEnvironment().getTracer().warning("Error occured after reading aliases");
            }
        }

        if(aliases != null && aliases.length > 0) {
            return aliases;
        } else {
            return null;
        }
    }
    
    public Callable<String[]> getAliasesCaller(final char[] password, final String configPath) {
        return new Callable<String[]>() {
            @Override
            public String[] call() throws Exception {
                return  password == null ? null : getSelectedAlias(password, configPath);
            }
        };
    }

    @Override
    public void done(final int result) {
        for (File file: filesToDelete){
            file.delete();
        }
        super.done(result);
    }
} 
