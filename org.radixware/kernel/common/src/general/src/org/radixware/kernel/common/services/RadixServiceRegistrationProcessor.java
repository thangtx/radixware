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

package org.radixware.kernel.common.services;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;


@SupportedAnnotationTypes(value = "org.radixware.kernel.common.services.RadixServiceRegistration")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class RadixServiceRegistrationProcessor extends AbstractServiceRegistrationProcessor {

    IElementAcceptor acceptor = new InterfaceImplementationAcceptor(IRadixService.class.getCanonicalName());

    @Override
    protected IElementAcceptor getAcceptor() {
        return acceptor;
    }

    @Override
    protected String getServiceName() {
        return IRadixService.class.getName();
    }

}
