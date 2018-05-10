
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComprehensiveTestUtils {

    private ComprehensiveTestUtils() {
    }

    static private int execProcess(final Process p, final String[] args, final StringBuilder out) {
        try {
            java.io.InputStream stdin = p.getInputStream();
            java.io.InputStreamReader isr = new java.io.InputStreamReader(stdin);
            java.io.BufferedReader br = new java.io.BufferedReader(isr);
            int result;
            boolean first = true;
            while (true){
                final String line = br.readLine();
                if (line==null) {
                    break;
                }
                
                if (out!=null) {
                    if (first){
                        first = false;
                    }
                    else{
                        out.append("\n");
                    }
                    out.append(line);
                }
                
            }
            
            result = p.waitFor();
            
            StringBuilder sb = new StringBuilder();
            boolean f = false;
            for (String arg : args) {
                if (!f) {
                    f = true;
                } else {
                    sb.append(" ");
                }
                sb.append(arg);
            }
            System.out.println("Process '" + sb.toString() + "' returned: " + result);
            return result;
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    static protected int execProcess(final String[] args, final StringBuilder out)  {
        try {
            Process p;
            p = Runtime.getRuntime().exec(args);          
            return execProcess(p, args, out);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    static protected int execProcess(final String[] args)  {
        return execProcess(args, null);
    }
    
    static private void recreateRep(final String repName)  {
        int t=0;
        t+=execProcess(new String[]{"rm", "-rf", "/home/sgorbunov/work/svn/" + repName});
        t+=execProcess(new String[]{"svnadmin", "create", "/home/sgorbunov/work/svn/" + repName});        
        t+=execProcess(new String[]{"cp",  "/home/sgorbunov/work/svn/svnserve.conf", "/home/sgorbunov/work/svn/" + repName + "/conf/svnserve.conf"});
        t+=execProcess(new String[]{"rm", "-r", "/home/sgorbunov/work/svn/" + repName + "/hooks/pre-revprop-change.tmpl"});
        t+=execProcess(new String[]{"cp", "/home/sgorbunov/work/svn/pre-revprop-change", "/home/sgorbunov/work/svn/"+ repName + "/hooks/pre-revprop-change" });
        t+=execProcess(new String[]{"chmod", "+x", "/home/sgorbunov/work/svn/" + repName + "/hooks/pre-revprop-change" });
        t+=execProcess(new String[]{"svnsync", "init", "svn://127.0.0.1/" + repName,"svn://127.0.0.1/rep1"});
        assert(t==0);
                
    }

    static protected void recreateRA_rep5() {
        recreateRep("rep5");
    }
    
    static protected void recreateRA_rep6() {
        recreateRep("rep6");
    }
        
    static protected String getURI_RA_rep5() {
      return "svn://10.7.2.138/rep5";
    }
    
    static protected String getURI_RA_rep6() {
      return "svn://10.7.2.138/rep6";
    }
    
    static protected String getURI_HTTP_rep2() {
      return "http://10.77.204.138/svn/rep2";
    }
    
    static protected String getURI_HTTP_rep3() {
      return "http://10.77.204.138/svn/rep3";
    }
    
    static private final String FIRST_WORK_DIR =  "/home/sgorbunov/work/ComprehensiveTestDir";
    static private final String SECOND_WORK_DIR =  "/home/sgorbunov/work/ComprehensiveTestDir2";
    
    static protected String getFirstWorkDirPath(){
        return FIRST_WORK_DIR;
    }
    
    static protected String getSecondWorkDirPath(){
        return SECOND_WORK_DIR;
    }
        
    static protected void recreateFirstWorkDir() {
        recreateWorkDir(getFirstWorkDirPath());
    }
        
    static protected void recreateSecondWorkDir() {
        recreateWorkDir(getSecondWorkDirPath());
    }
    
    static protected void recreateWorkDir(final String currPath) {
        try {
            int t=0;
            final Path dir = Paths.get(currPath);
            if (Files.exists(dir)){
                t+=execProcess(new String[]{"rm", "-r", currPath});
                assert(t==0);
            }
            Files.createDirectory(dir);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    

    /*

     rm -rf /home/sgorbunov/work/svn/rep6
     svnadmin create /home/sgorbunov/work/svn/rep6

     cp  /home/sgorbunov/work/svn/svnserve.conf /home/sgorbunov/work/svn/rep6/conf/svnserve.conf

     rm -r /home/sgorbunov/work/svn/rep6/hooks/pre-revprop-change.tmpl
     cp  /home/sgorbunov/work/svn/pre-revprop-change /home/sgorbunov/work/svn/rep6/hooks/pre-revprop-change
     chmod +x /home/sgorbunov/work/svn/rep6/hooks/pre-revprop-change

     #            to                   from              
     svnsync init svn://127.0.0.1/rep6 svn://127.0.0.1/rep1
    
    
     */
}
