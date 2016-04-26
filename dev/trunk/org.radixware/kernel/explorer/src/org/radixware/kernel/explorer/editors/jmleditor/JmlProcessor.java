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

package org.radixware.kernel.explorer.editors.jmleditor;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QTextCharFormat;
import com.trolltech.qt.gui.QTextCharFormat.UnderlineStyle;
import com.trolltech.qt.gui.QTextCursor;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.type.AdsDefinitionType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.Jml.Tag;
import org.radixware.kernel.common.jml.JmlTagDbEntity;
import org.radixware.kernel.common.jml.JmlTagDbName;
import org.radixware.kernel.common.jml.JmlTagId;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.jml.JmlTagLocalizedString;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Item;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.jmleditor.jmltags.JmlTag;
import org.radixware.kernel.explorer.editors.jmleditor.jmltags.JmlTag_DbEntity;
import org.radixware.kernel.explorer.editors.jmleditor.jmltags.JmlTag_DbName;
import org.radixware.kernel.explorer.editors.jmleditor.jmltags.JmlTag_Id;
import org.radixware.kernel.explorer.editors.jmleditor.jmltags.JmlTag_Invocate;
import org.radixware.kernel.explorer.editors.jmleditor.jmltags.JmlTag_LocalizedString;
import org.radixware.kernel.explorer.editors.jmleditor.jmltags.JmlTag_Type;
import org.radixware.kernel.explorer.editors.xscmleditor.TagInfo;
import org.radixware.kernel.explorer.editors.xscmleditor.TagProcessor;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;

import org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument;
import org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument.AdsUserFuncDefinition;
import org.radixware.schemas.adsdef.LocalizedString;
import org.radixware.schemas.adsdef.LocalizingBundleDefinition;
import org.radixware.schemas.xscml.JmlType;


public class JmlProcessor extends TagProcessor {

    private Map<Scml.Tag, TagInfo> tagMap;
    private final JmlEditor editor;
    private final JmlTag_DbEntity.IDbEntityTitleProvider titleProvider = new JmlTag_DbEntity.IDbEntityTitleProvider() {
        @Override
        public String getTitle(Id tableId, String pidAsStr) {
            return editor.getActionProvider().getObjectFromOptimizerCache(tableId.toString() + "~" + pidAsStr);
        }
    };

    public JmlProcessor(final JmlEditor editor) {
        super(editor.getEnvironment());
        //this.environment = editor.getEnvironment();
        this.editor = editor;
        String settingsKey = SettingNames.SYSTEM + "/" + "user_func_editor_showModuleName";
        int i = environment.getConfigStore().readInteger(settingsKey, 1);
        if (i == EDefinitionDisplayMode.SHOW_SHORT_NAMES.ordinal()) {
            this.showMode = EDefinitionDisplayMode.SHOW_SHORT_NAMES;
        } else {
            this.showMode = EDefinitionDisplayMode.SHOW_FULL_NAMES;
        }
    }

    @Override
    public void open(final Scml javaSrc) {
        super.open(javaSrc);
        tagMap = new HashMap<>();

    }

    @Override
    public TagInfo createCompletionTag(final Scml.Item newItem, final int pos) {
        Definition context = newItem.getOwnerScml().getOwnerDefinition();
        if (newItem instanceof JmlTagInvocation) {
            JmlTagInvocation tagInvocation = (JmlTagInvocation) newItem;
            Definition def = tagInvocation.resolve(context);
            boolean isDeprecated = def != null ? def.isDeprecated() : false;
            return new JmlTag_Invocate(environment, tagInvocation, pos, isDeprecated, showMode);
        } else if (newItem instanceof JmlTagId) {
            JmlTagId tagId = (JmlTagId) newItem;
            Definition def = tagId.resolve(context);
            boolean isDeprecated = def != null ? def.isDeprecated() : false;
            return new JmlTag_Id(environment, tagId, pos, isDeprecated, showMode);
        } else if (newItem instanceof JmlTagTypeDeclaration) {
            boolean isDeprecated = isTypeDeprecated((JmlTagTypeDeclaration) newItem);
            return new JmlTag_Type(environment, (JmlTagTypeDeclaration) newItem, pos, isDeprecated, showMode);
        }
        return null;
    }

    public TagInfo createTagId(final AdsDefinition newItem, final int pos, final RadixObjects<Item> items/*,boolean invocate,DefInfoType newItem, int tagType*/) {
        JmlTagId tag = new JmlTagId(newItem);
        return createTagId(tag, pos, newItem.isDeprecated(), items);
    }

    public TagInfo createTagId(final Id[] ids, final int pos, final boolean isDeprecated, final RadixObjects<Item> items/*,boolean invocate,DefInfoType newItem, int tagType*/) {
        JmlTagId tag = new JmlTagId(ids);
        return createTagId(tag, pos, isDeprecated, items);
    }

    private TagInfo createTagId(final JmlTagId tag, final int pos, final boolean isDeprecated, final RadixObjects<Item> items) {
        items.add(tag);
        return new JmlTag_Id(environment, tag, pos, isDeprecated, showMode);
    }

    public TagInfo createTagLocalizedStr(final JmlTagLocalizedString tag, final int pos, final RadixObjects<Item> items) {
        items.add(tag);
        return new JmlTag_LocalizedString(environment, tag, pos, showMode);
    }

    public TagInfo createTagInvocate(final AdsDefinition newItem, final int pos, final RadixObjects<Item> items/*,boolean invocate,DefInfoType newItem, int tagType*/) {
        final JmlTagInvocation tag = JmlTagInvocation.Factory.newInstance(newItem);
        items.add(tag);
        return new JmlTag_Invocate(environment, tag, pos, newItem.isDeprecated(), showMode);
    }

    public TagInfo createTagType(final RadixObject newItem, final int pos, final RadixObjects<Item> items, final EValType valType/*,boolean invocate,DefInfoType newItem, int tagType*/) {
        JmlTagTypeDeclaration tag;
        if (newItem instanceof AdsTypeDeclaration) {
            tag = new JmlTagTypeDeclaration((AdsTypeDeclaration) newItem);
        } else if (valType == null) {
            tag = new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance((IAdsTypeSource) newItem));
        } else {
            tag = new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance(valType, (IAdsTypeSource) newItem, null, 0));
        }

        items.add(tag);
        final boolean isDeprecated = (newItem instanceof Definition) ? ((Definition) newItem).isDeprecated() : false;
        return new JmlTag_Type(environment, tag, pos, isDeprecated, showMode);
    }

    public TagInfo createTagDbName(final ISqmlDefinition newItem, final int pos, final boolean isDeprecated, final RadixObjects<Item> items) {
        final JmlType.Item.IdReference refId = JmlType.Item.IdReference.Factory.newInstance();
        refId.setDbName(true);
        final List<Id> list = Arrays.asList(newItem.getIdPath());
        if (newItem instanceof ISqmlColumnDef || newItem instanceof ISqmlTableDef) {
            final List<Id> newList = new ArrayList<>(list);
            final Id id = newList.get(0);
            newList.remove(id);
            newList.add(0, Id.Factory.changePrefix(id, EDefinitionIdPrefix.DDS_TABLE));
            refId.setPath(newList);
        } else {
            refId.setPath(list);
        }

        final JmlTagDbName tag = JmlTagDbName.Factory.loadFrom(refId);
        items.add(tag);
        return new JmlTag_DbName(environment, tag, pos, isDeprecated, showMode);
    }

    TagInfo createTagDbEntity(final Id entityId, final String pidAsStr, final int pos, final RadixObjects<Item> items, final boolean owner) {
        final JmlTagDbEntity tag = owner ? JmlTagDbEntity.Factory.newInstanceForUFOwner(entityId, pidAsStr) : JmlTagDbEntity.Factory.newInstance(entityId, pidAsStr, false);
        items.add(tag);
        JmlTag_DbEntity newTag = new JmlTag_DbEntity(environment, tag, pos, showMode, titleProvider, editor.isReadOnly());
        if (!newTag.isValid()) {
            //  String mess = Application.translate("SqmlEditor", "Can not actualize entity for #%s");
            //   editor.addValidateProblem(String.format(mess, pidAsStr), newTag);
            return null;
        }
        return newTag;
    }

    @Override
    public String getStrFromXml(final String plainText, final List<TagInfo> copyTags, final QTextCursor tc) {
        if (!tc.hasSelection()) {
            return "";
        }
        String res = "";
        try {
            final JmlType jmlType = JmlType.Factory.newInstance();
            final Jml jml = Jml.Factory.newCopy(sourceCode.getOwnerDefinition(), (Jml) sourceCode);
            toXml(jml, plainText, copyTags, tc, tc.selectionStart(), tc.selectionEnd());
            jml.appendTo(jmlType, ESaveMode.NORMAL);
            AdsUserFuncDefinitionDocument xDoc = null;
            AdsUserFuncDefinition xUserFunc = null;
            LocalizingBundleDefinition xBundle = null;
            for (TagInfo tag : copyTags) {
                if (((JmlTag) tag).getTag() instanceof JmlTagLocalizedString) {
                    if (xBundle == null) {
                        xDoc = AdsUserFuncDefinitionDocument.Factory.newInstance();
                        xUserFunc = xDoc.addNewAdsUserFuncDefinition();
                        xBundle = xUserFunc.addNewStrings();
                    }
                    JmlTagLocalizedString strTag = (JmlTagLocalizedString) ((JmlTag) tag).getTag();
                    AdsMultilingualStringDef strDef = strTag.findLocalizedString(strTag.getStringId());
                    LocalizedString xDef = xBundle.addNewString();
                    if (strDef != null) {
                        strDef.appendTo(xDef, ESaveMode.NORMAL);
                    }
                }
            }
            if (xUserFunc != null && xDoc != null) {
                xUserFunc.setSource(jmlType);
                StringWriter w = new StringWriter();
                xDoc.save(w);
                res = w.toString();
            } else {
                StringWriter w = new StringWriter();
                jmlType.save(w);
                res = w.toString();
            }
        } catch (IOException ex) {
            Logger.getLogger(JmlEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    @Override
    public void toHtml(final QTextCursor tc,final QTextCharFormat charFormat) {
        final Jml javaSrc1 = (Jml) sourceCode;
        toHtml(tc, charFormat, javaSrc1);
    }

    public void toHtml(final QTextCursor tc, final QTextCharFormat charFormat, final Jml javaSrc1) {
        final List<TagInfo> tagList = new ArrayList<>();
        if ((javaSrc1 != null) && (javaSrc1.getItems() != null)) {
            final RadixObjects<Scml.Item> itemList = javaSrc1.getItems();

            int insertIndex = -1;
            int textCursorPos = tc.position() + 1;
            final List<TagInfo> currentTagList = getCurrentTagList();
            if ((tagListHystory != null) && (tagListHystory.size() > 0)) {
                for (int i = 0; i < currentTagList.size(); i++) {
                    tagList.add(currentTagList.get(i).copy());
                    if (insertIndex < 0 && tagList.get(i).getStartPos() >= textCursorPos) {
                        insertIndex = i;
                    }
                }
            }
            if (insertIndex < 0) {
                insertIndex = tagList.size();
            }

            final IProgressHandle ph = environment.getProgressHandleManager().newStandardProgressHandle();
            try {
                ph.startProgress(null, false);
                QApplication.processEvents();
                //int tagNameIndex = 0;
                ph.setMaximumValue(itemList.size());
                for (int j = 0, size = itemList.size(); j < size; j++) {
                    if (j >= 300 && j % 300 == 0) {
                        QApplication.processEvents();
                    }
                    final Jml.Item item = itemList.get(j);
                    TagInfo tag = null;
                    final int textCursorPosition = tc.position() + 1;
                    if (item instanceof Jml.Tag) {
                        Jml.Tag jmlTag = (Jml.Tag) item;
                        Definition context = jmlTag.getOwnerJml().getOwnerDefinition();
                        if (jmlTag instanceof JmlTagInvocation) {
                            JmlTagInvocation tagInvocation = (JmlTagInvocation) jmlTag;
                            Definition def = tagInvocation.resolve(context);
                            boolean isDeprecated = def != null ? def.isDeprecated() : false;
                            tag = new JmlTag_Invocate(environment, (JmlTagInvocation) jmlTag, textCursorPosition, isDeprecated, showMode);
                        } else if (jmlTag instanceof JmlTagDbName) {
                            Definition def = ((JmlTagDbName) jmlTag).resolve(context);
                            boolean isDeprecated = def != null ? def.isDeprecated() : false;
                            tag = new JmlTag_DbName(environment, (JmlTagDbName) jmlTag, textCursorPosition, isDeprecated, showMode);
                        } else if (jmlTag instanceof JmlTagId) {
                            JmlTagId tagId = (JmlTagId) jmlTag;
                            Definition def = tagId.resolve(context);
                            boolean isDeprecated = def != null ? def.isDeprecated() : false;
                            tag = new JmlTag_Id(environment, (JmlTagId) jmlTag, textCursorPosition, isDeprecated, showMode);
                        } else if (jmlTag instanceof JmlTagTypeDeclaration) {
                            boolean isDeprecated = isTypeDeprecated((JmlTagTypeDeclaration) jmlTag);
                            tag = new JmlTag_Type(environment, (JmlTagTypeDeclaration) jmlTag, textCursorPosition, isDeprecated, showMode);
                        } else if (jmlTag instanceof JmlTagDbEntity) {
                            JmlTagDbEntity dbEntityTag = (JmlTagDbEntity) jmlTag;
                            if (dbEntityTag.isUFOwnerRef() && editor.getUserFunc() != null) {
                                dbEntityTag.update(editor.getUserFunc().getOwnerClassId(), editor.getUserFunc().getOwnerPid());
                            }
                            tag = new JmlTag_DbEntity(environment, dbEntityTag, textCursorPosition, showMode, titleProvider, editor.isReadOnly());
                            /*if (!((JmlTag_DbEntity) tag).isValid()) {
                                String mess = Application.translate("SqmlEditor", "Can not actualize entity for #%s");
                                String pidAsStr = ((JmlTagDbEntity) jmlTag).getPidAsStr();
                                //editor.addValidateProblem(String.format(mess, pidAsStr), tag);
                            }*/
                        } else if (jmlTag instanceof JmlTagLocalizedString) {
                            tag = new JmlTag_LocalizedString(environment, (JmlTagLocalizedString) jmlTag, textCursorPosition, showMode);
                        }
                    } else {
                        final Jml.Text jml = (Jml.Text) item;
                        final String s = XscmlEditor.replaceIllegalCharacters(jml.getText());
                        tc.insertText(s, charFormat);
                    }
                    if (tag != null) {
                        tagList.add(insertIndex, tag);
                        insertIndex = insertIndex + 1;
                        final QTextCharFormat c = tag.getCharFormat();
                        tc.insertText(tag.getDisplayName(), c);
                        tc.insertText("");
                    }
                    ph.incValue();
                }
            } finally {
                ph.finishProgress();
            }
            addHystory(false);
            this.tagListHystory.add(tagList);//will be changed
            hystoryIndex = this.tagListHystory.size() - 1;
        } else {
            this.tagListHystory.add(tagList);
            tc.insertText("");
        }
    }

    private boolean isTypeDeprecated(final JmlTagTypeDeclaration typeTag) {
        boolean isDeprecated = false;
        final AdsTypeDeclaration type = typeTag.getType();
        if (type != null) {
            List<AdsType> allTypes = type.resolveAllTypes(editor.getUserFunc());
            for (AdsType resolvedType : allTypes) {
                if (resolvedType instanceof AdsDefinitionType) {
                    Definition def = ((AdsDefinitionType) resolvedType).getSource();
                    if (def != null && def.isDeprecated()) {
                        isDeprecated = true;
                    }
                }
            }
        }
        return isDeprecated;
    }

    public int getItemOffset(final Scml.Item startItem) {
        final int idx = sourceCode.getItems().indexOf(startItem);
        if ((idx >= sourceCode.getItems().size())) {
            return -1;
        }
        int offs = 0;
        Scml.Item item;
        final Iterator<Scml.Item> it = sourceCode.getItems().iterator();
        for (int i = 0; i < idx; i++) {
            item = it.next();
            if (item instanceof Scml.Text) {
                offs += ((Scml.Text) item).getText().length();
            } else if (item instanceof Scml.Tag) {
                TagInfo info = getTagInfoFromMap((Scml.Tag) item);
                if (info != null) {
                    offs += info.getDisplayName().length();
                }
            }
        }
        return offs;
    }

    public int[] itemOffsetAndLength(final Scml.Item startItem) {
        final int idx = sourceCode.getItems().indexOf(startItem);
        if ((idx >= sourceCode.getItems().size()) || (idx == -1)) {
            return null;
        }
        int offs = getItemOffset(startItem), len = 0;
        final Scml.Item item = sourceCode.getItems().get(idx);
        if (item instanceof Scml.Text) {
            len = ((Scml.Text) item).getText().length();
        } else if (item instanceof Scml.Tag) {
            final TagInfo info = getTagInfoFromMap((Scml.Tag) item);
            if (info != null) {
                len += info.getDisplayName().length();
            }
        }
        return new int[]{offs, len};
    }

    /*private List<Object> getCommentPosition(String s){
     List<Object> res=new ArrayList<Object>();
     int end=0;
     while((end<s.length())&&(end!=-1)){
     int[] pos=null;
     int startSingleComment=s.indexOf("//",end), startMultiComment=s.indexOf("/*",end);
     if((startSingleComment!=-1)&&(startMultiComment!=-1)){
     if(startSingleComment<startMultiComment){
     pos=getCommentPos(startSingleComment, s,"\n");
     }else{
     pos=getCommentPos(startMultiComment, s,"/*");
     }
     }else if(startSingleComment!=-1){
     pos=getCommentPos(startSingleComment, s,"\n");
     }else if(startMultiComment!=-1){
     pos=getCommentPos(startMultiComment, s,"/*");
     }
     res.add(pos);
     }
     return res;
     }

     private int[] getCommentPos(int start, String s,String subStr){
     int end=s.indexOf(subStr,start);
     return new int[]{start,end};
     }*/
    public Jml toXml(final String plaintText, final QTextCursor tc) {
        List<TagInfo> tagList = null;
        if (tagListHystory.size() > 0) {
            tagList = getCurrentTagList();
        }
        if (tagList == null) {
            tagList = new ArrayList<>();
        }
        return toXml((Jml) sourceCode, plaintText, tagList, tc, 0, plaintText.length());
    }

    public Jml toXml(final Jml jml, final String plaintText, final List<TagInfo> tagList, final QTextCursor tc, final int pos, final int endpos) {
        jml.getItems().clear();
        int startPosForTag = pos, endPosForTag = pos;
        for (int i = 0, size = tagList.size(); i < size; i++) {
            final JmlTag tag = (JmlTag) tagList.get(i);
            //находим тег в editText
            QTextCharFormat f;
            int curEndPos = endPosForTag;
            do {
                startPosForTag = plaintText.indexOf(tag.getDisplayName(), curEndPos);
                if (startPosForTag == -1) {
                    final String s = getExceptionInfo(jml, plaintText, tagList, i);
                    throw new IllegalStateException(s);
                }
                tc.setPosition(startPosForTag + 1);
                f = tc.charFormat();
                if (!(f.underlineStyle() == UnderlineStyle.SingleUnderline || f.underlineStyle() == UnderlineStyle.SpellCheckUnderline)) {
                    curEndPos = startPosForTag + 1;
                }
            } while (!(f.underlineStyle() == UnderlineStyle.SingleUnderline || f.underlineStyle() == UnderlineStyle.SpellCheckUnderline) && ((startPosForTag + tag.getDisplayName().length()) < endpos));
            //создаем java item
            if (endPosForTag < startPosForTag) {
                int n = startPosForTag < endpos ? startPosForTag : endpos;
                final String d = new String(plaintText.substring(endPosForTag, n));
                Jml.Item jtag = Jml.Text.Factory.newInstance(XscmlEditor.replaceIllegalCharacters(d.replace("\u200B", "")));
                jml.getItems().add(jtag);
            }
            //tag
            endPosForTag = startPosForTag + tag.getDisplayName().length();
            Jml.Item jtag = (Tag) tag.getTag();
            jtag.delete();
            jml.getItems().add(jtag);
            //editor.getValidProblemList().containsValue(tag);
        }
        //создаем java item
        if (endPosForTag < plaintText.length()) {
            final String s = new String(plaintText.substring(endPosForTag, endpos));
            Jml.Item jtag = Jml.Text.Factory.newInstance(XscmlEditor.replaceIllegalCharacters(s.replace("\u200B", "")));
            jml.getItems().add(jtag);
        }
        return jml;
    }
    
    private String getExceptionInfo(final Jml jml, final String plaintText, final List<TagInfo> tagList, final int i) {
        final StringBuilder sb=new StringBuilder("plaintText=");
        sb.append(plaintText).append("\ntagList.size()=");
        sb.append(tagList.size()).append("; index=").append(i).append("\njml=");
        sb.append((jml == null ? "<updateTagsPos>" : jml.toString())).append("\n");
        //String s = "plaintText=" + plaintText + "\n";
        //s += "tagList.size()=" + tagList.size() + "; index=" + i + "\n";
        //s += "jml=" + (jml == null ? "<updateTagsPos>" : jml.toString()) + "\n";
        for (int j = 0; j < tagList.size(); j++) {
            sb.append(((JmlTag) tagList.get(j)).getDisplayName()).append( "(");
            sb.append(((JmlTag) tagList.get(j)).getStartPos()).append(", ");
            sb.append(((JmlTag) tagList.get(j)).getEndPos()).append(")\n");
            //s += ((JmlTag) tagList.get(j)).getDisplayName() + "(";
            //s += ((JmlTag) tagList.get(j)).getStartPos() + ", ";
            //s += ((JmlTag) tagList.get(j)).getEndPos() + ")\n";
        }
        return sb.toString();
    }

    /*private int isInSingleComment(String s){
     int startComment=-1;
     if(((startComment=s.indexOf("//"))!=-1)&&(s.indexOf("\n",startComment)==-1)){
     return startComment;
     }
     return -1;
     }

     private int isInMultiLineComment(String s){
     int startComment=-1;
     if(((startComment=s.indexOf())!=-1)&&(s.indexOf(,startComment)==-1)){
     return startComment;
     }
     return -1;
     }*/
    /* public String getCompletionText(CompletionItem complItem, QFont font) {
     String s1 = complItem.getLeadDisplayText();
     String s2 = complItem.getTailDisplayText();
     return HtmlItemCreator.createTable(s1, s2, font, 445);
     }*/
    @Override
    public void updateTagsPos(final String plaintText, final QTextCursor tc, final boolean isUndoRedo) {
        if ((tagListHystory != null) && (tagListHystory.size() > 0)) {
            final List<TagInfo> tagList = getCurrentTagList();
            if (tagList == null) {
                return;
            }
            QTextCharFormat f;
            tagMap.clear();
            int endPosForTag = 0, startPosForTag;
            for (int i = 0; i < tagList.size(); i++) {
                final JmlTag tag = (JmlTag) tagList.get(i);

                do {
                    startPosForTag = plaintText.indexOf(tag.getDisplayName(), endPosForTag);
                    if (startPosForTag == -1) {
                        String s = getExceptionInfo(null, plaintText, tagList, i);
                        throw new IllegalStateException(s);
                        //return;
                    }
                    tc.setPosition(startPosForTag + 1);
                    f = tc.charFormat();
                    if (!(f.underlineStyle() == UnderlineStyle.SingleUnderline || f.underlineStyle() == UnderlineStyle.SpellCheckUnderline)) {
                        endPosForTag = startPosForTag + 1;
                    } else {
                        tag.calcTagPos(startPosForTag);
                    }
                    tagMap.put(tag.getTag(), tag);
                } while (!(f.underlineStyle() == UnderlineStyle.SingleUnderline || f.underlineStyle() == UnderlineStyle.SpellCheckUnderline));
                endPosForTag = (int) tag.getEndPos() - 1;
            }
            addHystory(isUndoRedo);
        }
    }

    public TagInfo getTagInfoFromMap(final Scml.Tag item) {
        return tagMap.get(item);
    }

    public void setTagInfoMap() {
        if ((tagListHystory != null) && (tagListHystory.size() > 0)) {
            final List<TagInfo> tagList = getCurrentTagList();
            tagMap.clear();
            for (int i = 0; i < tagList.size(); i++) {
                JmlTag tag = (JmlTag) tagList.get(i);
                tagMap.put(tag.getTag(), tag);
            }
        }
    }
}
