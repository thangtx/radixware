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

package org.radixware.kernel.common.build.xbeans;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.apache.xmlbeans.impl.util.FilerImpl;

import repackage.Repackager;

public class FilerFactory {

    private static class StubFiler extends FilerImpl {

        private static class StubStream extends OutputStream {

            @Override
            public void write(int b) throws IOException {
                // System.out.write(b);//for debugging
            }
        }

        private static class StubWriter extends Writer {

            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                // ignore
            }

            @Override
            public void flush() throws IOException {
                //ignore
            }

            @Override
            public void close() throws IOException {
                //ignore
            }
        }

        public StubFiler(File classdir, File srcdir, Repackager repackager, boolean verbose, boolean incrSrcGen) {
            super(classdir, srcdir, repackager, verbose, incrSrcGen);
        }

        @Override
        public OutputStream createBinaryFile(String typename) throws IOException {
            //System.out.println(typename);
            //return new StubStream();
            return super.createBinaryFile(typename);
        }

        @Override
        public Writer createSourceFile(String typename) throws IOException {
            return new StubWriter();
        }
    }
    private final DirManager dirManager;
    private final String fsType;

    public FilerFactory(String type) {
        if ("fs".equalsIgnoreCase(type)) {
            dirManager = new FSDirManager();
        } else if ("virtual".equalsIgnoreCase(type)) {
            dirManager = new FSDirManager();//new VirtualDirManager();
        } else {
            throw new IllegalArgumentException("Unknown File System Type: "
                    + type);
        }
        fsType = type;
    }

    public FilerImpl getFiler(File classdir, File srcdir, Repackager repackager, boolean verbose, boolean incrSrcGen) {
        if ("fs".equalsIgnoreCase(fsType)) {
            return new FilerImpl(classdir, srcdir, repackager, verbose, incrSrcGen);
        } else if ("virtual".equalsIgnoreCase(fsType)) {
            return new StubFiler(classdir, srcdir, repackager, verbose, incrSrcGen);
        } //		else if (fsType.equalsIgnoreCase("oracle"))
        //			return new OracleFilerImpl((OracleDirManager)dirManager, classdir, srcdir, repackager, verbose, incrSrcGen);
        else {
            return null;
        }
    }

    DirManager getDirManager() {
        return dirManager;
    }

    static private String normalizePart(String p, boolean fromStart) {
        String sregex = "\\".equals(File.separator) ? "\\\\" : File.separator;
        String s = p.replaceAll(sregex + "+", File.separator);
        s = s.replaceAll("(?<!^)" + sregex + "$", "");
        if (fromStart) {
            s = s.replaceAll("^" + sregex, "");
        }
        return s; // string 'a/b///c/' to 'a/b/c'
    }

    static public String joinPath(String... parts) {
        StringBuilder b = null;

        for (String part : parts) {
            if (part.length() == 0) {
                continue;
            }
            if (b == null || part.charAt(0) == File.separatorChar) {
                b = new StringBuilder(normalizePart(part, false));
            } else {
                b.append(File.separator).append(normalizePart(part, true));
            }
        }
        return b != null ? b.toString() : "";
    }
}
