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
package org.radixware.kernel.designer.ads.editors.xml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.designer.common.dialogs.utils.IAcceptor;

/**
 *
 * @author dlastochkin
 */
public class XmlTreeFilterAcceptor implements IAcceptor<XmlTreeNode> {

    Set<IAcceptor<XmlTreeNode>> acceptors;
    Map<String, Set<IAcceptor<XmlTreeNode>>> acceptorsGroups;

    public XmlTreeFilterAcceptor(Set<IAcceptor<XmlTreeNode>> acceptors) {
        this.acceptors = acceptors;
        acceptorsGroups = new HashMap<>();
    }
    
    public void addToGroup(IAcceptor<XmlTreeNode> acceptor, String groupName) {
        Set<IAcceptor<XmlTreeNode>> groupAcceptors;
        if (acceptorsGroups.containsKey(groupName)) {
            groupAcceptors = acceptorsGroups.get(groupName);
            groupAcceptors.add(acceptor);
        } else {
            groupAcceptors = new HashSet<>();
            groupAcceptors.add(acceptor);
            acceptorsGroups.put(groupName, groupAcceptors);
        }
    }
    
    private boolean accept(XmlTreeNode candidate, Set<IAcceptor<XmlTreeNode>> acceptors) {
        for (IAcceptor<XmlTreeNode> acceptionPart : acceptors) {
            if (!acceptionPart.accept(candidate)) {
                return false;
            }
        }
        return true;
    }
            
    @Override
    public boolean accept(XmlTreeNode candidate) {
        return accept(candidate, acceptors);
    }
    
    public boolean acceptGroup(XmlTreeNode candidate, String groupName) {
        if (acceptorsGroups.containsKey(groupName)) {
            return accept(candidate, acceptorsGroups.get(groupName));
        } else {
            return false;
        }
    }
}
