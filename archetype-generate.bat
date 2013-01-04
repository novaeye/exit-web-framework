@echo off

echo [INFO] create project from vcs admin template.

if not exist %~dp0\generated-sources (md %~dp0\generated-sources)

cd %~dp0\generated-sources


:message
set /p var=1:vcs-admin-panel 2:vcs-admin-jpa-panel Ñ¡ÔñÄ£°å:

if "%var%" == "1" (
call mvn archetype:generate -DarchetypeGroupId=org.exitsoft.showcase -DarchetypeArtifactId=exitsoft-vcs-admin-archetype -DarchetypeVersion=1.1.0
) else if "%var%" == "2" (
call mvn archetype:generate -DarchetypeGroupId=org.exitsoft.showcase -DarchetypeArtifactId=exitsoft-vcs-admin-jpa-archetype -DarchetypeVersion=1.1.0
) else ( goto message )

cd %~dp0

pause