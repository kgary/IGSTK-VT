
IF(NOT DEFINED ENV{XERCESCROOT})
	MESSAGE("XERCESCROOT variable not set.")
	MESSAGE("Please download xercesc library from xml.apache.org")
	MESSAGE("Please use export command to set XERCESCROOT variable on Unix systems.")
	MESSAGE(FATAL_ERROR "$XERCESCROOT/lib should have xerces libraries for the software")
ENDIF(NOT DEFINED ENV{XERCESCROOT})

FILE (GLOB SRC_FILES *.cxx)

SET(CMAKE_CXX_COMPILER g++)

SET(CMAKE_CXX_FLAGS "-DLINUX -D_REENTRANT -DNDEBUG  -DAPP_NO_THREADS -DXML_USE_NO_THREADS -lpthread")

SET(EXECUTABLE_OUTPUT_PATH ${Coverage_BINARY_DIR}/bin)

INCLUDE_DIRECTORIES(${Coverage_SOURCE_DIR}/include)
INCLUDE_DIRECTORIES($ENV{XERCESCROOT}/include)

LINK_DIRECTORIES($ENV{XERCESCROOT}/lib)

ADD_EXECUTABLE(coverage ${SRC_FILES})

TARGET_LINK_LIBRARIES(coverage xerces-c)

