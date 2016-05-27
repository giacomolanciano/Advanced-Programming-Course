CP=`/home/biar/hadoop/bin/hadoop classpath`
BIN = bin/
SRC = src/

all:
	javac -classpath $(CP) -d $(BIN) $(SRC)InvertEdges.java 
	jar -cvf InvertEdges.jar -C $(BIN) .

copy:
	hadoop fs -rm -f -skipTrash /graph/in.txt
	hadoop fs -put in.txt /graph/in.txt

run:
	hadoop fs -rm -f -r -skipTrash /outInvert
	time yarn jar InvertEdges.jar InvertEdges -r 1 /graph/in.txt /outInvert
	

.phony: clean

clean:
	rm -rf $(BIN)* *.jar
