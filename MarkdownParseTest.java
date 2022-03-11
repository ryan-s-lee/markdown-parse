import static org.junit.Assert.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.junit.*;

public class MarkdownParseTest {
    String myPath = "C:\\Users\\ryans\\OneDrive\\Desktop\\College\\2021-Q2\\Assignments\\cse15l\\markdown-parse\\";
    @Test
    public void addition() {
        assertEquals(2, 1 + 1);
    }

    @Test
    public void vimTest() {
        assertEquals(true, true);
    }
    @Test
    public void testBreakFile0() throws IOException {
        Path fileName = Path.of(myPath + "break0.md");
        String contents = Files.readString(fileName);
        assertEquals(List.of("https://something.com", "some-page.html"), MarkdownParse.getLinks(contents));
    }

    @Test
    public void testBreakFile1() throws IOException {
        Path fileName = Path.of(myPath + "break1.md");
        String contents = Files.readString(fileName);
        assertEquals(List.of("https://something.com", "some-page.html"), MarkdownParse.getLinks(contents));
    }

    @Test
    public void testBreakFile2() throws IOException {
        Path fileName = Path.of(myPath + "break2.md");
        String contents = Files.readString(fileName);
        assertEquals(List.of("https://something().com"), MarkdownParse.getLinks(contents));
    }

    @Test
    public void testBreakFile3() throws IOException {
        Path fileName = Path.of(myPath + "break3.md");
        String contents = Files.readString(fileName);
        assertEquals(Collections.EMPTY_LIST, MarkdownParse.getLinks(contents));
    }

    @Test
    public void testBreakFile5() throws IOException {
        Path fileName = Path.of(myPath + "break5.md");
        String contents = Files.readString(fileName);
        assertEquals(List.of(), MarkdownParse.getLinks(contents));
    }

    @Test
    public void testBreakFile11() throws IOException {
        Path fileName = Path.of(myPath + "break11.md");
        String contents = Files.readString(fileName);
        assertEquals(List.of(
                "https://docs.microsoft.com/en-us/previous-versions/windows/internet-explorer/ie-developer/platform-apis/aa752574(v=vs.85)?redirectedfrom=MSDN"),
                MarkdownParse.getLinks(contents));
    }

    @Test
    public void testSnippet1() throws IOException {
        List<String> expected = List.of(
                "`google.com",
                "google.com",
                "ucsd.edu");
        Path fileName = Path.of(myPath + "snippet1.md");
        String contents = Files.readString(fileName);
        assertEquals(
                expected,
                MarkdownParse.getLinks(contents));
    }

    @Test
    public void testSnippet2() throws IOException {
        List<String> expected = List.of(
                "a.com",
                "a.com(())",
                "example.com");
        Path fileName = Path.of(myPath + "snippet2.md");
        String contents = Files.readString(fileName);
        assertEquals(
                expected,
                MarkdownParse.getLinks(contents));
    }

    @Test
    public void testSnippet3() throws IOException {
        List<String> expected = List.of(
                "https://ucsd-cse15l-wi22.github.io");
        Path fileName = Path.of(myPath + "snippet3.md");
        String contents = Files.readString(fileName);
        assertEquals(
                expected,
                MarkdownParse.getLinks(contents));
    }
}
