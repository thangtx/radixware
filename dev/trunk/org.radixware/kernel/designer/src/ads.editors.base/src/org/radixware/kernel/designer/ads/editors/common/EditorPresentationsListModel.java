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

package org.radixware.kernel.designer.ads.editors.common;

import java.util.ArrayList;
import javax.swing.AbstractListModel;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.editors.common.PresentationListCfgPanel;


class EditorPresentationsListModel extends AbstractListModel {

    private PresentationListCfgPanel.PresentationsListAdapter presentations;
    private ArrayList<Id> content;

    public EditorPresentationsListModel(PresentationListCfgPanel.PresentationsListAdapter presentations) {
        content = new ArrayList<Id>();
        for (Id id : presentations.currentlySelectedIds()) {
            content.add(id);
        }
        this.presentations = presentations;
    }

    public void addElement(Definition def) {
        if (def != null) {
            int size = content.size();
            content.add(def.getId());
            fireIntervalAdded(this, size, size);
        }
    }

    public void addElement(Id id) {
        if (id != null) {
            int size = content.size();
            content.add(id);
            fireIntervalAdded(this, size, size);
        }
    }

    public void removeElement(Id id) {
        if (content.contains(id)) {
            int size = content.size();
            content.remove(id);
            fireContentsChanged(this, 0, size);
        }
    }

    @Override
    public Object getElementAt(int index) {
        return content.get(index);
    }

    @Override
    public int getSize() {
        return content.size();
    }
}
