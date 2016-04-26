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
package org.radixware.kernel.designer.common.dialogs.jmlnb;

import java.util.Arrays;
import java.util.List;
import org.netbeans.editor.Acceptor;
import org.netbeans.editor.AcceptorFactory;
import org.netbeans.editor.TokenContext;
import org.netbeans.editor.ext.java.JavaLayerTokenContext;
import org.netbeans.editor.ext.java.JavaTokenContext;

public final class JmlNbSettingsInitializer {

    public static Acceptor getAbbrevResetAcceptor() {
        return AcceptorFactory.NON_JAVA_IDENTIFIER;
    }

    public static Acceptor getIdentifierAcceptor() {
        return AcceptorFactory.JAVA_IDENTIFIER;
    }

    public static Acceptor getIndentHotCharsAcceptor() {
        return HOT_CHARS_FOR_INDENT_ACCEPTOR;
    }

    public static List getTokenContextList() {
        return Arrays.asList(new TokenContext[]{
            JavaTokenContext.context,
            JavaLayerTokenContext.context
        });
    }

    private JmlNbSettingsInitializer() {
    }
    
    private static final Acceptor HOT_CHARS_FOR_INDENT_ACCEPTOR = new Acceptor() {
        public boolean accept(char ch) {
            switch (ch) {
                case '{': //NOI18N
                case '}': //NOI18N
                    return true;
            }
            return false;
        }
    };
}
