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

package org.radixware.kernel.common.client.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.Arrays;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.types.ArrRef;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.utils.Base64;


public class CsvWriter {
    
    public static String UTF_8_WITH_BOM = "UTF-8 with BOM character";
    private static char BOM = '\ufeff';
    
    public static class FormatOptions{

        private final String encoding;
        
        public FormatOptions(final String csn){
            encoding = csn==null || csn.isEmpty() ? Charset.defaultCharset().name() : csn;
        }
        
        public String getEncoding() {
            return encoding;
        }
    }
    
    private final static Character SEP = Character.valueOf('\u002c');
    private final static Character DQUOTE = Character.valueOf('\u0022');
    private final static String EOR = "\r\n";
    
    private final static char[] SAFE_CHARS = new char[93];
    {
        SAFE_CHARS[0]='\u0020';
        SAFE_CHARS[1]='\u0021';
        int i = 2;
        for (char c='\u0023'; c<='\u002b'; c++){
            SAFE_CHARS[i]=c;
            i++;
        }
        for (char c='\u002d'; c<='\u007e'; c++){
            SAFE_CHARS[i]=c;
            i++;
        }
    }
    
    private final PrintWriter writer;
    private final int fieldsCount;
    private int fieldIndex=1;
    private boolean needToWriteBom;
    
    public CsvWriter(final File file, final FormatOptions formatOptions, final int fieldsCount) throws FileNotFoundException, UnsupportedEncodingException{
        final String encoding = formatOptions.getEncoding();
        if (UTF_8_WITH_BOM.equals(encoding)){
            writer = new PrintWriter(file, "UTF-8");
            needToWriteBom = true;
        }else{
            writer = new PrintWriter(file, encoding);
        }
        
        this.fieldsCount = fieldsCount;
    }
        
    private void beforeWriteFieldText(){
        if (fieldIndex>1){
            write(SEP);
        }
    }
    
    private void afterWriteFieldText(){        
        if (fieldIndex==fieldsCount){
            write(EOR);
            fieldIndex=1;
        }else{
            fieldIndex++;
        }
    }
    
    public void writeSafeStringField(final String text){
        beforeWriteFieldText();
        write(text);            
        afterWriteFieldText();
    }
    
    public void writeEmptyField(){
        beforeWriteFieldText();
        afterWriteFieldText();
    }
    
    public void writeUnsafeStringField(final String text){
        beforeWriteFieldText();
        write(DQUOTE);
        for (int i=0; i<text.length(); i++){
            final char curChar = text.charAt(i);
            if (curChar==DQUOTE.charValue()){
                write(DQUOTE);
            }
            write(curChar);
        }
        write(DQUOTE);
        afterWriteFieldText();
    }
    
    public void writeStringField(final String text){
        if (isSafeText(text)){
            writeSafeStringField(text);
        }else{
            writeUnsafeStringField(text);
        }
    }
    
    public void writeValue(final Object value, final EValType type){
        if (value==null){
            writeEmptyField();
        }else{
            switch(type){
                case INT:
                case NUM:                
                case BOOL:
                case ARR_INT:
                case ARR_NUM:
                case ARR_DATE_TIME:
                case ARR_BOOL:
                case ARR_BIN:
                    writeSafeStringField(String.valueOf(value));
                    break;
                case DATE_TIME:
                    writeSafeStringField(String.valueOf(((Timestamp)value).getTime()));
                    break;
                case BIN:
                    writeSafeStringField(ValueConverter.arrByte2Str(((Bin)value).get(), ""));
                    break;
                case PARENT_REF:
                case OBJECT:{
                    final Pid pid;
                    if (value instanceof Reference){
                        pid = ((Reference)value).getPid();
                    }else if (value instanceof Pid){
                        pid = (Pid)value;                        
                    }else if (value instanceof EntityModel){
                        pid = ((EntityModel)value).getPid();
                    }else{
                        throw new IllegalArgumentException("Unsupported type of parent reference value: "+value.getClass().getName());
                    }
                    if (pid==null){
                        writeEmptyField();
                    }else{
                        writeStringField(pid.toString());
                    }
                    break;
                }
                case ARR_REF:{
                    final ArrRef arrRef = (ArrRef)value;
                    final ArrStr arrStr = new ArrStr(arrRef.size());
                    for (Reference reference: arrRef){
                        if (reference==null || reference.getPid()==null){
                            arrStr.add(null);
                        }else{
                            arrStr.add(reference.getPid().toString());
                        }
                    }
                    writeStringField(arrStr.toString());
                    break;
                }
                case XML:{
                    final byte[] asBytes;
                    try{
                        asBytes = ((XmlObject)value).xmlText().getBytes("UTF-8");
                    }catch(UnsupportedEncodingException exception){
                        throw new WrongFormatError("Failed to write xml value",exception);
                    }
                    writeSafeStringField(Base64.encode(asBytes));
                    break;
                }
                case STR:
                case CHAR:
                case ARR_STR:
                case ARR_CHAR:
                    writeStringField(String.valueOf(value));
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported value type "+type.getName());
            }
        }
    }
    
    private static boolean isSafeText(final String text){
        for (int i=0; i<text.length(); i++){
            if (Arrays.binarySearch(SAFE_CHARS, text.charAt(i))<0){
                return false;
            }
        }
        return true;
    }
    
    public boolean flush(){
        return !writer.checkError();
    }
    
    public void close(){
        writer.close();
    }
    
    private void write(final char c){
        if (needToWriteBom){
            writer.append(BOM);
            needToWriteBom = false;
        }            
        writer.append(c);
    }
    
    private void write(final CharSequence string){
        if (needToWriteBom){
            writer.append(BOM);
            needToWriteBom = false;
        }            
        writer.append(string);
    }
    
}