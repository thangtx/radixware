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

package org.radixware.kernel.common.client.env;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;


public class UserDefinitionSettings {

    private final IClientEnvironment env;
    private static final Id SYSTEM_CLASS_ID = Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E");
    private static final Id SYSTEM_CLASS_GENERAL_EPR_ID = Id.Factory.loadFrom("eprRF6WOIPPZBD3TKO4ECANZHYFAA");
    private static final Id LANG_LIST_PROP_ID = Id.Factory.loadFrom("col2JACLKO3UNAMTIFUAMZ43AXNB4");

    public UserDefinitionSettings(IClientEnvironment env) {
        this.env = env;
    }

    private Pid getThisSystemPid() {
        return new Pid(SYSTEM_CLASS_ID, 1);
    }

    public List<EIsoLanguage> getSupportedLanguages() {
        EntityModel model;
        try {
            model = EntityModel.openContextlessModel(env, getThisSystemPid(), SYSTEM_CLASS_ID, SYSTEM_CLASS_GENERAL_EPR_ID);
            Object val = model.getProperty(LANG_LIST_PROP_ID).getValueObject();
            Set<EIsoLanguage> result = new HashSet<>();
            if (val instanceof List) {
                for (Object obj : (List) val) {
                    if (obj instanceof EIsoLanguage) {
                        result.add((EIsoLanguage) obj);
                    }
                }
            }
            if (result.isEmpty()) {
                return getDefaultLanguageSet();
            } else {
                List<EIsoLanguage> langs = new ArrayList<>(result);
                Collections.sort(langs, new Comparator<EIsoLanguage>() {
                    @Override
                    public int compare(EIsoLanguage o1, EIsoLanguage o2) {
                        return o1.name().compareTo(o2.name());
                    }
                });
                return langs;
            }
        } catch (ServiceClientException | InterruptedException ex) {
            return getDefaultLanguageSet();
        }

    }

    public List<EIsoLanguage> getDefaultLanguageSet() {
        List<EIsoLanguage> appList = env.getApplication().getLanguages();
        Set<EIsoLanguage> result = new HashSet<>();
        if (appList == null || appList.isEmpty()) {
            result.add(env.getLanguage());
        } else {
            result.addAll(appList);
        }
        List<EIsoLanguage> langs = new ArrayList<>(result);
        Collections.sort(langs, new Comparator<EIsoLanguage>() {
            @Override
            public int compare(EIsoLanguage o1, EIsoLanguage o2) {
                return o1.name().compareTo(o2.name());
            }
        });
        return langs;
    }
}
