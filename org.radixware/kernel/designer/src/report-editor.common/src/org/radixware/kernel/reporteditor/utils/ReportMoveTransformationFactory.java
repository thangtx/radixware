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

package org.radixware.kernel.reporteditor.utils;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.utils.IMoveTransformation;
import org.radixware.kernel.designer.common.dialogs.utils.IMoveTransformationFactory;


public class ReportMoveTransformationFactory implements IMoveTransformationFactory {

    @Override
    public IMoveTransformation findTransformation(final RadixObject externalRadixObject,final Definition movedDef,final RadixObject destination) {
        if (movedDef instanceof AdsReportClassDef) {

            
            return new IMoveTransformation() {

                @Override
                public boolean isPossible() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public String getDisplayName() {
                    return "All dependent reports MUST be in the same state";
                }

                @Override
                public String getCause() {
                    return "Because of runtime integrity";
                }

                @Override
                public void perform() {
                    System.out.println("DO SOMETHING");
                }

                @Override
                public RadixIcon getIcon() {
                    return null;
                }
            };
        } else {
            return new IMoveTransformation() {

                @Override
                public boolean isPossible() {
                    return false;
                }

                @Override
                public String getDisplayName() {
                    return "This operation is only allowed for reports";
                }

                @Override
                public String getCause() {
                    return "Invalid moved definition. Report expected";
                }

                @Override
                public void perform() {
                    //do nothing
                }

                @Override
                public RadixIcon getIcon() {
                    return null;
                }
            };
        }
    }

    @Override
    public boolean matches(final Definition def) {
        return def instanceof AdsReportClassDef;
    }
}
