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

package org.radixware.kernel.common.svn;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.svn.client.ISvnFSClient;


public class SvnPathUtils {

    public static final String ROOT = "";

    public static String normalizePath(String path) {
        if (path == null) {
            throw new NullPointerException("Path is null.");
        }
        if (!path.isEmpty() && path.charAt(0) == '/') {
            path = path.substring(1);
        }

        path = path.replaceAll("//", "/");

        if (!path.isEmpty() && path.charAt(path.length() - 1) == '/') {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    public static String getParentDir(String dir) {
        int pos = dir.lastIndexOf('/');
        if (pos > 0) {
            return dir.substring(0, pos);
        } else {
            return ROOT;
        }
    }

    public static String removeHead(String dir) {
        if (dir == null || dir.isEmpty()) {
            return ROOT;
        }
        int pos = dir.charAt(0) == '/' ? 1 : 0;
        pos = dir.indexOf('/', pos);
        if (pos == -1) {
            return ROOT;
        }
        return dir.substring(pos);

    }
    
    public static String getFilePath(ISvnFSClient client, SVNRepositoryAdapter repository, File file) throws ISvnFSClient.SvnFsClientException, URISyntaxException, RadixSvnException{
        URI url = new URI(SVN.getFileUrl(client, file));
        String path = url.toString();
        String preffix = repository.getRepositoryRoot();
        
        return path.substring(preffix.length());
    }
}