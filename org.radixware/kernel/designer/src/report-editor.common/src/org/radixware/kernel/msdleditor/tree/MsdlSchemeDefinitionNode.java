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

package org.radixware.kernel.msdleditor.tree;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.Children;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.userreport.repository.msdl.MsdlScheme;
import org.radixware.kernel.designer.tree.ads.nodes.defs.AdsObjectNode;



public class MsdlSchemeDefinitionNode extends AdsObjectNode<AdsMsdlSchemeDef>{
    
    private final MsdlScheme msdlScheme;
    private final DeleteMsdlSchemesAction.Cookie deleteCookie;
    
    
    /*@NodeFactoryRegistration
    public static final class Factory implements INodeFactory<AdsMsdlSchemeDef> {

        @Override
        public Node newInstance(AdsMsdlSchemeDef object) {
            return new MsdlSchemeDefinitionNode(object);
        }
    }

    protected MsdlSchemeDefinitionNode(AdsMsdlSchemeDef descriptor) {
        super(descriptor, Children.LEAF);
    }*/

    protected MsdlSchemeDefinitionNode(final MsdlScheme msdlScheme,final AdsMsdlSchemeDef descriptor) {
        super(descriptor, Children.LEAF);
        
        this.msdlScheme=msdlScheme;
        deleteCookie = new DeleteMsdlSchemesAction.Cookie(msdlScheme); 
        if (!msdlScheme.isReadOnly()) {
            addCookie(deleteCookie);
        }       
        msdlScheme.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if (msdlScheme.isReadOnly()) {
                    removeCookie(deleteCookie);
                } else {
                    addCookie(deleteCookie);
                }
            }
        });
    }   

   
    @Override
    public void addCustomActions(final List<Action> actions) {
        super.addCustomActions(actions);
        actions.add(SystemAction.get(DeleteMsdlSchemesAction.class));
    }
}