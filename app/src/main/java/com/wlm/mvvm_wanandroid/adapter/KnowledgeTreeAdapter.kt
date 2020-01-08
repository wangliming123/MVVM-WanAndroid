package com.wlm.mvvm_wanandroid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.common.Knowledge
import com.wlm.mvvm_wanandroid.startKtxActivity
import com.wlm.mvvm_wanandroid.ui.activity.KnowledgeActivity

class KnowledgeTreeAdapter : PagedListAdapter<Knowledge, KnowledgeTreeViewHolder>(
    diffCallback
) {


    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Knowledge>() {
            override fun areItemsTheSame(oldItem: Knowledge, newItem: Knowledge): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Knowledge, newItem: Knowledge): Boolean =
                oldItem == newItem

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KnowledgeTreeViewHolder {
        return KnowledgeTreeViewHolder(parent)
    }

    override fun onBindViewHolder(holder: KnowledgeTreeViewHolder, position: Int) {
        val knowledge = getItem(position)
        holder.bind(knowledge)
        holder.itemView.setOnClickListener { view ->
            knowledge?.let {
                view.context.startKtxActivity<KnowledgeActivity>(value = KnowledgeActivity.KEY_KNOWLEDGE to it)
            }

        }
    }
}

class KnowledgeTreeViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_knowledge, parent, false)
) {
    private val tvTitle = itemView.findViewById<TextView>(R.id.knowledge_title)
    private val tvChildren = itemView.findViewById<TextView>(R.id.knowledge_children)

    fun bind(knowledge: Knowledge?) {
        knowledge?.run {
            tvTitle.text = name
            tvChildren.text = children.joinToString("    "){
                it.name
            }
        }
    }
}