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

package org.radixware.kernel.common.exceptions;

/**
 * Ошибка при разборе сообщения
 */
public class SmioException extends RadixPublishedException {

    private static final long serialVersionUID = -6666839925171144974L;
    private String fieldName;

    public SmioException(String mess) {
        super(mess);
    }

    public SmioException(String mess, Throwable cause) {
        super(mess, cause);
    }

    public SmioException(String mess, Throwable cause, String field) {
        super(mess, cause);
        fieldName = field;
    }

    public SmioException(String mess, String field) {
        super(mess);
        fieldName = field;
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String getMessage() {
        if (getCause() != null) {
            if (getCause().getMessage() == null) {
                return super.getMessage();
            } else {
                return super.getMessage() + "\n" + getCause().getMessage();
            }
        } else {
            return super.getMessage();
        }
    }
}
