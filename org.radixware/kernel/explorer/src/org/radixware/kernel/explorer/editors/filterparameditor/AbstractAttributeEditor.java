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

package org.radixware.kernel.explorer.editors.filterparameditor;

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;


abstract class AbstractAttributeEditor<T> extends QObject {

    public static class Factory {

        private Factory() {
        }

        public static AbstractAttributeEditor newInstance(IClientEnvironment environment, final EFilterParamAttribute attribute,
                final boolean isReadonly,
                final ISqmlTableDef context,
                final List<String> restrictedNames,
                final QWidget parent) {
            switch (attribute) {
                case NAME:
                    return new NameAttributeEditor(environment, restrictedNames, isReadonly, parent);
                case PROPERTY:
                    return new TargetPropertyAttributeEditor(environment, context, isReadonly, parent);
                case DEFAULT_VALUE:
                    return new DefaultValueAttributeEditor(environment, isReadonly, parent);
                case VALUE_TYPE:
                    return new ValueTypeAttributeEditor(environment, isReadonly, parent);
                case ENUM:
                    return new EnumAttributeEditor(environment, isReadonly, parent);
                case EDIT_MASK:
                    return new EditMaskAttributeEditor(environment, isReadonly, parent);
                case IS_MANDATORY:
                    return new NotNullAttributeEditor(environment, isReadonly, parent);
                case NULL_TITLE:
                    return new NullTitleAttributeEditor(environment, isReadonly, parent);
                case SELECTOR_PRESENTATION:
                    return new SelectorPresentationAttributeEditor(environment, isReadonly, parent);
                case PERSISTENT_VALUE_DEFINED:
                    return new PersistentValDefinedAttributeEditor(environment, isReadonly, parent);
                case PERSISTENT_VALUE:
                    return new PersistentValueAttributeEditor(environment, isReadonly, parent);
                default:
                    throw new IllegalArgumentException("Attribute " + attribute.name() + " is not supported");
            }
        }
    }
    public final com.trolltech.qt.QSignalEmitter.Signal1<AbstractAttributeEditor> attributeChanged =
            new com.trolltech.qt.QSignalEmitter.Signal1<>();
    protected final IClientEnvironment environment;

    public AbstractAttributeEditor(IClientEnvironment environment) {
        super();
        this.environment = environment;
    }

    public IClientEnvironment getEnvironment() {
        return environment;
    }
    
    protected static void setupLabelTextOptions(final QLabel label, final boolean isReadonly){
        final ETextOptionsMarker lbMarker;
        if (isReadonly) {
            lbMarker = ETextOptionsMarker.READONLY_VALUE;
        } else {
            lbMarker = ETextOptionsMarker.REGULAR_VALUE;
        }
        ExplorerTextOptions.Factory.getLabelOptions(lbMarker).applyTo(label);        
    }

    public abstract boolean updateParameter(ISqmlParameter parameter);

    public abstract void updateEditor(ISqmlParameter parameter);

    public abstract EFilterParamAttribute getAttribute();

    public abstract T getAttributeValue();

    public abstract EnumSet<EFilterParamAttribute> getBaseAttributes();

    public abstract void onBaseAttributeChanged(AbstractAttributeEditor linkedEditor);

    public abstract QLabel getLabel();

    public abstract QWidget getEditorWidget();

    public abstract void free();
}
