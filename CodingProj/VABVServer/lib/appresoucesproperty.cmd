@echo off
@set JAVA_HOME=D:\JAVA\jdk1.6.0_01
@set JAVA_RUN=D:\JAVA\jdk1.6.0_01
@rem set CLASSPATH=D:\JAVA\tomcat6\lib;.;%CLASSPATH%
@set CURRENT_DIR=D:\JAVA\tomcat6\lib
d:
@cd %CURRENT_DIR%

@copy ApplicationResources.properties ApplicationResources_en.properties
@"%JAVA_RUN%\bin\native2ascii.exe" ApplicationResources_zh.txt ApplicationResources_zh.properties
@rem "%JAVA_RUN%\bin\native2ascii.exe" -reverse -encoding Cp1258 ApplicationResources_vi.properties ApplicationResources_vi.txt
@rem "%JAVA_RUN%\bin\native2ascii.exe" -reverse ApplicationResources_vi.properties ApplicationResources_vi.txt
@dir *Resources*.*
set SERVICE_NAME=tomcat6
set CATALINA_HOME=D:\Tomcat 6.0
net stop  %SERVICE_NAME%
@if exist "%CATALINA_HOME%\work" rd /q /s "%CATALINA_HOME%\work"
@if exist "%CATALINA_HOME%\logs\*.log" del "%CATALINA_HOME%\logs\*.log"
@if exist "%CATALINA_HOME%\logs\*.sh" del "%CATALINA_HOME%\logs\*.sh"
@if exist "%CATALINA_HOME%\logs\*.cmd" del "%CATALINA_HOME%\logs\*.cmd"
net start %SERVICE_NAME%
if not errorlevel 1 echo ------service %SERVICE_NAME% already startup------
java -classpath "%CATALINA_HOME%\lib\catalina.jar" org.apache.catalina.util.ServerInfo
@choice /t 6 /d y>nul
@rem pause
exit
