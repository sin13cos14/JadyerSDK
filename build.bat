@echo off
title �Զ�������ߡ�����������
color 02
set curdir=%~dp0

echo ��ʼ���Maven���� =============================================================
echo;
call mvn clean install -DskipTests
echo;
echo Maven���̴����� =============================================================

echo;
echo ��ʼ����War�������� ===========================================================
echo;
if exist "%userprofile%\Desktop\" (
    xcopy %curdir%\JadyerSDK-demo\target\*.war %userprofile%\Desktop\ /Y
    c:
    cd %userprofile%\Desktop\
) else if exist "%userprofile%\����\" (
    xcopy %curdir%\JadyerSDK-demo\target\*.war %userprofile%\����\ /Y
    c:
    cd %userprofile%\����\
)
if exist "JadyerSDK.war" (
    del JadyerSDK.war /Q
)
ren JadyerSDK*.war JadyerSDK.war
echo;
echo War���Ѿ����������� ===========================================================

echo;
pause