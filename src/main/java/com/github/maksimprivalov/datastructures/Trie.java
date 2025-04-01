package com.github.maksimprivalov.datastructures;

public class Trie {
    private TrieNode root;
    private final LRUCache lruCache;

    public Trie() {
        root = new TrieNode();
        lruCache = new LRUCache(20, this::delete); // define max capacity and Consumer<String>
    }

    // Inserting new node with the AI completion answer merged with the prompt
    // Not bad because in collaboration with LRU we will have maks 10 big strings in our heap (.intern())
    public void insert(String line, String content) {
        TrieNode current = root;

        for (char c: line.toCharArray()) {
            current = current.getChildren().computeIfAbsent(c, n -> {
                TrieNode node = new TrieNode();
                node.setContent(content.intern());
                return node;
            });
        }
        current.setEndOfWord(true);
        lruCache.put(line, true);
    }

    public boolean find(String line) {
        TrieNode current = root;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            TrieNode node = current.getChildren().get(ch);
            if (node == null) {
                return false;
            }
            current = node;
        }
        return current.isEndOfWord();
    }

    public String getContent(String prompt){
        TrieNode current = root;
        for (int i = 0; i < prompt.length(); i++) {
            char ch = prompt.charAt(i);

            // So basically working with previous prompt.
            // In the situation when we have 2+ nods in our Trie branch we need to check weather
            // the next character of the prompt is the next char. after the node on the branch,
            // if not, return the content (AI generated response we stored in previous iterations).
            // Note: we also should check the prefix after.
            if(current.isEndOfWord() && current.getChildren().get(ch) == null){
                return current.getContent();
            }
            TrieNode node = current.getChildren().get(ch);

            if (node == null) {
                return null;
            }
            current = node;
        }

        // In this case we are still on the branch,
        // prompt ends here -> we can use the response stored at this node!
        return current.getContent();
    }
    public void clear() {
        root = new TrieNode(); // make root pointer null so JVM will delete all the branches.
        lruCache.clear();
    }
    public void delete(String line) {
        delete(root, line, 0);
        lruCache.remove(line); // remove from LRU Cache too
    }

    private boolean delete(TrieNode current, String line, int index) {
        if (index == line.length()) {
            if (!current.isEndOfWord()) {
                return false;
            }
            current.setEndOfWord(false);
            return current.getChildren().isEmpty();
        }
        char ch = line.charAt(index);
        TrieNode node = current.getChildren().get(ch);
        if (node == null) {
            return false;
        }
        boolean shouldDeleteCurrentNode = delete(node, line, index + 1) && !node.isEndOfWord();

        if (shouldDeleteCurrentNode) {
            current.getChildren().remove(ch);
            return current.getChildren().isEmpty();
        }
        return false;
    }


}
