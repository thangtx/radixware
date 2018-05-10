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

package org.radixware.kernel.common.msdl.fields.parser.piece;

import org.radixware.kernel.common.msdl.fields.parser.SmioField;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;

public class SmioPiece {

    protected SmioField smioField;

    public SmioPiece(SmioField smioField) {
        this.smioField = smioField;
    }

    public IDataSource parse(IDataSource ids) throws IOException, SmioException {
        return ids;
    }

    public ByteBuffer merge(ByteBuffer bf) throws SmioException {
        return bf;
    }

    public void check(RadixObject source, IProblemHandler handler) {

    }
}
