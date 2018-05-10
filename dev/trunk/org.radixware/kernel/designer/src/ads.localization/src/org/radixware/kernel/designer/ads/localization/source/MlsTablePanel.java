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
 * mlstringPanel.java
 *
 * Created on Aug 14, 2009, 4:43:44 PM
 */
package org.radixware.kernel.designer.ads.localization.source;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import javax.swing.Action;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.localization.AdsPhraseBookDef;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.ads.localization.MultilingualEditor;
import org.radixware.kernel.designer.ads.localization.MultilingualEditorUtils;
import org.radixware.kernel.designer.ads.localization.MultilingualEditorUtils.SelectionInfo;
import org.radixware.kernel.designer.ads.localization.RowString;


public class MlsTablePanel extends AbstractTablePanel {

    private List<EIsoLanguage> sourceLangs;
    private List<EIsoLanguage> translLangs;
    private ToolBarUi toolBarUi;
    private final MultilingualEditor parent;
    private SelectionInfo selectionInfo = SelectionInfo.NONE;
    private final FilterSettings filterSettings = new FilterSettings();

    /**
     * Creates new form mlstringPanel
     */
    public MlsTablePanel(final MultilingualEditor parent) {
        super();
        //initComponents();
        this.parent = parent;
        this.setAlignmentX(0.0f);
        createUi();
    }
    
    public void open(){
        if (sourceLangs == null || translLangs == null 
                || !sourceLangs.equals(parent.getSourceLags()) || 
                !translLangs.equals(parent.getTranslatedLags())){
            sourceLangs = parent.getSourceLags();
            translLangs = parent.getTranslatedLags();
            tableUi.open(sourceLangs, translLangs);
            selectRow();
        }
    }
    
    public void update() {
        tableUi.fireTableDataChanged();
        selectRow();
    }

    public Map<Layer, List<Module>> getLayersAndModules() {
        return parent.getLayersAndModules();
    }
    
    public Map<Layer, List<Module>> getSelectedLayers() {
        return parent.getSelectedLayers();
    }

    public boolean changeLayers(Map<Layer, List<Module>> newSelectedLayers){
        return parent.changeLayers(newSelectedLayers);
    }
    private void createUi() {
        toolBarUi = new ToolBarUi(this);
        parent.addChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                String propertyName = e.getPropertyName();
                switch(propertyName){
                    case MultilingualEditorUtils.LOADING_STRINGS:
                        List<RowString> rows = (List<RowString>) e.getNewValue();
                        if (!rows.isEmpty()) {
                            tableUi.addAllRow(rows, filterSettings);
                        }
                        break; 
                    case MultilingualEditorUtils.CLEAR_STRINGS:
                        tableUi.clear();
                        break;
                    case MultilingualEditorUtils.FILTER_REFRESH:
                        toolBarUi.update();
                        break;
                    case MultilingualEditorUtils.STRINGS_UP_TO_DATE:
                        tableUi.refreshTableModel();
                        toolBarUi.update();
                        break;
                    case MultilingualEditorUtils.PROGRESS_HANDLE:
                        Boolean serching = (Boolean) e.getNewValue();
                        toolBarUi.setStatisticEnabled(!serching && getTable().getRowCount() > 0);
                       break;
                    case MultilingualEditorUtils.GO_TO_NEXT_ROW:
                        setNextString();
                        break;
                    case MultilingualEditorUtils.GO_TO_PREVIOUS_ROW:
                        setPrevString();
                        break;
                    case MultilingualEditorUtils.GO_TO_PREVIOUS_UNCHECKED_ROW:
                        setPrevUncheckedString();
                        break;
                    case MultilingualEditorUtils.GO_TO_NEXT_UNCHECKED_ROW:
                        setNextUncheckedString();
                        break;
                    case MultilingualEditorUtils.GO_TO_PREV_EDITABLE_ROW:
                        setPrevEditableString();
                        break;
                    case MultilingualEditorUtils.GO_TO_NEXT_EDITABLE_ROW:
                        setNextEditableString();
                }
            }
        });
    }

    @Override
    protected void selectRow() {
        int rowIndex = 0;
        if (curMlString != null) {
            rowIndex = tableUi.getRowIndex(curMlString);
        }

        if (rowIndex < getTable().getRowCount() && (rowIndex >= 0)) {
            getTable().getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
        } else if (getTable().getRowCount() > 0) {
            getTable().getSelectionModel().setSelectionInterval(0, 0);
        } else {
            toolBarUi.setReadOnly(true);
            parent.setRowString(null, false, SelectionInfo.NONE);
        }
    }

    @Override
    protected void changeStatus(final int row_index, final int col_index) {
        if (col_index != 0) {
            return;
        }
        final RowString row = tableUi.getRowString(row_index);
        if ((!row.hasCheckedTranslation(sourceLangs)) && (!row.isSrcLangsInTranslList(sourceLangs, translLangs))) {
            final String msg = "Can not change status!\nSource text is not checked.\nTo change status of translation, change status of source text on checked.";
            showMsg(msg);
            return;
        }
        final boolean status = row.isNeedsCheck(translLangs);
        for (EIsoLanguage lang : translLangs) {
            final String str = row.getValue(lang);
            if ((str != null) && (!str.equals(""))) {
                row.setNeedCheck(lang, !status);
                if (status) {
                    parent.createPrompt(row);                    
                } else {
                    parent.removePrompt(row);
                }
                tableUi.fireTableCellUpdated(row_index, 0);
                getTable().getSelectionModel().setSelectionInterval(row_index, row_index);
                parent.updateTargetLangsStatus(row);
            } else {
                final String msg = "Can not change status of " + lang.getName() + " translation!\n" + lang.getName() + " translation is not specified.";
                showMsg(msg);
                return;
            }
        }
    }

    @Override
    public void setCurrentRowString() {
        final int row = getTable().getSelectedRow();
        if ((row > -1) && (row < getTable().getRowCount())) {
            toolBarUi.setReadOnly(false);
            if (selectionInfo == SelectionInfo.NONE && setFocusOnTranslation){
                selectionInfo = SelectionInfo.FOCUS;
            }
            parent.setRowString(tableUi.getRowString(row), selectionInfo);
            curMlString = tableUi.getRowString(row);
            toolBarUi.canViewHtml(tableUi.isHtml(row));
        } else {
            toolBarUi.setReadOnly(true);
            parent.setRowString(null);
            toolBarUi.canViewHtml(false);
        }
        selectionInfo = SelectionInfo.NONE;
    }

    public void setPrevString() {
        if (getTable().getRowCount() == 0){
            return;
        }
        int row = getTable().getSelectedRow();
        if (row - 1 >= 0) {
            row = row - 1;
        } else {
            row = getTable().getRowCount() - 1;
        }

        getTable().getSelectionModel().setSelectionInterval(row, row);
        tableUi.fireTableCellUpdated(row, 0);
        selectionInfo = SelectionInfo.PREV;
    }
    
    public void setNextUncheckedString() {
        if (getTable().getRowCount() == 0){
            return;
        }
        int row = getTable().getSelectedRow();
        do {
            if (row + 1 < getTable().getRowCount()) {
                row = row + 1;
            } else {
                row = 0;
            }
        } while ((!tableUi.getRowString(row).isNeedsCheck(getAllLanguages())) && (row != getTable().getSelectedRow()));

        if (row != getTable().getSelectedRow()) {
            selectionInfo = SelectionInfo.UNCHECK_NEXT;
            getTable().getSelectionModel().setSelectionInterval(row, row);
            tableUi.fireTableCellUpdated(row, 0);
        }
    }
    
    public void setPrevUncheckedString() {
        if (getTable().getRowCount() == 0){
            return;
        }
        
        int row = getTable().getSelectedRow();
        do {
            if (row - 1 >= 0) {
                row = row - 1;
            } else {
                row = getTable().getRowCount() - 1;
            }
        } while ((!tableUi.getRowString(row).isNeedsCheck(getAllLanguages())) && (row != getTable().getSelectedRow()));

        if (row != getTable().getSelectedRow()) {
            selectionInfo = SelectionInfo.UNCHECK_PREV;
            getTable().getSelectionModel().setSelectionInterval(row, row);
            tableUi.fireTableCellUpdated(row, 0);
        }
    }
    

    public void setPrevEditableString() {
        if (getTable().getRowCount() == 0) {
            return;
        }
        
        int row = getTable().getSelectedRow();
        do {
            if (row - 1 >= 0) {
                row = row - 1;
            } else {
                row = getTable().getRowCount() - 1;
            }
        } while ((!tableUi.getRowString(row).isNeedTranslate(getAllLanguages())) && (row != getTable().getSelectedRow()));

        if (row != getTable().getSelectedRow()) {
            selectionInfo = SelectionInfo.EDITABLE_PREV;
            getTable().getSelectionModel().setSelectionInterval(row, row);
            tableUi.fireTableCellUpdated(row, 0);
        }
    }

    public void setNextEditableString() {
        if (getTable().getRowCount() == 0) {
            return;
        }

        int row = getTable().getSelectedRow();
        do {
            if (row + 1 < getTable().getRowCount()) {
                row = row + 1;
            } else {
                row = 0;
            }
        } while ((!tableUi.getRowString(row).isNeedTranslate(getAllLanguages())) && (row != getTable().getSelectedRow()));

        if (row != getTable().getSelectedRow()) {
            selectionInfo = SelectionInfo.EDITABLE_NEXT;
            getTable().getSelectionModel().setSelectionInterval(row, row);
            tableUi.fireTableCellUpdated(row, 0);
        }
    }


    @Override
    public List<EIsoLanguage> getTranslatedLags() {
        return translLangs;
    }

    @Override
    public List<EIsoLanguage> getSourceLags() {
        return sourceLangs;
    }

    public void setTranslatedLags(final List<EIsoLanguage> translLangs) {
        this.translLangs = translLangs;
    }

    public void setSourceLags(final List<EIsoLanguage> sounseLangs) {
        this.sourceLangs = sounseLangs;
    }

    public void changeLangs() {
        parent.changeLangs();
    }

    public List<RowString> getRowStrings() {
        return tableUi.getRows();
    }
    
    public List<RowString> getFiltredRowStrings() {
        return tableUi.getFiltedRows();
    }

    public void setSelectedDefs(final List<Definition> defs) {
        parent.setSelectedDefs(defs);
    }

    public void reloadMlStrings() {
        parent.resetSelectedDefinitions();
    }

    public void updateStringRow() {
        final int row = getTable().getSelectedRow();
        if ((row < getTable().getRowCount()) && (row >= 0)) {
            final RowString rowString = tableUi.getRowString(row);
            rowString.setWasEdit(true);
            parent.addEditedStringList(rowString);
            tableUi.fireTableRowsUpdated(row);
        }
    }

    public void firePropertyChange(String propertyName) {
        parent.fireChange(propertyName, false, true);
    }

    public void goToNextUncheckedTranslation() {
        parent.fireChange(MultilingualEditorUtils.GO_TO_NEXT_UNCHECKED, false, true);
    }

    public List<AdsPhraseBookDef> getOpenedPhraseBook() {
        return parent.getOpenedPhraseBook();
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    public void addCurMlStringToPhraseBook(final AdsPhraseBookDef phraseBook) {
        final RowString rowString = getCurrentRowString();
        if (rowString != null) {
            final IMultilingualStringDef curStr = rowString.getMlStrings().get(0);
            if (curStr != null && curStr instanceof AdsMultilingualStringDef) {
                final IMultilingualStringDef newStr = AdsMultilingualStringDef.Factory.newInstance(curStr.getSrcKind());
                final Layer layer = phraseBook.getModule().getSegment().getLayer();
                final List<EIsoLanguage> langs = layer.getLanguages();
                IMultilingualStringDef.Storage.copyValueByLanguages(curStr.getInternalStorage(),newStr.getInternalStorage(),new HashSet<>(langs));
                final ILocalizingBundleDef bundle = phraseBook.findLocalizingBundle();
                if (isInPhraseBook(newStr, bundle, langs)) {
                    final String msg = "String with such source and target texts exist in phrase book.";//!!!!!!!!!!!переделать
                    showMsg(msg);
                }
                bundle.getStrings().getLocal().add((RadixObject)newStr);
            }
        }
    }

    private boolean isInPhraseBook(final IMultilingualStringDef newStr, final ILocalizingBundleDef<? extends IMultilingualStringDef> bundle, final List<EIsoLanguage> langs) {
        boolean result=false;
        final Definitions<? extends IMultilingualStringDef> list=bundle.getStrings().getLocal();
        for (IMultilingualStringDef mls : list) {
            for (EIsoLanguage l : langs) {
                if ((mls.getValue(l) != null) && (mls.getValue(l).equals(newStr.getValue(l)))) {
                    result=true;
                    break;
                }
            }
        }
        return result;
    }

    public void canAddStringToPhraseBook(final boolean canAdd) {
        toolBarUi.canAddStringToPhraseBook(canAdd);
    }

    public FilterSettings getFilterSettings() {
        return filterSettings;
    }
    
    private List<EIsoLanguage> getAllLanguages(){
        List<EIsoLanguage> languages = new ArrayList<>();
        if (sourceLangs != null){
            languages.addAll(sourceLangs);
        }
        if (translLangs != null){
            languages.addAll(translLangs);
        }
        
        return languages;
    }

    public Action getActionByKey(String key){
        return parent.getActionMap().get(key);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setMinimumSize(new java.awt.Dimension(200, 100));
        setPreferredSize(new java.awt.Dimension(200, 400));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}