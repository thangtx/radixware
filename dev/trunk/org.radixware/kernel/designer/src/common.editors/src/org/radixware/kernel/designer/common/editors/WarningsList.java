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

package org.radixware.kernel.designer.common.editors;

import java.util.LinkedList;
import java.util.List;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;


abstract class WarningsList {

    public void explore(final RadixObject object) {
        RequestProcessor.getDefault().post(new Runnable() {

            @Override
            public void run() {
                final List<RadixObject> suppressers = new LinkedList<RadixObject>();
                object.visit(new IVisitor() {

                    @Override
                    public void accept(RadixObject radixObject) {
                        suppressers.add(radixObject);
                    }
                }, new VisitorProvider() {

                    @Override
                    public boolean isTarget(RadixObject radixObject) {
                        RadixProblem.WarningSuppressionSupport sp = radixObject.getWarningSuppressionSupport(false);
                        if (sp != null && !sp.isEmpty()) {
                            return true;
                        } else if (radixObject instanceof AdsDefinition) {
                            return ((AdsDefinition) radixObject).getCompilerWarnings().length > 0;
                        } else {
                            return false;
                        }
                    }
                });
                acceptSuppressers(suppressers);
            }
        });
    }

    public abstract void acceptSuppressers(List<RadixObject> objects);
}
