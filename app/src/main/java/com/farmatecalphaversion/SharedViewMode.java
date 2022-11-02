package com.farmatecalphaversion;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewMode extends ViewModel {
    private MutableLiveData<String> name;

    public void setNameData(String nameData) {
        name.setValue(nameData);
    }

    public MutableLiveData<String> getNameData() {
        if (name == null) {
            name = new MutableLiveData<>();
        }

        return name;
    }

}
