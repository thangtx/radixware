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
package org.radixware.kernel.designer.ads.editors.role;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.radixware.kernel.common.enums.EDrcServerResource;

public class AdsRoleEditorPanelUtils {

    private static Map<Integer, EDrcServerResource> serverResourceToIndex = null;

    private static Map<Integer, EDrcServerResource> getServerResourceToIndex() {
        if (serverResourceToIndex == null) {
            serverResourceToIndex = new TreeMap<>();
            int i = 0;
            for (EDrcServerResource res : EDrcServerResource.values()) {
                if (!res.isDeprecated()) {
                    serverResourceToIndex.put(i, res);
                    ++i;
                }
            }
        }
        return serverResourceToIndex;
    }

    protected static Object[][] getjtblServerResourceInitialObjects() {        
        List<Object[]> list = new ArrayList<>();
        for (int i = 0; i < getServerResourceCount(); ++i) {
            final String resourceTitle = getServerResourceToIndex().get(i).getAsStr();
            final Object lines[] = new Object[]{resourceTitle, null, null, null, null};
            list.add(lines);
        }
        return list.toArray(new Object[5][list.size()]);
    }

//     new Object[][]{
//                    {"Connect to Explorer Access Service", null, null, null, null},
//                    {"Selector color schemes creation in Explorer", null, null, null, null},
//                    {"Filter creation and refining in Explorer", null, null, null, null},
//                    {"Access to server file resources", null, null, null, null},
//                    {"Sorting creation and refining in Explorer", null, null, null, null},
//                    {"Debug", null, null, null, null},
//                    {"Trace", null, null, null, null},
//                    {"View audit", null, null, null, null},
//                    {"User functions development", null, null, null, null}
//                }
    protected static int getServerResourceCount() {
        return getServerResourceToIndex().size();
    }

    protected static EDrcServerResource indexToServerResource(final int i) {
        return getServerResourceToIndex().get(i);

//        EDrcServerResource serverResource;
//        switch (i) {
//
//            case 0:
//                serverResource = EDrcServerResource.EAS;
//                break;
//            case 1:
//                serverResource = EDrcServerResource.EAS_COLORING_CREATION;
//                break;
//            case 2:
//                serverResource = EDrcServerResource.EAS_FILTER_CREATION;
//                break;
//            case 3:
//                serverResource = EDrcServerResource.EAS_SERVER_FILES;
//                break;
//            case 4:
//                serverResource = EDrcServerResource.EAS_SORTING_CREATION;
//                break;
//            case 5:
//                serverResource = EDrcServerResource.DEBUG;
//                break;
//            case 6:
//                serverResource = EDrcServerResource.TRACING;
//                break;
//            case 7:
//                serverResource = EDrcServerResource.VIEW_AUDIT;
//                break;
//            /*
//             * case 8: serverResource = EDrcServerResource.USER_FUNC_DEV; break;
//             */
//            default:
//                serverResource = EDrcServerResource.USER_FUNC_DEV;
//                break;
//        }
//        return serverResource;
    }

}
