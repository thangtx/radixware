
/* Radix::Reports::EReportColumnsUserResizeMode - Common Executable*/

/*Radix::Reports::EReportColumnsUserResizeMode-Enumeration*/

package org.radixware.ads.Reports.common;

public class EReportColumnsUserResizeMode{






	public static final org.radixware.kernel.common.meta.RadEnumDef getRadMeta(){ return org.radixware.ads.Reports.common.EReportColumnsUserResizeMode_mi.rdxMeta;}
	@Deprecated
	public static final org.radixware.kernel.common.meta.RadEnumDef meta = getRadMeta();

		public static class Arr extends org.radixware.kernel.common.types.Arr<org.radixware.kernel.common.enums.EReportColumnsUserResizeMode>{
			public static final long serialVersionUID = 0L;
			public Arr(){super();}
			public Arr(java.util.Collection<org.radixware.kernel.common.enums.EReportColumnsUserResizeMode> collection){
				if(collection==null)throw new NullPointerException();
				for(org.radixware.kernel.common.enums.EReportColumnsUserResizeMode item : collection){
					add(item);
				}
			}
			public Arr(org.radixware.kernel.common.enums.EReportColumnsUserResizeMode[] array){
				if(array==null)throw new NullPointerException();
				for(int i = 0; i < array.length; i ++){
					add(array[i]);
				}
			}
			public Arr(org.radixware.kernel.common.types.ArrStr array){
				if(array == null) throw new NullPointerException();
				for(Str item : array){
					add(item==null?null:org.radixware.kernel.common.enums.EReportColumnsUserResizeMode.getForValue(item));
				}
			}
			public org.radixware.kernel.common.enums.EValType getItemValType(){ 
				return org.radixware.kernel.common.enums.EValType.STR;
			}
		}
}

/* Radix::Reports::EReportColumnsUserResizeMode - Common Meta*/

/*Radix::Reports::EReportColumnsUserResizeMode-Enumeration*/

package org.radixware.ads.Reports.common;
public class EReportColumnsUserResizeMode_mi{






	public static final org.radixware.kernel.common.meta.RadEnumDef rdxMeta;
	static{ 
	@SuppressWarnings("deprecation")
	org.radixware.kernel.common.meta.RadEnumDef.Item[] item_meta_arr = 
				new org.radixware.kernel.common.meta.RadEnumDef.Item[]{

						/*Radix::Reports::EReportColumnsUserResizeMode:NONE-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci6IN2HSFJPZDYFPVFZGPDZZQ574"),"NONE",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("None"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsP44GAVGA5RECPKJC6VMJLFWD3I"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3RIAVJABSBGTTBHNOKSX7SFEPI"),null,0,null,false,org.radixware.kernel.common.enums.EReportColumnsUserResizeMode.NONE),

						/*Radix::Reports::EReportColumnsUserResizeMode:MANUAL-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciGBNCSLARZ5BUDLF3FY4JDK4AGQ"),"MANUAL",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Manual"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsP44GAVGA5RECPKJC6VMJLFWD3I"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsETXF66SDANDBNBZ3W34UQH3Y6I"),null,1,null,false,org.radixware.kernel.common.enums.EReportColumnsUserResizeMode.MANUAL),

						/*Radix::Reports::EReportColumnsUserResizeMode:ADJUST_WIDTH-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciK4E2QCIDC5ATFBFA3GAFY3C4IE"),"ADJUST_WIDTH",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Adjust width"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsP44GAVGA5RECPKJC6VMJLFWD3I"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCRE55BZA55FINOHXM2ETNIPBJM"),null,2,null,false,org.radixware.kernel.common.enums.EReportColumnsUserResizeMode.ADJUST_WIDTH)
				};
	 rdxMeta = new org.radixware.kernel.common.meta.RadEnumDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsP44GAVGA5RECPKJC6VMJLFWD3I"),
				"EReportColumnsUserResizeMode",
				org.radixware.kernel.common.enums.EValType.STR,false,item_meta_arr,$env_instance_storage_for_internal_usage$);
	}

}

/* Radix::Reports::EReportColumnsUserResizeMode - Localizing Bundle */
package org.radixware.ads.Reports.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EReportColumnsUserResizeMode - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Use original width");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Использовать оригинальную ширину");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3RIAVJABSBGTTBHNOKSX7SFEPI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Reports"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Automatic width selection (in proportion to the original)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Автоматических подбор ширины (пропорционально исходной)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCRE55BZA55FINOHXM2ETNIPBJM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Reports"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Set width manually");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Установить ширину вручную");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsETXF66SDANDBNBZ3W34UQH3Y6I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Reports"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(EReportColumnsUserResizeMode - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacsP44GAVGA5RECPKJC6VMJLFWD3I"),"EReportColumnsUserResizeMode - Localizing Bundle",$$$items$$$);
}
