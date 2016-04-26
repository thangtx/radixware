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

import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QWidget;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Set;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;


abstract class AbstractOptionsContainer extends QFrame implements IStrOptionsContainer {
    private int numberOfRegisteredOptions = 0;
    private int numberOfHiddenOptions = 0;
    
    private final EnumMap<EEditMaskOption, IEditMaskEditorSetting> widgets = 
            new EnumMap<EEditMaskOption, IEditMaskEditorSetting>(EEditMaskOption.class);
    
    public AbstractOptionsContainer(final QWidget parent) {
        super(parent);
    }
    
    public void registerEditorSetting(final IEditMaskEditorSetting setting, final EEditMaskOption option){
        widgets.put(option, setting);
        numberOfRegisteredOptions++;
    }
    
    protected Collection<IEditMaskEditorSetting> settings() {
        return widgets.values();
    }
    
    @Override
    public IEditMaskEditorSetting getSetting(final EEditMaskOption option) {
        return widgets.get(option);
    }

    @Override
    public void setVisibleOptions(final Set<EEditMaskOption> options) {
        IEditMaskEditorSetting setting = null;
        for(EEditMaskOption o : options) {
            setting = widgets.get(o);
            //no need to set an option visible if it is visible
            if(setting != null && setting.isVisible() == false) {
                setting.show();
                numberOfHiddenOptions--;
            }
        }
        if(numberOfHiddenOptions < numberOfRegisteredOptions) {
            this.show();
        } 
    }

    @Override
    public void setHiddenOptions(final Set<EEditMaskOption> options) {
        IEditMaskEditorSetting setting = null;
        for(EEditMaskOption o : options) {
            setting = widgets.get(o);
            //no need to hide an option if it has been hidden
            if(setting != null && setting.isVisible()) {
                setting.hide();
                numberOfHiddenOptions++;
            }
        }
        
        if(numberOfHiddenOptions == numberOfRegisteredOptions) {
            this.hide();
        }
    }

    @Override
    public void setEnabledOptions(final Set<EEditMaskOption> options) {
        IEditMaskEditorSetting setting = null;
        for(EEditMaskOption o : options) {
            setting = widgets.get(o);
            if(setting != null) {
                setting.setEnabled(true);
            }
        }
    }

    @Override
    public void setDisabledOptions(final Set<EEditMaskOption> options) {
        IEditMaskEditorSetting setting = null;
        for(EEditMaskOption o : options) {
            setting = widgets.get(o);
            if(setting != null) {
                setting.setDisabled(true);
            }
        }
    }
}
