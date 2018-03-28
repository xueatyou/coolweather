package com.example.bianlifeng.coolweather;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bianlifeng.coolweather.database.City;
import com.example.bianlifeng.coolweather.database.County;
import com.example.bianlifeng.coolweather.database.Province;
import com.example.bianlifeng.coolweather.util.HttpUtil;
import com.example.bianlifeng.coolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by bianlifeng on 2018/3/28.
 */

public class ChooseAreaFragement extends Fragment {

    public static final int LEVEL_PROVINCE = 0;
    public static  final  int LEVEL_CITY = 1;
    public  static final int LEVEL_CONUNTY=2;

    private ProgressDialog progressDialog;
    private TextView textView;
    private Button  button;
    private ListView listView;

    private ArrayAdapter<String> adapter;
    private List<String> datalist = new ArrayList<>();

    private List<Province> provinceList;

    private List<City> cityList;

    private List<County>    countyList;

    private Province selectedProvince;

    private City  selectedCity;

    private  County selectedCounty;

    private int currentLevel;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view= inflater.inflate(R.layout.choose_area,container,false);


        textView=view.findViewById(R.id.title_text);
        button=view.findViewById(R.id.back_button);
        listView=view.findViewById(R.id.list_view);

        adapter=new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1,datalist);

        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel==LEVEL_PROVINCE){

                    selectedProvince = provinceList.get(position);
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    //
                    selectedCity=cityList.get(position);
                    queryCountries();
                }

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(currentLevel == LEVEL_CITY){
                            queryProvinvce();
                        }else if(currentLevel == LEVEL_CONUNTY){
                            queryCities();
                        }
                    }
                });
            }
        });
    }

    private void queryProvinvce(){

        textView.setText("中国");

        button.setVisibility(View.GONE);

        provinceList= DataSupport.findAll(Province.class);

        if(provinceList.size() >0){     //从数据库中查询数据
            datalist.clear();
            for (Province province:provinceList
                    ) {
                datalist.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
        }else{      //找不到，进入服务器中查找  ，第一次是进入服务器中寻找的，因为没有保存到本地
            String address="http://guolin.tech/api/china";
            querFromServer(address,"province");
        }

    }
    private void queryCities(){
        textView.setText(selectedProvince.getProvinceName());
        button.setVisibility(View.INVISIBLE);

        cityList=DataSupport.where("province = ?",
                String.valueOf(selectedProvince.getId())).find(City.class);

        if(cityList.size()>0){
            datalist.clear();
            for (City city:cityList
                 ) {
                datalist.add(city.getCityName());

            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_CITY;
        }else {
            int provinceCode = selectedProvince.getProvinceId();
            String address="http://guolin.tech/china/"+provinceCode;
            querFromServer(address,"city");
        }
    }

    private void queryCountries(){
        textView.setText(selectedCity.getCityName());
        button.setVisibility(View.INVISIBLE);
        countyList=DataSupport.where("city = ?",
                String.valueOf(selectedCity.getCityId())).find(County.class);

        if(countyList.size()>0){
            datalist.clear();
            for (County county:countyList
                 ) {
                datalist.add(county.getConutryName());
            }

            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_CONUNTY;
        }else{
            int city=selectedCity.getCityId();
            int province=selectedProvince.getProvinceId();
            String address = "http://guolin.tech/china/"+province+"/"+city;
            querFromServer(address,"country");
        }
    }

    private void querFromServer(String address, final  String type) {
        showProgressDialog();
        HttpUtil.sendOKHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(MyApplication.getContext(),"加载失败...",Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseText = response.body().toString();
                boolean result = false;
                if(type.equals("city")){
                    result= Utility.handleCityResponse(responseText,
                            selectedProvince.getId());
                }else if (type.equals("county")){
                    result=Utility.handleProvinceResponse(responseText);

                }else if (type.equals("province")){
                    result=Utility.handleCountyResponse(responseText,
                            selectedCity.getCityId());
                }
            }
        });
    }

    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载中...");
            progressDialog.setCanceledOnTouchOutside(false);

        }
        progressDialog.show();
    }
    private void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
