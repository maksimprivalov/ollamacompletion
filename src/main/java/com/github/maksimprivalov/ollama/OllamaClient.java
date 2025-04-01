package com.github.maksimprivalov.ollama;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.ollama.OllamaChatModel;


public class OllamaClient {
    private static final boolean MOCK_MODE = false;

    private static final OllamaChatModel model = OllamaChatModel.builder()
            .baseUrl("http://localhost:11434")
            .modelName("starcoder2:3b")
            .temperature(0.2)
            .build();

    public static String getCompletion(String prompt) {
        if (MOCK_MODE) return "// mock completion";

        if (prompt == null || prompt.trim().isEmpty()) {
            return "// empty prompt";
        }

        try {
            return model.generate(new UserMessage(prompt)).content().text();
        } catch (Exception e) {
            return "// Exception Ollama: " + e.getClass().getSimpleName() + " - " + e.getMessage();
        }
    }
}
