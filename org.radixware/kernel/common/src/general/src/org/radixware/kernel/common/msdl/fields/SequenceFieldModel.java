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
import org.radixware.kernel.common.msdl.fields.parser.SmioField;
import org.radixware.kernel.common.msdl.fields.parser.SmioFieldSequence;
import org.radixware.schemas.msdl.Field;
import org.radixware.schemas.msdl.SequenceField;


public class SequenceFieldModel extends AbstractFieldModel {

    private final MsdlField sequenceItem;

    public SequenceFieldModel(MsdlField container, SequenceField field) {
        super(container, field);
        sequenceItem = new MsdlField(field.getItem());
        add(sequenceItem);
    }

    public MsdlField getItem(){
        return sequenceItem;
    }

    @Override
    public SequenceField getField() {
        return (SequenceField)super.getField();
    }

    @Override
    public Field getFullField() {
        SequenceField sf = getField();
        sf.setItem(sequenceItem.getFullField());
        return sf;
    }

    @Override
    public EFieldType getType() {
        return EFieldType.SEQUENCE;
    }

    @Override
    public SmioField getParser() {
        if (parser == null) {
            parser = new SmioFieldSequence(this);
        }
        return parser;
    }

    @Override
    public void clearParser() {
        super.clearParser();
        getItem().getFieldModel().clearParser();
    }

}