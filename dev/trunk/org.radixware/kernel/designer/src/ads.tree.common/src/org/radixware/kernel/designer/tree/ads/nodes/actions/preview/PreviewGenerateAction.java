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
package org.radixware.kernel.designer.tree.ads.nodes.actions.preview;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class PreviewGenerateAction extends AbstractAction implements LookupListener, ContextAwareAction {

    private Lookup context;
    Lookup.Result<RadixObject> lkpInfo;
    
    public PreviewGenerateAction() {
        this(Utilities.actionsGlobalContext());
    }
 
    PreviewGenerateAction(Lookup context) {
        putValue(Action.NAME, "Generate");
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        init();
        Map<RadixObject, File> objects = getRadixObjects();
        for (RadixObject ro : objects.keySet()) {
            if (!AdsUtils.isEnableHumanReadable(ro)) {
                continue;
            }
            File file = objects.get(ro);
            if (file != null && file.exists()) {
                if (!DialogUtils.messageConfirmation("Preview file '" + file.getPath() + "' for " + ro.getQualifiedName() + " is already exists. Overwrite?")) {
                    continue;
                }
            }
            AdsUtils.savePreview(ro);
            DialogUtils.openFile(objects.get(ro));
        }
        
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

    private Map<RadixObject, File> getRadixObjects() {
        Map<RadixObject, File> map = new HashMap<>();
        final Collection<? extends RadixObject> radixObjects = lkpInfo.allInstances();
        for (RadixObject ro : radixObjects) {
            if (!AdsUtils.isEnableHumanReadable(ro)) {
                continue;
            }
            File file = AdsUtils.calcHumanReadableFile(ro);
            if (file == null) {
                continue;
            }
            map.put(ro, file);
        }
        return map;
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        setEnabled(!getRadixObjects().isEmpty());
    }
    
    @Override
    public Action createContextAwareInstance(Lookup context) {
        return new PreviewGenerateAction(context);
    }

    @Override
    public boolean isEnabled() {
        init();
        return super.isEnabled();
    }

    
}
