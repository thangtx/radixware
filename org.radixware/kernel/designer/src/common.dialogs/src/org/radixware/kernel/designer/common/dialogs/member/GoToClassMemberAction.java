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

package org.radixware.kernel.designer.common.dialogs.member;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import org.openide.util.*;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.designer.common.dialogs.actions.AbstractRadixAction;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;


public class GoToClassMemberAction extends AbstractRadixAction implements LookupListener, ContextAwareAction {

    private Lookup context;
    Lookup.Result<RadixObject> lkpInfo;

    public GoToClassMemberAction() {
        this(Utilities.actionsGlobalContext());
    }

    private GoToClassMemberAction(Lookup context) {
        this.context = context;
    }

    void init() {
        assert SwingUtilities.isEventDispatchThread() : "this shall be called just from AWT thread";

        if (lkpInfo != null) {
            return;
        }

        lkpInfo = context.lookupResult(RadixObject.class);
        lkpInfo.addLookupListener(this);
        resultChanged(null);
    }

    @Override
    public boolean isEnabled() {
        init();
        return super.isEnabled();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        init();
        final AdsClassDef classDef = getAdsClassDef();
        final List<AdsPropertyDef> properties = classDef.getProperties().get(EScope.ALL);
        final List<AdsMethodDef> methods = classDef.getMethods().get(EScope.ALL);
        final List<Definition> propertiesAndMethods = new ArrayList<Definition>();
        propertiesAndMethods.addAll(properties);
        propertiesAndMethods.addAll(methods);
        final Definition def = ChooseDefinition.chooseDefinition(ChooseDefinitionCfg.Factory.newInstance(propertiesAndMethods));

        if (def != null) {
            EditorsManager.getDefault().open(def);
        }

    }

    private AdsClassDef getAdsClassDef() {
        final Collection<? extends RadixObject> radixObjects = lkpInfo.allInstances();
        if (radixObjects == null || radixObjects.size() != 1) {
            return null;
        }
        RadixObject radixObject = radixObjects.iterator().next();
        while (radixObject != null && !(radixObject instanceof AdsClassDef)) {
            radixObject = radixObject.getContainer();
        }
        if (radixObject != null) {
            return (AdsClassDef) radixObject;
        }
        return null;
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        setEnabled(getAdsClassDef() != null);
    }

    @Override
    public Action createContextAwareInstance(Lookup context) {
        return new GoToClassMemberAction(context);
    }
}
