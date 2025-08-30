package com.example.foodorderapp.view.ui.shoppingcart

import android.media.tv.interactive.TvInteractiveAppService
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.foodorderapp.R
import com.example.foodorderapp.utils.applySystemBarPadding
import com.example.foodorderapp.view.MainActivity
import com.example.foodorderapp.viewmodel.ViewModelGetInforUser


class FragmentCustomInformation : Fragment() {
    private   lateinit var viewModelGetInforUser: ViewModelGetInforUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_custom_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.applySystemBarPadding(applyTop = true, applyBottomNav = true)
        (requireActivity() as MainActivity).setNavagationBarBottom(false)
        super.onViewCreated(view, savedInstanceState)

        val thoat= view.findViewById<ImageView>(R.id.imgThoat)
        val spinnerTinh= view.findViewById<Spinner>(R.id.spinerTinh)
        val spinnerPhuongXa= view.findViewById<Spinner>(R.id.spinerHuyen)
        val nameUser= view.findViewById<EditText>(R.id.edtName)
        val phoneUser=view.findViewById<EditText>(R.id.edtPhone )
        val thongTinAddress= view.findViewById<EditText>(R.id.edtThongtin)
        val xacNhan= view.findViewById<AppCompatButton>(R.id.btnXacNhan)

        viewModelGetInforUser= ViewModelProvider(requireActivity())[ViewModelGetInforUser::class.java]


            //Load Tỉnh
            val tinhAdapter= ArrayAdapter.createFromResource(
                requireContext(),
                R.array.arrayTinh,
                android.R.layout.simple_spinner_item
            )
            tinhAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTinh.adapter= tinhAdapter
            //Load tỉnh xong sẽ load phường, xã phù hợp với tỉnh đó
            spinnerTinh.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                   val phuongXa= when (position){
                        0-> R.array.arrayHoChiMinh
                        1-> R.array.arrayGiaLai
                        2-> R.array.arrayHaNoi
                        else->0
                    }
                    if(phuongXa!=0){
                        val phuongXaAdapter= ArrayAdapter.createFromResource(requireContext(), phuongXa, android.R.layout.simple_spinner_item)
                        phuongXaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerPhuongXa.adapter=phuongXaAdapter

                    }


                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }


        thoat.setOnClickListener {
            (requireActivity() as MainActivity).setNavagationBarBottom(true)
            findNavController().navigate(R.id.action_frgamentCustomInformtion_to_fragmentShopping)
        }
        viewModelGetInforUser.theodoiInforUser.observe(viewLifecycleOwner){newUser->
            val address: String= newUser.address
            if (address.isNotEmpty()){
                loadExistingAddress(address, spinnerTinh,spinnerPhuongXa,thongTinAddress)
            }
            nameUser.setText(newUser.full_name)
            if (newUser.phone.isNotEmpty()){
                phoneUser.setText(newUser.phone)
            }

        }
        xacNhan.setOnClickListener {
            val name=nameUser.text.toString()
            val phone= phoneUser.text.toString()
            val address= thongTinAddress.text.toString()
            if(name.isEmpty()){
                Toast.makeText(context,"Họ tên không được rỗng", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(phone.isEmpty()&& phone.length==10)
            {
                Toast.makeText(context,"Số điện thoại không hợp lệ",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (address.isEmpty()){
                Toast.makeText(context,"Địa chỉ không được rỗng",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

        }

    }
    //Load địa chỉ của user lên(nếu có)
    private fun loadExistingAddress(address: String, spinnerTinh: Spinner?, spinnerPhuongXa: Spinner?, thongTinAddress: EditText?) {
        val list= address.split(",")//địa chỉ có 3 phần và tách nó ra
        if (list.size>=3)
        {
            val tinh= list[2].trim()
            val phuong= list[1].trim()
            //lấy ds tỉnh
           val arrayTinh= resources.getStringArray(R.array.arrayTinh)
            val tinhIndex= arrayTinh.indexOf(tinh)//lấy vị trí tỉnh của hiện tại của user nằm trong array

            if(tinhIndex>=0){
                spinnerTinh?.setSelection(tinhIndex)//cho danh sách tỉnh hiển thị vị trí của tỉnh vừa tìm được

                // khi hiển thị được tỉnh của user thì ta load hết toàn bộ danh sách Xã phường của tỉnh đó
                spinnerTinh?.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                            val phuongXaRes= when(position){
                                0-> R.array.arrayHoChiMinh
                                1-> R.array.arrayGiaLai
                                2->R.array.arrayHaNoi
                                else->0
                            }
                            if(phuongXaRes!=0){
                                val phuongXaAdapter= ArrayAdapter.createFromResource(
                                    requireContext(),
                                    phuongXaRes,
                                    android.R.layout.simple_spinner_item
                                )
                                phuongXaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                spinnerPhuongXa?.adapter=phuongXaAdapter

                                //load hết danh sách xong thì kiếm phường xã của user để hiển thị lên
                                val phuongXaArray= resources.getStringArray(phuongXaRes)
                                val phuongXaIndex= phuongXaArray.indexOf(phuong)
                                if (phuongXaIndex>=0) spinnerPhuongXa?.setSelection(phuongXaIndex)
                            }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            }
            thongTinAddress?.setText(list[0].trim())// hiển thị tên thôn/đường/ấp...
        }
    }
}