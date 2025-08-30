@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@echo off

setlocal

set DIR=%~dp0
set APP_BASE_NAME=%~n0
set APP_HOME=%DIR%

@rem Resolve any ".lnk" (shortcut) that might point to this script
:resolveLnk
if exist "%APP_HOME%\%APP_BASE_NAME%.lnk" (
    for /f "tokens=*" %%i in ('dir /b /a:-d "%APP_HOME%\%APP_BASE_NAME%.lnk"') do (
        set LINK_FILE=%APP_HOME%%%i
        for /f "tokens=*" %%j in ('powershell -nologo -noprofile -command "(New-Object -COM WScript.Shell).CreateShortcut('%LINK_FILE%').TargetPath"') do (
            set APP_HOME=%%~dpj
        )
    )
)

set CLASSPATH=
set JAVA_EXE=java

if defined JAVA_HOME (
    set JAVA_EXE=%JAVA_HOME%\bin\java.exe
)

set WRAPPER_JAR=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar
set WRAPPER_MAIN=org.gradle.wrapper.GradleWrapperMain

"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% -classpath "%WRAPPER_JAR%" %WRAPPER_MAIN% %*

endlocal
