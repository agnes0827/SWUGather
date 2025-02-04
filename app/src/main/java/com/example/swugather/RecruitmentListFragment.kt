import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swugather.DBManager
import com.example.swugather.R
import com.example.swugather.RecruitmentAdapter

class RecruitmentListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recruitmentAdapter: RecruitmentAdapter
    private lateinit var dbManager: DBManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recruitment_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        dbManager = DBManager(requireContext())
        recruitmentAdapter = RecruitmentAdapter(emptyList())
        recyclerView.adapter = recruitmentAdapter

        // newInstance()에서 전달된 값 가져오기
        val category = arguments?.getString("category") ?: "전체"
        loadRecruitments(category)

        return view
    }

    fun loadRecruitments(category: String) {
        val recruitmentList = dbManager.getAllPosts().filter {
            it.category == category || category == "전체"
        }
        recruitmentAdapter.updateData(recruitmentList)
    }

    companion object {
        fun newInstance(category: String): RecruitmentListFragment {
            val fragment = RecruitmentListFragment()
            val args = Bundle()
            args.putString("category", category)
            fragment.arguments = args
            return fragment
        }
    }
}