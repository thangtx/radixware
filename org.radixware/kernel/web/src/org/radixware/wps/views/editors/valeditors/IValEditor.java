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

package org.radixware.wps.views.editors.valeditors;

import java.util.EnumSet;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.text.ITextOptionsProvider;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.wps.rwt.ValueEditor;
import org.radixware.wps.text.WpsTextOptions;


public interface IValEditor<T, V extends EditMask> extends ValueEditor<T> {

    public void setValue(T value);

    public T getValue();

    public boolean isReadOnly();

    public void setReadOnly(boolean readOnly);

    public void addButton(IButton button);

    public void setToolTip(String toolTip);

    public void setValidationResult(final ValidationResult validationResult);

    public ValidationResult getValidationResult();

    public void refresh();
    
    public void setTextOptions(final WpsTextOptions options);
    
    public boolean addTextOptionsMarkers(final ETextOptionsMarker...markers);
    
    public boolean removeTextOptionsMarkers(final ETextOptionsMarker...markers);
    
    public boolean setTextOptionsMarkers(final ETextOptionsMarker...markers);
    
    public EnumSet<ETextOptionsMarker> getTextOptionsMarkers();
        
    public void setTextOptionsProvider(final ITextOptionsProvider textOptionsProvider);
        
    public void setDefaultTextOptions(final WpsTextOptions options);
    
    public void setTextOptionsForMarker(final ETextOptionsMarker marker, final WpsTextOptions options);
    
    public WpsTextOptions getTextOptions();
    
    public void refreshTextOptions();

    public ValEditorController<T, V> getController();
    
    public void setInitialValue(final T value);
    
    public T getInitialValue();
}