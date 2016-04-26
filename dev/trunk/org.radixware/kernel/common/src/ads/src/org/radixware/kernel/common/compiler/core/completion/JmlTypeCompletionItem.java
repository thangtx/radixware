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

import java.util.LinkedList;
import java.util.List;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.Scml;

public class JmlTypeCompletionItem extends AbstractCompletionItem {

    final char[][] typeName;
    final char[][] packageName;
    private boolean isMemberType;
    private AdsTypeDeclaration enclosingType;

    public JmlTypeCompletionItem(char[] typeName, char[][] packageName, boolean isMemberType, int relevance, int replaceStart, int replaceEnd) {
        super(relevance, replaceStart, replaceEnd);
        this.leadText = String.valueOf(typeName).replace("$", ".");
        this.sortText = this.leadText;
        this.tailText = String.valueOf(CharOperation.concatWith(packageName, '.'));
        this.typeName = new char[][]{typeName};
        this.packageName = packageName;
        this.isMemberType = isMemberType;
    }

    public JmlTypeCompletionItem(char[][] typeName, RadixObject context, AdsTypeDeclaration enclosingType, boolean isMemberType, int relevance, int replaceStart, int replaceEnd) {
        super(relevance, replaceStart, replaceEnd);
        this.leadText = buildLeadText(typeName);
        this.sortText = this.leadText;
        this.tailText = enclosingType.getQualifiedName(context);
        this.typeName = typeName;
        this.packageName = null;
        this.enclosingType = enclosingType;
        this.isMemberType = isMemberType;
    }

    private String buildLeadText(char[][] typeName) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < typeName.length; i++) {
            if (i > 0) {
                sb.append('.');
            }
            sb.append(typeName[i]);
        }
        return sb.toString();
    }

    @Override
    public RadixIcon getIcon() {
        return RadixObjectIcon.JAVA.CLASS;
    }

    @Override
    public Scml.Item[] getNewItems() {
        List<Scml.Item> result = new LinkedList<>();
        if (isMemberType) {
            for (int i = 0; i < typeName.length; i++) {
                if (i > 0) {
                    result.add(Scml.Text.Factory.newInstance("."));
                }
                result.add(Scml.Text.Factory.newInstance(String.valueOf(typeName[i]).replace("$", ".")));
            }
        } else {
            if (enclosingType != null) {
                result.add(new JmlTagTypeDeclaration(enclosingType));
            } else {
                result.add(Scml.Text.Factory.newInstance(String.valueOf(CharOperation.concatWith(packageName, '.'))));
            }
            for (int i = 0; i < typeName.length; i++) {
                result.add(Scml.Text.Factory.newInstance("."));
                result.add(Scml.Text.Factory.newInstance(String.valueOf(typeName[i]).replace("$", ".")));
            }
        }
        return result.toArray(new Scml.Item[result.size()]);
    }
}