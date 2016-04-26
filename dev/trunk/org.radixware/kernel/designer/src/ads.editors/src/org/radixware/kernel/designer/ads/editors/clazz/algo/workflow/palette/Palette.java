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

import javax.swing.Action;
import java.io.IOException;
import org.openide.util.Lookup;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import org.netbeans.spi.palette.DragAndDropHandler;
import org.netbeans.spi.palette.PaletteActions;
import org.netbeans.spi.palette.PaletteController;
import org.netbeans.spi.palette.PaletteFactory;
import org.openide.nodes.AbstractNode;
import org.openide.util.datatransfer.ExTransferable;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


public class Palette {

    public static final DataFlavor MY_DATA_FLAVOR = new DataFlavor(Item.class, "My Item Data");
    public static PaletteController PALETTE = PaletteFactory.createPalette(new AbstractNode(new GroupChildren()), new MyActions(), null, new MyDnDHandler());

    public static void updatePalette(AdsDefinition context) {
        for (Group_ group : GroupChildren.groups) {
            Group_Node node = (Group_Node) NodesManager.findOrCreateNode(group);
            node.update(context);
        }
    }

    private static class MyActions extends PaletteActions {

        @Override
        public Action[] getImportActions() {
            return null;
        }

        @Override
        public Action[] getCustomPaletteActions() {
            return null;
        }

        @Override
        public Action[] getCustomCategoryActions(Lookup lookup) {
            return null;
        }

        @Override
        public Action[] getCustomItemActions(Lookup lookup) {
            return null;
        }

        @Override
        public Action getPreferredAction(Lookup lookup) {
            return null;
        }
    }

    private static class MyDnDHandler extends DragAndDropHandler {

        @Override
        public void customize(ExTransferable exTransferable, Lookup lookup) {
            final Item item = lookup.lookup(Item.class);
            exTransferable.put(new ExTransferable.Single(MY_DATA_FLAVOR) {

                @Override
                protected Object getData() throws IOException, UnsupportedFlavorException {
                    return item;
                }
            });
        }
    }
}
