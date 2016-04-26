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

import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.jml.JmlTagInvocation;


public class AdsMessageSend extends MessageSend {

    public AdsMethodDef method;
    public Definition referenceContext;
    public JmlTagInvocation invocationTag;

    public AdsMessageSend(MessageSend src, Definition referenceContext, JmlTagInvocation invocationTag) {
        this.receiver = src.receiver;
        this.arguments = src.arguments;
        method = (AdsMethodDef) invocationTag.resolve(referenceContext);

        if (method.getTransparence() != null && method.getTransparence().isTransparent()) {
            String publishedName = method.getTransparence().getPublishedName();
            int index = publishedName.indexOf("(");
            if (index > 0) {
                publishedName = publishedName.substring(0, index);
            }
            this.selector = publishedName.toCharArray();
        } else {
            this.selector = method.getId().toCharArray();
        }
    }

    @Override
    public TypeBinding resolveType(BlockScope scope) {
        return super.resolveType(scope); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StringBuffer printExpression(int indent, StringBuffer output) {
        if (!this.receiver.isImplicitThis()) {
            this.receiver.printExpression(0, output).append('.');
        }
        if (this.typeArguments != null) {
            output.append('<');
            int max = this.typeArguments.length - 1;
            for (int j = 0; j < max; j++) {
                this.typeArguments[j].print(0, output);
                output.append(", ");//$NON-NLS-1$
            }
            this.typeArguments[max].print(0, output);
            output.append('>');
        }
        output.append("`").append(this.method.getName()).append("`").append('(');
        if (this.arguments != null) {
            for (int i = 0; i < this.arguments.length; i++) {
                if (i > 0) {
                    output.append(", "); //$NON-NLS-1$
                }
                this.arguments[i].printExpression(0, output);
            }
        }
        return output.append(')');
    }
}
