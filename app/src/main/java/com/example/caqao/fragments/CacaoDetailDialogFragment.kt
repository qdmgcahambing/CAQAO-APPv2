package com.example.caqao.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.caqao.R
import com.example.caqao.caqaodetail.CacaoDetailViewModel
import com.example.caqao.caqaodetail.CacaoDetailViewModelFactory
import com.example.caqao.databinding.FragmentCacaoDetailDialogBinding
import com.example.caqao.models.CacaoDetectionViewModel
import kotlin.math.max
import kotlin.math.min


class CacaoDetailDialogFragment : DialogFragment() {

    private var scaleFactor = 1f
    private var lastX = 0f
    private var lastY = 0f
    private var posX = 0f
    private var posY = 0f

    private val sharedViewModel: CacaoDetectionViewModel by activityViewModels()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentCacaoDetailDialogBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_cacao_detail_dialog, container, false)

        val rootView = binding.root
        val imageView = rootView.findViewById<ImageView>(R.id.cacao_detect_result)
        val detector = ScaleGestureDetector(requireContext(), ScaleListener(imageView))

        imageView.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.rawX
                    lastY = event.rawY
                    posX = view.x
                    posY = view.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = event.rawX - lastX
                    val deltaY = event.rawY - lastY
                    view.animate()
                        .x(posX + deltaX)
                        .y(posY + deltaY)
                        .setDuration(0)
                        .start()
                }
            }
            detector.onTouchEvent(event)
            true
        }

        val viewModelFactory = arguments?.getInt("cacaoDetectionId")
            ?.let { CacaoDetailViewModelFactory(it) }

        // Get a reference to the ViewModel associated with this fragment.
        val cacaoDetailViewModel =
            viewModelFactory?.let {
                ViewModelProvider(
                    this, it
                ).get(CacaoDetailViewModel::class.java)
            }

        arguments?.getInt("cacaoDetectionId")
            ?.let { cacaoDetailViewModel?.getCacaoDetectionWithId(it) }


        binding.viewModel = cacaoDetailViewModel

        binding.lifecycleOwner = this

        return rootView
    }

    private inner class ScaleListener(private val view: View) : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            scaleFactor *= scaleGestureDetector.scaleFactor
            scaleFactor = max(0.1f, min(scaleFactor, 10.0f))
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
            return true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val closeButton = view.findViewById<ImageButton>(R.id.closeBtn)
        closeButton.setOnClickListener {
            dismiss()
        }

    }
}
