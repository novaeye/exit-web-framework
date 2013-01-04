@echo off

set base_path=%cd%

echo [INFO] install jar to local m2 repository.

call mvn clean source:jar install -Dmaven.test.skip=true

echo [INFO] create vcs admin archetype

cd %base_path%\showcase\vcs_admin
call mvn archetype:create-from-project

cd %base_path%\showcase\vcs_admin\target\generated-sources\archetype
call mvn clean install -Dmaven.test.skip=true

cd %base_path%\showcase\vcs_admin_jpa
call mvn archetype:create-from-project

cd %base_path%\showcase\vcs_admin_jpa\target\generated-sources\archetype
call mvn clean install -Dmaven.test.skip=true

cd %base_path%\showcase\vcs_admin
rd /S /Q target

cd %base_path%\showcase\vcs_admin_jpa
rd /S /Q target

cd %base_path%

pause