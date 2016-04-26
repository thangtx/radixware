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

package org.radixware.kernel.designer.common.editors.module;

import java.awt.BorderLayout;
import javax.swing.*;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.components.TabManager;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class ModuleEditor<ModuleType extends Module> extends RadixObjectEditor<ModuleType> {

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<Module> {

        @Override
        public RadixObjectEditor newInstance(Module module) {
            return new ModuleEditor<>(module);
        }
    }
    
    private final TabManager tabManager = new TabManager(new JTabbedPane());

    public ModuleEditor(final ModuleType module) {
        super(module);

        setLayout(new BorderLayout());

        add(getTabManager().getTabbedPane(), BorderLayout.CENTER);

        getTabManager().addTab(new GeneralTab(module));
    }

    public ModuleType getModule() {
        return getRadixObject();
    }

    @Override
    public boolean open(OpenInfo openInfo) {

        getTabManager().openSelectedTab();
        return true;
    }

    @Override
    public void update() {
        getTabManager().updateSelectedTab();
    }

    protected final TabManager getTabManager() {
        return tabManager;
    }
}
