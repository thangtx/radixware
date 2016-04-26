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

package org.radixware.kernel.explorer.editors.editmask.streditor;

import com.trolltech.qt.core.Qt.Orientation;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.utils.html.HtmlTableBuilder;
import org.radixware.kernel.explorer.editors.editmask.controls.AbstractLabeledEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;



final class StrDefaultQtMaskSetting extends AbstractLabeledEditor<String> implements IEditMaskEditorSetting {
    //private final ValStrEditor valueEditor;
    
    public StrDefaultQtMaskSetting(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent, Orientation.Horizontal);
          
        setMaskTooltip(environment);
        setDefaultValue();
    }
    
    @Override
    public void addToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        if(!this.getValue().isEmpty()) {
            editMask.getStr().setMask(this.getValue());
        }
    }

    @Override
    public void loadFromXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskStr editMaskStr = 
                editMask.getStr();
        if(editMaskStr.isSetMask()) {
            this.setValue(editMaskStr.getMask());
        } else {
            setDefaultValue();
        }
        
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.STR_VC_DEFAULT_QTMASK;
    }

    @Override
    public void setDefaultValue() {
        this.setValue("");
    }

    private void setMaskTooltip(final IClientEnvironment env) {
        final MessageProvider mp = env.getMessageProvider();
        final HtmlTableBuilder tooltip = new HtmlTableBuilder();
        
        tooltip.putTwoCellHeader(mp.translate("EditMask", "Character"), mp.translate("EditMask", "Meaning") );
        tooltip.putTwoCellRow("A", mp.translate("EditMask", "ASCII alphabetic character required. A-Z, a-z."));
        tooltip.putTwoCellRow("a", mp.translate("EditMask", "ASCII alphabetic character permitted but not required."));
        tooltip.putTwoCellRow("N", mp.translate("EditMask", "ASCII alphanumeric character required. A-Z, a-z, 0-9."));
        tooltip.putTwoCellRow("n", mp.translate("EditMask", "ASCII alphanumeric character permitted but not required."));
        tooltip.putTwoCellRow("X", mp.translate("EditMask", "Any character required."));
        tooltip.putTwoCellRow("x", mp.translate("EditMask", "Any character permitted but not required."));
        tooltip.putTwoCellRow("9", mp.translate("EditMask", "ASCII digit required. 0-9."));
        tooltip.putTwoCellRow("0", mp.translate("EditMask", "ASCII digit permitted but not required."));
        tooltip.putTwoCellRow("D", mp.translate("EditMask", "ASCII digit required. 1-9."));
        tooltip.putTwoCellRow("d", mp.translate("EditMask", "ASCII digit permitted but not required (1-9)."));
        tooltip.putTwoCellRow("#", mp.translate("EditMask", "ASCII digit or plus/minus sign permitted but not required."));
        tooltip.putTwoCellRow("H", mp.translate("EditMask", "Hexadecimal character required. A-F, a-f, 0-9."));
        tooltip.putTwoCellRow("h", mp.translate("EditMask", "Hexadecimal character permitted but not required."));
        tooltip.putTwoCellRow("B", mp.translate("EditMask", "Binary character required. 0-1.") );
        tooltip.putTwoCellRow("b", mp.translate("EditMask", "Binary character permitted but not required."));
        tooltip.putTwoCellRow("&gt;", mp.translate("EditMask", "All following alphabetic characters are uppercased."));
        tooltip.putTwoCellRow("&lt;", mp.translate("EditMask", "All following alphabetic characters are lowercased."));
        tooltip.putTwoCellRow("!", mp.translate("EditMask", "Switch off case conversion."));
        tooltip.putTwoCellRow("\\", mp.translate("EditMask", "Use \\ to escape the special characters listed above to use them as separators."));
        setToolTip(tooltip.toString());
    }

    @Override
    protected ValStrEditor initValueEditor(final IClientEnvironment environment) {
        return new ValStrEditor(environment, this);
    }
    

}
