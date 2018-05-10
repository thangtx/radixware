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

package org.radixware.kernel.common.builder.check.ads.algo;

import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.Queue;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.generation.PageGraph;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;


@RadixObjectCheckerRegistration
public class AdsPageChecker<T extends AdsPage> extends AdsDefinitionChecker<T> {

    public AdsPageChecker() {
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsPage.class;
    }

    private static String getName(AdsBaseObject node) {
        String name = node.getName();
        if (name == null || name.isEmpty()) {
            name = node.getKind().getName();
        }
        return name;
    }

    public void checkFork(AdsForkObject fork, IProblemHandler problemHandler) {
        PageGraph graph = new PageGraph(fork.getOwnerPage());

        AdsMergeObject merge = null;
        AdsPin pin = fork.getSourcePins().get(0);
        for (int i = 0; i < graph.nextSize(pin); i++) {
            AdsBaseObject node = graph.nextNode(pin, i);

            Set<AdsBaseObject> nodes = new HashSet<AdsBaseObject>();
            Queue<AdsBaseObject> queue = new LinkedList<AdsBaseObject>();
            queue.add(node);

            while (!queue.isEmpty()) {
                AdsBaseObject n = queue.poll();

                if (n instanceof AdsForkObject) {
                    error(n, problemHandler, "Fork can not contain enclosed forks");
                    return;
                }

                if (n instanceof AdsMergeObject) {
                    if (merge != null) {
                        if (merge != n) {
                            error(n, problemHandler, "Fork can contain only one merge block");
                            return;
                        }
                    } else {
                        merge = (AdsMergeObject) n;
                    }
                    continue;
                }

                if (nodes.contains(n)) {
                    continue;
                } else {
                    nodes.add(n);
                }

                for (AdsPin p : n.getSourcePins()) {
                    AdsBaseObject next = graph.nextNode(p);
                    queue.add(next);
                }
            }
        }

        if (merge == null) {
            error(fork, problemHandler, "Fork must contain merge block");
        }
    }

    @Override
    public void check(T page, IProblemHandler problemHandler) {
        super.check(page, problemHandler);

//        // sync returns
//        for (AdsBaseObject node : page.getNodes()) {
//            if (node instanceof AdsScopeObject || node instanceof AdsCatchObject)
//                node.sync();
//        }

        HashMap<AdsPin, AdsBaseObject> entries = new HashMap<AdsPin, AdsBaseObject>();
        HashMap<AdsPin, AdsBaseObject> leaves = new HashMap<AdsPin, AdsBaseObject>();

        for (AdsBaseObject node : page.getNodes()) {
            if (node instanceof AdsIncludeObject) {
                if (((AdsIncludeObject) node).getAlgoDef() == null) {
                    error(node, problemHandler, MessageFormat.format("Algorithm is not defined for include node \"{0}\"", getName(node)));
                }
            }
            if (node instanceof AdsThrowObject) {
                if (((AdsThrowObject) node).getExceptionDef() == null) {
                    error(node, problemHandler, "Exception is not defined for throw node");
                }
            }
            if (node instanceof AdsCatchObject) {
                if (((AdsCatchObject) node).getExceptionDef() == null) {
                    error(node, problemHandler, MessageFormat.format("Exception is not defined for catch node \"{0}\"", getName(node)));
                }
            }
            if (node instanceof AdsAppObject) {
                AdsAppObject app = (AdsAppObject) node;
                if (app.isUserObject()) {
                    AdsClassDef def = app.getUserDef();
                    if (def == null) {
                        error(node, problemHandler, MessageFormat.format("Execution object is not found for applied node \"{0}\", class: <#{1}>", getName(node), app.getClazz()));
                    }
                }
            }
            if (node instanceof AdsVarObject) {
                if (((AdsVarObject) node).getType() == null) {
                    error(node, problemHandler, MessageFormat.format("Type is not defined for var node \"{0}\"", getName(node)));
                } else {
                    ((AdsVarObject) node).getType().check(node, problemHandler, getHistory().getMap());
                }
            }
            for (AdsPin pin : node.getPins()) {
                if (node.isSourcePin(pin)) {
                    leaves.put(pin, node);
                }
                if (node.isTargetPin(pin)) {
                    entries.put(pin, node);
                }
            }
        }

        for (AdsEdge edge : page.getEdges()) {
            if (entries.get(edge.getTarget()) != null) {
                entries.remove(edge.getTarget());
            }
            if (leaves.get(edge.getSource()) != null) {
                leaves.remove(edge.getSource());
            }
        }

        Set<AdsBaseObject> nodes = new HashSet<AdsBaseObject>();
        nodes.addAll(leaves.values());
        nodes.addAll(entries.values());

        boolean linked = true;
        for (AdsBaseObject node : nodes) {
            if (!(node instanceof AdsReturnObject)) {
                error(node, problemHandler, MessageFormat.format("Node \"{0}\" is not linked", getName(node)));
                linked = false;
            }
        }

        if (linked) {
            for (AdsBaseObject node : page.getNodes()) {
                if (node instanceof AdsForkObject) {
                    checkFork((AdsForkObject) node, problemHandler);
                }
            }
        }
    }
}
