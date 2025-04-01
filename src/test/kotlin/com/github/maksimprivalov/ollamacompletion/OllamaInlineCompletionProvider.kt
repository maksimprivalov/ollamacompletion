package com.github.maksimprivalov.ollamacompletion

import com.github.maksimprivalov.datastructures.TrieSingleton
import com.github.maksimprivalov.ollama.OllamaClient
import com.intellij.codeInsight.inline.completion.*
import com.intellij.openapi.editor.CaretModel
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.Document
import io.mockk.*
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OllamaInlineCompletionProviderTest {

    private val provider = OllamaInlineCompletionProvider()

    @BeforeEach
    fun setup() {
        TrieSingleton.trie.clear()
    }
    @AfterEach
    fun teardown(){
        unmockkAll()
    }
    @Test
    fun `should return cached completion from Trie`() = runTest {
        val prompt = "fun main() {"
        val full = "fun main() {\n    println(\"Hello\")\n}"
        TrieSingleton.trie.insert(prompt, full)

        val request = createMockRequest(prompt)
        val result = provider.getProposals(request).toList()

        assertEquals(1, result.size)
        assertEquals("\n    println(\"Hello\")\n}", result[0].text)
    }

    @Test
    fun `should call OllamaClient and cache response when not in Trie`() = runTest {
        val prompt = "val x ="
        val response = " 42"

        mockkStatic("com.github.maksimprivalov.ollama.OllamaClient")

        coEvery { OllamaClient.getCompletion(prompt) } returns response

        val request = createMockRequest(prompt)
        val result = provider.getProposals(request).toList()

        assertEquals(1, result.size)
        assertEquals(" 42", result[0].text)

        val cached = TrieSingleton.trie.getContent(prompt)
        assertEquals("val x = 42", cached)
    }

    private fun createMockRequest(textBeforeCursor: String): InlineCompletionRequest {
        val caretOffset = textBeforeCursor.length

        val document = mockk<Document>()
        every { document.text } returns textBeforeCursor

        val caretModel = mockk<CaretModel>()
        every { caretModel.offset } returns caretOffset

        val editor = mockk<Editor>()
        every { editor.document } returns document
        every { editor.caretModel } returns caretModel

        val request = mockk<InlineCompletionRequest>()
        every { request.editor } returns editor

        return request
    }

}