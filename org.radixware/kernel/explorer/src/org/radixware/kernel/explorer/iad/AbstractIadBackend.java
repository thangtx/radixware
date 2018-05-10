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

package org.radixware.kernel.explorer.iad;

import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QWidget;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.env.progress.TaskWaiter;
import org.radixware.kernel.explorer.iad.sane.IadUnixBackend;
import org.radixware.kernel.explorer.iad.wia.IadWiaBackend;

public abstract class AbstractIadBackend {
    
    private boolean inited;
    
    public static class Factory{
        
        private Factory(){            
        }
        
        public static AbstractIadBackend getInstance(final EImageAcquiringDeviceType type){
            if (SystemTools.isWindows){
                return IadWiaBackend.getInstance();
            }else if (SystemTools.isUnix()){
                return IadUnixBackend.getInstance();
            }else{
                return null;
            }
        }
    }
    

    public final Collection<ImageAcquiringDevice> enumDevices(final IClientEnvironment environment, 
                                                              final EImageAcquiringDeviceType type){
        if (!inited){
            if (init(environment)){
                inited = true;                
            }else{
                return Collections.emptyList();
            }
        }
        return enumDevicesImpl(environment, type);
    }
    
    protected abstract Collection<ImageAcquiringDevice> enumDevicesImpl(final IClientEnvironment environment, 
                                                                        final EImageAcquiringDeviceType type);
    
    protected Throwable processEnumDevicesTaskException(final ExecutionException exception){
        return exception.getCause();
    }
        
    protected boolean init(final IClientEnvironment environment){
        return true;
    }
    
    public final void release(){
        if (inited){
            releaseImpl();
            inited = false;
        }
    }
    
    protected void releaseImpl(){
        
    }
    
    protected String getDeviceNotFoundMessage(final MessageProvider messageProvider){
        return messageProvider.translate("IAD", "No device was found. Make sure the device is online, connected to the PC, and has the correct driver installed on the PC.");
    }
    
    public final ImageAcquiringDevice selectDevice(final IClientEnvironment environment, 
                                                    final EImageAcquiringDeviceType type,
                                                    final QWidget parentWidget,
                                                    final boolean forcedlyShowDialog) {
        if (!inited){
            if (init(environment)){
                inited = true;
            }else{
                return null;
            }
        }
        final TaskWaiter taskWaiter = new TaskWaiter(environment,parentWidget);
        taskWaiter.setMessage(environment.getMessageProvider().translate("IAD", "Detecting Devices..."));
        taskWaiter.setCanBeCanceled(true);        
        final Collection<ImageAcquiringDevice> devices;
        try{
            devices = taskWaiter.runAndWait(new Callable<Collection<ImageAcquiringDevice>>() {
                @Override
                public Collection<ImageAcquiringDevice> call() throws Exception {
                    return enumDevices(environment,type);
                }
            });
        }catch(InterruptedException exception){
            return null;
        }catch(ExecutionException exception){
            environment.processException(processEnumDevicesTaskException(exception));
            return null;
        }finally{
            taskWaiter.close();
        }
        if (devices.size()==1 && !forcedlyShowDialog){
            return devices.iterator().next();
        }else{
            final ChooseDeviceDialog dialog = 
                new ChooseDeviceDialog(environment, this, devices, type, parentWidget);
            dialog.setDeviceNotFoundMessage(getDeviceNotFoundMessage(environment.getMessageProvider()));
            if (dialog.exec()==QDialog.DialogCode.Accepted.value()){
                return dialog.getSelectedDevice();
            }else{
                return null;
            }
        }
    }
}
