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
package org.radixware.kernel.common.defs.ads.ui;

import java.util.EnumSet;
import java.util.Set;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;

import org.radixware.kernel.common.defs.ads.clazz.IModelClassOwner;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomDialogDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomPropEditorDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomWidgetDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.schemas.adsdef.ClassDefinition;

public class AdsDialogModelClassDef extends AdsModelClassDef {

    public static final class Factory {

        public static AdsDialogModelClassDef newInstance(ICustomDialog owner) {
            if (owner instanceof AdsRwtCustomWidgetDef) {
                return new AdsCustomWidgetModelClassDef((AdsRwtCustomWidgetDef) owner);
            } else if (owner instanceof AdsRwtCustomPropEditorDef) {
                return new AdsPropEditorModelClassDef((AdsRwtCustomPropEditorDef) owner);
            } else if (owner instanceof AdsRwtCustomDialogDef) {
                return new AdsDialogModelClassDef((AdsRwtCustomDialogDef) owner);
            } else if (owner instanceof AdsCustomPropEditorDef) {
                return new AdsPropEditorModelClassDef((AdsCustomPropEditorDef) owner);
            } else if (owner instanceof AdsCustomWidgetDef) {
                return new AdsCustomWidgetModelClassDef((AdsCustomWidgetDef) owner);
            } else {
                return new AdsDialogModelClassDef(owner.getDialogDef());
            }
        }

        public static AdsDialogModelClassDef loadFrom(ICustomDialog owner, ClassDefinition xDef) {
            if (xDef == null) {
                return newInstance(owner);
            }
            if (owner instanceof AdsRwtCustomWidgetDef) {
                return new AdsCustomWidgetModelClassDef((AdsRwtCustomWidgetDef) owner, xDef);
            } else if (owner instanceof AdsCustomWidgetDef) {
                return new AdsCustomWidgetModelClassDef((AdsCustomWidgetDef) owner, xDef);
            } else if (owner instanceof AdsCustomPropEditorDef || owner instanceof AdsRwtCustomPropEditorDef) {
                return new AdsPropEditorModelClassDef(owner.getDialogDef(), xDef);
            } else if (owner instanceof AdsCustomDialogDef || owner instanceof AdsRwtCustomDialogDef) {
                return new AdsDialogModelClassDef(owner.getDialogDef(), xDef);
            } else {
                return new AdsDialogModelClassDef(owner.getDialogDef(), xDef);
            }
        }
    }

    protected AdsDialogModelClassDef(AdsDefinition owner, EDefinitionIdPrefix idprefix) {
        super(owner, idprefix);
    }

    AdsDialogModelClassDef(AdsDefinition owner) {
        this(owner, EDefinitionIdPrefix.ADS_DIALOG_MODEL_CLASS);
    }

    AdsDialogModelClassDef(AdsDefinition owner, ClassDefinition xDef) {
        this(owner, xDef, EDefinitionIdPrefix.ADS_DIALOG_MODEL_CLASS);
    }

    AdsDialogModelClassDef(AdsDefinition owner, ClassDefinition xDef, EDefinitionIdPrefix prefix) {
        super(owner, xDef, prefix);
    }

    @Override
    public AdsClassDef findServerSideClasDef() {
        return null;
    }

    @Override
    public boolean canChangePublishing() {
        return false;
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.DIALOG_MODEL;
    }

    public IModelClassOwner getOwnerDialog() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof IModelClassOwner) {
                return (IModelClassOwner) owner;
            }
        }
        return null;
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        IModelClassOwner dlg = getOwnerDialog();
        return dlg == null ? ERuntimeEnvironmentType.COMMON_CLIENT : dlg.getUsageEnvironment();
    }

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        IModelClassOwner dlg = getOwnerDialog();
        return dlg == null ? super.getTypeUsageEnvironments() : EnumSet.of(dlg.getUsageEnvironment());
    }
}
