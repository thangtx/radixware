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

package org.radixware.kernel.common.enums;

import java.io.Serializable;
import java.util.List;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;

public enum EDefinitionIdPrefix implements IKernelStrEnum, Serializable {
    /*
     * ADS class prefixes
     */

    ADS_ALGORITHM_CLASS("aac"),
    ADS_APPLICATION_CLASS("acl"),
    ADS_DYNAMIC_CLASS("adc"),
    ADS_ENTITY_CLASS("aec"),
    ADS_EXCEPTION_CLASS("axc"),
    ADS_INTERFACE_CLASS("aic"),
    ADS_FORM_HANDLER_CLASS("afh"),
    ADS_PRESENTATION_INTERFACE_CLASS("pic"),
    ADS_PRESENTATION_EXCEPTION_CLASS("pxc"),
    ADS_PREDEFINED_CLASS("pdc"),
    ADS_PRESENTATION_CLASS("apc"),
    ADS_SQL_CURSOR_CLASS("acc"),
    ADS_SQL_PROCEDURE_CLASS("spc"),
    ADS_SQL_STATEMENT_CLASS("ssc"),
    ADS_DIALOG_MODEL_CLASS("adm"),
    ADS_PROP_EDITOR_MODEL_CLASS("arm"),
    ADS_PRESENTATION_ENTITY_ADAPTER_CLASS("apa"),
    //model class prefixes
    //---------------entity model--------------
    ADS_ENTITY_MODEL_CLASS("aem"),
    //---------------group model--------------
    ADS_GROUP_MODEL_CLASS("agm"),
    //---------------filter model--------------
    ADS_FILTER_MODEL_CLASS("fmc"),
    //---------------paragraph model--------------    
    ADS_PARAGRAPH_MODEL_CLASS("apm"),
    //---------------from model--------------        
    ADS_FORM_MODEL_CLASS("afm"),
    //---------------report model--------------            
    ADS_REPORT_MODEL_CLASS("rpm"),
    //---------------command model--------------            
    ADS_COMMAND_MODEL_CLASS("cmc"),
    //end of model class prefixes
    ADS_ENTITY_GROUP_CLASS("agc"),
    /**
     * Ads Definitions
     */
    ADS_ENUMERATION("acs"),
    ADS_METHOD_PARAMETER("mpr"),
    ADS_PHRASE_BOOK("phr"),
    /*
     * class components
     */
    ADS_METHOD_GROUP("cmg"),
    ADS_PROPERTY_GROUP("cpg"),
    ADS_LOCALIZED_STRING("mls"),
    ADS_LOCALIZING_BUNDLE("mlb"),
    ADS_CLASS_METHOD("mth"),
    /**
     * ADS_ENUM_CLASS prefix
     */
    ADS_ENUM_CLASS("aet"),
    ADS_ENUM_CLASS_FIELD("aef"),
    ADS_ENUM_CLASS_PARAM("aep"),
    /**
     * ADS_PROPERTIES
     */
    ADS_CURSOR_FIELD_PROP("prf"),
    ADS_PRESENTATION_PROP("prp"),
    ADS_DYNAMIC_PROP("prd"),
    ADS_FORM_PROP("pfm"),
    ADS_GROUP_PROP("pgp"),
    ADS_USER_PROP("pru"),
    /*
     * ADS PRESENTATIONS
     */
    ADS_CREATION_CLASS_CATALOG("ecc"),
    ADS_CREATION_CLASS_CATALOG_TOPIC("cct"),
    /*
     * DDS definition prefixes
     */
    DDS_ACCESS_PARTITION_FAMILY("apf"),
    DDS_FUNC_PARAM("dfp"),
    DDS_FUNCTION("dfn"),
    DDS_COLUMN("col"),
    DDS_COLUMN_TEMPLATE("cdm"),
    DDS_PACKAGE("pkg"),
    DDS_PROTOTYPE("dpr"),
    DDS_CUSTOM_TEXT("dtx"),
    DDS_TYPE("dbt"),
    DDS_TYPE_FIELD("dtf"),
    DDS_ENUM("ecs"),
    DDS_EXT_TABLE("tbe"),
    DDS_INDEX("idx"),
    DDS_UNIQUE_CONSTRAINT("unq"),
    DDS_CHECK_CONSTRAINT("chk"),
    DDS_LABEL("lbl"),
    DDS_MODEL("prj"),
    DDS_REFERENCE("ref"),
    DDS_SEQUENCE("sqn"),
    DDS_TABLE("tbl"),
    DDS_TRIGGER("trg"),
    /*
     * TODO:
     */
    AUDIT_SCHEME("aud"),
    COLOR_SCHEME("csh"),
    COMMAND("cmd"),
    CONST_ITEM("aci"),
    //DBP_CONST_SET_TEMPLATE("tcs"),
    CONTEXTLESS_COMMAND("clc"),
    CUSTOM_DIALOG("cdl"),
    CUSTOM_EDITOR("cee"),
    CUSTOM_EDITOR_PAGE("cep"),
    CUSTOM_PARAG_EDITOR("cce"),
    CUSTOM_PROP_EDITOR("cpe"),
    CUSTOM_FORM_DIALOG("cfd"),
    CUSTOM_FILTER_DIALOG("cfe"),
    CUSTOM_REPORT_DIALOG("crd"),
    CUSTOM_SELECTOR("ces"),
    DOMAIN("dmn"),
    EDITOR_PAGE("epg"),
    EDITOR_PRESENTATION("epr"),
    ENTITY_TITLE_FORMAT("etf"),
    EXPLORER_ITEM("xpi"),
    //---------------user explorer item--------------
    USER_EXPLORER_ITEM("xpu"),
    EXPLORER_CONNECTION_OPTIONS("eco"),
    FILTER("flt"),
    PREDEFINED_FILTER_PARAMETER("pfp"),
    USER_FILTER_PARAMETER("ufp"),
    IMAGE("img"),
    MODULE("mdl"),
    MSDL_SCHEME("smd"),
    XSLT_TRANSFORM("xsl"),
    NOTIFICATION_ITEM("nti"),
    //NATIVE_PROPERTY("col"),
    PARAGRAPH("par"),
    PARAGRAPH_LINK("prl"),
    PARAMETER("prm"),
    ROLE("rol"),
    DATA_SEGMENT("sgm"),
    APPLICATION_ROLE("apl"),
    SAVEPOINT("spt"),
    SELECTOR_PRESENTATION("spr"),
    SERVER_RESOURCE("csr"),
    SORTING("srt"),
    USER_FUNC_CLASS("usf"),
    LIB_USERFUNC_PREFIX("luf"),
    USER_PROP_CLASSIFIER("upc"),
    USER_PROP_INHERIT_SCHEMA("ups"),
    WIDGET("wdg"),
    CUSTOM_WIDGET("wdc"),
    SIGNAL("sig"),
    XML_SCHEME("xsd"),
    /*
     * Workflow
     */
    ALGO_PAGE("pge"),
    ALGO_NODE("nde"),
    ALGO_EDGE("ege"),
    ALGO_PIN("pin"),
    /*
     * parameters
     */
    ALGO_PARAMETER("apr"),
    ALGO_BLOCK_PARAMETER("abp"),
    ALGO_GLOBAL_VAR("glb"),
    /*
     * Report
     */
    REPORT("rpt"),
    REPORT_LINK("rpl"),
    REPORTS_GROUP("rpg"),
    USER_DEFINED_REPORT("rpu");
    private static final long serialVersionUID = 1L;
    private String val;
    private EDefinitionIdPrefix[] envLinks = null;

    private EDefinitionIdPrefix(String val) {
        this.val = val;
    }

    @Override
    public String getValue() {
        return val;
    }

    @Override
    public String getName() {
        return null;
    }

    public boolean isEnvMutable() {
        return envLinks != null;
    }

    public EDefinitionIdPrefix getForEnv(ERuntimeEnvironmentType env) {
        if (envLinks == null || env == null) {
            return this;
        } else {
            switch (env) {
                case COMMON_CLIENT:
                    return envLinks[0];
                case EXPLORER:
                    return envLinks[1];
                case WEB:
                    return envLinks[2];
                default:
                    return this;
            }
        }
    }

    public static EDefinitionIdPrefix getForValue(final String val) {
        for (EDefinitionIdPrefix e : EDefinitionIdPrefix.values()) {
            if (e.getValue().equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EDefinitionIdPrefix has no item with value: " + String.valueOf(val), val);
    }

//    public static boolean mayBeDefinitionId(String id) {
//        return id.startsWith(ADS_APPLICATION_CLASS.getValue()) ||
//                id.startsWith(ADS_ALGORITHM_CLASS.getValue()) ||
//                id.startsWith(APP_CONST_SET.getValue()) ||
//                id.startsWith(TABLE.getValue()) ||
//                id.startsWith(ADS_ENTITY_CLASS.getValue()) ||
//                id.startsWith(ADS_ENTITY_GROUP_CLASS.getValue()) ||
//                id.startsWith(ADS_SQL_CURSOR_CLASS.getValue()) ||
//                id.startsWith(ADS_SQL_PROCEDURE_CLASS.getValue()) ||
//                id.startsWith(ADS_SQL_STATEMENT_CLASS.getValue()) ||
//                id.startsWith(ADS_DYNAMIC_CLASS.getValue()) ||
//                id.startsWith(ADS_INTERFACE_CLASS.getValue()) ||
//                id.startsWith(PARAGRAPH.getValue()) ||
//                id.startsWith(CONTEXTLESS_COMMAND.getValue()) ||
//                id.startsWith(ADS_EXCEPTION_CLASS.getValue());
//
//
//    }
//
//    public static boolean mayBePropertyId(String id) {
//        return id.startsWith(DYNAMIC_PROPERTY.getValue()) ||
//                id.startsWith(USER_PROPERTY.getValue()) ||
//                id.startsWith(CURSOR_FIELD.getValue()) ||
//                id.startsWith(NATIVE_PROPERTY.getValue());
//    }
    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }

    protected Object readResolve() {
        return getForValue(val);
    }
}
