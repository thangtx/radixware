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
package org.radixware.kernel.designer.common.editors.documentation;

import java.io.File;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

public class ChooseDocResourceDialog extends ModalDisplayer {

    private final DocResourcesEditor editor;
    private File file;

    public static class Factory {

        public static ChooseDocResourceDialog newInstation(AdsModule adsModule, EIsoLanguage language) {
            DocResourcesEditor editor = new DocResourcesEditor(adsModule, language);
            ChooseDocResourceDialog dialog = new ChooseDocResourceDialog(editor);
            return dialog;
        }
    }

    private ChooseDocResourceDialog(DocResourcesEditor editor) {
        super(editor);
        this.editor = editor;
    }

    public boolean chooseFile() {
        setTitle("Select file");
        getDialogDescriptor().setValid(true);
        boolean result = showModal();
        return result;
    }

    @Override
    protected void apply() {
        file = editor.getSelectFile();
    }

    public File getFile() {
        return file;
    }
}
