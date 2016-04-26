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
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.msdl.fields.ChoiceFieldModel;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.schemas.msdl.AnyField;
import org.radixware.schemas.msdl.ChoiceField;
import org.radixware.schemas.msdl.ChoiceFieldVariant;
import org.radixware.schemas.msdl.StrField;


public class MsdlVariantFields extends RadixObjects<MsdlVariantField> {

    public MsdlVariantFields() {
        super("Variants");
    }

    public void open(ChoiceField field) {
        for (ChoiceFieldVariant v : field.getVariantList()) {
            add(new MsdlVariantField(v));
        }
    }

    public MsdlVariantField createChild() {
        ChoiceFieldVariant variant = ChoiceFieldVariant.Factory.newInstance();
        AnyField created = variant.addNewField();
        StrField strField = created.addNewStr();
        strField.setName("NewField");
        strField.setIsRequired(true);
        return new MsdlVariantField(variant);
    }

    @Override
    protected CanPasteResult canPaste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {
        boolean result = true;
        for (Transfer cur : objectsInClipboard) {
            if (!(cur.getObject() instanceof MsdlVariantField)) {
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
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
    }

    @Override
    protected boolean isQualifiedNamePart() {
        return false;
    }
    
    @Override
    public boolean isEmpty() {
        boolean ret = super.isEmpty();
        if(ret && getContainer() instanceof ChoiceFieldModel) {
            ChoiceFieldModel cfm = (ChoiceFieldModel)getContainer();
            ret = cfm.getFieldDescriptorList().isEmpty();
        }
        return ret;
    }
}
