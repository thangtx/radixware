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

import java.util.List;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.Id;


public enum EDefType implements IKernelIntEnum {

    PARAGRAPH(1),
    CUSTOM_EDITOR(3),
    CUSTOM_SELECTOR(4),
    CUSTOM_PROP_EDITOR(5),
    CUSTOM_PARAG_EDITOR(6),
    ENUMERATION(7),
    CUSTOM_DIALOG(8),
    CONTEXTLESS_COMMAND(9),
    CLASS(11),
    XML_SCHEME(12),
    LOCALIZING_BUNDLE(13),
    CUSTOM_FORM_EDITOR(14),

    MSDL_SCHEME(17),
    ROLE(16),
    CUSTOM_PAGE_EDITOR(18),
    MODULE(19),
    EXPLORER_ITEM(20),
    PARAGRAPH_LINK(21),
    ENUM_ITEM(22),
    SCOPE_COMMAND(23),
    STMT_PARAM(24),
    CLASS_PROPERTY(25),
    FILTER(26),
    SORTING(27),
    COLOR_SCHEME(28),
    CLASS_CATALOG(29),
    EDITOR_PAGE(30),
    MULTILINGUAL_STRING(31),
    CLASS_METHOD(32),
    DOMAIN(33),
    ALGO_NODE(34),
    CLASS_CATALOG_TOPIC(35),
    EDITOR_PRESENTATION(36),
    SELECTOR_PRESENTATION(37),
    WIDGET(38),
    TITLE_FORMAT(39),
    ACTION(40),
    FILTER_PARAM(41),
    METHOD_GROUP(42),
    PROPERTY_GROUP(43),
    APP_NODE_PROP(44),
    INCLUDE_NODE_PARAM(45),
    ALGO_VAR(46),
    ALGO_PARAM(47),
    ALGO_PAGE(48),
    ALGO_EDGE(49),
    ALGO_PIN(50),
    USER_FUNC(52),
    CUSTOM_WIDGET_DEF(53),
    SIGNAL(54),
    MULTILINGUAL_EVENT_CODE(55),
    IMAGE(56),
    PHRASE_BOOK(63),
    XSLT(64),
    CUSTOM_REPORT_EDITOR(65),
    CUSTOM_FILTER_DIALOG(66),
    RPC_COMMAND(67),
    DATA_SEGMENT(70),
    
    ENUM_CLASS_PARAM(99),
    ENUM_CLASS_FIELD(100),
    GENERIC(100001);
    
    
    /***/
    private final Long val;

    private EDefType(long val) {
        this.val = Long.valueOf(val);

    }

    @Override
    public Long getValue() {
        return val;
    }

    @Override
    public String getName() {
        return this.name();
    }

    public static EDefType getForValue(final Long val) {
        for (EDefType e : EDefType.values()) {
            if (e.val.equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EDefType has no item with value: " + String.valueOf(val),val);
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
}
