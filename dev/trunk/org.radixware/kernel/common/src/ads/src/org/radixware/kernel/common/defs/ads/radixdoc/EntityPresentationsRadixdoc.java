/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.defs.ads.radixdoc;

import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ClassPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityObjectPresentations;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.ContentContainer;

/**
 *
 * @author npopov
 */
public class EntityPresentationsRadixdoc extends ClassPresentationsRadixdoc {

    public EntityPresentationsRadixdoc(ContentContainer container, ClassRadixdoc classDoc, ClassPresentations presentations) {
        super(container, classDoc, presentations);
    }

    @Override
    protected void writeOverrideInfo(Block presChapter) {
        EntityObjectPresentations entityPres = (EntityObjectPresentations) presentations;
        clWriter.writeElementsList(presChapter, entityPres.getEditorPresentations().getAll(ExtendableDefinitions.EScope.ALL), "Editor Presentations");
        clWriter.writeElementsList(presChapter, entityPres.getSelectorPresentations().getAll(ExtendableDefinitions.EScope.ALL), "Selector Presentations");
        clWriter.writeElementsList(presChapter, entityPres.getFilters().getAll(ExtendableDefinitions.EScope.ALL), "Filters");
        clWriter.writeElementsList(presChapter, entityPres.getSortings().getAll(ExtendableDefinitions.EScope.ALL), "Sortings");
        clWriter.writeElementsList(presChapter, entityPres.getClassCatalogs().getAll(ExtendableDefinitions.EScope.ALL), "Class Catalogs");
    }
}
