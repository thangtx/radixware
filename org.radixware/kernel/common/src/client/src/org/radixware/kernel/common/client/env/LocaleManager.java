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

package org.radixware.kernel.common.client.env;

import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.enums.EIsoCountry;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.xscml.Sqml;


public abstract class LocaleManager {//NOPMD
    
    private final static Id COUNTRY_ENUM_ID = Id.Factory.loadFrom("acsQK3EKSSOTZDKVFKO3FY5PTAV7A");
    
    private EIsoLanguage language = EIsoLanguage.ENGLISH;
    private EIsoCountry country;
    private Locale locale;
    
    public LocaleManager(){
        country = getDefaultCountry(language);
        if (country==null){
            locale = new Locale(language.getValue());
        }
        else{
            locale = new Locale(language.getValue(), country.getValue());
        }
    }
    
    public void changeLocale(final EIsoLanguage language, final EIsoCountry country){
        this.language = language;
        this.country = country==null ? getDefaultCountry(language) : country;
        if (this.country==null){
            locale = new Locale(language.getValue());
        }
        else{            
            locale = new Locale(language.getValue(), this.country.getValue());
        }
    }
    
    public EIsoLanguage getLanguage(){        
        return language;
    }
    
    public EIsoCountry getCountry(){
        return country;
    }
    
    public Locale getLocale(){
        return locale;
    }
    
    public static EIsoCountry getDefaultCountry(final EIsoLanguage language){
        if (Locale.getDefault().getLanguage().equals(language.getValue())){
            final String countryAsStr = Locale.getDefault().getCountry();
            if (countryAsStr==null || countryAsStr.isEmpty()){
                return null;
            }
            try{
                return EIsoCountry.getForValue(countryAsStr);
            }               
            catch(NoConstItemWithSuchValueError error){
                return null;
            }
        }
        else{
            return null;
        }
    }
    
    public static EnumSet<EIsoCountry> getCountriesForLanguage(final EIsoLanguage language){
        final EnumSet<EIsoCountry> countries = EnumSet.noneOf(EIsoCountry.class);
        for (Locale locale: Locale.getAvailableLocales()){
            if (locale.getLanguage().equals(language.getValue())){
                try{
                    countries.add(EIsoCountry.getForValue(locale.getCountry()));
                }
                catch(NoConstItemWithSuchValueError error){//NOPMD
                    //ignore unknown country
                }
            }
        }
        return countries;
    }
    
    public static String getLocalizedCountryName(final EIsoCountry country, final IClientEnvironment environment){
        if (country==null){
            return null;
        }
        if (!environment.getApplication().isReleaseRepositoryAccessible()){
            return country.getName();
        }
        final RadEnumPresentationDef enumDef;
        try{
            enumDef = environment.getDefManager().getEnumPresentationDef(COUNTRY_ENUM_ID);
        }catch(DefinitionNotFoundError error){
            return country.getName();
        }
        final RadEnumPresentationDef.Item enumItem = enumDef.getItems().findItemForConstant(country);
        return enumItem==null ? country.getName() : enumItem.getTitle(environment.getLanguage());        
    }
    
    public abstract List<EIsoLanguage> getSupportedLanguages();
}
