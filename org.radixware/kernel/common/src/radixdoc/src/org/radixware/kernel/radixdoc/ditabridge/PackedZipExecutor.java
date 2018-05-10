/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.radixdoc.ditabridge;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.radixdoc.enums.EDecorationProperties;

public class PackedZipExecutor {

    public static final String SOURCE_DIR = "./source";
    public static final String TARGET_DIR = "./target";
    public static final String P_I = "-i";
    public static final String P_INPUT = "-input";
    public static final String P_F = "-f";
    public static final String P_FORMAT = "-format";
    public static final String P_O = "-o";
    public static final String P_OUTPUT = "-output";

    public static final String DECOR_PROPS = "style.properties";

    public static final String DECOR_PROPS_MAIN_LOGO = "mainLogo";
    public static final String DECOR_PROPS_PAGE_LOGO = "pageLogo";

    private static final String[][] DECORATON_LOCATOR = new String[][]{
        {DECOR_PROPS_MAIN_LOGO, "customization/common/artwork/logo.png"},
        {DECOR_PROPS_PAGE_LOGO, "customization/common/artwork/companylogo.png"}
    };
    private static final Set<String> DECORATION_AVAILABLE_PROPS = new HashSet<>();

    private static final File TEMP_DIR = new File(System.getProperty("java.io.tmpdir"));
    private static final String ZIP_RESOURCE = "dita.zip";
    private static final String ZIP_DECORATION = "defaultdecoration.zip";

    private static final String XML_CUSTOM_FILE_LOCATION = "customization/fo/attrs/custom.xsl";
    private static final String XML_BEFORE = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\" version=\"2.0\">";
    private static final String XML_ATTR = "\t<xsl:variable name=\"%1$s\">%2$s</xsl:variable>";
    private static final String XML_AFTER = "</xsl:stylesheet>";
    private static final String[] XML_UPLOAD_LIST = {
        EDecorationProperties.COMPANY_NAME, EDecorationProperties.COPYRIGHT,
        EDecorationProperties.FOOTER_TEXT, EDecorationProperties.DOC_LANGUAGE,
        EDecorationProperties.LAYER_URI, EDecorationProperties.LAYER_VERSION_BASE,
        EDecorationProperties.LAYER_VERSION_FULL, EDecorationProperties.GENERATION_DATE,
        EDecorationProperties.GENERATION_TIME, EDecorationProperties.SUBTITLE,
        EDecorationProperties.TARGET_NAMESPACE, EDecorationProperties.HEADER_TEXT,
        EDecorationProperties.VERSIONS_MAPPING, EDecorationProperties.CHANGE_LOG_HISTORY_DEPTH
    };

    static {
        DECORATION_AVAILABLE_PROPS.add(DECOR_PROPS_MAIN_LOGO);
        DECORATION_AVAILABLE_PROPS.add(DECOR_PROPS_PAGE_LOGO);
        DECORATION_AVAILABLE_PROPS.add(EDecorationProperties.COMPANY_NAME);
        DECORATION_AVAILABLE_PROPS.add(EDecorationProperties.COPYRIGHT);
        DECORATION_AVAILABLE_PROPS.add(EDecorationProperties.FOOTER_TEXT);
        DECORATION_AVAILABLE_PROPS.add(EDecorationProperties.LAYER_URI);
        DECORATION_AVAILABLE_PROPS.add(EDecorationProperties.LAYER_VERSION_BASE);
        DECORATION_AVAILABLE_PROPS.add(EDecorationProperties.LAYER_VERSION_FULL);
        DECORATION_AVAILABLE_PROPS.add(EDecorationProperties.GENERATION_DATE);
        DECORATION_AVAILABLE_PROPS.add(EDecorationProperties.GENERATION_TIME);
        DECORATION_AVAILABLE_PROPS.add(EDecorationProperties.DOC_LANGUAGE);
        DECORATION_AVAILABLE_PROPS.add(EDecorationProperties.SUBTITLE);
        DECORATION_AVAILABLE_PROPS.add(EDecorationProperties.TARGET_NAMESPACE);
        DECORATION_AVAILABLE_PROPS.add(EDecorationProperties.HEADER_TEXT);
        DECORATION_AVAILABLE_PROPS.add(EDecorationProperties.VERSIONS_MAPPING);
        DECORATION_AVAILABLE_PROPS.add(EDecorationProperties.CHANGE_LOG_HISTORY_DEPTH);
    }

    public static void unpackAndExecute(final InputStream sourceData, final OutputStream targetData, final Properties parameters) throws IOException {
        try (final InputStream zipSource = PackedZipExecutor.class.getResourceAsStream(ZIP_DECORATION);
                final ZipInputStream zis = new ZipInputStream(zipSource)) {
            unpackAndExecute(sourceData, targetData, zis, parameters);
        }
    }

    public static String unpackAndExecute(final InputStream sourceData, final OutputStream targetData, final String... parameters) throws IOException {
        try (final InputStream zipSource = PackedZipExecutor.class.getResourceAsStream(ZIP_DECORATION);
                final ZipInputStream zis = new ZipInputStream(zipSource)) {
            return unpackAndExecute(sourceData, targetData, zis, parameters);
        }
    }

    public static String unpackAndExecute(final InputStream sourceData, final OutputStream targetData, final ZipInputStream decorations, final String... parameters) throws IOException {
        if (parameters == null || parameters.length == 0) {
            throw new IllegalArgumentException("Parameters can't be null or empty array");
        } else if (parameters.length % 2 != 0) {
            throw new IllegalArgumentException("Odd parameters amount in the " + Arrays.toString(parameters) + ". Parameters need be a key/value pairs!");
        } else {
            final Properties props = new Properties();

            for (int index = 0; index < parameters.length; index += 2) {
                if (parameters[index] == null || parameters[index].isEmpty()) {
                    throw new IllegalArgumentException("Parameter number " + index + " in the " + Arrays.toString(parameters) + " is null");
                } else if (parameters[index + 1] == null || parameters[index + 1].isEmpty()) {
                    throw new IllegalArgumentException("Parameter number " + (index + 1) + " in the " + Arrays.toString(parameters) + " is null");
                } else {
                    props.setProperty(parameters[index], parameters[index + 1]);
                }
            }
            return unpackAndExecute(sourceData, targetData, decorations, props);
        }
    }

    public static String unpackAndExecute(final InputStream sourceData, final OutputStream targetData, final ZipInputStream decorations, final Properties parameters) throws IOException {
        String log = null;        
        if (sourceData == null) {
            throw new IOException("Source data can't be null");
        } else if (parameters == null) {
            throw new IOException("Parameters can't be null");
        } else if (targetData == null) {
            throw new IOException("Target data can't be null");
        } else if (decorations == null) {
            throw new IOException("Decorations can't be null");
        } else if (!parameters.containsKey(P_I) && !parameters.containsKey(P_INPUT)) {
            throw new IOException("Properties not contain mandatory parameter [" + P_I + "] or [" + P_INPUT + "]");
        } else if (!parameters.containsKey(P_F) && !parameters.containsKey(P_FORMAT)) {
            throw new IOException("Properties not contain mandatory parameter [" + P_F + "] or [" + P_FORMAT + "]");
        } else if (parameters.containsKey(P_O) && parameters.containsKey(P_OUTPUT)) {
            throw new IOException("Properties: attempt to redefine [" + P_O + "] or [" + P_OUTPUT + "] parameter(s)! Remove this parameter(s) from the properteis");
        } else {
            final int random = (int) (1000000000 * Math.random());
            final File zipDir = new File(TEMP_DIR, "zip" + random);
            final File sourceDir = new File(zipDir, SOURCE_DIR);
            final File targetDir = new File(zipDir, TARGET_DIR);
            final DecorationRepo decor = new DecorationRepo(decorations);

            if (!zipDir.mkdir()) {
                throw new IOException("Temporary directory [" + zipDir.getAbsolutePath() + "] was not created");
            } else if (!sourceDir.mkdir()) {
                throw new IOException("Temporary source directory [" + sourceDir.getAbsolutePath() + "] was not created");
            } else if (!targetDir.mkdir()) {
                throw new IOException("Temporary target directory [" + targetDir.getAbsolutePath() + "] was not created");
            } else {
                try {
                    try (final InputStream zip = PackedZipExecutor.class.getResourceAsStream(ZIP_RESOURCE)) {
                        unzipData(zipDir, zip);
                    }
                    for (DecorationItem item : decor) {
                        if (item.target != null) {
                            final File f = new File(zipDir, item.target);

                            f.getParentFile().mkdirs();
                            try (final OutputStream os = new FileOutputStream(f)) {
                                os.write(item.content);
                                os.flush();
                            }
                        }
                    }
                    try (final OutputStream os = new FileOutputStream(new File(zipDir, XML_CUSTOM_FILE_LOCATION));
                            final PrintStream ps = new PrintStream(os, true, "utf-8")) {
                        uploadAttributes(decor, ps);
                    }
                    unzipData(sourceDir, sourceData);

                    log = processData(zipDir, parameters);

                    try (final ZipOutputStream zos = new ZipOutputStream(targetData)) {
                        zipData(targetDir, targetDir, zos);
                        zos.flush();
                    }
                } finally {
                    removeContent(zipDir);
                }
            }
        }
        return log;
    }

    static void uploadAttributes(final DecorationRepo repo, final PrintStream os) throws IOException {
        if (repo == null) {
            throw new IllegalArgumentException("Repo can't be null");
        } else if (os == null) {
            throw new IllegalArgumentException("Output stream can't be null");
        } else {
            os.println(XML_BEFORE);
            for (String item : XML_UPLOAD_LIST) {
                os.println(String.format(XML_ATTR, item, repo.getProperty(item, item)));
            }
            os.println(XML_AFTER);
            os.flush();
        }
    }

    private static void unzipData(final File zipDir, final InputStream zip) throws IOException {
        try (final ZipInputStream zis = new ZipInputStream(zip)) {
            ZipEntry ze;

            while ((ze = zis.getNextEntry()) != null) {
                if (!ze.getName().endsWith("/")) {
                    final File target = new File(zipDir, ze.getName());

                    target.getParentFile().mkdirs();
                    target.setReadable(true, true);
                    target.setExecutable(true, true);
                    Files.copy(zis, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    private static String processData(final File workDir, final Properties parameters) throws IOException {
        final List<String> list = new ArrayList<>();

        boolean isWindows = SystemTools.isWindows;

        if (isWindows) {
            list.add(new File(workDir, "bin/dita.bat").getAbsolutePath());
        } else {
            list.add("sh");
            list.add(new File(workDir, "bin/dita").getAbsolutePath());
        }

        list.add(P_I);
        list.add(SOURCE_DIR + '/' + parameters.getProperty(P_I, parameters.getProperty(P_INPUT)));
        list.add(P_F);
        list.add(parameters.getProperty(P_F, parameters.getProperty(P_FORMAT)));
        list.add(P_O);
        list.add(TARGET_DIR);

        for (Entry<Object, Object> item : parameters.entrySet()) {
            if (!item.getKey().equals(P_I) && !item.getKey().equals(P_INPUT) && !item.getKey().equals(P_F) && !item.getKey().equals(P_FORMAT)) {
                list.add(item.getKey().toString());
                list.add(item.getValue().toString());
            }
        }

        final ProcessBuilder pb = new ProcessBuilder(list);

        pb.directory(workDir);
        pb.redirectErrorStream(true);
        final Process p = pb.start();

        p.getOutputStream().close();
        final String message;
        try (final InputStream is = p.getInputStream();
                final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            final byte[] buffer = new byte[8192];
            int len;

            while ((len = is.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }
            message = baos.toString();
        }

        try {
            p.waitFor();
        } catch (InterruptedException e) {
            p.destroy();
            Thread.currentThread().interrupt();
            throw new IOException("Execution terminated by Thread.interrupt signal");
        }
        final int rc = p.exitValue();

        if (rc != 0) {
            throw new IOException("Failed execution (RC=" + rc + ") : " + message);
        }
        
        return message;
    }

    private static void zipData(final File targetDir, final File actual, final ZipOutputStream targetData) throws IOException {
        if (actual.isDirectory()) {
            actual.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    try {
                        zipData(targetDir, pathname, targetData);
                    } catch (IOException e) {
                    }
                    return false;
                }
            }
            );
        } else {
            targetData.putNextEntry(new ZipEntry(actual.getAbsolutePath().substring(targetDir.getAbsolutePath().length() + 1).replace('\\', '/')) {
                {
                    setMethod(ZipEntry.DEFLATED);
                }
            });
            Files.copy(actual.toPath(), targetData);
            targetData.closeEntry();
        }
    }

    private static void removeContent(final File zipDir) {
        if (zipDir.isDirectory()) {
            zipDir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    removeContent(pathname);
                    return false;
                }
            }
            );
        }
        zipDir.delete();
    }

    static class DecorationItem {

        final String name;
        final byte[] content;
        String target = null;

        public DecorationItem(final String name, final byte[] content) {
            this.name = name;
            this.content = content;
        }
    }

    static class DecorationRepo implements Iterable<DecorationItem> {

        private static final Set<EIsoLanguage> LANGS_AVAILABLE = new HashSet<>();

        static {
            LANGS_AVAILABLE.add(EIsoLanguage.ENGLISH);
            LANGS_AVAILABLE.add(EIsoLanguage.RUSSIAN);
        }

        private final List<DecorationItem> list = new ArrayList<>();
        private final Properties props = new Properties();

        DecorationRepo(final ZipInputStream zip) throws IOException {
            if (zip == null) {
                throw new IllegalArgumentException("Zip input stream can't be null");
            } else {
                final StringBuilder sb = new StringBuilder();
                ZipEntry ze;

                while ((ze = zip.getNextEntry()) != null) {
                    if (DECOR_PROPS.equals(ze.getName())) {
                        props.load(zip);
                    } else {
                        try (final ByteArrayOutputStream container = new ByteArrayOutputStream()) {
                            final byte[] buffer = new byte[8192];
                            int len;

                            while ((len = zip.read(buffer)) > 0) {
                                container.write(buffer, 0, len);
                            }
                            container.flush();
                            list.add(new DecorationItem(ze.getName(), container.toByteArray()));
                        }
                    }
                }
                if (props.size() == 0) {
                    throw new IllegalArgumentException("Decoration zip input stream not contains [" + DECOR_PROPS + "] part or this part is empty!");
                }

                for (Entry<Object, Object> item : props.entrySet()) {
                    if (!DECORATION_AVAILABLE_PROPS.contains(item.getKey())) {
                        sb.append(',').append(item.getKey());
                    }
                }
                if (sb.length() > 0) {
                    throw new IllegalArgumentException("Decoration zip input stream contains unknown key(s) [" + sb.toString().substring(1) + "] in the [" + DECOR_PROPS + "] part!");
                }

                if (!props.containsKey(EDecorationProperties.DOC_LANGUAGE)) {
                    throw new IllegalArgumentException("Decoration zip input stream: [" + DECOR_PROPS + "] part not contains mandatory key [" + EDecorationProperties.DOC_LANGUAGE + "]!");
                } else {
                    try {
                        EIsoLanguage.getForValue(props.getProperty(EDecorationProperties.DOC_LANGUAGE));
                    } catch (NoConstItemWithSuchValueError exc) {
                        throw new IllegalArgumentException("Decoration zip input stream: [" + DECOR_PROPS + "] part contains illegal value [" + props.getProperty(EDecorationProperties.DOC_LANGUAGE) + "] for key [" + EDecorationProperties.DOC_LANGUAGE + "]!");
                    }
                }

                loop:
                for (String[] item : DECORATON_LOCATOR) {
                    if (props.containsKey(item[0])) {
                        final String fileName = props.getProperty(item[0]);

                        for (DecorationItem member : this) {
                            if (fileName.equals(member.name)) {
                                member.target = item[1];
                                continue loop;
                            }
                        }
                        throw new IllegalArgumentException("Decoration zip input stream: [" + DECOR_PROPS + "] part contains key [" + item[0] + "] that is referenced to non-existent zip part [" + fileName + "]!");
                    }
                }
            }
        }

        @Override
        public Iterator<DecorationItem> iterator() {
            return list.iterator();
        }

        public String getProperty(final String key) {
            if (key == null || key.isEmpty()) {
                throw new IllegalArgumentException("Property key can't be null or empty");
            } else {
                return props.containsKey(key) ? substitute(props.getProperty(key)) : null;
            }
        }

        public String getProperty(final String key, final String defaultValue) {
            if (key == null || key.isEmpty()) {
                throw new IllegalArgumentException("Property key can't be null or empty");
            } else if (defaultValue == null || defaultValue.isEmpty()) {
                throw new IllegalArgumentException("Default value can't be null or empty");
            } else {
                return substitute(props.getProperty(key, defaultValue));
            }
        }

        private String substitute(final String content) {
            if (content.contains("${")) {
                final String[] parts = content.split("\\$\\{");
                final StringBuilder sb = new StringBuilder(parts[0]);

                for (int index = 1; index < parts.length; index++) {
                    final String[] leftRight = parts[index].split("\\}");
                    sb.append(props.getProperty(leftRight[0], "${" + leftRight[0] + "}"));
                    if (leftRight.length > 1) {
                        sb.append(leftRight[1]);
                    }
                }
                return sb.toString();
            } else {
                return content;
            }
        }
    }
}
