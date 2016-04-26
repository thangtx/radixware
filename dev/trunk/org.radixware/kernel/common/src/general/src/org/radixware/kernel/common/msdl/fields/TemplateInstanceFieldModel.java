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

import org.radixware.kernel.common.msdl.EFieldType;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.MsdlStructureField;
import org.radixware.kernel.common.msdl.MsdlVariantField;
import org.radixware.kernel.common.msdl.fields.extras.MsdlFieldDescriptor;
import org.radixware.kernel.common.msdl.fields.extras.MsdlFieldDescriptorsList;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.msdl.Field;


public abstract class TemplateInstanceFieldModel extends AbstractFieldModel {

    public TemplateInstanceFieldModel(MsdlField container, Field field) {
        super(container, field);
    }

    public boolean isTemplateInstance() {
        return getField().isSetTemplateFieldPath() && !getField().getTemplateFieldPath().isEmpty();
    }

    public abstract MsdlFieldDescriptorsList getFieldDescriptorList();

    protected void storeTemplaFields(MsdlFieldDescriptorsList currentModelFields) {
        EFieldType currType = getType();
        AbstractFieldModel tfm = getTemplateFieldModel(currType);
        if (tfm != null) {
            MsdlFieldDescriptorsList temporary = new MsdlFieldDescriptorsList();

            if (currType == EFieldType.STRUCTURE) {
                for (MsdlStructureField f : ((StructureFieldModel) tfm).getFields()) {
                    MsdlFieldDescriptor w = new MsdlFieldDescriptor(f);
                    w.setInstanceField(true);
                    temporary.add(w);
                }
            } else if (currType == EFieldType.CHOICE) {
                for (MsdlVariantField f : ((ChoiceFieldModel) tfm).getFields()) {
                    MsdlFieldDescriptor w = new MsdlFieldDescriptor(f);
                    w.setInstanceField(true);
                    temporary.add(w);
                }
            }

            temporary.reorder(this);
            temporary.handleOverriden(currentModelFields);
            temporary.handleRemovedFields(this);

            for (MsdlFieldDescriptor w : temporary) {
                currentModelFields.add(w);
            }
        }
    }

    private AbstractFieldModel getTemplateFieldModel(EFieldType type) {
        ISchemeSearcher searcher = getMsdlField().getRootMsdlScheme().getSchemeSearcher();
        AbstractFieldModel tfm = searcher.findField(Id.Factory.loadFrom(getField().getTemplateSchemeId()),
                getField().getTemplateFieldPath(),
                type);

        return tfm;
    }
}
