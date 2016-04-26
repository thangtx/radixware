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

/*
 * Ошибка в описании структуры сообщения
 */
public class SmioError extends DefinitionError {
	
	private static final long serialVersionUID = -6666839925171144975L;
	
	private String fieldName;
	
	public SmioError(String mess) {
		super(mess);
	}
	
	public SmioError(String mess, Throwable cause) {
        super(mess, cause);
    }
	
	public SmioError(String mess, String field) {
        super(mess + " (field name: '" + field + "')");
        fieldName = field;
    }
	
	public SmioError(String mess, Throwable cause, String field) {
        super(mess + " (field name: '" + field + "')", cause);
        fieldName = field;
    }
	
	public String getFieldName() {
		return fieldName;
	}
	
    @Override
	public String getMessage() {
    	if (getCause() != null)
    		return super.getMessage() + "\n" + getCause().getMessage();
    	else
    		return super.getMessage();
    }
}
