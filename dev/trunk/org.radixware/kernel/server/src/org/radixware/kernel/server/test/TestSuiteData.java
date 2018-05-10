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

package org.radixware.kernel.server.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.radixware.kernel.server.SrvRunParams;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class TestSuiteData extends TestDurationData implements Iterable<TestData> {

    private final List<TestData> tests = new ArrayList<TestData>(100);
    private int failedCount = 0;
    private final String name;

    public TestSuiteData() {
        this(null);
    }

    public TestSuiteData(String name) {
        this.name = name;
    }

    public final void regTest(final TestData t) {
        tests.add(t);
        if (!t.isOk()) {
            failedCount++;
        }
    }
    private static final String ILLEGAL_CHARS_CLASS = "[^\\u0009\\u000a\\u000d\\u0020-\\uD7FF\\uE000-\\uFFFD]";

    public final void writeToStream(final OutputStream os) throws IOException {
        final Pattern illegalCharsPattern = Pattern.compile(ILLEGAL_CHARS_CLASS);
        try {
            final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            final Element root = doc.createElement("testsuites");
            doc.appendChild(root);
            for (TestData t : tests) {
                final Element suiteElement = doc.createElement("testsuite");
                root.appendChild(suiteElement);
                suiteElement.setAttribute("errors", String.valueOf(t.isOk() ? 0 : 1));
                suiteElement.setAttribute("failures", "0");
                suiteElement.setAttribute("hostname", SrvRunParams.getDbUrl());
                suiteElement.setAttribute("name", SrvRunParams.getDbSchema() + ".TESTCASES");
                suiteElement.setAttribute("tests", String.valueOf(1));
                suiteElement.setAttribute("time", String.valueOf(0.0010 * t.durationMillis));

                final Element test = doc.createElement("testcase");
                suiteElement.appendChild(test);
                final String className;
                if (name == null || name.isEmpty()) {
                    className = t.getTitle();
                } else {
                    className = name + "." + t.getTitle();
                }
                test.setAttribute("classname", t.getTitle());
                final String testName;
                if (t.getNotes() == null || t.getNotes().isEmpty()) {
                    testName = "_";
                } else {
                    testName = t.getNotes();
                }
                test.setAttribute("name", testName);
                test.setAttribute("time", String.valueOf(0.0010 * t.getDurationMillis()));
                if (!t.isOk()) {
                    final Element err = doc.createElement("error");
                    test.appendChild(err);
                    String error = illegalCharsPattern.matcher(t.getError()).replaceAll("");
                    err.setAttribute("message", "Error:");
                    final CDATASection stderrCDATA = doc.createCDATASection(error);
                    err.appendChild(stderrCDATA);
                }

                if (t.getStdout() != null) {
                    final Element stdout = doc.createElement("system-out");
                    String stdoutString = illegalCharsPattern.matcher(t.getStdout()).replaceAll("");
                    test.appendChild(stdout);
                    final CDATASection stdoutCDATA = doc.createCDATASection(stdoutString);
                    stdout.appendChild(stdoutCDATA);
                }
            }
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Transformer transformer = transformerFactory.newTransformer();
            final DOMSource source = new DOMSource(doc);
            final StreamResult result = new StreamResult(os);
            transformer.transform(source, result);
            os.flush();
        } catch (ParserConfigurationException | DOMException | TransformerException ex) {
            throw new IOException(ex);
        }
    }

    public final void writeToFile(final String fileName) throws IOException {
        final File file = new File(fileName);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            writeToStream(fos);
        }
    }

    @Override
    public Iterator<TestData> iterator() {
        return tests.iterator();
    }
}
