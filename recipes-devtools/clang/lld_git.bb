# Copyright (C) 2017 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "LLD is a linker from the LLVM project"
HOMEPAGE = "http://lld.llvm.org/"
LICENSE = "MIT | NCSA"
SECTION = "devel"

DEPENDS += "clang-native libcxx"

require clang.inc

FILESPATH =. "${FILE_DIRNAME}/clang:"

inherit cmake
PV .= "+git${SRCPV}"

LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=${LLVMMD5SUM}; \
                    file://tools/lld/LICENSE.TXT;md5=${LLDMD5SUM}; \
                   "

SRC_URI = "\
           ${LLVM_GIT}/llvm.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=llvm \
           ${LLVM_GIT}/lld.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};destsuffix=git/tools/lld;name=lld \
           file://0001-llvm-Remove-CMAKE_CROSSCOMPILING-so-it-can-cross-com.patch \
           file://0002-llvm-Do-not-assume-linux-glibc.patch \
           file://0003-llvm-TargetLibraryInfo-Undefine-libc-functions-if-th.patch \
          "
SRCREV_FORMAT = "llvm_lld"

S = "${WORKDIR}/git"

OECMAKE_FIND_ROOT_PATH_MODE_PROGRAM = "BOTH"
EXTRA_OECMAKE = "\
    -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen \
"
CXXFLAGS += "-stdlib=libc++"

do_compile() {
	cd ${B}/tools/lld
	base_do_compile VERBOSE=1
}

do_install() {
	cd ${B}/tools/lld
	oe_runmake 'DESTDIR=${D}' install
}
BBCLASSEXTEND = "native nativesdk"
