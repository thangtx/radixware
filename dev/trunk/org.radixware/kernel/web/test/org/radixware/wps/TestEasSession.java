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

package org.radixware.wps;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.eas.EasSession;

import org.radixware.kernel.common.client.eas.IEasClient;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadParagraphDef;
import org.radixware.kernel.common.enums.EAuthType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.eas.CreateSessionRs;


public class TestEasSession extends EasSession {

    private final Id rootId = Id.Factory.loadFrom("parROGSM5O5ZZC4FMUSVYLVBTE5TI");
    private WpsEnvironment env;

    public TestEasSession(WpsEnvironment environment) {
        super(environment);
        this.env = environment;
    }

    @Override
    public List<Id> listVisibleExplorerItems(Id explorerRootId) throws ServiceClientException, InterruptedException {
        RadParagraphDef paragraph = env.getDefManager().getParagraphDef(explorerRootId);
        List<Id> ids = new LinkedList<Id>();
        fillAllChildren(paragraph, ids);
        return ids;
    }
    /*
     @Override
     public List<Id> login(Id explorerRootId) throws ServiceClientException, InterruptedException {
     RadParagraphDef paragraph = env.getDefManager().getParagraphDef(explorerRootId);
     List<Id> ids = new LinkedList<Id>();
     fillAllChildren(paragraph, ids);
     return ids;
     }
     */

    private void fillAllChildren(RadParagraphDef p, List<Id> ids) {
        ids.add(p.getId());
        for (RadExplorerItemDef ei : p.getChildrenExplorerItems()) {
            if (ei instanceof RadParagraphDef) {
                fillAllChildren((RadParagraphDef) ei, ids);
            } else {
                ids.add(ei.getId());
            }
        }
    }

    @Override
    public CreateSessionRs open(IEasClient soapConnection, String stationName, String userName, String password, EAuthType authType, Id explorerRoot) throws ServiceClientException, InterruptedException {
        CreateSessionRs rs = CreateSessionRs.Factory.newInstance();
        CreateSessionRs.ExplorerRoots xRoots = rs.addNewExplorerRoots();
        CreateSessionRs.ExplorerRoots.Item xItem = xRoots.addNewItem();
        xItem.setId(rootId);
        xItem.setTitle("TestExplorerRoot");
        return rs;
    }
}
