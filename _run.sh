./_build.sh
cd ./k8s/
docker-compose -f docker-compose.yml up --build -d
rm -rf **.jar
cd ../