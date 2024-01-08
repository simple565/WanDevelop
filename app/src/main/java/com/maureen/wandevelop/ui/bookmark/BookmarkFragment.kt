package com.maureen.wandevelop.ui.bookmark

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.maureen.wandevelop.R

class BookmarkFragment : Fragment() {

    private lateinit var knowledgeViewModel: BookmarkViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        knowledgeViewModel =
                ViewModelProvider(this).get(BookmarkViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_knowledge, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        knowledgeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAG", "onDestroy: KnowledgeFragment")
    }
}