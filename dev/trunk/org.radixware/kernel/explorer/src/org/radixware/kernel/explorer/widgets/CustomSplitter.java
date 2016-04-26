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

package org.radixware.kernel.explorer.widgets;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QSplitter;
import com.trolltech.qt.gui.QSplitterHandle;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;


public final class CustomSplitter extends QSplitter {
    
    public final class CustomSplitterHandle extends QSplitterHandle {
        private int width = 0;
        public CustomSplitterHandle(final Qt.Orientation o, final QSplitter s) {
            super(o, s);
            final QVBoxLayout layout = new QVBoxLayout();
            setLayout(layout);
            layout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignTop));
        }

        @Override
        protected void paintEvent(final QPaintEvent event) {
            /*Protection from default painting*/
        }
        
        /**
         * Adds a widget on a splitter
         * @param widget a widget to add
         */
        public void addWidget(final QWidget widget) {
            layout().addWidget(widget);
            width = Math.max(width, widget.sizeHint().width());
        }
                        
        public void alignWigetsInside() {
            final QVBoxLayout layout = (QVBoxLayout) layout();
            splitter().setHandleWidth(width + layout.spacing()*2 + layout.getContentsMargins().left);
            layout.insertSpacing(0, layout.spacing() * 2);
            
        }
    }    
    
    public CustomSplitter(final QWidget parent) {
        super(parent);
        setChildrenCollapsible(false);
    }

    @Override
    protected QSplitterHandle createHandle() {
        return new CustomSplitterHandle(orientation(), this);
    }
}
    
    