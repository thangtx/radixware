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

package org.radixware.kernel.designer.ads.editors.clazz.forms.widget;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.LayoutProcessor;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.PanelWebLayout;


public class RwtPanelWidget extends BaseWidget {

    public RwtPanelWidget(GraphSceneImpl scene, AdsRwtWidgetDef node) {
        super(scene, node);
        setLayoutProcessor(LayoutProcessor.Factory.newInstance(this, node));
    }

    @Override
    protected WidgetPopupMenu createPopupMenu() {
        return new RwtPanelPopupMenu();
    }

    static class RwtPanelPopupMenu extends WidgetPopupMenu {

        @Override
        public JPopupMenu getPopupMenu(Widget widget, Point localLocation) {
            JPopupMenu menu = super.getPopupMenu(widget, localLocation);
            final BaseWidget bw = (BaseWidget) widget;
            RadixObject node = bw.getNode();
            if (node instanceof AdsRwtWidgetDef) {
                final AdsRwtWidgetDef w = (AdsRwtWidgetDef) node;
                Map.Entry<AdsRwtWidgetDef.PanelGrid.Row.Cell, Rectangle> cell = PanelWebLayout.cellFromPoint(bw, localLocation);
                if (cell != null) {
                    final AdsRwtWidgetDef.PanelGrid.Row.Cell cellObj = cell.getKey();

                    if (menu.getComponents() != null && menu.getComponents().length > 0) {
                        menu.addSeparator();
                    }
                    menu.add(new JMenuItem(new AbstractAction("Add Cell to Current Row") {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    cellObj.getRow().addCell();
                                    bw.revalidate();
                                }
                            });
                        }
                    }));
                    menu.add(new JMenuItem(new AbstractAction("Remove Cell") {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    cellObj.getRow().removeCell(cellObj);
                                    bw.revalidate();
                                }
                            });
                        }
                    }));
                    menu.addSeparator();
                    menu.add(new JMenuItem(new AbstractAction("Add Row") {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    w.getPanelGrid().addRow();
                                    bw.revalidate();
                                }
                            });
                        }
                    }));
                    menu.add(new JMenuItem(new AbstractAction("Remove Row") {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    w.getPanelGrid().removeRow(cellObj.getRow());
                                    bw.revalidate();
                                }
                            });
                        }
                    }));
                }
            }
            return menu;
        }
    }
}
