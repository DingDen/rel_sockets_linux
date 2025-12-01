#!/bin/bash

start_time=$(date +%s.%N)
for i in {1..10000}; do
    echo "Requisição $i" | ./client 127.0.0.1 5001 &
done

wait

end_time=$(date +%s.%N)
echo ""
echo "Tempo total: $(echo "$end_time - $start_time" | bc) segundos"