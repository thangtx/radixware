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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlCursor;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.msdl.MsdlVariantField;
import org.radixware.kernel.common.msdl.fields.ChoiceFieldModel;
import org.radixware.kernel.common.exceptions.SmioError;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.fields.extras.MsdlFieldDescriptor;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;
import org.radixware.kernel.common.msdl.fields.parser.fieldlist.ExtByteBuffer;
import org.radixware.kernel.common.msdl.fields.parser.structure.FieldNamesContainer;
import org.radixware.schemas.types.Str;

public final class SmioFieldChoice extends SmioField {

    private FieldNamesContainer fieldNamesContainer = new FieldNamesContainer();
    private SmioFieldStr selector;
    private Method selectorAdvisorMethod;

    public SmioFieldChoice(ChoiceFieldModel model) throws SmioError, SmioException {
        super(model);

        for (MsdlVariantField cur : model.getFields()) {
            fieldNamesContainer.add(cur);
        }
        selector = (SmioFieldStr) getModel().getSelector().getParser();
        Class preprocessorClass = getModel().getRootMsdlScheme().getPreprocessorClass();
        if (preprocessorClass != null && getModel().getField().getSelectorAdvisorFunctionName() != null) {
            try {
                selectorAdvisorMethod = preprocessorClass.getMethod(getModel().getField().getSelectorAdvisorFunctionName(), new Class[]{byte[].class});
            } catch (NoSuchMethodException e) {
                LogFactory.getLog(ChoiceFieldModel.class).warn(String.format("Could not find appropriate method %s", getField().getName()));
                selectorAdvisorMethod = null;
            }
            if (selectorAdvisorMethod != null) {
                selectorAdvisorMethod.setAccessible(true);
            }
        }
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

    @Override
    public void parseField(XmlObject obj, IDataSource ids, boolean containsOddEl) throws SmioException, IOException {
        Str sel = Str.Factory.newInstance();

        String key = null;
        if (selectorAdvisorMethod != null) {
            ByteBuffer bf = ids.getByteBuffer();
            byte arr[] = new byte[bf.limit() - bf.position()];
            int oldPosition = bf.position();
            bf.get(arr, bf.position(), bf.limit());
            bf.position(oldPosition);
            try {
                key = (String) selectorAdvisorMethod.invoke(selectorAdvisorMethod, new Object[]{arr});
            } catch (IllegalAccessException ex) {
                throw new SmioException(preprocessorFunctionExecutionError, ex);
            } catch (IllegalArgumentException ex) {
                throw new SmioException(preprocessorFunctionExecutionError, ex);
            } catch (InvocationTargetException ex) {
                throw new SmioException(preprocessorFunctionExecutionError, ex);
            }

        }
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
        for (MsdlVariantField cur : getModel().getFields()) {
            fireNoSelectorProblem(cur, handler, source);
        }
        if (getModel().isTemplateInstance()) {
            for (MsdlFieldDescriptor d : getModel().getFieldDescriptorList()) {
                if (d.getMsdlField() instanceof MsdlVariantField) {
                    MsdlVariantField vf = (MsdlVariantField) d.getMsdlField();
                    fireNoSelectorProblem(vf, handler, source);
                }
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
}
