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

package org.radixware.kernel.common.sqml.translate;

import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.sqml.tags.TypifiedValueTag;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.exceptions.TagTranslateError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.ISqmlProperty;
import org.radixware.kernel.common.utils.ValueToSqlConverter;


class TypifiedValueTagTranslator<T extends TypifiedValueTag> extends SqmlTagTranslator<T> {

    @Override
    public void translate(T tag, CodePrinter cp) {
        final String displayName = tag.getDisplayValue();
        if (displayName == null || displayName.isEmpty()) {
            throw new TagTranslateError(tag, "Display name of typified value tag must be defined.");
        }

        final ISqmlProperty property = tag.getProperty();
        final EValType valType = property.getValType();
        final ValAsStr valAsStr = tag.getValue();
        final Object val;

        if (valAsStr == null) {
            val = null;
        } else {
            val = valAsStr.toObject(valType);
        }

        cp.print(ValueToSqlConverter.toSql(val, valType));
    }
}
