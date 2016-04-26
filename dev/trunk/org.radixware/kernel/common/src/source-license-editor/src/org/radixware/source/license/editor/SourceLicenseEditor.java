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
package org.radixware.source.license.editor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import static org.radixware.source.license.editor.SourceLicenseEditor.mergeLines;
import static org.radixware.source.license.editor.SourceLicenseEditor.writeStringToFile;

//  "Какие-то русские буквы !;%:?*()".getBytes());
public class SourceLicenseEditor {

    private final static String COMPASS_STRING = "Compass Plus Limited.";
    private final static String AUTHOR_STRING = "@author";
    private final static String TO_CHANGE_THIS_TEMPLATE = "To change this template, choose Tools | Templates";
    private final static String COPYRIGHT = " Copyright ";
    private final static String LICENSED = " Licensed ";
    private final static String MAIN_LICENSE_TEXT = /*" " +*/
            "/*\n"
            + " * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.\n"
            + " *\n"
            + " * This Source Code Form is subject to the terms of the Mozilla Public License,\n"
            + " * v. 2.0. If a copy of the MPL was not distributed with this file, You can\n"
            + " * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed\n"
            + " * WITHOUT ANY WARRANTY; including any implied warranties but not limited to\n"
            + " * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the\n"
            + " * Mozilla Public License, v. 2.0. for more details.\n"
            + " */";
    private final static String UTF8 = "UTF8";
    private final static String WIN1251 = "windows-1251";

    private static boolean checkCharset(final File file, final String charset) throws IOException {

        final File tmpFile = File.createTempFile("rdx", "license");
        try {
            String stringUTF8 = readStringFromFile(file, charset);
            writeStringToFile(tmpFile, stringUTF8, charset);
            return comparyBinary(file, tmpFile);
        } finally {
            deleteFileImpl(tmpFile);
        }
    }

    
//    private final static String SERVER_FILES[] = {"ViewUnitsPanel.java", "ChannelPort.java", "SocketChannelClientPort.java", "SocketChannelServerPort.java", "ChannelPort.java", "ChannelPortsTest.java", "CreateRequest.java"};
    
    private static boolean canIgnore(File file){
//        for (String serverFile : SERVER_FILES){
//            if (serverFile.equals(file.getName())){
//                return true;
//            }            
//        }
        return false;
    }
    
    private static boolean comparyBinary(final File file1, final File file2) throws IOException {
        if (file1.length() != file2.length()) {
            return false;
        }

        try (InputStream in1 = new BufferedInputStream(new FileInputStream(file1));
                InputStream in2 = new BufferedInputStream(new FileInputStream(file2));) {

            int value1, value2;
            do {
                value1 = in1.read();
                value2 = in2.read();
                if (value1 != value2) {
                    return false;
                }
            } while (value1 >= 0);
            return true;
        }

    }

    private static String getCharsetName(final File file) throws IOException {
        if (checkCharset(file, UTF8)) {
            return UTF8;
        }

        if (checkCharset(file, WIN1251)) {
            return WIN1251;
        }

        throw new RuntimeException("Unknown сharset in file \'" + file.getAbsolutePath() + "\'");
    }

    /**
     * @param args[0] must be project dir or java file
     */
    public static void main(final String[] args) throws IOException {



        if (args.length == 0) {
            throw new SourceLicenseEditor.SourceLicenseEditorException("Empty argument list. Use command: java -jar source-license-editor.jar [project dir | java file]");
        }
        if (args.length > 1) {
            throw new SourceLicenseEditor.SourceLicenseEditorException("Too many arguments. Use command: java -jar source-license-editor.jar [project dir | java file]");
        }

        final String fileOrDirPath = args[0];
        final File fileOrDir = new File(fileOrDirPath);
        if (!fileOrDir.exists()) {
            throw new SourceLicenseEditor.SourceLicenseEditorException("\'" + fileOrDirPath + "\' not found.");
        }

        final SourceLicenseEditor.BigCommentParser parser = new SourceLicenseEditor.BigCommentParser();

        if (fileOrDir.isDirectory()) {
            final int processedFiles = processDirectory(fileOrDir, parser);
            System.out.println(String.valueOf(processedFiles) + " files changed.");
        } else {
            processFile(fileOrDir, parser);
        }
        
//        Collections.sort(autors);
//        StringBuilder ssb = new StringBuilder();
//        for (String autor : autors){
//            ssb.append(", \"");
//            ssb.append(autor);
//            ssb.append("\"");
//        }
//        System.out.println(ssb);
    }
    private static String AUTHORS_ARR[] = {AUTHOR_STRING, "aalexeeva", "aatapin", "abelyaev", "agrabareva", "ak", "akaplanov", "akaptsan", "akiliyevich", "akrylov", "akrylovget", "akrylovi", "anikiforova", "ashamsutdinov", "atapin", "bao", "dgrigorenko", "dkanatova", "dmorozov", "dsafonov", "dsafonv", "ebarkhanova", "igorh", "mgukov", "mmarinchenko", "npopov", "nvayner", "salekseev", "sgorbunov", "vmohov", "vmokhov", "vrymar", "ygalkina", "yremizov", "ysnegirev"};
    private static List<String> AUTHORS = Arrays.asList(AUTHORS_ARR);
    

    private static int processDirectory(final File directory, final SourceLicenseEditor.BigCommentParser parser) throws IOException {
        int processedFiles = 0;
        for (final File childFileOrDir : directory.listFiles()) {
            if (childFileOrDir.isDirectory()) {
                processedFiles += processDirectory(childFileOrDir, parser);
            } else {
                if (childFileOrDir.getName().trim().toLowerCase(Locale.ENGLISH).endsWith(".java")) {
                    if (processFile(childFileOrDir, parser)) {
                        processedFiles++;
                    }
                }
            }
        }
        return processedFiles;
    }
    
    private static List<String> parseLines(final String text) {
        List<String> lines = new LinkedList();
        if (text == null || text.isEmpty()) {
            return lines;
        }

        int last = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n') {
                if (last == i - 1) {
                    lines.add("");
                } else {
                    lines.add(text.substring(last + 1, i));
                }
                last = i;
            }
        }

        if (last == text.length() - 1) {
            lines.add("");
        } else {
            lines.add(text.substring(last + 1));
        }

        return lines;
    }

    public static List<String> parseLines(String text, String delim) {
        List<String> lines = new ArrayList();
        if (text == null || text.isEmpty()) {
            return lines;
        }

        StringTokenizer stringTokenizer = new StringTokenizer(text, delim);

        while (stringTokenizer.hasMoreTokens()) {
            String s = stringTokenizer.nextToken();
            s = s.trim();
            lines.add(s);
        }
        return lines;
    }
    
    private static boolean processFile(final File file, final SourceLicenseEditor.BigCommentParser parser) throws IOException {
//        if (file.getName().startsWith(SourceLicenseEditor.class.getSimpleName())){
//            return false;
//        }
        if (canIgnore(file)){
            return false;
        }
        
        final String charset = getCharsetName(file);
        String fileAsString = readStringFromFile(file, charset);

        

        final SourceLicenseEditor.OutAnilize out = new SourceLicenseEditor.OutAnilize();
        
        //System.out.println("\'" + file.getName());

        crashOldLicences(out, fileAsString, parser);

        if (!out.canAddCompassLicense && !out.changed) {
            //System.out.println("\'" + file.getAbsolutePath() + "\' ignored.");
            return false;
        }

        final String clearString = out.NewValue;

        final String newString;
        if (out.canAddCompassLicense) {
            newString = addLicences(clearString);
        } else {
            newString = clearString;
        }


        System.out.println("\'" + file.getAbsolutePath() + "\' changed. [" + charset + "]");
        writeStringToFile(file, newString, UTF8);
        

        return true;
    }
    
    private static boolean isEnglishWord(final String word){
        for (int i=0; i<word.length(); i++){
            char ch = word.charAt(i);
            if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')){
                
            }
            else{
                return false;
            }
        }
        return true;
    }
    
    
    //private static List<String> autors = new ArrayList();
    
    private static String removeAuthors(final String text){
        final List<String> lines = parseLines(text) ;
        final List<String> lines2 = new ArrayList(lines.size());
        boolean jastAutor = true;
        for (int j=0; j<lines.size(); j++){
            String line = lines.get(j);
            
            final String lineWithoutAsterisk = line.replace("*", " ").replace("/", " ").replace(",", " ").trim();
            final List<String> words = parseLines(lineWithoutAsterisk, " ");
            
            
            if (!words.isEmpty()){

                boolean contantAutor = false;
                for (String word : words){
                    if (AUTHOR_STRING.equals(word)){
                        contantAutor = true;
                    }
                }
                
                if (!contantAutor){
                    jastAutor = false;
                }
                
                boolean mustRemoveLine = false;
                if (contantAutor){
                    String lastAutor = null;
                    
                    for (int i=words.size()-1; i>=0; i--){
                        String word = words.get(i);
                        if (word!=null){
                            word = word.trim();
                        }
                        if (AUTHORS.contains(word)){
                           lastAutor = word;
                           break;
                        }
                    }
                        


                    final int pos = line.indexOf(lastAutor);
                    line = line.substring(pos + lastAutor.length());
                    //lines.set(j, line);
                    if (line.isEmpty()){
                        mustRemoveLine = true;
                    }
                }
                
                if (!mustRemoveLine){
                    lines2.add(line);
                }
            }
            else{
                lines2.add(line);
            }
        }
        
        if (jastAutor){
            return "";
        }
        return mergeLines(lines2, "\n");
    }
    
    
    
    public static String mergeLines(List<String> lines, String delim) {
        if (lines == null || lines.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;

        for (String line : lines) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(delim);
            }
            sb.append(line);
        }
        return sb.toString();
    }

    private static void crashOldLicences(final SourceLicenseEditor.OutAnilize out, final String fileAsString, final SourceLicenseEditor.BigCommentParser parser) {
        parser.process(fileAsString);

        final StringBuilder buffer = new StringBuilder(fileAsString.length());
        for (int i = 0; i < parser.getSize(); i++) {
            String lexBody = parser.get(i);

            boolean mastRemoveComment = false;
            if (parser.isComment(i)) {
                
                if (lexBody.contains(AUTHOR_STRING)){
                    lexBody = removeAuthors(lexBody);
                }

                if (MAIN_LICENSE_TEXT.trim().equals(lexBody)
                        || lexBody.contains(LICENSED) && !lexBody.contains(COMPASS_STRING)
                        || lexBody.contains(COPYRIGHT) && !lexBody.contains(COMPASS_STRING)) {
                    out.canAddCompassLicense = false;
                } else if (lexBody.contains(COMPASS_STRING)
                        //|| lexBody.contains(AUTHOR_STRING)
                        || lexBody.contains(TO_CHANGE_THIS_TEMPLATE)) {
                    mastRemoveComment = true;
                    out.changed = true;
                }
            }

            if (mastRemoveComment) {
                //do nothing
            } else {
                buffer.append(lexBody);
            }
        }

        out.NewValue = buffer.toString();
    }

    private static String addLicences(final String data) {
        final String separator;
        if (data.startsWith("\n\n")) {
            separator = "";
        } else if (data.startsWith("\n")) {
            separator = "\n";
        } else {
            separator = "\n\n";
        }
        return MAIN_LICENSE_TEXT + separator + data;
//        final List<String> lines = parseLines(data);
//        int index = -1;
//        for (int i=0; i<lines.size(); i++){
//            final String line = lines.get(i);
//            if (line!=null && line.trim().startsWith("package")){
//                index = i;
//                break;
//            }
//        }        
//        
//        if (index+1>=lines.size()){
//            lines.add("");
//        }
//        if (index+2>=lines.size()){
//            lines.add("");
//        }
//        if (index+3>=lines.size()){
//            lines.add("");
//        }
//        
//        if (!lines.get(index+1).trim().isEmpty()){
//            lines.add(index+1, "");
//        }
//        if (!lines.get(index+2).trim().isEmpty()){
//            lines.add(index+2, "");
//        }
//        
//        if (!lines.get(index+3).trim().isEmpty()){
//            lines.add(index+3, "");
//        }
//        
//        final StringBuilder builder = new StringBuilder(data.length() + MAIN_LICENSE_TEXT.length());
//        boolean first = true;
//        for (int i=0, n=index+3; i<n; i++){
//            if (first){
//                first = false;
//            }
//            else{
//                builder.append("\n");
//            }
//            builder.append(lines.get(i));
//        }
//        
//        builder.append(MAIN_LICENSE_TEXT);
//        
//        for (int i=index+3; i<lines.size(); i++){
//            if (first){
//                first = false;
//            }
//            else{
//                builder.append("\n");
//            }
//            builder.append(lines.get(i));
//        }        
//        return builder.toString();
    }

//    private static List<String> parseLines(final String text) {
//        List<String> lines = new LinkedList();
//        if (text == null || text.isEmpty()) {
//            return lines;
//        }
//
//        int last = -1;
//        for (int i = 0; i < text.length(); i++) {
//            if (text.charAt(i) == '\n') {
//                if (last == i - 1) {
//                    lines.add("");
//                } else {
//                    lines.add(text.substring(last + 1, i));
//                }
//                last = i;
//            }
//        }
//
//        if (last == text.length() - 1) {
//            lines.add("");
//        } else {
//            lines.add(text.substring(last + 1));
//        }
//
//        return lines;
//    }       
    private static class SourceLicenseEditorException extends RuntimeException {

        public SourceLicenseEditorException(final String message) {
            super(message);
        }
    }

    /**
     * Delete file.
     *
     * @return true if successfully, false otherwise.
     */
    private static boolean deleteFileImpl(final File file) {
        int counter = 0;
        while (counter < 10) {
            if (file.delete()) {
                return true;
            }
            if (!file.exists()) {
                return true;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
            counter++;

        }
        return false;
    }

    private static String readStringFromFile(final File file, final String charset) throws IOException {

        byte[] bytes = Files.readAllBytes(file.toPath());
        String rez = new String(bytes, charset);
        return rez;

    }
//
//        private static String readStringFromFile2(final File file, final String  charset) throws IOException {
//        final StringBuilder stringBuilder = new StringBuilder();
//        try (FileInputStream fileInputStream = new FileInputStream(file);
//                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, charset);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
//            String string;
//            boolean first = true;
//            while ((string = bufferedReader.readLine()) != null) {
//                if (first) {
//                    first = false;
//                } else {
//                    stringBuilder.append("\n");
//                }
//                stringBuilder.append(string);
//            }
//        }
//        
//        return stringBuilder.toString();
//  
// 
//    }

    static public void writeStringToFile(final File file, final String data, final String charset) throws IOException {
        Files.write(file.toPath(), data.getBytes(charset), new OpenOption[0]);
    }
//        static public void writeStringToFile2(final File file, final String data, final String charset) throws IOException {
//        
//         
//       
//        
//        OutputStream outputStream = null;
//        Writer streamWriter = null;
//        BufferedWriter writer = null;        
//        try {
//           outputStream = new FileOutputStream(file);
//           streamWriter = new OutputStreamWriter(outputStream, charset);
//            writer = new BufferedWriter(streamWriter);
//            writer.write(data);
//        } catch (IOException e) {
//        } finally {
//            try {
//                if (writer != null) {
//                    writer.close();
//                }
//            } catch (IOException e) {
//            }            
//            try {
//                if (streamWriter!=null){
//                 streamWriter.close();
//                }
//            } catch (IOException e) {
//            }
//            try {
//                if (outputStream!=null){
//                 outputStream.close();
//                }
//            } catch (IOException e) {
//            }    
//        }
//    }   

    private static class OutAnilize {

        String NewValue;
        boolean canAddCompassLicense = true;
        boolean changed = false;
    }

    private static class BigCommentParser {

        List<SourceLicenseEditor.BigCommentParser.BigCommentLexema> list = new ArrayList();

        private static class BigCommentLexema {

            public BigCommentLexema(final int pos, final int len, final boolean isComment) {
                this.pos = pos;
                this.len = len;
                this.isComment = isComment;
            }
            int pos;
            int len;
            boolean isComment;
        }

        private int getLastPos() {
            if (list.isEmpty()) {
                return 0;
            } else {
                SourceLicenseEditor.BigCommentParser.BigCommentLexema lex = list.get(list.size() - 1);
                return lex.pos + lex.len;
            }
        }

        boolean isComment(final int index) {
            return list.get(index).isComment;
        }

        int getSize() {
            return list.size();
        }

        private void add(final int pos, final int len, final boolean isText) {
            final SourceLicenseEditor.BigCommentParser.BigCommentLexema lex = new SourceLicenseEditor.BigCommentParser.BigCommentLexema(pos, len, isText);
            list.add(lex);
        }

        private String get(final int index) {
            final SourceLicenseEditor.BigCommentParser.BigCommentLexema lex = list.get(index);
            return data.substring(lex.pos, lex.pos + lex.len);
        }
        String data;

        private void process(final String data) {
            this.data = data;
            boolean isText = true;
            boolean isString = false;
            boolean isSmallComment = false;
            list.clear();
            char priorChar = ' ';
            int n = data.length();
            for (int i = 0; i < n; i++) {
                char thisChar = data.charAt(i);

                if (isText) {
                    if (!isSmallComment && thisChar == '"') {
                        if (isString && priorChar == '\\') {
                            //do nothing
                        } else {
                            isString = !isString;
                        }

                    }
                    if (thisChar == '\n') {
                        isString = false;
                        isSmallComment = false;
                    }
                    if (!isString && thisChar == '/' && priorChar == '/') {
                        isSmallComment = true;
                    }

                    if (!isSmallComment && !isString && thisChar == '*' && priorChar == '/') {
                        int pos = getLastPos();
                        if (pos != i - 1) {
                            add(pos, i - pos - 1, !isText);
                        }
                        isText = false;
                        priorChar = ' ';
                        continue;
                    }
                } else {
                    if (thisChar == '/' && priorChar == '*') {
                        int pos = getLastPos();
                        if (pos != i + 1) {
                            add(pos, i - pos + 1, !isText);
                        }
                        isText = true;
                        isString = false;
                        priorChar = ' ';
                        continue;
                    }
                }
                priorChar = thisChar;
            }

            int pos = getLastPos();
            add(pos, n - pos, !isText);
        }
    }
}