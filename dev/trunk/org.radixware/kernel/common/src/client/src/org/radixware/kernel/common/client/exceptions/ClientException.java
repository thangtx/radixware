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

package org.radixware.kernel.common.client.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.errors.UnsupportedDefinitionVersionError;
import org.radixware.kernel.common.client.errors.UserAccountLockedError;
import org.radixware.kernel.common.client.errors.WrongPasswordError;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.enums.EAuthType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.ServiceCallFault;


public class ClientException {

    private ClientException() {
    }
    private static final Pattern VERSION_MISTMATCH = Pattern.compile("(.+)\nVersion signature is (\"(?:\\w+=\\d+,?)+\");\nproject signature is (\"(?:\\w+=\\d+,?)+\").\n(.+)");

    public static String getExceptionTitle(MessageProvider mp, final Throwable exception) {
        final ServiceCallFault fault = getServiceCallFault(exception);
        if (fault != null) {
            final org.radixware.schemas.eas.ExceptionEnum.Enum faultType =
                    getFaultType(fault);
            if (faultType == org.radixware.schemas.eas.ExceptionEnum.INVALID_REQUEST) {//RADIX-3469
                return mp.translate("ExplorerDialog", "Error");
            } else if (faultType == org.radixware.schemas.eas.ExceptionEnum.ACCESS_VIOLATION
                    || faultType == org.radixware.schemas.eas.ExceptionEnum.DEFINITION_ACCESS_VIOLATION) {
                return mp.translate("ExplorerDialog", "Insufficient Privileges");
            } else if (faultType == org.radixware.schemas.eas.ExceptionEnum.WEB_DRIVER_IS_NOT_ALLOWED){
                return mp.translate("ExplorerMessage", "Connection Error");
            }
            if (fault instanceof ObjectNotFoundError) {
                return ((ObjectNotFoundError) fault).getTitle(mp);
            }
        } else if (exception instanceof IClientError) {
            final String title = ((IClientError) exception).getTitle(mp);
            if (title != null && !title.isEmpty()) {
                return title;
            }
        }
        return mp.translate("ExplorerError", "Unhandled Exception");
    }

    public static String getExceptionReason(MessageProvider mp, final Throwable exception) {
        Throwable cause;
        if (exception instanceof ObjectNotFoundError) {
            return ((ObjectNotFoundError) exception).getMessageToShow();
        }
        for (cause = exception; cause.getCause() != null;) {
            if (cause instanceof ObjectNotFoundError) {
                return ((ObjectNotFoundError) cause).getMessageToShow();
            }
            cause = cause.getCause();
        }
        if (cause instanceof ServiceCallFault) {
            final org.radixware.schemas.eas.ExceptionEnum.Enum faultType =
                    getFaultType((ServiceCallFault) cause);
            if (faultType != null) {
                switch (faultType.intValue()) {
                    case org.radixware.schemas.eas.ExceptionEnum.INT_ACCESS_VIOLATION: {
                        return mp.translate("ExplorerError", "Insufficient privileges");
                    }
                    case org.radixware.schemas.eas.ExceptionEnum.INT_INVALID_REQUEST: {
                        final String message = cause.getMessage();
                        if (message != null && !message.isEmpty()) {//RADIX-2236, RADIX-3469
                            final int deviderPos = message.indexOf("\n\n");
                            if (deviderPos > 0) {
                                return message.substring(0, deviderPos);
                            } else {
                                return message;
                            }
                        } else {
                            return mp.translate("ExplorerError", "Invalid Request");
                        }
                    }
                    case org.radixware.schemas.eas.ExceptionEnum.INT_SUBOBJECTS_FOUND: {
                        final String message = cause.getMessage();
                        if (message != null && !message.isEmpty()) {//RADIX-2236
                            return message;
                        }
                        break;
                    }
                    case org.radixware.schemas.eas.ExceptionEnum.INT_SERVER_MALFUNCTION: {//RADIX-6788
                        final String message = ((ServiceCallFault) cause).getCauseExMessage();
                        if (message != null && !message.isEmpty() && message.indexOf("\n") < 0) {
                            return mp.translate("ExplorerError", "Error occurred during request processing") + ":\n" + message;
                        } else {
                            return mp.translate("ExplorerError", "Error occurred during request processing");
                        }
                    }
                    default: {
                        return mp.translate("ExplorerError", "Error occurred during request processing");
                    }
                }
            }
            return mp.translate("ExplorerError", "Error occurred during request processing");
        }
        for (cause = exception; cause.getCause() != null;) {
            if (cause instanceof IClientError) {
                return ((IClientError) cause).getLocalizedMessage(mp);
            }
            cause = cause.getCause();
        }
        final String reason = cause.getMessage();
        if (reason != null && !reason.isEmpty()) {
            return cause.getClass().getSimpleName() + "\n" + reason;
        }
        return cause.getClass().getSimpleName();
    }

    public static String exceptionStackToString(final Throwable exception) {
        final StringWriter result = new StringWriter();
        final PrintWriter stackPrintWriter = new PrintWriter(result);
        exception.printStackTrace(stackPrintWriter);
        stackPrintWriter.close();
        final ServiceCallFault fault = getServiceCallFault(exception);
        if (fault != null && fault.getCauseExStack() != null && !fault.getCauseExStack().isEmpty()) {
            result.append("\n");
            result.append(fault.getCauseExStack());
        }
        return result.toString();
    }

    public static EEventSeverity getFaultSeverity(final Throwable exception) {
        final ServiceCallFault fault = getServiceCallFault(exception);
        if (fault==null){
            return EEventSeverity.ERROR;
        }
        final org.radixware.schemas.eas.ExceptionEnum.Enum faultType = getFaultType(fault);
        if (faultType == org.radixware.schemas.eas.ExceptionEnum.KERBEROS_AUTHENTICATION_FAILED
                && org.radixware.schemas.eas.KerberosAuthFaultMessage.RENEW_CREDENTIALS_REQUIRED.toString().equals(fault.getMessage())) {
            return EEventSeverity.DEBUG;
        }
        if (DEBUG_LEVEL_FAULTS.contains(faultType)) {
            return EEventSeverity.DEBUG;
        } else if (EVENT_LEVEL_FAULTS.contains(faultType)) {
            return EEventSeverity.EVENT;
        } else if (WARNING_LEVEL_FAULTS.contains(faultType)) {
            return EEventSeverity.WARNING;
        } else {
            return EEventSeverity.ERROR;
        }
    }

    private static ServiceCallFault getServiceCallFault(final Throwable error) {
        Throwable err = error;
        while (err != null) {
            if (err instanceof ServiceCallFault) {
                return (ServiceCallFault) err;
            }
            if (err.getCause() == err) {
                return null;
            }
            err = err.getCause();
        }
        return null;
    }
    private static final List<org.radixware.schemas.eas.ExceptionEnum.Enum> NOT_SPECIAL =
            Arrays.asList(
            org.radixware.schemas.eas.ExceptionEnum.INVALID_REQUEST,
            org.radixware.schemas.eas.ExceptionEnum.ACCESS_VIOLATION,
            org.radixware.schemas.eas.ExceptionEnum.DEFINITION_ACCESS_VIOLATION,
            org.radixware.schemas.eas.ExceptionEnum.SUBOBJECTS_FOUND,
            org.radixware.schemas.eas.ExceptionEnum.SERVER_MALFUNCTION,
            org.radixware.schemas.eas.ExceptionEnum.SERVER_EXCEPTION,
            org.radixware.schemas.eas.ExceptionEnum.DEFINITION_NOT_FOUND,
            org.radixware.schemas.eas.ExceptionEnum.DEFINITION_ACCESS_VIOLATION);
    private static final List<org.radixware.schemas.eas.ExceptionEnum.Enum> DEBUG_LEVEL_FAULTS =
            Arrays.asList(
            org.radixware.schemas.eas.ExceptionEnum.CONFIRM_SUBOBJECTS_DELETE,
            org.radixware.schemas.eas.ExceptionEnum.REPEATATIVE_PASSWORD,
            org.radixware.schemas.eas.ExceptionEnum.INVALID_DEFINITION_VERSION,
            org.radixware.schemas.eas.ExceptionEnum.APPLICATION_ERROR);
    private static final List<org.radixware.schemas.eas.ExceptionEnum.Enum> EVENT_LEVEL_FAULTS =
            Arrays.asList(
                org.radixware.schemas.eas.ExceptionEnum.SESSION_DOES_NOT_EXIST,
                org.radixware.schemas.eas.ExceptionEnum.SHOULD_CHANGE_PASSWORD,                    
                org.radixware.schemas.eas.ExceptionEnum.SESSIONS_LIMIT_EXCEED,
                org.radixware.schemas.eas.ExceptionEnum.TEMPORARY_PASSWORD_EXPIRED,
                org.radixware.schemas.eas.ExceptionEnum.INVALID_PASSWORD,
                org.radixware.schemas.eas.ExceptionEnum.INVALID_CREDENTIALS);
    private static final List<org.radixware.schemas.eas.ExceptionEnum.Enum> WARNING_LEVEL_FAULTS =
            Arrays.asList(            
            org.radixware.schemas.eas.ExceptionEnum.INVALID_SORTING,
            org.radixware.schemas.eas.ExceptionEnum.FILTER_IS_OBSOLETE,
            org.radixware.schemas.eas.ExceptionEnum.FILTER_NOT_FOUND,
            org.radixware.schemas.eas.ExceptionEnum.UNSUPPORTED_DEFINITION_VERSION);

    public static boolean isInformationMessage(final Throwable error) {
        final ServiceCallFault fault = getServiceCallFault(error);
        if ((fault instanceof ObjectNotFoundError) || (fault instanceof CommonFilterNotFoundException)) {
            return true;
        }
        return fault != null && getFaultType(fault) == org.radixware.schemas.eas.ExceptionEnum.REPEATATIVE_PASSWORD;
    }

    //This exception must be processed strongly by Environment.processException
    public static boolean isSystemFault(final Throwable error) {
        final ServiceCallFault fault = getServiceCallFault(error);
        if (fault != null) {
            final org.radixware.schemas.eas.ExceptionEnum.Enum faultType = getFaultType(fault);
            return faultType == org.radixware.schemas.eas.ExceptionEnum.UNSUPPORTED_DEFINITION_VERSION;
        }
        for (Throwable err = error; err != null && err.getCause() != err; err = err.getCause()) {
            if (err instanceof WrongPasswordError
                    || err instanceof UserAccountLockedError
                    || err instanceof UnsupportedDefinitionVersionError) {
                return true;
            }
        }
        return false;
    }

    public static String getSerivceCallFaultDetails(final ServiceCallFault fault) {
        final org.radixware.schemas.eas.ExceptionEnum.Enum faultType = getFaultType(fault);
        String message = fault.getMessage();
        if (faultType == org.radixware.schemas.eas.ExceptionEnum.INVALID_REQUEST) {
            if (message == null || message.indexOf("\n\n") < 0) {
                return "";
            } else {
                final int markerIndex = message.lastIndexOf("\n\n");
                if (markerIndex + 2 == message.length()) {
                    message = message.substring(0, markerIndex);
                }
            }
        }
        final StringBuilder detailsBuilder = new StringBuilder();
        if (fault.getFaultCode() != null && !fault.getFaultCode().isEmpty()) {
            detailsBuilder.append("fault code: ");
            detailsBuilder.append(fault.getFaultCode());
            detailsBuilder.append("\n");
        }
        if (fault.getFaultString() != null && !fault.getFaultString().isEmpty()) {
            detailsBuilder.append("fault string: ");
            detailsBuilder.append(fault.getFaultString());
            detailsBuilder.append("\n");
        }
        if (fault.getCauseExClass() != null && !fault.getCauseExClass().isEmpty()) {
            detailsBuilder.append("exception class: ");
            detailsBuilder.append(fault.getCauseExClass());
            detailsBuilder.append("\n");
        }
        if (fault.getCauseExMessage() != null && !fault.getCauseExMessage().isEmpty()) {
            detailsBuilder.append("exception cause: ");
            detailsBuilder.append(fault.getCauseExMessage());
            detailsBuilder.append("\n");
        }
        if (message != null && !message.isEmpty()) {
            detailsBuilder.append("exception message: ");
            detailsBuilder.append(fault.getMessage());
            detailsBuilder.append("\n");
        }
        return detailsBuilder.toString();
    }

    //Use message box instead of exception box to show exception
    public static boolean isSpecialFault(final Throwable error) {
        final ServiceCallFault fault = getServiceCallFault(error);
        if (fault instanceof ObjectNotFoundError) {
            return ((ObjectNotFoundError) fault).inKnownContext();
        }
        if (fault != null) {
            final org.radixware.schemas.eas.ExceptionEnum.Enum faultType =
                    getFaultType(fault);
            if (faultType != null && !NOT_SPECIAL.contains(faultType)) {
                return true;
            }
            String exMessage = fault.getCauseExMessage();
            if (exMessage == null) {
                return false;
            }
            final Matcher matcher = VERSION_MISTMATCH.matcher(exMessage);
            return matcher.find();
        }
        return false;
    }

    private static org.radixware.schemas.eas.ExceptionEnum.Enum getFaultType(final ServiceCallFault fault) {
        final String faultString = fault.getFaultString();
        return org.radixware.schemas.eas.ExceptionEnum.Enum.forString(faultString);
    }

    public static String getSpecialFaultMessage(IClientEnvironment environment, final Throwable error) {
        final ServiceCallFault fault = getServiceCallFault(error);
        if (fault instanceof ObjectNotFoundError) {
            return ((ObjectNotFoundError) fault).getMessageToShow();
        }
        if (fault instanceof CommonFilterIsObsoleteException) {
            return fault.getLocalizedMessage();
        }
        if (fault instanceof CommonFilterNotFoundException) {
            return fault.getLocalizedMessage();
        }
        final org.radixware.schemas.eas.ExceptionEnum.Enum faultType = getFaultType(fault);
        if (faultType != null) {
            switch (faultType.intValue()) {
                case org.radixware.schemas.eas.ExceptionEnum.INT_USER_ACCOUNT_LOCKED: {
                    final String message = environment.getMessageProvider().translate("ExplorerError", "Account for user %s is locked");
                    return String.format(message, fault.getMessage());
                }
                case org.radixware.schemas.eas.ExceptionEnum.INT_INVALID_AUTH_TYPE: {
                    final int pos = fault.getMessage().indexOf("\n");
                    if (pos > 0) {
                        final String authTypeStr = fault.getMessage().substring(0, pos);
                        final String userName = fault.getMessage().substring(pos + 1);
                        final EAuthType authType;
                        try {
                            authType = EAuthType.getForValue(authTypeStr);
                        } catch (NoConstItemWithSuchValueError e) {
                            final String message = environment.getMessageProvider().translate("ExplorerError", "Unable to authenticate user '%1s'.\nTry to use another authentication type");
                            return String.format(message, userName);
                        }
                        switch (authType) {
                            case KERBEROS: {
                                final String message = environment.getMessageProvider().translate("ExplorerError", "Unable to authenticate user '%1s' via kerberos protocol.\nTry to use another authentication type");
                                return String.format(message, userName);
                            }
                            case CERTIFICATE: {
                                final String message = environment.getMessageProvider().translate("ExplorerError", "Unable to authenticate user '%1s' by certificate\nTry to use another authentication type");
                                return String.format(message, userName);
                            }
                            default: {
                                final String message = environment.getMessageProvider().translate("ExplorerError", "Unable to authenticate user '%1s' by password\nTry to use another authentication type");
                                return String.format(message, userName);
                            }
                        }
                    } else {
                        return environment.getMessageProvider().translate("ExplorerError", "Unable to authenticate user.\nTry to use another authentication type");
                    }
                }
                case org.radixware.schemas.eas.ExceptionEnum.INT_INVALID_USER: {
                    final String message = environment.getMessageProvider().translate("ExplorerError", "User '%s' not registered");
                    return String.format(message, fault.getMessage());
                }
                case org.radixware.schemas.eas.ExceptionEnum.INT_INVALID_STATION: {
                    final String message = environment.getMessageProvider().translate("ExplorerError", "Station name '%s' is invalid");
                    return String.format(message, fault.getMessage());
                }
                case org.radixware.schemas.eas.ExceptionEnum.INT_INVALID_PASSWORD: {
                    return environment.getMessageProvider().translate("ExplorerError", "Password is invalid");
                }
                case org.radixware.schemas.eas.ExceptionEnum.INT_INVALID_CREDENTIALS: {
                    return environment.getMessageProvider().translate("ExplorerError", "The user name or password is incorrect");
                }
                case org.radixware.schemas.eas.ExceptionEnum.INT_REPEATATIVE_PASSWORD: {
                    final String message = environment.getMessageProvider().translate("ExplorerError", "New password cannot repeat any of your previous %s passwords.\nPlease type a different password.");
                    return String.format(message, fault.getMessage());
                }
                case org.radixware.schemas.eas.ExceptionEnum.INT_FILTER_IS_OBSOLETE: {
                    return environment.getMessageProvider().translate("ExplorerError", "Current filter was modified. It is necessary to reread data with new filter");
                }
                case org.radixware.schemas.eas.ExceptionEnum.INT_FILTER_NOT_FOUND: {
                    return environment.getMessageProvider().translate("ExplorerError", "Current filter was removed. It is necessary to reread data");
                }
                case org.radixware.schemas.eas.ExceptionEnum.INT_INVALID_SORTING: {
                    if (fault instanceof IClientError) {
                        return ((IClientError) fault).getLocalizedMessage(environment.getMessageProvider());
                    } else {
                        return environment.getMessageProvider().translate("ExplorerError", "Sorting is not applicable for current filter");
                    }
                }
                case org.radixware.schemas.eas.ExceptionEnum.INT_INVALID_CERTIFICATE: {
                    return environment.getMessageProvider().translate("ExplorerError", "Unable to identify the user by the given certificate");
                }
                case org.radixware.schemas.eas.ExceptionEnum.INT_KERBEROS_AUTHENTICATION_FAILED: {
                    return environment.getMessageProvider().translate("ExplorerError", "Unable to identify the user");
                }
                case org.radixware.schemas.eas.ExceptionEnum.INT_LOGON_TIME_RESTRICTION_VIOLATION: {
                    if (environment.getEasSession().isOpened()){
                        return environment.getMessageProvider().translate("ExplorerError", "Login time has expired. It is impossible to continue work.");
                    }else{
                        return environment.getMessageProvider().translate("ExplorerError", "You cannot login at this moment.\nLogin time restriction violation");
                    }
                }
                case org.radixware.schemas.eas.ExceptionEnum.INT_SESSIONS_LIMIT_EXCEED: {
                    return environment.getMessageProvider().translate("ExplorerError", "The maximum number of opened sessions for this account has been exceeded");
                } 
                case org.radixware.schemas.eas.ExceptionEnum.INT_SHOULD_CHANGE_PASSWORD: {
                    return environment.getMessageProvider().translate("ExplorerError", "Your password has expired");
                }
                case org.radixware.schemas.eas.ExceptionEnum.INT_SESSION_DOES_NOT_EXIST: {
                    return environment.getMessageProvider().translate("ExplorerError", "Session does not exist");
                }
                case org.radixware.schemas.eas.ExceptionEnum.INT_WEB_DRIVER_IS_NOT_ALLOWED:{
                    return environment.getMessageProvider().translate("ExplorerError", "WebDriver is restricted to use on server.\nRemove '-webDrvServerAddress' start argument and try again.");
                }
                default: {
                    final String exMessage = fault.getCauseExMessage();
                    return exMessage != null && !exMessage.isEmpty() ? exMessage : fault.getMessage();
                }
            }
        }
        return "";
    }
}
