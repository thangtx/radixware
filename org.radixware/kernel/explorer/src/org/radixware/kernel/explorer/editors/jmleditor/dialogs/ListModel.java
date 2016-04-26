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

package org.radixware.kernel.explorer.editors.jmleditor.dialogs;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QListView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef.Lookup.DefInfo;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor;
import org.radixware.kernel.explorer.editors.jmleditor.completer.HtmlCompletionItem;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class ListModel extends BaseModelList {

    private List<DefInfo> defList;

    public ListModel(final QListView ownerList) {
        this.defList = new ArrayList<>();
        this.ownerList = ownerList;
    }

    public void setDefList(final List<DefInfo> defList) {
        this.defList = defList;
        this.dataChanged.emit(index(0, 0), index(rowCount(null) - 1, 0));
        this.reset();
        Collections.sort(this.defList, new DefInfoComparator());
    }

    public List<DefInfo> getDefList() {
        return defList;
    }

    @Override
    public int rowCount(final QModelIndex arg0) {
        return defList.size();
    }

    @Override
    public Object data(final QModelIndex index, final int role) {
        if ((index != null) && (ownerList != null)) {
            if (role == Qt.ItemDataRole.DisplayRole) {
                final DefInfo defInfo=defList.get(index.row());
                final String defName = defInfo.getName();
                final String moduleName = defInfo.getModuleName();
                final boolean isDepricated = defInfo.isDeprecated();
                final int scrollWidth = ownerList.verticalScrollBar().isVisible() ? ownerList.verticalScrollBar().size().width() : 0;
                final int width = ownerList.childrenRect().width() - ownerList.iconSize().width() - scrollWidth - 10;
                final QFont font = ownerList.font();
                final QFontMetrics fontMetrics = new QFontMetrics(font);
                font.setBold(true);
                final QFontMetrics boldMetrics = new QFontMetrics(font);
                font.setBold(false);
                final HtmlCompletionItem item = new HtmlCompletionItem(defName, moduleName, fontMetrics, boldMetrics,isDepricated);
                return item.getText(width,false);
            } else if (role == Qt.ItemDataRole.DecorationRole) {
                if (index.column() == 0) {
                    return getIcon(defList.get(index.row()).getPath()[0]);
                }
            } else if (role == Qt.ItemDataRole.TextAlignmentRole) {
                if (index.column() == 0) {
                    return new Qt.Alignment(Qt.AlignmentFlag.AlignLeft, Qt.AlignmentFlag.AlignVCenter);
                }
            }
        }
        return null;
    }

    private QIcon getIcon(final Id id) {
        final EDefinitionIdPrefix defPref = id.getPrefix();
        if ((defPref == EDefinitionIdPrefix.ADS_ENUMERATION)) {
            return ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.Definitions.CONSTSET);
        } else if ((defPref == EDefinitionIdPrefix.XML_SCHEME)||(defPref == EDefinitionIdPrefix.MSDL_SCHEME)) {
            return ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_XML);
        } else {
            return ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_CLASS);
        }
    }

    static class DefInfoComparator implements Comparator<DefInfo> {

        @Override
        public int compare(final DefInfo o1, final DefInfo o2) {
            if (o1.getName() != null && o2.getName() != null) {
                return o1.getName().compareTo(o2.getName());
            }
            return -1;
        }
    }
}
