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

package org.radixware.kernel.designer.ads.common;

import java.util.Collection;
import java.util.Iterator;
import javax.swing.JOptionPane;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.designer.common.general.utils.DefinitionsUtils;


public class EventSourceEnumFinder {

    public AdsEnumDef findEventSourceEnum(Definition context) {
        VisitorProvider vp = new VisitorProvider() {

            @Override
            public boolean isTarget(RadixObject radixObject) {
                if (radixObject instanceof AdsEnumDef) {
                    AdsEnumDef eDef = (AdsEnumDef) radixObject;
                    if (eDef.getPublishedPlatformEnumName() != null && eDef.getPublishedPlatformEnumName().equals(EEventSource.class.getName())) {
                        return true;
                    }
                }
                return false;
            }
        };
        Collection<Definition> enumCollection = DefinitionsUtils.collectAllAround(context, vp);
        Iterator<Definition> it = enumCollection.iterator();
        if (it.hasNext()) {
            return (AdsEnumDef) it.next();
        }

        AdsEnumDef eDef = (AdsEnumDef) context.getModule().getSegment().getLayer().getBranch().find(vp);
        if (eDef != null) {
            Module module = eDef.getModule();
            if (module != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("\tNo event sources can be found in dependencies of current context,\n");
                sb.append("but module \"" + module.getQualifiedName() + "\" contains required data.\n");
                sb.append("\n\tDo you want to add \"" + module.getQualifiedName() + "\" to dependencies of current module?");

                int result = JOptionPane.showConfirmDialog(null, sb.toString(), "EventSource List not Found", JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {
                    context.getModule().getDependences().add(module);
                } else {
                    return null;
                }
            }
        }
        return eDef;
    }
}
