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

/*
 * MainTranslationPanel.java
 *
 * Created on Aug 14, 2009, 5:21:35 PM
 */
package org.radixware.kernel.designer.ads.localization;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.builder.api.IProgressHandle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.build.Cancellable;
import org.radixware.kernel.common.defs.ads.localization.AdsPhraseBookDef;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.localization.MultilingualEditorUtils.SelectionInfo;
import org.radixware.kernel.designer.ads.localization.actions.EditorAction;
import org.radixware.kernel.designer.ads.localization.dialog.ChooseLanguagesDialog;
import org.radixware.kernel.designer.ads.localization.phrase_book.PhraseBookProvider;
import org.radixware.kernel.designer.ads.localization.prompt.ContextPanel;
import org.radixware.kernel.designer.ads.localization.prompt.PhrasesAndGuessesPanel;
import org.radixware.kernel.designer.ads.localization.prompt.Prompt;
import org.radixware.kernel.designer.ads.localization.prompt.TimeAuthorInfoPanel;
import org.radixware.kernel.designer.ads.localization.prompt.Validation;
import org.radixware.kernel.designer.ads.localization.prompt.WarningPanel;
import org.radixware.kernel.designer.ads.localization.source.MlsTablePanel;
import org.radixware.kernel.designer.ads.localization.translation.MainTranslationPanel;
import org.radixware.kernel.designer.common.dialogs.build.ProgressHandleFactoryDelegate;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;

public class MultilingualEditor extends TopComponent {

    private long stringVersion = -1;
    private boolean stringLoaded = false;
    private MlsTablePanel topPanel;
    private MainTranslationPanel bottomPanel;
    private ContextPanel context;
    private TimeAuthorInfoPanel timeAuthorInfoPanel;
    private PhrasesAndGuessesPanel phrases;
    private WarningPanel warning;

    private List<EIsoLanguage> sourceLangs;
    private List<EIsoLanguage> translLangs;
    private Map<Layer, List<Module>> selectedLayers = null;
    private final MultilingualListManager multiligualManager;
    private final String editorName = NbBundle.getMessage(MultilingualEditor.class, "MULTILINGUAL_EDITOR_NAME");
    //private final static int CONTEXT_TAB_INDEX=0;
    private final static int PROMPT_TAB_INDEX = 1;
    //private final static int LAST_UPDATE_INFO_TAB_INDEX=2;
    private final static int WARNING_TAB_INDEX = 3;
    private final PropertyChangeSupport propertyChangeSupport;
    

    /**
     * Creates new form MainTranslationPanel
     */
    public MultilingualEditor() {
        propertyChangeSupport = new PropertyChangeSupport(this);
        this.setName(editorName);
        this.setIcon(RadixWareIcons.MLSTRING_EDITOR.CHOOSE_LANGS.getImage());
        addKeyMap();
        initComponents();

        //this.setFocusable(true);
        AbstractAction actInsert = new AbstractAction(NbBundle.getMessage(MultilingualEditor.class, "INSERT_ACTION")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                String translation = phrases.getSelectedPhrase();
                setTranslationFromPraseList(translation);
            }
        };

        phrases = new PhrasesAndGuessesPanel(actInsert, this);
        multiligualManager = new MultilingualListManager(this);

        context = new ContextPanel();
        timeAuthorInfoPanel = new TimeAuthorInfoPanel();
        bottomPanel = new MainTranslationPanel(this);
        topPanel = new MlsTablePanel(this);
        if (phrases.getOpenedPhraseBook().size() > 0) {
            canAddStringToPhraseBook(true);
        }
        warning = new WarningPanel();

       // sourceLangs=getLangsFromCfg(SOURCE_LANGUAGE);
        // translLangs=getLangsFromCfg(TRANSLATE_LANGUAGE);
        // if(sourceLangs==null || translLangs==null){
        //      changeLangs();
        // }
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(bottomPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(bottomPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE)
                        .addContainerGap(0, Short.MAX_VALUE))//196
        );
        jPanel1.add(bottomPanel);
        jSplitPane2.setTopComponent(topPanel);

        jTabbedPane1.addTab(NbBundle.getMessage(MultilingualEditor.class, "TAB_CONTEXT"), context);
        jTabbedPane1.addTab(NbBundle.getMessage(MultilingualEditor.class, "TAB_PHRASES"), phrases/*phrasesTable*/);
        jTabbedPane1.addTab(NbBundle.getMessage(MultilingualEditor.class, "TAB_TIME_AUTHER_INFO"), timeAuthorInfoPanel);

        sourceLangs = getLangsFromCfg(MultilingualEditorUtils.SOURCE_LANGUAGE);
        translLangs = getLangsFromCfg(MultilingualEditorUtils.TRANSLATE_LANGUAGE);
        if (sourceLangs == null) {
            sourceLangs = new ArrayList<>();
        }
        if (translLangs == null) {
            translLangs = new ArrayList<>();
        }
        translLangs = getFilteredTargetLangs(sourceLangs, translLangs);

        //jTabbedPane1.addTab("Сomments", comment);
        //jTabbedPane1.addTab(NbBundle.getMessage(MultilingualEditor.class, "TAB_WARNINGS"), warning);
        //jTabbedPane1.setIconAt(2, RadixWareDesignerIcon.CHECK.ERRORS.getIcon(16, 16));
    }

    public boolean initLangs() {
        if ((sourceLangs.isEmpty()) || (translLangs.isEmpty())) {
            boolean init = showDialodChangeLang();
            resetStringVersion();
            return init;
        }
        return true;
    }
    
    void resetStringVersion(){
        stringVersion = -1;
    }
    

    public void openChildrenPanels(boolean reset) {
        bottomPanel.open();
        topPanel.open();
        if (!reset || !resetSelectedDefinitions()) {
            if (!updateRows()) {
                fireChange(MultilingualEditorUtils.STRINGS_UP_TO_DATE);
            }
        }
    }

    @Override
    public void open() {
        openChildrenPanels(true);
        super.open();
    }

    @Override
    protected void componentClosed() {
        if (cancellable != null && multiligualManager.isInProcess()){
            cancellable.cancel();
        }
        super.componentClosed();
    }

    @Override
    protected void componentDeactivated() {
        if (cancellable != null && !cancellable.wasCancelled()){
            stringVersion = ILocalizingBundleDef.version.get();
        }
        super.componentDeactivated();
    }

    private List<EIsoLanguage> getFilteredTargetLangs(List<EIsoLanguage> sourceLangs, List<EIsoLanguage> translLangs) {
        List<EIsoLanguage> langs = new ArrayList<>();
        List<EIsoLanguage> allLangs = getAllLangs();
        for (EIsoLanguage lang : translLangs) {
            if ((!sourceLangs.contains(lang)) && (allLangs.contains(lang))) {
                langs.add(lang);
            }
        }
        return langs;
    }

    public static List<EIsoLanguage> getAllLangs() {
        List<EIsoLanguage> langs = new ArrayList<>();
        Collection<Branch> openBranches = RadixFileUtil.getOpenedBranches();
        for (Branch branch : openBranches) {
            for (final Layer layer : branch.getLayers()) {
                int size = layer.getLanguages().size();
                final EIsoLanguage[] langsArr = new EIsoLanguage[size];
                layer.getLanguages().toArray(langsArr);
                for (int i = 0; i < size; i++) {
                    if (!langs.contains(langsArr[i])) {
                        langs.add(langsArr[i]);
                    }
                }
            }
        }
        return langs;
    }
    
    public JScrollPane getTranslationPanelScrollPane() {
        return jScrollPane1;
    }

    public void scroll(Rectangle rect) {
        jPanel1.validate();
        jPanel1.repaint();
        if (!jPanel1.getVisibleRect().contains(rect)) {
            jPanel1.scrollRectToVisible(rect);
        }
    }

    @Override
    public int getPersistenceType() {
        return PERSISTENCE_NEVER;
    }

    @Override
    protected void componentActivated() {
        final TopComponent paletteComponent = WindowManager.getDefault().findTopComponent("CommonPalette");
        if (paletteComponent != null) {
            paletteComponent.close();
        }
        TopComponent propertiesComponent = WindowManager.getDefault().findTopComponent("properties");
        if (propertiesComponent != null) {
            propertiesComponent.close();
        }
        update();
    }

    
    
    /*private List<EIsoLanguage> getLangsFromCfg(String node) {///FOR TEST
     List<EIsoLanguage> langs = new ArrayList<EIsoLanguage>();
     Collection<Branch> openBranches = RadixFileUtil.getOpenedBranches();
     for(Branch branch : openBranches){
     for(final Layer layer : branch.getLayers()){
     int size=layer.getLanguages().size();
     final EIsoLanguage[] langsArr = new EIsoLanguage[size];
     layer.getLanguages().toArray(langsArr);
     for(int i=0;i<size;i++){
     if(!langs.contains(langsArr[i]))
     langs.add(langsArr[i]);
     }
     }
     }
     return langs;
     }*/
    private List<EIsoLanguage> getLangsFromCfg(String node) {
        try {
            if (Preferences.userRoot().nodeExists(MultilingualEditorUtils.PREFERENCES_KEY)) {
                Preferences designerPreferences = Preferences.userRoot().node(MultilingualEditorUtils.PREFERENCES_KEY);
                if (designerPreferences.nodeExists(MultilingualEditorUtils.EDITOR_KEY)) {
                    Preferences editor = designerPreferences.node(MultilingualEditorUtils.EDITOR_KEY);
                    String sLang = editor.get(node, null);
                    if (sLang != null) {
                        List<EIsoLanguage> langs = new ArrayList<>();
                        String[] arrLang = sLang.split(",");
                        for (int i = 0; i < arrLang.length; i++) {
                            langs.add(EIsoLanguage.getForValue(arrLang[i]));
                        }
                        return langs;
                    }
                }
            }
            return null;
        } catch (BackingStoreException ex) {
            DialogUtils.messageError(ex);
            return null;
        }
    }

    public final Set<Layer> getLayersFromCfg() {
        try {
            if (Preferences.userRoot().nodeExists(MultilingualEditorUtils.PREFERENCES_KEY)) {
                Preferences designerPreferences = Preferences.userRoot().node(MultilingualEditorUtils.PREFERENCES_KEY);
                if (designerPreferences.nodeExists(MultilingualEditorUtils.EDITOR_KEY)) {
                    Preferences editor = designerPreferences.node(MultilingualEditorUtils.EDITOR_KEY);
                    String sLayers = editor.get(MultilingualEditorUtils.SELECTED_LAYERS, null);
                    if (sLayers != null) {
//                        String[] split = sLayers.split(";");
//                        for (String uri : split) {
//                            Collection<Branch> openBranches = RadixFileUtil.getOpenedBranches();
//                            for(Branch branch : openBranches){             
//                                Layer layer = branch.getLayers().findByURI(uri);
//                                if (layer!= null){
//                                    add
//                                }
//                            }
//                        }
                    } else {
                        selectedLayers = null;
                    }

                }
            }
            return null;
        } catch (BackingStoreException ex) {
            DialogUtils.messageError(ex);
            return null;
        }
    }

    public List<AdsPhraseBookDef> getPhraseBooksFromCfg() {
        try {
            if (Preferences.userRoot().nodeExists(MultilingualEditorUtils.PREFERENCES_KEY)) {
                Preferences designerPreferences = Preferences.userRoot().node(MultilingualEditorUtils.PREFERENCES_KEY);
                if (designerPreferences.nodeExists(MultilingualEditorUtils.EDITOR_KEY)) {
                    Preferences editor = designerPreferences.node(MultilingualEditorUtils.EDITOR_KEY);
                    String sId = editor.get(MultilingualEditorUtils.OPENED_PHRASEBOOK, null);
                    if (sId != null) {
                        List<AdsPhraseBookDef> phraseBooks = new ArrayList<>();
                        String[] arrIds = sId.split(",");
                        for (int i = 0; i < arrIds.length; i++) {
                            AdsPhraseBookDef phraseBook = findPhraseBook(getLayersAndModules().keySet(), Id.Factory.loadFrom(arrIds[i]));
                            if (phraseBook != null) {
                                phraseBooks.add(phraseBook);
                            }
                            //phraseBooks.add(AdsPhraseBookDef);
                        }

                        return phraseBooks;
                    }
                }
            }
            return null;
        } catch (BackingStoreException ex) {
            DialogUtils.messageError(ex);
            return null;
        }
    }

    public void setPhraseBooksToCfg(List<AdsPhraseBookDef> phraseBooks) {
        StringBuilder builder = new StringBuilder();
        for (AdsPhraseBookDef phraseBook : phraseBooks) {
            builder.append(phraseBook.getId());
            builder.append(",");
        }
        String s = builder.toString();
        if ("".equals(s)) {
            return;
        }
        s = s.substring(0, s.length() - 1);

        Preferences designerPreferences = Preferences.userRoot().node(MultilingualEditorUtils.PREFERENCES_KEY);
        Preferences editor = designerPreferences.node(MultilingualEditorUtils.EDITOR_KEY);
        editor.put(MultilingualEditorUtils.OPENED_PHRASEBOOK, s);
        try {
            Preferences.userRoot().flush();
        } catch (BackingStoreException ex) {
            DialogUtils.messageError(ex);
        }
    }

    private void setLangsToCfg(List<EIsoLanguage> langs, final String node) {
        StringBuilder builder = new StringBuilder();
        for (EIsoLanguage lang : langs) {
            builder.append(lang.getValue());
            builder.append(",");
        }
        String s = builder.toString();
        if ("".equals(s)) {
            return;
        }
        s = s.substring(0, s.length() - 1);

        Preferences designerPreferences = Preferences.userRoot().node(MultilingualEditorUtils.PREFERENCES_KEY);
        Preferences editor = designerPreferences.node(MultilingualEditorUtils.EDITOR_KEY);
        editor.put(node, s);
        try {
            Preferences.userRoot().flush();
        } catch (BackingStoreException ex) {
            DialogUtils.messageError(ex);
        }
    }

    private void setLayersToCfg(Set<Layer> layers) {
        StringBuilder builder = new StringBuilder();
        for (Layer layer : layers) {
            builder.append(layer.getURI());
            builder.append(";");
        }
        String s = builder.toString();
        s = !s.isEmpty() ? s : null;

        Preferences designerPreferences = Preferences.userRoot().node(MultilingualEditorUtils.PREFERENCES_KEY);
        Preferences editor = designerPreferences.node(MultilingualEditorUtils.EDITOR_KEY);
        editor.put(MultilingualEditorUtils.SELECTED_LAYERS, s);
        try {
            Preferences.userRoot().flush();
        } catch (BackingStoreException ex) {
            DialogUtils.messageError(ex);
        }
    }

    private boolean showDialodChangeLang() {
        ChooseLanguagesDialog panel = new ChooseLanguagesDialog(sourceLangs, translLangs);
        panel.check();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        StateAbstractDialog md = new StateAbstractDialog(panel, NbBundle.getMessage(MultilingualEditor.class, "CHOOSE_LANDUAGE_DIALOG_NAME")) {
            @Override
            protected void apply() {
            }
        };
        if (md.showModal()) {
            List<EIsoLanguage> newTranslLangs = panel.getTranslatedLangs();
            List<EIsoLanguage> newSourceLangs = panel.getSourceLangs();
            if ((translLangs == null) || (sourceLangs == null)
                    || (!translLangs.equals(newTranslLangs))
                    || (!sourceLangs.equals(newSourceLangs))) {
                translLangs = newTranslLangs;
                sourceLangs = newSourceLangs;
                saveLangsToCfg();
                stringVersion = ILocalizingBundleDef.version.get();
                openChildrenPanels(false);
                return true;
            }
        }

        return false;
    }

    public boolean changeLayers(Map<Layer, List<Module>> newSelectedLayers){
        if ((newSelectedLayers != null) && (!newSelectedLayers.equals(selectedLayers))
                    || (newSelectedLayers == null && selectedLayers != null)) {
                selectedLayers = newSelectedLayers;
                saveLayersToCfg();
                reopen();
            return true;
        }
        return false;
    }

    public void setSelectedLayers(Set<Layer> layers) {
        resetStringVersion();
        if (layers == null) {
            selectedLayers = null;
            return;
        }

        if (selectedLayers == null) {
            selectedLayers = new HashMap<>();
        } else {
            selectedLayers.clear();
        }
        for (Layer l : layers) {
            if (l.isLocalizing() && !l.getBaseLayerURIs().isEmpty()) {
                Collection<Branch> openBranches = RadixFileUtil.getOpenedBranches();
                for (Branch branch : openBranches) {
                    Layer layer = branch.getLayers().findByURI(l.getBaseLayerURIs().get(0));
                    if (layer != null) {
                        selectedLayers.put(layer, null);
                        break;
                    }
                }
            } else {
                selectedLayers.put(l, null);
            }
        }
    }

    public boolean canSetSelectedLayers(Set<Layer> layers) {
        if (selectedLayers == null) {
            return layers != null;
        }

        if (layers == null) {
            return true;
        } else {
            return !selectedLayers.equals(layers);
        }

    }

    public Map<Layer, List<Module>> getLayersAndModules() {
        return MultilingualEditorUtils.getLayersAndModules(selectedLayers);
    }
    
    public Map<Layer, List<Module>> getSelectedLayers() {
        return selectedLayers == null ? null: Collections.unmodifiableMap(selectedLayers);
    }
    
    private void saveLangsToCfg() {
        if (translLangs == null) {
            translLangs = new ArrayList<>();
        } else {
            setLangsToCfg(translLangs, MultilingualEditorUtils.TRANSLATE_LANGUAGE);
        }
        if (sourceLangs == null) {
            sourceLangs = new ArrayList<>();
        } else {
            setLangsToCfg(sourceLangs, MultilingualEditorUtils.SOURCE_LANGUAGE);
        }
    }

    private void saveLayersToCfg() {
        if (selectedLayers != null) {
            setLayersToCfg(selectedLayers.keySet());
        }
    }

    public void setRowString(final RowString rowString) {
        setRowString(rowString, SelectionInfo.NONE);
    }
    
    public void setRowString(final RowString rowString, final SelectionInfo selectUncheckedTranslation) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                bottomPanel.setMlString(rowString);
                switch (selectUncheckedTranslation){
                    case FOCUS:
                        fireChange(MultilingualEditorUtils.FOCUS_TEXT);
                        break;
                    case PREV:
                        fireChange(MultilingualEditorUtils.SELECT_LAST_TEXT);
                        break;
                    case UNCHECK_PREV:
                        fireChange(MultilingualEditorUtils.SELECT_LAST_UNCHECKED_TEXT);
                        break;
                    case UNCHECK_NEXT:
                        fireChange(MultilingualEditorUtils.SELECT_FIRST_UNCHECKED_TEXT);
                        break;
                    case EDITABLE_PREV:
                        fireChange(MultilingualEditorUtils.SELECT_LAST_EDITABLE_TEXT);
                        break;
                    case EDITABLE_NEXT:
                        fireChange(MultilingualEditorUtils.SELECT_FIRST_EDITABLE_TEXT);
                        break;    
                }
                context.setContext(rowString);
                List<EIsoLanguage> langs = new LinkedList<>();
                langs.addAll(translLangs);
                langs.addAll(sourceLangs);
                timeAuthorInfoPanel.setContext(rowString, langs);

                updatePhrasesPanel(rowString);
            }
        });
        
    }

    public void setTranslationFromPraseList(String translation) {
        bottomPanel.setTranslationFromPraseList(translation);
    }

    public final void updatePhrasesPanel() {
        RowString rowString = topPanel.getCurrentRowString();
        updatePhrasesPanel(rowString);
    }

    private void updatePhrasesPanel(RowString rowString) {
        EIsoLanguage translLang = bottomPanel.getCurTranslateLanguage();
        phrases.update(translLang, rowString, sourceLangs, translLangs);
        if (phrases.hasPrompts()) {
            jTabbedPane1.setIconAt(PROMPT_TAB_INDEX, AdsDefinitionIcon.PHRASE_BOOK.getIcon(16, 16)/*RadixWareIcons.MLSTRING_EDITOR.CHOOSE_LANGS.getIcon(16, 16)*/);
        } else {
            jTabbedPane1.setIconAt(PROMPT_TAB_INDEX, null);
        }
    }

    public void createPrompt(RowString row) {
        Prompt p = new Prompt(row, sourceLangs, translLangs);
        phrases.addItemToPromptList(p);
        updatePhrasesPanel();
    }

    public void removePrompt(RowString row) {
        Prompt p = new Prompt(row, sourceLangs, translLangs);
        phrases.removeItemFromPromptList(p);
        updatePhrasesPanel();
    }

    public void createPrompt(final IMultilingualStringDef mlstring) {
        Prompt p = new Prompt(mlstring, sourceLangs, translLangs);
        phrases.addItemToPromptList(p);
    }

    public void clearPromptList() {
        phrases.clearPromptList();
    }

    public List<AdsPhraseBookDef> getOpenedPhraseBook() {
        return phrases.getOpenedPhraseBook();
    }

    public List<EIsoLanguage> getTranslatedLags() {
        return translLangs;
    }

    public List<EIsoLanguage> getSourceLags() {
        return sourceLangs;
    }

    public void setSelectedDefs(List<Definition> defs) {
        setTabName(defs);
        multiligualManager.setSelectedDefs(defs);
        reopen();
    }

    private void setTabName(List<Definition> defs) {
        if ((defs == null) || (defs.isEmpty())) {
            return;
        }
        if (defs.size() == 1) {
            this.setName(editorName + " (" + defs.get(0).getName() + ")");
        } else {
            String s = MessageFormat.format(NbBundle.getMessage(MultilingualEditor.class, "MULTILINGUAL_EDITOR_NAME_SELECTED_DEFS"), defs.size());
            this.setName(s);//" ("+defs.size()+" definitions)");

            StringBuilder builder = new StringBuilder();
            for (Definition def : defs) {
                builder.append(def.getName());
                builder.append(";");
            }
            String toolTip = builder.toString();
            if (!toolTip.isEmpty()) {
                toolTip = toolTip.substring(0, toolTip.length() - 2);
            }
            this.setToolTipText(toolTip);
        }
    }

    public final void changeLangs() {
        if (showDialodChangeLang()) {
            update();
        }
    }

    Cancellable cancellable;
    private boolean updateRows() {
        if (stringVersion != ILocalizingBundleDef.version.get()) {
            if (cancellable != null){
                cancellable.cancel();
            }
            cancellable = new CancellableImpl(this);
            stringVersion = ILocalizingBundleDef.version.get();
            multiligualManager.updateMlStrings(sourceLangs, translLangs, selectedLayers, cancellable);
            return true;
        }
        return false;
    }

    public void reopen() {
        fireChange(MultilingualEditorUtils.FILTER_REFRESH);
        if (cancellable != null){
            cancellable.cancel();
        }
        cancellable = new CancellableImpl(this);
        multiligualManager.updateMlStrings(sourceLangs, translLangs, selectedLayers, cancellable);
        stringVersion = ILocalizingBundleDef.version.get();
        update();
    }

    public final void update() {
        updateRows();
        bottomPanel.update();
        topPanel.update();
        updatePhrasesPanel();
        timeAuthorInfoPanel.update();
    }

    public void updateTargetLangsStatus(RowString rowString) {
        bottomPanel.updateTargetLangsStatus(rowString);
    }

    public boolean resetSelectedDefinitions() {
        if (multiligualManager.showSelectedDefs()) {
            multiligualManager.setSelectedDefs(null);
            this.setName(editorName);
            reopen();
            return true;
        }
        return false;
    }

    public void translationWasEdited() {
        topPanel.updateStringRow();
        timeAuthorInfoPanel.update();
    }

    public void addEditedStringList(RowString s) {
        multiligualManager.addEditedStringList(s);
    }

    public final void canAddStringToPhraseBook(boolean b) {
        topPanel.canAddStringToPhraseBook(b);
    }

    public void checkWarnings(RowString rowString) {
        EIsoLanguage lang = bottomPanel.getCurTranslateLanguage();
        String s = rowString.getValue(lang);

        List<String> sourceTexts = new ArrayList<>();
        for (EIsoLanguage l : sourceLangs) {
            if (!l.equals(lang)) {
                sourceTexts.add(rowString.getValue(l));
            }
        }

        Validation v = new Validation(s, sourceTexts, phrases.getPhraseBookPrompts());

        if (!v.isValid()) {
            if (jTabbedPane1.getTabCount() == 3) {
                jTabbedPane1.addTab(NbBundle.getMessage(MultilingualEditor.class, "TAB_WARNINGS"), warning);
                jTabbedPane1.setIconAt(WARNING_TAB_INDEX, RadixWareIcons.CHECK.ERRORS.getIcon(16, 16));
            }
        } else {
            if (jTabbedPane1.getTabCount() == 4) {
                jTabbedPane1.remove(WARNING_TAB_INDEX);
            }
        }
        warning.setWarningList(v.getWarningList());
    }

    public void save() {
        multiligualManager.saveStings();
        topPanel.update();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();

        setPreferredSize(new java.awt.Dimension(800, 600));

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.9);

        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setResizeWeight(0.4);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(jPanel1);

        jSplitPane2.setBottomComponent(jScrollPane1);

        jSplitPane1.setTopComponent(jSplitPane2);
        jSplitPane1.setRightComponent(jTabbedPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
        );
    }// </editor-fold>                        

    // Variables declaration - do not modify                     
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration                   

    private AdsPhraseBookDef findPhraseBook(Set<Layer> layers, final Id id) {
        for (final Layer layer : layers) {
            AdsPhraseBookDef phraseBook = (AdsPhraseBookDef) layer.find(new PhraseBookProvider(id));
            if (phraseBook != null) {
                return phraseBook;
            }
        }
        return null;
    }
    
    public void removeChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public void addChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void fireChange(String propertyName) {
        propertyChangeSupport.firePropertyChange(propertyName, false, true);
    }

    public void fireChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void fireChange(String propertyName, boolean oldValue, boolean newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    private void addKeyMap(){
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        //Ctrl+< - GO_TO_PREV
        Icon icon = RadixWareIcons.MLSTRING_EDITOR.LEFT.getIcon(20);
        final EditorAction actGoToPrev = new EditorAction(this, MultilingualEditorUtils.GO_TO_PREV, icon);
        getActionMap().put(actGoToPrev.getName(), actGoToPrev);
        KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, KeyEvent.CTRL_DOWN_MASK, true);
        inputMap.put(keyStroke, actGoToPrev.getName());
        
        //Ctrl+> - GO_TO_NEXT
        icon = RadixWareIcons.MLSTRING_EDITOR.RIGHT.getIcon(20);
        final EditorAction actGoToNext = new EditorAction(this, MultilingualEditorUtils.GO_TO_NEXT, icon);
        getActionMap().put(actGoToNext.getName(), actGoToNext);
        keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, KeyEvent.CTRL_DOWN_MASK, true);
        inputMap.put(keyStroke, actGoToNext.getName());
        
       //Ctrl+K - GO_TO_PREV_UNCHECKED
       icon = RadixWareIcons.MLSTRING_EDITOR.PREV_CHECKED_STRING.getIcon(20);
       final EditorAction actGoToPrevUnch = new EditorAction(this, MultilingualEditorUtils.GO_TO_PREV_UNCHECKED, icon);
       getActionMap().put(actGoToPrevUnch.getName(), actGoToPrevUnch);
       keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_DOWN_MASK, true); 
       inputMap.put(keyStroke, actGoToPrevUnch.getName());
        
       //Ctrl+L - GO_TO_NEXT_UNCHECKED
       icon = RadixWareIcons.MLSTRING_EDITOR.NEXT_CHECKED_STRING.getIcon(20);
       final EditorAction actGoToNextUnch = new EditorAction(this, MultilingualEditorUtils.GO_TO_NEXT_UNCHECKED, icon);
       getActionMap().put(actGoToNextUnch.getName(), actGoToNextUnch);
       keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK, true);
       inputMap.put(keyStroke, actGoToNextUnch.getName());
       
       
       //Ctrl+: - GO_TO_PREV_EDITABLE
        final EditorAction actGoToPrevEdit = new EditorAction(this, MultilingualEditorUtils.GO_TO_PREV_EDITABLE);
        getActionMap().put(actGoToPrevEdit.getName(), actGoToPrevEdit);
        keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_SEMICOLON, InputEvent.CTRL_DOWN_MASK, true);
        inputMap.put(keyStroke, actGoToPrevEdit.getName());
        
        //Ctrl+' - GO_TO_NEXT_EDITABLE
        final EditorAction actGoToNextEdit = new EditorAction(this, MultilingualEditorUtils.GO_TO_NEXT_EDITABLE);
        getActionMap().put(actGoToNextEdit.getName(), actGoToNextEdit);
        keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_QUOTE, InputEvent.CTRL_DOWN_MASK, true);
        inputMap.put(keyStroke, actGoToNextEdit.getName());
        
        //Ctrl+Up - GO_TO_PREVIOUS_ROW
        final EditorAction actGoToPrevRow = new EditorAction(this, MultilingualEditorUtils.GO_TO_PREVIOUS_ROW);
        getActionMap().put(actGoToPrevRow.getName(), actGoToPrevRow);
        keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_DOWN_MASK, true);
        inputMap.put(keyStroke, actGoToPrevRow.getName());
        
        //Ctrl+Down - GO_TO_NEXT_ROW
        final EditorAction actGoToNextRow = new EditorAction(this, MultilingualEditorUtils.GO_TO_NEXT_ROW);
        getActionMap().put(actGoToNextRow.getName(), actGoToNextRow);
        keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_DOWN_MASK, true);
        inputMap.put(keyStroke, actGoToNextRow.getName());

        //Shift+Enter - GO_TO_NEXT_ROW
        final EditorAction actCheckAndGo = new EditorAction(this, MultilingualEditorUtils.CHECK_ALL_AND_GO);
        getActionMap().put(actCheckAndGo.getName(), actCheckAndGo);
        keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.SHIFT_DOWN_MASK, true);
        inputMap.put(keyStroke, actCheckAndGo.getName());
    }

    public void setRowString(Object object, boolean b, MultilingualEditorUtils.SelectionInfo uncheckedSelection) {
    }
}
