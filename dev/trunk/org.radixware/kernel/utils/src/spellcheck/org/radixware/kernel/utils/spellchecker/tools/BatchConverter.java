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

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import org.radixware.kernel.common.utils.FileUtils;


class BatchConverter {

    public static void convert(File[] dictFiles, File affixFile, String encoding, File resultDir,boolean ooAff) throws IOException {
        Affixes affixes = ooAff ? Affixes.readISpellAffixes(new FileInputStream(affixFile), encoding) : Affixes.readAffixes(new FileInputStream(affixFile), encoding);
        for (File dictFile : dictFiles) {
            DictionaryConverter dict = DictionaryConverter.loadDictionary(new FileInputStream(dictFile), encoding, affixes);
            File resultFile = new File(resultDir, dictFile.getName()+".new");
            dict.save(resultFile, FileUtils.XML_ENCODING);
        }
    }

    public static void main(String[] args) {
//        File dir = new File("/home/akrylov/Desktop/rus-dict");
//        File[] koi8Files = dir.listFiles(new FileFilter() {
//
//            @Override
//            public boolean accept(File pathname) {
//                return pathname.isFile() && pathname.getName().endsWith(".koi") && !pathname.getName().startsWith("russian.aff") && !pathname.getName().startsWith("rare")&& !pathname.getName().startsWith("README");
//            }
//        });
//        File aff = new File(dir, "russian.aff.koi");
//        try {
//            convert(koi8Files, aff, "KOI8", new File("/home/akrylov/Desktop/rus-dict-utf8"));
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }

        File dir = new File("/home/akrylov/Desktop/en");
        File[] koi8Files = new File[]{new File(dir,"ru_RU.dic")};
        File aff = new File(dir, "ru_RU.aff");
        try {
            convert(koi8Files, aff, "KOI8", new File("/home/akrylov/Desktop/en"),true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
