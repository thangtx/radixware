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

package org.radixware.kernel.designer.ads.editors.msdl.creation.fieldcreation;

import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.MsdlVariantFields;
import org.radixware.kernel.common.resources.icons.RadixIcon;


public class MsdlVariantFieldCreature extends MsdlFieldCreature {

    private MsdlVariantFields owner;

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.MSDL_SCHEME_FIELD;
    }

    public MsdlVariantFieldCreature(MsdlVariantFields owner) {
        super(owner);
        this.owner = owner;
    }

    @Override
    public MsdlField createInstance() {
        return owner.createChild();
    }
}
