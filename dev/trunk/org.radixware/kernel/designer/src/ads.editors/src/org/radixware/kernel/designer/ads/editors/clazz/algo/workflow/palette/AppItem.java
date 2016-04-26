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

package org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.palette;

import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.EditorDialog.EditorPanel;


public abstract class AppItem extends Item {

    public static final AppItem DEFAULT = new AppItem("", Group_.GROUP_APPBLOCKS, "", RadixObjectIcon.UNKNOWN) {

        @Override
        public EditorPanel<AdsAppObject> getPanel(AdsAppObject node) {
            return null;
        }

        @Override
        public void sync(AdsAppObject node) {
        }
    };

    public AppItem(String clazz, Group_ group, String title, RadixIcon icon) {
        super(clazz, group, title, icon);
        AdsDefinitionIcon.WORKFLOW.registerIcon(clazz, icon);
    }
    /*
     * init app node with initial data
     */
    public boolean init(AdsAppObject node) {
        syncProperties(node);
        return true;
    }
    /*
     * returns app node editor panel
     */
    abstract public EditorPanel<AdsAppObject> getPanel(AdsAppObject node);
    /*
     * sync afterEdit
     */
    abstract public void sync(AdsAppObject node);
    /*
     *  sync properties if changed
     */
    final public void syncProperties(AdsAppObject node) {
        node.syncProperties();
    }
}
