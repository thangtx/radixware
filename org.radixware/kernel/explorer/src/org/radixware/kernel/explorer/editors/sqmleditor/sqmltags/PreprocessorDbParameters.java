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

package org.radixware.kernel.explorer.editors.sqmleditor.sqmltags;

import com.trolltech.qt.gui.QFileDialog;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.enums.EComparisonOperator;
import org.radixware.kernel.common.enums.EOptionMode;
import org.radixware.schemas.xscml.Sqml;


public final class PreprocessorDbParameters {
    
    public static final class Factory{
        
        private Factory(){            
        }
        
        public static PreprocessorDbParameters newInstance(){
            return new PreprocessorDbParameters();
        }
        
        public static PreprocessorDbParameters parse(final Sqml.Item.TargetDbPreprocessor xml){
            final PreprocessorDbParameters parameters = new PreprocessorDbParameters();
            parameters.dbType = xml.getDbTypeName();
            if (xml.getCheckVersion()!=null && xml.getCheckVersion() && xml.isSetDbVersion() && xml.isSetVersionOperator()){
                parameters.dbVersion = xml.getDbVersion();
                parameters.checkVersionOperator = xml.getVersionOperator();                
            }
            if (xml.getCheckOptions()!=null && xml.getCheckOptions()){
                parameters.options = new LinkedHashMap<>();
                final List<Sqml.Item.TargetDbPreprocessor.Option> options = xml.getOptionList();
                if (options!=null){
                    for (Sqml.Item.TargetDbPreprocessor.Option option: options){
                        parameters.options.put(option.getName(), option.getValue()==EOptionMode.ENABLED);
                    }
                }
            }
            return parameters;
        };                
    }
        
    private String dbType;
    private String dbVersion;
    private EComparisonOperator checkVersionOperator;
    private Map<String,Boolean> options;
    
    private PreprocessorDbParameters(){        
    }
        
    public String getDbType() {
        return dbType;
    }

    public void setDbType(final String dbType) {
        this.dbType = dbType;
    }
    
    public void setCheckDbVersion(final String version, final EComparisonOperator operator){
        dbVersion = version;
        checkVersionOperator = operator;
    }

    public String getDbVersion() {
        return dbVersion;
    }

    public EComparisonOperator getCheckVersionOperator() {
        return checkVersionOperator;
    }
    
    public void addCheckOption(final String optionName, final boolean isEnabled){
        if (options==null){
            options = new LinkedHashMap<>();
        }
        options.put(optionName, isEnabled);
    }
    
    public boolean removeCheckOption(final String optionName){
        if (options!=null){
            return options.remove(optionName);
        }
        return false;
    }
    
    public void clearCheckOptions(){
        options = null;
    }
    
    public boolean containsOptionCheck(final String optionName){
        return options!=null && options.containsKey(optionName);
    }
    
    public boolean getCheckOption(final String optionName){
        if (options==null || !options.containsKey(optionName)){
            throw new IllegalArgumentException("Option \'"+optionName+"\' does not checks");
        }
        return options.get(optionName);
    }
    
    public List<String> getCheckOptionNames(){
        if (options==null){
            return Collections.emptyList();
        }
        final List<String> result = new LinkedList<>();
        for (Map.Entry<String,Boolean> entry: options.entrySet()){
            result.add(entry.getKey());
        }
        return result;
    }
    
    public void writeToXml(final Sqml.Item.TargetDbPreprocessor xml){
        if (dbType!=null){
            xml.setDbTypeName(dbType);
        }
        if (dbVersion==null || checkVersionOperator==null){
            xml.setCheckVersion(false);
        }else{
            xml.setCheckVersion(true);
            xml.setVersionOperator(checkVersionOperator);            
        }
        if (options==null){
            xml.setCheckOptions(false);
        }else{
            xml.setCheckOptions(true);
            for (Map.Entry<String,Boolean> entry: options.entrySet()){
               final Sqml.Item.TargetDbPreprocessor.Option option = xml.addNewOption();
               option.setName(entry.getKey());
               option.setValue(Boolean.TRUE==entry.getValue() ? EOptionMode.ENABLED : EOptionMode.DISABLED);
            }
        }
    }    
    
    @Override
    public String toString(){
        final StringBuilder builder = new StringBuilder();
        boolean firstCondition = true;
        if (dbType!=null){
            builder.append("DB_TYPE == \"");
            builder.append(dbType);
            builder.append("\"");
            firstCondition = false;
        }
        if (dbVersion!=null && checkVersionOperator!=null){
            if (!firstCondition){
                builder.append(" AND ");
            }
            builder.append("DB_VERSION ");
            builder.append(checkVersionOperator.getValue());
            builder.append(' ');
            builder.append(dbVersion);
            firstCondition = false;
        }
        if (options!=null){
            for (Map.Entry<String,Boolean> entry: options.entrySet()){
                if (!firstCondition){
                    builder.append(" AND ");
                }
                if (Boolean.TRUE!=entry.getValue()){
                    builder.append('!');
                }
                builder.append("isEnabled(\"");
                builder.append(entry.getKey());
                builder.append("\")");
                firstCondition = false;
            }
        }
        return builder.toString();
    }
    
    public PreprocessorDbParameters copy(){
        final PreprocessorDbParameters parameters = new PreprocessorDbParameters();
        parameters.dbType = dbType;
        parameters.dbVersion = dbVersion;
        parameters.checkVersionOperator = checkVersionOperator;
        if (options!=null){
            parameters.options = new LinkedHashMap<>(options);
        }
        return parameters;
    }
}
