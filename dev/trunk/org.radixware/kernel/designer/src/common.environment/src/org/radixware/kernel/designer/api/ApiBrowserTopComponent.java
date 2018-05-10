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

package org.radixware.kernel.designer.api;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jdesktop.swingx.JXSearchField;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.api.editors.OpenMode;
import org.radixware.kernel.designer.api.filters.AccessModel;
import org.radixware.kernel.designer.api.filters.RadixObjectNameFilter;
import org.radixware.kernel.designer.api.filters.AccessView;
import org.radixware.kernel.designer.api.filters.DescriptionMode;
import org.radixware.kernel.designer.api.filters.DescriptionModeModel;
import org.radixware.kernel.designer.api.filters.DescriptionModeView;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.components.GenericComboBox;
import org.radixware.kernel.designer.common.dialogs.components.selector.EmptyTextFilter;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


@TopComponent.Description(
        preferredID = "ApiBrowserTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE",
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@NbBundle.Messages({
    "ApiBrowserTopComponent_DisplayName=API Browser",
    "ApiBrowserTopComponent_SelectInProjects=Select in projects",
    "ApiBrowserTopComponent_Refresh=Refresh"
})
final class ApiBrowserTopComponent extends TopComponent {

    public static class Factory {

        private static final ApiBrowserTopComponent API_BROWSER_TOP_COMPONENT = new ApiBrowserTopComponent();

        public static ApiBrowserTopComponent findInstance() {
            return API_BROWSER_TOP_COMPONENT;
        }
        
        public static ApiBrowserTopComponent createInstance() {
            return new ApiBrowserTopComponent(); 
        }
    }

    private class ApiFilterPanel extends JPanel {

        private final GenericComboBox apiLevel;
        private final JLabel searchLabel = new JLabel("Search:");
        private final GenericComboBox descriptionMode;
        public ApiFilterPanel() {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            final GenericComboBox.PopupView view = new AccessView();
            apiLevel = new GenericComboBox(view) {
                @Override
                public Dimension getMaximumSize() {
                    return super.getPreferredSize();
                }
            };

            apiLevel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    final AccessModel model = (AccessModel) view.getModel();

                    if (view.isChanged()) {
                        final ApiFilter apiFilter = getApiFilter();
                        apiFilter.setAccessLevel(model.getAccessLevel().toArray(new EAccess[0]));
                        apiFilter.setShowNotPublished(!model.isOnlyPublished());

                        ApiBrowserTopComponent.this.updateEditor();

                        updateToolTip();
                    }
                }
            });
            
            final DescriptionModeView descriptionModeView = new DescriptionModeView();
            descriptionMode = new GenericComboBox(descriptionModeView);
            descriptionMode.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateDescriptionMode();
                }
            });
            RadixObjectNameFilter textFilter = getApiFilter().getTextFilter();
            final JXSearchField field = textFilter.getSearchField();
            textFilter.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    ApiBrowserTopComponent.this.updateEditor();
                    field.requestFocus();
                }
            });
            
            updateToolTip();

            add(apiLevel);
            add(new Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767)));
            add(descriptionMode);
            add(new Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767)));
            add(searchLabel);
            add(new Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767)));
            add(field);
            add(new Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767)));

        }
        
        private void updateDescriptionMode() {
            final DescriptionModeView descriptionModeView = (DescriptionModeView) descriptionMode.getView();
           if (descriptionModeView.isChanged()) { 
               final DescriptionModeModel model = (DescriptionModeModel) descriptionModeView.getModel();
                final ApiFilter apiFilter = getApiFilter();
                List<DescriptionMode> modes = model.getCurrentModes();
                apiFilter.setDescriptionModes(modes == null ? null : model.getCurrentModes().toArray(new DescriptionMode[0]));

                ApiBrowserTopComponent.this.updateEditor();

                updateToolTip();
            }
        }

        private void updateToolTip() {
            AccessModel model = (AccessModel) apiLevel.getView().getModel();
            apiLevel.setToolTipText(model.getToolTip());
            
            DescriptionModeModel descriptionModeModel = (DescriptionModeModel) descriptionMode.getView().getModel();
            descriptionMode.setToolTipText(descriptionModeModel.getToolTip());
        }
        
        public void open(RadixObject target){
            Layer layer = target.getLayer();
            if (layer != null){
                descriptionMode.setVisible(true);
                DescriptionModeView view  = (DescriptionModeView) descriptionMode.getView();
                view.open(layer);
                updateDescriptionMode();
            } else {
                descriptionMode.setVisible(false);
            }
        }
    }

    private IApiEditor<?> editor;
    private Action next, prev;
    private JScrollPane scrollPane = new JScrollPane();
    private ApiFilter apiFilter = new ApiFilter();
    final ApiFilterPanel filterPanel;

    private ApiBrowserTopComponent() {
        setLayout(new BorderLayout());

        final JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        final Action refrash = new AbstractAction("", RadixWareIcons.ARROW.CIRCLE.getIcon()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.update();
                updateToolbar();
            }
        };
        final JButton btnRefrash = toolBar.add(refrash);
        btnRefrash.setFocusable(false);
        btnRefrash.setToolTipText(Bundle.ApiBrowserTopComponent_Refresh());

        prev = new AbstractAction("", RadixWareDesignerIcon.ARROW.LEFT.getIcon()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                ApiEditorManager.find(ApiBrowserTopComponent.this).openPrev();
                updateToolbar();
            }
        };
        final JButton btnPrev = toolBar.add(prev);
        btnPrev.setFocusable(false);

        next = new AbstractAction("", RadixWareDesignerIcon.ARROW.RIGHT.getIcon()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                ApiEditorManager.find(ApiBrowserTopComponent.this).openNext();
                updateToolbar();
            }
        };
        final JButton btnNext = toolBar.add(next);
        btnNext.setFocusable(false);

        final Action select = new AbstractAction("", RadixWareDesignerIcon.TREE.SELECT_IN_TREE.getIcon()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectInTree();
            }
        };
        final JButton btnSelect = toolBar.add(select);
        btnSelect.setFocusable(false);
        btnSelect.setToolTipText(Bundle.ApiBrowserTopComponent_SelectInProjects());

        toolBar.addSeparator();

        filterPanel = new ApiFilterPanel();
        toolBar.add(filterPanel);

        add(toolBar, BorderLayout.PAGE_START);

        add(scrollPane, BorderLayout.CENTER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }

    @Override
    protected void componentOpened() {
    }

    @Override
    protected void componentActivated() {
        updateToolbar();
    }

    @Override
    protected void componentShowing() {
        updateEditor();
    }

    void updateEditor() {
        if (this.editor != null) {
            editor.update();
        }
    }

    private ApiFilter getApiFilter() {
        return apiFilter;
    }
    
    private void updateToolbar() {
        prev.setEnabled(ApiEditorManager.find(this).canPrev());
        next.setEnabled(ApiEditorManager.find(this).canNext());
    }

    public final void open(IApiEditor<?> editor, RadixObject target, EditorState state) {
        open();

        requestActive();

        if (this.editor == editor) {
            return;
        }

        this.editor = editor;
        scrollPane.setViewportView(editor.getComponent());
        editor.open(OpenMode.DEFAULT, getApiFilter());
        editor.setEditorState(state);
        editor.select(target);
        filterPanel.open(target);
        editor.setEditorState(state);
        updateActiveNode();

        revalidate();
        updateToolbar();
    }

    private void updateActiveNode() {
        final Node node = NodesManager.findOrCreateNode(editor.getSource());
        setActivatedNodes(new Node[]{node});
    }

    @Override
    public String getDisplayName() {
        return Bundle.ApiBrowserTopComponent_DisplayName();
    }

    private void selectInTree() {
        if (editor != null) {
            NodesManager.selectInProjects(editor.getSource());
        }
    }
}
