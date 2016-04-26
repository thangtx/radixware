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

package org.radixware.kernel.designer.dds.script;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.radixware.kernel.common.defs.dds.DdsCheckConstraintDef;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsPackageDef;
import org.radixware.kernel.common.defs.dds.DdsPath;
import org.radixware.kernel.common.defs.dds.DdsPlSqlBodyDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlHeaderDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlPartDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef;
import org.radixware.kernel.common.defs.dds.DdsTypeDef;
import org.radixware.kernel.common.defs.dds.DdsUniqueConstraintDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;
import org.radixware.kernel.common.defs.dds.IDdsDbDefinition;


class DefinitionPairs implements Iterable<DefinitionPair> {

    private final ArrayList<DefinitionPair> pairs = new ArrayList<DefinitionPair>();

    @Override
    public Iterator<DefinitionPair> iterator() {
        return pairs.iterator();
    }

    public int size() {
        return pairs.size();
    }

    public DefinitionPair get(int index) {
        return pairs.get(index);
    }

    private void add(DdsDefinition oldDefinition, DdsDefinition newDefinition) {
        if (oldDefinition instanceof DdsPlSqlObjectDef || newDefinition instanceof DdsPlSqlObjectDef) {
            final DdsPlSqlObjectDef oldPlSqlObject = (DdsPlSqlObjectDef) oldDefinition;
            final DdsPlSqlObjectDef newPlSqlObject = (DdsPlSqlObjectDef) newDefinition;

            // add headers
            final DdsPlSqlHeaderDef oldHeader = (oldPlSqlObject != null ? oldPlSqlObject.getHeader() : null);
            final DdsPlSqlHeaderDef newHeader = (newPlSqlObject != null ? newPlSqlObject.getHeader() : null);
            pairs.add(new DefinitionPair(oldHeader, newHeader));

            // add bodies
            final DdsPlSqlBodyDef oldBody = (oldPlSqlObject != null && !oldPlSqlObject.getBody().getItems().isEmpty() ? oldPlSqlObject.getBody() : null);
            final DdsPlSqlBodyDef newBody = (newPlSqlObject != null && !newPlSqlObject.getBody().getItems().isEmpty() ? newPlSqlObject.getBody() : null);
            if (oldBody != null || newBody != null) {
                pairs.add(new DefinitionPair(oldBody, newBody));
            }
        } else {
            pairs.add(new DefinitionPair(oldDefinition, newDefinition));
        }
    }

    private Map<DdsPath, DdsDefinition> toMapByPath(Collection<DdsDefinition> ddsDefinitions) {
        if (ddsDefinitions == null) {
            return null;
        }
        final Map<DdsPath, DdsDefinition> map = new HashMap<DdsPath, DdsDefinition>(ddsDefinitions.size());
        for (DdsDefinition ddsDefinition : ddsDefinitions) {
            final DdsPath path = new DdsPath(ddsDefinition);
            map.put(path, ddsDefinition);
        }
        return map;
    }

    protected DefinitionPairs(Collection<DdsDefinition> oldDefinitions, Collection<DdsDefinition> newDefinitions) {
        final Map<DdsPath, DdsDefinition> path2OldDefinition = toMapByPath(oldDefinitions);
        final Map<DdsPath, DdsDefinition> path2NewDefinition = toMapByPath(newDefinitions);

        if (oldDefinitions != null) {
            for (DdsDefinition oldDefinition : oldDefinitions) {
                final DdsPath path = new DdsPath(oldDefinition);
                final DdsDefinition newDefinition = (path2NewDefinition != null ? path2NewDefinition.get(path) : null);
                add(oldDefinition, newDefinition);
            }
        }

        if (newDefinitions != null) {
            for (DdsDefinition newDefinition : newDefinitions) {
                final DdsPath path = new DdsPath(newDefinition);
                final DdsDefinition oldDefinition = (path2OldDefinition != null ? path2OldDefinition.get(path) : null);
                if (oldDefinition == null) {
                    add(oldDefinition, newDefinition);
                }
            }
        }

        Collections.sort(pairs, new ByCreateOrderComparator());
    }

    private class ByCreateOrderComparator implements Comparator<DefinitionPair> {

        private int getPriority(DefinitionPair pair) {
            final DdsDefinition definition = pair.getAnyDefinition();

            if ((definition instanceof DdsPlSqlHeaderDef) && (((DdsPlSqlHeaderDef) definition).getPlSqlObjectDef() instanceof DdsTypeDef)) {
                return 1;
            }
            if ((definition instanceof DdsPlSqlHeaderDef) && (((DdsPlSqlHeaderDef) definition).getPlSqlObjectDef() instanceof DdsPackageDef)) {
                return 3;
            }
            if (definition instanceof DdsSequenceDef) {
                return 4;
            }
            if (definition instanceof DdsTableDef && !(definition instanceof DdsViewDef)) {
                return 5;
            }
            if (definition instanceof DdsColumnDef) {
                DdsColumnDef column = (DdsColumnDef) definition;
                if (column.getExpression() == null) {
                    return 6;
                } else {
                    return 7;
                }
            }
            if (definition instanceof DdsCheckConstraintDef) {
                return 9;
            }
            if (definition instanceof DdsIndexDef) {
                return 10;
            }
            if (definition instanceof DdsUniqueConstraintDef) {
                return 11;
            }
            if (definition instanceof DdsViewDef) {
                return 12;
            }
            if (definition instanceof DdsReferenceDef) {
                return 13;
            }
            if ((definition instanceof DdsPlSqlBodyDef) && (((DdsPlSqlBodyDef) definition).getPlSqlObjectDef() instanceof DdsTypeDef)) {
                return 14;
            }
            if ((definition instanceof DdsPlSqlBodyDef) && (((DdsPlSqlBodyDef) definition).getPlSqlObjectDef() instanceof DdsPackageDef)) {
                return 15;
            }
            if (definition instanceof DdsTriggerDef) {
                return 16;
            }
            if (definition instanceof DdsModelDef) {
                return 17;
            }

            throw new IllegalStateException("Illegal object in " + this.getClass().getName() + ": " + String.valueOf(definition));
        }

        @Override
        public int compare(DefinitionPair pair1, DefinitionPair pair2) {
            int p1 = getPriority(pair1);
            int p2 = getPriority(pair2);
            int result = p1 - p2;
            if (result == 0) {
                if (p1 == 12) {//DdsViewDef
                    //look for dependences
                    final DdsDefinition def1 = pair1.getNewDefinition();
                    final DdsDefinition def2 = pair2.getNewDefinition();
                    if (def1 instanceof DdsViewDef && def2 instanceof DdsViewDef) {
                        if (((DdsViewDef) def1).isDependsFromOtherView(def2.getId())) {
                            return 1;
                        }else if (((DdsViewDef) def2).isDependsFromOtherView(def1.getId())) {
                            return -1;                            
                        }
                    }
                }

                final DdsDefinition def1 = pair1.getAnyDefinition();
                final DdsDefinition def2 = pair2.getAnyDefinition();
                final String name1 = getComparableName(def1);
                final String name2 = getComparableName(def2);
                return name1.compareTo(name2);
            }
            return result;
        }

        public String getComparableName(DdsDefinition def) {
            if (def instanceof DdsPlSqlPartDef) {
                return ((DdsPlSqlPartDef) def).getPlSqlObjectDef().getDbName();
            } else {
                return (def instanceof IDdsDbDefinition ? ((IDdsDbDefinition) def).getDbName() : def.getName());
            }
        }
    }
}
