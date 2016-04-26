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

package org.radixware.kernel.designer.common.dialogs.components.localizing;

import java.util.Map;
import org.radixware.kernel.common.enums.EIsoLanguage;


public class LocalizingEditorPanel extends LocalizingStringEditor {

    public static class Factory {

        private Factory() {
        }

        public static LocalizingEditorPanel createAreaEditor(String name, int rowCount) {
            return new LocalizingEditorPanel(new Options()
                    .add(Options.COLLAPSABLE_KEY, true)
                    .add(Options.TITLE_KEY, name)
                    .add("row-count", rowCount)
                    .add(Options.MODE_KEY, EEditorMode.MULTILINE));
        }

        public static LocalizingEditorPanel createAreaEditor(Options options) {
            return new LocalizingEditorPanel(options);
        }
    }

    protected LocalizingEditorPanel(Options options) {
        super(options);
    }

    public LocalizingEditorPanel() {
        this(new Options().add(Options.COLLAPSABLE_KEY, true)
                .add(Options.TITLE_KEY, "Title")
                .add(Options.MODE_KEY, EEditorMode.LINE));
    }

    public LocalizingEditorPanel(String title) {
        this(new Options().add(Options.COLLAPSABLE_KEY, true)
                .add(Options.TITLE_KEY, title)
                .add(Options.MODE_KEY, EEditorMode.LINE));
    }

    public LocalizingEditorPanel(boolean expand) {
        this(new Options().add(Options.COLLAPSABLE_KEY, true)
                .add(Options.TITLE_KEY, "Title")
                .add(Options.MODE_KEY, EEditorMode.MULTILINE));
    }

    public Map<EIsoLanguage, String> getLanguages2PatternsMap() {
        return super.getValueMap();
    }

    @Deprecated
    public void setTitle(String title) {
    }

    @Deprecated
    public void setExpandable(boolean expandable) {
    }

    @Deprecated
    public void setExtendedMode(boolean extendedMode) {
    }
}
