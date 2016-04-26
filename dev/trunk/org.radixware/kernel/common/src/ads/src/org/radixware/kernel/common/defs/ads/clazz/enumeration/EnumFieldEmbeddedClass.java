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

package org.radixware.kernel.common.defs.ads.clazz.enumeration;

import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsEmbeddedClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsEnumClassFieldDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.clazz.AdsClassWriter;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ClassDefinition;


public class EnumFieldEmbeddedClass extends AdsEmbeddedClassDef {

    public static final class Factory {

        private Factory() {
        }

        public static EnumFieldEmbeddedClass newInstance(AdsEnumClassFieldDef container) {
            return new EnumFieldEmbeddedClass(container,
                Id.Factory.newInstance(EDefinitionIdPrefix.ADS_ENUM_CLASS), "EmbeddedClass");
        }

        public static EnumFieldEmbeddedClass newInstance(AdsEnumClassFieldDef container, Id id) {
            return new EnumFieldEmbeddedClass(container, id, "EmbeddedClass");
        }

        public static EnumFieldEmbeddedClass newInstance(AdsEnumClassFieldDef container, ClassDefinition classDef) {
            return new EnumFieldEmbeddedClass(container, classDef);
        }

        public static EnumFieldEmbeddedClass newTemporaryInstance(AdsEnumClassFieldDef container) {
            final AdsClassDef embeddedClass = container.findEmbeddedClass(ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE).get();
            if (embeddedClass != null) {
                return EnumFieldEmbeddedClass.Factory.newInstance(container, embeddedClass.getId());
            }
            return newInstance(container);
        }
    }

    protected static class EmbeddedClassCodeWriter extends AdsClassWriter<EnumFieldEmbeddedClass> {

        public EmbeddedClassCodeWriter(JavaSourceSupport support, EnumFieldEmbeddedClass target, UsagePurpose usagePurpose) {
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

            printer.println('{');
            printer.enterBlock();

            writeExecutableBody(printer);

            printer.println();
            printer.leaveBlock();
            printer.print('}');

            return true;
        }
    }

    EnumFieldEmbeddedClass(AdsEnumClassFieldDef container, ClassDefinition xClass) {
        super(xClass);

        init(container);
    }

    EnumFieldEmbeddedClass(AdsEnumClassFieldDef container, AdsClassDef source) {
        super(source);

        init(container);
    }

    EnumFieldEmbeddedClass(AdsEnumClassFieldDef container, Id id, String name) {
        super(id, name);

        init(container);
    }

    private void init(AdsEnumClassFieldDef container) {
        setContainer(container);
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {

            @Override
            public JavaSourceSupport.CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new EmbeddedClassCodeWriter(this, EnumFieldEmbeddedClass.this, purpose);
            }
        };
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.ENUMERATION;
    }
}
