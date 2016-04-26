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

import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsPresentationEntityAdapterClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyPresentationPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsCursorClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsStatementClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;


abstract class PredefinedClassInheritance extends Inheritance {

    static class AlgoClassInheritance extends PredefinedClassInheritance {

        public AlgoClassInheritance(AdsAlgoClassDef context, AdsTypeDeclaration superClass, List<AdsTypeDeclaration> superInterfaces) {
            super(context, superClass, superInterfaces);
        }

        @Override
        protected String getDefaultSuperClassJavaClassName() {
            return AdsAlgoClassDef.PLATFORM_CLASS_NAME;
        }

        @Override
        protected Id getPredefinedSuperClassId() {
            return AdsAlgoClassDef.PREDEFINED_ID;
        }
    }

    static class EntityClassInheritance extends PredefinedClassInheritance {

        public EntityClassInheritance(AdsEntityClassDef context, AdsTypeDeclaration superClass, List<AdsTypeDeclaration> superInterfaces) {
            super(context, superClass, superInterfaces);
        }

        @Override
        protected String getDefaultSuperClassJavaClassName() {
            return AdsEntityClassDef.PLATFORM_CLASS_NAME;
        }

        @Override
        protected Id getPredefinedSuperClassId() {
            return AdsEntityClassDef.PREDEFINED_ID;
        }
    }

    static class EntityGroupClassInheritance extends PredefinedClassInheritance {

        public EntityGroupClassInheritance(AdsEntityGroupClassDef context, AdsTypeDeclaration superClass, List<AdsTypeDeclaration> superInterfaces) {
            super(context, superClass, superInterfaces);
        }

        @Override
        protected String getDefaultSuperClassJavaClassName() {
            return AdsEntityGroupClassDef.PLATFORM_CLASS_NAME;
        }

        @Override
        protected Id getPredefinedSuperClassId() {
            return AdsEntityGroupClassDef.PREDEFINED_ID;
        }
    }

    static class CursorClassInheritance extends PredefinedClassInheritance {

        public CursorClassInheritance(AdsCursorClassDef context, AdsTypeDeclaration superClass, List<AdsTypeDeclaration> superInterfaces) {
            super(context, superClass, superInterfaces);
        }

        @Override
        protected String getDefaultSuperClassJavaClassName() {
            return AdsCursorClassDef.PLATFORM_CLASS_NAME;
        }

        @Override
        protected Id getPredefinedSuperClassId() {
            return AdsCursorClassDef.PREDEFINED_ID;
        }
    }

    static class StatementClassInheritance extends PredefinedClassInheritance {

        public StatementClassInheritance(AdsCursorClassDef context, AdsTypeDeclaration superClass, List<AdsTypeDeclaration> superInterfaces) {
            super(context, superClass, superInterfaces);
        }

        @Override
        protected String getDefaultSuperClassJavaClassName() {
            return AdsStatementClassDef.PLATFORM_CLASS_NAME;
        }

        @Override
        protected Id getPredefinedSuperClassId() {
            return AdsStatementClassDef.PREDEFINED_ID;
        }
    }

    static class ReportClassInheritance extends PredefinedClassInheritance {

        public ReportClassInheritance(AdsReportClassDef context, AdsTypeDeclaration superClass, List<AdsTypeDeclaration> superInterfaces) {
            super(context, superClass, superInterfaces);
        }

        @Override
        protected String getDefaultSuperClassJavaClassName() {
            return AdsReportClassDef.PLATFORM_CLASS_NAME;
        }

        @Override
        protected Id getPredefinedSuperClassId() {
            return AdsReportClassDef.PREDEFINED_ID;
        }
    }

    static class FormClassInheritance extends PredefinedClassInheritance {

        public FormClassInheritance(AdsFormHandlerClassDef context, AdsTypeDeclaration superClass, List<AdsTypeDeclaration> superInterfaces) {
            super(context, superClass, superInterfaces);
        }

        @Override
        protected String getDefaultSuperClassJavaClassName() {
            return AdsFormHandlerClassDef.PLATFORM_CLASS_NAME;
        }

        @Override
        protected Id getPredefinedSuperClassId() {
            return AdsFormHandlerClassDef.PREDEFINED_ID;
        }

        @Override
        public SuperClassLookupContext createSuperClassLookupContext() {
            return new DefaultSuperclassLookupContext(EClassType.FORM_HANDLER);
        }

        @Override
        protected boolean ignorePredefinitionRequirements() {
            return true;
        }
    }

    static class PresentationEntityAdapterClassInheritance extends PredefinedClassInheritance {

        public PresentationEntityAdapterClassInheritance(AdsClassDef context, List<AdsTypeDeclaration> superInterfaces) {
            super(context, null, superInterfaces);
        }

        @Override
        public AdsTypeDeclaration getSuperClassRef() {
            AdsPresentationEntityAdapterClassDef ownerClass = (AdsPresentationEntityAdapterClassDef) getOwnerClass();
            AdsEntityObjectClassDef basis = ownerClass.findBasis();
            if (basis == null) {
                return getDefaultSuperClass();
            } else {
                AdsTypeDeclaration.TypeArguments args = AdsTypeDeclaration.TypeArguments.Factory.newInstance(null);
                args.add(AdsTypeDeclaration.TypeArgument.Factory.newInstance(AdsTypeDeclaration.Factory.newInstance(basis)));
                return getDefaultSuperClass().toGenericType(args);
            }
        }

        @Override
        protected String getDefaultSuperClassJavaClassName() {
            return AdsPresentationEntityAdapterClassDef.PLATFORM_ADAPTER_CLASS_NAME;
        }

        @Override
        protected Id getPredefinedSuperClassId() {
            return null;
        }
    }

//    static class EnumClassInheritance extends PredefinedClassInheritance {
//
//        private static final AdsTypeDeclaration ENUM_SUPER_CLASS = AdsTypeDeclaration.Factory.newPlatformClass("java.lang.Enum");
//
//        public EnumClassInheritance(AdsClassDef context) {
//            super(context, null, null);
//        }
//
//        @Override
//        protected Id getPredefinedSuperClassId() {
//            return null;
//        }
//
//        @Override
//        public AdsTypeDeclaration getSuperClassRef() {
//            final TypeArguments arguments = AdsTypeDeclaration.TypeArguments.Factory.newInstance(null);
//            arguments.add(AdsTypeDeclaration.TypeArgument.Factory.newInstance(AdsTypeDeclaration.Factory.newInstance(getOwnerClass())));
//            return ENUM_SUPER_CLASS.toGenericType(arguments);
//        }
//    }
    static class EnumClassFieldInheritance extends PredefinedClassInheritance {

        public EnumClassFieldInheritance(AdsClassDef context) {
            super(context, null, null);
        }

        @Override
        protected Id getPredefinedSuperClassId() {
            return null;
        }

        @Override
        public AdsTypeDeclaration getSuperClassRef() {
            final AdsEnumClassDef enumClass = RadixObjectsUtils.findContainer(this, AdsEnumClassDef.class);
            if (enumClass != null) {
                return AdsTypeDeclaration.Factory.newInstance(enumClass);
            }
            return null;
        }
    }

    static class PresentationPropertyClassInheritance extends PredefinedClassInheritance {

        private String EXPLORER_PROPERTY_CLASS_NAME = "org.radixware.kernel.common.client.models.items.properties.";
        private String PROPERTY = "Property";

        public PresentationPropertyClassInheritance(AdsClassDef context) {
            super(context, null, null);
        }

        @Override
        protected Id getPredefinedSuperClassId() {
            return null;
        }

        @Override
        protected String getDefaultSuperClassJavaClassName() {
            final AdsPropertyPresentationPropertyDef property = RadixObjectsUtils.findContainer(this, AdsPropertyPresentationPropertyDef.class);

            String propName = "SimpleProperty";
            if (property != null) {
                final EValType typeId = property.getValue().getType().getTypeId();
                if (typeId != null) {

                    switch (typeId) {
                        case PARENT_REF:
                            propName = "PropertyRef";
                            break;
                        case ARR_REF:
                            propName = "PropertyArrRef";
                            break;
                        default:
                            propName = PROPERTY + typeId.getName();
                    }
                }
            }
            return EXPLORER_PROPERTY_CLASS_NAME + propName;
        }

        @Override
        public AdsTypeDeclaration getSuperClassRef() {
            return AdsTypeDeclaration.Factory.newPlatformClass(getDefaultSuperClassJavaClassName());
        }
    }

    protected PredefinedClassInheritance(AdsClassDef context, AdsTypeDeclaration superClass, List<AdsTypeDeclaration> superInterfaces) {
        super(context, superClass, superInterfaces);
    }

    @Override
    public AdsTypeDeclaration getSuperClassRef() {
        AdsTypeDeclaration decl = super.getSuperClassRef();
        if (decl == null) {
            return getDefaultSuperClass();
        } else {
            return decl;
        }
    }

    protected abstract Id getPredefinedSuperClassId();

    @Override
    public synchronized boolean setSuperClassRef(AdsTypeDeclaration superClass) {
        if (ignorePredefinitionRequirements()) {
            return super.setSuperClassRef(superClass);
        }
        Id predefinedId = getPredefinedSuperClassId();
        if (predefinedId == null) {
            return false;
        }
        if (superClass != null && superClass.getPath() != null && !superClass.isTypeArgument()) {
            List<Id> path = superClass.getPath().asList();
            if (path.size() == 1 && path.get(0) == predefinedId) {
                return super.setSuperClassRef(superClass);
            }
        }
        return false;
    }

    protected boolean ignorePredefinitionRequirements() {
        return false;
    }

    @Override
    public SuperClassLookupContext createSuperClassLookupContext() {
        return new SuperClassLookupContext() {

            @Override
            public VisitorProvider getTypeSourceProvider(EValType toRefine) {
                return new VisitorProvider() {

                    @Override
                    public boolean isTarget(RadixObject object) {
                        return false;
                    }
                };
            }
        };
    }
}
