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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import org.radixware.kernel.common.svn.RadixSvnException;

/**
 *
 * @author akrylov
 */
public class SvnHttpEditor implements SvnEditor, ISvnDeltaConsumer {

    private final SvnHttpConnection connection;
    private final SvnHttpRepository repo;
    private String activity;
    private boolean isAborted;

    private final Stack<DAV.Resource> dirsStack = new Stack<>();
    private final Map<String, String> pathsMap = new HashMap<>();
    private final Map<String, DAV.Resource> filesMap = new HashMap<>();
    private String baseChecksum;
    private String myActivityLocation;
    private File deltaFile;
    private OutputStream currentDelta = null;
    private boolean isFirstWindow = false;
    private final DAV.PathDataCache cache = new DAV.PathDataCache();

    public SvnHttpEditor(SvnHttpRepository repository) throws RadixSvnException {
        this.connection = new SvnHttpConnection();
        this.connection.open(repository);
        this.repo = repository;
    }

    private DAV.Resource getTopDir(long revision) throws RadixSvnException {
        if (dirsStack.isEmpty()) {
            openRoot(revision);
        }
        return dirsStack.peek();
    }

    @Override
    public void openRoot(long revision) throws RadixSvnException {
        final String[] activityUrls = createActivity();
        activity = activityUrls[0];
        myActivityLocation = activityUrls[1];
        DAV.Resource root = new DAV.Resource(connection, cache, "", revision);
        root.fetchVersionURL(null, false);
        dirsStack.push(root);
        pathsMap.put(root.getURL(), root.getPath());
    }

    @Override
    public void openDir(String path, long revision) throws RadixSvnException {
        path = SvnPath.uriEncode(path);
        // do nothing,
        DAV.Resource parent = getTopDir(revision);//dirsStack.peek() != null ? dirsStack.peek() : null;
        DAV.Resource directory = new DAV.Resource(connection, cache, path, revision, parent != null && parent.isCopy());
        if (parent != null && parent.getVersionURL() == null) {
            directory.setWorkingURL(SvnPath.append(parent.getWorkingURL(), SvnPath.tail(path)));
        } else {
            directory.fetchVersionURL(parent, false);
        }
        dirsStack.push(directory);
        pathsMap.put(directory.getURL(), directory.getPath());
    }

    @Override
    public void addDir(String path, String copyFromPath, long copyFromRevision) throws RadixSvnException {
        path = SvnPath.uriEncode(path);

        DAV.Resource parentResource = getTopDir(-1);//dirsStack.peek();
        checkoutResource(parentResource, true);
        String wPath = parentResource.getWorkingURL();

        DAV.Resource newDir = new DAV.Resource(connection, cache, path, -1, copyFromPath != null);
        newDir.setWorkingURL(SvnPath.append(wPath, SvnPath.tail(path)));
        newDir.setAdded(true);

        dirsStack.push(newDir);
        pathsMap.put(newDir.getURL(), path);
        if (copyFromPath != null) {
            // convert to full path?
            while (copyFromPath.startsWith("/")) {
                copyFromPath = copyFromPath.substring(1);
            }
            copyFromPath = SvnPath.uriEncode(copyFromPath);
            DAV.BaselineInfo info = repo.getBaselineInfo(connection, repo, copyFromPath, copyFromRevision, false, false, null);
            copyFromPath = SvnPath.append(info.baselineBase, info.baselinePath);

            URI loc = repo.getLocation();
            try {
                URI uri = new URI(loc.getScheme(), loc.getAuthority(), loc.getHost(), loc.getPort(), newDir.getWorkingURL(), loc.getQuery(), loc.getFragment());
                connection.copy(copyFromPath, uri.toString(), 1);
            } catch (URISyntaxException ex) {
                throw new RadixSvnException(ex);
            }
        } else {
            try {
                connection.makeCollection(newDir.getWorkingURL());
            } catch (RadixSvnException e) {
                // check if dir already exists.

                DAV.BaselineInfo info = repo.getBaselineInfo(connection, repo, newDir.getURL(), -1, false, false, null);
                if (info != null) {
                    throw new RadixSvnException("Path " + newDir.getURL() + " already exists");
                }

                throw e;
            }
        }
    }

    @Override
    public void closeDir() throws RadixSvnException {
        DAV.Resource resource = dirsStack.pop();
        resource.dispose();
    }

    @Override
    public void deleteEntry(String path, long revision) throws RadixSvnException {
        DAV.Resource parentResource = getTopDir(-1);
        checkoutResource(parentResource, true);
        String relativePath = SvnPath.getRelativePath(parentResource.getPath(), path);

        String wPath = SvnPath.append(parentResource.getWorkingURL(), relativePath);
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        connection.delete(path, wPath, revision);
        pathsMap.put(SvnPath.append(parentResource.getURL(), path), path);
    }

    @Override
    public void abortEdit() throws RadixSvnException {
        if (isAborted) {
            return;
        }
        isAborted = true;
        try {
            try {
                if (activity != null) {
                    connection.deleteUrl(activity);
                }
            } finally {
                // dispose all resources!
                if (filesMap != null) {
                    for (Iterator<DAV.Resource> files = filesMap.values().iterator(); files.hasNext();) {
                        DAV.Resource file = files.next();
                        file.dispose();
                    }
                    filesMap.clear();
                }
                for (Iterator<DAV.Resource> files = dirsStack.iterator(); files.hasNext();) {
                    DAV.Resource resource = files.next();
                    resource.dispose();
                }
                dirsStack.clear();
            }
        } finally {
            runCloseCallback();
        }
    }

    @Override
    public SvnCommitSummary closeEdit() throws RadixSvnException {
        try {
            if (!dirsStack.isEmpty()) {
                DAV.Resource resource = dirsStack.pop();
                resource.dispose();
            }

            patchResourceProperties(myActivityLocation);

            return connection.merge(activity, true);
        } finally {
            // we should run abort edit if exception is thrown
            // abort edit will not be run if there was an error (from server side) on MERGE.
            try {
                abortEdit();
            } catch (RadixSvnException e) {
                e.printStackTrace();
            }

            // always run close callback to 'unlock' SVNRepository.
            runCloseCallback();
        }
    }

    private void runCloseCallback() {
    }

    @Override
    public void appendFile(String path, long revision, InputStream data) throws RadixSvnException {
        addFile(path, null, revision);
        applyTextDelta(path, null);
        String chk_sum = new SvnDeltaGenerator().sendDelta(path, data, this, true);
        closeFile(path, chk_sum);

    }

    @Override
    public void appendFile(String path, long revision, String content) throws RadixSvnException {
        addFile(path, null, revision);
        applyTextDelta(path, null);
        String chk_sum = new SvnDeltaGenerator().sendDelta(path, new ByteArrayInputStream(content.getBytes()), this, true);
        closeFile(path, chk_sum);

    }

    @Override
    public void addFile(String path, String copyFromPath, long revision) throws RadixSvnException {
        String originalPath = path;
        path = SvnPath.uriEncode(path);
        // checkout parent collection.
        DAV.Resource parentResource = getTopDir(-1);//dirsStack.peek();
        checkoutResource(parentResource, true);
        String wPath = parentResource.getWorkingURL();
        // create child resource.
        DAV.Resource newFile = new DAV.Resource(connection, cache, path, revision);
        newFile.setWorkingURL(SvnPath.append(wPath, SvnPath.tail(path)));

        if (!parentResource.isAdded() && !pathsMap.containsKey(newFile.getURL())) {
            String filePath = SvnPath.append(parentResource.getURL(), SvnPath.tail(path));
            try {
                repo.getResourceProperties(connection, filePath, null, DAV.Element.STARTING_PROPERTIES);
            } catch (RadixSvnException e) {
                if (e.tag != 404) {
                    throw e;
                }

            }
            try {
                repo.getResourceProperties(connection, newFile.getWorkingURL(), null, DAV.Element.STARTING_PROPERTIES);
            } catch (RadixSvnException e) {
                if (e.tag != 404) {
                    throw e;
                }
            }

        }
        // put to have working URL to make PUT or PROPPATCH later (in closeFile())
        pathsMap.put(newFile.getURL(), newFile.getPath());
        filesMap.put(originalPath, newFile);

        newFile.setAdded(true);
    }

    @Override
    public void updateFile(String path, long revision, String content) throws RadixSvnException {
        openFile(path, revision);
        applyTextDelta(path, null);
        String chk_sum = new SvnDeltaGenerator().sendDelta(path, new ByteArrayInputStream(content.getBytes()), this, true);
        closeFile(path, chk_sum);
    }

    @Override
    public void updateFile(String path, long revision, InputStream content) throws RadixSvnException {
        SvnEntry.Kind kind = repo.checkPath(path, revision);
        System.out.print(kind);
                
        openFile(path, revision);
        applyTextDelta(path, null);
        String chk_sum = new SvnDeltaGenerator().sendDelta(path, content, this, true);
        closeFile(path, chk_sum);
    }

    @Override
    public OutputStream textDeltaChunk(String path, SvnDiffWindow diffWindow) throws RadixSvnException {
        try {
            if (currentDelta == null) {
                deltaFile = File.createTempFile("radix_svn_delta", ".tmp");
                currentDelta = new FileOutputStream(deltaFile);
            }

            diffWindow.writeTo(currentDelta, isFirstWindow);
            isFirstWindow = false;
            return DUMMY_STREAM;
        } catch (IOException e) {
            try {
                currentDelta.close();
            } catch (IOException ex) {
            }
            FileUtils.deleteFile(deltaFile);
            deltaFile = null;
            currentDelta = null;
            throw new RadixSvnException("Unable to send patch data", e);
        }
    }

    @Override
    public void textDeltaEnd(String path) throws RadixSvnException {
        try {
            currentDelta.close();
        } catch (IOException ex) {
        }
    }

    @Override
    public void openFile(String path, long revision) throws RadixSvnException {
        String originalPath = path;
        path = SvnPath.uriEncode(path);
        DAV.Resource parent = getTopDir(revision);//dirsStack.peek();
        DAV.Resource file = new DAV.Resource(connection, cache, path, revision);
        if (parent.getVersionURL() == null) {            
            file.setWorkingURL(SvnPath.append(parent.getWorkingURL(), SvnPath.tail(path)));
        } else {
            file.fetchVersionURL(parent, false);
        }
        checkoutResource(file, true);
        pathsMap.put(file.getURL(), file.getPath());
        filesMap.put(originalPath, file);
    }

    @Override
    public void applyTextDelta(String path, String baseChecksum) throws RadixSvnException {
        currentDelta = null;
        isFirstWindow = true;
        deltaFile = null;
        this.baseChecksum = baseChecksum;
    }

    @Override
    public void closeFile(String path, String textChecksum) throws RadixSvnException {
        DAV.Resource currentFile = filesMap.get(path);
        try {
            if (deltaFile != null) {
                InputStream combinedData = null;
                try {
                    combinedData = new FileInputStream(deltaFile);
                    connection.putDiff(currentFile.getURL(), currentFile.getWorkingURL(), combinedData, deltaFile.length(),
                            baseChecksum, textChecksum);
                } catch (IOException ex) {
                    throw new RadixSvnException("Can not read delta file content", ex);
                } catch (RadixSvnException e) {
                    throw e;
                } finally {
                    if (combinedData != null) {
                        try {
                            combinedData.close();
                        } catch (IOException ex) {
                        }
                    }
                    FileUtils.deleteFile(deltaFile);
                    deltaFile = null;
                }
            }

        } finally {
            currentFile.dispose();
            currentDelta = null;
            baseChecksum = null;
            filesMap.remove(path);
        }
    }

    private String[] createActivity() throws RadixSvnException {
        final String activity = connection.makeActivity();
        final String path = SvnPath.uriEncode(connection.getLocation().getPath());
        final String vcc = connection.getRepository().getPropertyValue(connection, path, null, DAV.Element.VERSION_CONTROLLED_CONFIGURATION);

        String location = null;
        for (int i = 0; i < 5; i++) {
            String head = connection.getRepository().getPropertyValue(connection, vcc, null, DAV.Element.CHECKED_IN);
            try {
                location = connection.checkout(activity, null, head, false);
                if (location != null) {
                    break;
                }
            } catch (RadixSvnException svne) {
                throw svne;
            }
        }
        if (location == null) {
            throw new RadixSvnException("The CHECKOUT response did not contain a 'Location:' header");
        }
        return new String[]{activity, location};
    }

    private void checkoutResource(DAV.Resource resource, boolean allow404) throws RadixSvnException {
        if (resource.getWorkingURL() != null) {
            return;
        }
        String status = null;
        try {
            status = connection.checkout(activity, resource.getURL(), resource.getVersionURL(), allow404);
            if (allow404 && status == null) {
                resource.fetchVersionURL(null, true);
                status = connection.checkout(activity, resource.getURL(), resource.getVersionURL(), false);
            }
        } catch (RadixSvnException e) {
            throw e;
        }
        if (status == null) {
            throw new RadixSvnException("The CHECKOUT response did not contain a 'Location:' header");
        }
        resource.setWorkingURL(status);
    }

    private void patchResourceProperties(String path) throws RadixSvnException {
//        if (properties != null && properties.size() > 0) {
//            final StringBuffer request = DAVProppatchHandler.generatePropertyRequest(null, properties);
//            try {
//                myConnection.doProppatch(null, path, request, null, null);
//            } catch (SVNException e) {
//                SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.RA_DAV_REQUEST_FAILED, "applying log message to {0}", path);
//                SVNErrorManager.error(err, SVNLogType.NETWORK);
//            }
//        }
    }

}
