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

package org.radixware.kernel.designer.common.dialogs.choosetype;

import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;


public final class TypeEditorModel {

    private final ITypeFilter filter;

    private AdsTypeDeclaration type;
    private ETypeNature nature;

    public TypeEditorModel(AdsTypeDeclaration type, ITypeFilter filter) {
        this.type = type;
        this.filter = filter;
    }

    public AdsTypeDeclaration getType() {
        return type;
    }

    public void setType(AdsTypeDeclaration type) {
        this.type = type;
    }

    public ITypeFilter getTypeFilter() {
        return filter;
    }

    public ETypeNature getNature() {
        return nature;
    }

    public void setNature(ETypeNature nature) {
        this.nature = nature;
    }
}