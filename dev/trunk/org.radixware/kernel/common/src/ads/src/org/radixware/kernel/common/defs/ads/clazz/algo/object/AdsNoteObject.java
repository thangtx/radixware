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

package org.radixware.kernel.common.defs.ads.clazz.algo.object;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


public class AdsNoteObject extends AdsBaseObject {
    
    private String text;

    protected AdsNoteObject() {
        this(ObjectFactory.createNodeId(), EMPTY_NAME);
    }

    protected AdsNoteObject(Id id, String name) {
        super(Kind.NOTE, id, EMPTY_NAME);
    }

    protected AdsNoteObject(final AdsNoteObject node) {
        super(node);
        text = node.getText();
    }

    protected AdsNoteObject(org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode) {
        super(xNode);
        text = xNode.getText();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (!Utils.equals(this.text, text)) {
            this.text = text;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public void appendTo(org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode,ESaveMode saveMode) {
        super.appendTo(xNode,saveMode);
        xNode.setText(text);
    }

    private static final String TYPE_TITLE = "Note Node";
    @Override
    public String getTypeTitle() {
        return TYPE_TITLE;
    }
}