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
package org.radixware.kernel.common.scml;

import org.radixware.kernel.common.utils.Utils;

/**
 * Parser of scml for tags, tags in comments, and text.
 */
public abstract class ScmlProcessor {

    /**
     * Get comments analizer. Called only before parse.
     */
    protected abstract CommentsAnalizer getCommentsAnalizer();

    protected abstract void processText(Scml.Text text);

    protected abstract void processTag(Scml.Tag tag);

    protected void processTagInComment(Scml.Tag tag) {
    }

    public final void process(final Scml scml) {
        if (scml == null) {
            return;
        }

        final CommentsAnalizer commentsAnalizer = getCommentsAnalizer();

        for (Scml.Item item : scml.getItems()) {
            if (item instanceof Scml.Text) {
                String text = ((Scml.Text) item).getText();
                if (Utils.isNotNull(commentsAnalizer)) {
                    commentsAnalizer.process(text);
                }
                processText((Scml.Text) item);
            } else {
                if (Utils.isNotNull(commentsAnalizer) && commentsAnalizer.isInComment()) { // ignore tags in comments
                    processTagInComment((Scml.Tag) item);
                } else {
                    processTag((Scml.Tag) item);
                }
            }
        }
    }
}
