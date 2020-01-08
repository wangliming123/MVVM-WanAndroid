package com.wlm.mvvm_wanandroid.base.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource

abstract class BaseDataSourceFactory<DS : ItemKeyedDataSource<Int, T>, T> : DataSource.Factory<Int, T>() {
     val sourceLivaData = MutableLiveData<DS>()

     override fun create(): DS {
         val dataSource: DS = createDataSource()
         sourceLivaData.postValue(dataSource)
         return dataSource
     }

     abstract fun createDataSource(): DS

 }