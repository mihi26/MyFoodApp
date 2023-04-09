package com.example.myfoodapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.myfoodapp.CustomAdapter.AdapterHienThiBanAn;
import com.example.myfoodapp.CustomAdapter.AdapterHienThiThanhToan;
import com.example.myfoodapp.DAO.BanAnDAO;
import com.example.myfoodapp.DAO.GoiMonDAO;
import com.example.myfoodapp.DTO.ThanhToanDTO;

import java.util.List;

public class ThanhToanActivity extends AppCompatActivity implements View.OnClickListener{
    private GridView gridView;
    private Button btnThanhToan, btnThoat;
    private TextView txtTongTien;
    private GoiMonDAO goiMonDAO;
    private List<ThanhToanDTO> thanhToanDTOList;
    private AdapterHienThiThanhToan adapterHienThiThanhToan;
    private BanAnDAO banAnDAO;
    private FragmentManager fragmentManager;

    long tongtien = 0;
    int maban;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_thanhtoan);

        gridView = findViewById(R.id.gvThanhToan);
        btnThanhToan = findViewById(R.id.btnThanhToan);
        btnThoat = findViewById(R.id.btnThoatThanhToan);
        txtTongTien = findViewById(R.id.txtTongTien);

        goiMonDAO = new GoiMonDAO(this);
        banAnDAO = new BanAnDAO(this);

        fragmentManager = getSupportFragmentManager();

        maban = getIntent().getIntExtra("maban", 0);
        if (maban != 0){
            HienThiThanhToan();

            for (int i = 0; i < thanhToanDTOList.size(); i++){
                int soluong = thanhToanDTOList.get(i).getSoLuong();
                int giatien = thanhToanDTOList.get(i).getGiatien();

                tongtien += (soluong * giatien);
            }
            txtTongTien.setText(getResources().getString(R.string.tongcong) + " " + tongtien);
        }

        btnThanhToan.setOnClickListener(this);
        btnThoat.setOnClickListener(this);
    }

    private void HienThiThanhToan(){
        int magoimon = (int) goiMonDAO.LayMaGoiMonTheoMaBan(maban, "false");
        thanhToanDTOList = goiMonDAO.LayDanhSachMonAnTheoMaGoiMon(magoimon);

        adapterHienThiThanhToan = new AdapterHienThiThanhToan(this, R.layout.custom_layout_hienthithanhtoan, thanhToanDTOList);
        gridView.setAdapter(adapterHienThiThanhToan);
        adapterHienThiThanhToan.notifyDataSetChanged();
    }

    @SuppressLint({"NonConstantResourceId", "ShowToast"})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnThanhToan:
                boolean kiemtrabanan = banAnDAO.CapNhatTinhTrangBan(maban, "false");
                boolean kiemtragoimom = goiMonDAO.CapNhatTrangThaiGoiMonTheoMaBan(maban, "true");

                if(kiemtrabanan && kiemtragoimom){
                    Toast.makeText(ThanhToanActivity.this, getResources().getString(R.string.thanhtoanthanhcong), Toast.LENGTH_SHORT).show();
                    HienThiThanhToan();
                }else
                    Toast.makeText(ThanhToanActivity.this, getResources().getString(R.string.loi), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnThoatThanhToan:
                setResult(AdapterHienThiBanAn.RESQUEST_CODE_THANHTOAN);
                finish();
                break;
        }
    }
}
