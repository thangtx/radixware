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

import org.radixware.kernel.common.defs.ads.clazz.presentation.AbstractFormPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ClassPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityObjectPresentations;
import org.radixware.kernel.common.radixdoc.DefaultStyle;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.ContentContainer;

/**
 *
 * @author npopov
 */
public class ClassPresentationsRadixdoc {

    private final ContentContainer container;
    protected final ClassRadixdoc.ClassWriter clWriter;
    protected final ClassPresentations presentations;
    protected final ClassRadixdoc classDoc;

    public ClassPresentationsRadixdoc(ContentContainer container, ClassRadixdoc classDoc, ClassPresentations presentations) {
        this.classDoc = classDoc;
        this.clWriter = this.classDoc.getClassWriter();
        this.container = container;
        this.presentations = presentations;
    }

    public abstract static class Factory {

        public static ClassPresentationsRadixdoc getInstance(ContentContainer container, ClassRadixdoc classDoc, ClassPresentations presentations) {
            if (presentations instanceof EntityObjectPresentations) {
                return new EntityPresentationsRadixdoc(container, classDoc, presentations);
            } else if (presentations instanceof AbstractFormPresentations) {
                return new FormPresentationsRadixdoc(container, classDoc, presentations);
            }
            return new ClassPresentationsRadixdoc(container, classDoc, presentations);
        }
    }

    public void writePresentationsInfo() {
        final Block presChapter = container.addNewBlock();
        clWriter.appendStyle(presChapter, DefaultStyle.CHAPTER);
        clWriter.addStrTitle(presChapter, "Presentations Detail");        
        clWriter.writeElementsList(presChapter, presentations.getCommands().getLocal().list(), "Used Commands");
        writeOverrideInfo(presChapter);
    }

    protected void writeOverrideInfo(Block presChapter) {
        //overrided
    }
}
