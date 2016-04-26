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

package org.radixware.kernel.designer.common.dialogs.choosetype;

import java.awt.Dialog;
import java.awt.Dimension;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.openide.DialogDisplayer;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration.TypeArgument;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeWizard.WizardModel;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardDescriptor;


public class ChooseType {

    public static final class EmptyTypeFilter implements ITypeFilter {

        private static final EmptyTypeFilter instance = new EmptyTypeFilter();

        private EmptyTypeFilter() {
        }

        public static EmptyTypeFilter getInstance() {
            return instance;
        }

        @Override
        public Definition getContext() {
            return null;
        }

        @Override
        public IAdsTypedObject getTypedObject() {
            return null;
        }

        @Override
        public AdsTypeDeclaration getBaseType() {
            return null;
        }

        @Override
        public boolean acceptType(AdsTypeDeclaration type) {
            return false;
        }

        @Override
        public boolean acceptNature(ETypeNature typeNature) {
            return false;
        }
    }

    public static abstract class AbstractTypeFilter implements ITypeFilter {

        protected final Definition definition;
        protected final IAdsTypedObject typedObject;
        protected final AdsTypeDeclaration baseType;
        private final Set<Object> except = new HashSet<>();

        public AbstractTypeFilter(Definition definition, IAdsTypedObject typedObject, AdsTypeDeclaration baseType) {
            this.definition = definition;
            this.typedObject = typedObject;
            this.baseType = baseType;
        }

        public final void except(Object object) {
            except.add(object);
        }

        public final boolean accept(Object object) {
            return !except.contains(object);
        }

        @Override
        public Definition getContext() {
            return definition;
        }

        @Override
        public IAdsTypedObject getTypedObject() {
            return typedObject;
        }

        @Override
        public AdsTypeDeclaration getBaseType() {
            return baseType;
        }
    }

    public static class DefaultTypeFilter extends AbstractTypeFilter {

        public DefaultTypeFilter(Definition definition, IAdsTypedObject typedObject, AdsTypeDeclaration baseType) {
            super(definition, typedObject, baseType);
        }

        public DefaultTypeFilter(Definition definition, IAdsTypedObject typedObject) {
            this(definition, typedObject, null);
        }

        public DefaultTypeFilter(ITypeFilter filter, AdsTypeDeclaration baseType) {
            this(filter.getContext(), filter.getTypedObject(), baseType);
        }

        public DefaultTypeFilter(ITypeFilter filter) {
            this(filter.getContext(), filter.getTypedObject(), filter.getBaseType());
        }

        @Override
        public boolean acceptNature(ETypeNature typeNature) {
            boolean allow;

            if (!accept(typeNature)) {
                return false;
            }

            switch (typeNature) {
                case JAVA_PRIMITIVE:
                    return typedObject == null || typedObject.isTypeAllowed(EValType.JAVA_TYPE);

                case JAVA_CLASS:
                    return typedObject == null || typedObject.isTypeAllowed(EValType.JAVA_CLASS);

                case TYPE_PARAMETER:
                    AdsClassDef classContext = null;

                    if (definition instanceof AdsClassDef) {
                        classContext = (AdsClassDef) definition;
                    } else if (definition.getOwnerDefinition() instanceof AdsClassDef) {
                        classContext = (AdsClassDef) definition.getOwnerDefinition();
                    } else if (definition instanceof AdsPropertyDef) {
                        classContext = ((AdsPropertyDef) definition).getOwnerClass();
                    }
                    if (classContext != null) {
                        final List<TypeArgument> genericArguments = AdsTypeDeclaration.getAccessibleArguments(classContext);
                        return !genericArguments.isEmpty() && !onlyAnonymus(genericArguments);
                    }
                    return false;

                default:
                    return true;
            }
        }

        @Override
        public boolean acceptType(AdsTypeDeclaration type) {
            if (typedObject != null && type != null && accept(type)) {
                return typedObject.isTypeAllowed(type.getTypeId());
            }
            return false;
        }

        private boolean onlyAnonymus(List<TypeArgument> args) {
            for (TypeArgument a : args) {
                if (!a.getName().equals("?")) {
                    return false;
                }
            }
            return true;
        }
    }

    public static class WrapTypeFilter extends AbstractTypeFilter {

        private final ITypeFilter filter;

        public WrapTypeFilter(ITypeFilter filter) {
            super(filter.getContext(), filter.getTypedObject(), filter.getBaseType());
            this.filter = filter != null ? filter : EmptyTypeFilter.getInstance();
        }

        public WrapTypeFilter(ITypeFilter filter, Definition definition, IAdsTypedObject typedObject, AdsTypeDeclaration baseType) {
            super(definition, typedObject, baseType);
            this.filter = filter != null ? filter : EmptyTypeFilter.getInstance();
        }

        @Override
        public IAdsTypedObject getTypedObject() {
            return typedObject != null ? typedObject : filter.getTypedObject();
        }

        @Override
        public Definition getContext() {
            return definition != null ? definition : filter.getContext();
        }

        @Override
        public AdsTypeDeclaration getBaseType() {
            return baseType != null ? baseType : filter.getBaseType();
        }

        @Override
        public boolean acceptType(AdsTypeDeclaration type) {
            return filter.acceptType(type) && accept(type);
        }

        @Override
        public boolean acceptNature(ETypeNature typeNature) {
            return filter.acceptNature(typeNature) && accept(typeNature);
        }
    }

    private ChooseType() {
    }
    private static final ChooseType instance = new ChooseType();

    public static ChooseType getInstance() {
        return instance;
    }

    AdsTypeDeclaration choose(ETypeWizardMode mode, ITypeFilter filter) {
        final TypeWizard.WizardModel model = new TypeWizard.WizardModel(mode);
        TypeWizard.TypeModel typeModel = new TypeWizard.TypeModel();

        return choose(model, typeModel, filter);
    }

    AdsTypeDeclaration choose(TypeWizard.WizardModel model, TypeWizard.TypeModel typeModel, ITypeFilter filter) {

        final TypeWizard wizard = new TypeWizard(model, typeModel, filter);
        final WizardDescriptor desc = new WizardDescriptor(wizard);
        final Dialog dialog = DialogDisplayer.getDefault().createDialog(desc);


        dialog.setMinimumSize(new Dimension(450, 350));
        dialog.setVisible(true);

        Object descValue = desc.getValue();
        if (descValue != null && descValue.equals(WizardDescriptor.FINISH_OPTION)) {
            return wizard.getChosenType();
        }
        return null;
    }

    public AdsTypeDeclaration chooseType(ITypeFilter filter) {
        return choose(ETypeWizardMode.CHOOSE_TYPE_NATURE, filter);
    }

    public AdsTypeDeclaration editType(ETypeNature nature, ITypeFilter filter) {
        final ETypeWizardMode wizardMode = ETypeWizardMode.getByTypeNature(nature);
        final TypeWizard.WizardModel model = new TypeWizard.WizardModel(wizardMode);
        TypeWizard.TypeModel typeModel = new TypeWizard.TypeModel();
        typeModel.setNature(nature);
        AdsTypeDeclaration type = choose(model, typeModel, filter);
        if (nature == ETypeNature.RADIX_CLASS && type != null) {
            if (filter.getTypedObject() != null && filter.getTypedObject().getType().getTypeId() == EValType.PARENT_REF && type.getTypeId() == EValType.USER_CLASS) {
                type = AdsTypeDeclaration.Factory.newInstance(EValType.PARENT_REF, type.getPath().asArray());
            }
        }
        return type;
    }

    public AdsTypeDeclaration editGenericParameters(AdsTypeDeclaration currType, ITypeFilter filter) {
        final TypeWizard.WizardModel model = new TypeWizard.WizardModel(ETypeWizardMode.EDIT_GENERIC_PARAMETERS);
        TypeWizard.TypeModel typeModel = new TypeWizard.TypeModel();
        if (currType != null) {
            typeModel.setTypeArguments(currType.getGenericArguments());
            typeModel.setType(currType);
        }

        return choose(model, typeModel, filter);
    }

    public AdsTypeDeclaration refineType(AdsTypeDeclaration currType, ITypeFilter typeFilter) {

        if (typeFilter != null && typeFilter.getBaseType() != null) {
            AdsTypeDeclaration baseType = typeFilter.getBaseType();

            int dimension = baseType.getArrayDimensionCount();
            if (dimension > 0) {
                baseType = baseType.getArrayItemType();
                if (currType.isArray()) {
                    currType = currType.getArrayItemType();
                }

                WrapTypeFilter wrapTypeFilter = new WrapTypeFilter(typeFilter, null, null, baseType);
                wrapTypeFilter.except(ETypeNature.JAVA_ARRAY);
                typeFilter = wrapTypeFilter;
            }

            ETypeNature typeNature = ETypeNature.getByType(baseType, typeFilter.getContext());

            AdsTypeDeclaration refineType = null;
            switch (typeNature) {
                case JAVA_CLASS:
                    if (AdsTypeDeclaration.isObject(baseType)) {
                        TypeEditDisplayer displayer = new TypeEditDisplayer();
                        refineType = displayer.editType(currType, typeFilter);
                        break;
                    }
                // else break down
//                case RADIX_MODEL:
                case RADIX_CLASS:
                    ETypeWizardMode wizardMode = ETypeWizardMode.getByTypeNature(typeNature);
                    WizardModel wizardModel = new TypeWizard.WizardModel(wizardMode, true);
                    refineType = choose(wizardModel, new TypeWizard.TypeModel(), typeFilter);
                    if (refineType != null) {
                        if (currType.getTypeId() == EValType.PARENT_REF && refineType.getTypeId() == EValType.USER_CLASS) {
                            refineType = AdsTypeDeclaration.Factory.newInstance(EValType.PARENT_REF, refineType.getPath().asArray());
                        }
                    }
                    break;

                default:
                    refineType = baseType;
                    break;
            }

            if (refineType != null) {
                return dimension > 0 ? refineType.toArrayType(dimension) : refineType;
            }
        }
        return currType;
    }
}