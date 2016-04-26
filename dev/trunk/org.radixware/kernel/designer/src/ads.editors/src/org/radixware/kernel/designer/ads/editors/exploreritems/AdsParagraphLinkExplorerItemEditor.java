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

package org.radixware.kernel.designer.ads.editors.exploreritems;

import java.awt.BorderLayout;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphLinkExplorerItemDef;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class AdsParagraphLinkExplorerItemEditor extends RadixObjectEditor<AdsParagraphLinkExplorerItemDef> {

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsParagraphLinkExplorerItemDef> {

        @Override
        public IRadixObjectEditor<AdsParagraphLinkExplorerItemDef> newInstance(AdsParagraphLinkExplorerItemDef object) {
            return new AdsParagraphLinkExplorerItemEditor(object);
        }

    }

    private ParagraphLinkEditorPanel content = new ParagraphLinkEditorPanel();

    public AdsParagraphLinkExplorerItemDef getParagraphLink() {
        return getRadixObject();
    }

    public AdsParagraphLinkExplorerItemEditor(AdsParagraphLinkExplorerItemDef link){
        super(link);

        setLayout(new BorderLayout()); 
        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane();
        scroll.setViewportView(content);
        add(scroll, java.awt.BorderLayout.CENTER);
    }

    @Override
    public boolean open(OpenInfo openInfo) {
        final AdsParagraphLinkExplorerItemDef link = getParagraphLink();
        content.open(link);
        return super.open(openInfo);
    }

    @Override
    public void update() {
        content.update();
    }

}
