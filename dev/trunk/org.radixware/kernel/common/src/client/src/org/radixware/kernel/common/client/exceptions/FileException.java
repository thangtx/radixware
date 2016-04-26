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

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.exceptions.RadixPrivateException;

public final class FileException extends RadixPrivateException {

    private static final long serialVersionUID = -7896818094742737986L;

    public enum EExceptionCode{
        NOT_FILE,
        FILE_NOT_FOUND,
        CANT_CREATE,
        CANT_READ,
        CANT_WRITE,
        CANT_DELETE;
    }

    final private EExceptionCode code;
    final private String fileName;

    public FileException(final IClientEnvironment environment, final EExceptionCode code, final String fileName) {
        super(String.format(getExceptionTitle(environment.getMessageProvider(), code), fileName));
        this.code = code;
        this.fileName = fileName;
    }

    public FileException(final IClientEnvironment environment, final EExceptionCode code, final String fileName, Throwable cause) {
        super(String.format(getExceptionTitle(environment.getMessageProvider(), code), fileName), cause);
        this.code = code;
        this.fileName = fileName;
    }

    private static String getExceptionTitle(final MessageProvider msgProvider, final EExceptionCode code){
        switch (code){
            case NOT_FILE:
                return msgProvider.translate("ExplorerException", "\'%s\' is not a file");
            case FILE_NOT_FOUND:
                return msgProvider.translate("ExplorerException", "file \'%s\' not found");
            case CANT_CREATE:
                return msgProvider.translate("ExplorerException", "failed to create file \'%s\'");
            case CANT_READ:
                return msgProvider.translate("ExplorerException", "failed to read file \'%s\'");
            case CANT_WRITE:
                return msgProvider.translate("ExplorerException", "failed to write file \'%s\'");
            case CANT_DELETE:
                return msgProvider.translate("ExplorerException", "failed to delete file \'%s\'");
                
            default:
                return code.name()+"\'%s\'";
        }
    }

    @Override
    public String getMessage() {
        return String.format(super.getMessage(), fileName);
    }

    public String getFileName() {
        return fileName;
    }
}
