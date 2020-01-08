package com.wlm.mvvm_wanandroid.adapter

import androidx.recyclerview.widget.RecyclerView

/**
 * 有header的recyclerView的AdapterDataObserver代理（若不添加该代理，则会出现闪屏）
 */
class AdapterDataObserverProxy(
    private val observer: RecyclerView.AdapterDataObserver,
    private val headerCount: Int
) : RecyclerView.AdapterDataObserver() {
    override fun onChanged() {
        observer.onChanged()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        observer.onItemRangeRemoved(positionStart + headerCount, itemCount)
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        super.onItemRangeMoved(fromPosition + headerCount, toPosition + headerCount, itemCount)
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(positionStart + headerCount, itemCount)
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        super.onItemRangeChanged(positionStart + headerCount, itemCount)
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        super.onItemRangeChanged(positionStart + headerCount, itemCount, payload)
    }
}