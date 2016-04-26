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

package org.radixware.kernel.common.msdl.fields.extras;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.msdl.EFieldType;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;
import org.radixware.kernel.common.msdl.fields.ISchemeSearcher;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;


public class MsdlFieldDescriptor extends RadixObject {

    private final MsdlField field;
    private boolean overridesTemplateField = false;
    private boolean isInstanceField = false;

    public MsdlFieldDescriptor(MsdlField field) {
        this.field = field;
    }

    public MsdlField getMsdlField() {
        return field;
    }

    public MsdlField getTemplateMsdlField() {
        ISchemeSearcher searcher = getMsdlField().getRootMsdlScheme().getSchemeSearcher();
        AbstractFieldModel tfm = searcher.findField(Id.Factory.loadFrom(
                getMsdlField().getFieldModel().getField().getTemplateSchemeId()),
                getMsdlField().getFieldModel().getField().getTemplateFieldPath(),
                EFieldType.STRUCTURE);

        if (tfm == null) {
            return null;
        }
        return tfm.getMsdlField();
    }

    public boolean getOverridesTemplateField() {
        return overridesTemplateField;
    }

    public void setOverridesTemplateField(boolean value) {
        overridesTemplateField = value;
    }

    public void setInstanceField(boolean value) {
        isInstanceField = value;
    }

    public boolean getIsInstanceField() {
        return isInstanceField;
    }

    @Override
    public String getName() {
        return getMsdlField().getName();
    }

    @Override
    public RadixIcon getIcon() {
        return getMsdlField().getIcon();
    }
}
