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

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.clazz.AdsPropertyGroup;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.sqml.ads.AdsSqmlEnvironment;
import org.radixware.kernel.common.utils.ValTypes;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;
import org.radixware.schemas.adsdef.PropertyDefinition;


public class AdsExpressionPropertyDef extends AdsServerSidePropertyDef {

    public static final class Factory {

        public static AdsExpressionPropertyDef newInstance() {
            return new AdsExpressionPropertyDef("newExpresstionProperty");
        }

        public static AdsExpressionPropertyDef newTemporaryInstance(AdsPropertyGroup context) {
            AdsExpressionPropertyDef prop = newInstance();
            prop.setContainer(context);
            return prop;
        }
    }
    AdsSqmlEnvironment env = AdsSqmlEnvironment.Factory.newInstance(this);
    private boolean isInvisibleForArte = false;

    private final class Expression extends Sqml {

        public Expression() {
            super(AdsExpressionPropertyDef.this);
            setEnvironment(env);
        }
    }
    private final Sqml expression = new Expression();

    AdsExpressionPropertyDef(AbstractPropertyDefinition xProp) {
        super(xProp);
        if (xProp instanceof PropertyDefinition) {
            final PropertyDefinition xPropDef = (PropertyDefinition) xProp;
            this.expression.loadFrom(xPropDef.getSqmlExpression());
        }
        if (xProp.isSetIsInvisibleForArte()) {
            this.isInvisibleForArte = xProp.getIsInvisibleForArte();
        } else {
            this.isInvisibleForArte = false;
        }
    }

    @Override
    public boolean canDefineAccessors() {
        return !isInvisibleForArte();
    }

    AdsExpressionPropertyDef(String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.DDS_COLUMN), name);
    }

    AdsExpressionPropertyDef(AdsExpressionPropertyDef source, boolean forOverride) {
        super(source, forOverride);
    }

    @Override
    protected AdsPropertyDef createOvr(boolean forOverride) {
        return new AdsExpressionPropertyDef(this, forOverride);
    }

    @Override
    public EPropNature getNature() {
        return EPropNature.EXPRESSION;
    }

    public Sqml getExpresssion() {
        return expression;
    }

    public boolean isTypeAllowed(EValType type) {
        return ValTypes.INNATE_PROPERTY_TYPES.contains(type);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        this.expression.collectDependences(list);
    }

    @Override
    public void appendTo(PropertyDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        expression.appendTo(xDef.addNewSqmlExpression());

    }

    @Override
    protected void appendHeaderTo(AbstractPropertyDefinition xDef, ESaveMode saveMode) {
        super.appendHeaderTo(xDef, saveMode);
        if (isInvisibleForArte) {
            xDef.setIsInvisibleForArte(true);
        }
    }

    public boolean isInvisibleForArte() {
        return isInvisibleForArte;
    }

    public void setInvisibleForArte(boolean invisible) {
        if (isInvisibleForArte != invisible) {
            isInvisibleForArte = invisible;
            setEditState(EEditState.MODIFIED);
            fireNameChange();
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        expression.visit(visitor, provider);
    }
}
