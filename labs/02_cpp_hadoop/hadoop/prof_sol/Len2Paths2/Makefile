CP = ${HADOOP_CLASSPATH}
BIN = bin/
SRC = src/

all:
	javac -classpath $(CP) -d $(BIN) $(SRC)Len2Paths2.java
	jar -cvf Len2Paths2.jar -C $(BIN) .

run:
	hdfs dfs -rm -f -r -skipTrash /out /tmp
	time hadoop jar Len2Paths2.jar Len2Paths2 -r 1 /graph/facebook.txt /out

.phony: clean

clean:
	rm -rf $(BIN)* *.jar
