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
package org.radixware.kernel.common.defs.ads.clazz.presentation;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.IOverwritable;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.radixdoc.SortingRadixdoc;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.IOverridable;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.defs.ads.src.clazz.presentation.AdsSortingWriter;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EOrder;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.sqml.ads.AdsSqmlEnvironment;
import org.radixware.schemas.adsdef.ClassDefinition.Presentations.Sortings.Sorting;
import org.radixware.schemas.radixdoc.Page;

public class AdsSortingDef extends AdsPresentationsMember implements IJavaSource, IOverridable, IOverwritable, IRadixdocProvider {

    @Override
    public void afterOverride() {
        //do nothing
    }

    @Override
    public void afterOverwrite() {
        //do nothing
    }

    @Override
    public boolean allowOverwrite() {
        return true;
    }

    public static final class Factory {

        public static final AdsSortingDef loadFrom(Sorting xSorting) {
            return new AdsSortingDef(xSorting);
        }

        public static final AdsSortingDef newInstance() {
            return new AdsSortingDef();
        }
    }

    public static class OrderBy extends PropertyUsage {

        public static class Factory {

            public static final OrderBy newInstance(AdsPropertyDef prop) {
                return new OrderBy(prop);
            }
        }
        private EOrder order;

        private OrderBy(Sorting.OrderBy xOrderBy) {
            super(xOrderBy.getPropId());
            this.order = xOrderBy.getDesc() ? EOrder.DESC : EOrder.ASC;

        }

        private OrderBy(AdsPropertyDef prop) {
            super(prop);
            this.order = EOrder.ASC;
        }

        @Override
        public AdsClassDef getOwnerClass() {
            for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
                if (owner instanceof AdsClassDef) {
                    return (AdsClassDef) owner;
                }
            }
            return null;
        }

        public EOrder getOrder() {
            return order;
        }

        public void setOrder(EOrder order) {
            this.order = order;
            setEditState(EEditState.MODIFIED);
        }

        private void appendTo(Sorting.OrderBy xDef) {
            xDef.setDesc(order == EOrder.DESC);
            if (propertyId != null) {
                xDef.setPropId(propertyId);
            }
        }
    }

    public class Order extends RadixObjects<OrderBy> {

        private class PropertyFilter implements IFilter<AdsPropertyDef> {

            private HashMap<Id, Object> usedProps = new HashMap<Id, Object>();

            private PropertyFilter() {
                for (OrderBy o : Order.this) {
                    usedProps.put(o.propertyId, null);
                }
            }

            @Override
            public boolean isTarget(AdsPropertyDef object) {
                return object != null && object.hasDbRepresentation() && usedProps.get(object.getId()) == null;
            }
        }

        public IFilter<AdsPropertyDef> newAvailablePropertyFilter() {
            return new PropertyFilter();
        }

        public Order(List<Sorting.OrderBy> xDef) {
            super(AdsSortingDef.this);
            if (xDef != null) {
                for (Sorting.OrderBy ob : xDef) {
                    this.add(new OrderBy(ob));
                }
            }
        }

        public void appendTo(Sorting xDef) {
            if (!isEmpty()) {
                for (OrderBy ob : this) {
                    ob.appendTo(xDef.addNewOrderBy());
                }
            }
        }
    }
    AdsSqmlEnvironment env = AdsSqmlEnvironment.Factory.newInstance(this);

    private final class Hint extends Sqml {

        public Hint() {
            super(AdsSortingDef.this);
            setEnvironment(env);
        }
    }
    private final Sqml hint = new Hint();
    private final Order order;

    protected AdsSortingDef(Sorting xSorting) {
        super(xSorting);
        this.hint.loadFrom(xSorting.getHint());
        this.order = new Order(xSorting.getOrderByList());
    }

    protected AdsSortingDef() {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.SORTING), "NewSorting", null);
        this.order = new Order(null);
    }

    public Order getOrder() {
        return order;
    }

    public Sqml getHint() {
        return hint;
    }

    public void appendTo(Sorting xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (saveMode == ESaveMode.NORMAL) {
            this.order.appendTo(xDef);
            this.hint.appendTo(xDef.addNewHint());
        }
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.SORTING;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        this.order.visit(visitor, provider);
        this.hint.visit(visitor, provider);
    }

    @Override
    public AdsEntityObjectClassDef getOwnerClass() {
        return (AdsEntityObjectClassDef) super.getOwnerClass();
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AdsSortingWriter(this, AdsSortingDef.this, purpose);
            }
        };
    }

    @Override
    public ClipboardSupport<AdsSortingDef> getClipboardSupport() {
        return new AdsClipboardSupport<AdsSortingDef>(this) {
            @Override
            protected XmlObject copyToXml() {
                Sorting xDef = Sorting.Factory.newInstance();
                appendTo(xDef, ESaveMode.NORMAL);
                return xDef;
            }

            @Override
            protected AdsSortingDef loadFrom(XmlObject xmlObject) {
                if (xmlObject instanceof Sorting) {
                    return Factory.loadFrom((Sorting) xmlObject);
                } else {
                    return super.loadFrom(xmlObject);
                }
            }

            @Override
            protected Method getDecoderMethod() {
                try {
                    return AdsSortingDef.Factory.class.getDeclaredMethod("loadFrom", Sorting.class);
                } catch (NoSuchMethodException | SecurityException ex) {
                    return null;
                }
            }
        };
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        return collection instanceof ExtendablePresentations.ExtendablePresentationsLocal && collection.getContainer() instanceof Sortings;
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.SORTING;
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsSortingDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new SortingRadixdoc(getSource(), page, options);
            }
        };
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }
}
