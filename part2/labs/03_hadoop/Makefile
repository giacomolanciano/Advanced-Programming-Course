CP=`/home/biar/hadoop/bin/hadoop classpath`
BIN = bin/
SRC = src/

all:
	javac -classpath $(CP) -d $(BIN) $(SRC)ClusteringCoefficient.java 
	jar -cvf ClusteringCoefficient.jar -C $(BIN) .

copy:
	#hadoop fs -rm -f -skipTrash /graph/facebook.txt
	#hadoop fs -put facebook.txt /graph/facebook.txt
	hadoop fs -rm -f -skipTrash /graph/in.txt
	hadoop fs -put facebook.txt /graph/in.txt

run:
	hadoop fs -rm -f -r -skipTrash /outClust /outClustTemp1 /outClustTemp2
	#time yarn jar ClusteringCoefficient.jar ClusteringCoefficient -r 1 /graph/facebook.txt /outClust
	time yarn jar ClusteringCoefficient.jar ClusteringCoefficient -r 1 /graph/in.txt /outClust

.phony: clean

clean:
	rm -rf $(BIN)* *.jar
