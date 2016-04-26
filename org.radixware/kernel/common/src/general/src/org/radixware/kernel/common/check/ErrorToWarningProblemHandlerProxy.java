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

package org.radixware.kernel.common.check;

import org.radixware.kernel.common.check.RadixProblem.IAnnotation;
import org.radixware.kernel.common.defs.RadixObject;

/**
 * Changes errors to warnings, used to check tags in comments.
 */
class ErrorToWarningProblemHandlerProxy implements IProblemHandler {

    private final IProblemHandler sourceProblemHandler;

    protected ErrorToWarningProblemHandlerProxy(IProblemHandler sourceProblemHandler) {
        this.sourceProblemHandler = sourceProblemHandler;
    }

    @Override
    public void accept(RadixProblem problem) {
        if (problem.getSeverity() == RadixProblem.ESeverity.ERROR) {
            final RadixObject source = problem.getSource();
            final String newMessage = "Error in commented tag: " + problem.getMessage();
            final IAnnotation annotation = problem.getAnnotation();
            problem = RadixProblem.Factory.newWarning(source, newMessage, annotation);
        }
        sourceProblemHandler.accept(problem);
    }
}
