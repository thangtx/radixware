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

package org.radixware.kernel.designer.tree.ads.nodes.defs;

import java.util.ArrayList;
import java.util.Collection;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.ads.ui.*;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


public class FormWidgetProvider extends MixedNodeChildrenProvider<AdsUIItemDef> {

    private AdsUIItemDef widget;
    @SuppressWarnings({"rawtypes"})
    private MixedNodeChildrenAdapter adapte;
    protected final RadixObjects.ContainerChangesListener changeListener = new RadixObjects.ContainerChangesListener() {
        @Override
        public void onEvent(ContainerChangedEvent e) {
            if (widget != null && adapte != null) {
                adapte.refresh(FormWidgetProvider.this);
            }
        }
    };

    @Override
    public Node findOrCreateNode(Key key) {
        final RadixObject radixObject = ((ObjKey) key).getObject();
        return NodesManager.findOrCreateNode(radixObject);
    }

    @Override
    public Collection<Key> updateKeys() {
        final AdsUIItemDef curWidget = widget;//AdsUIUtil.currentWidget(widget);
        final ArrayList<Key> keys = new ArrayList<>();
        if (curWidget instanceof AdsWidgetDef && ((AdsWidgetDef) curWidget).getLayout() != null) {
            final AdsWidgetDef wdg = (AdsWidgetDef) curWidget;
            for (AdsLayout.Item item : wdg.getLayout().getItems()) {
                keys.add(Key.Factory.forObject(AdsUIUtil.getItemNode(item)));
            }

            for (final AdsWidgetDef w : wdg.getWidgets()) {
                keys.add(Key.Factory.forObject(w));
            }
        } else {
            Definitions<? extends AdsUIItemDef> ws = curWidget instanceof AdsRwtWidgetDef ? ((AdsRwtWidgetDef) curWidget).getWidgets() : ((AdsWidgetDef) curWidget).getWidgets();

            for (AdsUIItemDef w : ws) {
                if (w instanceof AdsWidgetDef) {
                    AdsWidgetDef qw = ((AdsWidgetDef) w);
                    if (AdsMetaInfo.WIDGET_CLASS.equals(qw.getClassName()) && qw.getLayout() != null) {
                        keys.add(Key.Factory.forObject(qw.getLayout()));
                    } else {
                        keys.add(Key.Factory.forObject(w));
                    }
                } else {
                    keys.add(Key.Factory.forObject(w));
                }


            }
        }
        return keys;
    }

    @Override
    @SuppressWarnings({"rawtypes"})
    public boolean enterContext(MixedNodeChildrenAdapter adapter, AdsUIItemDef context) {
        this.adapte = adapter;
        this.widget = context;
        AdsUIUtil.addContainerListener(widget, changeListener);
        return widget != null;
    }

    public void refresh() {
        AdsUIUtil.addContainerListener(widget, changeListener);
        if (adapte != null) {
            adapte.refresh(this);
        }
    }

    @Override
    public ICreatureGroup[] createCreatureGroups() {
        return null;
    }
}