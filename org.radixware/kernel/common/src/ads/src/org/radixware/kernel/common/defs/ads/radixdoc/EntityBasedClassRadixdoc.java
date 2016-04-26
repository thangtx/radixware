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

import java.util.List;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityBasedClassDef;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;


public class EntityBasedClassRadixdoc extends ClassRadixdoc {

    public EntityBasedClassRadixdoc(AdsClassDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    protected void writeClassDefInfo(ContentContainer overview, Table overviewTable) {
        AdsEntityBasedClassDef entityCl = (AdsEntityBasedClassDef) source;
        getClassWriter().addAllStrRow(overviewTable, "Client Environment", entityCl.getClientEnvironment().getName());
        if (entityCl.findBasis() != null) {
            getClassWriter().addStr2RefRow(overviewTable, "Basis", entityCl.findBasis(), source);
        }
        getClassWriter().addStr2RefRow(overviewTable, "Table", entityCl.findTable(source), source);

        final Table titlesTable = getClassWriter().setBlockCollapsibleAndAddTable(overview.addNewBlock(), "Titles");
        writeTitles(entityCl, titlesTable);
    }

    protected void writeTitles(AdsEntityBasedClassDef entityBasedDef, Table titlesTable) {
        getClassWriter().addStr2MslIdRow(titlesTable, "Singular", entityBasedDef.getLocalizingBundleId(), entityBasedDef.getTitleId());
    }

    @Override
    protected void collectModifiers(AdsClassDef classDef, List<String> modifiers) {
        super.collectModifiers(classDef, modifiers);
        AdsEntityBasedClassDef entityClass = (AdsEntityBasedClassDef) classDef;
        if(entityClass.getAccessFlags().isAbstract()) {
            modifiers.add("Abstract");
        }
    }
}