echo off
rem assumes you are in folder D:\IJGradle\gppDemos\batFiles
rem from which you invoke runDemo or any of the other bat files
rem typical invocation
rem D:\IJGradle\gppDemos\batFiles>runDemo MCpi RunSkelMCpi piTest 4 1024 10000
rem where %1 is the folder that contains the script given as %2 and
rem %3 is the file to which the csv style output will be written in the folder D:\IJGradle\gppDemos\csvFiles
cd ..
for %%A in (1 2 3 4 5 6 7 8 9 10 ) do (
echo run %%A of %1 %2 to %3 args %4 %5 %6 %7 %8
java -jar .\out\artifacts\gppDemos_jar\gppDemos.jar .\src\main\groovy\gppDemos\%1\%2.groovy %1 %4 %5 %6 %7 %8 >>  .\csvFiles\%3.csv) 2> .\csvFiles\%3.err
cd batFiles

