/*
* Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.explorer.iad.AbstractIadBackend;
import org.radixware.kernel.explorer.iad.EImageAcquiringDeviceType;
import org.radixware.kernel.explorer.iad.ImageAcquiringDevice;
import org.radixware.kernel.utils.wia.ComException;
import org.radixware.kernel.utils.wia.EHResult;
import org.radixware.kernel.utils.wia.WiaDeviceManager;
import org.radixware.kernel.utils.wia.WiaDevices;
import org.radixware.kernel.utils.wia.ComLibrary;
import org.radixware.kernel.utils.wia.properties.ComProperty;
import org.radixware.kernel.utils.wia.properties.ComPropertyStr;
import org.radixware.kernel.utils.wia.properties.EComPropSpecId;
import org.radixware.kernel.utils.wia.properties.WiaPropertyStorage;


public class IadWiaBackend extends AbstractIadBackend{

    private final static IadWiaBackend INSTANCE = new IadWiaBackend();
    private ComLibrary library;
    
    private IadWiaBackend(){
    }
    
    public static IadWiaBackend getInstance(){
        return INSTANCE;
    }
    
    @Override
    protected boolean init(final IClientEnvironment environment){
        if (library==null && !ComLibrary.wasInitialized()){
            try{
                library = ComLibrary.initialize();
            }catch(ComException ex){
                environment.getTracer().error(ex);
                return false;
            }
        }
        return true;
    }    
    
    @Override
    protected Collection<ImageAcquiringDevice> enumDevicesImpl(IClientEnvironment environment, EImageAcquiringDeviceType type) {
        final ComLibrary localLib;        
        if (!ComLibrary.wasInitialized()){
            try{
                localLib = ComLibrary.initialize();
            }catch(ComException ex){
                environment.getTracer().error(ex);
                return Collections.emptySet();
            }
        }else{
            localLib = null;
        }
        try{
            final Collection<ImageAcquiringDevice> result = new LinkedList<>();
            try(WiaDeviceManager devManager=WiaDeviceManager.newInstance()){
                try (WiaDevices devices = devManager.loadDevices()){
                    for (WiaPropertyStorage propertyStorage: devices){
                        try{
                            final WiaDevice device = createWiaDevice(environment, propertyStorage);
                            if (device!=null){
                                result.add(device);
                            }
                        }finally{
                            propertyStorage.close();
                        }
                    }
                }
                return result;
            }catch (ComException ex) {
                if (EHResult.WIA_S_NO_DEVICE_AVAILABLE!=ex.getResult()){
                    environment.getTracer().error(ex);
                }
            }
            return Collections.emptySet();
        }finally{
            if (localLib!=null){
                localLib.close();
            }
        }
    }
    
    private static WiaDevice createWiaDevice(IClientEnvironment environment, final WiaPropertyStorage propertyStorage) throws ComException{
        final ComPropertyStr propDevId = new ComPropertyStr(EComPropSpecId.WIA_DIP_DEV_ID, null);
        final ComPropertyStr propDevName = new ComPropertyStr(EComPropSpecId.WIA_DIP_DEV_NAME, null);
        final ComPropertyStr propDevManufactorer = new ComPropertyStr(EComPropSpecId.WIA_DIP_VEND_DESC, null);
        propertyStorage.readMultiple(Arrays.<ComProperty>asList(propDevId, propDevName, propDevManufactorer));
        if (propDevId.getValue()!=null && !propDevId.getValue().isEmpty()){
            final StringBuilder descBuilder = new StringBuilder();
            if (propDevManufactorer.getValue()!=null){
                descBuilder.append(propDevManufactorer.getValue());
            }
            if (propDevName.getValue()!=null && !propDevName.getValue().isEmpty()){
                if (descBuilder.length()>0){
                    descBuilder.append(' ');
                }
                descBuilder.append(propDevName.getValue());
            }
            return new WiaDevice(environment, propDevId.getValue(), descBuilder.toString());
        }else if (environment.getTracer().getProfile().getEventSourceSeverity(EEventSource.IAD.getValue())==EEventSeverity.DEBUG){
            final MessageProvider mp = environment.getMessageProvider();
            final String deviceProps;
            try{
                deviceProps = WiaDevice.readProperties(propertyStorage);
            }catch(ComException exception){
                final String traceMessageTemplate = 
                    mp.translate("IAD", "Failed to read WIA item properties:\n%1$s\n%2$s");
                final String exceptionStack = ClientException.exceptionStackToString(exception);
                final String traceMessage = 
                    String.format(traceMessageTemplate, exception.getMessage(), exceptionStack);
                environment.getTracer().put(EEventSeverity.DEBUG, traceMessage, EEventSource.IAD);
                return null;
            }
            final String traceMessageTemplate = 
                mp.translate("IAD", "Unable to get device identifier. Device properties:\n%1$s");
            final String traceMessage = 
                String.format(traceMessageTemplate, deviceProps);
            environment.getTracer().put(EEventSeverity.DEBUG, traceMessage, EEventSource.IAD);
        }        
        return null;
    }
    
    @Override
    protected void releaseImpl(){
        if (library!=null){
            library.close();
        }
    }
}
