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

package org.radixware.kernel.common.meta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.environment.IRadixEnvironment;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.types.MultilingualString;

public class RadEnumDef extends RadDefinition implements IEnumDef {

    private final IRadixEnvironment environment;
    private final EValType valType;
    private final boolean isDeprecated;

    @Override
    public EValType getItemType() {
        return valType;
    }
    private IItems<IItem> items = null;

    @Override
    public IItems<? extends IItem> getItems() {
        if (items == null) {
            items = new IItems<IItem>() {
                @Override
                public List<IItem> list(final EScope scope) {
                    if (EScope.ALL != scope) {
                        throw new IllegalArgumentException("Only EScope.ALL is supported");
                    }
                    final ArrayList<IItem> lst = new ArrayList<IItem>(getItemsByVal().size());
                    lst.addAll(getItemsByVal().values());
                    return Collections.unmodifiableList(lst);
                }

                @Override
                public IItem findItemById(final Id itemId, final EScope scope) {
                    if (EScope.ALL != scope) {
                        throw new IllegalArgumentException("Only EScope.ALL is supported");
                    }
                    for (IItem i : getItemsByVal().values()) {
                        if (i.getId().equals(itemId)) {
                            return i;
                        }
                    }
                    return null;
                }
            };
        }
        return items;
    }

    public boolean getItemIsInDomain(final IKernelEnum item, final Id domainId) {
        if (item == null) {
            throw new IllegalArgumentException("Item parameter can't be null");
        }
        final Item i = getItemByVal(item.getValue());
        return i.getIsInDomain(environment, domainId);
    }

    /**
     * @return the itemsByVal
     */
    Map<Object, Item> getItemsByVal() {
        return itemsByVal;
    }

    public static class Item extends RadDefinition implements IEnumDef.IItem {

        public Item(final Id id,
                final String name,
                final ValAsStr valAsStr,
                final Id ownerId,
                final Id titleId,
                final Id iconId,
                int viewOrder,
                final Id[] domainIds,
                IKernelEnum itemRef) {
            this(id, name, valAsStr, ownerId, titleId, iconId, viewOrder, domainIds, false, itemRef);
        }

        public Item(final Id id,
                final String name,
                final ValAsStr valAsStr,
                final Id ownerId,
                final Id titleId,
                final Id iconId,
                int viewOrder,
                final Id[] domainIds,
                final boolean isDeprecated,
                IKernelEnum itemRef) {
            super(id, name);
            this.valAsStr = valAsStr;
            this.itemRef = itemRef;
            this.ownerId = ownerId;
            this.titleId = titleId;
            this.viewOrder = viewOrder;
            this.isDeprecated = isDeprecated;
            if (domainIds == null) {
                this.domainIds = Collections.emptyList();
            } else {
                this.domainIds = Collections.unmodifiableList(Arrays.asList(domainIds));
            }
            this.iconId = iconId;
        }
        private final ValAsStr valAsStr;
        private final IKernelEnum itemRef;
        private final Id ownerId;
        private final Id titleId;
        private final Collection<Id> domainIds;
        private final boolean isDeprecated;
        private final int viewOrder;
        private final Id iconId;

        @Override
        public String getTitle(final IRadixEnvironment env) {
            return MultilingualString.get(env, getOwnerId(), getTitleId());
        }

        public final IKernelEnum getConstant() {
            return itemRef;
        }

        @Override
        public String getTitle(final IRadixEnvironment env, final EIsoLanguage lang) {
            return MultilingualString.get(env, getOwnerId(), getTitleId(), lang);
        }

        @Override
        public ValAsStr getValue() {
            return getValAsStr();
        }

        public int getViewOrder() {
            return viewOrder;
        }

        /**
         * @return the valAsStr
         */
        ValAsStr getValAsStr() {
            return valAsStr;
        }

        /**
         * @return the ownerId
         */
        Id getOwnerId() {
            return ownerId;
        }

        /**
         * @return the titleId
         */
        Id getTitleId() {
            return titleId;
        }

        public Id getIconId() {
            return iconId;
        }

        @Override
        public Collection<Id> getDomainIds() {
            return domainIds;
        }

        /**
         *
         * @param environment - to process domains' hierarchy if necessary
         * @param domaindId
         * @return
         */
        public final boolean getIsInDomain(final IRadixEnvironment environment, final Id domaindId) {
            if (getDomainIds().contains(domaindId)) {
                return true;
            }
            for (Id id : getDomainIds()) {
                if (environment.getDefManager().isDefInDomain(id, domaindId)) {
                    return true;
                }
            }
            return false;
        }

        /**
         *
         * @param environment - to process domains' hierarchy if necessary
         * @param domaindIds
         * @return
         */
        public final boolean getIsInDomains(final IRadixEnvironment environment, final List<Id> domaindIds) {
            if (domaindIds == null) {
                return false;
            }
            for (Id id : domaindIds) {
                if (getIsInDomain(environment, id)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public final boolean isDeprecated() {
            return isDeprecated;
        }
    }
    //Constructor

    public RadEnumDef(
            final Id id,
            final String name,
            final EValType valType,
            final Item[] items,
            final IRadixEnvironment environment) {
        this(id, name, valType, false, items, environment);
    }

    public RadEnumDef(
            final Id id,
            final String name,
            final EValType valType,
            final boolean isDeprecated,
            final Item[] items,
            final IRadixEnvironment environment) {
        super(id, name);
        this.valType = valType;
        itemsByVal = linkedItems(valType, items);
        this.isDeprecated = isDeprecated;
        this.environment = environment;
    }
    //Service methods
    private final Map<Object, Item> itemsByVal;

    private Map<Object, Item> linkedItems(final EValType valType, final Item[] items) {
        final HashMap<Object, Item> res = new HashMap<Object, Item>();
        for (Item i : items) {
            ValAsStr valAsStr = i.getValue();
            res.put(valAsStr == null ? null : valAsStr.toObject(valType), i);
        }
        return res;
    }

    public final Item getItemByVal(Comparable val) {
        if (val instanceof IKernelEnum) {
            val = ((IKernelEnum) val).getValue();
        }
        Item item = getItemsByVal().get(val);
        if (item == null) {
            throw new NoConstItemWithSuchValueError("Enum #" + getId() + " has no item with value=" + String.valueOf(val), val);
        }
        return item;
    }

    @Override
    public boolean isDeprecated() {
        return isDeprecated; //To change body of generated methods, choose Tools | Templates.
    }
    
}
