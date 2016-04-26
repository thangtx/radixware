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

package org.radixware.kernel.explorer.editors.xscmleditor;

import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QTextCharFormat;
import com.trolltech.qt.gui.QTextCursor;
import com.trolltech.qt.gui.QWidget;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ITaskWaiter;
import org.radixware.kernel.common.client.env.ReleaseRepository;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.sqml.*;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.types.Restrictions;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.ChooseSqmlDefinitionDialog;
import org.radixware.kernel.explorer.editors.sqmleditor.tageditors.EventCodeChoiceDialog;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.models.SqmlTreeModel;


public abstract class TagProcessor {

    public int hystoryIndex;//
    public List<List<TagInfo>> tagListHystory = new ArrayList<>();
    protected EDefinitionDisplayMode showMode;
    protected Scml sourceCode;
    protected final IClientEnvironment environment;

    static public enum TagType {

        Id, DataType, Literal, Task, EventCode, Nls
    }

    public TagProcessor(final IClientEnvironment environment) {
        this.environment = environment;
        showMode = EDefinitionDisplayMode.SHOW_FULL_NAMES;
    }

    public void open(Scml javaSrc) {
        this.sourceCode = javaSrc;
        tagListHystory.clear();
        hystoryIndex = 0;
    }

    public Scml getSource() {
        return sourceCode;
    }

    public List<TagInfo> getCurrentTagList() {
        return tagListHystory == null || hystoryIndex < 0 || hystoryIndex >= tagListHystory.size() ? Collections.<TagInfo>emptyList() : tagListHystory.get(hystoryIndex);
    }

    /**
     * ???????? ??? ? ?????? ?????
     *
     * @param newTag - ??????????? ???
     */
    public int insertTagToTagList(final TagInfo newTag) {
        List<TagInfo> tagList = getCurrentTagList();
        for (int i = 0; i < tagList.size(); i++) {
            if (newTag.getStartPos() <= tagList.get(i).getStartPos()) {
                tagList.add(i, newTag);
                return i;
            }
        }
        tagList.add(newTag);
        return tagList.size();
    }
    
    public void insertTagToTagList(final TagInfo newTag, int tagPos) {
        getCurrentTagList().add(tagPos, newTag);
    }

    /**
     * ???????? ????????? ????? ? ?????? ?????
     *
     * @param newTags - ?????? ??????????? ?????
     * @param pos - ??????? ??????? ? ?????????
     */
    protected void insertTagsToTagList(final List<TagInfo> newTags, final int pos) {
        final List<TagInfo> tagList = getCurrentTagList();
        for (int i = 0; i < tagList.size(); i++) {
            if (pos <= tagList.get(i).getStartPos()) {
                tagList.addAll(i, newTags);
                return;
            }
        }
        tagList.addAll(newTags);
    }

    /**
     * ??????? ????????? ????? ?? ?????? ?????
     *
     * @param deletingTagList - ?????? ????????? ?????
     */
    public void deleteTagList(final List<TagInfo> deletingTagList) {
        final List<TagInfo> tagList = getCurrentTagList();
        for (int j = 0; j < deletingTagList.size(); j++) {
            for (int i = 0; i < tagList.size(); i++) {
                if (deletingTagList.get(j).equals(tagList.get(i))) {
                    tagList.remove(i);
                    break;
                }
            }
        }
    }

    /**
     * ??????? ??? ?? ?????? ?????
     *
     * @param deletingTag - ????????? ???
     * @return
     */
    public boolean deleteTag(final TagInfo deletingTag) {
        List<TagInfo> tagList = getCurrentTagList();
        return tagList.remove(deletingTag);
    }

    /**
     * ????????????? ???????? ????? ?? xml ? ????? ???????????? ? ?????????
     *
     * @param tc - ?????? ?????????
     * @param charFormat -
     */
    public abstract void toHtml(final QTextCursor tc, final QTextCharFormat charFormat);

    /**
     * ????????????? ???????? ????? ? xml
     *
     * @param plaintText - ????????? ????????????? ????????? ??????
     * @param tc - ?????? ?????????
     * @return ???????? ????? ? ???? xml
     */
    //public abstract Scml toXml( String plaintText, QTextCursor tc);
    /**
     * ??????? ??? (??? Jml-?????????)
     *
     * @param c
     * @param pos - ??????? ??????? ? ?????????
     * @return ????????? ???
     */
    public TagInfo createCompletionTag(final Scml.Item newItem, final int pos) {
        return null;
    }

    /**
     * ???????? ??????? ?????
     *
     * @param plaintText- ???????? ?????
     * @param tc - ?????? ?????????
     * @param isUndoRedo
     */
    public abstract void updateTagsPos(final String plaintText, final QTextCursor tc, final boolean isUndoRedo);

    protected void addHystory(final boolean isUndoRedo) {
        /*List<TagInfo> tagList=new ArrayList<TagInfo>();
         for(int i=0;i<tagListHystory.get(hystoryIndex).size();i++){
         tagList.add(tagListHystory.get(hystoryIndex).get(i).copy());
         }*/
        if (!isUndoRedo) {
            if (hystoryIndex < tagListHystory.size() - 1) {
                while (tagListHystory.size() > hystoryIndex + 1) {
                    tagListHystory.remove(hystoryIndex + 1);
                }
            }
        }
        //this.tagListHystory.add(hystoryIndex,tagList);
        //hystoryIndex=hystoryIndex+1;
    }

    /**
     * ???????? ??? ????
     *
     * @param tag - ???
     * @return true ???? ??? ???? ????????
     */
    public boolean changeTagName(final TagInfo tag) {
        return tag.setDisplayedInfo(showMode);
    }

    /**
     * ????? ???
     *
     * @param pos- ??????? ??????? ?????????
     * @param isDelete true ???? ??? ??????? ??? ????????
     * @return ???
     */
    public TagInfo getTagInfoForCursorMove(final int pos, final boolean isDelete) {
        if ((tagListHystory != null) && (tagListHystory.size() > hystoryIndex)) {
            List<TagInfo> tagList = getCurrentTagList();
            if (tagList == null) {
                return null;
            }
            for (int i = 0; i < tagList.size(); i++) {
                if (isDelete) {
                    if ((pos <= tagList.get(i).getEndPos()) && (pos >= tagList.get(i).getStartPos())) {
                        return tagList.get(i);
                    }
                } else {
                    if ((pos < tagList.get(i).getEndPos()) && (pos > tagList.get(i).getStartPos())) {
                        return tagList.get(i);
                    }
                    if (pos == tagList.get(i).getStartPos()) {
                        return tagList.get(i);
                    }

                }
            }
        }
        return null;
    }

    public TagInfo getTagInfoForCursorMove(final int pos, final boolean isDelete, final boolean isLeftMove) {
        if ((tagListHystory != null) && (tagListHystory.size() >= hystoryIndex)) {
            List<TagInfo> tagList = getCurrentTagList();
            if (tagList == null) {
                return null;
            }
            for (int i = 0; i < tagList.size(); i++) {
                if (isDelete) {
                    if (isLeftMove) {
                        if ((pos <= tagList.get(i).getEndPos()) && (pos > tagList.get(i).getStartPos())) {
                            return tagList.get(i);
                        }
                    } else {
                        if ((pos < tagList.get(i).getEndPos()) && (pos >= tagList.get(i).getStartPos())) {
                            return tagList.get(i);
                        }
                    }
                } else {
                    if ((pos < tagList.get(i).getEndPos()) && (pos > tagList.get(i).getStartPos())) {
                        return tagList.get(i);
                    }
                }
            }
        }
        return null;
    }

    /**
     * ???????? ?????? ????? ???????? ? ????????? ?????
     *
     * @param startSelection - ????????? ??????? ?????????
     * @param endSelection - ???????? ??????? ?????????
     * @param forDelet - true ???? ?????? ??????? ??? ???????? ??????
     * @return
     */
    public List<TagInfo> getTagListInSelection(final int startSelection, final int endSelection, final boolean forDelet) {
        final List<TagInfo> tagList = getCurrentTagList();
        final List<TagInfo> res = new ArrayList<>();
        for (int i = 0; i < tagList.size(); i++) {
            if (((tagList.get(i).getStartPos() <= startSelection) && (endSelection <= tagList.get(i).getEndPos()))
                    || ((startSelection <= tagList.get(i).getStartPos()) && (tagList.get(i).getEndPos() <= endSelection))
                    || ((startSelection <= tagList.get(i).getStartPos()) && (endSelection <= tagList.get(i).getEndPos()) && (tagList.get(i).getStartPos() < endSelection))
                    || ((tagList.get(i).getStartPos() <= startSelection) && (tagList.get(i).getEndPos() <= endSelection) && (startSelection < tagList.get(i).getEndPos()))) {
                if (forDelet) {
                    res.add(tagList.get(i));
                } else {
                    res.add(tagList.get(i).copyWithScmlItem());
                }
            }
        }
        return res;
    }

    protected int getInsertTagIndex(final List<TagInfo> tagList, final int pos) {
        for (int i = 0; i < tagList.size(); i++) {
            if (tagList.get(i).getStartPos() >= pos) {
                return i;
            }
        }
        return tagList.size();
    }

    public abstract String getStrFromXml(final String plainText, final List<TagInfo> copyTags, final QTextCursor tc);

    public void clearHystory() {
        final List<TagInfo> tagList = new ArrayList<>();
        final List<TagInfo> currentTagList = getCurrentTagList();
        for (TagInfo info : currentTagList) {
            tagList.add(info.copy());
        }
        tagListHystory.clear();
        tagListHystory.add(tagList);
        hystoryIndex = tagListHystory.size() - 1;
    }

    public EDefinitionDisplayMode getShowMode() {
        return showMode;
    }

    public void setShowMode(final EDefinitionDisplayMode showMode) {
        this.showMode = showMode;
    }

    //TableSqmlName            
    public ISqmlTableDef chooseSqmlTable(final ISqmlTableDef currentTable, final boolean isReadOnly, final QWidget parent, final boolean filterByRestrictions) {
        final SqmlTreeModel sqmlModel;
        if (filterByRestrictions) {
            Collection<ISqmlTableDef> tables = environment.getSqmlDefinitions().getTables();
            sqmlModel = new SqmlTreeModel(environment, filterByRestriction(tables), EnumSet.of(SqmlTreeModel.ItemType.MODULE_INFO));
        } else {
            sqmlModel = new SqmlTreeModel(environment, null, EnumSet.of(SqmlTreeModel.ItemType.MODULE_INFO));
        }
        sqmlModel.setMarkDeprecatedItems(true);
        sqmlModel.setDisplayMode(showMode);
        final List<ISqmlDefinition> currentItemPath = Collections.<ISqmlDefinition>singletonList(currentTable);
        final ChooseSqmlDefinitionDialog choiceObj = new ChooseSqmlDefinitionDialog(environment, sqmlModel, currentItemPath, isReadOnly, parent);
        final String dialogTitle = isReadOnly ? environment.getMessageProvider().translate("SqmlEditor", "Table")
                : environment.getMessageProvider().translate("SqmlEditor", "Select Table");
        choiceObj.setWindowTitle(dialogTitle);
        choiceObj.setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.TABLE));
        if (choiceObj.exec() == QDialog.DialogCode.Accepted.value()) {
            return (ISqmlTableDef) choiceObj.getCurrentItem();
        }
        return null;
    }

    public ISqmlColumnDef chooseSqmlColumn(final ISqmlTableDef currentTable, final ISqmlColumnDef currentColumn, final boolean isReadOnly, final QWidget parent) {
        final EnumSet<SqmlTreeModel.ItemType> itemTypes = EnumSet.of(SqmlTreeModel.ItemType.PROPERTY, SqmlTreeModel.ItemType.MODULE_INFO);
        final SqmlTreeModel sqmlModel = new SqmlTreeModel(environment, null, itemTypes);
        sqmlModel.setMarkDeprecatedItems(true);
        sqmlModel.setDisplayMode(showMode);
        final List<ISqmlDefinition> currentItemPath = Arrays.asList(currentTable, currentColumn);
        final ChooseSqmlDefinitionDialog choiceObj = new ChooseSqmlDefinitionDialog(environment, sqmlModel, currentItemPath, isReadOnly, parent);
        final String dialogTitle = isReadOnly ? environment.getMessageProvider().translate("SqmlEditor", "Column")
                : environment.getMessageProvider().translate("SqmlEditor", "Select Column");
        choiceObj.setWindowTitle(dialogTitle);
        choiceObj.setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.PROPERTY));
        if (choiceObj.exec() == QDialog.DialogCode.Accepted.value()) {
            return (ISqmlColumnDef) choiceObj.getCurrentItem();
        }
        return null;        
    }
    
    public ISqmlColumnDef chooseSqmlColumn( final ISqmlTableDef currentTable, 
                                            final ISqmlColumnDef currentColumn, 
                                            final boolean isReadOnly, 
                                            final boolean includePropObject,
                                            final QWidget parent) {
        if (includePropObject){
            final EnumSet<SqmlTreeModel.ItemType> itemTypes = 
                EnumSet.of(SqmlTreeModel.ItemType.PROPERTY, SqmlTreeModel.ItemType.PROPERTY_OBJECT, SqmlTreeModel.ItemType.MODULE_INFO);
            final String dialogTitle = isReadOnly
                ? environment.getMessageProvider().translate("SqmlEditor", "Property")
                : environment.getMessageProvider().translate("SqmlEditor", "Select Property");                  
            return (ISqmlColumnDef)chooseAdsDefinition(isReadOnly, 
                                                       itemTypes, 
                                                       dialogTitle, 
                                                       ExplorerIcon.getQIcon(ClientIcon.Definitions.COLUMN),
                                                       parent);
        }else{
            return chooseSqmlColumn(currentTable, currentColumn, isReadOnly, parent);
        }
    }
    
    

    public ISqmlDefinition chooseDbFunc(final ISqmlTableDef currentTable, final boolean isReadOnly, final QWidget parent, final boolean filterByRestrictions) {
        final SqmlTreeModel model = new SqmlTreeModel(environment,
                new ArrayList<ISqmlDefinition>(environment.getSqmlDefinitions().getPackages()),
                EnumSet.of(SqmlTreeModel.ItemType.FUNCTION));
        model.setMarkDeprecatedItems(true);
        model.setDisplayMode(showMode);
        ISqmlDefinitionsFilter filter = new ISqmlDefinitionsFilter() {
            @Override
            public boolean isAccepted(ISqmlDefinition definition, ISqmlDefinition ownerDefinition) {
                return true;//((ISqmlFunctionDef)definition).isWNDS();
            }
        };
        final ChooseSqmlDefinitionDialog dialog = new ChooseSqmlDefinitionDialog(environment, model, filter, null, false, parent);
        final String dialogTitle = isReadOnly ? environment.getMessageProvider().translate("SqmlEditor", "Function")
                : environment.getMessageProvider().translate("SqmlEditor", "Select Function");
        dialog.setWindowTitle(dialogTitle);
        dialog.setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.SQL_FUNCTION));
        if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
            return dialog.getCurrentItem();
        }
        return null;
    }

    public ISqmlTableIndexDef chooseSqmlIndex(final ISqmlTableDef currentTable, final ISqmlTableIndexDef currentIndex, final boolean isReadOnly, final QWidget parent) {
        final SqmlTreeModel sqmlModel = new SqmlTreeModel(environment, null, EnumSet.of(SqmlTreeModel.ItemType.INDEX, SqmlTreeModel.ItemType.MODULE_INFO));
        sqmlModel.setMarkDeprecatedItems(true);
        sqmlModel.setDisplayMode(showMode);
        final List<ISqmlDefinition> currentItemPath = Arrays.asList(currentTable, currentIndex);
        final ChooseSqmlDefinitionDialog choiceObj = new ChooseSqmlDefinitionDialog(environment, sqmlModel, currentItemPath, isReadOnly, parent);
        final String dialogTitle = isReadOnly ? environment.getMessageProvider().translate("SqmlEditor", "Table Index")
                : environment.getMessageProvider().translate("SqmlEditor", "Select Table Index");
        choiceObj.setWindowTitle(dialogTitle);
        //choiceObj.setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.));
        if (choiceObj.exec() == QDialog.DialogCode.Accepted.value()) {
            return (ISqmlTableIndexDef) choiceObj.getCurrentItem();
        }
        return null;
    }

    public ISqmlDefinition chooseDomainId(final boolean isReadOnly, final QWidget parent) {
        final List<ISqmlDefinition> domains = new ArrayList<>();
        domains.addAll(environment.getSqmlDefinitions().getDomains());
        final SqmlTreeModel treeModel = new SqmlTreeModel(environment, domains);
        treeModel.setMarkDeprecatedItems(true);
        //treeModel.setDisplayMode(displayMode);
        ChooseSqmlDefinitionDialog dialog = new ChooseSqmlDefinitionDialog(environment, treeModel, null, false, parent);
        final String dialogTitle = isReadOnly
                ? environment.getMessageProvider().translate("SqmlEditor", "Domain")
                : environment.getMessageProvider().translate("SqmlEditor", "Select Domain");
        dialog.setWindowTitle(dialogTitle);
        dialog.setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.DOMAIN));
        if (dialog.execDialog() == IDialog.DialogResult.ACCEPTED) {
            return dialog.getCurrentItem();
        }
        return null;
    }
    
    public ISqmlDefinition chooseEnumId(final boolean isReadOnly, final QWidget parent) {
        final List<ISqmlDefinition> enums = new ArrayList<>();
        enums.addAll(environment.getSqmlDefinitions().getEnums());
        final SqmlTreeModel treeModel = new SqmlTreeModel(environment, enums, EnumSet.of( SqmlTreeModel.ItemType.MODULE_INFO));
        
        treeModel.setMarkDeprecatedItems(true);
        //treeModel.setDisplayMode(displayMode);
        ChooseSqmlDefinitionDialog dialog = new ChooseSqmlDefinitionDialog(environment, treeModel, null, false, parent);
        final String dialogTitle = isReadOnly
                ? environment.getMessageProvider().translate("SqmlEditor", "Enumeration")
                : environment.getMessageProvider().translate("SqmlEditor", "Select Enumeration");
        dialog.setWindowTitle(dialogTitle);
        dialog.setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.CONSTSET));
        if (dialog.execDialog() == IDialog.DialogResult.ACCEPTED) {
            return dialog.getCurrentItem();
        }
        return null;
    }

    public ISqmlDefinition chooseClassId(final boolean isReadOnly, final QWidget parent) {
        final String dialogTitle = isReadOnly
                ? environment.getMessageProvider().translate("SqmlEditor", "Class")
                : environment.getMessageProvider().translate("SqmlEditor", "Select Class");        
        return chooseAdsDefinition(isReadOnly, 
                                   EnumSet.of(SqmlTreeModel.ItemType.MODULE_INFO), 
                                   dialogTitle, 
                                   ExplorerIcon.getQIcon(ClientIcon.Definitions.CLASS),
                                   parent);
    }
    
    private ISqmlDefinition chooseAdsDefinition(final boolean isReadOnly, 
                                                final EnumSet<SqmlTreeModel.ItemType> itemTypes, 
                                                final String dialogTitle, 
                                                final QIcon dialogIcon,
                                                final QWidget parent){
        final Collection<ReleaseRepository.DefinitionInfo> defs = environment.getDefManager().getRepository().getDefinitions(EDefType.CLASS);
        final ITaskWaiter taskWaiter = environment.getApplication().newTaskWaiter();
        try {
            final ISqmlDefinition[] classes = taskWaiter.runAndWait(new Callable<ISqmlDefinition[]>() {
                @Override
                public ISqmlDefinition[] call() throws Exception {
                    return defInfosToSqmlDef(defs);
                }
            });
            final List<ISqmlDefinition> sqmls = Arrays.asList(classes);
            final SqmlTreeModel sqmlModel = new SqmlTreeModel(environment, sqmls, itemTypes);
            sqmlModel.setMarkDeprecatedItems(true);
            sqmlModel.setDisplayMode(showMode);
            final ChooseSqmlDefinitionDialog dlg = new ChooseSqmlDefinitionDialog(environment, sqmlModel, null, isReadOnly, parent);
            dlg.setWindowTitle(dialogTitle);
            dlg.setWindowIcon(dialogIcon);
            if (dlg.execDialog() == IDialog.DialogResult.ACCEPTED) {
                return dlg.getCurrentItem();
            }
        } catch (ExecutionException | InterruptedException ex) {
            environment.messageError(environment.getMessageProvider().translate("SqmlEditor", "Couldn't read classes"));
        } finally {
            taskWaiter.close();
        }
        return null;
    }

    private ISqmlDefinition[] defInfosToSqmlDef(final Collection<ReleaseRepository.DefinitionInfo> defs) {
        final List<ISqmlDefinition> list = new LinkedList<>();
        final ISqmlDefinitions sqmls = environment.getSqmlDefinitions();
        for (ReleaseRepository.DefinitionInfo i : defs) {
            EDefinitionIdPrefix prefix = i.id.getPrefix();
            if (prefix == EDefinitionIdPrefix.ADS_ENTITY_CLASS || prefix == EDefinitionIdPrefix.ADS_APPLICATION_CLASS) {
                final ISqmlDefinition foundDef = sqmls.findTableById(i.id);
                if (foundDef != null) {
                    list.add(foundDef);
                }
            }
        }
        return list.<ISqmlDefinition>toArray(new ISqmlDefinition[0]);
    }

    public ISqmlDefinition chooseEventCode(final boolean isReadOnly, final QWidget parent) {
        Collection<ISqmlEventCodeDef> eventCodesDefs = environment.getSqmlDefinitions().getEventCodes();
        final EventCodeChoiceDialog dlg = new EventCodeChoiceDialog(environment, parent, eventCodesDefs);
        if (dlg.execDialog() == IDialog.DialogResult.ACCEPTED) {
            return dlg.getSelectedEventCode();
        }
        return null;
    }

    private List<ISqmlDefinition> filterByRestriction(final Collection<ISqmlTableDef> tables) {
        final List<ISqmlDefinition> res = new ArrayList<>();
        for (ISqmlTableDef table : tables) {
            if (table.hasEntityClass()) {
                Id id = ((ISqmlDefinition) table).getId();
                RadClassPresentationDef classDef = environment.getApplication().getDefManager().getClassPresentationDef(id);
                RadSelectorPresentationDef selPresDef = classDef.getDefaultSelectorPresentation();
                if (selPresDef != null) {
                    Restrictions selectorRestrictions = selPresDef.getRestrictions();
                    if (!selectorRestrictions.getIsContextlessUsageRestricted()) {
                        GroupModel groupModel = GroupModel.openTableContextlessSelectorModel(environment, selPresDef);
                        if (groupModel != null) {
                            res.add(table);
                        }
                    }
                }
            }
        }
        return res;

    }
}
