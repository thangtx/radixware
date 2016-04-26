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

package org.radixware.kernel.designer.ads.editors.module;

import javax.swing.JComponent;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.components.TabManager;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.editors.module.ModuleEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;


public class AdsModuleEditor extends ModuleEditor<AdsModule> {

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsModule> {

        @Override
        public RadixObjectEditor newInstance(AdsModule module) {
            return (RadixObjectEditor) new AdsModuleEditor(module);
        }
    }

    private static class ServicesTab extends TabManager.TabAdapter {

        private final ServicesCatalogEditor editor = new ServicesCatalogEditor();
        private final AdsModule module;

        public ServicesTab(AdsModule module) {
            this.module = module;
        }

        @Override
        public JComponent getTabComponent() {
            return editor;
        }

        @Override
        public void initTab() {
            super.initTab();
            editor.open(module);
        }

        @Override
        public String getTabName() {
            return "Exported services";
        }
    }

    public AdsModuleEditor(AdsModule module) {
        super(module);

        if (!(module instanceof UdsModule)) {
            getTabManager().addTab(new ServicesTab(module));
        }
        getTabManager().assembly(true);
    }
}
