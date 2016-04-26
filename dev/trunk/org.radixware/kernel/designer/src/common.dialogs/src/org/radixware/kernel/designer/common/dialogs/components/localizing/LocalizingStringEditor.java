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

package org.radixware.kernel.designer.common.dialogs.components.localizing;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;
import org.netbeans.editor.BaseDocument;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.components.BorderedCollapsablePanel;
import org.radixware.kernel.designer.common.dialogs.components.CollapsablePanel;
import org.radixware.kernel.designer.common.dialogs.components.TabManager;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingStringEditor.LocalizingEditorComponent;
import org.radixware.kernel.designer.common.dialogs.components.values.EventsSupport;
import org.radixware.kernel.designer.common.dialogs.spellchecker.Spellchecker;
import org.radixware.kernel.designer.common.dialogs.spellchecker.Spellchecker.SpellcheckControl;
import org.radixware.kernel.designer.common.dialogs.utils.TextComponentUtils;


public class LocalizingStringEditor extends JPanel {

    public enum EEditorMode {

        LINE, MULTILINE, EXPANDABLE, USER;
    }

    protected static abstract class LocalizingEditorComponent {
        
        static JToggleButton createIgnoreSpellCheckButton() {
            final JToggleButton ignoreSpellCheck = new JToggleButton() {
                @Override
                public Icon getIcon() {
                    return isSelected() ? RadixWareIcons.CHECK.STOP.getIcon()
                            : RadixWareIcons.CHECK.CHECK.getIcon();
                }
            };
            ignoreSpellCheck.setToolTipText(NbBundle.getMessage(LocalizingStringEditor.class,
                            ignoreSpellCheck.isSelected() ? "IgnoreSpellCheckBoxDisabled.tooltip" : "IgnoreSpellCheckBoxDisabled.tooltip"));
            ignoreSpellCheck.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ignoreSpellCheck.setToolTipText(NbBundle.getMessage(LocalizingStringEditor.class,
                            ignoreSpellCheck.isSelected() ? "IgnoreSpellCheckBoxDisabled.tooltip" : "IgnoreSpellCheckBoxDisabled.tooltip"));
                }
            });
            ignoreSpellCheck.setFocusable(false);
            return ignoreSpellCheck;
        }
        final Map<EIsoLanguage, ILocalizedEditor> editors = new EnumMap<>(EIsoLanguage.class);
        final LocalizingStringEditor localizingStringEditor;
        private boolean inUpdate;
        private boolean isReadonly;
        private final ChangeListener changeValueListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!inUpdate()) {
                    final ILocalizedEditor valueField = (ILocalizedEditor) e.getSource();
                    localizingStringEditor.getLocalizingStringContext().setValue(valueField.getLanguage(), valueField.getText());
                    localizingStringEditor.fireChange();
                }
            }
        };

        LocalizingEditorComponent(LocalizingStringEditor editor) {
            this.localizingStringEditor = editor;
        }

        final void setEditor(ILocalizedEditor localizedEditor, EIsoLanguage language) {
            registrSpellcheck(getTextComponent(localizedEditor), language, localizingStringEditor.getLocalizingStringContext());
            editors.put(language, localizedEditor);
        }

        final void enableSpellcheck(boolean enable) {
            if (localizingStringEditor.getLocalizingStringContext() != null) {
                localizingStringEditor.getLocalizingStringContext().enableSpellcheck(enable);

                for (final ILocalizedEditor editor : editors.values()) {
                    final JTextComponent textComponent = getTextComponent(editor);
                    if (textComponent != null) {
                        SpellcheckControl spellcheckControl = (SpellcheckControl) textComponent.getClientProperty(SpellcheckControl.class);

                        if (spellcheckControl != null) {
                            spellcheckControl.update();
                        }
                    }
                }
                localizingStringEditor.fireChange();
            }
        }

        public void setReadonly(boolean readonly) {
            this.isReadonly = readonly;
            for (final ILocalizedEditor c : editors.values()) {
                c.setReadonly(readonly);
            }
        }

        void connect() {
            for (final ILocalizedEditor c : editors.values()) {
                c.addChangeListener(changeValueListener);
            }
        }

        public final void update() {
            enterUpdate();

            updateImpl();

            exitUpdate();
        }

        void updateImpl() {
            for (final EIsoLanguage lang : editors.keySet()) {
                editors.get(lang).setText(getValueFromString(lang));
            }
        }

        private void enterUpdate() {
            inUpdate = true;
        }

        private void exitUpdate() {
            inUpdate = false;
        }

        final boolean inUpdate() {
            return inUpdate;
        }

        final String getValueFromString(EIsoLanguage lang) {
            return localizingStringEditor.getLocalizingStringContext().getValue(lang);
        }

        public boolean isReadonly() {
            return isReadonly;
        }

        public abstract JPanel getComponent();
    }

    private static final class LineEditor extends LocalizingEditorComponent {

        private final String ROW_NUMBER = "row-number";
        private boolean isExpandable;
        private final JToggleButton ignoreSpellCheck;
        private final ActionListener ignoreSpellCheckListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableSpellcheck(!ignoreSpellCheck.isSelected());
            }
        };
        private final JPanel panel = new JPanel();

        public LineEditor(final LocalizingStringEditor editor) {
            super(editor);
            ignoreSpellCheck = createIgnoreSpellCheckButton();
            build();
        }

        @Override
        public void setReadonly(boolean readonly) {
            super.setReadonly(readonly);
            ignoreSpellCheck.setEnabled(!readonly);
        }

        private void build() {
            editors.clear();

            panel.setLayout(new GridBagLayout());

            final List<EIsoLanguage> curLanguages = localizingStringEditor.getLanguages();
            final Definition context = localizingStringEditor.getLocalizingStringContext().getAdsDefinition();

            GridBagConstraints constraints = new GridBagConstraints();

            int totalLanguages = 0;
            for (final EIsoLanguage lang : curLanguages) {

                final ILocalizedEditor localizedEditor = isExpandable
                        ? new ExpandableLanguageTextField(lang, context)
                        : new LocalizedTextField(lang);

                setEditor(localizedEditor, lang);

                final JLabel label = new JLabel(lang.getName() + ":");
                constraints.weightx = 0.0;
                constraints.weighty = 0.0;
                constraints.gridx = 0;
                constraints.gridy = totalLanguages++;
                constraints.gridwidth = 1;
                constraints.fill = GridBagConstraints.NONE;
                constraints.anchor = GridBagConstraints.WEST;
                constraints.insets = new Insets(0, 0, 4, 4);
                panel.add(label, constraints);

                constraints.weightx = 1.0;
                constraints.gridx = 1;
                constraints.fill = GridBagConstraints.HORIZONTAL;
                constraints.anchor = GridBagConstraints.CENTER;
                assert (localizedEditor instanceof JComponent);

                ((JComponent) localizedEditor).putClientProperty(ROW_NUMBER, totalLanguages);
                panel.add((JComponent) localizedEditor, constraints);

                final JLabel iconInfo = new JLabel(RadixWareDesignerIcon.WORKFLOW.PALETTE_INFORMATION.getIcon());

//                final EIsoLanguage currLang = lang;
                iconInfo.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        final ILocalizedStringInfo stringInfo = localizingStringEditor.getLocalizingStringContext().getStringInfo();
                        if (stringInfo != null) {
                            iconInfo.setToolTipText(stringInfo.asHtml(lang));
                        }
                    }
                });

                constraints.weightx = 0;
                constraints.gridx = 2;
                constraints.fill = GridBagConstraints.NONE;
                constraints.anchor = GridBagConstraints.CENTER;
                panel.add(iconInfo, constraints);


                constraints.gridy = totalLanguages++;
            }
            constraints.gridx = 3;
            constraints.gridy = 0;
            constraints.fill = GridBagConstraints.NONE;
            constraints.weightx = 0.0;
            constraints.gridheight = 2;
            constraints.anchor = GridBagConstraints.PAGE_START;
            constraints.insets = new Insets(0, 0, 0, 0);

            panel.add(ignoreSpellCheck, constraints);
            panel.setFocusCycleRoot(true);
            panel.revalidate();
        }

        @Override
        public JPanel getComponent() {
            return panel;
        }

        @Override
        void updateImpl() {
            super.updateImpl();
            ignoreSpellCheck.setSelected(!localizingStringEditor.getLocalizingStringContext().isSpellcheckEnable());
        }

        @Override
        void connect() {
            super.connect();
            ignoreSpellCheck.addActionListener(ignoreSpellCheckListener);
        }
    }

    private static final class AreaEditor extends LocalizingEditorComponent {

        private class Policy extends FocusTraversalPolicy {

            private final TabManager tabManager;

            public Policy(TabManager tabManager) {
                this.tabManager = tabManager;
            }

            @Override
            public Component getComponentAfter(Container container, Component component) {
                final EditorTab tab = (EditorTab) tabManager.getNextTab(true);
                return move(tab.getLanguage());
            }

            @Override
            public Component getComponentBefore(Container container, Component component) {
                final EditorTab tab = (EditorTab) tabManager.getPrevTab(true);
                return move(tab.getLanguage());
            }

            @Override
            public Component getFirstComponent(Container container) {
                final EditorTab tab = (EditorTab) tabManager.getTab(0);
                return move(tab.getLanguage());
            }

            @Override
            public Component getLastComponent(Container container) {
                if (tabManager.getTabCount() > 0) {
                    final EditorTab tab = (EditorTab) tabManager.getTab(tabManager.getTabCount() - 1);
                    return move(tab.getLanguage());
                }
                return container;
            }

            @Override
            public Component getDefaultComponent(Container container) {
                final EditorTab tab = (EditorTab) tabManager.getSelectedTab();
                return (Component) editors.get(tab.getLanguage());
            }

            private Component move(EIsoLanguage lang) {
                tabManager.getTabbedPane().setFocusTraversalPolicyProvider(false);
                tabManager.setSelectedTab(lang.getValue());
//                tabManager.getTabbedPane().updateUI();
                tabManager.getTabbedPane().setFocusTraversalPolicyProvider(true);

                return (Component) editors.get(lang);
            }
        }

        private class EditorTab extends TabManager.TabAdapter {

            private final EditorPanel tab;

            public EditorTab(EIsoLanguage language) {
                tab = new EditorPanel(language);
            }

            @Override
            public String getTabName() {
                return tab.getLanguage().getName();
            }

            @Override
            public final JComponent getTabComponent() {
                return tab;
            }

            @Override
            public String getTabKey() {
                return tab.getLanguage().getValue();
            }

            public EIsoLanguage getLanguage() {
                return tab.getLanguage();
            }

            @Override
            protected void opened() {
                ((JComponent) tab.getLocalizedEditor()).requestFocusInWindow();
            }
        }

        private class EditorPanel extends JScrollPane {

            private final ILocalizedEditor localizedEditor;
            private final EIsoLanguage language;

            EditorPanel(EIsoLanguage language) {
//                super(new BorderLayout());
                this.language = language;

                final int rowCount = Options.tryCast(Integer.class, localizingStringEditor.getOptions().get("row-count"), 6);
                localizedEditor = new LocalizedTextArea(language, "", rowCount);

                setEditor(localizedEditor, language);
//                add(new JScrollPane((JComponent) localizedEditor), BorderLayout.CENTER);

                setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

                setViewportView((JComponent) localizedEditor);
            }

            ILocalizedEditor getLocalizedEditor() {
                return localizedEditor;
            }

            EIsoLanguage getLanguage() {
                return language;
            }

            @Override
            public Dimension getPreferredSize() {
                if (localizedEditor != null) {
                    final Dimension size = ((LocalizedTextArea) localizedEditor).calcPreferredSize();
                    final Insets insets = getInsets();

                    size.width = 100;

                    if (insets != null) {
//                        size.width += insets.left + insets.right;
                        size.height += insets.bottom + insets.top;
                    }
                    return size;
                } else {
                    return super.getPreferredSize();
                }
            }
        }
        private JPanel panel;
        private final JToggleButton ignoreSpellCheck;
        private final ActionListener ignoreSpellCheckListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableSpellcheck(!ignoreSpellCheck.isSelected());
            }
        };
        private TabManager tabManager;

        public AreaEditor(final LocalizingStringEditor editor) {
            super(editor);

            ignoreSpellCheck = createIgnoreSpellCheckButton();

            build();
        }

        @Override
        public void setReadonly(boolean readonly) {
            super.setReadonly(readonly);
            ignoreSpellCheck.setEnabled(!readonly);
        }

        private void build() {
            panel = new JPanel();
            panel.setLayout(new GridBagLayout());

            editors.clear();

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridheight = 2;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 1;
            constraints.weighty = 1;

            final List<EIsoLanguage> langs = localizingStringEditor.getLanguages();

            if (langs.size() == 1) {
                panel.add(new EditorPanel(langs.get(0)), constraints);
            } else {


                tabManager = new TabManager(new JTabbedPane());
                if (!langs.isEmpty()) {
                    final EIsoLanguage lang = langs.get(0);
                    for (final EIsoLanguage language : langs) {
                        tabManager.addTab(new EditorTab(language));
                    }

                    tabManager.getTabbedPane().setTabPlacement(JTabbedPane.LEFT);
                    tabManager.setSelectedTab(lang.getValue());

                    tabManager.getTabbedPane().setFocusTraversalPolicy(new Policy(tabManager));
                    tabManager.getTabbedPane().setFocusTraversalPolicyProvider(true);

                    panel.addAncestorListener(new AncestorListener() {
                        @Override
                        public void ancestorAdded(AncestorEvent event) {
                            final EditorTab tab = (EditorTab) tabManager.getSelectedTab();

                            if (tab != null) {
                                final ILocalizedEditor editor = editors.get(tab.getLanguage());
                                if (editor instanceof Component) {
                                    ((Component) editor).requestFocusInWindow();
                                }
                            }
                            panel.removeAncestorListener(this);
                        }

                        @Override
                        public void ancestorRemoved(AncestorEvent event) {
                        }

                        @Override
                        public void ancestorMoved(AncestorEvent event) {
                        }
                    });
                }
                panel.add(tabManager.getTabbedPane(), constraints);
            }
            constraints = new GridBagConstraints();
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.fill = GridBagConstraints.NONE;
            constraints.insets = new Insets(0, 4, 4, 0);
            constraints.anchor = GridBagConstraints.PAGE_START;

            ignoreSpellCheck.addActionListener(ignoreSpellCheckListener);
            ignoreSpellCheck.setSelected(!localizingStringEditor.getLocalizingStringContext().isSpellcheckEnable());
            panel.add(ignoreSpellCheck, constraints);

            constraints = new GridBagConstraints();
            constraints.gridx = 1;
            constraints.gridy = 1;
            constraints.fill = GridBagConstraints.NONE;
            constraints.insets = new Insets(0, 4, 4, 0);
            constraints.anchor = GridBagConstraints.PAGE_END;

            final JLabel iconInfo = new JLabel(RadixWareDesignerIcon.WORKFLOW.PALETTE_INFORMATION.getIcon());
            iconInfo.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    final ILocalizedStringInfo stringInfo = localizingStringEditor.getLocalizingStringContext().getStringInfo();
                    if (stringInfo != null) {
                        iconInfo.setToolTipText(stringInfo.asHtml(getCurrentEditorLang()));
                    }
                }
            });
            panel.add(iconInfo, constraints);
        }

        @Override
        public JPanel getComponent() {
            return panel;
        }

        @Override
        void updateImpl() {
            super.updateImpl();
            ignoreSpellCheck.setSelected(!localizingStringEditor.getLocalizingStringContext().isSpellcheckEnable());
        }

        @Override
        void connect() {
            super.connect();
            ignoreSpellCheck.addActionListener(ignoreSpellCheckListener);
        }

        private EIsoLanguage getCurrentEditorLang() {
            if (tabManager != null) {
                return ((EditorTab) tabManager.getSelectedTab()).getLanguage();
            }
            final List<EIsoLanguage> languages = localizingStringEditor.getLanguages();
            if (languages != null && !languages.isEmpty()) {
                return languages.get(0);
            }

            return EIsoLanguage.ENGLISH;
        }
    }

    private static final class CollapsableWrap extends LocalizingEditorComponent {

        private CollapsableEditor collapsablePanel;
        private final LocalizingEditorComponent source;

        public CollapsableWrap(LocalizingStringEditor editor, LocalizingEditorComponent source) {
            super(editor);
            this.source = source;

            build(source, editor.getOptions());
        }

        private void build(final LocalizingEditorComponent source, Options options) {
            final JPanel editor = source.getComponent();
            editor.setBorder(new EmptyBorder(4, 10, 10, 10));

            collapsablePanel = new CollapsableEditor(editor, options.getStr(Options.TITLE_KEY), false);
        }

        @Override
        public CollapsablePanel getComponent() {
            return collapsablePanel;
        }

        @Override
        public void setReadonly(boolean readonly) {
            super.setReadonly(readonly);
            source.setReadonly(readonly);
            collapsablePanel.setReadonly(readonly);
        }

        @Override
        void connect() {
            source.connect();

            collapsablePanel.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (!inUpdate()) {
                        if (collapsablePanel.isExpanded()) {
                            localizingStringEditor.getLocalizingStringContext().create();
                        } else {
                            localizingStringEditor.getLocalizingStringContext().remove();
                        }
                        source.update();
                        localizingStringEditor.fireChange();
                    }
                }
            });
        }

        @Override
        void updateImpl() {
            source.updateImpl();
            collapsablePanel.expand(localizingStringEditor.getLocalizingStringContext().hasValue());
        }
    }

    private static class CollapsableEditor extends BorderedCollapsablePanel {

        public CollapsableEditor(JPanel panel, String title, boolean expand) {
            super(panel, title, expand);
        }

//        @Override
//        public void collapse() {
//            if (!getHandleInfoAdapter().isValid()) {
//
//                final JLabel label = new JLabel(NbBundle.getMessage(LocalizingEditorPanel.class, "UnknownTitleError") + "handleInfo.getTitleId().toString()");
//                label.setForeground(Color.RED);
//
//                final JPanel panel = new JPanel(new BorderLayout());
//                panel.add(label, BorderLayout.CENTER);
//
//                expand(panel, false);
//            } else {
//                super.collapse();
//            }
//        }
        public void setReadonly(boolean readonly) {
            freeze(readonly);
        }
    }

    public final static class Options {

        public final static String TITLE_KEY = "title";
        public final static String MODE_KEY = "mode";
        public final static String COLLAPSABLE_KEY = "collapsable";

        public static <T> T tryCast(Class<T> cls, Object value, T defaultValue) {
            if (cls != null) {
                if (cls.isInstance(value)) {
                    return (T) value;
                }
            }
            return defaultValue;
        }
        private final Map<String, Object> options = new HashMap<>();

        public int size() {
            return options.size();
        }

        public Options add(String key, Object value) {
            options.put(key, value);
            return this;
        }

        public Set<String> keySet() {
            return options.keySet();
        }

        public Object get(String key) {
            return options.get(key);
        }

        public Object get(String key, Object defaultValue) {
            final Object value = get(key);
            return value != null ? value : defaultValue;
        }

        public boolean containsKey(String key) {
            return options.containsKey(key);
        }

        public boolean isTrue(String key) {
            final Object value = get(key);
            return value instanceof Boolean ? (Boolean) value : false;
        }

        public String getStr(String key) {
            final Object value = get(key);
            return value != null ? value.toString() : "";
        }
    }

    public static class Factory {

        public static LocalizingStringEditor createEditor(Options options) {
            return new LocalizingStringEditor(options != null ? options : createDefaultOptions(EEditorMode.LINE));
        }

        public static LocalizingStringEditor createLineEditor(Options options) {
            return createEditor(options != null
                    ? options.add(Options.MODE_KEY, EEditorMode.LINE)
                    : createDefaultOptions(EEditorMode.LINE));
        }

        public static LocalizingStringEditor createAreaEditor(Options options) {
            return createEditor(options != null
                    ? options.add(Options.MODE_KEY, EEditorMode.MULTILINE)
                    : createDefaultOptions(EEditorMode.MULTILINE));
        }

        private static Options createDefaultOptions(EEditorMode mode) {
            return new Options().add(Options.COLLAPSABLE_KEY, false).add(Options.TITLE_KEY, "Title").add(Options.MODE_KEY, mode);
        }
    }

    private static void registrSpellcheck(final JTextComponent editor, final EIsoLanguage language, final ILocalizingStringContext adapter) {
        if (editor != null) {
            final Spellchecker.SpellcheckControl spellcheckControl = new SpellcheckControl() {
                @Override
                public boolean isSpellcheckEnabled() {
                    return adapter.isSpellcheckEnable();
                }

                @Override
                public JTextComponent getTextComponent() {
                    return editor;
                }

                @Override
                public EIsoLanguage getLanguage() {
                    return language;
                }

                @Override
                public RadixObject getContext() {
                    return adapter.getAdsDefinition();
                }
            };

            Spellchecker.register(spellcheckControl);
            editor.putClientProperty(Spellchecker.SpellcheckControl.class, spellcheckControl);
        }
    }

    private static JTextComponent getTextComponent(ILocalizedEditor editor) {
        if (editor instanceof JTextComponent) {
            return (JTextComponent) editor;
        } else if (editor instanceof ExpandableLanguageTextField) {
            return ((ExpandableLanguageTextField) editor).getAsTextComponent();
        }

        return null;
    }

    static void installUndoRedoAction(JTextComponent tc) {
        TextComponentUtils.installUndoRedoAction(tc);
        final UndoManager manager = (UndoManager) tc.getDocument().getProperty(BaseDocument.UNDO_MANAGER_PROP);
        if (manager != null) {
            tc.addPropertyChangeListener(ILocalizedEditor.CONTENT, new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    manager.discardAllEdits();
                }
            });
        }
    }
    private ILocalizingStringContext localizingStringContext;
    private final Options options;
    private LocalizingEditorComponent editorComponent;
    private final EventsSupport<ChangeListener, ChangeEvent> eventsSupport = new EventsSupport<ChangeListener, ChangeEvent>() {
        @Override
        protected void performEvent(ChangeEvent event, ChangeListener listener) {
            listener.stateChanged(event);
        }
    };

    protected LocalizingStringEditor(Options options) {
        setLayoutInternal(new BorderLayout());
        this.options = options;
    }

    protected final void setLayoutInternal(LayoutManager mgr) {
        super.setLayout(mgr);
    }

    protected void installComponent(LocalizingEditorComponent editorComponent) {
        removeAll();

        add(editorComponent.getComponent(), BorderLayout.CENTER);

        editorComponent.update();
        editorComponent.connect();
    }

    protected LocalizingEditorComponent createComponent() {
        LocalizingEditorComponent editor = null;
        if (options != null && options.containsKey(Options.MODE_KEY)) {
            switch ((EEditorMode) options.get(Options.MODE_KEY)) {
                case MULTILINE:
                    editor = new AreaEditor(this);
                    break;
                case LINE:
                default:
                    editor = new LineEditor(this);
                    break;
            }
        }

        if (options.isTrue(Options.COLLAPSABLE_KEY)) {
            return new CollapsableWrap(this, editor);
        }
        return editor;
    }

    public final ILocalizingStringContext getLocalizingStringContext() {
        return localizingStringContext;
    }

    protected final LocalizingEditorComponent getEditor() {
        return editorComponent;
    }

    protected final void fireChange() {
        eventsSupport.fireChange(new ChangeEvent(this));
    }

    /**
     *
     * @deprecated useless, has no effect
     */
    @Override
    @Deprecated
    public final void setLayout(LayoutManager mgr) {
    }

    public final void open(ILocalizingStringContext localizingStringContext) {
        assert localizingStringContext != null;

        if (localizingStringContext != null) {
            this.localizingStringContext = localizingStringContext;
            editorComponent = createComponent();
            installComponent(editorComponent);

            final Definition definition = localizingStringContext.getAdsDefinition();
            setReadonly(definition == null ? true : definition.isReadOnly());
        }
    }

    public void open(HandleInfo handleInfo) {
        assert handleInfo != null;
        if (handleInfo != null) {
            open(LocalizingStringContextFactory.newInstance(handleInfo));
        }
    }

    public final Map<EIsoLanguage, String> getValueMap() {
        final Map<EIsoLanguage, String> valueMap = getLocalizingStringContext().getValueMap();
        if (valueMap != null) {
            return valueMap;
        }
        return Collections.<EIsoLanguage, String>emptyMap();
    }

    public void update(HandleInfo handleInfo) {
        assert handleInfo != null;
        if (handleInfo != null) {
            open(LocalizingStringContextFactory.newInstance(handleInfo));
        }
    }

    public void update(ILocalizingStringContext handleInfoAdapter) {
        assert handleInfoAdapter != null;
        if (handleInfoAdapter != null) {
            open(handleInfoAdapter);
        }
    }

    public final List<EIsoLanguage> getLanguages() {
        if (localizingStringContext != null) {
            final Definition definition = localizingStringContext.getAdsDefinition();
            if (definition != null && definition.isInBranch()) {
                return definition.getModule().getSegment().getLayer().getLanguages();
            }
        }
        return Collections.<EIsoLanguage>emptyList();
    }

    public void setReadonly(boolean readonly) {
        if (editorComponent != null) {
            editorComponent.setReadonly(readonly);
        }
    }

    public final Options getOptions() {
        return options;
    }

    public final void removeChangeListener(ChangeListener listener) {
        eventsSupport.removeListener(listener);
    }

    public final void addChangeListener(ChangeListener listener) {
        eventsSupport.addListener(listener);
    }

    public boolean isReadonly() {
        return editorComponent.isReadonly();
    }
}
