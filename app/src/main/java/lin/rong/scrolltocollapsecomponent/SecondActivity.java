package lin.rong.scrolltocollapsecomponent;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by dell on 2017/1/3.
 */
public class SecondActivity extends Activity implements ScrollToCollapseComponent.ScrollToCollapseComponentViews {

    private LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layoutInflater = LayoutInflater.from(SecondActivity.this);

        ScrollToCollapseComponent scrollToCollapseComponent = new ScrollToCollapseComponent(SecondActivity.this);
        scrollToCollapseComponent.setScrollToCollapseComponentViews(this);
        setContentView(scrollToCollapseComponent);
    }

    @Override
    public View getTopBarView() {
        View rootView = layoutInflater.inflate(R.layout.top_bar_demo, null);
        View btn_back = rootView.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }

        });
        return rootView;
    }

    @Override
    public View getTopContentView() {
        return layoutInflater.inflate(R.layout.top_content_demo, null);
    }

    @Override
    public View getBottomContentView() {
        return layoutInflater.inflate(R.layout.bottom_content_demo, null);
    }

}
