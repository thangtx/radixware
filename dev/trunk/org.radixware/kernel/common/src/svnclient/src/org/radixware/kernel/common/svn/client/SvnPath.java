/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.svn.client;

import java.io.File;
import java.util.Collection;

/**
 *
 * @author akrylov
 */
public class SvnPath {

    public static String getPathAsChild(String path, String pathChild) {
        if (path == null || pathChild == null) {
            return null;
        }
        if (pathChild.compareTo(path) == 0) {
            return null;
        }
        if (path.length() == 0 && !pathChild.startsWith("/")) {
            return pathChild;
        }
        if (!path.endsWith("/")) { // We don't want to have /foobar being a child of /foo
            path = path + "/";
        }
        if (pathChild.startsWith(path)) {
            return pathChild.substring(path.length());
        }
        return null;
    }

    public static String condencePaths(String[] paths, Collection condencedPaths, boolean removeRedundantPaths) {
        if (paths == null || paths.length == 0) {
            return null;
        }
        if (paths.length == 1) {
            return paths[0];
        }
        String rootPath = paths[0];
        for (int i = 0; i < paths.length; i++) {
            String url = paths[i];
            rootPath = getCommonPathAncestor(rootPath, url);
        }
        if (condencedPaths != null && removeRedundantPaths) {
            for (int i = 0; i < paths.length; i++) {
                String path1 = paths[i];
                if (path1 == null) {
                    continue;
                }
                for (int j = 0; j < paths.length; j++) {
                    if (i == j) {
                        continue;
                    }
                    String path2 = paths[j];
                    if (path2 == null) {
                        continue;
                    }
                    String common = getCommonPathAncestor(path1, path2);

                    if ("".equals(common) || common == null) {
                        continue;
                    }
                    if (common.equals(path1)) {
                        paths[j] = null;
                    } else if (common.equals(path2)) {
                        paths[i] = null;
                    }
                }
            }
            for (int j = 0; j < paths.length; j++) {
                String path = paths[j];
                if (path != null && path.equals(rootPath)) {
                    paths[j] = null;
                }
            }
        }

        if (condencedPaths != null) {
            for (int i = 0; i < paths.length; i++) {
                String path = paths[i];
                if (path == null) {
                    continue;
                }
                if (rootPath != null && !"".equals(rootPath)) {
                    path = path.substring(rootPath.length());
                    if (path.startsWith("/")) {
                        path = path.substring(1);
                    }
                }
                condencedPaths.add(path);
            }
        }
        return rootPath;
    }

    public static boolean isHexDigit(char ch) {
        return Character.isDigit(ch)
                || (Character.toUpperCase(ch) >= 'A' && Character.toUpperCase(ch) <= 'F');
    }

    public static String getCommonPathAncestor(String[] paths) {
        if (paths == null || paths.length == 0) {
            return null;
        }
        if (paths.length == 1) {
            return paths[0];
        }
        String rootPath = paths[0];
        for (int i = 0; i < paths.length; i++) {
            String url = paths[i];
            rootPath = getCommonPathAncestor(rootPath, url);
        }
        return rootPath;
    }

    public static String getCommonPathAncestor(String path1, String path2) {
        if (path1 == null || path2 == null) {
            return null;
        }
        path1 = path1.replace(File.separatorChar, '/');
        path2 = path2.replace(File.separatorChar, '/');

        int index = 0;
        int separatorIndex = 0;
        while (index < path1.length() && index < path2.length()) {
            if (path1.charAt(index) != path2.charAt(index)) {
                break;
            }
            if (path1.charAt(index) == '/') {
                separatorIndex = index;
            }
            index++;
        }
        if (index == path1.length() && index == path2.length()) {
            return path1;
        } else if (index == path1.length() && path2.charAt(index) == '/') {
            return path1;
        } else if (index == path2.length() && path1.charAt(index) == '/') {
            return path2;
        }
        return path1.substring(0, separatorIndex);
    }

    public static String append(String path1, String path2) {
        if (path2 == null) {
            return path1;
        }
        if (path1 == null) {
            return path2;
        }
        if (path1.endsWith("/")) {
            path1 = path1.substring(0, path1.length() - 1);
        }
        if (path2.startsWith("/")) {
            path2 = path2.substring(1, path2.length());
        }
        return path1 + "/" + path2;
    }

    public static String tail(String path) {
        if (path == null) {
            return "";
        }
        int last = path.length() - 1;
        if (last >= 0 && last < path.length() && path.charAt(last) == '/') {
            last--;
        }
        for (int i = last; i >= 0; i--) {
            if (path.charAt(i) == '/') {
                return path.substring(i + 1, last + 1);
            }
        }
        return path;
    }

    public static String removeTail(String path) {
        int index = path.length() - 1;
        while (index >= 0) {
            if (path.charAt(index) == '/') {
                return path.substring(0, index);
            }
            index--;
        }
        return "";
    }

    public static String uriEncode(String src) {
        return src;
    }

    public static String uriDecode(String src) {
        return src;
    }

    static int getSegmentsCount(String path) {
        int count = path.length() > 0 ? 1 : 0;
        for (int i = 1; i < path.length(); i++) {
            if (path.charAt(i) == '/') {
                count++;
            }
        }
        return count;
    }

    public static String getRelativePath(String parent, String child) {
        parent = parent.replace(File.separatorChar, '/');
        child = child.replace(File.separatorChar, '/');
        String relativePath = getPathAsChild(parent, child);
        return relativePath == null ? "" : relativePath;
    }

    public static String removeHead(String path) {
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == '/') {
                int ind = i;
                for (; ind < path.length(); ind++) {
                    if (path.charAt(ind) == '/') {
                        continue;
                    }
                    break;
                }
                return path.substring(ind);
            }
        }
        return "";
    }
}
