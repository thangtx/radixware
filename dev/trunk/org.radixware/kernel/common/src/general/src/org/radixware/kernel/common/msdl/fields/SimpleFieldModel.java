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

import java.util.HashSet;
import java.util.Set;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.schemas.msdl.SimpleField;
import org.radixware.kernel.common.msdl.enums.EEncoding;


public abstract class SimpleFieldModel extends AbstractFieldModel {

    protected Set<String> encodingSet = new HashSet<>();
    
    public SimpleFieldModel(MsdlField container, SimpleField field) {
        super(container,field);
    }

    @Override
    public SimpleField getField() {
        return (SimpleField)super.getField();
    }
    
    public boolean isAcceptableEncoding(EEncoding enc) {
        return !encodingSet.isEmpty() ? encodingSet.contains(enc.getValue()) : true;
    }
}
