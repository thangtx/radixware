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

package org.radixware.kernel.common.client.errors;

import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.exceptions.NoSapsForCurrentVersion;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.schemas.eas.ExceptionEnum;


public class UnsupportedDefinitionVersionError extends EasError {

    static final long serialVersionUID = -1392792475033675758L;
    private final MessageProvider msgProvider;
    private final long requiredVersion;
    private final boolean quiet;

    public UnsupportedDefinitionVersionError(final MessageProvider msgProvider, 
                                                                   final ServiceCallFault fault) {
        super("Unsupported definition version",fault);
        this.msgProvider = msgProvider;
        if (RunParams.isDevelopmentMode() || fault.getMessage()==null || fault.getMessage().isEmpty()){
            requiredVersion = -1;
        }else{
            long version;
            try{
                version = Long.parseLong(fault.getMessage());
            }catch(NumberFormatException exception){
                version = -1;
            }
            requiredVersion = version;
        }
        quiet = false;
    }
    
    public UnsupportedDefinitionVersionError(final MessageProvider msgProvider, 
                                                                   final NoSapsForCurrentVersion exception) {
        super("Unsupported definition version", createFaultFromException(exception));
        this.msgProvider = msgProvider;
        this.quiet = !RunParams.isDevelopmentMode() && exception.canWaitForBusySaps();//user was already asked to restart
        requiredVersion = exception.getRequiredVersion();
    }
    
    public UnsupportedDefinitionVersionError(final MessageProvider msgProvider, 
                                                                   final NoSapsForCurrentVersion exception,
                                                                   final boolean isWeb
                                                                   ) {
        super("Unsupported definition version", createFaultFromException(exception));
        this.msgProvider = msgProvider;
        this.quiet = !isWeb && !RunParams.isDevelopmentMode() && exception.canWaitForBusySaps();//user was already asked to restart
        requiredVersion = exception.getRequiredVersion();
    }
    
    
    private static ServiceCallFault createFaultFromException(final NoSapsForCurrentVersion exception){
        return new ServiceCallFault(ExceptionEnum.UNSUPPORTED_DEFINITION_VERSION.toString(), "Unsupported definitions version", null, exception);
    }

    @Override
    public String getMessage() {
        return msgProvider.translate("ExplorerError", "Unsupported definition version");
    }
    
    public boolean processQuiet(){
        return quiet;
    }
    
    public long getRequiredVersion(){
        return requiredVersion;
    }
}
