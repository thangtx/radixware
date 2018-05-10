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

import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;
import org.radixware.kernel.common.msdl.fields.BCHFieldModel;
import org.radixware.kernel.common.msdl.fields.BinFieldModel;
import org.radixware.kernel.common.msdl.fields.BooleanFieldModel;
import org.radixware.kernel.common.msdl.fields.ChoiceFieldModel;
import org.radixware.kernel.common.msdl.fields.DateTimeFieldModel;
import org.radixware.kernel.common.msdl.fields.IntFieldModel;
import org.radixware.kernel.common.msdl.fields.NumFieldModel;
import org.radixware.kernel.common.msdl.fields.SequenceFieldModel;
import org.radixware.kernel.common.msdl.fields.StrFieldModel;
import org.radixware.kernel.common.msdl.fields.StructureFieldModel;
import org.radixware.kernel.common.msdl.fields.parser.piece.SmioPiece;
import org.radixware.kernel.common.msdl.fields.parser.piece.SmioPieceEmbeddedLen;
import org.radixware.kernel.common.msdl.fields.parser.piece.SmioPieceFixedLen;
import org.radixware.kernel.common.msdl.fields.parser.piece.SmioPieceSeparated;
import org.radixware.kernel.common.msdl.fields.parser.structure.SmioFieldStructure;
import org.radixware.schemas.msdl.Piece;

/**
 *
 * @author npopov
 */
public class SmioParserFactory implements ISmioParserFactory {

    @Override
    public SmioField createParser(AbstractFieldModel model) {
        if (model instanceof StructureFieldModel) {
            return new SmioFieldStructure((StructureFieldModel) model);
        } else if (model instanceof ChoiceFieldModel) {
            return new SmioFieldChoice((ChoiceFieldModel) model);
        } else if (model instanceof SequenceFieldModel) {
            return new SmioFieldSequence((SequenceFieldModel) model);
        } else if (model instanceof BCHFieldModel) {
            return new SmioFieldBCH((BCHFieldModel) model);
        } else if (model instanceof BinFieldModel) {
            return new SmioFieldBin((BinFieldModel) model);
        } else if (model instanceof BooleanFieldModel) {
            return new SmioFieldBoolean((BooleanFieldModel) model);
        } else if (model instanceof DateTimeFieldModel) {
            return new SmioFieldDateTime((DateTimeFieldModel) model);
        } else if (model instanceof IntFieldModel) {
            return new SmioFieldInt((IntFieldModel) model);
        } else if (model instanceof NumFieldModel) {
            return new SmioFieldNum((NumFieldModel) model);
        } else if (model instanceof StrFieldModel) {
            return new SmioFieldStr((StrFieldModel) model);
        }
        throw new IllegalArgumentException("Unknown type of field: " + model.getClass().getName());
    }

    @Override
    public SmioPiece createParser(SmioField field, Piece piece) {
        if (piece.isSetFixedLen()) {
            return new SmioPieceFixedLen(field, piece.getFixedLen());
        } else if (piece.isSetSeparated()) {
            return new SmioPieceSeparated(field, piece.getSeparated());
        } else if (piece.isSetEmbeddedLen()) {
            return new SmioPieceEmbeddedLen(field, piece.getEmbeddedLen());
        } else {
            return new SmioPiece(field);
        }
    }
}
