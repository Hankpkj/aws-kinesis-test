all: target/study-aws-kinesis-1.0-SNAPSHOT.jar

target/study-aws-kinesis-1.0-SNAPSHOT.jar: src/main/java/com/example/kinesis/*.java
	mvn package

clean:
	mvn clean