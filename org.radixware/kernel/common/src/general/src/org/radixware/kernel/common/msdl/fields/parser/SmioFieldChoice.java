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

package org.radixware.kernel.common.msdl.fields.parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.apache.xmlbeans.XmlCursor;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.msdl.MsdlVariantField;
import org.radixware.kernel.common.msdl.fields.ChoiceFieldModel;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.fields.extras.MsdlFieldDescriptor;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSourceArray;
import org.radixware.kernel.common.msdl.fields.parser.fieldlist.ExtByteBuffer;
import org.radixware.kernel.common.msdl.fields.parser.structure.FieldNamesContainer;
import org.radixware.schemas.types.Str;

public final class SmioFieldChoice extends SmioField {

    private final FieldNamesContainer fieldNamesContainer = new FieldNamesContainer();
    private final SmioFieldStr selector;

    public SmioFieldChoice(ChoiceFieldModel model) {
        super(model);

        for (MsdlVariantField cur : model.getFields()) {
            fieldNamesContainer.add(cur);
        }
        selector = (SmioFieldStr) getModel().getSelector().getParser();
    }

    @Override
    public ChoiceFieldModel getModel() {
        return (ChoiceFieldModel) super.getModel();
    }

    @Override
    public ByteBuffer mergeField(XmlObject obj) throws SmioException {
        XmlCursor c = obj.newCursor();
        c.toChild(0);
        String n = c.getName().getLocalPart();
        String selectorVal = fieldNamesContainer.getSelector(n);
        SmioField f = fieldNamesContainer.getField(n);
        if(f == null && getModel().isTemplateInstance()) {
            f = getModel().getFieldDescriptorList().getFieldByName(n);
        }
        if (f == null) {
            throw new SmioException("Selector value not found for variant '" + n + "'", getModel().getName());
        }
        XmlObject xo = XmlObject.Factory.newInstance();
        XmlCursor cc = xo.newCursor();
        cc.toNextToken();
        if(selectorVal.startsWith(FieldNamesContainer.EMPTY_SELECTOR_VAL_PREFIX)) {
            cc.insertElementWithText("Item", namespace, "");
        } else {
            cc.insertElementWithText("Item", namespace, selectorVal);
        }
        cc.dispose();
        ByteBuffer bfs = selector.mergeField(xo);
        bfs = selector.getPiece().merge(bfs);            // + AK 29.12.2010
        ByteBuffer bff = f.merge(obj);
        ExtByteBuffer exbf = new ExtByteBuffer();
        exbf.extPut(bfs);
        exbf.extPut(bff);
        return exbf.flip();
    }
    
    private String tryExecuteSelectFunction(IDataSource ids) throws IOException, SmioException {
        if (ids instanceof IDataSourceArray && getModel().getRootMsdlScheme().getPreprocessorAccess() != null
                && getModel().getField().getSelectorAdvisorFunctionName() != null) {
            ByteBuffer bf = ((IDataSourceArray) ids).getByteBuffer();
            byte arr[] = new byte[bf.remaining()];
            int oldPosition = bf.position();
            bf.get(arr);
            bf.position(oldPosition);
            return getModel().getRootMsdlScheme().getPreprocessorAccess().select(getModel().getField().getSelectorAdvisorFunctionName(), arr);
        }
        return null;
    }

    @Override
    public void parseField(XmlObject obj, IDataSource ids, boolean containsOddEl) throws SmioException, IOException {
        Str sel = Str.Factory.newInstance();

        String key = tryExecuteSelectFunction(ids);
        if (key == null) {
            IDataSource ds = selector.getPiece().parse(ids);
            selector.parseField(sel, ds, false);
            key = sel.getStringValue();
        }
        SmioField f = fieldNamesContainer.getField(key);
        if(f == null && getModel().isTemplateInstance()) {
            f = getModel().getFieldDescriptorList().getFieldByName(key);
        }

        if (f == null) {
            throw new SmioException("Cannot find matching field for selector \"" + key + "\"");
        }
        f.parse(obj, ids);
    }

    @Override
    public void check(RadixObject source, IProblemHandler handler) {
        super.check(source, handler);
        final Map<String, MsdlVariantField> uniqueSelectorVals = new HashMap<>();
        if (getModel().isTemplateInstance()) {
            for (MsdlFieldDescriptor d : getModel().getFieldDescriptorList()) {
                if (d.getMsdlField() instanceof MsdlVariantField) {
                    MsdlVariantField f = (MsdlVariantField) d.getMsdlField();
                    checkField(f, uniqueSelectorVals, source, handler);
                }
            }
        } else {
            for (MsdlVariantField f : getModel().getFields()) {
                checkField(f, uniqueSelectorVals, source, handler);
            }
        }
        selector.check(source, handler);
    }
    
    private void checkField(MsdlVariantField field,
            Map<String, MsdlVariantField> uniqueSelectorVals,
            RadixObject source, IProblemHandler handler) {

        fireNoSelectorProblem(field, handler, source);
        final String selectorVal = field.getVariant().getSelectorVal();
        if (selectorVal != null && !selectorVal.isEmpty()) {
            if (!uniqueSelectorVals.containsKey(selectorVal)) {
                uniqueSelectorVals.put(selectorVal, field);
            } else {
                fireSameSelectorProblem(field, uniqueSelectorVals.get(selectorVal), handler, source);
            }
        }
    }

    private boolean isSelectorValueSet(MsdlVariantField f) {
        return f.getVariant().getSelectorVal() != null;
    }

    private void fireNoSelectorProblem(MsdlVariantField cur, IProblemHandler handler, RadixObject source) {
        if (!isSelectorValueSet(cur)) {
            handler.accept(RadixProblem.Factory.newError(source, "No selector value specified for field: " + cur.getName()));
        }
    }

    private void fireSameSelectorProblem(MsdlVariantField f1, MsdlVariantField f2, IProblemHandler handler, RadixObject source) {
        handler.accept(RadixProblem.Factory.newError(source, "Same selector value specified for fields: '"
                + f1.getName() + "' and '" + f2.getName() + "'"));
    }
}
