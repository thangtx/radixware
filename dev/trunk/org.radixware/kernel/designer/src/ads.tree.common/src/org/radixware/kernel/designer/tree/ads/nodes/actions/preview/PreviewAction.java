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

import java.io.File;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.openide.nodes.Node;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.designer.tree.ads.nodes.actions.AdsDefinitionAction;

public class PreviewAction extends AdsDefinitionAction {
    
   public static class PreviewCookie extends AdsDefinitionActionCookie {

        private final AdsDefinition def;

        public PreviewCookie(final AdsDefinition def) {
            this.def = def;
        }

        @Override
        public AdsDefinition getDefinition() {
            return def;
        }
        
        public boolean isAllowed() {
            return AdsUtils.isEnableHumanReadable(def);
        }
        
        public boolean canOpen() {
           return isPreviewExist();
       }
        
        public boolean isPreviewExist() {
           File file = AdsUtils.calcHumanReadableFile(def);
           return file.exists();
       }

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void performAction(final Node[] activatedNodes) {
    }

    @SuppressWarnings("unchecked")
    @Override
    public JMenuItem getPopupPresenter() {
        final Node[] activatedNodes = getActivatedNodes();
        if (activatedNodes != null) {
            for (Node node : activatedNodes) {
                Class<? extends PreviewCookie> cookieClass = (Class<? extends PreviewCookie>) cookieClasses()[0];
                final PreviewCookie previewCookie = node.getLookup().lookup(cookieClass);
                if (previewCookie != null) {
                    JMenu root = new JMenu(getName());
                    JMenuItem sub = new JMenuItem(new PreviewOpenAction(Lookups.singleton(previewCookie.def)));
                    root.add(sub);
                    sub = new JMenuItem(new PreviewGenerateAction(Lookups.singleton(previewCookie.def)));
                    root.add(sub);
                    return root;
                }
            }
            return super.getPopupPresenter();
        } else {
            return super.getPopupPresenter();
        }
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }
    
    @Override
    protected boolean calcEnabled(Node[] activatedNodes) {
        for (Node n : activatedNodes) {
            PreviewCookie c = n.getLookup().lookup(PreviewCookie.class);
            if (c == null || !c.isAllowed()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{PreviewCookie.class};
    }


    @Override
    public String getName() {
        return "Preview";
    }
    
    
}
