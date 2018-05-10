/*
 * Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.wps.views.admin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.eas.AddressTranslationTable;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.types.ExplorerRoot;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.wps.Connections;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.ArrayEditorDialog;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.rwt.ValueEditor;
import org.radixware.wps.views.editors.valeditors.ValBoolEditorController;
import org.radixware.wps.views.editors.valeditors.ValListEditorController;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;

public class ConnectionsPropertiesWidget extends ValEditorControllerGrid {

    public static abstract class ConnectionOptionsChangedListener {

        public abstract void onConnectionOptionsChanged(ConnectionOptions connectionOptions);
    }
    
    public static abstract class DefaultConnectionChangedListener {

        public abstract void onDefaultConnectionChanged(boolean newValue);
    }

    private final WpsEnvironment env;
    private final MessageProvider mp;
    private boolean isNewRow = false;
    private final ValStrEditorController nameEditorController;
    private final ValStrEditorController serverEditorController;
    private final ValStrEditorController stationEditorController;
    private final ToolButton editFileBtn = new ToolButton();
    private final ToolButton deleteFileBtn = new ToolButton();
    private final ValBoolEditorController defaultConnectiobCb;
    private final ValBoolEditorController useTlsEncriptionEditorController;
    private final ValBoolEditorController isSapDiscoveryEnabledEditorController;
    private final ValListEditorController<String> explorerRootsDataEditorController;
    private final ValListEditorController<Long> eventSeverityEditorController;
    private final ValStrEditorController addrTranslationFilePathEditorController;
    final ValStrEditorController kerbEASPrincipalNameEditor;
    private final SslOptionsWidget sslOptionsWidget;
    private final List<ConnectionOptionsChangedListener> connectionOptionsChangedListenerList = new LinkedList<>();
    private final List<DefaultConnectionChangedListener> defaultConnectionChangedListenerList = new LinkedList<>();
    private ConnectionOptions options = null;
    private List<String> connectionNames = null;

    public ConnectionsPropertiesWidget(WpsEnvironment env, ConnectionsTableWidget connectionsTableWidget) {
        super();
        this.env = env;
        mp = env.getMessageProvider();
        this.getHtml().setCss("overflow", "auto");
        nameEditorController = new ValStrEditorController(env);
        nameEditorController.setMandatory(true);
        serverEditorController = new ValStrEditorController(env);
        serverEditorController.setMandatory(true);
        stationEditorController = new ValStrEditorController(env);
        stationEditorController.setMandatory(true);
        defaultConnectiobCb = new ValBoolEditorController(env);
        defaultConnectiobCb.setMandatory(true);
        useTlsEncriptionEditorController = new ValBoolEditorController(env);
        useTlsEncriptionEditorController.setMandatory(true);
        isSapDiscoveryEnabledEditorController = new ValBoolEditorController(env);
        isSapDiscoveryEnabledEditorController.setMandatory(true);
        explorerRootsDataEditorController = new ValListEditorController<>(env);
        explorerRootsDataEditorController.setMandatory(true);
        eventSeverityEditorController = new ValListEditorController<>(env);
        eventSeverityEditorController.setMandatory(true);
        addrTranslationFilePathEditorController = new ValStrEditorController(env);
        sslOptionsWidget = new SslOptionsWidget(env.getMessageProvider().translate("AdminPanel", "SSL Options"), env);
        kerbEASPrincipalNameEditor = new ValStrEditorController(env);
        setupUI();
    }

    private void setupUI() {
        addLabeledEditor(mp.translate("AdminPanel", "Name:"), nameEditorController);
        addLabeledEditor(mp.translate("AdminPanel", "Servers:"), serverEditorController);
        addLabeledEditor(mp.translate("AdminPanel", "Automatic discovery of server addresses:"), isSapDiscoveryEnabledEditorController);
        addLabeledEditor(mp.translate("AdminPanel", "Station:"), stationEditorController);
        addLabeledEditor(mp.translate("AdminPanel", "Default:"), defaultConnectiobCb);
        addLabeledEditor(mp.translate("AdminPanel", "Explorer root:"), explorerRootsDataEditorController);
        addLabeledEditor(mp.translate("AdminPanel", "Trace level:"), eventSeverityEditorController);
        addLabeledEditor(mp.translate("AdminPanel", "Address translation file path:"), addrTranslationFilePathEditorController);
        addLabeledEditor(mp.translate("AdminPanel", "Use TLS Encryption:"), useTlsEncriptionEditorController);
        ToolButton tbEditAddresses = new ToolButton();
        tbEditAddresses.setIcon(env.getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.EDIT));
        tbEditAddresses.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                onEditAddresses();
            }
        });
        initNameEditorController();
        stationEditorController.addValueChangeListener(new ValueEditor.ValueChangeListener<String>() {

            @Override
            public void onValueChanged(String oldValue, String newValue) {
                options.setStationName(newValue);
                for (ConnectionOptionsChangedListener listener : connectionOptionsChangedListenerList) {
                    listener.onConnectionOptionsChanged(options);
                }
            }
        });
        serverEditorController.addValueChangeListener(new ValueEditor.ValueChangeListener<String>() {

            @Override
            public void onValueChanged(String oldValue, String newValue) {
                if (newValue == null || newValue.isEmpty()) {
                    getEnvironment().messageError(mp.translate("ConnectionEditor", "Server Was Not Specified!"),
                            mp.translate("ConnectionEditor", "Please enter valid server address"));
                } else {
                    options.setInitialAddressesAsStr(newValue);
                    for (ConnectionOptionsChangedListener listener : connectionOptionsChangedListenerList) {
                        listener.onConnectionOptionsChanged(options);
                    }
                }
            }
        });

        serverEditorController.addButton(tbEditAddresses);
        defaultConnectiobCb.addValueChangeListener(new ValueEditor.ValueChangeListener<Boolean>() {

            @Override
            public void onValueChanged(Boolean oldValue, Boolean newValue) {
                if (!isNewRow) {
                    for (DefaultConnectionChangedListener listener : defaultConnectionChangedListenerList) {
                        listener.onDefaultConnectionChanged(newValue);
                    }
                }
            }
        });
        initExplorerRootsEditor();
        initEventSeverityEditor();
        initAddrTranslationFilePathEditor();
        this.addGroupWidgetRow(sslOptionsWidget);

        useTlsEncriptionEditorController.addValueChangeListener(new ValueEditor.ValueChangeListener<Boolean>() {

            @Override
            public void onValueChanged(Boolean oldValue, Boolean newValue) {
                sslOptionsWidget.setEnabled(newValue);
                if (!isNewRow) {
                    options.setSslOptions(newValue ? sslOptionsWidget.getSslOptions() : null);
                }
            }
        });
        
        isSapDiscoveryEnabledEditorController.addValueChangeListener(new ValueEditor.ValueChangeListener<Boolean>() {

            @Override
            public void onValueChanged(Boolean oldValue, Boolean newValue) {
                options.setSapDiscoveryEnabled(newValue);
            }
        });
        
        sslOptionsWidget.addSslOptionsChangedListener(new SslOptionsWidget.SslOptionsChangedListener() {

            @Override
            public void onSslOptionsChanged(ConnectionOptions.SslOptions sslOptions) {
                if (options != null) {
                    options.setSslOptions(sslOptions);
                }
            }
        });

        GroupWidget kerbOptionsGW = new GroupWidget(mp.translate("AdminPanel", "Kerberos Options"));
        kerbOptionsGW.addNewRow(mp.translate("ConnectionEditor", "EAS principal name:"), kerbEASPrincipalNameEditor);
        addGroupWidgetRow(kerbOptionsGW);

        kerbEASPrincipalNameEditor.addValueChangeListener(new ValueEditor.ValueChangeListener<String>() {

            @Override
            public void onValueChanged(String oldValue, String newValue) {
                if (!isNewRow) {
                    ConnectionOptions.KerberosOptions kerberosOptions = options.getKerberosOptions();
                    if (kerberosOptions == null) {
                        kerberosOptions = new ConnectionOptions.KerberosOptions();
                    }
                    kerberosOptions.setServicePrincipalName(newValue);
                    options.setKerberosOptions(kerberosOptions);
                }
            }
        });
    }

    private void initExplorerRootsEditor() {
        final Map<String, Id> id2StringMap = new HashMap<>();
        if (RadixLoader.getInstance() == null) {
            explorerRootsDataEditorController.setEnabled(false);
        } else {
            final String noExplorerRootTitle = mp.translate("ConnectionEditor", "<Select later>");
            final EditMaskList items = new EditMaskList();
            items.addItem(noExplorerRootTitle, null, (Icon) null);
            final List<ExplorerRoot> explorerRoots = ExplorerRoot.loadFromDefManager(getEnvironment());
            for (ExplorerRoot explorerRoot : explorerRoots) {
                if (explorerRoot.getIconId() == null) {
                    items.addItem(explorerRoot.getTitle(), explorerRoot.getId().toString(), getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.Definitions.TREE));
                } else {
                    items.addItem(explorerRoot.getTitle(), explorerRoot.getId().toString(), explorerRoot.getIconId());
                }
                id2StringMap.put(explorerRoot.getId().toString(), explorerRoot.getId());
            }
            explorerRootsDataEditorController.setEnabled(true);
            explorerRootsDataEditorController.setEditMask(items);
        }
        explorerRootsDataEditorController.addValueChangeListener(new ValueEditor.ValueChangeListener<String>() {

            @Override
            public void onValueChanged(String oldValue, String newValue) {
                ((Connections.ConnectionOptionsImpl) options).setExplorerRootId(id2StringMap.get(newValue));
            }
        });
    }

    private void initEventSeverityEditor() {
        final EditMaskList items = new EditMaskList();
        for (EEventSeverity eventSeverity : EEventSeverity.values()) {
            items.addItem(env.getMessageProvider().translate("ConnectionEditor", eventSeverity.getName()), eventSeverity.getValue());
        }
        eventSeverityEditorController.setEditMask(items);
        eventSeverityEditorController.addValueChangeListener(new ValueEditor.ValueChangeListener<Long>() {

            @Override
            public void onValueChanged(Long oldValue, Long newValue) {
                if (!isNewRow) {
                    options.setEventSeverity(EEventSeverity.getForValue(newValue));
                }
            }
        });
    }

    private void editAddressTranslationFile() {
        String fileName = addrTranslationFilePathEditorController.getValue();
        File f = new File(fileName);
        boolean isOk = true;
        if (f.exists() && !f.isDirectory()) {
        } else {
            if (f.getParentFile() != null) {
                f.getParentFile().mkdirs();
                try {
                    f.createNewFile();
                } catch (IOException ex) {
                    isOk = false;
                    getEnvironment().messageError(mp.translate("AdminPanel", "Error"), mp.translate("AdminPanel", "File can't be created"));
                }
            } else {
                isOk = false;
                getEnvironment().messageError(mp.translate("AdminPanel", "Error"), mp.translate("AdminPanel", "Wrong path"));
            }
        }
        if (isOk) {
            AddressTranslationTable addrTable = AddressTranslationTable.parseFile(fileName, env.getTracer(), env.getMessageProvider());
            Iterator<Map.Entry<String, AddressTranslationTable.TranslationTable>> it = addrTable.getIterator();
            AddressTranslationTableDialog addressTranslationTableDlg = new AddressTranslationTableDialog(it, f);
            addressTranslationTableDlg.execDialog();
        }
    }

    private void deleteFile() {
        String fileName = addrTranslationFilePathEditorController.getValue();
        String confirmationStr = mp.translate("AdminPanel", "Do you really want to delete file %s?");
        if (env.messageConfirmation("ExplorerMessage", String.format(confirmationStr, fileName))) {
            File f = new File(fileName);
            f.delete();
            addrTranslationFilePathEditorController.setValue(null);
        }
    }

    private void initAddrTranslationFilePathEditor() {
        addrTranslationFilePathEditorController.addValueChangeListener(new ValueEditor.ValueChangeListener<String>() {
            @Override
            public void onValueChanged(String oldValue, String newValue) {
                boolean isEnabled = newValue != null && !newValue.isEmpty();
                deleteFileBtn.setEnabled(isEnabled);
                editFileBtn.setEnabled(isEnabled);
                options.setAddressTranslationFilePath(newValue);
            }
        });
        
        editFileBtn.setIcon(env.getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.EDIT));
        editFileBtn.setEnabled(false);
        editFileBtn.setToolTip(mp.translate("AdminPanel", "Edit file"));
        editFileBtn.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                editAddressTranslationFile();
            }
        });

        deleteFileBtn.setIcon(env.getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.DELETE));
        deleteFileBtn.setToolTip(mp.translate("AdminPanel", "Delete file"));
        deleteFileBtn.setEnabled(false);
        deleteFileBtn.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                deleteFile();
            }
        });
        addrTranslationFilePathEditorController.addButton(editFileBtn);
        addrTranslationFilePathEditorController.addButton(deleteFileBtn);
    }

    private void onEditAddresses() {
        final String[] arrAddresses = serverEditorController.getValue() == null ? new String[0] : serverEditorController.getValue().split(";");
        ArrStr array = new ArrStr();
        for (String address : arrAddresses) {
            if (!address.trim().isEmpty()) {
                array.add(address.trim());
            }
        }
        final ArrayEditorDialog dialog = new ArrayEditorDialog(env, EValType.ARR_STR, ArrStr.class, false, env.getDialogDisplayer());
        dialog.setMandatory(true);
        dialog.setItemMandatory(true);
        dialog.setWindowTitle(mp.translate("ConnectionEditor", "Servers List"));
        dialog.setCurrentValue(array);
        if (dialog.execDialog() == IDialog.DialogResult.ACCEPTED) {
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
                String addressListStr = addressDataBuilder.toString();
                serverEditorController.setValue(addressListStr);
            }
        }
    }

    void refresh(ConnectionOptions connectionForRow, List<String> connectionNames, boolean isDefault) {
        this.options = connectionForRow;
        this.connectionNames = connectionNames;
        isNewRow = true;
        nameEditorController.setValue(connectionForRow.getName());
        serverEditorController.setValue(connectionForRow.getInitialAddressesAsStr());
        isSapDiscoveryEnabledEditorController.setValue(connectionForRow.isSapDiscoveryEnabled());
        stationEditorController.setValue(connectionForRow.getStationName());
        if (isDefault) {
            defaultConnectiobCb.setValue(true);
        } else {
            defaultConnectiobCb.setValue(false);
        }
        Id explorerRootId = connectionForRow.getExplorerRootId(null);
        explorerRootsDataEditorController.setValue(explorerRootId == null ? null : explorerRootId.toString());
        eventSeverityEditorController.setValue(connectionForRow.getEventSeverity().getValue());
        addrTranslationFilePathEditorController.setValue(connectionForRow.getAddressTranslationFilePath());
        ConnectionOptions.SslOptions currentSslOptions = connectionForRow.getSslOptions();
        if (currentSslOptions != null) {
            sslOptionsWidget.setSslOptions(currentSslOptions);
        } else {
            sslOptionsWidget.setSslOptions(null);
        }
        useTlsEncriptionEditorController.setValue(currentSslOptions != null);
        ConnectionOptions.KerberosOptions kerberosOptions = connectionForRow.getKerberosOptions();
        kerbEASPrincipalNameEditor.setValue(kerberosOptions == null ? null : kerberosOptions.getServicePrincipalName());
        isNewRow = false;
    }

    private void initNameEditorController() {
        nameEditorController.addValueChangeListener(new ValueEditor.ValueChangeListener<String>() {

            @Override
            public void onValueChanged(String oldValue, String newConnectionName) {
                if (!isNewRow) {
                    if (newConnectionName == null || newConnectionName.isEmpty()) {
                        env.messageError(mp.translate("ConnectionEditor", "Connection Name Was Not Specified!"),
                                mp.translate("ConnectionEditor", "Please enter connection name"));
                    } else {
                        if (oldValue != null && oldValue.equals(newConnectionName)) {
                        } else {
                            if (connectionNames.contains(newConnectionName)) {
                                env.messageError(mp.translate("ConnectionEditor", "Invalid Connection Name"),
                                        String.format(mp.translate("ConnectionEditor", "Connection with name \"%s\" is already exists!"), newConnectionName));
                                nameEditorController.setValue(oldValue);
                            } else {
                                options.setName(newConnectionName);
                                for (ConnectionOptionsChangedListener listener : connectionOptionsChangedListenerList) {
                                    listener.onConnectionOptionsChanged(options);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public void addConnectionOptionsChangedListener(ConnectionOptionsChangedListener listener) {
        connectionOptionsChangedListenerList.add(listener);
    }

    public void addDefaultConnectionChangedListener(DefaultConnectionChangedListener listener) {
        defaultConnectionChangedListenerList.add(listener);
    }
    
    public void setConnectionNames(List<String> connectionNames) {
        this.connectionNames = connectionNames;
    }
}
