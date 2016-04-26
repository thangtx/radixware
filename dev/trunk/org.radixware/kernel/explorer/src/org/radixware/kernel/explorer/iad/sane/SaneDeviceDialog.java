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

package org.radixware.kernel.explorer.iad.sane;

import com.trolltech.qt.gui.QImage;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QWidget;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.dialogs.ViewImageDialog;
import org.radixware.kernel.explorer.iad.sane.gui.SaneDeviceWidget;


final class SaneDeviceDialog extends ExplorerDialog{
    
    private final SaneDevice device;
    private final SaneDeviceWidget deviceWidget;
    private final boolean needForConfirmImage;
    private final boolean singleImageMode;
    
    public SaneDeviceDialog(final SaneDevice device, 
                            final boolean singleImageMode,
                            final boolean confirmScannedImage,
                            final IClientEnvironment env, 
                            final QWidget parent){
        super(env,parent);
        this.device = device;
        needForConfirmImage = confirmScannedImage;
        this.singleImageMode = singleImageMode;
        setWindowTitle(device.getDescription());
        deviceWidget = new SaneDeviceWidget(device, singleImageMode, env, this);
        deviceWidget.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
        dialogLayout().addWidget(deviceWidget);
        final IPushButton button = addButton(EDialogButtonType.OK);
        button.setTitle(env.getMessageProvider().translate("IAD", "Scan"));        
        button.setToolTip(env.getMessageProvider().translate("IAD", "Scan Final Image"));        
        acceptButtonClick.connect(this,"onScanButtonClick()");        
        addButton(EDialogButtonType.CANCEL);
        rejectButtonClick.connect(this,"reject()");        
    }

    @Override
    public void done(int result) {
        deviceWidget.close();
        device.close();
        super.done(result);
    }
    
    @SuppressWarnings("unused")
    private void onScanButtonClick(){
        deviceWidget.scan();
        if (deviceWidget.getScannedImages().size()>0 &&
            (!singleImageMode || !needForConfirmImage || confirmScannedImage(getScannedImages().get(0)))
           ){
            this.accept();
        }
    }
    
    private boolean confirmScannedImage(final QImage image){        
        return device.confirmScannedImage(getEnvironment(), image, this);
    }
    
    public List<QImage> getScannedImages(){
        return deviceWidget.getScannedImages();
    }
    
}
