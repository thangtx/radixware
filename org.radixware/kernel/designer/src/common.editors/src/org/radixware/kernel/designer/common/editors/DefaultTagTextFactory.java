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

package org.radixware.kernel.designer.common.editors;

import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane;
import org.radixware.kernel.designer.common.dialogs.scmlnb.finder.TagTextFactory;
import org.radixware.kernel.designer.common.editors.jml.JmlEditor;
import org.radixware.kernel.designer.common.editors.sqml.SqmlEditorPanel;


public class DefaultTagTextFactory implements TagTextFactory {

        private SqmlEditorPanel.SqmlVTagFactory sqmlVTagFactory = new SqmlEditorPanel.SqmlVTagFactory();
        private JmlEditor.JmlVTagFactory jmlVTagFactory = new JmlEditor.JmlVTagFactory();

        @Override
        public String getText(final Scml.Tag tag) {
            ScmlEditorPane.VTag vtag;
            if (tag instanceof Jml.Tag) {
                vtag = jmlVTagFactory.createVTag(tag);
            } else {
                vtag = sqmlVTagFactory.createVTag(tag);
            }
            if (vtag == null || vtag instanceof ScmlEditorPane.ErrorVTag) {
                throw new IllegalArgumentException("Can not create vtag for " + tag);
            }
            return vtag.getTitle();
        }
    };
