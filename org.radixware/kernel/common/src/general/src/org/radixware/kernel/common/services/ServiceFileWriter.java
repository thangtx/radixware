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

package org.radixware.kernel.common.services;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;


public class ServiceFileWriter {

    private ProcessingEnvironment processingEnv;
    private Map<String, List<String>> service2entries = new HashMap();
    private final Object lock = new Object();
    private static final String serviceFolder = "META-INF/services/";

    public ServiceFileWriter(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    public void addEntry(String service, TypeElement implementation) {
        synchronized (lock) {
            List<String> list = service2entries.get(service);
            if (list == null) {
                list = new LinkedList<String>();
            }
            service2entries.put(service, list);
            String entry = ProcessorUtilities.createStringEntry(implementation);
            list.add(entry);
            processingEnv.getMessager().printMessage(Kind.OTHER, "Queued service entry for " + service + ": " + entry);
        }
    }

    public void write() {
        synchronized (lock) {
            for (Entry<String, List<String>> serviceListEntry : service2entries.entrySet()) {
                String service = serviceListEntry.getKey();

                Set<String> entries = getExistingEntries(service);
                if (entries == null) {
                    entries = new HashSet<String>();
                }

                List<String> entryList = serviceListEntry.getValue();
                for (String newEntry : entryList) {
                    if (entries.contains(newEntry)) {
                        processingEnv.getMessager().printMessage(Kind.OTHER, "Entry " + newEntry + " for service " + service + " already exists");
                    } else {
                        entries.add(newEntry);
                        processingEnv.getMessager().printMessage(Kind.NOTE, "Generated service entry for " + service + ": " + newEntry);
                    }
                }

                writeEntries(entries, service);
            }
        }
    }

    private Set<String> getExistingEntries(String service) {
        Set<String> existingEntries = new HashSet<String>();
        Scanner sc = null;
        try {
            String fileName = serviceFolder + service;
            FileObject fo = processingEnv.getFiler().getResource(StandardLocation.CLASS_OUTPUT, "", fileName);
            InputStream is = fo.openInputStream();
            sc = new Scanner(is);
            while (sc.hasNext()) {
                existingEntries.add(sc.nextLine());
            }

        } catch (IOException ex) {
            processingEnv.getMessager().printMessage(Kind.OTHER, "No existing entries for service " + service);
        } finally {
            if (sc != null) {
                sc.close();
            }
        }
        return existingEntries;
    }

    private void writeEntries(Set<String> entries, String service) {
        String fileName = serviceFolder + service;
        PrintWriter pw = null;
        try {
            FileObject fo = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", fileName);
            Writer writer = fo.openWriter();
            pw = new PrintWriter(new BufferedWriter(writer));

            for (String entry : entries) {
                pw.println(entry);
            }
            pw.flush();
        } catch (IOException ex) {
            printFlushException(ex);
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    protected void printFlushException(Exception ex) {
        processingEnv.getMessager().printMessage(Kind.WARNING, "Can not write service entries. Reason:\n" + ex);
    }
}
