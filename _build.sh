#delete old build jar's
rm -rf ./k8s/**.jar

#build auth-service
mvn install -DskipTests && mvn compile -pl auth-service
cp ./auth-service/target/auth-0.0.1-SNAPSHOT.jar ./k8s/auth-0.0.1-SNAPSHOT.jar

#build discovery-service
mvn install -DskipTests && mvn compile -pl discovery-service
cp ./discovery-service/target/discovery-0.0.1-SNAPSHOT.jar ./k8s/discovery-0.0.1-SNAPSHOT.jar

#build gateway-service
mvn install -DskipTests && mvn compile -pl gateway-service
cp ./gateway-service/target/gateway-0.0.1-SNAPSHOT.jar ./k8s/gateway-0.0.1-SNAPSHOT.jar

#build payment-service
mvn install -DskipTests && mvn compile -pl payment-service
cp ./payment-service/target/payment-0.0.1-SNAPSHOT.jar ./k8s/payment-0.0.1-SNAPSHOT.jar

#build product-service
mvn install -DskipTests && mvn compile -pl product-service
cp ./product-service/target/product-0.0.1-SNAPSHOT.jar ./k8s/product-0.0.1-SNAPSHOT.jar