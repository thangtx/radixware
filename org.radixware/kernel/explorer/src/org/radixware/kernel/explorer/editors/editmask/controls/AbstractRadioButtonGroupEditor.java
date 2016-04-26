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

package org.radixware.kernel.explorer.editors.editmask.controls;

import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QRadioButton;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;


abstract public class AbstractRadioButtonGroupEditor extends QWidget { 

    public final Signal1<Object> stateChanged = new Signal1<>();
    public List<QRadioButton> radioButtonlist;
    private ArrayList<String> titlesLst;
    private QGroupBox groupBox = new QGroupBox(this);

    public AbstractRadioButtonGroupEditor(IClientEnvironment env, QWidget parent) {
        super(parent);
        groupBox.toggled.connect(stateChanged);
    }
    
    public void createCheckBoxGroup(ArrayList<String> list){
        this.titlesLst = list;
        for (String title : titlesLst) {
            QRadioButton rb = new QRadioButton(title, groupBox);
            radioButtonlist.add(rb);
        }
    }

    public void setChecked(int i, boolean val) {
        radioButtonlist.get(i).setChecked(val);
    }

    public boolean isChecked(int index) {
        return radioButtonlist.get(index).isChecked();
    }
    
    public int getChecked(){
        for (int i = 0; i<radioButtonlist.size(); i++){
            if (isChecked(i)){
                return i;
            }
        }
        setChecked(0,true);
        return 0;
    }
}
