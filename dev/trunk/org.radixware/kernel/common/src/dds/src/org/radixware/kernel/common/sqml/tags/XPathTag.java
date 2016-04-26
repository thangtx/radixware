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

package org.radixware.kernel.common.sqml.tags;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.sqml.ISqmlSchema;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

/**
 * RADIX-3522
 */
public class XPathTag extends Sqml.Tag {

    private static class XPath extends RadixObjects<Item> {

        public XPath(XPathTag owner) {
            super(owner);
        }
    }

    public static class Item extends RadixObject {

        protected Item() {
        }
        private Id schemaId = null;
        private Long index = null;
        private boolean isAttribute = false;
        private String condition = null;

        public Long getIndex() {
            return index;
        }

        public void setIndex(final Long index) {
            if (!Utils.equals(this.index, index)) {
                this.index = index;
                setEditState(EEditState.MODIFIED);
            }
        }

        public Id getSchemaId() {
            return schemaId;
        }

        public void setSchemaId(final Id schemaId) {
            if (!Utils.equals(this.schemaId, schemaId)) {
                this.schemaId = schemaId;
                setEditState(EEditState.MODIFIED);
            }
        }

        public boolean isAttribute() {
            return isAttribute;
        }

        public void setIsAttribute(boolean isAttribute) {
            if (this.isAttribute != isAttribute) {
                this.isAttribute = isAttribute;
                setEditState(EEditState.MODIFIED);
            }
        }

        @Override
        public ENamingPolicy getNamingPolicy() {
            return ENamingPolicy.IDENTIFIER;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            if (!Utils.equals(this.condition, condition)) {
                this.condition = condition;
                setEditState(EEditState.MODIFIED);
            }
        }

        public static final class Factory {

            private Factory() {
            }

            public static Item newInstance() {
                return new Item();
            }
        }
    }
    private final RadixObjects<Item> items = new XPath(this);

    protected XPathTag() {
    }

    public RadixObjects<Item> getItems() {
        return items;
    }

    public static final class Factory {

        private Factory() {
        }

        public static XPathTag newInstance() {
            return new XPathTag();
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        items.visit(visitor, provider);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        final Sqml sqml = getOwnerSqml();
        if (sqml != null) {
            final Set<Id> scmehaIds = new HashSet<Id>();
            for (Item item : items) {
                final Id schemaId = item.getSchemaId();
                scmehaIds.add(schemaId);
            }

            for (Id schemaId : scmehaIds) {
                final ISqmlSchema schema = sqml.getEnvironment().findSchemaById(schemaId);
                final Definition def = (schema != null ? schema.getDefinition() : null);
                if (def != null) {
                    list.add(def);
                }
            }
        }
    }
}
