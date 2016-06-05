CP  = `hadoop classpath`
BIN = bin/
SRC = src/

compile:
	javac -classpath $(CP) -d $(BIN) $(SRC)LocalCC.java -Xlint:unchecked
	jar -cf LocalCC.jar -C $(BIN) .

copy:
	hadoop fs -rm -f -skipTrash /graph/facebook.txt
	hadoop fs -put facebook.txt /graph/facebook.txt
	hadoop fs -rm -f -skipTrash /graph/small.txt
	hadoop fs -put small.txt /graph/small.txt

run:
	hadoop fs -rm -f -r -skipTrash /cc-out /interm
	time yarn jar LocalCC.jar LocalCC -r 1 /graph/small.txt /cc-out
	rm -f cc-out.txt
	hadoop fs -get /cc-out/part-r-00000 cc-out.txt

run-fb:
	hadoop fs -rm -f -r -skipTrash /cc-out-fb /interm
	time yarn jar LocalCC.jar LocalCC -r 1 /graph/facebook.txt /cc-out-fb
	rm -f cc-out-fb.txt
	hadoop fs -get /cc-out-fb/part-r-00000 cc-out-fb.txt

.phony: clean

clean:
	rm -rf $(BIN)* *.jar
