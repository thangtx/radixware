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
package org.radixware.kernel.utils.traceview.utils;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.radixware.kernel.utils.traceview.TraceViewSettings;
import org.radixware.kernel.utils.traceview.TraceViewSettings.ESplitArgs;

public class ParseArguments {

    public static enum EResultCode {

        CONSOLE, WINDOW, HELP, ERROR;
    }

    private EResultCode operatingMode;
    private String parseException;
    private CommandLine commands;
    private String query = new String();
    private String outputDir = new String();
    private int threadPoolSize = -1;
    private String outputFilePrefix = new String();
    private String outputFileSuffix = new String();
    private final List<String> openFiles = new ArrayList<>();
    private Options options;
    private String[] args;

    public ParseArguments(final String[] args) {
        operatingMode = EResultCode.WINDOW;
        this.args = args;
        createOptions();
        try {
            commands = new DefaultParser().parse(options, args);
            if (commands.hasOption("h")) {
                operatingMode = EResultCode.HELP;
            } else {
                if (commands.hasOption("files")) {
                    operatingMode = EResultCode.CONSOLE;
                    parseArg();
                }
            }
        } catch (ParseException ex) {
            operatingMode = EResultCode.ERROR;
            parseException = ex.getMessage();
        }
    }

    public String getArgs() {
        if (args != null) {
            if (args.length == 3) {
                if (args[0].equals("--threadPoolSize") || args[0].equals("-t")) {
                    return args[2];
                }
                if (args[1].equals("--threadPoolSize") || args[1].equals("-t")) {
                    return args[0];
                }
            }
            if (args.length == 1) {
                return args[0];
            }
        }
        return null;
    }

    private boolean isEmptyOptionArguments(String optionName) {
        return commands.getOptionValue(optionName).equals("");
    }

    private void parseArg() throws ParseException {
        if (!isEmptyOptionArguments("files")) {
            for (String adress : commands.getOptionValue("files").split(";")) {
                if (adress.matches(".+\\..+")) {
                    openFiles.add(adress);
                } else {
                    for (String fileAdress : new File(adress).list()) {
                        openFiles.add(adress + File.separator + fileAdress);
                    }
                }
            }
        } else {
            throw new ParseException("Invalid argument for option: files");
        }

        if (commands.hasOption("filter")) {
            if (!commands.getParsedOptionValue("filter").equals("")) {
                query = " WHERE (" + commands.getOptionValue("filter") + ")";
            } else {
                throw new ParseException("Invalid argument for option: filter");
            }
        }

        if (commands.hasOption("split")) {
            String[] splitArr = commands.getOptionValue("split").split(";");
            for (int i = 0; i < splitArr.length; i++) {
                switch (ESplitArgs.getESplitArgs(splitArr[i])) {
                    case CONTEXT:
                        ESplitArgs.CONTEXT.isIncluded = true;
                        ESplitArgs.CONTEXT.position = i;
                        break;
                    case SOURCE:
                        ESplitArgs.SOURCE.isIncluded = true;
                        ESplitArgs.SOURCE.position = i;
                        break;
                    case MINUTE:
                        ESplitArgs.MINUTE.isIncluded = true;
                        ESplitArgs.MINUTE.position = i;
                        break;
                    case HOUR:
                        ESplitArgs.HOUR.isIncluded = true;
                        ESplitArgs.HOUR.position = i;
                        break;
                    case DAY:
                        ESplitArgs.DAY.isIncluded = true;
                        ESplitArgs.DAY.position = i;
                        break;
                    case SRC_FILE:
                        ESplitArgs.SRC_FILE.isIncluded = true;
                        ESplitArgs.SRC_FILE.position = i;
                        break;
                    case SRC_FOLDER:
                        ESplitArgs.SRC_FOLDER.isIncluded = true;
                        ESplitArgs.SRC_FOLDER.position = i;
                        break;
                    default:
                        throw new ParseException("Invalid argument for option: split");
                }
            }
        }
        try {
            threadPoolSize = commands.hasOption("threadPoolSize") ? Integer.parseInt(commands.getOptionValue("threadPoolSize")) : -1;
        } catch (NumberFormatException ex) {
            threadPoolSize = -1;
        }
        outputDir = commands.hasOption("outputDir") ? commands.getOptionValue("outputDir") : "";
        outputFilePrefix = commands.hasOption("outputFilePrefix") ? commands.getOptionValue("outputFilePrefix") : "parsedFile";
        outputFileSuffix = commands.hasOption("outputFileSuffix") ? commands.getOptionValue("outputFileSuffix") : ".txt";
        outputDir = "".equals(outputDir) ? Paths.get("").toAbsolutePath().toString() : outputDir;
    }

    private void createOptions() {
        options = new Options();
        Option file = new Option("i", "files", true, "Paths to files or directories, separated by ';'. If option is not present, GUI mode is used.");
        file.setValueSeparator('\n');
        options.addOption(file);

        Option filter = new Option("f", "filter", true, "Filter conditions (SQL - query), fields: source, message,\ndate (Format: '" + TraceViewSettings.DATE_FORMAT_PLAIN_TEXT.get().toPattern() + "'), context, severity. Arguments must be in single quotes.");
        filter.setValueSeparator('\n');
        options.addOption(filter);

        options.addOption(new Option("s", "split", true, "File separation parameters, separated by ';', supported fields: srcfile, srcfolder, context, source, minute, hour, day"));
        options.addOption(new Option("o", "outputDir", true, "Output directory (by default current directory)"));
        options.addOption(new Option("p", "outputFilePrefix", true, "Output file prefix (by default parsedFile)"));
        options.addOption(new Option("u", "outputFileSuffix", true, "Output file suffix (by defaul .txt)"));
        options.addOption(new Option("h", "help", false, "Help"));
        options.addOption(new Option("t", "threadPoolSize", false, "Size of thread pool"));
        options.addOption(new Option("r", "recursive", false, "Recursive directory processing"));
    }

    public String getParseException() {
        return (operatingMode == EResultCode.ERROR) ? parseException : "";
    }

    public EResultCode getOperatingMode() {
        return operatingMode;
    }

    public List<String> getOpenFiles() {
        return openFiles;
    }

    public String getQuery() {
        return query;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public String getOutputFilePrefix() {
        return outputFilePrefix;
    }

    public String getOutputFileSuffix() {
        return outputFileSuffix;
    }

    public Options getOptions() {
        return options;
    }

    public CommandLine getCommandLineArg() {
        return commands;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }
}
