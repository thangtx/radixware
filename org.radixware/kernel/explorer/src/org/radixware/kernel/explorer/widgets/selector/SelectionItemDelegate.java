/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.widgets.selector;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import java.awt.Rectangle;
import org.radixware.kernel.explorer.utils.EQtStyle;
import org.radixware.kernel.explorer.utils.ItemDelegatePainter;


final class SelectionItemDelegate extends ItemDelegateWithFocusFrame{
    
    private final int textMargin;
    private final QAbstractItemView view;
    private final QSize sizeHint = new QSize(0, 0);
    private boolean sizeHintInited;

    public SelectionItemDelegate(final QAbstractItemView view) {
        super(view);
        this.view = view;
        textMargin = QApplication.style().pixelMetric(QStyle.PixelMetric.PM_FocusFrameHMargin);
    }

    @Override
    public QSize sizeHint(final QStyleOptionViewItem option, final QModelIndex index) {
        if (!sizeHintInited){
            final QStyle style = view.style();
            final int hmargin = style.pixelMetric(QStyle.PixelMetric.PM_FocusFrameHMargin, null, view)+1;            
            final QRect rect = style.subElementRect(QStyle.SubElement.SE_CheckBoxIndicator, option);
            final EQtStyle gStyle = EQtStyle.detectStyle(style);
            final int offset;
            if (gStyle==EQtStyle.Windows || gStyle==EQtStyle.Plastique){
            //This styles paints checkbox with offset
                offset = 1;
            }else{
                offset = 0;
            }
            sizeHint.setWidth(rect.width()+hmargin*4+offset+1);
            sizeHint.setHeight(rect.height()+hmargin*2);
        }
        return sizeHint;
    }
}
