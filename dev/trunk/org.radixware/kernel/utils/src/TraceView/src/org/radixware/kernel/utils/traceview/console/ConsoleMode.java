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
package org.radixware.kernel.utils.traceview.console;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.query.parser.common.ValueParser;
import com.googlecode.cqengine.query.parser.sql.SQLParser;
import com.googlecode.cqengine.resultset.ResultSet;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import org.radixware.kernel.utils.traceview.utils.ParseArguments;
import org.radixware.kernel.utils.traceview.TraceEvent;
import org.radixware.kernel.utils.traceview.TraceViewSettings;
import org.radixware.kernel.utils.traceview.TraceViewSettings.ESplitArgs;
import org.radixware.kernel.utils.traceview.utils.ContextForParsing;
import org.radixware.kernel.utils.traceview.utils.TraceViewUtils;

public class ConsoleMode {

    private static final Logger logger = Logger.getLogger(ConsoleMode.class.getName());
    private final ParseArguments arguments;
    private static final List<File> openFiles = new ArrayList<>();

    public ConsoleMode(ParseArguments arguments) {
        this.arguments = arguments;
        for (String path : arguments.getOpenFiles()) {
            openFiles.add(new File(path));
        }
        startConsoleMode();
    }

    public class DateTimeParser extends ValueParser<Date> {

        @Override
        protected Date parse(Class<? extends Date> type, String string) {
            try {
                return TraceViewSettings.DATE_FORMAT_PLAIN_TEXT.get().parse(string.replaceAll("'", ""));
            } catch (ParseException e) {
                throw new IllegalArgumentException("Wrong date/time format. Found: " + string + ", but expected format: '" + TraceViewSettings.DATE_FORMAT_PLAIN_TEXT.get().toPattern() + "'");
            }
        }
    }

    private void parseFiles(List<File> files, IndexedCollection<TraceEvent> traceIndexedCollection, boolean isRecursive, boolean isFilterByContext) {
        for (File file : files) {
            if (file.isFile()) {
                try {
                    ContextForParsing ctx = new ContextForParsing(null, file, isFilterByContext, false);
                    ctx.parseFile();
                    logger.log(Level.INFO, "File opened: {0}", file.getAbsolutePath());
                    traceIndexedCollection.addAll((List<TraceEvent>) ctx.getData().get(TraceViewSettings.DATA));
                } catch (IllegalArgumentException ex) {
                    logger.log(Level.INFO, "Unable to parse file: {0}", file.getAbsolutePath());
                }
            } else if (file.isDirectory() && isRecursive) {
                List<File> childFiles = new ArrayList<>();
                for (File f : file.listFiles()) {
                    childFiles.add(f);
                }
                parseFiles(childFiles, traceIndexedCollection, isRecursive, isFilterByContext);
            }
        }
    }

    private void parseFilesSplitFiles(List<File> files, boolean isRecursive, boolean isFilterByContext, SQLParser<TraceEvent> sqlparser, String queryStr) {
        for (File file : files) {
            if (file.isFile()) {
                try {
                    ContextForParsing ctx = new ContextForParsing(null, file, isFilterByContext, false);
                    ctx.parseFile();
                    IndexedCollection<TraceEvent> traceIndexedCollection = new ConcurrentIndexedCollection<>();
                    traceIndexedCollection.addAll((List<TraceEvent>) ctx.getData().get(TraceViewSettings.DATA));
                    logger.log(Level.INFO, "File opened: {0}", file.getAbsolutePath());
                    writeResultToFile(sqlparser.retrieve(traceIndexedCollection, queryStr));
                } catch (IllegalArgumentException ex) {
                    logger.log(Level.INFO, "Unable to parse file: {0}", file.getAbsolutePath());
                }
            } else if (file.isDirectory() && isRecursive) {
                List<File> childFiles = new ArrayList<>();
                for (File f : file.listFiles()) {
                    childFiles.add(f);
                }
                parseFilesSplitFiles(childFiles, isRecursive, isFilterByContext, sqlparser, queryStr);
            }
        }
    }

    private void addFile(File file, boolean isRecursive, List<File> files) {
        if (file.isFile()) {
            files.add(file);
        } else if (file.isDirectory() && isRecursive) {
            for (File f : file.listFiles()) {
                addFile(f, isRecursive, files);
            }
        }
    }

    private void parseFilesSplitFolder(List<File> files, boolean isRecursive, boolean isFilterByContext, SQLParser<TraceEvent> sqlparser, String queryStr) {
        List<File> filesList = new ArrayList<>();

        for (File file : files) {
            addFile(file, isRecursive, filesList);
        }

        List<File> folderList = new ArrayList<>();
        for (File file : filesList) {
            if (!folderList.contains(file.getParentFile())) {
                folderList.add(file.getParentFile());
            }
        }

        List<File> ListFolderFiles = new ArrayList<>();
        for (File folder : folderList) {
            ListFolderFiles.clear();
            for (File file : files) {
                if (folder.getAbsolutePath().equals(file.getParentFile().getAbsolutePath())) {
                    ListFolderFiles.add(file);
                }
            }
            IndexedCollection<TraceEvent> traceIndexedCollection = new ConcurrentIndexedCollection<>();
            parseFiles(ListFolderFiles, traceIndexedCollection, isRecursive, isFilterByContext);
            writeResultToFile(sqlparser.retrieve(traceIndexedCollection, queryStr));
        }
    }

    private void startConsoleMode() {
        IndexedCollection<TraceEvent> traceIndexedCollection = new ConcurrentIndexedCollection<>();

        SQLParser<TraceEvent> sqlparser = SQLParser.forPojo(TraceEvent.class);
        sqlparser.registerAttribute(TraceEvent.SOURCE);
        sqlparser.registerAttribute(TraceEvent.CONTEXT);
        sqlparser.registerAttribute(TraceEvent.SEVERITY);
        sqlparser.registerAttribute(TraceEvent.DATE);
        sqlparser.registerAttribute(TraceEvent.MESSAGE);
        sqlparser.registerAttribute(TraceEvent.FILE);
        sqlparser.registerValueParser(Date.class, new DateTimeParser());

        final String queryStr = "SELECT * FROM traceIndexedCollection" + arguments.getQuery() + " ORDER BY " + TraceViewUtils.getQueryOrder();

        if (ESplitArgs.SRC_FILE.position == 0) {
            parseFilesSplitFiles(openFiles, arguments.getCommandLineArg().hasOption("recursive"), ESplitArgs.CONTEXT.isIncluded, sqlparser, queryStr);
        } else if (ESplitArgs.SRC_FOLDER.position == 0) {
            parseFilesSplitFolder(openFiles, arguments.getCommandLineArg().hasOption("recursive"), ESplitArgs.CONTEXT.isIncluded, sqlparser, queryStr);
        } else {
            parseFiles(openFiles, traceIndexedCollection, arguments.getCommandLineArg().hasOption("recursive"), ESplitArgs.CONTEXT.isIncluded);
            writeResultToFile(sqlparser.retrieve(traceIndexedCollection, queryStr));
        }
    }

    private void writeResultToFile(ResultSet<TraceEvent> result) {
        StringBuilder buffer = new StringBuilder();
        TraceEvent templateEvent = null;

        for (TraceEvent event : result) {
            if (TraceViewUtils.isInGroup(event, templateEvent)) {
                buffer.append(event.toString()).append("\n");
            } else {
                printFile(buffer, templateEvent);
                templateEvent = event;
                buffer.append(event.toString()).append("\n");
            }
        }
        printFile(buffer, templateEvent);
    }

    private void printFile(StringBuilder buffer, TraceEvent templateEvent) {
        if (buffer.length() != 0) {
            try {
                File outputFile = new File(arguments.getOutputDir() + File.separator + arguments.getOutputFilePrefix() + TraceViewUtils.getFileName(templateEvent) + arguments.getOutputFileSuffix());
                if (!outputFile.exists()) {
                    if (!outputFile.createNewFile()) {
                        logger.log(Level.SEVERE, "Error on file creation:{0}", outputFile.getAbsolutePath());
                    }
                }
                try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile.getAbsoluteFile()), StandardCharsets.UTF_8))) {
                    out.write(buffer.toString());
                    logger.log(Level.INFO, "File created:{0}", outputFile.getAbsolutePath());
                    buffer.delete(0, buffer.length());
                }
            } catch (IOException e) {
                throw new RuntimeException("This directory does not exist: " + arguments.getOutputDir());
            }
        }
    }

}
