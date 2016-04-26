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

package org.radixware.kernel.explorer.iad.wia;

import com.trolltech.qt.gui.QImage;
import com.trolltech.qt.gui.QWidget;
import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.explorer.env.progress.TaskWaiter;
import org.radixware.kernel.explorer.iad.ImageAcquaringDevice;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.utils.wia.ComException;
import org.radixware.kernel.utils.wia.EDeviceDialogFlag;
import org.radixware.kernel.utils.wia.EImageIntent;
import org.radixware.kernel.utils.wia.IComObjectStream;
import org.radixware.kernel.utils.wia.WiaDeviceManager;
import org.radixware.kernel.utils.wia.WiaItem;
import org.radixware.kernel.utils.wia.WiaRootItem;
import org.radixware.kernel.utils.wia.properties.ComProperty;
import org.radixware.kernel.utils.wia.properties.WiaProperties;
import org.radixware.kernel.utils.wia.properties.WiaPropertyStorage;


final class WiaDevice extends ImageAcquaringDevice{
    
    private final static EnumSet<EImageIntent> IMAGE_INTENTS = EnumSet.of(EImageIntent.NONE);
    private final static EnumSet<EDeviceDialogFlag> DIALOG_FLAGS = EnumSet.of(EDeviceDialogFlag.SINGLE_IMAGE);
    private final static String DIAGNOSTICS_SYS_PROP_NAME = "org.radixware.kernel.explorer.iad.wia.diagnostics";
        
    private final String deviceId;
    private final String description;
    private final IClientEnvironment environment;
    private final boolean isDiagnosticEnabled;
    
    public WiaDevice(final IClientEnvironment environment, final String id, final String desc){
        this.environment = environment;
        deviceId = id;
        description = desc;
        isDiagnosticEnabled = 
            System.getProperty(DIAGNOSTICS_SYS_PROP_NAME)!=null &&
            environment.getTracer().getProfile().getEventSourceSeverity(EEventSource.IAD.getValue())==EEventSeverity.DEBUG;
    }

    @Override
    public String getId() {
        return deviceId;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public File loadImageToFile(final String filePath, final boolean showImageConfirmationDialog, final QWidget parentWidget) {
        if (showImageConfirmationDialog){
            return super.loadImageToFile(filePath, showImageConfirmationDialog, parentWidget);
        }else{
            final File imageFile = createTemporaryFile(filePath);
            if (imageFile==null){
                return null;
            }
            try(WiaRootItem device = openDevice()){
                try(WiaItem imageItem = getImageItem(device, parentWidget)){                    
                    return transferImage(imageItem, imageFile.getAbsolutePath(), parentWidget);
                }
            }catch(ComException exception){
                processComExceptionOnGetImageItem(exception);
                return null;
            }
        }
    }
    
    private File createTemporaryFile(final String filePath){
        final File imageFile;
        if (filePath==null || filePath.isEmpty()){
            if (RadixLoader.getInstance()==null){
                try{
                    imageFile = File.createTempFile("WIA_image", null);
                    imageFile.deleteOnExit();
                }catch(IOException exception){         
                    final String message = 
                        environment.getMessageProvider().translate("IAD", "Failed to create temporary file");
                    environment.processException(message, exception);
                    return null;
                }
            }else{
                imageFile = RadixLoader.getInstance().createTempFile("WIA_image");
            }
            if (imageFile==null){
                final String message = 
                    environment.getMessageProvider().translate("IAD", "Unable to create temporary file");
                environment.getTracer().put(EEventSeverity.ERROR, message, EEventSource.IAD);
                return null;
            }
        }else{
            imageFile = new File(filePath);
        }
        return imageFile;
    }
    
    @Override
    public QImage getImage(final boolean showImageConfirmationDialog, final QWidget parent) {        
        try(WiaRootItem device = openDevice()){
            QImage image;            
            do{
                try(WiaItem imageItem = getImageItem(device, parent)){                    
                    image = transferImage(imageItem, parent);
                }
            }while(showImageConfirmationDialog && image!=null && !confirmScannedImage(environment, image, parent));
            return image;
        }catch(ComException exception){
            processComExceptionOnGetImageItem(exception);
            return null;
        }
    }
    
    private WiaRootItem openDevice() throws ComException{
        try(WiaDeviceManager devManager=WiaDeviceManager.newInstance()){
            final WiaRootItem device = devManager.openDevice(deviceId);
            final String traceMessageTemplate = 
                environment.getMessageProvider().translate("IAD", "Device properties:\n%1$s");
            traceWiaItemProperties(traceMessageTemplate, device);
            return device;
        }        
    }
    
    private WiaItem getImageItem(final WiaRootItem device, final QWidget parent) throws ComException{
        final long parentWindowId = parent==null ? 0 : parent.window().winId();
        final WiaItem[] imageItems = 
            device.deviceDlg(parentWindowId, DIALOG_FLAGS, IMAGE_INTENTS);
        if (imageItems==null || imageItems.length==0){
            final String traceMessage = 
                environment.getMessageProvider().translate("IAD", "Unable to receive image item");
            traceDebugMessage(traceMessage);
            return null;
        }
        if (imageItems.length==1){
            final String traceMessageTemplate = 
                environment.getMessageProvider().translate("IAD", "Image properties:\n%1$s");
            traceWiaItemProperties(traceMessageTemplate, imageItems[0]);            
        }else {
            traceImages(imageItems);
            for (int i=1; i<imageItems.length; i++){
                imageItems[i].close();
            }
        }                
        return imageItems[0];
    }
    
    private void processComExceptionOnGetImageItem(final ComException exception){
        final String message = environment.getMessageProvider().translate("IAD", "Failed to work with the device");
        environment.processException(new WiaException(getErrorTitle(), message, environment.getMessageProvider(), exception));
    }
    
    private ScanningTask executeTransferImageTask(final WiaItem imageItem, final String imageFilePath, final QWidget parent){
        final IComObjectStream<WiaItem> stream;
        try{
            stream = imageItem.writeToStream();
        }catch(ComException exception){
            final MessageProvider mp = environment.getMessageProvider();
            final String message = mp.translate("IAD", "Failed to transfer image");
            environment.processException(new WiaException(message, message, mp, exception));
            return null;
        }
        final TaskWaiter taskWaiter = new TaskWaiter(environment, parent);
        try{                        
            final ScanningTask task = new ScanningTask(environment, stream, imageFilePath);
            try {
                taskWaiter.runAndWait(task);
            } catch (InterruptedException ex) {
                Logger.getLogger(WiaDevice.class.getName()).log(Level.SEVERE, null, ex);
            }
            return task;
        }finally{
            taskWaiter.close();
        }
    }
    
    private File transferImage(final WiaItem imageItem, final String filePath, final QWidget parent){
        if (imageItem==null){
            return null;
        }
        final ScanningTask task = executeTransferImageTask(imageItem, filePath, parent);
        if (task==null){
            return null;
        }
        try{
            final String imageFilePath = task.getImageFilePath();
            return imageFilePath==null ? null : new File(imageFilePath);
        }catch(Exception exception){
            processExceptionOnTransferImage(task, exception);
            return null;
        }        
    }
    
    private QImage transferImage(final WiaItem imageItem, final QWidget parent){
        if (imageItem==null){
            return null;
        }
        final ScanningTask task = executeTransferImageTask(imageItem, null, parent);
        if (task==null){
            return null;
        }        
        try{
            final QImage image = task.getImage();
            if (image==null){
                final String imageFilePath = task.getImageFilePath();
                if (imageFilePath==null || imageFilePath.isEmpty()){
                    return null;
                }
                return loadImageFromFile(imageFilePath, parent);
            }else{
                return image;
            }
        }catch(Exception exception){
            processExceptionOnTransferImage(task, exception);
            return null;
        }finally{
            task.deleteTemporaryImageFile();
        }
    }
    
    private void processExceptionOnTransferImage(final ScanningTask task, final Throwable exception){
        final String message;
        if (task.getExceptionTitle()==null || task.getExceptionTitle().isEmpty()){
            message = exception.getMessage();
        }else{
            message = task.getExceptionTitle();
        }            
        environment.processException(new WiaException(getErrorTitle(), message, environment.getMessageProvider(), exception));        
    }
    
    private QImage loadImageFromFile(final String filePath, final QWidget parent){
        final TaskWaiter taskWaiter = new TaskWaiter(environment, parent);
        taskWaiter.setMessage(environment.getMessageProvider().translate("IAD", "Loading image..."));
        final ReadImageFileTask task = new ReadImageFileTask(filePath);
        try{            
            taskWaiter.runAndWait(task);
        }catch(InterruptedException exception){
            environment.getTracer().error(exception);
            return null;
        }finally{
            taskWaiter.close();
        }
        final QImage image = task.getQImage();
        if (image.isNull()){
            final String errorTextTemplage = 
                environment.getMessageProvider().translate("IAD", "Unable to load image from file \'%1$s\'");
            environment.messageError(getErrorTitle(), String.format(errorTextTemplage, filePath));
            return null;
        }
        return image;
    }
    
    private String getErrorTitle(){
        return environment.getMessageProvider().translate("IAD", "Failed to receive image");
    }
        
    private void traceImages(final WiaItem[] images){
        if (isDiagnosticEnabled){
            final MessageProvider mp = environment.getMessageProvider();
            final StringBuilder traceMessageBuilder = new StringBuilder();
            traceMessageBuilder.append(mp.translate("IAD", "Several images accessible for transferring:\n"));
            for (int i=0; i<images.length; i++){
                if (i>0){
                    traceMessageBuilder.append('\n');
                }
                traceMessageBuilder.append(String.format(mp.translate("IAD", "Image #%1$s:\n"), i+1));
                final String properties = getWiaItemProperties(images[i]);
                if (properties!=null){
                    traceMessageBuilder.append(properties);
                }
            }
            traceDebugMessage(traceMessageBuilder.toString());
        }
    }
    
    private void traceWiaItemProperties(final String traceMessageTemplate, final WiaItem item){
        if (isDiagnosticEnabled){
            final String deviceProps = getWiaItemProperties(item);
            if (deviceProps!=null && !deviceProps.isEmpty()){
                final String traceMessage = String.format(traceMessageTemplate, deviceProps);
                traceDebugMessage(traceMessage);                
            }
        }
        
    }
    
    private String getWiaItemProperties(final WiaItem item){
        try(WiaPropertyStorage propertyStorage = item.openPropertyStorage()){            
            return readProperties(propertyStorage);
        }catch(ComException exception){
            final String traceMessageTemplate = 
                environment.getMessageProvider().translate("IAD", "Failed to read WIA item properties:\n%1$s\n%2$s");
            traceDebugMessage(String.format(traceMessageTemplate, exception.getMessage(), ClientException.exceptionStackToString(exception)));
            return null;
        }
    }
    
    static String readProperties(final WiaPropertyStorage propertyStorage) throws ComException{
        final List<ComProperty> props = new LinkedList<>();
        try (WiaProperties properties = propertyStorage.loadProperties()){
            for (ComProperty property: properties){
                props.add(property);
            }            
            propertyStorage.readMultiple(props);
        }
        final StringBuilder strBuilder = new StringBuilder();
        for (ComProperty property: props){
            if (strBuilder.length()>0){
                strBuilder.append('\n');
            }
            strBuilder.append(property.toString());
        }
        return strBuilder.toString();        
    }
    
    private void traceDebugMessage(final String message){
        environment.getTracer().put(EEventSeverity.DEBUG, message, EEventSource.IAD);
    }
}
