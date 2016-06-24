CP=`/home/biar/hadoop/bin/hadoop classpath`
BIN = bin/
SRC = src/

all:
	javac -classpath $(CP) -d $(BIN) $(SRC)MiddleNode.java 
	jar -cvf MiddleNode.jar -C $(BIN) .

copy:
	hadoop fs -rm -f -skipTrash /graph/middle.txt
	hadoop fs -put in.txt /graph/middle.txt

run:
	hadoop fs -rm -f -r -skipTrash /outMiddle
	time yarn jar MiddleNode.jar MiddleNode -r 1 /graph/middle.txt /outMiddle
	

.phony: clean

clean:
	rm -rf $(BIN)* *.jar
