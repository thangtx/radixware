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
package org.radixware.kernel.server.meta.clazzes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.WrongFormatError;

public final class RadParentPropDef extends RadJoinPropDef {

    private final List<Id> refPropIds;
    private List<IRadRefPropertyDef> refProps = null;
    RadPropDef ppJoinedProp = null;

    public RadParentPropDef(
            final Id id,
            final String name,
            final Id titleId,
            final Id[] refPropIds,
            final Id destPropId,
            final IRadPropAccessor accessor) {
        super(id, name, titleId, destPropId, null, null, false, null, null, null, null, accessor);
        if (refPropIds == null || refPropIds.length == 0) {
            throw new WrongFormatError("Referece properties of parent property list can't be empty", null);
        }
        this.refPropIds = Collections.unmodifiableList(Arrays.asList(refPropIds));
    }

    @Override
    public void link() {
        super.link();
        getJoinedProp();
        getRefProps();
    }

    public final List<IRadRefPropertyDef> getRefProps() {
        if (refProps == null) {
            final IRadRefPropertyDef[] arrRefProps = new IRadRefPropertyDef[getRefPropIds().size()];
            RadClassDef curClassDef = getClassDef();
            RadPropDef prop;
            for (int i = 0; i < arrRefProps.length; i++) {
                prop = curClassDef.getPropById(getRefPropIds().get(i));
                if (!(prop instanceof IRadRefPropertyDef)) {
                    throw new WrongFormatError("Unsupported property type \"" + prop.getClass().getName() + " of property " + prop.getName() + " (#" + prop.getId() + ")\" in references list of parent property \"" + getName() + "\" (#" + getId() + ")", null);
                }
                arrRefProps[i] = (IRadRefPropertyDef) prop;
                curClassDef = getClassDef().getRelease().getClassDef(arrRefProps[i].getDestinationClassId());
            }
            refProps = Collections.unmodifiableList(Arrays.asList(arrRefProps));
        }
        return refProps;
    }

    @Override
    public Id getJoinedClassId() {
        final List<IRadRefPropertyDef> linkedRefProps = getRefProps();
        return getRefProps().get(linkedRefProps.size() - 1).getDestinationClassId();
    }

    @Override
    public Id getJoinedEntityId() {
        final List<IRadRefPropertyDef> linkedRefProps = getRefProps();
        return getRefProps().get(linkedRefProps.size() - 1).getDestinationEntityId();
    }

    /**
     * @return the refPropIds
     */
    public List<Id> getRefPropIds() {
        return refPropIds;
    }

    @Override
    public Id getEnumId() {
        return getJoinedProp().getEnumId();
    }

    @Override
    public EValType getValType() {
        return getJoinedProp().getValType();
    }

    @Override
    RadPropDef getJoinedProp() {//RADIX-5813
        if (ppJoinedProp == null) {
            final RadPropDef propDef = getJoinedClassDef().getPropById(getJoinedPropId());
            if (propDef instanceof RadJoinPropDef && propDef instanceof RadDetailPropDef == false/*RADIX-7585*/) {
                ppJoinedProp = ((RadJoinPropDef) propDef).getJoinedProp();
            } else {
                ppJoinedProp = super.getJoinedProp();
            }
        }
        return ppJoinedProp;
    }

    private RadClassDef getJoinedClassDef() {
        return getClassDef().getRelease().getClassDef(getJoinedClassId());
    }
}
