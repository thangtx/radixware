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

package org.radixware.wps;

import java.util.logging.Logger;
import org.radixware.kernel.common.kerberos.KerberosUtils;
import static org.radixware.kernel.common.kerberos.KerberosUtils.EKrbLoginEvent.CANT_GET_GSS_CREDENTIALS;
import static org.radixware.kernel.common.kerberos.KerberosUtils.EKrbLoginEvent.ERROR_ON_LOGIN;
import static org.radixware.kernel.common.kerberos.KerberosUtils.EKrbLoginEvent.KEYTAB_FILE_DOES_NOT_EXIST;
import static org.radixware.kernel.common.kerberos.KerberosUtils.EKrbLoginEvent.KEYTAB_FILE_FOUND;
import static org.radixware.kernel.common.kerberos.KerberosUtils.EKrbLoginEvent.NO_COMPATIBLE_KEY;
import static org.radixware.kernel.common.kerberos.KerberosUtils.EKrbLoginEvent.NO_KEYS;
import static org.radixware.kernel.common.kerberos.KerberosUtils.EKrbLoginEvent.SOME_KEYS_FOUND;
import static org.radixware.kernel.common.kerberos.KerberosUtils.EKrbLoginEvent.SPN_TO_USE;
import static org.radixware.kernel.common.kerberos.KerberosUtils.EKrbLoginEvent.SUPPORTED_ETYPES;
import static org.radixware.kernel.common.kerberos.KerberosUtils.EKrbLoginEvent.USING_DEFAULT_KEY_TAB_FILE;
import static org.radixware.kernel.common.kerberos.KerberosUtils.EKrbLoginEvent.WRONG_SPN_FORMAT;


final class KrbLoginEventsPrinter implements KerberosUtils.IKrbLoginEventsPrinter{
    
    private final Logger logger;
    
    public KrbLoginEventsPrinter(final Logger logger){
        this.logger = logger;
    }

    @Override
    public void printEvent(final KerberosUtils.EKrbLoginEvent event, final String[] args) {
        switch (event){
            case KEYTAB_FILE_DOES_NOT_EXIST:{
                final String message = String.format("Keytab file \"%1s\" does not exist.\nKerberos authentication will be disabled", args[0]);
                logger.severe(message);
                break;
            }
            case USING_DEFAULT_KEY_TAB_FILE:{
                logger.info("Path to keytab file is not defined. Default keytab file will be used");
                break;
            }
            case KEYTAB_FILE_FOUND:{
                final String message = String.format("keytab file path is \"%1s\"",args[0]);
                logger.info(message);
                break;
            }
            case WRONG_SPN_FORMAT:{
                final String message = 
                    "Wrong format of service principal name \"%1s\": %2s.\nKerberos authentication will be disabled";
                logger.severe(String.format(message,args[0], args[1]));
                break;
            }
            case SPN_TO_USE:{
                final String message = String.format("EAS service principal name is \"%1s\"", args[0]);
                logger.info(message);
                break;
            }
            case NO_KEYS:{
                final String message = 
                    String.format("For service \"%1s\" no keys found in keytab \"%2s\"",args[0], args[1]);
                logger.info(message);
                break;
            }
            case SOME_KEYS_FOUND:{
                final String message = 
                    "For service \"%1s\" following keys was found in keytab \"%2s\":\n%3s";
                logger.info(String.format(message,args[0], args[1], args[2]));                
                break;
            }
            case SUPPORTED_ETYPES:{
                final String message = String.format("Supported encryption types:\n%1s",args[0]);
                logger.info(message);
                break;
            }
            case NO_COMPATIBLE_KEY:{
                final String message = "Keytab file \"%1s\" does not contain any compatible key for \"%2s\" service";
                logger.warning(String.format(message,args[1],args[0]));
                break;
            }
            case ERROR_ON_LOGIN:{                
                final String message = 
                    "Error on login to key distribution center as service \"%1s\":\n%2s\nKerberos authentication will be disabled";                
                logger.severe(String.format(message,args[0],args[1]));
                break;
            }
            case CANT_GET_GSS_CREDENTIALS:{
                final String message = 
                    "Can't get credentials for service \"%1s\":\n%2s\nKerberos authentication will be disabled";
                logger.severe(String.format(message,args[0],args[1]));
                break;
            }
            default:{
                logger.finest(event.name());
            }
        }
    }
    
}
