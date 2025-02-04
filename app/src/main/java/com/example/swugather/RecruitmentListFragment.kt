import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swugather.DBManager
import com.example.swugather.R
import com.example.swugather.RecruitmentAdapter

/**
 * 모집 게시글 리스트를 표시하는 Fragment
 */
class RecruitmentListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView // RecyclerView 선언
    private lateinit var recruitmentAdapter: RecruitmentAdapter // 모집 게시글을 표시할 어댑터
    private lateinit var dbManager: DBManager // 데이터베이스 관리 객체

    /**
     * Fragment의 뷰를 생성하는 메서드
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_recruitment_list.xml 레이아웃을 inflate하여 뷰 생성
        val view = inflater.inflate(R.layout.fragment_recruitment_list, container, false)

        // RecyclerView 초기화 및 설정
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context) // 세로형 리스트 레이아웃 적용
        recyclerView.setHasFixedSize(false) // 아이템 크기가 고정되지 않음을 설정

        // 데이터베이스 매니저 초기화
        dbManager = DBManager(requireContext())

        // 어댑터 초기화 (초기에는 빈 리스트)
        recruitmentAdapter = RecruitmentAdapter(emptyList())
        recyclerView.adapter = recruitmentAdapter

        // newInstance()를 통해 전달된 카테고리 값 가져오기
        val category = arguments?.getString("category") ?: "전체"
        loadRecruitments(category) // 해당 카테고리의 모집 게시글 불러오기

        return view
    }

    /**
     * 데이터베이스에서 모집 게시글을 불러와 RecyclerView에 업데이트하는 메서드
     * @param category 선택한 카테고리 (기본값: 전체)
     */
    fun loadRecruitments(category: String) {
        val allPosts = dbManager.getAllPosts() // DB에서 모든 모집 게시글 가져오기

        // 선택한 카테고리에 따라 필터링하여 리스트 생성
        val recruitmentList = if (category == "전체") {
            allPosts // 전체 게시글 표시
        } else {
            allPosts.filter { it.category == category } // 특정 카테고리의 게시글만 필터링
        }

        // 어댑터에 새로운 데이터 설정 및 화면 업데이트
        recruitmentAdapter.updateData(recruitmentList)
    }

    /**
     * RecruitmentListFragment의 인스턴스를 생성하는 정적 메서드
     * @param category 선택한 카테고리
     * @return RecruitmentListFragment 인스턴스
     */
    companion object {
        fun newInstance(category: String): RecruitmentListFragment {
            val fragment = RecruitmentListFragment()
            val args = Bundle()
            args.putString("category", category) // 선택한 카테고리를 Bundle에 저장
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * dp 값을 px 값으로 변환하는 메서드
     * @param dp 변환할 dp 값
     * @return 변환된 px 값
     */
    fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
}


