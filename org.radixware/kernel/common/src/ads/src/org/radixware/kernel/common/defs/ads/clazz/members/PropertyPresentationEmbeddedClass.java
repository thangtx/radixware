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

import java.util.EnumSet;
import java.util.Set;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsEmbeddedClassDef;
import org.radixware.kernel.common.defs.ads.clazz.enumeration.EnumFieldEmbeddedClass;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.clazz.AdsClassWriter;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ClassDefinition;


public class PropertyPresentationEmbeddedClass extends AdsEmbeddedClassDef {

    public static final class Factory {

        private Factory() {
        }

        public static PropertyPresentationEmbeddedClass newInstance(AdsPropertyPresentationPropertyDef container) {
            return new PropertyPresentationEmbeddedClass(container,
                    Id.Factory.newInstance(EDefinitionIdPrefix.ADS_ENUM_CLASS), "EmbeddedClass");
        }

        public static PropertyPresentationEmbeddedClass newInstance(AdsPropertyPresentationPropertyDef container, Id id) {
            return new PropertyPresentationEmbeddedClass(container, id, "EmbeddedClass");
        }

        public static PropertyPresentationEmbeddedClass newInstance(AdsPropertyPresentationPropertyDef container, ClassDefinition classDef) {
            return new PropertyPresentationEmbeddedClass(container, classDef);
        }

        public static PropertyPresentationEmbeddedClass newTemporaryInstance(AdsPropertyPresentationPropertyDef container) {
            final AdsClassDef embeddedClass = container.findEmbeddedClass(ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE).get();
            if (embeddedClass != null) {
                return PropertyPresentationEmbeddedClass.Factory.newInstance(container, embeddedClass.getId());
            }
            return newInstance(container);
        }
    }

    public PropertyPresentationEmbeddedClass(AdsPropertyPresentationPropertyDef container, ClassDefinition xClass) {
        super(xClass);

        setContainer(container);
    }

    public PropertyPresentationEmbeddedClass(AdsPropertyPresentationPropertyDef container, Id id, String name) {
        super(id, name);

        setContainer(container);
    }

    public PropertyPresentationEmbeddedClass(AdsPropertyPresentationPropertyDef container, AdsClassDef source) {
        super(source);

        setContainer(container);
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.DYNAMIC;
    }

    @Override
    public boolean isDual() {
        return getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        return EnumSet.of(ERuntimeEnvironmentType.COMMON_CLIENT, ERuntimeEnvironmentType.EXPLORER, ERuntimeEnvironmentType.WEB);
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public JavaSourceSupport.CodeWriter getCodeWriter(JavaSourceSupport.UsagePurpose purpose) {
                return new PropertyPresentationEmbeddedClass.EmbeddedClassCodeWriter(this, PropertyPresentationEmbeddedClass.this, purpose);
            }
        };
    }

    private static class EmbeddedClassCodeWriter extends AdsClassWriter<PropertyPresentationEmbeddedClass> {

        public EmbeddedClassCodeWriter(JavaSourceSupport support, PropertyPresentationEmbeddedClass target, JavaSourceSupport.UsagePurpose usagePurpose) {
            super(support, target, usagePurpose);
        }

        @Override
        public boolean writeCode(CodePrinter printer) {
            if (!def.isUsed()) {
                return true;
            }

            return super.writeCode(printer);
        }

        @Override
        protected boolean writeExecutable(CodePrinter printer) {
            if (!def.isUsed()) {
                return true;
            }

//            printer.println('{');
//            printer.enterBlock();

            writeExecutableBody(printer);

            printer.println();
//            printer.leaveBlock();
//            printer.print('}');

            return true;
        }
    }
}
