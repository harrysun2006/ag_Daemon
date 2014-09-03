#!/bin/sh
# CLASSPATH="$JAVA_HOME"/lib/
JAVA_OPTS="-Dfile.encoding=UTF8"
JAVA_HOME="D:/Servers/jdk1.5.0_07"
_RUNJAVA="$JAVA_HOME"/bin/java
AMAXGS_HOME=`pwd`
CLASSES="$AMAXGS_HOME"/bin
LIB="$AMAXGS_HOME"/lib
MAINCLASS=com.amaxgs.daemon.Agloco
MAINARGS=$1
CLASSPATH=.:"$CLASSES":"$LIB"/activation.jar:"$LIB"/activemq.jar:"$LIB"/antlr.jar:"$LIB"/asm-2.2.jar:"$LIB"/cglib.jar:"$LIB"/commons-beanutils.jar:"$LIB"/commons-collections.jar:"$LIB"/commons-configuration.jar:"$LIB"/commons-dbcp.jar:"$LIB"/commons-digester.jar:"$LIB"/commons-lang.jar:"$LIB"/commons-logging.jar:"$LIB"/commons-pool.jar:"$LIB"/concurrent.jar:"$LIB"/counter-ejb.jar:"$LIB"/dom4j.jar:"$LIB"/easyconf.jar:"$LIB"/groovy-1.0-JSR-06.jar:"$LIB"/groovy-starter.jar:"$LIB"/hibernate3.jar:"$LIB"/j2ee-management.jar:"$LIB"/javassist.jar:"$LIB"/javax.servlet.jar:"$LIB"/javax.servlet.jsp.jar:"$LIB"/jcr.jar:"$LIB"/jgroups-all.jar:"$LIB"/jms.jar:"$LIB"/jta.jar:"$LIB"/lock-ejb.jar:"$LIB"/log4j.jar:"$LIB"/lucene.jar:"$LIB"/mail-ejb.jar:"$LIB"/mail.jar:"$LIB"/mysql-connector-java-3.1.12-bin.jar:"$LIB"/naming-factory-dbcp.jar:"$LIB"/naming-factory.jar:"$LIB"/naming-resources.jar:"$LIB"/oscache-2.3.2.jar:"$LIB"/portal-ejb.jar:"$LIB"/portal-kernel.jar:"$LIB"/portlet.jar:"$LIB"/quartz.jar:"$LIB"/spring.jar:"$LIB"/struts.jar:"$LIB"/trove.jar:"$LIB"/util-java.jar:"$LIB"/velocity.jar:
echo "Using CLASSPATH:  $CLASSPATH"
echo "Using MAINCLASS:  $MAINCLASS"
echo "Using JAVA:       $_RUNJAVA"
echo "Using JAVA_OPTS:  $JAVA_OPTS"
DAEMON_OUT="$AMAXGS_HOME"/daemon.`date +%Y%m%d`.out
touch "$DAEMON_OUT"
$_RUNJAVA $JAVA_OPTS -classpath $CLASSPATH $MAINCLASS $MAINARGS >> "$DAEMON_OUT" 2>&1 &
