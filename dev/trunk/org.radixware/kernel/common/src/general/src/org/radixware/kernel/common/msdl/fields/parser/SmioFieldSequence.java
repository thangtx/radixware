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
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.msdl.fields.SequenceFieldModel;
import org.radixware.kernel.common.exceptions.SmioError;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;
import org.radixware.kernel.common.msdl.fields.parser.fieldlist.ExtByteBuffer;
import org.radixware.kernel.common.msdl.fields.parser.fieldlist.IFieldList;
import org.radixware.kernel.common.msdl.fields.parser.fieldlist.PlainFieldList;
import org.radixware.kernel.common.msdl.fields.parser.fieldlist.SeparatedFieldListEnd;
import org.radixware.kernel.common.msdl.fields.parser.piece.SmioPiece;
import org.radixware.schemas.msdl.SequenceField;

public final class SmioFieldSequence extends SmioField {

    private byte[] itemSeparator;
    private SmioField item;
    private IFieldList fieldList;

    public SmioFieldSequence(SequenceFieldModel model) throws SmioError {
        super(model);
        itemSeparator = null;
        if (getField().isSetItemSeparator()) {
            itemSeparator = getField().getItemSeparator();
        } else {
            itemSeparator = getModel().getItemSeparator(true);
        }

        item = getModel().getItem().getFieldModel().getParser();
        byte[] sh = getModel().getShield(true);
        Byte shield = null;
        if (sh != null && sh.length > 0) {
            shield = sh[0];
        }
        Byte separator = null;
        if (itemSeparator != null && itemSeparator.length > 0) {
            separator = itemSeparator[0];
        }
        if (separator == null) {
            fieldList = new PlainFieldList();
        } else {
            fieldList = new SeparatedFieldListEnd(separator, shield);
        }
    }

    @Override
    public SequenceFieldModel getModel() {
        return (SequenceFieldModel) super.getModel();
    }

    @Override
    public SequenceField getField() {
        return (SequenceField) getModel().getField();
    }

    @Override
    public ByteBuffer mergeField(XmlObject obj) throws SmioException {
        XmlObject o[] = obj.selectChildren(namespace, item.getField().getName());
        ExtByteBuffer exbf = new ExtByteBuffer();
        if (o.length > 0) {
            for (XmlObject cur : o) {
                ByteBuffer bf = item.mergeField(cur);
                //added by akrylov. SEE RADIX-5497
                bf = item.getPiece().merge(bf);
                //end
                fieldList.mergeField(exbf, bf);
            }
            ByteBuffer bf = exbf.getByteBuffer();
            if (itemSeparator != null) {
                bf.position(bf.position() - 1);
            }
        } else {
            if (getField().getIsRequired()) {
                throw new SmioException("Required field is missing");
            }
        }
        return exbf.flip();
    }

    @Override
    public void parseField(XmlObject obj, IDataSource ids, boolean containsOddEl) throws SmioException, IOException {
        XmlCursor c = obj.newCursor();
        c.setTextValue("");
        c.dispose();
        
        while (ids.hasAvailable()) {
            IDataSource ds = fieldList.parseField(ids);
            //added by akrylov. SEE RADIX-5497
            while (ds.hasAvailable()) {
                IDataSource itemDataSource = item.getPiece().parse(ds);
                //npopov(RADIX-10891):
                //change y.snegirev(RADIX-7147) solution:
                //If we can't determine item size, we parse until byte buffer is empty
                //Else we parse only one element
                final boolean cantDetermineItemSize = 
                        itemSeparator == null &&
                        item.getPiece().getClass() == SmioPiece.class;  
                do {
                    XmlObject child = item.createNewChild(obj);
                    item.parseField(child, itemDataSource, false); 
                    //*****old solution, see comment above (RADIX-10891) *******
                    //y.snegirev - RADIX-7147. all item data has been consumed - 
                    //no need to proceed parsing
//                    if(item.getPiece() instanceof SmioPieceFixedLen || 
//                       item.getPiece() instanceof SmioPieceEmbeddedLen ||
//                       item.getPiece() instanceof SmioPieceBerTLV
//                     )
//                        break;
                    //end
                    //*********************************************************
                } while (itemDataSource.hasAvailable() && cantDetermineItemSize);
            }
            //end
        }
    }

    @Override
    public void check(RadixObject source, IProblemHandler handler) {
        super.check(source, handler);
        /*
        if (item != null && item.piece != null && !(item.piece instanceof SmioPieceFixedLen)) {
        if (itemSeparator == null || itemSeparator.length == 0) {
        handler.accept(RadixProblem.Factory.newError(source, "MSDL Field '" + source.getQualifiedName() + "' error: 'Item separator not defined'"));
        }
        }
         */
    }
}
