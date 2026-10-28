cd ..

echo "Test 1"
call make run PKG=My

echo "Test 2"
call make run PKG=My1 URL=htt://error_url

echo "Test 3"
call make run PKG=My2 URL=http://correct_url

echo "Test 4"
call make run PKG=My3 REPO=error/path/that/does/not/exist

echo "Test 5"
call make run PKG=My4 REPO=src/main/java/org/example/build/RunCLI.java DEPTH=4

echo "Test 6"
call make run PKG=My5 URL=http://correct_url DEPTH=0

echo "Test 7"
call make run PKG=My6 URL=http://correct_url TREE=IDN

echo "Test 8"
call make run PKG=My7 URL=http://correct_url TREE=True

echo "Test 9"
call make run PKG=My8 URL=http://correct_url TREE=false SUB=everystrwithoutspaces

pause