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
package org.radixware.kernel.common.msdl.fields;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import org.radixware.kernel.common.exceptions.SmioError;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.fields.parser.SmioCoder;
import org.radixware.kernel.common.msdl.fields.parser.structure.SmioFieldStructure;
import org.radixware.kernel.common.utils.Hex;

/**
 *
 * @author npopov
 */
public class ExtIdBytesProvider implements IExtIdBytesProvider {

    private final StructureFieldModel model;

    public ExtIdBytesProvider(StructureFieldModel model) {
        this.model = model;
    }

    @Override
    public SmioCoder getCoder() {
        return ((SmioFieldStructure) model.getParser()).getCoder();
    }

    @Override
    public String getExtIdUnit() {
        return model.getExtIdUnit();
    }

    @Override
    public byte[] toBytes(String extIdChars) {
        if (extIdChars != null) {
            try {
                return getCoder().encode(extIdChars);
            } catch (SmioException ex) {
                throw new SmioError("Error on encode extId: " + extIdChars, ex);
            }
        }
        return null;
    }

    @Override
    public String toExtIdChars(byte[] bytes) {
        if (bytes != null) {
            try {
                return getCoder().decode(ByteBuffer.wrap(bytes));
            } catch (SmioException | CharacterCodingException ex) {
                throw new SmioError("Error on decode extId: " + Hex.encode(bytes), ex);
            }
        }
        return null;
    }

}
