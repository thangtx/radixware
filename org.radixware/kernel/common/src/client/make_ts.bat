SET CURRENT_DIR=%CD%
SET CLIENT=%CD%\src\org\radixware\kernel\common\client
SET EXPLORER=%CURRENT_DIR%\..\..\..\explorer\src\org\radixware\kernel\explorer
SET WEB=%CURRENT_DIR%\..\..\..\web\src\org\radixware\wps
lupdate -no-obsolete -extensions java %EXPLORER% %CLIENT% %WEB% -ts .\client_en.ts
pause
linguist client_en.ts
