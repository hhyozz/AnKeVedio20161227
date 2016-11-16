package activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anke.vedio.R;
import activity.BaseActivity;
/**
 */
public class RecomandActivity extends BaseActivity {

    private TextView mTvPast;
    private RelativeLayout mRtlCancle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recomand);
        initview();
    }

    private void initview() {
        mTvPast = (TextView) findViewById(R.id.tv_past);
        mRtlCancle = (RelativeLayout) findViewById(R.id.rtl_cancle);
        mRtlCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvPast.setOnClickListener(new View.OnClickListener() {
            public ClipData myClip;
            public ClipboardManager myClipboard;

            @Override
            public void onClick(View v) {
                myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                myClip = ClipData.newPlainText("text", "");
                myClipboard.setPrimaryClip(myClip);
                Toast("已成功复制");
                finish();
            }
        });
    }
}
