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

import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.traceprofile.TraceProfileTreeController;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.enums.EEventSeverity;


public class ClientIcon {

    public static final ClientIcon EXPLORER = new ClientIcon("classpath:images/explorer.svg", true);

    public final static class Editor extends CommonOperations {

        private Editor(final String fileName) {
            super(fileName, true);
        }
        final public static ClientIcon NEED_FOR_SAVE = new ClientIcon("classpath:images/apply_changes.svg");
        public static final ClientIcon AUDIT = new ClientIcon("classpath:images/audit.svg");
    }

    public static final class Selector extends ClientIcon {

        private Selector(final String fileName) {
            super(fileName, true);
        }
        public static final ClientIcon INSERT_INTO_TREE = new ClientIcon("classpath:images/insert_into_tree.svg");
        public static final ClientIcon AUTOINSERT_INTO_TREE = new ClientIcon("classpath:images/autoinsert_into_tree.svg");
        public static final ClientIcon INSERT_INTO_TREE_AND_EDIT = new ClientIcon("classpath:images/insert_into_tree_and_edit.svg");
        public static final ClientIcon REPLACE_IN_TREE = new ClientIcon("classpath:images/replace_in_tree.svg");
        public static final ClientIcon COPY_ALL = new ClientIcon("classpath:images/copy_all.svg");
        public static final ClientIcon CLONE = new ClientIcon("classpath:images/clone.svg");
        public static final ClientIcon OPEN_IN_EDITOR = new ClientIcon("classpath:images/editor.svg");
        public static final ClientIcon FILTER_AND_SORTING = new ClientIcon("classpath:images/filter_and_sorting.svg");
        public static final ClientIcon AUDIT = new ClientIcon("classpath:images/audit.svg");
        public static final ClientIcon EXPORT = new ClientIcon("classpath:images/export_s.svg");
        public static final ClientIcon EXPORTCSV = new ClientIcon("classpath:images/csv.svg");
        public static final ClientIcon EXPORTXLSX = new ClientIcon("classpath:images/xlsx.svg");
        public static final ClientIcon CALC_STATISTIC = new ClientIcon("classpath:images/calc_statistic.svg");
    }

    public final static class Definitions extends ClientIcon {

        private Definitions(final String fileName) {
            super(fileName);
        }
        public static final ClientIcon TABLE = new ClientIcon("classpath:images/table.svg", true);
        public static final ClientIcon TABLE_ID = new ClientIcon("classpath:images/table_id.svg", true);
        public static final ClientIcon SELECTOR = new ClientIcon("classpath:images/selector.svg", true);
        public static final ClientIcon EDITOR = new ClientIcon("classpath:images/editor.svg", true);
        public static final ClientIcon TREES = new ClientIcon("classpath:images/explorer_trees.svg", true);
        public static final ClientIcon TREE = new ClientIcon("classpath:images/explorer_tree.svg", true);
        public static final ClientIcon PROPERTY = new ClientIcon("classpath:images/property.svg", true);
        public static final ClientIcon METHOD = new ClientIcon("classpath:images/method.svg", true);
        public static final ClientIcon CONSTSET = new ClientIcon("classpath:images/constSet.svg", true);
        public static final ClientIcon INTEGER_CONSTSET = new ClientIcon("classpath:images/constSet_int.svg", true);
        public static final ClientIcon STRING_CONSTSET = new ClientIcon("classpath:images/constSet_str.svg", true);
        public static final ClientIcon FILTER = new ClientIcon("classpath:images/filter.svg", true);
        public static final ClientIcon SORTING = new ClientIcon("classpath:images/sorting.svg", true);
        public static final ClientIcon USER_FILTER = new ClientIcon("classpath:images/user_filter.svg", true);
        public static final ClientIcon COMMON_FILTER = new ClientIcon("classpath:images/common_filter.svg", true);
        public static final ClientIcon USER_SORTING = new ClientIcon("classpath:images/user_sorting.svg", true);
        public static final ClientIcon APPEARANCE = new ClientIcon("classpath:images/skinSelector.svg", true);
        public static final ClientIcon SQL_FUNCTION = new ClientIcon("classpath:images/sqlFunction.svg", true);
        public static final ClientIcon COLUMN = new ClientIcon("classpath:images/column.svg", true);
        public static final ClientIcon CLASS = new ClientIcon("classpath:images/class.svg", true);
        public static final ClientIcon EVENT_CODE = new ClientIcon("classpath:images/event_code.svg", true);
        public static final ClientIcon DOMAIN = new ClientIcon("classpath:images/domain.svg", true);
        public static final ClientIcon ROLE = new ClientIcon("classpath:images/role.svg", true);
        public static final ClientIcon EDITOR_ID = new ClientIcon("classpath:images/editor_id.svg", true);
        public static final ClientIcon SELECTOR_ID = new ClientIcon("classpath:images/selector_id.svg", true);
    }

    public final static class ValueTypes extends ClientIcon {

        private ValueTypes(final String fileName) {
            super(fileName);
        }
        public static final ClientIcon INT = new ClientIcon("classpath:images/type_int.svg", true);
        public static final ClientIcon NUM = new ClientIcon("classpath:images/type_num.svg", true);
        public static final ClientIcon STR = new ClientIcon("classpath:images/type_string.svg", true);
        public static final ClientIcon CLOB = new ClientIcon("classpath:images/type_string.svg", true);
        public static final ClientIcon BOOL = new ClientIcon("classpath:images/type_bool.svg", true);
        public static final ClientIcon CHAR = new ClientIcon("classpath:images/type_char.svg", true);
        public static final ClientIcon DATE_TIME = new ClientIcon("classpath:images/time.svg", true);
        public static final ClientIcon BIN = new ClientIcon("classpath:images/type_bin.svg", true);
        public static final ClientIcon BLOB = new ClientIcon("classpath:images/type_bin.svg", true);
        public static final ClientIcon XML = new ClientIcon("classpath:images/xml.svg", true);
        public static final ClientIcon PARENT_REF = new ClientIcon("classpath:images/type_parentRef.svg", true);
    }

    public static class Dialog extends ClientIcon {

        protected Dialog(final String fileName) {
            super(fileName);
        }
        
        protected Dialog(final String fileName, final boolean isSvg) {
            super(fileName,isSvg);
        }                
        public static final ClientIcon BUTTON_OK = new ClientIcon("classpath:images/button_ok.svg", true);
        public static final ClientIcon BUTTON_CANCEL = new ClientIcon("classpath:images/button_cancel.svg", true);
        public static final ClientIcon BUTTON_NEXT = new ClientIcon("classpath:images/next.svg", true);
        public static final ClientIcon BUTTON_PREV = new ClientIcon("classpath:images/prev.svg", true);
        public static final ClientIcon BUTTON_YES_TO_ALL = new ClientIcon("classpath:images/all.svg", true);
        public static final ClientIcon BUTTON_ABORT = new ClientIcon("classpath:images/exceptionClass.svg", true);
        public static final ClientIcon ABOUT = new ClientIcon("classpath:images/about.svg", true);
        public static final ClientIcon CONNECTIONS_MANAGER = new ClientIcon("classpath:images/connections_manager.svg", true);
        public static final ClientIcon TRACE = new ClientIcon("classpath:images/trace.svg", true);
        public static final ClientIcon TESTER = new ClientIcon("classpath:images/tester.svg", true);
        public static final ClientIcon FILTER_PARAM_EDITOR = new ClientIcon("classpath:images/filter_param_editor.svg", true);
        public static final ClientIcon EXIT = new ClientIcon("classpath:images/exit.svg", false);
        public static final ClientIcon BUTTON_ARRIGHT = new ClientIcon("classpath:images/arrow_right.png", false);
        public static final ClientIcon BUTTON_ARDOWN = new ClientIcon("classpath:images/arrow_down.png", false);
    }

    public final static class Message extends ClientIcon {

        private Message(final String fileName) {
            super(fileName);
        }
        public static final ClientIcon ERROR = new ClientIcon("classpath:images/error.svg", true);
        public static final ClientIcon INFORMATION = new ClientIcon("classpath:images/information.svg", true);
        public static final ClientIcon CONFIRMATION = new ClientIcon("classpath:images/confirmation.svg", true);
        public static final ClientIcon WARNING = new ClientIcon("classpath:images/message_warning.svg", true);
    }

    public static final class Connection extends ClientIcon {

        private Connection(final String fileName) {
            super(fileName);
        }
        public static final ClientIcon CONNECT = new ClientIcon("classpath:images/connect.svg", true);
        public static final ClientIcon DISCONNECT = new ClientIcon("classpath:images/disconnect.svg", true);
        public static final ClientIcon KEY = new ClientIcon("classpath:images/key.svg", true);
        public static final ClientIcon CERTIFICATE = new ClientIcon("classpath:images/certificate.svg", true);
        public static final ClientIcon CERTIFICATE_MANAGER = new ClientIcon("classpath:images/certificate_manager.svg", true);
    }

    public static class CommonOperations extends ClientIcon {

        protected CommonOperations(final String fileName) {
            super(fileName);
        }

        protected CommonOperations(final String fileName, final boolean isSvgFormat) {
            super(fileName, isSvgFormat);
        }
        public static final ClientIcon OPEN = new ClientIcon("classpath:images/open.svg", true);
        public static final ClientIcon SAVE = new ClientIcon("classpath:images/save.svg", true);
        public static final ClientIcon CREATE = new ClientIcon("classpath:images/add.svg", true);
        public static final ClientIcon DELETE = new ClientIcon("classpath:images/delete.svg", true);
        public static final ClientIcon DELETE_ALL = new ClientIcon("classpath:images/delete_all.svg");
        public static final ClientIcon CLEAR = new ClientIcon("classpath:images/clear.svg", true);
        public static final ClientIcon CUT = new ClientIcon("classpath:images/cut.svg", true);
        public static final ClientIcon COPY = new ClientIcon("classpath:images/copy.svg", true);
        public static final ClientIcon PASTE = new ClientIcon("classpath:images/paste.svg", true);
        public static final ClientIcon UNDO = new ClientIcon("classpath:images/undo.svg", true);
        public static final ClientIcon REDO = new ClientIcon("classpath:images/redo.svg", true);
        public static final ClientIcon CANCEL = new ClientIcon("classpath:images/cancel_changes.svg");
        public static final ClientIcon VIEW = new ClientIcon("classpath:images/view.svg", true);
        public static final ClientIcon EDIT = new ClientIcon("classpath:images/edit.svg", true);
        public static final ClientIcon EDITING_HISTORY = new ClientIcon("classpath:images/editing_history.svg", true);
        public static final ClientIcon FAVORITES_POPUP = new ClientIcon("classpath:images/favorites_popup.svg", true);
        public static final ClientIcon FAVORITES = new ClientIcon("classpath:images/favorites.svg", true);
        public static final ClientIcon FIND = new ClientIcon("classpath:images/find.svg", true);
        public static final ClientIcon FIND_NEXT = new ClientIcon("classpath:images/find_next.svg", true);
        public static final ClientIcon REREAD = new ClientIcon("classpath:images/reread.svg", true);
        public static final ClientIcon REFRESH = new ClientIcon("classpath:images/refresh.svg", true);
        public static final ClientIcon UP = new ClientIcon("classpath:images/up.svg");
        public static final ClientIcon DOWN = new ClientIcon("classpath:images/down.svg");
        public static final ClientIcon RIGHT = new ClientIcon("classpath:images/right.svg");
        public static final ClientIcon LEFT = new ClientIcon("classpath:images/left.svg");
        public static final ClientIcon FORCE_RIGHT = new ClientIcon("classpath:images/force_right.svg");
        public static final ClientIcon FORCE_LEFT = new ClientIcon("classpath:images/force_left.svg");
        public static final ClientIcon TRANSLATE = new ClientIcon("classpath:images/transfer_in.svg");
    }

    public final static class ValueModification extends CommonOperations {

        private ValueModification(final String fileName) {
            super(fileName);
        }
        public static final ClientIcon INHERITANCE = new ClientIcon("classpath:images/inheritance.svg", true);
        public static final ClientIcon INHERIT = new ClientIcon("classpath:images/inherit.svg", true);
        public static final ClientIcon OVERRIDE = new ClientIcon("classpath:images/override.svg", true);
        public static final ClientIcon DEFINE = new ClientIcon("classpath:images/define.svg", true);
    }

    public final static class TraceLevel extends ClientIcon {

        private TraceLevel(final String fileName) {
            super(fileName);
        }
        public static final ClientIcon DEBUG = new ClientIcon("classpath:images/debug.svg", true);
        public static final ClientIcon EVENT = new ClientIcon("classpath:images/event.png");
        public static final ClientIcon WARNING = new ClientIcon("classpath:images/warning.svg");
        public static final ClientIcon ERROR = new ClientIcon("classpath:images/error.png");
        public static final ClientIcon ALARM = new ClientIcon("classpath:images/alarm.png");
        public static final ClientIcon NO_TRACING = new ClientIcon("classpath:images/disconnect.svg", true);
        public static final ClientIcon INHERITED = new ClientIcon("classpath:images/go_to_parent.svg", true);
        
        public static Icon findEventSeverityIcon(final EEventSeverity severity, final IClientEnvironment environment){
            final List <TraceProfileTreeController.EventSeverity> eventSeverityItems = 
                TraceProfileTreeController.getEventSeverityItemsByOrder(environment);
            for (TraceProfileTreeController.EventSeverity eventSeverity: eventSeverityItems){
                if (Objects.equals(eventSeverity.getValue(), severity.getName())){
                    return eventSeverity.getIcon();
                }
            }
            return null;
        }
        
        public static ClientIcon getIconBySeverity(final EEventSeverity severity) {
            if(severity == null) {
                return null;
            }
            
            switch(severity) {
                case ALARM:
                    return ALARM;
                case DEBUG:
                    return DEBUG;
                case ERROR:
                     return ERROR;
                case EVENT:
                    return EVENT;
                case NONE:
                    return NO_TRACING;
                case WARNING:
                    return WARNING;
                default:
                    return null;
            }
        }
    }
    public final String fileName;
    //Признак того, что пиктограмма имеет svg формат.
    //Метод ImageManager#loadSvgIcon работает быстрее чем
    //просто ImageManager#loadIcon
    public final boolean isSvg;

    protected ClientIcon(final String fileName) {
        this.fileName = fileName;
        isSvg = false;
    }

    protected ClientIcon(final String fileName, final boolean useSvgFormat) {
        this.fileName = fileName;
        isSvg = useSvgFormat;
    }
}
