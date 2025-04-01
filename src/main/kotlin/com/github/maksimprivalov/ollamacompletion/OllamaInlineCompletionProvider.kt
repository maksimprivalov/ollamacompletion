package com.github.maksimprivalov.ollamacompletion

import com.github.maksimprivalov.datastructures.TrieSingleton
import com.intellij.codeInsight.inline.completion.InlineCompletionElement
import com.intellij.codeInsight.inline.completion.InlineCompletionEvent
import com.intellij.codeInsight.inline.completion.InlineCompletionProvider
import com.intellij.codeInsight.inline.completion.InlineCompletionRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import com.github.maksimprivalov.ollama.OllamaClient

class OllamaInlineCompletionProvider : InlineCompletionProvider {
    override fun isEnabled(event: InlineCompletionEvent): Boolean = true


    override suspend fun getProposals(request: InlineCompletionRequest): Flow<InlineCompletionElement> = flow {
        val editor = request.editor

        val beforeCursor = editor.document.text.substring(0, editor.caretModel.offset)
        val prompt = beforeCursor.trimIndent()

        if (prompt.isBlank()) return@flow

        // In case when the prompt is the suffix of existing response or the
        val content = TrieSingleton.trie.getContent(prompt)
        if (content != null){
            val withOutSuffix = content.removeSuffix(prompt)
            // If prompt was the suffix of existing response (mean full code (old prompt + response!))
            if(withOutSuffix != content){
                emit(InlineCompletionElement(withOutSuffix))
                return@flow
            }
        }

        // If does not exist - request ollama
        val response = withContext(Dispatchers.IO) {
            OllamaClient.getCompletion(prompt)
        }

        TrieSingleton.trie.insert(prompt, prompt + response)
        emit(InlineCompletionElement(response))
        return@flow
    }

}