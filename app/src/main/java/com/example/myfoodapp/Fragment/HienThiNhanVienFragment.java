package com.example.myfoodapp.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myfoodapp.CustomAdapter.AdapterHienThiNhanVien;
import com.example.myfoodapp.DAO.NhanVienDAO;
import com.example.myfoodapp.DTO.NhanVienDTO;
import com.example.myfoodapp.DangKyActivity;
import com.example.myfoodapp.Database.CreateDatabase;
import com.example.myfoodapp.R;

import java.util.List;

public class HienThiNhanVienFragment extends Fragment{

    ListView listNhanVien;
    NhanVienDAO nhanVienDAO;
    List<NhanVienDTO> nhanVienDTOList;
    CreateDatabase createDatabase;
    int maquyen;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_hienthinhanvien, container, false);
        setHasOptionsMenu(true);

        listNhanVien = view.findViewById(R.id.listNhanVien);

        nhanVienDAO = new NhanVienDAO(getActivity());

        HienThiDanhSachNhanVien();

        sharedPreferences = getActivity().getSharedPreferences("luuquyen", Context.MODE_PRIVATE);
        maquyen = sharedPreferences.getInt("maquyen", 0);

        if (maquyen == 0){

            registerForContextMenu(listNhanVien);
        }

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.edit_context_menu, menu);
    }

    private void HienThiDanhSachNhanVien(){
        nhanVienDTOList = nhanVienDAO.LayDanhSachNhanVien();

        AdapterHienThiNhanVien adapterHienThiNhanVien = new AdapterHienThiNhanVien(getActivity(), R.layout.custom_layout_hienthinhanvien, nhanVienDTOList);
        listNhanVien.setAdapter(adapterHienThiNhanVien);
        adapterHienThiNhanVien.notifyDataSetChanged();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int vitri = menuInfo.position;
        int manhavien = nhanVienDTOList.get(vitri).getMANV();

        switch (id){
            case R.id.itSua:
                Intent iDangKy = new Intent(getActivity(), DangKyActivity.class);
                iDangKy.putExtra("manhanvien", manhavien);
                startActivityForResult(iDangKy,DangKyActivity.REQUEST_CODE_NHAN_VIEN);
                break;
            case R.id.itXoa:
                if(manhavien == 1){
                    Toast.makeText(getActivity(),"Không thể xóa tài khoản admin",Toast.LENGTH_SHORT).show();
                    break;
                }
                boolean kiemtra = nhanVienDAO.XoaNhanVien(manhavien);
                if (kiemtra){
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.xoathanhcong), Toast.LENGTH_SHORT).show();
                    HienThiDanhSachNhanVien();
                }else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.loi), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (maquyen == 0){
            MenuItem itThemNhanVien = menu.add(1, R.id.itThemNhanVien, 1, R.string.themnhanvien);
            itThemNhanVien.setIcon(R.drawable.nhanvien);
            itThemNhanVien.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.itThemNhanVien:
                Intent iDangKy = new Intent(getActivity(), DangKyActivity.class);
                startActivityForResult(iDangKy,DangKyActivity.REQUEST_CODE_NHAN_VIEN);
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DangKyActivity.REQUEST_CODE_NHAN_VIEN){
            HienThiDanhSachNhanVien();
        }
    }
}
