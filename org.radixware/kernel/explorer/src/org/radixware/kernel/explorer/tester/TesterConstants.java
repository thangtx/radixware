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

package org.radixware.kernel.explorer.tester;

import org.radixware.kernel.explorer.env.Application;


public enum TesterConstants {

    TEST_OPENING(Application.translate("TesterDialog", "Opening view")),
    TEST_INSERTIONS(Application.translate("TesterDialog", "Insertion")),
    TEST_FILTERS(Application.translate("TesterDialog", "Filters test")),
    TEST_PAGE(Application.translate("TesterDialog", "Editor page test")),
    TEST_CLOSING(Application.translate("TesterDialog", "Closing view")),
    TEST_CUSTOM(Application.translate("TesterDialog", "Custom test")),
    TEST_PROP_DIALOG(Application.translate("TesterDialog", "Properties dialogs test")),
    TEST_SINGLE_ENTITY_PROPS(Application.translate("TesterDialog", "Single entity properties test")),
    TEST_CREATION_DIALOG(Application.translate("TesterDialog", "Creation dialog test")),
    TEST_CREATION_SELECT_DIALOG(Application.translate("TesterDialog", "Multiple creation dialogs test")),

    RESULT_OPENING_SCS(Application.translate("TesterDialog", "Opened")),
    RESULT_CLOSING_SCS(Application.translate("TesterDialog", "Closed")),
    RESULT_CLOSING_FAIL(Application.translate("TesterDialog", "Closing failed")),
    RESULT_FILTERS_SCS(Application.translate("TesterDialog", "Filter has been applied")),
    RESULT_FILTERS_NO_APPLICABLE(Application.translate("TesterDialog", "No applicable filters")),
    RESULT_FILTERS_NO_AVAILABLE(Application.translate("TesterDialog", "No available filters")),
    RESULT_INSERTION_SCS(Application.translate("TesterDialog", "Insertion(s) have been done")),
    RESULT_EMPTY_SELECTOR(Application.translate("TesterDialog", "Empty selector")),
    RESULT_INSERTION_RESTRICTED(Application.translate("TesterDialog", "Insertion restricted for all entities")),
    RESULT_INSERTION_RECURSIVE(Application.translate("TesterDialog", "Contains recursive entities")),
    RESULT_PAGE_SCS(Application.translate("TesterDialog", "Page has been focused")),
    RESULT_PAGE_INVISIBLE(Application.translate("TesterDialog", "Page is invisible")),
    RESULT_PAGE_DISABLED(Application.translate("TesterDialog", "Page is disabled")),
    RESULT_CUSTOM_SCS(Application.translate("TesterDialog", "Test passed")),

    RESULT_PROPDIALOG_SCS(Application.translate("TesterDialog", "Dialog test passed")),
    RESULT_PROPDIALOG_FAIL(Application.translate("TesterDialog", "Dialog test failed")),
    RESULT_PROPDIALOG_NOPROPS(Application.translate("TesterDialog", "No available dialogs for test")),

    RESULT_CREATIONDIALOG_NOGROUPMODEL(Application.translate("TesterDialog", "Group model wasn't found for this selector")),
    RESULT_CREATIONDIALOG_RESTRICTED(Application.translate("TesterDialog", "Creation is restricted")),
    RESULT_CREATIONDIALOG_NODIALOG(Application.translate("TesterDialog", "Creation dialog wasn't identified")),
    RESULT_CREATIONDIALOG_CUSTOM_OPENED(Application.translate("TesterDialog", "Custom creation dialog was opened")),
    RESULT_CREATIONDIALOG_NOENTITYMODEL(Application.translate("TesterDialog", "Entity model of this class wasn't created")),

    RESULT_FAIL_EXCEPTION(Application.translate("TesterDialog", "Exceptions occurred")),
    RESULT_FAIL_INTERRUPTED(Application.translate("TesterDialog", "Interrupted")),
    RESULT_FAIL_ALREADY_OPENED(Application.translate("TesterDialog", "View was opened already")),
    RESULT_FAIL_TIME_LIMIT(Application.translate("TesterDialog", "Time limit overflow")),

    OBJ_EDITOR(Application.translate("TesterDialog", "Editor")),
    OBJ_SELECTOR(Application.translate("TesterDialog", "Selector")),
    OBJ_PARAGRAPH(Application.translate("TesterDialog", "Paragraph")),
    OBJ_PAGE(Application.translate("TesterDialog", "Editor page")),
    OBJ_ALL_PAGES(Application.translate("TesterDialog", "Pages")),
    OBJ_UNDEFINED(Application.translate("TesterDialog", "<Not Defined>")),
    OBJ_FILTER(Application.translate("TesterDialog", "Filter")),
    OBJ_SELECTOR_CREATIONDIALOG(Application.translate("TesterDialog", "Creation dialog")),

    WARNING_NO_OPTIONS(Application.translate("TesterDialog", "You must set tests options first")),

    RESUL_FILTER_ALL(Application.translate("TesterDialog", "All")),
    RESUL_FILTER_ERRORS(Application.translate("TesterDialog", "Errors")),
    RESUL_FILTER_WARNINGS(Application.translate("TesterDialog", "Warnings")),
    RESUL_FILTER_PASSED(Application.translate("TesterDialog", "Passed"));

    private final String title;
    
    TesterConstants(final String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }
}
