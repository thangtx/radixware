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

package org.radixware.kernel.utils.spellchecker.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


class DictionarySorter {

    private static final Comparator<String> comparator = new Comparator<String>() {

        @Override
        public int compare(String s1, String s2) {
            return s1.toLowerCase().compareTo(s2.toLowerCase());
        }
    };

    public static void sort(File[] in, File out) throws IOException {
        Set<String> words = new HashSet<String>();
        for (File inf : in) {
            FileInputStream istream = new FileInputStream(inf);
            InputStreamReader streamReader = new InputStreamReader(istream);
            BufferedReader reader = new BufferedReader(streamReader);

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                words.add(line);
            }
            reader.close();
            streamReader.close();
            istream.close();
        }
        List<String> list = new ArrayList<String>(words);
        Collections.sort(list, comparator);
        FileWriter writer = new FileWriter(out);
        for (String s : list) {
            writer.append(s).append('\n');
        }
        writer.close();
    }

    public static void main(String[] args) {
        File[] in = new File[]{new File("/home/akrylov/radix/trunk/org.radixware/kernel/utils/src/spellcheck/org/radixware/kernel/utils/spellchecker/dict/rdx-abb.dic")};
//                new File("/home/akrylov/Desktop/en").listFiles(new FilenameFilter() {
//
//            @Override
//            public boolean accept(File dir, String name) {
//                return !name.endsWith("~");
//            }
//        });
        File out = new File("/home/akrylov/radix/trunk/org.radixware/kernel/utils/src/spellcheck/org/radixware/kernel/utils/spellchecker/dict/rdx-abb.dic");
        try {
            sort(in, out);
        } catch (IOException ex) {
            Logger.getLogger(DictionarySorter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
