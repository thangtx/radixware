/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.reporteditor.common;

import java.util.Set;
import org.apache.xmlbeans.XmlObject;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.defs.ads.userfunc.UdsObserver;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.common.ReportUdsObserverCommon;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.common.utils.XmlObjectProcessor;
import org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.reports.LibUserFuncXmlRq;
import org.radixware.schemas.reports.LibUserFuncXmlRqDocument;
import org.radixware.schemas.reports.LibUserFuncsListRs;
import org.radixware.schemas.reports.LibUserFuncsListRsDocument;

/**
 *
 * @author npopov
 */
public class ReportUdsObserver extends ReportUdsObserverCommon {

    private static class UserReportRequestor implements IUserDefRequestor {

        @Override
        public ClassDefinition getClassDefXml(Id reportId) {
            return null;
        }

        @Override
        public AdsUserFuncDefinitionDocument.AdsUserFuncDefinition getLibUserFuncXml(final Id libUserFuncId) {
            final AdsUserFuncDefinitionDocument.AdsUserFuncDefinition[] xUserFunc = new AdsUserFuncDefinitionDocument.AdsUserFuncDefinition[1];
            UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
                @Override
                public void execute(IClientEnvironment env) {
                    try {
                        LibUserFuncXmlRqDocument requestDoc = LibUserFuncXmlRqDocument.Factory.newInstance();
                        LibUserFuncXmlRq rq = requestDoc.addNewLibUserFuncXmlRq();
                        rq.setLibFuncId(libUserFuncId.toString());
                        XmlObject output = UserExtensionManagerCommon.getInstance().getEnvironment().getEasSession().executeContextlessCommand(CMD_LIB_USER_FUNC_XML, requestDoc, AdsUserFuncDefinitionDocument.class);
                        if (output == null) {
                            xUserFunc[0] = null;
                            return;
                        }
                        AdsUserFuncDefinitionDocument resDoc = XmlObjectProcessor.cast(getClass().getClassLoader(), output, AdsUserFuncDefinitionDocument.class);
                        xUserFunc[0] = resDoc.getAdsUserFuncDefinition();
                    } catch (ServiceClientException | InterruptedException ex) {
                        UserExtensionManagerCommon.getInstance().getEnvironment().processException(ex);
                    }
                }
            });

            return xUserFunc[0];
        }

        @Override
        public void readUserDefHeaders(Set<EDefType> defTypes, final UdsObserver.IUserDefReceiver recv) {
            UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
                @Override
                public void execute(IClientEnvironment env) {
                    try {
                        XmlObject output = UserExtensionManagerCommon.getInstance().getEnvironment().getEasSession().executeContextlessCommand(CMD_LIB_USER_FUNCS, null, LibUserFuncsListRsDocument.class);
                        if (output == null) {
                            return;
                        }
                        LibUserFuncsListRsDocument rs = XmlObjectProcessor.cast(getClass().getClassLoader(), output, LibUserFuncsListRsDocument.class);
                        if (rs != null && rs.getLibUserFuncsListRs() != null) {
                            for (LibUserFuncsListRs.LibUserFunc xUserFuncInfo : rs.getLibUserFuncsListRs().getLibUserFuncList()) {
                                Id ufId = Id.Factory.loadFrom(xUserFuncInfo.getLibFuncId());
                                Id libId = Id.Factory.loadFrom(xUserFuncInfo.getLibId());
                                recv.accept(ufId, xUserFuncInfo.getLibFuncName(), xUserFuncInfo.getLibName(), libId);
                            }
                        }
                    } catch (ServiceClientException | InterruptedException ex) {
                        UserExtensionManagerCommon.getInstance().getEnvironment().processException(ex);
                    }
                }
            });
        }
    }

    @Override
    public IUserDefRequestor getReportRequestor() {
        return requestor != null ? requestor : (requestor = new UserReportRequestor());
    }
}
