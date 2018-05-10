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

package org.radixware.kernel.common.client.exceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.sc.FailedSapInfo;
import org.radixware.kernel.common.sc.FailedSapsInfo;


public class AllSapsBusyException extends ServiceCallException {

    static final long serialVersionUID = 3787638345424344349L;
    
    private final FailedSapsInfo failedSapsInfo;

    public AllSapsBusyException(final FailedSapsInfo info, MessageProvider provider) {
        super(provider.translate("ExplorerException", "Can't establish connection to any SAP"));
        failedSapsInfo = info;
    }
    
    public FailedSapsInfo getFailedSapsInfo(){
        return failedSapsInfo;
    }
    
    public String getDetailedMessage(final MessageProvider provider){
        final StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(provider.translate("ClientSessionException", "No suitable service access points found"));
        if (failedSapsInfo!=null){
            final Map<String,Long>  versionMismatch = new HashMap<>(3);
            final List<String> busy = new ArrayList<>(3);
            final List<String> aadcMismatch = new ArrayList<>(3);
            final List<String> unavailable = new ArrayList<>(3);
            for (FailedSapInfo info : failedSapsInfo.getInfos()) {
                if (info.getBusyFaultCode() != null && info.getBusyFaultCode().equals(ServiceProcessFault.FAULT_CODE_SERVER_BUSY_INVALID_VERSION)) {
                    versionMismatch.put(info.getAddress(), info.getAvailableVersion());
                } else if (info.getBusyFaultCode() != null && info.getBusyFaultCode().equals(ServiceProcessFault.FAULT_CODE_SERVER_BUSY)) {
                    busy.add(info.getAddress());
                } else if (info.getAvailable() == null) {
                    aadcMismatch.add(info.getAddress());
                } else {
                    unavailable.add(info.getAddress());
                }
            }
            if (!busy.isEmpty()){
                messageBuilder.append('\n');
                messageBuilder.append(provider.translate("ClientSessionException", "Busy access points:"));
                for (String sap: busy){
                    messageBuilder.append("\n\t");
                    messageBuilder.append(sap);                    
                }
            }
            if (!versionMismatch.isEmpty()){
                messageBuilder.append('\n');
                messageBuilder.append(provider.translate("ClientSessionException", "Access points working with other version:"));
                for (Map.Entry<String,Long> sap: versionMismatch.entrySet()){
                    messageBuilder.append("\n\t");
                    messageBuilder.append(sap.getKey());
                    if (sap.getValue()!=null){
                        messageBuilder.append(String.format(provider.translate("ClientSessionException"," (version: %1$s)"),sap.getValue()));
                    }
                }
            }
            if (!aadcMismatch.isEmpty()){
                messageBuilder.append('\n');
                messageBuilder.append(provider.translate("ClientSessionException", "Access points working with other AADC member:"));
                for (String sap: aadcMismatch){
                    messageBuilder.append("\n\t");
                    messageBuilder.append(sap);
                }
            }
            if (!unavailable.isEmpty()){
                messageBuilder.append('\n');
                messageBuilder.append(provider.translate("ClientSessionException", "Following access points is not responsive:"));
                for (String sap: unavailable){
                    messageBuilder.append("\n\t");
                    messageBuilder.append(sap);
                }
            }
        }
        return messageBuilder.toString();
    }
}
