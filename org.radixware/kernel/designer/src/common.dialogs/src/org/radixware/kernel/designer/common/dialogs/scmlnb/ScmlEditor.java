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
package org.radixware.kernel.designer.common.dialogs.scmlnb;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.DefaultFocusTraversalPolicy;
import java.awt.Point;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;
import net.miginfocom.swing.MigLayout;
import org.netbeans.api.editor.mimelookup.MimeLookup;
import org.netbeans.editor.Utilities;
import org.netbeans.editor.EditorUI;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.scml.Scml;

import org.netbeans.editor.GlyphGutter;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.scml.IScmlPosition;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane.VTag;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane.VTagFactory;
import org.radixware.kernel.designer.common.dialogs.scmlnb.finder.ScmlLocation;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.ScmlToolBarAction;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.utils.RadixNbEditorUtils;

/**
 * Wrapper for {@link ScmlEditorPane}
 *
 */
public class ScmlEditor<T extends Scml> extends JPanel implements Lookup.Provider {

    private static ScmlEditorRegistry REGISTRY = new ScmlEditorRegistry();

    private ScmlEditorPane currentPane;
    private final ScmlEditorPane mainPane;
    private ScmlEditorPane previewPane;
    private boolean isInPreview = false;
    private JPanel cardPanel;
    private JComponent currentWrapperComponent;
    private JComponent mainWrapperComponent;
    private JComponent previewWrapperComponent;
    private JToolBar toolbar;
    private ScmlToolBarAction[] toolbarActions;
    private int toolbarItemsCount = 0;
    public static final String SQML_MIME_TYPE = "text/x-sqml";
    public static final String JML_MIME_TYPE = "text/x-jml";
    public static final String DEFAULT_SCML_MIME_TYPE = "text/x-scml";
    private static final String EDITABLE_BEFORE_PREVIEW = "editable-before-preview";
    private final InstanceContent lookupContent = new InstanceContent();
    private Lookup lookup;
    private Definition context = null;

    public ScmlEditor(String mimeType) {

        if (MimeLookup.getLookup(mimeType).lookup(EditorKit.class) == null) {
            mimeType = DEFAULT_SCML_MIME_TYPE;
            if (JEditorPane.getEditorKitClassNameForContentType(DEFAULT_SCML_MIME_TYPE) == null) {
                JEditorPane.registerEditorKitForContentType(DEFAULT_SCML_MIME_TYPE, ScmlEditorKit.class.getName());
            }
        }

        this.setLayout(new BorderLayout());

        mainPane = createScmlEditorPane(mimeType);
        currentPane = mainPane;

        EditorUI mainEUI = Utilities.getEditorUI(mainPane);
        if (mainEUI != null) {
            mainWrapperComponent = mainEUI.getExtComponent();
            currentWrapperComponent = mainWrapperComponent;

            cardPanel = new JPanel(new BorderLayout());
            cardPanel.add(mainWrapperComponent, BorderLayout.CENTER);

            previewPane = createScmlEditorPane(mimeType);
            EditorUI previewEUI = Utilities.getEditorUI(previewPane);
            previewWrapperComponent = previewEUI.getExtComponent();

            this.add(cardPanel, BorderLayout.CENTER);
            toolbar = mainEUI.getToolBarComponent();
            this.add(toolbar, BorderLayout.NORTH);

            addToolbarItems();
        } else {//workaround for #RADIX-3923:
            //When mainEUI is null (normal at design time), this JPanel is empty.
            //Netbeans Form editor automatically sets GroupLayout to every empty JPanel
            //added to the form, thus breaking behaviour of the ScmlEditor.
            //As a workaround we can add filler component to prevent netbeans from
            //modifing ScmlEditor.
            setLayout(new MigLayout("fill"));
            add(new JLabel("Scml Editor"), "align center");
        }

        setFocusTraversalPolicy(new DefaultFocusTraversalPolicy() {
            @Override
            public Component getDefaultComponent(Container aContainer) {
                return currentPane;
            }
        });
        setFocusTraversalPolicyProvider(true);
        setFocusCycleRoot(true);

        //Adding this component to GroupLayout() with null Preferred size can cause NullPointer exception,
        //so we must set some initial value.
        setPreferredSize(getMinimumSize());
    }

    protected final ScmlEditorPane createScmlEditorPane(String mimeType) {
        return new ScmlEditorPane(mimeType, createVTagFactory(), createTagEditorFactory(), createTagPopupFactory());
    }

    protected void reloadToolbarActions() {
        removeToolbarItems();
        addToolbarItems();
        toolbar.revalidate();//#RADIX-4453
//        addDebugActions();
    }

    private void removeToolbarItems() {
        for (; toolbarItemsCount > 0; toolbarItemsCount--) {
            if (toolbar.getComponentCount() > 0) {
                toolbar.remove(0);
            }
        }
    }

    private void addToolbarItems() {
        toolbarItemsCount = 0;
        toolbarActions = createScmlToolBarActions();
        if (toolbarActions != null) {
            ScmlToolBarAction prevAction = null;
            Arrays.sort(toolbarActions, new ActionsComparator());
            for (ScmlToolBarAction action : toolbarActions) {
                if (action.isAvailable(getSource())) {
                    action.updateState();
                    if (prevAction != null && prevAction.getGroupType() != action.getGroupType()) {
                        JToolBar.Separator separator = new JToolBar.Separator();
                        toolbar.add(separator, toolbarItemsCount++);
                    }
                    Component c = action.getToolbarPresenter();
                    if (c instanceof AbstractButton) {
                        RadixNbEditorUtils.processToolbarButton((AbstractButton) c);
                    }
                    toolbar.add(c, toolbarItemsCount++);
                    if (action.getValue(Action.ACCELERATOR_KEY) instanceof String) {
                        KeyStroke ks = KeyStroke.getKeyStroke((String) action.getValue(Action.ACCELERATOR_KEY));
                        getPane().getInputMap().put(ks, action.getValue(Action.NAME));
                        getPane().getActionMap().put(action.getValue(Action.NAME), action);
                    }
                    prevAction = action;
                }
            }
            JToolBar.Separator separator = new JToolBar.Separator();
            toolbar.add(separator, toolbarItemsCount++);
        }
    }

    @Override
    public Lookup getLookup() {
        Lookup parentLookup = getParentLookup();
        if (lookup == null) {
            lookup = new AbstractLookup(lookupContent);
            Collection lookupObjects = getObjectsForLookup();
            if (lookupObjects != null) {
                for (Object lookupObject : lookupObjects) {
                    lookupContent.add(lookupObject);
                }
            }
            lookupContent.remove(this);
            lookupContent.add(this);
            lookupContent.remove(mainPane);
            lookupContent.add(mainPane);
            lookupContent.add(currentPane.getActionMap());
        }
        if (parentLookup != null) {
            return new ProxyLookup(lookup, Lookups.exclude(parentLookup, ActionMap.class));
        }
        return lookup;
    }

    protected Collection<Object> getObjectsForLookup() {
        return null;
    }

    private Lookup getParentLookup() {
        Component parent = this.getParent();
        while (parent != null) {
            if (parent instanceof Lookup.Provider) {
                return ((Lookup.Provider) parent).getLookup();
            }
            parent = parent.getParent();
        }
        return null;
    }

    private static class ActionsComparator implements Comparator<ScmlToolBarAction> {

        @Override
        public int compare(ScmlToolBarAction o1, ScmlToolBarAction o2) {
            if (o1 == null) {
                if (o2 != null) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                if (o1.getGroupType() == o2.getGroupType()) {
                    return o1.getPosition() - o2.getPosition();
                }
                return o1.getGroupType() - o2.getGroupType();
            }
        }
    }

    private void addDebugActions() {
        JButton bt = new JButton("");
        bt.setIcon(RadixWareIcons.SECURITY.KEY.getIcon());
        bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final List<Definition> defs = new ArrayList<Definition>();
                final Branch branch = RadixFileUtil.getOpenedBranches().iterator().next();
                branch.visitAll(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                        if (radixObject instanceof DdsIndexDef) {
                            if (((DdsIndexDef) radixObject).isSecondaryKey()) {
                                defs.add((Definition) radixObject);
                            }
                        }
                    }
                });

                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(defs);
                ChooseDefinition.chooseDefinition(cfg);

            }
        });
        toolbar.add(bt, 0);
    }

    protected ScmlToolBarAction[] createScmlToolBarActions() {
        return new ScmlToolBarAction[0];
    }

    public JToolBar getToolBar() {
        return toolbar;
    }

    public void setEditable(boolean editable) {
        currentPane.setEditable(editable);
        updateToolbarActionsState();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (Component comp : getToolBar().getComponents()) {
            comp.setEnabled(enabled);
        }
        currentWrapperComponent.setEnabled(enabled);
        setChildrenComponentsEnabled(currentWrapperComponent, enabled);
        currentPane.setEnabled(enabled);
    }

    private void setChildrenComponentsEnabled(Container container, boolean enabled) {
        for (Component comp : container.getComponents()) {
            //even if GlyphGutter is disabled, it still recieves mouse events
            //and creates popup menu on right-click
            if (comp instanceof GlyphGutter) {
                comp.setVisible(enabled);
            } else if (comp instanceof Container) {
                setChildrenComponentsEnabled((Container) comp, enabled);
            }
            comp.setEnabled(enabled);
        }
    }

    public ScmlEditorPane getPane() {
        return currentPane;
    }

    public synchronized boolean setPreviewMode(boolean preview) {
        boolean hasFocus = currentPane.isFocusOwner();
        if (preview) {
            String previewContent = mainPane.getScmlDocument().getPreviewContent();
            if (previewContent == null) {
                return false;
            }
            previewPane.setText(previewContent);
            int line = getLineAtCursor(mainPane);
            setCursorAtLineStart(previewPane, line);

            cardPanel.removeAll();
            cardPanel.add(previewWrapperComponent, BorderLayout.CENTER);

            boolean editable = mainPane.isEditable();
            mainPane.putClientProperty(EDITABLE_BEFORE_PREVIEW, editable);
            mainPane.setEditable(false);

            currentPane = previewPane;
        } else {
            cardPanel.removeAll();
            cardPanel.add(mainWrapperComponent, BorderLayout.CENTER);

            Object editable = mainPane.getClientProperty(EDITABLE_BEFORE_PREVIEW);
            if (editable instanceof Boolean) {
                mainPane.setEditable((Boolean) editable);
            }

            currentPane = mainPane;
        }
        if (hasFocus) {
            currentPane.requestFocusInWindow();
        }
        validate();
        repaint();
        currentPane.update();
        updateToolbarActionsState();
        isInPreview = preview;
        return true;
    }

    private int getLineAtCursor(ScmlEditorPane pane) {
        int ret = 0;
        try {
            ret = Utilities.getLineOffset(pane.getScmlDocument(), pane.getCaretPosition());
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }

    private void setCursorAtLineStart(ScmlEditorPane pane, int line) {
        int offset = Utilities.getRowStartFromLineOffset(pane.getScmlDocument(), line);
        if (offset == -1) {
            offset = 0;
        }
        pane.setCaretPosition(offset);
    }

    public void open(T source, OpenInfo openInfo) {
        mainPane.setScml(source);
        if (openInfo != null) {
            //1) ScmlPosition
            IScmlPosition scmlPosition = openInfo.getLookup().lookup(IScmlPosition.class);
            if (scmlPosition != null) {
                mainPane.setCursorAtScmlPosition(scmlPosition);
                //2) Tag
            } else if (openInfo.getTarget() instanceof Scml.Tag) {
                mainPane.setCursorAtScmlPosition(new FixedScmlPosition((Scml.Tag) openInfo.getTarget(), 0));
            } else {
                //3) RadixProblem
                RadixProblem problem = openInfo.getLookup().lookup(RadixProblem.class);
                if (problem != null) {
                    int problemOffset = getPane().getScmlDocument().getProblemOffset(problem);
                    if (problemOffset >= 0 && problemOffset <= getPane().getDocument().getLength()) {
                        mainPane.setCaretPosition(problemOffset);
                    }
                    //4) ScmlLocation
                } else {
                    final ScmlLocation location = openInfo.getLookup().lookup(ScmlLocation.class);
                    if (location != null) {
                        int offset = mainPane.getScmlDocument().scmlOffsetToTextOffset(location.getScmlOffset());
                        offset += location.getAdditionalOffset();
                        if (offset >= 0 && offset <= getPane().getDocument().getLength()) {
                            mainPane.setCaretPosition(offset);
                        }
                    }
                }
            }
        }
        reloadToolbarActions();
        setEditable(!source.isReadOnly());
        REGISTRY.afterOpen(this);
    }

    public T getSource() {
        return (T) currentPane.getScml();
    }

    public void setContext(Definition context) {
        this.context = context;
    }

    public Definition getContext() {
        return context;
    }

    public void update() {
        currentPane.update();
        updateToolbarActionsState();
    }

    private void updateToolbarActionsState() {
        if (toolbarActions != null) {
            for (ScmlToolBarAction action : toolbarActions) {
                action.updateState();
            }
        }
    }

    protected VTagFactory createVTagFactory() {
        return new VTagFactory() {
            @Override
            public VTag createVTag(Tag tag) {
                return new ScmlEditorPane.ErrorVTag(tag);
            }
        };
    }

    protected ScmlEditorPane.TagEditorFactory createTagEditorFactory() {
        return null;
    }

    protected ScmlEditorPane.TagPopupFactory createTagPopupFactory() {
        return new ScmlTagPopupFactory();
    }

    protected static class ScmlTagPopupFactory implements ScmlEditorPane.TagPopupFactory {

        @Override
        public ScmlEditorPane.TagPopup createTagPopup(ScmlEditorPane editor) {
            return new ScmlTagPopup(editor);
        }
    }

    protected static class ScmlTagPopup extends ScmlEditorPane.TagPopup {

        private Scml.Tag tag;

        public ScmlTagPopup(ScmlEditorPane editor) {
            super(editor);
        }

        @Override
        public boolean open(Scml.Tag tag) {
            this.tag = tag;
            return true;
        }

        @Override
        public void show(Component c, Point popupLocation) {
            try {
                editor.putClientProperty(PopupActionCodeGeneratorFactory.RADIX_OBJECT_FOR_TAG_REPLACEMENT, tag);
                editor.putClientProperty(ScmlEditorPane.DISABLE_STANDART_GENERATORS, Boolean.TRUE);

                Action generateAction = editor.getActionMap().get("generate-code");

                if (generateAction != null) {
                    generateAction.actionPerformed(new ActionEvent(editor, -1, null));
                }

            } finally {
                editor.putClientProperty(PopupActionCodeGeneratorFactory.RADIX_OBJECT_FOR_TAG_REPLACEMENT, null);
                editor.putClientProperty(ScmlEditorPane.DISABLE_STANDART_GENERATORS, Boolean.FALSE);

            }
        }
    }

    public static ScmlEditor findScmlEditor(final Scml scml) {
        return REGISTRY.find(scml);
    }

    private static class ScmlEditorRegistry {

        private final Object NULL_KEY = new Object();
        private final Map<ScmlEditor, Object> registry = new WeakHashMap<>();

        public synchronized void afterOpen(ScmlEditor editor) {
            registry.put(editor, NULL_KEY);
        }

        public synchronized ScmlEditor find(Scml scml) {
            for (ScmlEditor editor : registry.keySet()) {
                if (editor.getSource() == scml) {
                    return editor;
                }
            }
            return null;
        }
    }
}
