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

package org.radixware.kernel.common.defs.ads.src;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;

import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.jml.IJmlSource;
import org.radixware.kernel.common.jml.Jml;

import org.radixware.kernel.common.scml.IScmlPosition;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Item;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.CharOperations;


public class SrcPositionLocator {

    public static class SrcLocation implements Scml.ScmlAreaInfo, IScmlPosition {

        private RadixObject radixObject = null;
        private int sourceStartOffset = -1;
        private int sourceEndOffset = -1;
        private Jml.Item startJmlItem = null;
        private Jml.Item endJmlItem = null;
        private Jml jml = null;

        private SrcLocation() {
        }

        public SrcLocation(Jml jml, int startItemIdx, int startOffset, int endItemIdx, int endOffset) {
            this.jml = jml;
            int size = jml.getItems().size();
            this.startJmlItem = startItemIdx >= 0 && startItemIdx < size ? jml.getItems().get(startItemIdx) : null;
            this.endJmlItem = endItemIdx >= 0 && endItemIdx < size ? jml.getItems().get(endItemIdx) : null;
            this.sourceStartOffset = startOffset;
            this.sourceEndOffset = endOffset;
        }
        
        public RadixObject getRadixObject() {
            return radixObject;
        }

        @Override
        public int getSourceStartOffset() {
            return sourceStartOffset;
        }

        @Override
        public int getSourceEndOffset() {
            return sourceEndOffset;
        }

        @Override
        public Jml.Item getStartJmlItem() {
            return startJmlItem;
        }

        @Override
        public Jml.Item getEndJmlItem() {
            return endJmlItem;
        }

        @Override
        public Jml getScml() {
            return jml;
        }

        @Override
        public Item getItem() {
            return getStartJmlItem();
        }

        public int getStartItemIdx() {
            if (jml == null) {
                return 0;
            }
            return jml.getItems().indexOf(startJmlItem);
        }

        public int getEndItemIdx() {
            if (jml == null) {
                return 0;
            }
            return jml.getItems().indexOf(endJmlItem);
        }

        @Override
        public int getInternalOffset() {
            return getSourceStartOffset();
        }
    }

    public static final class Factory {

        public static final SrcPositionLocator newInstance(IJavaSource root, char[] source) {
            return new SrcPositionLocator(source, (Definition) root);
        }
    }
    private Id[] id_stack = new Id[10];
    private int id_stack_top = 0;
    private final char[] source;
    private final Definition root;

    private SrcPositionLocator(char[] source, Definition root) {
        this.source = source;
        this.root = root;
    }

    private void putId(int start, int end) {
        if (id_stack_top == id_stack.length) {
            Id[] newArr = new Id[id_stack_top * 2];
            System.arraycopy(id_stack, 0, newArr, 0, id_stack_top);
            id_stack = newArr;
        }
        id_stack[id_stack_top] = Id.Factory.loadFrom(String.valueOf(source, start, end - start));
        id_stack_top++;
    }

    private int findScopeMarkerEnd(int from) {
        return CharOperations.reverseFind(source, RadixObjectWriter.DEFINITION_LOCATOR_END_MARKER, from);
    }

    private int findScopeMarkerStart(int from) {
        return CharOperations.reverseFind(source, RadixObjectWriter.DEFINITION_LOCATOR_START_MARKER, from);
    }

    private int findTagEnd(int from, int to) {
        return CharOperations.reverseFind(source, Jml.Tag.TAG_TAIL_MARKER, from, to);
    }

    private int findTagStart(int from, int to) {
        return CharOperations.reverseFind(source, Jml.Tag.TAG_LEAD_MARKER, from, to);
    }
    private int itemIndex = -1;
    private int offset = -1;

    void calcTagIndexAndOffset(int calcFrom, int origFrom, boolean insideOfTag) {
        itemIndex = -1;
        offset = -1;
        int posAdd;
        int indexAdd;
        if (insideOfTag) {
            posAdd = Jml.Tag.TAG_LEAD_MARKER.length;
            indexAdd = 0;
        } else {
            posAdd = Jml.Tag.TAG_TAIL_MARKER.length;
            indexAdd = 1;
        }
        if (calcFrom >= 0) {
            int tagIndexStart = calcFrom + posAdd;
            int tagIndexEnd = tagIndexStart;
            for (int i = tagIndexStart; i < source.length; i++) {
                if (source[i] == '#') {
                    break;
                }
                tagIndexEnd = i;
            }
            if (tagIndexEnd >= tagIndexStart) {
                char[] numChars = new char[tagIndexEnd - tagIndexStart + 1];
                System.arraycopy(source, tagIndexStart, numChars, 0, numChars.length);
                try {

                    itemIndex = Integer.parseInt(String.valueOf(numChars)) + indexAdd;
                    if (itemIndex >= 0) {
                        offset = origFrom - (tagIndexEnd + Jml.Tag.TAG_MARKER_TAIL.length);
                    }
                    //loc.sourceLength = to - from;
                } catch (NumberFormatException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        }
    }
    public static final char[] IL_MARKER = "/*$il$*/".toCharArray();
    private static final int IL_MARKER_LEN = IL_MARKER.length;

    private boolean isLineStartMarker(char[] text, int curPos) {
        if (curPos - IL_MARKER_LEN >= 0) {
            for (int i = 0, j = IL_MARKER_LEN - 1; i < IL_MARKER_LEN; i++, j--) {
                if (text[curPos - i] != IL_MARKER[j]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public SrcLocation calc(int from, int to) {
        SrcLocation loc = new SrcLocation();
        loc.radixObject = root;

        if (source == null || root == null) {
            return loc;
        }
        id_stack_top = 0;

        int scopeMarkerEnd = findScopeMarkerEnd(from);
        if (scopeMarkerEnd < 0) {
            return loc;
        }

        int scopeMarkerStart = findScopeMarkerStart(scopeMarkerEnd);

        if (scopeMarkerStart < 0) {
            return loc;
        }

        int scopeDescriptorStart = scopeMarkerStart + RadixObjectWriter.DEFINITION_LOCATOR_START_MARKER.length;
        int scopeDescriptorEnd = scopeMarkerEnd;

        int localStart = scopeDescriptorStart;
        String marker = null;
        for (int i = scopeDescriptorStart + 1; i <= scopeDescriptorEnd; i++) {
            if (source[i] == '-') {
                putId(localStart, i);
                localStart = i + 1;
            } else if (source[i] == ':') {
                putId(localStart, i);
                marker = String.valueOf(source, i + 1, scopeDescriptorEnd - i - 2);
                break;
            }
        }



        if (id_stack_top > 0) {
            Id[] usedIds = new Id[id_stack_top];
            System.arraycopy(id_stack, 0, usedIds, 0, id_stack_top);
            //  usedIds[0] = root.getId();
            Definition def = AdsPath.resolve(root, usedIds).get();
            if (def instanceof Definition) {
                loc.radixObject = def;
                // tag information calculation

                if (loc.radixObject instanceof IJmlSource) {
                    int tagEndMarkerIndex = findTagEnd(from, scopeDescriptorEnd);
                    int tagStartMarkerIndex = findTagStart(from, scopeDescriptorEnd);

                    boolean inside = tagEndMarkerIndex < tagStartMarkerIndex;

                    calcTagIndexAndOffset(inside ? tagStartMarkerIndex : tagEndMarkerIndex, from, inside);

                    Jml jml = ((IJmlSource) loc.radixObject).getSource(marker);


                    //assert jml != null : "No jml on " + marker;
                    if (jml == null) {
                        return loc;
                    }
                    loc.radixObject = jml.getContainer();

                    loc.jml = jml;
                    if (itemIndex >= 0) {
                        loc.jml = jml;
                        if (jml.getItems().size() > itemIndex) {
                            loc.startJmlItem = jml.getItems().get(itemIndex);
                            loc.sourceStartOffset = offset >= 0 ? offset : 0;

                            if (loc.startJmlItem instanceof Jml.Text) {
                                //compute position correction;
                                int realOffset = offset;
                                for (int i = from, counter = 0; i >= 0 && counter < offset; i--, counter++) {
                                    if (/*
                                             * isLineStartMarker(source, i)
                                             */source[i] == '\t') {
                                        for (int j = i; j >= 0 && counter < offset; j--, counter++) {
                                            if (source[j] == '\n') {
                                                i = j - 1;
                                                break;
                                            } else {
                                                realOffset--;
                                            }
                                        }
                                    }
                                }
                                loc.sourceStartOffset = realOffset - 1;
                            }
                        }
                    }

                    tagEndMarkerIndex = findTagEnd(to, scopeDescriptorEnd);
                    tagStartMarkerIndex = findTagStart(to, scopeDescriptorEnd);

                    inside = tagEndMarkerIndex < tagStartMarkerIndex;

                    calcTagIndexAndOffset(inside ? tagStartMarkerIndex : tagEndMarkerIndex, to, inside);

                    if (itemIndex >= 0) {
                        loc.jml = jml;
                        if (jml.getItems().size() > itemIndex) {
                            loc.endJmlItem = jml.getItems().get(itemIndex);
                            loc.sourceEndOffset = offset >= 0 ? offset : 0;

                            if (loc.endJmlItem instanceof Jml.Text) {
                                //compute position correction;

                                int realOffset = offset;
                                for (int i = to, counter = 0; i >= 0 && counter < offset; i--, counter++) {
                                    if (/*
                                             * isLineStartMarker(source, i)
                                             */source[i] == '\t') {
                                        for (int j = i; j >= 0 && counter < offset; j--, counter++) {
                                            if (source[j] == '\n') {
                                                i = j - 1;
                                                break;
                                            } else {
                                                realOffset--;
                                            }
                                        }
                                    }
                                }
                                loc.sourceEndOffset = realOffset;
                            }
                        }
                    }
                }
                return loc;
            } else {
                return loc;
            }
        } else {
            return loc;
        }
    }
}
