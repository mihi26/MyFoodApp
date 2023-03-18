package com.example.myfoodapp.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myfoodapp.DTO.MonAnDTO;
import com.example.myfoodapp.Database.CreateDatabase;

import java.util.ArrayList;
import java.util.List;

public class MonAnDAO {
    private final SQLiteDatabase database;

    public MonAnDAO(Context context){
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }

    public boolean ThemMonAn(MonAnDTO monAnDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TB_MONAN_TENMONAN,monAnDTO.getTenMonAn());
        contentValues.put(CreateDatabase.TB_MONAN_GIATIEN,monAnDTO.getGiaTien());
        contentValues.put(CreateDatabase.TB_MONAN_HINHANH, monAnDTO.getHinhAnh());

        long kiemtra = database.insert(CreateDatabase.TB_MONAN,null,contentValues);
        return kiemtra != 0;
    }

    public boolean XoaMonAn(int mamon){
        int kiemtra = database.delete(CreateDatabase.TB_MONAN, CreateDatabase.TB_MONAN_MAMON + " = " + mamon,null);
        return kiemtra != 0;
    }
    @SuppressLint("Recycle")
    public List<MonAnDTO> LayDanhSachMonAn(){
        List<MonAnDTO> monAnDTOs = new ArrayList<MonAnDTO>();

        String truyvan = "SELECT * FROM " + CreateDatabase.TB_MONAN;
        Cursor cursor = database.rawQuery(truyvan,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            MonAnDTO monAnDTO = new MonAnDTO();
            monAnDTO.setHinhAnh(cursor.getString(cursor.getColumnIndex(CreateDatabase.TB_MONAN_HINHANH)));
            monAnDTO.setTenMonAn(cursor.getString(cursor.getColumnIndex(CreateDatabase.TB_MONAN_TENMONAN)));
            monAnDTO.setGiaTien(cursor.getString(cursor.getColumnIndex(CreateDatabase.TB_MONAN_GIATIEN)));
            monAnDTO.setMaMonAn(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TB_MONAN_MAMON)));

            monAnDTOs.add(monAnDTO);
            cursor.moveToNext();
        }

        return monAnDTOs;
    }

    public MonAnDTO LayMonAnTheoMa(int mamon){
        MonAnDTO monAnDTO = new MonAnDTO();
        String truyVan = "SELECT * FROM " + CreateDatabase.TB_MONAN + " WHERE " +CreateDatabase.TB_MONAN_MAMON+" = "+mamon;
        Cursor cursor = database.rawQuery(truyVan,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            monAnDTO.setMaMonAn(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TB_MONAN_MAMON)));
            monAnDTO.setGiaTien(cursor.getString(cursor.getColumnIndex(CreateDatabase.TB_MONAN_GIATIEN)));
            monAnDTO.setHinhAnh(cursor.getString(cursor.getColumnIndex(CreateDatabase.TB_MONAN_HINHANH)));
            monAnDTO.setTenMonAn(cursor.getString(cursor.getColumnIndex(CreateDatabase.TB_MONAN_TENMONAN)));
            cursor.moveToNext();
        }

        return monAnDTO;
    }

    public boolean SuaMonAn(MonAnDTO monAnDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TB_MONAN_TENMONAN,monAnDTO.getTenMonAn());
        contentValues.put(CreateDatabase.TB_MONAN_GIATIEN,monAnDTO.getGiaTien());
        contentValues.put(CreateDatabase.TB_MONAN_HINHANH, monAnDTO.getHinhAnh());

        long kiemtra = database.update(CreateDatabase.TB_MONAN,contentValues,CreateDatabase.TB_MONAN_MAMON + " = "+monAnDTO.getMaMonAn(),null);
        return kiemtra != 0;
    }
}
