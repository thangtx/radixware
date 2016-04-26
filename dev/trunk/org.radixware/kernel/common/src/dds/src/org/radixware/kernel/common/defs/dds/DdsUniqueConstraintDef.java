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

package org.radixware.kernel.common.defs.dds;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.enums.EDdsConstraintDbOption;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.resources.icons.RadixIcon;


public class DdsUniqueConstraintDef extends DdsConstraintDef {

    protected DdsUniqueConstraintDef() {
        super(EDefinitionIdPrefix.DDS_UNIQUE_CONSTRAINT);
        this.getDbOptions().add(EDdsConstraintDbOption.RELY);
    }

    protected DdsUniqueConstraintDef(org.radixware.schemas.ddsdef.Index.UniqueConstraint xUc) {
        super(xUc);
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.CONST;
    }

    @Override
    public String getName() {
        return "UniqueConstraint";
    }

    public DdsIndexDef getOwnerIndex() {
        return (DdsIndexDef) getContainer();
    }

    protected void setOwnerIndex(DdsIndexDef ownerIndex) {
        setContainer(ownerIndex);
    }

    @Override
    public boolean isGeneratedInDb() {
        final DdsIndexDef ownerIndex = getOwnerIndex();
        return ownerIndex.isGeneratedInDb();
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsUniqueConstraintDef newInstance() {
            return new DdsUniqueConstraintDef();
        }

        public static DdsUniqueConstraintDef loadFrom(org.radixware.schemas.ddsdef.Index.UniqueConstraint xUc) {
            return new DdsUniqueConstraintDef(xUc);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return DdsDefinitionIcon.UNIQUE_CONSTRAINT;
    }

    @Override
    public String calcAutoDbName() {
        DdsIndexDef index = getOwnerIndex();
        String indexDbName = index.getDbName();
        if (indexDbName.startsWith("IDX_")) {
            if (indexDbName.length() > 4) {
                indexDbName = indexDbName.substring(4);
            } else {
                indexDbName = "";
            }
        }

        if (index instanceof DdsPrimaryKeyDef) {
            return indexDbName;
        } else {
            return DbNameUtils.calcAutoDbName("UNQ", indexDbName);
        }
    }

    private class DdsUcClipboardSupport extends DdsClipboardSupport<DdsUniqueConstraintDef> {

        public DdsUcClipboardSupport() {
            super(DdsUniqueConstraintDef.this);
        }

//        @Override
//        public boolean canCopy() {
//            return !(DdsIndexDef.this instanceof DdsPrimaryKeyDef);
//        }
        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.ddsdef.Index.UniqueConstraint xUc = org.radixware.schemas.ddsdef.Index.UniqueConstraint.Factory.newInstance();
            DdsModelWriter.writeUniqueConstraint(DdsUniqueConstraintDef.this, xUc);
            return xUc;
        }

        @Override
        protected DdsUniqueConstraintDef loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.ddsdef.Index.UniqueConstraint xUc = (org.radixware.schemas.ddsdef.Index.UniqueConstraint) xmlObject;
            return DdsUniqueConstraintDef.Factory.loadFrom(xUc);
        }
    }

    @Override
    public ClipboardSupport<? extends DdsUniqueConstraintDef> getClipboardSupport() {
        return new DdsUcClipboardSupport();
    }
}
