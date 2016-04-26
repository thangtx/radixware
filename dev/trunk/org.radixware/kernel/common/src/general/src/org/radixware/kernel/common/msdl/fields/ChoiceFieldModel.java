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

import java.util.List;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.EFieldType;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.MsdlVariantField;
import org.radixware.kernel.common.msdl.MsdlVariantFields;
import org.radixware.kernel.common.msdl.fields.extras.MsdlFieldDescriptorsList;
import org.radixware.kernel.common.msdl.fields.parser.SmioField;
import org.radixware.kernel.common.msdl.fields.parser.SmioFieldChoice;
import org.radixware.schemas.msdl.ChoiceField;
import org.radixware.schemas.msdl.ChoiceFieldVariant;
import org.radixware.schemas.msdl.Field;
import org.radixware.kernel.common.msdl.fields.extras.MsdlFieldDescriptor;


public class ChoiceFieldModel extends TemplateInstanceFieldModel {

    private final StrFieldModel selector;
    private final MsdlVariantFields fields;

    public ChoiceFieldModel(MsdlField container, ChoiceField field) {
        super(container, field);
        selector = new StrFieldModel(container, field.getSelector());
        fields = new MsdlVariantFields();
        add(fields);
        fields.open(field);
    }

    @Override
    public ChoiceField getField() {
        return (ChoiceField) super.getField();
    }

    @Override
    public Field getFullField() {
        ChoiceField f = getField();
        f.getSelector().set(selector.getFullField());
        List<ChoiceFieldVariant> list = f.getVariantList();
        list.clear();
        if (isTemplateInstance()) {
            for (MsdlFieldDescriptor d : getFieldDescriptorList()) {
                MsdlField unwrapped = d.getMsdlField();
                if (unwrapped instanceof MsdlVariantField) {
                    list.add(((MsdlVariantField)unwrapped).getFullVariant());
                }
            }
        } else {
            for (MsdlVariantField cur : fields) {
                list.add(cur.getFullVariant());
            }
        }
        return f;
    }

    public StrFieldModel getSelector() {
        return selector;
    }

    public MsdlVariantFields getFields() {
        return fields;
    }

    @Override
    public EFieldType getType() {
        return EFieldType.CHOICE;
    }

    @Override
    public SmioField getParser() {
        if (parser == null) {
            try {
                parser = new SmioFieldChoice(this);
            } catch (SmioException e) {
                LogFactory.getLog(ChoiceFieldModel.class).warn(String.format("Could not create ChoiceFieldParse: %s; Id: %s", getName(), getDefinition().getId().toString()));
            }
        }
        return parser;
    }

    @Override
    public void clearParser() {
        super.clearParser();
        for (MsdlField cur : fields) {
            cur.getFieldModel().clearParser();
        }
    }

    @Override
    public MsdlFieldDescriptorsList getFieldDescriptorList() {
        MsdlFieldDescriptorsList ret = new MsdlFieldDescriptorsList();
        for (MsdlVariantField f : fields) {
            ret.add(new MsdlFieldDescriptor(f));
        }
        storeTemplaFields(ret);
        return ret;
    }
}
