
/* Radix::Reports::ReportColumnsVisibilityMode - Common Executable*/

/*Radix::Reports::ReportColumnsVisibilityMode-Enumeration*/

package org.radixware.ads.Reports.common;

public enum ReportColumnsVisibilityMode implements org.radixware.kernel.common.types.IKernelStrEnum,org.radixware.kernel.common.types.ITitledEnum{
	aciVELKLY7NV5BS7GMPGGRI4RQEWA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciVELKLY7NV5BS7GMPGGRI4RQEWA"),"ALL","ALL",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSL2HT4HR7VEONIAYM6C2THIMTM")),
	aciJGEAQIFL4RHFZLPRUSXHECDHH4(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciJGEAQIFL4RHFZLPRUSXHECDHH4"),"SELECTED","SELECTED",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW27LMFL6TRDRFIZVU6GKJE3VLQ"));

	private final org.radixware.kernel.common.types.Id id;
	private final String name;
	private final org.radixware.kernel.common.types.Id titleId;
	private Str val;






	private ReportColumnsVisibilityMode(org.radixware.kernel.common.types.Id id, String name,
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
		return titleId==null?null:org.radixware.kernel.common.environment.IMlStringBundle.Lookup.getValue(acsVR6TRN62UZGH3BVLMIVBW3FVTQ.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacsVR6TRN62UZGH3BVLMIVBW3FVTQ"),titleId);
	}
	public String getTitle(org.radixware.kernel.common.enums.EIsoLanguage lang){
		return titleId==null?null:org.radixware.kernel.common.environment.IMlStringBundle.Lookup.getValue(acsVR6TRN62UZGH3BVLMIVBW3FVTQ.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacsVR6TRN62UZGH3BVLMIVBW3FVTQ"),titleId,lang);
	}
	private org.radixware.kernel.common.meta.RadEnumDef.Item getItemMeta(org.radixware.kernel.common.types.Id id){
		org.radixware.kernel.common.meta.RadEnumDef.Item im = (org.radixware.kernel.common.meta.RadEnumDef.Item)org.radixware.ads.Reports.common.ReportColumnsVisibilityMode_mi.rdxMeta.getItems().findItemById(id,org.radixware.kernel.common.defs.ExtendableDefinitions.EScope.ALL);
		if(im == null)
			throw new org.radixware.kernel.common.exceptions.DefinitionNotFoundError(id);
		else
			return im;
		}

	public static final ReportColumnsVisibilityMode getForValue(final Str val) {
			for (ReportColumnsVisibilityMode t : ReportColumnsVisibilityMode.values()) {
				if (t.getValue() == null && val == null || t.getValue().equals(val)) {
				return t;}
		}
		throw new org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError("Radix::Reports::ReportColumnsVisibilityMode has no item with value: " + String.valueOf(val),val);}
	public static final org.radixware.kernel.common.meta.RadEnumDef getRadMeta(){ return org.radixware.ads.Reports.common.ReportColumnsVisibilityMode_mi.rdxMeta;}
	@Deprecated
	public static final org.radixware.kernel.common.meta.RadEnumDef meta = getRadMeta();

		public static class Arr extends org.radixware.kernel.common.types.Arr<org.radixware.ads.Reports.common.ReportColumnsVisibilityMode>{
			public static final long serialVersionUID = 0L;
			public Arr(){super();}
			public Arr(java.util.Collection<org.radixware.ads.Reports.common.ReportColumnsVisibilityMode> collection){
				if(collection==null)throw new NullPointerException();
				for(org.radixware.ads.Reports.common.ReportColumnsVisibilityMode item : collection){
					add(item);
				}
			}
			public Arr(org.radixware.ads.Reports.common.ReportColumnsVisibilityMode[] array){
				if(array==null)throw new NullPointerException();
				for(int i = 0; i < array.length; i ++){
					add(array[i]);
				}
			}
			public Arr(org.radixware.kernel.common.types.ArrStr array){
				if(array == null) throw new NullPointerException();
				for(Str item : array){
					add(item==null?null:org.radixware.ads.Reports.common.ReportColumnsVisibilityMode.getForValue(item));
				}
			}
			public org.radixware.kernel.common.enums.EValType getItemValType(){ 
				return org.radixware.kernel.common.enums.EValType.STR;
			}
		}
}

/* Radix::Reports::ReportColumnsVisibilityMode - Common Meta*/

/*Radix::Reports::ReportColumnsVisibilityMode-Enumeration*/

package org.radixware.ads.Reports.common;
public class ReportColumnsVisibilityMode_mi{






	public static final org.radixware.kernel.common.meta.RadEnumDef rdxMeta;
	static{ 
	@SuppressWarnings("deprecation")
	org.radixware.kernel.common.meta.RadEnumDef.Item[] item_meta_arr = 
				new org.radixware.kernel.common.meta.RadEnumDef.Item[]{

						/*Radix::Reports::ReportColumnsVisibilityMode:ALL-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciVELKLY7NV5BS7GMPGGRI4RQEWA"),"ALL",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("ALL"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsVR6TRN62UZGH3BVLMIVBW3FVTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSL2HT4HR7VEONIAYM6C2THIMTM"),null,0,null,false,org.radixware.ads.Reports.common.ReportColumnsVisibilityMode.ALL),

						/*Radix::Reports::ReportColumnsVisibilityMode:SELECTED-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciJGEAQIFL4RHFZLPRUSXHECDHH4"),"SELECTED",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("SELECTED"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsVR6TRN62UZGH3BVLMIVBW3FVTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW27LMFL6TRDRFIZVU6GKJE3VLQ"),null,1,null,false,org.radixware.ads.Reports.common.ReportColumnsVisibilityMode.SELECTED)
				};
	 rdxMeta = new org.radixware.kernel.common.meta.RadEnumDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsVR6TRN62UZGH3BVLMIVBW3FVTQ"),
				"ReportColumnsVisibilityMode",
				org.radixware.kernel.common.enums.EValType.STR,false,item_meta_arr,$env_instance_storage_for_internal_usage$);
	}

}

/* Radix::Reports::ReportColumnsVisibilityMode - Localizing Bundle */
package org.radixware.ads.Reports.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ReportColumnsVisibilityMode - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"All");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Все");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSL2HT4HR7VEONIAYM6C2THIMTM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Reports"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Selected");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выбранные");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW27LMFL6TRDRFIZVU6GKJE3VLQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Reports"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(ReportColumnsVisibilityMode - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacsVR6TRN62UZGH3BVLMIVBW3FVTQ"),"ReportColumnsVisibilityMode - Localizing Bundle",$$$items$$$);
}
