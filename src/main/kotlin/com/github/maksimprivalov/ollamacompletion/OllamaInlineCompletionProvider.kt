package com.github.maksimprivalov.ollamacompletion

import com.intellij.codeInsight.inline.completion.InlineCompletionElement
import com.intellij.codeInsight.inline.completion.InlineCompletionEvent
import com.intellij.codeInsight.inline.completion.InlineCompletionProvider
import com.intellij.codeInsight.inline.completion.InlineCompletionRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ollamacompletion.ollama.OllamaClient
import java.lang.String

class OllamaInlineCompletionProvider : InlineCompletionProvider {
    override fun isEnabled(event: InlineCompletionEvent): Boolean = true


    override suspend fun getProposals(request: InlineCompletionRequest): Flow<InlineCompletionElement> = flow {
        val editor = request.editor
        val document = editor.document
        val offset = editor.caretModel.offset

        val beforeCursor = document.text.substring(0, offset)
//        val afterCursor = document.text.substring(offset) Wanted to write a normal prompt but failed

        val prompt = """
        $beforeCursor
        """.trimIndent()

        val response = withContext(Dispatchers.IO) {
            OllamaClient.getCompletion(prompt)
        }


        emit(InlineCompletionElement(response))

    }

}