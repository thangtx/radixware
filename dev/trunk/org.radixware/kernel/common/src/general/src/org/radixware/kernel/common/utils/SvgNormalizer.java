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

package org.radixware.kernel.common.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SvgNormalizer {

    public static void main(String[] args) {
        process(new String[]{"/home/akrylov/ssd/radix/dev/trunk/org.radixware/kernel/common/src/client/src/org/radixware/kernel/common/client/images/"});
    }

    public static void process(String[] args) {
        if (args.length == 0) {
            System.out.println("Pass svg file or directory containing svg files as command line argumet");

        }
        File file = new File(args[0]);
        File[] files;
        if (file.isDirectory()) {
            files = file.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    return pathname.isFile() && pathname.getName().endsWith(".svg");
                }
            });
        } else {
            files = new File[]{file};
        }
        for (File f : files) {
            InputStream input = null;
            String converted = null;
            try {
                input = new FileInputStream(f);
                converted = normalizeSvg(input);

            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException ex) {
                        Logger.getLogger(SvgNormalizer.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                }
            }
            if (converted != null) {
                System.out.println();
                try {
                    System.out.println("File was normalized: " + f.getPath());
                    FileUtils.writeString(f, converted, "UTF-8");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static String readAttrValue(String src, String attrName) {
        int pos = src.indexOf(attrName);
        if (pos < 0) {
            return null;
        }
        int assign = src.indexOf("=", pos + 1);
        if (assign < 0) {
            return null;
        }
        int valueStart = src.indexOf("\"", assign + 1);
        if (valueStart < 0) {
            return null;
        }
        int valueEnd = src.indexOf("\"", valueStart + 1);
        if (valueEnd < 0) {
            return null;
        }
        return src.substring(valueStart + 1, valueEnd);
    }

    public static String normalizeSvg(InputStream svgInputStream) throws IOException {

        String svgText = FileUtils.readTextStream(svgInputStream, "UTF-8");
        int svgStart = svgText.indexOf("<svg");
        if (svgStart > 0) {
            int innerTag = svgText.indexOf(">", svgStart + 1);
            if (innerTag > 0) {
                String analyse = svgText.substring(svgStart + 4, innerTag);
                int viewBox = analyse.indexOf("viewBox");
                if (viewBox > 0) {
                    return null;
                }
                String width = readAttrValue(analyse, "width");
                if (width == null) {
                    return null;
                }
                String height = readAttrValue(analyse, "height");
                if (height == null) {
                    return null;
                }
                String viewBoxVal = "\nviewBox=\"0 0 " + width + " " + height + "\"\n";
                String head = svgText.substring(0, innerTag);
                String tail = svgText.substring(innerTag);
                return head + viewBoxVal + tail;
            }
        }
        return null;
    }
}
