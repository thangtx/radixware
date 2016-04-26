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

package org.radixware.kernel.designer.ads.editors.common;

import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.CreatePresentationsList;
import org.radixware.kernel.common.defs.ads.clazz.presentation.CreatePresentationsList.ICreatePresentationListOwner;
import org.radixware.kernel.common.types.Id;


public class CreatePresentationsListPanel extends PresentationListCfgPanel {

    private class Adapter implements PresentationListCfgPanel.PresentationsListAdapter {

        private final CreatePresentationsList.ICreatePresentationListOwner createListOwner;

        public Adapter(ICreatePresentationListOwner createListOwner) {
            this.createListOwner = createListOwner;
        }

        @Override
        public boolean isReadOnly() {
            return createListOwner.getCreatePresentationsList().isReadOnly();
        }

        @Override
        public void remove(Id id) {
            createListOwner.getCreatePresentationsList().removePresentationId(id);
        }

        @Override
        public void add(Id id) {
            createListOwner.getCreatePresentationsList().addPresentationId(id);
        }

        @Override
        public void moveUp(Id id) {
            createListOwner.getCreatePresentationsList().movePresentationUp(id);
        }

        @Override
        public void moveDn(Id id) {
            createListOwner.getCreatePresentationsList().movePresentationDn(id);
        }

        @Override
        public ExtendableDefinitions<AdsEditorPresentationDef> availablePresentations() {
            AdsEntityObjectClassDef clazz = createListOwner.getCreatePresentationsList().findTargetClass();
            return clazz == null ? null : clazz.getPresentations().getEditorPresentations();
        }

        @Override
        public List<Id> currentlySelectedIds() {
            return createListOwner.getCreatePresentationsList().getPresentationIds();
        }
    }

    /** Creates new form PresentationsForEditing */
    public CreatePresentationsListPanel() {
        super("Editor presentations for creation");
    }

    public void open(CreatePresentationsList.ICreatePresentationListOwner object) {
        Adapter a = new Adapter(object);
        open(a);
    }

}
