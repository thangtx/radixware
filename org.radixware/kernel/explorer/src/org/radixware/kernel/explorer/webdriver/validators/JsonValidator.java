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

package org.radixware.kernel.explorer.webdriver.validators;

import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

public class JsonValidator {
    
    private final Map<Object, IJsonValueValidator> valueValidators = new HashMap<>();
    private final boolean strong;
    
    private JsonValidator(final boolean strong){
        this.strong = strong;
    }

    public void validate(final JSONObject json) throws WebDrvException{                
        for (Object key: json.keySet()){            
            final IJsonValueValidator validator = valueValidators.get(key);
            if (validator==null){
                if (strong){
                    throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "Unknown JSON property \'"+String.valueOf(key)+"\'");
                }
            }else if (!validator.isValid(json.get(key))){
                throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "property \'"+String.valueOf(key)+"\' has invalid value");
            }
        }
    }
    
    public static JsonValidator getProxyConfigurationValidator(){
        final JsonValidator validator = new JsonValidator(true);
        validator.valueValidators.put("proxyType", EnumValidator.getInstance(new String[]{"pac", "direct", "noproxy", "autodetect", "system", "manual"}, true));
        validator.valueValidators.put("proxyAutoconfigUrl", UrlValidator.getInstance());
        validator.valueValidators.put("ftpProxy", ProxyUrlValidator.getInstance());
        validator.valueValidators.put("ftpProxyPort", IntValidator.getInstance());
        validator.valueValidators.put("httpProxy", ProxyUrlValidator.getInstance());
        validator.valueValidators.put("httpProxyPort", IntValidator.getInstance());        
        validator.valueValidators.put("sslProxy", ProxyUrlValidator.getInstance());
        validator.valueValidators.put("sslProxyPort", IntValidator.getInstance());        
        validator.valueValidators.put("socksProxy", ProxyUrlValidator.getInstance());
        validator.valueValidators.put("socksProxyPort", IntValidator.getInstance());  
        validator.valueValidators.put("socksVersion", StrValidator.getInstance());
        validator.valueValidators.put("socksUserName", StrValidator.getInstance());
        validator.valueValidators.put("socksPassword", StrValidator.getInstance());
        return validator;
    }
    
    public static JsonValidator getCapabilitiesValidator(){
        final JsonValidator validator = new JsonValidator(false);
        validator.valueValidators.put("browserName", StrValidator.getInstance());
        validator.valueValidators.put("browserVersion", StrValidator.getInstance());
        validator.valueValidators.put("platformName", EnumValidator.getInstance(new String[]{"linux", "windows", "mac"}, true));
        validator.valueValidators.put("acceptInsecureCerts", BoolValidator.getInstance());
        validator.valueValidators.put("pageLoadStrategy", EnumValidator.getInstance(new String[]{"none", "eager", "normal"}, true));
        validator.valueValidators.put("setWindowRect", BoolValidator.getInstance());
        validator.valueValidators.put("unhandledPromptBehavior", EnumValidator.getInstance(new String[]{"dismiss", "accept"}, true));
        return validator;
    }
    
    public static JsonValidator getTimeoutdValidator(){
        final JsonValidator validator = new JsonValidator(false);
        validator.valueValidators.put("script", TimeoutValidator.getInstance());
        validator.valueValidators.put("pageLoad", TimeoutValidator.getInstance());
        validator.valueValidators.put("implicit", TimeoutValidator.getInstance());
        return validator;
    }
}
