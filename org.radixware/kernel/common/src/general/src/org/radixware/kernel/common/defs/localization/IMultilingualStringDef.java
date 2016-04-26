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

package org.radixware.kernel.common.defs.localization;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import static org.radixware.kernel.common.defs.ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ELocalizedStringKind;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchNameError;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.LocalizedString;


public interface IMultilingualStringDef {

    public static class StringStorage {

        private String value;
        private boolean needsCheck;
        private EIsoLanguage language;
        private long lastModified;
        private long timeChangeOfStatus;
        private String lastChangeAuthor;
        private String authorChangeOfStatus;

        public StringStorage(EIsoLanguage lang) {
            this.language = lang;
        }

        public StringStorage(StringStorage src) {
            this.value = src.value == null ? null : src.value;
            this.needsCheck = src.needsCheck;
            this.language = src.language;
        }

        public EIsoLanguage getLanguage() {
            return language;
        }

        public String getValue() {
            return value;
        }

        public boolean isNeedsCheck() {
            return needsCheck;
        }

        public void setNeedsCheck(boolean needsCheck,String authorName,long dateChangeOfStatus) {
            if (this.needsCheck != needsCheck){
                if (this.needsCheck){
                    this.authorChangeOfStatus = null;
                    this.timeChangeOfStatus = -1;
                } else {
                    if (getValue() != null && !getValue().isEmpty()){
                        this.authorChangeOfStatus = authorName;
                        this.timeChangeOfStatus = dateChangeOfStatus;
                    }
                }
            }
            ILocalizingBundleDef.version.incrementAndGet();
            this.needsCheck = needsCheck;
        }

        public boolean isChecked() {
            return !needsCheck;
        }

        public long getLastModified() {
            return lastModified;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public void setLastModified(long lastModified) {
            this.lastModified = lastModified;
        }

        public void setLastChangeAuthor(String lastChangeAuthor) {
            this.lastChangeAuthor = lastChangeAuthor;
            ILocalizingBundleDef.authors.addAuthor(lastChangeAuthor);
        }

        public String getLastChangeAutor() {
            return lastChangeAuthor;
        }

        public long getTimeChangeOfStatus() {
            return timeChangeOfStatus;
        }

        public String getAuthorChangeOfStatus() {
            return authorChangeOfStatus;
        }
        
        public void accept(IMultilingualStringDef multilingualString) {
            value = multilingualString.getValue(language);
            needsCheck = multilingualString.getNeedsCheck(language);
            
            Timestamp lastModifiedTime = multilingualString.getLastChangeTime(language);
            lastModified = lastModifiedTime == null ? -1 : lastModifiedTime.getTime();
            lastChangeAuthor = multilingualString.getLastChangeAuthor(language);
        }

        public void accept(String value, boolean needsCheck, long lastModified, String lastChangeAuthor) {
            setNeedsCheck(needsCheck, lastChangeAuthor, lastModified);
            this.value = value;
            this.lastModified = lastModified;
            this.lastChangeAuthor = lastChangeAuthor;
            ILocalizingBundleDef.authors.addAuthor(lastChangeAuthor);
        }
        
        public void accept(String value, boolean needsCheck, String authorChangeOfStatus, long timeChangeOfStatus, long lastModified, String lastChangeAuthor) {
            this.value = value;
            this.needsCheck = needsCheck;
            this.authorChangeOfStatus = authorChangeOfStatus;
            this.timeChangeOfStatus =  timeChangeOfStatus;
            this.lastModified = lastModified;
            this.lastChangeAuthor = lastChangeAuthor;
            ILocalizingBundleDef.authors.addAuthor(lastChangeAuthor);
        }
    }

    public static abstract class Storage {

        private List<StringStorage> values = null;
        private Map<EIsoLanguage, StringStorage> index;
        private boolean doSpellCheck = true;
        private int version = 0;
        private String author;
        private long creationDate;
        private final Definition owner;

        public Storage(Definition owner) {
            this.owner = owner;
            author = System.getProperty("user.name");
            creationDate = System.currentTimeMillis();
            ILocalizingBundleDef.version.incrementAndGet();
        }

        public Storage(Definition owner, LocalizedString xDef) {
            this.owner = owner;
            values = new LinkedList<>();
            for (LocalizedString.Value val : xDef.getValueList()) {
                try {
                    StringStorage storage = new StringStorage(val.getLanguage());
                    storage.accept(val.getStringValue(), val.getNeedsCheck(), val.isSetAuthorChangeOfStatus()? val.getAuthorChangeOfStatus(): null, val.isSetDateChangeOfStatus() ? val.getDateChangeOfStatus().getTime() : -1 ,val.isSetLastModified() ? val.getLastModified().getTime() : -1, val.isSetAuthor() ? val.getAuthor() : null);
                    values.add(storage);
                } catch (NoConstItemWithSuchNameError e) {
                    continue;
                }
            }
            if (xDef.isSetSpellCheck()) {
                doSpellCheck = xDef.getSpellCheck();
            }
            if (xDef.isSetVersion()) {
                version = xDef.getVersion();
            }
            author = xDef.isSetCreator() ? xDef.getCreator() : null;
            creationDate = xDef.isSetCreationDate() ? xDef.getCreationDate().getTime() : -1;
            ILocalizingBundleDef.authors.addAuthor(author);
        }

        public static void copyValue(Storage src, Storage dest) {
            if (src.values != null) {
                dest.values = new ArrayList<>();
                for (final StringStorage e : src.values) {
                    dest.values.add(new StringStorage(e));
                }
            }
            dest.version = src.getVersion();
        }
        
        public void updateLanguages(Set<EIsoLanguage> languages) {
            synchronized (this) {
                Iterator<StringStorage> iterator = getValues().iterator();
                while (iterator.hasNext()) {
                    StringStorage e = iterator.next();
                    EIsoLanguage lang = e.getLanguage();
                    if (!languages.contains(lang)) {
                        iterator.remove();
                        if (index != null) {
                            index.remove(lang);
                        }
                    }
                }
            }
        }
        
        public static void copyValueByLanguages(Storage src, Storage dest, Set<EIsoLanguage> languages) {
            if (src.values != null) {
                dest.values = new ArrayList<>();
                for (final StringStorage e : src.values) {
                    if (languages.contains(e.getLanguage())){
                        dest.values.add(new StringStorage(e));
                    }
                }
            }
            dest.version = src.getVersion();
        }

        public List<StringStorage> getValues() {
            synchronized (this) {
                if (values == null) {
                    values = new LinkedList<>();
                }
                return values;
            }
        }

        public Map<EIsoLanguage, StringStorage> getIndex() {
            synchronized (this) {
                if (index == null) {
                    index = new HashMap<>();
                }
                return index;
            }
        }

        public boolean isDoSpellCheck() {
            return doSpellCheck;
        }

        public void setDoSpellCheck(boolean doSpellCheck) {
            ILocalizingBundleDef.version.incrementAndGet();
            this.doSpellCheck = doSpellCheck;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
            ILocalizingBundleDef.authors.addAuthor(author);
        }

        public long getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(long creationDate) {
            this.creationDate = creationDate;
        }

        public StringStorage getString(EIsoLanguage language) {
            if (values == null) {
                return null;
            }
            if (index == null) {
                index = new EnumMap<>(EIsoLanguage.class);
                for (StringStorage s : values) {
                    index.put(s.getLanguage(), s);
                }
            }
            return index.get(language);
        }

        public StringStorage addString(EIsoLanguage lang) {
            StringStorage s = new StringStorage(lang);
            if (values == null) {
                values = new ArrayList<>();
            }
            values.add(s);
            if (index != null) {
                index.put(lang, s);
            }
            return s;
        }

        public String getValue(EIsoLanguage language) {
            synchronized (this) {
                StringStorage string = getString(language);
                return string == null ? null : string.getValue();
            }
        }

        public boolean setValue(EIsoLanguage language, String value) {
            synchronized (this) {
                StringStorage string = getString(language);

                if (string == null) {
                    string = addString(language);
                }
                string.accept(value, true, System.currentTimeMillis(), System.getProperty("user.name"));
                owner.setEditState(RadixObject.EEditState.MODIFIED);
                return true;
            }
        }

        public boolean loadValue(IMultilingualStringDef multilingualString, EIsoLanguage language) {
            synchronized (this) {
                StringStorage s = getString(language);
                if (s == null) {
                    s = addString(language);
                }
                s.accept(multilingualString);
                owner.setEditState(RadixObject.EEditState.MODIFIED);
                return true;
            }
        }

        public String findValue(EIsoLanguage preferredLanguage) {
            String val = getValue(preferredLanguage);
            List<EIsoLanguage> langs = new ArrayList<>(getLanguages());
            while ((val == null || val.isEmpty()) && !langs.isEmpty()) {
                EIsoLanguage lang = langs.remove(0);
                val = getValue(lang);
            }
            return val == null ? "" : val;
        }

        public boolean getNeedsCheck(EIsoLanguage language) {
            StringStorage storage = getString(language);
            return storage == null ? true : storage.isNeedsCheck();
        }

        public void setChecked(EIsoLanguage language, boolean needsCheck) {
            StringStorage storage = getString(language);
            if (storage == null) {
                storage = addString(language);
            }
            long time = System.currentTimeMillis();
            String authorName = System.getProperty("user.name");
            storage.setNeedsCheck(needsCheck,authorName,time);
            storage.setLastModified(time);
            storage.setLastChangeAuthor(authorName);
            owner.setEditState(RadixObject.EEditState.MODIFIED);
        }

        public String getAuthorChangeOfStatus(EIsoLanguage language){
            StringStorage storage = getString(language);
            if (storage == null) {
                return null;
            } else {
                return storage.getAuthorChangeOfStatus();
            }
        }
        
        public Timestamp getTimeChangeOfStatus(EIsoLanguage language){
            StringStorage storage = getString(language);
            if (storage == null) {
                return null;
            } else {
                return storage.getTimeChangeOfStatus()> 0 ? new Timestamp(storage.getTimeChangeOfStatus()) : null;
            }
        }
        
        public Timestamp getLastChangeTime(EIsoLanguage lang) {
            StringStorage storage = getString(lang);
            if (storage == null) {
                return null;
            } else {
                return storage.getLastModified() > 0 ? new Timestamp(storage.getLastModified()) : null;
            }
        }

        public String getLastChangeAuthor(EIsoLanguage lang) {
            StringStorage storage = getString(lang);
            if (storage == null) {
                return null;
            } else {
                return storage.getLastChangeAutor();
            }
        }

        public void appendTo(LocalizedString xDef, boolean isApi, EIsoLanguage lang) {
            if (values != null) {
                for (StringStorage storage : this.values) {
                    if (storage.getValue() != null && (lang == null || lang.equals(storage.getLanguage()))) {
                        LocalizedString.Value xVal = xDef.addNewValue();
                        xVal.setLanguage(storage.getLanguage());
                        xVal.setStringValue(storage.getValue());
                        xVal.setNeedsCheck(storage.isNeedsCheck());
                        if (!isApi) {
                            if (storage.getLastModified() > 0) {
                                xVal.setLastModified(new Timestamp(storage.getLastModified()));
                            }
                            if (storage.getLastChangeAutor() != null) {
                                xVal.setAuthor(storage.getLastChangeAutor());
                            }
                            if (storage.getAuthorChangeOfStatus() != null){
                                xVal.setAuthorChangeOfStatus(storage.getAuthorChangeOfStatus());
                            }
                            if (storage.getTimeChangeOfStatus() > 0){
                                xVal.setDateChangeOfStatus(new Timestamp(storage.getTimeChangeOfStatus()));
                            }
                        }
                    }
                }
            }
            if (!doSpellCheck) {
                xDef.setSpellCheck(false);
            }
            if (author != null && !author.isEmpty()) {
                xDef.setCreator(author);
            }
            if (creationDate != -1) {
                xDef.setCreationDate(new Timestamp(creationDate));
            }
            xDef.setVersion(version);
        }

        public List<StringStorage> getValues(ExtendableDefinitions.EScope scope) {
            synchronized (this) {
                Map<EIsoLanguage, Object> all_values = new EnumMap<>(EIsoLanguage.class);
                ArrayList<StringStorage> storage = new ArrayList<>();
                collectValues(all_values, storage, scope, this);
                return storage;
            }
        }

        public Set<EIsoLanguage> getLanguages() {
            if (values == null) {
                return Collections.emptySet();
            }
            EnumSet<EIsoLanguage> set = EnumSet.noneOf(EIsoLanguage.class);
            for (StringStorage s : values) {
                set.add(s.getLanguage());
            }
            return set;
        }

        private void collectValues(final Map<EIsoLanguage, Object> c, final List<StringStorage> collection, final ExtendableDefinitions.EScope scope, final Storage string) {
            synchronized (string) {
                if (string.values != null) {
                    for (StringStorage e : values) {
                        if (c.get(e.getLanguage()) == null) {
                            c.put(e.getLanguage(), null);
                            collection.add(e);
                        }
                    }
                }
                switch (scope) {
                    case LOCAL_AND_OVERWRITE:
                        findOverwritten(string.owner).iterate(new SearchResult.Acceptor<IMultilingualStringDef>() {
                            @Override
                            public void accept(IMultilingualStringDef object) {
                                if (object != null) {
                                    Storage storage = ((IMultilingualStringDef) object).getInternalStorage();
                                    storage.collectValues(c, collection, scope, storage);
                                }
                            }
                        });
                        break;
                }
            }
        }

        public SearchResult findOverwritten(Definition definition) {
            return ((IMultilingualStringDef) definition).findOverwritten();
        }
    }

    Id getId();

    Set<EIsoLanguage> getLanguages();

    IMultilingualStringDef cloneString(ILocalizingBundleDef bundle);
    
    IMultilingualStringDef cloneString(ILocalizingBundleDef bundle,boolean useSrcId);
    
    void updateLanguages(Set<EIsoLanguage> languages);

    void appendTo(LocalizedString xDef);

    void appendTo(LocalizedString xDef, EIsoLanguage lang);

    RadixObject getContainer();

    SearchResult findOverwritten();

    String getValue(EIsoLanguage lang);

    boolean setValue(EIsoLanguage lang, String value);

    boolean getNeedsCheck(EIsoLanguage language);
    
    Timestamp getTimeChangeOfStatus(EIsoLanguage language);
    
    String getAuthorChangeOfStatus(EIsoLanguage language);

    void setChecked(EIsoLanguage lang, boolean isChecked);

    EDefType getDefinitionType();    

    ILocalizingBundleDef getOwnerBundle();

    int getVersion();

    void setVersion(int version);

    Timestamp getLastChangeTime(EIsoLanguage language);

    String getLastChangeAuthor(EIsoLanguage language);

    boolean loadValue(IMultilingualStringDef multilingualString, EIsoLanguage language);

    List<StringStorage> getValues(ExtendableDefinitions.EScope scope);

    Module getModule();

    ELocalizedStringKind getSrcKind();

    boolean isReadOnly();

    boolean isSpellCheckEnabled();

    void setSpellCheckEnabled(boolean enabled);

    long getCreationDate();

    String getAuthor();

    Storage getInternalStorage();
}
