test: 
	javac -cp lib/*:. *.java
	java -cp lib/*:. org.junit.runner.JUnitCore MarkdownParseTest 

