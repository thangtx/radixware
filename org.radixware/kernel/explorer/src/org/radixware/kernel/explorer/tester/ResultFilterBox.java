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

package org.radixware.kernel.explorer.tester;

import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.explorer.env.Application;


public class ResultFilterBox extends QWidget {

    QComboBox box = new QComboBox();

    public ResultFilterBox(QWidget parent) {
        super(parent);
        QLabel label = new QLabel(Application.translate("TesterDialog", "Show: "));
        label.setScaledContents(true);
        QHBoxLayout layout = new QHBoxLayout(this);
        layout.setWidgetSpacing(5);
        layout.addWidget(label);
        layout.addWidget(box);
        setContentsMargins(0, 0, 0, 0);
        layout.setContentsMargins(0, 0, 0, 0);

    }

    public int getCount(){
        return box.count();
    }

    public void connectToActivatedIndex(QWidget owner, String method){
        box.activatedIndex.connect(owner, method);
    }

    public void addItem(String item){
        box.addItem(item);
        if (box.currentIndex()<0){
            box.setCurrentIndex(0);
        }
    }

    public String getItem(int index){
        if (index > 0 && index < box.count())
            return box.itemText(index);
        return "";
    }

    public int getCurrentIndex(){
        return box.currentIndex();
    }

    public String getCurrentItem(){
        return box.currentText();
    }

    public void setCurrentItem(String item){
        int index = box.findText(item);
        box.setCurrentIndex(index);
    }
}
