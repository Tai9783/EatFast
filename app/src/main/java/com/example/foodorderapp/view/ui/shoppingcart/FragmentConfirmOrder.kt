package com.example.foodorderapp.view.ui.shoppingcart
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.R
import com.example.foodorderapp.adapter.AdapterItemProblemDish
import com.example.foodorderapp.utils.applySystemBarMargin
import com.example.foodorderapp.viewmodel.ViewModelOrder

class FragmentConfirmOrder : Fragment() {
    private lateinit var viewModelOrder: ViewModelOrder
    private lateinit var adapter: AdapterItemProblemDish
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_order, container, false)
    }
    override fun  onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.applySystemBarMargin(applyTop = true, applyBottom = true)

        viewModelOrder= ViewModelProvider(requireActivity())[ViewModelOrder::class.java]

        val exit= view.findViewById<ImageView>(R.id.imgThoat)
        val imgResult= view.findViewById<ImageView>(R.id.imgResult)
        val textResult= view.findViewById<TextView>(R.id.txtResult)
        val rvFoodProblem= view.findViewById<RecyclerView>(R.id.rvFoodProblem)
        val textContext= view.findViewById<TextView>(R.id.txtContent)
        val btnConfirm= view.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnConfirm)
        val layoutWarning= view.findViewById<androidx.cardview.widget.CardView>(R.id.layoutWarning)


        exit.setOnClickListener {
           findNavController().popBackStack()
            viewModelOrder.clearExcessStock()
        }
        val key= arguments?.let { FragmentConfirmOrderArgs.fromBundle(it).key }
        if (key=="true"){
            imgResult.setImageResource(R.drawable.icon_success)
            textResult.text = getString(R.string.ConfirmOrder_result)
            textContext.text= getString(R.string.ConfirmOrder_Content)
            btnConfirm.text = getString(R.string.ConfirmOrder_ViewOrder )
        }
        else if(key=="false") {
            layoutWarning.visibility = View.VISIBLE
            imgResult.setImageResource(R.drawable.icon_fail)
            textResult.text = getString(R.string.ConfirmOrder_failed)
            textContext.text = getString(R.string.ConfirmOrder_problem)
            btnConfirm.text = getString(R.string.ConfirmOrder_order)

            lifecycleScope.launchWhenStarted {
                viewModelOrder.listExeededStockFoods.collect { listFoodProblem ->
                    adapter = AdapterItemProblemDish(listFoodProblem)
                    rvFoodProblem.adapter = adapter
                    rvFoodProblem.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    rvFoodProblem.addItemDecoration(object : RecyclerView.ItemDecoration() {
                        override fun getItemOffsets(
                            outRect: Rect,
                            view: View,
                            parent: RecyclerView,
                            state: RecyclerView.State
                        ) {
                            outRect.bottom = 40
                        }
                    })
                }
            }
        }

    }
}