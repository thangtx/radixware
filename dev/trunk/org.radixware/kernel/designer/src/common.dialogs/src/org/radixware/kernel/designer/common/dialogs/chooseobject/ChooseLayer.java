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

package org.radixware.kernel.designer.common.dialogs.chooseobject;

import java.util.Collections;
import java.util.Set;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


public class ChooseLayer {

    public static Set<Layer> choose(RadixObject context, Set<Layer> currentSelection,String title) {
        final Branch branch = context == null ? null : context.getBranch();
        if (branch == null) {
            return Collections.emptySet();
        } else {
            ChooseLayerPanel layerPanel = new ChooseLayerPanel(branch, currentSelection);
            ModalDisplayer displayer = new ModalDisplayer(layerPanel, title==null?"Choose Layer":title);
            if (displayer.showModal()) {
                return layerPanel.selection();
            } else {
                return null;
            }
        }
    }
    
}
