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

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.msdl.MsdlStructureField;
import org.radixware.kernel.common.msdl.MsdlVariantField;
import org.radixware.kernel.common.msdl.fields.IExtIdBytesProvider;
import org.radixware.kernel.common.msdl.fields.parser.SmioField;
import org.radixware.schemas.msdl.LenUnitDef;


public class FieldNamesContainer {

    private final Map<String, SmioField> fieldByExtIdChars;
    private final Map<SmioField, String> extIdCharsByField;
    private final Map<BinArr, SmioField> fieldByExtId;
    private final Map<SmioField, byte[]> extIdByField;
    private final Map<String, SmioField> fieldBySelector = new HashMap<>();
    private final Map<String, String> selectorValByName = new HashMap<>();
    public  static final String EMPTY_SELECTOR_VAL_PREFIX = "EMPTY_SELECTOR_VAL#";
    private long emptySelectorValsSequence = 0;
    private final IExtIdBytesProvider extIdProvider;

    public FieldNamesContainer(IExtIdBytesProvider provider) {
        this.extIdProvider = provider;
        if (isExtIdUnitChar()) {
            fieldByExtIdChars = new HashMap<>();
            extIdCharsByField = new HashMap<>();
            fieldByExtId = null;
            extIdByField = null;
        } else {
            fieldByExtId = new HashMap<>();
            extIdByField = new HashMap<>();
            fieldByExtIdChars = null;
            extIdCharsByField = null;
        }
    }

    public FieldNamesContainer() {
        this(null);
    }
    
    public void add(MsdlStructureField f) {
        SmioField parser = f.getFieldModel().getParser();
        if (isExtIdUnitChar()) {
            String extIdChars = f.getExtIdChar() != null ? f.getExtIdChar() : f.getName();
            fieldByExtIdChars.put(extIdChars, parser);
            extIdCharsByField.put(parser, extIdChars);
        } else {
            byte[] id = f.isSetExtId() ? f.getExtId() : f.getName().getBytes(StandardCharsets.UTF_8);
            fieldByExtId.put(new BinArr(id), parser);
            extIdByField.put(parser, id);
        }
        fieldBySelector.put(f.getName(), parser);
    }

    public void add(MsdlVariantField f) {
        String key = f.getVariant().getSelectorVal();
        if (key == null) {
            Logger.getLogger(FieldNamesContainer.class.getName()).log(Level.SEVERE, "{0}: Selector value is null", f.getQualifiedName());
            return;
        }
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
        if (isExtIdUnitChar()) {
            SmioField f = fieldByExtIdChars.remove(name);
            extIdCharsByField.remove(f);
        } else {
            SmioField f = fieldByExtId.remove(new BinArr(name.getBytes(StandardCharsets.UTF_8)));
            extIdByField.remove(f);
        }
        
        if (selectorValByName.containsKey(name)) {
            String selector = selectorValByName.get(name);
            fieldBySelector.remove(selector);
            selectorValByName.remove(name);
        }
    }

    public SmioField getField(BinArr extId) {
        if (isExtIdUnitChar()) {
            String extIdStr = extIdProvider.toExtIdChars(extId.data);
            return fieldByExtIdChars.get(extIdStr);
        }
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
        if (isExtIdUnitChar()) {
            String extId = extIdCharsByField.get(f);
            return extIdProvider.toBytes(extId);
        }
        return extIdByField.get(f);
    }
    
    private boolean isExtIdUnitChar() {
        return extIdProvider != null && LenUnitDef.CHAR.toString().equals(extIdProvider.getExtIdUnit());
    }
}
