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

import java.io.File;
import java.nio.file.Files;
import java.sql.SQLException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;

import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.schemas.eas.ExceptionEnum;
import org.radixware.schemas.eas.ReadManifestMess;
import org.radixware.schemas.eas.ReadManifestRs;
import org.radixware.schemas.easWsdl.ReadManifestDocument;

final class ReadManifestRequest extends EasRequest {

    private static final String SAP_ADDRESSES_MAP_FILE = SystemPropUtils.getStringSystemProp("rdx.sap.addresses.map.file", null);//scp,originalAddress,newAddress triples on separate lines
    private static final Map<String, String> ADDRESSES_MAP = new HashMap<>();

    static {
        try {
            if (SAP_ADDRESSES_MAP_FILE != null) {
                final byte[] fileData = Files.readAllBytes(new File(SAP_ADDRESSES_MAP_FILE).toPath());
                if (fileData != null) {
                    final String allFileData = new String(fileData);
                    final String[] lines = allFileData.split("\n");
                    for (String line : lines) {
                        line = line.replace("\r", "");
                        final String[] items = line.split(",");
                        if (items.length == 3) {
                            ADDRESSES_MAP.put(getAddressKey(items[0], items[1]), items[2]);
                        }
                    }
                }
                System.out.println("Loaded addresses map from " + SAP_ADDRESSES_MAP_FILE + ": " + ADDRESSES_MAP);
            }
        } catch (Throwable t) {
            System.out.println("Unable to load addresses map from " + SAP_ADDRESSES_MAP_FILE + ": " + ExceptionTextFormatter.throwableToString(t));
        }
    }

    private static String getAddressKey(final String scp, final String originalAddress) {
        return (scp == null ? "" : scp) + "~" + originalAddress;
    }

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
            saps = presenter.readSaps(scpName, request.getReadManifestRq().getAuthType());
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
            final String effectiveAddress = getOverridenAddress(scpName, sap.getAddress());
            sapXml.setAddress(effectiveAddress);//for backward compatibility
            sapXml.setAddressCDATA(effectiveAddress);//JMS address can be written in multiple lines
            sapXml.setBlockingPeriod(Math.max(0, sap.getBlockingPeriod()));
            sapXml.setConnectTimeout(Math.max(0, sap.getConnectTimeout()));
            sapXml.setName(sap.getName());
            sapXml.setPriority(Math.max(0, sap.getPriority()));
            sapXml.setSecurityProtocol(sap.getSecurityProtocol());
            sapXml.setChannelType(sap.getChannelType());            
            if (sap.getAadcMemberId()!=null){
                sapXml.setAadcMemberId(Long.valueOf(sap.getAadcMemberId().longValue()));
            }
        }
        return res;
    }

    @Override
    XmlObject process(final XmlObject rq) throws ServiceProcessFault, InterruptedException {
        ReadManifestDocument doc = null;
        try{
            doc = process((ReadManifestMess) rq);
        }finally{
            postProcess(rq, doc==null ? null : doc.getReadManifest().getReadManifestRs());
        }
        return doc;
    }

    @Override
    protected String getUsrDbTraceProfile() {
        return null;
    }

    private String getOverridenAddress(final String scp, final String originalAddress) {
        final String key = getAddressKey(scp, originalAddress);
        if (ADDRESSES_MAP.containsKey(key)) {
            return ADDRESSES_MAP.get(key);
        }
        return originalAddress;
    }
}
