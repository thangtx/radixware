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

package org.radixware.kernel.common.compiler.core;

import java.io.UnsupportedEncodingException;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.lookup.AdsCompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;


public class JMLClassFile extends ClassFile {

    public JMLClassFile(SourceTypeBinding typeBinding) {
        super(typeBinding);
    }

    /**
     * INTERNAL USE-ONLY This methods generate all the attributes for the
     * receiver. For a class they could be: - source file attribute - inner
     * classes attribute - deprecated attribute
     */
    @Override
    public void addAttributes() {
        int attributeOffset = this.contentsOffset;        

        final char[] smap = ((AdsCompilationUnitDeclaration) referenceBinding.scope.referenceCompilationUnit()).getSMAPData();
        final boolean generateSMAPAttr = smap != null && smap.length > 0;

        if (generateSMAPAttr) {//force modify constant pool to register required attribute name
            constantPool.literalIndex(SourceDebugExtension);
        }

        super.addAttributes();
        //get alredy generated attributes count
        int attributesNumber = (this.contents[attributeOffset] << 8 | (this.contents[attributeOffset + 1] & 0xFF));

        if (generateSMAPAttr) {
            generateSDEAttribute(smap);
            attributesNumber++;
            if (attributeOffset + 2 >= this.contents.length) {
                resizeContents(2);
            }
            this.contents[attributeOffset++] = (byte) (attributesNumber >> 8);
            this.contents[attributeOffset] = (byte) attributesNumber;
        }

    }
    private static final char[] SourceDebugExtension = "SourceDebugExtension".toCharArray(); //

    private void generateSDEAttribute(char[] smap) {
        String smapString = new String(smap);
        try {
            byte[] smapBytes = smapString.getBytes("UTF-8");
            if (contentsOffset + 6 + smapBytes.length >= contents.length) {
                resizeContents(6 + smapBytes.length);
            }

            final int sourceAttributeNameIndex = constantPool.literalIndex(SourceDebugExtension);
            contents[contentsOffset++] = (byte) (sourceAttributeNameIndex >> 8);
            contents[contentsOffset++] = (byte) sourceAttributeNameIndex;
            final int attributeLength = smapBytes.length;
            contents[contentsOffset++] = (byte) (attributeLength >> 24);
            contents[contentsOffset++] = (byte) (attributeLength >> 16);
            contents[contentsOffset++] = (byte) (attributeLength >> 8);
            contents[contentsOffset++] = (byte) attributeLength;
            // write the source file name
            for (int i = 0; i < smapBytes.length; i++) {
                contents[contentsOffset++] = smapBytes[i];
            }
        } catch (UnsupportedEncodingException ex) {
        }

    }

    private final void resizeContents(int minimalSize) {
        int length = this.contents.length;
        int toAdd = length;
        if (toAdd < minimalSize) {
            toAdd = minimalSize;
        }
        System.arraycopy(this.contents, 0, this.contents = new byte[length + toAdd], 0, length);
    }
}
