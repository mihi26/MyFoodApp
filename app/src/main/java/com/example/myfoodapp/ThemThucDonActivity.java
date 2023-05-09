package com.example.myfoodapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodapp.DAO.MonAnDAO;
import com.example.myfoodapp.DTO.MonAnDTO;
import com.example.myfoodapp.Fragment.HienThiThucDonFragment;

public class ThemThucDonActivity extends AppCompatActivity implements View.OnClickListener{
    public static int REQUEST_CODE_THEMLOAITHUCDON = 113;
    public static int REQUEST_CODE_MOHINH = 123;

    private MonAnDAO monAnDAO;

    private TextView tvTitle;
    private ImageView imHinhThucDon;
    private Button btnDongYThemMonAn, btnThoatThemMonAn;
    private String sDuongdanhinh;
    private EditText edTenMonAn, edGiaTien;
    private int mamon;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_themthucdon);

        sDuongdanhinh = "@drawable/backgroundheader1";
        monAnDAO = new MonAnDAO(this);

        mamon = getIntent().getIntExtra("mamon", -1);
//        Toast.makeText(this,mamon+"",Toast.LENGTH_SHORT).show();

        tvTitle =findViewById(R.id.tv_title);
        imHinhThucDon = findViewById(R.id.imHinhThucDon);
        btnDongYThemMonAn = findViewById(R.id.btnDongYThemMonAn);
        btnThoatThemMonAn = findViewById(R.id.btnThoatThemMonAn);
        edTenMonAn = findViewById(R.id.edThemTenMonAn);
        edGiaTien = findViewById(R.id.edThemGiaTien);


        if(mamon != -1){
            tvTitle.setText("Sửa món ăn");
            MonAnDTO monAnDTO = monAnDAO.LayMonAnTheoMa(mamon);
            edTenMonAn.setText(monAnDTO.getTenMonAn());
            edGiaTien.setText(monAnDTO.getGiaTien());
            imHinhThucDon.setImageURI(Uri.parse(monAnDTO.getHinhAnh()));
        }

        imHinhThucDon.setOnClickListener(this);
        btnDongYThemMonAn.setOnClickListener(this);
        btnThoatThemMonAn.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.imHinhThucDon:
                Intent iMoHinh = new Intent();
                iMoHinh.setType("image/*");
                iMoHinh.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(iMoHinh,"Chọn hình thực đơn"), REQUEST_CODE_MOHINH);
                break;
            case R.id.btnDongYThemMonAn:
                String tenmonan = edTenMonAn.getText().toString();
                String giatien = edGiaTien.getText().toString();

                if (tenmonan != null && giatien != null && !tenmonan.equals("") && !giatien.equals("")){
                    MonAnDTO monAnDTO = new MonAnDTO();
                    monAnDTO.setGiaTien(giatien);
                    monAnDTO.setHinhAnh(sDuongdanhinh);
                    monAnDTO.setTenMonAn(tenmonan);
                    monAnDTO.setMaMonAn(mamon);

                   boolean kiemtra ;
                   String toastText;
                   if(mamon != -1){
                       kiemtra = monAnDAO.SuaMonAn(monAnDTO);
                       toastText = "Sửa thành công";
                   }
                   else{
                       kiemtra = monAnDAO.ThemMonAn(monAnDTO);
                       toastText = "Thêm thành công";
                   }

                    if (kiemtra){
                        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
                        setResult(HienThiThucDonFragment.RESQUEST_CODE_THUC_DON);
                        finish();
                    }
                    else
                        Toast.makeText(this, getResources().getString(R.string.themthatbai), Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(this, getResources().getString(R.string.loithemmonan), Toast.LENGTH_SHORT).show();

                break;
            case R.id.btnThoatThemMonAn:
                setResult(HienThiThucDonFragment.RESQUEST_CODE_THUC_DON);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(REQUEST_CODE_MOHINH == requestCode){
            if (resultCode == Activity.RESULT_OK && data != null){
                sDuongdanhinh = data.getData().toString();
                imHinhThucDon.setImageURI(data.getData());
            }
        }
    }
}
