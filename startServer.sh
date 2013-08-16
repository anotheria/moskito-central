#!/bin/sh
java -DlocalRmiRegistryPort=9249 -DskipCentralRegistry=true -cp target/moskito-central-2.2.3-SNAPSHOT-jar-with-dependencies.jar org.moskito.central.endpoints.rmi.generated.RMIEndpointServer