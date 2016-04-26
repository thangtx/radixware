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

package org.radixware.kernel.explorer.editors.jmleditor.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef.Lookup.DefInfo;


public class ChooseObjectPanel extends BaseChoosePanel{

    private Collection<RadixObject> allowedDefinitions;

    public ChooseObjectPanel(final IChooseDefFromList parent, final Collection<RadixObject> allowedDefinitions){
        super(parent);
        this.allowedDefinitions=allowedDefinitions;
        if(this.allowedDefinitions==null)
            this.allowedDefinitions=new ArrayList<>();
        createListUi();
        open( allowedDefinitions);
    }

     private void open(final Collection<RadixObject> allowedDefinitions){
        this.allowedDefinitions=allowedDefinitions;
        setVisibleForList(true);
        this.setEnabled(true);
    }

    public void update(final Collection<RadixObject> allowedDefinitions){
        this.allowedDefinitions=allowedDefinitions;
        setDefinitionList(allowedDefinitions.size());
    }

    @Override
    protected void findTextChanged(String s) {
        //List<String> serchMaskList=parseFindString(s);
        final LinkedList<RadixObject> defList=new LinkedList<>();
        defList.addAll(allowedDefinitions);
        if((!"".equals(s))&&(!"*".equals(s))){
            for(int i=0;i<defList.size();i++){
                if(!checkName(s,defList.get(i).getName()/*,serchMaskList*/)){
                    defList.remove(i);
                    i--;
                }
            }
        }
        updateModelForObj(defList);
    }

    @Override
    protected BaseModelList createModelList(final int i, final Collection<DefInfo> definitionList) {        
        if(allowedDefinitions==null){
            return null;
        }
        final ListModelForRadixObj dm = new ListModelForRadixObj(null);
        final List<RadixObject> defList=new ArrayList<>();
        defList.addAll(allowedDefinitions);
        dm.setDefList(defList);
        setLoadedDefsCount(-1);
        return dm;
    }
}
