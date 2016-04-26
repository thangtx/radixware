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
package org.radixware.kernel.common.builder.check.ads.clazz.presentations;

import java.text.MessageFormat;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import org.radixware.kernel.common.builder.check.common.RadixObjectChecker;
import static org.radixware.kernel.common.builder.check.common.RadixObjectChecker.error;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ColumnProperty;
import org.radixware.kernel.common.defs.ads.clazz.members.ServerPresentationSupport;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef.Parameter.ParameterEditOptions;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditOptions;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ParentRefPropertyPresentation;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyEditOptions;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyPresentation;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMask;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.ParentRefType;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;

public abstract class EditOptionsChecker<T extends EditOptions> extends RadixObjectChecker<T> {

    public static final class Factory {

        public static EditOptionsChecker newInstance(AdsDefinition owner, AdsTypeDeclaration ownerType, AdsEnumDef enumDef) {
            if (owner.getDefinitionType() == EDefType.CLASS_PROPERTY) {
                return new PropertyEditOptionsChecker(owner, ownerType, enumDef);
            } else if (owner.getDefinitionType() == EDefType.FILTER_PARAM) {
                return new ParamEditOptionsChecker(owner, ownerType, enumDef);
            } else {
                throw new IllegalStateException("Unsupported edit options owner: " + owner.getClass().getName());
            }
        }
    }
    protected final AdsDefinition owner;
    protected final AdsTypeDeclaration ownerType;
    protected final AdsEnumDef enumeration;
    protected final String ownerName;

    public EditOptionsChecker(AdsDefinition owner, AdsTypeDeclaration ownerType, AdsEnumDef enumeration, String ownerName) {
        this.ownerType = ownerType;
        this.owner = owner;
        this.enumeration = enumeration;
        this.ownerName = ownerName;
    }

    @Override
    public void check(T options, IProblemHandler problemHandler) {
        super.check(options, problemHandler);
        EditMask editMask = options.getEditMask();

        if (ownerType != null) {
            ownerType.check(options, problemHandler);
        }
        CheckUtils.checkEditMask(editMask, ownerType.getTypeId(), enumeration, problemHandler, ownerType, ownerName);
        CheckUtils.checkMLStringId(owner, options.getNullValTitleId(), problemHandler, "null display value");
        if (ownerType.getTypeId() != null && ownerType.getTypeId().isArrayType()) {
            CheckUtils.checkMLStringId(owner, options.getEmptyArrayValTitleId(), problemHandler, "empty array display value");
        }

        Set<ERuntimeEnvironmentType> envs = EnumSet.of(ERuntimeEnvironmentType.EXPLORER, ERuntimeEnvironmentType.WEB);
        if (options.isShowDialogButton()) {
            for (ERuntimeEnvironmentType e : envs) {
                if (options.getCustomDialogId(e) != null) {
                    if (options.findCustomDialog(e) == null) {
                        error(owner, problemHandler, "Custom dialog not found: #" + options.getCustomDialogId(e));
                    }
                }
            }
        }
        if (owner instanceof AdsPropertyDef) {
            final ERuntimeEnvironmentType env = options.getEditEnvironment();
            final AdsPropertyDef prop = (AdsPropertyDef) owner;
            if (prop instanceof IAdsPresentableProperty && ((IAdsPresentableProperty) prop).getPresentationSupport() != null
                    && ((IAdsPresentableProperty) prop).getPresentationSupport().getPresentation() != null && ((IAdsPresentableProperty) prop).getPresentationSupport().getPresentation().isPresentable()) {
                if (!prop.isTransferableAsMeta(env)) {
                    if (prop.getAccessFlags().isStatic()) {
                        error(prop, problemHandler, MessageFormat.format("Static property {0} must not be presentable", prop.getQualifiedName()));
                    } else {
                        error(prop, problemHandler, MessageFormat.format("Property {0} can not be editable in client environment " + env + ". Check edit environment or edit possibility for this property", prop.getQualifiedName()));
                    }
                   // options.setEditEnvironment(ERuntimeEnvironmentType.EXPLORER);
                }
            }
        }
    }

    public static void checkPresentationOptionsUsedInClientPart(RadixObjectChecker checker, RadixObject context, ERuntimeEnvironmentType env, AdsDefinition contextProperty, ServerPresentationSupport presentationSupport, IProblemHandler problemHandler) {
        final PropertyPresentation presentation = presentationSupport.getPresentation();
        if (presentation != null && presentation.isPresentable()) {

            final PropertyEditOptions options = presentationSupport.getPresentation().getEditOptions();
            Set<ERuntimeEnvironmentType> envs = env == ERuntimeEnvironmentType.COMMON_CLIENT ? EnumSet.of(ERuntimeEnvironmentType.EXPLORER, ERuntimeEnvironmentType.WEB) : EnumSet.of(env);
            ERuntimeEnvironmentType editEnv = options.getEditEnvironment();
            if (options.isShowDialogButton()) {
                if (options.isCustomEditOnly()) {
                    for (ERuntimeEnvironmentType e : envs) {
                        if (editEnv == ERuntimeEnvironmentType.COMMON_CLIENT || editEnv == e) {
                            if (options.getCustomDialogId(e) == null) {
                                if (context instanceof AdsEditorPresentationDef) {
                                    if (!context.isWarningSuppressed(AdsEditorPresentationDef.Problems.CUSTOM_EDIT_PROPERTY_WITHOUT_EDITOR)) {
                                        warning(context, problemHandler, AdsEditorPresentationDef.Problems.CUSTOM_EDIT_PROPERTY_WITHOUT_EDITOR, contextProperty.getTypeTitle(), contextProperty.getQualifiedName(), e.getName(), context.getQualifiedName());
                                    }
                                } else {
                                    warning(context, problemHandler, contextProperty.getTypeTitle() + " " + contextProperty.getQualifiedName() + " is marked as editable only via custom dialog. But there no custom dialog specified for environment " + e.getName() + ", required by " + context.getQualifiedName());
                                }
                            }
                        }
                    }
                }
            }
            if (presentation instanceof ParentRefPropertyPresentation) {
                ParentRefPropertyPresentation parentRef = (ParentRefPropertyPresentation) presentation;
                if (parentRef.getParentSelect() != null) {
                    AdsSelectorPresentationDef spr = parentRef.getParentSelect().findParentSelectorPresentation();
                    if (spr != null) {
                        ERuntimeEnvironmentType sprEnv = spr.getClientEnvironment();
                        if (sprEnv != ERuntimeEnvironmentType.COMMON_CLIENT && sprEnv != editEnv && options.getEditPossibility() != EEditPossibility.NEVER) {
                            for (ERuntimeEnvironmentType e : envs) {
                                if (e != sprEnv) {
                                    error(context, problemHandler, "Selector presentation " + spr.getQualifiedName() + " used as parent selector presentation of " + contextProperty.getQualifiedName() + " will not be available for client environment " + e.getName());

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static class PropertyEditOptionsChecker extends EditOptionsChecker<PropertyEditOptions> {

        public PropertyEditOptionsChecker(AdsDefinition owner, AdsTypeDeclaration ownerType, AdsEnumDef enumeration) {
            super(owner, ownerType, enumeration, "property");
        }

        @Override
        public Class<? extends RadixObject> getSupportedClass() {
            return PropertyEditOptions.class;
        }

        @Override
        public void check(PropertyEditOptions options, IProblemHandler problemHandler) {
            super.check(options, problemHandler);
            final AdsPropertyDef prop = (AdsPropertyDef) owner;
            if (prop instanceof ColumnProperty) {
                if (((ColumnProperty) prop).getColumnInfo().isPrimaryKey()) {
                    if (options.getEditPossibility() != EEditPossibility.NEVER && options.getEditPossibility() != EEditPossibility.ON_CREATE) {
                        error(prop, problemHandler, "Inadmissible primary key edit possibility. Should be \"Never\" or \"On Create\"");
                    }
                }
            }
            final EValType propTypeId = ownerType.getTypeId();
            if (propTypeId == EValType.PARENT_REF || propTypeId == EValType.ARR_REF || propTypeId == EValType.OBJECT) {
                final List<Id> objectEditorPresentations = options.getObjectEditorPresentations();
                if (objectEditorPresentations != null && !objectEditorPresentations.isEmpty()) {
                    for (final Id id : objectEditorPresentations) {
                        final AdsEditorPresentationDef presentation = options.findObjectEditorPresentation(id);
                        if (presentation == null) {
                            if (propTypeId == EValType.PARENT_REF || propTypeId == EValType.ARR_REF) {
                                error(prop, problemHandler, "Parent editor presentation #" + id + " not found");
                            } else {
                                error(prop, problemHandler, "Object editor presentation #" + id + " not found");
                            }
                        }
                    }
                }
            } else if (propTypeId == EValType.BLOB || propTypeId == EValType.CLOB) {
                if (prop.getUsageEnvironment() == ERuntimeEnvironmentType.SERVER) {
                    if (!options.isReadSeparately()) {
                        if (!prop.isWarningSuppressed(AdsPropertyDef.Problems.READ_LOB_SEPARATELY)) {
                            warning(prop, problemHandler, AdsPropertyDef.Problems.READ_LOB_SEPARATELY);
                        }
                    }
                }
            }
            if (propTypeId.isArrayType()) {
                if (options.getMaxArrayItemCount() >= 0 && options.getMinArrayItemCount() >= 0) {
                    if (options.getMaxArrayItemCount() < options.getMinArrayItemCount()) {
                        error(options, problemHandler, "Maximum size of array must not be less than it's minimum size");
                    }
                }
            }
        }
    }

    private static class ParamEditOptionsChecker extends EditOptionsChecker<AdsFilterDef.Parameter.ParameterEditOptions> {

        public ParamEditOptionsChecker(AdsDefinition owner, AdsTypeDeclaration ownerType, AdsEnumDef enumeration) {
            super(owner, ownerType, enumeration, "filter parameter");
        }

        @Override
        public Class<? extends RadixObject> getSupportedClass() {
            return AdsFilterDef.Parameter.ParameterEditOptions.class;
        }

        @Override
        public void check(ParameterEditOptions options, IProblemHandler problemHandler) {
            super.check(options, problemHandler);

            if (ownerType.getTypeId() == EValType.PARENT_REF) {
                AdsType type = ownerType.resolve(owner).get(AdsDefinitionChecker.<AdsType>getSearchDuplicatesChecker(options, problemHandler));
                if (type instanceof ParentRefType) {
                    AdsEntityObjectClassDef parentRef = ((ParentRefType) type).getSource();
                    if (parentRef == null) {
                        problemHandler.accept(RadixProblem.Factory.newError(owner, "Parent reference can not be resolved"));
                    } else {
                        AdsUtils.checkAccessibility(options, parentRef, false, problemHandler);
                        CheckUtils.checkExportedApiDatails(options, parentRef, problemHandler);
                        if (options.getParentSelectorPresentationId() == null) {
                            problemHandler.accept(RadixProblem.Factory.newError(owner, "Parent selector presentation not specified"));
                        } else {
                            AdsSelectorPresentationDef spr = parentRef.getPresentations().getSelectorPresentations().findById(options.getParentSelectorPresentationId(), EScope.ALL).get();
                            if (spr == null) {
                                problemHandler.accept(RadixProblem.Factory.newError(owner, "Parent selector presentation not found: #" + options.getParentSelectorPresentationId()));
                            }
                        }
                    }
                }
            }
        }
    }
}
