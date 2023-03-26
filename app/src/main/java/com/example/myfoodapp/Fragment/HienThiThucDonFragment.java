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
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myfoodapp.CustomAdapter.AdapterHienThiDanhSachMonAn;
import com.example.myfoodapp.DAO.MonAnDAO;
import com.example.myfoodapp.DTO.MonAnDTO;
import com.example.myfoodapp.R;
import com.example.myfoodapp.ThemThucDonActivity;
import com.example.myfoodapp.TrangChuActivity;

import java.util.List;

public class HienThiThucDonFragment extends Fragment{
    public static int RESQUEST_CODE_THUC_DON = 456;


    private GridView gridView;
    private FragmentManager fragmentManager;
    private SharedPreferences sharedPreferences;
    private MonAnDAO monAnDAO;
    private List<MonAnDTO> monAnDTOList;
    AdapterHienThiDanhSachMonAn adapterHienThiDanhSachMonAn;

    private int maban;
    private int maquyen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_hienthithucdon, container, false);
        setHasOptionsMenu(true);
        ((TrangChuActivity) getActivity()).getSupportActionBar().setTitle(R.string.thucdon);
        gridView = view.findViewById(R.id.gvHienThiThucDon);

        fragmentManager = getActivity().getSupportFragmentManager();

        monAnDAO = new MonAnDAO(getActivity());
        HienThiMonAn();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("luuquyen", Context.MODE_PRIVATE);
        maquyen = sharedPreferences.getInt("maquyen", 0);

        if (maquyen == 0) {
            registerForContextMenu(gridView);
        }

        return view;
    }

    private void HienThiMonAn(){
        monAnDTOList = monAnDAO.LayDanhSachMonAn();
        adapterHienThiDanhSachMonAn = new AdapterHienThiDanhSachMonAn(getActivity(), R.layout.custom_layout_hienthidanhsachmonan, monAnDTOList);
        gridView.setAdapter(adapterHienThiDanhSachMonAn);
        adapterHienThiDanhSachMonAn.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.edit_context_menu, menu);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int vitri = menuInfo.position;
        int mamon = monAnDTOList.get(vitri).getMaMonAn();

        switch (id){
            case R.id.itSua:
                Intent intent = new Intent(getActivity(), ThemThucDonActivity.class);
                intent.putExtra("mamon", mamon);
                startActivityForResult(intent, RESQUEST_CODE_THUC_DON);
                break;
            case R.id.itXoa:
                boolean kiemtra = monAnDAO.XoaMonAn(mamon);
                if (kiemtra){
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.xoathanhcong), Toast.LENGTH_SHORT).show();
                    HienThiMonAn();
                }else
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.loi) + mamon, Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (maquyen == 0){
            MenuItem itThemThucDon = menu.add(1, R.id.itThemThucDon, 1, R.string.themthucdon);
            itThemThucDon.setIcon(R.drawable.logodangnhap);
            itThemThucDon.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.itThemThucDon) {
            Intent iThemThucDon = new Intent(getActivity(), ThemThucDonActivity.class);
            startActivityForResult(iThemThucDon,RESQUEST_CODE_THUC_DON);
            getActivity().overridePendingTransition(R.anim.hieuung_activity_vao, R.anim.hieuung_activity_ra);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESQUEST_CODE_THUC_DON){
            HienThiMonAn();
        }
    }
}
