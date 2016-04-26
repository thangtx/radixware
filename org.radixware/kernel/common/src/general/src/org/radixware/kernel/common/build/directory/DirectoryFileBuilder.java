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

package org.radixware.kernel.common.build.directory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.schemas.product.Directory;
import org.radixware.schemas.product.DirectoryDocument;


public class DirectoryFileBuilder {

    File homeDir = null;
    private String homeDirPath = null;
    
    

    public static void main(String[] args) {
        try {
            new DirectoryFileBuilder().main0(args);
        } catch (Exception ex) {
            Logger.getLogger(DirectoryFileBuilder.class.getName()).log(Level.SEVERE, "Error: ", ex);
        }
    }

    public void main0(String[] args) throws IOException, NoSuchAlgorithmException {
        if (args.length < 3) {
            throw new IllegalArgumentException("Command line must be: DirectoryFile [-compact] excludeRegexp group1 regexp1  group2 regexp2 ...\n   for example: ../  KernelServer (server/bin/*.jar|(server/lib/*.jar)");
        }
        File directory = (new File(args[0])).getAbsoluteFile();
        homeDir = directory.getParentFile();
        homeDirPath = homeDir.getCanonicalPath();
        List<String> fileList = new ArrayList<String>(100);
        DirectoryDocument doc = DirectoryDocument.Factory.newInstance();
        Directory.FileGroups groups = doc.addNewDirectory().addNewFileGroups();
        boolean compact = false;
        int i = 1;
        if (args[i].equals("-compact")) {
            compact = true;
            i++;
        }
        String tailRegex = null;
        if (args[i].startsWith("-order-tail=")) {
            int index = args[i].indexOf("=");
            tailRegex = args[i].substring(index + 1);

            i++;
        }
        String excludeMask = args[i++];
        for (; i < args.length; i += 2) {
            System.out.print("Group=" + args[i] + " IncludeFiles=" + args[i + 1]);
            if (tailRegex != null) {
                System.out.println("Ordering hint= " + tailRegex);
            } else {
                System.out.println();
            }

            Directory.FileGroups.FileGroup.GroupType.Enum groupType = Directory.FileGroups.FileGroup.GroupType.Enum.forString(args[i]);
            if (groupType == null) {
                throw new IllegalArgumentException("Invalid group type: " + args[i]);
            }
            fileList.clear();

            listFiles(homeDirPath, homeDir, excludeMask, args[i + 1], tailRegex, fileList);
            System.out.println("    " + fileList.size() + " files found");
            if (fileList.isEmpty()) {
                continue;
            }
            Directory.FileGroups.FileGroup group = getGroup(groups, groupType);
            for (String path : fileList) {
                addFile(group, path, compact);
            }
        }

//        XmlOptions opt = new XmlOptions();
//        opt.setSaveNamespacesFirst();
//        opt.setUseDefaultNamespace();
//        opt.setSavePrettyPrint();
//        doc.save(directory, opt);
        XmlUtils.saveXmlPretty(doc, directory);
    }

    private boolean listFiles(String homeDirPath, File dir, String excludeRegexp, String includeRegexp, String tailRegexp, List<String> list) throws IOException {
        File[] allFiles = dir.listFiles();
        if (allFiles == null) {
            return false;
        }

        List<File> head = new LinkedList<>();
        List<File> tail = new LinkedList<>();
        if (tailRegexp != null) {
            for (File f : allFiles) {
                if (f.getName().matches(tailRegexp)) {
                    tail.add(f);
                } else {
                    head.add(f);
                }
            }
        } else {
            head.addAll(Arrays.asList(allFiles));
        }
        final Comparator<File> comparator = new Comparator<File>() {

            @Override
            public int compare(File f1, File f2) {
                String nwe1 = f1.getName();
                String nwe2 = f2.getName();
                String n1;
                String e1;
                int lp = nwe1.lastIndexOf('.');
                if (lp > 0 && lp < nwe1.length()) {
                    n1 = nwe1.substring(0, lp);
                    e1 = nwe1.substring(lp + 1);
                } else {
                    n1 = nwe1;
                    e1 = "";
                }
                //  System.out.println(nwe1 + " -> " + n1 + " and " + e1);
                String n2;
                String e2;
                lp = nwe2.lastIndexOf('.');
                if (lp > 0 && lp < nwe1.length()) {
                    n2 = nwe2.substring(0, lp);
                    e2 = nwe2.substring(lp + 1);
                } else {
                    n2 = nwe2;
                    e2 = "";
                }
                //     System.out.println(nwe2 + " -> " + n2 + " and " + e2);
                int result = n1.compareTo(n2);
                if (result != 0) {
                    return result;
                } else {
                    return e1.compareTo(e2);
                }


                //return f1.getName().compareTo(f2.getName());
            }
        };
        Collections.sort(head, comparator);
        Collections.sort(tail, comparator);
        head.addAll(tail);
        tail = null;

        List<File> directories = new LinkedList<>();
        for (int i = 0, len = head.size(); i < len; i++) {
            File f = head.get(i);
            String path = f.getCanonicalPath().substring(homeDirPath.length() + 1);
            path = path.replace(File.separatorChar, '/');
//        System.out.println(path);
            if (f.isDirectory()) {
                path += "/";
                if (!path.matches(excludeRegexp)) {
                    directories.add(f);
                }
            } else {
                if (!path.matches(excludeRegexp) && path.matches(includeRegexp)) {
                    list.add(path);
                }
            }
        }
        for (File f : directories) {
            listFiles(homeDirPath, f, excludeRegexp, includeRegexp, tailRegexp, list);
        }
        return !list.isEmpty();
    }

    private Directory.FileGroups.FileGroup getGroup(Directory.FileGroups groups, Directory.FileGroups.FileGroup.GroupType.Enum groupType) {
        for (Directory.FileGroups.FileGroup g : groups.getFileGroupList()) {
            if (g.getGroupType() == groupType) {
                return g;
            }
        }
        Directory.FileGroups.FileGroup g = groups.addNewFileGroup();
        g.setGroupType(groupType);
        return g;
    }

    private void addFile(Directory.FileGroups.FileGroup group, String path, boolean compact) throws IOException {
        Directory.FileGroups.FileGroup.File f = group.addNewFile();
        f.setName(path);
        if (!compact && path.endsWith(".jar")) {
            addJarContent(f, path);
        }
    }

    private void addJarContent(Directory.FileGroups.FileGroup.File f, String path) throws IOException {
        File jarFile = new File(homeDir, path);
        final FileInputStream fileInputStream = new FileInputStream(jarFile);
        try {
            final ZipInputStream jar = new ZipInputStream(fileInputStream);
            try {
                for (ZipEntry entry = jar.getNextEntry(); entry != null; entry = jar.getNextEntry()) {
                    if (!entry.isDirectory()) {
                        f.addNewEntry().setName(entry.getName());
                    }
                }
            } finally {
                jar.close();
            }
        } finally {
            fileInputStream.close();
        }
    }
}
