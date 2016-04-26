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

import java.util.*;


public class RegExpUtils {

    private static abstract class INameItem {

        abstract String getName(boolean full);
    }

    private static class NameItem extends INameItem {

        String name;

        NameItem(String name) {
            this.name = name;
        }

        @Override
        public String getName(boolean full) {
            return name;
        }
    }

    private static class PrefItem extends INameItem {

        String pref;
        Set<String> names;

        PrefItem(String pref, Set<String> names) {
            this.pref = pref;
            this.names = names;
        }

        @Override
        public String getName(boolean full) {

            if (full) {
                final StringBuilder builder = new StringBuilder();

                builder.append(pref);
                builder.append("(");

                boolean first = true;
                for (final String name : names) {
                    if (first) {
                        first = false;
                    } else {
                        builder.append('|');
                    }
                    builder.append(name.substring(pref.length()));
                }

                builder.append(")");

                return builder.toString();
            } else {
                return pref;
            }
        }
    }

    public static final class AnalysisNamesResult {

        private Set<INameItem> names = new HashSet<>();
        private Set<INameItem> prefs = new HashSet<>();

        private void add(String name) {
            names.add(new NameItem(name));
        }

        private void add(String pref, Set<String> names) {
            prefs.add(new PrefItem(pref, names));
        }

        public String getExpress() {
            return getExpress(false, true);
        }

        private String getExpress(boolean full, boolean partition) {
            StringBuilder builder = new StringBuilder();

            if (full) {
                builder.append("^(");
                if (!names.isEmpty()) {
                    join(names, builder, full, partition);
                }

                if (!prefs.isEmpty()) {
                    if (!names.isEmpty()) {
                        builder.append('|');
                    }
                    join(prefs, builder, full, partition);
                }
                builder.append(")$");
            } else {
                if (!names.isEmpty()) {
                    builder.append("^(");
                    join(names, builder, full, partition);
                    builder.append(")$");
                }

                if (!prefs.isEmpty()) {
                    if (!names.isEmpty()) {
                        builder.append('|');
                    }
                    builder.append("(^(");
                    join(prefs, builder, full, partition);
                    builder.append(").*)");
                }
            }

            return builder.toString();
        }

        private void join(Set<INameItem> items, StringBuilder builder, boolean full, boolean partition) {

            if (partition) {
                partition(items, builder);
                return;
            }

            boolean first = true;
            for (INameItem item : items) {
                if (first) {
                    first = false;
                } else {
                    builder.append('|');
                }

                builder.append(item.getName(full));
            }
        }

        private void partition(Set<INameItem> items, StringBuilder builder) {
            final List<String> sordedNames = new LinkedList<>();
            for (final INameItem name : items) {
                sordedNames.add(name.getName(false));
            }

            Collections.sort(sordedNames);

            final AnalysisNamesResult analyse = PrefixTree.analyse(sordedNames, Collections.EMPTY_LIST, 3, 2);

            join(analyse.names, builder, true, false);
            if (!analyse.names.isEmpty() && !analyse.prefs.isEmpty()) {
                builder.append('|');
            }
            join(analyse.prefs, builder, true, false);
        }
    }

    private static class PrefixTree {

        PrefixTreeNode root = new PrefixTreeNode(null, null);

        public PrefixTree() {
        }

        static class PrefixTreeNode {

            private final Character ch;
            private final Map<Character, PrefixTreeNode> childs = new HashMap<>();
            private boolean isWord = false;
            private final PrefixTreeNode parent;

            public PrefixTreeNode(Character ch, PrefixTreeNode parent) {
                this.ch = ch;
                this.parent = parent;
            }

            String getPrefix() {
                StringBuilder builder = new StringBuilder();

                builder.append(ch);

                PrefixTreeNode curr = parent;
                while (curr != null && !curr.isRoot()) {
                    builder.insert(0, curr.ch);
                    curr = curr.parent;
                }
                return builder.toString();
            }

            boolean isRoot() {
                return parent == null;
            }

            boolean isLeaf() {
                return childs.isEmpty();
            }

            PrefixTreeNode getChild(char ch) {
                return childs.get(ch);
            }

            public void insert(int index, String word) {
                if (word.length() <= index) {
                    return;
                }

                if (word.length() > index + 1) {
                    findOrCreate(word.charAt(index + 1)).insert(index + 1, word);
                } else {
                    isWord = true;
                }
            }

            private PrefixTreeNode findOrCreate(char ch) {
                PrefixTreeNode child = getChild(ch);
                if (child == null) {
                    child = new PrefixTreeNode(ch, this);
                    childs.put(ch, child);
                }

                return child;
            }

            int getLevel() {
                int level = 0;
                PrefixTreeNode curr = parent;
                while (curr != null && !curr.isRoot()) {
                    ++level;
                    curr = curr.parent;
                }

                return level;
            }

            public Set<String> getWords() {
                final Set<String> words = new HashSet<>();

                walk(new IAcceptor() {

                    @Override
                    public boolean accept(PrefixTreeNode node) {
                        if (node.isWord()) {
                            words.add(node.getPrefix());
                        }

                        return true;
                    }
                }, this);

                return words;
            }

            public boolean isWord() {
                return isWord;
            }
        }

        public void build(List<String> words) {
            for (final String word : words) {
                insert(word);
            }
        }

        public void insert(String word) {
            if (word != null && !word.isEmpty()) {
                root.findOrCreate(word.charAt(0)).insert(0, word);
            }
        }

        public void walk(IAcceptor acceptor) {
            walk(acceptor, root);
        }

        public static void walk(IAcceptor acceptor, PrefixTreeNode start) {
            final Stack<PrefixTreeNode> stack = new Stack<>();

            stack.push(start);

            while (!stack.isEmpty()) {
                final PrefixTreeNode curr = stack.pop();

                if (acceptor.accept(curr)) {
                    for (PrefixTreeNode child : curr.childs.values()) {
                        stack.push(child);
                    }
                }
            }
        }

        interface IAcceptor {

            boolean accept(PrefixTreeNode node);
        }

        static AnalysisNamesResult analyse(final List<String> names, final List<String> illegal, final int minLen, final int minCount) {
            final PrefixTree prefixTree = new PrefixTree();

            prefixTree.build(names);

            final AnalysisNamesResult result = new AnalysisNamesResult();

            prefixTree.walk(new PrefixTree.IAcceptor() {

                @Override
                public boolean accept(PrefixTreeNode node) {


                    final String prefix = node.getPrefix();

                    if (node.isRoot()) {
                        return true;
                    }

                    if (node.isLeaf()) {
                        final Set<String> words = node.getWords();
                        assert words.size() == 1;
                        result.add(words.iterator().next());
                        return false;
                    }

                    if (getValidLen(illegal, prefix) < minLen) {
                        return true;
                    }

                    if (node.childs.size() + (node.isWord() ? 1 : 0) > 1) {
                        result.add(prefix, node.getWords());
                        return false;
                    }

                    return true;
                }
            });

            return result;
        }
    }

    private static int getValidLen(final List<String> illegal, String prefix) {
        for (final String str : illegal) {
            if (prefix.startsWith(str)) {
                return prefix.length() - str.length();
            }
        }
        return prefix.length();
    }

    public static AnalysisNamesResult analyseNames(final List<String> names, final List<String> exclude, final int minLen, final int minCount) {
        return PrefixTree.analyse(names, exclude, minLen, minCount);
    }
}
