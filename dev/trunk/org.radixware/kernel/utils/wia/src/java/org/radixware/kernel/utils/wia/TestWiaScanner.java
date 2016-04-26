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

package  org.radixware.kernel.utils.wia;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.utils.wia.properties.ComProperty;
import org.radixware.kernel.utils.wia.properties.ComPropertyInt;
import org.radixware.kernel.utils.wia.properties.ComPropertyUUID;
import org.radixware.kernel.utils.wia.properties.EComPropSpecId;
import org.radixware.kernel.utils.wia.properties.WiaPropertyStorage;


public class TestWiaScanner {
    
    private static class ImageReceiver extends WiaDataCallback{        

        @Override
        protected boolean headerCallback(WiaDataCallbackHeader header) throws ComException {
            System.out.println("Data header received \""+header.toString()+"\"");            
            return true;
        }

        @Override
        protected boolean bandedDataCallback(EWiaDataCallbackMessage message, EWiaDataCallbackStatus status, long percentComplete, long offset, long length, ByteBuffer dataFrame) throws ComException {
            final StringBuilder logMessage = new StringBuilder();
            logMessage.append("Message ");
            logMessage.append(message.name());
            logMessage.append(" received:\n");
            if (status==null){
                logMessage.append("\tno status information\n");
            }else{
                logMessage.append("\tcurrent status is ");
                logMessage.append(status.name());
                logMessage.append('\n');
            }
            logMessage.append("\tPercent Complete: ");
            logMessage.append(String.valueOf(percentComplete));
            logMessage.append('\n');
            logMessage.append("\tFragment offset: ");
            logMessage.append(String.valueOf(offset));
            logMessage.append('\n');
            logMessage.append("\tFragment length: ");
            logMessage.append(String.valueOf(length));
            logMessage.append('\n');
            final byte[] array = dataFrame==null ? null : dataFrame.array();
            if (array==null || array.length==0){
                logMessage.append("\tNo data received\n");
            }else{
                logMessage.append("\tReceived ");
                logMessage.append(String.valueOf(array.length));
                logMessage.append(" bytes of data");
            }
            System.out.println(logMessage.toString());
            return true;
        }
    }

    public static void main(final String[] args) throws ComException {
		try (ComLibrary library = ComLibrary.initialize()){
	        try (WiaDeviceManager manager = WiaDeviceManager.newInstance()) {
	            final WiaDeviceDescription description = manager.selectDevice(0, EWiaDeviceType.SCANNER, true);
	            if (description != null) {
	                final WiaRootItem rootItem = description.getRootItem();                
	                if (rootItem != null) {
	                    final EnumSet<EImageIntent> imageIntents = EnumSet.of(EImageIntent.NONE);
	                    final EnumSet<EDeviceDialogFlag> dialogFlags = EnumSet.of(EDeviceDialogFlag.DEFAULT);
						
	                    final WiaItem[] wiaItems = rootItem.deviceDlg(0, dialogFlags, imageIntents);
	                    if (wiaItems != null && wiaItems.length > 0) {
	                        transferWiaItem(wiaItems[0], args.length>0 ? args[0] : null);
	                        for (WiaItem wiaItem : wiaItems) {
	                            wiaItem.close();
	                        }
	                    }
	                    rootItem.close();
	                }
	            }
	        }
		}
    }
    
    public static void transferWiaItem(final WiaItem wiaItem, final String fileName) throws ComException{
        final List<ComProperty> properties = new LinkedList<>();
        properties.add(new ComPropertyUUID(EComPropSpecId.WIA_IPA_FORMAT, EWiaImageFormat.BMP.getGuid()));
        properties.add(new ComPropertyInt(EComPropSpecId.WIA_IPA_TYMED, ETymed.FILE.getValue()));
        try (WiaPropertyStorage propertyStorage = wiaItem.openPropertyStorage()){
            propertyStorage.writeMultiple(properties);
            try (WiaDataTransfer dataTransfer = wiaItem.createDataTransfer()){
                try (FileStorage stgFile = StgMedium.Factory.createFileStorage(fileName)){
                    final ImageReceiver receiver = new ImageReceiver();
                    dataTransfer.idtGetData(stgFile, receiver);
                    System.out.println("Data transfer complete");
                    System.out.println("Transferred filename: "+stgFile.getFileName());
                }
            }
        }
    }
}
