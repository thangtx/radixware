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

import org.radixware.kernel.common.exceptions.SmioError;
import org.radixware.kernel.common.msdl.fields.SimpleFieldModel;
import org.radixware.schemas.msdl.IntField;
import org.radixware.schemas.msdl.NumField;


public abstract class SmioFieldSigned extends SmioFieldSimple {

    protected String plusSign, minusSign;
    protected Byte sign;

    public SmioFieldSigned(SimpleFieldModel model) throws SmioError {
        super(model);
        plusSign = "";
        minusSign = "-";
        if (this instanceof SmioFieldInt) {
            IntField field = (IntField) getField();
            if (field.isSetPlusSign()) {
                plusSign = field.getPlusSign().toString();
            } else {
                Character ch = getModel().getPlusSign(false);
                if (ch != null) {
                    plusSign = ch.toString();
                }
            }
            if (field.isSetMinusSign()) {
                minusSign = field.getMinusSign().toString();
            } else {
                Character ch = getModel().getMinusSign(false);
                if (ch != null) {
                    minusSign = ch.toString();
                }
            }
        }
        if (this instanceof SmioFieldNum) {
            NumField field = (NumField) getField();
            if (field.isSetPlusSign()) {
                plusSign = field.getPlusSign().toString();
            } else {
                Character ch = getModel().getPlusSign(false);
                if (ch != null) {
                    plusSign = ch.toString();
                }
            }
            minusSign = null;
            if (field.isSetMinusSign()) {
                minusSign = field.getMinusSign().toString();
            } else {
                Character ch = getModel().getMinusSign(false);
                if (ch != null) {
                    minusSign = ch.toString();
                }
            }
        }
    }

    protected String parseSign(String val) {
        if (plusSign != null && plusSign.length() > 0 && val.charAt(0) == plusSign.charAt(0)) {
            val = val.substring(1);
        }
        if (minusSign != null && minusSign.length() > 0 && val.charAt(0) == minusSign.charAt(0)) {
            val = "-" + val.substring(1);
        }
        return val;
    }
}
