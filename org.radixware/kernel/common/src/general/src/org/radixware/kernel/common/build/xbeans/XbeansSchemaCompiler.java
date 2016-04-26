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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import org.apache.xmlbeans.ResourceLoader;
import org.apache.xmlbeans.SchemaCodePrinter;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.SystemProperties;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.common.ResolverUtil;
import org.apache.xmlbeans.impl.common.XmlErrorPrinter;
import org.apache.xmlbeans.impl.common.XmlErrorWatcher;
import org.apache.xmlbeans.impl.config.BindingConfigImpl;
import org.apache.xmlbeans.impl.schema.PathResourceLoader;
import org.apache.xmlbeans.impl.schema.SchemaTypeLoaderImpl;
import org.apache.xmlbeans.impl.schema.SchemaTypeSystemCompiler;
import org.apache.xmlbeans.impl.schema.SchemaTypeSystemImpl;
import org.apache.xmlbeans.impl.schema.StscState;
import org.apache.xmlbeans.impl.tool.CodeGenUtil;
import org.apache.xmlbeans.impl.tool.CommandLine;
import org.apache.xmlbeans.impl.tool.Extension;
import org.apache.xmlbeans.impl.tool.SchemaCompilerExtension;
import org.apache.xmlbeans.impl.util.FilerImpl;
import org.apache.xmlbeans.impl.values.XmlListImpl;
import org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument;
import org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config;
import org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.xml.sax.EntityResolver;

import repackage.Repackager;

public class XbeansSchemaCompiler {

    public static class MessagePrinter extends XmlErrorPrinter {

        protected MessagePrinter(boolean noisy, URI baseURI) {
            super(noisy, baseURI);
        }

        @Override
        public final boolean add(Object o) {
            if (o instanceof XmlError) {
                XmlError e = (XmlError) o;
                switch (e.getSeverity()) {
                    case XmlError.SEVERITY_ERROR:
                        return acceptError(e);
                    case XmlError.SEVERITY_WARNING:
                        return acceptWarning(e);
                    case XmlError.SEVERITY_INFO:
                        return acceptInfo(e);
                    default:
                        return super.add(o);

                }
            } else {
                return super.add(o);
            }

        }

        protected boolean acceptError(XmlError error) {
            return super.add(error);
        }

        protected boolean acceptInfo(XmlError info) {
            return super.add(info);
        }

        protected boolean acceptWarning(XmlError warning) {
            return super.add(warning);
        }
    }
    private static final MessagePrinter defaultPrinter = new MessagePrinter(true, null);

    public static void main(String[] args) throws Exception {
        compile(args, defaultPrinter);
    }

    static void compile(String[] args, MessagePrinter messagePrinter) throws Exception {

        Set<String> flags = new HashSet<String>();
        flags.add("h");
        flags.add("help");
        flags.add("usage");
        flags.add("license");
        flags.add("quiet");
        flags.add("verbose");
        flags.add("version");
        flags.add("dl");
        flags.add("noupa");
        flags.add("nopvr");
        flags.add("noann");
        flags.add("novdoc");
        flags.add("srconly");
        flags.add("debug");
        flags.add("noradixenums");

        Set<String> opts = new HashSet<String>();
        opts.add("out");
        opts.add("name");
        opts.add("src");
        opts.add("d");
        opts.add("cp");
        opts.add("compiler");
        opts.add("javasource");
        opts.add("jar"); // deprecated
        opts.add("ms");
        opts.add("mx");
        opts.add("repackage");
        opts.add("schemaCodePrinter");
        opts.add("extension");
        opts.add("extensionParms");
        opts.add("allowmdef");
        opts.add("catalog");
        opts.add("fstype");
        CommandLine cl = new CommandLine(args, flags, opts);

        String[] badopts = cl.getBadOpts();
        if (badopts.length > 0) {
            for (int i = 0; i
                    < badopts.length; i++) {
                messagePrinter.acceptError(XmlError.forMessage("Unrecognized option: " + badopts[i], XmlError.SEVERITY_ERROR));
//            System.exit(0);
            }

            return;
        }

       
        boolean verbose = (cl.getOpt("verbose") != null);
        boolean quiet = (cl.getOpt("quiet") != null);
        if (verbose) {
            quiet = false;
        }

        if (verbose) {
            CommandLine.printVersion();
        }

        String outputfilename = cl.getOpt("out");

        String repackage = cl.getOpt("repackage");

        String fsType = cl.getOpt("fsType");
        if (fsType == null) {
            fsType = "fs";
        }

        FilerFactory filerFactory = new FilerFactory(fsType);

        String codePrinterClass = cl.getOpt("schemaCodePrinter");
        SchemaCodePrinter codePrinter = null;
        if (codePrinterClass != null) {
            try {
                codePrinter = (SchemaCodePrinter) Class.forName(codePrinterClass).newInstance();
            } catch (Exception e) {
                messagePrinter.acceptError(XmlError.forMessage("Failed to load SchemaCodePrinter class "
                        + codePrinterClass + "; proceeding with default printer", XmlError.SEVERITY_ERROR));
            }

        } else {
            messagePrinter.acceptError(XmlError.forMessage("Parameter 'schemaCodePrinter' is not defined"));
            throw new Exception("Parameter 'schemaCodePrinter' is not defined");
        }

        String name = cl.getOpt("name");
        

        boolean download = (cl.getOpt("dl") != null);
        boolean noUpa = (cl.getOpt("noupa") != null);
        boolean noPvr = (cl.getOpt("nopvr") != null);
        boolean noAnn = (cl.getOpt("noann") != null);
        boolean noVDoc = (cl.getOpt("novdoc") != null);
        boolean nojavac = (cl.getOpt("srconly") != null);
        boolean debug = (cl.getOpt("debug") != null);
        boolean noRadixEnums = (cl.getOpt("noradixenums") != null);

        String allowmdef = cl.getOpt("allowmdef");
        Set mdefNamespaces = (allowmdef == null ? Collections.EMPTY_SET : new HashSet<String>(Arrays.asList(XmlListImpl.split_list(allowmdef))));

        List<Extension> extensions = new ArrayList<Extension>();
        if (cl.getOpt("extension") != null) {
            try {
                Extension e = new Extension();
                e.setClassName(Class.forName(cl.getOpt("extension"), false, Thread.currentThread().getContextClassLoader()));
                extensions.add(e);
            } catch (ClassNotFoundException e) {
                messagePrinter.acceptError(XmlError.forMessage("Could not find extension class: " + cl.getOpt("extension") + "  Is it on your classpath?", XmlError.SEVERITY_ERROR));
                throw e;
            }

        }

        if (extensions.size() > 0) {
            // example: -extensionParms typeMappingFileLocation=d:\types
            if (cl.getOpt("extensionParms") != null) {
                Extension e = extensions.get(0);
                // extensionParms are delimited by ';'
                StringTokenizer parmTokens = new StringTokenizer(cl.getOpt("extensionParms"), ";");
                while (parmTokens.hasMoreTokens()) {
                    // get name value pair for each extension parms and stick into extension parms
                    String nvPair = parmTokens.nextToken();
                    int index = nvPair.indexOf('=');
                    if (index < 0) {
                        messagePrinter.acceptError(XmlError.forMessage("extensionParms should be name=value;name=value", XmlError.SEVERITY_ERROR));
//                        System.exit(1);
                        throw new Exception("extensionParms should be name=value;name=value");
                    }

                    String n = nvPair.substring(0, index);
                    String v = nvPair.substring(index + 1);
                    Extension.Param param = e.createParam();
                    param.setName(n);
                    param.setValue(v);
                }

            }
        }


        String classesdir = cl.getOpt("d");
        File classes = null;
        if (classesdir != null) {
            classes = new File(classesdir);
        }

        String srcdir = cl.getOpt("src");
        File src = null;
        if (srcdir != null) {
            src = new File(srcdir);
        }

        if (nojavac && srcdir == null && classes != null) {
            src = classes;
        }

        File tempdir = null;
        if (src == null || classes == null) {
            try {
                tempdir = filerFactory.getDirManager().createTempDir();
            } catch (java.io.IOException e) {
                messagePrinter.acceptError(XmlError.forMessage("Error creating temp dir " + e.getMessage(), XmlError.SEVERITY_ERROR));
                throw e;
            }

        }


        File jarfile = null;
        if (outputfilename == null && classes == null && !nojavac) {
            outputfilename = "xmltypes.jar";
        }

        if (outputfilename != null) {
            jarfile = new File(outputfilename);
        }

        if (src == null) {
            src = filerFactory.getDirManager().createDir(tempdir, "src");
        }

        if (classes == null) {
            classes = filerFactory.getDirManager().createDir(tempdir, "classes");
        }

        File[] classpath = null;
        String cpString = cl.getOpt("cp");
        if (cpString != null) {
            String[] cpparts = cpString.split(File.pathSeparator);
            List<File> cpList = new ArrayList<File>();
            for (int i = 0; i
                    < cpparts.length; i++) {
                cpList.add(new File(cpparts[i]));
            }

            classpath = cpList.toArray(new File[cpList.size()]);
        } else {
            classpath = CodeGenUtil.systemClasspath();
        }

        String javasource = cl.getOpt("javasource");
        String compiler = cl.getOpt("compiler");

        String jar = cl.getOpt("jar");
        if (verbose && jar != null) {
            messagePrinter.acceptInfo(XmlError.forMessage("The 'jar' option is no longer supported", XmlError.SEVERITY_INFO));
        }

        String memoryInitialSize = cl.getOpt("ms");
        String memoryMaximumSize = cl.getOpt("mx");

        File[] xsdFiles = cl.filesEndingWith(".xsd");
        File[] wsdlFiles = cl.filesEndingWith(".wsdl");
        File[] javaFiles = cl.filesEndingWith(".java");
        File[] configFiles = cl.filesEndingWith(".xsdconfig");

        final File defaultConfigFile = File.createTempFile("xsdconf", "xsdconf");

        try {
            ConfigDocument xDoc = ConfigDocument.Factory.newInstance();
            ConfigDocument.Config xConf = xDoc.addNewConfig();
            Extensionconfig xExt = xConf.addNewExtension();

            xExt.setFor("*");
            Extensionconfig.Interface iface = xExt.addNewInterface();
            iface.setName("org.radixware.kernel.common.build.xbeans.IXBeansChangeEmitter");
            iface.setStaticHandler("org.radixware.kernel.common.build.xbeans.XBeansChangeEmitterHandler");

            xExt = xConf.addNewExtension();
            xExt.setFor("*");
            Extensionconfig.PrePostSet prepost = xExt.addNewPrePostSet();
            prepost.setStaticHandler("org.radixware.kernel.common.build.xbeans.XBeansChangeEmitterHandler");
            xDoc.save(defaultConfigFile);
            File[] arr = new File[configFiles.length + 1];
            System.arraycopy(configFiles, 0, arr, 0, configFiles.length);
            arr[configFiles.length] = defaultConfigFile;
            configFiles = arr;

        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(XbeansSchemaCompiler.class.getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        try {
            URL[] urlFiles = cl.getURLs();

            if (xsdFiles.length + wsdlFiles.length + urlFiles.length == 0) {
                messagePrinter.acceptError(XmlError.forMessage("Could not find any xsd or wsdl files to process", XmlError.SEVERITY_ERROR));
//            System.exit(0);
                throw new Exception("Could not find any xsd or wsdl files to process.");
            }

            File baseDir = cl.getBaseDir();
            URI baseURI = baseDir == null ? null : baseDir.toURI();

            XmlErrorPrinter err = messagePrinter == defaultPrinter ? new XmlErrorPrinter(verbose, baseURI) : messagePrinter;

            String catString = cl.getOpt("catalog");

            Parameters params = new Parameters();
            params.setBaseDir(baseDir);
            params.setXsdFiles(xsdFiles);
            params.setWsdlFiles(wsdlFiles);
            params.setJavaFiles(javaFiles);
            params.setConfigFiles(configFiles);
            params.setUrlFiles(urlFiles);
            params.setClasspath(classpath);
            params.setOutputJar(jarfile);
            params.setName(name);
            params.setSrcDir(src);
            params.setClassesDir(classes);
            params.setCompiler(compiler);
            params.setJavaSource(javasource);
            params.setMemoryInitialSize(memoryInitialSize);
            params.setMemoryMaximumSize(memoryMaximumSize);
            params.setNojavac(nojavac);
            params.setQuiet(quiet);
            params.setVerbose(verbose);
            params.setDownload(download);
            params.setNoUpa(noUpa);
            params.setNoPvr(noPvr);
            params.setNoAnn(noAnn);
            params.setNoVDoc(noVDoc);
            params.setDebug(debug);
            params.setErrorListener(err);
            params.setRepackage(repackage);
            params.setExtensions(extensions);
            params.setMdefNamespaces(mdefNamespaces);
            params.setCatalogFile(catString);
            params.setSchemaCodePrinter(codePrinter);
            params.setFilerFactory(filerFactory);
            params.setNoRadixEnums(noRadixEnums);

            boolean result = compile(params, messagePrinter);

            if (tempdir != null) {
                filerFactory.getDirManager().removeDir(tempdir);
            }

            if (!result) {
                throw new IllegalStateException("Compilation failed");
//
//        System.exit(0);
            }
        } finally {
            defaultConfigFile.delete();
        }
    }

    public static class Parameters {

        private File baseDir;
        private File[] xsdFiles;
        private File[] wsdlFiles;
        private File[] javaFiles;
        private File[] configFiles;
        private URL[] urlFiles;
        private File[] classpath;
        private File outputJar;
        private String name;
        private File srcDir;
        private File classesDir;
        private String memoryInitialSize;
        private String memoryMaximumSize;
        private String compiler;
        private String javasource;
        private boolean nojavac;
        private boolean quiet;
        private boolean verbose;
        private boolean download;
        private Collection errorListener;
        private boolean noUpa;
        private boolean noPvr;
        private boolean noAnn;
        private boolean noVDoc;
        private boolean debug;
        private boolean incrementalSrcGen;
        private String repackage;
        private List<Extension> extensions = Collections.emptyList();
        private Set mdefNamespaces = Collections.EMPTY_SET;
        private String catalogFile;
        private SchemaCodePrinter schemaCodePrinter;
        private EntityResolver entityResolver;
        private FilerFactory filerFactory;
        private boolean noRadixEnums;
        private SchemaTypeLoader loader = null;

        public SchemaTypeLoader getSchemaTypeLoader() {
            return loader == null ? XmlBeans.typeLoaderForClassLoader(SchemaDocument.class.getClassLoader()) : loader;
        }

        public void setSchemaTypeLoader(SchemaTypeLoader loader) {
            this.loader = loader;
        }

        public File getBaseDir() {
            return baseDir;
        }

        public void setBaseDir(File baseDir) {
            this.baseDir = baseDir;
        }

        public File[] getXsdFiles() {
            return xsdFiles;
        }

        public void setXsdFiles(File[] xsdFiles) {
            this.xsdFiles = xsdFiles;
        }

        public File[] getWsdlFiles() {
            return wsdlFiles;
        }

        public void setWsdlFiles(File[] wsdlFiles) {
            this.wsdlFiles = wsdlFiles;
        }

        public File[] getJavaFiles() {
            return javaFiles;
        }

        public void setJavaFiles(File[] javaFiles) {
            this.javaFiles = javaFiles;
        }

        public File[] getConfigFiles() {
            return configFiles;
        }

        public void setConfigFiles(File[] configFiles) {
            this.configFiles = configFiles;
        }

        public URL[] getUrlFiles() {
            return urlFiles;
        }

        public void setUrlFiles(URL[] urlFiles) {
            this.urlFiles = urlFiles;
        }

        public File[] getClasspath() {
            return classpath;
        }

        public void setClasspath(File[] classpath) {
            this.classpath = classpath;
        }

        public File getOutputJar() {
            return outputJar;
        }

        public void setOutputJar(File outputJar) {
            this.outputJar = outputJar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public File getSrcDir() {
            return srcDir;
        }

        public void setSrcDir(File srcDir) {
            this.srcDir = srcDir;
        }

        public File getClassesDir() {
            return classesDir;
        }

        public void setClassesDir(File classesDir) {
            this.classesDir = classesDir;
        }

        public boolean isNojavac() {
            return nojavac;
        }

        public void setNojavac(boolean nojavac) {
            this.nojavac = nojavac;
        }

        public boolean isQuiet() {
            return quiet;
        }

        public void setQuiet(boolean quiet) {
            this.quiet = quiet;
        }

        public boolean isVerbose() {
            return verbose;
        }

        public void setVerbose(boolean verbose) {
            this.verbose = verbose;
        }

        public boolean isDownload() {
            return download;
        }

        public void setDownload(boolean download) {
            this.download = download;
        }

        public boolean isNoUpa() {
            return noUpa;
        }

        public void setNoUpa(boolean noUpa) {
            this.noUpa = noUpa;
        }

        public boolean isNoPvr() {
            return noPvr;
        }

        public void setNoPvr(boolean noPvr) {
            this.noPvr = noPvr;
        }

        public boolean isNoAnn() {
            return noAnn;
        }

        public void setNoAnn(boolean noAnn) {
            this.noAnn = noAnn;
        }

        public boolean isNoVDoc() {
            return noVDoc;
        }

        public void setNoVDoc(boolean newNoVDoc) {
            this.noVDoc = newNoVDoc;
        }

        public boolean isIncrementalSrcGen() {
            return incrementalSrcGen;
        }

        public void setIncrementalSrcGen(boolean incrSrcGen) {
            this.incrementalSrcGen = incrSrcGen;
        }

        public boolean isDebug() {
            return debug;
        }

        public void setDebug(boolean debug) {
            this.debug = debug;
        }

        public String getMemoryInitialSize() {
            return memoryInitialSize;
        }

        public void setMemoryInitialSize(String memoryInitialSize) {
            this.memoryInitialSize = memoryInitialSize;
        }

        public String getMemoryMaximumSize() {
            return memoryMaximumSize;
        }

        public void setMemoryMaximumSize(String memoryMaximumSize) {
            this.memoryMaximumSize = memoryMaximumSize;
        }

        public String getCompiler() {
            return compiler;
        }

        public void setCompiler(String compiler) {
            this.compiler = compiler;
        }

        public String getJavaSource() {
            return javasource;
        }

        public void setJavaSource(String javasource) {
            this.javasource = javasource;
        }

        @Deprecated
        public String getJar() {
            return null;
        }

        @Deprecated
        public void setJar(@SuppressWarnings("unused") final String jar) {
            // no op
        }

        public Collection getErrorListener() {
            return errorListener;
        }

        public void setErrorListener(Collection errorListener) {
            this.errorListener = errorListener;
        }

        public String getRepackage() {
            return repackage;
        }

        public void setRepackage(String newRepackage) {
            repackage = newRepackage;
        }

        public List<Extension> getExtensions() {
            return extensions;
        }

        public void setExtensions(List<Extension> extensions) {
            this.extensions = extensions;
        }

        public Set getMdefNamespaces() {
            return mdefNamespaces;
        }

        public void setMdefNamespaces(Set mdefNamespaces) {
            this.mdefNamespaces = mdefNamespaces;
        }

        public String getCatalogFile() {
            return catalogFile;
        }

        public void setCatalogFile(String catalogPropFile) {
            this.catalogFile = catalogPropFile;
        }

        public SchemaCodePrinter getSchemaCodePrinter() {
            return schemaCodePrinter;
        }

        public void setSchemaCodePrinter(SchemaCodePrinter schemaCodePrinter) {
            this.schemaCodePrinter = schemaCodePrinter;
        }

        public EntityResolver getEntityResolver() {
            return entityResolver;
        }

        public void setEntityResolver(EntityResolver entityResolver) {
            this.entityResolver = entityResolver;
        }

        public FilerFactory getFilerFactory() {
            return filerFactory;
        }

        public void setFilerFactory(FilerFactory filerFactory) {
            this.filerFactory = filerFactory;
        }

        public boolean isNoRadixEnums() {
            return noRadixEnums;
        }

        public void setNoRadixEnums(boolean noRadixEnums) {
            this.noRadixEnums = noRadixEnums;
        }
    }

    private static SchemaTypeSystem loadTypeSystem(
            String name, File[] xsdFiles, File[] wsdlFiles, URL[] urlFiles, File[] configFiles,
            File[] javaFiles, ResourceLoader cpResourceLoader,
            boolean download, boolean noUpa, boolean noPvr, boolean noAnn, boolean noVDoc,
            Set mdefNamespaces, File baseDir, Map sourcesToCopyMap,
            Collection outerErrorListener, File schemasDir, EntityResolver entResolver, File[] classpath, String javasource, SchemaTypeLoader loader) {
        XmlErrorWatcher errorListener = new XmlErrorWatcher(outerErrorListener);

        // construct the state (have to initialize early in case of errors)
        StscState state = StscState.start();
        state.setErrorListener(errorListener);

        // For parsing XSD and WSDL files, we should use the SchemaDocument
        // classloader rather than the thread context classloader.  This is
        // because in some situations (such as when being invoked by ant
        // underneath the ide) the context classloader is potentially weird
        // (because of the design of ant).



        // step 1, parse all the XSD files.
        ArrayList<XmlObject> scontentlist = new ArrayList<XmlObject>();

        if (xsdFiles != null) {
            for (int i = 0; i < xsdFiles.length; i++) {
                try {
                    XmlOptions options = new XmlOptions();
                    options.setLoadLineNumbers();
                    options.setLoadMessageDigest();
                    options.setEntityResolver(entResolver);

                    XmlObject schemadoc = loader.parse(xsdFiles[i], null, options);
                    if (!(schemadoc instanceof SchemaDocument)) {
                        StscState.addError(errorListener, XmlErrorCodes.INVALID_DOCUMENT_TYPE,
                                new Object[]{xsdFiles[i], "schema"}, schemadoc);
                    } else {
                        addSchema(xsdFiles[i].toString(), (SchemaDocument) schemadoc,
                                errorListener, noVDoc, scontentlist);
                    }
                } catch (XmlException e) {
                    errorListener.add(e.getError());
                } catch (Exception e) {
                    StscState.addError(errorListener, XmlErrorCodes.CANNOT_LOAD_FILE,
                            new Object[]{"xsd", xsdFiles[i], e.getMessage()}, xsdFiles[i]);
                }
            }
        }

        // step 2, parse all WSDL files
        if (wsdlFiles != null) {
            for (int i = 0; i < wsdlFiles.length; i++) {
                try {
                    XmlOptions options = new XmlOptions();
                    options.setLoadLineNumbers();
                    options.setLoadSubstituteNamespaces(Collections.singletonMap(
                            "http://schemas.xmlsoap.org/wsdl/", "http://www.apache.org/internal/xmlbeans/wsdlsubst"));
                    options.setEntityResolver(entResolver);

                    XmlObject wsdldoc = loader.parse(wsdlFiles[i], null, options);

                    if (!(wsdldoc instanceof org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument)) {
                        StscState.addError(errorListener, XmlErrorCodes.INVALID_DOCUMENT_TYPE,
                                new Object[]{wsdlFiles[i], "wsdl"}, wsdldoc);
                    } else {
                        addWsdlSchemas(wsdlFiles[i].toString(), (org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument) wsdldoc, errorListener, noVDoc, scontentlist);
                    }
                } catch (XmlException e) {
                    errorListener.add(e.getError());
                } catch (Exception e) {
                    StscState.addError(errorListener, XmlErrorCodes.CANNOT_LOAD_FILE,
                            new Object[]{"wsdl", wsdlFiles[i], e.getMessage()}, wsdlFiles[i]);
                }
            }
        }

        // step 3, parse all URL files
        // XMLBEANS-58 - Ability to pass URLs instead of Files for Wsdl/Schemas
        if (urlFiles != null) {
            for (int i = 0; i < urlFiles.length; i++) {
                try {
                    XmlOptions options = new XmlOptions();
                    options.setLoadLineNumbers();
                    options.setLoadSubstituteNamespaces(Collections.singletonMap("http://schemas.xmlsoap.org/wsdl/", "http://www.apache.org/internal/xmlbeans/wsdlsubst"));
                    options.setEntityResolver(entResolver);

                    XmlObject urldoc = loader.parse(urlFiles[i], null, options);

                    if ((urldoc instanceof org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument)) {
                        addWsdlSchemas(urlFiles[i].toString(), (org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument) urldoc, errorListener, noVDoc, scontentlist);
                    } else if ((urldoc instanceof SchemaDocument)) {
                        addSchema(urlFiles[i].toString(), (SchemaDocument) urldoc,
                                errorListener, noVDoc, scontentlist);
                    } else {
                        StscState.addError(errorListener, XmlErrorCodes.INVALID_DOCUMENT_TYPE,
                                new Object[]{urlFiles[i], "wsdl or schema"}, urldoc);
                    }

                } catch (XmlException e) {
                    errorListener.add(e.getError());
                } catch (Exception e) {
                    StscState.addError(errorListener, XmlErrorCodes.CANNOT_LOAD_FILE,
                            new Object[]{"url", urlFiles[i], e.getMessage()}, urlFiles[i]);
                }
            }
        }
        SchemaDocument.Schema[] sdocs = scontentlist.toArray(new SchemaDocument.Schema[scontentlist.size()]);

        // now the config files.
        ArrayList<Config> cdoclist = new ArrayList<Config>();
        if (configFiles != null) {
            for (int i = 0; i < configFiles.length; i++) {
                try {
                    XmlOptions options = new XmlOptions();
                    options.put(XmlOptions.LOAD_LINE_NUMBERS);
                    options.setEntityResolver(entResolver);
                    options.setLoadSubstituteNamespaces(MAP_COMPATIBILITY_CONFIG_URIS);

                    XmlObject configdoc = loader.parse(configFiles[i], null, options);
                    if (!(configdoc instanceof ConfigDocument)) {
                        StscState.addError(errorListener, XmlErrorCodes.INVALID_DOCUMENT_TYPE,
                                new Object[]{configFiles[i], "xsd config"}, configdoc);
                    } else {
                        StscState.addInfo(errorListener, "Loading config file " + configFiles[i]);
                        if (configdoc.validate(new XmlOptions().setErrorListener(errorListener))) {
                            cdoclist.add(((ConfigDocument) configdoc).getConfig());
                        }
                    }
                } catch (XmlException e) {
                    errorListener.add(e.getError());
                } catch (Exception e) {
                    StscState.addError(errorListener, XmlErrorCodes.CANNOT_LOAD_FILE,
                            new Object[]{"xsd config", configFiles[i], e.getMessage()}, configFiles[i]);
                }
            }
        }
        ConfigDocument.Config[] cdocs = cdoclist.toArray(new ConfigDocument.Config[cdoclist.size()]);
        SchemaTypeLoader linkTo = SchemaTypeLoaderImpl.build(null, cpResourceLoader, null);
        URI baseURI = null;
        if (baseDir != null) {
            baseURI = baseDir.toURI();
        }
        XmlOptions opts = new XmlOptions();

        if (download) {
            opts.setCompileDownloadUrls();
        }
        if (noUpa) {
            opts.setCompileNoUpaRule();
        }
        if (noPvr) {
            opts.setCompileNoPvrRule();
        }
        if (noAnn) {
            opts.setCompileNoAnnotations();
        }
        if (mdefNamespaces != null) {
            opts.setCompileMdefNamespaces(mdefNamespaces);
        }
        opts.put(XmlOptions.COMPILE_MDEF_NAMESPACES, Collections.singleton("##any"));
        opts.setCompileNoValidation(); // already validated here
        opts.setEntityResolver(entResolver);

        if (javasource != null) {
            opts.setGenerateJavaVersion(javasource);        // now pass it to the main compile function
        }
        SchemaTypeSystemCompiler.Parameters params = new SchemaTypeSystemCompiler.Parameters();
        params.setName(name);
        params.setSchemas(sdocs);
        params.setConfig(BindingConfigImpl.forConfigDocuments(cdocs, javaFiles, classpath));
        params.setLinkTo(linkTo);
        params.setOptions(opts);
        params.setErrorListener(errorListener);
        params.setJavaize(
                true);
        params.setBaseURI(baseURI);
        params.setSourcesToCopyMap(sourcesToCopyMap);
        params.setSchemasDir(schemasDir);
        return SchemaTypeSystemCompiler.compile(params);
    }

    private static void addSchema(String name, SchemaDocument schemadoc,
            XmlErrorWatcher errorListener, boolean noVDoc, List<XmlObject> scontentlist) {
        StscState.addInfo(errorListener, "Loading schema file " + name);
        XmlOptions opts = new XmlOptions().setErrorListener(errorListener);
        if (noVDoc) {
            opts.setValidateTreatLaxAsSkip();
        }

        if (schemadoc.validate(opts)) {
            scontentlist.add((schemadoc).getSchema());
        }

    }

    private static void addWsdlSchemas(String name,
            org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument wsdldoc,
            XmlErrorWatcher errorListener, boolean noVDoc, List<XmlObject> scontentlist) {
        if (wsdlContainsEncoded(wsdldoc)) {
            StscState.addWarning(errorListener, "The WSDL " + name + " uses SOAP encoding. SOAP encoding is not compatible with literal XML Schema.", XmlErrorCodes.GENERIC_ERROR, wsdldoc);
        }

        StscState.addInfo(errorListener, "Loading wsdl file " + name);
        XmlOptions opts = new XmlOptions().setErrorListener(errorListener);
        if (noVDoc) {
            opts.setValidateTreatLaxAsSkip();
        }

        XmlObject[] types = wsdldoc.getDefinitions().getTypesArray();
        int count = 0;
        for (int j = 0; j
                < types.length; j++) {
            XmlObject[] schemas = types[j].selectPath("declare namespace xs=\"http://www.w3.org/2001/XMLSchema\" xs:schema");
            if (schemas.length == 0) {
                StscState.addWarning(errorListener, "The WSDL " + name + " did not have any schema documents in namespace 'http://www.w3.org/2001/XMLSchema'", XmlErrorCodes.GENERIC_ERROR, wsdldoc);
                continue;

            }





            for (int k = 0; k
                    < schemas.length; k++) {
                if (schemas[k] instanceof SchemaDocument.Schema
                        && schemas[k].validate(opts)) {
                    count++;
                    scontentlist.add(schemas[k]);
                }

            }
        }
        StscState.addInfo(errorListener, "Processing " + count + " schema(s) in " + name);
    }
    public static final String SCHEMAS_DIR_NAME = "schema" + SchemaTypeSystemImpl.METADATA_PACKAGE_GEN;

    public static SchemaTypeSystem computeTypeSystem(Parameters params, MessagePrinter messagePrinter, SchemaTypeLoader loader) {
        File baseDir = params.getBaseDir();
        File[] xsdFiles = params.getXsdFiles();
        File[] wsdlFiles = params.getWsdlFiles();
        URL[] urlFiles = params.getUrlFiles();
        File[] javaFiles = params.getJavaFiles();
        File[] configFiles = params.getConfigFiles();
        File[] classpath = params.getClasspath();

        String name = params.getName();
        File srcDir = params.getSrcDir();
        File classesDir = params.getClassesDir();
        String javasource = params.getJavaSource();
        boolean nojavac = params.isNojavac();
        boolean debug = params.isDebug();
        boolean verbose = params.isVerbose();
        boolean quiet = params.isQuiet();
        boolean download = params.isDownload();
        boolean noUpa = params.isNoUpa();
        boolean noPvr = params.isNoPvr();
        boolean noAnn = params.isNoAnn();
        boolean noVDoc = params.isNoVDoc();
        FilerFactory filerFactory = params.getFilerFactory();
        Collection outerErrorListener = params.getErrorListener();

        String repackage = params.getRepackage();

        DirManager dirManager = filerFactory.getDirManager();

        if (!quiet && verbose) {
            messagePrinter.acceptInfo(XmlError.forMessage("Start compilation...", XmlError.SEVERITY_INFO));
        }

        if (repackage != null) {
            SchemaTypeLoaderImpl.METADATA_PACKAGE_LOAD = SchemaTypeSystemImpl.METADATA_PACKAGE_GEN;

            String stsPackage = SchemaTypeSystem.class.getPackage().getName();
            Repackager repackager = new Repackager(repackage);

            SchemaTypeSystemImpl.METADATA_PACKAGE_GEN = repackager.repackage(new StringBuffer(stsPackage)).toString().replace('.', '_');

//            System.out.println("\n\n\n" + stsPackage + ".SchemaCompiler  Metadata LOAD:" + SchemaTypeLoaderImpl.METADATA_PACKAGE_LOAD + " GEN:" + SchemaTypeSystemImpl.METADATA_PACKAGE_GEN);
        }

        SchemaCodePrinter codePrinter = params.getSchemaCodePrinter();
        try {

            Method setup = codePrinter.getClass().getMethod("setup", XmlOptions.class);
            if (setup != null) {
                XmlOptions options = new XmlOptions();
                if (params.isNoRadixEnums()) {
                    options.put("No Radix Enums");
                }
                setup.invoke(codePrinter, options);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        Set mdefNamespaces = params.getMdefNamespaces();

        EntityResolver cmdLineEntRes = params.getEntityResolver() == null ? ResolverUtil.resolverForCatalog(params.getCatalogFile()) : params.getEntityResolver();

        if (srcDir == null || classesDir == null) {
            throw new IllegalArgumentException("src and class gen directories may not be null.");
        }

        // Calculate the usenames based on the relativized filenames on the filesystem
        if (baseDir == null) {
            baseDir = new File(SystemProperties.getProperty("user.dir"));
        }

        ResourceLoader cpResourceLoader = null;

        Map sourcesToCopyMap = new HashMap();

        if (classpath != null) {
            cpResourceLoader = new PathResourceLoader(classpath);
        }

        File schemasDir = dirManager.createDir(classesDir, SCHEMAS_DIR_NAME + "/src");

        // build the in-memory type system
        XmlErrorWatcher errorListener = new XmlErrorWatcher(outerErrorListener);
        return loadTypeSystem(name, xsdFiles, wsdlFiles, urlFiles, configFiles,
                javaFiles, cpResourceLoader, download, noUpa, noPvr, noAnn, noVDoc, mdefNamespaces,
                baseDir, sourcesToCopyMap, errorListener, schemasDir, cmdLineEntRes, classpath, javasource, loader);
    }

    @SuppressWarnings("unchecked")
    public static boolean compile(Parameters params, MessagePrinter messagePrinter) {
        File baseDir = params.getBaseDir();
        File[] xsdFiles = params.getXsdFiles();
        File[] wsdlFiles = params.getWsdlFiles();
        URL[] urlFiles = params.getUrlFiles();
        File[] javaFiles = params.getJavaFiles();
        File[] configFiles = params.getConfigFiles();
        File[] classpath = params.getClasspath();
        File outputJar = params.getOutputJar();
        String name = params.getName();
        File srcDir = params.getSrcDir();
        File classesDir = params.getClassesDir();
        String compiler = params.getCompiler();
        String javasource = params.getJavaSource();
        String memoryInitialSize = params.getMemoryInitialSize();
        String memoryMaximumSize = params.getMemoryMaximumSize();
        boolean nojavac = params.isNojavac();
        boolean debug = params.isDebug();
        boolean verbose = params.isVerbose();
        boolean quiet = params.isQuiet();
        boolean download = params.isDownload();
        boolean noUpa = params.isNoUpa();
        boolean noPvr = params.isNoPvr();
        boolean noAnn = params.isNoAnn();
        boolean noVDoc = params.isNoVDoc();
        boolean incrSrcGen = params.isIncrementalSrcGen();
        SchemaTypeLoader typeLoader = params.getSchemaTypeLoader();
        FilerFactory filerFactory = params.getFilerFactory();
        Collection outerErrorListener = params.getErrorListener();

        String repackage = params.getRepackage();

        DirManager dirManager = filerFactory.getDirManager();

        if (!quiet && verbose) {
            messagePrinter.acceptInfo(XmlError.forMessage("Start compilation...", XmlError.SEVERITY_INFO));
        }

        if (repackage != null) {
            SchemaTypeLoaderImpl.METADATA_PACKAGE_LOAD = SchemaTypeSystemImpl.METADATA_PACKAGE_GEN;

            String stsPackage = SchemaTypeSystem.class.getPackage().getName();
            Repackager repackager = new Repackager(repackage);

            SchemaTypeSystemImpl.METADATA_PACKAGE_GEN = repackager.repackage(new StringBuffer(stsPackage)).toString().replace('.', '_');

            //System.err.println("\n\n\n" + stsPackage + ".SchemaCompiler  Metadata LOAD:" + SchemaTypeLoaderImpl.METADATA_PACKAGE_LOAD + " GEN:" + SchemaTypeSystemImpl.METADATA_PACKAGE_GEN);
        }

        SchemaCodePrinter codePrinter = params.getSchemaCodePrinter();
        try {

            Method setup = codePrinter.getClass().getMethod("setup", XmlOptions.class);
            if (setup != null) {
                XmlOptions options = new XmlOptions();
                if (params.isNoRadixEnums()) {
                    options.put("No Radix Enums");
                }
                setup.invoke(codePrinter, options);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        List<Extension> extensions = params.getExtensions();
        Set mdefNamespaces = params.getMdefNamespaces();

        EntityResolver cmdLineEntRes = params.getEntityResolver() == null ? ResolverUtil.resolverForCatalog(params.getCatalogFile()) : params.getEntityResolver();

        if (srcDir == null || classesDir == null) {
            throw new IllegalArgumentException("src and class gen directories may not be null.");
        }

        long start = System.currentTimeMillis();

        // Calculate the usenames based on the relativized filenames on the filesystem
        if (baseDir == null) {
            baseDir = new File(SystemProperties.getProperty("user.dir"));
        }

        ResourceLoader cpResourceLoader = null;

        Map sourcesToCopyMap = new HashMap();

        if (classpath != null) {
            cpResourceLoader = new PathResourceLoader(classpath);
        }

        boolean result = true;

        File schemasDir = dirManager.createDir(classesDir, SCHEMAS_DIR_NAME + "/src");

        // build the in-memory type system
        XmlErrorWatcher errorListener = new XmlErrorWatcher(outerErrorListener);
        SchemaTypeSystem system = loadTypeSystem(name, xsdFiles, wsdlFiles, urlFiles, configFiles,
                javaFiles, cpResourceLoader, download, noUpa, noPvr, noAnn, noVDoc, mdefNamespaces,
                baseDir, sourcesToCopyMap, errorListener, schemasDir, cmdLineEntRes, classpath, javasource, typeLoader);
        if (errorListener.hasError()) {
            result = false;
        }

        long finish = System.currentTimeMillis();
        if (!quiet && verbose) {
            messagePrinter.acceptInfo(XmlError.forMessage("Time to build schema type system: " + ((finish - start) / 1000.0) + " seconds", XmlError.SEVERITY_INFO));        // now code generate and compile the JAR
        }

        if (result && system != null) // todo: don't check "result" here if we want to compile anyway, ignoring invalid schemas
        {
            start = System.currentTimeMillis();

            // filer implementation writes binary .xsd and generated source to disk
            Repackager repackager = (repackage == null ? null : new Repackager(repackage));
            FilerImpl filer = filerFactory.getFiler(classesDir, srcDir, repackager, verbose, incrSrcGen);

            // currently just for schemaCodePrinter
            XmlOptions options = new XmlOptions();
            if (codePrinter != null) {
                options.setSchemaCodePrinter(codePrinter);
            }

            if (javasource != null) {
                options.setGenerateJavaVersion(javasource);            // save .xsb files
            }

            system.save(filer);

            // gen source files
            result &=
                    SchemaTypeSystemCompiler.generateTypes(system, filer, options);

            if (incrSrcGen) {
                dirManager.deleteObsoleteFiles(srcDir, srcDir,
                        new HashSet(filer.getSourceFiles()));
            }

            if (result) {
                finish = System.currentTimeMillis();
                if (!quiet && verbose) {
                    messagePrinter.acceptInfo(XmlError.forMessage("Time to generate code: " + ((finish - start) / 1000.0) + " seconds", XmlError.SEVERITY_INFO));
                }

            }

            // compile source
            if (result && !nojavac) {
                start = System.currentTimeMillis();

                List<File> sourcefiles = filer.getSourceFiles();

                if (javaFiles != null) {
                    sourcefiles.addAll(java.util.Arrays.asList(javaFiles));
                }

                if (!quiet && verbose) {
                    messagePrinter.acceptInfo(XmlError.forMessage("Compile with : " + compiler, XmlError.SEVERITY_INFO));
                }

                if (!CodeGenUtil.externalCompile(sourcefiles, classesDir, classpath, debug, compiler, javasource, memoryInitialSize, memoryMaximumSize, quiet, verbose)) {
                    result = false;
                }

                finish = System.currentTimeMillis();
                if (result && !quiet && verbose) {
                    messagePrinter.acceptInfo(XmlError.forMessage("Time to compile code: " + ((finish - start) / 1000.0) + " seconds", XmlError.SEVERITY_INFO));                // jar classes and .xsb
                }

                if (result && outputJar != null) {
                    try {
                        dirManager.jarDir(classesDir, outputJar);
                    } catch (IOException e) {
                        messagePrinter.acceptError(XmlError.forMessage("IO Error " + e.getMessage(), XmlError.SEVERITY_ERROR));
                        result =
                                false;
                    }

                    if (result && !quiet && verbose) {
                        messagePrinter.acceptInfo(XmlError.forMessage("Compiled types to: " + outputJar, XmlError.SEVERITY_INFO));
                    }

                }
            }
        }

        if (!result && !quiet) {
            messagePrinter.acceptError(XmlError.forMessage("BUILD FAILED"));
        } else {
            // call schema compiler extension if registered
            runExtensions(extensions, system, classesDir, messagePrinter);
        }

        if (cpResourceLoader != null) {
            cpResourceLoader.close();
        }

        return result;
    }

    private static void runExtensions(List<Extension> extensions, SchemaTypeSystem system, File classesDir, MessagePrinter messagePrinter) {
        if (extensions != null && extensions.size() > 0) {
            SchemaCompilerExtension sce = null;
            Iterator<Extension> i = extensions.iterator();
            Map<String, String> extensionParms = null;
            String classesDirName = null;
            try {
                classesDirName = classesDir.getCanonicalPath();
            } catch (java.io.IOException e) {
                messagePrinter.acceptWarning(XmlError.forMessage("Unable to get the path for schema jar file", XmlError.SEVERITY_WARNING));
                classesDirName =
                        classesDir.getAbsolutePath();
            }

            while (i.hasNext()) {
                Extension extension = i.next();
                try {
                    sce = (SchemaCompilerExtension) extension.getClassName().newInstance();
                } catch (InstantiationException e) {
                    messagePrinter.acceptError(XmlError.forMessage("UNABLE to instantiate schema compiler extension:" + extension.getClassName().getName()));
                    messagePrinter.acceptError(XmlError.forMessage("EXTENSION Class was not run"));
                    break;

                } catch (IllegalAccessException e) {
                    messagePrinter.acceptError(XmlError.forMessage("ILLEGAL ACCESS Exception when attempting to instantiate schema compiler extension: " + extension.getClassName().getName()));
                    messagePrinter.acceptError(XmlError.forMessage("EXTENSION Class was not run"));
                    break;

                }
                messagePrinter.acceptInfo(XmlError.forMessage("Running Extension: " + sce.getExtensionName(), XmlError.SEVERITY_INFO));
                extensionParms =
                        new HashMap<String, String>();
                Iterator parmsi = extension.getParams().iterator();
                while (parmsi.hasNext()) {
                    Extension.Param p = (Extension.Param) parmsi.next();
                    extensionParms.put(p.getName(), p.getValue());
                }

                extensionParms.put("classesDir", classesDirName);
                sce.schemaCompilerExtension(system, extensionParms);
            }

        }
    }

    private static boolean wsdlContainsEncoded(XmlObject wsdldoc) {
        // search for any <soap:body use="encoded"/> etc.
        XmlObject[] useAttrs = wsdldoc.selectPath(
                "declare namespace soap='http://schemas.xmlsoap.org/wsdl/soap/' "
                + ".//soap:body/@use|.//soap:header/@use|.//soap:fault/@use");
        for (int i = 0; i
                < useAttrs.length; i++) {
            if ("encoded".equals(((SimpleValue) useAttrs[i]).getStringValue())) {
                return true;
            }

        }
        return false;
    }
    private static final String CONFIG_URI = "http://xml.apache.org/xmlbeans/2004/02/xbean/config";
    private static final String COMPATIBILITY_CONFIG_URI = "http://www.bea.com/2002/09/xbean/config";
    private static final Map<String, String> MAP_COMPATIBILITY_CONFIG_URIS;

    static {

        MAP_COMPATIBILITY_CONFIG_URIS = new HashMap<String, String>();
        MAP_COMPATIBILITY_CONFIG_URIS.put(COMPATIBILITY_CONFIG_URI, CONFIG_URI);
    }
}
