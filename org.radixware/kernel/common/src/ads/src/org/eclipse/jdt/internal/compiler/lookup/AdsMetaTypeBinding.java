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

package org.eclipse.jdt.internal.compiler.lookup;


public class AdsMetaTypeBinding extends AdsTypeBinding {

    public AdsMetaTypeBinding(AdsTypeDeclaration decl, PackageBinding pkg, AdsDefinitionScope scope) {
        super(decl, pkg, scope);
    }

    @Override
    public FieldBinding resolveTypeFor(FieldBinding field) {
       return super.resolveTypeFor(field); //To change body of generated methods, choose Tools | Templates.
    }
}
