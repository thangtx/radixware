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

import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps;


class TypeWizard extends WizardSteps {

    private final Settings settings;

    TypeWizard(WizardModel wizardModel, TypeModel typeModel, ITypeFilter filter) {
        super();

        settings = new Settings(wizardModel, typeModel, filter);
    }

    @Override
    protected final Settings getSettings() {
        return settings;
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(TypeWizard.class, "TypeWizardTitle");
    }

    @Override
    public final Settings createSettings() {
        return settings;
    }

    @Override
    public Step createInitial() {

        switch (settings.wizardModel.getMode()) {
            case CHOOSE_TYPE_NATURE:
                return new ChooseTypeNatureStep();
            case CHOOSE_ITEM_ARRAY_TYPE:
                return new ChooseArrayTypeNatureStep();
            case CHOOSE_RADIX_TYPE:
                return new ChooseRadixTypeStep();
            case CHOOSE_XML_TYPE:
                return new ChooseRadixXmlSchemeStep();
            case CHOOSE_JAVA_CLASS:
                return new ChooseJavaClassStep();
            case CHOOSE_JAVA_TYPE:
                return new ChooseJavaTypeStep();
            case CHOOSE_RADIX_CLASS:
                return new ChooseRadixClassStep();
            case CHOOSE_RADIX_ENUM:
                return new ChooseRadixEnumStep();
            case CHOOSE_RADIX_MODEL:
                return new ChooseModelOwnerStep();
            case CHOOSE_GENERIC_TYPE:
                return new ChooseGenericTypeStep();
            case EDIT_GENERIC_PARAMETERS:
                return new SpecifyGenericArgumentsStep();
            default:
                return new ChooseTypeNatureStep();
        }
    }

    public AdsTypeDeclaration getChosenType() {
        return settings.typeModel.calculate();
    }

    static final class Settings {

        final WizardModel wizardModel;
        final TypeModel typeModel;
        final ITypeFilter filter;

        public Settings(WizardModel wizardModel, TypeModel typeModel, ITypeFilter filter) {
            this.filter = filter;
            this.wizardModel = wizardModel;
            this.typeModel = typeModel;
        }
    }

    public static final class WizardModel {

        private final ETypeWizardMode mode;
        private final boolean refine;

        public WizardModel(ETypeWizardMode mode) {
            this(mode, false);
        }

        public WizardModel(ETypeWizardMode mode, boolean refine) {
            this.mode = mode;
            this.refine = refine;
        }

        public ETypeWizardMode getMode() {
            return mode;
        }

        public boolean isRefine() {
            return refine;
        }
    }

    public static final class TypeModel {

        private int dimension = 0;
        private ETypeNature nature;
        private AdsTypeDeclaration.TypeArguments typeArguments;
        private AdsTypeDeclaration type;
        private Object object;

        private final TypeModel parent;
        private TypeModel subtype;
        private TypeModel current;

        public TypeModel() {
            this(null);
        }
        public TypeModel(TypeModel parent) {
            this.parent = parent;
            current = this;
        }

        public boolean isArray() {
            return getDimension() > 0;
        }

        public boolean isGeneric() {
            return getTypeArguments() != null && !getTypeArguments().isEmpty();
        }

        public void switchToSubtype() {
            current.subtype = new TypeModel();
            current = current.subtype;
        }

        public void switchToParent() {
            current = current.parent;
            assert current != null;
        }

        public void reset() {
            current = this;
            subtype = null;
            setDimension(0);
            setObject(null);
            setNature(null);
            setTypeArguments(null);
            setType(null);
        }

        AdsTypeDeclaration calculate() {
            AdsTypeDeclaration result = getType();

            if (result == null) {
                result = AdsTypeDeclaration.UNDEFINED;
            }

            if (nature == ETypeNature.JAVA_ARRAY && dimension > 0) {
                result = result.toArrayType(dimension);
            }

            return result;
        }

        public Object getObject() {
            return current.object;
        }

        public void setObject(Object object) {
            current.object = object;
        }

        public ETypeNature getNature() {
            return current.nature;
        }

        public void setNature(ETypeNature nature) {
            current.nature = nature;
        }

        public AdsTypeDeclaration.TypeArguments getTypeArguments() {
            return current.typeArguments;
        }

        public void setTypeArguments(AdsTypeDeclaration.TypeArguments typeArguments) {
            current.typeArguments = typeArguments;
        }

        public AdsTypeDeclaration getType() {
            return current.type;
        }

        public void setType(AdsTypeDeclaration type) {
            current.type = type;
        }

        public int getDimension() {
            return current.dimension;
        }

        public void setDimension(int dimension) {
            current.dimension = dimension;
        }
    }
}
