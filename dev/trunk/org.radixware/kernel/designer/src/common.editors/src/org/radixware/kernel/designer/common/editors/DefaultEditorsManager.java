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

package org.radixware.kernel.designer.common.editors;

import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import org.openide.util.Mutex;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;


public class DefaultEditorsManager extends EditorsManager {

    private final Map<RadixObject, RadixObjectTopComponent> radixObject2TopComponent = new WeakHashMap<RadixObject, RadixObjectTopComponent>();
    private final IRadixEventListener closedListener = new IRadixEventListener() {

        @Override
        public void onEvent(RadixEvent e) {
            removeClosedEditorsInfo();
        }
    };

    public <T extends RadixObject> boolean openInReadAccess(final T root, final OpenInfo openInfo, final IEditorFactory<T> editorFactory) {
        RadixObjectTopComponent tc = radixObject2TopComponent.get(root);
        if (tc != null) {
            return tc.open(openInfo);
        }

        final IRadixObjectEditor editor;
        if (root instanceof AdsDefinition && ((AdsDefinition) root).getSaveMode() == AdsDefinition.ESaveMode.API) {
            editor = new APIDefinitionEditor((AdsDefinition) root);
        } else {
            editor = editorFactory.newInstance(root);
        }

        if (editor instanceof RadixObjectModalEditor) {
            final RadixObjectModalEditor modalEditor = (RadixObjectModalEditor) editor;
            final RadixObjectModalDisplayer modalDisplayer = new RadixObjectModalDisplayer(modalEditor);
            return modalDisplayer.open(openInfo);
        } else if (editor instanceof RadixObjectEditor) {
            final RadixObjectEditor simpleEditor = (RadixObjectEditor) editor;
            tc = new RadixObjectTopComponent(simpleEditor);
            //tc.requestActive();//trick for giving focus to ScmlEditorPane when user wants to go to the problem.
            radixObject2TopComponent.put(root, tc);
            tc.addClosedListener(closedListener);
            return tc.open(openInfo);
        } else {
            throw new UnsupportedOperationException("Unable to display " + editor.getClass().getName());
        }
    }

    @Override
    public <T extends RadixObject> boolean open(final T root, final OpenInfo openInfo, final IEditorFactory<T> editorFactory) {

        return RadixMutex.readAccess(new Mutex.Action<Boolean>() {

            @Override
            public Boolean run() {
                return openInReadAccess(root, openInfo, editorFactory);
            }
        });
    }

    @Override
    public void updateEditorIfOpened(final RadixObject radixObject) {
        final RadixObject root = findRootForEditor(radixObject);
        if (root != null) {
            final RadixObjectTopComponent tc = radixObject2TopComponent.get(root);
            if (tc != null && tc.isOpened()) {
                tc.update();
                tc.repaint();
            }
        }
    }

    private void removeClosedEditorsInfo() {
        final Iterator<Map.Entry<RadixObject, RadixObjectTopComponent>> iter = radixObject2TopComponent.entrySet().iterator();
        while (iter.hasNext()) {
            final Map.Entry<RadixObject, RadixObjectTopComponent> entry = iter.next();
            final RadixObjectTopComponent tc = entry.getValue();
            if (!tc.isOpened()) {
                entry.setValue(null); // help gc
                iter.remove();
            }
        }
    }
}
