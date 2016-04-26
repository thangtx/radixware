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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.api.editors.OpenMode;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.components.GenericComboBox;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


@TopComponent.Description(
        preferredID = "ApiBrowserTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE",
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@NbBundle.Messages({
    "ApiBrowserTopComponent_DisplayName=API Browser",
    "ApiBrowserTopComponent_ApiFilterPanel=Access level",
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

    private final static class Model extends GenericComboBox.Model {

        List<EAccess> accessLevel = new ArrayList<>();
        boolean onlyPublished = true;

        public Model() {
            accessLevel.add(EAccess.PUBLIC);
            accessLevel.add(EAccess.PROTECTED);
        }

        @Override
        public String getName() {
            return Bundle.ApiBrowserTopComponent_ApiFilterPanel();
        }

        public String getToolTip() {
            StringBuilder sb = new StringBuilder();

            boolean first = true;

            for (EAccess access : accessLevel) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }

                sb.append(access.getName());
            }

            if (sb.length() > 0 && onlyPublished) {
                sb.append(", only published");
            }

            return sb.toString();
        }
    }

    private final static class View extends GenericComboBox.PopupView {

        private final JPanel panel = new JPanel();
        private final JCheckBox chbPublic = new JCheckBox("public");
        private final JCheckBox chbProtected = new JCheckBox("protected");
        private final JCheckBox chbDefault = new JCheckBox("default");
        private final JCheckBox chbPrivate = new JCheckBox("private");
        private final JCheckBox chbPublished = new JCheckBox("only published");
        private final ItemListener listener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if (inUpdate) {
                    return;
                }

                EAccess access = null;
                if (e.getSource() == chbPublic) {
                    access = EAccess.PUBLIC;
                } else if (e.getSource() == chbProtected) {
                    access = EAccess.PROTECTED;
                } else if (e.getSource() == chbDefault) {
                    access = EAccess.DEFAULT;
                } else if (e.getSource() == chbPrivate) {
                    access = EAccess.PRIVATE;
                } else if (e.getSource() == chbPublished) {
                    model.onlyPublished = chbPublished.isSelected();
                }

                if (access != null) {

                    if (access.isLess(EAccess.PROTECTED)) {
                        final boolean bool = chbPrivate.isSelected() || chbDefault.isSelected();

                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                if (bool) {
                                    chbPublished.setSelected(false);
                                }
                                chbPublished.setEnabled(!bool);
                            }
                        });
                    }
                    if (((JCheckBox) e.getItem()).isSelected()) {
                        model.accessLevel.add(access);
                    } else {
                        model.accessLevel.remove(access);
                    }
                }

                changes = true;
            }
        };
        private Model model;
        private boolean changes = false;
        private boolean inUpdate = false;

        public View() {
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            panel.add(chbPublished);
            panel.add(chbPublic);
            panel.add(chbProtected);
            panel.add(chbDefault);
            panel.add(chbPrivate);

            model = new Model();

            update();

            chbPublic.addItemListener(listener);
            chbProtected.addItemListener(listener);
            chbDefault.addItemListener(listener);
            chbPrivate.addItemListener(listener);
            chbPublished.addItemListener(listener);
        }

        @Override
        public final void update() {
            inUpdate = true;

            chbPublic.setSelected(model.accessLevel.contains(EAccess.PUBLIC));
            chbProtected.setSelected(model.accessLevel.contains(EAccess.PROTECTED));
            chbDefault.setSelected(model.accessLevel.contains(EAccess.DEFAULT));
            chbPrivate.setSelected(model.accessLevel.contains(EAccess.PRIVATE));
            chbPublished.setSelected(model.onlyPublished);

            changes = false;
            inUpdate = false;
        }

        @Override
        public JComponent getComponent() {
            return panel;
        }

        @Override
        public GenericComboBox.Model getModel() {
            return model;
        }

        boolean isChanged() {
            return changes;
        }
    }

    private class ApiFilterPanel extends JPanel {

        private final GenericComboBox apiLevel;
        public ApiFilterPanel() {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            final View view = new View();
            apiLevel = new GenericComboBox(view) {
                @Override
                public Dimension getMaximumSize() {
                    return super.getPreferredSize();
                }
            };

            apiLevel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    final Model model = (Model) view.getModel();

                    if (view.isChanged()) {
                        final ApiFilter apiFilter = getApiFilter();
                        apiFilter.setAccessLevel(model.accessLevel.toArray(new EAccess[0]));
                        apiFilter.setShowNotPublished(!model.onlyPublished);

                        ApiBrowserTopComponent.this.updateEditor();

                        updateToolTip();
                    }
                }
            });

            updateToolTip();

            add(apiLevel);
        }

        private void updateToolTip() {
            ApiBrowserTopComponent.Model model = (ApiBrowserTopComponent.Model) apiLevel.getView().getModel();
            apiLevel.setToolTipText(model.getToolTip());
        }
    }

    private IApiEditor<?> editor;
    private Action next, prev;
    private JScrollPane scrollPane = new JScrollPane();
    private ApiFilter apiFilter = new ApiFilter();

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

        final ApiFilterPanel filterPanel = new ApiFilterPanel();
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

    private void updateEditor() {
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
