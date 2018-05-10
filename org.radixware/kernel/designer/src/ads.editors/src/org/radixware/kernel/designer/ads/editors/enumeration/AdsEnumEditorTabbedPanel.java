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
package org.radixware.kernel.designer.ads.editors.enumeration;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;

public class AdsEnumEditorTabbedPanel extends JPanel {

    private javax.swing.JTabbedPane tabbedPanel = new javax.swing.JTabbedPane();
    private EnumerationTablePanel adsEnumEditorPanel = new EnumerationTablePanel();
    private ValueRangesPanel valueRangesPanel = new ValueRangesPanel();

    /** Creates new form AdsEnumEditorTabbedPanel */
    public AdsEnumEditorTabbedPanel() {
        setLayout(new BorderLayout());
        add(tabbedPanel, BorderLayout.CENTER);
        tabbedPanel.add(NbBundle.getMessage(AdsEnumEditorTabbedPanel.class, "ItemTabs-Values"), adsEnumEditorPanel);
        tabbedPanel.add(NbBundle.getMessage(AdsEnumEditorTabbedPanel.class, "ItemTabs-Ranges"), valueRangesPanel);
    }

    public void open(AdsEnumDef radixObject, OpenInfo info) {
        adsEnumEditorPanel.open(radixObject);
        valueRangesPanel.open(radixObject);
    }

    public void update() {
        adsEnumEditorPanel.update();
        valueRangesPanel.update();
    }

    public void onExtendablityChange(boolean enabled) {
        adsEnumEditorPanel.onExtendabilityChange(enabled);
    }

    public void onSynchronizeAction(ActionEvent evt) {
        adsEnumEditorPanel.onSynchronizeAction(evt);
    }

    public void setSelectedEnumItem(AdsEnumItemDef adsEnumItemDef){
        adsEnumEditorPanel.setSelectedItem(adsEnumItemDef);
    }
}
