cd ..

call make run PKG=A REPO=test/test#3-repo/dependencies#1.txt TREE=False

call make run PKG=C REPO=test/test#3-repo/dependencies#2.txt TREE=true

call make run PKG=A REPO=test/test#5-repo/dependencies#1.txt TREE=True

pause