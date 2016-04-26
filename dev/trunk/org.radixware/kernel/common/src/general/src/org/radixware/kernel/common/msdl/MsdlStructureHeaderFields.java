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

package org.radixware.kernel.common.msdl;

import java.util.List;
import org.radixware.kernel.common.defs.ClipboardSupport.CanPasteResult;
import org.radixware.kernel.common.defs.ClipboardSupport.DuplicationResolver;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.schemas.msdl.AnyField;
import org.radixware.schemas.msdl.StrField;
import org.radixware.schemas.msdl.Structure;


public class MsdlStructureHeaderFields extends RadixObjects<MsdlField> {

    public MsdlStructureHeaderFields() {
        super("Header Fields");
    }

    public void open(Structure structure) {
        for (AnyField anyField : structure.getHeaderFieldList()) {
            add(new MsdlField(anyField));
        }

    }

    public MsdlField createChild() {
        AnyField created = AnyField.Factory.newInstance();
        StrField strField = created.addNewStr();
        strField.setName("NewField");
        strField.setIsRequired(true);
        return new MsdlField(created);
    }

    @Override
    protected CanPasteResult canPaste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {
        boolean result = true;
        for (Transfer cur : objectsInClipboard) {
            if (!(cur.getObject() instanceof MsdlField)) {
                result = false;
            }
        }
        return result ? CanPasteResult.YES : CanPasteResult.NO;
    }

    @Override
    public RadixIcon getIcon() {
        return MsdlIcon.MSDL_SCHEME_FIELDS;
    }

    @Override
    protected boolean isQualifiedNamePart() {
        return false;
    }
}
