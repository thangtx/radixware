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

package org.radixware.kernel.designer.ads.editors.filters;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.designer.common.editors.sqml.SqmlEditorPanel;
import org.openide.util.NbBundle;


public class AdsFilterTabbedPanel extends JTabbedPane {

    private AdsFilterDef adsFilterDef;
    private SqmlEditorPanel hintEditorPanel, conditionEditorPanel;
    private SortingsPanel sortingsPanel;
    private double dividerLocation = 0.75;
    private boolean isDividerAutoSet = false;
    private boolean wasShown = false;

    ;

    public AdsFilterTabbedPanel() {
        super();

        conditionEditorPanel = new SqmlEditorPanel();
        conditionEditorPanel.setAlignmentX(0.0f);
        conditionEditorPanel.setBorder(new TitledBorder(NbBundle.getMessage(AdsFilterTabbedPanel.class, "Condition")));

        hintEditorPanel = new SqmlEditorPanel();
        hintEditorPanel.setAlignmentX(0.0f);
        hintEditorPanel.setBorder(new TitledBorder(NbBundle.getMessage(AdsFilterTabbedPanel.class, "Hint")));

        final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, conditionEditorPanel, hintEditorPanel);
        splitPane.setBorder(null);
        splitPane.setContinuousLayout(true);

        splitPane.setDividerLocation(dividerLocation);

        splitPane.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (JSplitPane.DIVIDER_LOCATION_PROPERTY.equals(evt.getPropertyName())) {
                    if (isDividerAutoSet) {
                        return;
                    } else {
                        if (splitPane.getLastDividerLocation() >= 0 && splitPane.getLastDividerLocation() != splitPane.getDividerLocation()) {
                            dividerLocation = (double) splitPane.getDividerLocation() / (double) splitPane.getHeight();
                        }
                    }
                }
            }
        });

        splitPane.addComponentListener(new ComponentListener() {

            @Override
            public void componentResized(ComponentEvent e) {
                try {
                    isDividerAutoSet = true;
                    splitPane.setDividerLocation(dividerLocation);
                } finally {
                    isDividerAutoSet = false;
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {
//                try {
//                    isDividerAutoSet = true;
//                    splitPane.setDividerLocation(dividerLocation);
//                } finally {
//                    isDividerAutoSet = false;
//                }
            }

            @Override
            public void componentShown(ComponentEvent e) {
//                try {
//                    isDividerAutoSet = true;
//                    splitPane.setDividerLocation(dividerLocation);
//                } finally {
//                    isDividerAutoSet = false;
//                    wasShown = true;
//                }
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });

        //first tab
        final JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        p.add(splitPane);
        this.addTab(NbBundle.getMessage(AdsFilterTabbedPanel.class, "Condition"), p);

        sortingsPanel = new SortingsPanel();
        sortingsPanel.setAlignmentX(0.0f);

        //second tab
        this.addTab(NbBundle.getMessage(AdsFilterTabbedPanel.class, "Sortings"), sortingsPanel);

        //select first tab as default
        this.setSelectedIndex(this.indexOfComponent(p));
    }

    public void open(AdsFilterDef adsFilterDef) {
        this.adsFilterDef = adsFilterDef;
        hintEditorPanel.open(adsFilterDef.getHint());
        conditionEditorPanel.open(adsFilterDef.getCondition());
        sortingsPanel.open(adsFilterDef);
        setReadonly(adsFilterDef.isReadOnly());
    }

    public void update() {
        hintEditorPanel.update();
        conditionEditorPanel.update();
        sortingsPanel.update();
    }

    public void setReadonly(boolean readonly) {
        hintEditorPanel.setEditable(!adsFilterDef.isReadOnly() && !readonly);
        conditionEditorPanel.setEditable(!adsFilterDef.isReadOnly() && !readonly);
        sortingsPanel.setEnabled(!readonly);
    }
}
