#!/bin/bash

TOTAL=10000

echo "Iniciando stress test com $TOTAL threads simultâneas..."

start_time=$(date +%s.%N)

for i in $(seq 1 $TOTAL); do
    (
        echo "Thread test $i" | ./client 127.0.0.1 5000 > /dev/null
    ) &
done

wait

end_time=$(date +%s.%N)

echo "Stress test finalizado!"

echo "Tempo total: $(echo "$end_time - $start_time" | bc) segundos"


