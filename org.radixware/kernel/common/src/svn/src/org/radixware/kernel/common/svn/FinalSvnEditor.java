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


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter.Editor;




public class FinalSvnEditor {

    private final Editor editor;
    private final LinkedList<String> dirStack = new LinkedList<>();
    

    public FinalSvnEditor(final SVNRepositoryAdapter repository, final String commitMessage) throws RadixSvnException {

        editor = repository.createEditor(commitMessage);
//        try{
            editor.openDir(null);
            //editor.openRoot(-1);
//        }
//        catch (SVNAuthenticationException ex) {
//            editor.abortEdit();
//            throw ex;
//        }
        dirStack.add(SvnPathUtils.ROOT);
    }

    public void commit() throws RadixSvnException {
        while (!dirStack.isEmpty()) {
            editor.closeDir();
            dirStack.removeLast();
        }
//        editor.closeEdit();
        editor.commit();
    }

    public void abort() throws RadixSvnException {
        editor.cancel();
    }

    private void openPath(final String path) throws RadixSvnException {
        
        
        final String parentDir = SvnPathUtils.getParentDir(path);
        final String currentDir = dirStack.getLast();
        if (currentDir.equals(parentDir)) {
            return;
        }

        final List<String> fromParentToCur = new ArrayList<String>();

        String cur = parentDir;
        while (true) {
            int idx = dirStack.indexOf(cur);
            if (idx >= 0) {
                while (!dirStack.getLast().equals(cur)) {
                    editor.closeDir();
                    dirStack.removeLast();
                }
                break;
            }
            if (SvnPathUtils.ROOT.equals(cur)) {
                break;
            }
            fromParentToCur.add(cur);
            cur = SvnPathUtils.getParentDir(cur);
        }

        for (int i = fromParentToCur.size() - 1; i >= 0; i--) {
            final String dir = fromParentToCur.get(i);
            editor.openDir(dir);
            dirStack.add(dir);
        }
    }

    public void appendDir(String path) throws RadixSvnException {
        
        
        path = SvnPathUtils.normalizePath(path);
        openPath(path);

        editor.appendDir(path);
        dirStack.add(path);
    }

    private void appendFile(String path, InputStream data) throws RadixSvnException {
        
        
        path = SvnPathUtils.normalizePath(path);
        openPath(path);

        editor.appendFile(path, data);
    }

    public void appendFile(String path, byte data[]) throws RadixSvnException, IOException {
        
        
        try (ByteArrayInputStream is = new ByteArrayInputStream(data)) {
            appendFile(path, is);
        }
    }


    
    private void modifyFile(String path, final InputStream data) throws RadixSvnException {
        

        
        path = SvnPathUtils.normalizePath(path);
        openPath(path);
        
        editor.modifyFile(path, data);
    }

    public void modifyFile(String path, byte data[]) throws RadixSvnException, IOException {
        try (ByteArrayInputStream is = new ByteArrayInputStream(data)) {
            modifyFile(path, is);
        }
    }

    public void copyDir(String fromDir, String toDir, long fromRevision) throws RadixSvnException {
        
        
        
        fromDir = SvnPathUtils.normalizePath(fromDir);
        toDir = SvnPathUtils.normalizePath(toDir);
        openPath(toDir);

        editor.copyDir(fromDir, toDir, fromRevision);
        //editor.addDir(toDir, fromDir, fromRevision);
        dirStack.add(toDir);
    }

    public void copyFile(String fromFile, String toFile, long fromRevision) throws RadixSvnException {
        
        fromFile = SvnPathUtils.normalizePath(fromFile);
        toFile = SvnPathUtils.normalizePath(toFile);
        openPath(toFile);

        editor.copyFile(fromFile, toFile, fromRevision); 
        //editor.addFile(toFile, fromFile, fromRevision);
    }

    public void deleteEntry(String entry/*, long revision*/
            //RADIX-11104
    ) throws RadixSvnException {
        
        entry = SvnPathUtils.normalizePath(entry);
        openPath(entry);

        editor.deleteEntry(entry, -1);
    }    
    
    public Editor getEditor() {
        return editor;
    }
}