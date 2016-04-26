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

package org.radixware.kernel.common.compiler.core.lookup.locations;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException;
import org.eclipse.jdt.internal.compiler.util.Util;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.utils.FileUtils;


public class AdsClassFileReader extends ClassFileReader {

    public final Definition sourceDefinition;

    public AdsClassFileReader(Definition sourceDefinition, byte[] classFileBytes, char[] fileName) throws ClassFormatException {
        super(classFileBytes, fileName);
        this.sourceDefinition = sourceDefinition;
    }

    public AdsClassFileReader(Definition sourceDefinition, byte[] classFileBytes, char[] fileName, boolean fullyInitialize) throws ClassFormatException {
        super(classFileBytes, fileName, fullyInitialize);
        this.sourceDefinition = sourceDefinition;
    }

    public static AdsClassFileReader read(Definition definition, File file, boolean fullyInitialize) throws ClassFormatException, IOException {
        final byte classFileBytes[] = FileUtils.readBinaryFile(file);
        return new AdsClassFileReader(definition, classFileBytes, file.getAbsolutePath().toCharArray(), fullyInitialize);
    }

    public static AdsClassFileReader read(Definition definition, InputStream file, String fileName, boolean fullyInitialize) throws ClassFormatException, IOException {
        final byte classFileBytes[] = FileUtils.readBinaryStream(file);
        return new AdsClassFileReader(definition, classFileBytes, fileName.toCharArray(), fullyInitialize);
    }

    public static AdsClassFileReader read(Definition definition, File file) throws ClassFormatException, IOException {
        return read(definition, file, false);
    }
}
