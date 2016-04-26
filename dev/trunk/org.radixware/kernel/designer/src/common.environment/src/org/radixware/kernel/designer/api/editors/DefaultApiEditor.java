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

package org.radixware.kernel.designer.api.editors;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Collection;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.utils.graphs.GraphWalker;
import org.radixware.kernel.designer.api.ApiFilter;
import org.radixware.kernel.designer.api.EditorState;
import org.radixware.kernel.designer.api.IApiEditor;


public abstract class DefaultApiEditor<T extends RadixObject> extends JPanel implements IApiEditor<T> {

    private final T source;
    private OpenMode mode;
    private ApiFilter filter;

    protected DefaultApiEditor(T source) {
        this.source = source;
    }

    @Override
    public void open(OpenMode mode, ApiFilter filter, RadixObject subTarget) {
        this.mode = mode;
        this.filter = filter;
        build();

        select(subTarget);
    }

    @Override
    public boolean select(final RadixObject subTarget) {
        synchronized (modelLock) {
            if (model == null) {
                return false;
            }

            if (getSource() == subTarget) {
                return true;
            }

            final GraphWalker walker = new GraphWalker();
            final GraphWalker.SearchResult<ApiEditorModel> result = walker.breadthSearch(new GraphWalker.SearchNodeFilter<ApiEditorModel>() {
                @Override
                protected boolean isTarget(ApiEditorModel node, int level) {
                    return node.getSource() == subTarget;
                }

                @Override
                protected Collection collectNodes(ApiEditorModel source) {
                    return source.getBricks();
                }
            }, model);

            if (result.isObtained) {
                for (final ApiEditorModel brik : result.way) {
                    brik.setEditorState(EditorState.Factory.createSimple(EditorState.OpenState.OPENED));
                }

                final Container currView = result.target.getCurrentView();

                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        int y = currView.getLocationOnScreen().y - DefaultApiEditor.this.getLocationOnScreen().y;
                        
                        final JScrollPane scrollPane = findScroll(getParent());
                        if (scrollPane != null) {
                            scrollPane.getVerticalScrollBar().setValue(y);
                        }
                    }
                });
            }
            return true;
        }
    }

    @Override
    public void open(OpenMode mode, ApiFilter filter) {
        open(mode, filter, null);
    }

    @Override
    public EditorState getState() {
        synchronized (modelLock) {
            if (model == null) {
                return null;
            }

            final EditorState editorState = EditorState.Factory.createComplex(EditorState.OpenState.OPENED);
            editorState.putControllState("model", model.getState());
            if (!isEmbedded()) {
                final JScrollPane scrollPane = findScroll(getParent());
                if (scrollPane != null) {
                    editorState.setScrollPosition(scrollPane.getVerticalScrollBar().getValue());
                }
            }

            return editorState;
        }
    }

    @Override
    public void setEditorState(final EditorState state) {
        if (state == null) {
            return;
        }
        synchronized (modelLock) {
            if (model == null) {
                return;
            }
            model.setEditorState(state.getControllState("model"));

            if (!isEmbedded()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        final JScrollPane scrollPane = findScroll(getParent());
                        if (scrollPane != null) {
                            scrollPane.getVerticalScrollBar().setValue(state.getScrollPosition());
                        }
                    }
                });
            }
        }
    }

    private JScrollPane findScroll(Container component) {
        Container curr = component;
        while (curr != null) {
            if (curr instanceof JScrollPane) {
                return (JScrollPane) curr;
            }
            curr = curr.getParent();
        }

        return null;
    }

    @Override
    public void update() {
        if (getSource().isInBranch()) {
            final EditorState state = getState();
            open(mode, filter);
            setEditorState(state);
        }
    }

    @Override
    public final JComponent getComponent() {
        return this;
    }

    protected BrickFactory createFactory() {
        return BrickFactory.getDefault();
    }

    @Override
    public T getSource() {
        return source;
    }

    protected ApiEditorBuilder createBuilder() {
        return new DefaultApiEditorBuilder(source, createFactory());
    }

    protected final OpenMode getOpenMode() {
        return mode;
    }
    private ApiEditorModel model;
    private final Object modelLock = new Object();

    protected final void build() {
        synchronized (modelLock) {
            setLayout(new BorderLayout());

            final ApiEditorModel buildModel = createBuilder().buildModel();
            final JComponent view = buildModel.getView(mode, filter);

            removeAll();
            add(view, BorderLayout.CENTER);
            revalidate();

            model = buildModel;
        }
    }
}
