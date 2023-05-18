package com.example.myfoodapp;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfoodapp.DAO.MonAnDAO;
import com.example.myfoodapp.DTO.MonAnDTO;
import com.example.myfoodapp.Fragment.HienThiThucDonFragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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

        sDuongdanhinh = "";
        monAnDAO = new MonAnDAO(this);

        mamon = getIntent().getIntExtra("mamon", -1);

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
            String hinhanhUrl = monAnDTO.getHinhAnh();
            if(hinhanhUrl == null || hinhanhUrl.equals(""))
                imHinhThucDon.setImageResource(R.drawable.backgroundheader1);
            else {
                byte[] byteArray = Base64.decode(hinhanhUrl, Base64.DEFAULT);
                Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                imHinhThucDon.setImageBitmap(image);
            }
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
                iMoHinh.setAction(Intent.ACTION_GET_CONTENT);
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
                try {
                    // Chuyển đổi URI thành InputStream
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    // Chuyển đổi InputStream thành Bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
                    byte[] byteImage = outputStream.toByteArray();
                    sDuongdanhinh  = Base64.encodeToString(byteImage, Base64.DEFAULT);
                    imHinhThucDon.setImageURI(data.getData());
                } catch (IOException e) {
                    sDuongdanhinh = "";
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
