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
package org.radixware.kernel.common.defs.ads.clazz.members;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsPropertyGroup;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import static org.radixware.kernel.common.enums.EClassType.DYNAMIC;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.utils.ValTypes;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;
import org.radixware.schemas.adsdef.PropertyDefinition;

public class AdsDynamicPropertyDef extends AdsPropertyDef implements IAdsPresentableProperty {

    private final ServerPresentationSupport presentationSupport;

    @Override
    public ServerPresentationSupport getPresentationSupport() {
        if (getContainer() instanceof AdsPropertyPresentationPropertyDef) {
            return presentationSupport;
        }
        if (getUsageEnvironment() == ERuntimeEnvironmentType.SERVER || !isInBranch()) {
            AdsClassDef ownerClass = getOwnerClass();
            if (ownerClass != null) {
                switch (ownerClass.getClassDefType()) {
                    case DYNAMIC:
                    case EXCEPTION:
                    case INTERFACE:
                        return null;
                }
            }
            return presentationSupport;
        } else {
            return null;
        }
    }

    public static final class Factory {

        private Factory() {
            super();
        }

        public static AdsDynamicPropertyDef newInstance() {
            return new AdsDynamicPropertyDef("newDynamicProperty");
        }

        public static AdsDynamicPropertyDef newTemporaryInstance(final AdsPropertyGroup context) {
            final AdsDynamicPropertyDef prop = newInstance();
            prop.setContainer(context);
            return prop;
        }
    }

    AdsDynamicPropertyDef(RadixObject owner, AbstractPropertyDefinition xProp) {
        super(xProp);
        if (xProp instanceof PropertyDefinition) {
            presentationSupport = new ServerPresentationSupport(this, (PropertyDefinition) xProp);
        } else {
            presentationSupport = new ServerPresentationSupport(this);
        }
        if (owner != null) {
            setContainer(owner);
        }
    }

    AdsDynamicPropertyDef(String name) {
        this(EDefinitionIdPrefix.ADS_DYNAMIC_PROP, name);
    }

    AdsDynamicPropertyDef(Id id, String name) {
        super(id, name);
        presentationSupport = new ServerPresentationSupport(this);
    }

    AdsDynamicPropertyDef(AdsDynamicPropertyDef source, boolean forOverride) {
        super(source, forOverride);
        presentationSupport = new ServerPresentationSupport(this, source.getPresentationSupport(), forOverride);
    }

    AdsDynamicPropertyDef(RadixObject container, Id id, String name) {
        super(id, name);
        presentationSupport = new ServerPresentationSupport(this);
        if (container != null) {
            setContainer(container);
        }
    }

    AdsDynamicPropertyDef(RadixObject container, AdsDynamicPropertyDef source, boolean forOverride) {
        super(source, forOverride);
        presentationSupport = new ServerPresentationSupport(this, source.getPresentationSupport(), forOverride);
        if (container != null) {
            setContainer(container);
        }
    }

    @Override
    protected AdsPropertyDef createOvr(boolean forOverride) {
        return new AdsDynamicPropertyDef(this, forOverride);
    }

    protected AdsDynamicPropertyDef(EDefinitionIdPrefix prefix, String name) {
        super(Id.Factory.newInstance(prefix), name);
        presentationSupport = new ServerPresentationSupport(this);
    }

    @Override
    public EPropNature getNature() {
        return EPropNature.DYNAMIC;
    }

    @Override
    public String getName() {
        if (getContainer() instanceof AdsPropertyPresentationPropertyDef) {
            return getContainer().getName();
        } else {
            return super.getName(); //To change body of generated methods, choose Tools | Templates.
        }
    }

    @Override
    public void appendTo(final PropertyDefinition xDef, final ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (getUsageEnvironment() == ERuntimeEnvironmentType.SERVER) {
            ServerPresentationSupport support = getPresentationSupport();
            if (support != null) {
                support.appendTo(xDef, saveMode);
            }
        }
    }

    void appendExtractionTo(final AbstractPropertyDefinition xDef, final ESaveMode saveMode) {
        super.appendHeaderTo(xDef, saveMode);
        ServerPresentationSupport support = getPresentationSupport();
        if (support != null) {
            support.appendTo(xDef, saveMode);
        }
    }

    @Override
    public void afterOverride() {
        super.afterOverride();
        getAccessFlags().setAbstract(false);

        if (getUsageEnvironment() == ERuntimeEnvironmentType.SERVER) {
            presentationSupport.afterOverride();
        }
    }

    @Override
    public void afterOverwrite() {
        super.afterOverwrite();
        if (getUsageEnvironment() == ERuntimeEnvironmentType.SERVER) {
            presentationSupport.afterOverwrite();
        }
    }

    @Override
    public boolean isTransferable(ERuntimeEnvironmentType env) {
        if (isSuperTransferable(env)) {
            AdsClassDef clazz = getOwnerClass();
            if (clazz != null) {
                switch (clazz.getClassDefType()) {
                    // case FORM_HANDLER:
                    case REPORT:
                        return false;
                    default:
                        return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean canBeUsedInSqml() {
        AdsTypeDeclaration decl = getValue().getType();
        if (decl == null) {
            return false;
        } else {
            if (decl.getArrayDimensionCount() == 0) {
                return ValTypes.ADS_SQL_CLASS_PARAM_TYPES.contains(decl.getTypeId());
            } else {
                return false;
            }
        }
    }

    protected final boolean isSuperTransferable(ERuntimeEnvironmentType env) {
        return super.isTransferable(env);
    }
}
