    package com.example.foodorderapp.view.ui.shoppingcart

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.collection.emptyLongSet
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.R
import com.example.foodorderapp.adapter.AdapterItemMonAnShoppingCart
import com.example.foodorderapp.utils.FormatterMoney
import com.example.foodorderapp.utils.applySystemBarPadding
import com.example.foodorderapp.view.MainActivity
import com.example.foodorderapp.viewmodel.ViewModelGetInforUser
import com.example.foodorderapp.viewmodel.ViewModelShoppingcart
import org.w3c.dom.Text

    class FragmentOrderSummary : Fragment() {
    private lateinit var viewModelShoppingcart: ViewModelShoppingcart
    private lateinit var adapter: AdapterItemMonAnShoppingCart
    private lateinit var viewModelGetInforUser: ViewModelGetInforUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.applySystemBarPadding(applyTop = true, applySystemNavBar = true)

        val exit= view.findViewById<ImageView>(R.id.imgThoat)
        val rvFood = view.findViewById<RecyclerView>(R.id.rvFood)
        val originalPrice= view.findViewById<TextView>(R.id.OriginalPrice)
        val voucher= view.findViewById<TextView>(R.id.Voucher)
        val voucherShippping= view.findViewById<TextView>(R.id.VoucherShippping)
        val shippingFee= view.findViewById<TextView>(R.id.ShippingFee)
        val total= view.findViewById<TextView>(R.id.total)
        val namePhoneUser= view.findViewById<TextView>(R.id.txtName_Phone_User)
        val street= view.findViewById<TextView>(R.id.txtStreet)
        val region= view.findViewById<TextView>(R.id.txtRegion)
        val quantityFood= view.findViewById<TextView>(R.id.txtQuantityFood)
        val rdbCod= view.findViewById<RadioButton>(R.id.rdbCod)
        val rdbMomo= view.findViewById<RadioButton>(R.id.rdbMomo)
        val rdbZaloPay= view.findViewById<RadioButton>(R.id.rdbZalopay)
        val confirmOrder=view.findViewById<TextView>(R.id.btnOrder)


        viewModelGetInforUser= ViewModelProvider(requireActivity())[ViewModelGetInforUser::class.java]
        viewModelShoppingcart= ViewModelProvider(requireActivity())[ViewModelShoppingcart::class.java]
        adapter= AdapterItemMonAnShoppingCart(viewModelShoppingcart,"OrderSummary")
       



        exit.setOnClickListener {
            findNavController().popBackStack()
            (requireActivity() as MainActivity).setNavagationBarBottom(true)
        }
        //Xử lý nút Back hệ thống
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
            (requireActivity() as MainActivity).setNavagationBarBottom(true)
        }
        
        showFood(rvFood)
        viewModelShoppingcart.theoDoiTongTienTamTinh.observe(viewLifecycleOwner){newOriginalPrice->
            originalPrice.text= FormatterMoney.formatterMoney(newOriginalPrice)
        }
        viewModelShoppingcart.theoDoiMaGiamGia.observe(viewLifecycleOwner) { newVoucher ->
            voucher.text = FormatterMoney.formatterMoney(newVoucher)
        }
        viewModelShoppingcart.theoDoiPhiVanChuyen.observe(viewLifecycleOwner) { newShippingFee ->
            shippingFee.text = FormatterMoney.formatterMoney(30000)
            if (newShippingFee == 0) {
                voucherShippping.text = FormatterMoney.formatterMoney(-30000)
            }
            else
                voucherShippping.text= FormatterMoney.formatterMoney(0)
        }
        viewModelShoppingcart.theoDoiTongTienHang.observe(viewLifecycleOwner) { newTotal ->
            total.text = FormatterMoney.formatterMoney(newTotal)
        }
        viewModelGetInforUser.theodoiInforUser.observe(viewLifecycleOwner) { newInforUser ->
            namePhoneUser.text = newInforUser.full_name+"(${newInforUser.phone})"
            val address= newInforUser.address
            val list= address.split(",")
            street.text= list[0]
            region.text= list[1]+", "+list[2]
        }
        viewModelShoppingcart.dsDuocChon.observe(viewLifecycleOwner){listNew->
            quantityFood.text= "${listNew.size} món"
        }

        val radioButtons: List<RadioButton> = listOf(rdbCod,rdbMomo,rdbZaloPay)
        radioButtons.forEach {rb->
            rb.setOnClickListener {
                radioButtons.forEach {
                    it.isChecked=false
                }
                rb.isChecked=true
            }
        }
        confirmOrder.setOnClickListener {
           findNavController().navigate(R.id.action_shoppingCartFragment_to_fragmentConfirmOrder)
            Toast.makeText(context,"Đặt hàng thành công",Toast.LENGTH_SHORT).show()
        }
    }

        private fun showFood(rvFood: RecyclerView?) {
            rvFood?.adapter= adapter
            rvFood?.layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            viewModelShoppingcart.dsDuocChon.observe(viewLifecycleOwner){listNew->
                adapter.submitList(listNew)
            }
            rvFood?.addItemDecoration(object : RecyclerView.ItemDecoration(){
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.bottom=40
                }
            })
        }
    }