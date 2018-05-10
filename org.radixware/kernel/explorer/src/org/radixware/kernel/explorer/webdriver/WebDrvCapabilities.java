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

package org.radixware.kernel.explorer.webdriver;

import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;
import org.radixware.kernel.explorer.webdriver.exceptions.InvalidPropertyValueTypeException;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import java.util.LinkedList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.webdriver.validators.JsonValidator;

public final class WebDrvCapabilities {
    
    public static enum EPromptAction{ ACCEPT, DISMISS };
    
    public static final class Timeouts {

        private final JSONObject json;

        private Timeouts(final JSONObject timeouts){
            json = (JSONObject)timeouts.clone();
        }

        public static Timeouts parse(final JSONObject json) throws WebDrvException{
            JsonValidator.getTimeoutdValidator().validate(json);
            return new Timeouts(json);
        }

        @SuppressWarnings("unchecked")
        void init(){
            if (json.get("implicit") instanceof Long==false){
                json.put("implicit", Long.valueOf(0));
            }
            if (json.get("pageLoad") instanceof Long==false){
                json.put("pageLoad", Long.valueOf(300000));
            }
            if (json.get("script") instanceof Long==false){
                json.put("script", Long.valueOf(30000));
            }                     
        }
        
        @SuppressWarnings("unchecked")
        public void writeToJson(final JSONObject writeTo){
            for (Object key: json.keySet()){
                writeTo.put(key, json.get(key));
            }
        }        
        
        public JSONObject getAsJsonObject(){
            return (JSONObject)json.clone();
        }
        
        public long getImplicit(){
            return getTimeout("implicit", 0);
        }
        
        public long getPageLoad(){
            return getTimeout("pageLoad", 300000);
        }        
        
        private long getTimeout(final String name, final long defaultValue){
            final Object timeout = json.get(name);
            if (timeout instanceof Long==false){
                return defaultValue;
            }else{
                return (Long)timeout;
            }            
        }        
    }   

    private final JSONObject caps;
    
    private WebDrvCapabilities(final JSONObject caps){
        this.caps = caps;
    }
    
    private static void validateCaps(final JSONObject caps) throws WebDrvException{
        JsonValidator.getCapabilitiesValidator().validate(caps);
        final Object proxy = caps.get("proxy");
        if (proxy instanceof JSONObject){
            JsonValidator.getProxyConfigurationValidator().validate((JSONObject)proxy);
        }else if (proxy!=null){
            throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "proxy property value has invalid type: "+proxy.getClass().getName());
        }
        final Object timeouts= caps.get("timeouts");
        if (timeouts instanceof JSONObject){
            Timeouts.parse((JSONObject)timeouts);
        }else if (timeouts!=null){
            throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "timeouts property value has invalid type: "+timeouts.getClass().getName());
        }        
    }
    
    @SuppressWarnings("unchecked")
    private WebDrvCapabilities merge(final WebDrvCapabilities other) throws WebDrvException{
        final WebDrvCapabilities result = new WebDrvCapabilities(new JSONObject());
        for (Object key: caps.keySet()){
            result.caps.put(key, caps.get(key));
        }
        for (Object key: other.caps.keySet()){
            if (caps.get(key)==null){
                result.caps.put(key, other.caps.get(key));
            }else{
                throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "failed to merge \'"+String.valueOf(key)+"\' capability property");
            }
        }
        return result;
    }
    
    private boolean match(){        
        if (!matchStringProperty("browserName", "org.radixware.kernel.explorer")){
            return false;
        }
        final String rxVersion = Application.getVersionInfo().get("org.radixware");
        if (rxVersion!=null && !rxVersion.isEmpty() && !matchStringProperty("browserVersion", rxVersion)){
            return false;
        }
        final String platformName;
        if (SystemTools.isWindows){
            platformName = "windows";
        }else if (SystemTools.isOSX){
            platformName = "mac";
        }else{
            platformName = "linux";
        }
        if (!matchStringProperty("platformName", platformName)){
            return false;
        }
        final JSONObject proxy = (JSONObject)caps.get("proxy");
        if (proxy!=null 
            && !"noproxy".equalsIgnoreCase((String)proxy.get("proxyType"))
            && !"direct".equalsIgnoreCase((String)proxy.get("proxyType"))){
            return false;
        }
        return true;
    }
    
    private boolean matchStringProperty(final String name, final String value){
        return caps.get(name)==null || value.equals(caps.get(name));
    }
    
    public static WebDrvCapabilities parse(final JSONObject params) throws WebDrvException{
        final Object capsObj = params.get("capabilities");
        if (capsObj==null){
            throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "Capabilities was not defined");
        }
        if (capsObj instanceof JSONObject==false){
            throw new InvalidPropertyValueTypeException("capabilities", capsObj);
        }
        final JSONObject caps = (JSONObject)capsObj;
        final Object alwaysMatchObj = caps.get("alwaysMatch");
        final WebDrvCapabilities alwaysMatch;
        if (alwaysMatchObj==null){
            alwaysMatch = new WebDrvCapabilities(new JSONObject());
        }else if (alwaysMatchObj instanceof JSONObject){
            validateCaps((JSONObject)alwaysMatchObj);
            alwaysMatch = new WebDrvCapabilities((JSONObject)alwaysMatchObj);
        }else{
            throw new InvalidPropertyValueTypeException("alwaysMatch", alwaysMatchObj);
        }        
        final Object firstMatchObj = caps.get("firstMatch");
        if (firstMatchObj!=null){
            if (firstMatchObj instanceof JSONArray==false){
                throw new InvalidPropertyValueTypeException("firstMatch", firstMatchObj);
            }
            final List<WebDrvCapabilities> firstMatchCaps = new LinkedList<>();            
            for (Object item: ((JSONArray)firstMatchObj)){
                if (item!=null){
                    if (item instanceof JSONObject==false){
                        throw new InvalidPropertyValueTypeException("firstMatch item", firstMatchObj);
                    }
                    validateCaps((JSONObject)item);
                    firstMatchCaps.add(new WebDrvCapabilities((JSONObject)item));
                }
            }
            if (firstMatchCaps.isEmpty()){
                return alwaysMatch.match() ? alwaysMatch : null;
            }else{
                WebDrvCapabilities merged;
                for (WebDrvCapabilities firstMatch: firstMatchCaps){
                    merged = alwaysMatch.merge(firstMatch);
                    if (merged.match()){
                        return merged;
                    }
                }
                return null;
            }
        }else{
            return alwaysMatch.match() ? alwaysMatch : null;
        }
    }

    @SuppressWarnings("unchecked")
    void init() {
        if (caps.get("pageLoadStrategy") instanceof String==false){
            caps.put("pageLoadStrategy", "normal");            
        }
        if (caps.get("proxy") instanceof JSONObject==false){
            final JSONObject proxy = new JSONObject();
            proxy.put("proxyType", "direct");
            caps.put("proxy", proxy);
        }
        final Timeouts timeouts;
        if (caps.get("timeouts") instanceof JSONObject==false){
            timeouts = new Timeouts(new JSONObject());
        }else{
            timeouts = new Timeouts((JSONObject)caps.get("timeouts"));
        }
        timeouts.init();
        caps.put("timeouts", timeouts.json);        
    }
    
    public Timeouts getTimeouts(){
        return new Timeouts((JSONObject)caps.get("timeouts"));
    }
    
    @SuppressWarnings("unchecked")
    public void setTimeouts(final Timeouts timeouts){
        if (caps.get("timeouts") instanceof JSONObject){
            JSONObject json = (JSONObject)caps.get("timeouts");
            for (Object key: timeouts.json.keySet()){
                json.put(key, timeouts.json.get(key));
            }
        }else{
            caps.put("timeouts", timeouts.json.clone());
        }
    }
    
    @SuppressWarnings("unchecked")
    public void writeToJson(final JSONObject writeTo){
        writeTo.put("capabilities", (JSONObject)caps.clone());
    }
    
    public EPromptAction getUnhandledPromptBehaviour(){
        final String behaviour = (String)caps.get("unhandledPromptBehavior");
        if (behaviour!=null && !behaviour.isEmpty()){
            for (EPromptAction action: EPromptAction.values()){
                if (action.name().equalsIgnoreCase(behaviour)){
                    return action;
                }
            }
        }
        return null;                
    }
}
