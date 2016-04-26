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

package org.radixware.kernel.common.defs.ads.clazz;

import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.enumeration.*;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.IdPrefixes;
import org.radixware.schemas.adsdef.ClassDefinition;


public class AdsEnumClassDef extends AdsDynamicClassDef {

    private final AdsEnumClassFieldStructDef fieldStruct;
    private final AdsFields fields;

    public static final class Factory {

        public static AdsEnumClassDef loadFrom(ClassDefinition enumDef) {
            return new AdsEnumClassDef(enumDef);
        }

        public static AdsEnumClassDef newInstance(ERuntimeEnvironmentType env) {
            return new AdsEnumClassDef("NewEnumClass", env);
        }
    }

    protected AdsEnumClassDef(String name, ERuntimeEnvironmentType env) {
        super(EDefinitionIdPrefix.ADS_ENUM_CLASS, name, env);
        fieldStruct = AdsEnumClassFieldStructDef.Factory.newInstance(this);
        fields = AdsFields.Factory.newInstance(this);
    }

    protected AdsEnumClassDef(ClassDefinition xDef) {
        super(xDef);
        fieldStruct = AdsEnumClassFieldStructDef.Factory.loadFrom(this, xDef.getEnumClassFieldStruct());
        fields = AdsFields.Factory.loadFrom(this, xDef.getEnumClassFields());
    }

    protected AdsEnumClassDef(AdsEnumClassDef source) {
        super(source);
        fields = AdsFields.Factory.newInstance(this);
        fieldStruct = AdsEnumClassFieldStructDef.Factory.newInstance(this);
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.ENUMERATION;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CLASS_ENUM;
    }

    @Override
    public String getTypeTitle() {
        return environment.getName() + " Enum Class";
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new ClassJavaSourceSupport() {

            @Override
            public CodeWriter getCodeWriter(final UsagePurpose purpose) {
                return new AdsEnumClassWriter(this, AdsEnumClassDef.this, purpose);
            }
        };
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        fields.visit(visitor, provider);
        fieldStruct.visit(visitor, provider);
    }

    @Override
    public AdsType getType(EValType typeId, String extStr) {
        return AdsEnumClassType.Factory.newInstanse(this);
    }

    @Override
    public void appendTo(ClassDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        fields.appendTo(xDef.addNewEnumClassFields(), saveMode);
        fieldStruct.appendTo(xDef.addNewEnumClassFieldStruct(), saveMode);
    }

    @Override
    public void afterOverwrite() {
        super.afterOverwrite();
        this.getFields().getLocal().clear();
        this.getFieldStruct().getLocal().clear();
    }

    @Override
    public boolean canBeFinal() {
        return false;
    }

    @Override
    public void setFinal(boolean f) {
    }

    @Override
    public SearchResult<? extends AdsDefinition> findComponentDefinition(Id id) {
        if (IdPrefixes.isAdsEnumClassFieldId(id)) {
            return getFields().findById(id, EScope.ALL);
        } else if (IdPrefixes.isAdsEnumClassParameterId(id)) {
            return getFieldStruct().findById(id, EScope.ALL);
        }
        return super.findComponentDefinition(id);
    }

    public AdsEnumClassFieldStructDef getFieldStruct() {
        return fieldStruct;
    }

    public AdsFields getFields() {
        return fields;
    }
}