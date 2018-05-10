
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
package org.radixware.kernel.common.svn.client;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import org.junit.Test;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter.Editor;
import org.radixware.kernel.common.svn.SVNRepositoryExtendedOptions;

public class ComprehensiveTest {
    
    
  private static  void copyRevisionProperties(
            SvnCommitSummary info,
            SVNRepositoryAdapter fromRepository, 
            SVNRepositoryAdapter toRepository,
            long revision) throws RadixSvnException {
        Map<String, SvnProperties.Value>  revProps = fromRepository.getRevisionProperties(revision).map();
        
        if (revProps.size()!=2){
//            throw new RuntimeException();
        }

        boolean isLog = false;

        for (String propName : revProps.keySet()) {
            SvnProperties.Value propValue = revProps.get(propName);
   
            toRepository.setRevisionProperty(revision, propName, propValue); //setRevisionPropertyValue(revision, propName,propValue);
            if (!isLog) {
                isLog = true;
                System.out.println("Revision " + String.valueOf(info.revision) + ", " + propValue.getValue());
            } 
        }
    }

    private static long synchronizeRepository(SVNRepositoryAdapter fromRepos,
            SVNRepositoryAdapter toRepos) throws RadixSvnException  {
        long lastMergedRevision = toRepos.getLatestRevision();
        long fromLatestRevision = fromRepos.getLatestRevision();
        long count = 0;
        for (long currentRev = lastMergedRevision + 1; currentRev <= fromLatestRevision; currentRev++) {

            
            
            
            final Editor svnEditor = toRepos.createEditor("Check write access");
            
            SVNRepositoryAdapter.Editor commitEditor = svnEditor;
            fromRepos.replay(0, currentRev, true, commitEditor);

            SvnCommitSummary  info = commitEditor.commit();

            if (info.revision != currentRev) {

                throw  new RuntimeException(
                        "Commit created rev "
                        + info.revision
                        + " but should have created " + currentRev);
            }
            copyRevisionProperties(info, fromRepos, toRepos, currentRev);
            count++;
        }
        return count;
    }    
    
    

    private static enum RepositoryType {

        RA, HTTP
    }

    private static enum RepositoryTestType {

        single(RepositoryType.RA);

        private RepositoryTestType(final RepositoryType value) {
            this.value = value;
        }
        RepositoryType value;

    };

    private SVNRepositoryAdapter reсreateSingleRA1() throws Throwable {
        ComprehensiveTestUtils.recreateRA_rep6();
        final SVNRepositoryAdapter svn = SVNRepositoryAdapter.Factory.newInstance(ComprehensiveTestUtils.getURI_RA_rep6(), "", "svn", SvnAuthType.NONE, (String)null);
        return svn;
    }

  private SVNRepositoryAdapter reсreateSingleRA2() throws Throwable {
        ComprehensiveTestUtils.recreateRA_rep5();
        final SVNRepositoryAdapter svn = SVNRepositoryAdapter.Factory.newInstance(ComprehensiveTestUtils.getURI_RA_rep5(), "", "svn", SvnAuthType.NONE, (String)null);
        return svn;
    }
            
 private SVNRepositoryAdapter reсreateSingleHTTP(final String uri) throws Throwable {
        final SVNRepositoryAdapter svn = SVNRepositoryAdapter.Factory.newInstance(
//                        (SVNRepositoryExtendedOptions)null, 
                uri, "", "svn", SvnAuthType.SVN_PASSWORD, (String)null, new ISvnPasswordProvider() {

            @Override
            public char[] getPassword(boolean firstAttempt, SvnAuthType authType, String message, String targetTitle, String targetLocation, String userName) throws RadixSvnException {
                return "svn".toCharArray();
            }
        });
        return svn;
    }
  
 

    @Test
    public void test1() throws Throwable {
        

        {       
        final SVNRepositoryAdapter repository1 = reсreateSingleHTTP(ComprehensiveTestUtils.getURI_HTTP_rep2());
        final SVNRepositoryAdapter repository2 = reсreateSingleHTTP(ComprehensiveTestUtils.getURI_HTTP_rep3());
        singleCheckRepository(repository1, repository2, 0);
        }        
        
        
        {
        final SVNRepositoryAdapter repository1 = reсreateSingleRA1();
        final SVNRepositoryAdapter repository2 = reсreateSingleRA2();
        singleCheckRepository(repository1, repository2, 0);
        }
    }

    void singleCheckRepository(final SVNRepositoryAdapter masterRepository, final SVNRepositoryAdapter slaveRepository, long currRevision) throws RadixSvnException {
        assert (masterRepository.getLatestRevision() == currRevision);

        {
            final Editor editor = masterRepository.createEditor("");
            //editor.openRoot(-1);
            editor.cancel();
            assert (masterRepository.getLatestRevision() == currRevision);
        }

        {
            final Editor editor = masterRepository.createEditor("SSSSSSSSS1111111");
            editor.appendDir("dir1");
            editor.commit();
            currRevision++;
            assert (masterRepository.getLatestRevision() == currRevision);
        }

        {
            final Editor editor = masterRepository.createEditor("SSSSSSSSS222222222");
            editor.appendDir("dir2");
            editor.appendDir("dir2/dir22");
            editor.appendDir("dir2/dir22/dir222");
            editor.commit();
            currRevision++;
            assert (masterRepository.getLatestRevision() == currRevision);
        }

        {
            final Editor editor = masterRepository.createEditor("SSSSSSSSS3333333333");
            editor.appendDir("dir3");
            editor.appendDir("dir3/dir33");
            editor.appendDir("dir3/dir33/dir333");
            editor.closeDirs(2);
            editor.appendFile("dir3/data31.txt",  "__data31".getBytes());
            editor.appendFile("dir3/data32.txt", "__data32".getBytes());
            editor.appendFile("dir3/data33.txt", "__data33".getBytes());
            editor.appendFile("dir3/data34.txt", "__data34".getBytes());
            editor.commit();
            currRevision++;
            assert (masterRepository.getLatestRevision() == currRevision);
        }
        
        {
            final Editor editor = masterRepository.createEditor("SSSSSSSSS4444444444");
            
            //editor.openDir("dir3");
            //editor.appendDir("dir4");
            editor.openDir("");
            editor.copyDir("dir3", "dir4", currRevision);
            editor.closeDir(); 
            editor.closeDir(); 
            //editor.closeDirs(t);
            editor.commit(); 
            currRevision++;
            assert (masterRepository.getLatestRevision() == currRevision);
        }  
        

        ComprehensiveTestUtils.recreateFirstWorkDir();
        final String firstWirkDir = ComprehensiveTestUtils.getFirstWorkDirPath() + "/";

        assert (ComprehensiveTestUtils.execProcess(new String[]{
            "svn",
            "checkout",
            masterRepository.getRepositoryRoot(),
            ComprehensiveTestUtils.getFirstWorkDirPath()
                
             ,"--username", "svn",
             "--password",  "svn"
        }) == 0);

        SvnPropManager.setProp(firstWirkDir + "dir3/data31.txt", "PROP_AAA", "PROP_VAL_111");
        currRevision++;
        assert (masterRepository.getLatestRevision() == currRevision);
        SvnPropManager.checkPropExist(firstWirkDir + "dir3/data31.txt", "PROP_AAA", "PROP_VAL_111");

        SvnPropManager.setProp(firstWirkDir + "dir3/data32.txt", "PROP_BBB", "PROP_VAL_222");
        currRevision++;
        assert (masterRepository.getLatestRevision() == currRevision);
        SvnPropManager.checkPropExist(firstWirkDir + "dir3/data32.txt", "PROP_BBB", "PROP_VAL_222");
        
        
        move(firstWirkDir + "dir3/data32.txt", firstWirkDir + "dir2/data32.txt");
        currRevision++;
        assert (masterRepository.getLatestRevision() == currRevision);   
        
        
        replace(firstWirkDir + "dir2/data32.txt");
        replace(firstWirkDir + "dir3/data34.txt");
        
        update(firstWirkDir);
        
        
        synchronizeRepository(masterRepository, slaveRepository);
        
        ComprehensiveTestUtils.recreateSecondWorkDir();
        
        assert (ComprehensiveTestUtils.execProcess(new String[]{
            "svn",
            "checkout",
            slaveRepository.getRepositoryRoot(),
            ComprehensiveTestUtils.getSecondWorkDirPath()
        }) == 0);
        
        final String secondWorkDir = ComprehensiveTestUtils.getSecondWorkDirPath() + "/";
        SvnPropManager.checkPropExist(secondWorkDir  + "dir3/data31.txt", "PROP_AAA", "PROP_VAL_111");
        
        
    }
    
    
    static private void update(final String file) {
        checkFileExists(file);
        assert (ComprehensiveTestUtils.execProcess(new String[]{
            "svn",
            "up",
            file
                ,"--username", "svn",
                    "--password",  "svn"
        }) == 0);
    }
    
    static private void replace(final String file) {
        checkFileExists(file);
        assert (ComprehensiveTestUtils.execProcess(new String[]{
            "svn",
            "del",
            file
                ,"--username", "svn",
                    "--password",  "svn"
        }) == 0);
        
        try {
            FileUtils.appendToFile(new File(file), "replaced_file_Content");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        assert (ComprehensiveTestUtils.execProcess(new String[]{
            "svn",
            "add",
            file
                ,"--username", "svn",
                    "--password",  "svn"
        }) == 0);
        
        commit("test comment", file);        
        
    }

    static private void move(final String fromFile, final String toFile) {
        checkFileExists(fromFile);
        assert (ComprehensiveTestUtils.execProcess(new String[]{
            "svn",
            "move",
            fromFile,
            toFile
            ,"--username", "svn",
                "--password",  "svn"
        }) == 0);
        commit("test comment", fromFile, toFile);
    }

    static private void checkFileExists(final String pathToFile) {
        final Path path = Paths.get(pathToFile);
        assert (Files.exists(path));
    }

    static private void commit(final String comment, final String... pathToFiles) {
        final String[] args = new String[pathToFiles.length + 7];
        args[0] = "svn";
        args[1] = "commit";
        args[2] = "-m \"" + comment + "\"";
        args[3] = "--username";
        args[4] = "svn";
        args[5] = "--password";
        args[6] = "svn";

        int i = 7;
        for (String path : pathToFiles) {
            args[i] = path;
            i++;
        }
        assert (ComprehensiveTestUtils.execProcess(args) == 0);
    }

    static private class SvnPropManager {

        static public void setProp(final String pathToFile, final String propName, final String propData) {
            checkFileExists(pathToFile);
            assert (ComprehensiveTestUtils.execProcess(new String[]{
                "svn",
                "propset",
                propName,
                propData,
                pathToFile
                    ,"--username", "svn",
                    "--password",  "svn"
            }) == 0);

            commit(" Set property " + propName + " value : " + propData, pathToFile);
        }

        static public void checkPropExist(final String pathToFile, final String propName, final String propData) {
            checkFileExists(pathToFile);

            final StringBuilder out = new StringBuilder();

            assert (ComprehensiveTestUtils.execProcess(new String[]{
                "svn",
                "propget",
                propName,
                pathToFile
                ,"--username", "svn",
                "--password",  "svn"
            }, out) == 0);

            assert (out.toString().contains(propData));
        }
    }

}
