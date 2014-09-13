PROJECT(IGSTK-VT-Drools)

INCLUDE(Dart)
ENABLE_TESTING()

FILE(GLOB CLASSPATH ${IGSTK-VT-Drools_SOURCE_DIR}/lib/*.jar)

STRING(REPLACE ".jar" ".jar:" CLASSPATH ${CLASSPATH})

SET(CLASSPATH "${CLASSPATH}:${IGSTK-VT-Drools_SOURCE_DIR}/simulator.jar:${IGSTK-VT-Drools_SOURCE_DIR}/generator.jar:${IGSTK-VT-Drools_SOURCE_DIR}/properties")

ADD_CUSTOM_TARGET(COMPILE ALL)

ADD_CUSTOM_COMMAND(
        TARGET COMPILE
        COMMAND ant
        ARGS dist-simulator
        )

ADD_TEST(IGSTK-VT-TestDrools java -cp ${CLASSPATH} org.igstk.validation.simulator.IgstkvtVMStaticRunner)
