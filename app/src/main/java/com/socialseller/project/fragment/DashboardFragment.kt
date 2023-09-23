package com.socialseller.project.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.socialseller.project.R
import com.socialseller.project.adapter.DashBoardRecyclerViewAdapter
import com.socialseller.project.database.StudentViewModel


class DashboardFragment : Fragment(), SearchView.OnQueryTextListener {
    private lateinit var mStudentViewModel: StudentViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerViewAdapter: DashBoardRecyclerViewAdapter


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        setHasOptionsMenu(true)

        recyclerView = view.findViewById(R.id.recyclerDashboard)

        mStudentViewModel = ViewModelProvider(this).get(StudentViewModel::class.java)
        mStudentViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            recyclerViewAdapter.setData(it)
        })



        linearLayoutManager = LinearLayoutManager(activity)
        recyclerViewAdapter = DashBoardRecyclerViewAdapter(activity as Context,mStudentViewModel)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                activity,
                LinearLayoutManager.VERTICAL
            )
        )

        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_sort,menu)
        val search = menu.findItem(R.id.search)
        val searchView = search?.actionView as SearchView
//        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id==R.id.ascending){
            mStudentViewModel.readAllData.observe(viewLifecycleOwner, Observer {
                recyclerViewAdapter.setData(it)
            })
        }else if (id==R.id.descending){
            mStudentViewModel.readAllDataDescending.observe(viewLifecycleOwner, Observer {
                recyclerViewAdapter.setData(it)
            })
        }
        else if(id==R.id.search){
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query!=null){
            searchDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query!=null){
            searchDatabase(query)
        }
        return true
    }
    private fun searchDatabase(query: String){
        val searchQuery = "%$query%"
        mStudentViewModel.searchDatabase(searchQuery).observe(this) { list ->
            list.let {
                recyclerViewAdapter.setData(it)

            }

        }
    }

}