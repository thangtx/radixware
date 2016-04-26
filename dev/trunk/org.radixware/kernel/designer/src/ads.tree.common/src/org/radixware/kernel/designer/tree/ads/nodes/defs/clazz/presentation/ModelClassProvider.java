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

package org.radixware.kernel.designer.tree.ads.nodes.defs.clazz.presentation;

import java.util.Collection;
import java.util.Collections;
import javax.swing.SwingUtilities;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef.ModelStateChangeEvent;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AbstractFormPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenAdapter;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider.Key;


public class ModelClassProvider extends MixedNodeChildrenProvider<RadixObject> {

    private AdsModelClassDef modelClass;
    private static final Key ALWAYS_EXIST = new Key() {
    };

    @Override
    public Collection<Key> updateKeys() {
        if (modelClass == null) {
            return Collections.emptyList();
        } else {
            return Collections.singleton(ALWAYS_EXIST);
        }
    }

    @Override
    protected Node findOrCreateNode(Key key) {
        if (key == ALWAYS_EXIST) {
            if (modelClass != null) {
                return NodesManager.findOrCreateNode(modelClass);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    private AdsPresentationDef presentation = null;
    private final AdsPresentationDef.IModelStateChangeListener modelStateListener = new AdsPresentationDef.IModelStateChangeListener() {

        @Override
        public void onEvent(ModelStateChangeEvent e) {
            if (presentation != null) {
                modelClass = presentation.getModel();
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        adapter.refresh(ModelClassProvider.this);
                    }
                });
            }
        }
    };
    private MixedNodeChildrenAdapter adapter;

    @Override
    public boolean enterContext(MixedNodeChildrenAdapter adapter, RadixObject context) {
        this.adapter = adapter;
        if (context instanceof AbstractFormPresentations) {
            modelClass = ((AbstractFormPresentations) context).getModel();
            return true;
        } else if (context instanceof AdsEditorPresentationDef) {
            modelClass = ((AdsEditorPresentationDef) context).getModel();
            presentation = ((AdsEditorPresentationDef) context);
            presentation.getModelStateChangeSupport().addEventListener(modelStateListener);
            return true;
        } else if (context instanceof AdsSelectorPresentationDef) {
            modelClass = ((AdsSelectorPresentationDef) context).getModel();
            presentation = ((AdsSelectorPresentationDef) context);
            presentation.getModelStateChangeSupport().addEventListener(modelStateListener);
            return true;
        } else if (context instanceof AdsParagraphExplorerItemDef) {
            modelClass = ((AdsParagraphExplorerItemDef) context).getModel();
            return true;
        } else if (context instanceof AdsFilterDef) {
            modelClass = ((AdsFilterDef) context).getModel();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ICreatureGroup[] createCreatureGroups() {
        return null;
    }
}
