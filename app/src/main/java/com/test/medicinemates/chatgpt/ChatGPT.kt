package com.test.medicinemates.chatgpt

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.test.medicinemates.utilis.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatGPT {
    companion object {
        @OptIn(BetaOpenAI::class)
        suspend fun getGPTResponse(prompt: String): String =
            withContext(Dispatchers.IO) {
            val client = OpenAI("123")
            val messages = listOf(
                ChatMessage(
                    role = ChatRole.System,
                    content = Constants.AI_PROMPT
                ),
                ChatMessage(
                    role = ChatRole.User,
                    content = prompt
                )
            )

            val request = ChatCompletionRequest(
                model = ModelId("gpt-3.5-turbo"),
                messages = messages,
                temperature = 0.2,
                maxTokens = 150,
            )

            val response = client.chatCompletion(request)

            return@withContext response.choices.first().message?.content ?: "No response"
        }
    }

}