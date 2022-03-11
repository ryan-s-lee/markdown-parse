CLASSPATH = "lib/*;."

test: 
	javac -cp lib/*:. *.java
	java -cp lib/*:. org.junit.runner.JUnitCore MarkdownParseTest 

testwindows: 
	javac -cp "lib/*;." *.java
	java -cp "lib/*;." org.junit.runner.JUnitCore MarkdownParseTest 

TryCommonMark.class: TryCommonMark.java
	javac -g -cp $(CLASSPATH) TryCommonMark.java

debugwindows:
	javac -cp "lib/*;." *.java
	jdb -classpath "lib/*;." org.junit.runner.JUnitCore MarkdownParseTest