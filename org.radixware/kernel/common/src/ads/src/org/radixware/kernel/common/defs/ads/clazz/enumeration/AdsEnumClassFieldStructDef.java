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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsEnumClassDef;
import org.radixware.kernel.common.defs.ads.clazz.ClassHierarchyIterator;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.EnumClassFieldParamDefinition;


public class AdsEnumClassFieldStructDef extends ExtendableDefinitions<AdsFieldParameterDef> implements IAdsEnumClassElement {

    public static final class Factory {

        private Factory() {
        }

        public static AdsEnumClassFieldStructDef newInstance(AdsEnumClassDef context) {
            return new AdsEnumClassFieldStructDef(context);
        }

        public static AdsEnumClassFieldStructDef loadFrom(AdsEnumClassDef context, ClassDefinition.EnumClassFieldStruct xDef) {
            return new AdsEnumClassFieldStructDef(context, xDef);
        }
    }

    public static final class ParamDefinitions extends Definitions<AdsFieldParameterDef> {

        protected ParamDefinitions() {
            super();
        }

        public AdsFieldParameterDef add(String name, AdsTypeDeclaration type, AdsValAsStr initVal) {
            final AdsFieldParameterDef param = AdsFieldParameterDef.Factory.newInstance(name);
            param.getValue().setInitValue(initVal);
            param.getValue().setType(type);
            this.add(param);
            return param;
        }

        public AdsFieldParameterDef add(String name) {
            final AdsFieldParameterDef param = AdsFieldParameterDef.Factory.newInstance(name);
            this.add(param);
            return param;
        }

        @Override
        public AdsEnumClassDef getOwnerDefinition() {
            return (AdsEnumClassDef) super.getOwnerDefinition();
        }
    }

    public static class ParamHierarchyIterator extends HierarchyIterator<AdsEnumClassFieldStructDef> {

        private List<AdsEnumClassFieldStructDef> next;
        private AdsEnumClassFieldStructDef init;
        private HierarchyIterator<AdsClassDef> internal;

        protected ParamHierarchyIterator(AdsEnumClassDef context, EScope scope, HierarchyIterator.Mode mode) {
            super(mode);
            this.internal = new ClassHierarchyIterator(context, scope, mode);
            this.init = context.getFieldStruct();
            this.next = null;
        }

        @Override
        public boolean hasNext() {
            if (next == null && internal.hasNext()) {
                List<AdsEnumClassFieldStructDef> list = new LinkedList<>();
                while (internal.hasNext()) {

                    Chain<AdsClassDef> nextClasses = internal.next();

                    for (AdsClassDef clazz : nextClasses) {
                        AdsEnumClassFieldStructDef struct = init.findInstance(clazz);
                        if (struct != null) {
                            if (!list.contains(struct)) {
                                list.add(struct);
                            }
                        }
                    }
                    if (!list.isEmpty()) {
                        break;
                    }
                }
                if (list.isEmpty()) {
                    next = null;
                } else {
                    next = new ArrayList<>(list);
                }
            }
            return next != null;
        }

        @Override
        public Chain<AdsEnumClassFieldStructDef> next() {
            if (hasNext()) {
                Chain<AdsEnumClassFieldStructDef> result = Chain.newInstance(next);
                next = null;
                return result;
            } else {
                return Chain.empty();
            }
        }
    }

    protected AdsEnumClassFieldStructDef(AdsEnumClassDef context, ClassDefinition.EnumClassFieldStruct xDef) {
        this(context);
        if (xDef != null) {
            final List<EnumClassFieldParamDefinition> params = xDef.getParameterList();
            if (params != null) {
                for (final EnumClassFieldParamDefinition p : params) {
                    AdsFieldParameterDef param = AdsFieldParameterDef.Factory.loadFrom(p);
                    if (param != null) {
                        super.getLocal().add(param);
                    }
                }
            }
        }
    }

    protected AdsEnumClassFieldStructDef(AdsEnumClassDef context) {
        super(context, new ParamDefinitions());
    }

    /**
     *
     * @return number of all local and inheritance parameters
     */
    public int getFullSize() {

        final HierarchyIterator<? extends ExtendableDefinitions<AdsFieldParameterDef>> iter = newIterator(EScope.ALL, HierarchyIterator.Mode.FIND_FIRST);
        int size = 0;
        while (iter.hasNext()) {
            size += iter.next().first().getLocal().size();
        }
        return size;
    }

    public void appendTo(ClassDefinition.EnumClassFieldStruct xDef, ESaveMode saveMode) {
        for (final AdsFieldParameterDef param : getLocal()) {
            if (saveMode == ESaveMode.API && !param.isPublished()) {
                continue;
            }
            param.appendTo(xDef.addNewParameter());
        }
    }

    /**
     *
     * @return list of parameters ordered by hierarchy
     */
    public List<AdsFieldParameterDef> getOrdered() {
        final List<AdsFieldParameterDef> list = new ArrayList<>();
        final Map<Id, Integer> ids = new HashMap<>();

        List<ExtendableDefinitions<AdsFieldParameterDef>> levels = new ArrayList<>();
        HierarchyIterator<? extends ExtendableDefinitions<AdsFieldParameterDef>> iter = newIterator(EScope.LOCAL_AND_OVERWRITE, HierarchyIterator.Mode.FIND_FIRST);

        while (iter.hasNext()) {
            levels.add(iter.next().first());
        }

        int pos = 0;
        for (int i = levels.size() - 1; i >= 0; --i) {
            for (final AdsFieldParameterDef p : levels.get(i).getLocal()) {

                Id currId = p.getId();

                if (ids.containsKey(currId)) {
                    int index = ids.get(currId);
                    list.set(index, p);
                } else {
                    list.add(p);
                    ids.put(currId, pos++);
                }
            }
        }

        return list;
    }

    @Override
    public AdsEnumClassDef getOwnerEnumClass() {
        Definition def = getOwnerDefinition();
        while (def != null && !(def instanceof AdsEnumClassDef)) {
            def = def.getOwnerDefinition();
        }
        return (AdsEnumClassDef) def;
    }

    @Override
    protected HierarchyIterator<? extends ExtendableDefinitions<AdsFieldParameterDef>> newIterator(EScope scope, HierarchyIterator.Mode mode) {
        return new ParamHierarchyIterator(getOwnerEnumClass(), scope, mode);
    }

    @Override
    public ParamDefinitions getLocal() {
        return (ParamDefinitions) super.getLocal();
    }

    @Override
    public AdsEnumClassFieldStructDef findInstance(Definition owner) {
        return owner instanceof AdsEnumClassDef ? ((AdsEnumClassDef) owner).getFieldStruct() : null;
    }

    @Override
    public String getName() {
        return "Parameters";
    }
}
