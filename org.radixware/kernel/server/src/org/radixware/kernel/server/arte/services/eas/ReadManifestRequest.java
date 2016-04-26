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

package org.radixware.kernel.server.arte.services.eas;

import java.sql.SQLException;

import java.util.Collection;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;

import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.schemas.eas.ExceptionEnum;
import org.radixware.schemas.eas.ReadManifestMess;
import org.radixware.schemas.eas.ReadManifestRs;
import org.radixware.schemas.easWsdl.ReadManifestDocument;

final class ReadManifestRequest extends EasRequest {

    public ReadManifestRequest(final ExplorerAccessService presenter) {
        super(presenter);
    }

    final ReadManifestDocument process(final ReadManifestMess request) throws ServiceProcessServerFault, InterruptedException {
        getArte().switchToReadonlyTransaction();//it's readonly request

        if (!request.isSetReadManifestRq()) {
            throw EasFaults.newParamRequiedFault("ReadManifestRq", request.getDomNode().getNodeName());
        }
        final String stationName = request.getReadManifestRq().getStationName();
        if (stationName == null) {
            throw EasFaults.newParamRequiedFault("StationName", request.getReadManifestRq().getDomNode().getNodeName());
        }
        final Collection<ExplorerAccessService.Sap> saps;
        final String scpName;
        try {
            scpName = presenter.getScpByStation(stationName);
            saps = presenter.readSaps(scpName,request.getReadManifestRq().getAuthType());
        } catch (SQLException e) {
            final String preprocessedExStack = getArte().getTrace().exceptionStackToString(e);
            throw new ServiceProcessServerFault(ExceptionEnum.SERVER_MALFUNCTION.toString(), "Can't read system manifest: " + ExceptionTextFormatter.getExceptionMess(e), e, preprocessedExStack);
        } catch (ServiceProcessClientFault ex) {
            getArte().getTrace().put(EEventSeverity.EVENT, Messages.MLS_ID_ATTEMP_TO_REGISTER_FROM_INVALID_STATION, new ArrStr(stationName, getArte().getArteSocket().getRemoteAddress()), EEventSource.APP_AUDIT.getValue());
            throw ex;
        }

        final ReadManifestDocument res = ReadManifestDocument.Factory.newInstance();
        final ReadManifestRs rsStruct = res.addNewReadManifest().addNewReadManifestRs();
        rsStruct.setScpName(scpName);
        final ReadManifestRs.SAPs sapsXml = rsStruct.addNewSAPs();
        for (ExplorerAccessService.Sap sap : saps) {
            final ReadManifestRs.SAPs.Item sapXml = sapsXml.addNewItem();
            sapXml.setAddress(sap.getAddress());//for backward compatibility
            sapXml.setAddressCDATA(sap.getAddress());//JMS address can be written in multiple lines
            sapXml.setBlockingPeriod(Math.max(0, sap.getBlockingPeriod()));
            sapXml.setConnectTimeout(Math.max(0, sap.getConnectTimeout()));
            sapXml.setName(sap.getName());
            sapXml.setPriority(Math.max(0, sap.getPriority()));
            sapXml.setSecurityProtocol(sap.getSecurityProtocol());
            sapXml.setChannelType(sap.getChannelType());
        }
        return res;
    }

    @Override
    void prepare(final XmlObject rqXml) throws ServiceProcessServerFault, ServiceProcessClientFault {
        return;
    }

    @Override
    XmlObject process(final XmlObject rq) throws ServiceProcessFault, InterruptedException {
        return process((ReadManifestMess) rq);
    }

    @Override
    protected String getUsrDbTraceProfile() {
        return null;
    }
}