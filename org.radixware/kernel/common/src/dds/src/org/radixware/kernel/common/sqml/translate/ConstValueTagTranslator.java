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

import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.tags.ConstValueTag;
import org.radixware.kernel.common.utils.ValueToSqlConverter;

class ConstValueTagTranslator<T extends ConstValueTag> extends SqmlTagTranslator<T> {

    @Override
    public void translate(T tag, CodePrinter cp) {
        final IEnumDef enm = tag.getEnum();
        final IEnumDef.IItem item = tag.getEnumItem();
        final ValAsStr valAsStr = item.getValue();
        final EValType valType = enm.getItemType();
        final String valueInSqlFormat = ValueToSqlConverter.toSql(valAsStr, valType);
        cp.print(valueInSqlFormat);
    }
}
