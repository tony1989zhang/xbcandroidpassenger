package com.lvchehui.www.xiangbc.activity.chooseneeds;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.base.BaseListAdapter;
import com.lvchehui.www.xiangbc.utils.AMapUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.EditText.EditTextWithDel;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张灿能 on 2016/6/29.
 * 作用：搜索关键字列表
 */

@ContentView(R.layout.activity_poikeyword_search)
public class PoiKeyWordSearchActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener, TextWatcher
        , Inputtips.InputtipsListener, AdapterView.OnItemClickListener {

    @ViewInject(R.id.keyword)
    private EditTextWithDel m_keyword;
    private String keyword;
    @ViewInject(R.id.listfind)
    private ListView m_listfind;


    private String strCity = "";
    private PoiSearch.Query query;
    private PoiSearch poiSearch;
    private PoiResult poiResult;
    private SearchAdapter searchAdapter;
    private ArrayList<String> mDatas = new ArrayList<>();
    private boolean mIsSearch = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_keyword.addTextChangedListener(this);
        set_mListView_Adapter();
    }

    @Event(value = R.id.btn_search, type = View.OnClickListener.class)
    private void onPoiKeyWordOnClick(View v) {
        keyword = AMapUtil.checkEditText(m_keyword);
        if ("".equals(keyword)) {
            showToast("请输入关键字");
        } else {


            doSearchQuery();
        }
    }

    @Event(R.id.iv_back)
    private void onBackOnClick(View v) {
        finish();
    }

    @Event(value = {R.id.root, R.id.listfind, R.id.iv_back}, type = View.OnTouchListener.class)
    private boolean onRootOnTouchListener(View v, MotionEvent me) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void set_mListView_Adapter() {
        m_listfind.setOnItemClickListener(this);
        if (searchAdapter == null) {
            searchAdapter = new SearchAdapter();
        }
        m_listfind.setAdapter(searchAdapter);

    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dismissProgressDialog();
        if (rCode == 1000) {
            if (null != result && null != result.getQuery()) {
                if (result.getQuery().equals(query)) {
                    poiResult = result;
                    ArrayList<PoiItem> poiItems = poiResult.getPois();
                    List<SuggestionCity> suggestionCitys = poiResult.getSearchSuggestionCitys();
                    if (poiItems != null && poiItems.size() > 0) {
                        showToast("poiItems != null && poiItems.size()>0");
                        mDatas.clear();
                        for (PoiItem poi : poiItems) {
                            XgoLog.e("poi:" + poi);
                            mDatas.add(poi.getCityName() + poi.getTitle());
                        }

                        m_listfind.post(eChanged);
                    } else if (suggestionCitys != null && suggestionCitys.size() > 0) {
                        showSuggestCity(suggestionCitys);
                    } else {
                        showToast("没结果");
                    }
                } else {
                    showToast("没结果");
                }
            } else {
                showToast("rCode:" + rCode);
            }
        }

    }

    private void showSuggestCity(List<SuggestionCity> suggestionCitys) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < suggestionCitys.size(); i++) {
            infomation += "城市名称:" + suggestionCitys.get(i).getCityName() + "城市区号:" + suggestionCitys.get(i).getCityCode() + "城市编码:" + suggestionCitys.get(i).getAdCode() + "\n";
        }
        showToast(infomation);
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        String newText = s.toString().trim();
        if (!StringUtils.isEmpty(newText)) {
            InputtipsQuery inputquery = new InputtipsQuery(newText, strCity);
            Inputtips inputtips = new Inputtips(this, inputquery);
            inputtips.setInputtipsListener(this);
            inputtips.requestInputtipsAsyn();
        }

    }

    @Override
    public void onGetInputtips(List<Tip> list, int rCode) {
        mDatas.clear();
        if (rCode == 1000) {
            ArrayList<String> listString = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                listString.add(list.get(i).getName());//query.getCity()+
            }
//            ArrayAdapter<String> aAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.route_input,listString);
//            m_keyword.setAdapter(aAdapter);
//            aAdapter.notifyDataSetChanged();
            mDatas.addAll(listString);
            m_listfind.post(eChanged);

        } else {
            showToast("rCode的值:" + rCode);
        }
    }

    private void doSearchQuery() {

        showProgressDialog();
        //关键字匹配
        query = new PoiSearch.Query(keyword, "", strCity);
        query.setPageSize(20);
        query.setCityLimit(true);
        mIsSearch = true;
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!mIsSearch) {
            keyword = mDatas.get(position);
            doSearchQuery();
        } else {
            Intent intent = new Intent();
            intent.putExtra(Constants.DETAILED_ADDRESS, mDatas.get(position));
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    class SearchAdapter extends BaseListAdapter {

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView searchLayout = (TextView) getLayoutInflater().inflate(R.layout.route_input, null);
            searchLayout.setText("" + mDatas.get(position));
            return searchLayout;
        }
    }

    Runnable eChanged = new Runnable() {

        @Override
        public void run() {
            //设置数据
            searchAdapter.notifyDataSetChanged();
        }
    };
}
