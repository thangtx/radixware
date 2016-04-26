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

package org.radixware.kernel.designer.api.editors.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.utils.graphs.GraphWalker;
import org.radixware.kernel.designer.api.ApiFilter;
import org.radixware.kernel.designer.api.EditorState;
import org.radixware.kernel.designer.api.editors.OpenMode;
import org.radixware.kernel.designer.api.editors.SimpleBrick;


public class HierarchyBrick<T extends RadixObject> extends SimpleBrick<T> {

    public static final String HIERARCHY = "hierarchy";

    public HierarchyBrick(T object, GridBagConstraints constraints) {
        this(object, new BorderedRollUpPanel(new JPanel(), "Hierarchy", null, true), constraints);
    }

    private HierarchyBrick(T object, final BorderedRollUpPanel component, GridBagConstraints constraints) {
        super(object, component, constraints, HIERARCHY, null);
    }

    @Override
    public void setEditorState(EditorState state) {
        ((BorderedRollUpPanel)component).expand(state != null && state.getState() == EditorState.OpenState.OPENED);
    }

    @Override
    public EditorState getState() {
        return EditorState.Factory.createSimple(((BorderedRollUpPanel)component).isExpanded() ? EditorState.OpenState.OPENED : EditorState.OpenState.CLOSED);
    }

    @Override
    protected void beforeBuild(OpenMode mode, ApiFilter filter) {

        final JPanel panel = ((BorderedRollUpPanel) component).getContent();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        if (getSource() instanceof AdsDefinition) {

            final AdsDefinition adsDefinition = (AdsDefinition) getSource();
            final List<AdsDefinition> hierarchy = new LinkedList<>();

            new GraphWalker<AdsDefinition>().depthWalk(new GraphWalker.NodeFilter<AdsDefinition>() {
                @Override
                protected boolean accept(AdsDefinition node, int level) {
                    hierarchy.add(0, node);
                    return true;
                }

                @Override
                protected Collection<AdsDefinition> collectNodes(AdsDefinition source) {
                    final AdsDefinition.Hierarchy<AdsDefinition> hierarchy = source.getHierarchy();
                    final SearchResult<AdsDefinition> overwritten = hierarchy.findOverwritten();
                    final List<AdsDefinition> result = new ArrayList<>();
                    if (!overwritten.isEmpty()) {
                        result.addAll(overwritten.all());
                        return result;
                    }

                    final SearchResult<AdsDefinition> overridden = hierarchy.findOverridden();
                    if (!overridden.isEmpty()) {
                        result.addAll(overridden.all());
                        return result;
                    }

                    return result;
                }
            }, adsDefinition);

            if (hierarchy.size() > 1) {
                final JPanel base = new JPanel();
                base.setLayout(new GridBagLayout());

                GridBagConstraints c;

                for (int i = 0; i < hierarchy.size(); i++) {
                    c = new GridBagConstraints();
                    c.gridx = 0;
                    c.gridy = i;
                    c.insets = new Insets(2, 4 + i * 20, 2, 0);
                    c.anchor = GridBagConstraints.FIRST_LINE_START;
                    if (i == hierarchy.size() - 1) {
                        c.weighty = 1;
                        c.weightx = 1;
                        c.insets.bottom = 4;
                    }

                    final RefLabel ref = new RefLabel(hierarchy.get(i));
                    base.add(ref, c);
                }

                panel.add(base);
            }
        }
    }

    public static boolean hasHierarchy(RadixObject object) {
        if (object instanceof AdsDefinition) {

            final AdsDefinition adsDefinition = (AdsDefinition) object;
            final SearchResult<AdsDefinition> overwritten = adsDefinition.getHierarchy().findOverwritten();
            if (!overwritten.isEmpty()) {
                return true;
            }

            final SearchResult<AdsDefinition> overridden = adsDefinition.getHierarchy().findOverridden();
            if (!overridden.isEmpty()) {
                return true;
            }
        }

        return false;
    }
}
