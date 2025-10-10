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

        exit.setOnClickListener {
           findNavController().popBackStack()
        }
        lifecycleScope.launchWhenStarted {
            viewModelOrder.listExeededStockFoods.collect { listFoodProblem ->
                Log.d("ConfirmOrder", "$listFoodProblem")
                if (listFoodProblem.isNotEmpty()) {
                    Log.d("ConfirmOrder", "Danh sahcs là $listFoodProblem")
                    imgResult.setImageResource(R.drawable.icon_fail)
                    textResult.text = "Đặt hàng không thành công!"
                    textContext.text="Rất tiếc, một số món trong đơn hàng của bạn đã hết hàng hoặc không đủ số lượng."
                    btnConfirm.text = "Chỉnh sửa đơn hàng"
                    adapter = AdapterItemProblemDish(listFoodProblem)
                    rvFoodProblem.adapter = adapter
                    rvFoodProblem.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    rvFoodProblem.addItemDecoration(object : RecyclerView.ItemDecoration() {
                        override fun getItemOffsets(
                            outRect: Rect,
                            view: View,
                            parent: RecyclerView,
                            state: RecyclerView.State
                        ) {
                            outRect.bottom= 40
                        }
                    })
                }
                viewModelOrder.clearExcessStock()
            }
        }
    }
}