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

package org.radixware.kernel.designer.ads.editors.clazz.forms.palette;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import org.netbeans.spi.palette.DragAndDropHandler;
import org.netbeans.spi.palette.PaletteActions;
import org.netbeans.spi.palette.PaletteController;
import org.netbeans.spi.palette.PaletteFactory;
import org.openide.nodes.AbstractNode;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.datatransfer.ExTransferable;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomWidgetDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.CustomItem;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.general.utils.DefinitionsUtils;


public class Palette {

    public static final DataFlavor MY_DATA_FLAVOR = new DataFlavor(Item.class, "My Item Data");
    public static final PaletteController DEFAULT_EXPLORER_PALETTE = PaletteFactory.createPalette(new AbstractNode(new GroupChildren(ERuntimeEnvironmentType.EXPLORER)), new MyActions(), null, new MyDnDHandler());
    public static final PaletteController DEFAULT_WEB_PALETTE = PaletteFactory.createPalette(new AbstractNode(new GroupChildren(ERuntimeEnvironmentType.WEB)), new MyActions(), null, new MyDnDHandler());
    private static List<Item> lastItems = null;

    public static PaletteController createPalette(RadixObject object) {
        Definition def = object instanceof Definition ? (Definition) object : object.getOwnerDefinition();
        if (def instanceof AdsDefinition) {
            ERuntimeEnvironmentType env = ((AdsDefinition) def).getUsageEnvironment();
            if (env == ERuntimeEnvironmentType.EXPLORER) {
                return DEFAULT_EXPLORER_PALETTE;
            } else if (env == ERuntimeEnvironmentType.WEB) {
                return DEFAULT_WEB_PALETTE;
            }
        }
        return null;
    }
    private static final Lock updateLock = new ReentrantLock();
    private static final ExecutorService exec = Executors.newSingleThreadExecutor();

    public static void updatePalette(final RadixObject object) {

        synchronized (exec) {
            exec.submit(new Runnable() {
                @Override
                public void run() {
                    if (updateLock.tryLock()) {
                        try {
                            updatePaletteImpl(object);
                        } finally {
                            updateLock.unlock();
                        }
                    }
                }
            });
        }
    }

    private static void updatePaletteImpl(RadixObject object) {

        final AdsAbstractUIDef uiDef = AdsUIUtil.getUiDef(object);
        final List<Item> items = new ArrayList<>();

        if (uiDef == null) {
            Item.registerCustom(items);
        } else {

            final AdsVisitorProvider provider = new AdsVisitorProvider() {
                @Override
                public boolean isTarget(RadixObject object) {
                    return (object instanceof AdsCustomWidgetDef || object instanceof AdsRwtCustomWidgetDef) && !object.equals(uiDef) && uiDef.getUsageEnvironment() == ((AdsAbstractUIDef) object).getUsageEnvironment();
                }
            };
            if (object.isInBranch()) {
                final Collection<Definition> collection = DefinitionsUtils.collectTopAround(object, provider);
                for (Definition def : collection) {
                    items.add(new CustomItem((AdsAbstractUIDef) def));
                }
            }

            if (uiDef.getUsageEnvironment() == ERuntimeEnvironmentType.EXPLORER) {
                Item.registerCustom(items);
            } else {
                Item.registerCustomWeb(items);
            }
        }

        if (Utils.equals(items, lastItems)) {
            return;
        }

        lastItems = items;
        GroupNode customNode = null;
        PaletteController paletteController = null;

        if (uiDef != null) {
            if (uiDef.getUsageEnvironment() == ERuntimeEnvironmentType.EXPLORER) {
                customNode = (GroupNode) NodesManager.findOrCreateNode(Group.GROUP_CUSTOM_WIDGETS);
                paletteController = DEFAULT_EXPLORER_PALETTE;
            } else {
                customNode = (GroupNode) NodesManager.findOrCreateNode(Group.GROUP_WEB_CUSTOM_WIDGETS);
                paletteController = DEFAULT_WEB_PALETTE;
            }
        }
        if (customNode != null && paletteController != null) {
            customNode.update();
            paletteController.refresh();
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
