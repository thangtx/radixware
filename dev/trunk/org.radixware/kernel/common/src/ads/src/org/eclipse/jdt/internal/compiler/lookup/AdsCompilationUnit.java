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

import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;

public class AdsCompilationUnit implements ICompilationUnit {

    private Definition source;
    private String extName;
    private JavaSourceSupport.UsagePurpose up;
    private static final char[] UNIT_CONTENTS = new char[0];

    public AdsCompilationUnit(Definition source, JavaSourceSupport.UsagePurpose up) {
        this.source = source;
        this.up = up;
    }

    public AdsCompilationUnit(Definition source, JavaSourceSupport.UsagePurpose up, String extName) {
        this.source = source;
        this.extName = extName;
        this.up = up;
    }
    public AdsCompilationUnitDeclaration declaration;

    public String getExtName() {
        return extName;
    }

    @Override
    public char[] getContents() {
        return UNIT_CONTENTS;
    }

    @Override
    public char[] getMainTypeName() {
        return null;
    }

    @Override
    public char[][] getPackageName() {
        return null;
    }

    @Override
    public boolean ignoreOptionalProblems() {
        if (up.getCodeType() != JavaSourceSupport.CodeType.EXCUTABLE) {
            return true;
        }
        if (source instanceof IXmlDefinition || source instanceof AdsLocalizingBundleDef) {
            return true;
        }
        return false;
    }

    @Override
    public char[] getFileName() {
        if (source == null) {
            return extName.toCharArray();
        }
        return source.getQualifiedName().toCharArray();
    }
}
