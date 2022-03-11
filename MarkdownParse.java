
// File reading code from https://howtodoinjava.com/java/io/java-read-file-to-string-examples/
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;

public class MarkdownParse {
    public static ArrayList<String> getLinks(String markdown) {
        MarkdownParser parser = new MarkdownParser(markdown);
        return (parser.getLinks());
    }

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of(args[0]);
        String contents = Files.readString(fileName);
        ArrayList<String> links = getLinks(contents);
        System.out.println(links);
    }
}

class MarkdownParser {
    ArrayList<String> obtainedLinks;
    HashSet<Integer> states;
    String markdown;
    StringBuilder linkBuilder;
    int index;
    ArrayList<Integer> bracketIndices;

    // States
    static final int ST_JUST_NEWLINED = 0;
    static final int ST_READING_TITLE = 1;
    static final int ST_READING_LINK = 2;
    static final int ST_TAGGED_AS_IMG = 3;
    static final int ST_CODETOGGLED = 4;

    public MarkdownParser(String markdown) {
        states = new HashSet<>();
        this.markdown = markdown;
        linkBuilder = new StringBuilder();
        index = 0;
        bracketIndices = new ArrayList<>();
        obtainedLinks = new ArrayList<String>();
    }

    public ArrayList<String> getLinks() {
        // reset
        obtainedLinks.clear();
        states.clear();
        linkBuilder.setLength(0);
        index = 0;
        bracketIndices.clear();
        while (index < markdown.length()) {
            switch (markdown.charAt(index)) {
                case '\\':
                    escapeBehavior();
                    break;
                // case 10:
                //     newlineBehavior();
                //     break;
                case ']':
                    closeBracketBehavior();
                    break;
                case '[':
                    openBracketBehavior();
                    break;
                case '(':
                    openParenBehavior(); // partally covered by closedBracketBehavior
                    break;
                case ')':
                    closeParenBehavior();
                    break;
                // case '!':
                // exclamationBehavior();
                // break;
                case '`':
                    backtickBehavior();
                    break;
                default:
                    defaultBehavior();
            }
            ++index;
        }
        return obtainedLinks;
    }

    // private void newlineBehavior() {
    //     // we will assume this is windows, such that all newlines are crlf
    //     if (states.contains(ST_JUST_NEWLINED)) {
    //         // We've carriage returned twice so abort. 
    //         abort();
    //     } else {
    //         states.add(ST_JUST_NEWLINED);
    //     }
    //     // skip next character because it is probably an lf (only true for windows)
    //     index++;
    // }

    private void abort() {
        states.remove(ST_READING_LINK);
        states.remove(ST_READING_TITLE);
        states.remove(ST_TAGGED_AS_IMG);
        linkBuilder.setLength(0);
        bracketIndices.clear();
    }

    private void backtickBehavior() {
        states.remove(ST_JUST_NEWLINED);
        if (states.contains(ST_CODETOGGLED)) {
            states.remove(ST_CODETOGGLED);
        } else {
            states.add(ST_CODETOGGLED);
        }
        if (states.contains(ST_READING_LINK)) {
            linkBuilder.append(markdown.charAt(index));
        }
    }

    private void escapeBehavior() {
        states.remove(ST_JUST_NEWLINED);
        if (index + 1 < markdown.length()
                && markdown.charAt(index + 1) == ']') {
            // Skip the backtick and run default on the character
            ++index;
        }
        defaultBehavior();
        return;
    }

    private void openBracketBehavior() {
        states.remove(ST_JUST_NEWLINED);
        // if we're reading a link, just add the text to the link.
        if (states.contains(ST_READING_LINK)) {
            linkBuilder.append(markdown.charAt(index));
            return;
        }
        // if we're reading text that's not in a link, don't do anything.
        if (states.contains(ST_CODETOGGLED)) {
            return;
        }
        if (index != 0 && markdown.charAt(index - 1) == '!') {
            states.add(ST_TAGGED_AS_IMG);
        }
        states.add(ST_READING_TITLE);
        bracketIndices.add(Integer.valueOf(index));
    }

    private void closeBracketBehavior() {
        states.remove(ST_JUST_NEWLINED);
        // if we're reading a link, just add the text to the link.
        if (states.contains(ST_READING_LINK)) {
            linkBuilder.append(markdown.charAt(index));
            return;
        }
        // if we're reading text that's not in a link, don't do anything.
        if (states.contains(ST_CODETOGGLED)) {
            return;
        }
        if (states.contains(ST_READING_TITLE)) {
            if (bracketIndices.size() == 1) {
                bracketIndices.remove(0);
                // Link will only be considered if a parenthesis directly
                // follows the closing bracket.
                if (index + 1 == markdown.length()) {
                    // reading a link requires at least two characters
                    // after the closing bracket.
                    abort();
                    return;
                }
                if (markdown.charAt(index + 1) == '(') {
                    states.remove(ST_READING_TITLE);
                    states.add(ST_READING_LINK);
                    bracketIndices.add(index);
                    ++index;
                } else {
                    abort();
                }
            } else {
                // remove the last element
                bracketIndices.remove(bracketIndices.size() - 1);
            }
        }
    }

    private void openParenBehavior() {
        // NOTE: closeParenBehavior is the behavior that starts link reading.
        // Reaching this method should be the result of a nested parenthesis
        // or a non-link parenthesis.
        states.remove(ST_JUST_NEWLINED);
        if (states.contains(ST_READING_LINK)) {
            if (bracketIndices.size() == 0) {
                throw new IllegalStateException();
            }
            bracketIndices.add(Integer.valueOf(index));
            linkBuilder.append(markdown.charAt(index));
        }
    }

    private void closeParenBehavior() {
        // TODO
        states.remove(ST_JUST_NEWLINED);
        if (states.contains(ST_READING_LINK)) {
            if (bracketIndices.size() == 1) {
                // link should be completed.
                if (!states.contains(ST_TAGGED_AS_IMG)) {
                    obtainedLinks.add(linkBuilder.toString());
                }
                abort();
            } else {
                bracketIndices.remove(bracketIndices.size() - 1);
                linkBuilder.append(markdown.charAt(index));
            }
        }
    }

    // private void exclamationBehavior() {
    // // TODO
    // states.remove(ST_JUST_NEWLINED);
    // if (states.contains(ST_READING_LINK) || states.contains(ST_READING_TITLE)) {
    // return;
    // }
    // if ()
    // }

    private void defaultBehavior() {
        // Since this is not a newline character we have to unnewline it
        states.remove(ST_JUST_NEWLINED);
        if (states.contains(ST_READING_LINK)) {
            linkBuilder.append(markdown.charAt(index));
        }
    }
}