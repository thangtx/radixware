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

package org.radixware.wps.rwt.uploading;

import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.localization.MessageProvider;


public class UploadException extends Exception implements IClientError{
    
    private static final long serialVersionUID = 8558587816403168458L;
    
    private final String fileName;
    
    public UploadException(final String message, final String fileName){
        super(message);
        this.fileName = fileName;
    }    

    @Override
    public String getTitle(MessageProvider messageProvider) {
        return messageProvider.translate("ExplorerMessage", "Failed to Upload");
    }

    @Override
    public String getLocalizedMessage(MessageProvider messageProvider) {
        final String messageTemplate = 
            messageProvider.translate("ExplorerError", "Failed to upload file \'%1$s\'");
        return String.format(messageTemplate, fileName);
    }

    @Override
    public String getDetailMessage(final MessageProvider messageProvider) {
        return getMessage();
    }
    
}
