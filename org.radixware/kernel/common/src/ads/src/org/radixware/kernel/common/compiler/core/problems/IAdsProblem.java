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

import org.eclipse.jdt.core.compiler.IProblem;


public interface IAdsProblem extends IProblem {

    public static final int NotVisibleProperty = 100001;
    public static final int ReadOnlyProperty = 100002;
    public static final int MissingTypeInProperty = 100003;
    public static final int DeprecatedProperty = 100004;
    public static final int THIS_Usage = 1111001;
}
