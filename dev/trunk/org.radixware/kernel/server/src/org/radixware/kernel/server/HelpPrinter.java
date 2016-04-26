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

package org.radixware.kernel.server;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;
import org.radixware.kernel.common.utils.SystemTools;


public class HelpPrinter {

    private PrintStream output;
    private static final int PARAM_SECTION_LEVEL = 7;//count of pads between line start and description
    private static final String SPACES;
    private static final String PAD_STR = "    ";

    static {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < PARAM_SECTION_LEVEL; i++) {
            sb.append(PAD_STR);
        }
        SPACES = sb.toString();
    }

    public HelpPrinter(final PrintStream output) {
        if (SystemTools.isWindows && Locale.getDefault().getLanguage().equals("ru")) {
            try {
                this.output = new PrintStream(output, true, "cp866");
            } catch (UnsupportedEncodingException ex) {
                this.output = output;
            }
        } else {
            this.output = output;
        }
    }

    public void printUsage() {
        printDescription(Messages.MSG_SERVER_ARGUMENTS);

        printDescription(Messages.PARAMS_SWITCHES);

        printConsoleMandatoryParam(SrvRunParams.SWITCH_GUI_OFF);
        printDescription(Messages.SWITCH_GUI_OFF_DESC);

        printConsoleMandatoryParam(SrvRunParams.AUTOSTART);
        printDescription(Messages.AUTOSTART_DESC);

        printParam(SrvRunParams.EXTERNAL_AUTH);
        printDescription(Messages.EXTERNAL_AUTH_DESC);

        printParam(SrvRunParams.DETAILED_3RD_PARTY_LOGGING);
        printDescription(Messages.DETAILED_3RD_PARTY_LOGGING_DESC);

        printParam("-help");
        printDescription(Messages.HELP);

        printDescription(Messages.PARAMS_WITH_ARGS);

        printConsoleMandatoryParam(SrvRunParams.DB_URL);
        printDescription(Messages.DB_URL_DESC);

        printConsoleMandatoryParam(SrvRunParams.DB_SCHEMA);
        printDescription(Messages.DB_SCHEMA_DESC);

        printConsoleMandatoryParam(SrvRunParams.USER);
        printDescription(Messages.USER_DESC);

        printConsoleMandatoryParam(SrvRunParams.INSTANCE);
        printDescription(Messages.INSTANCE_DESC);

        printParam(SrvRunParams.SENS_TRC_FINISH_TIME);
        printDescription(Messages.SENS_TRC_FINISH_TIME_DESC);

        printConsoleMandatoryParam(SrvRunParams.DB_PWD);
        printDescription(Messages.DB_PWD_DESC);

        printParam(SrvRunParams.KEYSTORE_PWD);
        printDescription(Messages.KEYSTORE_PWD_DESC);

        printParam(SrvRunParams.ORA_WALLET_PWD);
        printDescription(Messages.ORA_WALLET_PWD_DESC);
    }

    private void printDescription(String string) {
        String[] strings = string.split("`");
        for (int i = 0; i < strings.length; i++) {
            if (i != 0) {
                print(SPACES);
            }
            println(strings[i]);
        }
        println();
    }

    private void printConsoleMandatoryParam(String param) {
        print(param);
        int spacesLeft = PAD_STR.length() * PARAM_SECTION_LEVEL - param.length();
        print(SPACES.substring(0, spacesLeft));
        final String mandatorySymbol = "";//(Optional)";//" (*)";
        print(mandatorySymbol);
    }

    private void printParam(String param) {
        print(param);
        int spacesLeft = PAD_STR.length() * PARAM_SECTION_LEVEL - param.length();
        print(SPACES.substring(0, spacesLeft));
    }

    private void print(String text) {
        print(0, text);
    }

    private void println(String text) {
        println(0, text);
    }

    private void println() {
        output.println();
    }

    private void print(int level, String text) {
        for (int i = 0; i < level; i++) {
            output.print(PAD_STR);
        }
        output.print(text);
    }

    private void println(int level, String text) {
        print(level, text);
        println();
    }

    private static class Messages {

        private static final String DB_URL_DESC;
        private static final String DB_SCHEMA_DESC;
        private static final String USER_DESC;
        private static final String EXTERNAL_AUTH_DESC;
        private static final String DETAILED_3RD_PARTY_LOGGING_DESC;
        private static final String INSTANCE_DESC;
        private static final String AUTOSTART_DESC;
        private static final String SWITCH_GUI_OFF_DESC;
        private static final String MSG_SERVER_ARGUMENTS;
        private static final String PARAMS_SWITCHES;
        private static final String PARAMS_WITH_ARGS;
        private static final String HELP;
        private static final String SENS_TRC_FINISH_TIME_DESC;
        private static final String DB_PWD_DESC;
        private static final String KEYSTORE_PWD_DESC;
        private static final String ORA_WALLET_PWD_DESC;

        static {
            final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.mess.messages");

            DB_URL_DESC = bundle.getString("DB_URL_DESC");
            DB_SCHEMA_DESC = bundle.getString("DB_SCHEMA_DESC");
            USER_DESC = bundle.getString("USER_DESC");
            EXTERNAL_AUTH_DESC = bundle.getString("EXTERNAL_AUTH_DESC");
            INSTANCE_DESC = bundle.getString("INSTANCE_DESC");
            AUTOSTART_DESC = bundle.getString("AUTOSTART_DESC");
            SWITCH_GUI_OFF_DESC = bundle.getString("SWITCH_GUI_OFF_DESC");
            MSG_SERVER_ARGUMENTS = bundle.getString("MSG_SERVER_ARGUMENTS");
            HELP = bundle.getString("HELP");
            PARAMS_SWITCHES = bundle.getString("PARAMS_SWITCHES");
            PARAMS_WITH_ARGS = bundle.getString("PARAMS_WITH_ARGS");
            SENS_TRC_FINISH_TIME_DESC = bundle.getString("SENS_TRC_FINISH_TIME_DESC");
            DB_PWD_DESC = bundle.getString("DB_PWD_DESC");
            KEYSTORE_PWD_DESC = bundle.getString("KEYSTORE_PWD_DESC");
            ORA_WALLET_PWD_DESC = bundle.getString("ORA_WALLET_PWD_DESC");
            DETAILED_3RD_PARTY_LOGGING_DESC = bundle.getString("DETAILED_3RD_PARTY_LOGGING_DESC");
        }
    }
}
