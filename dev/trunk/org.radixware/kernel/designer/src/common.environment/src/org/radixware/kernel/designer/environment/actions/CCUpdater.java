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
import org.radixware.kernel.common.defs.ads.clazz.members.AdsFieldRefPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.types.Id;


public class CCUpdater {

    private static final HashMap<AdsPath, Id> map = new HashMap<AdsPath, Id>();

    static {
        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aec42ES5R2PQND3TO3GUZ2TEMNKCM"), Id.Factory.loadFrom("sprYGTRMAOF4FCT3P4MML6HE5QRE4")}), Id.Factory.loadFrom("eccT6J4UZBBYVCWNE62QFMWRMOHM4"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acl26BKD5DYGFHEVCMTCXQAB2MGS4"), Id.Factory.loadFrom("sprQNGKP6LXRFGTHMFEEJQDVVRJQ4")}), Id.Factory.loadFrom("eccT6J4UZBBYVCWNE62QFMWRMOHM4"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecRWKSOOCNYJCGBIPGH4KP67QC3A"), Id.Factory.loadFrom("sprFNEXQLLJGNA6BCXAXDSQYONZW4")}), Id.Factory.loadFrom("ecc2VE4JF7HUBDHTG3P42G2M3MEN4"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecRWKSOOCNYJCGBIPGH4KP67QC3A"), Id.Factory.loadFrom("sprGIU2SLHOBZHHTFRBU7QERGS5R4")}), Id.Factory.loadFrom("ecc2VE4JF7HUBDHTG3P42G2M3MEN4"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acl3MLKC73QTNG3NDCM5SUKPORJBA"), Id.Factory.loadFrom("sprEA5NQNBNCFFQFPRAWTQLN4GHCU")}), Id.Factory.loadFrom("eccZMCDFRDL4DOBDIEAAALOMT5GDM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aclZILJNYPUJDPBDC47ABIFNQAABA"), Id.Factory.loadFrom("sprFQYG5CXYJDPBDC47ABIFNQAABA")}), Id.Factory.loadFrom("eccLOTOSJAFIXPBDC45ABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aclXSDLIFPMA3ORDCTWCEIRCEIRCE"), Id.Factory.loadFrom("sprY3GDS2R6YHORDBBJABIFNQAABA")}), Id.Factory.loadFrom("eccDNKWU4J6YHORDBBJABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aclW6N5QPMG3PNRDCK3ABIFNQAABA"), Id.Factory.loadFrom("sprCKJJ3IEC2RGRHE7ETGIEMG7SOE")}), Id.Factory.loadFrom("eccZ52OQNZ6YHORDBBJABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecB7WPZ6KBPJACXOYCH4FZHWOTKM"), Id.Factory.loadFrom("sprE226JB73JDPBDC47ABIFNQAABA")}), Id.Factory.loadFrom("eccB6JACJX2JDPBDC47ABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecB7WPZ6KBPJACXOYCH4FZHWOTKM"), Id.Factory.loadFrom("sprGWXQ5MQB55FPTFPPMVGASYUEKA")}), Id.Factory.loadFrom("eccLOTOSJAFIXPBDC45ABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecB7WPZ6KBPJACXOYCH4FZHWOTKM"), Id.Factory.loadFrom("sprRZKRJYX2JDPBDC47ABIFNQAABA")}), Id.Factory.loadFrom("eccIMKCBST3ITPBDC44ABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aclUYHIRODLITPBDC44ABIFNQAABA"), Id.Factory.loadFrom("sprBFTF7XT3ITPBDC44ABIFNQAABA")}), Id.Factory.loadFrom("eccIMKCBST3ITPBDC44ABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecOFDOVFGR2LNRDCKSABIFNQAABA"), Id.Factory.loadFrom("sprU3M5TRU42PNRDCKTABIFNQAABA")}), Id.Factory.loadFrom("ecc7YLSVZMCA7OBDCMDABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecH6LZDMGZ2LNRDCKSABIFNQAABA"), Id.Factory.loadFrom("sprRLZGP55D2PNRDCKTABIFNQAABA")}), Id.Factory.loadFrom("eccKWGFFVPJAHOBDA26AAMPGXSZKU"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecAE6RQPRVJPPBDL4SAAN7YHKUNI"), Id.Factory.loadFrom("sprSDX4OKCFCBFYRMPD7V7FMZQO4Q")}), Id.Factory.loadFrom("ecc7TF5T4VACNAB5JYU4NMHFISP3Q"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecS3GH4MTP3RE4HC7IYR4JWOWQHI"), Id.Factory.loadFrom("sprCSASAZZD6VEPJLCXGWI5GHTDIU")}), Id.Factory.loadFrom("eccANY3H4CTHFDSVABDDUFSTU23BI"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aec3QMF5LM5BZBOJI5O6W4VOZRLME"), Id.Factory.loadFrom("spr5YVAXTTNHVEQZEYQJWAEEJOCRI")}), Id.Factory.loadFrom("eccYNUEKU2SJZBT3IUVTVXSP6SBE4"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecW3GF3W2RY7ORDM76ABIFNQAABA"), Id.Factory.loadFrom("spr5HL74NVAZLORDM76ABIFNQAABA")}), Id.Factory.loadFrom("eccMNM56K4VY7ORDM76ABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecW3GF3W2RY7ORDM76ABIFNQAABA"), Id.Factory.loadFrom("spr6HDVZKU5ZLORDM76ABIFNQAABA")}), Id.Factory.loadFrom("eccKGPQBV45ZLORDM76ABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecW3GF3W2RY7ORDM76ABIFNQAABA"), Id.Factory.loadFrom("spr6LDVZKU5ZLORDM76ABIFNQAABA")}), Id.Factory.loadFrom("eccNUT5KP46ZLORDM76ABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecW3GF3W2RY7ORDM76ABIFNQAABA"), Id.Factory.loadFrom("sprNWQIE5E6ZLORDM76ABIFNQAABA")}), Id.Factory.loadFrom("eccVOY6MIUVY7ORDM76ABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecW3GF3W2RY7ORDM76ABIFNQAABA"), Id.Factory.loadFrom("sprWMKVH4CYY7ORDM76ABIFNQAABA")}), Id.Factory.loadFrom("eccPHN3BHCZY7ORDM76ABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecMBLGUQSUY7ORDM76ABIFNQAABA"), Id.Factory.loadFrom("sprDKSOTZDHY7ORDM76ABIFNQAABA")}), Id.Factory.loadFrom("ecc7I6YJRLHY7ORDM76ABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecMBLGUQSUY7ORDM76ABIFNQAABA"), Id.Factory.loadFrom("sprEJJ3KCLIY7ORDM76ABIFNQAABA")}), Id.Factory.loadFrom("ecc7I6YJRLHY7ORDM76ABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecMBLGUQSUY7ORDM76ABIFNQAABA"), Id.Factory.loadFrom("sprWKWCNMDGY7ORDM76ABIFNQAABA")}), Id.Factory.loadFrom("ecc7I6YJRLHY7ORDM76ABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"), Id.Factory.loadFrom("spr2PZ4XTKGHPOBDHZ7ABQAQH3XQ4")}), Id.Factory.loadFrom("eccUOCBFXHUF3PBDIJEABQAQH3XQ4"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"), Id.Factory.loadFrom("sprEPD5WJXAGXOBDGAPABIFNQAABA")}), Id.Factory.loadFrom("eccWXFNROPUF3PBDIJEABQAQH3XQ4"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aec2PODHIBAQPORDMNEABIFNQAABA"), Id.Factory.loadFrom("sprJRDPRG5WQPORDMNEABIFNQAABA")}), Id.Factory.loadFrom("ecc2VNWZ6CFEDPBDBRDABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acl2CRUOPNTSLOBDCKTAALOMT5GDM"), Id.Factory.loadFrom("sprLLIIEOYSU7OBDCLIAALOMT5GDM")}), Id.Factory.loadFrom("eccSK57DNFNSLOBDCKTAALOMT5GDM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aec37AELCFJSLOBDCKTAALOMT5GDM"), Id.Factory.loadFrom("spr53BP6D5OSLOBDCKTAALOMT5GDM")}), Id.Factory.loadFrom("eccHNISMCFOSLOBDCKTAALOMT5GDM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aec37AELCFJSLOBDCKTAALOMT5GDM"), Id.Factory.loadFrom("sprJM5MLWNSSLOBDCKTAALOMT5GDM")}), Id.Factory.loadFrom("eccIWOZ55WDTDOBDCK2AALOMT5GDM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aec37AELCFJSLOBDCKTAALOMT5GDM"), Id.Factory.loadFrom("sprK2FH7DVSSLOBDCKTAALOMT5GDM")}), Id.Factory.loadFrom("eccEGCQGRNSSLOBDCKTAALOMT5GDM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aec37AELCFJSLOBDCKTAALOMT5GDM"), Id.Factory.loadFrom("sprUD46VVIAZFD73FYHKXTCQY2YCY")}), Id.Factory.loadFrom("ecc33C2YEVZZNFNJOTBYDSH7JPQGY"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecGOXBLU5ISLOBDCKTAALOMT5GDM"), Id.Factory.loadFrom("sprQEO5WWFNSLOBDCKTAALOMT5GDM")}), Id.Factory.loadFrom("eccSK57DNFNSLOBDCKTAALOMT5GDM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aclLAS5UENTSLOBDCKTAALOMT5GDM"), Id.Factory.loadFrom("sprYLNKOZYSU7OBDCLIAALOMT5GDM")}), Id.Factory.loadFrom("eccSK57DNFNSLOBDCKTAALOMT5GDM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aclE3I7F6Q3JTPBDLZ4AAN7YHKUNI"), Id.Factory.loadFrom("sprTEW7RSQDPJA45ADQWLG7YBW5PE")}), Id.Factory.loadFrom("eccSK57DNFNSLOBDCKTAALOMT5GDM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acl6AES4EVRSLOBDCKTAALOMT5GDM"), Id.Factory.loadFrom("sprO3ZOJTIRU7OBDCLIAALOMT5GDM")}), Id.Factory.loadFrom("eccSK57DNFNSLOBDCKTAALOMT5GDM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecTJPMXIAMQBHBHNPRZHPVEWLGKA"), Id.Factory.loadFrom("sprGKZVT6XYTDOBDDUPAAEPIIDLLU")}), Id.Factory.loadFrom("ecc6NTBRVBTVVHPNBZ45GHQCFOBQ4"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecR6UPQQTWAJGC7DRTJN3S4N4PTU"), Id.Factory.loadFrom("sprMGBL4BJJOPOBDFKUAAMPGXUWTQ")}), Id.Factory.loadFrom("eccNPHB5FFLTHOBDDUQAAEPIIDLLU"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecRQH6ITMRFZABBHGRIYWHQY2DBM"), Id.Factory.loadFrom("sprHM6UP5OQ6JHDVG7E6V4CTC45IY")}), Id.Factory.loadFrom("eccUSBMFNVMZVFYXJUZUHVMMLNVUM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecRQH6ITMRFZABBHGRIYWHQY2DBM"), Id.Factory.loadFrom("sprKIWKZZ2TVFBBNBE7WWUZRKTUXQ")}), Id.Factory.loadFrom("eccBEBDTO776REC7CJSTTCUZEOOW4"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecRQH6ITMRFZABBHGRIYWHQY2DBM"), Id.Factory.loadFrom("sprU36RCCFV3JBNNJWL4CY5R7I3HU")}), Id.Factory.loadFrom("eccDVFX57IZQBD4ZHMAIZG3GEMHTI"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aec6I5V6I7MVNAXLG7DVM5FFCD4CU"), Id.Factory.loadFrom("sprJXHJ3M34THOBDDUQAAEPIIDLLU")}), Id.Factory.loadFrom("eccVEHFWL4NTHOBDDUQAAEPIIDLLU"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecYEDVXUI4PLNRDB4SAALOMT5GDM"), Id.Factory.loadFrom("sprLPVNQ3FOZDNRDCKIABIFNQAABA")}), Id.Factory.loadFrom("eccCGF66QAJGXOBDONAABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aec2L3DL5NYPLNRDB4TAALOMT5GDM"), Id.Factory.loadFrom("spr6M3PRRUC4LNRDC7HAAMPGXSZKU")}), Id.Factory.loadFrom("ecc3OZUD2NYNHORDJRCABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecLA7MVSB2X7OBDCMFAALOMT5GDM"), Id.Factory.loadFrom("spr66E24KR5YDOBDCMGAALOMT5GDM")}), Id.Factory.loadFrom("eccTR2G3HNZ33OBDID6AALOMT5GDM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecLA7MVSB2X7OBDCMFAALOMT5GDM"), Id.Factory.loadFrom("sprLZLHUHP7F5F2TFK4UJCEWMEQPA")}), Id.Factory.loadFrom("eccTR2G3HNZ33OBDID6AALOMT5GDM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecLA7MVSB2X7OBDCMFAALOMT5GDM"), Id.Factory.loadFrom("sprQYR3XB36WNE6TH73SOROOJ2BOQ")}), Id.Factory.loadFrom("eccTR2G3HNZ33OBDID6AALOMT5GDM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecLA7MVSB2X7OBDCMFAALOMT5GDM"), Id.Factory.loadFrom("sprU655C77H2NDU7PAGAZ5S7XFZMQ")}), Id.Factory.loadFrom("eccGBYALY5Z33OBDID6AALOMT5GDM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecLA7MVSB2X7OBDCMFAALOMT5GDM"), Id.Factory.loadFrom("sprXNA2WXKWVFAWFGZ3TL5GCLUYK4")}), Id.Factory.loadFrom("eccYIOL3W5Z33OBDID6AALOMT5GDM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecIX4HASTFX7OBDCMFAALOMT5GDM"), Id.Factory.loadFrom("sprU3V2O73Z37OBDID7AALOMT5GDM")}), Id.Factory.loadFrom("eccXVJFC7DY37OBDID7AALOMT5GDM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecIX4HASTFX7OBDCMFAALOMT5GDM"), Id.Factory.loadFrom("sprUMQHF4DY37OBDID7AALOMT5GDM")}), Id.Factory.loadFrom("eccRKFHXB3Y37OBDID7AALOMT5GDM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecIIPUO7U277NRDA26AAMPGXSZKU"), Id.Factory.loadFrom("spr4NONVDL6QPOBDCKCAALOMT5GDM")}), Id.Factory.loadFrom("eccJNISTA36QPOBDCKCAALOMT5GDM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecIIPUO7U277NRDA26AAMPGXSZKU"), Id.Factory.loadFrom("sprNAQEQ33EGDOBDCHVAALOMT5GDM")}), Id.Factory.loadFrom("eccUQX3HNLEGDOBDCHVAALOMT5GDM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecIIPUO7U277NRDA26AAMPGXSZKU"), Id.Factory.loadFrom("sprTIRIOLODTRHBJDTTKJZJ7PNGZM")}), Id.Factory.loadFrom("eccDDOVC66RHNAYJOVXGAPO4NCJBA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecIIPUO7U277NRDA26AAMPGXSZKU"), Id.Factory.loadFrom("sprVC244YVW35AAFNLKWP3BZB3FLQ")}), Id.Factory.loadFrom("eccWQWOUJ6CIVBYZOGPOIQOQYR5AE"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecIIPUO7U277NRDA26AAMPGXSZKU"), Id.Factory.loadFrom("sprVRKZDMTHGDOBDCHVAALOMT5GDM")}), Id.Factory.loadFrom("ecc4E6Z5SH3AHOBDA26AAMPGXSZKU"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecWKV3U7T5PPNRDB4UAALOMT5GDM"), Id.Factory.loadFrom("spr3LOKO2U5ZXOBDCMVAALOMT5GDM")}), Id.Factory.loadFrom("ecc2QKRWLNJALOBDA26AAMPGXSZKU"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecWKV3U7T5PPNRDB4UAALOMT5GDM"), Id.Factory.loadFrom("spr3LTSHNU6ZXOBDCMVAALOMT5GDM")}), Id.Factory.loadFrom("ecc5BG6DO4DQPOBDCKCAALOMT5GDM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecWKV3U7T5PPNRDB4UAALOMT5GDM"), Id.Factory.loadFrom("sprAWKSE27J4NFA7E7ZSRZGDBCWCU")}), Id.Factory.loadFrom("ecc5Y2A565345GRNFOTLEWN5A4DXA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecWKV3U7T5PPNRDB4UAALOMT5GDM"), Id.Factory.loadFrom("sprFO6QZHC2ATOBDA26AAMPGXSZKU")}), Id.Factory.loadFrom("eccWOY3WENJALOBDA26AAMPGXSZKU"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecWKV3U7T5PPNRDB4UAALOMT5GDM"), Id.Factory.loadFrom("sprJQLGFGM6ZXOBDCMVAALOMT5GDM")}), Id.Factory.loadFrom("eccU4NECIVJALOBDA26AAMPGXSZKU"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecWKV3U7T5PPNRDB4UAALOMT5GDM"), Id.Factory.loadFrom("sprO7VA3LU6ZXOBDCMVAALOMT5GDM")}), Id.Factory.loadFrom("ecc2RQJ2ZFJALOBDA26AAMPGXSZKU"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecWKV3U7T5PPNRDB4UAALOMT5GDM"), Id.Factory.loadFrom("sprPJMJLPM6ZXOBDCMVAALOMT5GDM")}), Id.Factory.loadFrom("eccZCEAXQUDQPOBDCKCAALOMT5GDM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecWKV3U7T5PPNRDB4UAALOMT5GDM"), Id.Factory.loadFrom("sprPYJFI4D5IBEC7EZVTREJSUL3NE")}), Id.Factory.loadFrom("eccQB6HWBIIHBALHHYZ6RSZ2ZW3QU"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecWKV3U7T5PPNRDB4UAALOMT5GDM"), Id.Factory.loadFrom("sprTPCB6XE6ZXOBDCMVAALOMT5GDM")}), Id.Factory.loadFrom("ecc44CBUUNJALOBDA26AAMPGXSZKU"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecWKV3U7T5PPNRDB4UAALOMT5GDM"), Id.Factory.loadFrom("sprUFUOMR46ZXOBDCMVAALOMT5GDM")}), Id.Factory.loadFrom("eccLK5C2C7PG5EGTH5H56SF6DTUSM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecWKV3U7T5PPNRDB4UAALOMT5GDM"), Id.Factory.loadFrom("sprVVIHF4C2ATOBDA26AAMPGXSZKU")}), Id.Factory.loadFrom("ecc7FIEORFJALOBDA26AAMPGXSZKU"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecFUUVPLPBOXPBDBTGABIFNQAABA"), Id.Factory.loadFrom("sprQPBAMXGIQHPBDAXSABIFNQAABA")}), Id.Factory.loadFrom("eccF5E27ZN2QDPBDLJRABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecFUUVPLPBOXPBDBTGABIFNQAABA"), Id.Factory.loadFrom("sprQRAMTEDAO7PBDBTIABIFNQAABA")}), Id.Factory.loadFrom("eccF5E27ZN2QDPBDLJRABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecKQ2RWPD6ZDNRDOLPABIFNQAABA"), Id.Factory.loadFrom("sprPGGFOLTM7HNRDOMLABIFNQAABA")}), Id.Factory.loadFrom("eccKSGFFVPJAHOBDA26AAMPGXSZKU"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("acl35YAFSZMU7PBDEO7ABIFNQAABA"), Id.Factory.loadFrom("sprAPFF7HBRU7PBDEO7ABIFNQAABA")}), Id.Factory.loadFrom("eccI45WGTJRU7PBDEO7ABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aclNDARHVJMU7PBDEO7ABIFNQAABA"), Id.Factory.loadFrom("sprN4YZAKZQU7PBDEO7ABIFNQAABA")}), Id.Factory.loadFrom("eccDTTWSDJRU7PBDEO7ABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aclG7I7N2JMU7PBDEO7ABIFNQAABA"), Id.Factory.loadFrom("sprQ7B6WLBSU7PBDEO7ABIFNQAABA")}), Id.Factory.loadFrom("eccSHUSUWRRU7PBDEO7ABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aclWFH5ZHJNU7PBDEO7ABIFNQAABA"), Id.Factory.loadFrom("spr7ID3P3BRU7PBDEO7ABIFNQAABA")}), Id.Factory.loadFrom("eccP47BEZRRU7PBDEO7ABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecKG37PKEBQPNRDB46AALOMT5GDM"), Id.Factory.loadFrom("sprDME2BV22WDNRDCJPABIFNQAABA")}), Id.Factory.loadFrom("eccIKIFLBCZGTOBDOM7ABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecKG37PKEBQPNRDB46AALOMT5GDM"), Id.Factory.loadFrom("sprYVCPTJB37HNRDOMLABIFNQAABA")}), Id.Factory.loadFrom("eccIKIFLBCZGTOBDOM7ABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecXL62J65UQDPBDGDFAAIWOWQC6E"), Id.Factory.loadFrom("spr4NCVPCLMQLPBDOCKAAN7YHKUNI")}), Id.Factory.loadFrom("eccVYBZJERUQ3PBDGDLABQARQGZVM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecNKNDMQI75HOBDCSUCEIRCEIRCE"), Id.Factory.loadFrom("sprDDNRGOT5ONHA7FHBAYN4CB4TXQ")}), Id.Factory.loadFrom("eccMMB6VOE4YLORDBBJABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecNKNDMQI75HOBDCSUCEIRCEIRCE"), Id.Factory.loadFrom("sprUPRK3GIL77OBDPDXAAN7YHKUNI")}), Id.Factory.loadFrom("eccMMB6VOE4YLORDBBJABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecTHH2BU632LNRDCKSABIFNQAABA"), Id.Factory.loadFrom("spr4QE5XGJ5ALPBDFKUAAN7YHKUNI")}), Id.Factory.loadFrom("eccU6XOH34H73ORDK36AAN7YHKUNI"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecTHH2BU632LNRDCKSABIFNQAABA"), Id.Factory.loadFrom("spr7THGKMWGJFHUJPEC7DZXLPY5MY")}), Id.Factory.loadFrom("eccBK7U5GBC77OBDPDXAAN7YHKUNI"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecTHH2BU632LNRDCKSABIFNQAABA"), Id.Factory.loadFrom("sprESU2YFVMY5DN7BSABMSPMZ37BY")}), Id.Factory.loadFrom("eccKC5SSFKHTBAQXPDJAUIHS5QQHM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecTHH2BU632LNRDCKSABIFNQAABA"), Id.Factory.loadFrom("sprLMILT6SCALPBDFKUAAN7YHKUNI")}), Id.Factory.loadFrom("eccCHXSTJKCALPBDFKUAAN7YHKUNI"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecTHH2BU632LNRDCKSABIFNQAABA"), Id.Factory.loadFrom("sprS5NG3HB5YXORDBBJABIFNQAABA")}), Id.Factory.loadFrom("eccLNGA47J5YXORDBBJABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecTHH2BU632LNRDCKSABIFNQAABA"), Id.Factory.loadFrom("sprUF5QRXCT2LORDKBMABIFNQAABA")}), Id.Factory.loadFrom("eccTHZANPJC77OBDPDXAAN7YHKUNI"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecYDGQACJVVLOBDCLSAALOMT5GDM"), Id.Factory.loadFrom("sprZ2IX76OO3PORDOFEABIFNQAABA")}), Id.Factory.loadFrom("eccUACC2BWP3PORDOFEABIFNQAABA"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"), Id.Factory.loadFrom("sprNGT5DE5G27NRDISQAAAAAAAAAA")}), Id.Factory.loadFrom("eccKGGFFVPJAHOBDA26AAMPGXSZKU"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"), Id.Factory.loadFrom("spr6AFPZS6TRHNRDB5MAALOMT5GDM")}), Id.Factory.loadFrom("eccKOGFFVPJAHOBDA26AAMPGXSZKU"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"), Id.Factory.loadFrom("sprM474UPJLXTNRDOLKABIFNQAABA")}), Id.Factory.loadFrom("eccKOGFFVPJAHOBDA26AAMPGXSZKU"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"), Id.Factory.loadFrom("sprTLCJTB3SJTOBDCIVAALOMT5GDM")}), Id.Factory.loadFrom("eccVXWRKQLYJTOBDCIVAALOMT5GDM"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecHC6VVBZ4I3OBDCIOAALOMT5GDM"), Id.Factory.loadFrom("spr2TX2LX7KG5DUFLHC6P655RM7PY")}), Id.Factory.loadFrom("ecc7OXRUQUMQZE77FGHGE5WGENMKI"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecHC6VVBZ4I3OBDCIOAALOMT5GDM"), Id.Factory.loadFrom("spr7ZWN26EIUBBJVBUF7GV2AGP7HM")}), Id.Factory.loadFrom("ecc7OXRUQUMQZE77FGHGE5WGENMKI"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecHC6VVBZ4I3OBDCIOAALOMT5GDM"), Id.Factory.loadFrom("sprBV5GRV22I3OBDCIOAALOMT5GDM")}), Id.Factory.loadFrom("ecc7OXRUQUMQZE77FGHGE5WGENMKI"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecCRD53OZ5I3OBDCIOAALOMT5GDM"), Id.Factory.loadFrom("spr3SF2T5XN55HBRFQBS4QDIEFCOA")}), Id.Factory.loadFrom("eccX6JDRFJHQNHXVMN4QMV6A3RDUU"));

        map.put(new AdsPath(new Id[]{Id.Factory.loadFrom("aecCRD53OZ5I3OBDCIOAALOMT5GDM"), Id.Factory.loadFrom("sprMUR6BU54BBHR3PHJJFFBZXB634")}), Id.Factory.loadFrom("eccTNVRW53A3FBSDHD6UFIKCAJPNA"));
    }

    public void update(RadixObject obj, StringBuilder b) {
        if (obj instanceof AdsSelectorPresentationDef) {
            AdsSelectorPresentationDef def = (AdsSelectorPresentationDef) obj;
            AdsPath path = new AdsPath(def.getIdPath());
            Id id = map.get(path);
            if (id != null) {
                def.setCreationClassCatalogId(id);
                b.append("Updated: " + def.getQualifiedName());
                b.append("\n");
            }
        }
    }
}
