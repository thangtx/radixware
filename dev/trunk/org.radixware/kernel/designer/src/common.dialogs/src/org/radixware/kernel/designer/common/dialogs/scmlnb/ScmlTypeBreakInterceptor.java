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

package org.radixware.kernel.designer.common.dialogs.scmlnb;

import javax.swing.text.BadLocationException;
import org.netbeans.api.editor.mimelookup.MimePath;
import org.netbeans.spi.editor.typinghooks.TypedBreakInterceptor;


public class ScmlTypeBreakInterceptor implements TypedBreakInterceptor {

    @Override
    public boolean beforeInsert(final Context context) throws BadLocationException {
        if (context.getDocument() instanceof ScmlDocument) {
            final ScmlDocument sdoc = (ScmlDocument) context.getDocument();
            return sdoc.getTagMapper().insideTagBounds(context.getBreakInsertOffset());
        }
        return false;
    }

    @Override
    public void insert(final MutableContext context) throws BadLocationException {
        //do nothing
    }

    @Override
    public void afterInsert(final Context context) throws BadLocationException {
        //do nothing
    }

    @Override
    public void cancelled(final Context context) {
        //do nothing
    }

    public static class Factory implements TypedBreakInterceptor.Factory {

        @Override
        public TypedBreakInterceptor createTypedBreakInterceptor(final MimePath mimePath) {
            return new ScmlTypeBreakInterceptor();
        }
    }
}
