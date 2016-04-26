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

package org.radixware.kernel.common.build.xbeans;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.impl.common.JarHelper;
import org.apache.xmlbeans.impl.tool.CommandLine;

public class XbeansSchemaCompilerAdapter {

    static final String THIS_PACKAGE = "org.radixware.kernel.common.build.xbeans";

    public static void main(String[] args) {
        try {                        
            new BatchCompiler(args).compile();
        } catch (RuntimeException e) {
            java.util.logging.Logger.getLogger(XbeansSchemaCompilerAdapter.class.getName()).log(Level.SEVERE, "XBeans code print failure", e);
        }
    }

    public static abstract class Compiler extends XbeansSchemaCompiler.MessagePrinter {

        public Compiler(boolean noisy, URI baseURI) {
            super(noisy, baseURI);
        }

        protected final void compile(String schemaFileName, String sourceDirPath, String configFilePath, boolean compileCode, String classPath, String resultJarPath, boolean quiet, boolean verbose, boolean noRadixEnums, String memorySize, String memoryMaxSize) {
            try {
                Set<String> flags = new HashSet<>();
                flags.add("compile");
                flags.add("quiet");
                flags.add("verbose");
                flags.add("noradivenums");

                Set<String> opts = new HashSet<>();
                opts.add("src");
                opts.add("addcp");
                opts.add("xsdconf");
                opts.add("ms");
                opts.add("mx");

                File f = new File(schemaFileName);
                if (!f.exists() || !f.isFile()) {
                    acceptError(XmlError.forMessage("Invalid schema file name, or schema file not found", XmlError.SEVERITY_ERROR));
                    return;
                }

                StringBuilder givenNameBuilder = new StringBuilder();
                File f1 = f.getCanonicalFile();
                boolean first = true;
                while (f1 != null) {
                   
                    if (f1.isDirectory()) {
                        File layerXml = new File(f1, "layer.xml");
                        if (layerXml.exists()) {
                            break;
                        }
                    }
                    if (first) {
                        first = false;
                    } else {
                        givenNameBuilder.insert(0, '_');
                    }
                    givenNameBuilder.insert(0, f1.getName());
                    f1 = f1.getParentFile();
                }
                String givenName = givenNameBuilder.toString().replace('.', '_');
               
            //    givenNameBuilder = null;
                //String givenName = null;//f.getName().replace('.', '_');

                File tempJar = null;
                try {
                    tempJar = File.createTempFile("xbean_radix", ".jar");
                } catch (IOException e1) {
                    acceptError(XmlError.forMessage("Cannot create temporary Radix jar file: " + e1.getMessage(), XmlError.SEVERITY_ERROR));
                    return;
                }
                tempJar.deleteOnExit();

                f = new File(resultJarPath);
                if (f.exists()) {
                    acceptError(XmlError.forMessage("Invalid Radix jar file name - file already exists: " + f.getAbsolutePath(), XmlError.SEVERITY_ERROR));
                    return;
                }

                if (sourceDirPath != null) {
                    f = new File(sourceDirPath);
                    if (!f.isDirectory()) {
                        acceptError(XmlError.forMessage("Invalid source dir name, or source dir not found: " + f.getAbsolutePath(), XmlError.SEVERITY_ERROR));
                        return;
                    } else if (f.list().length != 0) {
                        acceptError(XmlError.forMessage("Source directory not empty", XmlError.SEVERITY_ERROR));
                        return;
                    }
                }

                List<String> params = new ArrayList<>();
                params.addAll(Arrays.asList(new String[]{
                                       "-schemaCodePrinter", THIS_PACKAGE + ".XbeansSchemaCodePrinter",
                                       //"-verbose",
                                       "-debug",
                                       "-dl",
                                       "-javasource", "1.5",
                                       "-out", tempJar.getAbsolutePath(),
                                       schemaFileName
                                   }));

                if (sourceDirPath != null) {
                    params.add("-src");
                    params.add(sourceDirPath);
                }

                if (givenName != null) {
                    params.add("-name");
                    params.add(givenName);
                }

                if (compileCode) {
                    params.add("-compiler");
                    params.add("javac");
                    params.add("-cp");
                    params.add(classPath);
                } else {
                    params.add("-srconly");
                    if (sourceDirPath == null) {
                        throw new IllegalArgumentException("Parameter '-src' must be set when running in source generation mode");
                    }
                    params.add("-d");
                    params.add(sourceDirPath);
                }

                if (configFilePath != null) {
                    params.add(configFilePath);
                }
                if (memorySize != null) {
                    params.add("-ms");
                    params.add(memorySize);
                }

                if (memoryMaxSize != null) {
                    params.add("-mx");
                    params.add(memoryMaxSize);
                }

                if (verbose) {
                    params.add("-verbose");
                } else if (quiet) {
                    params.add("-quiet");
                }
                if (noRadixEnums) {
                    params.add("-noradixenums");
                }
                String[] paramsArr = new String[params.size()];
                params.toArray(paramsArr);

                XbeansSchemaCompiler.compile(paramsArr, this);

                if (!compileCode) {
                    new JarHelper().jarDir(new File(sourceDirPath), tempJar);
                }
                acceptInfo(XmlError.forMessage("Compilation completed", XmlError.SEVERITY_INFO));

                try {
                    FSDirManager.copyFile(tempJar, new File(resultJarPath));
                } catch (IOException e) {
                    acceptError(XmlError.forMessage("Cannot copy temporary jar to final one: " + e.getMessage(), XmlError.SEVERITY_ERROR));
                }
            } catch (Exception e) {
                acceptError(XmlError.forMessage("Exception while compilation:" + e.getMessage(), XmlError.SEVERITY_ERROR));
            }
        }
    }

    private static class BatchCompiler extends Compiler {

        private final boolean doCompile;
        private final boolean quiet;
        private final boolean verbose;
        private final boolean noRadixEnums;
        private final String schemaFile;
        private final String radixJar;
        private final String dirName;
        private final String xsdConf;
        private final String memorySize;
        private final String memoryMax;
        private boolean canCompile;

        private static CommandLine createCommandLine(String[] commandLineArgs) {
            Set<String> flags = new HashSet<String>();
            flags.add("compile");
            flags.add("quiet");
            flags.add("verbose");
            flags.add("noradivenums");

            Set<String> opts = new HashSet<String>();
            opts.add("src");
            opts.add("addcp");
            opts.add("xsdconf");
            opts.add("ms");
            opts.add("mx");
            opts.add("name");

            return new CommandLine(commandLineArgs, flags, opts);
        }

        public BatchCompiler(CommandLine cl, String[] commandLineArgs) {
            super(cl.getOpt("verbose") != null, cl.getBaseDir() == null ? null : cl.getBaseDir().toURI());
            String[] badopts = cl.getBadOpts();
            canCompile = true;
            if (badopts.length > 0) {
                for (int i = 0; i < badopts.length; i++) {
                    System.out.println("Bad command line option:" + badopts[i]);
                }
                canCompile = false;
            }

            doCompile = (cl.getOpt("compile") != null);
            quiet = (cl.getOpt("quiet") != null);
            verbose = (cl.getOpt("verbose") != null);
            noRadixEnums = (cl.getOpt("noradixenums") != null);

            schemaFile = commandLineArgs[0];
            radixJar = commandLineArgs[1];
            dirName = cl.getOpt("src");
            xsdConf = cl.getOpt("xsdconf");
            memorySize = cl.getOpt("ms");
            memoryMax = cl.getOpt("mx");
        }

        public BatchCompiler(String[] commandLineArgs) {
            this(createCommandLine(commandLineArgs), commandLineArgs);
        }

        public void compile() {
            if (canCompile) {
                compile(schemaFile, dirName, xsdConf, doCompile, System.getProperty("java.class.path"), radixJar, quiet, verbose, noRadixEnums, memorySize, memoryMax);
            }
        }
    }
}
