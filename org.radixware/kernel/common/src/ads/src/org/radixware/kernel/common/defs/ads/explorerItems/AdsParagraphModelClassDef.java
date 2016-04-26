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

package org.radixware.kernel.common.defs.ads.explorerItems;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.enums.EAccess;

import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.schemas.adsdef.ClassDefinition;


public class AdsParagraphModelClassDef extends AdsModelClassDef {

    public static class Factory {

        public static AdsParagraphModelClassDef loadFrom(AdsParagraphExplorerItemDef owner, ClassDefinition xClass) {
            return new AdsParagraphModelClassDef(owner, xClass);
        }

        public static AdsParagraphModelClassDef newInstance(AdsParagraphExplorerItemDef owner) {
            return new AdsParagraphModelClassDef(owner);
        }
    }

    private AdsParagraphModelClassDef(AdsParagraphExplorerItemDef owner, ClassDefinition xDef) {
        super(owner, xDef, EDefinitionIdPrefix.ADS_PARAGRAPH_MODEL_CLASS);
    }

    private AdsParagraphModelClassDef(AdsParagraphExplorerItemDef owner) {
        super(owner, EDefinitionIdPrefix.ADS_PARAGRAPH_MODEL_CLASS);
    }

    public AdsParagraphExplorerItemDef getOwnerParagraph() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsParagraphExplorerItemDef) {
                return (AdsParagraphExplorerItemDef) owner;
            }
        }
        return null;
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.PARAGRAPH_MODEL;
    }

    @Override
    public AdsClassDef findServerSideClasDef() {
        return null;
    }

    @Override
    public boolean isPublished() {
        AdsParagraphExplorerItemDef p = getOwnerParagraph();
        if (p != null) {
            if (p.getAccessMode() == EAccess.PRIVATE && !p.canChangeAccessMode()) {
                return false;
            }
        }
        return super.isPublished();
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        AdsParagraphExplorerItemDef par = getOwnerParagraph();
        if (par != null) {
            return par.getClientEnvironment();
        } else {
            return super.getUsageEnvironment();
        }
    }
}
