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

package org.radixware.kernel.server.instance;

import org.radixware.kernel.common.kerberos.KerberosUtils;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.server.trace.ServerTrace;


public final class KrbLoginEventsPrinter implements KerberosUtils.IKrbLoginEventsPrinter{
    
    private final ServerTrace trace;
    private final String instanceTitle;
    private final String serviceTitle;
    
    public KrbLoginEventsPrinter(final ServerTrace trace, final String instanceTitle, final String serviceTitle){
        this.trace = trace;
        this.instanceTitle = instanceTitle;
        this.serviceTitle = serviceTitle;
    }
    
    
    public KrbLoginEventsPrinter(final ServerTrace trace, final String instanceTitle){
        this(trace,instanceTitle,"EAS service");
    }
    
    @Override
    public void printEvent(final KerberosUtils.EKrbLoginEvent event, final String[] args) {
        switch (event){
            case KEYTAB_FILE_DOES_NOT_EXIST:{
                final String message = String.format(Messages.ERR_KEYTAB_DOES_NOT_EXIST, args[0]);
                trace.put(event.getSeverity(), message, Messages.MLS_ID_ERR_KEYTAB_DOES_NOT_EXIST, new ArrStr(args[0],instanceTitle), EEventSource.INSTANCE, false);
                break;
            }
            case USING_DEFAULT_KEY_TAB_FILE:{
                final String message = String.format("Path to keytab file is not defined for instance \"%1s\". Default keytab file will be used", instanceTitle);
                trace.debug(message, EEventSource.INSTANCE, false);
                break;
            }
            case KEYTAB_FILE_FOUND:{
                final String message = "For instance \"%1s\" keytab file path is \"%2s\"";
                trace.debug(String.format(message, instanceTitle, args[0]), EEventSource.INSTANCE, false);
                break;
            }
            case WRONG_SPN_FORMAT:{
                final String message = String.format(Messages.ERR_WRONG_SPN_FORMAT, args[0], args[1]);
                trace.put(event.getSeverity(), message, Messages.MLS_ID_ERR_WRONG_SPN_FORMAT, new ArrStr(args[0],args[1]), EEventSource.INSTANCE, false);                
                break;
            }
            case SPN_TO_USE:{
                final String message = String.format("%1s principal name is \"%2s\"",serviceTitle, args[0]);
                trace.debug(message, EEventSource.INSTANCE, false);
                break;
            }
            case NO_KEYS:{
                final String message = "For instance \"%1s\" and service \"%2s\" no keys found in keytab \"%3s\"";
                trace.debug(String.format(message, instanceTitle, args[0], args[1]), EEventSource.INSTANCE, false);
                break;
            }
            case SOME_KEYS_FOUND:{
                final String message = "For instance \"%1s\" and service \"%2s\" following keys was found in keytab \"%3s\":\n%4s";
                trace.debug(String.format(message, instanceTitle, args[0], args[1], args[2]), EEventSource.INSTANCE, false);
                break;
            }
            case SUPPORTED_ETYPES:{
                final String message = "Supported encryption types for intstance \"%1s\":\n%2s";
                trace.debug(String.format(message, instanceTitle, args[0]), EEventSource.INSTANCE, false);
                break;
            }
            case NO_COMPATIBLE_KEY:{
                final String message = String.format(Messages.WARN_NO_COMPATIBLE_KEY_IN_KEYTAB, args[1], args[0]);
                trace.put(event.getSeverity(), message, Messages.MLS_ID_WARN_NO_COMPATIBLE_KEY_IN_KEYTAB, new ArrStr(args[1],instanceTitle,args[0]), EEventSource.INSTANCE, false);
                break;
            }
            case ERROR_ON_LOGIN:{
                final String message = String.format(Messages.ERR_ON_KDC_LOGIN, args[0], args[1]);
                trace.put(event.getSeverity(), message, Messages.MLS_ID_ERR_ON_KDC_LOGIN, new ArrStr(instanceTitle, args[0], args[1]), EEventSource.INSTANCE, false);
                break;
            }
            case CANT_GET_GSS_CREDENTIALS:{
                final String message = String.format(Messages.ERR_CANT_GET_GSS_CREDENTIALS, args[0], args[1]);
                trace.put(event.getSeverity(), message, Messages.MLS_ID_ERR_CANT_GET_GSS_CREDENTIALS, new ArrStr(instanceTitle, args[0], args[1]), EEventSource.INSTANCE, false);
                break;
            }
            default:{
                trace.put(EEventSeverity.DEBUG, event.name(), EEventSource.INSTANCE);
            }
        }
    }    
}
