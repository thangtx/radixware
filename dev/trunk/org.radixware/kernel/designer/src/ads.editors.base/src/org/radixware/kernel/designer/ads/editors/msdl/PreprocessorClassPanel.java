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

package org.radixware.kernel.designer.ads.editors.msdl;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;


public class PreprocessorClassPanel extends DefinitionLinkEditPanel implements ChangeListener {

    private AdsMsdlSchemeDef schemeDef;
    public PreprocessorClassPanel(){

    }

    public void open(AdsMsdlSchemeDef schemeDef) {
        this.schemeDef = schemeDef;
        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(schemeDef, new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return radixObject instanceof AdsClassDef;
            }
        });
        String stringId = schemeDef.getRootMsdlScheme().getPreprocessorClassGuid();
        if (stringId != null) {
            Id id = Id.Factory.loadFrom(stringId);
            AdsClassDef def = (AdsClassDef) AdsSearcher.Factory.newAdsDefinitionSearcher(schemeDef).findById(id).get();
            super.open(cfg, def, id);
        }
        else
            super.open(cfg, null, null);
        addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Definition def = getDefinition();
        if (def == null)
            schemeDef.getRootMsdlScheme().setPreprocessorClassGuid(null);
        else
            schemeDef.getRootMsdlScheme().setPreprocessorClassGuid(def.getId().toString());
        schemeDef.getRootMsdlScheme().setEditState(EEditState.MODIFIED);
    }

}
