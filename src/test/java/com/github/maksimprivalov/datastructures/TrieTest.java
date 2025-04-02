package com.github.maksimprivalov.datastructures;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TrieTest {

    private Trie trie;
    private static final String CONTENT = "int main(){\n  println(\"Hello\")\n}"; // Prompt + AI response
    @BeforeEach
    void setup() {
        trie = new Trie();
        trie.insert("int main()", CONTENT);
    }

    @Test
    void shouldReturnContentWhenExactPromptExists() {
        String prompt = "int main()";

        String result = trie.getContent(prompt);
        assertEquals(CONTENT, result);
    }

    @Test
    void shouldReturnContentWhenPromptIsPrefix() {
        String prompt = "int main()";
        trie.insert(prompt, CONTENT);

        String result = trie.getContent("int m");
        assertEquals(result, CONTENT);
    }

    @Test
    void shouldReturnNullWhenPromptIsNotAPrefix() {
        String result = trie.getContent("while(true){ break");
        assertNull(result);
    }

    @Test
    void shouldReturnContentWhenPromptIsPrefixOfTheContent() {
        String result = trie.getContent("int main(){\n  pri");
        assertEquals(result, CONTENT);
    }
    @Test
    void shouldReturnFalseWhenPromptIsPrefixOfTheBranchButNotContent() {
        String prompt = "int main(){\n  print(";
        String result = trie.getContent(prompt);
        // not println, but matches Trie branch int main()
        assertFalse(result.startsWith(prompt));
    }
    @Test
    void shouldReturnContentWhenPromptSplitsBranch() {
        String result = trie.getContent("int make()");
        assertNull(result);
    }

}