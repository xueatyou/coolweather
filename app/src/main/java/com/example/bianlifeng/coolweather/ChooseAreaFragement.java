package com.example.bianlifeng.coolweather;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
    String TAG = "ChooseAreaFragement";

    public static final int LEVEL_PROVINCE = 0;
    public static  final  int LEVEL_CITY = 1;
    public  static final int LEVEL_COUNTY=2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button  button;
    private ListView listView;

    private ArrayAdapter<String> adapter;
    private List<String> datalist = new ArrayList<>();

    private List<Province> provinceList;

    private List<City> cityList;

    private List<County>    countyList;

    private Province selectedProvince;

    private City  selectedCity;


    private int currentLevel;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view= inflater.inflate(R.layout.choose_area,container,false);

      //  selectedCity=new City();
        titleText=view.findViewById(R.id.title_text);
        button=view.findViewById(R.id.back_button);
        listView=view.findViewById(R.id.list_view);

        adapter=new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1,datalist);

        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,  int position, long id) {
                //position=+1;
                Log.d(TAG, "onItemClick: currentLevel:"+currentLevel+"  postion:"+position);
                if(currentLevel==LEVEL_PROVINCE){

                    selectedProvince = provinceList.get(position);
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    //
                    selectedCity=cityList.get(position);
                    queryCountries();
                }else if (currentLevel == LEVEL_COUNTY){
                    String weatherId = countyList.get(position).getWeatherId();
                    int selectedcountyid=countyList.get(position).getCountyid();
                    Log.d("进入weather页面", "onItemClick:county ："+selectedCity.getCityName()
                            +"   weatherid="+weatherId+" selectedid :"+selectedcountyid);

                    Intent intent = new Intent(getActivity(),WeatherActivity.class);
                    intent.putExtra("weather_id",weatherId);
                    startActivity(intent);

                    getActivity().finish();
                }

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(currentLevel == LEVEL_CITY){
                            queryProvince();
                        }else if(currentLevel == LEVEL_COUNTY){
                            queryCities();
                        }
                    }
                });
            }
        });

        Log.d(TAG, "onActivityCreated: pre queryprovince()");
        queryProvince();
    }

    private void queryProvince(){
        Log.d(TAG, "queryProvince: ");

        titleText.setText("中国");

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
            queryFromServer(address,"province");
        }

    }
    private void queryCities(){
        Log.d(TAG, "queryCities: "+selectedProvince.getProvinceId());
        titleText.setText(selectedProvince.getProvinceName());
        button.setVisibility(View.VISIBLE);

        cityList=DataSupport.where("provinceid = ?",
                String.valueOf(selectedProvince.getId())).find(City.class);

        if(cityList.size()>0){
            Log.d(TAG, "queryCities: find sql");
            datalist.clear();
            for (City city:cityList
                 ) {
                datalist.add(city.getCityName());

            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_CITY;
        }else {
            Log.d(TAG, "queryCities: find server");
            int provinceCode = selectedProvince.getProvinceId();
            String address="http://guolin.tech/api/china/"+provinceCode;
            Log.d(TAG, "queryCities: address=="+address+"   province id="+provinceCode);
            queryFromServer(address,"city");
        }
    }

    private void queryCountries(){
        Log.d(TAG, "queryCountries: ");
        titleText.setText(selectedCity.getCityName());
        button.setVisibility(View.VISIBLE);
        countyList=DataSupport.where("cityid = ?",
                String.valueOf(selectedCity.getCityId())).find(County.class);

        if(countyList.size()>0){
            Log.d(TAG, "queryCountries: find sql");
            datalist.clear();
            for (County county:countyList
                 ) {
                datalist.add(county.getConutryName());
            }

            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_COUNTY;
        }else{
            Log.d(TAG, "queryCountries: findserver");
            int city=selectedCity.getCityId();
            int province=selectedProvince.getProvinceId();
            String address = "http://guolin.tech/api/china/"+province+"/"+city;
            Log.d(TAG, "queryCountries: "+address+"provinced "+province+"city "+city);
            queryFromServer(address,"county");
        }
    }

    private void queryFromServer(String address, final  String type) {
        Log.d(TAG, "queryFromServer: ");
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

                String responseText = response.body().string();

                Log.d(TAG, "onResponse: responsetext:"+responseText);

                boolean result = false;
                if(type.equals("city")){
                    Log.d(TAG, "onResponse: type = "+type);
                    result= Utility.handleCityResponse(responseText,
                            selectedProvince.getId());
                }else if (type.equals("county")){
                    Log.d(TAG, "onResponse: type = "+type);
                    result=Utility.handleCountyResponse(responseText,selectedCity.getCityId());
                }else if (type.equals("province")){
                    Log.d(TAG, "onResponse: type = "+type);
                    result=Utility.handleProvinceResponse(responseText);
                }
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvince();
                            }else if ("city".equals(type)){
                                queryCities();
                            }else if ("county".equals(type)){
                                queryCountries();
                            }
                        }
                    });
                }
            }
        });
    }

    private void showProgressDialog(){
        Log.d(TAG, "showProgressDialog: ");
        if(progressDialog == null){
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载中...");
            progressDialog.setCanceledOnTouchOutside(false);

        }
        progressDialog.show();
    }
    private void closeProgressDialog(){
        Log.d(TAG, "closeProgressDialog: ");
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
