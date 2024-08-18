package com.skilrock.retailapp.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.skilrock.retailapp.models.rms.HomeDataBean;

import java.util.ArrayList;
import java.util.List;

public class ScratchAndDrawGameViewModel extends ViewModel {

    private MutableLiveData<List<HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList>> listMutableLiveDataMenuBean = new MutableLiveData<>();

    public MutableLiveData<List<HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList>> getMenuList(){
        return listMutableLiveDataMenuBean;
    }

    public void createScratchGameModuleList(final int index, ArrayList<HomeDataBean.ResponseData.ModuleBeanList.MenuBeanList> listMenuBean) {
        if (listMenuBean == null)
            listMutableLiveDataMenuBean.postValue(null);
        else if (listMenuBean.size() < 1)
            listMutableLiveDataMenuBean.postValue(null);
        else
            listMutableLiveDataMenuBean.postValue(listMenuBean);
    }
}
