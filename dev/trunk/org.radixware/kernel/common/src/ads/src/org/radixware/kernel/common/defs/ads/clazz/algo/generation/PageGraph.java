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

package org.radixware.kernel.common.defs.ads.clazz.algo.generation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.common.types.Id;


public class PageGraph {

    final private HashMap<AdsPin, List<AdsEdge>> pin2Edges = new HashMap<AdsPin, List<AdsEdge>>();
    final private HashMap<AdsPin, AdsBaseObject> pin2Node = new HashMap<AdsPin, AdsBaseObject>();

    public PageGraph(AdsPage page) {
        for (AdsBaseObject node: page.getNodes()) {
            for (AdsPin pin: node.getTargetPins()) {
                pin2Node.put(pin, node);
            }
        }
        for (AdsEdge edge: page.getEdges()) {
            List<AdsEdge> edges = pin2Edges.get(edge.getSource());
            if (edges == null) {
                edges = new ArrayList<AdsEdge>();
                pin2Edges.put(edge.getSource(), edges);
            }
            edges.add(edge);
        }
    }

    public int nextSize(AdsPin pin) {
        List<AdsEdge> edges = pin2Edges.get(pin);
        if (edges != null)
            return edges.size();
        return 0;
    }

    public AdsBaseObject nextNode(AdsPin pin, int i) {
        List<AdsEdge> edges = pin2Edges.get(pin);
        if (edges != null) {
            AdsEdge edge = edges.get(i);
            AdsPin targetPin = edge.getTarget();
            AdsBaseObject targetNode = (AdsBaseObject)targetPin.getOwnerDefinition();
            if (targetNode instanceof AdsEmptyObject)
                return nextNode(targetPin, 0);
            return targetNode;
        }
        return null;
    }

    public AdsBaseObject nextNode(AdsPin pin) {
        return nextNode(pin, 0);
    }

    public Id next(AdsPin pin, int i) {
        List<AdsEdge> edges = pin2Edges.get(pin);
        if (edges != null) {
            AdsEdge edge = edges.get(i);
            if (AppUtils.isExecutable(edge))
                return edge.getId();
            AdsPin targetPin = edge.getTarget();
            AdsBaseObject targetNode = (AdsBaseObject)targetPin.getOwnerDefinition();
            if (targetNode instanceof AdsEmptyObject)
                return next(targetPin);
            if (AppUtils.isExecutable(targetNode))
                return targetNode.getId();
            return null;
        }
        AdsBaseObject node = pin2Node.get(pin);
        if (node != null) {
            if (node instanceof AdsEmptyObject)
                return next(pin);
            if (AppUtils.isExecutable(node))
                return node.getId();
            return null;
        }
        return null;
    }

    public Id next(AdsPin pin) {
        return next(pin, 0);
    }

    public int nextInput(AdsPin pin, int i) {
        List<AdsEdge> edges = pin2Edges.get(pin);
        if (edges != null) {
            AdsEdge edge = edges.get(i);
            if (AppUtils.isExecutable(edge))
                return 0;
            AdsPin targetPin = edge.getTarget();
            AdsBaseObject targetNode = (AdsBaseObject)targetPin.getOwnerDefinition();
            if (targetNode instanceof AdsEmptyObject)
                return nextInput(targetPin);
            if (targetNode instanceof AdsMergeObject) {
                return targetNode.getTargetPins().indexOf(targetPin);
            }
            return 0;
        }
        AdsBaseObject node = pin2Node.get(pin);
        if (node != null) {
            if (node instanceof AdsEmptyObject)
                return nextInput(pin);
            if (node instanceof AdsMergeObject)
                return node.getTargetPins().indexOf(pin);
            return 0;
        }
        return -1;
    }

    public int nextInput(AdsPin pin) {
        return nextInput(pin, 0);
    }

}
