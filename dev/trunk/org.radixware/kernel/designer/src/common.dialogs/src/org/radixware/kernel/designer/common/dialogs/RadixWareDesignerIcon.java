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

package org.radixware.kernel.designer.common.dialogs;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.resources.icons.RadixIcon;

/**
 * Repository of RadixWare Designer icons. Classes are generable.
 *
 */
public class RadixWareDesignerIcon extends RadixIcon {

//    static {
//        SvgImageLoaderManager.registerSvgImageLoader(SvgImageLoader.getInstance());
//    }
//    public static class ALIGN extends RadixWareDesignerIcon {
//        public static final ALIGN CENTER_BOTTOM = new ALIGN("align/center_bottom.svg");
//        public static final ALIGN CENTER_MIDDLE = new ALIGN("align/center_middle.svg");
//        public static final ALIGN CENTER_TOP = new ALIGN("align/center_top.svg");
//        public static final ALIGN LEFT_BOTTOM = new ALIGN("align/left_bottom.svg");
//        public static final ALIGN LEFT_MIDDLE = new ALIGN("align/left_middle.svg");
//        public static final ALIGN LEFT_TOP = new ALIGN("align/left_top.svg");
//        public static final ALIGN RIGHT_BOTTOM = new ALIGN("align/right_bottom.svg");
//        public static final ALIGN RIGHT_MIDDLE = new ALIGN("align/right_middle.svg");
//        public static final ALIGN RIGHT_TOP = new ALIGN("align/right_top.svg");
//        public static final ALIGN LAYOUT_GRID = new ALIGN("align/layout_grid.svg");
//        public static final ALIGN LAYOUT_HORIZONTALY = new ALIGN("align/layout_horizontaly.svg");
//        public static final ALIGN LAYOUT_VERTICALLY = new ALIGN("align/layout_vertically.svg");
//        public static final ALIGN NO_LAYOUT = new ALIGN("align/no_layout.svg");
//        private ALIGN(String uri) {
//            super(uri);
//        }
//    }
    public static class ALIGN extends RadixWareDesignerIcon {

        public static class TEXT extends RadixWareDesignerIcon {

            public static final TEXT CENTER_BOTTOM = new TEXT("align/text/bottom-center.png");
            public static final TEXT CENTER_MIDDLE = new TEXT("align/text/center-center.png");
            public static final TEXT CENTER_TOP = new TEXT("align/text/top-center.png");
            public static final TEXT LEFT_BOTTOM = new TEXT("align/text/bottom-left.png");
            public static final TEXT LEFT_MIDDLE = new TEXT("align/text/center-left.png");
            public static final TEXT LEFT_TOP = new TEXT("align/text/top-left.png");
            public static final TEXT RIGHT_BOTTOM = new TEXT("align/text/bottom-right.png");
            public static final TEXT RIGHT_MIDDLE = new TEXT("align/text/center-right.png");
            public static final TEXT RIGHT_TOP = new TEXT("align/text/top-right.png");

            private TEXT(String uri) {
                super(uri);
            }
        }

        public static class DIAGRAM extends RadixWareDesignerIcon {

            public static final DIAGRAM BY_HEIGHT = new DIAGRAM("align/diagram/by_height.png");
            public static final DIAGRAM BY_WIDTH = new DIAGRAM("align/diagram/by_width.png");
            public static final DIAGRAM BOTTOM = new DIAGRAM("align/diagram/bottom.png");
            public static final DIAGRAM CENTER = new DIAGRAM("align/diagram/center.png");
            public static final DIAGRAM LEFT = new DIAGRAM("align/diagram/left.png");
            public static final DIAGRAM MIDDLE = new DIAGRAM("align/diagram/middle.png");
            public static final DIAGRAM RIGHT = new DIAGRAM("align/diagram/right.png");
            public static final DIAGRAM TOP = new DIAGRAM("align/diagram/top.png");
            public static final DIAGRAM ADJUST = new DIAGRAM("align/diagram/align-height.png");

            private DIAGRAM(String uri) {
                super(uri);
            }
        }

        private ALIGN(String uri) {
            super(uri);
        }
    }

    public static class ARRANGE extends RadixWareDesignerIcon {

        public static final ARRANGE BY_HORIZONTAL = new ARRANGE("arrange/by_horizontal.png");
        public static final ARRANGE BY_VERTICAL = new ARRANGE("arrange/by_vertical.png");

        private ARRANGE(String uri) {
            super(uri);
        }
    }

    public static class RIGHTS extends RadixWareDesignerIcon {

        public static final RIGHTS ALL = new RIGHTS("rights/all.svg");
        public static final RIGHTS ACCESS_ONLY = new RIGHTS("rights/access_only.svg");

        private RIGHTS(String uri) {
            super(uri);
        }
    }

    public static class ARROW extends RadixWareDesignerIcon {

        public static final ARROW MOVE_UP = new ARROW("arrow/up.svg");
        public static final ARROW MOVE_DOWN = new ARROW("arrow/down.svg");
        public static final ARROW MOVE = new ARROW("arrow/move.svg");
        public static final ARROW CIRCLE = new ARROW("arrow/circle.svg");
        public static final ARROW GO_TO_OBJECT = new ARROW("arrow/go_to_object.png");
        public static final ARROW LEFT = new ARROW("arrow/left.svg");
        public static final ARROW RIGHT = new ARROW("arrow/right.svg");
        public static final ARROW GO_TO_OVERWRITTEN = new ARROW("template/inherit.svg");
        public static final ARROW GO_TO_OVERWRITE = new ARROW("template/override.svg");
        public static final ARROW UP_RIGHT = new ARROW("arrow/up_right.svg");
        public static final ARROW RIGHT_UP = new ARROW("arrow/right_up.svg");

        private ARROW(String uri) {
            super(uri);
        }
    }

    public static class CHECK extends RadixWareDesignerIcon {

        public static final CHECK ERRORS = new CHECK("check/errors.svg");
        public static final CHECK CHECK = new CHECK("check/check.svg");
        public static final CHECK STACK = new CHECK("check/stack.svg");
        public static final CHECK SET = new CHECK("check/set.svg");
        public static final CHECK STOP = new CHECK("check/stop.svg");
        public static final CHECK FILTER_BY_OBJECT = new CHECK("check/filter_by_object.svg");
        public static final CHECK FILTER_BY_MODULE = new CHECK("check/filter_by_module.svg");
        public static final CHECK UNSET = new CHECK("check/unset.svg");

        private CHECK(String uri) {
            super(uri);
        }
    }

    public static class CREATE extends RadixWareDesignerIcon {

        public static final CREATE ADD = new CREATE("create/add.svg");
        public static final CREATE ADD_SUB = new CREATE("create/add_sub.svg");
        public static final CREATE ADD_COLUMN = new CREATE("create/add_column.svg");
        public static final CREATE ADD_ROW = new CREATE("create/add_row.svg");
        public static final CREATE NEW_DOCUMENT = new CREATE("create/new_document.svg");

        private CREATE(String uri) {
            super(uri);
        }
    }

    public static class DATABASE extends RadixWareDesignerIcon {

        public static final DATABASE RESOLVER = new DATABASE("database/database_resolver.svg");
        public static final DATABASE IMPORT = new DATABASE("database/import_from_database.svg");
        public static final DATABASE LOAD = new DATABASE("database/load_from_database.svg");
        public static final DATABASE ORACLE_HINT = new DATABASE("database/oracle_hint.svg");
        public static final DATABASE DB_NAME = new DATABASE("database/db_name.svg");
        public static final DATABASE THIS_TABLE = new DATABASE("database/this_table.svg");
        public static final DATABASE TABLE_INITIAL_VALUES = new DATABASE("database/table_initial_values.svg");
        public static final DATABASE FIRST_ROWS = new DATABASE("dds/first_rows.svg");
        public static final DATABASE ALL_ROWS = new DATABASE("dds/all_rows.svg");
        public static final DATABASE CALCULATED_FIELD = new DATABASE("ads/sql/calculated_field.svg");
        public static final DATABASE TYPIFIED_FIELD = new DATABASE("ads/sql/typified_field.svg");

        private DATABASE(String uri) {
            super(uri);
        }
    }

    public static class DEBUG extends RadixWareDesignerIcon {

        public static final DEBUG IF = new DEBUG("debug/if.svg");
        public static final DEBUG IF_ELSE = new DEBUG("debug/if_else.svg");
        public static final DEBUG IF_END = new DEBUG("debug/if_end.svg");

        private DEBUG(String uri) {
            super(uri);
        }
    }

    public static class DELETE extends RadixWareDesignerIcon {

        public static final DELETE DELETE = new DELETE("delete/delete.svg");
        public static final DELETE CLEAR = new DELETE("delete/clear.svg");
        public static final DELETE DELETE_COLUMN = new DELETE("delete/delete_column.svg");
        public static final DELETE DELETE_ROW = new DELETE("delete/delete_row.svg");

        private DELETE(String uri) {
            super(uri);
        }
    }

    public static class DIALOG extends RadixWareDesignerIcon {

        public static final DIALOG OK = new DIALOG("dialog/ok.svg");
        public static final DIALOG CHOOSE = new DIALOG("dialog/choose.svg");
        public static final DIALOG ALL = new DIALOG("dialog/all.svg");

        private DIALOG(String uri) {
            super(uri);
        }
    }

    public static class EDIT extends RadixWareDesignerIcon {

        public static final EDIT SEARCH = new EDIT("edit/search.svg");
        public static final EDIT EDIT = new EDIT("edit/edit.svg");
        public static final EDIT INSERT_FIELD = new EDIT("edit/insert_field.svg");
        public static final EDIT INSERT_OBJECT = new EDIT("edit/insert_object.svg");
        public static final EDIT NO_ICON = new EDIT("edit/no_icon.svg");
        public static final EDIT NOT_DEFINED = new EDIT("edit/not_defined.svg");
        public static final EDIT PROPERTIES = new EDIT("edit/properties.svg");
        public static final EDIT PROPERTIES_PLUS = new EDIT("edit/properties_plus.svg");
        public static final EDIT VIEW_SQL = new EDIT("edit/view_sql.svg");
        public static final EDIT FIX = new EDIT("edit/fix.svg");
        public static final EDIT REPLACE_IMAGE = new EDIT("edit/replace_image.svg");
        // for module images editor
        public static final EDIT CURRENT_MODULE_IMAGES = new EDIT("edit/current_module_images.svg");
        public static final EDIT DEPENDENT_MODULE_IMAGES = new EDIT("edit/dependent_module_images.svg");
        public static final EDIT ALL_IMAGES = new EDIT("edit/all_images.svg");
        public static final EDIT BACKGROUND = new EDIT("edit/background.png");
        public static final EDIT FOREGROUND = new EDIT("edit/textcolor.png");
        public static final EDIT BORDER_COLOR = new EDIT("edit/bordercolor.png");
        public static final EDIT LINK = new EDIT("edit/link.svg");

        private EDIT(String uri) {
            super(uri);
        }
    }

    public static class EVENT_LOG extends RadixWareDesignerIcon {

        public static final EVENT_LOG NONE = new EVENT_LOG("eventlog/none.svg");
        public static final EVENT_LOG DEBUG = new EVENT_LOG("eventlog/debug.svg");
        public static final EVENT_LOG EVENT = new EVENT_LOG("eventlog/event.svg");
        public static final EVENT_LOG WARNING = new EVENT_LOG("eventlog/warning.svg");
        public static final EVENT_LOG ERROR = new EVENT_LOG("eventlog/error.svg");
        public static final EVENT_LOG ALARM = new EVENT_LOG("eventlog/alarm.svg");

        private EVENT_LOG(String uri) {
            super(uri);
        }

        public static RadixIcon getForSeverity(final EEventSeverity severity) {
            switch (severity) {
                case NONE:
                    return NONE;
                case DEBUG:
                    return DEBUG;
                case EVENT:
                    return EVENT;
                case WARNING:
                    return WARNING;
                case ERROR:
                    return ERROR;
                case ALARM:
                    return ALARM;
                default:
                    throw new IllegalStateException(".svg");
            }
        }
    }

    public static class FILE extends RadixWareDesignerIcon {

        public static final FILE NEW_DOCUMENT = new FILE("create/new_document.svg");
        public static final FILE LOAD_IMAGE = new FILE("file/load_image.svg");
        public static final FILE SAVE = new FILE("file/save.svg");
        public static final FILE SAVE_ALL = new FILE("file/save_all.svg");
        public static final FILE SAVE_IMAGE = new FILE("file/save_image.svg");

        private FILE(String uri) {
            super(uri);
        }
    }

    public static class JAVA extends RadixWareDesignerIcon {

        public static final JAVA JAVA = new JAVA("java/java.svg");
        public static final JAVA PACKAGE = new JAVA("java/package.svg");
        public static final JAVA CLASS = new JAVA("java/class.svg");
        public static final JAVA DEPRECATE = new JAVA("java/deprecate.svg");
        public static final JAVA UNDEPRECATE = new JAVA("java/undeprecate.svg");

        private JAVA(String uri) {
            super(uri);
        }
    }

    public static class JML_EDITOR extends RadixWareDesignerIcon {

        // TODO: remove duplications with other icon collections
        public static final JML_EDITOR ID = new JML_EDITOR("jmledit/id.svg");
        public static final JML_EDITOR CLASS = new JML_EDITOR("java/class.svg");
        public static final JML_EDITOR NLS = new JML_EDITOR("jmledit/nls.svg");
        public static final JML_EDITOR TASK = new JML_EDITOR("jmledit/task.svg");
        public static final JML_EDITOR EVENT_CODE = new JML_EDITOR("jmledit/event_code.svg");
        public static final JML_EDITOR DATA = new JML_EDITOR("object/any_object.svg");
        public static final JML_EDITOR COMPLETE = new JML_EDITOR("jmledit/complete.svg");
        public static final JML_EDITOR METHOD = new JML_EDITOR("jmledit/method.svg");
        public static final JML_EDITOR PROPERTY = new JML_EDITOR("jmledit/property.svg");
        public static final JML_EDITOR WIDGET_REF = new JML_EDITOR("jmledit/widget_ref.svg");
        public static final JML_EDITOR FORMAT = new JML_EDITOR("jmledit/format.svg");

        private JML_EDITOR(String uri) {
            super(uri);
        }
    }

    public static class METHOD extends RadixWareDesignerIcon {

        public static final METHOD VOID = new METHOD("method/void.svg");
        public static final METHOD PUBLISHED = new METHOD("method/published.svg");

        private METHOD(String uri) {
            super(uri);
        }
    }

    public static class TREE extends RadixWareDesignerIcon {

        public static final TREE ALLOWED = new TREE("tree/allowed.svg");
        public static final TREE COLLAPSE = new TREE("tree/collapse.svg");
        public static final TREE DEPENDENCIES = new TREE("tree/dependencies.png");
        public static final TREE DEPENDENCIES2 = new TREE("tree/dependencies2.svg");
        public static final TREE FORBIDDEN = new TREE("tree/forbidden.svg");
        public static final TREE INHERIT = new TREE("tree/inherit.svg");
        public static final TREE SELECT_IN_TREE = new TREE("tree/select_in_tree.svg");
        public static final TREE UNDEPENDENCIES = new TREE("tree/undependencies.svg");
        @Deprecated
        public static final TREE INSERT_FROM_TREE = new TREE("edit/insert_object.svg");

        private TREE(String uri) {
            super(uri);
        }
    }

    public static class SECURITY extends RadixWareDesignerIcon {

        public static final SECURITY KEY = new SECURITY("security/key.svg");

        private SECURITY(String uri) {
            super(uri);
        }
    }

    public static class WIDGETS extends RadixWareDesignerIcon {

        public static final WIDGETS SNAP = new WIDGETS("widgets/snap.svg");
        /*
         * public static final WIDGETS BUTTON_BOX = new
         * WIDGETS("widgets/button_box.svg"); public static final WIDGETS
         * CHECK_BOX = new WIDGETS("widgets/check_box.svg"); public static final
         * WIDGETS DATE_EDIT = new WIDGETS("widgets/date_edit.svg"); public
         * static final WIDGETS TIME_EDIT = new
         * WIDGETS("widgets/time_edit.svg"); public static final WIDGETS
         * DATE_TIME_EDIT = new WIDGETS("widgets/date_time_edit.svg"); public
         * static final WIDGETS GRID_LAYOUT = new
         * WIDGETS("widgets/grid_layout.svg"); public static final WIDGETS
         * GROUP_BOX = new WIDGETS("widgets/group_box.svg"); public static final
         * WIDGETS HORIZONTAL_LAYOUT = new
         * WIDGETS("widgets/horizontal_layout.svg"); public static final WIDGETS
         * HORIZONTAL_SPACER = new WIDGETS("widgets/horizontal_spacer.svg");
         * public static final WIDGETS LABEL = new WIDGETS("widgets/label.svg");
         * public static final WIDGETS PROGRESS_BAR = new
         * WIDGETS("widgets/progress_bar.svg"); public static final WIDGETS
         * LINE_EDIT = new WIDGETS("widgets/line_edit.svg"); public static final
         * WIDGETS COMBO_BOX = new WIDGETS("widgets/combo_box.svg"); public
         * static final WIDGETS LIST = new WIDGETS("widgets/list.svg"); public
         * static final WIDGETS PUSH_BUTTON = new
         * WIDGETS("widgets/push_button.svg"); public static final WIDGETS
         * RADIO_BUTTON = new WIDGETS("widgets/radio_button.svg"); public static
         * final WIDGETS SPIN_BOX = new WIDGETS("widgets/spin_box.svg"); public
         * static final WIDGETS DOUBLE_SPIN_BOX = new
         * WIDGETS("widgets/double_spin_box.svg"); public static final WIDGETS
         * TAB = new WIDGETS("widgets/tab.svg"); public static final WIDGETS
         * TABLE = new WIDGETS("widgets/table.svg"); public static final WIDGETS
         * TAB_SET = new WIDGETS("widgets/tab_set.svg"); public static final
         * WIDGETS TEXT_EDIT = new WIDGETS("widgets/text_edit.svg"); public
         * static final WIDGETS TOOL_BAR = new WIDGETS("widgets/tool_bar.svg");
         * public static final WIDGETS TOOL_BUTTON = new
         * WIDGETS("widgets/tool_button.svg"); public static final WIDGETS TREE
         * = new WIDGETS("widgets/tree.svg"); public static final WIDGETS
         * VERTICAL_LAYOUT = new WIDGETS("widgets/vertical_layout.svg"); public
         * static final WIDGETS VERTICAL_SPACER = new
         * WIDGETS("widgets/vertical_spacer.svg");
         *
         * public static final RadixIcon PROP_LABEL =
         * AdsDefinitionIcon.CLASS_FORM_HANDLER; public static final RadixIcon
         * PROP_EDITOR = AdsDefinitionIcon.CLASS_FORM_HANDLER; public static
         * final RadixIcon COMMAND_PUSH_BUTTON_ITEM =
         * AdsDefinitionIcon.CLASS_FORM_HANDLER; public static final RadixIcon
         * EDITOR_PAGE_ITEM = AdsDefinitionIcon.CLASS_FORM_HANDLER; public
         * static final RadixIcon EMBEDDED_EDITOR_ITEM =
         * AdsDefinitionIcon.CLASS_FORM_HANDLER; public static final RadixIcon
         * EMBEDDED_SELECTOR_ITEM = AdsDefinitionIcon.CLASS_FORM_HANDLER;
         */
        public static final WIDGETS CHECK_BOX_CHECKED = new WIDGETS("widgets/checkbox_checked.svg");
        public static final WIDGETS CHECK_BOX_UNCHECKED = new WIDGETS("widgets/checkbox_unchecked.svg");
        public static final WIDGETS RADIOBUTTON_CHECKED = new WIDGETS("widgets/radiobutton_checked.svg");
        public static final WIDGETS RADIOBUTTON_UNCHECKED = new WIDGETS("widgets/radiobutton_unchecked.svg");
        public static final WIDGETS ARROW_UP = new WIDGETS("widgets/arrow_up.svg");
        public static final WIDGETS ARROW_DOWN = new WIDGETS("widgets/arrow_down.svg");
        public static final WIDGETS ARROW_RIGHT = new WIDGETS("widgets/arrow_right.svg");
        public static final WIDGETS ARROW_LEFT = new WIDGETS("widgets/arrow_left.svg");
        public static final WIDGETS ARROW_UP_DISABLE = new WIDGETS("widgets/arrow_up_disable.svg");
        public static final WIDGETS ARROW_DOWN_DISABLE = new WIDGETS("widgets/arrow_down_disable.svg");
        public static final WIDGETS ARROW_RIGHT_DISABLE = new WIDGETS("widgets/arrow_right_disable.svg");
        public static final WIDGETS ARROW_LEFT_DISABLE = new WIDGETS("widgets/arrow_left_disable.svg");
        public static final WIDGETS TREE_ITEM_HAS_CHILDS_LAST = new WIDGETS("widgets/tree_item_with_childs_last20.svg");
        public static final WIDGETS TREE_ITEM_HAS_CHILDS = new WIDGETS("widgets/tree_item_with_childs20.svg");
        public static final WIDGETS TREE_ITEM_LAST = new WIDGETS("widgets/tree_item_last20.svg");
        public static final WIDGETS TREE_ITEM = new WIDGETS("widgets/tree_item20.svg");
        public static final WIDGETS STANDARD_OK = new WIDGETS("widgets/standardbutton-ok-16.png");
        public static final WIDGETS STANDARD_CANCEL = new WIDGETS("widgets/standardbutton-cancel-16.png");
        public static final WIDGETS STANDARD_APPLY = new WIDGETS("widgets/standardbutton-apply-16.png");
        public static final WIDGETS STANDARD_CLEAR = new WIDGETS("widgets/standardbutton-clear-16.png");
        public static final WIDGETS STANDARD_CLOSE = new WIDGETS("widgets/standardbutton-close-16.png");
        public static final WIDGETS STANDARD_DELETE = new WIDGETS("widgets/standardbutton-cancel-16.png");
        public static final WIDGETS STANDARD_HELP = new WIDGETS("widgets/standardbutton-help-16.png");
        public static final WIDGETS STANDARD_NO = new WIDGETS("widgets/standardbutton-no-16.png");
        public static final WIDGETS STANDARD_OPEN = new WIDGETS("widgets/standardbutton-open-16.png");
        public static final WIDGETS STANDARD_SAVE = new WIDGETS("widgets/standardbutton-save-16.png");
        public static final WIDGETS STANDARD_YES = new WIDGETS("widgets/standardbutton-yes-16.png");
        public static final WIDGETS SIGNALS = new WIDGETS("widgets/signals.svg");
        public static final WIDGETS PARAMS = new WIDGETS("widgets/params.svg");
        public static final WIDGETS CHOOSE_DEFS = new WIDGETS("mlstr/def.svg");
        public static final WIDGETS CHOOSE_LANGS = new WIDGETS("mlstr/languages.svg");
        public static final WIDGETS CHOOSE_PARAM = new WIDGETS("mlstr/param.svg");
        public static final WIDGETS LOAD = new WIDGETS("mlstr/child_ref.svg");
        public static final WIDGETS STATISTICS = new WIDGETS("mlstr/entity.svg");
        public static final WIDGETS TRANSLATION_NOT_CHECKED = new WIDGETS("mlstr/translation_not_checked.svg");
        public static final WIDGETS TRANSLATION_NOT_CHECKED_DISABLED = new WIDGETS("mlstr/translation_not_checked_disabled.svg");
        public static final WIDGETS TRANSLAT_DISABLED = new WIDGETS("mlstr/translate_disabled.svg");
        public static final WIDGETS NEW_LINE = new WIDGETS("mlstr/new_line.svg");
        public static final WIDGETS TRANSLATION_NOT_CHECKED_BORDERED = new WIDGETS("mlstr/translation_not_checked_bordered.svg");
        public static final WIDGETS TRANSLATE_BORDERED = new WIDGETS("mlstr/translate_bordered.svg");
        public static final WIDGETS UNCHECKED_SOURCE = new WIDGETS("mlstr/unchecked_source.svg");
        

        private WIDGETS(String uri) {
            super(uri);
        }
    }

    public static class WORKFLOW extends RadixWareDesignerIcon {
        /*
         * public static final WORKFLOW NOTE = new
         * WORKFLOW("workflow/palette/note.svg"); public static final WORKFLOW
         * WAIT = new WORKFLOW("workflow/palette/wait.svg"); public static final
         * WORKFLOW EMPTY = new WORKFLOW("workflow/palette/empty.svg"); public
         * static final WORKFLOW FORK = new
         * WORKFLOW("workflow/palette/fork.svg"); public static final WORKFLOW
         * MERGE = new WORKFLOW("workflow/palette/merge.svg"); public static
         * final WORKFLOW SCOPE = new WORKFLOW("workflow/palette/scope.svg");
         * public static final WORKFLOW VAR = new
         * WORKFLOW("workflow/palette/var.svg"); public static final WORKFLOW
         * CATCH = new WORKFLOW("workflow/palette/catch.svg"); public static
         * final WORKFLOW FINISH = new WORKFLOW("workflow/palette/finish.svg");
         * public static final WORKFLOW PROGRAM = new
         * WORKFLOW("workflow/palette/program.svg"); public static final
         * WORKFLOW RETURN = new WORKFLOW("workflow/palette/return.svg"); public
         * static final WORKFLOW START = new
         * WORKFLOW("workflow/palette/start.svg"); public static final WORKFLOW
         * TERM = new WORKFLOW("workflow/palette/term.svg"); public static final
         * WORKFLOW THROW = new WORKFLOW("workflow/palette/throw.svg"); public
         * static final WORKFLOW INCLUDE = new
         * WORKFLOW("workflow/palette/include.svg");
         */

        public static final WORKFLOW WIDGET_FINISH = new WORKFLOW("workflow/widget/finish.svg");
        public static final WORKFLOW WIDGET_RETURN = new WORKFLOW("workflow/widget/return.svg");
        public static final WORKFLOW WIDGET_START = new WORKFLOW("workflow/widget/start.svg");
        public static final WORKFLOW WIDGET_TERM = new WORKFLOW("workflow/widget/term.svg");
        public static final WORKFLOW WIDGET_THROW = new WORKFLOW("workflow/widget/throw.svg");
        public static final WORKFLOW WIDGET_FORK = new WORKFLOW("workflow/widget/fork.svg");
        public static final WORKFLOW WIDGET_MERGE = new WORKFLOW("workflow/widget/merge.svg");
        public static final WORKFLOW PALETTE_INFORMATION = new WORKFLOW("workflow/palette/information.svg");
        /*
         * public static final WORKFLOW DOC_MANAGER_CREATOR = new
         * WORKFLOW("workflow/app/docmanagercreator.svg"); public static final
         * WORKFLOW PERSONAL_COMMUNICATOR = new
         * WORKFLOW("workflow/app/personalcommunicator.svg"); public static
         * final WORKFLOW EDITOR_FORM_CREATOR = new
         * WORKFLOW("workflow/app/editorformcreator.svg"); public static final
         * WORKFLOW SELECTOR_FORM_CREATOR = new
         * WORKFLOW("workflow/app/selectorformcreator.svg"); public static final
         * WORKFLOW REPORT_GENERATOR = new
         * WORKFLOW("workflow/app/reportgenerator.svg"); public static final
         * WORKFLOW DIALOG_CREATOR = new
         * WORKFLOW("workflow/app/dialogcreator.svg"); public static final
         * WORKFLOW DIALOG_DUPLICATOR = new
         * WORKFLOW("workflow/app/dialogduplicator.svg"); public static final
         * WORKFLOW NETPORT = new WORKFLOW("workflow/app/netport.svg");
         */
        public static final WORKFLOW DIALOG_SET = new WORKFLOW("workflow/dialog/set.svg");
        public static final WORKFLOW DIALOG_UNSET = new WORKFLOW("workflow/dialog/unset.svg");

        private WORKFLOW(String uri) {
            super(uri);
        }
    }

    public static class BORDER extends RadixWareDesignerIcon {

        public static final BORDER NO_BORDER = new BORDER("border/border_none.png");
        public static final BORDER ALL_BORDER = new BORDER("border/border_all.png");
        public static final BORDER TOP_BORDER = new BORDER("border/border_top.png");
        public static final BORDER LEFT_BORDER = new BORDER("border/border_left.png");
        public static final BORDER BOTTOM_BORDER = new BORDER("border/border_bottom.png");
        public static final BORDER RIGHT_BORDER = new BORDER("border/border_right.png");

        private BORDER(String uri) {
            super(uri);
        }
    }

    public static class REPORT extends RadixWareDesignerIcon {

        public static final REPORT GRID = new REPORT("ads/report/grid.png");

        private REPORT(String uri) {
            super(uri);
        }
    }

    private RadixWareDesignerIcon(String uri) {
        super(uri);
    }
}
