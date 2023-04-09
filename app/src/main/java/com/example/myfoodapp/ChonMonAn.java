package com.example.myfoodapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodapp.CustomAdapter.AdapterHienThiBanAn;
import com.example.myfoodapp.CustomAdapter.AdapterHienThiDanhSachMonAn;
import com.example.myfoodapp.DAO.MonAnDAO;
import com.example.myfoodapp.DTO.MonAnDTO;

import java.util.List;

public class ChonMonAn extends AppCompatActivity {
    GridView gridView;
    MonAnDAO monAnDAO;
    List<MonAnDTO> monAnDTOList;
    AdapterHienThiDanhSachMonAn adapterHienThiDanhSachMonAn;
    int maban;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chon_mon_an);

        maban = getIntent().getIntExtra("maban", 0);
        gridView = (GridView) findViewById(R.id.gvHienThiThucDon);
        button = (Button) findViewById(R.id.btn_chon_mon_xong);

        monAnDAO = new MonAnDAO(this);

        monAnDTOList = monAnDAO.LayDanhSachMonAn();

        adapterHienThiDanhSachMonAn = new AdapterHienThiDanhSachMonAn(this, R.layout.custom_layout_hienthidanhsachmonan, monAnDTOList);
        gridView.setAdapter(adapterHienThiDanhSachMonAn);
        adapterHienThiDanhSachMonAn.notifyDataSetChanged();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (maban != 0){
                    Intent iSoLuong = new Intent(ChonMonAn.this, SoLuongActivity.class);
                    iSoLuong.putExtra("maban", maban);
                    iSoLuong.putExtra("mamonan", monAnDTOList.get(position).getMaMonAn());

                    startActivity(iSoLuong);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void finish() {
        setResult(AdapterHienThiBanAn.RESQUEST_CODE_THEMMONAN);
        super.finish();
    }
}