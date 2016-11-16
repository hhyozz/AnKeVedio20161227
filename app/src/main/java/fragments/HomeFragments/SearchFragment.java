package fragments.HomeFragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.anke.vedio.R;
import fragments.BaseFragment;
public class SearchFragment extends BaseFragment {
    public SearchFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_search,null);
        iniview(layout);
        return layout;
    }

    private void iniview(View layout) {

    }

}
