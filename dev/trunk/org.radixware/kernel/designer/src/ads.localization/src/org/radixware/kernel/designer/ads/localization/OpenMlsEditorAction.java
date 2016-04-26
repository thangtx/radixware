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

package org.radixware.kernel.designer.ads.localization;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.kernel.common.resources.RadixWareIcons;



public class OpenMlsEditorAction  extends NodeAction {
    
    public static class OpenMlEditorCookie implements Node.Cookie {

        private final transient Layer layer;

        public OpenMlEditorCookie(final Layer layer) {
            this.layer = layer;
        }

        public Layer getLayer() {
            return layer;
        }
    }

    public OpenMlsEditorAction() {
        this.setIcon( RadixWareIcons.MLSTRING_EDITOR.CHOOSE_LANGS.getIcon(20));
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(MultilingualEditor.class, "MULTILINGUAL_MENU_NAME");
    }

    @Override
    public HelpCtx getHelpCtx() {
       return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public boolean isEnabled() {
        Collection<Branch> openBranches = RadixFileUtil.getOpenedBranches();
        if((openBranches!=null) &&(!openBranches.isEmpty()))
            return true;
        return false;
    }


    @Override
    @SuppressWarnings("deprecation")
    public void performAction() {
        openMlStringEditor(null);
    }
    
    private void openMlStringEditor(Set<Layer> selectedLayers){
        String editorName=NbBundle.getMessage(MultilingualEditor.class, "MULTILINGUAL_EDITOR_NAME");
        final TopComponent editor = WindowManager.getDefault().findTopComponent(editorName);
        if (editor == null) {
            MultilingualEditor multilingualEditor = new MultilingualEditor();
            if (multilingualEditor.initLangs()){
                if(multilingualEditor.canSetSelectedLayers(selectedLayers)){
                     multilingualEditor.setSelectedLayers(selectedLayers);
                }
                multilingualEditor.open();
                multilingualEditor.requestActive();
            }
        }else if(editor instanceof MultilingualEditor){
            MultilingualEditor multilingualEditor = (MultilingualEditor) editor;
            if(multilingualEditor.canSetSelectedLayers(selectedLayers)){
                  multilingualEditor.setSelectedLayers(selectedLayers);
            }
            if(!editor.isOpened()){
                multilingualEditor.open();
            } else {
                multilingualEditor.update();
            }
            multilingualEditor.requestActive();
        }
    }
    
    @Override
    protected void performAction(org.openide.nodes.Node[] activatedNodes) {
       Set<Layer> selectedLayers = new HashSet<>();
       for (Node node : activatedNodes) {
            final OpenMlEditorCookie buildCookie = node.getLookup().lookup(OpenMlEditorCookie.class);
            if (buildCookie != null) {
               Layer obj = buildCookie.getLayer();
               selectedLayers.add(obj);
            }
       }
       openMlStringEditor(selectedLayers);      
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    protected boolean enable(Node[] nodes) {
       return true;
    }


}
