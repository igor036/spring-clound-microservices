#delete old build jar's
rm -rf docker/**.jar

#build auth-service
mvn install -DskipTests && mvn compile -pl auth-service
cp ./auth-service/target/auth-0.0.1-SNAPSHOT.jar docker/auth-0.0.1-SNAPSHOT.jar

#build discovery-service
mvn install -DskipTests && mvn compile -pl discovery-service
cp ./discovery-service/target/discovery-0.0.1-SNAPSHOT.jar docker/discovery-0.0.1-SNAPSHOT.jar

#build gateway-service
mvn install -DskipTests && mvn compile -pl gateway-service
cp ./gateway-service/target/gateway-0.0.1-SNAPSHOT.jar docker/gateway-0.0.1-SNAPSHOT.jar

#build payment-service
mvn install -DskipTests && mvn compile -pl payment-service
cp ./payment-service/target/payment-0.0.1-SNAPSHOT.jar docker/payment-0.0.1-SNAPSHOT.jar

#build product-service
mvn install -DskipTests && mvn compile -pl product-service
cp ./product-service/target/product-0.0.1-SNAPSHOT.jar docker/product-0.0.1-SNAPSHOT.jar

#clear kube file's 
rm -rf k8s/**.yaml

#build docker image's
eval $(minikube docker-env)
docker-compose -f docker/docker-compose.yml build

#build kube file's
kompose convert -f ./docker/docker-compose.yml -o ./k8s/kube.yaml

#delete old build jar's
rm -rf docker/**.jar