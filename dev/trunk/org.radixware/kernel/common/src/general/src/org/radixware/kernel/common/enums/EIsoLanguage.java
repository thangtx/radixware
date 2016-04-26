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

package org.radixware.kernel.common.enums;

import java.util.List;
import java.util.MissingResourceException;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixResourceBundle;

public enum EIsoLanguage implements IKernelStrEnum {

    ABKHAZIAN("ab"),
    AFAR("aa"),
    AFRIKAANS("af"),
    AKAN("ak"),
    ALBANIAN("sq"),
    AMHARIC("am"),
    ARABIC("ar"),
    ARAGONESE("an"),
    ARMENIAN("hy"),
    ASSAMESE("as"),
    AVARIC("av"),
    AVESTAN("ae"),
    AYMARA("ay"),
    AZERBAIJANI("az"),
    BAMBARA("bm"),
    BASHKIR("ba"),
    BASQUE("eu"),
    BELARUSIAN("be"),
    BENGALI("bn"),
    BIHARI("bh"),
    BISLAMA("bi"),
    BOSNIAN("bs"),
    BRETON("br"),
    BULGARIAN("bg"),
    BURMESE("my"),
    CATALAN("ca"),
    CHAMORRO("ch"),
    CHECHEN("ce"),
    CHICHEWA("ny"),
    CHINESE("zh"),
    CHURCH("cu"),
    CHUVASH("cv"),
    CORNISH("kw"),
    CORSICAN("co"),
    CREE("cr"),
    CROATIAN("hr"),
    CZECH("cs"),
    DANISH("da"),
    DIVEHI("dv"),
    DUTCH("nl"),
    DZONGKHA("dz"),
    ENGLISH("en"),
    ESPERANTO("eo"),
    ESTONIAN("et"),
    EWE("ee"),
    FAROESE("fo"),
    FIJIAN("fj"),
    FINNISH("fi"),
    FRENCH("fr"),
    FULAH("ff"),
    GALICIAN("gl"),
    GANDA("lg"),
    GEORGIAN("ka"),
    GERMAN("de"),
    GREEK("el"),
    GUARANA("gn"),
    GUJARATI("gu"),
    HAITIAN("ht"),
    HAUSA("ha"),
    HEBREW("he"),
    HERERO("hz"),
    HINDI("hi"),
    HIRI("ho"),
    HUNGARIAN("hu"),
    ICELANDIC("is"),
    IDO("io"),
    IGBO("ig"),
    INDONESIAN("id"),
    INTERLINGUA("ia"),
    INTERLINGUE("ie"),
    INUKTITUT("iu"),
    INUPIAQ("ik"),
    IRISH("ga"),
    ITALIAN("it"),
    JAPANESE("ja"),
    JAVANESE("jv"),
    KALAALLISUT("kl"),
    KANNADA("kn"),
    KANURI("kr"),
    KASHMIRI("ks"),
    KAZAKH("kk"),
    KHMER("km"),
    KIKUYU("ki"),
    KINYARWANDA("rw"),
    KIRGHIZ("ky"),
    KIRUNDI("rn"),
    KOMI("kv"),
    KONGO("kg"),
    KOREAN("ko"),
    KURDISH("ku"),
    KWANYAMA("kj"),
    LAO("lo"),
    LATIN("la"),
    LATVIAN("lv"),
    LIMBURGISH("li"),
    LINGALA("ln"),
    LITHUANIAN("lt"),
    LUBA_KATANGA("lu"),
    LUXEMBOURGISH("lb"),
    MACEDONIAN("mk"),
    MALAGASY("mg"),
    MALAY("ms"),
    MALAYALAM("ml"),
    MALTESE("mt"),
    MANX("gv"),
    MAORI("mi"),
    MARATHI("mr"),
    MARSHALLESE("mh"),
    MOLDAVIAN("mo"),
    MONGOLIAN("mn"),
    NAURU("na"),
    NAVAJO("nv"),
    NDONGA("ng"),
    NEPALI("ne"),
    NORTH("nd"),
    NORTHERN("se"),
    NORWEGIAN1("no"),
    NORWEGIAN2("nb"),
    NORWEGIAN3("nn"),
    OCCITAN("oc"),
    OJIBWA("oj"),
    ORIYA("or"),
    OROMO("om"),
    OSSETIAN("os"),
    PALI("pi"),
    PANJABI("pa"),
    PASHTO("ps"),
    PERSIAN("fa"),
    POLISH("pl"),
    PORTUGUESE("pt"),
    QUECHUA("qu"),
    RAETO_ROMANCE("rm"),
    ROMANIAN("ro"),
    RUSSIAN("ru"),
    RUSYN("ry"),
    SAMOAN("sm"),
    SANGO("sg"),
    SANSKRIT("sa"),
    SARDINIAN("sc"),
    SCOTTISH("gd"),
    SERBIAN("sr"),
    SERBO_CROATIAN("sh"),
    SHONA("sn"),
    SICHUAN("ii"),
    SINDHI("sd"),
    SINHALESE("si"),
    SLOVAK("sk"),
    SLOVENIAN("sl"),
    SOMALI("so"),
    SOTHO("st"),
    SOUTH("nr"),
    SPANISH("es"),
    SUNDANESE("su"),
    SWAHILI("sw"),
    SWATI("ss"),
    SWEDISH("sv"),
    TAGALOG("tl"),
    TAHITIAN("ty"),
    TAJIK("tg"),
    TAMIL("ta"),
    TATAR("tt"),
    TELUGU("te"),
    THAI("th"),
    TIBETAN("bo"),
    TIGRINYA("ti"),
    TONGA("to"),
    TSONGA("ts"),
    TSWANA("tn"),
    TURKISH("tr"),
    TURKMEN("tk"),
    TWI("tw"),
    UIGHUR("ug"),
    UKRAINIAN("uk"),
    URDU("ur"),
    UZBEK("uz"),
    VENDA("ve"),
    VIETNAMESE("vi"),
    VOLAPAK("vo"),
    WALLOON("wa"),
    WELSH("cy"),
    WESTERN("fy"),
    WOLOF("wo"),
    XHOSA("xh"),
    YIDDISH("yi"),
    YORUBA("yo"),
    ZHUANG("za"),
    ZULU("zu");
    private final String val;

    private EIsoLanguage(String val) {
        this.val = val;
    }

    @Override
    public String getValue() {
        return val;
    }

    @Override
    public String getName() {
        try {
            return RadixResourceBundle.getMessage(EIsoLanguage.class, "Language-Display-Name-" + this.name());
        } catch (MissingResourceException ex) {
            return name();
        }
    }

    public static EIsoLanguage getForValue(final String val) {
        for (EIsoLanguage e : EIsoLanguage.values()) {
            if (e.getValue().equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("IsoLanguageEnum has no item with value: " + String.valueOf(val),val);
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
}
