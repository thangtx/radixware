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

package org.radixware.kernel.explorer.editors.sqmleditor;

import com.trolltech.qt.core.Qt.FocusPolicy;
import com.trolltech.qt.gui.QShowEvent;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameters;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.scml.SqmlExpression;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.xscmleditor.AbstractXscmlEditor;
import org.radixware.kernel.explorer.editors.xscmleditor.Highlighter;
import org.radixware.kernel.explorer.editors.xscmleditor.TagInfo;
import org.radixware.kernel.explorer.editors.xscmleditor.TagProcessor;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;
import org.radixware.schemas.xscml.Sqml;

public class SqmlEditor extends ExplorerWidget {
    
    private static final class DummyXscmlEditor extends AbstractXscmlEditor{
        
        private TagProcessor converter;
        
        public DummyXscmlEditor(final QWidget parent, final TagProcessor converter){
            super(parent);
            this.converter = converter;
            setReadOnly(true);            
        }

        @Override
        public void insertTag(final TagInfo tag, final String suffix) {
            //Empty implementation
        }

        @Override
        public void insertText(final String s) {
            //Empty implementation
        }

        @Override
        public void setTagConverter(final TagProcessor converter) {            
            this.converter = converter;
        }

        @Override
        public TagProcessor getTagConverter() {
            return converter;
        }        
    }
    
    private final class InternalXscmlEditor extends XscmlEditor{
        
        public InternalXscmlEditor(final IClientEnvironment environment, final TagProcessor converter, final QWidget parent){
            super(environment, converter, parent);
            textChanged.connect(SqmlEditor.this, "textChange()");
        }

        @Override
        public void updateTagsName() {
            textChanged.disconnect(SqmlEditor.this);
            try{
                super.updateTagsName();
            }finally{
                textChanged.connect(SqmlEditor.this, "textChange()");
            }
            SqmlEditor.this.textChange();
        }       
    }
    
    private final AbstractXscmlEditor editText;
    private final SqmlProcessor converter;
    private final SqmlEditor_UI ui;
    private final Highlighter hightlighter;
    private final boolean isSqmlAccessible;    
    private final SqmlTagInsertion tagInsertion;
        
    private Sqml sqml;
    private Sqml additionalFrom;
    private ISqmlTableDef contextClass;
    private Id contextClassId;        
    private boolean modified = false;    
    private boolean readOnly = false;    
    private boolean wasShown = false;
    public final Signal0 onParametersChanged = new Signal0();

    public static final class SqmlEditorIcons extends ClientIcon {
        public static final SqmlEditorIcons IMG_ADD_TAG = new SqmlEditorIcons("classpath:images/tableColumn.svg");
        public static final SqmlEditorIcons IMG_ADD_CONDITION = new SqmlEditorIcons("classpath:images/columnCondition.svg");
        public static final SqmlEditorIcons IMG_INSERT_COLUMN = new SqmlEditorIcons("classpath:images/columns.svg");
        public static final SqmlEditorIcons IMG_INSERT_THIS_TABLE_REF = new SqmlEditorIcons("classpath:images/this_table.svg");
        public static final SqmlEditorIcons IMG_INSERT_TAG_DB_OBJECT = new SqmlEditorIcons("classpath:images/db_object.svg");
        public static final SqmlEditorIcons IMG_INSERT_ID = new SqmlEditorIcons("classpath:images/class_id.svg");
        public static final SqmlEditorIcons IMG_INSERT_EVENT_CODE = new SqmlEditorIcons("classpath:images/event_code.svg");
        public static final SqmlEditorIcons IMG_ADD_ALIAS = new SqmlEditorIcons("classpath:images/add_alias.svg");
        public static final SqmlEditorIcons IMG_SQML_TRAN = new SqmlEditorIcons("classpath:images/view_sql.svg");        
        public static final SqmlEditorIcons IMG_PARAM_VAL_COUNT = new SqmlEditorIcons("classpath:images/param_valcount.svg");
        public static final SqmlEditorIcons IMG_ADD_PARAM_VAL_CAOUNT = new SqmlEditorIcons("classpath:images/add_param_valcount.svg");
        public static final SqmlEditorIcons IMG_INSERT_IF_TAG = new SqmlEditorIcons("classpath:images/if.svg");
        public static final SqmlEditorIcons IMG_INSERT_ELSE_TAG = new SqmlEditorIcons("classpath:images/if_else.svg");
        public static final SqmlEditorIcons IMG_INSERT_END_TAG = new SqmlEditorIcons("classpath:images/if_end.svg");
        public static final SqmlEditorIcons IMG_CUSTOM_PARAMETER = new SqmlEditorIcons("classpath:images/custom_parameter.svg");
        public static final SqmlEditorIcons IMG_EXPORT = new SqmlEditorIcons("classpath:images/export_r.svg");
        public static final SqmlEditorIcons IMG_IMPORT = new SqmlEditorIcons("classpath:images/import_r.svg");
        private SqmlEditorIcons(final String fileName) {
            super(fileName, true);
        }
    }

    public SqmlEditor(final IClientEnvironment environment, final QWidget parent, final Id contextId) {
        this(environment, parent, contextId, null);
    }

    /**
     * @param parent родительский виджет (может быть null)
     * @param contextClass класс, в контексте которого происходит
     * редактирование sqml-выражения.
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public SqmlEditor(final IClientEnvironment environment, final QWidget parent, final Id contextId, final ISqmlParameters param) {
        super(environment, parent);
        isSqmlAccessible = environment.getApplication().isSqmlAccessible();
        contextClassId = contextId;
        if (isSqmlAccessible && contextId != null) {
            this.contextClass = environment.getSqmlDefinitions().findTableById(contextId);
        }else{
            contextClass = null;
        }
        //this.contextId = contextId;
        ((Application) environment.getApplication()).getActions().settingsChanged.connect(this, "updateEditorHightlight()");

        converter = new SqmlProcessor(environment, contextClass, param);
        tagInsertion = new SqmlTagInsertion(this);
        
        converter.setSqmlTagInsertion(tagInsertion);
        
        if (isSqmlAccessible){
            editText = new InternalXscmlEditor(environment, converter, this);            
            editText.textChanged.connect(this, "textChange()");
        }else{
            editText = new DummyXscmlEditor(parent, converter);            
        }
        editText.setObjectName("xscmleditor");
        editText.setIndent(5);
        ui = new SqmlEditor_UI(this, param);
        
        setSizePolicy(QSizePolicy.Policy.MinimumExpanding, QSizePolicy.Policy.MinimumExpanding);
        hightlighter = new Highlighter(environment, editText, converter, "org.radixware.explorer/S_E/SYNTAX_SQML/");
        open();
        modified = false;
        if (!isSqmlAccessible){
            readOnly = true;
            ui.setReadOnlyMode(true);
        }
    }
    
    public void setContextClass(final Id contextId) {
        if (!Objects.equals(contextId, contextClassId)){
            contextClassId = contextId;
            if (isSqmlAccessible && contextId != null) {
                this.contextClass = getEnvironment().getSqmlDefinitions().findTableById(contextId);
            }else{
                contextClass = null;
            }
            converter.setContextClass(contextClass);
            ui.contextClassChanged(contextClass);
            ui.updatePropTree();
            open();
        }
    }
    
    public final void open() {
        this.setFocusPolicy(FocusPolicy.StrongFocus);
        if (sqml == null) {
            sqml = Sqml.Factory.newInstance();
        }
        ui.clearAliases();
        converter.open(null);
        converter.setSqml(sqml);
        if (editText instanceof XscmlEditor){
            ((XscmlEditor)editText).open(null);
        }else{
            updateSqlText();
        }
    }
    
    private void updateSqlText(){
        final String plainText;                
        if (sqml==null || sqml.getItemList()==null || sqml.getItemList().isEmpty()){
            plainText = "";            
        }else{
            final TagProcessor tagProcessor = editText==null ? null : editText.getTagConverter();
            final EDefinitionDisplayMode displayMode;
            displayMode = tagProcessor==null ? EDefinitionDisplayMode.SHOW_SHORT_NAMES : tagProcessor.getShowMode();
            if (contextClass==null){       
                plainText = 
                    SqmlTranslator.translate(getEnvironment(), sqml, null, contextClassId, getParamters(), null, displayMode);
            }else{
                plainText = 
                    SqmlTranslator.translate(getEnvironment(), sqml, contextClass, getParamters(), null, displayMode);
            }
        }
        editText.setPlainText(plainText);
    }
    
    protected final void appendAdditionalTables(){//append tables that we can find in addintional from condition
        final List<Sqml.Item> items = additionalFrom==null ? null : additionalFrom.getItemList();
        if (items!=null && !items.isEmpty()){
            final Map<String,Id> tableAliases = new HashMap<>();
            final Set<Id> additionalTables = new HashSet<>();
            for (Sqml.Item item: items){
                final Sqml.Item.TableSqlName tableTag = item.getTableSqlName();
                final Id tableId = tableTag==null ? null : tableTag.getTableId();
                if (tableId!=null){
                    if (tableTag.getTableAlias()==null || tableTag.getTableAlias().isEmpty()){
                        additionalTables.add(tableId);
                    }else{
                        tableAliases.put(tableTag.getTableAlias(), tableId);
                    }
                }
            }
            if (!additionalTables.isEmpty()){
                ui.addTables(additionalTables);
            }
            if (!tableAliases.isEmpty()){
                ui.addAliases(tableAliases);
            }
        }        
    }

    public void sortPropTree() {
        if (ui != null) {
            ui.sortPropTree();
        }
    }

    public void setAliases(final Map<String, Id> aliases) {
        ui.addAliases(aliases);
    }

    /**
     * Задать sqml-выражение.
     * Заменят текущее выражение редактора, выражением переданным в аргументе
     * @param sqml sqml-выражение
     */
    public void setSqml(final org.radixware.schemas.xscml.Sqml sqml) {
        setSqml(sqml,null,null);
    }

    public void setSqml(final org.radixware.schemas.xscml.Sqml sqml, final ISqmlParameters parameters) {
        setSqml(sqml,null,parameters);
    }
    
    public void setSqml(final org.radixware.schemas.xscml.Sqml sqml, 
                                 final org.radixware.schemas.xscml.Sqml additionalFrom,  
                                 final ISqmlParameters parameters) {
        if (editText == null) {
            return;
        }
        this.sqml = sqml==null ? null : (org.radixware.schemas.xscml.Sqml)sqml.copy();
        this.additionalFrom = additionalFrom==null ? null : (org.radixware.schemas.xscml.Sqml)additionalFrom.copy();
        if (parameters!=null){
            converter.setParameters(parameters);
            ui.setParameters(parameters);
        }
        open();
        modified = false;
    }
    
    @SuppressWarnings("unused")
    private void updateEditorHightlight() {
        if (hightlighter != null) {
            editText.blockSignals(true);
            hightlighter.updateSettings();
            if (editText instanceof XscmlEditor){
                ((XscmlEditor)editText).updateTagsFormat();
            }
            editText.blockSignals(false);
        }
    }

    public void updateTagsName() {
        if (editText instanceof XscmlEditor){
            ((XscmlEditor)editText).updateTagsName();
        }
    }

    /**
     * Задать sqml-выражение.
     * Заменеят текущее выражение редактора, выражением переданным в аргументе
     * @param sqml sqml-выражение
     */
    public void setSqml(final SqmlExpression sqml) {
        setSqml(sqml != null ? sqml.asXsqml() : null);
    }

    /**
     * Разрешить/запретить редактирование sqml
     * Если редактирование запрещено
     * @param flag false если можно редактировать
     */
    public void setReadonly(final boolean flag) {
        if (isSqmlAccessible){
            readOnly = flag;
            ui.setReadOnlyMode(flag);
        }
    }

    /**
     * Возвращает текущее sqml-выражение
     * @return
     */
    public org.radixware.schemas.xscml.Sqml getSqml() {
        if (modified){
            sqml = converter.toXml(editText.toPlainText(), editText.textCursor());
        }
        return sqml==null ? null : (org.radixware.schemas.xscml.Sqml)sqml.copy();
    }
    
    public org.radixware.schemas.xscml.Sqml getAdditionalFrom(){
        return additionalFrom==null ? null : (org.radixware.schemas.xscml.Sqml)additionalFrom.copy();
    }

    /**
     * Возвращает true если редактор находится в режиме "только чтение"
     * @return
     */
    public boolean isReadonly() {
        return readOnly;
    }

    /**
     * Возвращает true если sqml-выражение изменялось пользователем.
     * @return
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * Очистить sqml-выражение
     */
    public void clear() {
        final int index = converter.hystoryIndex;
        converter.tagListHystory.get(index).clear();
        editText.clear();
        ui.clearAliases();
        modified = false;
        sqml = null;
    }

    @SuppressWarnings("unused")
    private void textChange() {
        if (hightlighter != null) {
            editText.setUpdatesEnabled(false);
            hightlighter.rehighlight();
            editText.setUpdatesEnabled(true);
        }
        modified = true;
    }    

    @Override
    protected void showEvent(final QShowEvent event) {
        super.showEvent(event);
        if (!wasShown) {
            ui.showEvent(event);
        }
        wasShown = true;
    }

    public AbstractXscmlEditor getTextEditor() {
        return editText;
    }

    public ISqmlTableDef getContextClassDef() {
        return contextClass;
    }

    public SqmlProcessor getSqmlProcessor() {
        return converter;
    }

    public void addToolButton(final QToolButton toolBtn) {
        ui.addToolButton(toolBtn);
    }

    public void insertToolButton(final QToolButton toolBtn) {
        ui.insertToolButton(toolBtn);
    }
    
    public void setImportAccessible(final boolean isAccessible){
        ui.setImportAccessible(isAccessible);
    }
    
    public boolean isImportAccessible(){
        return ui.isImportAccessible();
    }
    
    public void setExportAccessible(final boolean isAccessible){
        ui.setExportAccessible(isAccessible);
    }
    
    public boolean isExportAccessible(){
        return ui.isExportAccessible();
    }    

    public SqmlTagInsertion getSqmlTagInsertion() {
        return tagInsertion;
    }
    
    public void setTranslateButtonEnabled(final boolean isEnabled){
        ui.setTranslateButtonEnabled(isEnabled);
    }
    
    public boolean isTranslateSqmlEnabled(){
        return ui.isTranslateSqmlEnabled();
    }
    
    public ISqmlParameters getParamters(){
        return converter.getParameters();
    }
}
