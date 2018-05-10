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

package org.radixware.kernel.common.defs.ads.userfunc;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.Pid;
import org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument.AdsUserFuncDefinition;
import org.radixware.schemas.adsdef.ClassDefinition;


public abstract class UdsObserver {
    
    protected IUserDefRequestor requestor;

    public interface IUserDefReceiver {

        public void accept(Id id, String name, String moduleName, Id moduleId);
    }

    public interface IUserDefRequestor {

        public void readUserDefHeaders(final Set<EDefType> defTypes, IUserDefReceiver recv);

        public ClassDefinition getClassDefXml(Id reportId);

        public AdsUserFuncDefinition getLibUserFuncXml(Id libUserFuncId);
    }

    public class UserDefReceiver implements IUserDefReceiver {

        public final IUserDefRequestor requestor;
        private List<AdsUserFuncDef.Lookup.DefInfo> list = null;

        public UserDefReceiver(IUserDefRequestor requestor) {
            this.requestor = requestor;
        }

        public void listUserDefs(final Set<EDefType> defTypes, List<AdsUserFuncDef.Lookup.DefInfo> list) {
            synchronized (this) {
                this.list = list;
                try {
                    requestor.readUserDefHeaders(defTypes, this);
                } finally {
                    this.list = null;
                }
            }
        }

        @Override
        public void accept(Id id, String name, String moduleName, Id moduleId) {
            list.add(new AdsUserFuncDef.Lookup.DefInfo(id, name, moduleName, moduleId, false, ERuntimeEnvironmentType.SERVER, null));
        }
    }

    public abstract IUserDefRequestor getReportRequestor();

    public void addUserDefInfo(final Set<EDefType> defTypes, List<AdsUserFuncDef.Lookup.DefInfo> infos) {

        UserDefReceiver recv = new UserDefReceiver(getReportRequestor());
        recv.listUserDefs(defTypes, infos);
    }
    
    public abstract Map<Pid, Boolean> checkEntitiesExistance(Set<Pid> pidsToCheck);
}
