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

package org.radixware.kernel.common.client.eas.connections;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.SystemTools;

public final class Pkcs11Config implements Cloneable{
    
    private final static String DELIMITER = "#advanced";
    private final static String DEFAULT_NAME = "HSM";
    private final static String DEFAULT_LIB_WIN = "/system32/eTPKCS11.dll";
    private final static String DEFAULT_LIB_NIX = "/usr/lib/opensc-pkcs11.so";
    private final static String DEFAULT_SLOTINDEX = "0";
    private final static String NEW_LINE = System.getProperty("line.separator");
        
    public enum Field {                
        NAME("name", true, DEFAULT_NAME),
        LIBPATH("library", true, getDefaultLibPath()),        
        SLOTLI("slotListIndex",true, DEFAULT_SLOTINDEX),
        DESCRIPTION("description"),
        ENABLED_MECH("enabledMechanisms"),
        DISABLED_MECH("disabledMechanisms"),
        ADVANCED("", false,getDefaultAdvancedOptions()),
        UNDEFINED("", false);
        
        public static final EnumSet<Field> STRING_FIELDS = EnumSet.of(NAME,LIBPATH,DESCRIPTION);
        
        public final String property;
        public final boolean isRequired;
        public final String defaultValue;
        
        Field(final String property) {
            this.property = property;
            isRequired = false;
            defaultValue = null;
        }

        private Field(final String property, final boolean isRequired) {
            this.property = property;
            this.isRequired = isRequired;
            defaultValue = null;
        }
        
        private Field(final String property, final boolean isRequired, final String defaultValue) {
            this.property = property;
            this.isRequired = isRequired;
            this.defaultValue = defaultValue;
        }
        
        private static String getDefaultLibPath(){
            if (SystemTools.isWindows) {
                return System.getenv("windir") + DEFAULT_LIB_WIN;
            } else {
                return DEFAULT_LIB_NIX;
            }                        
        }
        
        private static String getDefaultAdvancedOptions(){
        final StringBuilder sb = new StringBuilder(256);
        sb.append("attributes (*, CKO_PRIVATE_KEY, CKK_RSA) = {");
        sb.append(NEW_LINE);
        sb.append("  CKA_SIGN = true");
        sb.append(NEW_LINE);
        sb.append("  CKA_DECRYPT = true");
        sb.append(NEW_LINE);
        sb.append('}');
        sb.append(NEW_LINE);
        sb.append("attributes (*, CKO_PUBLIC_KEY, CKK_RSA) = {");
        sb.append(NEW_LINE);
        sb.append("  CKA_VERIFY = true");
        sb.append(NEW_LINE);
        sb.append("  CKA_ENCRYPT = true");
        sb.append(NEW_LINE);
        sb.append('}');
        sb.append(NEW_LINE);
        return sb.toString();            
        }
    }
    
    private final IClientEnvironment environment;    
    private final Map<Field,String> content = new HashMap<>();

    public Pkcs11Config(final IClientEnvironment environment){
        this.environment = environment;        
        for (Field field: EnumSet.allOf(Field.class)){
            if (field.defaultValue!=null){
                content.put(field, field.defaultValue);
            }
        }
    }
    
    public Pkcs11Config(final Pkcs11Config copy){
        environment = copy.environment;
        content.putAll(copy.content);
    }
    
    public String getFieldValue(final Field field){
        return content.get(field);
    }
    
    public void setFieldValue(final Field field, final String value){
        if (field==Field.SLOTLI){
            Integer.parseInt(value);
        }
        content.put(field, value);
    }
    
    public void readFromFile(final String fileName) throws IOException{
        final File file = new File(fileName);
        if (file.exists() && file.isFile()){
            final String fileContent = FileUtils.readTextFile(file, Charset.defaultCharset().name());
            if (!fileContent.isEmpty()){
                for (Field field: Field.STRING_FIELDS){
                    final String value = parseSimpleOption(field, fileContent);
                    if (value!=null){
                        content.put(field, value);
                    }
                }
                final String slotIndexAsStr = parseSimpleOption(Field.SLOTLI, fileContent);
                if (slotIndexAsStr!=null){
                    try{
                        Integer.parseInt(slotIndexAsStr);
                        content.put(Field.SLOTLI, slotIndexAsStr);
                    }catch(NumberFormatException exception){
                        final String message = 
                            environment.getMessageProvider().translate("PKCS11", "Failed to read \'%1$s\' option from \'%2$s\' file");
                        environment.getTracer().debug(String.format(message, Field.SLOTLI.property, file.getAbsolutePath()));
                    }                 
                }
                final String disabledMechs = parseMechanismsOption(Field.DISABLED_MECH, fileContent);
                if (disabledMechs==null){
                    final String enabledMechs = parseMechanismsOption(Field.ENABLED_MECH, fileContent);
                    if (enabledMechs!=null){
                        content.put(Field.ENABLED_MECH, enabledMechs);
                    }
                }else{
                    content.put(Field.DISABLED_MECH, disabledMechs);
                }
                final String advancedOptions = parseAdvancedOptions(fileContent);
                if (advancedOptions!=null){
                    content.put(Field.ADVANCED, advancedOptions);
                }
            }
        }
    }
    
    public void readFromSslOptions(final ConnectionOptions.SslOptions sslOptions){
        final String libPath = sslOptions.getPkcs11Lib();
        if (libPath!=null && !libPath.isEmpty()){
            setFieldValue(Field.LIBPATH, libPath);
        }
        final int slotIndex = sslOptions.getSlotId();
        if (slotIndex>=0){
            setFieldValue(Field.SLOTLI, String.valueOf(slotIndex));
        }
    }
    
    public void writeToFile(final String fileName) throws IOException{
        final File file = new File(fileName);
        if (file.exists()){
            FileUtils.deleteFile(file);
        }
        final StringBuilder fileContent = new StringBuilder(64);
        for (Field f: EnumSet.of(Field.NAME, Field.LIBPATH, Field.SLOTLI, Field.DESCRIPTION)){
            final String value = getFieldValue(f);
            if (value!=null){
                if (fileContent.length()>0){
                    fileContent.append(NEW_LINE);
                }
                fileContent.append(f.property);
                fileContent.append('=');
                fileContent.append(value);
            }
        }
        writeMechanisms(Field.DISABLED_MECH, getFieldValue(Field.DISABLED_MECH), fileContent);
        writeMechanisms(Field.ENABLED_MECH, getFieldValue(Field.ENABLED_MECH), fileContent);
        final String advancedOptions = getFieldValue(Field.ADVANCED);
        if (advancedOptions!=null && !advancedOptions.isEmpty()){
            if (fileContent.length()>0){
                fileContent.append(NEW_LINE);
            }            
            fileContent.append(DELIMITER);
            fileContent.append(NEW_LINE);
            fileContent.append(advancedOptions);
        }
        
        FileUtils.writeString(file, fileContent.toString(), Charset.defaultCharset().name());        
    }
    
    public void writeToSslOptions(final ConnectionOptions.SslOptions sslOptions){
        sslOptions.setPkcs11Lib(getFieldValue(Field.LIBPATH));
        sslOptions.setSlotId(Integer.parseInt(getFieldValue(Field.SLOTLI)));
    }

    @Override
    public String toString(){
        final StringBuilder contentBuilder = new StringBuilder(64);
        for (Field f: EnumSet.allOf(Field.class)){
            final String value = getFieldValue(f);          
            if (value!=null && !value.isEmpty()){
                if (contentBuilder.length()>0){
                    contentBuilder.append('\n');
                }
                if (!f.property.isEmpty()){
                    contentBuilder.append(f.property);
                    contentBuilder.append('=');
                }
                contentBuilder.append(value);
            }
        }
        return contentBuilder.toString();
    }
    
    @Override
    public Pkcs11Config clone(){
        return new Pkcs11Config(this);
    }
    
    private static String parseSimpleOption(final Field field, final String config){
        final String key = field.property;
        if(config.contains(key)) {
            int startIndex = config.indexOf(key) + key.length();

            while(isSpecialCharacter(config.charAt(startIndex)) && startIndex < config.length()) {
                startIndex++;
            }

            int endIndex = config.indexOf(NEW_LINE, startIndex);
            if(endIndex == -1) {
                endIndex = config.length();
            }
            final String value = config.substring(startIndex, endIndex).trim();
            return value.isEmpty() ? null : value;
        }
        return null;
    }
    
    private static String parseMechanismsOption(final Field field, final String config){
        final String key = field.property;
        if(config.contains(key)) {
            int startIndex = config.indexOf(key) + key.length();
            while(isSpecialCharacter(config.charAt(startIndex)) && startIndex < config.length()) {
                startIndex++;
            }
            final int endIndex = config.indexOf('}', startIndex);
            final String mechs = config.substring(startIndex, endIndex).trim().replaceAll("\\s+", ", ");
            return mechs.isEmpty() ? null : mechs;
        }else{
            return null;
        }
    }
    
    private static String parseAdvancedOptions(final String config){
        if(config.contains(DELIMITER)) {
            final int start = config.indexOf(DELIMITER) + DELIMITER.length() + NEW_LINE.length();
            if (start<config.length()){
                final String options = config.substring(start, config.length()).trim();
                return options.isEmpty() ? null : options;
            }
        }
        return null;
    }
    
    private static boolean writeMechanisms(final Field field, final String mechanisms, final StringBuilder dest){
        if (mechanisms==null || mechanisms.trim().isEmpty()){
            return false;
        }else{
            final String arrMechanisms[] = mechanisms.replaceAll("\\s", "").split(",");
            if (dest.length()>0){
                dest.append(NEW_LINE);
            }            
            dest.append(field.property);
            dest.append("={");
            for(String mechanism : arrMechanisms) {
                dest.append(NEW_LINE);
                dest.append('\t');
                dest.append(mechanism);
            }
            dest.append(NEW_LINE);
            dest.append('}');
            return true;
        }
    }
    
    private static boolean isSpecialCharacter(char ch) {
        return ch == ' ' || ch == '=' || ch=='{';
    }    
    
    public static void changeSlotIndex(final IClientEnvironment environment, final Id connectionId, final int newSlotIndex) throws IOException{
        final String workPath = environment.getWorkPath();
        changeSlotIndex(environment, workPath+"/"+connectionId.toString()+".pkcs11", newSlotIndex);
    }
    
    public static void changeSlotIndex(final IClientEnvironment environment, final String configFilePath, final int newSlotIndex) throws IOException{
        final Pkcs11Config pkcs11Config = new Pkcs11Config(environment);
        pkcs11Config.readFromFile(configFilePath);
        pkcs11Config.setFieldValue(Field.SLOTLI, String.valueOf(newSlotIndex));
        pkcs11Config.writeToFile(configFilePath);
    }
}
