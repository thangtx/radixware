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

package org.radixware.kernel.designer.tree.ads.nodes.actions;

import java.util.Set;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;


public class ViewClientSourceAction extends ViewSourceAction {

    public ViewClientSourceAction() {
    }

    public static class ViewClientSourceCookie extends ViewSourceCookie {

        public ViewClientSourceCookie(IJavaSource def, Set<CodeType> codeTypes) {
            super(def, ERuntimeEnvironmentType.COMMON_CLIENT, codeTypes);
        }
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{ViewClientSourceCookie.class};
    }

    @Override
    public String getName() {
        return "View Client-Common Source";
    }
}
