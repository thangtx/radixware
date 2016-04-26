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

package org.radixware.kernel.common.client.models.items.properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.views.IPropEditor;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.utils.FileUtils;

public class PropertyBin extends SimpleProperty<Bin> {

    public PropertyBin(Model owner, RadPropertyDef propDef) {
        super(owner, propDef);
    }
    
    @Override
    public final RadPropertyDef getDefinition() {
        return super.getDefinition();
    }        

    @Override
    public IPropEditor createPropertyEditor() {
        return getEnvironment().getApplication().getStandardViewsFactory().newPropBinEditor(this);
    }

    @Override
    public Class<?> getValClass() {
        return Bin.class;
    }

    /*public void setServerValObject(Object x) {
    setServerVal((Bin) x);
    }*/
    @Override
    public void setValObjectImpl(Object x) {
        setInternalVal((Bin) x);
    }

    @Override
    public final EValType getType() {
        return EValType.BIN;
    }
    
    @Override
    public Bin loadFromStream(InputStream input) throws IOException {
        return Bin.wrap(FileUtils.getInputStreamAsByteArray(input, -1));        
    }

    @Override
    public void saveToStream(OutputStream output, Bin value) throws IOException {
        if (value!=null){
            FileUtils.writeBytes(output, value.get());
        }        
    }
}
