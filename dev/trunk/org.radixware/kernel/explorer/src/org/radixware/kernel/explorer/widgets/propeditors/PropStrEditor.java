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
package org.radixware.kernel.explorer.widgets.propeditors;

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EWidgetMarker;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskFilePath;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.editors.valeditors.AdvancedValBoolEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditorFactory;
import org.radixware.kernel.explorer.editors.valeditors.ValFilePathEditor;

import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;

public class PropStrEditor extends PropEditor {

    private static class ValBoolEditorFactoryImpl extends ValEditorFactory {

        public final static ValBoolEditorFactoryImpl INSTANCE = new ValBoolEditorFactoryImpl();

        private ValBoolEditorFactoryImpl() {
        }

        @Override
        public ValEditor<String> createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment environment, final QWidget parentWidget) {
            return new AdvancedValBoolEditor<>(environment, parentWidget, editMask, false, false);
        }
    }

    private static class ValFilePathEditorFactoryImpl extends ValEditorFactory {

        public final static ValFilePathEditorFactoryImpl INSTANCE = new ValFilePathEditorFactoryImpl();

        private ValFilePathEditorFactoryImpl() {
        }

        @Override
        public ValEditor<String> createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment environment, final QWidget parentWidget) {
            return new ValFilePathEditor(environment, parentWidget, (EditMaskFilePath) editMask, false, false);
        }
    }

    private static class ValStrEditorFactoryImpl extends ValEditorFactory {

        public final static ValStrEditorFactoryImpl INSTANCE = new ValStrEditorFactoryImpl();

        private ValStrEditorFactoryImpl() {
        }

        @Override
        public ValEditor createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment environment, final QWidget parentWidget) {
            return new ValStrEditor(environment, parentWidget, (EditMaskStr) editMask, false, false);
        }
    }

    public static ValEditorFactory getValEditorFactory(final Property prop) {
        if (prop.getEditMask() instanceof EditMaskStr) {
            return new ValStrEditorFactoryImpl();
        } else if (prop.getEditMask() instanceof EditMaskFilePath) {
            return new ValFilePathEditorFactoryImpl();            
        } else {
            return new ValBoolEditorFactoryImpl();
        }
    }

    public PropStrEditor(final Property property) {
        this(property, getValEditorFactory(property));
    }

    protected PropStrEditor(final Property property, final ValEditorFactory factory) {
        super(property, factory);
        if (property.getEditMask() instanceof EditMaskStr) {
            setupMemo(property);
        }
        if (getPropertyEditMask() instanceof EditMaskFilePath) {
            setupInitialPathConfigKey();
        }
    }        

    @Override
    void setProperty(final Property property) {
        super.setProperty(property);
        if (property != null) {
            setupMemo(property);
            if (getPropertyEditMask() instanceof EditMaskFilePath) {
                setupInitialPathConfigKey();
            }
        }
    }

    private void setupMemo(final Property property) {
        if (getValEditor() instanceof ValStrEditor) {
            if (property.getDefinition().isMemo()) {
                ((ValStrEditor) (getValEditor())).addMemo();
            } else {
                ((ValStrEditor) (getValEditor())).removeMemo();
            }
        }
    }
    
    private void setupInitialPathConfigKey(){        
        if (((EditMaskFilePath) getPropertyEditMask()).getStoreLastPathInConfig()){
            final String configKey = "last_property_path_" + getProperty().getId().toString();
            ((ValFilePathEditor) getValEditor()).setInitialPathConfigKey(configKey);        
        }
    }
    
    @Override
    public final EWidgetMarker getWidgetMarker() {
        return EWidgetMarker.STR_PROP_EDITOR;
    }     
}