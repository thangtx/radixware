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

package org.radixware.kernel.designer.dds.editors;

import java.util.List;
import org.netbeans.api.visual.model.ObjectSceneEvent;
import org.netbeans.api.visual.model.ObjectState;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.designer.dds.editors.diagram.DdsModelDiagram;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;
import org.netbeans.api.visual.model.ObjectSceneEventType;
import org.netbeans.api.visual.model.ObjectSceneListener;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.dds.editors.diagram.widgets.DdsEditorsManager;

/**
 * DDS Model Editor
 */
public class DdsModelEditor extends RadixObjectEditor<DdsModelDef> implements ObjectSceneListener {

    private final DdsModelDiagram diagram;
    private final JComponent view;

    protected DdsModelEditor(DdsModelDef model) {
        super(model);

        this.setLayout(new BorderLayout());
        this.diagram = new DdsModelDiagram(model, getFont());
        view = diagram.createView();
        JScrollPane panel = new JScrollPane(view);
        this.add(panel, BorderLayout.CENTER);

        diagram.addObjectSceneListener(this, ObjectSceneEventType.OBJECT_SELECTION_CHANGED);

        final KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK);
        view.getInputMap().put(ks, DefaultEditorKit.selectAllAction);
        view.getActionMap().put(DefaultEditorKit.selectAllAction, selectAllAction);
    }
    private final AbstractAction selectAllAction = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent e) {
            diagram.setSelectedObjects(diagram.getObjects());
        }
    };

    protected DdsModelDef getModel() {
        return super.getRadixObject();
    }

    private boolean focus(final RadixObject radixObject) {
        final Widget widget = diagram.findWidget(radixObject);
        if (widget == null) {
            return false;
        }

        diagram.setFocusedWidget(widget);

        diagram.setSelectedObjects(Collections.singleton(radixObject));
        Rectangle bounds = widget.getBounds();
        if (bounds != null) {
            bounds = widget.convertLocalToScene(bounds);
            view.scrollRectToVisible(bounds);
        }
        return true;
    }

    @Override
    public boolean open(OpenInfo openInfo) {
        super.open(openInfo);

        final RadixObject target = openInfo.getTarget();

        if (target != null) {
            for (RadixObject curObject = target; curObject != null; curObject = curObject.getOwnerDefinition()) {
                if (focus(curObject)) {
                    DdsDefinition ddsDefinition = (DdsDefinition) curObject;
                    if (curObject != target) { // for example: select for table, open for column
                        DdsEditorsManager.open(ddsDefinition, openInfo);
                    }
                    break;
                }
            }
        }

        return true;
    }

    @Override
    public void update() {
        diagram.update();
    }

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<DdsModelDef> {

        @Override
        public RadixObjectEditor newInstance(DdsModelDef model) {
            return new DdsModelEditor(model);
        }
    }

    @Override
    public void focusChanged(ObjectSceneEvent event, Object previousFocusedObject, Object newFocusedObject) {
    }

    @Override
    public void highlightingChanged(ObjectSceneEvent event, Set<Object> previousHighlighting, Set<Object> newHighlighting) {
    }

    @Override
    public void hoverChanged(ObjectSceneEvent event, Object previousHoveredObject, Object newHoveredObject) {
    }

    @Override
    public void objectAdded(ObjectSceneEvent event, Object addedObject) {
    }

    @Override
    public void objectRemoved(ObjectSceneEvent event, Object removedObject) {
    }

    @Override
    public void objectStateChanged(ObjectSceneEvent event, Object changedObject, ObjectState previousState, ObjectState newState) {
    }

    @Override
    public void selectionChanged(ObjectSceneEvent event, Set<Object> previousSelection, Set<Object> newSelection) {
        List<RadixObject> newSelectedObjects = new ArrayList<RadixObject>();
        int index = 0;
        for (Object object : newSelection) {
            DdsDefinition definition = (DdsDefinition) object;
            if (definition instanceof DdsReferenceDef) {
                newSelectedObjects.add(definition);
            } else {
                newSelectedObjects.add(index++, definition);
            }
        }
        if (newSelectedObjects.isEmpty()) {
            newSelectedObjects.add(getModel());
        }
        this.notifySelectionChanged(newSelectedObjects);
    }

    @Override
    public void requestFocus() {
        super.requestFocus();
        view.requestFocus();
    }
}
