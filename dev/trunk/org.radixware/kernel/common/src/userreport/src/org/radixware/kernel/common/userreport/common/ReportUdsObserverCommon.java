/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * mplied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.userreport.common;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.defs.ads.userfunc.UdsObserver;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.Pid;
import org.radixware.kernel.common.utils.XmlObjectProcessor;
import org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.reports.LibUserFuncXmlRq;
import org.radixware.schemas.reports.LibUserFuncXmlRqDocument;
import org.radixware.schemas.reports.LibUserFuncsListRs;
import org.radixware.schemas.reports.LibUserFuncsListRsDocument;

public class ReportUdsObserverCommon extends UdsObserver{
    
    protected final static Id CMD_LIB_USER_FUNCS=Id.Factory.loadFrom("clc2IUSW2KMURCC3ESXW3RGDFSCTY");
    protected final static Id CMD_LIB_USER_FUNC_XML=Id.Factory.loadFrom("clcXL6E3PQJVJDCFKK5665TCOCPN4");

    public class UserReportReceiver implements IUserDefReceiver {

        public final IUserDefRequestor requestor;
        private List<AdsUserFuncDef.Lookup.DefInfo> list = null;

        public UserReportReceiver(IUserDefRequestor requestor) {
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
    
    public class UserReportRequestorCommon implements IUserDefRequestor{

        @Override
        public void readUserDefHeaders(Set<EDefType> defTypes, IUserDefReceiver recv) {
           try {
                XmlObject output = UserExtensionManagerCommon.getInstance().getEnvironment().getEasSession().executeContextlessCommand(CMD_LIB_USER_FUNCS, null, LibUserFuncsListRsDocument.class);
                if (output == null) {
                    return;
                }
                LibUserFuncsListRsDocument  rs= XmlObjectProcessor.cast(getClass().getClassLoader(), output, LibUserFuncsListRsDocument.class);
                if(rs!=null && rs.getLibUserFuncsListRs()!=null)
                for(LibUserFuncsListRs.LibUserFunc xUserFuncInfo: rs.getLibUserFuncsListRs().getLibUserFuncList()){
                    Id ufId=Id.Factory.loadFrom(xUserFuncInfo.getLibFuncId());
                    Id libId=Id.Factory.loadFrom(xUserFuncInfo.getLibId());
                    recv.accept(ufId, xUserFuncInfo.getLibFuncName(), xUserFuncInfo.getLibName(), libId);
                }
            } catch (ServiceClientException | InterruptedException ex) {
                UserExtensionManagerCommon.getInstance().getEnvironment().processException(ex);
            } //finally {
             //   lock.countDown();
            //}
        }

        @Override
        public ClassDefinition getClassDefXml(Id reportId) {
            return null;
        }

        @Override
        public AdsUserFuncDefinitionDocument.AdsUserFuncDefinition getLibUserFuncXml(Id libUserFuncId) {
            try {
                //GroupModel group = GroupModel.openTableContextlessSelectorModel(env, REPORTS_CLASS_ID, REPORTS_SELECTOR_ID);
                LibUserFuncXmlRqDocument requestDoc = LibUserFuncXmlRqDocument.Factory.newInstance();
                LibUserFuncXmlRq rq = requestDoc.addNewLibUserFuncXmlRq();
                rq.setLibFuncId(libUserFuncId.toString());
                XmlObject output = UserExtensionManagerCommon.getInstance().getEnvironment().getEasSession().executeContextlessCommand(CMD_LIB_USER_FUNC_XML, requestDoc, AdsUserFuncDefinitionDocument.class);
                if (output == null) {
                    return null;
                }
                AdsUserFuncDefinitionDocument resDoc=XmlObjectProcessor.cast(getClass().getClassLoader(), output, AdsUserFuncDefinitionDocument.class);
                return resDoc.getAdsUserFuncDefinition();
            } catch (ServiceClientException | InterruptedException ex) {
                UserExtensionManagerCommon.getInstance().getEnvironment().processException(ex);
            }
            return null;
        }
        
    }
    
    @Override
    public IUserDefRequestor getReportRequestor() {
        return requestor != null ? requestor : (requestor = new UserReportRequestorCommon());
    }   
    
    @Override
    public void addUserDefInfo(final Set<EDefType> defTypes, List<AdsUserFuncDef.Lookup.DefInfo> infos) {
        UserReportReceiver recv = new UserReportReceiver(getReportRequestor());
        recv.listUserDefs(defTypes, infos);
    }
    
    @Override
    public Map<Pid, Boolean> checkEntitiesExistance(Set<Pid> pidsToCheck) {
        throw new UnsupportedOperationException("Not supported in ReportEditor Environment");
    }
}
