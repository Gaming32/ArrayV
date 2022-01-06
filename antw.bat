@REM MIT License
@REM
@REM Copyright (c) 2022 Josiah (Gaming32) Glosson
@REM
@REM Permission is hereby granted, free of charge, to any person obtaining a copy
@REM of this software and associated documentation files (the "Software"), to deal
@REM in the Software without restriction, including without limitation the rights
@REM to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
@REM copies of the Software, and to permit persons to whom the Software is
@REM furnished to do so, subject to the following conditions:
@REM
@REM The above copyright notice and this permission notice shall be included in all
@REM copies or substantial portions of the Software.
@REM
@REM THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
@REM IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
@REM FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
@REM AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
@REM LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
@REM OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
@REM SOFTWARE.

@echo off
setlocal enabledelayedexpansion
if "%JAVA_HOME%"=="" goto setJavaHome
goto setupArgs

:setJavaHome
for /f "tokens=2 delims==" %%a in ('java.exe -XshowSettings:properties -version 2^>^&1 ^| findstr java.home') do (
    set JAVA_HOME=%%a
    set JAVA_HOME=!JAVA_HOME:~1!
)

@REM echo %JAVA_HOME%
@REM exit /b

:setupArgs
if ""%1""=="""" goto runAnt
set ANT_CMD_LINE_ARGS=%ANT_CMD_LINE_ARGS% %1
shift
goto setupArgs

:runAnt
"%JAVA_HOME%\bin\java.exe" %ANT_OPTS% -classpath "%JAVA_HOME%\lib\tools.jar;.\wrapper\wrapper.jar" "-Djava.home=%JAVA_HOME%" com.rimerosolutions.ant.wrapper.AntWrapperMain %ANT_ARGS% %ANT_CMD_LINE_ARGS%
