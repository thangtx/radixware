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

import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.Scml;


public class JmlPackageCompletionItem extends AbstractCompletionItem {

    private String packageName;
    private String parentPackage;

    public JmlPackageCompletionItem(String packageName, String parentPackage, int relevance, int replaceStart, int replaceEnd) {
        super(relevance, replaceStart, replaceEnd);
        this.leadText = packageName;
        this.sortText = packageName;
        this.tailText = parentPackage;
        this.packageName = packageName;
        this.parentPackage = parentPackage;
    }

    @Override
    public RadixIcon getIcon() {
        return RadixObjectIcon.JAVA.PACKAGE;
    }

    @Override
    public Scml.Item[] getNewItems() {
        return new Scml.Item[]{
            Scml.Text.Factory.newInstance(parentPackage + "." + packageName)
        };
    }
}
