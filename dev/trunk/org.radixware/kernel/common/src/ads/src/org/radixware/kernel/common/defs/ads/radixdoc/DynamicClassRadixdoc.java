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

package org.radixware.kernel.common.defs.ads.radixdoc;

import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsDynamicClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;


public class DynamicClassRadixdoc extends ClassRadixdoc {

    public DynamicClassRadixdoc(AdsClassDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    protected void writeClassDefInfo(ContentContainer overview, Table overviewTable) {
        AdsDynamicClassDef dynCl = (AdsDynamicClassDef) source;
        getClassWriter().addStr2BoolRow(overviewTable, "Abstract", dynCl.getAccessFlags().isAbstract());
        getClassWriter().addAllStrRow(overviewTable, "Client Environment", dynCl.getClientEnvironment().getName());
        getClassWriter().addStr2BoolRow(overviewTable, "Separate client runtime", dynCl.isDual());

        if (!dynCl.getTypeArguments().getArgumentList().isEmpty()) {
            Table typeArgsTable = getClassWriter().setBlockCollapsibleAndAddTable(overview.addNewBlock(), "Type arguments", "Name", "Type", "Bound");
            for (AdsTypeDeclaration.TypeArgument arg : dynCl.getTypeArguments().getArgumentList()) {
                getClassWriter().addAllStrRow(typeArgsTable, arg.getName(), arg.getType().toString(), arg.getDerivation().name());
            }
        }
    }
}
