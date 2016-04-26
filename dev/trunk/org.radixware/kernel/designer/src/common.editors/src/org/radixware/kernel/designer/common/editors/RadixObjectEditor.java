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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;
import org.openide.util.Lookup;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;

/**
 * {@linkplain RadixObject} editor.
 */
public abstract class RadixObjectEditor<T extends RadixObject> extends JPanel implements IRadixObjectEditor<T> {

    private final T radixObject;

    protected RadixObjectEditor(T radixObject) {
        super();
        this.radixObject = radixObject;
        notifySelectionChanged(Collections.singletonList(radixObject));
    }

    public T getRadixObject() {
        return radixObject;
    }

    public Lookup getLookup() {
        return null;
    }

    @Override
    public boolean open(OpenInfo openInfo) {
        return false;
    }

    @Override
    public boolean isOpeningAfterNewObjectCreationRequired() {
        return true;
    }
    private volatile List<RadixObject> selectedObjects = null;

    public List<RadixObject> getSelectedObjects() {
        if (selectedObjects != null) {
            return selectedObjects;
        } else {
            return Collections.emptyList();
        }
    }
    private final RadixEventSource selectionSupport = new RadixEventSource();

    public void notifySelectionChanged(final List<? extends RadixObject> newSelectedObjects) {
        this.selectedObjects = new ArrayList<RadixObject>(newSelectedObjects);
        selectionSupport.fireEvent(new RadixEvent());
    }

    public RadixEventSource getSelectionSupport() {
        return selectionSupport;
    }

    public boolean isShowProperties() {
        return false;
    }

    public void onClosed() {
        // NOTHING by default
    }

    public void onHidden() {
        // NOTHING by default
    }

    public void onShown() {
        // NOTHING by default
    }

    public void onActivate() {
        // NOTHING by default
    }

    public boolean canClose(){
        return true;
    }
}
