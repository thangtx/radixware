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
package org.radixware.kernel.designer.ads.editors.clazz.report.action;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance.Font;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.ads.editors.clazz.report.AdsReportFontPanel;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportFormDiagram;
import org.radixware.kernel.designer.common.dialogs.components.ComponentTitledBorder;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

public class FontAction extends AbstractAction {

    private final AdsReportFormDiagram diagram;

    public FontAction(final AdsReportFormDiagram diagram) {
        super("Adjust height", RadixWareIcons.REPORT.FONT.getIcon());
        this.diagram = diagram;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final List<RadixObject> selectedObj = diagram.getSelectedObjects();
        if (!selectedObj.isEmpty()) {
            int i;
            boolean isCellSelected = false;
            for (i = selectedObj.size() - 1; i >= 0; i--) {
                if (selectedObj.get(i) instanceof AdsReportCell) {
                    isCellSelected = true;
                    break;
                }
            }
            if (isCellSelected) {
                final AdsReportCell selectedCell = (AdsReportCell) selectedObj.get(i);
                final Font newFont = selectedCell.getFont().copy();

                final AdsReportFontPanel fontPanel = new AdsReportFontPanel(newFont, "Font");
                fontPanel.setFont(newFont, true);
                final JCheckBox cbInheritFont = new JCheckBox("Use band font");
                cbInheritFont.setSelected(selectedCell.getFontInherited());
                final javax.swing.JPanel panel = createFontPanel(fontPanel, cbInheritFont);
                fontPanel.setPanelEnabled(!selectedCell.getFontInherited());

                final ModalDisplayer modalDisplayer = new ModalDisplayer(panel, "Cells Font");
                if (modalDisplayer.showModal()) {
                    final boolean isInheritFont = cbInheritFont.isSelected();
                    for (RadixObject obj : diagram.getSelectedObjects()) {
                        if (obj instanceof AdsReportCell) {
                            final AdsReportCell cell = (AdsReportCell) obj;
                            cell.setFontInherited(isInheritFont);
                            final Font font = cell.getFont();
                            font.setName(newFont.getName());
                            font.setBold(newFont.getBold());
                            font.setItalic(newFont.getItalic());
                            font.setSizeMm(newFont.getSizeMm());
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isEnabled() {
        if (diagram != null && diagram.getMode() == AdsReportForm.Mode.TEXT) {
            return false;
        }
        return super.isEnabled(); //To change body of generated methods, choose Tools | Templates.
    }

    @SuppressWarnings("deprecation")
    private javax.swing.JPanel createFontPanel(final AdsReportFontPanel innerFontPanel, final JCheckBox cbInheritFont) {
        final javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(innerFontPanel);

        ComponentTitledBorder border = new ComponentTitledBorder(cbInheritFont, innerFontPanel, new TitledBorder(""));
        innerFontPanel.setBorder(border);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        cbInheritFont.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                final boolean inheritFont = cbInheritFont.isSelected();
                innerFontPanel.setPanelEnabled(!inheritFont);
                //if (!inheritFont/*  && !updating*/) {
                //    innerFontPanel.apply();
                //}
            }
        });
        return panel;
    }
}
