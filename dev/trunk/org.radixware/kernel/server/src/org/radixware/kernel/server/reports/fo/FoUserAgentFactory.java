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
package org.radixware.kernel.server.reports.fo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.apps.io.ResourceResolverFactory;
import static org.radixware.kernel.server.reports.fo.FopReportGenerator.DEFAULT_REPORT_DPI;
import org.radixware.kernel.starter.log.StarterLog;
import org.xml.sax.SAXException;

class FoUserAgentFactory {
//    private String getFopCfg() {
//        return "<?xml version=\"1.0\"?>\n"
//                + "<fop version=\"1.0\">\n"
//                + "  <base>.</base>\n"
//                + "  <renderers>\n"
//                + "    <renderer mime=\"application/pdf\">\n"
//                + "      <filterList>\n"
//                + "        <value>flate</value>\n"
//                + "      </filterList>\n"
//                + "      <fonts>\n"
//                + //			"	    <directory>C:\\Windows\\Fonts</directory>\n"+
//                //			"		<!-- <auto-detect/> -->\n"+
//                "		<auto-detect/>\n"
//                + "        <!-- embedded fonts -->\n"
//                + "      </fonts>\n"
//                + "    </renderer>\n"
//                + "    <renderer mime=\"text/plain\">\n"
//                + "      <pageSize columns=\"80\"/>\n"
//                + "    </renderer>\n"
//                + "  </renderers>\n"
//                + "</fop>";
//    }
//    private FopFactory createFopFactory() throws SAXException, IOException, ConfigurationException {
//        final FopFactory fopFactory = FopFactory.newInstance();
//        final DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
//        final InputStream cfgStream = new ByteArrayInputStream(getFopCfg().getBytes());
//        final Configuration cfg = cfgBuilder.build(cfgStream);
//        fopFactory.setUserConfig(cfg);
//        return fopFactory;
//    }

    private static void disableWarningsOfClass(String className) {
        final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(className);

        if (log instanceof org.apache.commons.logging.impl.Jdk14Logger) {
            org.apache.commons.logging.impl.Jdk14Logger jdk14Logger = (org.apache.commons.logging.impl.Jdk14Logger) log;
            jdk14Logger.getLogger().setLevel(java.util.logging.Level.SEVERE);
        } else if (log instanceof org.apache.commons.logging.impl.Log4JLogger) {
            //org.apache.commons.logging.impl.Log4JLogger log4JLogger = (org.apache.commons.logging.impl.Log4JLogger) log;
            System.out.println("-----------------------------------!");
            System.out.println(log.getClass());
//            log4JLogger.getLogger().setLevel(java.util.logging.Level.OFF);
        } else if (log instanceof StarterLog) {
            StarterLog st = (StarterLog) log;
            st.setLevel(Level.SEVERE);
        }

    }

    private static String getFopCfg() {
        return "<?xml version=\"1.0\"?>"
                + "    <fop version=\"1.0\">"
                + "        <base>.</base>"
                + "        <renderers>"
                + "            <renderer mime=\"application/pdf\">"
                + "                <filterList>"
                + "                    <value>flate</value>"
                + "                </filterList>"
                + "                <fonts>"
                //+ "                    <!-- <directory>C:\\Windows\\Fonts</directory> -->"
                + "                    <auto-detect/>"
                // + "                    <!-- embedded fonts -->"
                + "                </fonts>"
                + "            </renderer>"
                + "            <renderer mime=\"text/plain\">"
                + "                <pageSize columns=\"80\"/>"
                + "            </renderer>"
                + "        </renderers>"
                + "    </fop>";
    }

    private static FOUserAgent createFoUserAgent() throws SAXException, IOException, ConfigurationException {
        disableWarningsOfClass("org.apache.fop.layoutmgr.BlockContainerLayoutManager");
        disableWarningsOfClass("org.apache.fop.layoutmgr.PageBreakingAlgorithm");
        disableWarningsOfClass("org.apache.fop.layoutmgr.BreakingAlgorithm");
        disableWarningsOfClass("org.apache.fop.fonts.FontCache"); // WARNING: I/O exception while reading font cache (org.apache.fop.fonts.FontCache;
        disableWarningsOfClass("org.apache.fop.events.LoggingEventListener"); // WARNING: Unable to load font file: file:/C:/Windows/FONTS/CAMBRIA.TTC. Reason: java.lang.IllegalArgumentException: For TrueType collection you must specify which font to select (-ttcname)
        disableWarningsOfClass("org.apache.fop.apps.FOUserAgent"); // Content overflows the viewport of an
        disableWarningsOfClass("org.apache.fop.fo.properties.PropertyMaker");
        disableWarningsOfClass("org.apache.fop.render.intermediate.IFRenderer");
        disableWarningsOfClass("org.apache.fop.area.AreaTreeHandler");
        disableWarningsOfClass("org.apache.fop.area.PageViewport");
        disableWarningsOfClass("org.apache.fop.layoutmgr.PageBreakingAlgorithm");
        disableWarningsOfClass("org.apache.fop.layoutmgr.BreakingAlgorithm");
        disableWarningsOfClass("org.apache.fop.layoutmgr.AbstractLayoutManager");
        disableWarningsOfClass("org.apache.fop.pdf.PDFObject");
        disableWarningsOfClass("org.apache.fop.pdf.PDFNumbe");
        disableWarningsOfClass("org.apache.fop.fonts.truetype.TTFFile");
        disableWarningsOfClass("org.apache.fop.layoutmgr.PageSequenceLayoutManager");
        disableWarningsOfClass("org.apache.fop.fonts.FontInfo");
        disableWarningsOfClass("org.apache.fop.layoutmgr.SpaceResolver");
        disableWarningsOfClass("org.apache.fop.fo.FOTreeBuilder");
        disableWarningsOfClass("org.apache.fop.layoutmgr.AbstractPageSequenceLayoutManager");
        disableWarningsOfClass("org.apache.fop.area.IDTracker");
        disableWarningsOfClass("org.apache.fop.layoutmgr.AbstractBreaker");
        disableWarningsOfClass("org.apache.fop.layoutmgr.PageProvider");
        disableWarningsOfClass("org.apache.fop.layoutmgr.inline.LineLayoutManager");
        disableWarningsOfClass("org.apache.fop.fonts.autodetect.FontFileFinder");
        disableWarningsOfClass("org.apache.fop.render.AbstractConfigurator");
        disableWarningsOfClass("org.apache.fop.render.ImageHandlerRegistry");
        disableWarningsOfClass("org.apache.fop.render.XMLHandlerRegistry");
        disableWarningsOfClass("org.apache.xmlgraphics.image.loader.spi.ImageImplRegistry");
        disableWarningsOfClass("org.apache.fop.util.ContentHandlerFactoryRegistry");
        disableWarningsOfClass("org.apache.fop.fonts.FontInfoConfigurator");
        disableWarningsOfClass("org.apache.fop.render.PrintRendererConfigurator");
        disableWarningsOfClass("org.apache.fop.render.RendererFactory");
        disableWarningsOfClass("FOP");
        disableWarningsOfClass("org.apache.fop.apps.FopFactoryConfigurator");
        
        String filePath = findFontsDir();
        FopFactoryBuilder builder;
        if (filePath != null){
            builder = new FopFactoryBuilder(new File(filePath).toURI());
        } else {
            builder = new FopFactoryBuilder(new File("./fonts").toURI());
        }
        
        builder.setComplexScriptFeatures(true);
        builder.setSourceResolution(DEFAULT_REPORT_DPI);
        builder.setTargetResolution(DEFAULT_REPORT_DPI);
        builder.setConfiguration(createConfiguration());
        FopFactory factory = builder.build();
        return factory.newFOUserAgent();
    }

    static Configuration createConfiguration() throws SAXException, IOException, ConfigurationException {
        final DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
        final InputStream cfgStream = new ByteArrayInputStream(getFopCfg().getBytes());
        final Configuration cfg = cfgBuilder.build(cfgStream);
        disableWarningsOfClass("org.apache.fop.layoutmgr.BlockContainerLayoutManager");
        disableWarningsOfClass("org.apache.fop.layoutmgr.PageBreakingAlgorithm");
        disableWarningsOfClass("org.apache.fop.layoutmgr.BreakingAlgorithm");
        disableWarningsOfClass("org.apache.fop.fonts.FontCache"); // WARNING: I/O exception while reading font cache (org.apache.fop.fonts.FontCache;
        disableWarningsOfClass("org.apache.fop.events.LoggingEventListener"); // WARNING: Unable to load font file: file:/C:/Windows/FONTS/CAMBRIA.TTC. Reason: java.lang.IllegalArgumentException: For TrueType collection you must specify which font to select (-ttcname)
        disableWarningsOfClass("org.apache.fop.apps.FOUserAgent"); // Content overflows the viewport of an
        disableWarningsOfClass("org.apache.fop.fo.properties.PropertyMaker");
        disableWarningsOfClass("org.apache.fop.render.intermediate.IFRenderer");
        disableWarningsOfClass("org.apache.fop.area.AreaTreeHandler");
        disableWarningsOfClass("org.apache.fop.area.PageViewport");
        disableWarningsOfClass("org.apache.fop.layoutmgr.PageBreakingAlgorithm");
        disableWarningsOfClass("org.apache.fop.layoutmgr.BreakingAlgorithm");
        disableWarningsOfClass("org.apache.fop.layoutmgr.AbstractLayoutManager");
        disableWarningsOfClass("org.apache.fop.pdf.PDFObject");
        disableWarningsOfClass("org.apache.fop.pdf.PDFNumbe");
        disableWarningsOfClass("org.apache.fop.fonts.truetype.TTFFile");
        disableWarningsOfClass("org.apache.fop.layoutmgr.PageSequenceLayoutManager");
        disableWarningsOfClass("org.apache.fop.fonts.FontInfo");
        disableWarningsOfClass("org.apache.fop.layoutmgr.SpaceResolver");
        disableWarningsOfClass("org.apache.fop.fo.FOTreeBuilder");
        disableWarningsOfClass("org.apache.fop.layoutmgr.AbstractPageSequenceLayoutManager");
        disableWarningsOfClass("org.apache.fop.area.IDTracker");
        disableWarningsOfClass("org.apache.fop.layoutmgr.AbstractBreaker");
        disableWarningsOfClass("org.apache.fop.layoutmgr.PageProvider");
        disableWarningsOfClass("org.apache.fop.layoutmgr.inline.LineLayoutManager");
        disableWarningsOfClass("org.apache.fop.fonts.autodetect.FontFileFinder");
        disableWarningsOfClass("org.apache.fop.render.AbstractConfigurator");
        disableWarningsOfClass("org.apache.fop.render.ImageHandlerRegistry");
        disableWarningsOfClass("org.apache.fop.render.XMLHandlerRegistry");
        disableWarningsOfClass("org.apache.xmlgraphics.image.loader.spi.ImageImplRegistry");
        disableWarningsOfClass("org.apache.fop.util.ContentHandlerFactoryRegistry");
        disableWarningsOfClass("org.apache.fop.fonts.FontInfoConfigurator");
        disableWarningsOfClass("org.apache.fop.render.PrintRendererConfigurator");
        disableWarningsOfClass("org.apache.fop.render.RendererFactory");
        disableWarningsOfClass("FOP");
        disableWarningsOfClass("org.apache.fop.apps.FopFactoryConfigurator");
        return cfg;
    }
    private static FOUserAgent pdfFoUserAgentInstance;

    public static FOUserAgent getFoUserAgent(final String mimeFormat) throws SAXException, IOException, ConfigurationException {

        if (MimeConstants.MIME_PDF.equals(mimeFormat)) {
            synchronized (FoUserAgentFactory.class) {
                if (pdfFoUserAgentInstance == null) {
                    pdfFoUserAgentInstance = createFoUserAgent();
                    // RADIX-4610
                    //    final Renderer renderer = pdfFoUserAgentInstance.getRendererFactory().createRenderer(pdfFoUserAgentInstance, mimeFormat);
                    //    pdfFoUserAgentInstance.setRendererOverride(renderer);
                }
                return pdfFoUserAgentInstance;
            }
        } else {
            return createFoUserAgent();
        }
    }

    public static String findFontsDir() {
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            if (FONT_DIR_ENV.equals(envName)){
                return env.get(envName);
            }
        }
        return null;
    }
    
    public static String FONT_DIR_ENV = "RDX_REPORT_EXT_FONTS_DIR";
}
