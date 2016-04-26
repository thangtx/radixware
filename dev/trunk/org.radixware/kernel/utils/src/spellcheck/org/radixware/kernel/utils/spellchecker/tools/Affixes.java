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

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.utils.FileUtils;


class Affixes {

    static class Rule {

        boolean excludeList = false;
        char[] list;
        char[] remove;
        char[] append;
        char[] match;
    }

    static class Flag {

        final char liter;
        List<Rule> rules = new LinkedList<Rule>();

        public Flag(char liter) {
            this.liter = liter;
        }
    }

    public static Affixes readISpellAffixes(InputStream stream, String encoding) {
        Affixes affixes = new Affixes();

        String affixFileContent;
        try {
            affixFileContent = FileUtils.readTextStream(stream, encoding);
        } catch (IOException ex) {
            return null;
        }
        String[] affixFileLines = affixFileContent.split("\n");
        affixFileContent = null;
        int mode = 0;


        for (String affixLine : affixFileLines) {
            if (affixLine.startsWith("SFX")) {
                int start = 4;
                char liter = affixLine.charAt(start);
                start++;
                char c = affixLine.charAt(start);
                while (c == ' ' || c == '\t') {
                    start++;
                    c = affixLine.charAt(start);
                }
                if (c == 'Y') {
                    start++;
                    c = affixLine.charAt(start);
                    while (c == ' ' || c == '\t') {
                        start++;
                        c = affixLine.charAt(start);
                    }
                    if (Character.isDigit(c)) {
                        continue;
                    } else {
                        throw new RadixError("Digit expected");
                    }
                }
                Flag flag = affixes.getFlag(liter);
                if (flag == null) {
                    flag = new Flag(liter);
                    affixes.addFlag(flag);
                }
                Rule rule = new Rule();
                if (c == '0') {//nothing to remove
                    start++;
                    rule.remove = new char[0];
                    c = affixLine.charAt(start);
                } else {
                    StringBuilder remove = new StringBuilder();
                    remove.append(c);
                    start++;
                    c = affixLine.charAt(start);
                    while (c != ' ' && c != '\t') {
                        remove.append(c);
                        start++;
                        c = affixLine.charAt(start);
                    }
                    rule.remove = remove.toString().toCharArray();
                }
                c = affixLine.charAt(start);
                while (c == ' ' || c == '\t') {
                    start++;
                    c = affixLine.charAt(start);
                }
                if (c == '0') {//nothing to remove
                    start++;
                    rule.append = new char[0];
                    c = affixLine.charAt(start);
                } else {
                    StringBuilder append = new StringBuilder();
                    append.append(c);
                    start++;
                    c = affixLine.charAt(start);
                    while (c != ' ' && c != '\t' && start < affixLine.length()) {
                        append.append(c);
                        start++;
                        if (start < affixLine.length()) {
                            c = affixLine.charAt(start);
                        }
                    }
                    rule.append = append.toString().toCharArray();
                }
                while ((c == ' ' || c == '\t') && start < affixLine.length()) {
                    start++;
                    c = affixLine.charAt(start);
                }
                if (start < affixLine.length()) {
                    c = affixLine.charAt(start);

                    if (c == '[') {
                        start++;
                        c = affixLine.charAt(start);
                        if (c == '^') {
                            rule.excludeList = true;
                            start++;
                            c = affixLine.charAt(start);

                        }
                        StringBuilder list = new StringBuilder();
                        while (c != ']') {
                            list.append(c);
                            start++;
                            c = affixLine.charAt(start);
                        }
                        start++;

                        rule.list = list.toString().toCharArray();
                        if (start < affixLine.length()) {
                            c = affixLine.charAt(start);
                        }
                    } else {
                        rule.list = new char[0];
                    }
                    StringBuilder match = new StringBuilder();
                    while (start < affixLine.length() && (Character.isLetter(c) || c == '.')) {
                        match.append(c);
                        start++;
                        if (start < affixLine.length()) {
                            c = affixLine.charAt(start);
                        }
                    }
                    rule.match = match.toString().toCharArray();
                    if (rule.match.length == 0 && rule.list.length == 0) {
                        System.out.println();
                    }
                }
                flag.rules.add(rule);
            }
        }

        return affixes;
    }

    public static Affixes readAffixes(InputStream stream, String encoding) {
        try {
            String affixFileContent = FileUtils.readTextStream(stream, encoding);
            String[] affixFileLines = affixFileContent.split("\n");
            affixFileContent = null;
            int mode = 0;
            Affixes affixes = new Affixes();
            Flag flag = null;
            for (String affixLine : affixFileLines) {
                int commentIndex = affixLine.indexOf("#");
                String workingLine;
                if (commentIndex == 0) {
                    continue;
                } else if (commentIndex > 0) {
                    workingLine = affixLine.substring(0, commentIndex);
                } else {
                    workingLine = affixLine;
                }
                workingLine = workingLine.trim();
                if (workingLine.isEmpty()) {
                    continue;
                }
                if (mode == 0) {
                    if (workingLine.startsWith("nroffchars") || workingLine.startsWith("texchars")) {
                        continue;
                    }

                    if ("suffixes".equals(workingLine)) {
                        mode = 1;
                    } else {
                        throw new IOException("Invalid affix file format: expected \"suffixes\" but \"" + workingLine + "\" found");
                    }
                } else if (mode == 1) {
                    if (workingLine.startsWith("flag *")) {
                        flag = new Flag(workingLine.charAt(6));
                        affixes.addFlag(flag);
                        continue;
                    }

                    if (flag != null) {
                        Rule rule = new Rule();
                        int start = 0;
                        char c = workingLine.charAt(start);
                        StringBuilder list = new StringBuilder();
                        switch (c) {
                            case '['://start implicit or explicit list
                                start++;
                                c = workingLine.charAt(start);


                                if (c == '^') {//exclude flag
                                    start++;
                                    rule.excludeList = true;
                                    c = workingLine.charAt(start);
                                }
                                while (c != ']') {
                                    list.append(c);
                                    start++;
                                    c = workingLine.charAt(start);
                                }
                                start++;
                                break;
                        }
                        StringBuilder matchSuffix = new StringBuilder();
                        StringBuilder removePart = new StringBuilder();
                        StringBuilder appendPart = new StringBuilder();
                        boolean matchPart = true;
                        boolean isRemovePart = false;
                        while (start < workingLine.length()) {
                            c = workingLine.charAt(start);
                            start++;
                            if (c == ' ' || c == '\t') {

                                continue;
                            } else if (c == '>') {
                                matchPart = false;

                            } else {
                                if (matchPart) {
                                    matchSuffix.append(c);

                                } else {
                                    if (c == '-') {
                                        isRemovePart = true;

                                    } else if (c == ',') {
                                        if (isRemovePart) {
                                            isRemovePart = false;
                                        } else {
                                            throw new IOException("Unexpected \",\" in flag " + flag.liter);
                                        }
                                    } else {
                                        if (isRemovePart) {
                                            removePart.append(c);
                                        } else {
                                            appendPart.append(c);
                                        }

                                    }
                                }
                            }
                        }
                        rule.list = list.toString().toCharArray();
                        rule.remove = removePart.toString().toCharArray();
                        rule.append = appendPart.toString().toCharArray();
                        rule.match = matchSuffix.toString().toCharArray();
                        flag.rules.add(rule);
                    }
                } else {
                    throw new IOException("Invalid affix file format");
                }
            }
            return affixes;
        } catch (IOException ex) {
            return null;
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {
            }
        }
    }
    private Flag[] flags = new Flag[Character.MAX_VALUE];

    private void addFlag(Flag f) {
        if (flags[f.liter] != null) {
            throw new RadixError("Duplicate flag for liter " + f.liter);
        }
        flags[f.liter] = f;
    }

    Flag getFlag(char liter) {
        return flags[liter];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < flags.length; i++) {
            Flag fl = flags[i];
            if (fl == null) {
                continue;
            }
            sb.append("flag *");
            sb.append(fl.liter);
            sb.append('\n');
            for (Rule r : fl.rules) {
                sb.append("    ");
                if (r.list.length > 0) {
                    sb.append('[');
                    if (r.excludeList) {
                        sb.append('^');
                    }
                    for (int j = 0; j < r.list.length; j++) {
                        sb.append(r.list[j]);
                    }
                    sb.append(']');
                    sb.append(' ');
                }
                for (int j = 0; j < r.match.length; j++) {
                    sb.append(r.match[j]);
                    sb.append(' ');
                }
                sb.append(" > ");
                if (r.remove.length > 0) {
                    for (int j = 0; j < r.remove.length; j++) {
                        sb.append(r.remove[j]);
                    }
                }
                if (r.append.length > 0) {
                    if (r.remove.length > 0) {
                        sb.append(',');
                    }
                    for (int j = 0; j < r.append.length; j++) {
                        sb.append(r.append[j]);
                    }
                }
                sb.append('\n');
            }
            sb.append('\n');

        }
        return sb.toString();
    }
}
