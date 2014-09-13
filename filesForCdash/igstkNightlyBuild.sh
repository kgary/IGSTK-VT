#export PATH=/usr/local/jdk1.6.0/bin/:/usr/local/cmake-2.4.7-Linux-i386/bin/:$PATH

export IGSTKVT_HOME=/home/igstk/IGSTK/IGSTK-VT-ELO
/usr/bin/startx &
export DISPLAY=:0
cd /home/igstk/IGSTK/IGSTK-HEAD/
/usr/bin/cvs -d:pserver:anonymous@public.kitware.com:/cvsroot/IGSTK update -APd
cd /home/igstk/IGSTK/IGSTK-HEAD-bin/
/usr/bin/make
/usr/bin/ctest -D  Experimental >>/home/igstk/IGSTK/Cdash_Scripts_logs/logs/buildLog-`date +%m%d%Y`.log

echo "====================Done IGSTK +++++++++++++++++++++++" >>/home/igstk/IGSTK/Cdash_Scripts_logs/logs/buildLog-`date +%m%d%Y`.log


#/home/igstk/IGSTK/IGSTK-3.0-bin/bin/igstkStateMachineExportTest  /home/igstk/IGSTK/IGSTK-3.0-bin/bin/test >>/home/igstk/IGSTK/Cdash_Scripts_logs/logs/buildLog-`date +%m%d%Y`.log
#ls -ltr  /home/igstk/IGSTK/IGSTK-3.0-bin/bin/test/*.xml >>/home/igstk/IGSTK/Cdash_Scripts_logs/logs/buildLog-`date +%m%d%Y`.log
#cp /home/igstk/IGSTK/IGSTK-3.0-bin/bin/test/*.xml /home/igstk/IGSTK/IGSTK-VT-ELO/xmlFiles/scxmlFiles/ >>/home/igstk/IGSTK/Cdash_Scripts_logs/logs/buildLog-`date +%m%d%Y`.log
#cd /home/igstk/IGSTK/IGSTK-VT-ELO/xmlFiles/scxmlFiles/
#/usr/bin/cvs ci -m "Just got updated as IGSTK program ran" *.xml

echo "The igstk home is $IGSTKVT_HOME" >>/home/igstk/IGSTK/Cdash_Scripts_logs/logs/buildLog-`date +%m%d%Y`.log
cd /home/igstk/IGSTK/IGSTK-VT-ELO/junit
/usr/bin/ctest -D Nightly >>/home/igstk/IGSTK/Cdash_Scripts_logs/logs/buildLog-`date +%m%d%Y`.log
echo "++++++++++++++++++Done Junit++++++++++++++++++++++" >>/home/igstk/IGSTK/Cdash_Scripts_logs/logs/buildLog-`date +%m%d%Y`.log

cd /home/igstk/IGSTK/IGSTK-VT-ELO
cmake .  >>/home/igstk/IGSTK/Cdash_Scripts_logs/logs/buildLog-`date +%m%d%Y`.log
make  >>/home/igstk/IGSTK/Cdash_Scripts_logs/logs/buildLog-`date +%m%d%Y`.log
/usr/bin/ctest -D Nightly >>/home/igstk/IGSTK/Cdash_Scripts_logs/logs/buildLog-`date +%m%d%Y`.log
mv CTestConfig.cmake CTestConfigold.cmake
cd properties

#mv vtconfig.xml vtconfigold.xml
#mv igstk.drl igstkold.drl

mv igstkvt.properties igstkvtold.properties
cd ..
for (( i = 1; i <= 9; i++ ))
do
        mv CTestConfig_$i.cmake CTestConfig.cmake
        cd properties

       # mv vtconfig_$i.xml vtconfig.xml
       # mv igstk_$i.drl igstk.drl

        mv igstkvt_$i.properties igstkvt.properties
	cd ..
        cmake .  >>/home/igstk/IGSTK/Cdash_Scripts_logs/logs/buildLog-`date +%m%d%Y`.log
        make  >>/home/igstk/IGSTK/Cdash_Scripts_logs/logs/buildLog-`date +%m%d%Y`.log
        /usr/bin/ctest -D Experimental  >>/home/igstk/IGSTK/Cdash_Scripts_logs/logs/buildLog-`date +%m%d%Y`.log
        mv CTestConfig.cmake CTestConfig_$i.cmake 
        cd properties

       # mv vtconfig.xml vtconfig_$i.xml
       # mv igstk.drl igstk_$i.drl

        mv igstkvt.properties igstkvt_$i.properties
	cd ..
done
mv CTestConfigold.cmake CTestConfig.cmake
cd properties

#mv vtconfigold.xml vtconfig.xml
#mv igstkold.drl igstk.drl

mv igstkvtold.properties igstkvt.properties
cd ..
echo "++++++++++++++++++++++++Done Drools++++++++++++++++++++++++++" >>/home/igstk/IGSTK/Cdash_Scripts_logs/logs/buildLog-`date +%m%d%Y`.log
echo "---------- Killing the startx session ---------"
/usr/bin/killall xinit
