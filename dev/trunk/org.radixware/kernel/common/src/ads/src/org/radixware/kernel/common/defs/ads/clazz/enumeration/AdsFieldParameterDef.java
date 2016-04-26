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

import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.defs.ads.clazz.AdsEnumClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.Value;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.ValTypes;
import org.radixware.schemas.adsdef.DescribedAdsDefinition;
import org.radixware.schemas.adsdef.EnumClassFieldParamDefinition;


public class AdsFieldParameterDef extends AdsDefinition implements IAdsEnumClassElement, IJavaSource {

    public static final class Factory {

        private Factory() {
        }

        public static AdsFieldParameterDef newInstance(String name) {
            return new AdsFieldParameterDef(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_ENUM_CLASS_PARAM), name);
        }

        public static AdsFieldParameterDef newInstance(String name, AdsTypeDeclaration type, AdsValAsStr initVal) {
            return new AdsFieldParameterDef(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_ENUM_CLASS_PARAM), name, type, initVal);
        }

        public static AdsFieldParameterDef loadFrom(EnumClassFieldParamDefinition xDef) {
            if (xDef == null) {
                return null;
            }
            return new AdsFieldParameterDef(xDef);
        }

        public static AdsFieldParameterDef newInstanceFrom(AdsFieldParameterDef source) {
            if (source == null) {
                return null;
            }
            return newInstance(source.getName(), source.getValue().getType(), source.getValue().getInitialValue());
        }

        public static AdsFieldParameterDef newTemporaryInstance(AdsEnumClassDef container) {
            assert container != null : "null container";

            AdsFieldParameterDef temp = newInstance("");
            temp.setContainer(container);
            return temp;
        }

        public static AdsFieldParameterDef newTemporaryInstanceFrom(AdsEnumClassDef container, AdsFieldParameterDef source) {
            assert container != null : "null container";

            if (source == null) {
                return null;
            }
            AdsFieldParameterDef temp = newInstanceFrom(source);
            temp.setContainer(container);
            return temp;
        }
    }

    public static class ParamValue extends Value {

        public static final class ParameterValueController implements AdsValAsStr.IValueController {

            private AdsFieldParameterDef param;

            public ParameterValueController(AdsFieldParameterDef field) {
                this.param = field;
            }

            @Override
            public boolean isValueTypeAvailable(AdsValAsStr.EValueType type) {
                return true;
            }

            @Override
            public AdsTypeDeclaration getContextType() {
                return param.getValue().getType();
            }

            @Override
            public AdsFieldParameterDef getContextDefinition() {
                return param;
            }

            @Override
            public void setValue(AdsValAsStr value) {
                param.getValue().setInitValue(value);
            }

            @Override
            public AdsValAsStr getValue() {
                return param.getValue().getInitialValue();
            }

            @Override
            public String getValuePresentation() {
                return AdsValAsStr.DefaultPresenter.getAsString(this);
            }
        }

        private static final class Factory {

            private Factory() {
            }

            public static ParamValue loadFrom(AdsFieldParameterDef context, EnumClassFieldParamDefinition xDef) {
                return new ParamValue(context, xDef);
            }

            public static ParamValue newInstance(AdsFieldParameterDef context, AdsTypeDeclaration type, AdsValAsStr initVal) {
                return new ParamValue(context, type, initVal);
            }

            public static ParamValue emptyInstance(AdsFieldParameterDef context) {
                return newInstance(context, AdsTypeDeclaration.UNDEFINED, AdsValAsStr.NULL_VALUE);
            }
        }
        private AdsValAsStr initVal;
        private ParameterValueController controller;

        protected ParamValue(final AdsFieldParameterDef context) {
            super();

            setContainer(context);

            controller = new ParameterValueController(context);
        }

        protected ParamValue(AdsFieldParameterDef context, AdsTypeDeclaration type, AdsValAsStr initVal) {
            this(context);

            this.initVal = initVal == null ? AdsValAsStr.NULL_VALUE : initVal;
            super.setType(type != null ? type : AdsTypeDeclaration.UNDEFINED);
        }

        protected ParamValue(final AdsFieldParameterDef context, EnumClassFieldParamDefinition xDef) {
            this(context);

            AdsTypeDeclaration type = AdsTypeDeclaration.Factory.loadFrom(xDef.getType());
            type = type != null ? type : AdsTypeDeclaration.UNDEFINED;

            super.setType(type);
            initVal = AdsValAsStr.Factory.loadFrom(context, xDef.getInitialValue());

            if (initVal == null) {
                initVal = AdsValAsStr.NULL_VALUE;
            }
        }

        public AdsValAsStr getInitialValue() {
            return initVal;
        }

        public void setInitValue(AdsValAsStr value) {
            value = value != null ? value : AdsValAsStr.NULL_VALUE;
            if (!Utils.equals(value, initVal)) {
                initVal = value;
                setEditState(EEditState.MODIFIED);
            }
        }

        public AdsFieldParameterDef getOwnerParameter() {
            return (AdsFieldParameterDef) getContainer();
        }

        public boolean isInitialValueSet() {
            return initVal != AdsValAsStr.NULL_VALUE;
        }

        public void unsetInitialValue() {
            setInitValue(AdsValAsStr.NULL_VALUE);
        }

        public ParameterValueController getValueController() {
            return controller;
        }

        @Override
        public boolean isTypeAllowed(EValType type) {
            return ValTypes.ADS_ENUM_CLASS_FIELD_TYPES.contains(type);
        }

        @Override
        public boolean isTypeRefineAllowed(EValType type) {
            return type != null && (type.isEnumAssignableType() || type == EValType.USER_CLASS || type == EValType.XML);
        }

        @Override
        public VisitorProvider getTypeSourceProvider(EValType toRefine) {
            if (toRefine == null) {
                return VisitorProviderFactory.createEmptyVisitorProvider();
            } else if (toRefine.isEnumAssignableType()) {
                return AdsVisitorProviders.newEnumBasedTypeProvider(toRefine);
            } else if (toRefine == EValType.XML) {
                return AdsVisitorProviders.newXmlBasedTypesProvider(this.getOwnerParameter().getUsageEnvironment());
            } else if (toRefine == EValType.USER_CLASS) {
                return AdsVisitorProviders.newClassBasedTypesProvider(this.getOwnerParameter().getUsageEnvironment());
            } else {
                return VisitorProviderFactory.createEmptyVisitorProvider();
            }
        }

        @Override
        public void setType(AdsTypeDeclaration type) {
            type = type != null ? type : AdsTypeDeclaration.UNDEFINED;
            if (!Utils.equals(getType(), type)) {
                super.setType(type);
            }
        }

        @Override
        public void visitChildren(IVisitor visitor, VisitorProvider provider) {
            super.visitChildren(visitor, provider);
            if (initVal != null) {
                initVal.visit(visitor, provider);
            }
        }
    }
    private ParamValue value;

    protected AdsFieldParameterDef(Id id, String name) {
        super(id, name);
        this.value = ParamValue.Factory.emptyInstance(this);
    }

    protected AdsFieldParameterDef(Id id, String name, AdsTypeDeclaration type, AdsValAsStr initVal) {
        super(id, name);
        this.value = ParamValue.Factory.newInstance(this, type, initVal);
    }

    protected AdsFieldParameterDef(EnumClassFieldParamDefinition xDef) {
        super(xDef);
        setName(xDef.getName());
        value = ParamValue.Factory.loadFrom(this, xDef);
    }

    public ParamValue getValue() {
        if (value == null) {
            value = ParamValue.Factory.emptyInstance(this);
        }
        return value;
    }

    public void appendTo(EnumClassFieldParamDefinition xDef) {
        xDef.setId(getId());
        xDef.setName(getName());

        if (value != null) {
            value.initVal.appendTo(xDef.addNewInitialValue());
            value.getType().appendTo(xDef.addNewType());
        }
    }

    @Override
    public void appendTo(DescribedAdsDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        appendTo((EnumClassFieldParamDefinition) xDef);
    }

    @Override
    public final AdsEnumClassDef getOwnerEnumClass() {
        return (AdsEnumClassDef) getOwnerDef();
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.ENUM_CLASS_PARAM;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        if (value != null) {
            value.visit(visitor, provider);
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {

            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AdsEnumClassParameterWriter(this, AdsFieldParameterDef.this, purpose);
            }
        };
    }

    @Override
    public String toString() {
        return getName();
    }
}