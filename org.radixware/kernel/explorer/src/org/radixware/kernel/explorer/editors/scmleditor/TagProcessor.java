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

package org.radixware.kernel.explorer.editors.scmleditor;

import com.trolltech.qt.gui.QTextCharFormat;
import com.trolltech.qt.gui.QTextCharFormat.UnderlineStyle;
import com.trolltech.qt.gui.QTextCursor;
import java.util.ArrayList;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.explorer.editors.scml.AbstractScml;
import org.radixware.kernel.explorer.editors.scml.IScmlItem;
import org.radixware.kernel.explorer.editors.scml.ScmlTag;
import org.radixware.kernel.explorer.editors.scml.ScmlText;


public class TagProcessor {
    public int hystoryIndex;//
    public List<List<TagView>> tagListHystory;//
    //protected EDefinitionDisplayMode showMode;
    //protected Scml sourceCode;
    protected AbstractScml sourceScml;

   /* static public enum TagType {

        Id, DataType, Literal, Task, EventCode, Nls
    }*/

    public TagProcessor() {
       // showMode = EDefinitionDisplayMode.SHOW_FULL_NAMES;
    }

    public void open(AbstractScml javaSrc) {
        this.sourceScml = javaSrc;
        tagListHystory = new ArrayList<List<TagView>>();
    }
    
    public List<TagView> getCurrentTagList(){
        return tagListHystory.get(hystoryIndex);
    }

    /**
     * ???????? ??? ? ?????? ?????
     * @param newTag - ??????????? ???
     */
    public void insertTag(TagView newTag) {
        List<TagView> tagList = getCurrentTagList();
        for (int i = 0; i < tagList.size(); i++) {
            if (newTag.getStartPos() <= tagList.get(i).getStartPos()) {
                tagList.add(i, newTag);
                return;
            }
        }
        tagList.add(newTag);
    }

    /**
     * ???????? ????????? ????? ? ?????? ?????
     * @param newTags - ?????? ??????????? ?????
     * @param pos - ??????? ??????? ? ?????????
     */
    protected void insertTags(List<TagView> newTags, int pos) {
        List<TagView> tagList =getCurrentTagList();
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
     * @param deletingTagList - ?????? ????????? ?????
     */
    public void deleteTags(List<TagView> deletingTagList) {
        List<TagView> tagList = getCurrentTagList();
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
     * @param deletingTag - ????????? ???
     * @return  
     */
    public boolean deleteTag(TagView deletingTag) {
        List<TagView> tagList = getCurrentTagList();
        return tagList.remove(deletingTag);
    }

    /**
     * ????????????? ???????? ????? ?? xml ? ????? ???????????? ? ?????????
     * @param tc - ?????? ?????????
     * @param charFormat - 
     */
    public void toHtml(QTextCursor tc, QTextCharFormat charFormat,List<IScmlItem> tags){
        List<TagView> tagList = new ArrayList<>();
        if (tagListHystory!=null && !tagListHystory.isEmpty()) {
                tagList.addAll(getCurrentTagList());
        }
        int insertIndex = getInsertTagIndex(tagList, tc.position() + 1);
        for (int j = 0, size = tags.size(); j < size; j++) {
           IScmlItem item = tags.get(j);
           if (item.isText()) {
               String s = item.getTranslator().getDisplayString();
               tc.insertText(s, charFormat);
           } else {
               TagView tag = new TagView((ScmlTag)item, tc.position());                    
               tagList.add(insertIndex, tag);
               insertIndex = insertIndex + 1;
               //String s = tag.getDisplayString()createHtml("");
               tc.insertText(tag.getDisplayString(),tag.getCharFormat());
           }
        }
        this.tagListHystory.add(tagList);
        hystoryIndex = this.tagListHystory.size() - 1;
    }
    
    public AbstractScml strToScml(String plaintText, QTextCursor tc){
        List<TagView> tagList = new ArrayList<>();
        if (!tagListHystory.isEmpty()) {
            tagList = getCurrentTagList();
        }
        return strToScml(  plaintText, tagList, tc, 0, plaintText.length());
    }
  
    public AbstractScml strToScml( String plaintText, List<TagView> tagList, QTextCursor tc, int pos, int endpos){
        AbstractScml scml=(AbstractScml)sourceScml.newInstance();
        int startPosForTag = pos, endPosForTag = pos;
        for (int i = 0; i < tagList.size(); i++) {
            TagView tag = tagList.get(i);
            //������� ��� � editText
            QTextCharFormat f;
            int curEndPos = endPosForTag;
            do {
                startPosForTag = plaintText.indexOf(tag.getDisplayString(), curEndPos);
                tc.setPosition(startPosForTag + 1);
                f = tc.charFormat();
                if (f.underlineStyle() == UnderlineStyle.NoUnderline) {
                    curEndPos = startPosForTag + 1;
                }
            } while ((f.underlineStyle() == UnderlineStyle.NoUnderline) && ((startPosForTag + tag.getDisplayString().length()) < endpos));
            //������� java item
            if (endPosForTag < startPosForTag) {
                //org.radixware.schemas.xscml.Sqml.Item item = sqml.addNewItem();
                int n = startPosForTag < endpos ? startPosForTag : endpos;
                String text = plaintText.substring(endPosForTag, n).replace("\u200B", "");
                ScmlText textItem=new ScmlText(text);
                scml.add(textItem);
                //item.setSql(d);
            }
            //tag
            //org.radixware.schemas.xscml.Sqml.Item itemTag = sqml.addNewItem();
            //tag.addTagToSqml(itemTag);
            scml.add(tag.getScmlItem());
            endPosForTag = startPosForTag + tag.getDisplayString().length();
        }
        //������� java item
        if (endPosForTag < plaintText.length()) {
            String text = plaintText.substring(endPosForTag, endpos);
            text = text.replace("\u200B", "");
            ScmlText textItem=new ScmlText(text);
            scml.add(textItem);
            //org.radixware.schemas.xscml.Sqml.Item item = sqml.addNewItem();
            //item.setSql(s.replace("\u200B", ""));
        }
        return scml;
    }

     public XmlObject toXml( String plaintText, QTextCursor tc){
        return sourceScml.saveToXml();
     }
    
    /**
     * ????????????? ???????? ????? ? xml
     * @param plaintText - ????????? ????????????? ????????? ??????
     * @param tc - ?????? ?????????
     * @return ???????? ????? ? ???? xml 
     */
    public XmlObject toXml( String plaintText, List<TagView> tagList, QTextCursor tc, int pos, int endpos){
        
        throw new UnsupportedOperationException("Not supported yet.");
        //XmlObject xml = org.radixware.schemas.xscml.Sqml.Factory.newInstance();
        
        /*AbstractScml scml=new AbstractScml();
        int startPosForTag = pos, endPosForTag = pos;
        for (int i = 0; i < tagList.size(); i++) {
            TagView tag = (TagView) tagList.get(i);
            //������� ��� � editText
            QTextCharFormat f;
            int curEndPos = endPosForTag;
            do {
                startPosForTag = plaintText.indexOf(tag.getDisplayString(), curEndPos);
                tc.setPosition(startPosForTag + 1);
                f = tc.charFormat();
                if (f.underlineStyle() == UnderlineStyle.NoUnderline) {
                    curEndPos = startPosForTag + 1;
                }
            } while ((f.underlineStyle() == UnderlineStyle.NoUnderline) && ((startPosForTag + tag.getDisplayString().length()) < endpos));
            //������� java item
            if (endPosForTag < startPosForTag) {
                org.radixware.schemas.xscml.Sqml.Item item = sqml.addNewItem();
                int n = startPosForTag < endpos ? startPosForTag : endpos;
                String d = plaintText.substring(endPosForTag, n).replace("\u200B", "");
                item.setSql(d);
            }
            //tag
            org.radixware.schemas.xscml.Sqml.Item itemTag = sqml.addNewItem();
            tag.addTagToSqml(itemTag);
            endPosForTag = startPosForTag + tag.getDisplayName().length();
        }
        //������� java item
        if (endPosForTag < plaintText.length()) {
            String s = plaintText.substring(endPosForTag, endpos);
            s = s.replace("\u200B", "");
            org.radixware.schemas.xscml.Sqml.Item item = sqml.addNewItem();
            item.setSql(s.replace("\u200B", ""));
        }*/
       // return xml;
    }
    /**
     * ??????? ??? (??? Jml-?????????)
     * @param c 
     * @param pos - ??????? ??????? ? ?????????
     * @return ????????? ???
     */
    /*public TagInfo createCompletionTag(Scml.Item newItem, int pos) {
        return null;
    }*/

    /**
     * ???????? ??????? ?????
     * @param plaintText- ???????? ?????
     * @param tc - ?????? ?????????
     * @param isUndoRedo 
     */
    public void updateTagsPos(String plaintText, QTextCursor tc, boolean isUndoRedo){
        if ((tagListHystory != null) && (!tagListHystory.isEmpty())) {
            List<TagView> tagList = new ArrayList<>();
            tagList = getCurrentTagList();
            if (tagList == null) {
                return;
            }
            QTextCharFormat f;

            int endPosForTag = 0, startPosForTag;
            for (int i = 0; i < tagList.size(); i++) {
                TagView tag = tagList.get(i);
                do {
                    startPosForTag = plaintText.indexOf(tag.getDisplayString(), endPosForTag);
                    if (startPosForTag == -1) {
                        return;
                    }
                    tc.setPosition(startPosForTag + 1);
                    f = tc.charFormat();
                    if (f.underlineStyle() == UnderlineStyle.NoUnderline) {
                        endPosForTag = startPosForTag + 1;
                    } else {
                        tag.calcTagPos(startPosForTag);
                    }
                } while (f.underlineStyle() == UnderlineStyle.NoUnderline);
                endPosForTag = (int) tag.getEndPos() - 1;
            }
            addHystory(isUndoRedo);
        }
    }

    public void addHystory(boolean isUndoRedo) {
        if (!isUndoRedo) {
            if (hystoryIndex < tagListHystory.size() - 1) {
                while (tagListHystory.size() > hystoryIndex + 1) {
                    tagListHystory.remove(hystoryIndex + 1);
                }
            }
        }
    }

    /**
     * ???????? ??? ????
     * @param tag - ???
     * @return true ???? ??? ???? ????????
     */
    /*public boolean changeTagName(TagInfo tag) {
        return tag.setDisplayedInfo(showMode);
    }*/

    /**
     * ????? ???
     * @param pos- ??????? ??????? ?????????
     * @param isDelete true ???? ??? ??????? ??? ????????
     * @return ???
     */
    public TagView getTagInfoForCursorMove(int pos, boolean isDelete) {
        if ((tagListHystory != null) && (tagListHystory.size() > hystoryIndex)) {
            List<TagView> tagList = getCurrentTagList();
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

    public TagView getTagInfoForCursorMove(int pos, boolean isDelete, boolean isLeftMove) {
        if ((tagListHystory != null) && (tagListHystory.size() >= hystoryIndex)) {
            List<TagView> tagList = getCurrentTagList();
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
     * @param startSelection - ????????? ??????? ?????????
     * @param endSelection -  ???????? ??????? ?????????
     * @param forDelet - true ???? ?????? ??????? ??? ???????? ??????
     * @return
     */
    public List<TagView> getTagListInSelection(int startSelection, int endSelection, boolean forDelet) {
        List<TagView> tagList = getCurrentTagList();
        List<TagView> res = new ArrayList<TagView>();
        for (int i = 0; i < tagList.size(); i++) {
            if (((tagList.get(i).getStartPos() <= startSelection) && (endSelection <= tagList.get(i).getEndPos()))
                    || ((startSelection <= tagList.get(i).getStartPos()) && (tagList.get(i).getEndPos() <= endSelection))
                    || ((startSelection <= tagList.get(i).getStartPos()) && (endSelection <= tagList.get(i).getEndPos()) && (tagList.get(i).getStartPos() < endSelection))
                    || ((tagList.get(i).getStartPos() <= startSelection) && (tagList.get(i).getEndPos() <= endSelection) && (startSelection < tagList.get(i).getEndPos()))) {
                if (forDelet) {
                    res.add(tagList.get(i));
                } else {
                    res.add(tagList.get(i).copy());
                }
            }
        }
        return res;
    }

    protected int getInsertTagIndex(List<TagView> tagList, int pos) {
        for (int i = 0; i < tagList.size(); i++) {
            if (tagList.get(i).getStartPos() >= pos) {
                return i;
            }
        }
        return tagList.size();
    }

    public String getStrFromXml(String plainText, List<TagView> copyTags, QTextCursor tc){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void clearHystory() {
        List<TagView> tagList = new ArrayList<TagView>();
        for (int i = 0; i < getCurrentTagList().size(); i++) {
            tagList.add(getCurrentTagList().get(i).copy());
        }
        tagListHystory.clear();
        tagListHystory.add(tagList);
        hystoryIndex = tagListHystory.size() - 1;
    }
    
    public AbstractScml getScml(){
        return sourceScml;
    }

    /*public EDefinitionDisplayMode getShowMode() {
        return showMode;
    }

    public void setShowMode(EDefinitionDisplayMode showMode) {
        this.showMode = showMode;
    }*/
}
