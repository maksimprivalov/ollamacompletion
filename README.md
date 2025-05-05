# 🧠 Ollama Inline Completion Plugin

A lightweight IntelliJ plugin for inline code completion using the `starcoder2:3b` model via Ollama.

Autocompletion systems like GitHub Copilot and IntelliJ's native suggestions are used by **millions of developers daily**. This plugin explores how a **local-first, model-driven autocompletion** can be made fast and resource-efficient through **smart caching and reuse of previous completions**.


## 💡 Core Idea
Instead of generating a new completion every time a key is pressed — which can be costly and slow — this plugin introduces an optimization strategy based on **prompt reuse**.

As the user types, the plugin takes the **entire file content up to the caret** and uses it as a prompt. Before triggering a new generation via the model, it checks whether a **previously completed prompt can be reused**.

This avoids redundant calls, reduces latency, and gives the user a much smoother coding experience without compromising completion quality.


## 🧱 Data Structures & Caching Logic

To support fast lookup and memory-efficient reuse, the plugin uses two main data structures:

- **Trie (Prefix Tree):**  
  - Stores only prompts as keys (each character as a node).
  - Each node holds a `content` field, which is the **original prompt + model response**.
  - On each keystroke, the Trie is queried to find:
    - An exact match,
    - A shorter known prefix,
    - Or a case where the current prompt is a prefix of a previously completed prompt+response.

- **LRU Cache:**  
  - Stores up to N prompt-response pairs (e.g., 15).
  - Ensures the most recently used completions remain available.
  - Controls memory usage and speeds up repeated lookups.

This combination enables **instant inline suggestions** whenever a prefix has already been computed before — even partially.


## 🚀 How to Run

1. Install [Ollama](https://ollama.com/) and pull the model:
   ```bash
   ollama pull starcoder2:3b
2. Run the plugin from your IDE!

✅ Tested on IntelliJ IDEA 2023.2.8
Compatibility with other versions not guaranteed.

## ⭐ Project Highlights
  - Instant inline completions while typing
  - Efficient reuse of previous completions
  - Smart prompt trimming via Trie-based matching
  - Minimal latency, even with large files 
  - Local-first — no cloud latency or privacy concerns

---

⚙️ The entire codebase is **thoroughly tested and commented**
