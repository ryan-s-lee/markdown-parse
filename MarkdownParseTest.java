import static org.junit.Assert.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.junit.*;

public class MarkdownParseTest {
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
        Path fileName = Path.of("break0.md");
        String contents = Files.readString(fileName);
        assertEquals(List.of("https://something.com", "some-page.html"), MarkdownParse.getLinks(contents));
    }

    @Test
    public void testBreakFile1() throws IOException {
        Path fileName = Path.of("break1.md");
        String contents = Files.readString(fileName);
        assertEquals(List.of("https://something.com", "some-page.html"), MarkdownParse.getLinks(contents));
    }

    @Test
    public void testBreakFile2() throws IOException {
        Path fileName = Path.of("break2.md");
        String contents = Files.readString(fileName);
        assertEquals(List.of("https://something().com"), MarkdownParse.getLinks(contents));
    }

    @Test
    public void testBreakFile3() throws IOException {
        Path fileName = Path.of("break3.md");
        String contents = Files.readString(fileName);
        assertEquals(Collections.EMPTY_LIST, MarkdownParse.getLinks(contents));
    }

    @Test
    public void testBreakFile5() throws IOException {
        Path fileName = Path.of("break5.md");
        String contents = Files.readString(fileName);
        assertEquals(List.of(), MarkdownParse.getLinks(contents));
    }

    @Test
    public void testBreakFile11() throws IOException {
        Path fileName = Path.of("break11.md");
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
        Path fileName = Path.of("snippet1.md");
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
        Path fileName = Path.of("snippet2.md");
        String contents = Files.readString(fileName);
        assertEquals(
                expected,
                MarkdownParse.getLinks(contents));
    }

    @Test
    public void testSnippet3() throws IOException {
        List<String> expected = List.of(
                "https://www.twitter.com",
                "https://ucsd-cse15l-wi22.github.io",
                "https://cse.ucsd.edu");
        Path fileName = Path.of("snippet3.md");
        String contents = Files.readString(fileName);
        assertEquals(
                expected,
                MarkdownParse.getLinks(contents));
    }
}
