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
package org.radixware.kernel.starter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.starter.config.ConfigEntry;
import org.radixware.kernel.starter.config.ConfigFileAccessor;
import org.radixware.kernel.starter.config.ConfigFileParseException;

public class StarterCommandLineParser {

    public static StarterArguments parse(String[] args) throws ConfigFileParseException {
        if (("-" + StarterArguments.CONFIG_FILE).equals(args[0])) {
            StarterArguments argsFromFile = parseArgumentsFromFile(args[1]);
            if (args.length == 2) {
                return argsFromFile;
            }
            if ((args.length == 4 || args.length == 5) && (StarterArguments.EXPORT_COMMAND.equals(args[2]) || ("-" + StarterArguments.EXPORT_COMMAND).equals(args[2]))) {
                final List<String> newAppParameters = new ArrayList<>();
                for (int i = 2; i < args.length; i++) {
                    newAppParameters.add(args[i]);
                }
                return new StarterArguments(argsFromFile.getConfigFileName(), argsFromFile.getStarterParameters(), newAppParameters);
            }
            throw new ConfigFileParseException("Config file should be used alone or with export command, see Usage");
        }
        return parseArgumentsFromCommandLine(args);
    }

    private static StarterArguments parseArgumentsFromFile(final String fileName) throws ConfigFileParseException {
        final ConfigFileAccessor configAccessor = ConfigFileAccessor.get(fileName, StarterArguments.STARTER_SECTION);
        if (configAccessor == null) {
            throw new ConfigFileParseException(StarterArguments.STARTER_SECTION + " section was not found in specified config file");
        }
        final List<String> appParameters = new ArrayList<String>();
        final Map<String, String> starterParameters = new HashMap<String, String>();
        for (ConfigEntry entry : configAccessor.getEntries()) {
            if (StarterArguments.APP_CLASS.equals(entry.getKey())) {
                appParameters.add(0, entry.getValue());
            } else if (StarterArguments.APP_ARGS.equals(entry.getKey())) {
                appParameters.addAll(parseAppArgs(entry.getValue()));
            } else {
                starterParameters.put(entry.getKey(), entry.getValue());
            }
        }
        if (appParameters.size() == 1) {//by default pass the same config file to the application
            appParameters.add("-" + StarterArguments.CONFIG_FILE);
            appParameters.add(fileName);
        }
        return new StarterArguments(fileName, starterParameters, appParameters);
    }

    private static Collection<String> parseAppArgs(final String args) {
        if (args == null || args.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(args.split("\\s"));
    }

    private static StarterArguments parseArgumentsFromCommandLine(final String[] args) {
        final Map<String, String> starterParameters = new HashMap<>();
        final List<String> appParameters = new ArrayList<>();
        int i = 0;
        while (i < args.length) {
            if (args[i].charAt(0) == '-') {
                String par, val = null;
                int index = args[i].indexOf('=');
                if (index != -1) {
                    par = args[i].substring(1, index);
                    val = args[i].substring(index + 1);
                } else {
                    par = args[i].substring(1);
                }
                starterParameters.put(par, val);
                i++;
            } else {
                break;
            }
        }
        while (i < args.length) {
            appParameters.add(args[i++]);
        }
        return new StarterArguments(starterParameters, appParameters);
    }
}
