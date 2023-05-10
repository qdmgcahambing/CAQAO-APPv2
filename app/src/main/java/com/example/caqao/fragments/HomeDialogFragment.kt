package com.example.caqao.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import com.example.caqao.R


open class HomeDialogFragment : DialogFragment() {

//    private val sharedViewModel: CacaoDetailViewModel by activityViewModels()
//    private lateinit var binding: FragmentCacaoDetailDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = when (arguments?.getInt("layoutId")){
            //defects
            1 -> inflater.inflate(R.layout.fragment_mouldy_dialog, container, false)
            2 -> inflater.inflate(R.layout.fragment_insect_dialog, container, false)
            3 -> inflater.inflate(R.layout.fragment_slate_dialog, container, false)
            4 -> inflater.inflate(R.layout.fragment_germinated_dialog, container, false)

            //color
            5 -> inflater.inflate(R.layout.fragment_brown_dialog, container, false)
            6 -> inflater.inflate(R.layout.fragment_partly_purple_dialog, container, false)
            7 -> inflater.inflate(R.layout.fragment_total_purple_dialog, container, false)
            8 -> inflater.inflate(R.layout.fragment_very_dark_brown_dialog, container, false)

            //fissuring
            9 -> inflater.inflate(R.layout.fragment_g1_dialog, container, false)
            10 -> inflater.inflate(R.layout.fragment_g2_dialog, container, false)
            11 -> inflater.inflate(R.layout.fragment_g3_dialog, container, false)
            12 -> inflater.inflate(R.layout.fragment_g4_dialog, container, false)

            //bean cutting steps
            13 -> inflater.inflate(R.layout.step1_dialog, container, false)
            14 -> inflater.inflate(R.layout.step2_dialog, container, false)
            15 -> inflater.inflate(R.layout.step3_dialog, container, false)
            16 -> inflater.inflate(R.layout.step4_dialog, container, false)
            17 -> inflater.inflate(R.layout.step5_dialog, container, false)
            18 -> inflater.inflate(R.layout.step6_dialog, container, false)
            19 -> inflater.inflate(R.layout.step7_dialog, container, false)

            // cacao detail results
            // 20 -> inflater.inflate(R.layout.fragment_cacao_detail_dialog, container, false)
            // binding = FragmentCacaoDetailDialogBinding.inflate(inflater, container, false)
//            20 -> {
//                binding = FragmentCacaoDetailDialogBinding.bind(inflater.inflate(
//                    R.layout.fragment_cacao_detail_dialog, container, false))
//                if (arguments?.getInt("argument") == 20) {
//                    binding.viewModel = sharedViewModel
//                    binding.lifecycleOwner = viewLifecycleOwner
//                    Log.d("PopupImageUri", "${sharedViewModel.cacaoDetection.value?.imgSrcUrl}")
//                }
//                binding.root
//            }
            else -> throw IllegalArgumentException("Invalid layout ID")
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val closeButton = view.findViewById<ImageButton>(R.id.closeBtn)
        closeButton.setOnClickListener {
            dismiss()
        }

    }

}