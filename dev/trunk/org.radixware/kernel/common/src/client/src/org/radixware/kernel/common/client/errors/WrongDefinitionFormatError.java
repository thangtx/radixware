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

import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;


public final class WrongDefinitionFormatError extends DefinitionError {

    static final long serialVersionUID = -4634382059138633038L;
    private final static String TITLE = "Definition #%s has wrong format";

    public WrongDefinitionFormatError(final Id id) {
        super(String.format(TITLE, id.toString()));
    }

    public WrongDefinitionFormatError(final Id id, final String describtion) {
        super(String.format(TITLE, id.toString()) + ":\n" + describtion);
    }
}
