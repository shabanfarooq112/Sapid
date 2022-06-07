package com.horizam.ezlinq.fragments;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.horizam.ezlinq.App
import com.horizam.ezlinq.adapters.FeatureListAdapter
import com.horizam.ezlinq.adapters.SearchListAdapter
import com.horizam.ezlinq.databinding.FragmentSearchpeopleBinding
import com.horizam.ezlinq.networking.ApiListener
import com.horizam.ezlinq.networking.NetworkingModel
import com.horizam.ezlinq.networking.response.ConnectedUser
import com.horizam.ezlinq.networking.response.SearchData
import com.horizam.ezlinq.networking.response.SearchUserResponse


class SearchpeopleFragment : Fragment() {

    private lateinit var binding: FragmentSearchpeopleBinding
    private lateinit var networkingModel: NetworkingModel

    private lateinit var connectedUserList: List<ConnectedUser>
    private lateinit var searchListAdapter: SearchListAdapter
    private lateinit var featureDataList: List<SearchData>
    private lateinit var featureListAdapter: FeatureListAdapter


    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchpeopleBinding.inflate(layoutInflater)

        searchForUser()
        setRecycleview()
        exeSearchApi("")
        // Inflate the layout for this fragment
        return binding.root

    }

    private fun setRecycleview() {
        connectedUserList = ArrayList();
        featureDataList = ArrayList();

        binding.rvSearchResults.setHasFixedSize(true)
        binding.rvSearchResults.layoutManager =
            LinearLayoutManager(App.getAppContext(), RecyclerView.VERTICAL, false)

        searchListAdapter = SearchListAdapter(connectedUserList, App.getAppContext()!!)
        binding.rvSearchResults.adapter = searchListAdapter
        // feature recyclerview
        binding.rvFeaturedResults.setHasFixedSize(true)
        binding.rvFeaturedResults.layoutManager =
            LinearLayoutManager(App.getAppContext(), RecyclerView.HORIZONTAL, false)

        featureListAdapter = FeatureListAdapter(featureDataList, App.getAppContext()!!)
        binding.rvFeaturedResults.adapter = featureListAdapter

    }

    private fun searchForUser() {
        binding.svSearchUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    exeSearchApi(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }
        })
    }

    private fun exeSearchApi(searchQuery: String) {
        networkingModel = NetworkingModel()

        networkingModel.exeSearchUserApi(searchQuery, object : ApiListener<SearchUserResponse> {
            override fun onSuccess(body: SearchUserResponse?) {
                body.also {
                    if (it!!.status == 200) {
//                        val list:ArrayList<SearchData> = it.dataSearch.featureData as ArrayList<SearchData>
//                        list.addAll(it.dataSearch.searchData)
                        setData(it.dataSearch.connectedUser)
//                        setData2(it.dataSearch.featureData)
                    } else {
                        Toast.makeText(App.getAppContext(), it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(error: Throwable) {
                Toast.makeText(App.getAppContext(), error.message, Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onCancel() {
                super.onCancel()
            }
        })


    }

    private fun setData(connectedUser: List<ConnectedUser>) {
        if (connectedUser.isNotEmpty()) {
            binding.rvSearchResults.visibility = View.VISIBLE
            binding.textView.visibility = View.GONE
            searchListAdapter.renewlist(connectedUser)
        } else {
            binding.rvSearchResults.visibility = View.GONE
            binding.textView.visibility = View.VISIBLE

        }
    }

    private fun setData2(featureData: List<SearchData>) {
        if (featureData.isNotEmpty()) {
         //   binding.rvFeaturedResults.visibility = View.VISIBLE
//            binding.textView.visibility = View.GONE
          featureListAdapter.renewlist(featureData)
        } else {
            /*binding.rvSearchResults.visibility = View.GONE
            binding.textView.visibility = View.VISIBLE*/

        }
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): SearchpeopleFragment {
            val fragment = SearchpeopleFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}