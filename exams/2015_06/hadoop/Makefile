CP=`/home/biar/hadoop/bin/hadoop classpath`
BIN = bin/
SRC = src/

all:
	javac -classpath $(CP) -d $(BIN) $(SRC)Undirected.java 
	jar -cvf Undirected.jar -C $(BIN) .

copy:
	hadoop fs -rm -f -skipTrash /graph/in.txt
	hadoop fs -put in.txt /graph/in.txt

run:
	hadoop fs -rm -f -r -skipTrash /outUndir
	time yarn jar Undirected.jar Undirected -r 1 /graph/in.txt /outUndir
	

.phony: clean

clean:
	rm -rf $(BIN)* *.jar
