CP=`/home/biar/hadoop/bin/hadoop classpath`
BIN = bin/
SRC = src/

all:
	javac -classpath $(CP) -d $(BIN) $(SRC)Len2Paths2.java 
	jar -cvf Len2Paths2.jar -C $(BIN) .

copy:
	hadoop fs -rm -f -skipTrash /graph/facebook.txt
	hadoop fs -put facebook.txt /graph/facebook.txt

run:
	hadoop fs -rm -f -r -skipTrash /out /interm
	time yarn jar Len2Paths2.jar Len2Paths2 -r 1 /graph/facebook.txt /out

.phony: clean

clean:
	rm -rf $(BIN)* *.jar
