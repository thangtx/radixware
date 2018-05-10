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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.radixware.kernel.common.compiler.core.completion.JmlCompletionEngine;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.KernelIcon;

public class CompletionListModel extends QAbstractListModel {

    private List<CompletionItem> completionList = new ArrayList<>();
    private int itemWidth = 500;
    private String completionPrefix;
    
    private static final Qt.Alignment TEXT_ALIGNMENT = new Qt.Alignment(Qt.AlignmentFlag.AlignLeft, Qt.AlignmentFlag.AlignJustify);

    public CompletionListModel() {
        super();
    }

    public void setList(final Map<String, HtmlCompletionItem> completionList) {
        beginResetModel();
        this.completionList.clear();
        for (Map.Entry<String, HtmlCompletionItem> entry : completionList.entrySet()) {
            this.completionList.add(new CompletionItem(entry.getKey(), entry.getValue()));
        }
        sortData();
        endResetModel();
    }

    private void sortData() {
        Collections.sort(completionList, new Comparator<CompletionItem>() {

            @Override
            public int compare(CompletionItem o1, CompletionItem o2) {
                if (completionPrefix != null) {
                    final String s1 = (o1 != null && o1.item != null && o1.item.getCompletionItem() != null) ? o1.item.getCompletionItem().getSortText() : null;
                    final String s2 = (o2 != null && o2.item != null && o2.item.getCompletionItem() != null) ? o2.item.getCompletionItem().getSortText() : null;
                    if (completionPrefix.equals(s1)) {
                        if (completionPrefix.equals(s2)) {
                            return 0;
                        }
                        return -1;
                    } else if (completionPrefix.equals(s2)) {
                        return 1;
                    }
                }
                if (o1 == null || o1.key == null) {
                    if (o2 == null || o2.key == null) {
                        return 0;
                    } else {
                        return -1;
                    }
                } else if (o2 == null || o2.key == null) {
                    return 1;
                }
                return o1.key.compareTo(o2.key);
            }

        });
    }

    public void setCompletionPrefix(final String completionPrefix) {
        this.completionPrefix = completionPrefix;
        beginResetModel();
        sortData();
        endResetModel();
    }

    public void setItemWidth(int itemWidth) {
        this.itemWidth = itemWidth;
    }
    
    @Override
    public Object data(final QModelIndex index, final int role) {
        if (index.row() < 0 || index.row() >= completionList.size()) {
            return null;
        }

        if (role == Qt.ItemDataRole.DisplayRole) {
            HtmlCompletionItem item = completionList.get(index.row()).item;
            if (!item.isShowTextAsItIs()) {
                return item.getText(itemWidth, true);
            } else {
                return completionList.get(index.row()).key;
            }
        }
        if (role == Qt.ItemDataRole.EditRole) {
            String str = completionList.get(index.row()).item.getCompletionItem().getSortText();
            str = str == null ? getHint(completionList.get(index.row()).item.getCompletionItem().getLeadDisplayText()) : str;
            if (completionPrefix == null) {
                return str;
            }
            final boolean matches = JmlCompletionEngine.nameMatchesCamelCaseIgnoringDots(str, completionPrefix);
            if (matches) {
                return completionPrefix;
            } else {
                return "#";
            }
            //return getHint(completionList.get(displayList.get(index.row())).getLeadDisplayText());
        }
        if (role == Qt.ItemDataRole.ToolTipRole) {
            String s1 = HtmlCompletionItem.changeColor(completionList.get(index.row()).item.getCompletionItem().getLeadDisplayText());
            String s2 = HtmlCompletionItem.changeColor(completionList.get(index.row()).item.getCompletionItem().getTailDisplayText());
            if (s1.isEmpty() && s2.isEmpty()) {
                return null;
            }
            return s1 + " \t " + s2;
        }
        if (role == Qt.ItemDataRole.TextAlignmentRole) {
            return TEXT_ALIGNMENT;
        }
        if (role == Qt.ItemDataRole.DecorationRole) {
            HtmlCompletionItem item = this.completionList.get(index.row()).item;
            if (item != null && item.getCompletionItem() != null) {
                if (item.getCompletionItem().getIcon() != null) {
                    return ExplorerIcon.getQIcon(KernelIcon.getInstance(item.getCompletionItem().getIcon()));
                } else if (item.getCompletionItem() instanceof SuggestCompletionItem) {
                    return null;
                }
            }
            if (completionList.get(index.row()).key.contains("(")) {
                return ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_METHOD);
            } else {
                return ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_PROP);
            }
        }
        if (role == Qt.ItemDataRole.UserRole) {
            return completionList.get(index.row()).key;
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
        return completionList.size();
    }

    public boolean containsItem(final String key) {
        for (CompletionItem item : completionList) {
            if (Objects.equals(item.key, key)) {
                return true;
            }
        }
        return false;
    }

    public void addItem(final String key, final HtmlCompletionItem html) {
        completionList.add(new CompletionItem(key, html));
        reset();
    }

    public void removeItem(final String key) {
        for (int i = 0; i < completionList.size(); i++) {
            if (Objects.equals(completionList.get(i).key, key)) {
                completionList.remove(i);
                reset();
                return;
            }
        }
    }

    private static final class CompletionItem {

        public final String key;
        public final HtmlCompletionItem item;

        public CompletionItem(String key, HtmlCompletionItem item) {
            this.key = key;
            this.item = item;
        }

    }
}
