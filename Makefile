build:
	mvn clean install

clean:
	mvn clean

rundemo:
	mpirun -n 4 java -cp target/PSGDSVM-1.0-SNAPSHOT-jar-with-dependencies.jar edu.iu.psgd.Program -dataset ijcnn1 -itr 100 -alpha 0.1 -features 22 -trainingSamples 35000 -split -ratio 0.99 -threads 1 -workers 4 -logPath /home/vibhatha/github/PSGDSVM/results/results.csv

t2j:
	scp target/PSGDSVM-1.0-SNAPSHOT-jar-with-dependencies.jar vlabeyko@juliet.futuresystems.org:/N/u/vlabeyko/github/PSGDSVM/target/
