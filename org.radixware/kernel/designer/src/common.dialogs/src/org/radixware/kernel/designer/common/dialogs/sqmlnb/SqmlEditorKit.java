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

package org.radixware.kernel.designer.common.dialogs.sqmlnb;

import javax.swing.Action;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.TextAction;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.translate.ISqmlPreprocessorConfig;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlDocument;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorKit;
import org.radixware.kernel.designer.dds.script.defs.DdsScriptGeneratorUtils;

public class SqmlEditorKit extends ScmlEditorKit {

    /**
     * Creates a new instance of SQLEditorKit
     */
    public SqmlEditorKit() {
    }

    @Override
    public Document createDefaultDocument() {
        return new ScmlDocument(getContentType()) {
            private Lookup getLookup() {
                return (Lookup) getProperty(Lookup.class);
            }

            @Override
            protected String getPreviewContent() {
                String content = null;
                if (getScml() instanceof Sqml) {
                    final ISqmlPreprocessorConfigProvider configProvider = getLookup().lookup(ISqmlPreprocessorConfigProvider.class);
                    configProvider.beforeStart(getLookup());
                    final ISqmlPreprocessorConfig config = configProvider.getConfig();
                    if (config == null) {
                        return null;
                    }
                    content = DdsScriptGeneratorUtils.preprocessAndTranslate((Sqml) getScml(), config);
                } else {
                    try {
                        content = getText(0, getLength());
                    } catch (BadLocationException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
                return content;
            }

            @Override
            public Scml createDefaultScml() {
                return Sqml.Factory.newInstance();
            }
        };
    }

    @Override
    protected Action[] createActions() {
        Action[] superActions = super.createActions();
        Action[] sqlActions = new Action[]{
            new ToggleCommentAction("--"),};
        return TextAction.augmentList(superActions, sqlActions);
    }

    /**
     * Retrieves the content type for this editor kit
     */
    @Override
    public String getContentType() {
        return ScmlEditor.SQML_MIME_TYPE;
    }
}
