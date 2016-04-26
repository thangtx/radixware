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

package org.radixware.kernel.designer.common.dialogs.scmlnb.library;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;

/**
 * A support class for selcting all Tag's text when only part of it became selected
 */
public class TagSelector implements CaretListener {

    private boolean lock = false;
    @Override
    public void caretUpdate(CaretEvent ce) {
        if(lock) {
            return;
        }
        JTextComponent textComponent = (JTextComponent) ce.getSource();
        if(ce.getDot() == ce.getMark()) {
            return;
        }
        int p0, p1;
        if(ce.getDot() < ce.getMark()) {
            p0 = ce.getDot();
            p1 = ce.getMark();
        }
        else {
            p1 = ce.getDot();
            p0 = ce.getMark();
        }

        TagMapper tagMapper = TagMapper.getInstance(textComponent.getDocument());

        TagBounds tb;
        int start = p0,end = p1;

        if(tagMapper.insideTagBounds(p0)) {
            tb = tagMapper.findContainingBounds(p0);
            start = tb.getBeginOffset();
        }

        if(tagMapper.insideTagBounds(p1)) {
            tb = tagMapper.findContainingBounds(p1);
            end = tb.getEndOffset();
        }

        if(p0 != start || p1 != end) {
            lock = true;
            if (ce.getDot() > ce.getMark()) {
                textComponent.getCaret().setDot(start);
                textComponent.getCaret().moveDot(end);
            }
            else {
                textComponent.getCaret().setDot(end);
                textComponent.getCaret().moveDot(start);
            }
            lock = false;
        }



    }

}
