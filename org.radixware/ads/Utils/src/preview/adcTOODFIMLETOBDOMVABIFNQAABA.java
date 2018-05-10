
/* Radix::Utils::String - Common Executable*/

/*Radix::Utils::String-Common Dynamic Class*/

package org.radixware.ads.Utils.common;

import java.util.Arrays;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;




@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::String")
public published class String  {

	private static final char START_CHAR = 'Ё';
	private static final String[] charTable = new String[81];
	static
	{  fillCharTable(); }

	private static MessageDigest mdSha1 = null;

	static 
	{
	   try 
	   {
	       mdSha1  = MessageDigest.getInstance( "SHA-1" );
	   } 
	   catch ( NoSuchAlgorithmException e) 
	   {
	       throw new AppError( "Failed to initialize SHA-1 messаge digest algorithm" );
	   }   
	}



	/*Radix::Utils::String:Nested classes-Nested Classes*/

	/*Radix::Utils::String:Properties-Properties*/

	/*Radix::Utils::String:Methods-Methods*/

	/*Radix::Utils::String:truncLead-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::String:truncLead")
	public static published  Str truncLead (Str s, int len) {
		if(s==null || len>=s.length())
		   return s;
		return s.substring(s.length()-len,s.length());
	}

	/*Radix::Utils::String:transliteToLat-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::String:transliteToLat")
	public static published  Str transliteToLat (Str text) {
		/*
		Диапазон 00-1F занят неотображаемыми управляющими кодами, большинство из которых 
		уже вышли из употребления
		Диапазон 20-7F занят самыми распространёнными знаками пунктуации, цифрами и буквами латинского алфавита.
		*/

		char chars[] = text.toCharArray();
		final StringBuilder sb = new StringBuilder(text.length());

		char symbol;        
		for (int c = 0 ; c < chars.length; c++){
		    symbol = chars[c];
		    final int i = symbol - START_CHAR;
		    if (i>=0 && i<charTable.length){
		         final String replace = charTable[i];
		         if (replace == null) {
		             sb.append('?');
		         } else {
		             if (//NEYVABANKRU-268
		                Char.isUpperCase(symbol) && 
		                (
		                    c < chars.length-1 ?
		                        Char.isUpperCase(chars[c+1]) :
		                        c > 0 && Char.isUpperCase(chars[c-1])
		                )    
		             ){
		                 sb.append(replace.toUpperCase()); 
		             } else {    
		                 sb.append(replace);
		             }   
		         }   
		    } else {
		        sb.append(symbol);
		    }           
		}
		return sb.toString();


	}

	/*Radix::Utils::String:fillCharTable-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::String:fillCharTable")
	private static  void fillCharTable () {
		charTable['А'- START_CHAR] = "A";
		charTable['Б'- START_CHAR] = "B";
		charTable['В'- START_CHAR] = "V";
		charTable['Г'- START_CHAR] = "G";
		charTable['Д'- START_CHAR] = "D";
		charTable['Е'- START_CHAR] = "E";
		charTable['Ё'- START_CHAR] = "E";  //NEYVABANKRU-268: old: "Yo";
		charTable['Ж'- START_CHAR] = "Zh";
		charTable['З'- START_CHAR] = "Z";
		charTable['И'- START_CHAR] = "I";
		charTable['Й'- START_CHAR] = "I";    //NEYVABANKRU-268: old: "Y";
		charTable['К'- START_CHAR] = "K";
		charTable['Л'- START_CHAR] = "L";
		charTable['М'- START_CHAR] = "M";
		charTable['Н'- START_CHAR] = "N";
		charTable['О'- START_CHAR] = "O";
		charTable['П'- START_CHAR] = "P";
		charTable['Р'- START_CHAR] = "R";
		charTable['С'- START_CHAR] = "S";
		charTable['Т'- START_CHAR] = "T";
		charTable['У'- START_CHAR] = "U";
		charTable['Ф'- START_CHAR] = "F";
		charTable['Х'- START_CHAR] = "Kh";   //NEYVABANKRU-268: old: "X";
		charTable['Ц'- START_CHAR] = "Tc";   //NEYVABANKRU-268: old: "C";
		charTable['Ч'- START_CHAR] = "Ch";
		charTable['Ш'- START_CHAR] = "Sh";
		charTable['Щ'- START_CHAR] = "Shch"; //NEYVABANKRU-268: old: "Shh";
		charTable['Ъ'- START_CHAR] = "";     //NEYVABANKRU-268: old: "``";
		charTable['Ы'- START_CHAR] = "Y";    //NEYVABANKRU-268: old: "Y'";
		charTable['Ь'- START_CHAR] = "";     //NEYVABANKRU-268: old: "'"
		charTable['Э'- START_CHAR] = "E";
		charTable['Ю'- START_CHAR] = "Iu";   //NEYVABANKRU-268: old: "Yu";
		charTable['Я'- START_CHAR] = "Ia";   //NEYVABANKRU-268: old: "Ya";

		for (int count = 0; count<charTable.length; count++) 
		{
		   char indx = (char)((char)count + START_CHAR);
		   char lower = new String(new char[]{indx}).toLowerCase().charAt(0);
		   if (charTable[count] != null) 
		     charTable[lower - START_CHAR] = charTable[count].toLowerCase();
		}
	}

	/*Radix::Utils::String:truncTrail-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::String:truncTrail")
	public static published  Str truncTrail (Str s, int len) {
		if(s==null || len>=s.length())
		   return s;
		return s.substring(0,len);
	}

	/*Radix::Utils::String:padTrail-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::String:padTrail")
	public static published  Str padTrail (Str s, int len, char ch) {
		int padLen=len-s.length();
		if(padLen<=0) 
		   return s;

		char[] pad=new char[padLen]; Arrays.fill(pad,ch); 
		StringBuilder sb= (s==null ? new StringBuilder() : new StringBuilder(s));
		return sb.append(pad).toString();
	}

	/*Radix::Utils::String:collectionToString-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::String:collectionToString")
	public static published  Str collectionToString (java.util.Collection<?> col, Str delimeter) {
		String res = "";
		for (Object obj : col) {
		    res += (res != "" ? delimeter : "") + String.valueOf(obj);
		}
		return res;
	}

	/*Radix::Utils::String:padLead-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::String:padLead")
	public static published  Str padLead (Str s, int len, char ch) {
		int padLen = len - (s == null ? 0 : s.length());
		if (padLen <= 0)
		    return s;

		char[] pad = new char[padLen];
		Arrays.fill(pad, ch);
		StringBuilder sb = (s == null ? new StringBuilder() : new StringBuilder(s));
		return sb.insert(0, pad).toString();
	}

	/*Radix::Utils::String:sha1-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::String:sha1")
	public static published  byte[] sha1 (byte[] b) {
		if(b != null){
		   mdSha1.reset();
		   mdSha1.update(b);
		   return mdSha1.digest();
		} else {
		   return null;
		}

	}

	/*Radix::Utils::String:trimLead-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::String:trimLead")
	public static published  Str trimLead (Str s, char ch) {
		if(s==null)
		   return s;
		int i;
		for(i=0;i<s.length();i++) {
		   if(s.charAt(i)!=ch) 
		      break; 
		   }
		return i<s.length() ? s.substring(i) : "";
	}

	/*Radix::Utils::String:trimTrail-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::String:trimTrail")
	public static published  Str trimTrail (Str s, char ch) {
		if(s==null)
		   return s;
		int i;
		for(i=s.length()-1;i>=0;i--) {
		   if(s.charAt(i)!=ch) 
		      break; 
		   }
		return i>=0 ? s.substring(0,i+1) : "";
	}

	/*Radix::Utils::String:collectionToString-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::String:collectionToString")
	public static published  Str collectionToString (java.util.Collection<?> col) {
		return collectionToString(col,", ");
			
	}

	/*Radix::Utils::String:adjustStrLength-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::String:adjustStrLength")
	public static published  Str adjustStrLength (Str s, int len, char padChar, boolean padLead) {
		Str res = s==null?"":s;
		if ( len<0||res.length()==len)
		    return res;
		if (res.length()>len)
		    res = padLead ? res.substring(0,len) : res.substring(res.length()-len);
		if (res.length()<len)
		    res = padLead? padLead(res, len, padChar): padTrail(res, len, padChar);
		return res;
	}

	/*Radix::Utils::String:toLiteralStr-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::String:toLiteralStr")
	public static published  Str toLiteralStr (java.sql.Timestamp date, org.radixware.kernel.common.enums.EIsoLanguage language) {
		return org.radixware.kernel.common.utils.DateConverter.convertDate(date, language);

	}

	/*Radix::Utils::String:toLiteralStr-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::String:toLiteralStr")
	public static published  Str toLiteralStr (Int number, org.radixware.kernel.common.enums.EIsoLanguage language, org.radixware.kernel.common.enums.EGender gender) {
		return org.radixware.kernel.common.utils.NumericConverter.convertNumber(number.longValue(), language, gender);

	}

	/*Radix::Utils::String:toLiteralStr-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::String:toLiteralStr")
	public static published  Str toLiteralStr (Int number, org.radixware.kernel.common.enums.EIsoLanguage language) {
		return org.radixware.kernel.common.utils.NumericConverter.convertNumber(number.longValue(), language);

	}

	/*Radix::Utils::String:sha1-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::String:sha1")
	public static published  byte[] sha1 (Str s) {
		try {
		    return sha1( s!=null ? s.getBytes("utf-8") : null ) ;
		} catch (java.io.UnsupportedEncodingException e) {
		    throw new AppError(e.getMessage(), e);
		}

	}

	/*Radix::Utils::String:isBlank-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::String:isBlank")
	public static published  boolean isBlank (Str s) {
		return org.apache.commons.lang.StringUtils.isBlank(s);
	}

	/*Radix::Utils::String:isNotBlank-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::String:isNotBlank")
	public static published  boolean isNotBlank (Str s) {
		return org.apache.commons.lang.StringUtils.isNotBlank(s);
	}


}

/* Radix::Utils::String - Server Meta*/

/*Radix::Utils::String-Common Dynamic Class*/

package org.radixware.ads.Utils.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class String_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcTOODFIMLETOBDOMVABIFNQAABA"),"String",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Utils::String:Properties-Properties*/
						null,

						/*Radix::Utils::String:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4W7W2V36TRADVMYWFV7H2HA5VI"),"truncLead",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("s",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHZCF3MWWGNAALM3DVNAVZ3HWIM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("len",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3WOLB2KHSJBZ3F5RQN73O5BOUI"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth55TXFAKITPOBDCNHAAMPGXSZKU"),"transliteToLat",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("text",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWS7G4YTFUVEHTPRMNENYJYVTY4"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthA2LX7D74LNAI3DB3KWRBBTDSU4"),"fillCharTable",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAZH2BGENETOBDOMVABIFNQAABA"),"truncTrail",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("s",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprS6R444V5FZBOVNKPEPKMIYX7SY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("len",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEDA5MRUEGBFHXFSLO6GIRBEOHA"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDRFIKPENETOBDOMVABIFNQAABA"),"padTrail",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("s",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprR7ARLECU65BW3OVTV56EOFSTS4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("len",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMRC7SUEFQ5HKXLVFD63QRTQ6KI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("ch",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5PY5PC2KONFZZAYMAYUKJSLXBE"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOGXFBL5QD3PBDHKOAAN7YHKUNI"),"collectionToString",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("col",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGZALKTPV3RFA3FOV2KWSMAQ3OQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("delimeter",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJX3L6UEZIZF3XCK2DNWNQIMB7Y"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPAY6JM4LETOBDOMVABIFNQAABA"),"padLead",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("s",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprL6SQPLERKJFH7HK7IHJGNL5X2U")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("len",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYIW3LLZIUNH5DAOKA7WCPCCLLQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("ch",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVTNF4UD53RBJJM42ZIB4E6XLQE"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPWTNHLJMNDOBDCPCABIFNQAABA"),"sha1",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("b",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDZSVTYPATVA4REPU5D6H4KS4Y4"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUX2FCPUOETOBDOMVABIFNQAABA"),"trimLead",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("s",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMCJZYHCZEJDUNGEHQJEHOA6EQE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("ch",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOGK32CZNJ5ALTHQE4QPLE3VK4U"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthX57LROUPETOBDOMVABIFNQAABA"),"trimTrail",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("s",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2XRJTM2VQRCQZPSXY6VNNYTP4A")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("ch",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKTJVYRKXKRCJRJGN77BMQEHMYU"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXFHPALEDCTPBDEQYAAN7YHKUNI"),"collectionToString",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("col",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDGEK4ZRBD5H43MEHZUQ2UFLCIY"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGLXAZHUXUNEHLDV37APSSGPY2E"),"adjustStrLength",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("s",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNDEBTNCHE5G4VKE2SACRPQGIWY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("len",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNA5BFD7WRZAPDJRYSJSE6EJSJI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("padChar",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprG6C6CFZK6BFJDJLXVKJO4QFIVE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("padLead",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6MBFHWUCJVEPBONMMBLDRO4X6Y"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4C65AQ5MD5DBZOEOJE5L7VC4BU"),"toLiteralStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("date",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUOFERYIXOFCBLK5RU2QDMXQKZM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("language",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXIZANW6NXFDHFBQPEVUGFAXSWE"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMGOCNFO23BAW5LZODVKNVXTZMQ"),"toLiteralStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("number",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFV3A2SXMXVEXXL3YS2FVEXH65E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("language",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQ4ZWSKKG4NEB5GQVHCLVTHSWOI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("gender",org.radixware.kernel.common.enums.EValType.CHAR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprD7ZGH2APKJE4TGDGHD2XVF6FZU"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthREYWVGDYHBGXPEIT7IKGJVAVKM"),"toLiteralStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("number",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprL35II7FMINFXNJHORKVKUT4LGE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("language",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprD5OYQYZW65GK3PJT2H2AEFRMTE"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4TJFLLXJZFDERD6POQTPUQUMIQ"),"sha1",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("s",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSJFGTSWCRRC65GHZCJL25NCTAI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2OJZGD67LVFFBNMVZAREP3NGME"),"isBlank",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("s",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUDGI4RGGOBDQJN4A6PHWF4DU6Y"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth44PNOR5YVZH6DFYIO5GA5KIBDU"),"isNotBlank",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("s",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUDGI4RGGOBDQJN4A6PHWF4DU6Y"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

/* Radix::Utils::String - Client-Common Meta*/

/*Radix::Utils::String-Common Dynamic Class*/

package org.radixware.ads.Utils.common_client;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class String_mi{
}

/* Radix::Utils::String - Desktop Meta*/

/*Radix::Utils::String-Common Dynamic Class*/

package org.radixware.ads.Utils.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class String_mi{
}

/* Radix::Utils::String - Web Meta*/

/*Radix::Utils::String-Common Dynamic Class*/

package org.radixware.ads.Utils.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class String_mi{
}

/* Radix::Utils::String - Localizing Bundle */
package org.radixware.ads.Utils.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class String - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(String - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcTOODFIMLETOBDOMVABIFNQAABA"),"String - Localizing Bundle",$$$items$$$);
}
