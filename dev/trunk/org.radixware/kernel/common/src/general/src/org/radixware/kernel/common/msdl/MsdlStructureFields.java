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

import com.linuxense.javadbf.DBFField;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.ClipboardSupport.CanPasteResult;
import org.radixware.kernel.common.defs.ClipboardSupport.DuplicationResolver;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.schemas.msdl.StrField;
import org.radixware.schemas.msdl.Structure;
import org.radixware.kernel.common.msdl.fields.StructureFieldModel;


public class MsdlStructureFields extends RadixObjects<MsdlStructureField> {

    public MsdlStructureFields() {
        super("Fields");
    }

    public void open(Structure structure) {
        for (Structure.Field f : new ArrayList<>(structure.getFieldList())) {
            add(new MsdlStructureField(f));
        }
    }

    public MsdlStructureField createChild() {
        Structure.Field created = Structure.Field.Factory.newInstance();
        StrField strField = created.addNewStr();
        strField.setName("NewField");
        strField.setIsRequired(true);
        return new MsdlStructureField(created);
    }

    @Override
    protected CanPasteResult canPaste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {
        boolean result = true;
        for (Transfer cur : objectsInClipboard) {
            if (!(cur.getObject() instanceof MsdlStructureField)) {
                result = false;
            }
        }
        return result ? CanPasteResult.YES : CanPasteResult.NO;
    }

    @Override
    public RadixIcon getIcon() {
        return MsdlIcon.MSDL_SCHEME_FIELDS;
    }

    public MsdlField get(String name) {
        for (MsdlField f : this) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }

    public DBFField[] getDBFFields() {
        DBFField fields[] = new DBFField[list().size()];
        int i = 0;
        for (MsdlField cur : this) {
            fields[i] = cur.getFieldModel().getDBFField();
            i++;
        }
        return fields;
    }

    @Override
    protected boolean isQualifiedNamePart() {
        return false;
    }

    @Override
    protected void onRemove(MsdlStructureField f) {
        super.onRemove(f);
        f.getFieldModel().setModified();
    }

    @Override
    protected void onAdd(MsdlStructureField f) {
        super.onRemove(f);
        f.getFieldModel().setModified();
    }

    @Override
    public boolean isEmpty() {
        boolean ret = super.isEmpty();
        if (ret && getContainer() instanceof StructureFieldModel) {
            StructureFieldModel sfm = (StructureFieldModel) getContainer();
            ret = sfm.getFieldDescriptorList().isEmpty();
        }
        return ret;
    }
}
