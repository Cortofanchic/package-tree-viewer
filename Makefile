ifeq ($(OS),Windows_NT)
    GRADLEW := gradlew.bat
else
    GRADLEW := ./gradlew
endif

GRADLE ?= $(GRADLEW)
GRADLE_FLAGS ?= --console=plain --no-daemon


PKG   ?= $(PACKAGE_NAME)
URL   ?= $(REPO_URL)
REPO  ?= $(REPO_PATH)
TREE  ?= $(TREE_MODE)
DEPTH ?= $(MAX_DEPTH)
SUB   ?= $(FILTER_SUB)


GRADLE_ARGS := $(if $(strip $(PKG)),  -PPKG=$(PKG))\
               $(if $(strip $(URL)),  -PURL=$(URL))\
               $(if $(strip $(REPO)), -PREPO=$(REPO))\
               $(if $(strip $(TREE)), -PTREE=$(TREE))\
               $(if $(strip $(DEPTH)),-PDEPTH=$(DEPTH))\
               $(if $(strip $(SUB)),  -PSUB=$(SUB))

.PHONY: all build run jar clean help

all: build

build:
	$(GRADLE) $(GRADLE_FLAGS) build

run:
	@echo "[INFO] gradle run $(GRADLE_ARGS)"
	$(GRADLE) $(GRADLE_FLAGS) run $(GRADLE_ARGS)

jar:
	$(GRADLE) $(GRADLE_FLAGS) jar
	@echo "[OK] jars in build/libs/"

clean:
	$(GRADLE) $(GRADLE_FLAGS) clean
	@echo "[OK] cleaned"

help:
	@echo Usage:
	@echo   make [all]                 # gradle build
	@echo   make run [VAR=VAL ...]     # gradle run with -P
	@echo   make jar                   # build jar(s)
	@echo   make clean                 # gradle clean
	@echo   make build                 # gradle build
	@echo Run-time variables (or env PACKAGE_NAME, REPO_URL, REPO_PATH,
	@echo TREE_MODE, MAX_DEPTH, FILTER_SUB):
	@echo   PKG=NAME   URL=URL   REPO=PATH
	@echo   TREE=true  DEPTH=N   SUB=STR