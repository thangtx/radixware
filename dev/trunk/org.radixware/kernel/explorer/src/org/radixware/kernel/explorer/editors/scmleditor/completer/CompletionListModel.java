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

package org.radixware.kernel.explorer.editors.scmleditor.completer;

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
import org.radixware.kernel.explorer.editors.scml.IScmlCompletionItem;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class CompletionListModel extends QAbstractListModel {

    private Map<String, IScmlCompletionItem> completionList;
    private List<String> displayList;

    public CompletionListModel() {
        super();
        completionList = new LinkedHashMap<String, IScmlCompletionItem>();
        displayList = new ArrayList<>();
    }

    public void setList(Map<String, IScmlCompletionItem> completionList) {
        this.completionList.clear();
        this.completionList.putAll(completionList);
        displayList.clear();
        displayList.addAll(completionList.keySet());
        Collections.sort(displayList);
    }

    @Override
    public Object data(QModelIndex index, int role) {
        if (role == Qt.ItemDataRole.DisplayRole) {
            return displayList.get(index.row());
        }
        if (role == Qt.ItemDataRole.EditRole) {
            String str = completionList.get(displayList.get(index.row())).getSortText();
            str = str == null ? getHint(completionList.get(displayList.get(index.row())).getLeadText()) : str;
            return str;
            //return getHint(completionList.get(displayList.get(index.row())).getLeadDisplayText());
        }
        if (role == Qt.ItemDataRole.ToolTipRole) {            
            String s1 = HtmlCompletionItem.changeColor(completionList.get(displayList.get(index.row())).getLeadText());
            String s2 = HtmlCompletionItem.changeColor(completionList.get(displayList.get(index.row())).getTailText());
            if ("".equals(s1) && "".equals(s2)) {
                return null;
            }
            return s1 + " \t " + s2;
        }
        if (role == Qt.ItemDataRole.TextAlignmentRole) {
            return new Qt.Alignment(Qt.AlignmentFlag.AlignLeft, Qt.AlignmentFlag.AlignJustify);
        }
        if (role == Qt.ItemDataRole.DecorationRole) {
            IScmlCompletionItem item = this.completionList.get(displayList.get(index.row()));
            if ((item != null) && (item.getIcon() != null)) {
                return ExplorerIcon.getQIcon(/*KernelIcon.getInstance(*/item.getIcon());
            } else {
                if (displayList.get(index.row()).equals(CompleterProcessor.NO_SUGGEST) || displayList.get(index.row()).equals(CompleterProcessor.PLEASE_WAIT)) {
                    return new QIcon();
                } else if (displayList.get(index.row()).indexOf("(") != -1) {
                    return ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_METHOD);
                } else {
                    return ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_PROP);
                }
            }
        }
        return null;
    }

    private String getHint(String s) {
        int start = s.indexOf('<'), end = 0;
        while (start!= -1) {
            end = s.indexOf('>');
            if (end  != -1) {
                s = (s.substring(0, start) + s.substring(end + 1, s.length()));
                start = s.indexOf('<');
            }
        }
        return s;
    }

    @Override
    public int rowCount(QModelIndex index) {
        return displayList.size();
    }

    public boolean containsItem(String key) {
        completionList.get(key);
        displayList.contains(key);
        return (completionList.get(key) != null) && displayList.contains(key);
    }

    public void addItem(IScmlCompletionItem item, String key) {
        completionList.put(key, item);
        displayList.add(key);
        reset();
    }

    public void removeItem(String key) {
        if (completionList.containsKey(key)) {
            completionList.remove(key);
            displayList.remove(key);
            reset();
        }
    }
}
