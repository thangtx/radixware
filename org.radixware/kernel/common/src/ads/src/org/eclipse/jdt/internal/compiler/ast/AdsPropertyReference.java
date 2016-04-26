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

package org.eclipse.jdt.internal.compiler.ast;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;


public class AdsPropertyReference extends FieldReference {

    private AdsPropertyDef property;
    private Definition referenceContext;

    public AdsPropertyReference(Definition referenceContext, AdsPropertyDef property, FieldReference source) {
        super(property.getId().toCharArray(), source.sourceStart);
        this.property = property;
        this.referenceContext = referenceContext;
    }

    @Override
    public StringBuffer printExpression(int indent, StringBuffer output) {
        return this.receiver.printExpression(0, output).append('.').append("`").append(property.getQualifiedName(referenceContext.getDefinition())).append("`");
    }
}
