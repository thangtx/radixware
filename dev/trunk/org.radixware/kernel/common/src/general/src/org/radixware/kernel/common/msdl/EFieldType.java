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

package org.radixware.kernel.common.msdl;

import org.radixware.schemas.msdl.AnyField;
import org.radixware.schemas.msdl.BCHField;
import org.radixware.schemas.msdl.BinField;
import org.radixware.schemas.msdl.BooleanField;
import org.radixware.schemas.msdl.ChoiceField;
import org.radixware.schemas.msdl.DateTimeField;
import org.radixware.schemas.msdl.Field;
import org.radixware.schemas.msdl.IntField;
import org.radixware.schemas.msdl.NumField;
import org.radixware.schemas.msdl.SequenceField;
import org.radixware.schemas.msdl.StrField;
import org.radixware.schemas.msdl.StructureField;


public enum EFieldType {

    NONE("None", "None"),
    BCH("BCH", "BCH"),
    BIN("Bin", "Bin"),
    CHOICE("Choice", "Choice"),
    DATETIME("DateTime", "DateTime"),
    INT("Int", "Int"),
    NUM("Num", "Int"),
    SEQUENCE("Sequence", "Sequence"),
    STR("Str", "Str"),
    STRUCTURE("Structure", "Structure"),
    BOOLEAN("Boolean", "Boolean");
    private String value, title;

    EFieldType(String value, String title) {
        this.value = value;
        this.title = title;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return value;
    }

    public String getTitle() {
        return title;
    }

    public static EFieldType getFieldType(Field field) {
        if (field instanceof BCHField) {
            return BCH;
        }
        if (field instanceof BinField) {
            return BIN;
        }
        if (field instanceof ChoiceField) {
            return CHOICE;
        }
        if (field instanceof DateTimeField) {
            return DATETIME;
        }
        if (field instanceof IntField) {
            return INT;
        }
        if (field instanceof NumField) {
            return NUM;
        }
        if (field instanceof SequenceField) {
            return SEQUENCE;
        }
        if (field instanceof StrField) {
            return STR;
        }
        if (field instanceof StructureField) {
            return STRUCTURE;
        }
        if (field instanceof BooleanField) {
            return BOOLEAN;
        }
        return NONE;
    }

    public static Field getField(AnyField anyField) {
        if (anyField.isSetBCH()) {
            return anyField.getBCH();
        }
        if (anyField.isSetBin()) {
            return anyField.getBin();
        }
        if (anyField.isSetChoice()) {
            return anyField.getChoice();
        }
        if (anyField.isSetDateTime()) {
            return anyField.getDateTime();
        }
        if (anyField.isSetInt()) {
            return anyField.getInt();
        }
        if (anyField.isSetNum()) {
            return anyField.getNum();
        }
        if (anyField.isSetSequence()) {
            return anyField.getSequence();
        }
        if (anyField.isSetStr()) {
            return anyField.getStr();
        }
        if (anyField.isSetStructure()) {
            return anyField.getStructure();
        }
        return null;
    }
}
