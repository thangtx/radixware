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

package org.radixware.kernel.designer.environment.actions;

import java.util.ArrayList;
import java.util.HashMap;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsTitledDefinition;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsFieldRefPropertyDef;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.types.Id;


public class FieldRefUpdater {

    private static final HashMap<AdsPath, ArrayList<AdsFieldRefPropertyDef.RefMapItem>> map = new HashMap<AdsPath, ArrayList<AdsFieldRefPropertyDef.RefMapItem>>();

    static {
        ArrayList<AdsFieldRefPropertyDef.RefMapItem> list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        AdsFieldRefPropertyDef.RefMapItem item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colASOJQU3FX7OBDCMFAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accRTGCUIE4YLOBDCMIAALOMT5GDM"), Id.Factory.loadFrom("prfKVD42XM4YLOBDCMIAALOMT5GDM")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col2S6CTYJ2X7OBDCMFAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfGUID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accEYV32QRX6NDF7H5BBTHIDTXAKQ"), Id.Factory.loadFrom("prfF5XTUGVWZNADVLNFGQ6Z3MOPUQ")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colUXGJTWQMYDOBDCMGACAL36IEWM"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accVERICUFCYLOBDCMIAALOMT5GDM"), Id.Factory.loadFrom("prfLYA2M5BWS5FRHFA3SBR64WJY2E")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colASOJQU3FX7OBDCMFAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accLTCXDF7OYPOBDCMNAALOMT5GDM"), Id.Factory.loadFrom("prfKVD42XM4YLOBDCMIAALOMT5GDM")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colE5Q6DYEDERBUZKAZJGZQLEL3PQ"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc327CS6242ZCW3NENMBVNMRFGSQ"), Id.Factory.loadFrom("prfDBCGB6SAOFGJTOXP6TMKQFPMDQ")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colCMPQPWEEGZGFJIE43TCB7BQD6I"));
        item.setFieldId(Id.Factory.loadFrom("prfFOLDERID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colCAWS3HK3LBA3JOJOHNJ557NDZE"));
        item.setFieldId(Id.Factory.loadFrom("prfINDICATORKIND"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accQXRDCYDRUFGO3EIVACCROYCQF4"), Id.Factory.loadFrom("prfTGJAWOMDCBARHKPWFCCQYNMXAQ")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col6UDMFMIWNVA2TGIOHC4I77M5JE"));
        item.setFieldId(Id.Factory.loadFrom("prfFOLDERID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colDZJNEK376FHTTDA7STWXKRYSPA"));
        item.setFieldId(Id.Factory.loadFrom("prfTERMID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc2R7EBR5FFRHE3FG6NEPOPPW6NI"), Id.Factory.loadFrom("prfEAHBPAHK2NBC5AKU6DRFE4SDCY")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colCLO2Y3YOAZBSPHXJ5WMJWYYYDU"));
        item.setFieldId(Id.Factory.loadFrom("prfTERMID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colOWYXV2ASENA6DIFAMSOBB5C5D4"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc6XYCVW7KQZFQ7GZQQJBZBUV77Y"), Id.Factory.loadFrom("prf5WLDWTYZQFCPFOYVWDFR6PFDCI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colMCSW4IAZNNGE3AYMAP4HT5AGRE"));
        item.setFieldId(Id.Factory.loadFrom("prfTERMID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colEGBZKEGMAVDKPDYBHLPHZ6P4QI"));
        item.setFieldId(Id.Factory.loadFrom("prfRID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accJZFGOEV3IZD7ZHMJZPQIP34G3A"), Id.Factory.loadFrom("prfBBSOYUHPB5CDJHG7OBKSRDC4AA")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colMPJPTKW6CFFMHFZ66SFHU5JIKE"));
        item.setFieldId(Id.Factory.loadFrom("prfFOLDERID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colGJ7PN6PMMFG2NDKYUTRCSA4R7E"));
        item.setFieldId(Id.Factory.loadFrom("prfINDICATORKIND"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colCMJKB42YVZGVPGQ2WGXNO7UBVU"));
        item.setFieldId(Id.Factory.loadFrom("prfSEVERITY"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accT5GTN7CUQFCULFFU5UTJ5S276I"), Id.Factory.loadFrom("prf2RO32TFLEVGAZE3S56NBSFRA7Q")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colGTTECIL6AJCDRKLOCEAMX5GFII"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accIP72EJGG2DORDE4MAAN7YHKUNI"), Id.Factory.loadFrom("prfE6KX4QOJ2DORDE4MAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colGTTECIL6AJCDRKLOCEAMX5GFII"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accJPHZOE5X4XOBDB5FAAN7YHKUNI"), Id.Factory.loadFrom("prfQ7CZVRGC4XOBDB5FAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colQPSMRJC24RDJPJWO54FNYTU2IQ"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc7RSTHHGA7TORDJDSAAN7YHKUNI"), Id.Factory.loadFrom("prfQ7CZVRGC4XOBDB5FAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colGTTECIL6AJCDRKLOCEAMX5GFII"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accXBCUWUWJ2DORDE4MAAN7YHKUNI"), Id.Factory.loadFrom("prfE6KX4QOJ2DORDE4MAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colHMFZF3SVPHNRDB4QAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prffInstId"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colCPHTT7SVPHNRDB4QAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prffPrevDay"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accUWAKCCRTPXOBDP2XAAN7YHKUNI"), Id.Factory.loadFrom("prfDRUUEPRIPXOBDP2XAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colHMFZF3SVPHNRDB4QAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prffInstId"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colCPHTT7SVPHNRDB4QAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prffNextDay"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc7OMZ3AZHPXOBDP2XAAN7YHKUNI"), Id.Factory.loadFrom("prfDRUUEPRIPXOBDP2XAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colIYAE4JV76ZBFLBVOMM5VZ6BUVU"));
        item.setFieldId(Id.Factory.loadFrom("prfGUID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colGO3C56PR4FHCTMF54XSP4RXD2U"));
        item.setFieldId(Id.Factory.loadFrom("prfSCHEMEGUID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accDQ45JDLZGBFGNCRYT4TZXRN2CM"), Id.Factory.loadFrom("prfY74EGILEBHPBDHNEABIFNQAABA")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colIYAE4JV76ZBFLBVOMM5VZ6BUVU"));
        item.setFieldId(Id.Factory.loadFrom("prfGUID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colGO3C56PR4FHCTMF54XSP4RXD2U"));
        item.setFieldId(Id.Factory.loadFrom("prfSCHEMEGUID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc4PNYHOA3U5HYFPIJVZOIZONCWE"), Id.Factory.loadFrom("prfEGRKFKEGTRH2XL72N26V6AOMWU")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colLM4EODOJ5HOBDCSVCEIRCEIRCE"));
        item.setFieldId(Id.Factory.loadFrom("prfGUID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accDCMTUPXTRJCRRAYKI2IBRM37RQ"), Id.Factory.loadFrom("prfFD7DL66ETRBVDBZGMTGANR23LA")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colLM4EODOJ5HOBDCSVCEIRCEIRCE"));
        item.setFieldId(Id.Factory.loadFrom("prfGUID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accOW3CDUMGGRA4PHZOGCQNY2PLTQ"), Id.Factory.loadFrom("prfIDQH2YNQDFD4LFIMKU3V456FBA")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col45E5HYG32LNRDCKSABIFNQAABA"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accDTWP6Y4JV5AONKXHSV6ZZPT57Y"), Id.Factory.loadFrom("prfZFD4EPENW5HATCXAUV5AXCVGQM")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colRMOUBWS7PHNRDB4QAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfTRANID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accXUYSFVG62XNRDISQAAAAAAAAAA"), Id.Factory.loadFrom("prfFS4K2WGO4DNRDJKTAAMPGXSZKU")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accRS2VFOVDEDOBDOMUABIFNQAABA"), Id.Factory.loadFrom("prf732TOQ5LEDOBDOMUABIFNQAABA")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colJZOWNPRTHPOBDONCABIFNQAABA"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accWEZQC2QQUTPBDO5BABIFNQAABA"), Id.Factory.loadFrom("prfYDHVYXARUTPBDO5BABIFNQAABA")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colY4Z2DT6ZPLNRDB4TAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accAIULMRX3A3PBDLT5AAN7YHKUNI"), Id.Factory.loadFrom("prfAAULMRX3A3PBDLT5AAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col5NPXV64OZLORDEEYAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accKX7UDGBTMLPBDN6HAAN7YHKUNI"), Id.Factory.loadFrom("prfZ4OLHPA75XORDLF2AAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col5NPXV64OZLORDEEYAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accUUHUEGDSGZDINDQSHNHBDNGJTY"), Id.Factory.loadFrom("prfCPPLGBAS2JAAZJCEH3KSIVSDTE")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colY4Z2DT6ZPLNRDB4TAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accAII5JRH2A3PBDLT5AAN7YHKUNI"), Id.Factory.loadFrom("prfAAULMRX3A3PBDLT5AAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col5NPXV64OZLORDEEYAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accQWNIHPQ65XORDLF2AAN7YHKUNI"), Id.Factory.loadFrom("prfZ4OLHPA75XORDLF2AAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colHMQNH7EFY7ORDM76ABIFNQAABA"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accMXYRSNWRFZDHBKRK5VXOBE7KAY"), Id.Factory.loadFrom("prf2FQPRCM7P5DYHOUZ3J2JDJNCD4")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colMJLGUQSUY7ORDM76ABIFNQAABA"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc5KM4Z6HZRZDN5L3QHSKXRF7FRE"), Id.Factory.loadFrom("prfO2Y5WEEPCBEIREB2CGPVSZBBJI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colUCCPDFTTZLORDM76ABIFNQAABA"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accUDC3GMWFQZEEZPNBQV3XPMVMIA"), Id.Factory.loadFrom("prfROEEVOQLHRESZGZP5BXXRBME3A")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col7FRTIWCTY7ORDM76ABIFNQAABA"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc3KNV5AAL4RHVPHOTSU6D7HAI3Q"), Id.Factory.loadFrom("prfTBYZ7BASLFEGRIAARTO5IT2TIA")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col4ESVII2TY7ORDM76ABIFNQAABA"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accWLBX3VUHAFEOJM3SRNSE7B43MA"), Id.Factory.loadFrom("prf2QGXFAFQQFEYVKHRI6WM6T5DZM")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colSEHLZWBOYPOBDCRLABIFNQAABA"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accSEDIPMIMDBD3ZGQCEPICIIOJS4"), Id.Factory.loadFrom("prfUZIKYSF5XNBTFEXJELZ4764XQE")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colOZIBKY7PJXNRDAQSABIFNQAAAE"));
        item.setFieldId(Id.Factory.loadFrom("prfSWITCHINSTID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accSHXLPR3N4PNRDC7HAAMPGXSZKU"), Id.Factory.loadFrom("prf2XXGDNTO4PNRDC7HAAMPGXSZKU")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colQ4UUICTJ4PNRDC7HAAMPGXSZKU"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accSHXLPR3N4PNRDC7HAAMPGXSZKU"), Id.Factory.loadFrom("prfHY7N2V3QHPOBDLVOAAMPGXSZKU")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colYDNWBB4Q43OBDB5FAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfSCHEMEGUID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colYHNWBB4Q43OBDB5FAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc5KY5Y5UV43OBDB5FAAN7YHKUNI"), Id.Factory.loadFrom("prfWBOL37UV43OBDB5FAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colTLIR32A7QPORDMNEABIFNQAABA"));
        item.setFieldId(Id.Factory.loadFrom("prfTOPICID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc5VPP7AE3QPORDMNEABIFNQAABA"), Id.Factory.loadFrom("prfBCXB7RE5QPORDMNEABIFNQAABA")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col2XODHIBAQPORDMNEABIFNQAABA"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc5VPP7AE3QPORDMNEABIFNQAABA"), Id.Factory.loadFrom("prfDVPQ7NE5QPORDMNEABIFNQAABA")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colBALJTTRM7FHXLO2AIZJZANIWG4"));
        item.setFieldId(Id.Factory.loadFrom("prfCFGID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colJT6UMX2XBVDANMRBAIECJT2B3E"));
        item.setFieldId(Id.Factory.loadFrom("prfCLASSIDX"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colWOCOKBKPENC6NO43VYEBYRSOPU"));
        item.setFieldId(Id.Factory.loadFrom("prfIDX"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colLO4ZYDAD35ASBCRE7VI6EJLI7Y"));
        item.setFieldId(Id.Factory.loadFrom("prfVERSION"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc2JBZBRQOPBFDZPRJFD5VOBZBJU"), Id.Factory.loadFrom("prfRAIYVQF5HND2VCYAQHJN4CCIZU")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colBALJTTRM7FHXLO2AIZJZANIWG4"));
        item.setFieldId(Id.Factory.loadFrom("prfCFGID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colJT6UMX2XBVDANMRBAIECJT2B3E"));
        item.setFieldId(Id.Factory.loadFrom("prfCLASSIDX"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colWOCOKBKPENC6NO43VYEBYRSOPU"));
        item.setFieldId(Id.Factory.loadFrom("prfIDX"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colLO4ZYDAD35ASBCRE7VI6EJLI7Y"));
        item.setFieldId(Id.Factory.loadFrom("prfVERSION"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accVSMC2M4JBBCEPP2Y5WDNTHFX7M"), Id.Factory.loadFrom("prfZXODWEEO2FBYJNQGK4GDWA6BVE")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colBALJTTRM7FHXLO2AIZJZANIWG4"));
        item.setFieldId(Id.Factory.loadFrom("prfCFGID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colJT6UMX2XBVDANMRBAIECJT2B3E"));
        item.setFieldId(Id.Factory.loadFrom("prfCLASSIDX"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colWOCOKBKPENC6NO43VYEBYRSOPU"));
        item.setFieldId(Id.Factory.loadFrom("prfIDX"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colLO4ZYDAD35ASBCRE7VI6EJLI7Y"));
        item.setFieldId(Id.Factory.loadFrom("prfVERSION"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accBBIQYUT2HJBMPAXBT6N7OUS7HQ"), Id.Factory.loadFrom("prfPISWHXD3BDPBDHNEABIFNQAABA")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colBALJTTRM7FHXLO2AIZJZANIWG4"));
        item.setFieldId(Id.Factory.loadFrom("prfCFGID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colJT6UMX2XBVDANMRBAIECJT2B3E"));
        item.setFieldId(Id.Factory.loadFrom("prfCLASSIDX"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colWOCOKBKPENC6NO43VYEBYRSOPU"));
        item.setFieldId(Id.Factory.loadFrom("prfIDX"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colLO4ZYDAD35ASBCRE7VI6EJLI7Y"));
        item.setFieldId(Id.Factory.loadFrom("prfVERSION"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accYP36QMIB7RAO3OS6DZ5BGXOMNI"), Id.Factory.loadFrom("prfHYNQT7L2NNBGDPBXCUFLWMWSUA")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colCZV4JXLT2XOBDBPOAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfTRANID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colOALPV3DT2XOBDBPOAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfENTRYSEQ"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colC5V4JXLT2XOBDBPOAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfSEQ"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc7C7B7IIO6HORDFB7AAN7YHKUNI"), Id.Factory.loadFrom("prfIOW5L5Q56HORDFB7AAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colY4Z2DT6ZPLNRDB4TAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfACCTID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc7C7B7IIO6HORDFB7AAN7YHKUNI"), Id.Factory.loadFrom("prfRR337LDE6PORDMDPAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colJSMX3ENJSLOBDCKTAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfGENERATORGUID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colNNID3G5JSLOBDCKTAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfSEQ"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accV7PCIBFQSLOBDCKTAALOMT5GDM"), Id.Factory.loadFrom("prf5NT5KV5QSLOBDCKTAALOMT5GDM")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colD3O7TQ6TPXNRDOKKABIFNQAABA"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accNTQ7MED2PVAGRPHYADH43EXKHM"), Id.Factory.loadFrom("prfEYYVFRAPPRH75MSMMON2F5QYEA")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col7FGYHS5HNBCE7M7FF7QC3ZXFCI"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc6AVJGUP7ETPBDLLBAAN7YHKUNI"), Id.Factory.loadFrom("prf3FGOOFLSDDPBDJDVAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colS5EA5TI45HOBDCSUCEIRCEIRCE"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accNZKFSWXXJDPBDL6BAAN7YHKUNI"), Id.Factory.loadFrom("prfJTJE727XJDPBDL6BAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colS5EA5TI45HOBDCSUCEIRCEIRCE"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accKYCS3THWJDPBDL6BAAN7YHKUNI"), Id.Factory.loadFrom("prf6USDPU7XJDPBDL6BAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colTD5M4R5GJLPBDLKAAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfCASGUID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colTH5M4R5GJLPBDLKAAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfFINOPERKIND"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colTL5M4R5GJLPBDLKAAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfDIR"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accZHFBCF7RJ7PBDNRJAAN7YHKUNI"), Id.Factory.loadFrom("prfVXR5KLXSJ7PBDNRJAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colU5P6ZTJ3JPPBDL4SAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfHANDLERID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colVBP6ZTJ3JPPBDL4SAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfSIGNUM"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc4OUNGLVWKDPBDOJTAAN7YHKUNI"), Id.Factory.loadFrom("prfMIC5FR5WKDPBDOJTAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col5XNHST45QPNRDB5BAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfCUSTCCY"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accPCUYY6LR63OBDCTDCEIRCEIRCE"), Id.Factory.loadFrom("prfK3QDKPJT67OBDCTGCEIRCEIRCE")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colRMOUBWS7PHNRDB4QAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accPCUYY6LR63OBDCTDCEIRCEIRCE"), Id.Factory.loadFrom("prfZCFRTZB467OBDCTGCEIRCEIRCE")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colS4HTPELMSBDSFGVUVFWIW3L2VE"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc3ATA6HXO73OBDPDXAAN7YHKUNI"), Id.Factory.loadFrom("prfHDJN5B7O73OBDPDXAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colZ6CWVQG5CLPBDL6XAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfCONTRACTID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col2CCWVQG5CLPBDL6XAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfPHASE"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accL6LFIACFCXPBDB55AAN7YHKUNI"), Id.Factory.loadFrom("prf7NLUM72FCXPBDB55AAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col45E5HYG32LNRDCKSABIFNQAABA"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accW5J7WHWU7POBDPJEAAN7YHKUNI"), Id.Factory.loadFrom("prfYPUY5LGV7POBDPJEAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colS5EA5TI45HOBDCSUCEIRCEIRCE"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colTBEA5TI45HOBDCSUCEIRCEIRCE"));
        item.setFieldId(Id.Factory.loadFrom("prfINSTID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colKEH3JCQ55HOBDCSUCEIRCEIRCE"));
        item.setFieldId(Id.Factory.loadFrom("prfCONTRACT1RID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colU4TKL7Q45HOBDCSUCEIRCEIRCE"));
        item.setFieldId(Id.Factory.loadFrom("prfCONTRACT2RID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accSNYZPHQL6DOBDKUWAAN7YHKUNI"), Id.Factory.loadFrom("prfUKSULEYL6DOBDKUWAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colS5EA5TI45HOBDCSUCEIRCEIRCE"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accCDRIBVFL6XOBDCTCCEIRCEIRCE"), Id.Factory.loadFrom("prf6TBELTVXM7ORDNU7ABIFNQAABA")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colM2H5A7U4JLPBDLKAAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfCASGUID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colM6H5A7U4JLPBDLKAAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfROLE"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accR2EJR3LPJ3PBDMDJAAN7YHKUNI"), Id.Factory.loadFrom("prfEUWR4KLSJ3PBDMDJAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colHT6O6A5SUHORDLRKAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfCONTRACTID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colHX6O6A5SUHORDLRKAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accHLKDVC5TUHORDLRKAAN7YHKUNI"), Id.Factory.loadFrom("prfELGHMPFUUHORDLRKAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col54GW2LFDJLPBDLKAAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc3KNYR3DUJ3PBDMDJAAN7YHKUNI"), Id.Factory.loadFrom("prfXDAOGRDVJ3PBDMDJAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col2S6CTYJ2X7OBDCMFAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfTARIFFPLANGUID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accTHPFYDTRDDPBDJDVAAN7YHKUNI"), Id.Factory.loadFrom("prf3FGOOFLSDDPBDJDVAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colTHJNWLY45HOBDCSUCEIRCEIRCE"));
        item.setFieldId(Id.Factory.loadFrom("prfCONTRACTID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colUE2IONQ45HOBDCSUCEIRCEIRCE"));
        item.setFieldId(Id.Factory.loadFrom("prfCCY"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colIXIXCRA45HOBDCSUCEIRCEIRCE"));
        item.setFieldId(Id.Factory.loadFrom("prfACCTROLE"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colSTSP3NI533ORDLT3AAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfCLASSIFICATION"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accETXMSYIR67OBDBGIAAN7YHKUNI"), Id.Factory.loadFrom("prfNAZJFTQR67OBDBGIAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col4VEWOWBVJPPBDL4SAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accI5IXQPXSJ7PBDNRJAAN7YHKUNI"), Id.Factory.loadFrom("prfKY3VG6HSJ7PBDNRJAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colY4Z2DT6ZPLNRDB4TAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfACCTID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accNZIW73CCWRHM7C26HPMNO7XJOE"), Id.Factory.loadFrom("prfAKSXQ2CMAFCVBCRDEAMPHI454E")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colS4HTPELMSBDSFGVUVFWIW3L2VE"));
        item.setFieldId(Id.Factory.loadFrom("prfterminalId"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc63YN5RXJLHOBDHLHAAMPGXSZKU"), Id.Factory.loadFrom("prfGLPSWYXKLHOBDHLHAAMPGXSZKU")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colS4HTPELMSBDSFGVUVFWIW3L2VE"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accAFBACM7YB5ANRPJTTP3KWJ4YQY"), Id.Factory.loadFrom("prf6SKTRZYKKFDL5PMLBGGKYPQ7SM")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colZMRCUREYUXORDLQMAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accKVZJBMSNMRG7ZMTKLQDISKENRI"), Id.Factory.loadFrom("prfR5QOJCHGETPBDLLBAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colZMRCUREYUXORDLQMAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accBIZM2LCUXXORDJWWAAN7YHKUNI"), Id.Factory.loadFrom("prfQ5TMHZ2UXXORDJWWAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colRHNYG7HRNBB4VCP6G4HHKMZUFI"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accQ4CJRUVZXPORDEGLAAN7YHKUNI"), Id.Factory.loadFrom("prfZ5LT7FF3XPORDEGLAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colO5D7MQEIUXORDLQMAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accZAHBBYMRY3ORDMAZAAN7YHKUNI"), Id.Factory.loadFrom("prfYJ4W7CUWY3ORDMAZAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col4VWOCZKBUDORDJSUAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accV4S4OUYPXLORDMBEAAN7YHKUNI"), Id.Factory.loadFrom("prfQPHKXW5FXPORDEGLAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colJRPDEECAUDORDJSUAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accMVEV2MVZW7ORDLWQAAN7YHKUNI"), Id.Factory.loadFrom("prfCJH3ITQOXLORDMBEAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colO5D7MQEIUXORDLQMAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accQFE5OXMTY3ORDMAZAAN7YHKUNI"), Id.Factory.loadFrom("prfE6IFCREXY3ORDMAZAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colO5D7MQEIUXORDLQMAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accEBYD4IPEW3ORDDMBAAN7YHKUNI"), Id.Factory.loadFrom("prfTDFIV37GW3ORDDMBAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colRMOUBWS7PHNRDB4QAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfPREVTRANID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accQGT3Q4Y7UPOBDCBGAAN7YHKUNI"), Id.Factory.loadFrom("prfPJ3GFGGP4DNRDJKTAAMPGXSZKU")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colOENTM3HGPHNRDB4RAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfTRANID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colNUQS4JHGPHNRDB4RAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfSEQ"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accRE2TOL2ZITPBDMHWAAN7YHKUNI"), Id.Factory.loadFrom("prf4UK4W3WO4DNRDJKTAAMPGXSZKU")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colOENTM3HGPHNRDB4RAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfTRANID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colNUQS4JHGPHNRDB4RAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfPARENTDOERSEQ"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accRE2TOL2ZITPBDMHWAAN7YHKUNI"), Id.Factory.loadFrom("prfUUG2XGDIITPBDMHWAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colRMOUBWS7PHNRDB4QAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfNEXTTRANID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accNBMOSNOO2LNRDISQAAAAAAAAAA"), Id.Factory.loadFrom("prfJAFGD3GP4DNRDJKTAAMPGXSZKU")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colOENTM3HGPHNRDB4RAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfTRANID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colNUQS4JHGPHNRDB4RAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfSEQ"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accBZWXQAZZ2LNRDISQAAAAAAAAAA"), Id.Factory.loadFrom("prf4UK4W3WO4DNRDJKTAAMPGXSZKU")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colRMOUBWS7PHNRDB4QAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfNEXTTRANID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accABMLXBAUPPOBDCNFAAN7YHKUNI"), Id.Factory.loadFrom("prfJAFGD3GP4DNRDJKTAAMPGXSZKU")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colQDOWGDTW2XOBDBPOAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfTRANID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colP7OWGDTW2XOBDBPOAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accZK3S7GUB4DOBDKK4AAN7YHKUNI"), Id.Factory.loadFrom("prfPPLQQKUC4DOBDKK4AAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colRMOUBWS7PHNRDB4QAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfTRANID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accPB4Q77TX53NRDOMBABIFNQAABA"), Id.Factory.loadFrom("prf3Y5NFYLC6HNRDOMDABIFNQAABA")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col5NPXV64OZLORDEEYAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accDLAGKAQA7DOBDBAWAAN7YHKUNI"), Id.Factory.loadFrom("prfXUQPWOYC7DOBDBAWAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colYSJXT4LW2XOBDBPOAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfTRANID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colYWJXT4LW2XOBDBPOAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfSEQ"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc7PHDLUMMO3PBDNUFAAN7YHKUNI"), Id.Factory.loadFrom("prfBOQGIEMNO3PBDNUFAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colRMOUBWS7PHNRDB4QAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfTRANID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc26AFFRFOMLORDO4HABIFNQAABA"), Id.Factory.loadFrom("prfQV4BURVSMLORDO4HABIFNQAABA")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colRMOUBWS7PHNRDB4QAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfPREVTRANID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accWVMGGINLYDNRDISQAAAAAAAAAA"), Id.Factory.loadFrom("prfPJ3GFGGP4DNRDJKTAAMPGXSZKU")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colCZV4JXLT2XOBDBPOAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfTRANID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colOALPV3DT2XOBDBPOAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfENTRYSEQ"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colC5V4JXLT2XOBDBPOAAN7YHKUNI"));
        item.setFieldId(Id.Factory.loadFrom("prfSEQ"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accTKHFCVMKO3PBDNUFAAN7YHKUNI"), Id.Factory.loadFrom("prfVZQDRHUMO3PBDNUFAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colYDY4KBUJPPNRDB4UAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accRB7PRVTEZ3OBDBHCAAN7YHKUNI"), Id.Factory.loadFrom("prfADBOXD3FZ3OBDBHCAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colRMOUBWS7PHNRDB4QAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfTRANID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc6SNWP7KXZ3OBDBHCAAN7YHKUNI"), Id.Factory.loadFrom("prfLVYLHS2YZ3OBDBHCAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colHCKH2CEMPPNRDB4UAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc6SNWP7KXZ3OBDBHCAAN7YHKUNI"), Id.Factory.loadFrom("prfYYD2SYT5YDORDMPCAAN7YHKUNI")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colTLDOORT6ZDNRDOLPABIFNQAABA"));
        item.setFieldId(Id.Factory.loadFrom("prfINTERFACEID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colTPDOORT6ZDNRDOLPABIFNQAABA"));
        item.setFieldId(Id.Factory.loadFrom("prfEXTATTRKIND"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colTTDOORT6ZDNRDOLPABIFNQAABA"));
        item.setFieldId(Id.Factory.loadFrom("prfINTATTRKIND"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colTXDOORT6ZDNRDOLPABIFNQAABA"));
        item.setFieldId(Id.Factory.loadFrom("prfISINCOMING"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colHSA4ISI3HTOBDONCABIFNQAABA"));
        item.setFieldId(Id.Factory.loadFrom("prfTRANDIRECTION"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accCU5INFX34PNRDOL3ABIFNQAABA"), Id.Factory.loadFrom("prfUVKUT5N26TNRDOMHABIFNQAABA")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colY43B3GKXE5DFVIJLTHCQVZXZNE"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accO4R66HPLQNCO7FW2O77YVAD2U4"), Id.Factory.loadFrom("prfOGRMCFAPR5AFTKHWXM2V6RXX3M")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colVHVLPHPMTDOBDCK2AALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfPRODUCTID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colFMASRLXMTDOBDCK2AALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accF54L6YQLT7OBDCLAAALOMT5GDM"), Id.Factory.loadFrom("prfGYBUAKYMT7OBDCLAAALOMT5GDM")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colZ5LAFQ6Z2LNRDCKSABIFNQAABA"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc6J3NQPJKA3ORDCTVCEIRCEIRCE"), Id.Factory.loadFrom("prfKIPF47BKA3ORDCTVCEIRCEIRCE")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colDL4HONPSPHNRDB4RAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accFLENZQYIQVBXBFTDOKKHCHAL5E"), Id.Factory.loadFrom("prfA224HH6NGRCK3PKRGI7OLTUADM")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colZ5LAFQ6Z2LNRDCKSABIFNQAABA"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accDC2J2JCJ7FEAFPFSI6JGLSXJ3Q"), Id.Factory.loadFrom("prfAL6OU5GDLZDXNOLYYMKGYENANE")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col4PWRIYYD4TNRDC7HAAMPGXSZKU"));
        item.setFieldId(Id.Factory.loadFrom("prfOWNERENTITYGUID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col4TWRIYYD4TNRDC7HAAMPGXSZKU"));
        item.setFieldId(Id.Factory.loadFrom("prfOWNERID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colOAFRLDMZ77NRDA26AAMPGXSZKU"));
        item.setFieldId(Id.Factory.loadFrom("prfGUID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accSL4DSAGQAHOBDA26AAMPGXSZKU"), Id.Factory.loadFrom("prfUK6ZF5WQAHOBDA26AAMPGXSZKU")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colXDGF3W2RY7ORDM76ABIFNQAABA"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accNVX6KNWS4FEJZA2JWBCBPWJ2KA"), Id.Factory.loadFrom("prfJOLRTZZOHBAMDBYQPPOXQW6RHE")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col45E5HYG32LNRDCKSABIFNQAABA"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accOEM3YA4BIRDGVPKQ6HOHCWYHRI"), Id.Factory.loadFrom("prf5DYHHIEOABB5ZNWVZK3PY55IJY")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col4PWRIYYD4TNRDC7HAAMPGXSZKU"));
        item.setFieldId(Id.Factory.loadFrom("prfOWNERENTITYGUID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col4TWRIYYD4TNRDC7HAAMPGXSZKU"));
        item.setFieldId(Id.Factory.loadFrom("prfOWNERID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colOAFRLDMZ77NRDA26AAMPGXSZKU"));
        item.setFieldId(Id.Factory.loadFrom("prfGUID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accLIAZYVDJHLOBDCIDAALOMT5GDM"), Id.Factory.loadFrom("prfUK6ZF5WQAHOBDA26AAMPGXSZKU")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colS4HTPELMSBDSFGVUVFWIW3L2VE"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc63LTU4UCUFF73GCUGI444M56ZQ"), Id.Factory.loadFrom("prf3BGT4752DVEFXBKL3DIARHAKIM")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colS5EA5TI45HOBDCSUCEIRCEIRCE"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc4PX72L6VGVF6FOU6NOAO7ECIIM"), Id.Factory.loadFrom("prfRO5JBQZLHFF3TAELLJ7RILREMA")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colRMOUBWS7PHNRDB4QAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfTRANID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accVLY5SORDFHOBDOMWABIFNQAABA"), Id.Factory.loadFrom("prf3Y5NFYLC6HNRDOMDABIFNQAABA")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col7AT4HYUBPPNRDB4UAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfENTITYGUID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col2IP2N7MBPPNRDB4UAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfOBJECTID"));
        list.add(item);
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colZZW7QHUCPPNRDB4UAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfRESTRICTIONGUID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accIL7AMV7XIZCOXAV5HZAC5GTOVY"), Id.Factory.loadFrom("prfUMROSO7Y2FHU3JPMCKGYYFBB24")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colHWHRNERXVLOBDCLSAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc5SA67DA4V3OBDCLVAALOMT5GDM"), Id.Factory.loadFrom("prf3PJECBY2V3OBDCLVAALOMT5GDM")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colMF3T2LPMKXOBDCI6AALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accWNPZNUAZKBFG3MPXTFGAAEETE4"), Id.Factory.loadFrom("prfDXNYNQ35YJB53NCTAPONEQ6BOU")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colDHYHOUSBI3OBDCIOAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accCR2G3AHYOZDMHDRP7THJYVFF4A"), Id.Factory.loadFrom("prfFQMKAZDZAZCP3LUKC7Q53LBILM")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("col2BAXRDR4I3OBDCIOAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acc7NDGVBXM2JAX3AU2CR4D5HILKE"), Id.Factory.loadFrom("prfNWGVXBT4TZEBXDTS427HX7244M")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colDQQG5QKDI3OBDCIOAALOMT5GDM"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accFCIAAXYOSNELLGDHNOZN3S2AZA"), Id.Factory.loadFrom("prf4WVG74PGIZGWJESMOKW4A7OIDY")}), list);

        list = new ArrayList<AdsFieldRefPropertyDef.RefMapItem>();
        item = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
        item.setColumnId(Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA"));
        item.setFieldId(Id.Factory.loadFrom("prfID"));
        list.add(item);
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("accVVDQV2J6RBDEFI4CPVH3KUBLE4"), Id.Factory.loadFrom("prfT7RCLAD4LZAOPBCLOQMP2G75OQ")}), list);
    }

    public void update(RadixObject obj, StringBuilder b) {
        if (obj instanceof AdsFieldRefPropertyDef) {
            AdsFieldRefPropertyDef def = (AdsFieldRefPropertyDef) obj;
            AdsPath path = new AdsPath(def.getIdPath());
            ArrayList<AdsFieldRefPropertyDef.RefMapItem> items = map.get(path);
            if (items != null) {
                def.getFieldToColumnMap().clear();
                for (AdsFieldRefPropertyDef.RefMapItem item : items) {
                    def.getFieldToColumnMap().add(item);
                }
                b.append("Updated: " + def.getQualifiedName());
                b.append("\n");
            }
        }
    }
}
