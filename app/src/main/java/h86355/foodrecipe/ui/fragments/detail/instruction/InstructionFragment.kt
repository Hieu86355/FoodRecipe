package h86355.foodrecipe.ui.fragments.detail.instruction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrecipes.models.Result
import h86355.foodrecipe.R
import h86355.foodrecipe.adapters.InstructionRecyclerAdapter
import h86355.foodrecipe.databinding.FragmentInstructionBinding

class InstructionFragment : Fragment() {

    private var _binding: FragmentInstructionBinding? = null
    private val binding get() = _binding!!
    private val instructionAdapter by lazy { InstructionRecyclerAdapter() }
    private lateinit var result: Result

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInstructionBinding.inflate(inflater, container, false)

        result = requireArguments().getParcelable<Result>("recipeBundle")!!

        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        binding.instructionList.layoutManager = LinearLayoutManager(requireContext())
        binding.instructionList.adapter = instructionAdapter

        val divider = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider_layer)!!)
        binding.instructionList.addItemDecoration(divider)

        /*
            show webview if instruction is empty, otherwise add to recyclerview
         */
        val instructions = result.analyzedInstructions
        if (!instructions.isNullOrEmpty()) {
            val instruction = instructions.get(0)
            if (!instruction.steps.isNullOrEmpty()) {
                instructionAdapter.submitList(instruction.steps)
            } else {
                setupWebView()
            }
        } else {
            setupWebView()
        }
    }

    private fun setupWebView() {
        binding.instructionWebView.visibility = View.VISIBLE
        binding.instructionProgress.visibility = View.VISIBLE
        binding.instructionList.visibility = View.GONE
        binding.instructionWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                binding.instructionProgress.visibility = View.GONE
            }
        }
        result.sourceUrl?.let { binding.instructionWebView.loadUrl(it) }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}