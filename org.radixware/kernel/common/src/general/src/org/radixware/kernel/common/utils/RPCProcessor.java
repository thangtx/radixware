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

package org.radixware.kernel.common.utils;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.schemas.utils.RPCRequestDocument;
import org.radixware.schemas.utils.RPCRequestType;
import org.radixware.schemas.utils.RPCResponseDocument;
import org.radixware.schemas.utils.RPCResponseType;


public class RPCProcessor {

    public static abstract class ClientSideInvocation {

        private List<ArgumentInfo> arguments;

        public ClientSideInvocation(List<ArgumentInfo> arguments) {
            this.arguments = arguments;
        }

        public Object invoke() throws ServiceClientException, InterruptedException {
            RPCResponseDocument response = invokeImpl(RPCProcessor.createRPCRequest(arguments));
            ArgumentInfo info = RPCProcessor.parseRPCResponse(response);
            if (info == null) {
                return null;
            } else {
                return info.value;
            }
        }

        protected abstract RPCResponseDocument invokeImpl(RPCRequestDocument rq) throws ServiceClientException, InterruptedException;
    }

    public static abstract class ServerSideInvocation {

        private final RPCRequestDocument request;

        public ServerSideInvocation(RPCRequestDocument request) {
            this.request = request;
        }

        public RPCResponseDocument invoke() {
            List<ArgumentInfo> argumentInfos = RPCProcessor.parseRPCRequest(request);
            Object[] arguments;
            if (argumentInfos != null) {
                arguments = new Object[argumentInfos.size()];
                for (int i = 0; i < arguments.length; i++) {
                    arguments[i] = argumentInfos.get(i).value;
                }
            } else {
                arguments = new Object[0];
            }
            Object result = invokeImpl(arguments);
            EValType returnType = getReturnType();
            if (returnType != null) {
                return RPCProcessor.createRPCResponse(new ArgumentInfo(returnType, result));
            } else {
                return RPCProcessor.createRPCResponse(null);
            }

        }

        protected abstract EValType getReturnType();

        protected abstract Object invokeImpl(Object[] arguments);
    }
    public static final String RPC_REQUEST_TYPE_NAME = RPCRequestDocument.class.getName();
    public static final String RPC_RESPONSE_TYPE_NAME = RPCResponseDocument.class.getName();

    public static class ArgumentInfo {

        EValType valType;
        Object value;

        public ArgumentInfo(EValType valType, Object value) {
            this.valType = valType;
            this.value = value;
        }
    }

    public static RPCRequestDocument createRPCRequest(List<ArgumentInfo> argumentsAsStr) {
        RPCRequestDocument xDoc = RPCRequestDocument.Factory.newInstance();
        if (argumentsAsStr != null) {
            RPCRequestType rq = xDoc.addNewRPCRequest();
            for (ArgumentInfo vals : argumentsAsStr) {
                RPCRequestType.Arg xArg = rq.addNewArg();
                xArg.setType(vals.valType);
                if (vals.value != null) {
                    xArg.setValAsStr(ValAsStr.toStr(vals.value, vals.valType));
                }
            }
        }
        return xDoc;
    }

    public static RPCResponseDocument createRPCResponse(ArgumentInfo argument) {
        RPCResponseDocument xDoc = RPCResponseDocument.Factory.newInstance();
        if (argument != null) {
            RPCResponseType rs = xDoc.addNewRPCResponse();
            RPCResponseType.Result res = rs.addNewResult();
            res.setType(argument.valType);
            if (argument.value != null) {
                res.setValAsStr(ValAsStr.toStr(argument.value, argument.valType));
            }
        }
        return xDoc;
    }

    public static List<ArgumentInfo> parseRPCRequest(RPCRequestDocument xDoc) {
        RPCRequestType rq = xDoc.getRPCRequest();
        if (rq == null) {
            return null;
        }
        List<RPCRequestType.Arg> args = rq.getArgList();
        if (args == null || args.isEmpty()) {
            return null;
        }
        List<ArgumentInfo> arguments = new LinkedList<ArgumentInfo>();
        int index = 0;
        for (RPCRequestType.Arg a : args) {
            if (a.getType() == null) {
                throw new IllegalArgumentException("Undefined type of RPC argument " + index);
            }
            index++;
            Object value = null;
            if (a.getValAsStr() != null) {
                value = ValAsStr.fromStr(a.getValAsStr(), a.getType());
            }
            ArgumentInfo info = new ArgumentInfo(a.getType(), value);
            arguments.add(info);
        }
        return arguments;
    }

    public static ArgumentInfo parseRPCResponse(RPCResponseDocument xDoc) {
        if (xDoc == null) {
            return null;
        }
        RPCResponseType rs = xDoc.getRPCResponse();
        if (rs != null) {
            RPCResponseType.Result res = rs.getResult();
            if (res == null) {
                return null;
            } else {
                if (res.getType() == null) {
                    return null;
                } else {
                    Object value = null;
                    if (res.getValAsStr() != null) {
                        value = ValAsStr.fromStr(res.getValAsStr(), res.getType());
                    }
                    return new ArgumentInfo(res.getType(), value);
                }
            }
        }
        return null;
    }
}
