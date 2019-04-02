parallelism=$1
xmsFull=32768
xmxFull=98304
xms=$((${xmsFull}/${parallelism}))
xmx=$((${xmxFull}/${parallelism}))
itr=500
echo mpirun -n ${parallelism} --hostfile nodes${parallelism} java -Xms${xms}m -Xmx${xmx}m -cp target/PSGDSVM-1.0-SNAPSHOT-jar-with-dependencies.jar edu.iu.psgd.Program -dataset epsilon -itr ${itr} -alpha 0.1 -features 2000 -trainingSamples 400000 -split -ratio 0.80 -threads 1 -logPath /N/u/vlabeyko/github/PSGDSVM/results/results.csv -workers ${parallelism}

mpirun -n ${parallelism} --hostfile nodes${parallelism} java -Xms${xms}m -Xmx${xmx}m -cp target/PSGDSVM-1.0-SNAPSHOT-jar-with-dependencies.jar edu.iu.psgd.Program -dataset epsilon -itr ${itr} -alpha 0.1 -features 2000 -trainingSamples 400000 -split -ratio 0.80 -threads 1 -logPath /N/u/vlabeyko/github/PSGDSVM/results/results.csv -workers ${parallelism}
