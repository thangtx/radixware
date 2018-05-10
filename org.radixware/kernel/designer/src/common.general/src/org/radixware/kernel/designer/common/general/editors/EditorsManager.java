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

package org.radixware.kernel.designer.common.general.editors;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.defs.IPropertyModule;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.OrderedPage;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.common.general.editors.statistics.spi.IStatisticsProcessor;

/**
 * Manager for definition editors.
 * Allows to open definition in new or existent editor.
 * All editors are registered in layer.xml files.
 */
public abstract class EditorsManager {

    public static final String PROP_EDITOR_OPENED = "editor-opened";
    private volatile boolean wasOpened = false;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Open definition in editor.
     * @return true if editor was found and its non-modal or its modal and modal result was OK, false otherwise
     */
    public boolean open(final RadixObject target) {
        final OpenInfo openInfo = new OpenInfo(target);
        return open(target, openInfo);
    }

    public static EditorsManager getDefault() {
        final EditorsManager editorsManager = Lookup.getDefault().lookup(EditorsManager.class);
        return editorsManager;
    }

    protected RadixObject findRootForEditor(RadixObject radixObject) {
        if (radixObject.isInBranch()) {
            do {
                if (radixObject instanceof OrderedPage) {
                    final AdsEditorPageDef page = ((OrderedPage) radixObject).findPage();
                    if (page != null) {
                        final IEditorFactory<RadixObject> editorFactory = EditorFactoriesManager.getDefault().find(radixObject);
                        if (editorFactory != null) {
                            return page;
                        }
                    }
                }

                if (!(radixObject instanceof DdsColumnDef)) { // prevent use DdsColumnTemplateEditor for DdsColumnDef
                    final IEditorFactory<RadixObject> editorFactory = EditorFactoriesManager.getDefault().find(radixObject);
                    if (editorFactory != null) {
                        return radixObject;
                    }
                }
                
                if (radixObject instanceof IPropertyModule) { // editors these objects should be located in the module editor
                    return radixObject.getModule(); 
                }
                
                radixObject = radixObject.getContainer();
            } while (radixObject != null && !(radixObject instanceof Module) && !(radixObject instanceof Layer)); // prevent use module and layer editors for objects without editor.
        }
        return null;
    }
    private final RequestProcessor notifier = new RequestProcessor("RadixObjectEditorNotifier");

    /**
     * Open definition in editor with specified open information.
     * @return true if editor was found and its non-modal or its modal and modal result was OK, false otherwise
     */
    public boolean open(final RadixObject target, final OpenInfo openInfo) {
        final RadixObject root = findRootForEditor(target);
        if (root == null) {
            return false;
        }
        final IEditorFactory editorFactory = EditorFactoriesManager.getDefault().get(root);
        wasOpened = true;
        logEditorOpening(root);
        boolean result = open(root, openInfo, editorFactory);
        notifier.post(new Runnable() {

            @Override
            public void run() {
                pcs.firePropertyChange(PROP_EDITOR_OPENED, null, target);
            }
        });

        return result;
    }

    private void logEditorOpening(RadixObject radixObject) {
        Collection<? extends IStatisticsProcessor> processors = Lookup.getDefault().lookupAll(IStatisticsProcessor.class);
        if (processors != null) {
            for (IStatisticsProcessor processor : processors) {
                processor.editorOpened(radixObject);
            }
        }
    }

    public boolean wasOpened() {
        return wasOpened;
    }

    public abstract <T extends RadixObject> boolean open(T root, OpenInfo openInfo, IEditorFactory<T> editorFactory);

    public abstract void updateEditorIfOpened(final RadixObject radixObject);

    public boolean isOpeningAfterNewObjectCreationRequired(final RadixObject radixObject) {
        final IEditorFactory editorFactory = EditorFactoriesManager.getDefault().find(radixObject);
        if (editorFactory != null) {
            @SuppressWarnings("unchecked")
            final IRadixObjectEditor editor = editorFactory.newInstance(radixObject);
            return editor.isOpeningAfterNewObjectCreationRequired();
        } else {
            return false;
        }
    }

    public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
}
