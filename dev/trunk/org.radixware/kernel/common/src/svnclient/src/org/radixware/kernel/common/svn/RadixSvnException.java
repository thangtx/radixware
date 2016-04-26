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
package org.radixware.kernel.common.svn;

/**
 *
 * @author akrylov
 */
public class RadixSvnException extends Exception {

    public int tag;

    public enum Type {

        NONE,
        IO,
        MALFORMED_DATA,
        FS_NOT_FOUND,
        CANCEL,
        REJECT_AUTH
    }
    private Type type;

    public RadixSvnException() {
    }

    public RadixSvnException(String message) {
        super(message);
    }

    public RadixSvnException(String message, int tag) {
        super(message);
        this.tag = tag;
    }

    public RadixSvnException(String message, Throwable cause) {
        super(message, cause);
    }

    public RadixSvnException(String message, Type type, int tag) {
        super(message);
        this.type = type;
        this.tag = tag;
    }

    public RadixSvnException(Throwable cause) {
        super(cause);
    }

    public RadixSvnException(Throwable cause, Type type) {
        super(cause);
        this.type = type;
    }

    public RadixSvnException(Type type) {
        super(type == Type.CANCEL ? "Operation cancelled" : "");
        this.type = type;
    }

    public RadixSvnException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public int getSvnErrorCode() {
        return 0;
    }

    public boolean isIOError() {
        return type == Type.IO;
    }

    public boolean isFsNotFound() {
        return type == Type.FS_NOT_FOUND;
    }

    public boolean isCancel() {
        return type == Type.CANCEL;
    }

    public boolean isMalformedDataError() {
        return type == Type.MALFORMED_DATA;
    }

    public boolean isAuthenticationCancelled() {
        return type == Type.REJECT_AUTH;
    }

}
