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

package org.radixware.kernel.designer.common.dialogs.scmlnb.finder;

import java.util.Iterator;
import java.util.Vector;
import org.radixware.kernel.common.scml.Scml;


public class ScmlTextInput {

    private Scml scml;
    private TagTextFactory tagTextFactory;
    private Iterator<Scml.Item> iterator;
    private Vector<Character> buf = new Vector<Character>();
    private int bufOffs = buf.size();
    private Scml.Item pendingItem;
    private String pendingItemText;
    private int scmlOffset = -1;
    private boolean isTagItem = false;

    public ScmlTextInput(Scml scml, TagTextFactory tagTextFactory) {
        assert scml != null;
        assert tagTextFactory != null;
        this.scml = scml;
        this.tagTextFactory = tagTextFactory;
        this.iterator = scml.getItems().iterator();
    }

    public char next() {
        if (!hasNext()) {
            throw new IllegalStateException("No characters left in input");
        }
        if (bufOffs == buf.size()) {
            readPendingItemToBuffer();
            pendingItem = null;
            bufOffs = 0;
        }

        //if we are not inside of tag, increment scmlOffset
        if (!isTagItem || bufOffs == 0) {
            scmlOffset++;
        }

        return buf.get(bufOffs++);
    }

    public char lookForward(int distance) throws CannotLookForwardException {
        assert distance > 0;
        if (bufOffs + distance - 1 < buf.size()) {
            return buf.get(bufOffs + distance - 1);
        }
        if (hasNext()) {
            distance -= buf.size() - bufOffs;
            if (distance < pendingItemText.length()) {
                return pendingItemText.charAt(distance);
            }
        }
        throw new CannotLookForwardException();
    }

    private void readPendingItemToBuffer() {
        if (pendingItem instanceof Scml.Text) {
            isTagItem = false;
        } else if (pendingItem instanceof Scml.Tag) {
            isTagItem = true;
        } else {
            throw new IllegalStateException("Unknown scml item: " + pendingItem);
        }

        buf.setSize(pendingItemText.length());

        for (int i = 0; i < pendingItemText.length(); i++) {
            buf.setElementAt(pendingItemText.charAt(i), i);
        }
    }

    private String calcItemtext() {
        if (pendingItem instanceof Scml.Text) {
            Scml.Text textItem = (Scml.Text) pendingItem;
            return textItem.getText();
        } else if (pendingItem instanceof Scml.Tag) {
            Scml.Tag tagItem = (Scml.Tag) pendingItem;
            return tagTextFactory.getText(tagItem);
        }
        throw new IllegalStateException();
    }

    public ScmlLocation currentLocation() {
        return new ScmlLocation(scml, scmlOffset, isTagItem ? bufOffs - 1 : 0);
    }

    public boolean hasNext() {
        //current buffer has more characters
        if (bufOffs < buf.size()) {
            return true;
        }

        if (pendingItem != null) {
            return true;
        }

        //search for first non-empty item
        while (iterator.hasNext()) {
            pendingItem = iterator.next();
            pendingItemText = calcItemtext();
            if (pendingItemText.length() > 0) {
                return true;
            }
        }

        pendingItem = null;
        pendingItemText = null;

        //no tags or non-empty text iems left
        return false;
    }

    public static class CannotLookForwardException extends Exception {
    }
}
