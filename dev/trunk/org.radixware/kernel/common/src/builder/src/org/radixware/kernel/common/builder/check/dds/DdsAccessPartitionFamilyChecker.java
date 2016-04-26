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

package org.radixware.kernel.common.builder.check.dds;

import org.radixware.kernel.common.builder.check.common.RadixObjectChecker;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.dds.providers.DdsApfHeadProvider;


@RadixObjectCheckerRegistration
public class DdsAccessPartitionFamilyChecker<T extends DdsAccessPartitionFamilyDef> extends RadixObjectChecker<T> {

    public DdsAccessPartitionFamilyChecker() {
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return DdsAccessPartitionFamilyDef.class;
    }

    @Override
    public void check(final T apf,final IProblemHandler problemHandler) {
        Definition head = apf.findHead();
        if (head != null) {
            final DdsApfHeadProvider headProvider = (DdsApfHeadProvider) DdsVisitorProviderFactory.newApfHeadProvider();
            headProvider.setProblemDdsApfHead(new DdsApfHeadProvider.ProblemDdsApfHead() {
                @Override
                public boolean isNotGeneratedInDb() {
                    problemHandler.accept(RadixProblem.Factory.newError(apf, "The head table must be generated in database"));
                    return true;
                }

                @Override
                public boolean isView() {
                    problemHandler.accept(RadixProblem.Factory.newError(apf, "The head cannot be a veiw"));
                    return true;
                }

                @Override
                public boolean isInvalidPrimaryKey() {
                    problemHandler.accept(RadixProblem.Factory.newError(apf, "Invalid primary key of head table. Primary key must consist of a single attribute"));
                    return true;
                }
                
            });
            headProvider.isTarget(head);
        } else {
            error(apf, problemHandler, "Head not found: #" + String.valueOf(apf.getHeadId()));
        }

        final Id parentFamilyReferenceId = apf.getParentFamilyReferenceId();
        if (parentFamilyReferenceId != null) {
            DdsReferenceDef parentFamilyReference = apf.findParentFamilyReference();
            if (parentFamilyReference != null) {
                final VisitorProvider parentReferenceProvider = DdsVisitorProviderFactory.newApfParentReferenceProvider(apf);
                if (!parentReferenceProvider.isTarget(parentFamilyReference)) {
                    error(apf, problemHandler, "Illegal parent family reference");
                }
            } else {
                error(apf, problemHandler, "Parent family reference not found: #" + String.valueOf(parentFamilyReferenceId));
            }

        }
    }
}
