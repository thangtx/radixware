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

package org.radixware.kernel.designer.ads.common.lookup;

import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsCursorClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.types.Id;

/**
 * Utility class for simple standard class setup
 *
 */
public class AdsClassLookupSupport {

    private static abstract class Lookuper {

        public final AdsClassDef lookup(Definition context) {
            Module m = context.getModule();
            if (m == null) {
                return null;
            }
            Segment s = m.getSegment();
            if (s == null) {
                return null;
            }
            return Layer.HierarchyWalker.walk(s.getLayer(), new Layer.HierarchyWalker.Acceptor<AdsClassDef>() {

                @Override
                public void accept(HierarchyWalker.Controller<AdsClassDef> controller, Layer l) {
                    RadixObject obj = l.getAds().find(new VisitorProvider() {

                        @Override
                        public boolean isTarget(RadixObject radixObject) {
                            if (radixObject instanceof AdsClassDef) {
                                return check((AdsClassDef) radixObject);
                            } else {
                                return false;
                            }
                        }
                    });
                    if (obj != null) {
                        controller.setResultAndStop((AdsClassDef) obj);
                    }
                }
            });
        }

        public abstract boolean check(AdsClassDef clazz);
    }

    private static boolean setupSuperClass(AdsClassDef clazz, AdsClassDef superClass) {
        if (superClass != null) {
            clazz.getInheritance().setSuperClass(superClass);
            if (clazz.getModule().getDependences().findModuleById(superClass.getModule().getId()).isEmpty()) {
                clazz.getModule().getDependences().add(superClass.getModule());
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets base class for given cursor and fixup module dependences to make
     * sure superclass is being resoved
     */
    public static boolean setupCursorClass(AdsCursorClassDef cursor) {
        return setupSuperClass(cursor, findPdcCursor(cursor));
    }

    /**
     * Sets base class for given report and fixup module dependences to make
     * sure superclass is being resoved
     */
    public static boolean setupReportClass(AdsReportClassDef report) {
        return setupSuperClass(report, findPdcReport(report));
    }

    /**
     * Sets base class for given entity and fixup module dependences to make
     * sure superclass is being resoved
     */
    public static boolean setupEntityClass(AdsEntityClassDef entity) {
        return setupSuperClass(entity, findPdcEntity(entity));
    }

    /**
     * Sets base class for given entity group and fixup module dependences to
     * make sure superclass is being resoved
     */
    public static boolean setupEntityGroupClass(AdsEntityGroupClassDef entity) {
        return setupSuperClass(entity, findPdcEntityGroup(entity));
    }

    /**
     * Sets base class for given algorithm and fixup module dependences to make
     * sure superclass is being resoved
     */
    public static boolean setupAlgoClass(AdsAlgoClassDef entity) {
        return setupSuperClass(entity, findPdcAlgorithm(entity));
    }

    private static AdsClassDef findById(Definition context, final Id id) {
        return new Lookuper() {

            @Override
            public boolean check(AdsClassDef clazz) {
                return clazz.getId().equals(id);
            }
        }.lookup(context);
    }

    /**
     * Looks for standard publishing of platform cursor class {@linkplain org.radixware.kernel.server.types.Cursor}
     */
    public static AdsClassDef findPdcCursor(Definition context) {
        return findById(context, AdsCursorClassDef.PREDEFINED_ID);
    }

    /**
     * Looks for standard publishing of platform report class {@linkplain org.radixware.kernel.server.types.Report}
     */
    public static AdsClassDef findPdcReport(Definition context) {
        return findById(context, AdsReportClassDef.PREDEFINED_ID);
    }

    /**
     * Looks for standard publishing of platform cursor class {@linkplain org.radixware.kernel.server.types.Entity}
     */
    public static AdsClassDef findPdcEntity(Definition context) {
        return findById(context, AdsEntityClassDef.PREDEFINED_ID);
    }

    /**
     * Looks for standard publishing of platform cursor class {@linkplain org.radixware.kernel.server.types.EntityGroup}
     */
    public static AdsClassDef findPdcEntityGroup(Definition context) {
        return findById(context, AdsEntityGroupClassDef.PREDEFINED_ID);
    }

    /**
     * Looks for standard publishing of platform cursor class {@linkplain org.radixware.kernel.server.types.Algorithm}
     */
    public static AdsClassDef findPdcAlgorithm(Definition context) {
        return findById(context, AdsAlgoClassDef.PREDEFINED_ID);
    }
}
