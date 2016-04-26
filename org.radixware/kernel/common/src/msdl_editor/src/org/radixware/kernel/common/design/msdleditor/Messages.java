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

package org.radixware.kernel.common.design.msdleditor;

import java.util.ResourceBundle;


public class Messages {

    static {
        final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.common.design.msdleditor.Bundle");
        ALIGN_LEFT=bundle.getString("ALIGN_LEFT");
        ALIGN_RIGHT=bundle.getString("ALIGN_RIGHT");
        ALIGN_NONE=bundle.getString("ALIGN_NONE");
        DATE_TIME_FORMAT_STR=bundle.getString("DATE_TIME_FORMAT_STR");
        DATE_TIME_FORMAT_JAVA=bundle.getString("DATE_TIME_FORMAT_JAVA");
        DATE_TIME_FORMAT_FLORA=bundle.getString("DATE_TIME_FORMAT_FLORA");
        ENCODING_HEX=bundle.getString("ENCODING_HEX");
        ENCODING_HEX_EBCDIC=bundle.getString("ENCODING_HEX_EBCDIC");
        ENCODING_DECIMAL=bundle.getString("ENCODING_DECIMAL");
        ENCODING_BIN=bundle.getString("ENCODING_BIN");
        ENCODING_BCD=bundle.getString("ENCODING_BCD");
        ENCODING_ASCII=bundle.getString("ENCODING_ASCII");
        ENCODING_EBCDIC=bundle.getString("ENCODING_EBCDIC");
        ENCODING_UTF8=bundle.getString("ENCODING_UTF8");
        ENCODING_CP866=bundle.getString("ENCODING_CP866");
        ENCODING_CP1251=bundle.getString("ENCODING_CP1251");
        ENCODING_CP1252=bundle.getString("ENCODING_CP1252");
        ENCODING_BIGENDIANBIN=bundle.getString("ENCODING_BIGENDIANBIN");
        ENCODING_LITTLEENDIANBIN=bundle.getString("ENCODING_LITTLEENDIANBIN");
        LENUNITDEF_BYTE=bundle.getString("LENUNITDEF_BYTE");
        LENUNITDEF_CHAR=bundle.getString("LENUNITDEF_CHAR");
        LENUNITDEF_ELEMENT=bundle.getString("LENUNITDEF_ELEMENT");
        SPACE=bundle.getString("SPACE");
        FIELDTYPE_BCH=bundle.getString("FIELDTYPE_BCH");
        FIELDTYPE_BIN=bundle.getString("FIELDTYPE_BIN");
        FIELDTYPE_CHOICE=bundle.getString("FIELDTYPE_CHOICE");
        FIELDTYPE_DATETIME=bundle.getString("FIELDTYPE_DATETIME");
        FIELDTYPE_INT=bundle.getString("FIELDTYPE_INT");
        FIELDTYPE_NUM=bundle.getString("FIELDTYPE_NUM");
        FIELDTYPE_SEQUENCE=bundle.getString("FIELDTYPE_SEQUENCE");
        FIELDTYPE_STR=bundle.getString("FIELDTYPE_STR");
        FIELDTYPE_STRUCTURE=bundle.getString("FIELDTYPE_STRUCTURE");
        SHIELDFORMAT_ASIS=bundle.getString("SHIELDFORMAT_ASIS");
        SHIELDFORMAT_HEX=bundle.getString("SHIELDFORMAT_HEX");
        FIELDNAMINGTYPE_PIECE=bundle.getString("FIELDNAMINGTYPE_PIECE");
        FIELDNAMINGTYPE_BERTLV=bundle.getString("FIELDNAMINGTYPE_BERTLV");
        FIELDNAMINGTYPE_NONE=bundle.getString("FIELDNAMINGTYPE_NONE");
        NOT_DEFINED=bundle.getString("NOT_DEFINED");
        PIECE_FIXEDLEN=bundle.getString("PIECE_FIXEDLEN");
        PIECE_NONE=bundle.getString("PIECE_NONE");
        PIECE_SEPARATED=bundle.getString("PIECE_SEPARATED");
        PIECE_EMBEDDEDLEN=bundle.getString("PIECE_EMBEDDEDLEN");
        PIECE_BERTLV=bundle.getString("PIECE_BERTLV");
        FIELD_NAME=bundle.getString("FIELD_NAME");
        CONFIRMATION=bundle.getString("CONFIRMATION");
        DEL_FIELD=bundle.getString("DEL_FIELD");
        STR_FIELD=bundle.getString("STR_FIELD");
        ROOT_HEADER=bundle.getString("ROOT_HEADER");
        NAMESPACE=bundle.getString("NAMESPACE");
        DEFINITION_TITLE=bundle.getString("DEFINITION_TITLE");
        NULL_INDICATOR=bundle.getString("NULL_INDICATOR");
        COMMENT = bundle.getString("COMMENT");
        ENCODING = bundle.getString("ENCODING");
        NULLABLE = bundle.getString("NULLABLE");
        REQUIRED = bundle.getString("REQUIRED");
        PIECE_TYPE = bundle.getString("PIECE_TYPE");
        ALIGN = bundle.getString("ALIGN");
        PAD_CHAR = bundle.getString("PAD_CHAR");
        INCLUSIVE = bundle.getString("INCLUSIVE");
        NOT_INCLUSIVE = bundle.getString("NOT_INCLUSIVE");
        FIELD_DEFAULTS = bundle.getString("FIELD_DEFAULTS");
        FORMATTING_DEFAULTS = bundle.getString("FORMATTING_DEFAULTS");
        LENGTH = bundle.getString("LENGTH");
        SHIELDED = bundle.getString("SHIELDED");
        FORMATTING = bundle.getString("FORMATTING");
        STRUCTURE_FORMAT = bundle.getString("STRUCTURE_FORMAT");
        NONE = bundle.getString("NONE");
        PIECE = bundle.getString("PIECE");
        FIELD_NAMING = bundle.getString("FIELD_NAMING");
        TYPE = bundle.getString("TYPE");
        NAME = bundle.getString("NAME");
        SELECTOR = bundle.getString("SELECTOR");
        SETTINGS = bundle.getString("SETTINGS");
        DECIMAL = bundle.getString("ENCODING_DECIMAL");
    }

    public static final String ALIGN_LEFT;
    public static final String ALIGN_RIGHT;
    public static final String ALIGN_NONE;
    public static final String DATE_TIME_FORMAT_STR;
    public static final String DATE_TIME_FORMAT_JAVA;
    public static final String DATE_TIME_FORMAT_FLORA;
    public static final String ENCODING_HEX;
    public static final String ENCODING_HEX_EBCDIC;
    public static final String ENCODING_BIN;
    public static final String ENCODING_BCD;
    public static final String ENCODING_ASCII;
    public static final String ENCODING_EBCDIC;
    public static final String ENCODING_UTF8;
    public static final String ENCODING_DECIMAL;
    public static final String ENCODING_CP866;
    public static final String ENCODING_CP1251;
    public static final String ENCODING_CP1252;
    public static final String ENCODING_BIGENDIANBIN;
    public static final String ENCODING_LITTLEENDIANBIN;
    public static final String LENUNITDEF_BYTE;
    public static final String LENUNITDEF_CHAR;
    public static final String LENUNITDEF_ELEMENT;
    public static final String SPACE;
    public static final String FIELDTYPE_BCH;
    public static final String FIELDTYPE_BIN;
    public static final String FIELDTYPE_CHOICE;
    public static final String FIELDTYPE_DATETIME;
    public static final String FIELDTYPE_INT;
    public static final String FIELDTYPE_NUM;
    public static final String FIELDTYPE_SEQUENCE;
    public static final String FIELDTYPE_STR;
    public static final String FIELDTYPE_STRUCTURE;
    public static final String SHIELDFORMAT_ASIS;
    public static final String SHIELDFORMAT_HEX;
    public static final String FIELDNAMINGTYPE_PIECE;
    public static final String FIELDNAMINGTYPE_BERTLV;
    public static final String FIELDNAMINGTYPE_NONE;
    public static final String NOT_DEFINED;
    public static final String PIECE_FIXEDLEN;
    public static final String PIECE_NONE;
    public static final String PIECE_SEPARATED;
    public static final String PIECE_EMBEDDEDLEN;
    public static final String PIECE_BERTLV;
    public static final String FIELD_NAME;    
    public static final String CONFIRMATION;    
    public static final String DEL_FIELD;    
    public static final String STR_FIELD;    
    public static final String ROOT_HEADER;    
    public static final String NAMESPACE;    
    public static final String DEFINITION_TITLE;
    public static final String NULL_INDICATOR;    
    public static final String COMMENT;    
    public static final String ENCODING;    
    public static final String NULLABLE;    
    public static final String REQUIRED;    
    public static final String PIECE_TYPE;    
    public static final String ALIGN;    
    public static final String PAD_CHAR;    
    public static final String INCLUSIVE;    
    public static final String NOT_INCLUSIVE;    
    public static final String FIELD_DEFAULTS;    
    public static final String FORMATTING_DEFAULTS;    
    public static final String LENGTH;
    public static final String SHIELDED;
    public static final String FORMATTING;
    public static final String STRUCTURE_FORMAT;
    public static final String NONE;
    public static final String PIECE;
    public static final String FIELD_NAMING;
    public static final String TYPE;
    public static final String NAME;
    public static final String SELECTOR;
    public static final String SETTINGS;
    public static final String DECIMAL;
}
