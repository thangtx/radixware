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

package org.radixware.kernel.common.client.meta;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsBaseObject;
import org.radixware.kernel.common.types.Id;

/**
 * Класс алгоритма.
 *
 */
public class RadAlgoDef extends TitledDefinition {

    private final Page page;
    private final Map<Id, String> titles;
    
    public static class Page {
        
        public final List<Node> nodes;
        public final List<Edge> edges;

        public Page(List<Node> nodes, List<Edge> edges) {
            this.nodes = nodes;
            this.edges = edges;
        }
        public List<Node> findNode(final Id id) {
            for (Node node: nodes) {
                final List<Node> l = node.findNode(id);
                if (l != null)
                    return l;
            }
            return null;
        }
    }
    
    public static class Node {
        
        public final Id id;
        public final AdsBaseObject.Kind kind;
        public final String name;
        public final Rectangle bounds;
        
        public final Id algoId;
        public final Page page;
        
        private Node(Id id, AdsBaseObject.Kind kind, String name, Rectangle bounds, Id algoId, Page page) {
            this.id = id;
            this.kind = kind;
            this.name = name;
            this.bounds = bounds;
            this.algoId = algoId;
            this.page = page;
        }
        public Node(Id id, AdsBaseObject.Kind kind, String name, Rectangle bounds) {
            this(id, kind, name, bounds, null, null);
        }
        public Node(Id id, AdsBaseObject.Kind kind, String name, Rectangle bounds, Id algoId) {
            this(id, kind, name, bounds, algoId, null);
        }
        public Node(Id id, AdsBaseObject.Kind kind, String name, Rectangle bounds, Page page) {
            this(id, kind, name, bounds, null, page);
        }
        public List<Node> findNode(final Id id) {
            if (this.id == id) {
                final List<Node> l = new ArrayList<Node>();
                l.add(this);
                return l;
            }
            if (page != null) {
                final List<Node> l = page.findNode(id);
                if (l != null) {
                    l.add(0, this);
                }
                return l;
            }
            return null;
        }
    }

    public static class Edge {
        
        public final Node source, target;
        public final List<Point> points;
        
        public Edge(Node source, Node target, List<Point> points) {
            this.source = source;
            this.target = target;
            this.points = points;
        }
    }

    public RadAlgoDef(
            final Id id,
            final String name,
            final Id titleId,
            final Page page,
            final Map<Id, String> titles) {
        super(id, name, titleId);
        this.page = page;
        this.titles = titles;
    }

    public Page getPage() {
        return page;
    }

    public Map<Id, String> getTitles() {
        return titles;
    }
    
    @Override
    public String getDescription() {
        return super.getDescription();
    }
}
