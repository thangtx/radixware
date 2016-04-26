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

import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenAdapter;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.OrderedPage;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AbstractFormPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.EditorPagesOperationSupport;
import org.radixware.kernel.common.defs.ads.common.ICustomViewable;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


public class CustomViewProvider extends MixedNodeChildrenProvider<RadixObject> {

    private class UIKey implements Key {

        private AdsAbstractUIDef customView;

        private UIKey(AdsAbstractUIDef ui) {
            this.customView = ui;
        }
    }
    private ICustomViewable<? extends RadixObject, ? extends AdsAbstractUIDef> target;

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Key> updateKeys() {
        if (target != null) {
            if (target instanceof AdsPresentationDef) {
                AdsPresentationDef prs = (AdsPresentationDef) target;
                if (prs.isCustomViewInherited()) {
                    return Collections.emptySet();
                }
            } else if (target instanceof AbstractFormPresentations) {
                AbstractFormPresentations form = (AbstractFormPresentations) target;
                if (form.isCustomViewInherited()) {
                    return Collections.emptySet();
                }
            } else if (target instanceof OrderedPage) {
                OrderedPage page = (OrderedPage) target;
                if (page.isOwnPage()) {
                    AdsEditorPageDef ep = page.findPage();
                    List<Key> keys = new LinkedList<Key>();
                    if (ep != null) {
                        if (ep.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
                            keys.add(new UIKey(target.getCustomViewSupport().getCustomView(ERuntimeEnvironmentType.EXPLORER)));
                        }
                        if (ep.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB)) {
                            keys.add(new UIKey(target.getCustomViewSupport().getCustomView(ERuntimeEnvironmentType.WEB)));
                        }
                        return keys;
                    }
                }
                return Collections.emptySet();
            }
            List<Key> keys = new LinkedList<Key>();
            if (target.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
                Key key = new UIKey(target.getCustomViewSupport().getCustomView(ERuntimeEnvironmentType.EXPLORER));
                keys.add(key);
            }
            if (target.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB)) {
                Key key = new UIKey(target.getCustomViewSupport().getCustomView(ERuntimeEnvironmentType.WEB));
                keys.add(key);
            }
            return keys;
        }
        return Collections.emptySet();
    }

    @Override
    public Node findOrCreateNode(Key key) {
        if (key instanceof UIKey) {
            AdsAbstractUIDef uiDef = ((UIKey) key).customView;
            if (uiDef == null) {
                return null;
            } else {
                return NodesManager.findOrCreateNode(uiDef);
            }
        } else {
            return null;
        }
    }
    private MixedNodeChildrenAdapter adapter;
    private final ICustomViewable.CustomViewChangeListener listener = new ICustomViewable.CustomViewChangeListener() {

        @Override
        public void onEvent(ICustomViewable.CustomViewChangedEvent e) {
            if (adapter != null && target != null) {
                adapter.refresh(CustomViewProvider.this);
            }
        }
    };
    private final IRadixEventListener pageStateChangeListener = new IRadixEventListener() {

        @Override
        public void onEvent(RadixEvent e) {
            if (adapter != null && target != null) {
                updateListenerState(target);
                adapter.refresh(CustomViewProvider.this);
            }
        }
    };
    private IRadixEventListener inheritanceListener = null;
    private RadixEventSource changeSupport = null;

    @Override
    @SuppressWarnings({"unchecked", "unchecked", "unchecked", "unchecked"})
    public boolean enterContext(MixedNodeChildrenAdapter adapter, RadixObject context) {
        EditorPagesOperationSupport.getDefault().addEventListener(pageStateChangeListener);
        this.adapter = adapter;
        if (context instanceof ICustomViewable) {
            updateListenerState((ICustomViewable) context);
            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private void updateListenerState(ICustomViewable context) {

        this.target = (ICustomViewable<RadixObject, AdsUIDef>) context;
        ICustomViewable.CustomViewSupport support = target.getCustomViewSupport();
        if (support != null) {
            changeSupport = support.getChangeSupport();
            changeSupport.addEventListener(listener);

            if (target instanceof AdsPresentationDef || target instanceof AbstractFormPresentations) {

                inheritanceListener = new IRadixEventListener() {

                    @Override
                    public void onEvent(RadixEvent e) {
                        changeSupport.removeEventListener(listener);
                        changeSupport = CustomViewProvider.this.target.getCustomViewSupport().getChangeSupport();
                        changeSupport.addEventListener(listener);
                        CustomViewProvider.this.adapter.refresh(CustomViewProvider.this);
                    }
                };
                if (target instanceof AdsPresentationDef) {
                    ((AdsPresentationDef) target).getCustomViewInheritanceChangesSupport().addEventListener(inheritanceListener);
                } else {
                    ((AbstractFormPresentations) target).getCustomViewInheritanceChangesSupport().addEventListener(inheritanceListener);
                }
            }
        }
    }

    @Override
    public ICreatureGroup[] createCreatureGroups() {
        return null;
    }
}
