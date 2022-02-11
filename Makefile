test: 
	javac -cp lib/*:. *.java
	java -cp lib/*:. org.junit.runner.JUnitCore MarkdownParseTest 

testwindows: 
	javac -cp "lib/*;." *.java
	java -cp "lib/*;." org.junit.runner.JUnitCore MarkdownParseTest 