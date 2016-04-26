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

package org.radixware.kernel.common.builder.check.dds;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.dds.DdsTableDef;


@RadixObjectCheckerRegistration
public class DdsViewChecker<T extends DdsViewDef> extends DdsTableChecker<T> {

    public DdsViewChecker() {
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return DdsViewDef.class;
    }

    @Override
    public void check(T view, IProblemHandler problemHandler) {
        super.check(view, problemHandler);

        Sqml query = view.getQuery();

        for (DdsColumnDef column : view.getColumns().get(EScope.LOCAL_AND_OVERWRITE)) {
            boolean tagExistFlag = false;
            Id columnId = column.getId();
            for (Sqml.Item item : query.getItems()) {
                if (item instanceof PropSqlNameTag) {
                    PropSqlNameTag tag = (PropSqlNameTag) item;
                    tagExistFlag = ((tag.getOwnerType() == PropSqlNameTag.EOwnerType.THIS) && (Utils.equals(tag.getPropId(), columnId)));
                    if (tagExistFlag) {
                        break;
                    }
                }
            }
            if (!tagExistFlag) {
                error(view, problemHandler, "Column '" + column.getName() + "' is not defined as 'This Column' in view query");
            }
        }

        final Set<DdsTableDef> tablesFromSqml = new HashSet<>();
        final List<Definition> dependences = new ArrayList<>();
        query.visit(new IVisitor() {
            @Override
            public void accept(RadixObject radixObject) {
                dependences.clear();
                radixObject.collectDependences(dependences);
                for (Definition def : dependences) {
                    DdsTableDef table = getTable(def);
                    if (table != null) {
                        tablesFromSqml.add(table);
                    }
                }
            }
        }, VisitorProviderFactory.createDefaultVisitorProvider());
        final List<Id> usedTableIds = view.getUsedTables().getUsedTableIds();
        for (DdsTableDef table : tablesFromSqml) {
            if (!usedTableIds.contains(table.getId())) {
                error(view, problemHandler, "Table " + table.getQualifiedName(view) + " is used in sqml, but not registered in used tables list of view");
            }
        }
        checkUsedTables(view, view, problemHandler, null);
    }

    private void checkUsedTables(DdsViewDef view, DdsViewDef rootView, IProblemHandler problemHandler, List<DdsTableDef> path) {
        List<DdsViewDef.UsedTableRef> usedTables = view.getUsedTables().getUsedTables();
        for (DdsViewDef.UsedTableRef ref : usedTables) {
            DdsTableDef table = ref.findTable();
            if (table == null && view == rootView) {
                error(view, problemHandler, "Used table not found: " + ref.getTableId());
            }
            if (table instanceof DdsViewDef) {
                List<DdsTableDef> realPath = path;
                if (path == null) {
                    realPath = new ArrayList<>();
                    realPath.add(rootView);
                }

                if (realPath.contains(table)) {
                    realPath.add(table);
                    StringBuilder message = new StringBuilder();
                    message.append("Loops in used views detected: ");
                    boolean first = true;
                    for (DdsTableDef t : path) {
                        if (!first) {
                            message.append(" -> ");
                        }
                        message.append(t.getQualifiedName());

                        if (t == table && !first) {
                            break;
                        }
                        if (first) {
                            first = false;
                        }
                    }
                    error(rootView, problemHandler, message.toString());
                    return;
                }
                realPath.add(table);
                checkUsedTables((DdsViewDef) table, rootView, problemHandler, realPath);
            }
        }
    }

    private DdsTableDef getTable(RadixObject radixObject) {
        for (RadixObject obj = radixObject; obj != null; obj = obj.getContainer()) {
            if (radixObject instanceof DdsTableDef) {
                return (DdsTableDef) radixObject;
            }
        }
        return null;
    }
}
