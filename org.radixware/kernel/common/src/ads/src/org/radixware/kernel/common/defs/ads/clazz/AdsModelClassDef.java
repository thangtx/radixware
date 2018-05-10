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

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsFilterModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsGroupModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.IModelPublishableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportModelClassDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphModelClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.schemas.adsdef.ClassDefinition;


public abstract class AdsModelClassDef extends AdsClassDef {

    public static class Problems extends AdsClassDef.Problems {

        public static final int DO_NOT_USE_EMPTY_MODEL = 200000;

        protected Problems(AdsModelClassDef owner, List<Integer> warnings) {
            super(owner, warnings);
        }

        @Override
        public boolean canSuppressWarning(int code) {
            switch (code) {
                case DO_NOT_USE_EMPTY_MODEL:
                    return true;
                default:
                    return super.canSuppressWarning(code);
            }
        }
    }

    @Override
    protected Problems instantiateWarningsSupport(List<Integer> list) {
        return new Problems(this, list);
    }
    public static final String ENTITY_MODEL_JAVA_CLASS_NAME = "org.radixware.kernel.common.client.models.EntityModel";
    public static final String GROUP_MODEL_JAVA_CLASS_NAME = "org.radixware.kernel.common.client.models.GroupModel";
    public static final String FORM_MODEL_JAVA_CLASS_NAME = "org.radixware.kernel.common.client.models.FormModel";
    public static final String DIALOG_MODEL_JAVA_CLASS_NAME = "org.radixware.kernel.common.client.models.DialogModel";
    public static final String FILTER_MODEL_JAVA_CLASS_NAME = "org.radixware.kernel.common.client.models.FilterModel";
    public static final String PROP_EDITOR_MODEL_JAVA_CLASS_NAME = "org.radixware.kernel.common.client.models.PropEditorModel";
    public static final String CUSTOM_WIDGET_MODEL_JAVA_CLASS_NAME = "org.radixware.kernel.explorer.models.CustomWidgetModel";
    public static final String WEB_CUSTOM_WIDGET_MODEL_JAVA_CLASS_NAME = "org.radixware.wps.models.CustomWidgetModel";
    public static final String PARAGRAPH_MODEL_JAVA_CLASS_NAME = "org.radixware.kernel.common.client.models.ParagraphModel";
    public static final String REPORT_MODEL_JAVA_CLASS_NAME = "org.radixware.kernel.common.client.models.ReportParamDialogModel";

    public final String getDefaultModelLocalClassName() {
        return getDefaultModelLocalClassName(getClassDefType());
    }

    public static String getDefaultModelLocalClassName(EClassType ct) {
        switch (ct) {
            case ENTITY_MODEL:
                return "DefaultEntityModel";
            case GROUP_MODEL:
                return "DefaultGroupModel";
            case FORM_MODEL:
                return "DefaultFormModel";
            case REPORT_MODEL:
                return "DefaultReportModel";
            case FILTER_MODEL:
                return "DefaultFilterModel";
            default:
                throw new RadixObjectError("Default models are not supported for class type " + ct.getName());
        }
    }

    public static final class Factory {

        public static AdsModelClassDef loadFrom(AdsDefinition context, ClassDefinition xDef) {
            if (context instanceof AdsEditorPresentationDef) {
                return AdsEntityModelClassDef.Factory.loadFrom((AdsEditorPresentationDef) context, xDef);
            } else if (context instanceof AdsSelectorPresentationDef) {
                return AdsGroupModelClassDef.Factory.loadFrom((AdsSelectorPresentationDef) context, xDef);
            } else if (context instanceof AdsFormHandlerClassDef) {
                return AdsFormModelClassDef.Factory.loadFrom((AdsFormHandlerClassDef) context, xDef);
            } else if (context instanceof AdsReportClassDef) {
                return AdsReportModelClassDef.Factory.loadFrom((AdsReportClassDef) context, xDef);
            } else if (context instanceof AdsFilterDef) {
                return AdsFilterModelClassDef.Factory.loadFrom((AdsFilterDef) context, xDef);
            } else if (context instanceof AdsParagraphExplorerItemDef) {
                return AdsParagraphModelClassDef.Factory.loadFrom((AdsParagraphExplorerItemDef) context, xDef);
            } else {
                throw new DefinitionError("Invalid context for model class: " + (context == null ? "null" : context.getClass().getName()));
            }
        }

        public static AdsModelClassDef newInstance(AdsDefinition context) {
            if (context instanceof AdsEditorPresentationDef) {
                return AdsEntityModelClassDef.Factory.newInstance((AdsEditorPresentationDef) context);
            } else if (context instanceof AdsSelectorPresentationDef) {
                return AdsGroupModelClassDef.Factory.newInstance((AdsSelectorPresentationDef) context);
            } else if (context instanceof AdsFormHandlerClassDef) {
                return AdsFormModelClassDef.Factory.newInstance((AdsFormHandlerClassDef) context);
            } else if (context instanceof AdsReportClassDef) {
                return AdsReportModelClassDef.Factory.newInstance((AdsReportClassDef) context);
            } else if (context instanceof AdsFilterDef) {
                return AdsFilterModelClassDef.Factory.newInstance((AdsFilterDef) context);
            } else if (context instanceof AdsParagraphExplorerItemDef) {
                return AdsParagraphModelClassDef.Factory.newInstance((AdsParagraphExplorerItemDef) context);
            } else {
                throw new DefinitionError("Invalid context for model class: " + (context == null ? "null" : context.getClass().getName()));
            }
        }
    }

    protected AdsModelClassDef(AdsDefinition owner, ClassDefinition xDef, EDefinitionIdPrefix idprefix) {
        super(Id.Factory.changePrefix(owner.getId(), idprefix), xDef);

        setContainer(owner);
    }

    protected AdsModelClassDef(AdsDefinition owner, EDefinitionIdPrefix idprefix) {
        super(Id.Factory.changePrefix(owner.getId(), idprefix), " Model");

        setContainer(owner);
    }

    public abstract AdsClassDef findServerSideClasDef();

    @Override
    public boolean canChangeClientEnvironment() {
        return false;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CLASS_MODEL;
    }

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        return EnumSet.of(ERuntimeEnvironmentType.EXPLORER, ERuntimeEnvironmentType.COMMON_CLIENT, ERuntimeEnvironmentType.WEB);
    }

    public IModelPublishableProperty.Provider findModelPropertyProvider() {
        for (RadixObject container = getContainer(); container != null; container = container.getContainer()) {
            if (container instanceof IModelPublishableProperty.Provider) {
                return (IModelPublishableProperty.Provider) container;
            }
        }
        return null;
    }

//    @Override
//    public EAccess getAccessMode() {
//        AdsDefinition def = getOwnerDef();
//        if (def != null) {
//            EAccess ownerAcc = def.getAccessMode();
//            if (ownerAcc.isLess(getMinimumAccess())) {
//                return getMinimumAccess();
//            } else {
//                return def.getAccessMode();
//            }
//        } else {
//            return EAccess.PUBLIC;
//        }
//    }
//
//    @Override
    @Override
    public EAccess getMinimumAccess() {
//        AdsDefinition def = getOwnerDef();
//        if (def != null) {
//            EAccess ownerAcc = def.getAccessMode();
//            EAccess superAcc = super.getMinimumAccess();
//            if (ownerAcc.isLess(superAcc)) {
//                return superAcc;
//            } else {
//                return ownerAcc;
//            }
//        } else {
//            return super.getMinimumAccess();
//        }
        return EAccess.PRIVATE;
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.CALC;
    }

    @Override
    public String getName() {
        AdsDefinition ownerDef = getOwnerDef();
        if (ownerDef == null) {
            return "Unknown:Model";
        } else {
            return getOwnerDef().getName() + ":Model";
        }
    }

    @Override
    public String getQualifiedName() {
        final RadixObject qualifiedNameOwner = getOwnerForQualifedName();
        if (qualifiedNameOwner != null) {
            return qualifiedNameOwner.getQualifiedName() + ":Model";
        } else {
            return getName();
        }
    }

    @Override
    public String getQualifiedName(RadixObject context) {
        final RadixObject qualifiedNameOwner = getOwnerForQualifedName();
        if (qualifiedNameOwner != null) {
            return qualifiedNameOwner.getQualifiedName(context) + ":Model";
        } else {
            return getName();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Hierarchy<AdsClassDef> getHierarchy() {
        return new PresentationModelHierarchy(this);
    }

    @Override
    public boolean isPublished() {
        AdsDefinition def = getOwnerDef();
        if (def == null) {
            return super.isPublished();
        } else {
            return def.isPublished();
        }
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        return ERuntimeEnvironmentType.COMMON_CLIENT;
    }

    @Override
    public boolean canChangeAccessMode() {
        return true;
    }

    @Override
    public boolean isDual() {
        return getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT;
    }

    @Override
    public ERuntimeEnvironmentType getDocEnvironment() {
        return getClientEnvironment();
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        if (!getInheritance().getInerfaceRefList(ExtendableDefinitions.EScope.ALL).isEmpty()) {
            sb.append("<br>Implements: <br>&nbsp;");
            int countImplements = getInheritance().getInerfaceRefList(ExtendableDefinitions.EScope.ALL).size();
            for (AdsTypeDeclaration implementsRealised : getInheritance().getInerfaceRefList(ExtendableDefinitions.EScope.ALL)) {
                sb.append("<a href=\"\">");
                sb.append(implementsRealised.getQualifiedName(this));
                sb.append("</a>");
                countImplements--;
                if (countImplements > 0) {
                    sb.append(", ");
                }
            }
        }
    }
}
