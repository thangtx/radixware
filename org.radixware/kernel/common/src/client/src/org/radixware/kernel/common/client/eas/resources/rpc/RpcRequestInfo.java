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

package org.radixware.kernel.common.client.eas.resources.rpc;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.env.DefManager;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;


class RpcRequestInfo {

    private String className = "";
    private String methodName = "";
    private Long instanceIndex = null;

    public RpcRequestInfo(final String requestAsStr, final DefManager defManager) {
        final int lastPointPos = requestAsStr.lastIndexOf(".");
        if (lastPointPos > 0) {
            methodName = requestAsStr.substring(lastPointPos + 1);
            final int indexPos = requestAsStr.lastIndexOf("#");
            if (indexPos > 0 && indexPos < lastPointPos) {
                className = requestAsStr.substring(0, indexPos);
                try {
                    instanceIndex = Long.parseLong(requestAsStr.substring(indexPos + 1, lastPointPos), 16);
                } catch (NumberFormatException exception) {
                    Logger.getLogger(RpcRequestInfo.class.getName()).log(Level.FINEST, "Error on extraction of instance index from request string " + requestAsStr, exception);
                }
            } else {
                className = requestAsStr.substring(0, lastPointPos);
            }
        } else {
            className = requestAsStr;
        }
        if (className.length() == 29) {
            //possibly dynamic class id
            final Id classId = Id.Factory.loadFrom(className);
            try {
                className = defManager.getDynamicClassById(classId).getName();
            } catch (DefinitionError error) {
                Logger.getLogger(RpcRequestInfo.class.getName()).log(Level.FINEST, "An attempt to get name of non-existing dynamic class", error);
            }
        }
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public Long getInstanceIndex() {
        return instanceIndex;
    }

    public String getInstanceKey() {
        return instanceIndex == null ? getClassName() : getClassName() + "#" + instanceIndex.toString();
    }
}
