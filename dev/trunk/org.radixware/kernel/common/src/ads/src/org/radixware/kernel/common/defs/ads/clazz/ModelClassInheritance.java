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
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsFilterModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsGroupModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportModelClassDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandModelClassDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphModelClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomWidgetModelClassDef;
import org.radixware.kernel.common.defs.ads.ui.AdsDialogModelClassDef;
import org.radixware.kernel.common.defs.ads.ui.AdsPropEditorModelClassDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomDialogDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.RadixObjectError;


abstract class ModelClassInheritance extends Inheritance {

    abstract static class PresentationModelInheritance<T extends AdsPresentationDef> extends ModelClassInheritance {

        protected PresentationModelInheritance(final AdsModelClassDef context, final List<AdsTypeDeclaration> superInterfaces) {
            super(context, superInterfaces);
        }

        @SuppressWarnings("unchecked")
        protected T getOwnerPresentation() {
            return (T) (getOwnerClass().getOwnerDef());
        }

        protected abstract T findBasePresentation(T presentation);

        @Override
        public AdsTypeDeclaration getSuperClassRef() {
            T presentation = getOwnerPresentation();
            if (presentation == null) {
                return getDefaultSuperClass();
            }
            presentation = findBasePresentation(presentation);
            while (presentation != null) {
                if (presentation.isUseDefaultModel()) {
                    presentation = findBasePresentation(presentation);
                } else {
                    return AdsTypeDeclaration.Factory.newInstance(presentation.getModel());
                }
            }
            return getDefaultSuperClass();
        }
    }

    static class EntityModelInheritance extends PresentationModelInheritance<AdsEditorPresentationDef> {

        public EntityModelInheritance(final AdsEntityModelClassDef context, final List<AdsTypeDeclaration> superInterfaces) {
            super(context, superInterfaces);
        }

        @Override
        protected AdsEditorPresentationDef findBasePresentation(final AdsEditorPresentationDef presentation) {
            return presentation.findBaseEditorPresentation().get();
        }

        @Override
        protected String getDefaultSuperClassJavaClassName() {
            return AdsModelClassDef.ENTITY_MODEL_JAVA_CLASS_NAME;
        }
    }

    static class GroupModelInheritance extends PresentationModelInheritance<AdsSelectorPresentationDef> {

        public GroupModelInheritance(final AdsGroupModelClassDef context, final List<AdsTypeDeclaration> superInterfaces) {
            super(context, superInterfaces);
        }

        @Override
        protected AdsSelectorPresentationDef findBasePresentation(final AdsSelectorPresentationDef presentation) {
            return presentation.findBaseSelectorPresentation().get();
        }

        @Override
        protected String getDefaultSuperClassJavaClassName() {
            return AdsModelClassDef.GROUP_MODEL_JAVA_CLASS_NAME;
        }
    }

    static class ParagraphModelInheritance extends ModelClassInheritance {

        public ParagraphModelInheritance(final AdsParagraphModelClassDef context, final List<AdsTypeDeclaration> superInterfaces) {
            super(context, superInterfaces);
        }

        @Override
        public AdsTypeDeclaration getSuperClassRef() {
            return getDefaultSuperClass();
        }

        @Override
        protected String getDefaultSuperClassJavaClassName() {
            return AdsModelClassDef.PARAGRAPH_MODEL_JAVA_CLASS_NAME;
        }
    }

    static class CommandModelInheritance extends ModelClassInheritance {

        public CommandModelInheritance(AdsCommandModelClassDef context, List<AdsTypeDeclaration> superInterfaces) {
            super(context, superInterfaces);
        }

        @Override
        public AdsTypeDeclaration getSuperClassRef() {
            return getDefaultSuperClass();
        }

        @Override
        protected AdsTypeDeclaration getDefaultSuperClass() {
            AdsCommandModelClassDef clazz = (AdsCommandModelClassDef) this.getOwnerClass();
            if (clazz == null) {
                return null;
            }
            AdsModelClassDef ownerModelClass = clazz.findOwnerModelClass();
            if (ownerModelClass == null) {
                return null;
            }
            AdsTypeDeclaration baseClassRef = ownerModelClass.getInheritance().getSuperClassRef();
            if (baseClassRef != null) {
                AdsType baseClass = baseClassRef.resolve(ownerModelClass).get();
                if (baseClass instanceof AdsClassType) {
                    AdsClassDef base = ((AdsClassType) baseClass).getSource();
                    if (base instanceof AdsModelClassDef) {
                        AdsClassDef nestedClass = base.getNestedClasses().findById(clazz.getId(), ExtendableDefinitions.EScope.ALL).get();
                        if (nestedClass instanceof AdsCommandModelClassDef) {
                            return AdsTypeDeclaration.Factory.newInstance(nestedClass);
                        }
                    }
                }
            }

            return AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.common.client.models.items.Command");
        }

        @Override
        public List<AdsClassDef> findDirectSubclasses() {
            return super.findDirectSubclasses(); //To change body of generated methods, choose Tools | Templates.
        }
    }

    static class FormModelClassInheritance extends ModelClassInheritance {

        public FormModelClassInheritance(final AdsFormModelClassDef context, final List<AdsTypeDeclaration> superInterfaces) {
            super(context, superInterfaces);
        }

        @Override
        public AdsTypeDeclaration getSuperClassRef() {
            final AdsFormHandlerClassDef clazz = (AdsFormHandlerClassDef) getOwnerClass().getOwnerDef();
            if (clazz != null) {
                final AdsClassDef superForm = clazz.getInheritance().findSuperClass().get();

                if (superForm instanceof AdsFormHandlerClassDef) {
                    return AdsTypeDeclaration.Factory.newInstance(((AdsFormHandlerClassDef) superForm).getPresentations().getModel());
                }
            }
            return getDefaultSuperClass();
        }

        @Override
        protected String getDefaultSuperClassJavaClassName() {
            return AdsModelClassDef.FORM_MODEL_JAVA_CLASS_NAME;
        }
    }

    static class ReportModelClassInheritance extends ModelClassInheritance {

        public ReportModelClassInheritance(final AdsReportModelClassDef context, final List<AdsTypeDeclaration> superInterfaces) {
            super(context, superInterfaces);
        }

        @Override
        public AdsTypeDeclaration getSuperClassRef() {
//            final AdsReportClassDef clazz = (AdsReportClassDef) getOwnerClass().getOwnerDef();
//            if (clazz != null) {
//                AdsClassDef superForm = clazz.getInheritance().findSuperClass();
//
//                if (superForm instanceof AdsReportClassDef) {
//                    return AdsTypeDeclaration.Factory.newInstance(((AdsFormHandlerClassDef) superForm).getPresentations().getModel());
//                }
//            }
            //KAV put his tooth that no superclasses for reports are allowed
            return getDefaultSuperClass();
        }

        @Override
        protected String getDefaultSuperClassJavaClassName() {
            return AdsModelClassDef.REPORT_MODEL_JAVA_CLASS_NAME;
        }
    }

    static class FilterModelClassInheritance extends ModelClassInheritance {

        public FilterModelClassInheritance(final AdsFilterModelClassDef context, final List<AdsTypeDeclaration> superInterfaces) {
            super(context, superInterfaces);
        }

        @Override
        public AdsTypeDeclaration getSuperClassRef() {
//            final AdsReportClassDef clazz = (AdsReportClassDef) getOwnerClass().getOwnerDef();
//            if (clazz != null) {
//                AdsClassDef superForm = clazz.getInheritance().findSuperClass();
//
//                if (superForm instanceof AdsReportClassDef) {
//                    return AdsTypeDeclaration.Factory.newInstance(((AdsFormHandlerClassDef) superForm).getPresentations().getModel());
//                }
//            }
            //KAV put his tooth that no superclasses for reports are allowed
            return getDefaultSuperClass();
        }

        @Override
        protected String getDefaultSuperClassJavaClassName() {
            return AdsModelClassDef.FILTER_MODEL_JAVA_CLASS_NAME;
        }
    }

    static class DialogModelClassInheritance extends ModelClassInheritance {

        public DialogModelClassInheritance(final AdsDialogModelClassDef context, final List<AdsTypeDeclaration> superInterfaces) {
            super(context, superInterfaces);
        }

        @Override
        public AdsTypeDeclaration getSuperClassRef() {
            return getDefaultSuperClass();
        }

        @Override
        protected String getDefaultSuperClassJavaClassName() {
            return AdsModelClassDef.DIALOG_MODEL_JAVA_CLASS_NAME;
        }
    }

    static class PropEditorModelClassInheritance extends ModelClassInheritance {

        public PropEditorModelClassInheritance(final AdsPropEditorModelClassDef context, final List<AdsTypeDeclaration> superInterfaces) {
            super(context, superInterfaces);
        }

        @Override
        public AdsTypeDeclaration getSuperClassRef() {
            return getDefaultSuperClass();
        }

        @Override
        protected String getDefaultSuperClassJavaClassName() {
            return AdsModelClassDef.PROP_EDITOR_MODEL_JAVA_CLASS_NAME;
        }
    }

    static class CustomWidgetModelClassInheritance extends ModelClassInheritance {

        public CustomWidgetModelClassInheritance(final AdsCustomWidgetModelClassDef context, final List<AdsTypeDeclaration> superInterfaces) {
            super(context, superInterfaces);
        }

        @Override
        public AdsTypeDeclaration getSuperClassRef() {
            return getDefaultSuperClass();
        }

        @Override
        protected String getDefaultSuperClassJavaClassName() {
            AdsCustomWidgetModelClassDef ownerClass = (AdsCustomWidgetModelClassDef) getOwnerClass();
            if (ownerClass.getOwnerDialog() instanceof AdsRwtCustomDialogDef) {
                return AdsModelClassDef.WEB_CUSTOM_WIDGET_MODEL_JAVA_CLASS_NAME;
            } else {
                return AdsModelClassDef.CUSTOM_WIDGET_MODEL_JAVA_CLASS_NAME;
            }
        }
    }

    ModelClassInheritance(final AdsClassDef context, final List<AdsTypeDeclaration> superInterfaces) {
        super(context, null, superInterfaces);
    }

    @Override
    public abstract AdsTypeDeclaration getSuperClassRef();

    @Override
    public boolean setSuperClassRef(final AdsTypeDeclaration superClass) {
        throw new RadixObjectError("Superclass setup is not allowed for model classes.", this);
    }

    @Override
    public SuperClassLookupContext createSuperClassLookupContext() {
        return new SuperClassLookupContext() {
            @Override
            public VisitorProvider getTypeSourceProvider(final EValType toRefine) {
                return new VisitorProvider() {
                    @Override
                    public boolean isTarget(final RadixObject object) {
                        return false;
                    }
                };
            }
        };
    }
}
