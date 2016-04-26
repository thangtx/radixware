nmake -f wia.msvc clean
nmake /E DEBUG_INFO=true -f wia.msvc
if %ERRORLEVEL% GEQ 1 EXIT /B 1
nmake -f wia.msvc