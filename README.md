# Merge files

## Setup IDE (Idea)
```
./gradlew idea
open mergefiles.ipr
```

## How to build and launch
```
./gradlew build
java -jar build/libs/mergefiles-1.0.jar <inputFolder> <outputPath>
```
For example
```
java -jar build/libs/mergefiles-1.0.jar /Users/rohit_mathew/Desktop/Soln /Users/rohit_mathew/Desktop/output.txt
```