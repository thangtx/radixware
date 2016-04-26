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


package org.radixware.kernel.common.msdl.fields.parser.structure;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.exceptions.SmioError;
import org.radixware.kernel.common.msdl.MsdlStructureField;
import org.radixware.kernel.common.msdl.MsdlVariantField;
import org.radixware.kernel.common.msdl.fields.parser.SmioField;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.schemas.msdl.Structure;


public class FieldNamesContainer {

    private Map<BinArr, SmioField> fieldByExtId = new HashMap<>();
    private Map<SmioField, byte[]> extIdByField = new HashMap<>();
    private Map<String, SmioField> fieldBySelector = new HashMap<>();
    private Map<String, String> selectorValByName = new HashMap<>();
    public  static final String EMPTY_SELECTOR_VAL_PREFIX = "EMPTY_SELECTOR_VAL#";
    private long emptySelectorValsSequence = 0;

    public FieldNamesContainer() {
    }
    
    public void add(MsdlStructureField f) {
        SmioField parser = f.getFieldModel().getParser();
        Structure.Field field = (Structure.Field) f.getField();
        byte[] id;
        try {
            id = field.isSetExtId() ? field.getExtId() : f.getName().getBytes(FileUtils.XML_ENCODING);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FieldNamesContainer.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new SmioError("Unable to add field", ex);
        }
        fieldByExtId.put(new BinArr(id), parser);
        extIdByField.put(parser, id);
        fieldBySelector.put(f.getName(), parser);
    }

    public void add(MsdlVariantField f) {
        String key = f.getVariant().getSelectorVal();
        SmioField value = f.getFieldModel().getParser();
        if(key.isEmpty()) {
            String keyForEmptySelectorVal = EMPTY_SELECTOR_VAL_PREFIX + (emptySelectorValsSequence++);
            fieldBySelector.put(keyForEmptySelectorVal, value);
            selectorValByName.put(f.getName(), keyForEmptySelectorVal);
        } else {
            fieldBySelector.put(key, value);
            selectorValByName.put(f.getName(), key);
        }
    }

    public void remove(String name) {
        try {
            SmioField f = fieldByExtId.remove(new BinArr(name.getBytes(FileUtils.XML_ENCODING)));
            extIdByField.remove(f);
            if (selectorValByName.containsKey(name)) {
                String selector = selectorValByName.get(name);
                fieldBySelector.remove(selector);
                selectorValByName.remove(name);
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FieldNamesContainer.class.getName()).log(Level.SEVERE, null, ex);
            throw new SmioError("Unable to remove field", ex);
        }
    }

    public SmioField getField(BinArr extId) {
        return fieldByExtId.get(extId);
    }

    public SmioField getField(String name) {
        SmioField ret = fieldBySelector.get(name);
        if (ret == null) {
            String selector = getSelector(name);
            ret = fieldBySelector.get(selector);
        }
        return ret;
    }

    public String getSelector(String name) {
        return selectorValByName.get(name);
    }

    public byte[] getExtId(SmioField f) {
        return extIdByField.get(f);
    }
}
