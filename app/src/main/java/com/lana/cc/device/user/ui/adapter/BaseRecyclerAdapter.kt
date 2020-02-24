package com.lana.cc.device.user.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

//抽象类，万能的RecyclerView的适配器 使用DataBinding构建
abstract class BaseRecyclerAdapter<Bean, Binding : ViewDataBinding>
constructor(
    private val layoutRes: Int,
    private val onCellClick: (Bean) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var baseList: List<Bean> = emptyList()

    class BaseSimpleViewHolder<Binding : ViewDataBinding>(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val binding: Binding? by lazy {
            DataBindingUtil.bind<Binding>(itemView)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return BaseSimpleViewHolder<Binding>(
            LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        )
    }

    override fun getItemCount() = baseList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as BaseSimpleViewHolder<Binding>
        holder.binding!!.root.setOnClickListener {
            onCellClick(baseList[position])
        }
        bindData(holder.binding!!, position)
    }

    abstract fun bindData(binding: Binding, position: Int)

    //替换列表数据的方法
    fun replaceData(newList: List<Bean>) {
        baseList = newList
        notifyDataSetChanged()
    }

}
