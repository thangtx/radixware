/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.eas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.CompositeInetSocketAddress;
import org.radixware.kernel.common.utils.ValueFormatter;


public final class AddressTranslationTable {
    private AddressTranslationTable() {
    }
    private final static AddressTranslationTable EMPTY = new AddressTranslationTable();
    
        public final static class TranslationTable{
            private TranslationTable() {
            }
        private final Map<InetSocketAddress, InetSocketAddress> table = new HashMap<>();
        
        public InetSocketAddress addRecord(final InetSocketAddress translateFrom, final InetSocketAddress translateTo){
            return table.put(translateFrom, translateTo);
        }
        
        public CompositeInetSocketAddress translate(final CompositeInetSocketAddress translateFrom){
            final InetSocketAddress replacedAddress = translateFrom.getRemoteAddress();
            if (replacedAddress==null){
                return translateFrom;
            }
            if (table.containsKey(replacedAddress)){
                final InetSocketAddress replacement = table.get(replacedAddress);
                return replacement==null ? null : new CompositeInetSocketAddress(replacement, translateFrom.getLocalAddress());
            }else{
                return translateFrom;
            }
        }
        
        public void printRecords(final String scpName, final StringBuilder builder){
            for (Map.Entry<InetSocketAddress,InetSocketAddress> record: table.entrySet()){
                if (builder.length()>0){
                    builder.append('\n');
                }                
                builder.append(scpName==null || scpName.isEmpty() ? "<default>" : scpName);
                builder.append(":\t\t");
                builder.append(record.getKey().toString());                
                builder.append(" -> ");
                builder.append(String.valueOf(record.getValue()));
            }
        }
        
        public Iterator<Entry<InetSocketAddress, InetSocketAddress>> getIterator() {
            return table.entrySet().iterator();
        }
    }

    private final Map<String, TranslationTable> translationTableByScp = new HashMap<>();
    
    public Iterator<Entry<String, TranslationTable>> getIterator() {
        return translationTableByScp.entrySet().iterator();
    }
    private TranslationTable getTranslationTableForScp(final String scpName){
        TranslationTable table = translationTableByScp.get(scpName);
        if (table==null){
            table = new TranslationTable();
            translationTableByScp.put(scpName, table);
        }
        return table;
    }
    
    private void addRecord(final String scpName, final InetSocketAddress translateFrom, final InetSocketAddress translateTo){
        getTranslationTableForScp(scpName).addRecord(translateFrom, translateTo);
    }
    
    public CompositeInetSocketAddress translate(final String scpName, final CompositeInetSocketAddress translateFrom){
        final TranslationTable table = translationTableByScp.get(scpName);
        return table==null ? translateFrom : table.translate(translateFrom);
    }
    
    public boolean isEmpty(){
        return translationTableByScp.isEmpty();
    }
    
    @Override
    public String toString(){
        final StringBuilder builder = new StringBuilder();
        for (Map.Entry<String,TranslationTable> table: translationTableByScp.entrySet()){
            table.getValue().printRecords(table.getKey(), builder);
        }
        return builder.toString();
    }
    
    public static AddressTranslationTable parseFile(final String fileName, final LocalTracer tracer, final MessageProvider messageProvider){
        if (fileName==null || fileName.isEmpty()){
            return EMPTY;
        }
        final File translationFile = new File(fileName);
        if (!translationFile.exists()){
            final String messageTemplate = messageProvider.translate("EASClient","Address translation table file '%1$s' does not exist");
            final String traceMessage = String.format(messageTemplate, fileName);
            tracer.put(EEventSeverity.WARNING, traceMessage, null, null, false);
            return EMPTY;
        }
        if (!translationFile.isFile()){
            final String messageTemplate = messageProvider.translate("EASClient","Failed to read address translation table: '%1$s' is not a file");
            final String traceMessage = String.format(messageTemplate, fileName);
            tracer.put(EEventSeverity.WARNING, traceMessage, null, null, false);            
            return EMPTY;
        }
                
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(translationFile), "UTF-8"))) {
            final AddressTranslationTable result = new AddressTranslationTable();
            int lineNumber=1;
            final StringBuilder parsingProblems = new StringBuilder();            
            CompositeInetSocketAddress translateFrom, translateTo;            
            for(String line; (line = br.readLine()) != null; lineNumber++) {
                if (!line.isEmpty()){
                    parseLine(line, lineNumber, result, messageProvider, parsingProblems);                    
                }
            }
            if (parsingProblems.length()>0){
                final String messageTemplate = messageProvider.translate("EASClient","Some records in address translation table file '%1$s' does not valid:\n%2$s");
                final String traceMessage = String.format(messageTemplate, fileName, parsingProblems.toString());
                tracer.put(EEventSeverity.WARNING, traceMessage, null, null, false); 
            }
            return result;
        }catch(FileNotFoundException exception){
            final String messageTemplate = messageProvider.translate("EASClient","Address translation table file '%1$s' does not exist");
            final String traceMessage =  String.format(messageTemplate, fileName);
            tracer.put(EEventSeverity.WARNING, traceMessage, null, null, false);
            return EMPTY;            
        }catch(IOException exception){
            final String messageTemplate = messageProvider.translate("EASClient", "Failed to read address translation table file '%1$s': %2$s");
            final String traceMessage = String.format(messageTemplate, fileName, exception.getMessage());
            tracer.put(EEventSeverity.WARNING, traceMessage, null, null, false);
            return EMPTY;            
        }
    }
    
    private static void parseLine(final String line, final int lineNumber, final AddressTranslationTable table,  final MessageProvider messageProvider, final StringBuilder parsingProblems){
        final String scpName;
        final CompositeInetSocketAddress translateFrom, translateTo;
        final String fields[] = line.split(",");
        if (fields.length<2){
            appendParsingProblem(getWrongRecordFormatMessage(line, lineNumber, messageProvider), parsingProblems);
            return;
        }
        
        int fieldIndex = fields.length-1;
        if (fieldIndex>1){
            try{
                translateTo = parseAddress(fields[fieldIndex].trim());
            }catch(IllegalArgumentException exception){
                final String problem = 
                    processParseAddressException(exception.getMessage(), line, fields[fieldIndex], lineNumber, messageProvider);
                appendParsingProblem(problem, parsingProblems);
                return;
            }        
            fieldIndex--;
        }else{
            translateTo  = null;
        }
        try{
            translateFrom = parseAddress(fields[fieldIndex].trim());
        }catch(IllegalArgumentException exception){
            final String problem = 
                processParseAddressException(exception.getMessage(), line, fields[fieldIndex].trim(), lineNumber, messageProvider);
            appendParsingProblem(problem, parsingProblems);            
            return;
        }
        fieldIndex--; 
        if (fieldIndex==0){
            scpName = fields[fieldIndex].isEmpty() ? null : fields[fieldIndex];
        }else{
            StringBuilder scpNameBuilder = new StringBuilder();
            for (int i=0; i<=fieldIndex; i++){
                if (i>0){
                    scpNameBuilder.append(',');
                }
                scpNameBuilder.append(fields[i]);                
            }
            scpName = scpNameBuilder.toString();
        }
        table.getTranslationTableForScp(scpName).addRecord(translateFrom.getRemoteAddress(), translateTo==null ? null : translateTo.getRemoteAddress());
    }
    
    private static void appendParsingProblem(final String problem, final StringBuilder parsingProblems){
        if (parsingProblems.length()>0){
            parsingProblems.append('\n');
        }            
        parsingProblems.append(problem);        
    }
    
    private static CompositeInetSocketAddress parseAddress(final String address){
        return address.isEmpty() ? null : ValueFormatter.parseCompositeInetSocketAddress(address);
    }
    
    private static String processParseAddressException(final String message, final String line, final String address, final int lineNumber, final MessageProvider messageProvider){
        final String messageTemplate = messageProvider.translate("EASClient","The record '%1$s' at line %2$s has wrong address '%3$s': %4$s. This record will be ignored");
        return  String.format(messageTemplate, line, lineNumber, address, message);
    }
    
    private static String getWrongRecordFormatMessage(final String line, final int lineNumber, final MessageProvider messageProvider){
        final String messageTemplate = messageProvider.translate("EASClient", "The record '%1$s' at line %2$s has wrong format and will be ignored. Valid format is '[scp name],<address from manifest>,[address to use]'");
        return String.format(messageTemplate, line, lineNumber);
    }
}
