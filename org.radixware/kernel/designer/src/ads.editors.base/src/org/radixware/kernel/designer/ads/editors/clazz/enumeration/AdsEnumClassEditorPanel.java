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

package org.radixware.kernel.designer.ads.editors.clazz.enumeration;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import org.radixware.kernel.common.defs.ads.clazz.AdsEnumClassDef;


public class AdsEnumClassEditorPanel extends JPanel {

    private AdsEnumClassStructureEditorPanel structureEditor;

    public AdsEnumClassEditorPanel() {
        super();
        initCompanent();
    }

    private void initCompanent() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        structureEditor = new AdsEnumClassStructureEditorPanel();
        add(structureEditor);
    }

    public void open(AdsEnumClassDef def) {
        structureEditor.open(def);
    }

    public void update() {
        structureEditor.update();
    }

    public void setReadonly(boolean readonly) {
        structureEditor.setReadonly(readonly);
    }
}
