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

package org.radixware.kernel.designer.common.dialogs.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.netbeans.api.progress.ProgressHandle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.repository.Branch;


class Move {

    private final List<RadixObject> movedObjects;
    private final RadixObject destination;

    public Move(List<RadixObject> movedObjects, RadixObject destination) {
        this.movedObjects = movedObjects;
        this.destination = destination;
    }

    private Set<RadixObject> calcMovedObjectsRecurs() {
        final Set<RadixObject> movedObjectsRecurs = new HashSet<RadixObject>();
        for (RadixObject movedObject : movedObjects) {
            movedObject.visitAll(new IVisitor() {

                @Override
                public void accept(RadixObject radixObject) {
                    movedObjectsRecurs.add(radixObject);
                }
            });
        }
        return movedObjectsRecurs;
    }

    private static class ProgressHandlerHolder {

        private final ProgressHandle progressHandle;
        private final long objectCount;
        private long processedCount = 0;
        private long procent = 0;

        public ProgressHandlerHolder(ProgressHandle progressHandle, final RadixObject context) {
            this.progressHandle = progressHandle;
            this.objectCount = calcObjectCount(context);
            progressHandle.start(100);
        }

        private static long calcObjectCount(RadixObject context) {
            final long[] countHolder = {0};

            context.visitAll(new IVisitor() {

                @Override
                public void accept(RadixObject radixObject) {
                    countHolder[0]++;
                }
            });

            return countHolder[0];
        }

        public void inc() {
            processedCount++;
            int newProcent = (int) ((1.0 * processedCount / objectCount) * 100);
            if (newProcent != procent) {
                progressHandle.progress(newProcent);
                procent = newProcent;
            }
        }
    }

    private Map<RadixObject, List<Definition>> calcExternalLinks(final Branch branch, final Set<RadixObject> movedObjectsRecurs, final ProgressHandle progressHandle) {
        final ProgressHandlerHolder progressHandlerHolder = new ProgressHandlerHolder(progressHandle, branch);

        final Map<RadixObject, List<Definition>> externalLinks = new HashMap<RadixObject, List<Definition>>();

        branch.visitAll(new IVisitor() {

            @Override
            public void accept(RadixObject radixObject) {
                if (!movedObjectsRecurs.contains(radixObject)) {
                    final List<Definition> links = new ArrayList<Definition>();
                    radixObject.collectDirectDependences(links);
                    final Iterator<Definition> iterator = links.iterator();
                    while (iterator.hasNext()) {
                        final Definition link = iterator.next();
                        if (!movedObjectsRecurs.contains(link)) {
                            iterator.remove();
                        }
                    }
                    if (!links.isEmpty()) {
                        externalLinks.put(radixObject, links);
                    }
                }
                progressHandlerHolder.inc();
            }
        });

        return externalLinks;
    }

    public List<IMoveTransformation> calcTransformations(ProgressHandle progressHandle) {
        final List<IMoveTransformation> result = new ArrayList<IMoveTransformation>();

        final Branch branch = destination.getBranch();
        final Set<RadixObject> movedObjectsRecurs = calcMovedObjectsRecurs();
        final Map<RadixObject, List<Definition>> externalLinks = calcExternalLinks(branch, movedObjectsRecurs, progressHandle);

        for (Map.Entry<RadixObject, List<Definition>> entry : externalLinks.entrySet()) {
            final RadixObject externalRadixObject = entry.getKey();
            for (Definition movedDef : entry.getValue()) {
                IMoveTransformationFactory factory = IMoveTransformationFactory.Lookup.getFactory(movedDef);
                final IMoveTransformation transformation = factory.findTransformation(externalRadixObject, movedDef, destination);
                if (transformation != null) {
                    result.add(transformation);
                }
            }
        }

        return result;
    }
}
