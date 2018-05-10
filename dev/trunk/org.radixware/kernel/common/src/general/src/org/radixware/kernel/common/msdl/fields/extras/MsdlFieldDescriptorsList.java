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

package org.radixware.kernel.common.msdl.fields.extras;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;
import org.radixware.kernel.common.msdl.fields.StructureFieldModel;
import org.radixware.kernel.common.msdl.fields.parser.SmioField;
import org.radixware.kernel.common.msdl.fields.parser.structure.BinArr;
import org.radixware.kernel.common.msdl.MsdlStructureField;
import org.radixware.kernel.common.msdl.fields.IExtIdBytesProvider;
import org.radixware.kernel.common.msdl.fields.parser.structure.FieldNamesContainer;


public class MsdlFieldDescriptorsList implements Iterable<MsdlFieldDescriptor> {

    protected List<MsdlFieldDescriptor> descriptorList = new ArrayList<>(10);
    protected Map<String, MsdlFieldDescriptor> descriptorMap = new HashMap<>();
    private final FieldNamesContainer fieldNamesContainer;

    public MsdlFieldDescriptorsList(IExtIdBytesProvider provider) {
        fieldNamesContainer = new FieldNamesContainer(provider);
    }

    public MsdlFieldDescriptorsList() {
        this(null);
    }
    
    @Override
    public Iterator<MsdlFieldDescriptor> iterator() {
        return descriptorList.iterator();
    }
    
    public Iterator<MsdlField> iteratorFields() {
        final Iterator<MsdlFieldDescriptor> iter = iterator();
        return new Iterator<MsdlField>() {

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public MsdlField next() {
                return iter.next().getMsdlField();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

        };
    }

    public void add(MsdlFieldDescriptor f) {
        descriptorList.add(f);
        descriptorMap.put(f.getMsdlField().getName(), f);
        if (f.getMsdlField() instanceof MsdlStructureField) {
            fieldNamesContainer.add((MsdlStructureField) f.getMsdlField());
        }
    }

    private void remove(MsdlFieldDescriptor w) {
        Iterator<MsdlFieldDescriptor> it = iterator();
        while (it.hasNext()) {
            MsdlFieldDescriptor c = it.next();
            if (c.getMsdlField().getName().equals(w.getMsdlField().getName())) {
                it.remove();
                break;
            }
        }
    }

    public void handleOverriden(MsdlFieldDescriptorsList instanceFields) {
        for (MsdlFieldDescriptor instanceField : instanceFields) {
            Iterator<MsdlFieldDescriptor> it = descriptorList.iterator();
            while (it.hasNext()) {
                MsdlFieldDescriptor currTemplateField = it.next();
                String templateFieldName = currTemplateField.getMsdlField().getName();
                if (instanceField.getMsdlField().getName().equals(
                        templateFieldName)) {
                    it.remove();
                    removeFieldFromContainers(templateFieldName);
                    instanceField.setOverridesTemplateField(true);
                    break;
                }
            }
        }
    }

    public void reorder(AbstractFieldModel instanceModel) {
        if (instanceModel instanceof StructureFieldModel
                && instanceModel.getField().isSetTemplateFieldsNames()
                && instanceModel.getField().getTemplateFieldsNames() != null) {
            Set<MsdlFieldDescriptor> processed = new HashSet<>();
            List<String> fieldNamesList = instanceModel.getField().getTemplateFieldsNames();
            List<MsdlFieldDescriptor> temp = new ArrayList<>();
            for (String s : fieldNamesList) {
                if (descriptorMap.containsKey(s)) {
                    MsdlFieldDescriptor w = descriptorMap.get(s);
                    temp.add(w);
                    processed.add(w);
                }
            }

            for (MsdlFieldDescriptor w : descriptorList) {
                if (!processed.contains(w)) {
                    temp.add(w);
                }
            }

            descriptorList.clear();
            descriptorMap.clear();

            for (MsdlFieldDescriptor w : temp) {
                add(w);
            }
        }
    }

    public SmioField getFieldById(BinArr arr) {
        return fieldNamesContainer.getField(arr);
    }

    public byte[] getIdByField(SmioField f) {
        return fieldNamesContainer.getExtId(f);
    }

    public SmioField getFieldByName(String name) {
        return fieldNamesContainer.getField(name);
    }

    public MsdlField getMsdlFieldByName(String name) {
        MsdlFieldDescriptor desc = descriptorMap.get(name);
        if (desc != null)
            return desc.getMsdlField();
        return null;
    }
    
    public boolean isEmpty() {
        return descriptorList.isEmpty();
    }

    public void handleRemovedFields(AbstractFieldModel instanceModel) {
        if (instanceModel.getField().isSetRemovedTemplateFields()) {
            List<String> removedFieldsNames = instanceModel.getField().getRemovedTemplateFields();
            for (String removedFieldName : removedFieldsNames) {
                Iterator<MsdlFieldDescriptor> it = descriptorList.iterator();
                while (it.hasNext()) {
                    MsdlFieldDescriptor cur = it.next();
                    if (cur.getName().equals(removedFieldName)) {
                        it.remove();
                        removeFieldFromContainers(removedFieldName);
                        break;
                    }
                }
            }
        }
    }

    private void removeFieldFromContainers(String templateFieldName) {
        descriptorMap.remove(templateFieldName);
        fieldNamesContainer.remove(templateFieldName);
    }

    public List<MsdlFieldDescriptor> asList() {
        return descriptorList;
    }
}
