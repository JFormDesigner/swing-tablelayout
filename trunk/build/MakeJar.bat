@echo off

echo Use Ant build instead
goto Done

echo --------------------------------
echo Go to TableLayout root directory
echo --------------------------------
cd ..

echo -----------------------
echo Cleanning jar directory
echo -----------------------
erase /s /q jar
deltree /y jar
md jar
md jar\src
md jar\src\info
md jar\classes\info
md jar\javadoc

echo ------------------------
echo Compiling layout package
echo ------------------------
javac -d classes -classpath %classpath%;.\src; src\info\clearthought\layout\*.java


echo ------------------
echo Generating JavaDoc
echo ------------------
javadoc -public -author -version -d jar\javadoc -sourcepath src info.clearthought.layout

echo -------------
echo Copying files
echo -------------
xcopy /e src\info jar\src\info
xcopy /e classes\info jar\classes\info

echo ------------
echo Jaring files
echo ------------
cd jar\src
jar cf ..\TableLayout-src.jar *.*

cd ..\classes
jar cf ..\TableLayout.jar *.*

cd ..\javadoc
jar cf ..\TableLayout-javadoc.jar *.*

cd ..\..

echo -------------------------
echo Return to build directory
echo -------------------------
cd build

:Done
echo Done
