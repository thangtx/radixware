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

package org.radixware.kernel.common.compiler.core.completion;

import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.Scml;


public class JmlMethodCompletionItem extends AbstractCompletionItem {

    private final MethodBinding method;

    public JmlMethodCompletionItem(MethodBinding method, int relevance, int replaceStart, int replaceEnd) {
        super(relevance, replaceStart, replaceEnd);
        this.method = method;
        StringBuilder text = new StringBuilder();
        text.append(String.valueOf(method.selector));
        text.append('(');
        if (method.parameters != null) {
            for (int i = 0; i < method.parameters.length; i++) {
                if (i > 0) {
                    text.append(", ");
                }
                text.append(String.valueOf(method.parameters[i].shortReadableName()));
                text.append(" arg");
                text.append(i);
            }
        }
        text.append(')');
        this.leadText = text.toString();
        this.sortText = String.valueOf(method.selector);
        this.tailText = String.valueOf(method.returnType.shortReadableName());
        this.tailText = this.tailText.replace("<", "&lt;").replace(">", "&gt;");
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.Method.METHOD;
    }

    @Override
    public Scml.Item[] getNewItems() {
        return new Scml.Item[]{
            Scml.Text.Factory.newInstance(String.valueOf(method.selector)),
            Scml.Text.Factory.newInstance("()")
        };
    }
}
