package com.example.mydiplomawork.ui.main.rootcheck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiplomawork.R
import com.example.mydiplomawork.ui.utils.hide
import com.example.mydiplomawork.ui.utils.show
import kotlinx.coroutines.*
import uk.co.barbuzz.beerprogressview.BeerProgressView

class RootCheckFragment : Fragment() {

    private lateinit var rootItemAdapter: RootItemAdapter
    private lateinit var checkForRoot: CheckForRootWorker
    val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_root_check, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkForRoot = CheckForRootWorker(requireActivity())
        rootItemAdapter = RootItemAdapter()
        initView(view)
        resetView(view)
        checkForRoot(view)
    }

    private fun initView(view: View) {
        val recycler = view.findViewById<RecyclerView>(R.id.rootResultsRecycler)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = rootItemAdapter
    }

    private fun resetView(view: View) {
        val progress = view.findViewById<BeerProgressView>(R.id.progressView)
        progress.max = 100
        progress.beerProgress = 0
        progress.show()
        view.findViewById<RootedResultTextView>(R.id.isRootedTextView).hide()
        rootItemAdapter.clear()
    }

    private fun checkForRoot(view: View) {
        resetView(view)

        scope.launch {
            val results = checkForRoot.invoke()
            animateResults(results, view)
        }
    }

    private fun animateResults(results: List<RootItemResult>, view: View) {
        val isRooted = results.any { it.result }
        val progress = view.findViewById<BeerProgressView>(R.id.progressView)
        // this allows us to increment the progress bar for x number of times for each of the results
        // all in the effort to smooth the animation
        val multiplier = 10
        progress.max = results.size * multiplier

            scope.launch {
            withContext(Dispatchers.IO) {
                results.forEachIndexed { index, rootItemResult ->
                    for (i in 1..multiplier) {
                        // arbitrary delay, 50 millis seems to look ok when testing with 12 results
                        delay(50)
                        // post the UI updates in the UI thread
                        withContext(Dispatchers.Main) {
                            progress.beerProgress = progress.beerProgress + 1

                            // only add to the once we hit the multiplier
                            if (i == multiplier) {
                                rootItemAdapter.add(rootItemResult)
                            }
                            //is it the end of the results
                            if (index == results.size - 1) {
                                onRootCheckFinished(isRooted = isRooted, view)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onRootCheckFinished(isRooted: Boolean, view: View) {
        val isRootedTextView = view.findViewById<RootedResultTextView>(R.id.isRootedTextView)
        isRootedTextView.update(isRooted = isRooted)
        isRootedTextView.show()
    }
}