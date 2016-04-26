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

package org.radixware.kernel.common.compiler.core.problems;

import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.compiler.problem.ProblemSeverities;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.types.Id;


public class RadixLevelProblem extends CategorizedProblem {

    private int severity;
    private int id;
    private String[] arguments;
    private String message;
    private RadixObject determinant;

    public RadixLevelProblem(RadixObject determinant, String message, int severity) {
        this.message = message;
        this.determinant = determinant;
        this.severity = severity;
    }

    @Override
    public int getCategoryID() {
        return ProblemReporter.getProblemCategory(this.severity, this.id);
    }
    private static final String MARKER_TYPE_PROBLEM = "org.eclipse.jdt.core.problem"; //$NON-NLS-1$
    private static final String MARKER_TYPE_TASK = "org.eclipse.jdt.core.task"; //$NON-NLS-1$

    @Override
    public String getMarkerType() {
        return this.id == IProblem.Task
                ? MARKER_TYPE_TASK
                : MARKER_TYPE_PROBLEM;
    }

    @Override
    public String[] getArguments() {
        return arguments;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public char[] getOriginatingFileName() {
        return determinant.getQualifiedName().toCharArray();
    }

    @Override
    public int getSourceEnd() {
        return 0;
    }

    @Override
    public int getSourceLineNumber() {
        return 0;
    }

    @Override
    public int getSourceStart() {
        return 0;
    }

    @Override
    public boolean isError() {
        return (this.severity & ProblemSeverities.Error) != 0;
    }

    @Override
    public boolean isWarning() {
        return ProblemSeverities.Warning == 0 ? this.severity == 0 : (this.severity & ProblemSeverities.Warning) != 0;
    }

    @Override
    public void setSourceEnd(int sourceEnd) {
    }

    @Override
    public void setSourceLineNumber(int lineNumber) {
    }

    @Override
    public void setSourceStart(int sourceStart) {
    }
}
