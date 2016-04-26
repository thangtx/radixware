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

package org.radixware.kernel.designer.tree.ads.nodes.defs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupport;
import org.radixware.kernel.designer.tree.ads.nodes.defs.AdsObjectNode;

/**
 * Provider for node representing class or class item Use it for calculate nodes
 * set for {@linkplain ClassNodeChildrenAdapter{
 */
public abstract class MixedNodeChildrenProvider<T extends RadixObject> {

    MixedNodeChildrenAdapter<T> adapter;
    public static final Collection<Key> EMPTY_KEYS = new ArrayList<>();

    public interface Key {

        public static final class Factory {

            public static IdKey forId(Id id) {
                return new IdKey(id);
            }

            public static ObjKey forObject(RadixObject object) {
                return new ObjKey(object);
            }

            public static <T> AnyKey<T> wrap(T object) {
                return new AnyKey<>(object);
            }
        }
    }

    public static class AnyKey<T> implements Key {

        public final T obj;

        public AnyKey(T obj) {
            this.obj = obj;
        }
    }

    public static class IdKey implements Key {

        private Id id;

        private IdKey(Id id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 73 * hash + (this.id != null ? this.id.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final IdKey other = (IdKey) obj;
            if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
                return false;
            }
            return true;
        }

        public Id getId() {
            return id;
        }
    }

    public static class ObjKey implements Key {

        private RadixObject obj;

        private ObjKey(RadixObject id) {
            this.obj = id;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 73 * hash + (this.obj != null ? this.obj.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ObjKey other = (ObjKey) obj;
            if (this.obj != other.obj && (this.obj == null || !this.obj.equals(other.obj))) {
                return false;
            }
            return true;
        }

        public RadixObject getObject() {
            return obj;
        }
    }

    /**
     * returns updated keyset for class context
     */
    public abstract Collection<Key> updateKeys();

    protected abstract Node findOrCreateNode(Key key);

    /**
     * returns node array for given key value
     */
    
    public final Node[] createNodes(Key key) {
        Node node = findOrCreateNode(key);
        if (node == null) {
            return new Node[0];
        } else {
            if (node instanceof FilterNode) {
                return new Node[]{node};
            } else {
                if (node instanceof AdsMixedNode && ((AdsMixedNode) node).noFilter()) {
                    return new Node[]{node};
                } else {
                    return new Node[]{new FilterNode(node)};
                }
            }
        }
    }

    /**
     * connexts current provider instance with definition context
     */
    public abstract boolean enterContext(MixedNodeChildrenAdapter<T> adapter, T context);

    public abstract ICreatureGroup[] createCreatureGroups();

    protected void reorder(int[] pos) {
        //do nothing by default
    }

    protected boolean isSorted() {
        return false;


    }

    private static class OverridableNodeHtmlNameSupport extends HtmlNameSupport {

        final RadixObjects usageContext;

        OverridableNodeHtmlNameSupport(RadixObjects usageContext, RadixObject object) {
            super(object);
            this.usageContext = usageContext;
        }

        @Override
        public Color getColor() {
            return getRadixObject().getContainer() == usageContext ? super.getColor() : Color.GRAY;
        }
    }

    public static class OverridableNodeDelegate extends FilterNode {

        final RadixObject radixObject;
        final Definitions usageContext;
        final OverridableNodeHtmlNameSupport htmlNameSupport;

        public OverridableNodeDelegate(Definitions usageContext, Node original) {
            super(original);
            this.usageContext = usageContext;
            if (original instanceof AdsObjectNode) {
                radixObject = ((AdsObjectNode) original).getRadixObject();
                htmlNameSupport = new OverridableNodeHtmlNameSupport(usageContext, radixObject);
            } else {
                radixObject = null;
                htmlNameSupport = null;
            }
        }

        @Override
        public boolean canDestroy() {
            if (radixObject == null) {
                return false;
            }
            return usageContext.findById(((Definition) radixObject).getId()) == radixObject;
        }

        @Override
        public boolean canCut() {
            return canDestroy();
        }

        @Override
        public String getHtmlDisplayName() {
            if (htmlNameSupport != null) {
                return htmlNameSupport.getHtmlName();
            } else {
                return super.getHtmlDisplayName();
            }
        }
    }
}
