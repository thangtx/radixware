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

package org.radixware.kernel.common.client;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.common.enums.EIsoCountry;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERadixApplication;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.starter.config.ConfigEntry;
import org.radixware.kernel.starter.config.ConfigFileAccessor;
import org.radixware.kernel.starter.config.ConfigFileParseException;


public final class RunParams {

    private RunParams() {
    }

    private enum EParam {

        LANGUAGE("-language"),
        COUNTRY("-country"),
        APP_DATA("-configPath"),
        CON_ENCODING("-consoleEncoding"),
		/**
		 * 20180130 Котрачев. Адрес и порт (например 127.0.0.1:8088), который должен слушать драйвер selenium, встроенный в эксплорер. Если не указан, драйвер не запускать.
		 */
		WEBDRV_SERVER_ADDRESS("-webDrvServerAddress"),
		WEBDRV_CLIENTS("-webDrvClients"),
        TRACE_FILE("-traceFile"),
        TRACE_PROFILE("-traceProfile"),
        TRACE_MIN_SEVERITY("-traceMinSeverity"),
        DETAILED_3RD_PARTY_LOGGING ("-detailed3rdPartyLogging", true),
        DEVELOPMENT_MODE("-development", true),
        MARK_UNCHECKED_STRINGS("-markUncheckedStrings", "org.radixware.kernel.client.markUncheckedStrings", true),
        CONNECTION_NAME("-connection"),
        USER_EXT_DESIGNER_HEAP_SIZE("-extDesignerMaxHeapSize"),
        USER_EXT_DESIGNER_PERM_SIZE("-extDesignerMaxPermSize"),
        USER_EXT_DESIGNER_AWAIT_STARTER_MILLIS("-extDesignerAwaitStarterMillis", "rdx.report.designer.wait.for.starter.actualize.millis", false),
        USER_NAME("-user"),
        PASSWORD("-pwd"),
        AUTO_TEST("-autoTest"),
        TEST_EXPLORER_ITEM_ID("-startItem"),
        JUNIT_REPORT("-junitReport"),
        PWD_HASH("-pwdHash"),
        ROOT_ID("-root"),
        RESTORE_CONNECTION("-restoreConnection", true),
        RESTORE_CONTEXT("-restoreContext", true),
        CONFIG_FILE("-configFile"),
        UDS_BUILD_PATH("-udsBuildPath", "org.radixware.kernel.uds.buildPath", false),
        ALTERNATE_UDF_COMPLETION_SHORTCUT("-alternateUdfCompletionShortcut", false),
        INSPECTOR_AGENT_ADDRESS("-inspectorAgentAddress"),
        FORCE_DIALOG_WINDOW_TYPE("-forceDialogWindowType"),
        NO_EXT_META_INFO("-noExtMetaInfo",true),
        NO_SQML("-noSqml",true),
        NO_SQML_PRELOAD("-noSqmlPreload",true),
        EXT_META_INF_PRELOAD("-extMetaInfoPreload",true),
        NO_UDS("-noUDS",true),
        QT_STYLE("-style"),
        APP_ICON("-appIcon");
        private final String argument;
        private final boolean logical;
        private final String systemProperty;

        private EParam(final String arg) {
            this(arg, null, false);
        }

        private EParam(final String arg, final boolean isLogical) {
            this(arg, null, isLogical);
        }

        private EParam(final String arg, final String systemProperty, final boolean isLogical) {
            argument = arg;
            logical = isLogical;
            this.systemProperty = systemProperty;
        }

        public String getParam() {
            return argument;
        }

        public boolean isBoolean() {
            return logical;
        }

        public void setSystemProperty(final String value) {
            if (systemProperty != null) {
                System.setProperty(systemProperty, value);
            }
        }

        public static EParam getForArg(final String arg) {
            for (EParam param : EParam.values()) {
                if (param.getParam().equals(arg)) {
                    return param;
                }
            }
            return null;
        }
    }
    private final static Map<EParam, Object> PARAM_VALUES = new EnumMap<>(EParam.class);

    private static class ParamValueException extends Exception {

        private static final long serialVersionUID = -1094924822601764815L;

        protected ParamValueException(final String mess) {
            super(mess);
        }

        protected ParamValueException(final String mess, final Throwable cause) {
            super(mess, cause);
        }
    }

    private static final class EmptyParamValueException extends ParamValueException {

        private static final long serialVersionUID = 1811008521421082468L;

        EmptyParamValueException(final EParam param, final Throwable cause) {
            super("The parameter \"" + param.getParam() + "\" does not have a value", cause);
        }

        EmptyParamValueException(final EParam param) {
            super("The parameter \"" + param.getParam() + "\" does not have a value");
        }
    }

    private static final class WrongParamValueException extends ParamValueException {

        private static final long serialVersionUID = -8518111493343612370L;

        WrongParamValueException(final EParam param, final String paramValue) {
            super("The parameter \"" + param.getParam() + "\" has wrong value: \"" + paramValue + "\"");
        }

        WrongParamValueException(final EParam param, final String paramValue, final Throwable cause) {
            super("The parameter \"" + param.getParam() + "\" has wrong value: \"" + paramValue + "\"", cause);
        }
    }

    private static class UnableToLoadOptionsFromFile extends Exception {

        private static final long serialVersionUID = -5558318489200908503L;

        public UnableToLoadOptionsFromFile(final Throwable cause) {
            super(cause);
        }

        public UnableToLoadOptionsFromFile(final String message) {
            super(message);
        }
    }

    public static void initialize(String[] args) throws Exception {
        RunParams.parseArgs(args);
    }

    private static void parseArgs(final String[] args) throws RunParams.ParamValueException {
        final List<String> argsList = Arrays.asList(args);
        loadFromConfigFile(argsList);
        //args from command line overrides args from config file
        parseArgsImpl(argsList);
    }

    private static void parseArgsImpl(final List<String> args) throws RunParams.ParamValueException {
        final int len = args.size();
        for (int i = 0; i < len; i++) {
            final EParam param = EParam.getForArg(args.get(i));
            if (param != null) {
                final Object value;
                switch (param) {
                    case LANGUAGE:
                        value = parseLanguage(readValue(args, ++i, param));
                        break;
                    case COUNTRY:
                        value = parseCountry(readValue(args, ++i, param));
                        break;
                    case PWD_HASH:
                        value = parsePwdHash(readValue(args, ++i, param));
                        break;
                    case CON_ENCODING:
                        final String encoding = readValue(args, ++i, param);
                        setConsoleEncoding(encoding);
                        value = encoding;
                        break;
                    case WEBDRV_SERVER_ADDRESS:
                        String addr = readValue(args, ++i, param);
                        InetSocketAddress webDrvServerAddress
                                = org.radixware.kernel.common.utils.ValueFormatter.parseInetSocketAddress(addr);
                        setWebDrvServerAddress(webDrvServerAddress);
                        value=webDrvServerAddress;
                        break;
                    case ROOT_ID:
                        value = Id.Factory.loadFrom(readValue(args, ++i, param));
                        break;
                    case TEST_EXPLORER_ITEM_ID:
                        value = Id.Factory.loadFrom(readValue(args, ++i, param));
                        break;
                    case USER_EXT_DESIGNER_HEAP_SIZE:
                    case USER_EXT_DESIGNER_PERM_SIZE:
                        value = parseMemoryUsage(param, readValue(args, ++i, param));
                        break;
                    case USER_EXT_DESIGNER_AWAIT_STARTER_MILLIS:
                        value = readValue(args, ++i, param);
                        break;
                    case ALTERNATE_UDF_COMPLETION_SHORTCUT:
                        value = readValue(args, ++i, param);
                        break;
                    case UDS_BUILD_PATH:
                        value = readValue(args, ++i, param);
                        break;
                    default:
                        value = (param.isBoolean() ? Boolean.TRUE : readValue(args, ++i, param));
                        break;
                }
                param.setSystemProperty(value == null ? null : String.valueOf(value));
                PARAM_VALUES.put(param, value);
            }
        }
    }

    private static boolean loadFromConfigFile(final List<String> args) throws RunParams.ParamValueException {
        for (int i = 0, length = args.size(); i < length; i++) {
            if (EParam.getForArg(args.get(i)) == EParam.CONFIG_FILE) {
                final String configFile = readValue(args, ++i, EParam.CONFIG_FILE);
                try {
                    final List<String> argsFromFile = readArgsFromFileImpl(configFile);
                    parseArgsImpl(argsFromFile);
                } catch (UnableToLoadOptionsFromFile ex) {
                    final ByteArrayOutputStream bos = new ByteArrayOutputStream(4000);
                    final PrintWriter pw = new PrintWriter(bos);
                    ex.printStackTrace(pw);
                    pw.flush();
                    LogFactory.getLog(RunParams.class).error("Error while reading Explorer parameters:\n" + bos.toString());
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Load options from file. The file must be a regular java .properties file
     *
     * @param fileName - properties file
     * @param optionsToRead - keys that should be read. null value permits all
     * keys.
     * @return list of strings that represents arguments array
     */
    private static List<String> readArgsFromFileImpl(final String fileName) throws UnableToLoadOptionsFromFile {
        try {
            final ConfigFileAccessor configAccessor = ConfigFileAccessor.get(fileName, ERadixApplication.EXPLORER.getName());
            if (configAccessor == null) {
                return Collections.emptyList();//no parameters for explorer
            }
            final List<String> args = new ArrayList<>();
            for (final ConfigEntry entry : configAccessor.getEntries()) {
                final String nameWithMinus = entry.getKey().startsWith("-") ? entry.getKey() : "-".concat(entry.getKey());
                args.add(nameWithMinus);
                final String propVal = entry.getValue();
                if (propVal != null && !propVal.isEmpty()) {
                    args.add(propVal);
                }
            }
            return args;
        } catch (ConfigFileParseException ex) {
            throw new UnableToLoadOptionsFromFile(ex);
        }
    }

    private static String readValue(final List<String> args, final int index, final EParam param) throws EmptyParamValueException {
        if (index >= args.size()) {
            throw new EmptyParamValueException(param);
        }
        final String value = args.get(index);
        if (value == null || value.isEmpty()) {
            throw new EmptyParamValueException(param);
        }
        return value;
    }

    private static EIsoLanguage parseLanguage(final String lng) throws ParamValueException {
        try {
            return EIsoLanguage.getForValue(lng.toLowerCase(Locale.getDefault()));
        } catch (NoConstItemWithSuchValueError error) {//NOPMD
            try {
                return EIsoLanguage.valueOf(lng.toUpperCase(Locale.getDefault()));
            } catch (IllegalArgumentException exception) {
                throw new WrongParamValueException(EParam.LANGUAGE, lng, exception);
            }
        }
    }

    private static EIsoCountry parseCountry(final String country) throws ParamValueException {
        try {
            return EIsoCountry.getForValue(country.toUpperCase(Locale.getDefault()));
        } catch (NoConstItemWithSuchValueError error) {
            throw new WrongParamValueException(EParam.COUNTRY, country, error);
        }
    }

    private static void setConsoleEncoding(final String encoding) throws WrongParamValueException {
        try {
            new OutputStreamWriter(System.out, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new WrongParamValueException(EParam.CON_ENCODING, encoding, e);
        }
    }

    private static Bin parsePwdHash(final String hash) throws WrongParamValueException, EmptyParamValueException {
        try {
            return new Bin(hash);
        } catch (IllegalArgumentError error) {//NOPMD
            throw new WrongParamValueException(EParam.PWD_HASH, hash);
        }
    }

    private static String parseMemoryUsage(EParam param, final String memUsage) throws WrongParamValueException, EmptyParamValueException {

        if (memUsage == null || memUsage.isEmpty()) {
            throw new WrongParamValueException(param, memUsage);
        }
        for (int i = 0; i < memUsage.length() - 1; i++) {
            if (!Character.isDigit(memUsage.charAt(i))) {
                throw new WrongParamValueException(param, memUsage);
            }
        }
        char c = Character.toLowerCase(memUsage.charAt(memUsage.length() - 1));
        switch (c) {
            case 'm':
            case 'g':
                break;
            default:
                throw new WrongParamValueException(param, memUsage);
        }
        return memUsage;
    }

    public static EIsoLanguage getLanguage() {
        return (EIsoLanguage) PARAM_VALUES.get(EParam.LANGUAGE);
    }

    public static EIsoCountry getCountry() {
        return (EIsoCountry) PARAM_VALUES.get(EParam.COUNTRY);
    }

    public static String getConfigPath() {
        return (String) PARAM_VALUES.get(EParam.APP_DATA);
    }

    public static String getConsoleEncoding() {
        return (String) PARAM_VALUES.get(EParam.CON_ENCODING);
    }

    public static String getTraceFile() {
        return (String) PARAM_VALUES.get(EParam.TRACE_FILE);
    }

    public static String getTraceMinSeverity() {
        return (String) PARAM_VALUES.get(EParam.TRACE_MIN_SEVERITY);
    }
    
    public static boolean isDetailed3rdPartyLoggingEnabled(){
        return PARAM_VALUES.containsKey(EParam.DETAILED_3RD_PARTY_LOGGING);
    }

    public static String getTestOptionsFile() {
        return (String) PARAM_VALUES.get(EParam.AUTO_TEST);
    }

    public static String getTraceProfile() {
        return (String) PARAM_VALUES.get(EParam.TRACE_PROFILE);
    }

    public static String getConnectionName() {
        return (String) PARAM_VALUES.get(EParam.CONNECTION_NAME);
    }

    public static Id getExplorerRootId() {
        return (Id) PARAM_VALUES.get(EParam.ROOT_ID);
    }
	
	/**
	 * Вовзращает прописанный в конфиге -webDrvServerAddress или 0.
	 */
	public static InetSocketAddress getWebDrvServerAddress() {
		if(PARAM_VALUES.containsKey(EParam.WEBDRV_SERVER_ADDRESS))
            return (InetSocketAddress)(PARAM_VALUES.get(EParam.WEBDRV_SERVER_ADDRESS));
		return null;
	}
	
	public static String getWebDrvClients () {
		return (String) PARAM_VALUES.get(EParam.WEBDRV_CLIENTS);
	}

    public static Id getTestExplorerItemId() {
        return (Id) PARAM_VALUES.get(EParam.TEST_EXPLORER_ITEM_ID);
    }

    public static String getJUnitReportFile() {
        return (String) PARAM_VALUES.get(EParam.JUNIT_REPORT);
    }

    public static String getPassword() {
        return (String) PARAM_VALUES.get(EParam.PASSWORD);
    }

    public static String getUserName() {
        return (String) PARAM_VALUES.get(EParam.USER_NAME);
    }

    public static boolean isDevelopmentMode() {
        return PARAM_VALUES.containsKey(EParam.DEVELOPMENT_MODE);
    }

    public static boolean isExtendedMetaInformationAccessible(){
        return !PARAM_VALUES.containsKey(EParam.NO_EXT_META_INFO);
    }

    public static boolean isSqmlAccessible(){
        return !PARAM_VALUES.containsKey(EParam.NO_SQML);
    }

    public static boolean isSqmlPreloadEnabled(){
        return !PARAM_VALUES.containsKey(EParam.NO_SQML_PRELOAD);
    }

    public static boolean isExtendedMetaInformationPreloadEnabled(){
        return PARAM_VALUES.containsKey(EParam.EXT_META_INF_PRELOAD);
    }

    public static String getInspectorAgentAddress() {
        return (String) PARAM_VALUES.get(EParam.INSPECTOR_AGENT_ADDRESS);
    }

    public static String getForceDialogWindowType(){
        return (String) PARAM_VALUES.get(EParam.FORCE_DIALOG_WINDOW_TYPE);
    }

    public static boolean needToRestoreContext() {
        return PARAM_VALUES.containsKey(EParam.RESTORE_CONTEXT);
    }

    public static String getExtDesignerMaxHeapSize() {
        return (String) PARAM_VALUES.get(EParam.USER_EXT_DESIGNER_HEAP_SIZE);
    }

    public static String getExtDesignerMaxPermSize() {
        return (String) PARAM_VALUES.get(EParam.USER_EXT_DESIGNER_PERM_SIZE);
    }
    
    public static String getAlternateUdfCompletionShortcut() {
        return (String) PARAM_VALUES.get(EParam.ALTERNATE_UDF_COMPLETION_SHORTCUT);
    }        

    public static boolean isUDSAccessible(){
        return !PARAM_VALUES.containsKey(EParam.NO_UDS);
    }

    public static String getQtStyle(){
        return (String) PARAM_VALUES.get(EParam.QT_STYLE);
    }
    
    public static String getApplicationIconPath(){
        return (String) PARAM_VALUES.get(EParam.APP_ICON);
    }
    
    public static void clearPassword(){
        PARAM_VALUES.remove(EParam.PASSWORD);
        PARAM_VALUES.remove(EParam.PWD_HASH);
    }

    public static void clearConnectionParams() {
        PARAM_VALUES.remove(EParam.CONNECTION_NAME);
        PARAM_VALUES.remove(EParam.USER_NAME);
    }

    public static void removeRestoringContextParam() {
        PARAM_VALUES.remove(EParam.RESTORE_CONTEXT);
    }

    public static void setRootId(final Id rootId) {
        PARAM_VALUES.put(EParam.ROOT_ID, rootId);
    }

    private static void setWebDrvServerAddress(InetSocketAddress addr) {
        PARAM_VALUES.put(EParam.WEBDRV_SERVER_ADDRESS, addr);
    }

    public static String[] getArgs() {
        final ArrayList<String> params = new ArrayList<>(16);
        String value;
        for (Map.Entry<EParam, Object> arg : PARAM_VALUES.entrySet()) {
            switch (arg.getKey()) {
                case CONFIG_FILE:
                    continue;
                case LANGUAGE:
                    value = getLanguage().getName();
                    break;
                default:
                    value = (arg.getKey().isBoolean() ? "" : String.valueOf(arg.getValue()));
                    break;
            }
            params.add(arg.getKey().getParam());
            if (value != null && !value.isEmpty()) {
                params.add(value);
            }
        }
        return params.toArray(new String[]{});
    }

    public static boolean needToRestoreConnection() {
        return PARAM_VALUES.containsKey(EParam.RESTORE_CONNECTION);
    }

    public static void removeRestoringConnectionParam() {
        PARAM_VALUES.remove(EParam.RESTORE_CONNECTION);
    }
}
