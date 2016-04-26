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
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;


public enum EHttpParameter implements IKernelStrEnum {

    HTTP_REQ_METHOD_PARAM("REQ_METHOD"), //pseudo attribute: "POST", "GET", default: "POST"
    HTTP_REQ_PATH_PARAM("REQ_PATH"), //pseudo attribute, default: "/"
    HTTP_RESP_STATUS_PARAM("RESP_STATUS"), //pseudo attribute: "200", "500" ...
    HTTP_RESP_REASON_PARAM("RESP_REASON"), //pseudo attribute: "Internal Server Error" ...
    @Deprecated
    HTTP_CLOSE_PARAM("Close"),
    HTTP_METHOD_POST("POST"),
    HTTP_METHOD_GET("GET"),
    HTTP_HOST_ATTR("host"),
    HTTP_CACHE_CONTROL_ATTR("cache-control"),
    HTTP_CONNECTION_ATTR("connection"),
    HTTP_CONTENT_TYPE_ATTR("content-type"),
    HTTP_SOAPACTION_ATTR("soapaction"),
    HTTP_USER_AGENT_ATTR("user-agent"),
    HTTP_ACCEPT_ATTR("accept"),
    HTTP_PRAGMA("pragma");
    private final String value;

    private EHttpParameter(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getName() {
        return null;
    }

    public static EHttpParameter getForValue(final String val) {
        for (EHttpParameter e : EHttpParameter.values()) {
            if (e.getValue().equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EHttpParameter has no item with value: " + String.valueOf(val), val);
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
