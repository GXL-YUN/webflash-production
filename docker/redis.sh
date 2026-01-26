cat > start-redis-cluster.sh << 'EOF'
#!/bin/bash

echo "Starting Redis Master..."
docker run -d --name redis-master -p 6379:6379 redis:7-alpine redis-server --requirepass masterpassword

sleep 3

echo "Starting Redis Slave 1..."
docker run -d --name redis-slave1 -p 6380:6379 redis:7-alpine redis-server --slaveof $(docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' redis-master) 6379 --masterauth masterpassword

echo "Starting Redis Slave 2..."
docker run -d --name redis-slave2 -p 6381:6379 redis:7-alpine redis-server --slaveof $(docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' redis-master) 6379 --masterauth masterpassword

echo "Redis cluster started!"
echo "Master: 127.0.0.1:6379 (password: masterpassword)"
echo "Slave1: 127.0.0.1:6380"
echo "Slave2: 127.0.0.1:6381"
EOF

chmod +x start-redis-cluster.sh
./start-redis-cluster.sh