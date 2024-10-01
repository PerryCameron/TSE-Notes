@echo off
setlocal enabledelayedexpansion

REM Debug: Print environment variables to see the paths being used
echo HOMEDRIVE: %HOMEDRIVE%
echo HOMEPATH: %HOMEPATH%

REM Define the preferred and secondary locations using C: instead of %HOMEDRIVE%
set preferredDir=C:\Users\%USERNAME%\OneDrive - Schneider Electric
set preferredLocation="%preferredDir%\TSENotes\notes.db"
set secondaryLocation=C:\Users\%USERNAME%\TSENotes\notes.db

REM Define the path to the database in the resources directory
set "resourceDbPath=%~dp0resources\notes.db"

REM Check if the preferred location already has the database
if exist %preferredLocation% (
    echo Database found at preferred location: %preferredLocation%
    goto :end
) else (
    echo Database not found at preferred location: %preferredLocation%
)

REM Check if the secondary location already has the database
if exist %secondaryLocation% (
    echo Database found at secondary location: %secondaryLocation%
    goto :end
) else (
    echo Database not found at secondary location: %secondaryLocation%
)

REM Preferred directory exists, but database file does not exist; copy it from resources
if exist "%preferredDir%" (
    echo Preferred directory exists: %preferredDir%
    set targetDir="%preferredDir%\TSENotes"
    if not exist "%targetDir%" mkdir "%targetDir%"
    echo Copying database from resources to preferred directory...
    copy "%resourceDbPath%" "%targetDir%\notes.db"
    goto :end
)

REM If preferred directory is not reachable, use the secondary location
echo OneDrive not reachable, using home directory instead.
set targetDir="C:\Users\%USERNAME%\TSENotes"
if not exist "%targetDir%" mkdir "%targetDir%"
echo Copying database from resources to home directory...
copy "%resourceDbPath%" "%targetDir%\notes.db"

:end
echo Database setup completed.
pause
exit /b

