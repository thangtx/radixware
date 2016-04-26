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

import com.trolltech.qt.gui.QDialog.DialogCode;
import com.trolltech.qt.gui.QImage;
import com.trolltech.qt.gui.QWidget;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.dialogs.ViewImageDialog;
import org.radixware.kernel.starter.radixloader.RadixLoader;


public abstract class ImageAcquaringDevice {  
    
    protected ImageAcquaringDevice(){        
    }
    
    public abstract String getId();
    
    public abstract String getDescription();
    
    public final QImage getImage(boolean showImageConfirmationDialog){
        return getImage(showImageConfirmationDialog, null);
    }
    
    public abstract QImage getImage(boolean showImageConfirmationDialog, final QWidget parentWidget);
    
    public File loadImageToFile(final String filePath, final boolean showImageConfirmationDialog, final QWidget parentWidget){        
        final File imageFile;
        if (filePath==null || filePath.isEmpty()){
            if (RadixLoader.getInstance()==null){
                try{
                    imageFile = File.createTempFile("image_file", null);                    
                }catch(IOException exception){
                    return null;
                }
            }else{
                imageFile = RadixLoader.getInstance().createTempFile("image_file");
            }
            if (imageFile==null){
                return null;
            }            
        }else{
            imageFile = new File(filePath);
        }
        final QImage image = getImage(showImageConfirmationDialog, parentWidget);
        if (image==null || image.isNull()){
            return null;
        }
        image.save(imageFile.getAbsolutePath(),"BMP");
        return imageFile;
    }
    
    public final File loadImageToFile(final String filePath, final boolean showImageConfirmationDialog){
        return ImageAcquaringDevice.this.loadImageToFile(filePath, showImageConfirmationDialog, null);
    }
    
    public final File loadImageToFile(final boolean showImageConfirmationDialog){
        return ImageAcquaringDevice.this.loadImageToFile(null, showImageConfirmationDialog, null);
    }
    
    private static AbstractIadBackend getBackend(final EImageAcquaringDeviceType type){
        return AbstractIadBackend.Factory.getInstance(type);
    }
    
    public static boolean isSupportedPlatform(final EImageAcquaringDeviceType type){
        return getBackend(type)!=null;
    }
    
    public static Collection<ImageAcquaringDevice> enumDevices(final IClientEnvironment environment, 
                                                              final EImageAcquaringDeviceType type){
        final AbstractIadBackend backend = getBackend(type);
        if (backend==null){
            showUnsupportedMsg(environment, type);
            return Collections.emptyList();
        }else{
            return backend.enumDevices(environment, type);
        }
    }
    
    public static ImageAcquaringDevice selectDevice(final IClientEnvironment environment, 
                                                    final EImageAcquaringDeviceType type,
                                                    final QWidget parentWidget,
                                                    final boolean forcedlyShowDialog){
        final AbstractIadBackend backend = getBackend(type);
        if (backend==null){
            showUnsupportedMsg(environment, type);
            return null;
        }else{
            return backend.selectDevice(environment, type, parentWidget, forcedlyShowDialog);
        }        
    } 
    
    private static void showUnsupportedMsg(final IClientEnvironment environment, final EImageAcquaringDeviceType type){
        if (type==EImageAcquaringDeviceType.SCANNER && SystemTools.isOSX){
            final String title = environment.getMessageProvider().translate("IAD", "Unsupported Operation");
            final String message = environment.getMessageProvider().translate("IAD", "This operation is not supported for OSX operation system yet");
            environment.messageInformation(title, message);                
        }else{
            final String title = environment.getMessageProvider().translate("IAD", "Unsupported Operation");
            final String message = environment.getMessageProvider().translate("IAD", "This operation is not supported for current operation system yet");
            environment.messageInformation(title, message);                
        }
        
    }
    
    public static QImage getImage(final IClientEnvironment environment, 
                                  final EImageAcquaringDeviceType type,
                                  final QWidget parentWidget,
                                  final boolean showImageConfirmationDialog){
        final ImageAcquaringDevice device = selectDevice(environment, type, parentWidget, false);
        return device==null ? null : device.getImage(showImageConfirmationDialog, parentWidget);
    }
    
    public static File loadImageToFile(final IClientEnvironment environment,
                                  final EImageAcquaringDeviceType type,
                                  final String filePath,
                                  final QWidget parentWidget,
                                  final boolean showImageConfirmationDialog){
        final ImageAcquaringDevice device = selectDevice(environment, type, parentWidget, false);
        return device.loadImageToFile(filePath, showImageConfirmationDialog, parentWidget);
    }
    
    public static void release(){
        for (EImageAcquaringDeviceType type: EImageAcquaringDeviceType.values()){
            final AbstractIadBackend backend = getBackend(type);
            if (backend!=null){
                backend.release();
            }
        }
    }
    
    public final boolean confirmScannedImage(final IClientEnvironment environment, final QImage image, final QWidget parentWidage){
        final ViewImageDialog confirmDialog = new ViewImageDialog(image, environment, parentWidage);
        final MessageProvider mp = environment.getMessageProvider();
        confirmDialog.setWindowTitle(mp.translate("IAD", "Scanned Image"));
        confirmDialog.clearButtons();
        final IPushButton pbOk = confirmDialog.addButton(EDialogButtonType.OK);
        pbOk.setTitle(mp.translate("IAD", "Accept image"));
        pbOk.addClickHandler(new IPushButton.ClickHandler() {
            @Override
            public void onClick(IButton source) {
                confirmDialog.accept();
            }
        });
        final IPushButton pbCancel = confirmDialog.addButton(EDialogButtonType.CANCEL);
        pbCancel.setTitle(mp.translate("IAD", "Reject image"));
        pbCancel.addClickHandler(new IPushButton.ClickHandler() {
            @Override
            public void onClick(IButton source) {
                confirmDialog.reject();
            }
        });        
        return confirmDialog.exec()==DialogCode.Accepted.value();            
    }    
}
