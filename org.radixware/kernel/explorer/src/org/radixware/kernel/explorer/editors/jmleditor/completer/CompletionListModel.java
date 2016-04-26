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

package org.radixware.kernel.explorer.editors.jmleditor.completer;

import com.trolltech.qt.core.QAbstractListModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QIcon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.KernelIcon;


public class CompletionListModel extends QAbstractListModel {
    
    private Map<String, HtmlCompletionItem> completionList;
    private final List<String> displayList;
    private int itemWidth = 500;

    public CompletionListModel() {
        super();
        completionList = new LinkedHashMap<>();
        displayList = new ArrayList<>();
    }
    
    public void setList(final Map<String, HtmlCompletionItem> completionList) {
        beginResetModel();
        this.completionList.clear();
        displayList.clear();
        this.completionList = completionList;
        displayList.addAll(completionList.keySet());
        Collections.sort(displayList);
        endResetModel();
    }

    public void setItemWidth(int itemWidth) {
        this.itemWidth = itemWidth;
    }
    
    @Override
    public Object data(final QModelIndex index, final int role) {
        if (index.row() < 0 || index.row() >= displayList.size()) {
            return null;
        }
        
        if (role == Qt.ItemDataRole.DisplayRole) {
            HtmlCompletionItem item = completionList.get(displayList.get(index.row()));
            if (!item.isShowTextAsItIs()) {
                return item.getText(itemWidth, true);
            } else {
                return displayList.get(index.row());
            }
        }
        if (role == Qt.ItemDataRole.EditRole) {
            String str = completionList.get(displayList.get(index.row())).getCompletionItem().getSortText();
            str = str == null ? getHint(completionList.get(displayList.get(index.row())).getCompletionItem().getLeadDisplayText()) : str;
            return str;
            //return getHint(completionList.get(displayList.get(index.row())).getLeadDisplayText());
        }
        if (role == Qt.ItemDataRole.ToolTipRole) {            
            String s1 = HtmlCompletionItem.changeColor(completionList.get(displayList.get(index.row())).getCompletionItem().getLeadDisplayText());
            String s2 = HtmlCompletionItem.changeColor(completionList.get(displayList.get(index.row())).getCompletionItem().getTailDisplayText());
            if (s1.isEmpty() && s2.isEmpty()) {
                return null;
            }
            return s1 + " \t " + s2;
        }
        if (role == Qt.ItemDataRole.TextAlignmentRole) {
            return new Qt.Alignment(Qt.AlignmentFlag.AlignLeft, Qt.AlignmentFlag.AlignJustify);
        }
        if (role == Qt.ItemDataRole.DecorationRole) {
            HtmlCompletionItem item = this.completionList.get(displayList.get(index.row()));
            if (item != null && item.getCompletionItem() != null && item.getCompletionItem().getIcon() != null) {
                return ExplorerIcon.getQIcon(KernelIcon.getInstance(item.getCompletionItem().getIcon()));
            } else {
                if (displayList.get(index.row()).equals(CompleterProcessor.NO_SUGGEST) || displayList.get(index.row()).equals(CompleterProcessor.PLEASE_WAIT)) {
                    return new QIcon();
                } else if (displayList.get(index.row()).contains("(")) {
                    return ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_METHOD);
                } else {
                    return ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_PROP);
                }
            }
        }
        if (role == Qt.ItemDataRole.UserRole) {
            return displayList.get(index.row());
        }
        return null;
    }

    private String getHint(String s) {
        int start = 0, end = 0;
        while ((start = s.indexOf('<')) != -1) {
            if ((end = s.indexOf('>')) != -1) {
                s = (s.substring(0, start) + s.substring(end + 1, s.length()));
            }
        }
        return s;
    }

    @Override
    public int rowCount(final QModelIndex index) {
        return displayList.size();
    }

    public boolean containsItem(final String key) {
        return (completionList.get(key) != null) && displayList.contains(key);
    }

    public void addItem(final String key, final HtmlCompletionItem html) {
        completionList.put(key, html);
        displayList.add(key);
        reset();
    }

    public void removeItem(final String key) {
        if (completionList.containsKey(key)) {
            completionList.remove(key);
            displayList.remove(key);
            reset();
        }
    }
}
