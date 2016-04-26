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

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.defs.ads.AdsValAsStr.EValueType;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;


public abstract class PropertyValue extends Value {

    static class Factory {

        private Factory() {
            super();
        }

        static final PropertyValue newInstance(final AdsPropertyDef context) {
            if (context instanceof ColumnProperty) {
                return new ColumnPropertyValue(context);
            } else if (context instanceof ParentRefProperty) {
                return new RefPropertyValue(context);
            } else if (context instanceof AdsParentPropertyDef) {
                return new ParentPropertyValue(context);
            } else if (context instanceof AdsTransmittablePropertyDef) {
                return new TransmittablePropertyValue(context);
            } else if (context instanceof AdsParameterPropertyDef) {
                return new ParameterPropertyValue(context);
            } else if (context instanceof AdsEventCodePropertyDef) {
                return new EventCodePropertyValue(context);
            } else if (context instanceof AdsDynamicPropertyDef) {
                return new DynamicPropertyValue(context);
            } else if (context instanceof AdsUserPropertyDef) {
                return new UserPropertyValue(context);
            } else if (context instanceof AdsPropertyPresentationPropertyDef) {
                return new PropertyPresentationValue(context);
            } else if (context instanceof AdsFieldPropertyDef) {
                return new FieldPropertyValue(context);
            } else if (context instanceof AdsFieldRefPropertyDef) {
                return new FieldPropertyValue(context);
            } else if (context instanceof AdsExpressionPropertyDef) {
                return new ExpressionPropertyValue(context);
            } else if (context instanceof AdsTransparentPropertyDef) {
                return new TransparentPropertyValue(context);
            } else {

                throw new RadixObjectError("Unsupported property value context.", context);
            }
        }

        static final PropertyValue newCopy(final AdsPropertyDef context, final PropertyValue source) {
            if (source instanceof ColumnPropertyValue) {
                return new ColumnPropertyValue(context, (ColumnPropertyValue) source);
            } else if (source instanceof RefPropertyValue) {
                return new RefPropertyValue(context, (RefPropertyValue) source);
            } else if (source instanceof ParentPropertyValue) {
                return new ParentPropertyValue(context, (ParentPropertyValue) source);
            } else if (source instanceof TransmittablePropertyValue) {
                return new TransmittablePropertyValue(context, (TransmittablePropertyValue) source);
            } else if (context instanceof AdsParameterPropertyDef) {
                return new ParameterPropertyValue(context, (ParameterPropertyValue) source);
            } else if (source instanceof EventCodePropertyValue) {
                return new EventCodePropertyValue(context, (DynamicPropertyValue) source);
            } else if (source instanceof DynamicPropertyValue) {
                return new DynamicPropertyValue(context, (DynamicPropertyValue) source);
            } else if (source instanceof UserPropertyValue) {
                return new UserPropertyValue(context, (UserPropertyValue) source);
            } else if (source instanceof PropertyPresentationValue) {
                return new PropertyPresentationValue(context, (PropertyPresentationValue) source);
            } else if (context instanceof AdsFieldPropertyDef) {
                return new FieldPropertyValue(context, (FieldPropertyValue) source);
            } else if (context instanceof AdsFieldRefPropertyDef) {
                return new FieldPropertyValue(context, (FieldPropertyValue) source);
            } else if (context instanceof AdsExpressionPropertyDef) {
                return new ExpressionPropertyValue(context, (ExpressionPropertyValue) source);
            } else {
                throw new RadixObjectError("Unsupported property value context.", context);
            }
        }

        static final PropertyValue loadFrom(final AdsPropertyDef context, final AbstractPropertyDefinition xDef) {
            if (context instanceof ColumnProperty) {
                return new ColumnPropertyValue(context, xDef);
            } else if (context instanceof ParentRefProperty) {
                return new RefPropertyValue(context, xDef);
            } else if (context instanceof AdsParentPropertyDef) {
                return new ParentPropertyValue(context, xDef);
            } else if (context instanceof AdsTransmittablePropertyDef) {
                return new TransmittablePropertyValue(context, xDef);
            } else if (context instanceof AdsParameterPropertyDef) {
                return new ParameterPropertyValue(context, xDef);
            } else if (context instanceof AdsEventCodePropertyDef) {
                return new EventCodePropertyValue(context, xDef);
            } else if (context instanceof AdsDynamicPropertyDef) {
                return new DynamicPropertyValue(context, xDef);
            } else if (context instanceof AdsUserPropertyDef) {
                return new UserPropertyValue(context, xDef);
            } else if (context instanceof AdsPropertyPresentationPropertyDef) {
                return new PropertyPresentationValue(context, xDef);
            } else if (context instanceof AdsFieldPropertyDef) {
                return new FieldPropertyValue(context, xDef);
            } else if (context instanceof AdsFieldRefPropertyDef) {
                return new FieldPropertyValue(context, xDef);
            } else if (context instanceof AdsExpressionPropertyDef) {
                return new ExpressionPropertyValue(context, xDef);
            } else if (context instanceof AdsTransparentPropertyDef) {
                return new TransparentPropertyValue(context, xDef);
            } else if (context instanceof AdsWrapperPropertyDef) {
                return new WrapperPropertyValue(context, xDef);
            } else {
                throw new RadixObjectError("Unsupported property value context.", context);
            }
        }
    }
    //private ValAsStr initValAsStr = null;
    private AdsValAsStr initialValue = null;

    private class TypeChangeListener implements IRadixEventListener {

        @Override
        public void onEvent(final RadixEvent e) {
            AdsTypeDeclaration type = getType();
            if (type != null && type.getTypeId() != null) {
                switch (type.getTypeId()) {
                    case CLOB:
                    case BLOB:
                        final AdsPropertyDef prop = getProperty();
                        if (prop instanceof IAdsPresentableProperty) {
                            final ServerPresentationSupport ps = ((IAdsPresentableProperty) prop).getPresentationSupport();
                            if (ps != null) {
                                ps.getPresentation().getEditOptions().setReadSeparately(true);
                            }
                        }
                        break;
                    default:
                    //do nothing
                }
                AdsPropertyDef prop = getProperty();
                if (prop instanceof IAdsPresentableProperty) {
                    ServerPresentationSupport support = ((IAdsPresentableProperty) prop).getPresentationSupport();
                    if (support != null) {
                        support.checkPresentation();
                    }
                }
                if ((prop instanceof AdsParameterPropertyDef) && (prop.getOwnerClass() instanceof AdsReportClassDef)) {
                    ((AdsReportClassDef) prop.getOwnerClass()).updateMethodsParams();
                }
            }
        }
    }
    private final transient TypeChangeListener typeListener = new TypeChangeListener();

    protected AdsPropertyDef getProperty() {
        return (AdsPropertyDef) getContainer();
    }

    protected PropertyValue(final AdsPropertyDef context) {
        super(AdsTypeDeclaration.Factory.undefinedType());
        setContainer(context);
    }

    @SuppressWarnings("unchecked")
    protected PropertyValue(final AdsPropertyDef context, final PropertyValue source) {
        super(source.getType());
        setContainer(context);
        if (source.initialValue != null) {
            this.initialValue = AdsValAsStr.Factory.newCopy(source.initialValue);// source.initialValue. ValAsStr.Factory.newInstance(source.initValAsStr.toObject(getType().getTypeId()), getType().getTypeId());
        }
        this.getTypeChangeSupport().addEventListener(typeListener);
    }

    @SuppressWarnings("unchecked")
    protected PropertyValue(final AdsPropertyDef context, final AbstractPropertyDefinition xDef) {
        super(AdsTypeDeclaration.Factory.loadFrom(xDef.getType()));
        setContainer(context);

        if (xDef.getInitialVal() != null) {
            ValAsStr valAsStr = ValAsStr.Factory.loadFrom(xDef.getInitialVal());
            this.initialValue = AdsValAsStr.Factory.newInstance(valAsStr);
        } else {
            if (xDef.getInitialValue() != null) {
                this.initialValue = AdsValAsStr.Factory.loadFrom(context, xDef.getInitialValue());
            }
        }

        this.getTypeChangeSupport().addEventListener(typeListener);
    }

    protected void appendTo(final AbstractPropertyDefinition xDef) {
        AdsTypeDeclaration type = getPersistentType();
        if (type != null) {
            type.appendTo(xDef.addNewType());
        }
        if (this.initialValue != null && this.initialValue != AdsValAsStr.NULL_VALUE) {
            this.initialValue.appendTo(xDef.addNewInitialValue());
        }
    }

    protected AdsTypeDeclaration getPersistentType() {
        return getType();
    }

    public AdsValAsStr getInitial() {
        return initialValue;
    }

    public void setInitial(final ValAsStr valAsStr) {
        setInitial(AdsValAsStr.Factory.newInstance(valAsStr));
    }

    public void setInitial(final AdsValAsStr valAsStr) {
        this.initialValue = valAsStr;
        setEditState(EEditState.MODIFIED);
    }

    protected boolean isJmlInitValAllowed() {
        return false;
    }

    public AdsValAsStr.IValueController getInitialValueController() {
        return new AdsValAsStr.IValueController() {
            @Override
            public boolean isValueTypeAvailable(EValueType type) {
                switch (type) {
                    case JML:
                        return isJmlInitValAllowed();
                    case NULL:
                        return true;
                    case VAL_AS_STR:
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public AdsTypeDeclaration getContextType() {
                return getType();
            }

            @Override
            public Definition getContextDefinition() {
                return getProperty();
            }

            @Override
            public void setValue(AdsValAsStr value) {
                setInitial(value);
            }

            @Override
            public AdsValAsStr getValue() {
                return getInitial();
            }

            @Override
            public String getValuePresentation() {
                return AdsValAsStr.DefaultPresenter.getAsString(this);
            }
        };
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        if (initialValue != null) {
            initialValue.visit(visitor, provider);
        }
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        final AdsValAsStr initialVal = this.getInitial();
        if (initialVal == null || initialVal.getValueType() != EValueType.VAL_AS_STR) {
            return;
        }

        final AdsValAsStr.IValueController initialValueController = this.getInitialValueController();
        if (initialValueController == null) {
            return;
        }

        final SearchResult<AdsType> resolve = initialValueController.getContextType().resolve(getDefinition());
        if (resolve.isEmpty()) {
            return;
        }
        
        final AdsType type = resolve.get();
        if (type instanceof AdsEnumType) {
            final AdsEnumDef sourceEnum = ((AdsEnumType) type).getSource();
            final ValAsStr valAsStr = initialVal.getValAsStr();

            if (valAsStr != null && sourceEnum != null) {
                final AdsEnumItemDef enumItem = sourceEnum.getItems().findByValue(valAsStr.toString(), ExtendableDefinitions.EScope.ALL);
                if (enumItem != null) {
                    list.add(enumItem);
                }
            }
        }
    }
}
