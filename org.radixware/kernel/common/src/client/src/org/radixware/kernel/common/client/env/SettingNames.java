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

public interface SettingNames {

    public static final String SYSTEM = "org.radixware.explorer";
    public static final String EXPLORER_TREE_GROUP = "X_T";
    public static final String EDITOR_GROUP = "X_E";
    public static final String SELECTOR_GROUP = "X_S";
    public static final String SOURCE_EDITOR = "S_E";
    public static final String VIEW_MANAGER_GROUP = "X_V";
    public static final String APP_STYLE = "app_style";
    public static final String STYLENAME = "stylename";
    public static final String MEM_LEAK_DETECTOR = "MemLeakDetector";
    public static final String FORMAT_SETTINGS = "format_settings";
    
    public interface Properties {
        public static final String READONLY_PROPERTY = "RDNL_PRP";
        public static final String MANDATORY_PROPERTY = "MNDT_PRP";
        public static final String OTHER_PROPERTY = "OTHR_PRP";
        public static final String OVERRIDED = "OVERRIDED";
        public static final String INHERITED = "INHERITED";
        public static final String UNDEFINED = "UNDEFINED";
        public static final String INVALID = "INVALID";
        public static final String BROKEN = "BROKEN";        
    }
    
    public interface TextOptions {
        public final static String FONT = "FNT";
        public final static String ALIGNMENT = "ALGN";
        public final static String BCOLOR = "BGRN";
        public final static String FCOLOR = "FGRN";        
    }

    public interface ExplorerTree {

        public static final String COMMON_GROUP = "C_S";
        public static final String PARAGRAPH_GROUP = "P_S";
        public static final String SELECTOR_GROUP = "S_S";
        public static final String EDITOR_GROUP = "E_S";
        public static final String USER_GROUP = "U_S";
        public static final String SPLITTER_POSITION = "SPLITTER_POS";        

        public interface Common {

            public static final String TREE_AREA = "TA";
            public static final String STATE = "STATE";
            public static final String ROOT = "ROOT_ID";
            public static final String RESTORE_POSITION = "SAVE_POS";
            public static final String ICON_SIZE = "IC_SZ";
            public static final String TREE_BACKGROUND = "TREE_BGRN";
            public static final String TREE_SELECTED_ITEM_FONT_COLOR = "TREE_S_I_F_C";
            public static final String TREE_SELECTED_ITEM_BACKGROUND = "TREE_S_I_B";
            public static final String KEEP_USER_EI = "KEEP_USER_EI";
            
            public static final String FONT = "FNT";
            public static final String BACKGROUND_COLOR = "B_C";
            public static final String FOREGROUND_COLOR = "F_C";
            public static final String SHOW_ICONS = "SH_IC";
        }

        public interface Editor {
            public static final String EDIT_AFTER_INSERT = "ED_AFTER_INS";
        }
    }

    public interface Editor {

        public static final String COMMON_GROUP = "C_E_G";
        public static final String PROPERTY_TITLES_GROUP = "P_T_G";
        public static final String PROPERTY_VALUES_GROUP = "P_V_G";

        public interface Common {

            public static final String ICON_SIZE_IN_TOOLBARS = "IC_SZ_TLBRS";
            public static final String ICON_SIZE_IN_TABS = "IC_SZ_TBS";
            public static final String FONT_IN_TABS = "FNT_TBS";
            public static final String RESTORE_TAB = "SAVE_TAB";
            public static final String TITLES_ALIGNMENT = "TTLS_ALGNM";
            public static final String BODY_ALIGNMENT = "PRPRTS_ALGNM";
            public static final String CHECK_MANDATORY_ON_CLOSE = "CHECK_MANDATORY_ON_CLOSE";
            public static final String DROP_DOWN_LIST_ITEMS_LIMIT = "DROP_DOWN_LIST_ITEMS_LIMIT";//максимальное количество элементов в выпадающем списке
        }
    }

    public interface Selector {

        public static final String COMMON_GROUP = "C_S_G";
        public static final String STYLES_GROUP = "S_S_G";
        public static final String COLUMNS_GROUP = "columns";
        public static final String SELECTION_STATISTIC_EXPORT_DIR = "statistic_export_dir";

        public interface Common {

            public static final String ICON_SIZE_IN_SELECTOR_TOOLBARS = "IC_SZ_SLCTR_TLBRS";
            public static final String HEADER_FONT_IN_SELECTOR = "HDR_SLCTR_FNT";
            public static final String TITLES_ALIGNMENT = "SLCTR_TTLS_ALGN";
            public static final String BODY_ALIGNMENT = "SLCTR_BDY_ALGN";
            public static final String FRAME_COLOR = "SLCTR_FRM_COLOR";//цвет рамки фокуса текущей ячейки
            public static final String ROW_FRAME_COLOR = "SLCTR_ROW_FRM_COLOR";//цвет рамки фокуса текущей строки
            public static final String MULTIPLE_SELECTION_MODE_ENABLED_BY_DEFAULT = "MULTIPLE_SELECTION_ENABLED";//режим множественного выбора включен при открытии
            public static final String SELECTED_ROW_COLOR = "SELECTED_ROW_COLOR";//цвет выбранной строки
            public static final String SLIDER_VALUE = "SLDR_VL";            
            public static final String SAVE_FILTER = "SAVE_FILTER";
            public static final String ROWS_LIMIT_FOR_SEARCH = "ROWS_LIMIT_FOR_SEARCH";//Максимальное количество строк, которое можно загрузить при выполнении операции поиска
            public static final String ROWS_LIMIT_FOR_RESTORING_POSITION = "ROWS_LIMIT_FOR_RESTORING_POSITION";//Максимальное количество строк, которое можно загрузить при выполнении операции позиционирования
            public static final String ROWS_LIMIT_FOR_NAVIGATION = "ROWS_LIMIT_FOR_NAVIGATION";//Максимальное количество строк, которое можно загрузить при скроллировании или переходе на последний объект
        }
    }

    public interface SourceCode {

        public static final String SYNTAX_GROUP = "SYNTAX_G";
        public static final String SYNTAX_TAB = "SYNTAX_TAB";
        public static final String SYNTAX_JML = "SYNTAX_JML";
        public static final String SYNTAX_SQML = "SYNTAX_SQML";
        //public static final String STYLES_GROUP = "S_S_G";
        //public static final String SOURCE_CODE_SETTINGS="columns";

        public interface Syntax {

            public static final String DEFAULT = "DEFAULT";
            public static final String RESERVED_WORDS = "RESERVED_WORDS";
            public static final String COMMENTS = "COMMENTS";
            public static final String STRINGS = "STRINGS";
            public static final String NUMBERS = "NUMBERS";
            public static final String SYMBOLS = "SYMBOLS";
            public static final String SEPARATORS = "SEPARATORS";
            public static final String JML_TAG_ID = "JML_TAG_ID";
            public static final String JML_TAG_INVOCATE = "JML_TAG_INVOCATE";
            public static final String JML_TAG_TYPE = "JML_TAG_TYPE";
            public static final String JML_TAG_DB_ENTITY = "JML_TAG_DB_ENTITY";
            public static final String JML_DB_NAME = "JML_DB_NAME";
            public static final String JML_TAG_LOCALIZED_STR = "JML_TAG_LOCALIZED_STR";

            public static final String SQML_CONSTANT_VALUE = "SQML_CONSTANT_VALUE";
            public static final String SQML_PARENT_CONDITION = "SQML_PARENT_CONDITION";
            //public static final String SQML_PARENT_PROP_REF_SQL_NAME = "SQML_PARENT_PROP_REF_SQL_NAME";
            public static final String SQML_PROP_SQL_NAME = "SQML_PROP_SQL_NAME";
            public static final String SQML_TABLE_SQL_NAME = "SQML_TABLE_SQL_NAME";
            public static final String SQML_THIS_TABLE_SQL_NAME = "SQML_THIS_TABLE_SQL_NAME";
            //public static final String SQML_THIS_TABLE_SQL_ID = "SQML_THIS_TABLE_SQL_ID";
            public static final String SQML_TYPIFIED_VALUE = "SQML_TYPIFIED_VALUE";
            public static final String SQML_PARAMETER = "SQML_PARAMETER";
            public static final String SQML_ENTITY_REF_PARAMETER = "SQML_ENTITY_REF_PARAMETER";
            public static final String SQML_UNKNOWN_TAG = "SQML_UNKNOWN_TAG";
            public static final String SQML_DB_FUNC_CALL = "SQML_DB_FUNC_CALL";
            public static final String SQML_EVENT_CODE = "SQML_EVENT_CODE";
            public static final String SQML_ID_PATH = "SQML_ID_PATH";
            public static final String SQML_DB_NAME = "SQML_DB_NAME";
            public static final String SQML_PREPROCESSOR = "SQML_PREPROCESSOR";
        }
    }
    
    public interface FormatSettings{
        
        public static final String NUMBER = "NUMBER";
        public static final String DATE = "DATE";
        public static final String TIME = "TIME";
        
        public interface Number {
            public static final String GROUP_SEPARATOR  = "GROUP_SEP";
            public static final String DECIMAL_PART_SEPARATOR = "DECIMAL_SEP";
        }        
    }
}
