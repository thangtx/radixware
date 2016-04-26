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

package org.radixware.kernel.common.defs.ads;

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsBaseObject.Kind;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.type.AdsAccessFlags;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EMethodNature;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.common.enums.EReportChartSeriesType;
import org.radixware.kernel.common.resources.icons.RadixIcon;


public class AdsDefinitionIcon extends RadixIcon {

    public static final AdsDefinitionIcon SEGMENT = new AdsDefinitionIcon("ads/segment.svg");
    public static final AdsDefinitionIcon MODULE = new AdsDefinitionIcon("ads/module.svg");
    public static final AdsDefinitionIcon TEST_MODULE = new AdsDefinitionIcon("ads/test_module.svg");
    /**
     * Top level definition icons
     */
    public static final AdsDefinitionIcon CONTEXTLESS_COMMAND = new AdsDefinitionIcon("ads/contextless_command.svg");
    public static final AdsDefinitionIcon PARAGRAPH = new AdsDefinitionIcon("ads/paragraph.svg");
    public static final AdsDefinitionIcon XML_SCHEME = new AdsDefinitionIcon("ads/xml_scheme.svg");
    public static final AdsDefinitionIcon XML_TRANSFORMATION = new AdsDefinitionIcon("ads/xslt_transformation.svg");
    public static final AdsDefinitionIcon MSDL_SCHEME = new AdsDefinitionIcon("ads/msdl/msdl_scheme.svg");
    public static final AdsDefinitionIcon MSDL_SCHEME_FIELD = new AdsDefinitionIcon("ads/msdl/field.svg");
    public static final AdsDefinitionIcon MSDL_SCHEME_FIELDS = new AdsDefinitionIcon("ads/msdl/fields.svg");
    public static final AdsDefinitionIcon ROLE = new AdsDefinitionIcon("ads/role.svg");
    public static final AdsDefinitionIcon DATA_SEGMENT = new AdsDefinitionIcon("ads/data_segment.svg");
    public static final AdsDefinitionIcon LOCALIZING_BUNDLE = new AdsDefinitionIcon("ads/localizing_bundle.svg");
    public static final AdsDefinitionIcon MULTILINGUAL_STRING = new AdsDefinitionIcon("ads/multilingual_string.svg");
    public static final AdsDefinitionIcon PHRASE_BOOK = new AdsDefinitionIcon("ads/phrase_book.svg");
    /**
     * Classes
     */
    public static final AdsDefinitionIcon CLASS_ALGORITHM = new AdsDefinitionIcon("ads/class/algorithm.svg");
    public static final AdsDefinitionIcon CLASS_ALGORITHM_SCOPE = new AdsDefinitionIcon("workflow/palette/scope.svg");
    public static final AdsDefinitionIcon CLASS_ALGORITHM_CATCH = new AdsDefinitionIcon("workflow/palette/catch.svg");
    public static final AdsDefinitionIcon CLASS_DYNAMIC = new AdsDefinitionIcon("ads/class/dynamic.svg");
    public static final AdsDefinitionIcon CLASS_EXCEPTION = new AdsDefinitionIcon("ads/class/exception.svg");
    public static final AdsDefinitionIcon CLASS_INTERFACE = new AdsDefinitionIcon("ads/class/interface.svg");
    public static final AdsDefinitionIcon CLASS_ENTITY = new AdsDefinitionIcon("ads/class/entity.svg");
    public static final AdsDefinitionIcon CLASS_ENTITY_GROUP = new AdsDefinitionIcon("ads/class/entity_group.svg");
    public static final AdsDefinitionIcon CLASS_ENTITY_ADAPTER = new AdsDefinitionIcon("ads/class/entity_adapter.svg");
    public static final AdsDefinitionIcon CLASS_APPLICATION = new AdsDefinitionIcon("ads/class/application.svg");
    public static final AdsDefinitionIcon CLASS_FORM_HANDLER = new AdsDefinitionIcon("ads/class/form_handler.svg");
    public static final AdsDefinitionIcon CLASS_MODEL = new AdsDefinitionIcon("ads/class/model.svg");
    public static final AdsDefinitionIcon CLASS_STATEMENT = new AdsDefinitionIcon("ads/sql/statement.svg");
    public static final AdsDefinitionIcon CLASS_CURSOR = new AdsDefinitionIcon("ads/sql/cursor.svg");
    public static final AdsDefinitionIcon CLASS_REPORT = new AdsDefinitionIcon("ads/sql/report.svg");
    public static final AdsDefinitionIcon CLASS_USER_REPORT = new AdsDefinitionIcon("ads/report/user_report.svg");
    public static final AdsDefinitionIcon CLASS_USER_REPORT_CURRENT_VERSION = new AdsDefinitionIcon("ads/report/user_report_current.svg");
    public static final AdsDefinitionIcon CLASS_PROCEDURE = new AdsDefinitionIcon("ads/sql/procedure.svg");
    public static final AdsDefinitionIcon CLASS_TESTCASE = new AdsDefinitionIcon("ads/class/testcase.svg");
    public static final AdsDefinitionIcon CLASS_ENUM = new AdsDefinitionIcon("ads/class/enum.svg");
    /**
     * Class components
     */
    public static final AdsDefinitionIcon METHOD_GROUP = new AdsDefinitionIcon("ads/method/method_group.svg");
    public static final AdsDefinitionIcon PROPERTY_GROUP = new AdsDefinitionIcon("ads/property/property_group.svg");
    public static final AdsDefinitionIcon SQL_CLASS_CUSTOM_PARAMETER = new AdsDefinitionIcon("ads/sql/custom_parameter.svg");
    public static final AdsDefinitionIcon SQL_CLASS_TYPIFIED_PARAMETER = new AdsDefinitionIcon("ads/sql/typified_parameter.svg");
    public static final AdsDefinitionIcon SQL_CLASS_PK_PARAMETER = new AdsDefinitionIcon("ads/sql/pk_parameter.svg");
    public static final AdsDefinitionIcon SQL_CLASS_LITERAL_PARAMETER = new AdsDefinitionIcon("ads/sql/literal_parameter.svg");
    public static final AdsDefinitionIcon SQL_CLASS_PARAMETERS = new AdsDefinitionIcon("ads/sql/parameters.svg");
    /**
     * Report components
     */
    public static final AdsDefinitionIcon REPORT_BAND = new AdsDefinitionIcon("ads/report/band.svg");
    public static final AdsDefinitionIcon REPORT_BANDS = new AdsDefinitionIcon("ads/report/bands.svg");
    public static final AdsDefinitionIcon REPORT_FORM = new AdsDefinitionIcon("ads/report/form.svg");
    public static final AdsDefinitionIcon REPORT_GROUP = new AdsDefinitionIcon("ads/report/group.svg");
    public static final AdsDefinitionIcon REPORT_TEXT_CELL = new AdsDefinitionIcon("ads/report/text_cell.svg");
    public static final AdsDefinitionIcon REPORT_PROPERTY_CELL = new AdsDefinitionIcon("ads/report/property_cell.svg");
    public static final AdsDefinitionIcon REPORT_EXPRESSION_CELL = new AdsDefinitionIcon("ads/method/method.svg");
    public static final AdsDefinitionIcon REPORT_PARAMETER_CELL = SQL_CLASS_CUSTOM_PARAMETER;
    public static final AdsDefinitionIcon REPORT_SPECIAL_CELL = new AdsDefinitionIcon("ads/report/special_cell.svg");
    public static final AdsDefinitionIcon REPORT_SUMMARY_CELL = new AdsDefinitionIcon("ads/report/summary_cell.svg");
    public static final AdsDefinitionIcon REPORT_SUB_REPORT = new AdsDefinitionIcon("ads/report/sub_report.svg");
    public static final AdsDefinitionIcon REPORT_PRE_REPORT = new AdsDefinitionIcon("ads/report/pre_report.svg");
    public static final AdsDefinitionIcon REPORT_POST_REPORT = new AdsDefinitionIcon("ads/report/post_report.svg");
    public static final AdsDefinitionIcon REPORT_IMAGE_CELL = new AdsDefinitionIcon("edit/image.svg");
    public static final AdsDefinitionIcon REPORT_DB_IMAGE_CELL = new AdsDefinitionIcon("ads/report/db_image_cell.svg");
    public static final AdsDefinitionIcon REPORT_CHART_CELL = new AdsDefinitionIcon("ads/report/chart.svg");
    public static final AdsDefinitionIcon REPORT_CHART_BAR = new AdsDefinitionIcon("ads/report/bar.svg");
    public static final AdsDefinitionIcon REPORT_CHART_LINE = new AdsDefinitionIcon("ads/report/line.svg");
    public static final AdsDefinitionIcon REPORT_CHART_AREA = new AdsDefinitionIcon("ads/report/area.svg");
    public static final AdsDefinitionIcon REPORT_CHART_BAR3D = new AdsDefinitionIcon("ads/report/bar3D.svg");
    public static final AdsDefinitionIcon REPORT_CHART_LINE3D = new AdsDefinitionIcon("ads/report/line3D.svg");
    public static final AdsDefinitionIcon REPORT_CHART_AREA_STACKED = new AdsDefinitionIcon("ads/report/stackedArea.svg");
    public static final AdsDefinitionIcon REPORT_CHART_BAR_STACKED = new AdsDefinitionIcon("ads/report/stackedBar.svg");
    public static final AdsDefinitionIcon REPORT_CHART_XY_BAR = new AdsDefinitionIcon("ads/report/xy_bar.svg");
    public static final AdsDefinitionIcon REPORT_CHART_PIE = new AdsDefinitionIcon("ads/report/pie.svg");
    public static final AdsDefinitionIcon REPORT_CHART_PIE3D = new AdsDefinitionIcon("ads/report/pie3D.svg");
    public static final AdsDefinitionIcon REPORT_CHART_BAR3D_STACKED = new AdsDefinitionIcon("ads/report/stackedBar3D.svg");

    public static RadixIcon getForReportCellType(final EReportCellType cellType) {
        switch (cellType) {
            case PROPERTY:
                return REPORT_PROPERTY_CELL;
            case SPECIAL:
                return REPORT_SPECIAL_CELL;
            case SUMMARY:
                return REPORT_SUMMARY_CELL;
            case TEXT:
                return REPORT_TEXT_CELL;
            case EXPRESSION:
                return REPORT_EXPRESSION_CELL;
            case IMAGE:
                return REPORT_IMAGE_CELL;
            case DB_IMAGE:
                return REPORT_DB_IMAGE_CELL;
            case CHART:
                return REPORT_CHART_CELL;
            default:
                return RadixObjectIcon.UNKNOWN;
        }
    }

    public static RadixIcon getForReportChartSeriesType(final EReportChartSeriesType seriesType) {
        switch (seriesType) {
            case AREA:
            case XY_AREA:
                return REPORT_CHART_AREA;
            case AREA_STACKED:
            case XY_AREA_STACKED:
                return REPORT_CHART_AREA_STACKED;
            case BAR:
                return REPORT_CHART_BAR;
            case BAR_3D:
                return REPORT_CHART_BAR3D;
            case BAR_STACKED:
            case XY_BAR_STACKED:
                return REPORT_CHART_BAR_STACKED;
            case XY_BAR:
                return REPORT_CHART_XY_BAR;
            case BAR_STACKED_3D:
                return REPORT_CHART_BAR3D_STACKED;
            case LINE:
            case XY_LINE:
                return REPORT_CHART_LINE;
            case LINE_3D:
                return REPORT_CHART_LINE3D;
            case PIE:
                return REPORT_CHART_PIE;
            case PIE_3D:
                return REPORT_CHART_PIE3D;
            default:
                return REPORT_CHART_CELL;
        }
    }
    /**
     * Class presentations
     */
    public static final AdsDefinitionIcon LOCAL_VARIABLE_COMPLETION_ITEM = new AdsDefinitionIcon("ads/local_variable_completion_item.svg");
    public static final AdsDefinitionIcon PRESENTATION_SET = new AdsDefinitionIcon("ads/presentation_set.svg");
    public static final AdsDefinitionIcon EDITOR_PRESENTATION = new AdsDefinitionIcon("ads/editor_presentation.svg");
    public static final AdsDefinitionIcon SELECTOR_PRESENTATION = new AdsDefinitionIcon("ads/selector_presentation.svg");
    public static final AdsDefinitionIcon COMMAND = new AdsDefinitionIcon("ads/command.svg");
    public static final AdsDefinitionIcon FILTER = new AdsDefinitionIcon("ads/filter.svg");
    public static final AdsDefinitionIcon SORTING = new AdsDefinitionIcon("ads/sorting.svg");
    public static final AdsDefinitionIcon COLOR_SCHEME = new AdsDefinitionIcon("ads/color_scheme.svg");
    public static final AdsDefinitionIcon CLASS_CATALOG = new AdsDefinitionIcon("ads/class_catalog.svg");
    public static final AdsDefinitionIcon EDITOR_PAGE = new AdsDefinitionIcon("ads/editorpage/editor_page.svg");
    public static final AdsDefinitionIcon EDITOR_PAGE_CUSTOM = new AdsDefinitionIcon("ads/editorpage/editor_page_custom.svg");
    public static final AdsDefinitionIcon EDITOR_PAGE_PROP = new AdsDefinitionIcon("ads/editorpage/editor_page_prop.svg");
    public static final AdsDefinitionIcon OBJECT_TITLE_FORMAT = new AdsDefinitionIcon("ads/object_title_format.svg");
    /**
     * Explorer Items
     */
    public static final AdsDefinitionIcon CHILD_REF_EXPLORER_ITEM = new AdsDefinitionIcon("ads/exploreritem/child_ref.svg");
    public static final AdsDefinitionIcon PARENT_REF_EXPLORER_ITEM = new AdsDefinitionIcon("ads/exploreritem/parent_ref.svg");
    public static final AdsDefinitionIcon ENTITY_EXPLORER_ITEM = new AdsDefinitionIcon("ads/exploreritem/entity.svg");
    public static final AdsDefinitionIcon PARAGRAPH_LINK_EXPLORER_ITEM = new AdsDefinitionIcon("ads/exploreritem/paragraph_link.svg");
    public static final AdsDefinitionIcon PARAGRAPH_EXPLORER_ITEM = new AdsDefinitionIcon("ads/exploreritem/paragraph.svg");
    /**
     * DRC
     */
    public static final AdsDefinitionIcon ACCESS_AREA = new AdsDefinitionIcon("ads/access_area.svg");
    public static final AdsDefinitionIcon ACCESS_PARTITION = new AdsDefinitionIcon("ads/access_partition.svg");
    public static final AdsDefinitionIcon DOMAIN = new AdsDefinitionIcon("ads/domain.svg");
    // Custom
    public static final AdsDefinitionIcon CUSTOM_DIALOG = new AdsDefinitionIcon("ads/custom/custom_dialog.svg");
    public static final AdsDefinitionIcon CUSTOM_EDITOR = new AdsDefinitionIcon("ads/custom/custom_editor.svg");
    public static final AdsDefinitionIcon CUSTOM_SELECTOR = new AdsDefinitionIcon("ads/custom/custom_selector.svg");
    public static final AdsDefinitionIcon CUSTOM_PROP_EDITOR = new AdsDefinitionIcon("ads/custom/custom_prop_editor.svg");
    public static final AdsDefinitionIcon CUSTOM_PARAG_EDITOR = new AdsDefinitionIcon("ads/custom/custom_parag_editor.svg");
    public static final AdsDefinitionIcon CUSTOM_SIGNAL = new AdsDefinitionIcon("widgets/signals.svg");
    //
    public static final AdsDefinitionIcon MODULE_IMAGES = new AdsDefinitionIcon("ads/module_images.svg");

    private AdsDefinitionIcon(String uri) {
        super(uri);
    }

    private static class AccGroup {

        public final AdsDefinitionIcon accPublic;
        public final AdsDefinitionIcon accPrivate;
        public final AdsDefinitionIcon accProtected;
        public final AdsDefinitionIcon accInternal;
        public final AdsDefinitionIcon accPublished;

        public AccGroup(AdsDefinitionIcon public_, AdsDefinitionIcon private_, AdsDefinitionIcon protected_, AdsDefinitionIcon internal_, AdsDefinitionIcon published_) {
            this.accPublic = public_;
            this.accPrivate = private_;
            this.accProtected = protected_;
            this.accInternal = internal_;
            this.accPublished = published_;
        }
//        public AccGroup(AdsDefinitionIcon public_, AdsDefinitionIcon private_, AdsDefinitionIcon protected_) {
//            this(public_, private_, protected_, protected_, public_);
//        }
    }

    public static final class Method extends AdsDefinitionIcon {

        private Method(String uri) {
            super(uri);
        }
        public static final AdsDefinitionIcon CONSTRUCTOR = new Method("ads/method/constructor.svg");
        public static final AdsDefinitionIcon CONSTRUCTOR_PROTECTED = new Method("ads/method/constructor_protected.svg");
        public static final AdsDefinitionIcon CONSTRUCTOR_PRIVATE = new Method("ads/method/constructor_private.svg");
        public static final AdsDefinitionIcon CONSTRUCTOR_INTERNAL = new Method("ads/method/constructor_internal.svg");
        public static final AdsDefinitionIcon CONSTRUCTOR_PUBLISHED = new Method("ads/method/constructor_published.svg");
        public static final AdsDefinitionIcon METHOD = AdsDefinitionIcon.REPORT_EXPRESSION_CELL;
        public static final AdsDefinitionIcon METHOD_PROTECTED = new Method("ads/method/method_protected.svg");
        public static final AdsDefinitionIcon METHOD_PRIVATE = new Method("ads/method/method_private.svg");
        public static final AdsDefinitionIcon METHOD_INTERNAL = new Method("ads/method/method_internal.svg");
        public static final AdsDefinitionIcon METHOD_PUBLISHED = new Method("ads/method/method_published.svg");
        public static final AdsDefinitionIcon METHOD_STATIC = new Method("ads/method/method_static.svg");
        public static final AdsDefinitionIcon METHOD_STATIC_PROTECTED = new Method("ads/method/method_static_protected.svg");
        public static final AdsDefinitionIcon METHOD_STATIC_PRIVATE = new Method("ads/method/method_static_private.svg");
        public static final AdsDefinitionIcon METHOD_STATIC_INTERNAL = new Method("ads/method/method_static_internal.svg");
        public static final AdsDefinitionIcon METHOD_STATIC_PUBLISHED = new Method("ads/method/method_static_published.svg");
        public static final AdsDefinitionIcon METHOD_ABSTRACT = new Method("ads/method/method_abstract.svg");
        public static final AdsDefinitionIcon METHOD_ABSTRACT_PROTECTED = new Method("ads/method/method_abstract_protected.svg");
        public static final AdsDefinitionIcon METHOD_ABSTRACT_PRIVATE = new Method("ads/method/method_abstract_private.svg");
        public static final AdsDefinitionIcon METHOD_ABSTRACT_INTERNAL = new Method("ads/method/method_abstract_internal.svg");
        public static final AdsDefinitionIcon METHOD_ABSTRACT_PUBLISHED = new Method("ads/method/method_abstract_published.svg");
        public static final AdsDefinitionIcon METHOD_ABSTRACT_STATIC = new Method("ads/method/method_abstract_static.svg");
        public static final AdsDefinitionIcon METHOD_ABSTRACT_STATIC_PROTECTED = new Method("ads/method/method_abstract_static_protected.svg");
        public static final AdsDefinitionIcon METHOD_ABSTRACT_STATIC_PRIVATE = new Method("ads/method/method_abstract_static_private.svg");
        public static final AdsDefinitionIcon METHOD_ABSTRACT_STATIC_INTERNAL = new Method("ads/method/method_abstract_static_internal.svg");
        public static final AdsDefinitionIcon METHOD_ABSTRACT_STATIC_PUBLISHED = new Method("ads/method/method_abstract_static_published.svg");
        public static final AdsDefinitionIcon METHOD_COMMAND = new Method("ads/method/method_command.svg");
        public static final AdsDefinitionIcon METHOD_COMMAND_PROTECTED = new Method("ads/method/method_command_protected.svg");
        public static final AdsDefinitionIcon METHOD_COMMAND_PRIVATE = new Method("ads/method/method_command_private.svg");
        public static final AdsDefinitionIcon METHOD_COMMAND_INTERNAL = new Method("ads/method/method_command_internal.svg");
        public static final AdsDefinitionIcon METHOD_COMMAND_PUBLISHED = new Method("ads/method/method_command_published.svg");
        public static final AdsDefinitionIcon METHOD_SLOT = new Method("ads/method/method_slot.svg");
        public static final AdsDefinitionIcon METHOD_SLOT_PROTECTED = new Method("ads/method/method_slot_protected.svg");
        public static final AdsDefinitionIcon METHOD_SLOT_PRIVATE = new Method("ads/method/method_slot_private.svg");
        public static final AdsDefinitionIcon METHOD_SLOT_INTERNAL = new Method("ads/method/method_slot_internal.svg");
        public static final AdsDefinitionIcon METHOD_SLOT_PUBLISHED = new Method("ads/method/method_slot_published.svg");
        private static final AccGroup REGULAR_GROUP = new AccGroup(METHOD, METHOD_PRIVATE, METHOD_PROTECTED, METHOD_INTERNAL, METHOD_PUBLISHED);
        private static final AccGroup CONSTRUCTOR_GROUP = new AccGroup(CONSTRUCTOR, CONSTRUCTOR_PRIVATE, CONSTRUCTOR_PROTECTED, CONSTRUCTOR_INTERNAL, CONSTRUCTOR_PUBLISHED);
        private static final AccGroup ABSTRACT_GROUP = new AccGroup(METHOD_ABSTRACT, METHOD_ABSTRACT_PRIVATE, METHOD_ABSTRACT_PROTECTED, METHOD_ABSTRACT_INTERNAL, METHOD_ABSTRACT_PUBLISHED);
        private static final AccGroup STATIC_ABSTRACT_GROUP = new AccGroup(METHOD_ABSTRACT_STATIC, METHOD_ABSTRACT_STATIC_PRIVATE, METHOD_ABSTRACT_STATIC_PROTECTED, METHOD_ABSTRACT_STATIC_INTERNAL, METHOD_ABSTRACT_STATIC_PUBLISHED);
        private static final AccGroup COMMAND_GROUP = new AccGroup(METHOD_COMMAND, METHOD_COMMAND_PRIVATE, METHOD_COMMAND_PROTECTED, METHOD_COMMAND_INTERNAL, METHOD_COMMAND_PUBLISHED);
        private static final AccGroup SLOT_GROUP = new AccGroup(METHOD_SLOT, METHOD_SLOT_PRIVATE, METHOD_SLOT_PROTECTED, METHOD_SLOT_INTERNAL, METHOD_SLOT_PUBLISHED);
        private static final AccGroup STATIC_GROUP = new AccGroup(METHOD_STATIC, METHOD_STATIC_PRIVATE, METHOD_STATIC_PROTECTED, METHOD_STATIC_INTERNAL, METHOD_STATIC_PUBLISHED);

        public static AdsDefinitionIcon calcIcon(AdsMethodDef method) {
            if (method == null) {
                return METHOD;
            }
            AccGroup g = REGULAR_GROUP;
            final AdsAccessFlags acc = method.getProfile().getAccessFlags();
            final EAccess access = method.getAccessMode();

            if (method.isConstructor()) {
                g = CONSTRUCTOR_GROUP;
            } else if (method.getNature() == EMethodNature.COMMAND_HANDLER) {
                g = COMMAND_GROUP;
            } else if (method.getNature() == EMethodNature.PRESENTATION_SLOT) {
                g = SLOT_GROUP;
            } else {
                if (acc.isStatic()) {
                    if (acc.isAbstract()) {
                        g = STATIC_ABSTRACT_GROUP;
                    } else {
                        g = STATIC_GROUP;
                    }
                } else {
                    if (acc.isAbstract()) {
                        g = ABSTRACT_GROUP;

                    }
                }
            }
            switch (access) {
                case PROTECTED:
                    return g.accProtected;
                case PRIVATE:
                    return g.accPrivate;
                case DEFAULT:
                    return g.accInternal;
                default:
                    return g.accPublic;

            }
        }
    }

    public static final class Property extends AdsDefinitionIcon {

        private Property(String uri) {
            super(uri);
        }
        public static final Property PROPERTY_DYNAMIC_ABSTRACT = new Property("ads/property/property_dynamic_abstract.svg");
        public static final Property PROPERTY_DYNAMIC_ABSTRACT_PROTECTED = new Property("ads/property/property_dynamic_abstract_protected.svg");
        public static final Property PROPERTY_DYNAMIC_ABSTRACT_INTERNAL = new Property("ads/property/property_dynamic_abstract_internal.svg");
        public static final Property PROPERTY_DYNAMIC_ABSTRACT_PUBLISHED = new Property("ads/property/property_dynamic_abstract_published.svg");
        public static final Property PROPERTY_DYNAMIC = new Property("ads/property/property_dynamic.svg");
        public static final Property PROPERTY_DYNAMIC_PROTECTED = new Property("ads/property/property_dynamic_protected.svg");
        public static final Property PROPERTY_DYNAMIC_PRIVATE = new Property("ads/property/property_dynamic_private.svg");
        public static final Property PROPERTY_DYNAMIC_INTERNAL = new Property("ads/property/property_dynamic_internal.svg");
        public static final Property PROPERTY_DYNAMIC_PUBLISHED = new Property("ads/property/property_dynamic_published.svg");
        public static final Property PROPERTY_DYNAMIC_STATIC = new Property("ads/property/property_dynamic_static.svg");
        public static final Property PROPERTY_DYNAMIC_STATIC_PROTECTED = new Property("ads/property/property_dynamic_static_protected.svg");
        public static final Property PROPERTY_DYNAMIC_STATIC_PRIVATE = new Property("ads/property/property_dynamic_static_private.svg");
        public static final Property PROPERTY_DYNAMIC_STATIC_INTERNAL = new Property("ads/property/property_dynamic_static_internal.svg");
        public static final Property PROPERTY_DYNAMIC_STATIC_PUBLISHED = new Property("ads/property/property_dynamic_static_published.svg");
        public static final Property PROPERTY_INNATE = new Property("ads/property/property_innate.svg");
        public static final Property PROPERTY_INNATE_PROTECTED = new Property("ads/property/property_innate_protected.svg");
        public static final Property PROPERTY_INNATE_PRIVATE = new Property("ads/property/property_innate_private.svg");
        public static final Property PROPERTY_INNATE_INTERNAL = new Property("ads/property/property_innate_internal.svg");
        public static final Property PROPERTY_INNATE_PUBLISHED = new Property("ads/property/property_innate_published.svg");
        public static final Property PROPERTY_USER = new Property("ads/property/property_user.svg");
        public static final Property PROPERTY_USER_PROTECTED = new Property("ads/property/property_user_protected.svg");
        public static final Property PROPERTY_USER_PRIVATE = new Property("ads/property/property_user_private.svg");
        public static final Property PROPERTY_USER_INTERNAL = new Property("ads/property/property_user_internal.svg");
        public static final Property PROPERTY_USER_PUBLISHED = new Property("ads/property/property_user_published.svg");
        private static final AccGroup DYNAMIC = new AccGroup(PROPERTY_DYNAMIC, PROPERTY_DYNAMIC_PRIVATE, PROPERTY_DYNAMIC_PROTECTED, PROPERTY_DYNAMIC_INTERNAL, PROPERTY_DYNAMIC_PUBLISHED);
        private static final AccGroup STATIC = new AccGroup(PROPERTY_DYNAMIC_STATIC, PROPERTY_DYNAMIC_STATIC_PRIVATE, PROPERTY_DYNAMIC_STATIC_PROTECTED, PROPERTY_DYNAMIC_STATIC_INTERNAL, PROPERTY_DYNAMIC_STATIC_PUBLISHED);
        private static final AccGroup INNATE = new AccGroup(PROPERTY_INNATE, PROPERTY_INNATE_PRIVATE, PROPERTY_INNATE_PROTECTED, PROPERTY_INNATE_INTERNAL, PROPERTY_INNATE_PUBLISHED);
        private static final AccGroup USER = new AccGroup(PROPERTY_USER, PROPERTY_USER_PRIVATE, PROPERTY_USER_PROTECTED, PROPERTY_USER_INTERNAL, PROPERTY_USER_PUBLISHED);
        private static final AccGroup ABSTRACT = new AccGroup(
                PROPERTY_DYNAMIC_ABSTRACT,
                PROPERTY_DYNAMIC_ABSTRACT,
                PROPERTY_DYNAMIC_ABSTRACT_PROTECTED,
                PROPERTY_DYNAMIC_ABSTRACT_INTERNAL,
                PROPERTY_DYNAMIC_ABSTRACT_PUBLISHED);

        public static AdsDefinitionIcon calcIcon(AdsPropertyDef prop) {
            AccGroup g = DYNAMIC;
            if (prop != null) {
                final AdsAccessFlags acc = prop.getAccessFlags();
                switch (prop.getNature()) {
                    case DYNAMIC:
                        if (acc.isStatic()) {
                            g = STATIC;
                        } else if (acc.isAbstract()) {
                            g = ABSTRACT;
                        }
                        break;
                    case USER:
                        g = USER;
                        break;
                    default:
                        g = INNATE;
                }
                final EAccess access = prop.getAccessMode();
                switch (access) {
                    case PROTECTED:
                        return g.accProtected;
                    case PRIVATE:
                        return g.accPrivate;
                    case DEFAULT:
                        return g.accInternal;
                    default:
                        return g.accPublic;

                }
            } else {
                return g.accPublic;
            }
        }
    }

    public static class WORKFLOW extends AdsDefinitionIcon {

        public static final WORKFLOW NOTE = new WORKFLOW("workflow/palette/note.svg");
        public static final WORKFLOW WAIT = new WORKFLOW("workflow/palette/wait.svg");
        public static final WORKFLOW EMPTY = new WORKFLOW("workflow/palette/empty.svg");
        public static final WORKFLOW FORK = new WORKFLOW("workflow/palette/fork.svg");
        public static final WORKFLOW MERGE = new WORKFLOW("workflow/palette/merge.svg");
        public static final WORKFLOW SCOPE = new WORKFLOW("workflow/palette/scope.svg");
        public static final WORKFLOW VAR = new WORKFLOW("workflow/palette/var.svg");
        public static final WORKFLOW CATCH = new WORKFLOW("workflow/palette/catch.svg");
        public static final WORKFLOW FINISH = new WORKFLOW("workflow/palette/finish.svg");
        public static final WORKFLOW PROGRAM = new WORKFLOW("workflow/palette/program.svg");
        public static final WORKFLOW RETURN = new WORKFLOW("workflow/palette/return.svg");
        public static final WORKFLOW START = new WORKFLOW("workflow/palette/start.svg");
        public static final WORKFLOW TERM = new WORKFLOW("workflow/palette/term.svg");
        public static final WORKFLOW THROW = new WORKFLOW("workflow/palette/throw.svg");
        public static final WORKFLOW INCLUDE = new WORKFLOW("workflow/palette/include.svg");
        public static final WORKFLOW DOC_MANAGER_CREATOR = new WORKFLOW("workflow/app/docmanagercreator.svg");
        public static final WORKFLOW PERSONAL_COMMUNICATOR = new WORKFLOW("workflow/app/personalcommunicator.svg");
        public static final WORKFLOW BATCH_EXECUTOR = new WORKFLOW("workflow/app/batchexecutor.svg");
        public static final WORKFLOW EDITOR_FORM_CREATOR = new WORKFLOW("workflow/app/editorformcreator.svg");
        public static final WORKFLOW SELECTOR_FORM_CREATOR = new WORKFLOW("workflow/app/selectorformcreator.svg");
        public static final WORKFLOW REPORT_GENERATOR = new WORKFLOW("workflow/app/reportgenerator.svg");
        public static final WORKFLOW DIALOG_CREATOR = new WORKFLOW("workflow/app/dialogcreator.svg");
        public static final WORKFLOW DIALOG_DUPLICATOR = new WORKFLOW("workflow/app/dialogduplicator.svg");
        public static final WORKFLOW NETPORT = new WORKFLOW("workflow/app/netport.svg");

        private WORKFLOW(String uri) {
            super(uri);
        }
        private static final Map<String, RadixIcon> clazz2Icon = new HashMap<>();

        public static void registerIcon(String clazz, RadixIcon icon) {
            clazz2Icon.put(clazz, icon);
        }

        public static RadixIcon calcIcon(String clazz) {
            final RadixIcon icon = clazz2Icon.get(clazz);
            if (icon == null) {
                return RadixObjectIcon.UNKNOWN;
            }
            return icon;
        }

        public static RadixIcon calcIcon(Kind kind) {
            switch (kind) {
                case START:
                    return START;
                case PROGRAM:
                    return PROGRAM;
                case SCOPE:
                    return SCOPE;
                case INCLUDE:
                    return INCLUDE;
                case FINISH:
                    return FINISH;
                case RETURN:
                    return RETURN;
                case TERMINATE:
                    return TERM;
                case THROW:
                    return THROW;
                case CATCH:
                    return CATCH;
                case FORK:
                    return FORK;
                case MERGE:
                    return MERGE;
                case EMPTY:
                    return EMPTY;
                case NOTE:
                    return NOTE;
                case VAR:
                    return VAR;
            }
            return RadixObjectIcon.UNKNOWN;
        }
    }
//edit icons

    public static class WIDGETS extends AdsDefinitionIcon {

        //public static final WIDGETS SNAP = new WIDGETS("widgets/snap.svg");
        public static final WIDGETS BUTTON_BOX = new WIDGETS("widgets/button_box.svg");
        public static final WIDGETS CHECK_BOX = new WIDGETS("widgets/check_box.svg");
        public static final WIDGETS DATE_EDIT = new WIDGETS("widgets/date_edit.svg");
        public static final WIDGETS TIME_EDIT = new WIDGETS("widgets/time_edit.svg");
        public static final WIDGETS DATE_TIME_EDIT = new WIDGETS("widgets/date_time_edit.svg");
        public static final WIDGETS GRID_LAYOUT = new WIDGETS("widgets/grid_layout.svg");
        public static final WIDGETS GROUP_BOX = new WIDGETS("widgets/group_box.svg");
        public static final WIDGETS HORIZONTAL_LAYOUT = new WIDGETS("widgets/horizontal_layout.svg");
        public static final WIDGETS HORIZONTAL_SPACER = new WIDGETS("widgets/horizontal_spacer.svg");
        public static final WIDGETS LABEL = new WIDGETS("widgets/label.svg");
        public static final WIDGETS SPLITTER = new WIDGETS("widgets/splitter.svg");
        public static final WIDGETS ADVANCED_SPLITTER = new WIDGETS("widgets/advanced_splitter.svg");
        public static final WIDGETS FRAME = new WIDGETS("widgets/frame.svg");
        public static final WIDGETS ACTION = new WIDGETS("widgets/action.svg");
        public static final WIDGETS SCROLL_AREA = new WIDGETS("widgets/scrollarea.svg");
        public static final WIDGETS STACKED_WIDGET = new WIDGETS("widgets/page_set.svg");
        public static final WIDGETS COMMAND_TOOL_BUTTON = new WIDGETS("widgets/page_set.svg");
        public static final WIDGETS VAL_BIN_EDITOR = new WIDGETS("widgets/val_edit.svg");
        public static final WIDGETS VAL_BOOL_EDITOR = new WIDGETS("widgets/val_edit.svg");
        public static final WIDGETS VAL_CHAR_EDITOR = new WIDGETS("widgets/val_edit.svg");
        public static final WIDGETS VAL_STR_EDITOR = new WIDGETS("widgets/val_edit.svg");
        public static final WIDGETS VAL_INT_EDITOR = new WIDGETS("widgets/val_edit.svg");
        public static final WIDGETS PROGRESS_BAR = new WIDGETS("widgets/progress_bar.svg");
        public static final WIDGETS LINE_EDIT = new WIDGETS("widgets/line_edit.svg");
        public static final WIDGETS COMBO_BOX = new WIDGETS("widgets/combo_box.svg");
        public static final WIDGETS LIST = new WIDGETS("widgets/list.svg");
        public static final WIDGETS PUSH_BUTTON = new WIDGETS("widgets/push_button.svg");
        public static final WIDGETS RADIO_BUTTON = new WIDGETS("widgets/radio_button.svg");
        public static final WIDGETS SPIN_BOX = new WIDGETS("widgets/spin_box.svg");
        public static final WIDGETS DOUBLE_SPIN_BOX = new WIDGETS("widgets/double_spin_box.svg");
        public static final WIDGETS TAB = new WIDGETS("widgets/tab.svg");
        public static final WIDGETS TABLE = new WIDGETS("widgets/table.svg");
        // public static final WIDGETS GRID_BOX_CONTAINER = new WIDGETS("widgets/table.svg");
        public static final WIDGETS TAB_SET = new WIDGETS("widgets/tab_set.svg");
        public static final WIDGETS TEXT_EDIT = new WIDGETS("widgets/text_edit.svg");
        public static final WIDGETS TOOL_BAR = new WIDGETS("widgets/tool_bar.svg");
        public static final WIDGETS TOOL_BUTTON = new WIDGETS("widgets/tool_button.svg");
        public static final WIDGETS TREE = new WIDGETS("widgets/tree.svg");
        public static final WIDGETS VERTICAL_LAYOUT = new WIDGETS("widgets/vertical_layout.svg");
        public static final WIDGETS VERTICAL_SPACER = new WIDGETS("widgets/vertical_spacer.svg");
        public static final WIDGETS PROP_LABEL = new WIDGETS("widgets/prop_label.svg");
        public static final WIDGETS PROP_EDITOR = new WIDGETS("widgets/prop_editor.svg");
        public static final WIDGETS COMMAND_PUSH_BUTTON_ITEM = new WIDGETS("widgets/command_push_button.svg");
        public static final WIDGETS EDITOR_PAGE_ITEM = new WIDGETS("widgets/editor_page.svg");
        public static final WIDGETS EMBEDDED_EDITOR_ITEM = new WIDGETS("widgets/embedded_editor.svg");
        public static final WIDGETS EMBEDDED_SELECTOR_ITEM = new WIDGETS("widgets/embedded_selector.svg");

        private WIDGETS(String uri) {
            super(uri);
        }

        public static RadixIcon calcIcon(String clazz) {
            switch (clazz) {
                case AdsMetaInfo.WIDGET_CLASS:
                case AdsMetaInfo.RWT_UI_CONTAINER:
                    return TAB;
                case AdsMetaInfo.DIALOG_BUTTON_BOX_CLASS:
                    return BUTTON_BOX;
                case AdsMetaInfo.CHECK_BOX_CLASS:
                case AdsMetaInfo.RWT_UI_CHECK_BOX:
                    return CHECK_BOX;
                case AdsMetaInfo.RWT_UI_RADIO_BUTTON:
                    return RADIO_BUTTON;
                case AdsMetaInfo.DATE_EDIT_CLASS:
                    return DATE_EDIT;
                case AdsMetaInfo.TIME_EDIT_CLASS:
                    return TIME_EDIT;
                case AdsMetaInfo.DATE_TIME_EDIT_CLASS:
                    return DATE_TIME_EDIT;
                case AdsMetaInfo.GRID_LAYOUT_CLASS:
                case AdsMetaInfo.RWT_LABELED_EDIT_GRID:
                case AdsMetaInfo.RWT_PROPERTIES_GRID:
                    return GRID_LAYOUT;
                case AdsMetaInfo.GROUP_BOX_CLASS:
                case AdsMetaInfo.RWT_UI_GROUP_BOX:
                    return GROUP_BOX;
                case AdsMetaInfo.HORIZONTAL_LAYOUT_CLASS:
                case AdsMetaInfo.RWT_HORIZONTAL_BOX_CONTAINER:
                    return HORIZONTAL_LAYOUT;
                case AdsMetaInfo.SPACER_CLASS:
                    return HORIZONTAL_SPACER;
                case AdsMetaInfo.LABEL_CLASS:
                case AdsMetaInfo.RWT_UI_LABEL:
                    return LABEL;
                case AdsMetaInfo.SPLITTER_CLASS:
                case AdsMetaInfo.RWT_SPLITTER:
                    return SPLITTER;
                case AdsMetaInfo.ADVANCED_SPLITTER_CLASS:
                    return ADVANCED_SPLITTER;
                case AdsMetaInfo.FRAME_CLASS:
                    return FRAME;
                case AdsMetaInfo.ACTION_CLASS:
                    return ACTION;
                case AdsMetaInfo.SCROLL_AREA_CLASS:
                    return SCROLL_AREA;
                case AdsMetaInfo.STACKED_WIDGET_CLASS:
                    return STACKED_WIDGET;
                case AdsMetaInfo.PROGRESS_BAR_CLASS:
                    return PROGRESS_BAR;
                case AdsMetaInfo.LINE_EDIT_CLASS:
                case AdsMetaInfo.RWT_UI_TEXT_FIELD:
                    return LINE_EDIT;
                case AdsMetaInfo.COMBO_BOX_CLASS:
                case AdsMetaInfo.RWT_UI_INPUT_BOX:
                    return COMBO_BOX;
                case AdsMetaInfo.LIST_WIDGET_CLASS:
                case AdsMetaInfo.LIST_VIEW_CLASS:
                case AdsMetaInfo.RWT_UI_LIST:
                    return LIST;
                case AdsMetaInfo.PUSH_BUTTON_CLASS:
                case AdsMetaInfo.RWT_UI_PUSH_BUTTON:
                    return PUSH_BUTTON;
                case AdsMetaInfo.RADIO_BUTTON_CLASS:
                    return RADIO_BUTTON;
                case AdsMetaInfo.SPIN_BOX_CLASS:
                    return SPIN_BOX;
                case AdsMetaInfo.DOUBLE_SPIN_BOX_CLASS:
                    return DOUBLE_SPIN_BOX;
                case AdsMetaInfo.TAB_WIDGET_CLASS:
                case AdsMetaInfo.RWT_TAB_SET:
                    return TAB_SET;
                case AdsMetaInfo.RWT_UI_GRID_BOX_CONTAINER:
                case AdsMetaInfo.TABLE_WIDGET_CLASS:
                case AdsMetaInfo.TABLE_VIEW_CLASS:
                case AdsMetaInfo.RWT_UI_GRID:
                    return TABLE;
                case AdsMetaInfo.TEXT_EDIT_CLASS:
                case AdsMetaInfo.RWT_UI_TEXT_AREA:
                    return TEXT_EDIT;
                case AdsMetaInfo.TOOL_BAR_CLASS:
                    return TOOL_BAR;
                case AdsMetaInfo.TOOL_BUTTON_CLASS:
                    return TOOL_BUTTON;
                case AdsMetaInfo.TREE_WIDGET_CLASS:
                case AdsMetaInfo.TREE_VIEW_CLASS:
                case AdsMetaInfo.RWT_UI_TREE:
                    return TREE;
                case AdsMetaInfo.VERTICAL_LAYOUT_CLASS:
                case AdsMetaInfo.RWT_VERTICAL_BOX_CONTAINER:
                    return VERTICAL_LAYOUT;
                case AdsMetaInfo.PROP_LABEL_CLASS:
                case AdsMetaInfo.RWT_PROP_LABEL:
                    return PROP_LABEL;
                case AdsMetaInfo.PROP_EDITOR_CLASS:
                case AdsMetaInfo.RWT_PROP_EDITOR:
                    return PROP_EDITOR;
                case AdsMetaInfo.COMMAND_PUSH_BUTTON_CLASS:
                case AdsMetaInfo.RWT_COMMAND_PUSH_BUTTON:
                    return COMMAND_PUSH_BUTTON_ITEM;
                case AdsMetaInfo.RWT_EDITOR_PAGE:
                case AdsMetaInfo.EDITOR_PAGE_CLASS:
                    return EDITOR_PAGE_ITEM;
                case AdsMetaInfo.EMBEDDED_EDITOR_CLASS:
                case AdsMetaInfo.RWT_EMBEDDED_EDITOR:
                    return EMBEDDED_EDITOR_ITEM;
                case AdsMetaInfo.EMBEDDED_SELECTOR_CLASS:
                case AdsMetaInfo.RWT_EMBEDDED_SELECTOR:
                    return EMBEDDED_SELECTOR_ITEM;
                case AdsMetaInfo.VAL_BIN_EDITOR_CLASS:
                    return VAL_BIN_EDITOR;
                case AdsMetaInfo.VAL_BOOL_EDITOR_CLASS:
                    return VAL_BOOL_EDITOR;
                case AdsMetaInfo.VAL_CHAR_EDITOR_CLASS:
                    return VAL_CHAR_EDITOR;
                case AdsMetaInfo.VAL_FILE_PATH_EDITOR_CLASS:
                case AdsMetaInfo.VAL_STR_EDITOR_CLASS:
                    return VAL_STR_EDITOR;
                case AdsMetaInfo.VAL_INT_EDITOR_CLASS:
                    return VAL_INT_EDITOR;
                case AdsMetaInfo.VAL_CONST_SET_EDITOR_CLASS:
                    return VAL_INT_EDITOR;
                case AdsMetaInfo.VAL_LIST_EDITOR_CLASS:
                    return VAL_INT_EDITOR;
                case AdsMetaInfo.VAL_DATE_TIME_EDITOR_CLASS:
                    return VAL_INT_EDITOR;
                case AdsMetaInfo.VAL_NUM_EDITOR_CLASS:
                    return VAL_INT_EDITOR;
                case AdsMetaInfo.VAL_TIMEINTERVAL_EDITOR_CLASS:
                    return VAL_INT_EDITOR;
                case AdsMetaInfo.VAL_REF_EDITOR_CLASS:
                    return VAL_INT_EDITOR;


            }
            return AdsDefinitionIcon.CLASS_FORM_HANDLER;
        }
    }
}
