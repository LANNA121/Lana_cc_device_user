package com.lana.cc.device.user.ui.fragment.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.CardTestBinding
import com.lana.cc.device.user.manager.sharedpref.SharedPrefModel
import com.lana.cc.device.user.model.api.search.SearchKeyConclusion

class TestPagerAdapter(
    private var pagerList: List<SearchKeyConclusion>,
    private var onAnswerCorrect: ((CardTestBinding?, Int, Int) -> Unit)? = null,
    private var onAnswerError: ((Int) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.card_test, parent, false)
        return TestPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = pagerList[position]
        (holder as TestPagerViewHolder).pagerBinding?.testCard = item
        holder.pagerBinding?.tickView?.isChecked = item.isCorrect
        holder.pagerBinding?.tickView?.visibility =
            if (item.isCorrect) View.VISIBLE else View.INVISIBLE
        holder.pagerBinding?.tvAnswer?.visibility =
            if (item.isCorrect) View.VISIBLE else View.INVISIBLE
        holder.pagerBinding?.tvAnswer?.text = item.answerText

        fun checkAnswer(chosenSortId: Int) {
            if (chosenSortId == item.sortId) {
                item.isCorrect = true
                notifyItemChanged(position)
                onAnswerCorrect?.invoke(holder.pagerBinding, position, chosenSortId)
            } else {
                item.isCorrect = false
                onAnswerError?.invoke(chosenSortId)
            }

        }

        holder.pagerBinding?.btnSort1?.setOnClickListener { checkAnswer(1) }
        holder.pagerBinding?.btnSort2?.setOnClickListener { checkAnswer(2) }
        holder.pagerBinding?.btnSort3?.setOnClickListener { checkAnswer(3) }
        holder.pagerBinding?.btnSort4?.setOnClickListener { checkAnswer(4) }
    }

    override fun getItemCount(): Int {
        return pagerList.size
    }

    class TestPagerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pagerBinding by lazy {
            DataBindingUtil.bind<CardTestBinding>(view)
        }
    }

    fun replaceData(newList: List<SearchKeyConclusion>) {
        pagerList = newList
        notifyDataSetChanged()
    }


}