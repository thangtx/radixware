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

package org.radixware.kernel.common.builder.check.ads.jml;

import java.util.HashMap;
import org.radixware.kernel.common.builder.check.common.CheckHistory;
import org.radixware.kernel.common.builder.check.common.RadixObjectChecker;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.Definition;


@RadixObjectCheckerRegistration
public class JmlChecker extends RadixObjectChecker<Jml> {

    
    public static class JmlSupport extends HashMap<Object, Object> {

        public static JmlSupport initialize(CheckHistory history) {
            JmlSupport support = history.findItemByClass(JmlSupport.class);
            if (support == null) {
                support = new JmlSupport();
                history.registerItemByClass(support);
            }
            return support;
        }
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return Jml.class;
    }

    @Override
    public void check(final Jml jml, final IProblemHandler problemHandler) {
        Definition def = jml.getOwnerDefinition();
        if (def instanceof AdsMethodDef && ((AdsMethodDef) def).getProfile().getAccessFlags().isAbstract()) {
            return;
        }

        CheckHistory history = getHistory();

        JmlSupport support = JmlSupport.initialize(getHistory());

        jml.check(problemHandler, support);
    }
}
