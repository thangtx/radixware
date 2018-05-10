
/* Radix::PersoComm::LimitPeriodKind - Common Executable*/

/*Radix::PersoComm::LimitPeriodKind-Enumeration*/

package org.radixware.ads.PersoComm.common;

public enum LimitPeriodKind implements org.radixware.kernel.common.types.IKernelStrEnum,org.radixware.kernel.common.types.ITitledEnum{
	aciHZZWDC7PDFFKPGPKGLSC6EGDZQ(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciHZZWDC7PDFFKPGPKGLSC6EGDZQ"),"Minute","minute",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWEXQUY7Q3ZBMNCWR2J3VVNZD4Q")),
	aciUBQAVBX2FBAVBNL3D6VD6VB5N4(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciUBQAVBX2FBAVBNL3D6VD6VB5N4"),"Hour","hour",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQOJGEGWYLRGWZB3J4FBYNUMKTQ")),
	aci4G74TPD53ZDUTJHXFNK33SEK7E(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci4G74TPD53ZDUTJHXFNK33SEK7E"),"Day","day",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAZMNTUUWUVA6RCD4F5SZGMAWMU"));

	private final org.radixware.kernel.common.types.Id id;
	private final String name;
	private final org.radixware.kernel.common.types.Id titleId;
	private Str val;






	private LimitPeriodKind(org.radixware.kernel.common.types.Id id, String name,
	Str value,org.radixware.kernel.common.types.Id titleId){
		this.id = id;
		this.name = name;
		this.titleId = titleId;
		this.val = value;
	}
	public String getName(){return name;}
	public Str getValue(){return val;}
	public boolean isInDomain(org.radixware.kernel.common.types.Id domainId){return getItemMeta(id).getIsInDomain($env_instance_storage_for_internal_usage$,domainId);}
	public boolean isInDomains( java.util.List<org.radixware.kernel.common.types.Id>ids){return getItemMeta(id).getIsInDomains($env_instance_storage_for_internal_usage$,ids);}
	/*Use {@linkplain #getValue()} instead*/
	@Deprecated
	public Str get(){return getValue();}
	public String getTitle(){
		return titleId==null?null:org.radixware.kernel.common.environment.IMlStringBundle.Lookup.getValue(acsS2G5HB3SZVBMTND5FBKRD7LXGU.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacsS2G5HB3SZVBMTND5FBKRD7LXGU"),titleId);
	}
	public String getTitle(org.radixware.kernel.common.enums.EIsoLanguage lang){
		return titleId==null?null:org.radixware.kernel.common.environment.IMlStringBundle.Lookup.getValue(acsS2G5HB3SZVBMTND5FBKRD7LXGU.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacsS2G5HB3SZVBMTND5FBKRD7LXGU"),titleId,lang);
	}
	private org.radixware.kernel.common.meta.RadEnumDef.Item getItemMeta(org.radixware.kernel.common.types.Id id){
		org.radixware.kernel.common.meta.RadEnumDef.Item im = (org.radixware.kernel.common.meta.RadEnumDef.Item)org.radixware.ads.PersoComm.common.LimitPeriodKind_mi.rdxMeta.getItems().findItemById(id,org.radixware.kernel.common.defs.ExtendableDefinitions.EScope.ALL);
		if(im == null)
			throw new org.radixware.kernel.common.exceptions.DefinitionNotFoundError(id);
		else
			return im;
		}

	public static final LimitPeriodKind getForValue(final Str val) {
			for (LimitPeriodKind t : LimitPeriodKind.values()) {
				if (t.getValue() == null && val == null || t.getValue().equals(val)) {
				return t;}
		}
		throw new org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError("Radix::PersoComm::LimitPeriodKind has no item with value: " + String.valueOf(val),val);}
	public static final org.radixware.kernel.common.meta.RadEnumDef getRadMeta(){ return org.radixware.ads.PersoComm.common.LimitPeriodKind_mi.rdxMeta;}
	@Deprecated
	public static final org.radixware.kernel.common.meta.RadEnumDef meta = getRadMeta();

		public static class Arr extends org.radixware.kernel.common.types.Arr<org.radixware.ads.PersoComm.common.LimitPeriodKind>{
			public static final long serialVersionUID = 0L;
			public Arr(){super();}
			public Arr(java.util.Collection<org.radixware.ads.PersoComm.common.LimitPeriodKind> collection){
				if(collection==null)throw new NullPointerException();
				for(org.radixware.ads.PersoComm.common.LimitPeriodKind item : collection){
					add(item);
				}
			}
			public Arr(org.radixware.ads.PersoComm.common.LimitPeriodKind[] array){
				if(array==null)throw new NullPointerException();
				for(int i = 0; i < array.length; i ++){
					add(array[i]);
				}
			}
			public Arr(org.radixware.kernel.common.types.ArrStr array){
				if(array == null) throw new NullPointerException();
				for(Str item : array){
					add(item==null?null:org.radixware.ads.PersoComm.common.LimitPeriodKind.getForValue(item));
				}
			}
			public org.radixware.kernel.common.enums.EValType getItemValType(){ 
				return org.radixware.kernel.common.enums.EValType.STR;
			}
		}
}

/* Radix::PersoComm::LimitPeriodKind - Common Meta*/

/*Radix::PersoComm::LimitPeriodKind-Enumeration*/

package org.radixware.ads.PersoComm.common;
public class LimitPeriodKind_mi{






	public static final org.radixware.kernel.common.meta.RadEnumDef rdxMeta;
	static{ 
	@SuppressWarnings("deprecation")
	org.radixware.kernel.common.meta.RadEnumDef.Item[] item_meta_arr = 
				new org.radixware.kernel.common.meta.RadEnumDef.Item[]{

						/*Radix::PersoComm::LimitPeriodKind:Minute-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciHZZWDC7PDFFKPGPKGLSC6EGDZQ"),"Minute",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("minute"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsS2G5HB3SZVBMTND5FBKRD7LXGU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWEXQUY7Q3ZBMNCWR2J3VVNZD4Q"),null,0,null,false,org.radixware.ads.PersoComm.common.LimitPeriodKind.Minute),

						/*Radix::PersoComm::LimitPeriodKind:Hour-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciUBQAVBX2FBAVBNL3D6VD6VB5N4"),"Hour",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("hour"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsS2G5HB3SZVBMTND5FBKRD7LXGU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQOJGEGWYLRGWZB3J4FBYNUMKTQ"),null,1,null,false,org.radixware.ads.PersoComm.common.LimitPeriodKind.Hour),

						/*Radix::PersoComm::LimitPeriodKind:Day-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci4G74TPD53ZDUTJHXFNK33SEK7E"),"Day",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("day"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsS2G5HB3SZVBMTND5FBKRD7LXGU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAZMNTUUWUVA6RCD4F5SZGMAWMU"),null,2,null,false,org.radixware.ads.PersoComm.common.LimitPeriodKind.Day)
				};
	 rdxMeta = new org.radixware.kernel.common.meta.RadEnumDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsS2G5HB3SZVBMTND5FBKRD7LXGU"),
				"LimitPeriodKind",
				org.radixware.kernel.common.enums.EValType.STR,false,item_meta_arr,$env_instance_storage_for_internal_usage$);
	}

}

/* Radix::PersoComm::LimitPeriodKind - Localizing Bundle */
package org.radixware.ads.PersoComm.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class LimitPeriodKind - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Day");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"День");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAZMNTUUWUVA6RCD4F5SZGMAWMU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Единицы измерения периода лимита.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unit of measurement for the limit period.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNWGB4FGEFVGHVHMLNKPLAFX2AY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Час");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Hour");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQOJGEGWYLRGWZB3J4FBYNUMKTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Минута");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Minute");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWEXQUY7Q3ZBMNCWR2J3VVNZD4Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\PersoComm"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(LimitPeriodKind - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacsS2G5HB3SZVBMTND5FBKRD7LXGU"),"LimitPeriodKind - Localizing Bundle",$$$items$$$);
}
