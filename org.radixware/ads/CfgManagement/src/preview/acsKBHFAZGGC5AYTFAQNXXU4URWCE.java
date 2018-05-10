
/* Radix::CfgManagement::CfgPacketState - Common Executable*/

/*Radix::CfgManagement::CfgPacketState-Enumeration*/

package org.radixware.ads.CfgManagement.common;

public enum CfgPacketState implements org.radixware.kernel.common.types.IKernelStrEnum,org.radixware.kernel.common.types.ITitledEnum{
	aciITIHI5S32BE7XG4VYYCHUNRKGM(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciITIHI5S32BE7XG4VYYCHUNRKGM"),"UnderConstruction","UnderConstruction",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMP3RHDH2BBF7VKTHLVIB5OE2XY")),
	aci5FX56NL26ZHU5M6UDSBK52IKYU(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci5FX56NL26ZHU5M6UDSBK52IKYU"),"Closed","Closed",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMISQWGI32ZHDLMKILZP5NBYLWI")),
	aciTWFNCSH2GRCF5EZMIEDAAOIFEQ(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciTWFNCSH2GRCF5EZMIEDAAOIFEQ"),"Exported","Exported",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW2AOBG55I5D37CWZ3CAIQARPCE")),
	@Deprecated
	aciAREQYHZMCRBVFEPET2M5WBVZ5Y(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciAREQYHZMCRBVFEPET2M5WBVZ5Y"),"UpdatedAfterExport","UpdatedAfterExport",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB3ULVOHHXVD4RILNTFOTNA4IDU"));

	private final org.radixware.kernel.common.types.Id id;
	private final String name;
	private final org.radixware.kernel.common.types.Id titleId;
	private Str val;






	private CfgPacketState(org.radixware.kernel.common.types.Id id, String name,
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
		return titleId==null?null:org.radixware.kernel.common.environment.IMlStringBundle.Lookup.getValue(acsKBHFAZGGC5AYTFAQNXXU4URWCE.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacsKBHFAZGGC5AYTFAQNXXU4URWCE"),titleId);
	}
	public String getTitle(org.radixware.kernel.common.enums.EIsoLanguage lang){
		return titleId==null?null:org.radixware.kernel.common.environment.IMlStringBundle.Lookup.getValue(acsKBHFAZGGC5AYTFAQNXXU4URWCE.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacsKBHFAZGGC5AYTFAQNXXU4URWCE"),titleId,lang);
	}
	private org.radixware.kernel.common.meta.RadEnumDef.Item getItemMeta(org.radixware.kernel.common.types.Id id){
		org.radixware.kernel.common.meta.RadEnumDef.Item im = (org.radixware.kernel.common.meta.RadEnumDef.Item)org.radixware.ads.CfgManagement.common.CfgPacketState_mi.rdxMeta.getItems().findItemById(id,org.radixware.kernel.common.defs.ExtendableDefinitions.EScope.ALL);
		if(im == null)
			throw new org.radixware.kernel.common.exceptions.DefinitionNotFoundError(id);
		else
			return im;
		}

	public static final CfgPacketState getForValue(final Str val) {
			for (CfgPacketState t : CfgPacketState.values()) {
				if (t.getValue() == null && val == null || t.getValue().equals(val)) {
				return t;}
		}
		throw new org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError("Radix::CfgManagement::CfgPacketState has no item with value: " + String.valueOf(val),val);}
	public static final org.radixware.kernel.common.meta.RadEnumDef getRadMeta(){ return org.radixware.ads.CfgManagement.common.CfgPacketState_mi.rdxMeta;}
	@Deprecated
	public static final org.radixware.kernel.common.meta.RadEnumDef meta = getRadMeta();

		public static class Arr extends org.radixware.kernel.common.types.Arr<org.radixware.ads.CfgManagement.common.CfgPacketState>{
			public static final long serialVersionUID = 0L;
			public Arr(){super();}
			public Arr(java.util.Collection<org.radixware.ads.CfgManagement.common.CfgPacketState> collection){
				if(collection==null)throw new NullPointerException();
				for(org.radixware.ads.CfgManagement.common.CfgPacketState item : collection){
					add(item);
				}
			}
			public Arr(org.radixware.ads.CfgManagement.common.CfgPacketState[] array){
				if(array==null)throw new NullPointerException();
				for(int i = 0; i < array.length; i ++){
					add(array[i]);
				}
			}
			public Arr(org.radixware.kernel.common.types.ArrStr array){
				if(array == null) throw new NullPointerException();
				for(Str item : array){
					add(item==null?null:org.radixware.ads.CfgManagement.common.CfgPacketState.getForValue(item));
				}
			}
			public org.radixware.kernel.common.enums.EValType getItemValType(){ 
				return org.radixware.kernel.common.enums.EValType.STR;
			}
		}
}

/* Radix::CfgManagement::CfgPacketState - Common Meta*/

/*Radix::CfgManagement::CfgPacketState-Enumeration*/

package org.radixware.ads.CfgManagement.common;
public class CfgPacketState_mi{






	public static final org.radixware.kernel.common.meta.RadEnumDef rdxMeta;
	static{ 
	@SuppressWarnings("deprecation")
	org.radixware.kernel.common.meta.RadEnumDef.Item[] item_meta_arr = 
				new org.radixware.kernel.common.meta.RadEnumDef.Item[]{

						/*Radix::CfgManagement::CfgPacketState:UnderConstruction-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciITIHI5S32BE7XG4VYYCHUNRKGM"),"UnderConstruction",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("UnderConstruction"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsKBHFAZGGC5AYTFAQNXXU4URWCE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMP3RHDH2BBF7VKTHLVIB5OE2XY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("imgOQFNHNXFXNB6PIEQS2W3W4FWZU"),0,null,false,org.radixware.ads.CfgManagement.common.CfgPacketState.UnderConstruction),

						/*Radix::CfgManagement::CfgPacketState:Closed-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci5FX56NL26ZHU5M6UDSBK52IKYU"),"Closed",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Closed"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsKBHFAZGGC5AYTFAQNXXU4URWCE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMISQWGI32ZHDLMKILZP5NBYLWI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("imgKJSYQ6EMZVATVHOAYVT3Y4Z6FU"),3,null,false,org.radixware.ads.CfgManagement.common.CfgPacketState.Closed),

						/*Radix::CfgManagement::CfgPacketState:Exported-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciTWFNCSH2GRCF5EZMIEDAAOIFEQ"),"Exported",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Exported"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsKBHFAZGGC5AYTFAQNXXU4URWCE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW2AOBG55I5D37CWZ3CAIQARPCE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("imgEZIMROBN5VCVNPUOAIAP7L2BBU"),1,null,false,org.radixware.ads.CfgManagement.common.CfgPacketState.Exported),

						/*Radix::CfgManagement::CfgPacketState:UpdatedAfterExport-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciAREQYHZMCRBVFEPET2M5WBVZ5Y"),"UpdatedAfterExport",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("UpdatedAfterExport"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsKBHFAZGGC5AYTFAQNXXU4URWCE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB3ULVOHHXVD4RILNTFOTNA4IDU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("imgIXY44OI4DBAGDCEHXL3E4LBZVA"),2,null,true,org.radixware.ads.CfgManagement.common.CfgPacketState.UpdatedAfterExport)
				};
	 rdxMeta = new org.radixware.kernel.common.meta.RadEnumDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsKBHFAZGGC5AYTFAQNXXU4URWCE"),
				"CfgPacketState",
				org.radixware.kernel.common.enums.EValType.STR,false,item_meta_arr,$env_instance_storage_for_internal_usage$);
	}

}

/* Radix::CfgManagement::CfgPacketState - Localizing Bundle */
package org.radixware.ads.CfgManagement.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgPacketState - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Updated after export");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обновлен после экспорта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB3ULVOHHXVD4RILNTFOTNA4IDU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\CfgManagement"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Configuration package state");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Состояние пакета конфигурации.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBQYEVNOOQBHQPPBYMM2OBLGWAM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\CfgManagement"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Closed");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Закрыт");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMISQWGI32ZHDLMKILZP5NBYLWI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\CfgManagement"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Under construction");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"В процессе заполнения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMP3RHDH2BBF7VKTHLVIB5OE2XY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\CfgManagement"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Exported");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспортирован");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW2AOBG55I5D37CWZ3CAIQARPCE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\CfgManagement"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"State");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Состояние");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWXRX6DYQPZACNBBAA2QNJHVUXI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\CfgManagement"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(CfgPacketState - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacsKBHFAZGGC5AYTFAQNXXU4URWCE"),"CfgPacketState - Localizing Bundle",$$$items$$$);
}
