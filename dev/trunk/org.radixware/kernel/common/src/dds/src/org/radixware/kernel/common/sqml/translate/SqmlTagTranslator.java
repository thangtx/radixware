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

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.scml.ITagTranslator;
import org.radixware.kernel.common.sqml.Sqml;

/**
 * SQML tag translator.
 */
// class is public to generalizate code of translation and title calculation
public abstract class SqmlTagTranslator<T extends Sqml.Tag> implements ITagTranslator<T> {

    private IProblemHandler problemHandler;

    protected SqmlTagTranslator() {
        this(null);
    }

    protected SqmlTagTranslator(IProblemHandler problemHandler) {
        this.problemHandler = problemHandler;
    }

    protected void checkDepecation(Sqml.Tag tag, Definition def) {
        if (problemHandler != null && def != null && def.isDeprecated()) {
            //check sqml is not in context of deprecated definition
            Sqml sqml = tag.getOwnerSqml();
            if (sqml != null) {
                Definition owner = sqml.getOwnerDefinition();
                if (owner != null && owner.isDeprecated()) {
                    return;
                }
            }

            problemHandler.accept(RadixProblem.Factory.newWarning(tag, def.getTypeTitle() + " " + def.getQualifiedName() + " is deprecated"));
        }
    }

    public interface Checker {

        public void check(IProblemHandler problemHandler);
    }

    protected void performCheckIfPossible(Checker checker) {
        if (problemHandler != null) {
            checker.check(problemHandler);
        }
    }
}
