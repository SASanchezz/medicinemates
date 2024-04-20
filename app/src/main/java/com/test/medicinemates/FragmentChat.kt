package com.test.medicinemates

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.test.medicinemates.chatgpt.ChatGPT
import com.test.medicinemates.databinding.FragmentChatBinding
import kotlinx.coroutines.launch

class FragmentChat : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or
        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        binding.bSend.setOnClickListener {
            lifecycleScope.launch {
                sendMessage()
            }
        }

        return binding.root
    }

    private suspend fun sendMessage() {
        val messageText = binding.etMessage.text.toString().trim()
        if (messageText.isNotEmpty()) {
            // Add the user's message to the chat container
            addMessageToChat("User", messageText)

            // Clear the input field after sending the message
            binding.etMessage.text.clear()

            // Scroll to the bottom of the chat container
            binding.scrollView.post {
                binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
            }

            //Update view
            binding.root.invalidate()

            // separate coroutine to get the response from the GPT-3 model
            val response = ChatGPT.getGPTResponse(messageText)
            addMessageToChat("Assistant", response)

        }
    }

    private fun addMessageToChat(sender: String, message: String) {
        val messageText = "<b>$sender:</b> $message"
        val messageTextView = TextView(requireContext())
        messageTextView.text = Html.fromHtml(messageText, Html.FROM_HTML_MODE_LEGACY)
        messageTextView.textSize = 18f

        binding.chatContainer.addView(messageTextView)
    }
}
