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

package org.radixware.kernel.explorer.iad;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QButtonGroup;
import com.trolltech.qt.gui.QContentsMargins;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QRadioButton;
import com.trolltech.qt.gui.QScrollArea;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;


class ChooseDeviceDialog extends ExplorerDialog{
    
    private static class ReloadCompleteEvent extends QEvent{
        
        private final Collection<ImageAcquiringDevice> devices;
        
        public ReloadCompleteEvent(final Collection<ImageAcquiringDevice> devices){
            super(QEvent.Type.User);
            this.devices = devices;
        }
        
        public Collection<ImageAcquiringDevice> getDevices(){
            return devices;
        }
    }
    
    private static class ReloadDevicesTask extends Thread{
        
        private final IClientEnvironment environment;
        private final AbstractIadBackend iadBackend;
        private final EImageAcquiringDeviceType type;
        private final QObject listener;
        
        public ReloadDevicesTask(final IClientEnvironment environment, 
                                 final AbstractIadBackend backend,
                                 final EImageAcquiringDeviceType type,
                                 final QObject obj){
            super("Reload IAD devices");
            iadBackend = backend;
            this.environment = environment;
            this.type = type;
            listener = obj;
        }

        @Override
        public void run() {
            final Collection<ImageAcquiringDevice> devices;
            try{
                devices = iadBackend.enumDevices(environment,type);
                QApplication.postEvent(listener, new ReloadCompleteEvent(devices));
                return;
            }catch(Exception exception){
                environment.getTracer().error(exception);
            }
            QApplication.postEvent(listener, new ReloadCompleteEvent(Collections.<ImageAcquiringDevice>emptyList()));
        }
    
    }
    
    private final QButtonGroup btgDevices = new QButtonGroup(this);
    private final QGroupBox    gbDevices = new QGroupBox(this);
    private final QWidget      wtDeviceContainer = new QWidget();
    private final QLabel       lbExplanation = new QLabel(gbDevices);
    private final QScrollArea  scaDevices = new QScrollArea(gbDevices);
    private final QVBoxLayout  btnLayout = new QVBoxLayout();        
    private final IPushButton  pbRefresh;
    private final EImageAcquiringDeviceType type;
    private final Map<String,ImageAcquiringDevice> deviceById = new HashMap<>();    
    private final AbstractIadBackend iadBackend;
    private String selectedDeviceId;
    private ImageAcquiringDevice selectedDevice;
    private ReloadDevicesTask reloadDevicesTask;
    
    public ChooseDeviceDialog(final IClientEnvironment environment, 
                              final AbstractIadBackend backend,
                              final Collection<ImageAcquiringDevice> initialDeviceList,
                              final EImageAcquiringDeviceType type,
                              final QWidget parentWidget){
        super(environment,parentWidget);
        iadBackend = backend;
        this.type = type;
        setupUi();
        pbRefresh = 
            addButtons(EnumSet.of(EDialogButtonType.RETRY, EDialogButtonType.OK, EDialogButtonType.CANCEL), true).get(EDialogButtonType.RETRY);
        pbRefresh.setTitle(getEnvironment().getMessageProvider().translate("IAD", "Reload devices list"));
        pbRefresh.setIcon(environment.getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.REFRESH));
        pbRefresh.addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(IButton source) {
                reloadDevicesList();
            }
        });
        updateDevicesList(initialDeviceList);
        layout().setSizeConstraint(QLayout.SizeConstraint.SetFixedSize);
    }
    
    public void setDeviceNotFoundMessage(final String message){
        lbExplanation.setText(message);
    }
    
    private void setupUi(){
        setWindowTitle(getEnvironment().getMessageProvider().translate("IAD", "Select Device"));
        final QVBoxLayout vltMain = new QVBoxLayout();
        gbDevices.setLayout(vltMain);
        wtDeviceContainer.setLayout(btnLayout);
        wtDeviceContainer.setObjectName("Rdx.ChooseDeviceDialog.wtDeviceContainer");
        final String explanationText = 
            getEnvironment().getMessageProvider().translate("IAD", "No device was found. Make sure the device is online, connected to the PC, and has the correct driver installed on the PC.");
        lbExplanation.setText(explanationText);
        lbExplanation.setOpenExternalLinks(true);
        final QContentsMargins margins = layout().getContentsMargins();
        lbExplanation.setContentsMargins(margins);
        vltMain.addWidget(lbExplanation);
        gbDevices.adjustSize();
        scaDevices.setWidgetResizable(true);
        scaDevices.setFrameShape(QFrame.Shape.NoFrame);
        scaDevices.setWidget(wtDeviceContainer);
        scaDevices.setMinimumSize(lbExplanation.sizeHint());
        vltMain.addWidget(scaDevices);
        vltMain.setContentsMargins(0,0,0,0);
        layout().addWidget(gbDevices);        
    }
    
    private void updateDevicesList(final Collection<ImageAcquiringDevice> deviceList){
        for (int i=btgDevices.buttons().size()-1; i>=0; i--){
            btgDevices.removeButton(btgDevices.buttons().get(i));
        }
        deviceById.clear();
        selectedDevice = null;
        selectedDeviceId = null;
        if (deviceList.isEmpty()){
            gbDevices.setTitle(getEnvironment().getMessageProvider().translate("IAD", "Sorry. No devices found."));
            lbExplanation.setVisible(true);
            scaDevices.setVisible(false);            
            getButton(EDialogButtonType.OK).setEnabled(false);
        }else{
            clearBtnLayout();
            gbDevices.setTitle(getEnvironment().getMessageProvider().translate("IAD", "Found devices:"));
            lbExplanation.setVisible(false);
            scaDevices.setVisible(true);
            for (ImageAcquiringDevice device: deviceList) {
                final QRadioButton rbDevice = new QRadioButton(wtDeviceContainer);                
                rbDevice.setObjectName(device.getId());                
                rbDevice.setText(device.getDescription());
                btnLayout.addWidget(rbDevice);
                btgDevices.addButton(rbDevice);
                deviceById.put(device.getId(),device);
                rbDevice.clicked.connect(this,"onDeviceClick(Boolean)");
                if(btgDevices.buttons().size()==1 || (Objects.equals(selectedDeviceId, device.getId()))) {
                    rbDevice.setChecked(true);
                    onDeviceClick(true);
                }
            }
            btnLayout.addStretch();
        }
        pbRefresh.setEnabled(true);
    }
    
    private void clearBtnLayout(){
        final List<QWidget> widgets = new LinkedList<>();
        for (int i = btnLayout.count() - 1; i >= 0; i--) {
            if (btnLayout.itemAt(i).widget() != null) {
                widgets.add(btnLayout.itemAt(i).widget());
                btnLayout.removeWidget(btnLayout.itemAt(i).widget());                    
            }else {
                btnLayout.removeItem(btnLayout.itemAt(i));
            }
        }
        for (QWidget widget: widgets){
            widget.close();
            widget.setParent(null);
        }
    }

    private void onDeviceClick(final Boolean activate) {
        getButton(EDialogButtonType.OK).setEnabled(activate);
        if (activate){
            selectedDeviceId =  btgDevices.checkedButton().objectName();
            selectedDevice = deviceById.get(selectedDeviceId);
        }
    }
    
    private void reloadDevicesList(){
        onDeviceClick(false);
        lbExplanation.setVisible(false);
        clearBtnLayout();
        btnLayout.addStretch();
        scaDevices.setVisible(true);        
        gbDevices.setTitle(getEnvironment().getMessageProvider().translate("IAD", "Detecting devices. Please wait."));
        pbRefresh.setEnabled(false);
        reloadDevicesTask = new ReloadDevicesTask(getEnvironment(), iadBackend, type, this);
        reloadDevicesTask.start();        
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof ReloadCompleteEvent){
            event.accept();
            if (reloadDevicesTask!=null){
                reloadDevicesTask = null;
                setUpdatesEnabled(false);
                try{
                    updateDevicesList( ((ReloadCompleteEvent)event).getDevices() );
                }finally{
                    setUpdatesEnabled(true);
                }
            }
        }else{
            super.customEvent(event);
        }
    }

    @Override
    public void done(int result) {
        if (reloadDevicesTask!=null){
            reloadDevicesTask.interrupt();
            reloadDevicesTask = null;
        }
        deviceById.clear();
        super.done(result);
    }
    
    public ImageAcquiringDevice getSelectedDevice(){
        return selectedDevice;
    }
    
}
